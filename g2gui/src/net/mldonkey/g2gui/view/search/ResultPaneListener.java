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

import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.model.enum.EnumRating;
import net.mldonkey.g2gui.view.helper.CTabFolderViewFrame;
import net.mldonkey.g2gui.view.helper.CTabFolderViewFrameListener;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.GView;
import net.mldonkey.g2gui.view.viewers.actions.AllFilterAction;
import net.mldonkey.g2gui.view.viewers.actions.ColumnSelectorAction;
import net.mldonkey.g2gui.view.viewers.actions.StateFilterAction;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.custom.CTabItem;


/**
 * ResultPaneListener
 *
 * @version $Id: ResultPaneListener.java,v 1.12 2003/12/07 19:40:19 lemmy Exp $
 *
 */
public class ResultPaneListener extends CTabFolderViewFrameListener {
	private Enum[] states;

    /**
     * @param gViewer
     * @param core
     */
    public ResultPaneListener(CTabFolderViewFrame cTabFolderViewFrame) {
        super(cTabFolderViewFrame);
        this.states = new Enum[] { EnumRating.EXCELLENT, EnumRating.VERYHIGH, EnumRating.HIGH, EnumRating.NORMAL, EnumRating.LOW };
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
     */
    public void menuAboutToShow(IMenuManager menuManager) {
        if (cTabFolder != null) {
            // check if a table exists in any tab
            if (PreferenceLoader.loadBoolean("advancedMode")) {
                CTabItem[] cTabItems = cTabFolder.getItems();

                for (int i = 0; i < cTabItems.length; i++) {
                    if (cTabItems[ i ].getData("gView") != null) {
                        menuManager.add(new ColumnSelectorAction(cTabFolder));

                        break;
                    }
                }
            }

            // filters available if currentTab has a table
            if (cTabFolder.getSelection() != null && cTabFolder.getSelection().getData("gView") != null) {
                gView = (GView) cTabFolder.getSelection().getData("gView");
                
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
                
                // state (avail) filter
                filterSubMenu.add(new Separator());
                for ( int i = 0; i < states.length; i++ )
					filterSubMenu.add( new StateFilterAction( states[ i ].toString(), gView,  states[ i ] ) );

                menuManager.add(filterSubMenu);
            }
        }
    }
}


/*
$Log: ResultPaneListener.java,v $
Revision 1.12  2003/12/07 19:40:19  lemmy
[Bug #1156] Allow a certain column to be 100% by pref

Revision 1.11  2003/12/04 08:47:29  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.10  2003/11/29 17:02:27  zet
more viewframes.. will continue later.

Revision 1.9  2003/11/22 02:16:36  zet
advancedMode typo

Revision 1.8  2003/11/14 00:46:04  zet
sort by column menu item (for macOS)

Revision 1.7  2003/11/10 08:54:47  lemmy
rating filter in searchresult

Revision 1.6  2003/11/08 18:25:54  zet
use GView instead of GTableViewer

Revision 1.5  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.4  2003/10/31 13:16:33  lemmy
Rename Viewer -> Page
Constructors changed

Revision 1.3  2003/10/31 10:42:47  lemmy
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

Revision 1.1  2003/10/29 16:56:21  lemmy
added reasonable class hierarchy for panelisteners, viewers...

*/
