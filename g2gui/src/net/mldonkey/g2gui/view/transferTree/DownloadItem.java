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
import java.util.Set;

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
 * @version $Id: DownloadItem.java,v 1.1 2003/07/11 17:53:51 dek Exp $ 
 *
 */
public class DownloadItem extends TableTreeItem {
	private TIntObjectHashMap namedclients = new TIntObjectHashMap();

	private FileInfo fileInfo;

	/**
	 * @param tableTree
	 * @param i
	 * @param fileInfo
	 */
	public DownloadItem(TableTree parent, int style, FileInfo fileInfo) {
		super(parent, style);
		this.fileInfo = fileInfo;		
		setText( 0, String.valueOf( fileInfo.getId() ) );
		setText( 1, fileInfo.getName() );
		setText( 2, String.valueOf( fileInfo.getRate() ) );
		setText( 3, String.valueOf( fileInfo.getDownloaded() ) );
		setText( 4, String.valueOf( fileInfo.getSize() ) );
		setText( 5, String.valueOf( fileInfo.getPerc() ) );
		setText( 6, String.valueOf( fileInfo.getState() ) );			
		Object[] clients = ((HashSet) fileInfo.getClientInfos()).toArray();		
			for (int i = 0; i < clients.length; i++) {
				ClientInfo info = (ClientInfo) clients[ i ];
						
				if ( info.getState().getState()==EnumState.CONNECTED_DOWNLOADING ){	
								TableTreeItem detailedInfos = new TableTreeItem(this,SWT.NONE);					
								detailedInfos.setText(1,info.getClientName());
								detailedInfos.setText(2,info.getState().toString());
					float rate = ( info.getClientRate()*100/(float)1024 )/100	;
					detailedInfos.setText(2,String.valueOf(rate));	
								namedclients.put( info.getClientid(), detailedInfos );
								}
			}
	}	
	
	void update(){	
		setText( 0, String.valueOf( fileInfo.getId() ) );
		setText( 1, fileInfo.getName() );
		setText( 2, String.valueOf( fileInfo.getRate() ) );
		setText( 3, String.valueOf( fileInfo.getDownloaded() ) );
		setText( 4, String.valueOf( fileInfo.getSize() ) );
		setText( 5, String.valueOf( fileInfo.getPerc() ) );
		setText( 6, String.valueOf( fileInfo.getState() ) );
		
		Object[] clients = ((HashSet) fileInfo.getClientInfos()).toArray();		
			for (int i = 0; i < clients.length; i++) {			
				ClientInfo info = (ClientInfo) clients[ i ];
				
				if ( namedclients.contains( info.getClientid() ) ){
					TableTreeItem detailedInfos = (TableTreeItem) namedclients.get(info.getClientid());
					detailedInfos.setText(1,info.getClientName());
					float rate = ( info.getClientRate()*100/(float)1024 )/100	;
					detailedInfos.setText(2,String.valueOf(rate));	
					
					//detailedInfos.setText(0,String.valueOf(info.getClientid()));
				}			
				
				else if (info.getState().getState()==EnumState.CONNECTED_DOWNLOADING){		
					TableTreeItem detailedInfos = new TableTreeItem(this,SWT.NONE);			
					detailedInfos.setText(1,info.getClientName());	
					float rate = ( info.getClientRate()*100/(float)1024 )/100	;
					detailedInfos.setText(2,String.valueOf(rate));					
					//detailedInfos.setText(0,String.valueOf(info.getClientid()));
					namedclients.put( info.getClientid(), detailedInfos );
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
}

/*
$Log: DownloadItem.java,v $
Revision 1.1  2003/07/11 17:53:51  dek
Tree for Downloads - still unstable

*/