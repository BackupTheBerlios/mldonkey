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
import net.mldonkey.g2gui.view.viewers.ColumnSelector;
import net.mldonkey.g2gui.view.viewers.GTableViewer;

import org.eclipse.jface.action.Action;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

import java.util.ArrayList;
import java.util.List;


/**
 * MultipleTablesColumnSelectorAction
 *
 * @version $Id: CTabFolderColumnSelectorAction.java,v 1.1 2003/10/28 00:36:25 zet Exp $
 *
 */
public class CTabFolderColumnSelectorAction extends Action {
    private List gTableViewerList = new ArrayList();

    public CTabFolderColumnSelectorAction(CTabFolder cTabFolder) {
        super(G2GuiResources.getString("TT_ColumnSelector"));
        setImageDescriptor(G2GuiResources.getImageDescriptor("table"));

        for (int i = 0; i < cTabFolder.getItems().length; i++) {
            CTabItem cTabItem = cTabFolder.getItems()[ i ];

            if (cTabItem.getData("gTableViewer") != null) {
                gTableViewerList.add((GTableViewer) cTabItem.getData("gTableViewer"));
            }
        }
    }

    public void run() {
        GTableViewer gTableViewer = (GTableViewer) gTableViewerList.get(0);

        ColumnSelector c = new ColumnSelector(gTableViewer.getTableViewer().getTable().getShell(), gTableViewer.getColumnLabels(),
                gTableViewer.getAllColumnIDs(), gTableViewer.getPreferenceString());

        if (c.open() == ColumnSelector.OK) {
            c.savePrefs();

            for (int i = 0; i < gTableViewerList.size(); i++) {
                ((GTableViewer) gTableViewerList.get(i)).resetColumns();
            }
        }
    }
}


/*
$Log: CTabFolderColumnSelectorAction.java,v $
Revision 1.1  2003/10/28 00:36:25  zet
init

*/
