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
package net.mldonkey.g2gui.view.viewers;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.view.viewers.actions.NetworkFilterAction;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.MenuManager;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;


/**
 * GPaneListener
 *
 * @version $Id: GPaneListener.java,v 1.2 2003/10/31 07:24:01 zet Exp $
 *
 */
public abstract class GPaneListener implements IMenuListener, DisposeListener {
    protected IGViewer gViewer;
    protected CoreCommunication core;

    public GPaneListener(IGViewer gViewer, CoreCommunication core) {
        this.gViewer = gViewer;
        this.core = core;

        // for stats the gViewer is null
        if (gViewer != null) {
            this.gViewer.addDisposeListener(this);
        }
    }

    protected void createNetworkFilterSubMenu(MenuManager menu) {
        NetworkInfo[] networks = core.getNetworkInfoMap().getNetworks();

        for (int i = 0; i < networks.length; i++) {
            NetworkInfo network = networks[ i ];

            if (network.isEnabled() && network.isSearchable()) {
                menu.add(new NetworkFilterAction(gViewer, network));
            }
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
     */
    public void widgetDisposed(DisposeEvent arg0) {
    }
}


/*
$Log: GPaneListener.java,v $
Revision 1.2  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.1  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

*/
