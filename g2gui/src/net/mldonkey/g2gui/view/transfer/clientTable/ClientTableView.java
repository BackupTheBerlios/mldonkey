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

import net.mldonkey.g2gui.view.helper.ViewFrame;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.viewers.table.GTableView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;


/**
 * ClientTableViewer
 *
 * @version $Id: ClientTableView.java,v 1.10 2004/03/01 21:12:22 psy Exp $
 *
 */
public class ClientTableView extends GTableView {
    public static final int NETWORK = 0;
    public static final int NAME = 1;
    public static final int SOFTWARE = 2;
    public static final int UPLOADED = 3;
    public static final int DOWNLOADED = 4;
    public static final int CONNECT_TIME = 5;
    public static final int SOCK_ADDR = 6;
    public static final int PORT = 7;
    public static final int KIND = 8;
    public static final int STATE = 9;

    public ClientTableView(ViewFrame viewFrame) {
        super(viewFrame);

        preferenceString = "client";
        columnLabels = new String[] {
                "TT_CT_NETWORK", "TT_CT_NAME", "TT_CT_SOFTWARE", "TT_CT_UPLOADED",
                "TT_CT_DOWNLOADED", "TT_CT_CONNECT_TIME", "TT_CT_SOCK_ADDR", "TT_CT_PORT",
                "TT_CT_KIND", "TT_CT_STATE"
            };
        columnAlignment = new int[] {
                SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT,
                SWT.LEFT, SWT.LEFT
            };
        columnDefaultWidths = new int[] { 100, 100, 75, 75, 75, 75, 100, 75, 75, 150 };

        gSorter = new ClientTableSorter(this);
        tableContentProvider = new ClientTableContentProvider(this, viewFrame.getCLabel());
        tableLabelProvider = new ClientTableLabelProvider(this);
        tableMenuListener = new ClientTableMenuListener(this);

        createContents(viewFrame.getChildComposite());
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
        table.setFont(PreferenceLoader.loadFont("viewerFontData"));
    }
}


/*
$Log: ClientTableView.java,v $
Revision 1.10  2004/03/01 21:12:22  psy
removed download-table font config (use global one instead)
started re-arranging the preferences (to be continued...)

Revision 1.9  2003/12/04 08:47:30  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.8  2003/12/01 13:28:16  zet
add port info

Revision 1.7  2003/11/30 23:42:56  zet
updates for latest mldonkey cvs

Revision 1.6  2003/11/27 21:42:33  zet
integrate ViewFrame a little more.. more to come.

Revision 1.5  2003/11/26 07:43:15  zet
quick attempt at an uploaders table w/proto 19 - still in progress...

Revision 1.4  2003/11/24 01:33:27  zet
move some classes

Revision 1.3  2003/11/22 02:24:30  zet
widgetfactory & save sash postions/states between sessions

Revision 1.2  2003/11/08 22:47:15  zet
update client table header

Revision 1.1  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.1  2003/10/31 13:16:33  lemmy
Rename Viewer -> Page
Constructors changed

Revision 1.8  2003/10/31 10:42:47  lemmy
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
