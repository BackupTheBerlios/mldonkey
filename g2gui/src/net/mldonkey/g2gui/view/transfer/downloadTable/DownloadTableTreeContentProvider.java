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
package net.mldonkey.g2gui.view.transfer.downloadTable;

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.FileInfoIntMap;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.transfer.ChunkCanvas;
import net.mldonkey.g2gui.view.transfer.CustomTableTreeViewer;
import net.mldonkey.g2gui.view.transfer.TreeClientInfo;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTreeEditor;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import gnu.trove.TIntObjectIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;


/**
 * DownloadTableTreeContentProvider
 *
 * @version $Id: DownloadTableTreeContentProvider.java,v 1.2 2003/09/20 22:40:43 zet Exp $
 *
 */
public class DownloadTableTreeContentProvider implements ITreeContentProvider, Observer, TreeListener {
    private final static int CHUNKS_COLUMN = DownloadTableTreeViewer.getChunksColumn(  );
    private final static Object[] EMPTY_ARRAY = new Object[ 0 ];
    private CustomTableTreeViewer tableTreeViewer = null;
    private Map parentChildrenMap = new Hashtable(  );
    private Set delayedFileInfoSet = new HashSet(  );
    private List tableTreeEditorList = Collections.synchronizedList( new ArrayList(  ) );
    private DownloadTableTreeViewer downloadTableTreeViewer = null;
    private int updateDelay = 2;
    private long lastUpdateTimeStamp = 0;
    
	private static final String[] allProperties = {
		  FileInfo.CHANGED_RATE, 
		  FileInfo.CHANGED_DOWNLOADED, 
		  FileInfo.CHANGED_PERCENT, 
		  FileInfo.CHANGED_AVAIL, 
		  FileInfo.CHANGED_ETA,
		  FileInfo.CHANGED_LAST
	  };
    

    /*
     *  (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     * Called at every tree expansion
     */
    public Object[] getChildren( Object parent ) {
        if ( parent instanceof FileInfo ) {
            List list = Collections.synchronizedList( new ArrayList(  ) );
            FileInfo fileInfo = (FileInfo) parent;

            synchronized ( fileInfo.getClientInfos(  ) ) {
                Iterator it = fileInfo.getClientInfos(  ).keySet(  ).iterator(  );

                while ( it.hasNext(  ) ) {
                    ClientInfo clientInfo = (ClientInfo) it.next(  );

                    if ( isInteresting( clientInfo ) ) {
                        TreeClientInfo t = new TreeClientInfo( fileInfo, clientInfo );
                        list.add( t );
                    }
                }

                parentChildrenMap.put( fileInfo, list );

                return list.toArray(  );
            }
        } else {
            return EMPTY_ARRAY;
        }
    }

    /*
     *  (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren( Object parent ) {
        if ( parent instanceof FileInfo ) {
            FileInfo fileInfo = (FileInfo) parent;

            synchronized ( fileInfo.getClientInfos(  ) ) {
                Iterator it = fileInfo.getClientInfos(  ).keySet(  ).iterator(  );

                while ( it.hasNext(  ) ) {
                    ClientInfo clientInfo = (ClientInfo) it.next(  );
                    if ( isInteresting( clientInfo ) ) {
                        return true;
                    }
                }
            }

            return false;
        }

        return false;
    }

    /*
     *  (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
     */
    public Object getParent( Object child ) {
        if ( child instanceof TreeClientInfo ) {
            TreeClientInfo treeClientInfo = (TreeClientInfo) child;

            return treeClientInfo.getFileInfo(  );
        } else if ( child instanceof FileInfo ) {
            return tableTreeViewer.getInput(  );
        }

        return null;
    }

    /*
     *  (non-Javadoc)
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements( Object element ) {
        if ( element instanceof FileInfoIntMap ) {
            synchronized ( element ) {
                FileInfoIntMap fileInfoIntMap = (FileInfoIntMap) element;
                ArrayList list = new ArrayList(  );
                TIntObjectIterator it = fileInfoIntMap.iterator(  );

                while ( it.hasNext(  ) ) {
                    it.advance(  );

                    FileInfo fileInfo = (FileInfo) it.value(  );

                    if ( isInteresting( fileInfo ) ) {
                        fileInfo.deleteObserver( this );
                        fileInfo.addObserver( this );
                        list.add( fileInfo );
                    }
                }

                return list.toArray(  );
            }
        }

        return EMPTY_ARRAY;
    }

    /*
     *  (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    public void dispose(  ) {
        tableTreeViewer = null;
    }

    /*
     *  (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public void inputChanged( Viewer tableTreeViewer, Object oldInput, Object newInput ) {
        if ( tableTreeViewer instanceof CustomTableTreeViewer ) {
            this.tableTreeViewer = (CustomTableTreeViewer) tableTreeViewer;
        }
    }

    /*
     * should this fileInfo be displayed
     */
    public static boolean isInteresting( FileInfo fileInfo ) {
        if ( ( fileInfo.getState(  ).getState(  ) == EnumFileState.DOWNLOADING ) || ( fileInfo.getState(  ).getState(  ) == EnumFileState.PAUSED ) ||
                ( fileInfo.getState(  ).getState(  ) == EnumFileState.DOWNLOADED ) || ( fileInfo.getState(  ).getState(  ) == EnumFileState.QUEUED ) ) {
            return true;
        }

        return false;
    }

    /*
     * should this clientInfo be displayed
     */
    public static boolean isInteresting( ClientInfo clientInfo ) {
        // we are downloading from this client, so he is interesting 
        if ( clientInfo.getState(  ).getState(  ) == EnumState.CONNECTED_DOWNLOADING ) {
            return true;
        }
        // we are connected to this client and have a queue-rank != 0 and smaller 200
        //		else if (clientInfo.getState().getState() == EnumState.CONNECTED_AND_QUEUED
        //				&& clientInfo.getState().getRank() != 0
        //				&& clientInfo.getState().getRank() < 200)
        //			return true;
        //		// we were connected to this client but have a queue-rank != 0 and smaller 200
        //		else if ( clientInfo.getState().getState() == EnumState.NOT_CONNECTED_WAS_QUEUED
        //				&& clientInfo.getState().getRank() != 0
        //				&& clientInfo.getState().getRank() < 200)
        //			return true;
        else {
            return false;
        }
    }

    /*
     *  (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update( final Observable o, final Object object ) {
        if ( ( tableTreeViewer == null ) || tableTreeViewer.getTableTree(  ).isDisposed(  ) ) {
            return;
        }

        tableTreeViewer.getTableTree(  ).getDisplay(  ).asyncExec( new Runnable(  ) {
                public void run(  ) {
                    sendUpdate( o, object );
                }
            } );
    }

    public void sendUpdate( Observable o, Object arg ) {
        if ( ( tableTreeViewer == null ) || tableTreeViewer.getTableTree(  ).isDisposed(  ) ) {
            return;
        }

        if ( o instanceof FileInfoIntMap ) {
            // a new FileInfo
            if ( arg instanceof FileInfo ) {
                FileInfo fileInfo = (FileInfo) arg;

                if ( isInteresting( fileInfo ) ) {
                    fileInfo.addObserver( this );
                    tableTreeViewer.add( tableTreeViewer.getInput(  ), fileInfo );
                    updateAllEditors(  );
                }

                // removeObsolete && readStream
            } else if ( arg == null ) {
                tableTreeViewer.refresh(  );
                updateAllEditors(  );
            }
        } else if ( o instanceof FileInfo ) {
            // updated fileInfo
            if ( arg instanceof String[] ) {
                FileInfo fileInfo = (FileInfo) o;

                if ( isInteresting( fileInfo ) ) {
                    if ( updateDelay == 0 ) {
                        tableTreeViewer.update( fileInfo, (String[]) arg );
                        updateAllEditors(  );
                    } else {
                        delayedUpdate( fileInfo, (String[]) arg );
                    }
                } else {
                    tableTreeViewer.remove( fileInfo );
                    updateAllEditors(  );
                }
            } else if ( arg instanceof TreeClientInfo ) {
                TreeClientInfo treeClientInfo = (TreeClientInfo) arg;
                ClientInfo clientInfo = treeClientInfo.getClientInfo(  );
                FileInfo fileInfo = (FileInfo) o;
                List list = (List) parentChildrenMap.get( fileInfo );
                TreeClientInfo foundTreeClientInfo = null;

                for ( int i = 0; ( list != null ) && ( i < list.size(  ) ); i++ ) {
                    TreeClientInfo t = (TreeClientInfo) list.get( i );

                    if ( ( treeClientInfo.getFileInfo(  ).getId(  ) == t.getFileInfo(  ).getId(  ) ) &&
                            ( treeClientInfo.getClientInfo(  ).getClientid(  ) == t.getClientInfo(  ).getClientid(  ) ) ) {
                        foundTreeClientInfo = t;

                        break;
                    }
                }

                if ( isInteresting( clientInfo ) ) {
                    if ( foundTreeClientInfo == null ) {
                        if ( list == null ) {
                            list = Collections.synchronizedList( new ArrayList(  ) );
                        }

                        parentChildrenMap.put( fileInfo, list );
                        list.add( treeClientInfo );
                        tableTreeViewer.add( fileInfo, treeClientInfo );
                        updateAllEditors(  );
                    }
                } else {
                    if ( foundTreeClientInfo != null ) {
                        tableTreeViewer.remove( foundTreeClientInfo );

                        if ( list != null ) {
                            list.remove( foundTreeClientInfo );
                        }

                        updateAllEditors(  );
                    }
                }
            }
        }
    }

    /**
     * @param fileInfo
     * @param properties
     *
     * Delay the update if desired
     *
     */
    public void delayedUpdate( FileInfo fileInfo, String[] properties ) {
        if ( System.currentTimeMillis(  ) > ( lastUpdateTimeStamp + ( updateDelay * 1000 ) ) ) {
            tableTreeViewer.update( delayedFileInfoSet.toArray(  ), allProperties );
            delayedFileInfoSet.clear(  );
            updateAllEditors(  );
            lastUpdateTimeStamp = System.currentTimeMillis(  );
        } else {
            delayedFileInfoSet.add( fileInfo );
        }
    }

    /*
     *  (non-Javadoc)
     * @see org.eclipse.swt.events.TreeListener#treeCollapsed(org.eclipse.swt.events.TreeEvent)
     */
    public void treeCollapsed( TreeEvent event ) {
        updateAllEditors(  );
    }

    /*
     *  (non-Javadoc)
     * @see org.eclipse.swt.events.TreeListener#treeExpanded(org.eclipse.swt.events.TreeEvent)
     */
    public void treeExpanded( TreeEvent event ) {
        updateAllEditors(  );
    }

    // Hack to update TableTreeEditors
    // A viewer can keep TableTreeItems in place while moving the associated data,
    // but a TableTreeEditor seems permanently afixed to a TableTreeItem
    // setData("..") are also afixed to a TableTreeItem 
    // @#!$*(& jface

    /*
     * close TreeTableEditor associated with tableTreeItem
     */
    public void closeEditor( TableTreeItem tableTreeItem ) {
        if ( !DownloadTableTreeViewer.displayChunkGraphs(  ) ) {
            return;
        }

        if ( tableTreeItem.getData( "tableTreeEditor" ) != null ) {
            TableTreeEditor tableTreeEditor = (TableTreeEditor) tableTreeItem.getData( "tableTreeEditor" );
            tableTreeEditorList.remove( tableTreeEditor );

            tableTreeEditor.getEditor(  ).dispose(  );
            tableTreeEditor.setEditor( null );

            //	tableTreeEditor.dispose();  // this crashes
            //			Not sure if this is needed...							
            //			ChunkCanvas chunkCanvas = (ChunkCanvas) tableTreeItem.getData("thisChunkCanvas");
            //			if (tableTreeItem.getData() instanceof FileInfo)
            //				((Observable) tableTreeItem.getData()).deleteObserver(chunkCanvas);
            //			else {
            //				TreeClientInfo treeClientInfo = (TreeClientInfo) tableTreeItem.getData();
            //				System.out.println(treeClientInfo.getClientInfo() + ">>" + chunkCanvas);
            //				treeClientInfo.getClientInfo().deleteObserver(chunkCanvas);
            //			}
            //			chunkCanvas.dispose();
            tableTreeItem.setData( "tableTreeEditor", null );
            tableTreeItem.setData( "oldHash", null );
        }
    }

    /*
     * close all child editors of parent
     */
    public void closeChildEditors( TableTreeItem parent ) {
        TableTreeItem[] children = parent.getItems(  );

        for ( int j = 0; j < children.length; j++ )
            closeEditor( children[ j ] );
    }

    /*
     * close all child editors currently displayed
     */
    public void closeAllChildEditors(  ) {
        TableTreeItem[] items = tableTreeViewer.getTableTree(  ).getItems(  );

        for ( int i = 0; i < items.length; i++ )
            closeChildEditors( items[ i ] );
    }

    /*
     * close all editors
     */
    public void closeAllEditors(  ) {
        if ( !DownloadTableTreeViewer.displayChunkGraphs(  ) ) {
            return;
        }

        Table thisTable = tableTreeViewer.getTableTree(  ).getTable(  );
        TableTreeItem[] tableTreeItems = tableTreeViewer.getTableTree(  ).getItems(  );

        for ( int i = 0; i < tableTreeItems.length; i++ ) {
            TableTreeItem tableTreeItem = tableTreeItems[ i ];
            closeChildEditors( tableTreeItem );
            closeEditor( tableTreeItem );
        }
    }

    /*
     * open a TableTreeEditor on the tableTreeItem
     */
    public void openEditor( TableTreeItem tableTreeItem ) {
        if ( !DownloadTableTreeViewer.displayChunkGraphs(  ) ) {
            return;
        }

        ChunkCanvas chunkCanvas;
        FileInfo fileInfo;
        TreeClientInfo treeClientInfo;
        ClientInfo clientInfo = null;

        if ( tableTreeItem.getData(  ) instanceof TreeClientInfo ) {
            treeClientInfo = (TreeClientInfo) tableTreeItem.getData(  );
            clientInfo = treeClientInfo.getClientInfo(  );
            fileInfo = treeClientInfo.getFileInfo(  );
            tableTreeItem.setData( "oldHash", new Integer( clientInfo.hashCode(  ) ) );
        } else { // hope for a fileInfo
            fileInfo = (FileInfo) tableTreeItem.getData(  );
            tableTreeItem.setData( "oldHash", new Integer( tableTreeItem.getData(  ).hashCode(  ) ) );
        }

        chunkCanvas = new ChunkCanvas( tableTreeViewer.getTableTree(  ).getTable(  ), SWT.NO_BACKGROUND, clientInfo, fileInfo, null );

        TableTreeEditor tableTreeEditor = new TableTreeEditor( tableTreeViewer.getTableTree(  ) );

        tableTreeEditorList.add( tableTreeEditor );

        tableTreeEditor.horizontalAlignment = SWT.LEFT;
        tableTreeEditor.grabHorizontal = true;

        tableTreeItem.setData( "tableTreeEditor", tableTreeEditor );

        tableTreeEditor.setEditor( chunkCanvas, tableTreeItem, CHUNKS_COLUMN );

        if ( tableTreeItem.getData(  ) instanceof TreeClientInfo ) {
            treeClientInfo = (TreeClientInfo) tableTreeItem.getData(  );
            clientInfo = treeClientInfo.getClientInfo(  );
            clientInfo.addObserver( chunkCanvas );
        } else {
            ( (Observable) tableTreeItem.getData(  ) ).addObserver( chunkCanvas );
        }
    }

    /*
     * update all child editors of this parent
     */
    public void updateChildEditors( TableTreeItem parent ) {
        Table thisTable = tableTreeViewer.getTableTree(  ).getTable(  );
        TableTreeItem[] children = parent.getItems(  );

        if ( tableTreeViewer.getExpandedState( parent.getData(  ) ) ) {
            for ( int j = 0; j < children.length; j++ ) {
                TableTreeItem child = children[ j ];

                if ( child.getData(  ) instanceof TreeClientInfo ) {
                    if ( child.getData( "tableTreeEditor" ) == null ) {
                        openEditor( child );
                    }
                }
            }
        }
    }

    /*
     * update all TableTreeEditors
     */
    public void updateAllEditors(  ) {
        if ( !DownloadTableTreeViewer.displayChunkGraphs(  ) ) {
            return;
        }

        // yet another attempt: try to clean up here instead of closing all editors..
        List tmpList = Collections.synchronizedList( new ArrayList(  ) );

        for ( int i = 0; i < tableTreeEditorList.size(  ); i++ ) {
            TableTreeEditor tableTreeEditor = (TableTreeEditor) tableTreeEditorList.get( i );

            int itemHash = 0;
            TableTreeItem tableTreeItem = tableTreeEditor.getItem(  );

            if ( ( tableTreeItem == null ) || tableTreeItem.isDisposed(  ) ) {
                // null Item and null Editor
                if ( tableTreeEditor.getEditor(  ) != null ) {
                    tableTreeEditor.getEditor(  ).dispose(  ); // dump bad ones here
                    tableTreeEditor.dispose(  );
                }
            } else {
                if ( tableTreeItem.getData(  ) instanceof TreeClientInfo ) {
                    TreeClientInfo treeClientInfo = (TreeClientInfo) tableTreeItem.getData(  );
                    itemHash = treeClientInfo.getClientInfo(  ).hashCode(  );
                } else {
                    if ( tableTreeItem.getData(  ) != null ) {
                        itemHash = tableTreeItem.getData(  ).hashCode(  );
                    }
                }

                if ( itemHash != ( (ChunkCanvas) tableTreeEditor.getEditor(  ) ).getHash(  ) ) {
                    tableTreeEditor.getItem(  ).setData( "tableTreeEditor", null );
                    tableTreeEditor.getEditor(  ).dispose(  );
                    tableTreeEditor.dispose(  );
                } else {
                    tmpList.add( tableTreeEditor );
                }
            }
        }

        tableTreeEditorList.clear(  );
        tableTreeEditorList = tmpList;

        // in rare circumstances, the tabletreeeditor isn't updated.. this appeared to
        // help, but who knows...
        TableColumn c = tableTreeViewer.getTableTree(  ).getTable(  ).getColumn( CHUNKS_COLUMN );
        c.setWidth( c.getWidth(  ) );

        Table thisTable = tableTreeViewer.getTableTree(  ).getTable(  );
        TableTreeItem[] tableTreeItems = tableTreeViewer.getTableTree(  ).getItems(  );

        // cycle through all items and opened children
        for ( int i = 0; i < tableTreeItems.length; i++ ) {
            TableTreeItem tableTreeItem = tableTreeItems[ i ];

            updateChildEditors( tableTreeItem );

            if ( tableTreeItem.getData( "tableTreeEditor" ) == null ) {
                if ( tableTreeItem.getData(  ) instanceof FileInfo ) {
                    openEditor( tableTreeItem );
                }
            }
        }
    }

    public void setDownloadTableTreeViewer( DownloadTableTreeViewer v ) {
        downloadTableTreeViewer = v;
    }

    public void setUpdateDelay( int i ) {
        updateDelay = i;
    }
}


/*
$Log: DownloadTableTreeContentProvider.java,v $
Revision 1.2  2003/09/20 22:40:43  zet
*** empty log message ***

Revision 1.1  2003/09/20 14:39:21  zet
move transfer package

Revision 1.26  2003/09/18 14:11:01  zet
revert

Revision 1.24  2003/09/15 22:10:32  zet
add availability %, refresh delay option

Revision 1.23  2003/09/14 16:23:56  zet
multi network avails

Revision 1.22  2003/09/14 04:23:25  zet
remove unneeded

Revision 1.21  2003/09/14 04:17:19  zet
remove unneeded meth

Revision 1.20  2003/09/14 03:37:43  zet
changedProperties

Revision 1.19  2003/09/13 22:26:44  zet
weak sets & !rawrate

Revision 1.18  2003/08/30 00:44:01  zet
move tabletree menu

Revision 1.17  2003/08/23 19:44:12  zet
split transfer table to basic/advanced modes

Revision 1.16  2003/08/23 15:24:03  zet
*** empty log message ***

Revision 1.15  2003/08/23 15:21:37  zet
remove @author

Revision 1.14  2003/08/23 15:13:00  zet
remove reference to static MainTab methods

Revision 1.13  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.12  2003/08/22 21:16:36  lemmster
replace $user$ with $Author: zet $

Revision 1.11  2003/08/21 00:59:57  zet
doubleclick expand

Revision 1.10  2003/08/20 22:18:56  zet
Viewer updates

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
