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
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
/**
 * UploadTableViewer
 *
 * @version $Id: UploadTableViewer.java,v 1.1 2003/09/25 18:28:07 dek Exp $ 
 *
 */
public class UploadTableViewer {
	private Shell shell;
	private CoreCommunication mldonkey;
	private Table table;
	private final String[] COLUMN_LABELS =
		{ "Network", "Filename", "Uploaded Bytes", "# of Queries", };
	private TableViewer tableviewer;
	
	/**
	 * @param parent the place where this table lives
	 * @param mldonkey the source of our data
	 * @param tab We live on this tab
	 */
	public UploadTableViewer( Composite parent, CoreCommunication mldonkey, TransferTab tab ) {
		this.shell = parent.getShell();
		this.mldonkey = mldonkey;
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
			TableColumn tableColumn = new TableColumn( table, SWT.NONE );
			tableColumn.setText( COLUMN_LABELS[ i ] );
			tableColumn.setData( COLUMN_LABELS[ i ] );
			tableColumn.setWidth( 80 );
		}
		tableviewer.setContentProvider( new MyContentProvider() );
		tableviewer.setLabelProvider( new MyLabelProvider() );
		tableviewer.setInput( mldonkey.getSharedFileInfoList() );
	}
	private class MyContentProvider implements IStructuredContentProvider, Observer {
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
		/* ( non-Javadoc )
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() { }
		/* ( non-Javadoc )
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged( org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object )
		 */
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
		/* ( non-Javadoc )
		 * @see java.util.Observer#update( java.util.Observable, java.lang.Object )
		 */
		public void update( Observable arg0, final Object arg1 ) {
			if ( tableviewer.getTable().isDisposed() )
				return;
			tableviewer.getTable().getDisplay().asyncExec( new Runnable() {
				public void run() {
					tableviewer.refresh();
				}
			} );
		}
	}
	private class MyLabelProvider implements ITableLabelProvider {
		/* ( non-Javadoc )
		 * @see org.eclipse.jface.viewers.ILabelProvider#getImage( java.lang.Object )
		 */
		public Image getImage( Object element ) {
			System.out.println( "LP: get Image" );
			return null;
		}
		/* ( non-Javadoc )
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener( org.eclipse.jface.viewers.ILabelProviderListener )
		 */
		public void addListener( ILabelProviderListener listener ) { }
		/* ( non-Javadoc )
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
		 */
		public void dispose() { }
		/* ( non-Javadoc )
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty( java.lang.Object, java.lang.String )
		 */
		public boolean isLabelProperty( Object element, String property ) {
			System.out.println( "isLabelProperty" );
			return false;
		}
		/* ( non-Javadoc )
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener( org.eclipse.jface.viewers.ILabelProviderListener )
		 */
		public void removeListener( ILabelProviderListener listener ) { }
		/* ( non-Javadoc )
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage( java.lang.Object, int )
		 */
		public Image getColumnImage( Object element, int columnIndex ) {
			return null;
		}
		/* ( non-Javadoc )
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText( java.lang.Object, int )
		 */
		public String getColumnText( Object element, int columnIndex ) {
			SharedFileInfo info = ( SharedFileInfo ) element;
			String result = "";
			switch ( columnIndex ) {
				case 1 :
					result = info.getSharedFileName();
					break;
				case 2 :
					result = String.valueOf( info.getNumOfBytesUploaded() );
					break;
				case 3 :
					result = String.valueOf( info.getNumOfQueriesForFile() );
					break;
				case 0 :
					result = String.valueOf( info.getNetwork().getNetworkName() );
					break;
				default :
					result = "";
					break;
			}
			return result;
		}
	}
}
/*
$Log: UploadTableViewer.java,v $
Revision 1.1  2003/09/25 18:28:07  dek
first sketch of upload-Table not yet added to transferTab.

*/
