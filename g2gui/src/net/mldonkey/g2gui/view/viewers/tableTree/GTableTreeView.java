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
import net.mldonkey.g2gui.view.viewers.CustomTableTreeViewer;
import net.mldonkey.g2gui.view.viewers.GView;
import net.mldonkey.g2gui.view.viewers.table.GTableContentProvider;
import net.mldonkey.g2gui.view.viewers.table.GTableLabelProvider;
import net.mldonkey.g2gui.view.viewers.table.GTableMenuListener;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;


/**
 * GTableTreeViewer
 *
 * @version $Id: GTableTreeView.java,v 1.1 2003/10/31 16:02:57 zet Exp $
 *
 */
public class GTableTreeView extends GView {
    protected GTableLabelProvider tableLabelProvider;
    protected GTableTreeContentProvider tableTreeContentProvider;
    protected GTableMenuListener tableTreeMenuListener;
    private StructuredViewer sViewer;

    public GTableTreeView(Composite parent, CoreCommunication core) {
        this.core = core;
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.IGViewer#getFilters()
     */
    public ViewerFilter[] getFilters() {
        return sViewer.getFilters();
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.IGViewer#addFilter(org.eclipse.jface.viewers.ViewerFilter)
     */
    public void addFilter(ViewerFilter viewerFilter) {
        sViewer.addFilter(viewerFilter);
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.IGViewer#removeFilter(org.eclipse.jface.viewers.ViewerFilter)
     */
    public void removeFilter(ViewerFilter viewerFilter) {
        sViewer.removeFilter(viewerFilter);
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.IGViewer#refresh()
     */
    public void refresh() {
        sViewer.refresh();
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.IGViewer#getTable()
     */
    public Table getTable() {
        return  ( (TableTreeViewer) sViewer).getTableTree().getTable();
    }

    /**
     * @return CustomTableTreeViewer (convenience)
     */
    protected CustomTableTreeViewer getTableTreeViewer() {
        return (CustomTableTreeViewer) sViewer;
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.IGViewer#getShell()
     */
    public Shell getShell() {
        return this.getTable().getShell();
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.IGViewer#getViewer()
     */
    public StructuredViewer getViewer() {
        return sViewer;
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

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.GViewer#getTableLabelProvider()
     */
    public GTableLabelProvider getTableLabelProvider() {
        return tableLabelProvider;
    }

    /**
      * Create the contents
      * @param parent
      */
    protected void createContents(Composite parent) {
        sViewer = new CustomTableTreeViewer(parent, SWT.FULL_SELECTION | SWT.MULTI);
        super.createContents();
    }
}


/*
$Log: GTableTreeView.java,v $
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
