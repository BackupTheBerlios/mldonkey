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

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;

/**
 * MaximizeSashMouseAdapter
 *
 * @version $Id: MaximizeSashMouseAdapter.java,v 1.1 2003/09/26 16:07:47 zet Exp $ 
 *
 */
public class MaximizeSashMouseAdapter extends HeaderBarMouseAdapter {
    private SashForm sashForm;
    private Control control;

    public MaximizeSashMouseAdapter(CLabel cLabel, MenuManager menuManager, 
    		SashForm sashForm, Control control) {
        super(cLabel, menuManager);
        this.sashForm = sashForm;
        this.control = control;
    }

    public void mouseDoubleClick(MouseEvent e) {
        if (sashForm.getMaximizedControl() == null) {
            sashForm.setMaximizedControl(control);
        }
        else {
            sashForm.setMaximizedControl(null);
        }
    }
}
/*
$Log: MaximizeSashMouseAdapter.java,v $
Revision 1.1  2003/09/26 16:07:47  zet
initial

*/