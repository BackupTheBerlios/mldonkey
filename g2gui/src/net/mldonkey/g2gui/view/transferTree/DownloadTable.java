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



import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;


import java.util.Observable;
import java.util.Observer;


import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.FileInfoIntMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;


/**
 * DownloadTable
 *
 * @author $user$
 * @version $Id: DownloadTable.java,v 1.1 2003/07/11 17:53:51 dek Exp $ 
 *
 */
public class DownloadTable  implements Observer,Runnable {
	private FileInfoIntMap files;
	private TableTree tableTree;
	private TIntObjectHashMap downloads;
	
	private String[] columns = {
							"ID",
							"Name",
							"Rate",
							"Downloaded",
							"Size",
							"%",
							"Status",
							};
	/**
	 * 
	 * Creates a new DonwloadTable inside the composite parent, speaking with CoreCommunication
	 * mldonkey
	 * @param parent here, this object will be placed
	 * @param mldonkey this object's master, from which this object gets the data
	 */
	public DownloadTable( Composite parent, CoreCommunication mldonkey ) {	
		downloads = new TIntObjectHashMap();	
		 tableTree = new TableTree(parent,SWT.NONE);
			Table table = tableTree.getTable();
			table.setLinesVisible(true);
			table.setHeaderVisible(true);			
			for (int i = 0; i < columns.length; i++) {
				TableColumn column = new TableColumn(table, SWT.NONE);
							column.setText(columns[ i ]);
			}
			mldonkey.addObserver(this);
	}



	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update( Observable o,  Object arg ) {		
		if ( arg instanceof FileInfoIntMap ){ 		
			files = (FileInfoIntMap) arg;			
			tableTree.getDisplay().asyncExec(this);	}	
		}



	/* (non-Javadoc)u
	 * @see java.lang.Runnable#run()
	 */
	public void run() {			
		
			TIntObjectIterator it = files.iterator();
			while ( it.hasNext() ) {
				it.advance();
				FileInfo fileInfo = ( FileInfo ) it.value();	
				
				
				if( downloads.containsKey( fileInfo.getId() ) ){
					downloads.get( fileInfo.getId());
					DownloadItem existingItem =
								( DownloadItem ) downloads.get( fileInfo.getId() );
					existingItem.update();
				}
				else {					
					DownloadItem newItem =
						new DownloadItem( tableTree, SWT.NONE, fileInfo );
					downloads.put( fileInfo.getId(), newItem );
				TableColumn[] cols = tableTree.getTable().getColumns();
				
				for (int i = 0; i < cols.length; i++) {
					cols[i].pack();
				}
				
				}			
		}
		tableTree.getTable().update();
	}
}
/*
$Log: DownloadTable.java,v $
Revision 1.1  2003/07/11 17:53:51  dek
Tree for Downloads - still unstable

*/