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
package net.mldonkey.g2gui.view.transfer.uploadersTable;

import net.mldonkey.g2gui.view.helper.SashViewFrame;
import net.mldonkey.g2gui.view.helper.SashViewFrameListener;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.actions.AllFilterAction;
import net.mldonkey.g2gui.view.viewers.actions.ColumnSelectorAction;
import net.mldonkey.g2gui.view.viewers.actions.BestFitColumnAction;
import net.mldonkey.g2gui.view.viewers.actions.FlipSashAction;
import net.mldonkey.g2gui.view.viewers.actions.MaximizeAction;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.events.DisposeEvent;


/**
 * UploadPaneListener
 *
 * @version $Id: UploadersPaneListener.java,v 1.4 2003/12/07 19:40:20 lemmy Exp $
 *
 */
public class UploadersPaneListener extends SashViewFrameListener {
    /**
     * @param gViewer
     * @param core
     * @param aSashForm
     * @param aControl
     */
    public UploadersPaneListener(SashViewFrame sashViewFrame) {
        super(sashViewFrame);
        
        int i = PreferenceLoader.getInt( "UploadersPaneListenerBestFit" );
        if ( i != -1 )
        	new BestFitColumnAction( gView, i ).run();
    }

    public void menuAboutToShow(IMenuManager menuManager) {
        boolean advancedMode = PreferenceLoader.loadBoolean("advancedMode");

        // columnSelector
        if (advancedMode)
            menuManager.add(new ColumnSelectorAction(gView));

        // for macOS
        createSortByColumnSubMenu(menuManager);

        // my pref column which should be autosized
        createBestFitColumnSubMenu(menuManager);
        
        // filter submenu			
        MenuManager filterSubMenu = new MenuManager(G2GuiResources.getString(
                    "TT_DOWNLOAD_MENU_FILTER"));

        // all filters
        filterSubMenu.add(new AllFilterAction(gView));
        filterSubMenu.add(new Separator());

        // network filters
        createNetworkFilterSubMenu(filterSubMenu);

        menuManager.add(filterSubMenu);

        // flip sash/maximize sash
        menuManager.add(new Separator());
        menuManager.add(new FlipSashAction(this.sashForm));
        menuManager.add(new MaximizeAction(this.sashForm, this.control, "TT_Uploads"));
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
     */
    public void widgetDisposed(DisposeEvent arg0) {
    	if ( gView != null && gView.getColumnControlListenerIsOn() != -1 )
    		PreferenceLoader.setValue( "UploadersPaneListenerBestFit", gView.getColumnControlListenerIsOn() );
    }
}


/*
$Log: UploadersPaneListener.java,v $
Revision 1.4  2003/12/07 19:40:20  lemmy
[Bug #1156] Allow a certain column to be 100% by pref

Revision 1.3  2003/11/28 01:06:21  zet
not much- slowly expanding viewframe - will continue later

Revision 1.2  2003/11/26 16:02:05  zet
resString

Revision 1.1  2003/11/26 07:43:15  zet
quick attempt at an uploaders table w/proto 19 - still in progress...


*/
