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
package net.mldonkey.g2gui.view.server;

import net.mldonkey.g2gui.model.Addr;
import net.mldonkey.g2gui.model.ServerInfo;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.viewers.GSorter;

import org.eclipse.jface.viewers.Viewer;
/**
 * ServerTableSorter
 *
 *
 * @version $Id: ServerTableSorter.java,v 1.8 2003/10/31 16:02:57 zet Exp $
 *
 */
public class ServerTableSorter extends GSorter {
    public ServerTableSorter(ServerTableView sTableViewer) {
        super(sTableViewer);
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public int compare(Viewer viewer, Object obj1, Object obj2) {
        ServerInfo server1 = (ServerInfo) obj1;
        ServerInfo server2 = (ServerInfo) obj2;

        switch (cViewer.getColumnIDs()[ columnIndex ]) {
        case ServerTableView.NETWORK:
            return compareStrings(server1.getNetwork().getNetworkName(), server2.getNetwork().getNetworkName());

        case ServerTableView.NAME:
            return compareStrings(server1.getNameOfServer(), server2.getNameOfServer());

        case ServerTableView.DESCRIPTION:
            return compareStrings(server1.getDescOfServer(), server2.getDescOfServer());

        case ServerTableView.ADDRESS:

            try {
                Addr addr1 = server1.getServerAddress();
                Addr addr2 = server2.getServerAddress();

                if (lastSort) {
                    return addr1.compareTo(addr2);
                } else {
                    return addr2.compareTo(addr1);
                }
            } catch (NullPointerException e) {
                return 0;
            }

        case ServerTableView.PORT:
            return compareIntegers(server1.getServerPort(), server2.getServerPort());

        case ServerTableView.SCORE:
            return compareIntegers(server1.getServerScore(), server2.getServerScore());

        case ServerTableView.USERS:
            return compareIntegers(server1.getNumOfUsers(), server2.getNumOfUsers());

        case ServerTableView.FILES:
            return compareIntegers(server1.getNumOfFilesShared(), server2.getNumOfFilesShared());

        case ServerTableView.STATE:

            EnumState state1 = (EnumState) server1.getConnectionState().getState();
            EnumState state2 = (EnumState) server2.getConnectionState().getState();

            if (lastSort) {
                return state1.compareTo(state2);
            } else {
                return state2.compareTo(state1);
            }

        case ServerTableView.FAVORITE:

            boolean bool1 = server1.isFavorite();
            boolean bool2 = server2.isFavorite();

            if (lastSort) {
                if ((bool1 && bool2) || ((bool1 == false) && (bool2 == false))) {
                    return 0;
                }
            }

            if (bool1 && (bool2 == false)) {
                return 1;
            }

            if ((bool1 == false) && bool2) {
                return -1;
            } else if ((bool2 && bool1) || ((bool2 == false) && (bool1 == false))) {
                return 0;
            }

            if (bool2 && (bool1 == false)) {
                return 1;
            }

            if ((bool2 == false) && bool1) {
                return -1;
            }

        default:
            return 0;
        }
    }
}


/*
$Log: ServerTableSorter.java,v $
Revision 1.8  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.7  2003/10/31 13:16:32  lemmster
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

Revision 1.5  2003/10/22 01:37:55  zet
add column selector to server/search (might not be finished yet..)

Revision 1.4  2003/10/21 17:00:45  lemmster
class hierarchy for tableviewer

Revision 1.3  2003/09/18 11:26:07  lemmster
checkstyle

Revision 1.2  2003/08/23 15:21:37  zet
remove @author

Revision 1.1  2003/08/23 09:46:18  lemmster
superclass TableMenuListener added

Revision 1.4  2003/08/11 19:25:04  lemmstercvs01
bugfix at CleanTable

Revision 1.3  2003/08/07 12:35:31  lemmstercvs01
cleanup, more efficient

Revision 1.2  2003/08/06 17:38:38  lemmstercvs01
some actions still missing. but it should work for the moment

Revision 1.1  2003/08/05 13:50:10  lemmstercvs01
initial commit

*/
