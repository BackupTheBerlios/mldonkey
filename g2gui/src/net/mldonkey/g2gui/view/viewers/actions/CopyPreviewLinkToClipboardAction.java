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
 * CopyPreviewLinkToClipboardAction
 *
 * @author $user$
 * @version $Id: CopyPreviewLinkToClipboardAction.java,v 1.2 2004/03/25 20:46:58 psy Exp $ 
 *
 */
public class CopyPreviewLinkToClipboardAction extends Action {
	private int[] fileIDs;
	private String baseURL;
	
	public CopyPreviewLinkToClipboardAction( String baseURL, int[] fileIDs ) {
	    setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_PREVIEW_COPY"));
	    setImageDescriptor(G2GuiResources.getImageDescriptor("preview"));

	    this.fileIDs = fileIDs;
		this.baseURL = baseURL;
	}

	public void run() {
		String link = "";
		Clipboard clipBoard = new Clipboard(Display.getDefault());
	
	    for (int i = 0; i < fileIDs.length; i++) {
            link += baseURL + fileIDs[ i ];
            if (fileIDs.length - 1 > i)
            	link += (SWT.getPlatform().equals("win32") ? "\r\n" : "\n");
	    }
	    
	    if (link != "" || link != null)
	    	clipBoard.setContents(new Object[] { link }, new Transfer[] { TextTransfer.getInstance() });
	    clipBoard.dispose();
	}
}
/*
$Log: CopyPreviewLinkToClipboardAction.java,v $
Revision 1.2  2004/03/25 20:46:58  psy
*** empty log message ***

Revision 1.1  2004/03/25 19:17:18  psy
added copy preview link to clipboard action

*/