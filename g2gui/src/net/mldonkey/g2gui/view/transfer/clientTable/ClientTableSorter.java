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
import net.mldonkey.g2gui.view.viewers.GSorter;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;


/**
 * ClientTableSorter
 *
 * @version $Id: ClientTableSorter.java,v 1.15 2003/12/04 08:47:30 lemmy Exp $
 *
 */
public class ClientTableSorter extends GSorter {
    public ClientTableSorter(ClientTableView cTableViewer) {
        super(cTableViewer);
    }

    /* (non-Javadoc)
                     * @see net.mldonkey.g2gui.view.viewers.GSorter#sortOrder(int)
                     */
    public boolean sortOrder(int columnIndex) {
        switch (cViewer.getColumnIDs()[ columnIndex ]) {
        case ClientTableView.UPLOADED:
        case ClientTableView.DOWNLOADED:
        case ClientTableView.CONNECT_TIME:
        case ClientTableView.SOCK_ADDR:
        case ClientTableView.PORT:
            return false;

        default:
            return true;
        }
    }

    /* (non-Javadoc)
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

        case ClientTableView.SOCK_ADDR:
            return compareAddrs(clientInfo1.getClientKind().getAddr(),
                clientInfo2.getClientKind().getAddr());

        case ClientTableView.PORT:
            return compareIntegers(clientInfo1.getClientKind().getPort(),
                clientInfo2.getClientKind().getPort());

        case ClientTableView.CONNECT_TIME:
            return compareIntegers(clientInfo1.getClientConnectTime(),
                clientInfo2.getClientConnectTime());

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

            ClientTableLabelProvider lprov = (ClientTableLabelProvider) ((TableViewer) viewer).getLabelProvider();

            s1 = lprov.getColumnText(obj1, columnIndex);
            s2 = lprov.getColumnText(obj2, columnIndex);

            return compareStrings(s1, s2);
        }
    }
}


/*
$Log: ClientTableSorter.java,v $
Revision 1.15  2003/12/04 08:47:30  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.14  2003/12/01 16:39:25  zet
set default sort order for specific columns

Revision 1.13  2003/12/01 13:28:16  zet
add port info

Revision 1.12  2003/11/30 23:42:56  zet
updates for latest mldonkey cvs

Revision 1.11  2003/11/28 22:37:53  zet
coalesce addr use

Revision 1.10  2003/11/26 07:43:15  zet
quick attempt at an uploaders table w/proto 19 - still in progress...

Revision 1.9  2003/11/23 17:58:03  lemmy
removed dead/unused code

Revision 1.8  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.7  2003/10/31 13:16:32  lemmy
Rename Viewer -> Page
Constructors changed

Revision 1.6  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.5  2003/10/22 01:38:19  zet
add column selector to server/search (might not be finished yet..)

Revision 1.4  2003/10/19 21:38:54  zet
columnselector support

Revision 1.3  2003/10/12 15:58:30  zet
rewrite downloads table & more..

Revision 1.2  2003/09/21 23:39:31  zet
displayTableColors preference

Revision 1.1  2003/09/20 14:39:21  zet
move transfer package

Revision 1.7  2003/09/18 14:11:01  zet
revert

Revision 1.5  2003/09/08 19:50:31  lemmy
just repaired the log

Revision 1.4  2003/08/23 15:21:37  zet
remove @author

*/
