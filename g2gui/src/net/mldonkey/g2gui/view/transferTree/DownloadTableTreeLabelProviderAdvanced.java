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
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.graphics.Image;

/**
 * DownloadtableTreeLabelProviderAdvanced
 *
 * @version $Id: DownloadTableTreeLabelProviderAdvanced.java,v 1.6 2003/09/15 22:10:32 zet Exp $ 
 *
 */
public class DownloadTableTreeLabelProviderAdvanced	extends DownloadTableTreeLabelProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
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
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
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
			case 6: // # sources  
				return ""+fileInfo.getSources();	
			case 7: // Relative availability %
				return ""+fileInfo.getRelativeAvail() + "%";		
			case 8: // rate
				if (fileInfo.getState().getState() == EnumFileState.PAUSED)
					return G2GuiResources.getString( "TT_Paused" );
				else if (fileInfo.getState().getState() == EnumFileState.QUEUED)
					return G2GuiResources.getString( "TT_Queued" );
				else if (fileInfo.getState().getState() == EnumFileState.DOWNLOADED)
					return G2GuiResources.getString( "TT_Downloaded" );	
				else 
					return "" + df.format(fileInfo.getRate() / 1000f);
			case 9: // chunks
				return ""+fileInfo.getNumChunks();
			case 10: // eta
				return ""+getFileETA(fileInfo);
			case 11: // priority
				return fileInfo.getStringPriority();
			case 12: // last
				return fileInfo.getStringOffset();
			case 13: // age
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
					return ""+treeClientInfo.getClientInfo().getClientConnection();
				case 4: // client activity
					return ""+treeClientInfo.getClientInfo().getClientActivity();
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
Revision 1.6  2003/09/15 22:10:32  zet
add availability %, refresh delay option

Revision 1.5  2003/09/14 03:37:43  zet
changedProperties

Revision 1.4  2003/09/13 22:26:44  zet
weak sets & !rawrate

Revision 1.3  2003/09/10 14:46:07  zet
sources

Revision 1.2  2003/08/31 01:46:33  zet
localise

Revision 1.1  2003/08/23 19:44:12  zet
split transfer table to basic/advanced modes


*/