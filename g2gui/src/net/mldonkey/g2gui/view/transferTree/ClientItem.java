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
import net.mldonkey.g2gui.model.enum.EnumClientMode;
import net.mldonkey.g2gui.model.enum.EnumState;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTreeEditor;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * ClientItem
 *
 * @author $user$
 * @version $Id: ClientItem.java,v 1.19 2003/07/20 12:35:07 dek Exp $ 
 *
 */
public class ClientItem extends TableTreeItem implements IItemHasMenue {
	
	private Menu menu;

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
	
	private TableTreeEditor editor;

	/**
	 * @param parent for wich downloaditem this object delivers detailed infos
	 * @param style the style, in which it appears (see TableTreeItem for details)
	 * @param clientInfo the source of the Information
	 */
	public ClientItem( DownloadItem parent, int style, ClientInfo clientInfo ) {
		super( parent, style );
		this.clientInfo = clientInfo;
		this.downloadItem = parent;
		this.fileInfo = downloadItem.getFileInfo();
		this.downloadItem = parent;
		editor = new TableTreeEditor( this.getParent() );		
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		Control oldEditor = editor.getEditor();
			if ( oldEditor != null )
			oldEditor.dispose();
		this.chunks = new ChunkView( this.getParent().getTable(), SWT.NONE, clientInfo, fileInfo, 6 );
		editor.setEditor ( chunks, this, 4 );
		updateCell( 4, "" );
		updateCell( 2, clientInfo.getClientName() );
		updateCell( 3, "" );		
		updateCell( 6, "rank: unknown" );		
		updateColums();	
		addDisposeListener( new DisposeListener() {
			public void widgetDisposed( DisposeEvent e ) {				
				chunks.dispose();
				// resetting the chunk-bar editor is a must 
				editor.setEditor( null );	
			} } );
		
		
	}
	private int numberCompleteChunks( ClientInfo clientInfo ) {
		int result = 0;
		int length = 0;
		String avail = clientInfo.getFileAvailability( fileInfo );
		if ( avail != null ) length = avail.length();
		for ( int i = 0; i < length; i++ ) {		
			//this availability is so low, we can assume, it is not available:			
			if ( avail.charAt( i ) == '0' ) {
			}
			else if ( avail.charAt( i ) == '1' ) {
				result++;		
			}
			else if ( avail.charAt( i ) == '2' ) {
				result++;				
			}
		}
		return result;
	}
	
	/**
	 * Sets the text of the specific Cell in this row and redraw it
	 * @param column the column, in which this cell lives
	 * @param text what do we want do display in this cell
	 */
	private void updateCell( int column, String text ) {
		setText( column, text );		
		int x = getBounds( column ).x;
		int y = getBounds( column ).y;
		int width = getBounds( column ).width;
		int height = getBounds( column ).height;		
		getParent().redraw( x, y, width, height, true );	
	}
	
	/**
	 * updates the whole row, in which this item is displayed
	 */
	private void updateColums() {
		//"ID"|"Network"|"Filename"|"Rate"|"Chunks"|"%"|"Downloaded"|"Size"
		//  0      1         2         3       4     5       6          7
		String connection = "";
		if ( clientInfo.getClientKind().getClientMode() == EnumClientMode.FIREWALLED ) 
			connection = "firewalled";			
		else
			connection = "direct";
		
		if ( !getText( 3 ).equals( connection ) )
			updateCell( 3, connection );
		
		if ( !getText( 6 ).equals( "rank: " + clientInfo.getState().getRank() )
			&& !( clientInfo.getState().getState() == EnumState.CONNECTED_DOWNLOADING ) ) 
				updateCell( 6, "rank: " + clientInfo.getState().getRank() );		
		else if ( clientInfo.getState().getState() == EnumState.CONNECTED_DOWNLOADING )
			updateCell( 6, "transfering" );	
			
		chunks.refresh();
	}


	void update() {
		updateColums();
	}


	/** (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.transferTree.IItemHasMenue#createMenu(org.eclipse.swt.widgets.Menu)
	 */
	public void createMenu( Menu menu ) {
		MenuItem menuItem;
		menuItem = new MenuItem( menu, SWT.PUSH );
		menuItem.setText( "Nothing to do here..." );		
		menuItem.setEnabled( false );
	}
	/**
	 * @return the clientInfo behind this Item
	 */
	public ClientInfo getClientInfo() {
		return clientInfo;
	}

}

/*
$Log: ClientItem.java,v $
Revision 1.19  2003/07/20 12:35:07  dek
saving some CPU time, when only sorting clientItems of expanded DownloadItems

Revision 1.18  2003/07/20 12:15:47  dek
another flickerTest

Revision 1.17  2003/07/20 11:47:04  dek
foobar

Revision 1.16  2003/07/20 11:06:36  dek
still don't know, where flickering comes from (tested with CRT)

Revision 1.15  2003/07/20 10:31:21  dek
done some work on flickering & sorting

Revision 1.14  2003/07/18 09:43:15  dek
never use * / (without space) in CVS-commit-comments......

Revision 1.13  2003/07/18 09:40:07  dek
finally got rid of the flickering?? dunno (* searching CRT to test*) 

Revision 1.12  2003/07/16 19:39:46  dek
fixed exception when items were expanded after a sort()

Revision 1.11  2003/07/16 18:16:53  dek
another flickering-test

Revision 1.10  2003/07/15 20:52:05  dek
exit-exceptions are gone...

Revision 1.9  2003/07/15 20:42:48  dek
*** empty log message ***

Revision 1.8  2003/07/15 20:17:27  dek
ok, sorting with opened chunkbars works also now, forgot disposeListener()

Revision 1.7  2003/07/15 18:14:47  dek
Wow, nice piece of work already done, it works, looks nice, but still lots of things to do

Revision 1.6  2003/07/15 14:43:30  dek
*** empty log message ***

Revision 1.5  2003/07/15 13:25:41  dek
right-mouse menu and some action to hopefully avoid flickering table

Revision 1.4  2003/07/14 19:26:40  dek
done some clean.up work, since it seems,as if this view becomes reality..

Revision 1.3  2003/07/13 12:48:28  dek
chunk-bar begins to work

Revision 1.2  2003/07/12 21:49:36  dek
transferring and queued clients shown

Revision 1.1  2003/07/12 13:50:01  dek
nothing to do, so i do senseless idle-working

*/