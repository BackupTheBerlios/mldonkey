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
package net.mldonkey.g2gui.view.transferTree;

import java.util.Comparator;

import net.mldonkey.g2gui.model.FileInfo;

/**
 * DownloadItemComparator
 *
 * @author $user$
 * @version $Id: FileInfoComparator.java,v 1.2 2003/07/15 20:19:57 dek Exp $ 
 *
 */
public class FileInfoComparator implements Comparator {
	private int column;

	/**
	 * @param columnIndex the column, which is responsible for the search order
	 */
	public FileInfoComparator( int columnIndex ) {
		super();
		this.column = columnIndex ;
	}

	/**
	 * Compares two rows (type DownloadItem) using the specified
	 * column entry.
	 * @param obj1 First row to compare
	 * @param obj2 Second row to compare
	 * @return negative if obj1 less than obj2, positive if
	 * obj1 greater than obj2, and zero if equal.
	 */
	public int compare( Object obj1, Object obj2 ) {
		//"ID"|"Network"|"Filename"|"Rate"|"Chunks"|"%"|"Downloaded"|"Size"
		//  0     1           2        3       4      5    6            7

		FileInfo row1 =  ( FileInfo ) obj1;
		FileInfo row2 =  ( FileInfo ) obj2;
		
		int result = 0;
		switch (column) {
			case 0 :
				Integer r1_0 = new Integer( row1.getId() );
				Integer r2_0 = new Integer( row2.getId() );
				result = r1_0.compareTo( r2_0 );
				break;
			case 1 :
				String r1_1 = row1.getNetwork().getNetworkName();
				String r2_1 = row2.getNetwork().getNetworkName();
				result = r1_1.compareToIgnoreCase( r2_1 );
				break;
			case 2 :
				String r1_2 = row1.getName();
				String r2_2 = row2.getName();
				result = r1_2.compareToIgnoreCase( r2_2 );
				break;
			case 3 :
				Float r1_3 = new Float( row1.getRate() );
				Float r2_3 = new Float( row2.getRate() );
				result = r1_3.compareTo( r2_3 );
				break;
			case 4 :
				Float r1_4 = new Float( row1.getRate() );
				Float r2_4 = new Float( row2.getRate() );
				result = r1_4.compareTo( r2_4 );
				break;
			case 5 :
				//Sorting by # of available chunks...
				result = 0;
				break;
			case 6 :
				Integer r1_6 = new Integer( row1.getDownloaded() );
				Integer r2_6 = new Integer( row2.getDownloaded() );
				result = r1_6.compareTo( r2_6 );
				break;
			case 7 :
				Integer r1_7 = new Integer( row1.getSize() );
				Integer r2_7 = new Integer( row2.getSize() );
				result = r1_7.compareTo( r2_7 );
				break;
			
			default :
				break;
		}
		return result;
	}
}

/*
$Log: FileInfoComparator.java,v $
Revision 1.2  2003/07/15 20:19:57  dek
*** empty log message ***

Revision 1.1  2003/07/15 20:13:56  dek
sorting works now, chunk-display is kind of broken, when sorting with expanded tree-items...

*/