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
package net.mldonkey.g2gui.view.transfer.uploadersTable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.helper.ObjectWeakMap;
import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.helper.ViewFrame;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.transfer.clientTable.ClientTableView;
import net.mldonkey.g2gui.view.viewers.GSorter;
import net.mldonkey.g2gui.view.viewers.actions.AddClientAsFriendAction;
import net.mldonkey.g2gui.view.viewers.actions.ClientDetailAction;
import net.mldonkey.g2gui.view.viewers.table.GTableContentProvider;
import net.mldonkey.g2gui.view.viewers.table.GTableLabelProvider;
import net.mldonkey.g2gui.view.viewers.table.GTableMenuListener;
import net.mldonkey.g2gui.view.viewers.table.GTableView;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;


/**
 * UploadersTableViewer
 *
 * @version $Id: UploadersTableView.java,v 1.2 2003/11/26 16:05:37 zet Exp $
 *
 */
public class UploadersTableView extends GTableView {
    private static final int NETWORK = 0;
    private static final int NAME = 1;
    private static final int SOFTWARE = 2;
    private static final int UPLOADED = 3;
    private static final int DOWNLOADED = 4;
    private static final int SOCK_ADDR = 5;
    private static final int KIND = 6;
    private static final int STATE = 7;
    private static final int FILENAME = 8;
    private ObjectWeakMap objectWeakMap;
    private CLabel headerCLabel;
    private long lastTimeStamp;

    /**
     * @param parent the place where this table lives
     * @param mldonkey the source of our data
     * @param tab We live on this tab
     */
    public UploadersTableView(ViewFrame viewFrame) {
        super(viewFrame.getChildComposite(), viewFrame.getCore());
        this.headerCLabel = viewFrame.getCLabel();

        preferenceString = "uploaders";
        columnLabels = new String[] {
                "TT_UT_NETWORK", "TT_UT_NAME", "TT_UT_SOFTWARE", "TT_UT_UPLOADED",
                "TT_UT_DOWNLOADED", "TT_UT_SOCK_ADDR", "TT_UT_KIND", "TT_UT_STATE", "TT_UT_FILENAME"
            };

        columnDefaultWidths = new int[] { 100, 100, 100, 100, 100, 100, 100, 150, 200 };
        columnAlignment = new int[] {
                SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.LEFT, SWT.LEFT,
                SWT.LEFT
            };

        gSorter = new UploadersTableSorter(this);
        tableContentProvider = new UploadContentProvider(this);
        tableLabelProvider = new UploadersTableLabelProvider(this);
        tableMenuListener = new UploadersTableMenuListener(this);

        createContents(viewFrame.getChildComposite());
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.GTableViewer#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected void createContents(Composite parent) {
        super.createContents(parent);
        objectWeakMap = core.getClientInfoIntMap().getUploadersWeakMap();
        sViewer.setInput(objectWeakMap);
        sViewer.addSelectionChangedListener((UploadersTableMenuListener) tableMenuListener);
        updateHeaderLabel();
    }

    private void updateHeaderLabel() {
        headerCLabel.setText(G2GuiResources.getString("TT_Uploaders") + ": " +
            objectWeakMap.getWeakMap().size());
    }

    /**
     * UploadContentProvider
     */
    public class UploadContentProvider extends GTableContentProvider implements Observer {
        /* ( non-Javadoc )
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements( java.lang.Object )
         */
        public UploadContentProvider(UploadersTableView uTableViewer) {
            super(uTableViewer);
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
         */
        public Object[] getElements(Object inputElement) {
            objectWeakMap = (ObjectWeakMap) inputElement;

            synchronized (objectWeakMap) {
                objectWeakMap.clearAllLists();

                return objectWeakMap.getKeySet().toArray();
            }
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
         */
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            if (oldInput != null)
                ((Observable) oldInput).deleteObserver(this);

            if (newInput != null)
                ((Observable) newInput).addObserver(this);
        }

        /* (non-Javadoc)
         * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
         */
        public void update(Observable arg0, final Object arg1) {
            final Integer updateType = (Integer) arg1;

            if (getTableViewer().getTable().isDisposed())
                return;

            getTableViewer().getTable().getDisplay().asyncExec(new Runnable() {
                    public void run() {
                        if (getTableViewer().getTable().isDisposed())
                            return;

                        switch (updateType.intValue()) {
                        case ObjectWeakMap.REMOVED:

                            synchronized (objectWeakMap) {
                                getTableViewer().remove(objectWeakMap.getRemovedList().toArray());
                                objectWeakMap.clearRemovedList();
                            }

                            updateHeaderLabel();

                            break;

                        case ObjectWeakMap.ADDED:

                            synchronized (objectWeakMap) {
                                getTableViewer().add(objectWeakMap.getAddedList().toArray());
                                objectWeakMap.clearAddedList();
                            }

                            updateHeaderLabel();

                            break;

                        case ObjectWeakMap.UPDATED:

                            synchronized (objectWeakMap) {
                                getTableViewer().update(objectWeakMap.getUpdatedList().toArray(),
                                    null);
                                objectWeakMap.clearUpdatedList();
                            }

                            break;
                        }
                    }
                });
        }
    }

    /**
     * UploadLabelProvider
     */
    public class UploadersTableLabelProvider extends GTableLabelProvider {
        public UploadersTableLabelProvider(UploadersTableView uTableViewer) {
            super(uTableViewer);
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex) {
            ClientInfo clientInfo = (ClientInfo) element;

            switch (cViewer.getColumnIDs()[ columnIndex ]) {
            case UploadersTableView.STATE:
                return G2GuiResources.getClientImage((EnumState) clientInfo.getState().getState());

            case UploadersTableView.NETWORK:
                return G2GuiResources.getNetworkImage(clientInfo.getClientnetworkid()
                                                                .getNetworkType());

            default:
                return null;
            }
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
         */
        public String getColumnText(Object element, int columnIndex) {
            ClientInfo clientInfo = (ClientInfo) element;

            switch (cViewer.getColumnIDs()[ columnIndex ]) {
            case UploadersTableView.STATE:
                return "" + clientInfo.getDetailedClientActivity();

            case UploadersTableView.NAME:
                return "" + clientInfo.getClientName();

            case UploadersTableView.NETWORK:
                return "" + clientInfo.getClientnetworkid().getNetworkName();

            case UploadersTableView.KIND:
                return "" + clientInfo.getClientConnection();

            case UploadersTableView.SOFTWARE:
                return clientInfo.getClientSoftware();

            case UploadersTableView.UPLOADED:
                return clientInfo.getUploadedString();

            case UploadersTableView.DOWNLOADED:
                return clientInfo.getDownloadedString();

            case UploadersTableView.SOCK_ADDR:
                return clientInfo.getClientSockAddr();

            case UploadersTableView.FILENAME:
                return clientInfo.getUploadFilename();

            default:
                return "";
            }
        }
    }

    /**
     * UploadTableSorter
     */
    public class UploadersTableSorter extends GSorter {
        public UploadersTableSorter(UploadersTableView uTableViewer) {
            super(uTableViewer);
        }

        /*
         * (non-Javadoc)
         * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
         */
        public int compare(Viewer viewer, Object obj1, Object obj2) {
            ClientInfo clientInfo1 = (ClientInfo) obj1;
            ClientInfo clientInfo2 = (ClientInfo) obj2;

            switch (cViewer.getColumnIDs()[ columnIndex ]) {
            case ClientTableView.UPLOADED:
                return compareLongs(clientInfo1.getUploaded(), clientInfo2.getUploaded());

            case ClientTableView.DOWNLOADED:
                return compareLongs(clientInfo1.getDownloaded(), clientInfo2.getDownloaded());

            case ClientTableView.STATE:

                if (clientInfo1.getState().getState() == EnumState.CONNECTED_DOWNLOADING)
                    return -1;
                else if (clientInfo2.getState().getState() == EnumState.CONNECTED_DOWNLOADING)
                    return 1;
                else if ((clientInfo1.getState().getRank() != 0) &&
                        (clientInfo2.getState().getRank() != 0))
                    return compareIntegers(clientInfo1.getState().getRank(),
                        clientInfo2.getState().getRank());
                else if (clientInfo1.getState().getRank() != 0)
                    return -1;
                else if (clientInfo2.getState().getRank() != 0)
                    return 1;

            // else fall through
            default:

                String s1;
                String s2;

                UploadersTableLabelProvider lprov = (UploadersTableLabelProvider) ((TableViewer) viewer).getLabelProvider();

                s1 = lprov.getColumnText(obj1, columnIndex);
                s2 = lprov.getColumnText(obj2, columnIndex);

                return compareStrings(s1, s2);
            }
        }
    }

    /**
     * UploadTableMenuListener
     */
    public class UploadersTableMenuListener extends GTableMenuListener
        implements ISelectionChangedListener, IMenuListener {
        private List selectedClientInfos = new ArrayList();

        /**
         * @param gTableViewer
         */
        public UploadersTableMenuListener(GTableView gTableViewer) {
            super(gTableViewer);
        }

        /* ( non-Javadoc )
         * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged( org.eclipse.jface.viewers.SelectionChangedEvent )
         */
        public void selectionChanged(SelectionChangedEvent event) {
            IStructuredSelection sSel = (IStructuredSelection) event.getSelection();
            Object o = sSel.getFirstElement();
            selectedClientInfos.clear();

            for (Iterator it = sSel.iterator(); it.hasNext();) {
                o = it.next();

                if (o instanceof ClientInfo)
                    selectedClientInfos.add(o);
            }
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
         */
        public void menuAboutToShow(IMenuManager menuManager) {
            if (selectedClientInfos.size() > 0) {
                ClientInfo[] clientInfoArray = new ClientInfo[ selectedClientInfos.size() ];

                for (int i = 0; i < selectedClientInfos.size(); i++)
                    clientInfoArray[ i ] = (ClientInfo) selectedClientInfos.get(i);

                menuManager.add(new AddClientAsFriendAction(core, clientInfoArray));
                menuManager.add(new ClientDetailAction(gView.getShell(),
                        null, (ClientInfo) selectedClientInfos.get(0),
                        core));
            }
        }
    }
}


/*
$Log: UploadersTableView.java,v $
Revision 1.2  2003/11/26 16:05:37  zet
minor

Revision 1.1  2003/11/26 07:43:15  zet
quick attempt at an uploaders table w/proto 19 - still in progress...


*/
