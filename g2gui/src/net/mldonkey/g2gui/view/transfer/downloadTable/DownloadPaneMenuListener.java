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
package net.mldonkey.g2gui.view.transfer.downloadTable;

import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.model.enum.EnumExtension;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.view.helper.SashViewFrame;
import net.mldonkey.g2gui.view.helper.SashViewFrameListener;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.actions.AllFilterAction;
import net.mldonkey.g2gui.view.viewers.actions.ColumnSelectorAction;
import net.mldonkey.g2gui.view.viewers.actions.ExtensionFilterAction;
import net.mldonkey.g2gui.view.viewers.actions.FilterAction;
import net.mldonkey.g2gui.view.viewers.actions.FlipSashAction;
import net.mldonkey.g2gui.view.viewers.actions.MaximizeAction;
import net.mldonkey.g2gui.view.viewers.actions.StateFilterAction;
import net.mldonkey.g2gui.view.viewers.actions.ToggleClientsAction;
import net.mldonkey.g2gui.view.viewers.filters.GViewerFilter;
import net.mldonkey.g2gui.view.viewers.filters.StateGViewerFilter;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.events.DisposeEvent;


/**
 *
 * DownloadPaneMenuListener
 *
 * @version $Id: DownloadPaneMenuListener.java,v 1.6 2003/12/17 13:06:05 lemmy Exp $
 *
 */
public class DownloadPaneMenuListener extends SashViewFrameListener {
    private Enum[] states;

    public DownloadPaneMenuListener(SashViewFrame sashViewFrame) {
        super(sashViewFrame);

        this.states = new Enum[] {
                EnumExtension.AUDIO, EnumExtension.VIDEO, EnumExtension.ARCHIVE,
                EnumExtension.CDIMAGE, EnumExtension.PICTURE
            };

        // set defaults on startup
        if (PreferenceLoader.loadBoolean("downloadsFilterQueued")) {
            GViewerFilter aFilter = new StateGViewerFilter(gView, true);
            aFilter.add(EnumFileState.QUEUED);
            gView.addFilter(aFilter);
        }

        if (PreferenceLoader.loadBoolean("downloadsFilterPaused")) {
            GViewerFilter aFilter = new StateGViewerFilter(gView, true);
            aFilter.add(EnumFileState.PAUSED);
            gView.addFilter(aFilter);
        }
        setFilterState( this.states );
        setBestFit();
        setNetworkFilterState();
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

        // sortMenu for macOS
        createSortByColumnSubMenu(menuManager);

        // my pref column which should be autosized
        createBestFitColumnSubMenu(menuManager);

        // filter submenu			
        MenuManager filterSubMenu = new MenuManager(G2GuiResources.getString(
                    "TT_DOWNLOAD_MENU_FILTER"));

        // all filters
        filterSubMenu.add(new AllFilterAction(gView));

        // network filters
        filterSubMenu.add(new Separator());
        createNetworkFilterSubMenu(filterSubMenu);

        // state filter - exclusionary
        filterSubMenu.add(new Separator());
        filterSubMenu.add(new StateFilterAction(G2GuiResources.getString("TT_Paused"), gView,
                EnumFileState.PAUSED, true));
        filterSubMenu.add(new StateFilterAction(G2GuiResources.getString("TT_Queued"), gView,
                EnumFileState.QUEUED, true));

        filterSubMenu.add(new Separator());

        for (int i = 0; i < states.length; i++) {
            EnumExtension extension = (EnumExtension) states[ i ];
            filterSubMenu.add(new ExtensionFilterAction(extension.toString(), gView, extension));
        }

        menuManager.add(filterSubMenu);

        // toggle clients windows
        if (advancedMode) {
            menuManager.add(new Separator());
            menuManager.add(new ToggleClientsAction(gView));
        }

        // flip sash/maximize sash
        menuManager.add(new Separator());
        menuManager.add(new FlipSashAction(this.sashForm));
        menuManager.add(new MaximizeAction(this.sashForm, this.control, "TT_Uploads"));
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
     */
    public void widgetDisposed(DisposeEvent arg0) {
    	PreferenceLoader.setValue("downloadsFilterPaused", FilterAction.isFiltered(gView, EnumFileState.PAUSED));
    	PreferenceLoader.setValue("downloadsFilterQueued", FilterAction.isFiltered(gView, EnumFileState.QUEUED));
    	saveFilterState( this.states );
    	saveBestFit();
    	saveNetworkFilterState();
    }
}


/*
$Log: DownloadPaneMenuListener.java,v $
Revision 1.6  2003/12/17 13:06:05  lemmy
save all panelistener states correctly to the prefstore

Revision 1.5  2003/12/07 19:40:20  lemmy
[Bug #1156] Allow a certain column to be 100% by pref

Revision 1.4  2003/12/04 08:47:28  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.3  2003/12/03 22:19:11  lemmy
store g2gui.pref in ~/.g2gui/g2gui.pref instead of the program directory

Revision 1.2  2003/11/28 01:06:21  zet
not much- slowly expanding viewframe - will continue later

Revision 1.1  2003/11/24 01:33:27  zet
move some classes

Revision 1.26  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

Revision 1.25  2003/11/15 21:15:29  zet
Label restore action

Revision 1.24  2003/11/14 00:46:04  zet
sort by column menu item (for macOS)

Revision 1.23  2003/11/06 13:52:33  lemmy
filters back working

Revision 1.22  2003/11/03 01:50:46  zet
remove expand/collapse

Revision 1.21  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.20  2003/10/31 13:16:32  lemmy
Rename Viewer -> Page
Constructors changed

Revision 1.19  2003/10/31 10:42:47  lemmy
Renamed GViewer, GTableViewer and GTableTreeViewer to gView... to avoid mix-ups with StructuredViewer...
Removed IGViewer because our abstract class gView do the job
Use supertype/interface where possible to keep the design flexible!

Revision 1.18  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.17  2003/10/29 16:56:43  lemmy
added reasonable class hierarchy for panelisteners, viewers...

Revision 1.16  2003/10/28 12:35:12  lemmy
moved NetworkInfo.Enum -> enum.EnumNetwork;
added Enum.MaskMatcher

Revision 1.15  2003/10/28 03:51:25  zet
*** empty log message ***

Revision 1.14  2003/10/22 17:17:30  zet
common actions

Revision 1.13  2003/10/22 01:38:04  zet
*** empty log message ***

Revision 1.12  2003/10/19 17:35:04  zet
generalize columnselector

Revision 1.11  2003/10/16 23:56:59  zet
not much

Revision 1.10  2003/10/16 19:58:03  zet
icons

Revision 1.9  2003/10/15 18:25:00  zet
icons

Revision 1.8  2003/10/14 23:23:43  zet
check properties

Revision 1.7  2003/10/12 23:14:08  zet
show clients menuitem

Revision 1.6  2003/10/12 15:58:30  zet
rewrite downloads table & more..

Revision 1.5  2003/10/08 01:12:41  zet
save Paused&Queued filter

Revision 1.4  2003/09/23 15:24:24  zet
not much..

Revision 1.3  2003/09/23 15:00:02  zet
extend CMenuListener

Revision 1.2  2003/09/21 23:39:31  zet
displayTableColors preference

Revision 1.1  2003/09/20 14:39:21  zet
move transfer package

Revision 1.4  2003/09/18 14:11:01  zet
revert

Revision 1.2  2003/09/01 01:38:30  zet
gtk workaround

Revision 1.1  2003/08/30 00:44:01  zet
move tabletree menu

*/
