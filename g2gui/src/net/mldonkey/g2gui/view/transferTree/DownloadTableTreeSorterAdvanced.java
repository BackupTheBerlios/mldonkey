/*
 * Copyright 2003
 * g2gui Team
 * 
 * 
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui; if not, write to the Free Software
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

/**
 * DownloadTableTreeSorterAdvanced
 *
 * @author $Author: zet $
 * @version $Id: DownloadTableTreeSorterAdvanced.java,v 1.1 2003/08/23 19:44:12 zet Exp $ 
 *
 */
public class DownloadTableTreeSorterAdvanced extends DownloadTableTreeSorter {
	
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
	
}
/*
$Log: DownloadTableTreeSorterAdvanced.java,v $
Revision 1.1  2003/08/23 19:44:12  zet
split transfer table to basic/advanced modes


*/
