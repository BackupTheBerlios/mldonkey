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

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.view.viewers.actions.AddClientAsFriendAction;
import net.mldonkey.g2gui.view.viewers.actions.ClientDetailAction;
import net.mldonkey.g2gui.view.viewers.table.GTableMenuListener;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * ClientTableMenuListener
 *
 *
 * @version $Id: ClientTableMenuListener.java,v 1.14 2003/11/23 17:58:03 lemmster Exp $
 *
 */
public class ClientTableMenuListener extends GTableMenuListener implements ISelectionChangedListener,
    IMenuListener {
    private CoreCommunication core;
    private List selectedClients = new ArrayList();

    /**
     * @param The parent ClientTableViewer
     */
    public ClientTableMenuListener(ClientTableView cTableViewer) {
        super(cTableViewer);
    }

    /* (non-Javadoc)
     * requires tableContentProvider to have been created
     * @see net.mldonkey.g2gui.view.viewers.GTableMenuListener#initialize()
     */
    public void initialize() {
        super.initialize();
        this.core = gView.getCore();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    public void selectionChanged(SelectionChangedEvent event) {
        IStructuredSelection sSel = (IStructuredSelection) event.getSelection();
        Object o;

        selectedClients.clear();

        for (Iterator it = sSel.iterator(); it.hasNext();) {
            o = it.next();

            if (o instanceof ClientInfo) {
                selectedClients.add(o);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
     */
    public void menuAboutToShow(IMenuManager menuManager) {
        if (selectedClients.size() > 0) {
            ClientInfo[] clientInfoArray = new ClientInfo[ selectedClients.size() ];

            for (int i = 0; i < selectedClients.size(); i++) {
                clientInfoArray[ i ] = (ClientInfo) selectedClients.get(i);
            }

            menuManager.add(new AddClientAsFriendAction(core, clientInfoArray));
            menuManager.add(new ClientDetailAction(gView.getShell(),
                    (FileInfo) tableViewer.getInput(), (ClientInfo) selectedClients.get(0), core));
        }
    }
}


/*
$Log: ClientTableMenuListener.java,v $
Revision 1.14  2003/11/23 17:58:03  lemmster
removed dead/unused code

Revision 1.13  2003/11/10 18:57:33  zet
use jface dialogs

Revision 1.12  2003/10/31 16:30:49  zet
minor renames

Revision 1.11  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.10  2003/10/31 13:16:32  lemmster
Rename Viewer -> Page
Constructors changed

Revision 1.9  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.8  2003/10/24 21:26:38  zet
common actions

Revision 1.7  2003/10/23 01:23:06  zet
remove println debug

Revision 1.6  2003/10/22 01:38:19  zet
add column selector to server/search (might not be finished yet..)

Revision 1.5  2003/10/15 18:37:33  zet
icons

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
