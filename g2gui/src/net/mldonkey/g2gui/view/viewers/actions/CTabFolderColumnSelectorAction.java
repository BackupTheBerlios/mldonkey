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

import java.util.ArrayList;
import java.util.List;

import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.ColumnSelector;
import net.mldonkey.g2gui.view.viewers.table.GTableViewer;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;


/**
 * CTabFolderColumnSelectorAction
 *
 * @version $Id: CTabFolderColumnSelectorAction.java,v 1.3 2003/10/31 07:24:01 zet Exp $
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
        if (gTableViewerList.size() == 0) return;
        
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
Revision 1.3  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.1  2003/10/28 00:36:25  zet
init

*/
