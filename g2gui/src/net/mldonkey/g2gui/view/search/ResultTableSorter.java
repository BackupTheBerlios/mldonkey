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
 * @version $Id: ResultTableSorter.java,v 1.1 2003/07/25 22:34:51 lemmstercvs01 Exp $ 
 *
 */
public class ResultTableSorter extends ViewerSorter {
	private int columnIndex;
	private boolean lastSort;
	
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
		
		/* network */
		if ( columnIndex == 0 ) {
			String aString1 = result1.getNetwork().getNetworkName();
			String aString2 = result2.getNetwork().getNetworkName();
			if ( lastSort )
				return aString1.compareToIgnoreCase( aString2 );
			else
				return aString2.compareToIgnoreCase( aString1 );
		}
		/* name */
		if ( columnIndex == 1 ) {
			String aString1 = result1.getNames()[ 0 ];
			String aString2 = result2.getNames()[ 0 ];
			if ( lastSort )
				return aString1.compareToIgnoreCase( aString2 );
			else
				return aString2.compareToIgnoreCase( aString1 );
		}
		/* size */
		if ( columnIndex == 2 ) {
			Integer anInt1 = new Integer( result1.getSize() );
			Integer anInt2 = new Integer( result2.getSize() );
			if ( lastSort )
				return anInt1.compareTo( anInt2 );
			else
				return anInt2.compareTo( anInt1 );
		}
		/* format */
		if ( columnIndex == 3 ) {
			String aString1 = result1.getFormat();
			String aString2 = result2.getFormat();
			if ( lastSort )
				return aString1.compareToIgnoreCase( aString2 );
			else
				return aString2.compareToIgnoreCase( aString1 );
		}
		/* media */
		if ( columnIndex == 4 ) {
			String aString1 = result1.getNames()[ 0 ];
			String aString2 = result2.getNames()[ 0 ];
			if ( lastSort )
				return aString1.compareToIgnoreCase( aString2 );
			else
				return aString2.compareToIgnoreCase( aString1 );
		}
		/* avail */
		if ( columnIndex == 5 ) {
			String aString1 = result1.getNames()[ 0 ];
			String aString2 = result2.getNames()[ 0 ];
			if ( lastSort )
				return aString1.compareToIgnoreCase( aString2 );
			else
				return aString2.compareToIgnoreCase( aString1 );
		}
		return 0;
	}
	
	/**
	 * Sets the column index
	 * @param i The column index to sort
	 */
	public void setColumnIndex( int i ) {
		columnIndex = i;
	}

	/**
	 * @param i The ascending or descending
	 */
	public void setLastSort( boolean i ) {
		lastSort = i;
	}

}

/*
$Log: ResultTableSorter.java,v $
Revision 1.1  2003/07/25 22:34:51  lemmstercvs01
lots of changes

*/