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

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.table.GTableLabelProvider;

import org.eclipse.swt.graphics.Image;


/**
 * FriendsTableLabelProvider
 *
 *
 * @version $Id: FriendsTableLabelProvider.java,v 1.8 2003/11/29 19:46:19 zet Exp $
 */
public class FriendsTableLabelProvider extends GTableLabelProvider {
    public FriendsTableLabelProvider(FriendsTableView fTableView) {
        super(fTableView);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
     */
    public Image getColumnImage(Object element, int columnIndex) {
        ClientInfo clientInfo = (ClientInfo) element;

        switch (cViewer.getColumnIDs()[ columnIndex ]) {
        case FriendsTableView.NAME:
            return (G2GuiResources.getImage(clientInfo.isConnected() ? "MessagesButtonSmall"
                                                                     : "MessagesButtonSmallBW"));

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
        case FriendsTableView.NAME:
            return clientInfo.getClientName();

        case FriendsTableView.STATE:
            return "" + clientInfo.getDetailedClientActivity();

        default:
            return "??";
        }
    }
}


/*
$Log: FriendsTableLabelProvider.java,v $
Revision 1.8  2003/11/29 19:46:19  zet
add state column (like mlgui)

Revision 1.7  2003/11/29 14:29:27  zet
small viewframe updates

Revision 1.6  2003/11/04 20:38:39  zet
update for transparent gifs

Revision 1.5  2003/09/18 09:54:45  lemmster
checkstyle

Revision 1.4  2003/08/31 15:37:30  zet
friend icons

Revision 1.3  2003/08/25 21:18:43  zet
localise/update friendstab

Revision 1.2  2003/08/23 15:21:37  zet
remove @author

Revision 1.1  2003/08/23 09:47:46  lemmster
just rename

Revision 1.4  2003/08/22 21:10:57  lemmster
replace $user$ with $Author: zet $

Revision 1.3  2003/08/17 23:13:42  zet
centralize resources, move images

Revision 1.2  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.1  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging



*/
