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
package net.mldonkey.g2gui.view.transfer.uploadTable;
import java.util.Observable;
import java.util.Observer;
import gnu.trove.TIntObjectIterator;
import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.SharedFileInfo;
import net.mldonkey.g2gui.model.SharedFileInfoIntMap;
import net.mldonkey.g2gui.view.TransferTab;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
/**
 * UploadTableViewer
 *
 * @version $Id: UploadTableViewer.java,v 1.7 2003/09/26 17:02:03 zet Exp $ 
 *
 */
public class UploadTableViewer {
	/**
	 * MyTableSorter
	 *
	 * @version $Id: UploadTableViewer.java,v 1.7 2003/09/26 17:02:03 zet Exp $ 
	 *
	 */
	
	private Shell shell;
	private CoreCommunication mldonkey;
	private Table table;
	private final String[] COLUMN_LABELS =
		{ "TT_Download_Network", "TT_UPLOAD_UPLOAD", "TT_UPLOAD_QUERIES","TT_Download_Name" };
	private final int[] COLUMN_ALIGNEMENT =
		{ SWT.LEFT, SWT.RIGHT, SWT.RIGHT, SWT.LEFT };
	private TableViewer tableviewer;
	private boolean ascending = false;
	private SharedFileInfoIntMap sharedFileInfoIntMap;
	
	/**
	 * @param parent the place where this table lives
	 * @param mldonkey the source of our data
	 * @param tab We live on this tab
	 */
	public UploadTableViewer( Composite parent, CoreCommunication mldonkey, TransferTab tab ) {
		this.shell = parent.getShell();
		this.mldonkey = mldonkey;
		this.sharedFileInfoIntMap = mldonkey.getSharedFileInfoIntMap();
		createContent( parent );		
	}
	
	/**
	 * @param parent
	 */
	private void createContent( Composite parent ) {
		tableviewer = new TableViewer( parent, SWT.FULL_SELECTION );
		table = tableviewer.getTable();
		table.setHeaderVisible( true );
		table.setLinesVisible( true );
		for ( int i = 0; i < COLUMN_LABELS.length; i++ ) {
			TableColumn tableColumn = new TableColumn( table, COLUMN_ALIGNEMENT[ i ] );
			tableColumn.setText( G2GuiResources.getString( COLUMN_LABELS[ i ] ) );			
			tableColumn.setWidth( 80 );
			
			final int columnIndex = i;
			tableColumn.addListener( SWT.Selection, new Listener() {
				public void handleEvent( Event e ) {
					/* set the column to sort */
					( ( MyTableSorter ) tableviewer.getSorter() ).setColumnIndex( columnIndex );
					/* set the way to sort (ascending/descending) */
					( ( MyTableSorter ) tableviewer.getSorter() ).setLastSort( ascending );

					/* get the data for all tableitems */
					TableItem[] items = tableviewer.getTable().getItems();
					SharedFileInfo[] temp = new SharedFileInfo[ items.length ];
					for ( int i = 0; i < items.length; i++ )
							temp[ i ] = ( SharedFileInfo ) items[ i ].getData();

					/* reverse sorting way */
					ascending = ascending ? false : true;

					tableviewer.getSorter().sort( tableviewer, temp );
					tableviewer.refresh();
				}	
			} );
			
		}
		
		tableviewer.setContentProvider( new MyContentProvider() );
		tableviewer.setLabelProvider( new MyLabelProvider() );
		tableviewer.setInput( this.sharedFileInfoIntMap );
		tableviewer.setSorter( new MyTableSorter() );
		
		UploadTableMenuListener tableMenuListener = 
								new UploadTableMenuListener( tableviewer, mldonkey );
		tableviewer.addSelectionChangedListener( tableMenuListener );
		MenuManager popupMenu = new MenuManager( "" );
		popupMenu.setRemoveAllWhenShown( true );
		popupMenu.addMenuListener( tableMenuListener );
		tableviewer.getTable().setMenu( popupMenu.createContextMenu( tableviewer.getTable() ) );
		
	}
	
	class MyContentProvider implements IStructuredContentProvider, Observer {
		/* ( non-Javadoc )
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements( java.lang.Object )
		 */
		public Object[] getElements( Object inputElement ) {
			SharedFileInfoIntMap sharedFiles = ( SharedFileInfoIntMap ) inputElement;
			TIntObjectIterator it = sharedFiles.iterator();
			SharedFileInfo[] result = new SharedFileInfo[ sharedFiles.size() ];
			int i = 0;
			while ( it.hasNext() ) {
				it.advance();
				result[ i ] = ( SharedFileInfo ) it.value();
				i++;
			}
			return result;
		}

		public void dispose() { }

		public void inputChanged( Viewer viewer, Object oldInput, Object newInput ) {
			SharedFileInfoIntMap oldI = ( SharedFileInfoIntMap ) oldInput;
			SharedFileInfoIntMap newI = ( SharedFileInfoIntMap ) newInput;
			if ( oldI != null ) {
				oldI.deleteObserver( this );
			}
			if ( newI != null ) {
				newI.addObserver( this );
			}
		}
		
		public void update( Observable arg0, final Object arg1 ) {
			if ( tableviewer.getTable().isDisposed() )
				return;
			tableviewer.getTable().getDisplay().asyncExec( new Runnable() {
				public void run() {
					if ( tableviewer.getTable().isDisposed() ) return;
		
					synchronized ( sharedFileInfoIntMap.getRemoved() ) {
						tableviewer.remove( sharedFileInfoIntMap.getRemoved().toArray() );
						sharedFileInfoIntMap.clearRemoved();
					}
					synchronized ( sharedFileInfoIntMap.getAdded() ) {
						tableviewer.add( sharedFileInfoIntMap.getAdded().toArray() );
						sharedFileInfoIntMap.clearAdded();
					}
					synchronized ( sharedFileInfoIntMap.getUpdated() ) {
						tableviewer.update( sharedFileInfoIntMap.getUpdated().toArray(), null );
						sharedFileInfoIntMap.clearUpdated();
					}				
				}
			} );
		}
	}
	
	class MyLabelProvider implements ITableLabelProvider {

		public void addListener( ILabelProviderListener listener ) { }

		public void dispose() { }

		public boolean isLabelProperty( Object element, String property ) {
			return false;
		}

		public void removeListener( ILabelProviderListener listener ) { }

		public Image getColumnImage( Object element, int columnIndex ) {
			if ( columnIndex == 0 ) {
				SharedFileInfo file = ( SharedFileInfo ) element;
				return G2GuiResources.getNetworkImage( file.getNetwork().getNetworkType() );
			}
			return null;
		}

		public String getColumnText( Object element, int columnIndex ) {
			SharedFileInfo info = ( SharedFileInfo ) element;
			String result = "";
			/*
			 * { "TT_Download_Network", "TT_UPLOAD_UPLOAD", "TT_UPLOAD_QUERIES","TT_Download_Name" };
			 */
			switch ( columnIndex ) {
				case 0 :
					return "" + info.getNetwork().getNetworkName();
				case 1 :
					return "" + info.getUploadedString();
				case 2 :
					return "" + String.valueOf( info.getNumOfQueriesForFile() );
				case 3 :
					return "" + info.getName();
				default :
					return "";
			}
		}
	}
	class MyTableSorter extends ViewerSorter {
		/* set the default sort column to state */
		private int columnIndex = 8;
		/* set the default way to descending */
		private boolean lastSort = true;
		
		/**
		 * Returns a negative, zero, or positive number depending on whether
		 * the first element is less than, equal to, or greater than
		 * the second element.
		 * <p>
		 * The default implementation of this method is based on
		 * comparing the elements' categories as computed by the <code>category</code>
		 * framework method. Elements within the same category are further
		 * subjected to a case insensitive compare of their label strings, either
		 * as computed by the content viewer's label provider, or their
		 * <code>toString</code> values in other cases. Subclasses may override.
		 * </p>
		 *
		 * @param viewer the viewer
		 * @param obj1 the first element
		 * @param obj2 the second element
		 * @return a negative number if the first element is less  than the
		 *  second element; the value <code>0</code> if the first element is
		 *  equal to the second element; and a positive number if the first
		 *  element is greater than the second element
		 */
		public int compare( Viewer viewer, Object obj1, Object obj2 ) {
			SharedFileInfo sharedFile1 = ( SharedFileInfo ) obj1;
			SharedFileInfo sharedFile2 = ( SharedFileInfo ) obj2;
			int result = 0;
			String aString1 = "" ;
			String aString2 = "" ;
			Long aLong1 = new Long( 0 );
			Long aLong2 = new Long( 0 );
			
			switch ( columnIndex ) {				
				case 0 :	/*network*/	
					aString1 = sharedFile1.getNetwork().getNetworkName();
					aString2 = sharedFile2.getNetwork().getNetworkName();
					if ( lastSort )
						result = aString1.compareToIgnoreCase( aString2 );
					else
						result = aString2.compareToIgnoreCase( aString1 );			
					break;				
				case 1 :/*upload*/
					aLong1 = new Long( sharedFile1.getNumOfBytesUploaded() );
					aLong2 = new Long( sharedFile2.getNumOfBytesUploaded() );
					if ( lastSort )
						result = aLong1.compareTo( aLong2 );
					else 
						result = aLong2.compareTo( aLong1 );								
					break;				
				case 2 :/*queries*/	
					aLong1 = new Long( sharedFile1.getNumOfQueriesForFile() );
					aLong2 = new Long( sharedFile2.getNumOfQueriesForFile() );
					if ( lastSort )
						result = aLong1.compareTo( aLong2 );
					else 
						result = aLong2.compareTo( aLong1 );			
					break;
				case 3 : /*filename*/	
					aString1 = sharedFile1.getNetwork().getNetworkName();
					aString2 = sharedFile2.getNetwork().getNetworkName();
					if ( lastSort )
						result = aString1.compareToIgnoreCase( aString2 );
					else
						result = aString2.compareToIgnoreCase( aString1 );					
					break;						
					
				default :
					break;
			}
			return result;
			
		}
		
		/**
		 * Sets the column index
		 * @param i The column index to sort
		 */
		public void setColumnIndex( int i ) {
			columnIndex = i;
		}

		/**
		 * @param i The ascending or descending
		 */
		public void setLastSort( boolean i ) {
			lastSort = i;
		}
	}
}
/*
$Log: UploadTableViewer.java,v $
Revision 1.7  2003/09/26 17:02:03  zet
reorder case statements
return instead of falling through

Revision 1.6  2003/09/26 15:45:59  dek
we now have upload-stats (well, kind of...)

Revision 1.5  2003/09/26 12:25:52  dek
changed refresh() -> update() to avoid flickering table

Revision 1.4  2003/09/26 11:55:48  dek
right-mouse menue for upload-Table

Revision 1.3  2003/09/25 21:50:16  dek
added icons for networks + TableSorter

Revision 1.2  2003/09/25 21:48:55  dek
added icons for networks + TableSorter

Revision 1.1  2003/09/25 18:28:07  dek
first sketch of upload-Table not yet added to transferTab.

*/
