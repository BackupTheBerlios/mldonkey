/*
 * Copyright 2003
 * G2GUI Team
 * 
 * 
 * This file is part of G2GUI.
 *
 * G2GUI is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2GUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2GUI; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.view.transferTree;

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.enum.EnumState;

import org.eclipse.swt.custom.TableTreeItem;

/**
 * ClientItem
 *
 * @author $user$
 * @version $Id: ClientItem.java,v 1.3 2003/07/13 12:48:28 dek Exp $ 
 *
 */
public class ClientItem extends TableTreeItem {
	
	private DownloadItem downloadItem;


	private ClientInfo clientInfo;


	public ClientItem( DownloadItem parent, int style, ClientInfo clientInfo ){
		super( parent, style );
		this.clientInfo = clientInfo;
		this.downloadItem = parent;
		updateColums();	
		
	}
	
	
	/**
	 * 
	 */
	private void updateColums() {
		// setText( 0, String.valueOf( clientInfo.getClientid() ) );
		setText( 1, clientInfo.getClientName() );
		
		String availability = clientInfo.getFileAvailability( downloadItem.getFileInfo() );
		
		String state = "";
		if ( clientInfo.getState().getState() == EnumState.CONNECTED )		
			state = "connected";
		if ( clientInfo.getState().getState() == EnumState.CONNECTED_AND_QUEUED )
			state = "Queued";
		if ( clientInfo.getState().getState() == EnumState.CONNECTED_DOWNLOADING )
			state = "transfering";
		if ( clientInfo.getState().getState() == EnumState.CONNECTED_INITIATING )
			state = "initiating";
		if ( clientInfo.getState().getState() == EnumState.CONNECTING )
			state = "connecting";
		if ( clientInfo.getState().getState() == EnumState.NOT_CONNECTED )
			state = "not connected";
		if ( clientInfo.getState().getState() == EnumState.BLACK_LISTED )
			state = "blackListed";		
		if ( clientInfo.getState().getState() == EnumState.NEW_HOST )
			state = "new host";	
		if ( clientInfo.getState().getState() == EnumState.NOT_CONNECTED_WAS_QUEUED )
			state = "NOT_CONNECTED_WAS_QUEUED";	
		if ( clientInfo.getState().getState() == EnumState.REMOVE_HOST )
			state = "removeHost";				
		if ( availability == null )	availability = "";	
		setText( 3, state );
		setText( 6, availability );		
		
		//setText( 6, String.valueOf( clientInfo.getState() ) );
		if ( !availability.equals( "" ) ) {
		}
	}


	void update() {
		updateColums();
	}
}

/*
$Log: ClientItem.java,v $
Revision 1.3  2003/07/13 12:48:28  dek
chunk-bar begins to work

Revision 1.2  2003/07/12 21:49:36  dek
transferring and queued clients shown

Revision 1.1  2003/07/12 13:50:01  dek
nothing to do, so i do senseless idle-working

*/