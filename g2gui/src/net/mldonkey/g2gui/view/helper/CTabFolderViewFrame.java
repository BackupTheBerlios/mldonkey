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
package net.mldonkey.g2gui.view.helper;

import net.mldonkey.g2gui.view.GuiTab;
import net.mldonkey.g2gui.view.viewers.GView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.SashForm;


/**
 * CTabFolderViewFrame
 *
 * @version $Id: CTabFolderViewFrame.java,v 1.1 2003/11/29 17:02:27 zet Exp $
 *
 */
public class CTabFolderViewFrame extends SashViewFrame {
    protected CTabFolder cTabFolder;

    public CTabFolderViewFrame(SashForm parentSashForm, String prefString, String prefImageString,
        GuiTab guiTab) {
        super(parentSashForm, prefString, prefImageString, guiTab);
        this.cTabFolder = new CTabFolder(childComposite, SWT.NONE);
    }

    /**
     * @param gPaneListener
     */
    public void createPaneListener(CTabFolderViewFrameListener cTabFolderViewFrameListener) {
        setupPaneListener(cTabFolderViewFrameListener);

        cLabel.addMouseListener(new MaximizeSashMouseAdapter(cLabel, menuManager,
                getParentSashForm(), getControl()));
    }

    public CTabFolder getCTabFolder() {
        return cTabFolder;
    }

    public GView getGView() {
        if ((cTabFolder.getSelection() != null) &&
                (cTabFolder.getSelection().getData("gView") != null))
            return (GView) cTabFolder.getSelection().getData("gView");

        return null;
    }
}


/*
$Log: CTabFolderViewFrame.java,v $
Revision 1.1  2003/11/29 17:02:27  zet
more viewframes.. will continue later.

*/
