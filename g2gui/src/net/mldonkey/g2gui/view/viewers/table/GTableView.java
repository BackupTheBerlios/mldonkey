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
package net.mldonkey.g2gui.view.viewers.table;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.viewers.CustomTableViewer;
import net.mldonkey.g2gui.view.viewers.GView;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;


/**
 * Generic Table Viewer
 *
 * @version $Id: GTableView.java,v 1.3 2003/11/06 13:52:33 lemmster Exp $
 *
 */
public class GTableView extends GView {
    protected GTableLabelProvider tableLabelProvider;
    protected GTableContentProvider tableContentProvider;
    protected GTableMenuListener tableMenuListener;
    protected StructuredViewer sViewer;

    public GTableView(Composite parent, CoreCommunication core) {
        this.core = core;
    }

    /**
     * Create the contents
     * @param parent
     */
    protected void createContents(Composite parent) {
        sViewer = new CustomTableViewer(parent, SWT.FULL_SELECTION | SWT.MULTI);
        super.createContents();
    }

    /**
     * @return CustomTableViewer
     */
    protected CustomTableViewer getTableViewer() {
        return (CustomTableViewer) sViewer;
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.GViewer#getTableContentProvider()
     */
    public GTableContentProvider getTableContentProvider() {
        return tableContentProvider;
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.GViewer#getTableMenuListener()
     */
    public GTableMenuListener getTableMenuListener() {
        return tableMenuListener;
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.GViewer#getTableLabelProvider()
     */
    public GTableLabelProvider getTableLabelProvider() {
        return tableLabelProvider;
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.IGViewer#getTable()
     */
    public Table getTable() {
        return ( (TableViewer) sViewer ).getTable();
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.GViewer#addFilter(org.eclipse.jface.viewers.ViewerFilter)
     */
    public void addFilter(ViewerFilter viewerFilter) {
    	super.addFilter( viewerFilter );
        this.sViewer.addFilter(viewerFilter);
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.GViewer#removeFilter(org.eclipse.jface.viewers.ViewerFilter)
     */
    public void removeFilter(ViewerFilter viewerFilter) {
		super.removeFilter( viewerFilter );
        this.sViewer.removeFilter(viewerFilter);
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.GViewer#refresh()
     */
    public void refresh() {
        this.sViewer.refresh();
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.GViewer#getShell()
     */
    public Shell getShell() {
        return this.getTable().getShell();
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.GViewer#getViewer()
     */
    public StructuredViewer getViewer() {
        return this.sViewer;
    }

	/*
	 * (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.viewers.GView#getFilters()
	 */
    public ViewerFilter[] getFilters() {
        return this.sViewer.getFilters();
    }
}


/*
$Log: GTableView.java,v $
Revision 1.3  2003/11/06 13:52:33  lemmster
filters back working

Revision 1.2  2003/11/04 21:06:35  lemmster
enclouse iteration of getFilters() to getFilter(someClass) into GView. Next step is optimisation of getFilter(someClass) in GView

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
