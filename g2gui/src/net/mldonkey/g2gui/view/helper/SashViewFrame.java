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

import org.eclipse.swt.custom.SashForm;


/**
 * SashViewFrame
 *
 * @version $Id: SashViewFrame.java,v 1.2 2003/11/29 01:51:53 zet Exp $
 *
 */
public class SashViewFrame extends ViewFrame {
    protected SashForm parentSashForm;

    public SashViewFrame(SashForm parentSashForm, String prefString, String prefImageString,
        GuiTab guiTab) {
        super(parentSashForm, prefString, prefImageString, guiTab);
        this.parentSashForm = parentSashForm;
    }

    /**
     * @param gPaneListener
     */
    public void createPaneListener(SashViewFrameListener sashViewFrameListener) {
        setupPaneListener(sashViewFrameListener);
        cLabel.addMouseListener(new MaximizeSashMouseAdapter(cLabel, menuManager,
                getParentSashForm(), getControl()));
    }

    public SashForm getParentSashForm() {
        return parentSashForm;
    }
 
}


/*
$Log: SashViewFrame.java,v $
Revision 1.2  2003/11/29 01:51:53  zet
a few more viewframe changes.. will continue later.

Revision 1.1  2003/11/28 01:06:21  zet
not much- slowly expanding viewframe - will continue later



*/
