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
package net.mldonkey.g2gui.view.friends;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.view.MessagesTab;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.table.GTableMenuListener;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;


/**
 * TableMenuListener
 *
 * @version $Id: FriendsTableMenuListener.java,v 1.12 2003/12/04 08:47:29 lemmy Exp $
 *
 */
public class FriendsTableMenuListener extends GTableMenuListener implements ISelectionChangedListener {
    private List selectedClients = new ArrayList();

    /**
     * Creates a new TableMenuListener
     * @param tableViewer The parent TableViewer
     * @param core The CoreCommunication supporting this with data
     * @param messagesTab The MessagesTab
     */
    public FriendsTableMenuListener(FriendsTableView fTableView) {
        super(fTableView);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    public void selectionChanged(SelectionChangedEvent event) {
        IStructuredSelection sSel = (IStructuredSelection) event.getSelection();
        Object o = sSel.getFirstElement();
        selectedClients.clear();

        for (Iterator it = sSel.iterator(); it.hasNext();) {
            o = it.next();

            if (o instanceof ClientInfo)
                selectedClients.add(o);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
     */
    public void menuAboutToShow(IMenuManager menuManager) {
        if (selectedClients.size() > 0) {
            menuManager.add(new SendMessageAction());
            menuManager.add(new RemoveFriendAction());
        }
            
        if (gView.getTable().getItemCount() > 0)
            menuManager.add(new RemoveAllFriendsAction());

        menuManager.add(new AddByIPAction());
    }

    /**
     * RemoveFriendAction
     */
    private class RemoveFriendAction extends Action {
        public RemoveFriendAction() {
            super();

            String num = ((selectedClients.size() > 1) ? (" (" + selectedClients.size() + ")") : "");
            setText(G2GuiResources.getString("FR_MENU_REMOVE_FRIEND") + num);
            setImageDescriptor(G2GuiResources.getImageDescriptor("MessagesButtonSmallBW"));
        }

        public void run() {
            for (int i = 0; i < selectedClients.size(); i++) {
                ClientInfo clientInfo = (ClientInfo) selectedClients.get(i);
                ClientInfo.removeFriend(gView.getCore(), clientInfo.getClientid());
            }
        }
    }

    /**
     * RemoveAllFriendsAction
     */
    private class RemoveAllFriendsAction extends Action {
        public RemoveAllFriendsAction() {
            super(G2GuiResources.getString("FR_MENU_REMOVE_ALL_FRIENDS"));
            setImageDescriptor(G2GuiResources.getImageDescriptor("MessagesButtonSmallBW"));
        }

        public void run() {
            ClientInfo.removeAllFriends(gView.getCore());
        }
    }

    /**
     * SendMessageAction
     */
    private class SendMessageAction extends Action {
        public SendMessageAction() {
            super();

            String num = ((selectedClients.size() > 1) ? (" (" + selectedClients.size() + ")") : "");
            setText(G2GuiResources.getString("FR_MENU_SEND_MESSAGE") + num);
            setImageDescriptor(G2GuiResources.getImageDescriptor("resume"));
        }

        public void run() {
            for (int i = 0; i < selectedClients.size(); i++) {
                ClientInfo clientInfo = (ClientInfo) selectedClients.get(i);
                MessagesTab messagesTab = (MessagesTab) gView.getViewFrame().getGuiTab();
                messagesTab.openTab(clientInfo);
            }
        }
    }

    /**
     * AddByIPAction
     */
    private class AddByIPAction extends Action {
        public AddByIPAction() {
            super(G2GuiResources.getString("FR_MENU_ADD_BY_IP"));
            setImageDescriptor(G2GuiResources.getImageDescriptor("MessagesButtonSmall"));
        }

        public void run() {
            AddFriend a = new AddFriend(gView.getShell(), gView.getCore());
            a.open();
        }
    }
}


/*
$Log: FriendsTableMenuListener.java,v $
Revision 1.12  2003/12/04 08:47:29  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.11  2003/11/29 14:29:27  zet
small viewframe updates

Revision 1.10  2003/11/23 17:58:03  lemmy
removed dead/unused code

Revision 1.9  2003/11/14 19:06:39  zet
use Dialog / fix #1087 (fox)

Revision 1.8  2003/11/04 20:38:39  zet
update for transparent gifs

Revision 1.7  2003/10/15 19:39:57  zet
icons

Revision 1.6  2003/09/20 01:20:26  zet
*** empty log message ***

Revision 1.5  2003/09/18 09:54:45  lemmy
checkstyle

Revision 1.4  2003/08/30 14:25:57  zet
multi select

Revision 1.3  2003/08/29 21:02:45  zet
Add friend by ip

Revision 1.2  2003/08/23 15:21:37  zet
remove @author

Revision 1.1  2003/08/23 09:47:46  lemmy
just rename

Revision 1.2  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.1  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging



*/
