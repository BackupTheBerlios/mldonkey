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
 * ( at your option ) any later version.
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



import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;


import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;


import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.FileInfoIntMap;
import net.mldonkey.g2gui.model.enum.EnumFileState;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;



/**
 * DownloadTable
 *
 * @author $user$
 * @version $Id: DownloadTable.java,v 1.10 2003/07/16 18:11:07 dek Exp $ 
 *
 */
public class DownloadTable  implements Observer, Runnable {
	private int lastSortColumn = -1;
	protected IItemHasMenue selectedItem;
	private TransferMain page;
	private FileInfoIntMap files;
	private TableTree tableTree;
	private TIntObjectHashMap downloads;
	
	private String[] columns = {
							"ID",
							"Network",
							"Name",							
							"Rate",
							"Chunks",
							"%",
							"Downloaded",
							"Size"
							};
	/**
	 * 
	 * Creates a new DonwloadTable inside the composite parent, speaking with CoreCommunication
	 * mldonkey
	 * @param parent here, this object will be placed
	 * @param mldonkey this object's master, from which this object gets the data
	 * @param page the TransferPage, where this table is located
	 */
	public DownloadTable( Composite parent, CoreCommunication mldonkey, TransferMain page ) {
		this.page = page;	
		downloads = new TIntObjectHashMap();	
		 tableTree = new TableTree( parent, SWT.FULL_SELECTION );
			Table table = tableTree.getTable();
			table.setLinesVisible( false );
			table.setHeaderVisible( true );	
									
			for ( int i = 0; i < columns.length; i++ ) {
				//"ID"|"Network"|"Filename"|"Rate"|"Chunks"|"%"|"Downloaded"|"Size"
				TableColumn column = new TableColumn( table, SWT.NONE );
							column.setText( columns[ i ] );							
				final int columnIndex = i;
				column.addSelectionListener( new SelectionAdapter() {
					public void widgetSelected( SelectionEvent e ) {
						sort( columnIndex );
					}
				});
			}
			
			table.addMouseListener( new MouseListener () {
				public void mouseDown( MouseEvent e ) {
						Point pt = new Point( e.x, e.y );
						DownloadTable.this.selectedItem = ( IItemHasMenue ) tableTree.getItem( pt );				    	
				}
				public void mouseDoubleClick( MouseEvent e ) { }
				public void mouseUp( MouseEvent e ) { }
			} );
			table.setMenu( createRightMouse() );
			mldonkey.addObserver( this );			
	}



	/**
	 * @param columnIndex
	 */
	protected void sort( int columnIndex ) {
		Object[] items = downloads.getValues();
		FileInfo[] files = new FileInfo[ items.length ];
		int[] expanded = new int[ items.length ];
		
		for ( int i = 0; i < items.length; i++ ) {
			files[ i ] = ( ( DownloadItem ) items[ i ] ).getFileInfo();
			// to save the expanded-status, we save all expanded fileIds:
			expanded[ i ] = -1;
			if ( ( ( DownloadItem ) items[ i ] ).getExpanded() )
				expanded[ i ] = ( ( DownloadItem ) items[ i ] ).getFileInfo().getId();
			//Now dispose the now unneeded Table-tree Entries:
			( ( DownloadItem ) items[ i ] ).dispose();
			downloads.remove( files[ i ].getId() );
		}
		Arrays.sort( files, new FileInfoComparator( columnIndex ) );
		
		if ( lastSortColumn != columnIndex ) {
			for ( int i = 0; i < files.length; i++ ) {
				DownloadItem newItem =
					new DownloadItem( tableTree, SWT.NONE, files[ i ] );
				downloads.put( files[ i ].getId(), newItem );				
			}
			lastSortColumn = columnIndex;
		} else {
			// reverse order if the current column is selected again
			int j = files.length - 1;
			for ( int i = files.length-1; i >=0 ; i-- ) {
				DownloadItem newItem =
					new DownloadItem( tableTree, SWT.NONE, files[ i ] );
				downloads.put( files[ i ].getId(), newItem );
				lastSortColumn = -1;
			}
		}
		// Now expand the previous expanded items:
		for (int i = 0; i < expanded.length; i++) {
			if ( expanded[ i ] != -1 ){			
				( ( DownloadItem )downloads.get( expanded[ i ] ) ).setExpanded( true );
			}
		}
	}
	/**
	 * @return creates a menu for this download-table end returns it
	 */
	private Menu createRightMouse() {
		/*
		 * If a menu is opened, all items are disposed, and the selected TabletreeItem
		 * is called to create a menu, suitable for his needs.
		 * If no item has been selected, a failsafe Menue is shown, saying, that
		 * no item has been selected 
		 */
		Shell shell = tableTree.getShell();
		final Menu menu = new Menu( shell, SWT.POP_UP );		
		menu.addListener( SWT.Show, new Listener() {
			public void handleEvent( Event event ) {
				if ( DownloadTable.this.selectedItem != null ) {
					MenuItem[] menuItems = menu.getItems();
					menu.setDefaultItem( null );
					for ( int i = 0; i < menuItems.length; i++ ) {
						menuItems[i].dispose();
					}
					DownloadTable.this.selectedItem.createMenu( menu );
				}
				else {
					MenuItem[] menuItems = menu.getItems();
					menu.setDefaultItem( null );
					for ( int i = 0; i < menuItems.length; i++ ) {
						menuItems[i].dispose();
					}
					MenuItem menuItem = new MenuItem( menu, SWT.PUSH );
					menuItem.setText( " No entry selected " );
					menuItem.setEnabled( false );					
				}
			}
		} );
		return menu;
	}



	/** ( non-Javadoc )
	 * @see java.util.Observer#update( java.util.Observable, java.lang.Object )
	 */
	public void update( Observable o, Object arg ) {
		if ( arg instanceof FileInfoIntMap ) {
			files = ( FileInfoIntMap ) arg;
			if ( page.isActive() ) {
				tableTree.getDisplay().syncExec( this );
			}
		}
	}



	/** ( non-Javadoc )u
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
			TIntObjectIterator it = files.iterator();
			while ( it.hasNext() ) {
				it.advance();
				FileInfo fileInfo = ( FileInfo ) it.value();
				/* only process downloading and paused files
				 * remove canceled files from table*/
				if ( fileInfo.getState().getState() == EnumFileState.DOWNLOADING 
					|| fileInfo.getState().getState() == EnumFileState.PAUSED ) 
					{					
					if ( downloads.containsKey( fileInfo.getId() ) ) {
						downloads.get( fileInfo.getId() );
						DownloadItem existingItem =
									( DownloadItem ) downloads.get( fileInfo.getId() );
						existingItem.update();
					}
					else {					
					DownloadItem newItem =
						new DownloadItem( tableTree, SWT.NONE, fileInfo );
					downloads.put( fileInfo.getId(), newItem );
					TableColumn[] cols = tableTree.getTable().getColumns();
					
						for ( int i = 0; i < cols.length; i++ ) {
							cols[i].pack();
						}				
					}	
				}	
				else if ( downloads.containsKey( fileInfo.getId() ) ) {	
					/* remove this file from the downloadList if contained*/					
						( ( DownloadItem ) downloads.get( fileInfo.getId() ) ).dispose();
						downloads.remove( fileInfo.getId() );												
					
				}
				else {
				 /* we really don't care about this one...*/
				}
		}		
	}
}
/*
$Log: DownloadTable.java,v $
Revision 1.10  2003/07/16 18:11:07  dek
returned to prev. version

Revision 1.8  2003/07/15 20:13:56  dek
sorting works now, chunk-display is kind of broken, when sorting with expanded tree-items...

Revision 1.7  2003/07/15 18:14:47  dek
Wow, nice piece of work already done, it works, looks nice, but still lots of things to do

Revision 1.6  2003/07/15 13:25:41  dek
right-mouse menu and some action to hopefully avoid flickering table

Revision 1.5  2003/07/14 20:13:39  dek
now full-row selection, with System.out.println'ing item, on which right-mous-button was clicked

Revision 1.4  2003/07/14 19:26:40  dek
done some clean.up work, since it seems,as if this view becomes reality..

Revision 1.3  2003/07/13 20:12:39  dek
fixed Exception and applied checkstyle

Revision 1.2  2003/07/12 13:50:01  dek
nothing to do, so i do senseless idle-working

Revision 1.1  2003/07/11 17:53:51  dek
Tree for Downloads - still unstable

*/