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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * Transfertab
 *
 * @author $user$
 * @version $Id: TransferTab.java,v 1.2 2003/06/25 00:58:23 lemmstercvs01 Exp $ 
 *
 */
public class TransferTab extends G2guiTab implements InterFaceUI {

	private Table table;

	/**
	 * @param gui gui the parent Gui
	 */
	public TransferTab( IG2gui gui ) {
		super( gui );
		this.button.setText( "Transfer" );
		createContents( this.content );

		this.registerListener( Main.getMldonkey() );
	}

	/**
	 * Create the content of this Tab
	 * @param content The Composite to display in
	 */
	protected void createContents( Composite content ) {
		table = new Table( content, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI );
		table.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		table.setLinesVisible( false );
		table.setHeaderVisible( true );
		
		String[] titles = { "ID", "Filename", "Rate", "Downloaded", "Size", };
		for ( int i = 0; i < titles.length; i++ ) {
			TableColumn column = new TableColumn( table, SWT.NULL );
			column.setText( titles [ i ] );
		}
		
		// Row in table 
		for ( int i = 0; i < titles.length; i++ ) {
			table.getColumn( i ).pack();
		}

		table.setSize( table.computeSize( 750, 500 ) );
	}
	
	/**
	 * Receive a notification about a change
	 * @param anInformation The Information which has changed
	 */
	public void notify( final Information anInformation ) {
		if ( anInformation instanceof FileInfoIntMap ) {
			table.getDisplay().syncExec( new Runnable () {
				public void run() {
					if ( table.isDisposed () ) return;

					int tablesize = table.getItemCount();
					TableItem[] items = table.getItems();

					TIntObjectIterator itr = ( ( InfoIntMap ) anInformation ).iterator();
					int collsize = ( ( InfoIntMap ) anInformation ).size();

					for ( ; collsize-- > 0;) {
						itr.advance();     
						FileInfo elem = ( FileInfo ) itr.value();
						
						TableItem anItem = getItemByFileInfo( table, elem );
						
						/* FileInfo not yet in the table */
						if ( anItem == null ) {
							TableItem item = new TableItem ( table, SWT.NULL );
							item.setText( 0, new Integer( itr.key() ).toString() );
							item.setText( 1, elem.getName() );
							item.setText( 2, new Float( Math.round( elem.getRate() / 1024 ) ).toString() );
							item.setText( 3, new Integer( elem.getDownloaded() ).toString() );
							item.setText( 4, new Integer( elem.getSize() ).toString() );
							item.setData( elem );
						}
						/* FileInfo already in the table, check for updates */
						else {
							if ( ! ( anItem.getText( 2 ).equals( new Float( Math.round( elem.getRate() / 1024 ) ).toString() ) ) ) {
								anItem.setText( 2, new Float( elem.getRate() / 1024 ).toString() );
							}
						}
					}
				}
			});
		}
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
	 * Register this object at a CoreCommunication
	 * @param mldonkey The CoreCommunication where to register
	 */
	public void registerListener( CoreCommunication mldonkey ) {
		mldonkey.registerListener( this );
	}

}

/*
$Log: TransferTab.java,v $
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