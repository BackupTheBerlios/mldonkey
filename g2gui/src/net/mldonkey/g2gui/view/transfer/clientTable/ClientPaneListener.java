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
package net.mldonkey.g2gui.view.transfer.clientTable;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.viewers.GViewer;
import net.mldonkey.g2gui.view.viewers.SashGPaneListener;
import net.mldonkey.g2gui.view.viewers.actions.ColumnSelectorAction;
import net.mldonkey.g2gui.view.viewers.actions.FlipSashAction;
import net.mldonkey.g2gui.view.viewers.actions.MaximizeAction;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Control;

/**
 * ClientPaneListener
 *
 * @version $Id: ClientPaneListener.java,v 1.1 2003/10/29 16:56:21 lemmster Exp $ 
 *
 */
public class ClientPaneListener extends SashGPaneListener {
	private Enum[] states;
	/**
	 * @param gViewer
	 * @param core
	 * @param aSashForm
	 * @param aControl
	 */
	public ClientPaneListener( GViewer gViewer,	CoreCommunication core, SashForm aSashForm, Control aControl ) {
		super( gViewer, core, aSashForm, aControl );
		this.states = new Enum[] { EnumState.BLACK_LISTED, EnumState.CONNECTED, 
				EnumState.CONNECTED_AND_QUEUED, EnumState.CONNECTED_DOWNLOADING,
				EnumState.CONNECTED_INITIATING, EnumState.CONNECTING,
				EnumState.NEW_HOST, EnumState.NOT_CONNECTED,
				EnumState.NOT_CONNECTED_WAS_QUEUED };
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */
	public void menuAboutToShow( IMenuManager menuManager ) {
		boolean advancedMode = PreferenceLoader.loadBoolean("advancedMode");
		// columnSelector
		if ( advancedMode ) {
			menuManager.add( new ColumnSelectorAction(gViewer) );
		}

//TODO fix filters (for some reason they arent working on clienttableviewer)
		// filter submenu			
/*		MenuManager filterSubMenu = new MenuManager( G2GuiResources.getString( "TT_DOWNLOAD_MENU_FILTER" ) );

		// all filters
		filterSubMenu.add( new AllFilterAction( gViewer ) );
		filterSubMenu.add( new Separator() );

		// network filters
		createNetworkFilterSubMenu( filterSubMenu );

		// state filter
		filterSubMenu.add( new Separator() );
		for ( int i = 0; i < states.length; i++ )
			filterSubMenu.add( new StateFilterAction( states[ i ].toString(), gViewer,  states[ i ] ) );

		menuManager.add( filterSubMenu );
*/
		// flip sash/maximize sash
		menuManager.add( new Separator() );
		menuManager.add( new FlipSashAction( this.sashForm ) );
		menuManager.add( new MaximizeAction( this.sashForm, this.control ) );
	}

}

/*
$Log: ClientPaneListener.java,v $
Revision 1.1  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

*/