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

import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.action.Action;


/**
 * ColumnSelectorAction
 *
 * @version $Id: ColumnSelectorAction.java,v 1.1 2003/10/22 01:36:59 zet Exp $
 *
 */
public class ColumnSelectorAction extends Action {
    private GTableViewer gTableViewer;

    public ColumnSelectorAction(GTableViewer gTableViewer) {
        super(G2GuiResources.getString("TT_ColumnSelector"));
        setImageDescriptor(G2GuiResources.getImageDescriptor("table"));
        this.gTableViewer = gTableViewer;
    }

    public void run() {
        ColumnSelector c = new ColumnSelector(gTableViewer.getTableViewer().getTable().getShell(), gTableViewer.getColumnLabels(),
                gTableViewer.getAllColumnIDs(), gTableViewer.getPreferenceString());

        if (c.open() == ColumnSelector.OK) {
            c.savePrefs();
            gTableViewer.resetColumns();
        }
    }
}


/*
$Log: ColumnSelectorAction.java,v $
Revision 1.1  2003/10/22 01:36:59  zet
add column selector to server/search (might not be finished yet..)

*/
