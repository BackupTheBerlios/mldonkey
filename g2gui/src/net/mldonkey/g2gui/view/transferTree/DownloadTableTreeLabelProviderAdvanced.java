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
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.model.enum.EnumPriority;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.graphics.Image;

/**
 * DownloadtableTreeLabelProviderAdvanced
 *
 * @version $Id: DownloadTableTreeLabelProviderAdvanced.java,v 1.1 2003/08/23 19:44:12 zet Exp $ 
 *
 */
public class DownloadTableTreeLabelProviderAdvanced	extends DownloadTableTreeLabelProvider {

	public Image getColumnImage(Object arg0, int arg1) {
		if (arg0 instanceof FileInfo && arg1 == 1) {
			FileInfo fileInfo = (FileInfo) arg0;
			return G2GuiResources.getNetworkImage( fileInfo.getNetwork().getNetworkType() );
		} else if (arg0 instanceof TreeClientInfo) {
			ClientInfo clientInfo = ((TreeClientInfo) arg0).getClientInfo();
			if (arg1 == 1)
				return G2GuiResources.getNetworkImage( clientInfo.getClientnetworkid().getNetworkType() );
			else if (arg1 == 2)
				return G2GuiResources.getClientImage( (EnumState) clientInfo.getState().getState() );
			else 
				return null;
		}
	
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
				return ""+dfp.format(fileInfo.getPerc()) + "%";
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

}
/*
$Log: DownloadTableTreeLabelProviderAdvanced.java,v $
Revision 1.1  2003/08/23 19:44:12  zet
split transfer table to basic/advanced modes


*/