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
package net.mldonkey.g2gui.view;

import gnu.trove.TIntObjectIterator;
import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.FileInfoIntMap;
import net.mldonkey.g2gui.model.InfoIntMap;
import net.mldonkey.g2gui.model.Information;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.view.download.FileInfoTableContentProvider;
import net.mldonkey.g2gui.view.download.FileInfoTableLabelProvider;
import net.mldonkey.g2gui.view.download.FileInfoTableViewerSorter;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * Transfertab
 *
 * @author $user$
 * @version $Id: TransferTab.java,v 1.4 2003/06/26 09:11:04 lemmstercvs01 Exp $ 
 *
 */
public class TransferTab extends G2guiTab implements InterFaceUI {

	private TableViewer table;

	/**
	 * @param gui gui the parent Gui
	 */
	public TransferTab( IG2gui gui ) {
		super( gui );
		this.button.setText( "Transfer" );
		createContents( this.content );

		this.registerListener( gui.getCore() );
	}

	/**
	 * Create the content of this Tab
	 * @param content The Composite to display in
	 */
	protected void createContents( Composite content ) {
		table = new TableViewer( content, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI );
		
		table.getTable().setLayoutData( new GridData( GridData.FILL_BOTH ) );
		table.getTable().setLinesVisible( false );
		table.getTable().setHeaderVisible( true );

		table.setContentProvider( new FileInfoTableContentProvider() );
		table.setLabelProvider( new FileInfoTableLabelProvider() );
		table.setSorter( new FileInfoTableViewerSorter() );
		
		String[] aString = { "ID", "Name", "Rate", "Downloaded", "Size", "%" };
		int[] anInt = { 25, 300, 40, 70, 70, 40 };
		TableColumn column = null;
		for ( int i = 0; i < aString.length; i++ ) {
			column = new TableColumn(table.getTable(), SWT.LEFT);
			column.setText( aString[ i ] );
			column.setWidth( anInt[ i ] );
		}
	}
	
	/**
	 * Receive a notification about a change
	 * @param anInformation The Information which has changed
	 */
	public void notify( final Information anInformation ) {
		if ( anInformation instanceof FileInfoIntMap )
			table.getTable().getDisplay().syncExec( new Runnable () {
			public void run() {
				
				int tablesize = table.getTable().getItemCount();
				TableItem[] items = table.getTable().getItems();
				TIntObjectIterator itr = ( ( InfoIntMap ) anInformation ).iterator();
				int collsize = ( ( InfoIntMap ) anInformation ).size();
				for ( ; collsize-- > 0;) {
					itr.advance();     
					FileInfo newElem = ( FileInfo ) itr.value();

					/* lets see if the item is already in the table */
					TableItem anItem = getItemByFileInfo( table.getTable(), newElem );
					
					/* FileInfo not yet in the table */
					if ( anItem == null ) {
						createItem( table, newElem );
					}
					/* FileInfo already in the table, check for updates */
					else {
						updateItem( anItem, newElem );
					}
				}
			}
		});
	}
	
	/**
	 * Creates a TableItem from a fileInfo
	 * @param table TableViewer to display on
	 * @param fileInfo FileInfo to use
	 */
	private static void createItem( TableViewer table, FileInfo fileInfo ) {
		TableItem item = new TableItem ( table.getTable(), SWT.NULL );
		item.setText( 0, new Integer( fileInfo.getId() ).toString() );
		item.setText( 1, fileInfo.getName() );
		item.setText( 2, new Float( ( fileInfo.getRate() ) ).toString() );
		item.setText( 3, new Integer( fileInfo.getDownloaded() ).toString() );
		item.setText( 4, new Integer( fileInfo.getSize() ).toString() );
		item.setText( 5, new Double( fileInfo.getPerc() ).toString() );
		tableItemSetColor( fileInfo, item );
		item.setData( fileInfo );
	}
	
	/**
	 * Refresh the data of a fileInfo
	 * @param item TableItem to refresh
	 * @param fileInfo FileInfo to use
	 */
	private static void updateItem( TableItem item, FileInfo fileInfo ) {
		/* update "rate" and "downloaded" */
		if ( ! ( item.getText( 2 ).equals( new Float( fileInfo.getRate() ).toString() )  )) {
			item.setText( 2, new Float( fileInfo.getRate() ).toString() );
			item.setText( 3, new Integer( fileInfo.getDownloaded() ).toString() );
		}
		/* update "downloaded" */
		else if ( ! ( item.getText( 3 ).equals( new Integer( fileInfo.getDownloaded() ).toString() ) ) ) {
			item.setText( 3, new Integer( fileInfo.getDownloaded() ).toString() );
			item.setText( 5, new Double( fileInfo.getPerc() ).toString() );
		}
		item.setData( fileInfo );
	}

	/**
	 * Checks if the table contains an id
	 * @param id
	 * @return
	 */
	private static TableItem getItemByFileInfo( Table table, FileInfo fileInfo ) {
		TableItem[] items = table.getItems();
	
		for ( int i = 0; i < items.length; i++ ) {
			if ( ( ( FileInfo ) items[ i ].getData() ).equals( fileInfo ) )
				return items[ i ];
		}
		return null;
	}
	
	/**
	 * Sets the color of a TableItem
	 * @param newElem FileInfo to use
	 * @param item TableItem to set color on
	 */	
	private static void tableItemSetColor( FileInfo newElem, TableItem item ) {
		if ( newElem.getState().getState() == EnumFileState.ABORTED )
			item.setForeground( new Color( null, 160, 7, 4 ) );
		else if ( newElem.getState().getState() == EnumFileState.CANCELLED )
			item.setForeground( new Color( null, 160, 7, 4 ) );
		else if ( newElem.getState().getState() == EnumFileState.DOWNLOADED )
			item.setForeground( new Color( null, 160, 133, 45 ) );
		else if ( newElem.getState().getState() == EnumFileState.DOWNLOADING )
			item.setForeground( new Color( null, 160, 7, 4 ) );
		else if ( newElem.getState().getState() == EnumFileState.NEW )
			item.setForeground( new Color( null, 160, 7, 4 ) );
		else if ( newElem.getState().getState() == EnumFileState.PAUSED )
			item.setForeground( new Color( null, 160, 7, 4 ) );
		else if ( newElem.getState().getState() == EnumFileState.QUEUED )
			item.setForeground( new Color( null, 1, 70, 160 ) );
		else if ( newElem.getState().getState() == EnumFileState.SHARED )
			item.setForeground( new Color( null, 160, 7, 4 ) );
	}
	
	/**
	 * Register this object at a CoreCommunication
	 * @param mldonkey The CoreCommunication where to register
	 */
	public void registerListener( CoreCommunication mldonkey ) {
		mldonkey.registerListener( this );
	}
}

/*
$Log: TransferTab.java,v $
Revision 1.4  2003/06/26 09:11:04  lemmstercvs01
added percent

Revision 1.3  2003/06/25 18:35:36  lemmstercvs01
not nice, but working

Revision 1.2  2003/06/25 00:58:23  lemmstercvs01
no flickering anymore, next step jface TableViewer

Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.3  2003/06/24 20:13:36  lemmstercvs01
added a real content

Revision 1.2  2003/06/24 19:18:45  dek
checkstyle apllied

Revision 1.1  2003/06/24 18:25:43  dek
working on main gui, without any connection to mldonkey atm, but the princip works
test with:
public class guitest{
	public static void main(String[] args) {
	Gui g2gui = new Gui(null);
}

*/