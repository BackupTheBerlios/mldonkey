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
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTreeEditor;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

/**
 * DownloadTableTreeContentProvider
 *
 * @author $user$
 * @version $Id: DownloadTableTreeContentProvider.java,v 1.1 2003/08/04 19:22:08 zet Exp $ 
 *
 */
public class DownloadTableTreeContentProvider implements ITreeContentProvider, Observer, ITreeViewerListener, TreeListener {
		
	private static Object[] EMPTY_ARRAY = new Object[0];	
	public TableTreeViewer tableTreeViewer = null;
	public Table table = null;
	public int CHUNKS_COLUMN = DownloadTableTreeViewer.getChunksColumn();
	
	// ...
	public List hadChildren = Collections.synchronizedList(new ArrayList());
	public List objectsToUpdate = Collections.synchronizedList(new ArrayList());
	public Map clientsForUpdate = new Hashtable();
	
	private int displayBuffer = 1;
	private int refreshType = 0;
	private long lastTimeStamp;

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 * Called at every tree expansion
	 */
	public Object[] getChildren(Object arg0) {
		if (arg0 instanceof FileInfo) {

			ArrayList list = new ArrayList();
			FileInfo file = (FileInfo) arg0;
			synchronized (file.getClientInfos()) {
				Iterator it = file.getClientInfos().iterator();
				while (it.hasNext()) {
					ClientInfo clientInfo = (ClientInfo) it.next();
					if (isInteresting(clientInfo))
						list.add(clientInfo);
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
	
	public boolean hasChildren(Object arg0) {

		if (arg0 instanceof FileInfo) {
			
			FileInfo fileInfo = (FileInfo) arg0;
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
			hadChildren.remove(fileInfo);
			return false;
		}
		return false;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object arg0) {
		return null;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object arg0) {
	
		if (arg0 instanceof FileInfoIntMap) {
			synchronized (arg0) {
				FileInfoIntMap files = (FileInfoIntMap) arg0;
				ArrayList list = new ArrayList();
				TIntObjectIterator it = files.iterator();
				int c = 0;
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
		if (tableTreeViewer instanceof TableTreeViewer) {
			this.tableTreeViewer = (TableTreeViewer) tableTreeViewer;
			this.table = this.tableTreeViewer.getTableTree().getTable();
		}
	}
	
	/*
	 * should this fileInfo be displayed
	 */
	public static boolean isInteresting ( FileInfo fileInfo ) {
	
		if ( fileInfo.getState().getState() == EnumFileState.DOWNLOADING
			|| fileInfo.getState().getState() == EnumFileState.PAUSED )
			return true;
		return false;
	
	}

	/*
	 * should this clientInfo be displayed
	 */
	public static boolean isInteresting( ClientInfo clientInfo ) {
		
			if // we are downloading from this client, so he is interesting 
				( clientInfo.getState().getState() == EnumState.CONNECTED_DOWNLOADING )
				return true;
			else if // we are connected to this client and have a queue-rank != 0 and smaller 200
				( clientInfo.getState().getState() == EnumState.CONNECTED_AND_QUEUED
						&& clientInfo.getState().getRank() != 0 
						&& clientInfo.getState().getRank() < 200
				)
				return true;
			else if // we were connected to this client but have a queue-rank != 0 and smaller 200
				( clientInfo.getState().getState() == EnumState.NOT_CONNECTED_WAS_QUEUED
						&& clientInfo.getState().getRank() != 0
						&& clientInfo.getState().getRank() < 200 
				)
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
	public void update(Observable o, Object arg) {

		// listen for new FileInfos and Observe them.
		if (o instanceof FileInfoIntMap) {
			if (arg instanceof FileInfo) {
				FileInfo fileInfo = (FileInfo) arg;
				fileInfo.addObserver(this);
				refreshType = 3;
				runTableUpdate(null);
			} else if (arg == null) {
				// removed old or reread fileinfolist
				refreshType = 3;
				runTableUpdate(null);
			}
		}
		// listen for FileInfo changes 
		else if (o instanceof FileInfo) {
			FileInfo fileInfo = (FileInfo) o;
			// FileInfo updated	
			if (arg instanceof FileInfo) {
				FileInfo fileInfoArg = (FileInfo) arg;
				if (!isInteresting(fileInfoArg)) {
					refreshType = 3;
					runTableUpdate(null);
				}
				else
					if (refreshType == 0) refreshType = 1;
					runTableUpdate(fileInfoArg);
				// FileInfo.clientInfos updated
			} else if (arg instanceof ClientInfo) {
		
				ClientInfo clientInfo = (ClientInfo) arg;
				// removed a client
				if (!fileInfo.getClientInfos().contains(clientInfo)) {
					refreshType = 3;
					runTableUpdate(null);
				}
				else if (isInteresting(clientInfo)) {
					
								
					if (!hadChildren.contains(fileInfo)) {
						hasChildren(fileInfo);
						refreshType = 3;
						runTableUpdate(null);
					} else { 
						
						if (refreshType == 0) refreshType = 1;
						
						// Hashtable's are sync'd, but this arrayList is not 
						// this is a temp attempt to use update instead of refresh (flickering)
						// if clientInfo is in an
						// a) unexpanded fileInfo
						// b) expanded fileInfo + already displayed
						// --> must determine (access viewer) in the SWT thread	
						if (clientsForUpdate.containsKey(fileInfo)) {
							ArrayList arrayList = (ArrayList) clientsForUpdate.get(fileInfo);
							if (!arrayList.contains(clientInfo))
								arrayList.add(clientInfo);
						} else {
							ArrayList arrayList = new ArrayList();
							arrayList.add(clientInfo);
							clientsForUpdate.put(fileInfo, arrayList);
						}

					}

				}
				// From queued to unqueued state and now interesting ?
			} else if (arg == null) {
				refreshType = 3;
				runTableUpdate(null);
			}
		}

	}
	
/*	

		// How to know when an existing source in FileInfo.clientInfos 
	// becomes interesting?
	
	// isExpandable calls hasChildren
	// why do some files have a rate, but no interesting clients? 

	/* this is wrong, these clients never seem to be interesting (rank of 0)...
	   - rank comes separately?

		else if (arg instanceof FileInfo) {
			FileInfo fileInfo = (FileInfo) arg;
			ArrayList newClientsList = (ArrayList) fileInfo.getNewClients();
			if (newClientsList.size() > 0) {

				if (tableTreeViewer!=null && (!tableTreeViewer.isExpandable(fileInfo)
					|| tableTreeViewer.getExpandedState(fileInfo))) {
					boolean foundInteresting = false;
					
					for (int i = 0; i < newClientsList.size(); i++) {
						ClientInfo clientInfo =
							(ClientInfo) newClientsList.get(i);
						if (isInteresting(clientInfo)) {
							foundInteresting = true;
						} else {
						
						}
					}
					if (foundInteresting)
						runTableUpdate(null, 1);
				}
				fileInfo.clearNewClients();
			} else {
				if (fileInfo.getState().getState() == EnumFileState.CANCELLED || 
					fileInfo.getState().getState() == EnumFileState.ABORTED) {
						fileInfo.deleteObserver( this );
						runTableUpdate(null, 2);			
					}
				
			}
		}
	 */	

	/*
	 * run table update 
	 */
	public void runTableUpdate(final Object object) {
		
		if (object != null && !objectsToUpdate.contains(object)) {
			objectsToUpdate.add(object);
		}

		if (System.currentTimeMillis() > lastTimeStamp + (1000*displayBuffer)) {
								
			lastTimeStamp = System.currentTimeMillis();
		
			final int rType = refreshType;
			refreshType = 0;
			
			
			if (MainTab.getShell().isDisposed()) return;
	
			Shell shell = MainTab.getShell();
			if(shell != null && shell.getDisplay()!=null) {
			
			shell.getDisplay().asyncExec(new Runnable() {
				public void run() {
			
				int newRefreshType = rType;
			
				// Now we are in SWT thread, test out all added clientInfos
				// trying to update() rather than refresh()..
				if (newRefreshType < 2 && !clientsForUpdate.isEmpty()) {
				
					for (Iterator i = clientsForUpdate.keySet().iterator(); i.hasNext();) {
						FileInfo fileInfo = (FileInfo)i.next();
						// if not expanded, don't bother
						if (tableTreeViewer.getExpandedState(fileInfo)) {
							
							
							// Unfortunately, getVisibleExpandedElements() only returns FileInfo's...
							
							
//							ArrayList visibleElements = new ArrayList(Arrays.asList(tableTreeViewer.getVisibleExpandedElements()));			
//							ArrayList arrayList = (ArrayList)clientsForUpdate.get(fileInfo);
//							
//							for (int j = 0; j < arrayList.size(); j++ ) {
//								
//								if (!visibleElements.contains(( ClientInfo) arrayList.get(j))) {
//									System.out.println("not visible, full refresh required");
//									newRefreshType = 3;
//									break;					
//								} else {
//									System.out.println("it is visible!");
//									objectsToUpdate.add(arrayList.get(j));
//								}
//							}
//							if (newRefreshType == 3) break;
						
							newRefreshType = 3;
							break;
						
						} else {
						
							break;	
						}
					
				
					}
				}		
				clientsForUpdate.clear();
							
				if ( tableTreeViewer != null ) {
					if (newRefreshType == 2) {
						closeAllChildEditors();
						tableTreeViewer.refresh();
					} else if (newRefreshType == 3) {
						closeAllEditors();
						tableTreeViewer.refresh();
					} else {
						tableTreeViewer.update(objectsToUpdate.toArray(),null);
					}
					objectsToUpdate.clear();
					updateAllEditors();
				}
			}
		});
		
			}
		
		}
	}	

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.swt.events.TreeListener#treeCollapsed(org.eclipse.swt.events.TreeEvent)
	 */
	public void treeCollapsed (TreeEvent event) 
	{
		TableTreeItem item = (TableTreeItem) event.item;
		closeChildEditors(item);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.swt.events.TreeListener#treeExpanded(org.eclipse.swt.events.TreeEvent)
	 */
	public void treeExpanded (TreeEvent event) 
	{
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

	/*
	 * close TreeTableEditor associated with tableTreeItem
	 */
	public void closeEditor(TableTreeItem tableTreeItem) {
		
		if (!DownloadTableTreeViewer.displayChunkGraphs()) return;
		
		if (tableTreeItem.getData("tableTreeEditor") != null) {
											
			TableTreeEditor tableTreeEditor = (TableTreeEditor) tableTreeItem.getData("tableTreeEditor");	
			tableTreeEditor.dispose(); 
							
			ChunkCanvas chunkCanvas = (ChunkCanvas) tableTreeItem.getData("thisChunkCanvas");
			((Observable) tableTreeItem.getData()).deleteObserver(chunkCanvas);
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
		ClientInfo clientInfo = null;
		
		if (tableTreeItem.getData() instanceof ClientInfo) {
			clientInfo = (ClientInfo) tableTreeItem.getData();
			fileInfo = (FileInfo) tableTreeItem.getParentItem().getData();
		} else {  // hope for a fileInfo
			fileInfo = (FileInfo) tableTreeItem.getData();
		}
		chunkCanvas = new ChunkCanvas( table, SWT.NO_BACKGROUND, clientInfo, fileInfo, CHUNKS_COLUMN );
		TableTreeEditor tableTreeEditor = new TableTreeEditor(tableTreeViewer.getTableTree());
		tableTreeEditor.horizontalAlignment = SWT.LEFT;
		tableTreeEditor.grabHorizontal = true;
		
		tableTreeItem.setData("oldHash", new Integer(tableTreeItem.getData().hashCode()));
		tableTreeItem.setData("thisChunkCanvas", chunkCanvas);	
		tableTreeItem.setData("tableTreeEditor", tableTreeEditor);
		
		tableTreeEditor.setEditor( chunkCanvas, tableTreeItem, CHUNKS_COLUMN );
		( (Observable) tableTreeItem.getData() ).addObserver( chunkCanvas );
	
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
		
			if (child.getData() instanceof ClientInfo) {
				if (child.getData("tableTreeEditor") == null) 
					openEditor(child);
				else {
					Integer oldHash = (Integer) child.getData("oldHash");
					if ( !oldHash.equals( new Integer ( child.getData().hashCode() ) ) ) {
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

}
/*
$Log: DownloadTableTreeContentProvider.java,v $
Revision 1.1  2003/08/04 19:22:08  zet
trial tabletreeviewer

*/
