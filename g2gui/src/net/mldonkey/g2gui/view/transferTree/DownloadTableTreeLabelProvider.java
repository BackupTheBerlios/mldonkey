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

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.enum.EnumClientMode;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.model.enum.EnumPriority;
import net.mldonkey.g2gui.model.enum.EnumState;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

/**
 * DownloadTableTreeLabelProvider
 *
 * @author $user$
 * @version $Id: DownloadTableTreeLabelProvider.java,v 1.1 2003/08/04 19:22:08 zet Exp $ 
 *
 */
public class DownloadTableTreeLabelProvider implements ITableLabelProvider, IColorProvider {
	
	private Color queuedFileColor = new Color(null, 41, 174, 57);
	private Color pausedFileColor = new Color(null, 255, 0, 0);
	
	public Color getBackground (Object arg0) {
			return null;
	}
	
	public Color getForeground (Object arg0) {
		if (arg0 instanceof FileInfo) {
			FileInfo fileInfo = (FileInfo) arg0;
			if ( fileInfo.getState().getState() == EnumFileState.QUEUED ) 
				return queuedFileColor;
				
			if (fileInfo.getState().getState() == EnumFileState.PAUSED )
				return pausedFileColor;
				
			return null;
		}
		return null;
	}
	
	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}
	
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
				return ""+fileInfo.getPerc();
			case 6: // # sources   TODO: remove clientInfos.size
				return ""+fileInfo.getSources() + "(" + fileInfo.getClientInfos().size() + ")";		
			case 7: // rate
				if (fileInfo.getState().getState() == EnumFileState.PAUSED)
					return "Paused";
				else if (fileInfo.getState().getState() == EnumFileState.QUEUED)
					return "Queued";
				else 
					return ""+fileInfo.getRate();
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
			default: 
				return "";
			} 
		
		} else if (arg0 instanceof ClientInfo) {
			ClientInfo clientInfo = ( ClientInfo ) arg0;
			
			switch(arg1) {
				case 1: // id
					return ""+clientInfo.getClientid();
				case 2: // client name
					return ""+clientInfo.getClientName();
				case 3: // client connection
					return ""+getClientConnection(clientInfo);
				case 4: // client activity
					return ""+getClientActivity(clientInfo);
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
	}

	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

	public void removeListener(ILabelProviderListener arg0) {
	}
	

}

/*
$Log: DownloadTableTreeLabelProvider.java,v $
Revision 1.1  2003/08/04 19:22:08  zet
trial tabletreeviewer


*/
