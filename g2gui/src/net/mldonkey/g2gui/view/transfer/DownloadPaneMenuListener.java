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
package net.mldonkey.g2gui.view.transfer;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.CustomTableTreeViewer;
import net.mldonkey.g2gui.view.viewers.IGViewer;
import net.mldonkey.g2gui.view.viewers.SashGPaneListener;
import net.mldonkey.g2gui.view.viewers.actions.AllFilterAction;
import net.mldonkey.g2gui.view.viewers.actions.ColumnSelectorAction;
import net.mldonkey.g2gui.view.viewers.actions.ExtensionFilterAction;
import net.mldonkey.g2gui.view.viewers.actions.FilterAction;
import net.mldonkey.g2gui.view.viewers.actions.FlipSashAction;
import net.mldonkey.g2gui.view.viewers.actions.MaximizeAction;
import net.mldonkey.g2gui.view.viewers.actions.StateFilterAction;
import net.mldonkey.g2gui.view.viewers.actions.ToggleClientsAction;
import net.mldonkey.g2gui.view.viewers.filters.StateGViewerFilter;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.PreferenceStore;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Control;


/**
 *
 * DownloadPaneMenuListener
 *
 * @version $Id: DownloadPaneMenuListener.java,v 1.18 2003/10/31 07:24:01 zet Exp $
 *
 */
public class DownloadPaneMenuListener extends SashGPaneListener {
    private static String[] extensionNames = {
        G2GuiResources.getString("TT_DOWNLOAD_FILTER_AUDIO"),
        G2GuiResources.getString("TT_DOWNLOAD_FILTER_VIDEO"),
        G2GuiResources.getString("TT_DOWNLOAD_FILTER_ARCHIVE"),
        G2GuiResources.getString("TT_DOWNLOAD_FILTER_CDIMAGE"),
        G2GuiResources.getString("TT_DOWNLOAD_FILTER_PICTURE")
    };
    private static String[] audioExtensions = {
        "aac", "ape", "au", "flac", "mpc", "mp2", "mp3", "mp4", "wav", "ogg", "wma"
    };
    private static String[] videoExtensions = {
        "avi", "mpg", "mpeg", "ram", "rm", "asf", "vob", "divx", "vivo", "ogm", "mov", "wmv"
    };
    private static String[] archiveExtensions = { "gz", "zip", "ace", "rar", "tar", "tgz", "bz2" };
    private static String[] cdImageExtensions = {
        "ccd", "sub", "cue", "bin", "iso", "nrg", "img", "bwa", "bwi", "bws", "bwt", "mds", "mdf"
    };
    private static String[] pictureExtensions = { "jpg", "jpeg", "bmp", "gif", "tif", "tiff", "png" };
    private static String[][] extensions = {
        audioExtensions, videoExtensions, archiveExtensions, cdImageExtensions, pictureExtensions
    };

    public DownloadPaneMenuListener(IGViewer gViewer, CoreCommunication core, SashForm aSashForm,
        Control aControl) {
        super(gViewer, core, aSashForm, aControl);

        // set defaults on startup
        if (PreferenceLoader.loadBoolean("downloadsFilterQueued")) {
            StateGViewerFilter aFilter = new StateGViewerFilter(true);
            aFilter.add(EnumFileState.QUEUED);
            gViewer.addFilter(aFilter);
        }

        if (PreferenceLoader.loadBoolean("downloadsFilterPaused")) {
            StateGViewerFilter aFilter = new StateGViewerFilter(true);
            aFilter.add(EnumFileState.PAUSED);
            gViewer.addFilter(aFilter);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
     */
    public void menuAboutToShow(IMenuManager menuManager) {
        boolean advancedMode = PreferenceLoader.loadBoolean("advancedMode");

        // columnSelector
        if (advancedMode) {
            menuManager.add(new ColumnSelectorAction(gViewer));
        }

        // filter submenu			
        MenuManager filterSubMenu = new MenuManager(G2GuiResources.getString(
                    "TT_DOWNLOAD_MENU_FILTER"));

        // all filters
        filterSubMenu.add(new AllFilterAction(gViewer));

        // network filters
        filterSubMenu.add(new Separator());
        createNetworkFilterSubMenu(filterSubMenu);

        // state filter - exclusionary
        filterSubMenu.add(new Separator());
        filterSubMenu.add(new StateFilterAction(G2GuiResources.getString("TT_Paused"), gViewer,
                EnumFileState.PAUSED, true));
        filterSubMenu.add(new StateFilterAction(G2GuiResources.getString("TT_Queued"), gViewer,
                EnumFileState.QUEUED, true));

        filterSubMenu.add(new Separator());

        for (int i = 0; i < extensions.length; i++)
            filterSubMenu.add(new ExtensionFilterAction(extensionNames[ i ], gViewer,
                    extensions[ i ]));

        menuManager.add(filterSubMenu);

        // expand action
        menuManager.add(new Separator());
        menuManager.add(new ExpandCollapseAction(true));
        menuManager.add(new ExpandCollapseAction(false));

        // toggle clients windows
        if (advancedMode) {
            menuManager.add(new Separator());
            menuManager.add(new ToggleClientsAction(gViewer));
        }

        // flip sash/maximize sash
        menuManager.add(new Separator());
        menuManager.add(new FlipSashAction(this.sashForm));
        menuManager.add(new MaximizeAction(this.sashForm, this.control));
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
     */
    public void widgetDisposed(DisposeEvent arg0) {
        PreferenceStore p = PreferenceLoader.getPreferenceStore();
        p.setValue("downloadsFilterPaused", FilterAction.isFiltered(gViewer, EnumFileState.PAUSED));
        p.setValue("downloadsFilterQueued", FilterAction.isFiltered(gViewer, EnumFileState.QUEUED));
    }

    /**
     * ExpandCollapseAction
     */
    private class ExpandCollapseAction extends Action {
        private boolean expand;

        public ExpandCollapseAction(boolean expand) {
            super();

            if (expand) {
                setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_EXPANDALL"));
                setImageDescriptor(G2GuiResources.getImageDescriptor("expandAll"));
            } else {
                setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_COLLAPSEALL"));
                setImageDescriptor(G2GuiResources.getImageDescriptor("collapseAll"));
            }

            this.expand = expand;
        }

        public void run() {
            if (expand) {
                ((CustomTableTreeViewer) gViewer.getViewer()).expandAll();
            } else {
                ((CustomTableTreeViewer) gViewer.getViewer()).collapseAll();
            }
        }
    }
}


/*
$Log: DownloadPaneMenuListener.java,v $
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

Revision 1.17  2003/10/29 16:56:43  lemmster
added reasonable class hierarchy for panelisteners, viewers...

Revision 1.16  2003/10/28 12:35:12  lemmster
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
