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

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.table.GTableLabelProvider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;


/**
 *
 * ClientTableLabelProvider
 *
 * @version $Id: ClientTableLabelProvider.java,v 1.17 2003/12/04 08:47:30 lemmy Exp $
 *
 */
public class ClientTableLabelProvider extends GTableLabelProvider implements ITableLabelProvider {
    public ClientTableLabelProvider(ClientTableView cTableViewer) {
        super(cTableViewer);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
     */
    public Image getColumnImage(Object element, int columnIndex) {
        ClientInfo clientInfo = (ClientInfo) element;

        switch (cViewer.getColumnIDs()[ columnIndex ]) {
        case ClientTableView.STATE:
            return G2GuiResources.getClientImage((EnumState) clientInfo.getState().getState());

        case ClientTableView.NETWORK:
            return G2GuiResources.getNetworkImage(clientInfo.getClientnetworkid().getNetworkType());

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
        case ClientTableView.STATE:
            return "" + clientInfo.getDetailedClientActivity();

        case ClientTableView.NAME:
            return "" + clientInfo.getClientName();

        case ClientTableView.NETWORK:
            return "" + clientInfo.getClientnetworkid().getNetworkName();

        case ClientTableView.KIND:
            return "" + clientInfo.getClientConnection();

        case ClientTableView.SOFTWARE:
            return clientInfo.getClientSoftware();

        case ClientTableView.UPLOADED:
            return clientInfo.getUploadedString();

        case ClientTableView.DOWNLOADED:
            return clientInfo.getDownloadedString();

        case ClientTableView.SOCK_ADDR:
            return clientInfo.getClientKind().getAddr().toString();
            
        case ClientTableView.PORT:
            return "" + clientInfo.getClientKind().getPort();
            
        case ClientTableView.CONNECT_TIME:
            return clientInfo.getClientConnectTimeString();

        default:
            return "";
        }
    }
}


/*
$Log: ClientTableLabelProvider.java,v $
Revision 1.17  2003/12/04 08:47:30  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.16  2003/12/01 13:28:16  zet
add port info

Revision 1.15  2003/11/30 23:42:56  zet
updates for latest mldonkey cvs

Revision 1.14  2003/11/29 13:01:11  lemmy
Addr.getString() renamed to the more natural word name Addr.toString()

Revision 1.13  2003/11/28 22:37:53  zet
coalesce addr use

Revision 1.12  2003/11/28 08:23:28  lemmy
use Addr instead of String

Revision 1.11  2003/11/26 07:43:15  zet
quick attempt at an uploaders table w/proto 19 - still in progress...

Revision 1.10  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.9  2003/10/31 13:16:33  lemmy
Rename Viewer -> Page
Constructors changed

Revision 1.8  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.7  2003/10/22 01:38:19  zet
add column selector to server/search (might not be finished yet..)

Revision 1.6  2003/10/19 21:38:54  zet
columnselector support

Revision 1.5  2003/10/12 15:58:30  zet
rewrite downloads table & more..

Revision 1.4  2003/09/24 03:07:43  zet
add # of active sources column

Revision 1.3  2003/09/23 01:28:23  zet
remove unneeded

Revision 1.2  2003/09/21 23:39:31  zet
displayTableColors preference

Revision 1.1  2003/09/20 14:39:21  zet
move transfer package

Revision 1.5  2003/09/18 14:11:01  zet
revert

Revision 1.3  2003/08/23 15:21:37  zet
remove @author

Revision 1.2  2003/08/22 21:17:25  lemmy
replace $user$ with $Author: lemmy $

Revision 1.1  2003/08/20 14:58:43  zet
sources clientinfo viewer



*/
