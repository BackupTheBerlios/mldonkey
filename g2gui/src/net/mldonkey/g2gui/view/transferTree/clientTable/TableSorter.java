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
package net.mldonkey.g2gui.view.transferTree.clientTable;

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.enum.EnumState;

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * TableSorter
 *
 *
 * @version $Id: TableSorter.java,v 1.5 2003/09/08 19:50:31 lemmster Exp $ 
 *
 */
public class TableSorter extends ViewerSorter {
	
	private boolean lastSort = true;
	private int lastColumnIndex = 0;
	private int columnIndex = 0;
	
	/**
	 * Creates a new viewer sorter
	 */
	public TableSorter() {
		super();
	}

	
	public int compare( Viewer viewer, Object obj1, Object obj2 ) {
		
		
		switch (columnIndex) {
			
			case 0:
					ClientInfo  clientInfo1 = ( ClientInfo ) obj1;
					ClientInfo  clientInfo2 = ( ClientInfo ) obj2;
			
					if (clientInfo1.getState().getState() == EnumState.CONNECTED_DOWNLOADING)
						return -1;
					if (clientInfo2.getState().getState() == EnumState.CONNECTED_DOWNLOADING)
						return 1;
					if (clientInfo1.getState().getRank() != 0
						&& clientInfo2.getState().getRank() != 0) {
							return compareIntegers(clientInfo1.getState().getRank(),
									clientInfo2.getState().getRank()); }
					
					if (clientInfo1.getState().getRank() != 0)
						return -1;
					if (clientInfo2.getState().getRank() != 0)
						return 1;				
			default:
		
				String s1, s2;
				
				IBaseLabelProvider prov = ((ContentViewer)viewer).getLabelProvider();
				TableLabelProvider lprov = (TableLabelProvider) ((TableViewer)viewer).getLabelProvider();
				
				s1 = lprov.getColumnText(obj1, columnIndex);
				s2 = lprov.getColumnText(obj2, columnIndex);
				
				return compareStrings(s1, s2);
				
		}
	
	}
	
	public int compareStrings(String aString1, String aString2) {
		if (aString1.equals ( "" )) return 1;
		if (aString2.equals ( "" )) return -1;
		return ( lastSort ? aString1.compareToIgnoreCase( aString2 )
						: aString2.compareToIgnoreCase( aString1 ) );
	}	
	public int compareIntegers(int anInt1, int anInt2) {
		if (anInt1 == 0) return 1;
		if (anInt2 == 0) return -1;
		return ( lastSort ? anInt1 - anInt2 
				: anInt2 - anInt1 );
	}
	public void setColumnIndex( int i ) {
		columnIndex = i;
		if (columnIndex == lastColumnIndex)
			lastSort = !lastSort;
		else {
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
$Log: TableSorter.java,v $
Revision 1.5  2003/09/08 19:50:31  lemmster
just repaired the log

Revision 1.4  2003/08/23 15:21:37  zet
remove @author

*/