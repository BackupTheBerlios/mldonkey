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
package net.mldonkey.g2gui.view.transfer.clientTable;

import net.mldonkey.g2gui.view.GuiTab;
import net.mldonkey.g2gui.view.helper.ViewFrame;
import net.mldonkey.g2gui.view.transfer.downloadTable.DownloadTableTreeView;
import net.mldonkey.g2gui.view.viewers.GView;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.widgets.Composite;


/**
 * ClientViewFrame
 *
 * @version $Id: ClientViewFrame.java,v 1.1 2003/11/24 01:33:27 zet Exp $
 *
 */
public class ClientViewFrame extends ViewFrame {
    public ClientViewFrame(SashForm parentSashForm, String prefString, String prefImageString,
        GuiTab guiTab, final GView downloadGView) {
        super(parentSashForm, prefString, prefImageString, guiTab);

        gView = new ClientTableView(this);
        createPaneListener(new ClientPaneListener(this));

        childComposite.addControlListener(new ControlAdapter() {
                public void controlResized(ControlEvent e) {
                    Composite c = (Composite) e.widget;
                    ((DownloadTableTreeView) downloadGView).updateClientsTable((c.getBounds().width > 0) &&
                        (c.getBounds().height > 0) && (downloadGView != null));
                }
            });
    }
}


/*
$Log: ClientViewFrame.java,v $
Revision 1.1  2003/11/24 01:33:27  zet
move some classes

*/
