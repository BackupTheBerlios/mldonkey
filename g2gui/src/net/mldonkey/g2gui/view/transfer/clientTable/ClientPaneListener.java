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

import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.PaneGuiTab;
import net.mldonkey.g2gui.view.helper.ViewFrame;
import net.mldonkey.g2gui.view.helper.ViewFrameListener;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.actions.AllFilterAction;
import net.mldonkey.g2gui.view.viewers.actions.ColumnSelectorAction;
import net.mldonkey.g2gui.view.viewers.actions.FlipSashAction;
import net.mldonkey.g2gui.view.viewers.actions.MaximizeAction;
import net.mldonkey.g2gui.view.viewers.actions.StateFilterAction;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;


/**
 * ClientPaneListener
 *
 * @version $Id: ClientPaneListener.java,v 1.8 2003/11/22 02:24:30 zet Exp $
 *
 */
public class ClientPaneListener extends ViewFrameListener {
    private Enum[] states;

    /**
     * @param gViewer
     * @param core
     * @param aSashForm
     * @param aControl
     */
    public ClientPaneListener(ViewFrame viewFrame, PaneGuiTab paneGuiTab) {
        super(viewFrame, paneGuiTab);
        this.states = new Enum[] {
                EnumState.BLACK_LISTED, EnumState.CONNECTED, EnumState.CONNECTED_AND_QUEUED,
                EnumState.CONNECTED_DOWNLOADING, EnumState.CONNECTED_INITIATING,
                EnumState.CONNECTING, EnumState.NEW_HOST, EnumState.NOT_CONNECTED,
                EnumState.NOT_CONNECTED_WAS_QUEUED
            };
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
     */
    public void menuAboutToShow(IMenuManager menuManager) {

        boolean advancedMode = PreferenceLoader.loadBoolean("advancedMode");

        // columnSelector
        if (advancedMode) {
            menuManager.add(new ColumnSelectorAction(gView));
        }

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

        // state filter
        filterSubMenu.add(new Separator());

        for (int i = 0; i < states.length; i++)
            filterSubMenu.add(new StateFilterAction(states[ i ].toString(), gView, states[ i ]));

        menuManager.add(filterSubMenu);

        // flip sash/maximize sash
        menuManager.add(new Separator());
        menuManager.add(new FlipSashAction(this.sashForm));
        menuManager.add(new MaximizeAction(this.sashForm, this.control, "TT_Downloads"));
    }
}


/*
$Log: ClientPaneListener.java,v $
Revision 1.8  2003/11/22 02:24:30  zet
widgetfactory & save sash postions/states between sessions

Revision 1.7  2003/11/15 21:15:29  zet
Label restore action

Revision 1.6  2003/11/14 00:46:04  zet
sort by column menu item (for macOS)

Revision 1.5  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

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
