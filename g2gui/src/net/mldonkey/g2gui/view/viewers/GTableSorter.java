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

import net.mldonkey.g2gui.view.pref.PreferenceLoader;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;


/**
 * GenericTableSorter
 *
 * @version $Id: GTableSorter.java,v 1.1 2003/10/22 01:36:59 zet Exp $
 *
 */
public class GTableSorter extends ViewerSorter implements DisposeListener {
    public GTableViewer gTableViewer;
    public CustomTableViewer tableViewer;
    protected int lastColumnIndex;
    protected int columnIndex;
    protected boolean lastSort;
    protected PreferenceStore preferenceStore = PreferenceLoader.getPreferenceStore();

    public GTableSorter(GTableViewer gTableViewer) {
        this.gTableViewer = gTableViewer;
    }

    /**
     * initialize after tableViewer creation
     */
    protected void initialize() {
        this.tableViewer = gTableViewer.getTableViewer();
        this.tableViewer.getTable().addDisposeListener(this);

        String savedSort = PreferenceLoader.loadString(gTableViewer.getPreferenceString() + "LastSortColumn");

        if (!savedSort.equals("") && (gTableViewer.getColumnIDs().indexOf(savedSort) != -1)) {
            setColumnIndex(gTableViewer.getColumnIDs().indexOf(savedSort));
			setLastSort(PreferenceLoader.loadBoolean(gTableViewer.getPreferenceString() + "LastSortOrder"));
        }
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public int compare(Viewer viewer, Object obj1, Object obj2) {
        return 0;
    }

    /**
     * Compare two strings
     * @param aString1
     * @param aString2
     * @return int
     */
    public int compareStrings(String aString1, String aString2) {
        if (aString1.equals("")) {
            return 1;
        }

        if (aString2.equals("")) {
            return -1;
        }

        return (lastSort ? aString1.compareToIgnoreCase(aString2) : aString2.compareToIgnoreCase(aString1));
    }

    /**
     * @param anInt1
     * @param anInt2
     * @return
     */
    public int compareIntegers(int anInt1, int anInt2) {
        if (anInt1 == 0) {
            return 1;
        }

        if (anInt2 == 0) {
            return -1;
        }

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
     * @param i
     */
    public void setColumnIndex(int i) {
        columnIndex = i;
        lastSort = columnIndex == lastColumnIndex ? !lastSort : true;
        lastColumnIndex = columnIndex;
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
     * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
     */
    public void widgetDisposed(DisposeEvent e) {
        preferenceStore.setValue(gTableViewer.getPreferenceString() + "LastSortColumn",
            String.valueOf(gTableViewer.getColumnIDs().charAt(columnIndex)));
		preferenceStore.setDefault(gTableViewer.getPreferenceString() + "LastSortOrder", true);
        preferenceStore.setValue(gTableViewer.getPreferenceString() + "LastSortOrder", lastSort);
    }
}


/*
$Log: GTableSorter.java,v $
Revision 1.1  2003/10/22 01:36:59  zet
add column selector to server/search (might not be finished yet..)

*/
