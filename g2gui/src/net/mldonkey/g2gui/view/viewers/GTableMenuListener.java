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
package net.mldonkey.g2gui.view.viewers;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;


/**
 * GenericTableMenuListener
 *
 * @version $Id: GTableMenuListener.java,v 1.1 2003/10/22 01:36:59 zet Exp $
 *
 */
public class GTableMenuListener implements IMenuListener {
    protected GTableViewer gTableViewer;
    protected CustomTableViewer tableViewer;

    public GTableMenuListener(GTableViewer gTableViewer) {
        this.gTableViewer = gTableViewer;
    }

    /**
     * initialize after tableViewer creation
     */
    public void initialize() {
        tableViewer = gTableViewer.getTableViewer();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
     */
    public void menuAboutToShow(IMenuManager menuManager) {
    }
}


/*
$Log: GTableMenuListener.java,v $
Revision 1.1  2003/10/22 01:36:59  zet
add column selector to server/search (might not be finished yet..)

*/
