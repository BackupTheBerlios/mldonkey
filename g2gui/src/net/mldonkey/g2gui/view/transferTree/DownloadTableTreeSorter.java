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
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.model.enum.EnumPriority;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * ResultTableSorter
 *
 *
 * @version $Id: DownloadTableTreeSorter.java,v 1.10 2003/08/23 15:21:37 zet Exp $ 
 *
 */
public class DownloadTableTreeSorter extends ViewerSorter {
	// set the default sort column to "rate"
	private int columnIndex = 7;
	private int lastColumnIndex = 7;
	// last sort ascending = fase
	private boolean lastSort = false;
	private ITableLabelProvider labelProvider;
	private boolean maintainSortOrder = false;
	/**
	 * Creates a new viewer sorter
	 */
	public DownloadTableTreeSorter() {
		super();
	}
	
	public int category(Object element) {
		if(element instanceof FileInfo) return 1;
		if(element instanceof TreeClientInfo) return 2;
		return 3;
	}
		
	public boolean isSorterProperty(Object element, String property) {
		
		// if you return true, the table will refresh() and flicker more often
		// but the table maintains sort order, so try to be precise with updates.
		if ((columnIndex == 4 
			|| columnIndex == 5
			|| columnIndex == 7
			|| columnIndex == 9
			|| columnIndex == 11) 
			&& (element instanceof FileInfo)
			&& maintainSortOrder) {		

			FileInfo fileInfo = (FileInfo) element;
			switch (columnIndex) {
				case 4:
					return (fileInfo.changedDownloaded ? true : false);
				case 5:	
					return (fileInfo.changedPercent ? true : false);
				case 7:
					return (fileInfo.changedRate ? true : false); 
				case 9:
					return (fileInfo.changedETA ? true : false);
				case 11:
					return (fileInfo.changedLast ? true : false);
				default: 
					return false;
			}
		}
		return false;
	}
		
	public int compare(Viewer viewer, Object e1, Object e2) {
		int cat1 = category(e1);
		int cat2 = category(e2);
		if (cat1 != cat2)
			return cat1 - cat2;

		// fill in all columns
		if (e1 instanceof FileInfo) {
			FileInfo fileInfo1 = (FileInfo) e1;
			FileInfo fileInfo2 = (FileInfo) e2;

			switch (columnIndex) {
				case 0 : // id
					return compareIntegers(
						fileInfo1.getId(),
						fileInfo2.getId());

				case 1 : // network name
					return compareStrings(
						fileInfo1.getNetwork().getNetworkName(),
						fileInfo2.getNetwork().getNetworkName());

				case 2 : // filesize
					return compareStrings(
						fileInfo1.getName(),
						fileInfo2.getName());

				case 3 : // size
					return compareLongs(
						fileInfo1.getSize(),
						fileInfo2.getSize());

				case 4 : // downloaded
					return compareLongs(
						fileInfo1.getDownloaded(),
						fileInfo2.getDownloaded());

				case 5 : // percent
					return compareDoubles(
						fileInfo1.getPerc(),
						fileInfo2.getPerc());
						
				case 6 : // sources getSources() is 0 ?
					return compareIntegers(
						fileInfo1.getClientInfos().size(),
						fileInfo2.getClientInfos().size());		

				case 7 : // rate - paused/queued on the bottom
						if (fileInfo1.getState().getState() == EnumFileState.DOWNLOADED)
							return -1;
						else if (fileInfo2.getState().getState() == EnumFileState.DOWNLOADED)
							return 1;
						else if (fileInfo1.getState().getState() == EnumFileState.QUEUED)
							return 2;
						else if (fileInfo2.getState().getState() == EnumFileState.QUEUED)
							return -2;
						else if (fileInfo1.getState().getState() == EnumFileState.PAUSED)
							return 3;
						else if (fileInfo2.getState().getState() == EnumFileState.PAUSED)
							return -3;
							
						else 
							return compareDoubles(
								fileInfo1.getRate(),
								fileInfo2.getRate());
								
				case 8 : // numChunks
					return compareIntegers(
						fileInfo1.getNumChunks(),
						fileInfo2.getNumChunks());
			
				case 9 : // eta - nulls on the bottom
					
					labelProvider = (ITableLabelProvider) ((TableTreeViewer) viewer).getLabelProvider();
					if (labelProvider.getColumnText(e1, columnIndex).equals("")) 
						return 1;
					else if (labelProvider.getColumnText(e2, columnIndex).equals("")) 
						return -1;
					else 
						return compareLongs(
						fileInfo1.getETA(),
						fileInfo2.getETA());
						
						
				case 10: // priority
					if (fileInfo1.getPriority() == EnumPriority.LOW)
						return (lastSort ? -1 : 1);
					else if (fileInfo1.getPriority() == EnumPriority.HIGH)
						return (lastSort ? 1 : -1);	
					else {
						if (fileInfo2.getPriority() == EnumPriority.LOW)
							return (lastSort ? 1 : -1);
						else return (lastSort ? -1 : 1);
					}
				
				case 11: // last
					return compareIntegers(fileInfo1.getOffset(), fileInfo2.getOffset());
				case 12: // age
					return compareLongs (Long.parseLong(fileInfo1.getAge()), 
										Long.parseLong(fileInfo2.getAge()));		
				default :
					return 0;

			}
		} else {
			TreeClientInfo treeClientInfo1 = (TreeClientInfo) e1;
			TreeClientInfo treeClientInfo2 = (TreeClientInfo) e2;
									
			switch (columnIndex) {

				case 1 : // id
					return compareIntegers(
						treeClientInfo1.getClientInfo().getClientid(),
						treeClientInfo2.getClientInfo().getClientid());

				case 2 : // name
					return compareStrings(
						treeClientInfo1.getClientInfo().getClientName(),
						treeClientInfo2.getClientInfo().getClientName());
						
				case 3 : // direct/firewall
					labelProvider = (ITableLabelProvider) ((TableTreeViewer) viewer).getLabelProvider();
					return compareStrings(labelProvider.getColumnText(e1, columnIndex),
								labelProvider.getColumnText(e2, columnIndex));

				case 4 : // rank
					return compareIntegers(
						treeClientInfo1.getClientInfo().getState().getRank(),
						treeClientInfo2.getClientInfo().getState().getRank());
						
				case 8: // numChunks 
					
					return compareIntegers(treeClientInfo1.getClientInfo().getNumChunks( treeClientInfo1.getFileInfo() ),
											treeClientInfo2.getClientInfo().getNumChunks( treeClientInfo2.getFileInfo() ) );
				default :
					return 0;
			}

		}

	}
	
	// always leave empty strings at the bottom
	public int compareStrings(String aString1, String aString2) {
		if (aString1.equals ( "" )) return 1;
		if (aString2.equals ( "" )) return -1;
		return ( lastSort ? aString1.compareToIgnoreCase( aString2 )
						: aString2.compareToIgnoreCase( aString1 ) );
	}
	
	public int compareIntegers(int anInt1, int anInt2) {
		return ( lastSort ? anInt1 - anInt2 
				: anInt2 - anInt1 );
	}

	public int compareDoubles(Double aDouble1, Double aDouble2) {
		return ( lastSort ? aDouble1.compareTo( aDouble2 )
						: aDouble2.compareTo( aDouble1 ) );
	}

	public int compareDoubles(double aDouble1, double aDouble2) {
		return compareDoubles(new Double(aDouble1), new Double(aDouble2));
	}

	public int compareLongs(Long aLong1, Long aLong2) {
		return ( lastSort ? aLong1.compareTo( aLong2 )
						: aLong2.compareTo( aLong1 ) );
	}
	
	public int compareLongs(long aLong1, long aLong2) {
		return compareLongs(new Long(aLong1), new Long(aLong2));
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
			if (i < 3)
				lastSort = true;
			else
				lastSort = false;
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
	public boolean getMaintainSortOrder() {
		return maintainSortOrder;
	}
	public void setMaintainSortOrder(boolean b) {
		maintainSortOrder=b;
	}
	
}

/*
$Log: DownloadTableTreeSorter.java,v $
Revision 1.10  2003/08/23 15:21:37  zet
remove @author

Revision 1.9  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.8  2003/08/22 21:16:36  lemmster
replace $user$ with $Author: zet $

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