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
package net.mldonkey.g2gui.view.viewers.tableTree;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.helper.ViewFrame;
import net.mldonkey.g2gui.view.viewers.CustomTableTreeViewer;
import net.mldonkey.g2gui.view.viewers.GView;
import net.mldonkey.g2gui.view.viewers.table.GTableContentProvider;
import net.mldonkey.g2gui.view.viewers.table.GTableMenuListener;

import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;


/**
 * GTableTreeViewer
 *
 * @version $Id: GTableTreeView.java,v 1.6 2003/11/27 21:42:33 zet Exp $
 *
 */
public class GTableTreeView extends GView {
    protected GTableTreeContentProvider tableTreeContentProvider;
    protected GTableMenuListener tableTreeMenuListener;

    public GTableTreeView(Composite parent, CoreCommunication core) {
        this.core = core;
    }

    public GTableTreeView(ViewFrame viewFrame) {
        this(viewFrame.getChildComposite(), viewFrame.getCore());
        this.viewFrame = viewFrame;
    }

    /**
    * Create the contents
    * @param parent
    */
    protected void createContents(Composite parent) {
        sViewer = new CustomTableTreeViewer(parent, SWT.FULL_SELECTION | SWT.MULTI);
        super.createContents();
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.IGViewer#getTable()
     */
    public Table getTable() {
        return ((TableTreeViewer) sViewer).getTableTree().getTable();
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.GViewer#getTableContentProvider()
     */
    public GTableContentProvider getTableContentProvider() {
        return tableTreeContentProvider;
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.GViewer#getTableMenuListener()
     */
    public GTableMenuListener getTableMenuListener() {
        return tableTreeMenuListener;
    }

    /**
     * @return CustomTableTreeViewer (convenience)
     */
    protected CustomTableTreeViewer getTableTreeViewer() {
        return (CustomTableTreeViewer) sViewer;
    }
}


/*
$Log: GTableTreeView.java,v $
Revision 1.6  2003/11/27 21:42:33  zet
integrate ViewFrame a little more.. more to come.

Revision 1.5  2003/11/07 02:24:03  zet
push sViewer into GView

Revision 1.4  2003/11/06 13:52:33  lemmster
filters back working

Revision 1.3  2003/11/04 21:06:35  lemmster
enclouse iteration of getFilters() to getFilter(someClass) into GView. Next step is optimisation of getFilter(someClass) in GView

Revision 1.2  2003/10/31 16:26:32  zet
private->protected

Revision 1.1  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.1  2003/10/31 10:42:47  lemmster
Renamed GViewer, GTableViewer and GTableTreeViewer to GPage... to avoid mix-ups with StructuredViewer...
Removed IGViewer because our abstract class GPage do the job
Use supertype/interface where possible to keep the design flexible!

Revision 1.1  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

*/
