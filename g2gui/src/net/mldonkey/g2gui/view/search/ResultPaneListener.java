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
import net.mldonkey.g2gui.view.viewers.actions.AllFilterAction;
import net.mldonkey.g2gui.view.viewers.actions.CTabFolderColumnSelectorAction;
import net.mldonkey.g2gui.view.viewers.table.GTableView;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;


/**
 * SearchPaneListener
 *
 * @version $Id: ResultPaneListener.java,v 1.5 2003/10/31 16:02:57 zet Exp $
 *
 */
public class ResultPaneListener extends GPaneListener {
    private CTabFolder cTabFolder;

    /**
     * @param gViewer
     * @param core
     */
    public ResultPaneListener(CTabFolder cTabFolder, CoreCommunication core) {
        super(null, core);
        this.cTabFolder = cTabFolder;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
     */
    public void menuAboutToShow(IMenuManager menuManager) {
        if (cTabFolder != null) {
            // check if a table exists in any tab
            if (PreferenceLoader.loadBoolean("advanced")) {
                CTabItem[] cTabItems = cTabFolder.getItems();

                for (int i = 0; i < cTabItems.length; i++) {
                    if (cTabItems[ i ].getData("gTableViewer") != null) {
                        menuManager.add(new CTabFolderColumnSelectorAction(cTabFolder));

                        break;
                    }
                }
            }

            // filters available if currentTab has a table
            if (cTabFolder.getSelection() != null && cTabFolder.getSelection().getData("gTableViewer") != null) {
                gView = (GTableView) cTabFolder.getSelection().getData("gTableViewer");

                // filter submenu			
                MenuManager filterSubMenu = new MenuManager(G2GuiResources.getString(
                            "TT_DOWNLOAD_MENU_FILTER"));

                // all filters
                filterSubMenu.add(new AllFilterAction(gView));
                filterSubMenu.add(new Separator());

                // network filters
                createNetworkFilterSubMenu(filterSubMenu);

                menuManager.add(filterSubMenu);
            }
        }
    }
}


/*
$Log: ResultPaneListener.java,v $
Revision 1.5  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.4  2003/10/31 13:16:33  lemmster
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