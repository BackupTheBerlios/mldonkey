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
import net.mldonkey.g2gui.view.transfer.TreeClientInfo;
import net.mldonkey.g2gui.view.viewers.CustomTableViewer;
import net.mldonkey.g2gui.view.viewers.table.GTableContentProvider;

import org.eclipse.jface.viewers.Viewer;

import org.eclipse.swt.widgets.Display;

import java.util.Observable;
import java.util.Observer;


/**
 *
 * ClientTableContentProvider
 *
 * @version $Id: ClientTableContentProvider.java,v 1.5 2003/10/31 07:24:01 zet Exp $
 *
 */
public class ClientTableContentProvider extends GTableContentProvider implements Observer {
    private long lastUpdateTime;

    public ClientTableContentProvider(ClientTableViewer cTableViewer) {
        super(cTableViewer);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement) {
        FileInfo fileInfo = (FileInfo) inputElement;

        return fileInfo.getClientInfos().keySet().toArray();
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
        }
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable o, final Object obj) {
        if (obj instanceof ClientInfo || obj instanceof TreeClientInfo) {
            Display.getDefault().asyncExec(new Runnable() {
                    public void run() {
                        refreshTable(obj);
                    }
                });
        }
    }

    // delay for 5 seconds to prevent too much flicker
    public void refreshTable(Object obj) {
        ClientInfo clientInfo = null;

        if (obj instanceof ClientInfo) {
            clientInfo = (ClientInfo) obj;
        } else if (obj instanceof TreeClientInfo) {
            clientInfo = ((TreeClientInfo) obj).getClientInfo();
        }

        if ((tableViewer != null) && !tableViewer.getTable().isDisposed()) {
            if (System.currentTimeMillis() > (lastUpdateTime + 5000)) {
                tableViewer.refresh();
                lastUpdateTime = System.currentTimeMillis();
            } else {
                tableViewer.update(clientInfo, new String[] { "z" }); // requires a property string
            }
        }
    }
}


/*
$Log: ClientTableContentProvider.java,v $
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

Revision 1.2  2003/08/22 21:17:25  lemmster
replace $user$ with $Author: zet $

Revision 1.1  2003/08/20 14:58:43  zet
sources clientinfo viewer



*/
