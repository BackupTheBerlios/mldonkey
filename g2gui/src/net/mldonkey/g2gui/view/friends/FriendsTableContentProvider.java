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

import net.mldonkey.g2gui.helper.ObjectWeakMap;
import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

import org.eclipse.swt.custom.CLabel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;


/**
 * TableContentProvider
 *
 *
 * @version $Id: FriendsTableContentProvider.java,v 1.8 2003/11/26 07:42:26 zet Exp $
 */
public class FriendsTableContentProvider implements IStructuredContentProvider, Observer, Runnable {
    private static Object[] EMPTY_ARRAY = new Object[ 0 ];
    private TableViewer viewer;
    private List observedClients = Collections.synchronizedList(new ArrayList());
    private long lastTime = 0;
    private int mustRefresh = 0;
    private CLabel friendsCLabel;

    public FriendsTableContentProvider(CLabel friendsCLabel) {
        this.friendsCLabel = friendsCLabel;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof ObjectWeakMap) {
            Map elementsMap = ((ObjectWeakMap) inputElement).getWeakMap();

            // Don't observe clients we are dumping
            for (int i = 0; i < observedClients.size(); i++) {
                ClientInfo clientInfo = (ClientInfo) observedClients.get(i);

                if (clientInfo != null)
                    clientInfo.deleteObserver(this);
            }

            observedClients.clear();

            // Keep a list of clients we are observing
            Iterator i = elementsMap.keySet().iterator();

            while (i.hasNext()) {
                ClientInfo clientInfo = (ClientInfo) i.next();
                clientInfo.addObserver(this);
                observedClients.add(clientInfo);
            }

            return elementsMap.keySet().toArray();
        } else

            return EMPTY_ARRAY;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    public void dispose() {
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#
     * inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public void inputChanged(Viewer aViewer, Object oldInput, Object newInput) {
        this.viewer = (TableViewer) aViewer;

        if (oldInput != null)
            ((Observable) oldInput).deleteObserver(this);

        if (newInput != null)
            ((Observable) newInput).addObserver(this);
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(final Observable o, final Object obj) {
        if ((viewer == null) || viewer.getTable().isDisposed())
            return;

        if (o instanceof ClientInfo)
            viewer.getTable().getDisplay().asyncExec(new Runnable() {
                    public void run() {
                        if (viewer != null)
                            viewer.update(o, null);
                    }
                });
        else if (o instanceof ObjectWeakMap)
		viewer.getTable().getDisplay().asyncExec(new Runnable() {
			   public void run() {
				   if (viewer != null)
					   runUpdate(o, obj);
			   }
		   });
    }

    public void runUpdate(Observable arg0, Object arg1) {
        if (arg0 instanceof ObjectWeakMap) {
            mustRefresh++;

            if (System.currentTimeMillis() > (lastTime + 2000))
                this.run();
            else {
                if (mustRefresh == 1)
                    viewer.getTable().getDisplay().timerExec(2500, this);
            }
        }
    }

    public void run() {
        if ((viewer != null) && !viewer.getTable().isDisposed()) {
            lastTime = System.currentTimeMillis();
            viewer.refresh();
            setFriendsLabel();
            mustRefresh = 0;
        }
    }

    public void setFriendsLabel() {
        friendsCLabel.setText(G2GuiResources.getString("FR_FRIENDS") + ": " +
            viewer.getTable().getItemCount());
    }
}


/*
$Log: FriendsTableContentProvider.java,v $
Revision 1.8  2003/11/26 07:42:26  zet
small changes

Revision 1.7  2003/11/23 17:58:03  lemmster
removed dead/unused code

Revision 1.6  2003/09/18 09:54:45  lemmster
checkstyle

Revision 1.5  2003/09/13 22:23:10  zet
weak sets

Revision 1.4  2003/09/04 02:35:06  zet
check disposed

Revision 1.3  2003/08/31 15:37:30  zet
friend icons

Revision 1.2  2003/08/23 15:21:37  zet
remove @author

Revision 1.1  2003/08/23 09:47:46  lemmster
just rename

Revision 1.2  2003/08/22 21:10:57  lemmster
replace $user$ with $Author: zet $

Revision 1.1  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging



*/
