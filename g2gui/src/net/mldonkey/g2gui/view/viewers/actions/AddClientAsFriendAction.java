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
package net.mldonkey.g2gui.view.viewers.actions;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.action.Action;


/**
 * AddClientAsFriendAction
 *
 * @version $Id: AddClientAsFriendAction.java,v 1.2 2003/11/04 20:39:11 zet Exp $
 *
 */
public class AddClientAsFriendAction extends Action {
    private CoreCommunication core;
    private ClientInfo[] clientInfoArray;

    public AddClientAsFriendAction(CoreCommunication core, ClientInfo[] clientInfoArray) {
        super(G2GuiResources.getString("TT_DOWNLOAD_MENU_ADD_FRIEND"));
        setImageDescriptor(G2GuiResources.getImageDescriptor("MessagesButtonSmall"));

        this.core = core;
        this.clientInfoArray = clientInfoArray;
    }

    public void run() {
        for (int i = 0; i < clientInfoArray.length; i++) {
            ClientInfo.addFriend(core, clientInfoArray[ i ].getClientid());
        }
    }
}


/*
$Log: AddClientAsFriendAction.java,v $
Revision 1.2  2003/11/04 20:39:11  zet
update for transparent gifs

Revision 1.1  2003/10/24 21:26:47  zet
common actions

*/
