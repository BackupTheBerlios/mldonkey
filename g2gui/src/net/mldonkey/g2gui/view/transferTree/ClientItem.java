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
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.enum.EnumState;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTreeEditor;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.widgets.Control;

/**
 * ClientItem
 *
 * @author $user$
 * @version $Id: ClientItem.java,v 1.4 2003/07/14 19:26:40 dek Exp $ 
 *
 */
public class ClientItem extends TableTreeItem {
	
	private ChunkView chunks;

	private DownloadItem downloadItem;
	
	/**
	 * the client, this object is a viewer for
	 */
	private ClientInfo clientInfo;
	
	/**
	 * for which file is this item a ClientItem
	 */
	private FileInfo fileInfo;

	public ClientItem( DownloadItem parent, int style, ClientInfo clientInfo ){
		super( parent, style );
		this.clientInfo = clientInfo;
		this.downloadItem = parent;
		this.fileInfo = downloadItem.getFileInfo();
		this.downloadItem = parent;
		TableTreeEditor editor = new TableTreeEditor( this.getParent() );		
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		Control oldEditor = editor.getEditor();
			if ( oldEditor != null )
			oldEditor.dispose();
		this.chunks = new ChunkView( this.getParent().getTable(), SWT.NONE, clientInfo, fileInfo, 6 );
		editor.setEditor ( chunks, this, 6 );
		
		updateColums();	
		
		
	}
	
	
	/**
	 * 
	 */
	private void updateColums() {
		// setText( 0, String.valueOf( clientInfo.getClientid() ) );
		setText( 1, clientInfo.getClientName() );
		
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
		setText( 3, state );
		String hhhh = clientInfo.getFileAvailability(fileInfo);
		if (hhhh == null) hhhh= "";
		setText(5,hhhh);
		//chunks.refresh();
	}


	void update() {
		updateColums();
	}
}

/*
$Log: ClientItem.java,v $
Revision 1.4  2003/07/14 19:26:40  dek
done some clean.up work, since it seems,as if this view becomes reality..

Revision 1.3  2003/07/13 12:48:28  dek
chunk-bar begins to work

Revision 1.2  2003/07/12 21:49:36  dek
transferring and queued clients shown

Revision 1.1  2003/07/12 13:50:01  dek
nothing to do, so i do senseless idle-working

*/