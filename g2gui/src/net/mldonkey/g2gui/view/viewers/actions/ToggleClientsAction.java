/*
 * Copyright 2003
 * g2gui Team
 *
 *
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package net.mldonkey.g2gui.view.viewers.actions;

import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.transfer.downloadTable.DownloadTableTreeViewer;
import net.mldonkey.g2gui.view.viewers.GViewer;

import org.eclipse.jface.action.Action;


/**
 * ToggleClientsAction
 *
 * @version $Id: ToggleClientsAction.java,v 1.2 2003/10/29 16:56:21 lemmster Exp $
 *
 */
public class ToggleClientsAction extends Action {
    private GViewer gViewer;

    public ToggleClientsAction(GViewer gViewer) {
        super();
        this.gViewer = gViewer;

        if ( ( ( DownloadTableTreeViewer ) gViewer ).clientsDisplayed()) {
            setText(G2GuiResources.getString("MISC_HIDE") + G2GuiResources.getString("TT_Clients"));
            setImageDescriptor(G2GuiResources.getImageDescriptor("minus"));
        } 
        else {
            setText(G2GuiResources.getString("MISC_SHOW") + G2GuiResources.getString("TT_Clients"));
            setImageDescriptor(G2GuiResources.getImageDescriptor("plus"));
        }
    }

    public void run() {
        ( ( DownloadTableTreeViewer ) gViewer ).toggleClientsTable();
    }
}


/*
$Log: ToggleClientsAction.java,v $
Revision 1.2  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

Revision 1.1  2003/10/22 17:17:30  zet
common actions

*/
