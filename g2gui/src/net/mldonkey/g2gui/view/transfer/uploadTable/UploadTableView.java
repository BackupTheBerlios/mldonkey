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

import net.mldonkey.g2gui.helper.ObjectWeakMap;
import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.G2Gui;
import net.mldonkey.g2gui.view.helper.ViewFrame;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


/**
 * UploadersTableViewer
 *
 * @version $Id: UploadTableView.java,v 1.14 2004/03/26 18:11:03 dek Exp $
 *
 */
public class UploadTableView extends GTableView {
    private static final int NETWORK = 0;
    private static final int NAME = 1;
    private static final int SOFTWARE = 2;
    private static final int UPLOADED = 3;
    private static final int DOWNLOADED = 4;
    private static final int CONNECT_TIME = 5;
    private static final int SOCK_ADDR = 6;
    private static final int PORT = 7;
    private static final int KIND = 8;
    private static final int STATE = 9;
    private static final int FILENAME = 10;
    private ObjectWeakMap objectWeakMap;
    private long lastTimeStamp;

    /**
     * @param parent the place where this table lives
     * @param mldonkey the source of our data
     * @param tab We live on this tab
     */
    public UploadTableView(ViewFrame viewFrame) {
        super(viewFrame);

        preferenceString = "uploaders";
        columnLabels = new String[] {
                "TT_UT_NETWORK", "TT_UT_NAME", "TT_UT_SOFTWARE", "TT_UT_UPLOADED",
                "TT_UT_DOWNLOADED", "TT_UT_CONNECT_TIME", "TT_UT_SOCK_ADDR", "TT_UT_PORT",
                "TT_UT_KIND", "TT_UT_STATE", "TT_UT_FILENAME"
            };

        columnDefaultWidths = new int[] { 100, 100, 100, 100, 100, 100, 100, 100, 100, 150, 200 };
        columnAlignment = new int[] {
                SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT,
                SWT.LEFT, SWT.LEFT, SWT.LEFT
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
        viewFrame.updateCLabelText(G2GuiResources.getString("TT_Uploads") + ": " +
            objectWeakMap.getWeakMap().size());
    }

    /**
     * UploadContentProvider
     */
    public class UploadContentProvider extends GTableContentProvider implements Observer {
        /* ( non-Javadoc )
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements( java.lang.Object )
         */
        public UploadContentProvider(UploadTableView uTableViewer) {
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
        public UploadersTableLabelProvider(UploadTableView uTableViewer) {
            super(uTableViewer);
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
         */
        public Image getColumnImage(Object element, int columnIndex) {
            ClientInfo clientInfo = (ClientInfo) element;

            switch (cViewer.getColumnIDs()[ columnIndex ]) {
            case UploadTableView.STATE:
                return G2GuiResources.getClientImage((EnumState) clientInfo.getState());

            case UploadTableView.NETWORK:
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
            case UploadTableView.STATE:
                return G2Gui.emptyString + clientInfo.getDetailedClientActivity();

            case UploadTableView.NAME:
                return G2Gui.emptyString + clientInfo.getClientName();

            case UploadTableView.NETWORK:
                return G2Gui.emptyString + clientInfo.getClientnetworkid().getNetworkName();

            case UploadTableView.KIND:
                return G2Gui.emptyString + clientInfo.getClientConnection();

            case UploadTableView.SOFTWARE:
                return clientInfo.getClientSoftware();

            case UploadTableView.UPLOADED:
                return clientInfo.getUploadedString();

            case UploadTableView.CONNECT_TIME:
                return clientInfo.getClientConnectTimePassedString();

            case UploadTableView.DOWNLOADED:
                return clientInfo.getDownloadedString();

            case UploadTableView.SOCK_ADDR:
                return clientInfo.getClientKind().getAddr().toString();

            case UploadTableView.PORT:
                return G2Gui.emptyString + clientInfo.getClientKind().getPort();

            case UploadTableView.FILENAME:
                return clientInfo.getUploadFilename();

            default:
                return G2Gui.emptyString;
            }
        }
    }

    /**
     * UploadTableSorter
     */
    public class UploadersTableSorter extends GSorter {
        public UploadersTableSorter(UploadTableView uTableViewer) {
            super(uTableViewer);
        }

        /* (non-Javadoc)
         * @see net.mldonkey.g2gui.view.viewers.GSorter#sortOrder(int)
         */
        public boolean sortOrder(int columnIndex) {
            switch (cViewer.getColumnIDs()[ columnIndex ]) {
            case UploadTableView.UPLOADED:
            case UploadTableView.DOWNLOADED:
            case UploadTableView.CONNECT_TIME:
            case UploadTableView.SOCK_ADDR:
            case UploadTableView.PORT:
                return false;

            default:
                return true;
            }
        }

        /*
         * (non-Javadoc)
         * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
         */
        public int compare(Viewer viewer, Object obj1, Object obj2) {
            ClientInfo clientInfo1 = (ClientInfo) obj1;
            ClientInfo clientInfo2 = (ClientInfo) obj2;

            switch (cViewer.getColumnIDs()[ columnIndex ]) {
            case UploadTableView.UPLOADED:
                return compareLongs(clientInfo1.getUploaded(), clientInfo2.getUploaded());

            case UploadTableView.DOWNLOADED:
                return compareLongs(clientInfo1.getDownloaded(), clientInfo2.getDownloaded());

            case UploadTableView.STATE:

                if (clientInfo1.getState() == EnumState.CONNECTED_DOWNLOADING)
                    return -1;
                else if (clientInfo2.getState() == EnumState.CONNECTED_DOWNLOADING)
                    return 1;
                else if ((clientInfo1.getRank() != 0) &&
                        (clientInfo2.getRank() != 0))
                    return compareIntegers(clientInfo1.getRank(),
                        clientInfo2.getRank());
                else if (clientInfo1.getRank() != 0)
                    return -1;
                else if (clientInfo2.getRank() != 0)
                    return 1;

            case UploadTableView.SOCK_ADDR:
                return compareAddrs(clientInfo1.getClientKind().getAddr(),
                    clientInfo2.getClientKind().getAddr());

            case UploadTableView.PORT:
                return compareIntegers(clientInfo1.getClientKind().getPort(),
                    clientInfo2.getClientKind().getPort());

            case UploadTableView.CONNECT_TIME:
                return compareIntegers(clientInfo1.getClientConnectTime(),
                    clientInfo2.getClientConnectTime());

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
                menuManager.add(new ClientDetailAction(gView.getShell(), null,
                        (ClientInfo) selectedClientInfos.get(0), core));
            }
        }
    }
}


/*
$Log: UploadTableView.java,v $
Revision 1.14  2004/03/26 18:11:03  dek
some more profiling and mem-saving option (hopefully)  introduced

Revision 1.13  2004/03/25 19:25:23  dek
yet more profiling

Revision 1.12  2004/01/22 21:28:03  psy
renamed "uploads" to "shares" and moved it to a tab of its own.
"uploaders" are now called "uploads" for improved naming-consistency

Revision 1.11  2003/12/19 22:15:27  psy
added human readable value for connectiontime passed since connection initiation

Revision 1.10  2003/12/04 08:47:31  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.9  2003/12/01 16:39:25  zet
set default sort order for specific columns

Revision 1.8  2003/12/01 13:28:16  zet
add port info

Revision 1.7  2003/11/30 23:42:56  zet
updates for latest mldonkey cvs

Revision 1.6  2003/11/29 13:01:11  lemmy
Addr.getString() renamed to the more natural word name Addr.toString()

Revision 1.5  2003/11/28 22:37:59  zet
coalesce addr use

Revision 1.4  2003/11/28 08:23:28  lemmy
use Addr instead of String

Revision 1.3  2003/11/27 21:42:33  zet
integrate ViewFrame a little more.. more to come.

Revision 1.2  2003/11/26 16:05:37  zet
minor

Revision 1.1  2003/11/26 07:43:15  zet
quick attempt at an uploaders table w/proto 19 - still in progress...


*/
