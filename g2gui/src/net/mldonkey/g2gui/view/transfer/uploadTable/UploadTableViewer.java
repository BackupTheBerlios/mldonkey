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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.SharedFileInfo;
import net.mldonkey.g2gui.model.SharedFileInfoIntMap;
import net.mldonkey.g2gui.view.TransferTab;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.GSorter;
import net.mldonkey.g2gui.view.viewers.actions.CopyED2KLinkToClipboardAction;
import net.mldonkey.g2gui.view.viewers.actions.RefreshUploadsAction;
import net.mldonkey.g2gui.view.viewers.table.GTableContentProvider;
import net.mldonkey.g2gui.view.viewers.table.GTableLabelProvider;
import net.mldonkey.g2gui.view.viewers.table.GTableMenuListener;
import net.mldonkey.g2gui.view.viewers.table.GTablePage;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;


/**
 * UploadTableViewer
 *
 * @version $Id: UploadTableViewer.java,v 1.17 2003/10/31 10:42:47 lemmster Exp $
 *
 */
public class UploadTableViewer extends GTablePage {
    private static final int NETWORK = 0;
    private static final int BYTES = 1;
    private static final int REQUESTS = 2;
    private static final int NAME = 3;
    private SharedFileInfoIntMap sharedFileInfoIntMap;

    /**
     * @param parent the place where this table lives
     * @param mldonkey the source of our data
     * @param tab We live on this tab
     */
    public UploadTableViewer(Composite parent, CoreCommunication aCore, TransferTab tab) {
        super(parent, aCore);

        preferenceString = "upload";
        columnLabels = new String[] {
                "TT_UPLOAD_NETWORK", "TT_UPLOAD_UPLOAD", "TT_UPLOAD_QUERIES", "TT_UPLOAD_NAME"
            };
        columnDefaultWidths = new int[] { 50, 50, 50, 50 };
        columnAlignment = new int[] { SWT.LEFT, SWT.RIGHT, SWT.RIGHT, SWT.LEFT };

        gSorter = new UploadTableSorter(this);
        tableContentProvider = new UploadContentProvider(this);
        tableLabelProvider = new UploadLabelProvider(this);
        tableMenuListener = new UploadTableMenuListener(this);

        this.sharedFileInfoIntMap = core.getSharedFileInfoIntMap();
        createContents(parent);
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.GTableViewer#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected void createContents(Composite parent) {
        super.createContents(parent);
        sViewer.setInput(this.sharedFileInfoIntMap);
        sViewer.addSelectionChangedListener((UploadTableMenuListener) tableMenuListener);
    }

    /**
     * UploadContentProvider
     */
    public class UploadContentProvider extends GTableContentProvider implements Observer {
        /* ( non-Javadoc )
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements( java.lang.Object )
         */
        public UploadContentProvider(UploadTableViewer uTableViewer) {
            super(uTableViewer);
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
         */
        public Object[] getElements(Object inputElement) {
            SharedFileInfoIntMap sharedFiles = (SharedFileInfoIntMap) inputElement;
            TIntObjectIterator it = sharedFiles.iterator();
            SharedFileInfo[] result = new SharedFileInfo[ sharedFiles.size() ];
            int i = 0;

            while (it.hasNext()) { // java.util.ConcurrentModificationException
                it.advance();
                result[ i ] = (SharedFileInfo) it.value();
                i++;
            }

            return result;
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
         */
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            SharedFileInfoIntMap oldI = (SharedFileInfoIntMap) oldInput;
            SharedFileInfoIntMap newI = (SharedFileInfoIntMap) newInput;

            if (oldI != null) {
                oldI.deleteObserver(this);
            }

            if (newI != null) {
                newI.addObserver(this);
            }
        }

        /* (non-Javadoc)
         * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
         */
        public void update(Observable arg0, final Object arg1) {
            if (getTableViewer().getTable().isDisposed()) {
                return;
            }

            getTableViewer().getTable().getDisplay().asyncExec(new Runnable() {
                    public void run() {
                        if (getTableViewer().getTable().isDisposed()) {
                            return;
                        }

                        synchronized (sharedFileInfoIntMap.getRemoved()) {
                            getTableViewer().remove(sharedFileInfoIntMap.getRemoved().toArray());
                            sharedFileInfoIntMap.clearRemoved();
                        }

                        synchronized (sharedFileInfoIntMap.getAdded()) {
                            getTableViewer().add(sharedFileInfoIntMap.getAdded().toArray());
                            sharedFileInfoIntMap.clearAdded();
                        }

                        synchronized (sharedFileInfoIntMap.getUpdated()) {
                            getTableViewer().update(sharedFileInfoIntMap.getUpdated().toArray(),
                                null);
                            sharedFileInfoIntMap.clearUpdated();
                        }
                    }
                });
        }
    }

    /**
     * UploadLabelProvider
     */
    public class UploadLabelProvider extends GTableLabelProvider {
        public UploadLabelProvider(UploadTableViewer uTableViewer) {
            super(uTableViewer);
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex) {
            switch (getTableViewer().getColumnIDs()[ columnIndex ]) {
            case UploadTableViewer.NETWORK:

                SharedFileInfo file = (SharedFileInfo) element;

                return G2GuiResources.getNetworkImage(file.getNetwork().getNetworkType());

            default:
                return null;
            }
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex) {
            SharedFileInfo info = (SharedFileInfo) element;

            switch (getTableViewer().getColumnIDs()[ columnIndex ]) {
            case UploadTableViewer.NETWORK:
                return info.getNetwork().getNetworkName();

            case UploadTableViewer.BYTES:
                return info.getUploadedString();

            case UploadTableViewer.REQUESTS:
                return "" + info.getNumOfQueriesForFile();

            case UploadTableViewer.NAME:
                return info.getName();

            default:
                return "";
            }
        }
    }

    /**
     * UploadTableSorter
     */
    public class UploadTableSorter extends GSorter {
        public UploadTableSorter(UploadTableViewer uTableViewer) {
            super(uTableViewer);
        }

        /*
         * (non-Javadoc)
         * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
         */
        public int compare(Viewer viewer, Object obj1, Object obj2) {
            SharedFileInfo sharedFile1 = (SharedFileInfo) obj1;
            SharedFileInfo sharedFile2 = (SharedFileInfo) obj2;

            switch (cViewer.getColumnIDs()[ columnIndex ]) {
            case UploadTableViewer.NETWORK:
                return compareStrings(sharedFile1.getNetwork().getNetworkName(), // NPE
                    sharedFile2.getNetwork().getNetworkName());

            case UploadTableViewer.BYTES:
                return compareLongs(sharedFile1.getNumOfBytesUploaded(),
                    sharedFile2.getNumOfBytesUploaded());

            case UploadTableViewer.REQUESTS:
                return compareLongs(sharedFile1.getNumOfQueriesForFile(),
                    sharedFile2.getNumOfQueriesForFile());

            case UploadTableViewer.NAME:
                return compareStrings(sharedFile1.getName(), sharedFile2.getName());

            default:
                return 0;
            }
        }
    }

    /**
     * UploadTableMenuListener
     */
    public class UploadTableMenuListener extends GTableMenuListener
        implements ISelectionChangedListener, IMenuListener {
        private SharedFileInfo selectedFile;
        private List selectedFiles = new ArrayList();

        /**
         * @param gTableViewer
         */
        public UploadTableMenuListener(GTablePage gTableViewer) {
            super(gTableViewer);
        }

        /* ( non-Javadoc )
         * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged( org.eclipse.jface.viewers.SelectionChangedEvent )
         */
        public void selectionChanged(SelectionChangedEvent event) {
            IStructuredSelection sSel = (IStructuredSelection) event.getSelection();
            Object o = sSel.getFirstElement();
            selectedFile = (o instanceof SharedFileInfo) ? (SharedFileInfo) o : null;
            selectedFiles.clear();

            for (Iterator it = sSel.iterator(); it.hasNext();) {
                o = it.next();

                if (o instanceof SharedFileInfo) {
                    selectedFiles.add((SharedFileInfo) o);
                }
            }
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
         */
        public void menuAboutToShow(IMenuManager menuManager) {
            /*copy ED2K-Link*/
            if (selectedFile != null) {
                String[] linkList = new String[ selectedFiles.size() ];

                for (int i = 0; i < selectedFiles.size(); i++) {
                    linkList[ i ] = new String(((SharedFileInfo) selectedFiles.get(i)).getED2K());
                }

                MenuManager clipboardMenu = new MenuManager(G2GuiResources.getString(
                            "TT_DOWNLOAD_MENU_COPYTO"));
                clipboardMenu.add(new CopyED2KLinkToClipboardAction(false, linkList));
                clipboardMenu.add(new CopyED2KLinkToClipboardAction(true, linkList));
                menuManager.add(clipboardMenu);
            }

            menuManager.add(new RefreshUploadsAction(gViewer));
        }
    }
}


/*
$Log: UploadTableViewer.java,v $
Revision 1.17  2003/10/31 10:42:47  lemmster
Renamed GViewer, GTableViewer and GTableTreeViewer to GPage... to avoid mix-ups with StructuredViewer...
Removed IGViewer because our abstract class GPage do the job
Use supertype/interface where possible to keep the design flexible!

Revision 1.16  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.15  2003/10/22 20:38:35  zet
common actions

Revision 1.14  2003/10/22 01:38:45  zet
add column selector

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
