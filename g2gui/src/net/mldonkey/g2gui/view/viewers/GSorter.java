/*
 * Copyright 2003
 * g2gui Team
 *
 *
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package net.mldonkey.g2gui.view.viewers;

import net.mldonkey.g2gui.model.Addr;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;


/**
 * GSorter - Generic sorter
 *
 * @version $Id: GSorter.java,v 1.5 2003/12/01 16:39:25 zet Exp $
 *
 */
public class GSorter extends ViewerSorter implements DisposeListener {
    protected int lastColumnIndex;
    protected int columnIndex;
    protected boolean lastSort;
    protected PreferenceStore preferenceStore = PreferenceLoader.getPreferenceStore();
    protected GView gViewer;
    protected ICustomViewer cViewer;

    public GSorter(GView gViewer) {
        this.gViewer = gViewer;
    }

    /**
     * after viewer creation
     */
    public void initialize() {
        cViewer = (ICustomViewer) gViewer.getViewer();
        gViewer.getTable().addDisposeListener(this);

        String savedSort = PreferenceLoader.loadString(gViewer.getPreferenceString() +
                "LastSortColumn");

        if (!savedSort.equals("") && (gViewer.getColumnIDs().indexOf(savedSort) != -1)) {
            setColumnIndex(gViewer.getColumnIDs().indexOf(savedSort));
            setLastSort(PreferenceLoader.loadBoolean(gViewer.getPreferenceString() +
                    "LastSortOrder"));
        }
    }

    /**
     * Compare two strings
     * @param aString1
     * @param aString2
     * @return int
     */
    public int compareStrings(String aString1, String aString2) {
        if (aString1.equals(""))
            return 1;

        if (aString2.equals(""))
            return -1;

        return (lastSort ? aString1.compareToIgnoreCase(aString2)
                         : aString2.compareToIgnoreCase(aString1));
    }

    /**
     * @param anInt1
     * @param anInt2
     * @return
     */
    public int compareIntegers(int anInt1, int anInt2) {
        if (anInt1 == 0)
            return 1;

        if (anInt2 == 0)
            return -1;

        return (lastSort ? (anInt1 - anInt2) : (anInt2 - anInt1));
    }

    /**
     * @param aLong1
     * @param aLong2
     * @return int
     */
    public int compareLongs(Long aLong1, Long aLong2) {
        return (lastSort ? aLong1.compareTo(aLong2) : aLong2.compareTo(aLong1));
    }

    /**
     * @param aLong1
     * @param aLong2
     * @return
     */
    public int compareLongs(long aLong1, long aLong2) {
        return compareLongs(new Long(aLong1), new Long(aLong2));
    }

    /**
     * @param aDouble1
     * @param aDouble2
     * @return int
     */
    public int compareDoubles(Double aDouble1, Double aDouble2) {
        return (lastSort ? aDouble1.compareTo(aDouble2) : aDouble2.compareTo(aDouble1));
    }

    /**
     * @param aDouble1
     * @param aDouble2
     * @return int
     */
    public int compareDoubles(double aDouble1, double aDouble2) {
        return compareDoubles(new Double(aDouble1), new Double(aDouble2));
    }

    /**
     * @param addr1
     * @param addr2
     * @return int
     */
    public int compareAddrs(Addr addr1, Addr addr2) {
        return (lastSort ? addr1.compareTo(addr2) : addr2.compareTo(addr1));
    }

    /**
     * @param i
     */
    public void setColumnIndex(int i) {
        columnIndex = i;
        lastSort = (columnIndex == lastColumnIndex) ? (!lastSort) : sortOrder(columnIndex);
        lastColumnIndex = columnIndex;
    }

    /**
     * @return true to sort in ascendingOrder, false for descending
     */
    public boolean sortOrder(int columnIndex) {
        return true;
    }

    /**
     * @return lastColumnIndex
     */
    public int getLastColumnIndex() {
        return lastColumnIndex;
    }

    /**
     * @param int
     */
    public void setLastColumnIndex(int i) {
        lastColumnIndex = i;
    }

    /**
     * @return lastSort
     */
    public boolean getLastSort() {
        return lastSort;
    }

    /**
     * @param boolean
     */
    public void setLastSort(boolean b) {
        lastSort = b;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public int compare(Viewer viewer, Object obj1, Object obj2) {
        return 0;
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
     */
    public void widgetDisposed(DisposeEvent e) {
        preferenceStore.setValue(gViewer.getPreferenceString() + "LastSortColumn",
            String.valueOf(gViewer.getColumnIDs().charAt(columnIndex)));
        preferenceStore.setDefault(gViewer.getPreferenceString() + "LastSortOrder", true);
        preferenceStore.setValue(gViewer.getPreferenceString() + "LastSortOrder", lastSort);
    }

    /**
     * update display
     */
    public void updateDisplay() {
    }
}


/*
$Log: GSorter.java,v $
Revision 1.5  2003/12/01 16:39:25  zet
set default sort order for specific columns

Revision 1.4  2003/11/28 22:38:06  zet
coalesce addr use

Revision 1.3  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.2  2003/10/31 10:42:47  lemmster
Renamed GViewer, GTableViewer and GTableTreeViewer to GPage... to avoid mix-ups with StructuredViewer...
Removed IGViewer because our abstract class GPage do the job
Use supertype/interface where possible to keep the design flexible!

Revision 1.1  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

*/
