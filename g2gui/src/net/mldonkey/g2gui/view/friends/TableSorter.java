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
package net.mldonkey.g2gui.view.friends;

import net.mldonkey.g2gui.model.ClientInfo;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * TableSorter
 *
 * @author $Author: zet $
 * @version $Id: TableSorter.java,v 1.1 2003/08/12 04:10:29 zet Exp $ 
 *
 */
public class TableSorter extends ViewerSorter {
	
	private boolean lastSort = true;
	private int columnIndex = 0;
	
	/**
	 * Creates a new viewer sorter
	 */
	public TableSorter() {
		super();
	}

	
	public int compare( Viewer viewer, Object obj1, Object obj2 ) {
		ClientInfo  clientInfo1 = ( ClientInfo ) obj1;
		ClientInfo  clientInfo2 = ( ClientInfo ) obj2;
		
		switch (columnIndex) {
			
			default: 
			 	return compareStrings(clientInfo1.getClientName(), 
					 					clientInfo2.getClientName());
			
		}
	
	}
	
	public int compareStrings(String aString1, String aString2) {
			if (aString1.equals ( "" )) return 1;
			if (aString2.equals ( "" )) return -1;
			return ( lastSort ? aString1.compareToIgnoreCase( aString2 )
							: aString2.compareToIgnoreCase( aString1 ) );
	}	
		
}

/*
$Log: TableSorter.java,v $
Revision 1.1  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging



*/