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

import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.model.FileInfoIntMap;
import net.mldonkey.g2gui.view.download.FileInfoTableContentProvider;
import net.mldonkey.g2gui.view.download.FileInfoTableLabelProvider;
import net.mldonkey.g2gui.view.download.FileInfoTableViewerSorter;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;


/**
 * Transfertab
 *
 * @author $user$
 * @version $Id: TransferTab.java,v 1.8 2003/06/27 17:40:19 lemmstercvs01 Exp $ 
 *
 */
public class TransferTab extends G2guiTab implements Observer {
	private TableViewer table;
	private TableViewer table2;
	private int lastSortColumn = -1;

	/**
	 * @param gui gui the parent Gui
	 */
	public TransferTab( IG2gui gui ) {
		super( gui );
		this.button.setText( "Transfer" );
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

		table.setContentProvider( new FileInfoTableContentProvider() );
		table.setLabelProvider( new FileInfoTableLabelProvider() );
		table.setSorter( new FileInfoTableViewerSorter() );

		/* create the headers and set the width */		
		String[] aString = { "ID", "Name", "Rate", "Downloaded", "Size", "%" };
		int[] anInt = { 25, 300, 40, 70, 70, 40 };
		TableColumn column = null;
		for ( int i = 0; i < aString.length; i++ ) {
			column = new TableColumn(table.getTable(), SWT.LEFT);
			column.setText( aString[ i ] );
			column.setWidth( anInt[ i ] );
		}
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
}

/*
$Log: TransferTab.java,v $
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