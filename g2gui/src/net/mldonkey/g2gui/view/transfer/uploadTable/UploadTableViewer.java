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
import gnu.trove.TIntObjectIterator;

import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.SharedFileInfo;
import net.mldonkey.g2gui.model.SharedFileInfoIntMap;
import net.mldonkey.g2gui.view.TransferTab;
import net.mldonkey.g2gui.view.helper.OurTableSorter;
import net.mldonkey.g2gui.view.helper.OurTableViewer;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
/**
 * UploadTableViewer
 *
 * @version $Id: UploadTableViewer.java,v 1.13 2003/10/21 17:00:45 lemmster Exp $ 
 *
 */
public class UploadTableViewer extends OurTableViewer {
	private SharedFileInfoIntMap sharedFileInfoIntMap;
	
	/**
	 * @param parent the place where this table lives
	 * @param mldonkey the source of our data
	 * @param tab We live on this tab
	 */
	public UploadTableViewer( Composite aComposite, CoreCommunication aCore, TransferTab tab ) {
		super( aComposite, aCore );

		this.tableColumns = new String[] { "TT_UPLOAD_NETWORK", "TT_UPLOAD_UPLOAD", "TT_UPLOAD_QUERIES","TT_UPLOAD_NAME" };
		this.tableWidth = new int[] { 50, 50, 50, 50 };
		this.tableAlign = new int[] { SWT.LEFT, SWT.RIGHT, SWT.RIGHT, SWT.LEFT };
 
		this.swtLayout = SWT.FULL_SELECTION;
		this.contentProvider = new UploadContentProvider();
		this.labelProvider = new UploadLabelProvider();
		this.tableSorter = new UploadTableSorter();
		this.menuListener = new UploadTableMenuListener( core );

		this.sharedFileInfoIntMap = core.getSharedFileInfoIntMap();
		this.create();		
	}
	
	/**
	 * @param parent
	 */
	protected void create() {
		super.create();

		this.createTableColumns();
	
		this.setInput( this.sharedFileInfoIntMap );

		updateDisplay();
	}
	/**
	 * refreshes the Display with values from preference-Store.
	 * This method is called, when preference-Sotre is closed, that means, something
	 * might have benn changed.
	 */
	public void updateDisplay() {
		getTableViewer().getTable().setLinesVisible(
						PreferenceLoader.loadBoolean( "displayGridLines" ) );
		
	}
	
	public class UploadContentProvider implements IStructuredContentProvider, Observer {
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
			if ( getTableViewer().getTable().isDisposed() )
				return;
			getTableViewer().getTable().getDisplay().asyncExec( new Runnable() {
				public void run() {
					if ( getTableViewer().getTable().isDisposed() ) return;
		
					synchronized ( sharedFileInfoIntMap.getRemoved() ) {
						getTableViewer().remove( sharedFileInfoIntMap.getRemoved().toArray() );
						sharedFileInfoIntMap.clearRemoved();
					}
					synchronized ( sharedFileInfoIntMap.getAdded() ) {
						getTableViewer().add( sharedFileInfoIntMap.getAdded().toArray() );
						sharedFileInfoIntMap.clearAdded();
					}
					synchronized ( sharedFileInfoIntMap.getUpdated() ) {
						getTableViewer().update( sharedFileInfoIntMap.getUpdated().toArray(), null );
						sharedFileInfoIntMap.clearUpdated();
					}				
				}
			} );
		}
	}
	
	public class UploadLabelProvider implements ITableLabelProvider {

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
	public class UploadTableSorter extends OurTableSorter {
		/**
		 * @param aSorterName
		 * @param aColumnIndex
		 */
		public UploadTableSorter() {
			super( "UploadSorter", 8 );
		}
		
		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
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
					result = compareStrings( aString1, aString2 );
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
					aString1 = sharedFile1.getName();
					aString2 = sharedFile2.getName();
					result = compareStrings( aString1, aString2 );
					break;						
					
				default :
					break;
			}
			return result;
			
		}
	}
}
/*
$Log: UploadTableViewer.java,v $
Revision 1.13  2003/10/21 17:00:45  lemmster
class hierarchy for tableviewer

Revision 1.12  2003/10/16 20:56:50  zet
save column widths

Revision 1.11  2003/09/27 13:30:22  dek
all tables have now show-Gridlines-behaviour as descibed in  preferences

Revision 1.10  2003/09/27 12:30:40  dek
upload-Table has now same show-Gridlines-behaviour as download-Table

Revision 1.9  2003/09/27 00:02:37  dek
bugfixes, merged right-mouse-click menues (nothing is uglier than one-item-menues)

Revision 1.8  2003/09/26 17:03:26  zet
remove unneeded String result

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
