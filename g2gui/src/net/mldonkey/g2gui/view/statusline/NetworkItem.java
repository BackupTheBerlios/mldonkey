/*
 * Copyright 2003
 * G2GUI Team
 * 
 * 
 * This file is part of G2GUI.
 *
 * G2GUI is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2GUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2GUI; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.view.statusline;

import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.view.GuiTab;
import net.mldonkey.g2gui.view.ServerTab;
import net.mldonkey.g2gui.view.StatusLine;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;


/**
 * NetworkItem
 *
 *
 * @version $Id: NetworkItem.java,v 1.21 2003/08/23 15:21:37 zet Exp $ 
 *
 */
public class NetworkItem implements Observer {
	private CoreCommunication core;
	private StatusLine statusline;
	private Composite composite;
	private GridLayout gridLayout;
	private CLabel cLabel;
	private GuiTab serverTab;


	/**
	 * @param statusline the Statusline in which this item should appear
	 * @param mldonkey The core to register on
	 */
	public NetworkItem( StatusLine statusline, CoreCommunication mldonkey ) {
		this.composite = statusline.getStatusline();
		this.statusline = statusline;
		this.core = mldonkey;
		
		/* get the servertab */
		GuiTab[] tabs = statusline.getMainTab().getTabs();
		for ( int i = 0; i < tabs.length; i++ ) {
			GuiTab tab = tabs[ i ];
			if ( tab instanceof ServerTab ) {
				this.serverTab = tab;
			}
		}
		this.createContent();

		this.core.getNetworkInfoMap().addObserver( this );
	}

	/**
	 * create the content
	 */
	private void createContent() {
		NetworkInfo[] networks = core.getNetworkInfoMap().getNetworks();

		composite = new Composite( composite, SWT.BORDER );
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = networks.length;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		composite.setLayout( gridLayout );
		
		/* sets the enabled/disabled image for each known network */
		for ( int i = 0; i < networks.length; i++ ) {
			NetworkInfo network = networks[ i ];
			cLabel = new CLabel( composite, SWT.NONE );
			cLabel.setData( network );
			NetworkItemMenuListener manager =
				new NetworkItemMenuListener( network, serverTab, statusline );
			MenuManager popupMenu = new MenuManager( "" );
			popupMenu.setRemoveAllWhenShown( true );
			popupMenu.addMenuListener( manager );
			cLabel.setMenu( popupMenu.createContextMenu( cLabel ) );
			
			/* set the network name and status as tooltip */
			if ( network.isEnabled() && ( network.hasServers() || network.hasSupernodes() ) )
				cLabel.setToolTipText( network.getNetworkName() + " connected to: "
									   + network.getConnectedServers() + " server" );
			else
				cLabel.setToolTipText( network.getNetworkName() + " " + new Boolean( network.isEnabled() ).toString() );
	
			/* on dispose() deregister on the model */			
			cLabel.addDisposeListener( new DisposeListener () {
				public void widgetDisposed(DisposeEvent e) {
					core.getNetworkInfoMap().deleteObserver( NetworkItem.this );
				}
			} );

			/* now create the image */
			cLabel.setImage( network.getImage() );
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update( Observable o, Object arg ) {
		// ClassCastException without this
		if (!(arg instanceof NetworkInfo)) return;
		
		final NetworkInfo network = ( NetworkInfo ) arg;
		composite.getDisplay().asyncExec( new Runnable () {
			public void run() {
				/* check for widget disposed */
				if ( composite.isDisposed() ) return;

				/* get all labels */
				Control[] controls = composite.getChildren();

				/* find the corresponding label */	
				cLabel = getLabelByNetwork( controls, network );

				/* set the tooltip text */
				if ( network.isEnabled() && ( network.hasServers() || network.hasSupernodes() ) )
				cLabel.setToolTipText( network.getNetworkName() + " connected to: "
									   + network.getConnectedServers() + " server" );
				else
					cLabel.setToolTipText( network.getNetworkName() + " " + new Boolean( network.isEnabled() ).toString() );

				/* set the image */
				cLabel.setImage( network.getImage() );
			}
		} );	
	}
	
	/**
	 * find the corresponding cLabel to the networkinfo
	 * @param controls The low level array of clabels
	 * @param network The networkinfo
	 * @return the corresponding clabel to the networkinfo
	 */
	private CLabel getLabelByNetwork( Control[] controls, NetworkInfo network ) {
		for ( int i = 0; i < controls.length; i++ ) {
			cLabel = ( CLabel ) controls[ i ];
			if ( cLabel.getData() == network ) {
				return cLabel;
			}
		}
		return null;
	}
}

/*
$Log: NetworkItem.java,v $
Revision 1.21  2003/08/23 15:21:37  zet
remove @author

Revision 1.20  2003/08/21 13:13:10  lemmster
cleanup in networkitem

Revision 1.19  2003/08/20 22:16:33  lemmster
badconnect is display too. added some icons

Revision 1.18  2003/08/19 14:34:36  lemmster
dont dispose() the image if its from the imageregistry

Revision 1.17  2003/08/19 12:14:16  lemmster
first try of simple/advanced mode

Revision 1.16  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.15  2003/08/17 23:13:42  zet
centralize resources, move images

Revision 1.14  2003/08/11 11:26:20  lemmstercvs01
added some images

Revision 1.13  2003/08/10 12:59:01  lemmstercvs01
"manage servers" in NetworkItem implemented

Revision 1.12  2003/08/04 19:22:08  zet
trial tabletreeviewer

Revision 1.11  2003/08/03 17:38:54  zet
check instanceof

Revision 1.10  2003/08/02 19:47:54  lemmstercvs01
finally working

Revision 1.9  2003/08/02 09:55:16  lemmstercvs01
observers changed

Revision 1.8  2003/08/01 17:21:19  lemmstercvs01
reworked observer/observable design, added multiversion support

Revision 1.7  2003/07/31 14:11:06  lemmstercvs01
reworked

Revision 1.6  2003/07/17 14:58:56  lemmstercvs01
refactored

Revision 1.5  2003/07/06 13:19:39  dek
small change

Revision 1.4  2003/07/04 17:48:57  dek
refactor

Revision 1.3  2003/06/28 09:50:00  lemmstercvs01
hasNext() optimized

Revision 1.2  2003/06/27 13:37:28  dek
tooltips added

Revision 1.1  2003/06/27 13:21:12  dek
added connected Networks

*/