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

import java.text.DecimalFormat;

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.enum.EnumClientMode;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.model.enum.EnumPriority;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.MainTab;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

/**
 * DownloadTableTreeLabelProvider
 *
 * @author $user$
 * @version $Id: DownloadTableTreeLabelProvider.java,v 1.7 2003/08/16 13:07:42 vnc Exp $ 
 *
 */
public class DownloadTableTreeLabelProvider implements ITableLabelProvider, IColorProvider {
	
	private Color unAvailableFileColor = new Color(null, 255,0,0);
	private Color availableFileColor = new Color(null, 255,165,0);
	private Color downloadedFileColor = new Color(null, 0,0,255);
	private Color queuedFileColor = new Color(null, 192,192,192);
	private Color pausedFileColor = new Color(null, 255, 0, 0);
	private Color rateAbove20Color = new Color(null, 35, 214, 0);
	private Color rateAbove10Color = new Color(null, 30, 170, 2);
	private Color rateAbove0Color = new Color(null, 24, 142, 4);
	private Color clientTransferringColor = new Color(null, 23, 229, 253);
	private Color clientRankedColor = new Color(null, 8, 162, 180);
	private DecimalFormat df = new DecimalFormat( "0.0" );
	private DecimalFormat dfp = new DecimalFormat( "0" );
	
	private CustomTableTreeViewer tableTreeViewer;
	
	public Color getBackground (Object arg0) {
			return null;
	}
	
	public Color getForeground(Object arg0) {
		if (arg0 instanceof FileInfo) {
			FileInfo fileInfo = (FileInfo) arg0;
			if (fileInfo.getState().getState() == EnumFileState.QUEUED)
				return queuedFileColor;
			if (fileInfo.getState().getState() == EnumFileState.PAUSED)
				return pausedFileColor;
			if (fileInfo.getState().getState() == EnumFileState.DOWNLOADED)
				return downloadedFileColor;
			if (fileInfo.getRate() > 20f) 
				return rateAbove20Color;
			if (fileInfo.getRate() > 10f) 
				return rateAbove10Color;
			if (fileInfo.getRate() > 0f) 
				return rateAbove0Color;	
			//TODO show 0% file availability in a red filename
			return availableFileColor;
		} else if (arg0 instanceof TreeClientInfo) {
			ClientInfo clientInfo = ((TreeClientInfo) arg0).getClientInfo();
			if (clientInfo.getState().getState() == EnumState.CONNECTED_DOWNLOADING)
				return clientTransferringColor;
			else
				return clientRankedColor;
		}
		return null;
	}
	
	public Image getColumnImage(Object arg0, int arg1) {
		if (arg0 instanceof FileInfo && arg1 == 1) {
			FileInfo fileInfo = (FileInfo) arg0;
			return MainTab.getNetworkImage( fileInfo.getNetwork().getNetworkType() );
		}
		return null;
	}
	// The full row is redrawn on each update()/refresh()
	// *&@%#%*# jface
	public String getColumnText(Object arg0, int arg1) {
			
		if (arg0 instanceof FileInfo) {
			FileInfo fileInfo = (FileInfo) arg0;
		
			switch(arg1) {
			case 0: // id
				return ""+fileInfo.getId();
			case 1: // network
				return ""+fileInfo.getNetwork().getNetworkName();
			case 2: // name
				return ""+fileInfo.getName();
			case 3: // size
				return ""+fileInfo.getStringSize();
			case 4: // downloaded
				return ""+fileInfo.getStringDownloaded();
			case 5: // percent
				return ""+dfp.format(fileInfo.getPerc());
			case 6: // # sources  fileInfo.getSources() is always 0 
				return ""+fileInfo.getClientInfos().size();		
			case 7: // rate
				if (fileInfo.getState().getState() == EnumFileState.PAUSED)
					return "Paused";
				else if (fileInfo.getState().getState() == EnumFileState.QUEUED)
					return "Queued";
				else if (fileInfo.getState().getState() == EnumFileState.DOWNLOADED)
					return "Downloaded";	
				else 
					return "" + df.format(fileInfo.getRawRate() / 1000f);
			case 8: // chunks
				return ""+fileInfo.getNumChunks();
			case 9: // eta
				return ""+getFileETA(fileInfo);
			case 10: // priority
				if (fileInfo.getPriority() == EnumPriority.HIGH) 
					return "High";
				else if (fileInfo.getPriority() == EnumPriority.NORMAL)
					return "Normal";
				else if (fileInfo.getPriority() == EnumPriority.LOW)
					return "Low";
				else 
					return "???";
			case 11: // last
				return fileInfo.getStringOffset();
			case 12: // age
				return fileInfo.getStringAge();
			default: 
				return "";
			} 
		
		} else if (arg0 instanceof TreeClientInfo) {
			TreeClientInfo treeClientInfo = ( TreeClientInfo ) arg0;
			
			switch(arg1) {
				case 1: // id
					return ""+treeClientInfo.getClientInfo().getClientid();
				case 2: // client name
					return ""+treeClientInfo.getClientInfo().getClientName();
				case 3: // client connection
					return ""+getClientConnection(treeClientInfo.getClientInfo());
				case 4: // client activity
					return ""+getClientActivity(treeClientInfo.getClientInfo());
				case 8: // num chunks
					return ""+treeClientInfo.getClientInfo().getNumChunks( treeClientInfo.getFileInfo() );
					
				default: 
					return "";
			}
		} 
		else 
			return "";
		
	}
	public String getFileETA (FileInfo fileInfo) {
		
		if ( fileInfo.getState().getState() == EnumFileState.QUEUED
			|| fileInfo.getState().getState() == EnumFileState.PAUSED )
			return "";
		return fileInfo.getStringETA();
	}

	public String getClientConnection(ClientInfo clientInfo) {
		if ( clientInfo.getClientKind().getClientMode() == EnumClientMode.FIREWALLED ) 
			return "firewalled";			
		else
			return "direct";	
	}
	
	public String getClientActivity( ClientInfo clientInfo) {
		if ( clientInfo.getState().getState() == EnumState.CONNECTED_DOWNLOADING )
			return "transferring";
		else 
			return  "rank: " + clientInfo.getState().getRank() ;
	}	

	public void addListener(ILabelProviderListener arg0) {
	}

	public void dispose() {
		queuedFileColor.dispose();
		pausedFileColor.dispose();
		downloadedFileColor.dispose();
		rateAbove20Color.dispose();
		rateAbove10Color.dispose();
		rateAbove0Color.dispose();
		clientRankedColor.dispose();
		clientTransferringColor.dispose();
	}

	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}
	public void removeListener(ILabelProviderListener arg0) {
	}
	public void setTableTreeViewer(CustomTableTreeViewer v) {
		tableTreeViewer = v;
	}
	
	

}

/*
$Log: DownloadTableTreeLabelProvider.java,v $
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
