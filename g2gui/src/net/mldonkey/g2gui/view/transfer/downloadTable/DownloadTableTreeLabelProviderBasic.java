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

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.transfer.TreeClientInfo;

import org.eclipse.swt.graphics.Image;

/**
 * DownloadTableTreeLabelProviderBasic
 *
 * @version $Id: DownloadTableTreeLabelProviderBasic.java,v 1.1 2003/09/20 14:39:21 zet Exp $ 
 *
 */
public class DownloadTableTreeLabelProviderBasic
	extends DownloadTableTreeLabelProvider {

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
			case 4: // percent
				return ""+dfp.format(fileInfo.getPerc()) + "%";
			case 5: // rate
				if (fileInfo.getState().getState() == EnumFileState.PAUSED)
					return G2GuiResources.getString( "TT_Paused" );
				else if (fileInfo.getState().getState() == EnumFileState.QUEUED)
					return G2GuiResources.getString( "TT_Queued" );
				else if (fileInfo.getState().getState() == EnumFileState.DOWNLOADED)
					return G2GuiResources.getString( "TT_Downloaded" );	
				else 
					return "" + df.format(fileInfo.getRate() / 1000f);
			case 6: // eta
				return ""+getFileETA(fileInfo);
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
				default: 
					return "";
			}
		} 
		else 
			return "";
	
	}

}

/*
$Log: DownloadTableTreeLabelProviderBasic.java,v $
Revision 1.1  2003/09/20 14:39:21  zet
move transfer package

Revision 1.6  2003/09/18 14:11:01  zet
revert

Revision 1.4  2003/09/14 03:37:43  zet
changedProperties

Revision 1.3  2003/09/13 22:26:44  zet
weak sets & !rawrate

Revision 1.2  2003/08/31 01:46:33  zet
localise

Revision 1.1  2003/08/23 19:44:12  zet
split transfer table to basic/advanced modes


*/

