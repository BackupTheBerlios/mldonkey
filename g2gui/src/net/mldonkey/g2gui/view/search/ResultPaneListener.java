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
package net.mldonkey.g2gui.view.search;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.GPaneListener;
import net.mldonkey.g2gui.view.viewers.GViewer;
import net.mldonkey.g2gui.view.viewers.actions.AllFilterAction;
import net.mldonkey.g2gui.view.viewers.actions.ColumnSelectorAction;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.custom.CTabFolder;

/**
 * SearchPaneListener
 *
 * @version $Id: ResultPaneListener.java,v 1.1 2003/10/29 16:56:21 lemmster Exp $
 *
 */
public class ResultPaneListener extends GPaneListener {
    private CTabFolder cTabFolder;

    /**
     * @param gViewer
     * @param core
     */
    public ResultPaneListener( CTabFolder cTabFolder, CoreCommunication core ) {
        super( null, core );
        this.cTabFolder = cTabFolder;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
     */
    public void menuAboutToShow( IMenuManager menuManager ) {
        // check if we already got results
        if ( ( cTabFolder != null )
        	&& ( cTabFolder.getSelection() != null )
        	&& ( cTabFolder.getSelection().getData( "gTableViewer" ) != null ) )

		// get each time the current GViewer
		this.gViewer = ( GViewer ) cTabFolder.getSelection().getData( "gTableViewer" );

		//TODO ColumnSelector works for all tables instead of only the current selection
		if ( PreferenceLoader.loadBoolean( "advanced" ) )
	        menuManager.add( new ColumnSelectorAction( gViewer ) );

		// filter submenu			
		MenuManager filterSubMenu = new MenuManager( G2GuiResources.getString( "TT_DOWNLOAD_MENU_FILTER" ) );

		// all filters
		filterSubMenu.add( new AllFilterAction( gViewer ) );

		filterSubMenu.add( new Separator() );

		// network filters
		createNetworkFilterSubMenu( filterSubMenu );

		menuManager.add( filterSubMenu );
    }
}

/*
$Log: ResultPaneListener.java,v $
Revision 1.1  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

*/
