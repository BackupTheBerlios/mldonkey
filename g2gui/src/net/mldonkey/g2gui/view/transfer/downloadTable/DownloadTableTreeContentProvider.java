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

import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.FileInfoIntMap;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.transfer.TreeClientInfo;
import net.mldonkey.g2gui.view.viewers.tableTree.GTableTreeContentProvider;

import gnu.trove.TIntObjectIterator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;


/**
 * DownloadTableTreeContentProvider
 *
 * @version $Id: DownloadTableTreeContentProvider.java,v 1.8 2003/10/31 22:41:59 zet Exp $
 *
 */
public class DownloadTableTreeContentProvider extends GTableTreeContentProvider implements Observer {
    private Set delayedFileInfoUpdateSet = new HashSet();
    private Set delayedFileInfoAddSet = new HashSet();
    private Set delayedFileInfoRemoveSet = new HashSet();
    private int updateDelay = 2;
    private long lastUpdateTimeStamp = 0;
    private long lastRemoveTimeStamp = 0;
    private long lastAddTimeStamp = 0;
    private boolean addTimerRunning = false;
    private boolean removeTimerRunning = false;
    private boolean updateTimerRunning = false;

    public DownloadTableTreeContentProvider(DownloadTableTreeView downloadTableTreeViewer) {
        super(downloadTableTreeViewer);
    }

    /*
     *  (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     * Called at every tree expansion
     */
    public Object[] getChildren(Object parent) {
        if (parent instanceof FileInfo) {
            FileInfo fileInfo = (FileInfo) parent;

            return fileInfo.getTreeClientInfoSet().toArray();
        } else {
            return EMPTY_ARRAY;
        }
    }

    /*
     *  (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(Object parent) {
        if (parent instanceof FileInfo) {
            FileInfo fileInfo = (FileInfo) parent;

            return (fileInfo.getTreeClientInfoSet().size() > 0);
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
        } else if (child instanceof FileInfo) {
            return tableTreeViewer.getInput();
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

                    if (fileInfo.isInteresting()) {
                        fileInfo.deleteObserver(this);
                        fileInfo.addObserver(this);
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
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(final Observable o, final Object object) {
        if ((tableTreeViewer == null) || tableTreeViewer.getTableTree().isDisposed()) {
            return;
        }

        tableTreeViewer.getTableTree().getDisplay().asyncExec(new Runnable() {
                public void run() {
                    sendUpdate(o, object);
                }
            });
    }

    /**
     * @param o
     * @param arg
     */
    public void sendUpdate(Observable o, Object arg) {
        if ((tableTreeViewer == null) || tableTreeViewer.getTableTree().isDisposed()) {
            return;
        }

        if (o instanceof FileInfoIntMap) {
            // a new FileInfo
            if (arg instanceof FileInfo) {
                FileInfo fileInfo = (FileInfo) arg;

                if (fileInfo.isInteresting()) {
                    fileInfo.addObserver(this);
                    delayedAddFileInfo(fileInfo);
                }

                // removeObsolete && readStream
            } else if (arg == null) {
                tableTreeViewer.refresh();
            }
        } else if (o instanceof FileInfo) {
            // updated fileInfo
            if (arg instanceof String[]) {
                FileInfo fileInfo = (FileInfo) o;

                if (fileInfo.isInteresting()) {
                    if (updateDelay == 0) {
                        String[] args = (String[]) arg;
                        tableTreeViewer.update(fileInfo, (args.length > 0) ? args : null);
                    } else {
                        delayedUpdateFileInfo(fileInfo);
                    }
                } else {
                    delayedRemoveFileInfo(fileInfo);
                }

                // ClientInfos in a TreeClientInfo
            } else if (arg instanceof TreeClientInfo) {
                TreeClientInfo treeClientInfo = (TreeClientInfo) arg;

                if (treeClientInfo.getDelete()) {
                    tableTreeViewer.remove(treeClientInfo);
                } else {
                    tableTreeViewer.add(treeClientInfo.getFileInfo(), treeClientInfo);
                }
            }
        }
    }

    /**
     * @param fileInfo
     */
    public void delayedUpdateFileInfo(FileInfo fileInfo) {
        if (fileInfo != null) {
            delayedFileInfoUpdateSet.add(fileInfo);
        }

        if (System.currentTimeMillis() > (lastUpdateTimeStamp + (updateDelay * 1000))) {
            if (delayedFileInfoUpdateSet.size() > 0) {
                if ((tableTreeViewer != null) && !tableTreeViewer.getTableTree().isDisposed()) {
                    tableTreeViewer.update(delayedFileInfoUpdateSet.toArray(),
                        FileInfo.ALL_PROPERTIES);
                }

                delayedFileInfoUpdateSet.clear();
            }

            lastUpdateTimeStamp = System.currentTimeMillis();
        } else {
            if (!updateTimerRunning && (delayedFileInfoUpdateSet.size() > 0)) {
                updateTimerRunning = true;
                tableTreeViewer.getTableTree().getDisplay().timerExec((updateDelay * 1000) + 2000,
                    new Runnable() {
                        public void run() {
                            delayedUpdateFileInfo(null);
                            updateTimerRunning = false;
                        }
                    });
            }
        }
    }

    /**
     * @param fileInfo
     */
    public void delayedAddFileInfo(FileInfo fileInfo) {
        if (fileInfo != null) {
            delayedFileInfoAddSet.add(fileInfo);
        }

        if (System.currentTimeMillis() > (lastAddTimeStamp + 789)) {
            if (delayedFileInfoAddSet.size() > 0) {
                if ((tableTreeViewer != null) && !tableTreeViewer.getTableTree().isDisposed()) {
                    tableTreeViewer.add(tableTreeViewer.getInput(), delayedFileInfoAddSet.toArray());
                }

                delayedFileInfoAddSet.clear();
            }

            lastAddTimeStamp = System.currentTimeMillis();
        } else {
            if (!addTimerRunning && (delayedFileInfoAddSet.size() > 0)) {
                addTimerRunning = true;
                tableTreeViewer.getTableTree().getDisplay().timerExec(2000,
                    new Runnable() {
                        public void run() {
                            delayedAddFileInfo(null);
                            addTimerRunning = false;
                        }
                    });
            }
        }
    }

    /**
     * @param fileInfo
     */
    public void delayedRemoveFileInfo(FileInfo fileInfo) {
        if (fileInfo != null) {
            delayedFileInfoRemoveSet.add(fileInfo);
        }

        if (System.currentTimeMillis() > (lastRemoveTimeStamp + 789)) {
            if (delayedFileInfoRemoveSet.size() > 0) {
                if ((tableTreeViewer != null) && !tableTreeViewer.getTableTree().isDisposed()) {
                    tableTreeViewer.remove(delayedFileInfoRemoveSet.toArray());
                }

                delayedFileInfoRemoveSet.clear();
            }

            lastRemoveTimeStamp = System.currentTimeMillis();
        } else {
            if (!removeTimerRunning && (delayedFileInfoRemoveSet.size() > 0)) {
                removeTimerRunning = true;
                tableTreeViewer.getTableTree().getDisplay().timerExec(2000,
                    new Runnable() {
                        public void run() {
                            delayedRemoveFileInfo(null);
                            removeTimerRunning = false;
                        }
                    });
            }
        }
    }

    public void updateDisplay() {
        updateDelay = PreferenceLoader.loadInteger("updateDelay");
    }
}


/*
$Log: DownloadTableTreeContentProvider.java,v $
Revision 1.8  2003/10/31 22:41:59  zet
rename to View

Revision 1.7  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.6  2003/10/22 01:38:31  zet
*** empty log message ***

Revision 1.5  2003/10/12 15:58:29  zet
rewrite downloads table & more..

Revision 1.4  2003/09/28 18:55:41  zet
minor

Revision 1.3  2003/09/24 03:07:43  zet
add # of active sources column

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
