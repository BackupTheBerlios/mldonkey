/*
 * Copyright 2003
 * g2gui Team
 * 
 * 
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.view.transferTree;

import gnu.trove.TIntObjectIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.FileInfoIntMap;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.MainTab;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTreeEditor;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

/**
 * DownloadTableTreeContentProvider
 *
 * @author $user$
 * @version $Id: DownloadTableTreeContentProvider.java,v 1.9 2003/08/17 16:48:08 zet Exp $ 
 *
 */
public class DownloadTableTreeContentProvider implements ITreeContentProvider, Observer, ITreeViewerListener, TreeListener {
		
	private static Object[] EMPTY_ARRAY = new Object[0];	
	public CustomTableTreeViewer tableTreeViewer = null;
	public Table table = null;
	public int CHUNKS_COLUMN = DownloadTableTreeViewer.getChunksColumn();
	
	// ...
	public List hadChildren = Collections.synchronizedList(new ArrayList());
	public List objectsToUpdate = Collections.synchronizedList(new ArrayList());
	
	private int tmpDisplayBuffer = -1;
	private int displayBuffer = 2;
	private int refreshType = 0;
	private long lastTimeStamp = 0;
	private boolean forceRefresh = false;
	private DownloadTableTreeViewer downloadTableTreeViewer = null;

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 * Called at every tree expansion
	 */
	public Object[] getChildren(Object parent) {
		if (parent instanceof FileInfo) {

			ArrayList list = new ArrayList();
			FileInfo fileInfo = (FileInfo) parent;
			synchronized (fileInfo.getClientInfos()) {
				Iterator it = fileInfo.getClientInfos().iterator();
				while (it.hasNext()) {
					ClientInfo clientInfo = (ClientInfo) it.next();
					if (isInteresting(clientInfo)) {
						list.add(new TreeClientInfo(fileInfo, clientInfo));
					}
				
				}
				return list.toArray();
			}
		} else
			return EMPTY_ARRAY;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	
	public boolean hasChildren(Object parent) {

		if (parent instanceof FileInfo) {
			
			FileInfo fileInfo = (FileInfo) parent;
			synchronized (fileInfo.getClientInfos()) {
				Iterator it = fileInfo.getClientInfos().iterator();
				
				while (it.hasNext()) {
					ClientInfo clientInfo = (ClientInfo) it.next();
					if (isInteresting(clientInfo)) {
						if (!hadChildren.contains(fileInfo)) hadChildren.add(fileInfo);
						return true;
					} 
				}
			}
			// barren loins
			hadChildren.remove(fileInfo);
			return false;
		}
		return false;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */

	public Object getParent(Object child) {
		if (child instanceof TreeClientInfo) {
			TreeClientInfo treeClientInfo = (TreeClientInfo) child;
			return treeClientInfo.getFileInfo();
		}
		return null;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object element) {
	
		if (element instanceof FileInfoIntMap) {
			synchronized (element) {
				FileInfoIntMap fileInfoIntMap = (FileInfoIntMap) element;
				ArrayList list = new ArrayList();
				TIntObjectIterator it = fileInfoIntMap.iterator();
				while (it.hasNext()) {
					it.advance();
					FileInfo fileInfo = (FileInfo) it.value();
					if (isInteresting(fileInfo)) {
						fileInfo.deleteObserver ( this );
						fileInfo.addObserver( this );
						list.add(fileInfo);
					}
				}
				return list.toArray();
			}
		}
		return EMPTY_ARRAY;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		tableTreeViewer = null;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer tableTreeViewer, Object oldInput, Object newInput) {
		if (tableTreeViewer instanceof CustomTableTreeViewer) {
			this.tableTreeViewer = (CustomTableTreeViewer) tableTreeViewer;
			this.table = this.tableTreeViewer.getTableTree().getTable();
		}
	}
	
	/*
	 * should this fileInfo be displayed
	 */
	public static boolean isInteresting ( FileInfo fileInfo ) {
	
		if ( fileInfo.getState().getState() == EnumFileState.DOWNLOADING
			|| fileInfo.getState().getState() == EnumFileState.PAUSED 
			|| fileInfo.getState().getState() == EnumFileState.DOWNLOADED 
			|| fileInfo.getState().getState() == EnumFileState.QUEUED 
			)
			return true;
		return false;
	
	}

	/*
	 * should this clientInfo be displayed
	 */
	public static boolean isInteresting(ClientInfo clientInfo) {

		// we are downloading from this client, so he is interesting 
		if (clientInfo.getState().getState() == EnumState.CONNECTED_DOWNLOADING)
			return true;
		 // we are connected to this client and have a queue-rank != 0 and smaller 200
		else if (clientInfo.getState().getState() == EnumState.CONNECTED_AND_QUEUED
				&& clientInfo.getState().getRank() != 0
				&& clientInfo.getState().getRank() < 200)
			return true;
		// we were connected to this client but have a queue-rank != 0 and smaller 200
		else if ( clientInfo.getState().getState() == EnumState.NOT_CONNECTED_WAS_QUEUED
				&& clientInfo.getState().getRank() != 0
				&& clientInfo.getState().getRank() < 200)
			return true;
		else
			return false;
	}
	
	public String getClientActivity( ClientInfo clientInfo) {
		if ( clientInfo.getState().getState() == EnumState.CONNECTED_DOWNLOADING )
			return "transferring";
		else 
			return  "rank: " + clientInfo.getState().getRank() ;
	}		
	
	/*
	 *  (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(final Observable o, final Object object) {
		if (MainTab.getShell().isDisposed()) return;
		Shell shell = MainTab.getShell();
		if(shell != null && shell.getDisplay()!=null) {
			shell.getDisplay().asyncExec(new Runnable() {
				public void run() {
					sendUpdate(o, object);
				}
			});
		}	
	}
	
	public void sendUpdate(Observable o, Object arg) {
		
		if (tableTreeViewer == null) return;
		if (o instanceof FileInfoIntMap) {
			// new fileInfo - observe it
			if (arg instanceof FileInfo) {
				FileInfo fileInfo = (FileInfo) arg;
				fileInfo.addObserver(this);
				scheduleUpdate(null);
			} 
			// remove old, or reread fileinfolist
			// fixthis: when a file is cancelled, this one is called, then the one above re-adds it.. 
			else if (arg == null) {
				scheduleUpdate(null);
			}
				
		}
		else if (o instanceof FileInfo) {
			FileInfo fileInfo = (FileInfo) o;
			// FileInfo updated	
			if (arg instanceof FileInfo) {
				FileInfo fileInfoArg = (FileInfo) arg;
				// if it is no longer interesting..
				if (!isInteresting(fileInfoArg)) {
					closeAllEditors();
					tableTreeViewer.refresh(fileInfoArg);
					updateAllEditors();
				}
				// we just started the app, and it already had interesting clients
				else if ( fileInfo.getRate() > 0 && !hadChildren.contains(fileInfo)) {
					hasChildren(fileInfo);
					TableTreeItem item = (TableTreeItem) tableTreeViewer.objectToItem(arg);
					tableTreeViewer.updatePlus(item, fileInfo);
				// update with this new info	
				} else {	
				// close editors?  if sort order changes, maybe!
					scheduleUpdate(fileInfoArg);
				}	
		
			} else if (arg instanceof ClientInfo) {
				ClientInfo clientInfo = (ClientInfo) arg;
				// removed a client
				if (!fileInfo.getClientInfos().contains(clientInfo)) {
					TableTreeItem item = (TableTreeItem) tableTreeViewer.objectToItem(arg);
					TableTreeItem parentItem = (TableTreeItem) tableTreeViewer.getParentItem(item);
					if (tableTreeViewer.getExpandedState(fileInfo)) {
						scheduleUpdate(null);
					} else {
						tableTreeViewer.updatePlus(parentItem, arg);
					}
				}
				else if (isInteresting(clientInfo)) {
					//didn't have children, but now needs a +
					if (!hadChildren.contains(fileInfo)) {
						hasChildren(fileInfo);
						TableTreeItem parentItem = (TableTreeItem) tableTreeViewer.objectToItem(fileInfo);
						tableTreeViewer.updatePlus(parentItem, fileInfo);
					} 
					else if (tableTreeViewer != null && tableTreeViewer.getExpandedState(fileInfo)) {
						TableTreeItem parentItem = (TableTreeItem) tableTreeViewer.objectToItem(fileInfo);
						Item[] items = tableTreeViewer.getItems(parentItem);
						if (items != null) {
							boolean found = false;
							for (int i = 0; i < items.length; i++) {
								TreeClientInfo treeClientInfo = (TreeClientInfo) items[ i ].getData();
								if (treeClientInfo != null 
									&& treeClientInfo.getFileInfo().hashCode() == fileInfo.hashCode()
									&& treeClientInfo.getClientInfo().hashCode() == clientInfo.hashCode()) {
										scheduleUpdate(treeClientInfo);
										found = true;
									}
							}
							if (!found) {
								scheduleUpdate(null);
							}
							
						} else {		
							scheduleUpdate(null);			
						}
					}
				
				}
			}
			// From queued to unqueued state and now interesting ?
			 else if (arg == null) {
			 	scheduleUpdate(null);
			 }
		}
	
	}

	/*
	 * run table update 
	 */
	public void scheduleUpdate(Object object) {
		
		
		if (object==null || forceRefresh) refreshType = 3;
		
		if (refreshType != 3 
			&& object != null
			&& !objectsToUpdate.contains(object) ) {
				objectsToUpdate.add(object);
				refreshType = 1;
		}

		if (refreshType != 0 && System.currentTimeMillis() > lastTimeStamp + (1000*displayBuffer)) {
					
			// try to prevent initial refresh flood upon first starting the app		
			if (lastTimeStamp == 0) {
				tmpDisplayBuffer = displayBuffer;
				displayBuffer=3;		
			} else if (tmpDisplayBuffer != -1) {
				displayBuffer=tmpDisplayBuffer;
				tmpDisplayBuffer = -1;
			}
									
			lastTimeStamp = System.currentTimeMillis();
		
			if ( tableTreeViewer != null ) {

				switch(refreshType) {
					case 1:
						tableTreeViewer.update(objectsToUpdate.toArray(),null);
						break;
					case 2:
					case 3:
					default:
						closeAllEditors();
						tableTreeViewer.refresh();
						break;
				}
				updateAllEditors();		
				objectsToUpdate.clear();	
				refreshType = 0;
			}
		}
	}				

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.swt.events.TreeListener#treeCollapsed(org.eclipse.swt.events.TreeEvent)
	 */
	public void treeCollapsed (TreeEvent event) 
	{
		downloadTableTreeViewer.setLastTreeEvent(System.currentTimeMillis());
		TableTreeItem item = (TableTreeItem) event.item;
		closeChildEditors(item);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.swt.events.TreeListener#treeExpanded(org.eclipse.swt.events.TreeEvent)
	 */
	public void treeExpanded (TreeEvent event) 
	{
		downloadTableTreeViewer.setLastTreeEvent(System.currentTimeMillis());
		TableTreeItem item = (TableTreeItem) event.item;
		updateChildEditors(item);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeViewerListener#treeCollapsed(org.eclipse.jface.viewers.TreeExpansionEvent)
	 * (data)
	 */
	public void treeCollapsed(TreeExpansionEvent event) 
	{
		
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeViewerListener#treeExpanded(org.eclipse.jface.viewers.TreeExpansionEvent)
	 */
	public void treeExpanded(TreeExpansionEvent event)
	{
		
	}


// Hack to update TableTreeEditors
// A viewer can keep TableTreeItems in place while moving the associated data,
// but a TableTreeEditor seems permanently afixed to a TableTreeItem
// setData("..") are also afixed to a TableTreeItem 
// @#!$*(& jface

	/*
	 * close TreeTableEditor associated with tableTreeItem
	 */
	public void closeEditor(TableTreeItem tableTreeItem) {
		
		if (!DownloadTableTreeViewer.displayChunkGraphs()) return;
		
		if (tableTreeItem.getData("tableTreeEditor") != null) {
										
			TableTreeEditor tableTreeEditor = (TableTreeEditor) tableTreeItem.getData("tableTreeEditor");	
			tableTreeEditor.getEditor().dispose();
			tableTreeEditor.setEditor(null);
		//	tableTreeEditor.dispose();  // this crashes
							
			ChunkCanvas chunkCanvas = (ChunkCanvas) tableTreeItem.getData("thisChunkCanvas");
			if (tableTreeItem.getData() instanceof FileInfo)
				((Observable) tableTreeItem.getData()).deleteObserver(chunkCanvas);
			else {
				TreeClientInfo treeClientInfo = (TreeClientInfo) tableTreeItem.getData();
				treeClientInfo.getClientInfo().deleteObserver(chunkCanvas);
			}
			chunkCanvas.dispose();
			
			tableTreeItem.setData("tableTreeEditor", null);
			tableTreeItem.setData("thisChunkCanvas", null);
			tableTreeItem.setData("oldHash", null);
		}	
	}

	/*
	 * close all child editors of parent
	 */			
	public void closeChildEditors(TableTreeItem parent) {
		TableTreeItem[] children = parent.getItems();
		for (int j = 0; j < children.length; j++) 
			closeEditor(children [ j ]);
	}

	/*
	 * close all child editors currently displayed
	 */
	public void closeAllChildEditors () {
		TableTreeItem[] items = tableTreeViewer.getTableTree().getItems();
		for (int i = 0; i < items.length ; i++) 
			closeChildEditors(items [ i ]);
	}

	/*
	 * close all editors
	 */
	public void closeAllEditors() {

		if (!DownloadTableTreeViewer.displayChunkGraphs()) return;

		Table thisTable = tableTreeViewer.getTableTree().getTable();
		TableTreeItem[] tableTreeItems = tableTreeViewer.getTableTree().getItems();
		for (int i = 0; i < tableTreeItems.length; i++) {
			TableTreeItem tableTreeItem = tableTreeItems[i];
			closeChildEditors(tableTreeItem);
			closeEditor(tableTreeItem);
		}
	}

	/*
	 * open a TableTreeEditor on the tableTreeItem
	 */
	public void openEditor(TableTreeItem tableTreeItem) {
		
		if (!DownloadTableTreeViewer.displayChunkGraphs()) return;
		
		ChunkCanvas chunkCanvas;
		FileInfo fileInfo;
		TreeClientInfo treeClientInfo;
		ClientInfo clientInfo = null;
		
		if (tableTreeItem.getData() instanceof TreeClientInfo) {
			treeClientInfo = (TreeClientInfo) tableTreeItem.getData();
			clientInfo = treeClientInfo.getClientInfo();
			fileInfo = treeClientInfo.getFileInfo();
			tableTreeItem.setData("oldHash", new Integer(clientInfo.hashCode()));
		} else {  // hope for a fileInfo
			fileInfo = (FileInfo) tableTreeItem.getData();
			tableTreeItem.setData("oldHash", new Integer(tableTreeItem.getData().hashCode()));
		}
			
		chunkCanvas = new ChunkCanvas( table, SWT.NO_BACKGROUND, clientInfo, fileInfo );
		TableTreeEditor tableTreeEditor = new TableTreeEditor(tableTreeViewer.getTableTree());
		tableTreeEditor.horizontalAlignment = SWT.LEFT;
		tableTreeEditor.grabHorizontal = true;
		
		tableTreeItem.setData("thisChunkCanvas", chunkCanvas);	
		tableTreeItem.setData("tableTreeEditor", tableTreeEditor);
		
		tableTreeEditor.setEditor( chunkCanvas, tableTreeItem, CHUNKS_COLUMN );
		
		if (tableTreeItem.getData() instanceof TreeClientInfo) {
			treeClientInfo = (TreeClientInfo) tableTreeItem.getData();
			clientInfo = treeClientInfo.getClientInfo();
			clientInfo.addObserver( chunkCanvas );
		} else {
			( (Observable) tableTreeItem.getData() ).addObserver( chunkCanvas );
		}
	
	}

	/*
	 * update all child editors of this parent
	 */
	public void updateChildEditors(TableTreeItem parent) {
		
		Table thisTable = tableTreeViewer.getTableTree().getTable();
		TableTreeItem[] children = parent.getItems();
		
		if (tableTreeViewer.getExpandedState(parent.getData())) {
		
		for (int j = 0; j < children.length; j++) {
			TableTreeItem child = children [ j ];
		
			if (child.getData() instanceof TreeClientInfo) {
				if (child.getData("tableTreeEditor") == null) 
					openEditor(child);
				else { 
					TreeClientInfo treeClientInfo = (TreeClientInfo) child.getData();
					FileInfo parentFileInfo = (FileInfo) parent.getData();
					if (treeClientInfo.getFileInfo().hashCode() == parentFileInfo.hashCode()) {
						ClientInfo clientInfo = treeClientInfo.getClientInfo();
						Integer oldHash = (Integer) child.getData("oldHash");
						if ( !oldHash.equals( new Integer ( clientInfo.hashCode() ) ) ) {
							closeEditor(child);
							openEditor(child);
						}
					} else {
						
						closeEditor(child);
						openEditor(child);
						
					}
		
				}
			}	
		}
		
		}	
	}

	/*
	 * update all TableTreeEditors
	 */
	public void updateAllEditors () {
	
		if (!DownloadTableTreeViewer.displayChunkGraphs()) return;
	
		Table thisTable = tableTreeViewer.getTableTree().getTable();
		TableTreeItem[] tableTreeItems = tableTreeViewer.getTableTree().getItems();
		// cycle through all items and opened children
		for (int i = 0; i < tableTreeItems.length ; i++) {
			TableTreeItem tableTreeItem = tableTreeItems [ i ]; 
		
			updateChildEditors(tableTreeItem);
		
			if (tableTreeItem.getData("tableTreeEditor") == null) {
				if (tableTreeItem.getData() instanceof FileInfo) 	
					openEditor( tableTreeItem );			
			} else {
				Integer oldHash = (Integer) tableTreeItem.getData("oldHash");
				if ( !oldHash.equals( new Integer ( tableTreeItem.getData().hashCode() ) ) ) {
					closeEditor(tableTreeItem);
					openEditor(tableTreeItem);
				} 
			}
		}
	}

	public void setUpdateBuffer(int i) {
		displayBuffer = i;
	}
	public void setForceRefresh(boolean b) {
		forceRefresh = b;
	}
	public void setDownloadTableTreeViewer(DownloadTableTreeViewer v) {
		downloadTableTreeViewer = v;
	}

}
/*
$Log: DownloadTableTreeContentProvider.java,v $
Revision 1.9  2003/08/17 16:48:08  zet
prevent rapid tree expansion from triggering double click

Revision 1.8  2003/08/16 19:00:59  zet
address fast + clicking crash

Revision 1.7  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.6  2003/08/11 00:30:10  zet
show queued files

Revision 1.5  2003/08/08 23:29:36  zet
dispose tabletreeeditor

Revision 1.4  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.3  2003/08/06 19:29:55  zet
minor

Revision 1.2  2003/08/06 17:13:58  zet
minor updates

Revision 1.1  2003/08/04 19:22:08  zet
trial tabletreeviewer

*/
