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
package net.mldonkey.g2gui.view.server;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.GPaneListener;
import net.mldonkey.g2gui.view.viewers.GViewer;
import net.mldonkey.g2gui.view.viewers.actions.AllFilterAction;
import net.mldonkey.g2gui.view.viewers.actions.ColumnSelectorAction;
import net.mldonkey.g2gui.view.viewers.actions.FilterAction;
import net.mldonkey.g2gui.view.viewers.actions.StateFilterAction;
import net.mldonkey.g2gui.view.viewers.filters.GViewerFilter;
import net.mldonkey.g2gui.view.viewers.filters.StateGViewerFilter;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.events.DisposeEvent;

/**
 * ServerPaneListener
 *
 * @version $Id: ServerPaneListener.java,v 1.1 2003/10/29 16:56:21 lemmster Exp $ 
 *
 */
public class ServerPaneListener extends GPaneListener {
	private Enum[] states;
	/**
	 * @param gViewer
	 * @param core
	 */
	public ServerPaneListener(GViewer gViewer, CoreCommunication core) {
		super(gViewer, core);
		this.states = new Enum[] { EnumState.BLACK_LISTED, EnumState.CONNECTED, 
									EnumState.CONNECTED_INITIATING, EnumState.CONNECTING,
									EnumState.NOT_CONNECTED };

		// set the last state from preferences
		boolean temp = false;
		GViewerFilter aFilter = new StateGViewerFilter();
		for ( int i = 0; i < this.states.length; i++ ) {
			if ( PreferenceLoader.loadBoolean( states[ i ].toString() ) ) {
				aFilter.add( states[ i ] );
				temp = true;
			}
		}
		// just add the filter if we really added enums to it
		if ( temp )
			gViewer.addFilter( aFilter );
		// everything is default, so pay attention to displayAllServers
		else
			if ( PreferenceLoader.loadBoolean( "displayAllServers" ) ) {
				StateGViewerFilter filter = new StateGViewerFilter();
				filter.add( EnumState.CONNECTED );
				gViewer.addFilter( filter );
			}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */
	public void menuAboutToShow(IMenuManager menuManager) {
		// columnSelector
		if ( PreferenceLoader.loadBoolean("advancedMode") )
			menuManager.add( new ColumnSelectorAction(gViewer) );

		// filter submenu			
		MenuManager filterSubMenu = new MenuManager( G2GuiResources.getString( "TT_DOWNLOAD_MENU_FILTER" ) );

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
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
	 */
	public void widgetDisposed(DisposeEvent arg0) {
		PreferenceStore p = PreferenceLoader.getPreferenceStore();
		for ( int i = 0; i < this.states.length; i++ )
			p.setValue( states[ i ].toString(), FilterAction.isFiltered( gViewer, states[ i ] ) ); 
	}
}

/*
$Log: ServerPaneListener.java,v $
Revision 1.1  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

*/