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

import net.mldonkey.g2gui.view.helper.ViewFrame;
import net.mldonkey.g2gui.view.helper.ViewFrameListener;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.actions.AllFilterAction;
import net.mldonkey.g2gui.view.viewers.actions.ColumnSelectorAction;
import net.mldonkey.g2gui.view.viewers.actions.FlipSashAction;
import net.mldonkey.g2gui.view.viewers.actions.MaximizeAction;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;


/**
 * UploadPaneListener
 *
 * @version $Id: UploadersPaneListener.java,v 1.2 2003/11/26 16:02:05 zet Exp $
 *
 */
public class UploadersPaneListener extends ViewFrameListener {
    /**
     * @param gViewer
     * @param core
     * @param aSashForm
     * @param aControl
     */
    public UploadersPaneListener(ViewFrame viewFrame) {
        super(viewFrame);
    }

    public void menuAboutToShow(IMenuManager menuManager) {
        boolean advancedMode = PreferenceLoader.loadBoolean("advancedMode");

        // columnSelector
        if (advancedMode)
            menuManager.add(new ColumnSelectorAction(gView));

        // for macOS
        createSortByColumnSubMenu(menuManager);

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
}


/*
$Log: UploadersPaneListener.java,v $
Revision 1.2  2003/11/26 16:02:05  zet
resString

Revision 1.1  2003/11/26 07:43:15  zet
quick attempt at an uploaders table w/proto 19 - still in progress...


*/