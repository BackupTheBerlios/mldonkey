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

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * ResultTableSorter
 *
 * @author $user$
 * @version $Id: ResultTableSorter.java,v 1.5 2003/08/15 22:51:50 dek Exp $ 
 *
 */
public class ResultTableSorter extends ViewerSorter {
	// set the default column to sort to name
	private int columnIndex = 1;
	private int lastColumnIndex = 1;
	// last sort ascending = true
	private boolean lastSort = true;
	
	/**
	 * Creates a new viewer sorter
	 */
	public ResultTableSorter() {
		super();
	}
	
	/**
	 * Returns a negative, zero, or positive number depending on whether
	 * the first element is less than, equal to, or greater than
	 * the second element.
	 * <p>
	 * The default implementation of this method is based on
	 * comparing the elements' categories as computed by the <code>category</code>
	 * framework method. Elements within the same category are further 
	 * subjected to a case insensitive compare of their label strings, either
	 * as computed by the content viewer's label provider, or their 
	 * <code>toString</code> values in other cases. Subclasses may override.
	 * </p>
	 * 
	 * @param viewer the viewer
	 * @param obj1 the first element
	 * @param obj2 the second element
	 * @return a negative number if the first element is less  than the 
	 *  second element; the value <code>0</code> if the first element is
	 *  equal to the second element; and a positive number if the first
	 *  element is greater than the second element
	 */
	public int compare( Viewer viewer, Object obj1, Object obj2 ) {
		ResultInfo result1 = ( ResultInfo ) obj1;
		ResultInfo result2 = ( ResultInfo ) obj2;

		String aString1, aString2;
		
		switch (columnIndex) {
			
			case 0: // network name
				aString1 = result1.getNetwork().getNetworkName();
				aString2 = result2.getNetwork().getNetworkName();
				return compareStrings( aString1, aString2 );
												
			case 1: // filename
				aString1 = result1.getNames()[ 0 ];
				aString2 = result2.getNames()[ 0 ];
				return compareStrings( aString1, aString2 );
						 	
			case 2: // filesize
				Long aLong1 = new Long( result1.getSize() );
				Long aLong2 = new Long( result2.getSize() );
				return ( lastSort ? aLong1.compareTo( aLong2 ) 
								: aLong2.compareTo( aLong1) );
								
			case 3: // format 
				aString1 = result1.getFormat();
				aString2 = result2.getFormat();
				return compareStrings( aString1, aString2 ); 
								
			case 4: // media
				aString1 = result1.getType();
				aString2 = result2.getType();
				return compareStrings( aString1, aString2 );
										
			case 5: // availability 
			Integer anInt1;
			Integer anInt2;
			/* null-checks important to avoid crashes because of accessing non-existant
			 * array[0]
			 */			
				if ( result1.getTags() != null )				
					anInt1 = new Integer ( result1.getTags()[ 0 ].getValue() );				
				else 
					anInt1 = new Integer(0);
									
				if ( result2.getTags() != null )			
					anInt2 = new Integer ( result2.getTags()[ 0 ].getValue() );				
				else 
					anInt2 = new Integer(0);
				
				return ( lastSort ? anInt1.compareTo( anInt2 ) 
								: anInt2.compareTo( anInt1 ) );	
		
			default:
				return 0;
		}
	}
	
	// always leave empty strings at the bottom
	public int compareStrings(String aString1, String aString2) {
		if (aString1.equals ( "" )) return 1;
		if (aString2.equals ( "" )) return -1;
		return ( lastSort ? aString1.compareToIgnoreCase( aString2 )
						: aString2.compareToIgnoreCase( aString1 ) );
		
	}
	
	
	/**
	 * Sets the column index
	 * @param i The column index to sort
	 */
	public void setColumnIndex( int i ) {
		columnIndex = i;
		if (columnIndex == lastColumnIndex)
			lastSort = !lastSort;
		else {
			if (i == 2)
				lastSort = false;
			else
				lastSort = true;
		}
		lastColumnIndex = columnIndex;
	}
	public int getLastColumnIndex() {
		return lastColumnIndex;
	}
	public void setLastColumnIndex(int i) {
		lastColumnIndex = i;
	}
	public boolean getLastSort() {
		return lastSort;
	}
	public void setLastSort(boolean b) {
		lastSort = b;
	}

}

/*
$Log: ResultTableSorter.java,v $
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