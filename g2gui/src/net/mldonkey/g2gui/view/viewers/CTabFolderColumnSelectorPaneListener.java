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

import net.mldonkey.g2gui.view.viewers.actions.CTabFolderColumnSelectorAction;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;

import org.eclipse.swt.custom.CTabFolder;


/**
 * ColumnSelectorPaneListener
 *
 * @version $Id: CTabFolderColumnSelectorPaneListener.java,v 1.1 2003/10/28 00:36:20 zet Exp $
 *
 */
public class CTabFolderColumnSelectorPaneListener implements IMenuListener {
    private CTabFolder cTabFolder;

    public CTabFolderColumnSelectorPaneListener(CTabFolder cTabFolder) {
        this.cTabFolder = cTabFolder;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
     */
    public void menuAboutToShow(IMenuManager menuManager) {
        if ((cTabFolder != null) && (cTabFolder.getSelection() != null) && (cTabFolder.getSelection().getData("gTableViewer") != null)) {
            menuManager.add(new CTabFolderColumnSelectorAction(cTabFolder));
        }
    }
}


/*
$Log: CTabFolderColumnSelectorPaneListener.java,v $
Revision 1.1  2003/10/28 00:36:20  zet
init


*/
