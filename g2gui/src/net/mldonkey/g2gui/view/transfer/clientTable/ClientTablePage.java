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
package net.mldonkey.g2gui.view.transfer.clientTable;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.viewers.table.GTablePage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;


/**
 * ClientTableViewer
 *
 * @version $Id: ClientTablePage.java,v 1.1 2003/10/31 13:16:33 lemmster Exp $
 *
 */
public class ClientTablePage extends GTablePage {
    public static final int STATE = 0;
    public static final int NAME = 1;
    public static final int NETWORK = 2;
    public static final int KIND = 3;

    public ClientTablePage(Composite parent, CoreCommunication core) {
        super(parent, core);

        preferenceString = "client";
        columnLabels = new String[] { "TT_CT_STATE", "TT_CT_NAME", "TT_CT_NETWORK", "TT_CT_KIND" };
        columnAlignment = new int[] { SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.LEFT };
        columnDefaultWidths = new int[] { 200, 100, 75, 75 };

        gSorter = new ClientTableSorter(this);
        tableContentProvider = new ClientTableContentProvider(this);
        tableLabelProvider = new ClientTableLabelProvider(this);
        tableMenuListener = new ClientTableMenuListener(this);

        createContents(parent);
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.GTableViewer#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected void createContents(Composite parent) {
        super.createContents(parent);
        sViewer.addSelectionChangedListener((ClientTableMenuListener) tableMenuListener);
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.GTableViewer#updateDisplay()
     */
    public void updateDisplay() {
        super.updateDisplay();

        Table table = getTableViewer().getTable();
        table.setBackground(PreferenceLoader.loadColour("downloadsBackgroundColor"));
        table.setForeground(PreferenceLoader.loadColour("downloadsAvailableColor"));
        table.setFont(PreferenceLoader.loadFont("downloadsFontData"));
    }
}


/*
$Log: ClientTablePage.java,v $
Revision 1.1  2003/10/31 13:16:33  lemmster
Rename Viewer -> Page
Constructors changed

Revision 1.8  2003/10/31 10:42:47  lemmster
Renamed GViewer, GTableViewer and GTableTreeViewer to GPage... to avoid mix-ups with StructuredViewer...
Removed IGViewer because our abstract class GPage do the job
Use supertype/interface where possible to keep the design flexible!

Revision 1.7  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.6  2003/10/22 01:38:19  zet
add column selector to server/search (might not be finished yet..)

Revision 1.5  2003/10/19 21:38:54  zet
columnselector support

Revision 1.4  2003/10/16 16:09:56  zet
updateDisplay

Revision 1.3  2003/10/12 15:58:30  zet
rewrite downloads table & more..

Revision 1.2  2003/09/21 23:39:31  zet
displayTableColors preference

Revision 1.1  2003/09/20 14:39:21  zet
move transfer package

*/
