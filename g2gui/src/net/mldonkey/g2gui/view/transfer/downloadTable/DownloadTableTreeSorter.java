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
package net.mldonkey.g2gui.view.transfer.downloadTable;

import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.view.G2Gui;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.transfer.TreeClientInfo;
import net.mldonkey.g2gui.view.viewers.GSorter;
import net.mldonkey.g2gui.view.viewers.GView;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.Viewer;


/**
 * ResultTableSorter
 *
 * @version $Id: DownloadTableTreeSorter.java,v 1.15 2004/03/29 14:51:44 dek Exp $
 *
 */
public class DownloadTableTreeSorter extends GSorter {
    private boolean maintainSortOrder = false;
    private ITableLabelProvider labelProvider;

    /**
     * Creates a new viewer sorter
     */
    public DownloadTableTreeSorter(GView gViewer) {
        super(gViewer);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ViewerSorter#category(java.lang.Object)
     */
    public int category(Object element) {
        if (element instanceof FileInfo)
            return 1;
        else if (element instanceof TreeClientInfo)
            return 2;

        return 3;
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.GSorter#sortOrder(int)
     */
    public boolean sortOrder(int columnIndex) {
        switch (cViewer.getColumnIDs()[ columnIndex ]) {
        case DownloadTableTreeView.ID:
        case DownloadTableTreeView.NAME:
        case DownloadTableTreeView.NETWORK:
        case DownloadTableTreeView.LAST:
        case DownloadTableTreeView.ETA:
            return true;

        default:
            return false;
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ViewerSorter#isSorterProperty(java.lang.Object, java.lang.String)
     */
    public boolean isSorterProperty(Object element, String property) {
        if (element instanceof FileInfo && maintainSortOrder) {
            switch (cViewer.getColumnIDs()[ columnIndex ]) {
            case DownloadTableTreeView.DOWNLOADED:
                return property.equals(FileInfo.CHANGED_DOWNLOADED);

            case DownloadTableTreeView.PERCENT:
                return property.equals(FileInfo.CHANGED_PERCENT);

            case DownloadTableTreeView.AVAIL:
                return property.equals(FileInfo.CHANGED_AVAIL);

            case DownloadTableTreeView.RATE:
                return property.equals(FileInfo.CHANGED_RATE);

            case DownloadTableTreeView.ETA:
                return property.equals(FileInfo.CHANGED_ETA);

            case DownloadTableTreeView.LAST:
                return property.equals(FileInfo.CHANGED_LAST);

            default:
                return false;
            }
        }

        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public int compare(Viewer viewer, Object e1, Object e2) {
        int cat1 = category(e1);
        int cat2 = category(e2);

        if (cat1 != cat2)
            return cat1 - cat2;

        // fill in all columns
        if (e1 instanceof FileInfo) {
            FileInfo fileInfo1 = (FileInfo) e1;
            FileInfo fileInfo2 = (FileInfo) e2;

            switch (cViewer.getColumnIDs()[ columnIndex ]) {
            case DownloadTableTreeView.ID:
                return compareIntegers(fileInfo1.getId(), fileInfo2.getId());

            case DownloadTableTreeView.NETWORK:
                return compareStrings(fileInfo1.getNetwork().getNetworkName(),
                    fileInfo2.getNetwork().getNetworkName());

            case DownloadTableTreeView.NAME:
                return compareStrings(fileInfo1.getName(), fileInfo2.getName());

            case DownloadTableTreeView.SIZE:
                return compareLongs(fileInfo1.getSize(), fileInfo2.getSize());

            case DownloadTableTreeView.DOWNLOADED:
                return compareLongs(fileInfo1.getDownloaded(), fileInfo2.getDownloaded());

            case DownloadTableTreeView.PERCENT:
                return compareDoubles(fileInfo1.getPerc(), fileInfo2.getPerc());

            case DownloadTableTreeView.SOURCES:
                return compareIntegers(fileInfo1.getSources(), fileInfo2.getSources());

            case DownloadTableTreeView.AVAIL:
                return compareIntegers(fileInfo1.getRelativeAvail(), fileInfo2.getRelativeAvail());

            case DownloadTableTreeView.RATE:

                if (fileInfo1.getState().getState() == EnumFileState.DOWNLOADED)
                    return -1;
                else if (fileInfo2.getState().getState() == EnumFileState.DOWNLOADED)
                    return 1;
                else if (fileInfo1.getState().getState() == EnumFileState.QUEUED)
                    return 2;
                else if (fileInfo2.getState().getState() == EnumFileState.QUEUED)
                    return -2;
                else if (fileInfo1.getState().getState() == EnumFileState.PAUSED)
                    return 3;
                else if (fileInfo2.getState().getState() == EnumFileState.PAUSED)
                    return -3;
                else

                    return compareDoubles(fileInfo1.getRate(), fileInfo2.getRate());

            case DownloadTableTreeView.CHUNKS:
                return compareIntegers(fileInfo1.getNumChunks(), fileInfo2.getNumChunks());

            case DownloadTableTreeView.ETA:
                labelProvider = (ITableLabelProvider) ((TableTreeViewer) viewer).getLabelProvider();

                if (labelProvider.getColumnText(e1, columnIndex).equals(G2Gui.emptyString))
                    return 1;
                else if (labelProvider.getColumnText(e2, columnIndex).equals(G2Gui.emptyString))
                    return -1;
                else

                    return compareLongs(fileInfo1.getETA(), fileInfo2.getETA());

            case DownloadTableTreeView.PRIORITY:
                return compareIntegers(fileInfo1.getPriority(), fileInfo2.getPriority());

            case DownloadTableTreeView.LAST:
                return compareIntegers(fileInfo1.getOffset(), fileInfo2.getOffset());

            case DownloadTableTreeView.AGE:
                return compareLongs(Long.parseLong(fileInfo1.getAge()),
                    Long.parseLong(fileInfo2.getAge()));

            default:
                return 0;
            }
        } else {
            TreeClientInfo treeClientInfo1 = (TreeClientInfo) e1;
            TreeClientInfo treeClientInfo2 = (TreeClientInfo) e2;

            switch (cViewer.getColumnIDs()[ columnIndex ]) {
            case DownloadTableTreeView.NETWORK:
                return compareIntegers(treeClientInfo1.getClientInfo().getClientid(),
                    treeClientInfo2.getClientInfo().getClientid());

            case DownloadTableTreeView.NAME:
                return compareStrings(treeClientInfo1.getClientInfo().getClientName(),
                    treeClientInfo2.getClientInfo().getClientName());

            case DownloadTableTreeView.SIZE:
                labelProvider = (ITableLabelProvider) ((TableTreeViewer) viewer).getLabelProvider();

                return compareStrings(labelProvider.getColumnText(e1, columnIndex),
                    labelProvider.getColumnText(e2, columnIndex));

            case DownloadTableTreeView.DOWNLOADED:
                return compareIntegers(treeClientInfo1.getClientInfo().getRank(),
                    treeClientInfo2.getClientInfo().getRank());

            case DownloadTableTreeView.CHUNKS:
                return compareIntegers(treeClientInfo1.getClientInfo().getNumChunks(treeClientInfo1.getFileInfo()),
                    treeClientInfo2.getClientInfo().getNumChunks(treeClientInfo2.getFileInfo()));

            default:
                return 0;
            }
        }
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.tableTree.GTableTreeSorter#updateDisplay()
     */
    public void updateDisplay() {
        maintainSortOrder = PreferenceLoader.loadBoolean("maintainSortOrder");
    }
}


/*
$Log: DownloadTableTreeSorter.java,v $
Revision 1.15  2004/03/29 14:51:44  dek
some mem-improvements

Revision 1.14  2004/03/25 19:25:24  dek
yet more profiling

Revision 1.13  2003/12/04 08:47:28  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.12  2003/12/01 17:02:05  zet
*** empty log message ***

Revision 1.11  2003/12/01 16:39:25  zet
set default sort order for specific columns

Revision 1.10  2003/10/31 22:41:59  zet
rename to View

Revision 1.9  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.8  2003/10/31 10:42:47  lemmy
Renamed GViewer, GTableViewer and GTableTreeViewer to GPage... to avoid mix-ups with StructuredViewer...
Removed IGViewer because our abstract class GPage do the job
Use supertype/interface where possible to keep the design flexible!

Revision 1.7  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.6  2003/10/22 01:38:31  zet
*** empty log message ***

Revision 1.5  2003/10/16 19:58:03  zet
icons

Revision 1.4  2003/10/12 15:58:29  zet
rewrite downloads table & more..

Revision 1.3  2003/09/24 03:07:43  zet
add # of active sources column

Revision 1.2  2003/09/21 23:39:31  zet
displayTableColors preference

Revision 1.1  2003/09/20 14:39:21  zet
move transfer package

Revision 1.15  2003/09/18 14:11:01  zet
revert

Revision 1.13  2003/09/15 22:10:32  zet
add availability %, refresh delay option

Revision 1.12  2003/08/23 19:48:58  zet
*** empty log message ***

Revision 1.11  2003/08/23 19:44:12  zet
split transfer table to basic/advanced modes

Revision 1.10  2003/08/23 15:21:37  zet
remove @author

Revision 1.9  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.8  2003/08/22 21:16:36  lemmy
replace $user$ with $Author: dek $

Revision 1.7  2003/08/16 20:03:34  zet
downloaded at top

Revision 1.6  2003/08/15 22:05:58  zet
*** empty log message ***

Revision 1.5  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.4  2003/08/11 00:30:10  zet
show queued files

Revision 1.3  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.2  2003/08/06 17:15:24  zet
2 new columns

Revision 1.1  2003/08/04 19:22:08  zet
trial tabletreeviewer


*/
