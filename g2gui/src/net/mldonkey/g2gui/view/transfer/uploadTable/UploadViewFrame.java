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
package net.mldonkey.g2gui.view.transfer.uploadTable;

import net.mldonkey.g2gui.view.GuiTab;
import net.mldonkey.g2gui.view.helper.SashViewFrame;

import org.eclipse.swt.custom.SashForm;


/**
 * UploadsViewFrame
 *
 * @version $Id: UploadViewFrame.java,v 1.5 2004/01/22 21:28:03 psy Exp $
 *
 */
public class UploadViewFrame extends SashViewFrame {
    public UploadViewFrame(SashForm parentSashForm, String prefString, String prefImageString,
        GuiTab guiTab) {
        super(parentSashForm, prefString, prefImageString, guiTab);

        gView = new UploadTableView(this);
        createPaneListener(new UploadPaneListener(this));
    }
}


/*
$Log: UploadViewFrame.java,v $
Revision 1.5  2004/01/22 21:28:03  psy
renamed "uploads" to "shares" and moved it to a tab of its own.
"uploaders" are now called "uploads" for improved naming-consistency

Revision 1.2  2003/11/28 01:06:21  zet
not much- slowly expanding viewframe - will continue later

Revision 1.1  2003/11/26 07:43:15  zet
quick attempt at an uploaders table w/proto 19 - still in progress...

Revision 1.1  2003/11/24 01:33:27  zet
move some classes

*/
