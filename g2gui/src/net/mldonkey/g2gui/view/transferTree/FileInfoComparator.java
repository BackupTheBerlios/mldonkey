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
 * @version $Id: FileInfoComparator.java,v 1.9 2003/07/20 10:31:21 dek Exp $ 
 *
 */
class FileInfoComparator implements Comparator {
	private int column;

	/**
	 * @param columnIndex the column, which is responsible for the search order
	 */
	FileInfoComparator( int columnIndex ) {
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
		switch ( column ) {
			case 0 :
				Integer r10 = new Integer( row1.getId() );
				Integer r20 = new Integer( row2.getId() );
				result = r10.compareTo( r20 );
				break;
			case 1 :
				String r11 = row1.getNetwork().getNetworkName();
				String r21 = row2.getNetwork().getNetworkName();
				result = r11.compareToIgnoreCase( r21 );
				break;
			case 2 :
				String r12 = row1.getName();
				String r22 = row2.getName();
				result = r12.compareToIgnoreCase( r22 );
				break;
			case 3 :
				Float r13 = new Float( row1.getRate() );
				Float r23 = new Float( row2.getRate() );
				result = r13.compareTo( r23 );
				break;
			case 4 :
			Float r14 = new Float( row1.getPerc() );
			Float r24 = new Float( row2.getPerc() );
			result = r14.compareTo( r24 );
				break;
			case 5 :
				Float r15 = new Float( row1.getPerc() );
				Float r25 = new Float( row2.getPerc() );
				result = r15.compareTo( r25 );
				break;
			case 6 :
				Integer r16 = new Integer( row1.getDownloaded() );
				Integer r26 = new Integer( row2.getDownloaded() );
				result = r16.compareTo( r26 );
				break;
			case 7 :
				Integer r17 = new Integer( row1.getSize() );
				Integer r27 = new Integer( row2.getSize() );
				result = r17.compareTo( r27 );
				break;
			
			default :
				break;
		}
		return result;
	}
}

/*
$Log: FileInfoComparator.java,v $
Revision 1.9  2003/07/20 10:31:21  dek
done some work on flickering & sorting

Revision 1.8  2003/07/18 15:45:16  dek
still working on flicker...

Revision 1.7  2003/07/18 09:43:15  dek
never use * / (without space) in CVS-commit-comments......

Revision 1.6  2003/07/18 09:40:07  dek
finally got rid of the flickering?? dunno (* searching CRT to test *)

Revision 1.5  2003/07/16 18:16:53  dek
another flickering-test

Revision 1.4  2003/07/15 20:48:09  dek
now even the %-values are sorted right...

Revision 1.1  2003/07/15 20:13:56  dek
sorting works now, chunk-display is kind of broken, when sorting with expanded tree-items...

*/