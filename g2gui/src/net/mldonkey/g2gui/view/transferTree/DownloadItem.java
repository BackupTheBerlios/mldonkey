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

import java.util.HashSet;
import java.util.Iterator;

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.enum.EnumState;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.custom.TableTreeItem;

/**
 * DownloadItem
 *
 * @author $user$
 * @version $Id: DownloadItem.java,v 1.4 2003/07/12 21:49:36 dek Exp $ 
 *
 */
public class DownloadItem extends TableTreeItem {
	private TableTree tableTree;

	private TIntObjectHashMap namedclients = new TIntObjectHashMap();

	private FileInfo fileInfo;

	/**
	 * @param tableTree
	 * @param i
	 * @param fileInfo
	 */
	public DownloadItem(TableTree parent, int style, FileInfo fileInfo) {
		super(parent, style);
		this.tableTree = parent;
		this.fileInfo = fileInfo;		
		setText( 0, String.valueOf( fileInfo.getId() ) );
		setText( 1, fileInfo.getName() );
		setText( 2, String.valueOf( fileInfo.getRate() ) );
		setText( 3, String.valueOf( fileInfo.getDownloaded() ) );
		setText( 4, String.valueOf( fileInfo.getSize() ) );
		setText( 5, String.valueOf( fileInfo.getPerc() ) );
		setText( 6, String.valueOf( fileInfo.getState() ) );
		
//		Iterator it = fileInfo.getClientInfos().iterator();
//			while ( it.hasNext() ) {		
//				ClientInfo clientInfo = ( ClientInfo ) it.next();
		Object[] temp = fileInfo.getClientInfos().toArray();
		for (int i = 0; i < temp.length; i++) {	
			ClientInfo clientInfo =  (ClientInfo) temp[ i ];
	//			Here comes the question, wether we want to add this clientInfo, or not?? at the moment,
	//			all clientInfos are accepted
				
					if (( clientInfo.getState().getState() == EnumState.CONNECTED_DOWNLOADING )
						|| ( clientInfo.getState().getState() == EnumState.CONNECTED_AND_QUEUED )	
							 
					){				
							if ( namedclients.containsKey( clientInfo.getClientid() ) ) {
								namedclients.get( clientInfo.getClientid() );
								ClientItem existingItem =
									( ClientItem ) namedclients.get( clientInfo.getClientid() );
								existingItem.update();
							} else {
								ClientItem newItem = new ClientItem( this, SWT.NONE, clientInfo );
								namedclients.put( clientInfo.getClientid(), newItem );
							}
					}
					else if ( namedclients.contains( clientInfo.getClientid() ) ) {
						ClientItem toBeRemovedItem =
									( ClientItem ) namedclients.get( clientInfo.getClientid() );
						toBeRemovedItem.dispose();
						namedclients.remove( clientInfo.getClientid() );
						
					}		
			}

		
	}
	
	void update() {
		setText( 0, String.valueOf( fileInfo.getId() ) );
		setText( 1, fileInfo.getName() );
		setText( 2, String.valueOf( fileInfo.getRate() ) );
		setText( 3, String.valueOf( fileInfo.getDownloaded() ) );
		setText( 4, String.valueOf( fileInfo.getSize() ) );
		setText( 5, String.valueOf( fileInfo.getPerc() ) );
		setText( 6, fileInfo.getChunks() );	
			
		Object[] temp =  fileInfo.getClientInfos().toArray();
		for (int i = 0; i < temp.length; i++) {	
			ClientInfo clientInfo =  (ClientInfo) temp[ i ];
			
		//Iterator it = fileInfo.getClientInfos().iterator();
		//while ( it.hasNext() ) {		
		//ClientInfo clientInfo = ( ClientInfo ) it.next();

		// Here comes the question, wether we want to add this clientInfo, or not?? at the moment,
		// all clientInfos are accepted
						if (( clientInfo.getState().getState() == EnumState.CONNECTED_DOWNLOADING )	
						|| ( clientInfo.getState().getState() == EnumState.CONNECTED_AND_QUEUED )								 
						){				
							if ( namedclients.containsKey( clientInfo.getClientid() ) ) {
								namedclients.get( clientInfo.getClientid() );
								ClientItem existingItem =
									( ClientItem ) namedclients.get( clientInfo.getClientid() );
								existingItem.update();
							} else {
								ClientItem newItem = new ClientItem( this, SWT.NONE, clientInfo );
								namedclients.put( clientInfo.getClientid(), newItem );
							}
					}
					else if ( namedclients.contains( clientInfo.getClientid() ) ) {
						ClientItem toBeRemovedItem =
									( ClientItem ) namedclients.get( clientInfo.getClientid() );
						toBeRemovedItem.dispose();
						namedclients.remove( clientInfo.getClientid() );
					
					}
				
			}
		
	}
	

	
	/**
	 * @param parent
	 * @param style
	 * @param index
	 */
	public DownloadItem(TableTree parent, int style, int index) {
		super(parent, style, index);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param parent
	 * @param style
	 */
	public DownloadItem(TableTreeItem parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param parent
	 * @param style
	 * @param index
	 */
	public DownloadItem(TableTreeItem parent, int style, int index) {
		super(parent, style, index);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return
	 */
	public FileInfo getFileInfo() {
		return fileInfo;
	}

}

/*
$Log: DownloadItem.java,v $
Revision 1.4  2003/07/12 21:49:36  dek
transferring and queued clients shown

Revision 1.3  2003/07/12 16:20:36  dek
*** empty log message ***

Revision 1.2  2003/07/12 13:50:01  dek
nothing to do, so i do senseless idle-working

Revision 1.1  2003/07/11 17:53:51  dek
Tree for Downloads - still unstable

*/