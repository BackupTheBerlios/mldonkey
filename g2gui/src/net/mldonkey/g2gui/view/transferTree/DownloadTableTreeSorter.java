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
package net.mldonkey.g2gui.view.transferTree;

import net.mldonkey.g2gui.model.FileInfo;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * ResultTableSorter
 *
 * @version $Id: DownloadTableTreeSorter.java,v 1.14 2003/09/18 12:12:23 lemmster Exp $
 *
 */
public class DownloadTableTreeSorter extends ViewerSorter {
    // set the default sort column to "rate"
    protected int columnIndex = 8;
    protected int lastColumnIndex = 8;

    // last sort ascending = fase
    protected boolean lastSort = false;
    protected ITableLabelProvider labelProvider;
    protected boolean maintainSortOrder = false;

    /**
     * Creates a new viewer sorter
     */
    public DownloadTableTreeSorter() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @param element DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int category( Object element ) {
        if ( element instanceof FileInfo )
            return 1;
        if ( element instanceof TreeClientInfo )
            return 2;
        return 3;
    }

    /**
     * DOCUMENT ME!
     *
     * @param element DOCUMENT ME!
     * @param property DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSorterProperty( Object element, String property ) {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param viewer DOCUMENT ME!
     * @param e1 DOCUMENT ME!
     * @param e2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compare( Viewer viewer, Object e1, Object e2 ) {
        return 0;
    }

	/**
	 * DOCUMENT ME!
	 * 
	 * @param aString1 DOCUMENT ME!
	 * @param aString2 DOCUMENT ME!
	 * @return DOCUMENT ME!
	 */
    public int compareStrings( String aString1, String aString2 ) {
        if ( aString1.equals( "" ) )
            return 1;
        if ( aString2.equals( "" ) )
            return -1;
        return
        	( lastSort ? aString1.compareToIgnoreCase( aString2 )
        	: aString2.compareToIgnoreCase( aString1 ) );
    }

    /**
     * DOCUMENT ME!
     *
     * @param anInt1 DOCUMENT ME!
     * @param anInt2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compareIntegers( int anInt1, int anInt2 ) {
        return ( lastSort ? ( anInt1 - anInt2 ) : ( anInt2 - anInt1 ) );
    }

    /**
     * DOCUMENT ME!
     *
     * @param aDouble1 DOCUMENT ME!
     * @param aDouble2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compareDoubles( Double aDouble1, Double aDouble2 ) {
        return ( lastSort ? aDouble1.compareTo( aDouble2 ) : aDouble2.compareTo( aDouble1 ) );
    }

    /**
     * DOCUMENT ME!
     *
     * @param aDouble1 DOCUMENT ME!
     * @param aDouble2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compareDoubles( double aDouble1, double aDouble2 ) {
        return compareDoubles( new Double( aDouble1 ), new Double( aDouble2 ) );
    }

    /**
     * DOCUMENT ME!
     *
     * @param aLong1 DOCUMENT ME!
     * @param aLong2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compareLongs( Long aLong1, Long aLong2 ) {
        return ( lastSort ? aLong1.compareTo( aLong2 ) : aLong2.compareTo( aLong1 ) );
    }

    /**
     * DOCUMENT ME!
     *
     * @param aLong1 DOCUMENT ME!
     * @param aLong2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compareLongs( long aLong1, long aLong2 ) {
        return compareLongs( new Long( aLong1 ), new Long( aLong2 ) );
    }

    /**
     * Sets the column index
     * 
     * @param i The column index to sort
     */
    public void setColumnIndex( int i ) {
        columnIndex = i;
        if ( columnIndex == lastColumnIndex )
            lastSort = !lastSort;
        else {
            if ( i < 3 )
                lastSort = true;
            else
                lastSort = false;
        }
        lastColumnIndex = columnIndex;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLastColumnIndex() {
        return lastColumnIndex;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     */
    public void setLastColumnIndex( int i ) {
        lastColumnIndex = i;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getLastSort() {
        return lastSort;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void setLastSort( boolean b ) {
        lastSort = b;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getMaintainSortOrder() {
        return maintainSortOrder;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void setMaintainSortOrder( boolean b ) {
        maintainSortOrder = b;
    }
}

/*
$Log: DownloadTableTreeSorter.java,v $
Revision 1.14  2003/09/18 12:12:23  lemmster
checkstyle

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

Revision 1.8  2003/08/22 21:16:36  lemmster
replace $user$ with $Author: lemmster $

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
