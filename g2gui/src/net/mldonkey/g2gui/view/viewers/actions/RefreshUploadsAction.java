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

import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.view.viewers.GTableViewer;

import org.eclipse.jface.action.Action;


/**
 * RefreshUploadsAction
 *
 * @version $Id: RefreshUploadsAction.java,v 1.1 2003/10/22 17:17:30 zet Exp $
 *
 */
public class RefreshUploadsAction extends Action {
    private GTableViewer gTableViewer;

    public RefreshUploadsAction(GTableViewer gTableViewer) {
        super("Refresh");
        this.gTableViewer = gTableViewer;
    }

    public void run() {
        Message refresh = new EncodeMessage(Message.S_REFRESH_UPLOAD_STATS);
        refresh.sendMessage(gTableViewer.getCore());
    }
}


/*
$Log: RefreshUploadsAction.java,v $
Revision 1.1  2003/10/22 17:17:30  zet
common actions

*/
