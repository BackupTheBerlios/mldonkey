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
import net.mldonkey.g2gui.view.viewers.GSorter;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;


/**
 * FriendsTableSorter
 *
 *
 * @version $Id: FriendsTableSorter.java,v 1.7 2003/12/04 08:47:29 lemmy Exp $
 *
 */
public class FriendsTableSorter extends GSorter {
    /**
     * Creates a new viewer sorter
     */
    public FriendsTableSorter(FriendsTableView fTableView) {
        super(fTableView);
    }

    public int compare(Viewer viewer, Object obj1, Object obj2) {
        ClientInfo clientInfo1 = (ClientInfo) obj1;
        ClientInfo clientInfo2 = (ClientInfo) obj2;

        switch (cViewer.getColumnIDs()[ columnIndex ]) {
        case FriendsTableView.NAME:
            return compareStrings(clientInfo1.getClientName(), clientInfo2.getClientName());

        case FriendsTableView.STATE:

            String s1;
            String s2;

            FriendsTableLabelProvider lprov = (FriendsTableLabelProvider) ((TableViewer) viewer).getLabelProvider();

            s1 = lprov.getColumnText(obj1, columnIndex);
            s2 = lprov.getColumnText(obj2, columnIndex);

            return compareStrings(s1, s2);

        default:
            return 0;
        }
    }
}


/*
$Log: FriendsTableSorter.java,v $
Revision 1.7  2003/12/04 08:47:29  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.6  2003/11/29 19:46:19  zet
add state column (like mlgui)

Revision 1.5  2003/11/29 14:29:27  zet
small viewframe updates

Revision 1.4  2003/09/20 01:20:26  zet
*** empty log message ***

Revision 1.3  2003/09/18 09:54:45  lemmy
checkstyle

Revision 1.2  2003/08/23 15:21:37  zet
remove @author

Revision 1.1  2003/08/23 09:47:46  lemmy
just rename

Revision 1.1  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging



*/
