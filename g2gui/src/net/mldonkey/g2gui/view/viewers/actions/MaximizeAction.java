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
package net.mldonkey.g2gui.view.viewers.actions;

import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Control;


/**
 * MaximizeAction
 *
 * @version $Id: MaximizeAction.java,v 1.2 2003/11/15 21:15:29 zet Exp $
 *
 */
public class MaximizeAction extends SashAction {
    private Control control;

    public MaximizeAction(SashForm aSashForm, Control control, String showResString) {
        super(aSashForm);
        this.control = control;

        if (sashForm.getMaximizedControl() == null) {
            setText(G2GuiResources.getString("MISC_MAXIMIZE"));
            setImageDescriptor(G2GuiResources.getImageDescriptor("maximize"));
        } else {
            setText(G2GuiResources.getString("MISC_SHOW") + " " +
                G2GuiResources.getString(showResString));
            setImageDescriptor(G2GuiResources.getImageDescriptor("restore"));
        }
    }

    public void run() {
        if (sashForm.getMaximizedControl() == null) {
            sashForm.setMaximizedControl(control);
        } else {
            sashForm.setMaximizedControl(null);
        }
    }
}


/*
$Log: MaximizeAction.java,v $
Revision 1.2  2003/11/15 21:15:29  zet
Label restore action

Revision 1.1  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

*/
