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
import net.mldonkey.g2gui.view.PaneGuiTab;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.GPaneListener;
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
 * @version $Id: ServerPaneListener.java,v 1.4 2003/10/31 13:16:32 lemmster Exp $ 
 *
 */
public class ServerPaneListener extends GPaneListener {
	private Enum[] states;
	/**
	 * @param gViewer
	 * @param core
	 */
	public ServerPaneListener(PaneGuiTab aPaneGuiTab, CoreCommunication core) {
		super(aPaneGuiTab, core);
	}
	
	public void initialize() {
		super.menuAboutToShow();
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
			gPage.addFilter( aFilter );
		// everything is default, so pay attention to displayAllServers
		else
			if ( PreferenceLoader.loadBoolean( "displayAllServers" ) ) {
				StateGViewerFilter filter = new StateGViewerFilter();
				filter.add( EnumState.CONNECTED );
				gPage.addFilter( filter );
			}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */
	public void menuAboutToShow(IMenuManager menuManager) {
		super.menuAboutToShow();

		// columnSelector
		if ( PreferenceLoader.loadBoolean("advancedMode") )
			menuManager.add( new ColumnSelectorAction(gPage) );

		// filter submenu			
		MenuManager filterSubMenu = new MenuManager( G2GuiResources.getString( "TT_DOWNLOAD_MENU_FILTER" ) );

		// all filters
		filterSubMenu.add( new AllFilterAction( gPage ) );

		filterSubMenu.add( new Separator() );

		// network filters
		createNetworkFilterSubMenu( filterSubMenu );

		// state filter
		filterSubMenu.add( new Separator() );
		for ( int i = 0; i < states.length; i++ )
			filterSubMenu.add( new StateFilterAction( states[ i ].toString(), gPage,  states[ i ] ) );

		menuManager.add( filterSubMenu );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
	 */
	public void widgetDisposed(DisposeEvent arg0) {
		PreferenceStore p = PreferenceLoader.getPreferenceStore();
		for ( int i = 0; i < this.states.length; i++ )
			p.setValue( states[ i ].toString(), FilterAction.isFiltered( gPage, states[ i ] ) ); 
	}
}

/*
$Log: ServerPaneListener.java,v $
Revision 1.4  2003/10/31 13:16:32  lemmster
Rename Viewer -> Page
Constructors changed

Revision 1.3  2003/10/31 10:42:47  lemmster
Renamed GViewer, GTableViewer and GTableTreeViewer to GPage... to avoid mix-ups with StructuredViewer...
Removed IGViewer because our abstract class GPage do the job
Use supertype/interface where possible to keep the design flexible!

Revision 1.2  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.1  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

*/