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

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.FileInfoIntMap;
import net.mldonkey.g2gui.view.download.FileInfoTableContentProvider;
import net.mldonkey.g2gui.view.download.FileInfoTableLabelProvider;
import net.mldonkey.g2gui.view.download.RowComparator;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;


/**
 * Transfertab
 *
 * @author $user$
 * @version $Id: TransferTab.java,v 1.13 2003/07/02 11:21:59 dek Exp $ 
 *
 */
public class TransferTab extends G2guiTab implements Observer {
	private static ResourceBundle res = ResourceBundle.getBundle("g2gui");
	private TableViewer table;
	private TableItem item;
	private int lastSortColumn = -1;

	/**
	 * @param gui gui the parent Gui
	 */
	public TransferTab( IG2gui gui ) {
		super( gui );
		toolItem.setText( res.getString( "TT_Button" ) );
		toolItem.setImage(Gui.createTransparentImage( 
							new Image(toolItem.getParent().getDisplay(),
									"src/icons/transfer2.png" ),
									toolItem.getParent() ) );
		createContents( this.content );

		gui.getCore().addObserver( this );
	}

	/**
	 * Create the content of this Tab
	 * @param content The Composite to display in
	 */
	protected void createContents( Composite content ) {
		/* create a SashFrom containing the two tables */
		SashForm sashForm = new SashForm(content, SWT.NULL | SWT.VERTICAL);
		
		/* create the first table */
		table = new TableViewer( sashForm, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI );
		table.getTable().setLayoutData( new GridData( GridData.FILL_BOTH ) );
		table.getTable().setLinesVisible( false );
		table.getTable().setHeaderVisible( true );
		table.getTable().setMenu( createRightMouse() );

		table.setContentProvider( new FileInfoTableContentProvider() );
		table.setLabelProvider( new FileInfoTableLabelProvider() );

		/* create the headers and set the width */		
		String[] aString = { res.getString( "TT_Field0" ),
						 	 res.getString( "TT_Field1" ),
				   			 res.getString( "TT_Field2" ),
							 res.getString( "TT_Field3" ),
							 res.getString( "TT_Field4" ),
 							 res.getString( "TT_Field5" ),
 							 res.getString( "TT_Field6" ) };

		int[] anInt = { 25, 300, 40, 70, 70, 40, 70 };
		TableColumn column = null;
		for ( int i = 0; i < aString.length; i++ ) {
			column = new TableColumn( table.getTable(), SWT.LEFT );
			column.setText( aString[ i ] );
			column.setWidth( anInt[ i ] );
			
			/* listen for table sorting */
			final int columnIndex = i;
			column.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent e ) {
					sort( columnIndex );
				}
			});
		}
		
		/* listen for right mouse click on item */
		table.getTable().addMouseListener( new MouseListener () {
			public void mouseDown( MouseEvent e ) {
				if ( e.button == 3 ) {
					Point pt = new Point( e.x, e.y );
					item = table.getTable().getItem( pt );
				}
			}
			public void mouseDoubleClick(MouseEvent e) {}
			public void mouseUp(MouseEvent e) {}
		});
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, final Object arg) {
		if ( arg instanceof FileInfoIntMap )
			table.getTable().getDisplay().asyncExec( new Runnable() {
				public void run() {
					FileInfoIntMap fileInfo = ( FileInfoIntMap ) arg;
					int tableCount = table.getTable().getItemCount();
					if ( tableCount == 0 ) 
						table.setInput( arg );
					else if ( tableCount != fileInfo.size() ) {
						table.refresh();
					}
					else if ( fileInfo.contains( fileInfo.getId() ) ) {
						table.update( fileInfo.get( fileInfo.getId() ), null );
					}
				}
			});			
	}
	
	/**
	 * Creates a right mouse click menu
	 * @return Menu
	 */
	private Menu createRightMouse() {
		Shell shell = table.getTable().getShell();
		Menu menu = new Menu( shell , SWT.POP_UP );
		
		MenuItem menuItem;

		/* Pause */
		menuItem = new MenuItem( menu, SWT.PUSH );
		menuItem.setText( res.getString( "TT_Menu0" ) );
		menuItem.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
			}
		});

		/* Resume */
		menuItem = new MenuItem( menu, SWT.PUSH );
		menuItem.setText( res.getString( "TT_Menu1" ) );
		menuItem.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
			}
		});

		/* Cancel */
		menuItem = new MenuItem( menu, SWT.PUSH );
		menuItem.setText( res.getString( "TT_Menu2" ) );
		menuItem.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
			}
		});

		new MenuItem( menu, SWT.SEPARATOR );	

		/* Rename to */
		menuItem = new MenuItem( menu, SWT.CASCADE );
		menuItem.setText( res.getString( "TT_Menu3" ) );
			final Menu rename = new Menu( menu );
		menuItem.setMenu( rename );				

		/* Copy to */
		menuItem = new MenuItem( menu, SWT.CASCADE );
		menuItem.setText( res.getString( "TT_Menu4" ) );
			Menu copy = new Menu( menu );
			MenuItem copyItem = new MenuItem( copy, SWT.PUSH );
			copyItem.setText( "" );
			copyItem.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent e ) {
				}
			});
		menuItem.setMenu( copy );	

		/* modify the menu when selected */
		menu.addListener( SWT.Show, new Listener () {
			/* create submenu for "Rename to" */
			public void handleEvent( Event event ) {
				/* remove the old items */
				MenuItem[] renameMenu = rename.getItems();
				for ( int i = 0; i < renameMenu.length; i++ ) {
					renameMenu[ i ].dispose();
				}
				/* create a menuItem for all names in fileinfo */
				MenuItem renameItem = null;
				FileInfo fileInfo = ( ( FileInfo ) item.getData() );
				String[] names = fileInfo.getNames();
				for ( int i = 0; i < names.length; i++ ) {
					renameItem = new MenuItem( rename, SWT.PUSH );
					renameItem.setText( names[ i ] );
					/* add listener to name choice */
					renameItem.addSelectionListener( new SelectionAdapter() {
						public void widgetSelected( SelectionEvent e ) {
						}
					});
				}
			}
		});
		// TODO add events for rightClick menu
		return menu;
	}
	
	/**
	 * 
	 * @param column
	 */
	private void sort(int column) {
		if( table.getTable().getItemCount() <= 1 ) return;
			TableItem[] items = table.getTable().getItems();
			String[][] data = new String[ items.length ][ table.getTable().getColumnCount() ];
			for ( int i = 0; i < items.length; i++ )
				for ( int j = 0; j < table.getTable().getColumnCount(); j++ )
					data[ i ][ j ] = items[ i ].getText(j);

			Arrays.sort( data, new RowComparator( column ) );

			if ( lastSortColumn != column ) {
				for ( int i = 0; i < data.length; i++ )
					items[ i ].setText( data[ i ] );
				lastSortColumn = column;
			}
			else {
			// reverse order if the current column is selected again
			int j = data.length -1;
			for ( int i = 0; i < data.length; i++ )
				items[ i ].setText( data[ j-- ] );
			lastSortColumn = -1;
			}	
	}	
}

/*
$Log: TransferTab.java,v $
Revision 1.13  2003/07/02 11:21:59  dek
transfer Icon

Revision 1.12  2003/06/30 21:39:04  dek
CoolBar

Revision 1.11  2003/06/30 07:32:24  lemmstercvs01
sorting of columns added (buggy)

Revision 1.10  2003/06/27 22:04:45  lemmstercvs01
ResourceBundle introduced

Revision 1.9  2003/06/27 21:45:54  lemmstercvs01
added right click menu

Revision 1.8  2003/06/27 17:40:19  lemmstercvs01
foobar

Revision 1.7  2003/06/27 11:07:52  lemmstercvs01
CoreCommunications implements addObserver(Observer)

Revision 1.6  2003/06/27 10:36:17  lemmstercvs01
changed notify to observer/observable

Revision 1.5  2003/06/26 21:54:53  lemmstercvs01
sorting by columns works, but inteference with updateItem()

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