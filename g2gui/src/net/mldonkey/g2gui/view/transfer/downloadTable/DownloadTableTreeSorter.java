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
package net.mldonkey.g2gui.view.transfer.downloadTable;

import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.view.transfer.CustomTableTreeViewer;
import net.mldonkey.g2gui.view.transfer.TreeClientInfo;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;


/**
 * ResultTableSorter
 *
 * @version $Id: DownloadTableTreeSorter.java,v 1.4 2003/10/12 15:58:29 zet Exp $
 *
 */
public class DownloadTableTreeSorter extends ViewerSorter {
    
    private int columnIndex = 0;
    private int lastColumnIndex = 0;
    private boolean lastSort = false;
    private boolean maintainSortOrder = false;
	private ITableLabelProvider labelProvider;
	private CustomTableTreeViewer tableTreeViewer;
	
    /**
     * Creates a new viewer sorter
     */
    public DownloadTableTreeSorter() {
        super();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ViewerSorter#category(java.lang.Object)
     */
    public int category( Object element ) {
        if ( element instanceof FileInfo ) {
            return 1;
        }

        if ( element instanceof TreeClientInfo ) {
            return 2;
        }

        return 3;
    }

	/**
	 * Leave empty strings on bottom
	 * @param aString1
	 * @param aString2
	 * @return int
	 */    
    public int compareStrings( String aString1, String aString2 ) {
        if ( aString1.equals( "" ) ) {
            return 1;
        }

        if ( aString2.equals( "" ) ) {
            return -1;
        }

        return ( lastSort ? aString1.compareToIgnoreCase( aString2 ) : aString2.compareToIgnoreCase( aString1 ) );
    }

	/**
	 * @param anInt1
	 * @param anInt2
	 * @return int
	 */
    public int compareIntegers( int anInt1, int anInt2 ) {
        return ( lastSort ? ( anInt1 - anInt2 ) : ( anInt2 - anInt1 ) );
    }

	/**
	 * @param aDouble1
	 * @param aDouble2
	 * @return int
	 */
    public int compareDoubles( Double aDouble1, Double aDouble2 ) {
        return ( lastSort ? aDouble1.compareTo( aDouble2 ) : aDouble2.compareTo( aDouble1 ) );
    }

	/**
	 * @param aDouble1
	 * @param aDouble2
	 * @return int
	 */
    public int compareDoubles( double aDouble1, double aDouble2 ) {
        return compareDoubles( new Double( aDouble1 ), new Double( aDouble2 ) );
    }
    
	/**
	 * @param aLong1
	 * @param aLong2
	 * @return int
	 */
    public int compareLongs( Long aLong1, Long aLong2 ) {
        return ( lastSort ? aLong1.compareTo( aLong2 ) : aLong2.compareTo( aLong1 ) );
    }

	/**
	 * @param aLong1
	 * @param aLong2
	 * @return
	 */
    public int compareLongs( long aLong1, long aLong2 ) {
        return compareLongs( new Long( aLong1 ), new Long( aLong2 ) );
    }

    /**
     * Sets the column index
     * @param i The column index to sort
     */
    public void setColumnIndex( int i ) {
        columnIndex = i;

        if ( columnIndex == lastColumnIndex ) {
            lastSort = !lastSort;
        } else {
        	switch (tableTreeViewer.getColumnIDs()[ columnIndex ]) {
        		case DownloadTableTreeViewer.ID:
        		case DownloadTableTreeViewer.NAME:
				case DownloadTableTreeViewer.NETWORK:
				case DownloadTableTreeViewer.LAST:   
				case DownloadTableTreeViewer.ETA:      		
        			lastSort = true;
        			break;
				default:
					lastSort = false;
					break;
        	}
        }

        lastColumnIndex = columnIndex;
    }

	/**
	 * @return int
	 */
    public int getLastColumnIndex() {
        return lastColumnIndex;
    }

	/**
	 * @param i
	 */
    public void setLastColumnIndex( int i ) {
        lastColumnIndex = i;
    }

	/**
	 * @return boolean
	 */
    public boolean getLastSort() {
        return lastSort;
    }

	/**
	 * @param b
	 */
    public void setLastSort( boolean b ) {
        lastSort = b;
    }

    public boolean getMaintainSortOrder() {
        return maintainSortOrder;
    }

	/**
	 * @param b
	 */
    public void setMaintainSortOrder( boolean b ) {
        maintainSortOrder = b;
    }
    
    /**
     * @param v
     */
	public void setTableTreeViewer( CustomTableTreeViewer v ) {
		tableTreeViewer = v;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerSorter#isSorterProperty(java.lang.Object, java.lang.String)
	 */
	public boolean isSorterProperty( Object element, String property ) {
		if ( element instanceof FileInfo && maintainSortOrder ) {
        	
			switch ( tableTreeViewer.getColumnIDs()[ columnIndex ] ) {
			
			case DownloadTableTreeViewer.DOWNLOADED:
				return ( property.equals( FileInfo.CHANGED_DOWNLOADED ) ? true : false );

			case DownloadTableTreeViewer.PERCENT:
				return ( property.equals( FileInfo.CHANGED_PERCENT ) ? true : false );

			case DownloadTableTreeViewer.ACTIVE_SOURCES:
				return ( property.equals( FileInfo.CHANGED_ACTIVE ) ? true : false );
					
			case DownloadTableTreeViewer.AVAIL:
				return ( property.equals( FileInfo.CHANGED_AVAIL ) ? true : false );

			case DownloadTableTreeViewer.RATE:
				return ( property.equals( FileInfo.CHANGED_RATE ) ? true : false );

			case DownloadTableTreeViewer.ETA:
				return ( property.equals( FileInfo.CHANGED_ETA ) ? true : false );

			case DownloadTableTreeViewer.LAST:
				return ( property.equals( FileInfo.CHANGED_LAST ) ? true : false );

			default:
				return false;
			}
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public int compare( Viewer viewer, Object e1, Object e2 ) {
		int cat1 = category( e1 );
		int cat2 = category( e2 );

		if ( cat1 != cat2 ) {
			return cat1 - cat2;
		}

		// fill in all columns
		if ( e1 instanceof FileInfo ) {
			FileInfo fileInfo1 = (FileInfo) e1;
			FileInfo fileInfo2 = (FileInfo) e2;

			switch ( tableTreeViewer.getColumnIDs()[ columnIndex ] ) {
        
			case DownloadTableTreeViewer.ID: 
				return compareIntegers( fileInfo1.getId(), fileInfo2.getId() );

			case DownloadTableTreeViewer.NETWORK:
				return compareStrings( fileInfo1.getNetwork().getNetworkName(), fileInfo2.getNetwork().getNetworkName() );

			case DownloadTableTreeViewer.NAME:
				return compareStrings( fileInfo1.getName(), fileInfo2.getName() );

			case DownloadTableTreeViewer.SIZE:
				return compareLongs( fileInfo1.getSize(), fileInfo2.getSize() );

			case DownloadTableTreeViewer.DOWNLOADED:
				return compareLongs( fileInfo1.getDownloaded(), fileInfo2.getDownloaded() );

			case DownloadTableTreeViewer.PERCENT:
				return compareDoubles( fileInfo1.getPerc(), fileInfo2.getPerc() );

			case DownloadTableTreeViewer.SOURCES:
				return compareIntegers( fileInfo1.getSources( ), fileInfo2.getSources() );

			case DownloadTableTreeViewer.ACTIVE_SOURCES:
				return compareIntegers( fileInfo1.getActiveSources( ), fileInfo2.getActiveSources( ) );

			case DownloadTableTreeViewer.AVAIL:
				return compareIntegers( fileInfo1.getRelativeAvail(), fileInfo2.getRelativeAvail() );

			case DownloadTableTreeViewer.RATE:

				if ( fileInfo1.getState().getState() == EnumFileState.DOWNLOADED ) {
					return -1;
				} else if ( fileInfo2.getState().getState() == EnumFileState.DOWNLOADED ) {
					return 1;
				} else if ( fileInfo1.getState().getState() == EnumFileState.QUEUED ) {
					return 2;
				} else if ( fileInfo2.getState().getState() == EnumFileState.QUEUED ) {
					return -2;
				} else if ( fileInfo1.getState().getState() == EnumFileState.PAUSED ) {
					return 3;
				} else if ( fileInfo2.getState().getState() == EnumFileState.PAUSED ) {
					return -3;
				}
				else {
					return compareDoubles( fileInfo1.getRate(), fileInfo2.getRate() );
				}

			case DownloadTableTreeViewer.CHUNKS:
				return compareIntegers( fileInfo1.getNumChunks(), fileInfo2.getNumChunks() );

			case DownloadTableTreeViewer.ETA:
				labelProvider = (ITableLabelProvider) ( (TableTreeViewer) viewer ).getLabelProvider();

				if ( labelProvider.getColumnText( e1, columnIndex ).equals( "" ) ) {
					return 1;
				} else if ( labelProvider.getColumnText( e2, columnIndex ).equals( "" ) ) {
					return -1;
				} else {
					return compareLongs( fileInfo1.getETA(), fileInfo2.getETA() );
				}

			case DownloadTableTreeViewer.PRIORITY:
				return compareIntegers( fileInfo1.getPriority( ), fileInfo2.getPriority( ) );

			case DownloadTableTreeViewer.LAST:
				return compareIntegers( fileInfo1.getOffset(), fileInfo2.getOffset() );

			case DownloadTableTreeViewer.AGE:
				return compareLongs( Long.parseLong( fileInfo1.getAge() ), Long.parseLong( fileInfo2.getAge() ) );

			default:
				return 0;
			}
		} else {
			TreeClientInfo treeClientInfo1 = (TreeClientInfo) e1;
			TreeClientInfo treeClientInfo2 = (TreeClientInfo) e2;

			switch ( tableTreeViewer.getColumnIDs()[ columnIndex ] ) {
        
			case DownloadTableTreeViewer.NETWORK: 
				return compareIntegers( treeClientInfo1.getClientInfo().getClientid(), treeClientInfo2.getClientInfo().getClientid() );

			case DownloadTableTreeViewer.NAME:
				return compareStrings( treeClientInfo1.getClientInfo().getClientName(), treeClientInfo2.getClientInfo().getClientName() );

			case DownloadTableTreeViewer.SIZE:
				labelProvider = (ITableLabelProvider) ( (TableTreeViewer) viewer ).getLabelProvider();

				return compareStrings( labelProvider.getColumnText( e1, columnIndex ), labelProvider.getColumnText( e2, columnIndex ) );

			case DownloadTableTreeViewer.DOWNLOADED:
				return compareIntegers( treeClientInfo1.getClientInfo().getState().getRank(),
					treeClientInfo2.getClientInfo().getState().getRank() );

			case DownloadTableTreeViewer.CHUNKS:
				return compareIntegers( treeClientInfo1.getClientInfo().getNumChunks( treeClientInfo1.getFileInfo() ),
					treeClientInfo2.getClientInfo().getNumChunks( treeClientInfo2.getFileInfo() ) );

			default:
				return 0;
			}
		}
	}    
    
}


/*
$Log: DownloadTableTreeSorter.java,v $
Revision 1.4  2003/10/12 15:58:29  zet
rewrite downloads table & more..

Revision 1.3  2003/09/24 03:07:43  zet
add # of active sources column

Revision 1.2  2003/09/21 23:39:31  zet
displayTableColors preference

Revision 1.1  2003/09/20 14:39:21  zet
move transfer package

Revision 1.15  2003/09/18 14:11:01  zet
revert

Revision 1.13  2003/09/15 22:10:32  zet
add availability %, refresh delay option

Revision 1.12  2003/08/23 19:48:58  zet
*** empty log message ***

Revision 1.11  2003/08/23 19:44:12  zet
split transfer table to basic/advanced modes

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
