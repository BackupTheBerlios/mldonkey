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
package net.mldonkey.g2gui.view.search;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.GuiTab;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.viewers.filters.WordViewerFilter;
import net.mldonkey.g2gui.view.viewers.table.GTableMenuListener;
import net.mldonkey.g2gui.view.viewers.table.GTableView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;


/**
 * ResultTableViewer
 *
 * @version $Id: ResultTableView.java,v 1.10 2003/12/04 08:47:29 lemmy Exp $
 *
 */
public class ResultTableView extends GTableView {
    public static final int NETWORK = 0;
    public static final int NAME = 1;
    public static final int SIZE = 2;
    public static final int FORMAT = 3;
    public static final int MEDIA = 4;
    public static final int AVAILABILITY = 5;
    private MouseListener aMouseListener;

    public ResultTableView(Composite parent, CoreCommunication aCore, CTabItem aCTabItem,
        MouseListener aMouseListener, GuiTab searchTab) {
        super(parent, aCore);
        this.aMouseListener = aMouseListener;
        aCTabItem.setData("gView", this);

        preferenceString = "result";
        columnLabels = new String[] {
                "SR_NETWORK", "SR_NAME", "SR_SIZE", "SR_FORMAT", "SR_MEDIA", "SR_AVAIL"
            };

        columnDefaultWidths = new int[] { 58, 300, 65, 45, 50, 70 };

        columnAlignment = new int[] { SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.LEFT, SWT.LEFT, SWT.LEFT };

        // content provider
        tableContentProvider = new ResultTableContentProvider(this);

        // label provider
        tableLabelProvider = new ResultTableLabelProvider(this);

        // table sorter
        gSorter = new ResultTableSorter(this);

        // table menu listener
        tableMenuListener = new ResultTableMenuListener(this, aCTabItem, searchTab);

        this.createContents(parent);
    }

    /* (non-Javadoc)
     * Method declared in Viewer.
     * This implementatation additionaly unmaps all the elements.
     */
    public void setInput(Object object) {
        sViewer.setInput(object);
    }

    public GTableMenuListener getMenuListener() {
        return this.tableMenuListener;
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.helper.OurTableViewer#create()
     */
    protected void createContents(Composite parent) {
        super.createContents(parent);
        sViewer.addSelectionChangedListener((ResultTableMenuListener) tableMenuListener);

        // add optional filters
        if (PreferenceLoader.loadBoolean("searchFilterPornography"))
            sViewer.addFilter(new WordViewerFilter(WordViewerFilter.PORNOGRAPHY_FILTER_TYPE));
        else if (PreferenceLoader.loadBoolean("searchFilterProfanity"))
            sViewer.addFilter(new WordViewerFilter(WordViewerFilter.PROFANITY_FILTER_TYPE));

        /* add a menuListener to make the first menu item bold */
        addMenuListener();

        /* add a mouse-listener to catch double-clicks */
        getTableViewer().getTable().addMouseListener(aMouseListener);

        // multiline tooltip
        ToolTipHandler tooltip = new ToolTipHandler(getTableViewer().getTable());
    }
}


/*
$Log: ResultTableView.java,v $
Revision 1.10  2003/12/04 08:47:29  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.9  2003/11/29 13:03:54  lemmy
ToolTip complete reworked (to be continued)

Revision 1.8  2003/11/26 23:58:16  zet
fix #1137 (ESC in tooltip)

Revision 1.7  2003/11/24 20:35:11  zet
show filenames in tooltip

Revision 1.6  2003/11/23 17:58:03  lemmy
removed dead/unused code

Revision 1.5  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

Revision 1.4  2003/11/15 11:44:04  lemmy
fix: [Bug #1089] 0.2 similair stop search crash

Revision 1.3  2003/11/08 18:25:54  zet
use GView instead of GTableViewer

Revision 1.2  2003/11/06 20:02:39  lemmy
move WordFilter
fix AllFilterAction

Revision 1.1  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.1  2003/10/31 13:16:33  lemmy
Rename Viewer -> Page
Constructors changed

Revision 1.6  2003/10/31 10:42:47  lemmy
Renamed GViewer, GTableViewer and GTableTreeViewer to GPage... to avoid mix-ups with StructuredViewer...
Removed IGViewer because our abstract class GPage do the job
Use supertype/interface where possible to keep the design flexible!

Revision 1.5  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.4  2003/10/28 00:36:06  zet
move columnselector into the pane

Revision 1.3  2003/10/22 02:25:10  zet
+prefString

Revision 1.2  2003/10/22 01:37:45  zet
add column selector to server/search (might not be finished yet..)

Revision 1.1  2003/10/21 17:00:45  lemmy
class hierarchy for tableviewer

*/
