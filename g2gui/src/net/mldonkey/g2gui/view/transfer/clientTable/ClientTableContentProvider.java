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
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.transfer.TreeClientInfo;
import net.mldonkey.g2gui.view.viewers.CustomTableViewer;
import net.mldonkey.g2gui.view.viewers.table.GTableContentProvider;

import org.eclipse.jface.viewers.Viewer;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Display;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;


/**
 *
 * ClientTableContentProvider
 *
 * @version $Id: ClientTableContentProvider.java,v 1.14 2004/03/27 22:04:27 psy Exp $
 *
 */
public class ClientTableContentProvider extends GTableContentProvider implements Observer {
    private long lastUpdateTime;

    public ClientTableContentProvider(ClientTableView cTableViewer, CLabel headerCLabel) {
        super(cTableViewer);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement) {
        FileInfo fileInfo = (FileInfo) inputElement;

        return fileInfo.getClientInfoWeakMap().getKeySet().toArray();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#
     * inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        tableViewer = (CustomTableViewer) viewer;

        if (oldInput != null) {
            FileInfo oldFileInfo = (FileInfo) oldInput;
            oldFileInfo.deleteObserver(this);
        }

        if (newInput != null) {
            FileInfo newFileInfo = (FileInfo) newInput;
            newFileInfo.addObserver(this);
            updateHeader(newInput);
        }
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable o, final Object obj) {
        if (obj instanceof ClientInfo || obj instanceof TreeClientInfo)
            Display.getDefault().asyncExec(new Runnable() {
                    public void run() {
                        if (gView.isActive())
                            refreshTable(obj);
                    }
                });

    }

    // delay for 5 seconds to prevent too much flicker
    public void refreshTable(Object obj) {
        ClientInfo clientInfo = null;

        if (obj instanceof ClientInfo)
            clientInfo = (ClientInfo) obj;
        else if (obj instanceof TreeClientInfo)
            clientInfo = ((TreeClientInfo) obj).getClientInfo();

        if ((tableViewer != null) && !tableViewer.getTable().isDisposed()) {
            if (System.currentTimeMillis() > (lastUpdateTime + 5000)) {
                tableViewer.refresh();
                lastUpdateTime = System.currentTimeMillis();
                updateHeader(tableViewer.getInput());
            } else
                tableViewer.update(clientInfo, new String[] { "z" }); // requires a property string
        }
    }

    public void updateHeader(Object input) {
        if (input == null)
            return;

        FileInfo fileInfo = (FileInfo) input;
        int totalClients = 0;
        int totalConnected = 0;

        synchronized (fileInfo.getClientInfoWeakMap()) {
            for (Iterator i = fileInfo.getClientInfoWeakMap().getKeySet().iterator(); i.hasNext();) {
                ClientInfo clientInfo = (ClientInfo) i.next();

                if (clientInfo.isConnected())
                    totalConnected++;

                totalClients++;
            }
        }

        gView.getViewFrame().updateCLabelText(G2GuiResources.getString("TT_Clients") + ": " +
            totalConnected + " / " + totalClients + " " +
            G2GuiResources.getString("ENS_CONNECTED").toLowerCase() + 
			" [" + fileInfo.getName() + "]"	);
    }
}


/*
$Log: ClientTableContentProvider.java,v $
Revision 1.14  2004/03/27 22:04:27  psy
show filename in clients sash header

Revision 1.13  2003/12/04 08:47:30  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.12  2003/11/29 01:51:53  zet
a few more viewframe changes.. will continue later.

Revision 1.11  2003/11/27 21:42:33  zet
integrate ViewFrame a little more.. more to come.

Revision 1.10  2003/11/26 23:32:35  zet
sync

Revision 1.9  2003/11/14 20:29:50  zet
null check (maybe #1086, not sure)

Revision 1.8  2003/11/08 22:47:15  zet
update client table header

Revision 1.7  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.6  2003/10/31 13:16:33  lemmy
Rename Viewer -> Page
Constructors changed

Revision 1.5  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.4  2003/10/22 01:38:19  zet
add column selector to server/search (might not be finished yet..)

Revision 1.3  2003/10/12 15:58:30  zet
rewrite downloads table & more..

Revision 1.2  2003/09/21 23:39:31  zet
displayTableColors preference

Revision 1.1  2003/09/20 14:39:21  zet
move transfer package

Revision 1.7  2003/09/18 14:11:01  zet
revert

Revision 1.5  2003/09/13 22:26:44  zet
weak sets & !rawrate

Revision 1.4  2003/08/23 15:21:37  zet
remove @author

Revision 1.3  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.2  2003/08/22 21:17:25  lemmy
replace $user$ with $Author: psy $

Revision 1.1  2003/08/20 14:58:43  zet
sources clientinfo viewer



*/
