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

import org.eclipse.jface.action.Action;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;


/**
 * CopyED2KLinkToClipboardAction
 *
 * @version $Id: CopyED2KLinkToClipboardAction.java,v 1.2 2003/10/22 20:47:21 zet Exp $
 *
 */
public class CopyED2KLinkToClipboardAction extends Action {
    private boolean useHTML = false;
    private String[] links;

    public CopyED2KLinkToClipboardAction(boolean useHTML, String[] links) {
        super();
        this.useHTML = useHTML;
        this.links = links;
        setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_ED2K_COPY") + (useHTML ? " (html)" : ""));
        setImageDescriptor(G2GuiResources.getImageDescriptor("edonkey"));
    }

    public void run() {
        Clipboard clipBoard = new Clipboard(Display.getDefault());
        String link = "";
        String fileName;

        for (int i = 0; i < links.length; i++) {
            if (useHTML) {
                // 13 = "ed2k://|file|"
                fileName = links[ i ].substring(13, links[ i ].indexOf("|", 13));

                link += ("<a href=\"" + links[ i ] + "\">" + fileName + "</a>");
            } else {
                link += links[ i ];
            }

            link += (SWT.getPlatform().equals("win32") ? "\r\n" : "\n");
        }

        clipBoard.setContents(new Object[] { link }, new Transfer[] { TextTransfer.getInstance() });
        clipBoard.dispose();
    }
}


/*
$Log: CopyED2KLinkToClipboardAction.java,v $
Revision 1.2  2003/10/22 20:47:21  zet
minor

Revision 1.1  2003/10/22 20:38:35  zet
common actions

*/
