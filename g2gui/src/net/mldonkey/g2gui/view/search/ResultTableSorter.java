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

import net.mldonkey.g2gui.model.ResultInfo;
import net.mldonkey.g2gui.view.helper.OurTableSorter;

import org.eclipse.jface.viewers.Viewer;

/**
 * ResultTableSorter
 *
 *
 * @version $Id: ResultTableSorter.java,v 1.11 2003/10/21 17:00:45 lemmster Exp $
 *
 */
public class ResultTableSorter extends OurTableSorter {
	/**
	 * @param sorterName
	 * @param aColumnIndex
	 */
	public ResultTableSorter() {
		super( "SearchSorter", 1 );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
    public int compare( Viewer viewer, Object obj1, Object obj2 ) {
        ResultInfo result1 = ( ResultInfo ) obj1;
        ResultInfo result2 = ( ResultInfo ) obj2;
        String aString1;
        String aString2;
        switch ( columnIndex ) {
        case 0: // network name
            aString1 = "" + result1.getNetwork().getNetworkName();
            aString2 = "" + result2.getNetwork().getNetworkName();
            return compareStrings( aString1, aString2 );
        case 1: // filename
            aString1 = "" + result1.getName();
            aString2 = "" + result2.getName();
            return compareStrings( aString1, aString2 );
        case 2: // filesize
            Long aLong1 = new Long( result1.getSize() );
            Long aLong2 = new Long( result2.getSize() );
            return ( lastSort ? aLong1.compareTo( aLong2 ) : aLong2.compareTo( aLong1 ) );
        case 3: // format 
            aString1 = "" + result1.getFormat();
            aString2 = "" + result2.getFormat();
            return compareStrings( aString1, aString2 );
        case 4: // media
            aString1 = "" + result1.getType();
            aString2 = "" + result2.getType();
            return compareStrings( aString1, aString2 );
        case 5: // availability 
            Integer anInt1 = new Integer( result1.getAvail() );
            Integer anInt2 = new Integer( result2.getAvail() );
            return ( lastSort ? anInt1.compareTo( anInt2 ) : anInt2.compareTo( anInt1 ) );
        default:
            return 0;
        }
    }
}

/*
$Log: ResultTableSorter.java,v $
Revision 1.11  2003/10/21 17:00:45  lemmster
class hierarchy for tableviewer

Revision 1.10  2003/09/18 10:39:21  lemmster
checkstyle

Revision 1.9  2003/09/17 20:07:44  lemmster
avoid NPE´s in search

Revision 1.8  2003/08/23 15:21:37  zet
remove @author

Revision 1.7  2003/08/22 21:10:57  lemmster
replace $user$ with $Author: lemmster $

Revision 1.6  2003/08/16 20:59:09  dek
searching works now without errors AGAIN ;-)

Revision 1.5  2003/08/15 22:51:50  dek
searching works now without errors again :-)

Revision 1.4  2003/07/31 11:55:39  zet
sort with empty strings at bottom

Revision 1.3  2003/07/31 04:11:10  zet
searchresult changes

Revision 1.2  2003/07/27 18:45:47  lemmstercvs01
lots of changes

Revision 1.1  2003/07/25 22:34:51  lemmstercvs01
lots of changes

*/
