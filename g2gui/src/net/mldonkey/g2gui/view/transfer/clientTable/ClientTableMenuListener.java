/*
 * Copyright 2003
 * G2Gui Team
 *
 *
 * This file is part of G2Gui.
 *
 * G2Gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2Gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2Gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package net.mldonkey.g2gui.view.transfer.clientTable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.transfer.ClientDetailDialog;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;


/**
 * TableMenuListener
 *
 *
 * @version $Id: ClientTableMenuListener.java,v 1.4 2003/10/12 15:58:30 zet Exp $
 *
 */
public class ClientTableMenuListener implements ISelectionChangedListener, IMenuListener {
    private ClientTableContentProvider tableContentProvider;
    private TableViewer tableViewer;
    private CoreCommunication core;
    private ClientInfo selectedClientInfo;
    private List selectedClients = new ArrayList();

    /**
     * Creates a new TableMenuListener
     * @param The parent TableViewer
     * @param The CoreCommunication supporting this with data
     */
    public ClientTableMenuListener( TableViewer tableViewer, CoreCommunication core ) {
        super();
        this.tableViewer = tableViewer;
        this.core = core;
        this.tableContentProvider = (ClientTableContentProvider) this.tableViewer.getContentProvider();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    public void selectionChanged( SelectionChangedEvent event ) {
        IStructuredSelection sSel = (IStructuredSelection) event.getSelection();
        Object o = sSel.getFirstElement();

        if ( o instanceof ClientInfo ) {
            selectedClientInfo = (ClientInfo) o;
        } else {
            selectedClientInfo = null;
        }

        selectedClients.clear();

        for ( Iterator it = sSel.iterator(); it.hasNext(); ) {
            o = it.next();

            if ( o instanceof ClientInfo ) {
                selectedClients.add( (ClientInfo) o );
            }
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
     */
    public void menuAboutToShow( IMenuManager menuManager ) {
        /* disconnect */
        if ( selectedClientInfo != null ) {
            menuManager.add( new AddFriendAction() );
            menuManager.add( new ClientDetailAction() );
        }
    }

    /**
     * AddFriendAction
     */
    private class AddFriendAction extends Action {
        public AddFriendAction() {
            super();
            setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_ADD_FRIEND" ) );
        }

        public void run() {
            for ( int i = 0; i < selectedClients.size(); i++ ) {
                ClientInfo selectedClientInfo = (ClientInfo) selectedClients.get( i );
                ClientInfo.addFriend( core, selectedClientInfo.getClientid() );
            }
        }
    }

    /**
     * ClientDetailAction
     */
    class ClientDetailAction extends Action {
        public ClientDetailAction() {
            super();
            setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_CLIENT_DETAILS" ) );
        }

        public void run() {
            new ClientDetailDialog( (FileInfo) tableViewer.getInput(), selectedClientInfo, core );
        }
    }
}


/*
$Log: ClientTableMenuListener.java,v $
Revision 1.4  2003/10/12 15:58:30  zet
rewrite downloads table & more..

Revision 1.3  2003/09/23 15:24:24  zet
not much..

Revision 1.2  2003/09/21 23:39:31  zet
displayTableColors preference

Revision 1.1  2003/09/20 14:39:21  zet
move transfer package

Revision 1.6  2003/09/18 14:11:01  zet
revert

Revision 1.4  2003/08/31 00:08:59  zet
add buttons

Revision 1.3  2003/08/28 22:53:22  zet
add client details action

Revision 1.2  2003/08/23 15:21:37  zet
remove @author

Revision 1.1  2003/08/20 14:58:43  zet
sources clientinfo viewer




*/
