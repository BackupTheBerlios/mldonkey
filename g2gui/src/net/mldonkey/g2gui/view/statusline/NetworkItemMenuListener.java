/*
 * Copyright 2003
 * G2Gui Team
 * 
 * 
 * This file is part of G2Gui.
 *
 * G2Gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2Gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2Gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.view.statusline;

import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.view.GuiTab;
import net.mldonkey.g2gui.view.ServerTab;
import net.mldonkey.g2gui.view.StatusLine;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;

/**
 * NetworkItemMenuListener
 *
 * @author $Author: lemmster $
 * @version $Id: NetworkItemMenuListener.java,v 1.1 2003/08/21 13:13:10 lemmster Exp $ 
 *
 */
public class NetworkItemMenuListener implements IMenuListener {
	/**
	 * The <code>StatusLine</code>
	 */
	private StatusLine statusline;
	/**
	 * The <code>NetworkInfo</code> to this <code>IMenuListener</code>
	 */
	private NetworkInfo network;
	/**
	 * The <code>GuiTab</code>
	 */
	private GuiTab serverTab;
	/**
	 * Creates a new NetworkItemMenuListener
	 * @param network The <code>NetworkInfo</code> this obj belong to
	 * @param serverTab The <code>GuiTab</code>
	 */
	public NetworkItemMenuListener( NetworkInfo network, GuiTab serverTab, StatusLine statusline ) {
		super();
		this.network = network;
		this.serverTab = serverTab;
		this.statusline = statusline;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuListener#
	 * menuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */
	public void menuAboutToShow( IMenuManager manager ) {
		boolean advancedMode = PreferenceLoader.loadBoolean( "advancedMode" );
		if ( network.isEnabled() ) {
			if ( network.hasServers() && advancedMode )
					manager.add( new ManageAction() );

			if ( network.hasServers() || network.hasSupernodes() )
				manager.add( new ConnectMoreAction() );

			manager.add( new Separator() );
		}
		
		if ( !network.isVirtual() ) {
			if ( network.isEnabled() )
				manager.add( new DisableAction() );
			else
				manager.add( new EnableAction() );	
		}
	}
	
	private class EnableAction extends Action {
		public EnableAction() {
			super();
			setText( "Enable" );
		}
		public void run() {
			network.setEnabled();
		}
	}

	private class DisableAction extends Action {
		public DisableAction() {
			super();
			setText( "Disable" );
		}
		public void run() {
			network.setEnabled();
		}
	}

	private class ManageAction extends Action {
		public ManageAction() {
			super();
			setText( "manage server" );
		}
		public void run() {
			statusline.getMainTab().setActive( serverTab );
			( ( ServerTab ) serverTab ).setFilter( network.getNetworkType() );
		}
	}

	private class ConnectMoreAction extends Action {
		public ConnectMoreAction() {
			super();
			setText( "connect more" );
		}
		public void run() {
			network.getCore().getServerInfoIntMap().connectMore();
		}
	}
}

/*
$Log: NetworkItemMenuListener.java,v $
Revision 1.1  2003/08/21 13:13:10  lemmster
cleanup in networkitem

*/