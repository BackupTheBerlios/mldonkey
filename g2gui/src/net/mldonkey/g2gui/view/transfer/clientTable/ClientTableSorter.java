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
import net.mldonkey.g2gui.view.viewers.GTableSorter;

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;


/**
 * ClientTableSorter
 *
 * @version $Id: ClientTableSorter.java,v 1.5 2003/10/22 01:38:19 zet Exp $
 *
 */
public class ClientTableSorter extends GTableSorter {
    public ClientTableSorter(ClientTableViewer cTableViewer) {
        super(cTableViewer);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public int compare(Viewer viewer, Object obj1, Object obj2) {
        switch (tableViewer.getColumnIDs()[ columnIndex ]) {
        case ClientTableViewer.STATE:

            ClientInfo clientInfo1 = (ClientInfo) obj1;
            ClientInfo clientInfo2 = (ClientInfo) obj2;

            if (clientInfo1.getState().getState() == EnumState.CONNECTED_DOWNLOADING) {
                return -1;
            } else if (clientInfo2.getState().getState() == EnumState.CONNECTED_DOWNLOADING) {
                return 1;
            } else if ((clientInfo1.getState().getRank() != 0) && (clientInfo2.getState().getRank() != 0)) {
                return compareIntegers(clientInfo1.getState().getRank(), clientInfo2.getState().getRank());
            } else if (clientInfo1.getState().getRank() != 0) {
                return -1;
            } else if (clientInfo2.getState().getRank() != 0) {
                return 1;
            }

        // else fall through
        default:

            String s1;
            String s2;

            IBaseLabelProvider prov = ((ContentViewer) viewer).getLabelProvider();
            ClientTableLabelProvider lprov = (ClientTableLabelProvider) ((TableViewer) viewer).getLabelProvider();

            s1 = lprov.getColumnText(obj1, columnIndex);
            s2 = lprov.getColumnText(obj2, columnIndex);

            return compareStrings(s1, s2);
        }
    }
}


/*
$Log: ClientTableSorter.java,v $
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

Revision 1.5  2003/09/08 19:50:31  lemmster
just repaired the log

Revision 1.4  2003/08/23 15:21:37  zet
remove @author

*/
