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
package net.mldonkey.g2gui.view.transfer.downloadTable;

import java.text.DecimalFormat;

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.transfer.TreeClientInfo;
import net.mldonkey.g2gui.view.viewers.GView;
import net.mldonkey.g2gui.view.viewers.table.GTableLabelProvider;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;


/**
 * DownloadTableTreeLabelProvider
 *
 * @version $Id: DownloadTableTreeLabelProvider.java,v 1.19 2004/03/25 19:25:24 dek Exp $
 *
 */
public class DownloadTableTreeLabelProvider extends GTableLabelProvider implements IColorProvider {
    private Color availableFileColor;
    private Color unAvailableFileColor; 
    private Color downloadedFileColor;
    private Color queuedFileColor;
    private Color pausedFileColor;
    private Color rateAbove20Color; 
    private Color rateAbove10Color;
    private Color rateAbove0Color;
	
	private boolean displayColors = true;
    private DecimalFormat dfp = new DecimalFormat( "0" );

	public DownloadTableTreeLabelProvider(GView gViewer) {
	    super(gViewer);
	}

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IColorProvider#getBackground(java.lang.Object)
     */
    public Color getBackground( Object arg0 ) {
        	return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IColorProvider#getForeground(java.lang.Object)
     */
    public Color getForeground( Object arg0 ) {
        if ( !displayColors ) {
            return null;
        }

        if ( arg0 instanceof FileInfo ) {
            FileInfo fileInfo = (FileInfo) arg0;

            if ( fileInfo.getState().getState() == EnumFileState.QUEUED ) {
                return queuedFileColor;
            } 
            else if ( fileInfo.getState().getState() == EnumFileState.PAUSED ) {
                return pausedFileColor;
            }
            else if ( fileInfo.getState().getState() == EnumFileState.DOWNLOADED ) {
                return downloadedFileColor;
            }
            else if ( fileInfo.getRate() / 1000f > 20f ) {
                return rateAbove20Color;
            }
            else if ( fileInfo.getRate() / 1000f > 10f ) {
                return rateAbove10Color;
            }
            else if ( fileInfo.getRate() / 1000f > 0f ) {
                return rateAbove0Color;
            } 
            else if ( fileInfo.getRelativeAvail() == 0 ) {
            	return unAvailableFileColor;
            } 
            else {
            	return availableFileColor;
            }
        } else if ( arg0 instanceof TreeClientInfo ) {
            ClientInfo clientInfo = ( (TreeClientInfo) arg0 ).getClientInfo();

            if ( clientInfo.getState() == EnumState.CONNECTED_DOWNLOADING ) {
                return rateAbove0Color;
            } 
            else {
                return null;
            }
        }

        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
     */
    public boolean isLabelProperty( Object arg0, String arg1 ) {
        return true;
    }
    
    public void updateDisplay() {
		displayColors = PreferenceLoader.loadBoolean("displayTableColors");
		unAvailableFileColor = PreferenceLoader.loadColour( "downloadsUnAvailableFileColor" );
		downloadedFileColor = PreferenceLoader.loadColour( "downloadsDownloadedFileColor" );
		queuedFileColor = PreferenceLoader.loadColour( "downloadsQueuedFileColor" );
		pausedFileColor = PreferenceLoader.loadColour( "downloadsPausedFileColor" );
		availableFileColor = PreferenceLoader.loadColour( "downloadsAvailableFileColor" );
		rateAbove0Color = PreferenceLoader.loadColour( "downloadsRateAbove0FileColor" );
		rateAbove10Color = PreferenceLoader.loadColour( "downloadsRateAbove10FileColor" );
		rateAbove20Color = PreferenceLoader.loadColour( "downloadsRateAbove20FileColor" );		
    }
    
	/* (non-Javadoc)
		* @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
		*/

   public Image getColumnImage( Object arg0, int arg1 ) {
	   if ( arg0 instanceof FileInfo 
		   && cViewer.getColumnIDs()[ arg1 ] == DownloadTableTreeView.NETWORK ) {
		   FileInfo fileInfo = (FileInfo) arg0;

		   return G2GuiResources.getNetworkImage( fileInfo.getNetwork().getNetworkType() );
	   } else if ( arg0 instanceof TreeClientInfo ) {
		   ClientInfo clientInfo = ( (TreeClientInfo) arg0 ).getClientInfo();

		   switch ( cViewer.getColumnIDs()[ arg1 ] ) {

			   case DownloadTableTreeView.NETWORK:
				   return G2GuiResources.getNetworkImage( clientInfo.getClientnetworkid().getNetworkType() );
			   case DownloadTableTreeView.NAME:
				   return G2GuiResources.getClientImage( (EnumState) clientInfo.getState() );
			   default:
				   return null;
        
		   }
	   }

	   return null;
   }

   /* (non-Javadoc)
	* @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	*/
   public String getColumnText( Object arg0, int arg1 ) {
	   if ( arg0 instanceof FileInfo ) {
		   FileInfo fileInfo = (FileInfo) arg0;

		   switch ( cViewer.getColumnIDs()[ arg1 ] ) {
        
			   case DownloadTableTreeView.ID: 
				   return "" + fileInfo.getId();
	
			   case DownloadTableTreeView.NETWORK:
				   return fileInfo.getNetwork().getNetworkName();
	
			   case DownloadTableTreeView.NAME:
				   return fileInfo.getName();
	
			   case DownloadTableTreeView.SIZE:
				   return fileInfo.getStringSize();
	
			   case DownloadTableTreeView.DOWNLOADED:
				   return fileInfo.getStringDownloaded();
	
			   case DownloadTableTreeView.PERCENT:
				   return dfp.format( fileInfo.getPerc() ) + "%";
	
			   case DownloadTableTreeView.SOURCES:
			   	   return fileInfo.getStringSources();
			   	
			   case DownloadTableTreeView.AVAIL:
				   return fileInfo.getRelativeAvail() + "%";
	
			   case DownloadTableTreeView.RATE:
				   return fileInfo.getStringRate();
	
			   case DownloadTableTreeView.CHUNKS:
				   return new Integer(fileInfo.getNumChunks()).toString();
	
			   case DownloadTableTreeView.ETA:
				   return fileInfo.getStringETA(); 
	
			   case DownloadTableTreeView.PRIORITY:
				   return fileInfo.getStringPriority();
	
			   case DownloadTableTreeView.LAST:
				   return fileInfo.getStringOffset();
	
			   case DownloadTableTreeView.AGE:
				   return fileInfo.getStringAge();
	
			   default:
				   return "";
		   }
	   } else if ( arg0 instanceof TreeClientInfo ) {
		   TreeClientInfo treeClientInfo = (TreeClientInfo) arg0;

		   switch ( cViewer.getColumnIDs()[ arg1 ] ) {
       
			   case DownloadTableTreeView.NETWORK:
				   return "" + treeClientInfo.getClientInfo().getClientid();
	
			   case DownloadTableTreeView.NAME:
				   return treeClientInfo.getClientInfo().getClientName();
	
			   case DownloadTableTreeView.SIZE:
				   return treeClientInfo.getClientInfo().getClientConnection();
	
			   case DownloadTableTreeView.DOWNLOADED:
				   return treeClientInfo.getClientInfo().getClientActivity();
	
			   case DownloadTableTreeView.CHUNKS:
				   return "" + treeClientInfo.getClientInfo().getNumChunks( treeClientInfo.getFileInfo() );
	
			   default:
				   return "";
		   }
	   } else {
		   return "";
	   }
   }
    
}


/*
$Log: DownloadTableTreeLabelProvider.java,v $
Revision 1.19  2004/03/25 19:25:24  dek
yet more profiling

Revision 1.18  2003/12/07 19:38:09  lemmy
refactoring

Revision 1.17  2003/12/04 08:47:28  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.16  2003/11/13 00:45:28  zet
fix #1081

Revision 1.15  2003/10/31 22:41:59  zet
rename to View

Revision 1.14  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.13  2003/10/31 10:42:47  lemmy
Renamed GViewer, GTableViewer and GTableTreeViewer to GPage... to avoid mix-ups with StructuredViewer...
Removed IGViewer because our abstract class GPage do the job
Use supertype/interface where possible to keep the design flexible!

Revision 1.12  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.11  2003/10/23 00:07:19  zet
use "-" instead of 0

Revision 1.10  2003/10/23 00:04:32  zet
use "-" instead of 0

Revision 1.9  2003/10/22 01:38:31  zet
*** empty log message ***

Revision 1.8  2003/10/16 19:58:03  zet
icons

Revision 1.7  2003/10/15 22:06:32  zet
colours

Revision 1.6  2003/10/13 21:36:33  zet
Use "-" instead of 0's

Revision 1.5  2003/10/13 21:26:39  zet
different, darker greens

Revision 1.4  2003/10/13 20:47:16  zet
orange -> black

Revision 1.3  2003/10/12 15:58:29  zet
rewrite downloads table & more..

Revision 1.2  2003/09/21 23:39:31  zet
displayTableColors preference

Revision 1.1  2003/09/20 14:39:21  zet
move transfer package

Revision 1.22  2003/09/18 14:11:01  zet
revert

Revision 1.20  2003/09/14 03:37:43  zet
changedProperties

Revision 1.19  2003/08/31 01:46:33  zet
localise

Revision 1.18  2003/08/23 19:44:12  zet
split transfer table to basic/advanced modes

Revision 1.17  2003/08/23 15:21:37  zet
remove @author

Revision 1.16  2003/08/23 01:04:03  zet
*** empty log message ***

Revision 1.15  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.14  2003/08/22 21:16:36  lemmy
replace $user$ with $Author: dek $

Revision 1.13  2003/08/20 14:58:43  zet
sources clientinfo viewer

Revision 1.12  2003/08/17 23:13:42  zet
centralize resources, move images

Revision 1.11  2003/08/17 19:16:36  vnc
cosmetics: added "%" sign

Revision 1.10  2003/08/16 20:05:19  zet
*** empty log message ***

Revision 1.9  2003/08/16 13:48:24  vnc
dispose() the two new added colors

Revision 1.8  2003/08/16 13:26:42  vnc
switched client transferring/ranking colors

Revision 1.7  2003/08/16 13:07:42  vnc
more file colors based on availability

Revision 1.6  2003/08/15 22:05:58  zet
*** empty log message ***

Revision 1.5  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.4  2003/08/11 00:30:10  zet
show queued files

Revision 1.3  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.2  2003/08/06 17:14:17  zet
2 new columns

Revision 1.1  2003/08/04 19:22:08  zet
trial tabletreeviewer


*/
