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

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.model.enum.EnumClientMode;

/**
 * ClientInfoComparator
 *
 * @author $user$
 * @version $Id: ClientInfoComparator.java,v 1.2 2003/07/20 13:08:23 dek Exp $ 
 *
 */
class ClientInfoComparator implements Comparator {

	private FileInfo fileInfo;

	private int column;
	
	ClientInfoComparator( int columnIndex, FileInfo fileInfo ) {
		super();
		this.fileInfo = fileInfo;
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
		//               ClientName  Connect.            Rank 

		ClientInfo row1 =  ( ClientInfo ) obj1;
		ClientInfo row2 =  ( ClientInfo ) obj2;
		
		int result = 0;
		switch ( column ) {
			case 0 :				
				break;
			case 1 :				
				break;
			case 2 :
				String r12 = row1.getClientName();
				String r22 = row2.getClientName();
				result = r12.compareToIgnoreCase( r22 );
				break;
			case 3 :
				String r13 = stateToString( row1.getClientKind().getClientMode() );
				String r23 = stateToString( row2.getClientKind().getClientMode() );
				result = r13.compareToIgnoreCase( r23 );
				break;
			case 4 :
				Integer r14 = numberCompleteChunks( row1 );
				Integer r24 = numberCompleteChunks( row2 );
				result = r24.compareTo( r14 );							
				break;
			case 5 :				
				break;
			case 6 :
				Integer r16 = new Integer( row1.getState().getRank() );
				Integer r26 = new Integer( row2.getState().getRank() );
				result = r16.compareTo( r26 );
				break;
			case 7 :			
				break;			
			default :
				break;
		}
		return result;
	}

	
	private String stateToString( Enum enumState ) {
		String connection = "";
		if ( enumState == EnumClientMode.FIREWALLED ) {
			connection = "firewalled";
		} else {
			connection = "direct";
		}
		return connection;
	}
	
	private Integer numberCompleteChunks( ClientInfo clientInfo ) {
		int resultInt = 0;
		int length = 0;
		String avail = clientInfo.getFileAvailability( fileInfo );
		if ( avail != null ) length = avail.length();
		for ( int i = 0; i < length; i++ ) {		
			//this availability is so low, we can assume, it is not available:			
			if ( avail.charAt( i ) == '0' ) {
			}
			else if ( avail.charAt( i ) == '1' ) {
				resultInt++;		
			}
			else if ( avail.charAt( i ) == '2' ) {
				resultInt++;				
			}
		}
		Integer result = new Integer ( resultInt );
		return result;
	}
}

/*
$Log: ClientInfoComparator.java,v $
Revision 1.2  2003/07/20 13:08:23  dek
chunks are sorted descending , when first sorted, as this "feels more natural"

Revision 1.1  2003/07/20 10:31:21  dek
done some work on flickering & sorting

*/