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

import net.mldonkey.g2gui.view.PaneGuiTab;
import net.mldonkey.g2gui.view.viewers.GPaneListener;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Control;


/**
 * ViewFrameListener
 *
 * @version $Id: ViewFrameListener.java,v 1.2 2003/11/24 01:33:27 zet Exp $
 *
 */
public abstract class ViewFrameListener extends GPaneListener {
    protected SashForm sashForm;
    protected Control control;
    protected ViewFrame viewFrame;

    public ViewFrameListener(ViewFrame viewFrame) {
        // GPaneListener ? 
        super((PaneGuiTab) viewFrame.getGuiTab(), viewFrame.getGView().getCore()); 
        this.viewFrame = viewFrame;
        this.sashForm = viewFrame.getParentSashForm();
        this.control = viewFrame.getControl();
        this.gView = viewFrame.getGView();
    }

    public void menuAboutToShow() {
    }
}


/*
$Log: ViewFrameListener.java,v $
Revision 1.2  2003/11/24 01:33:27  zet
move some classes

Revision 1.1  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

*/
