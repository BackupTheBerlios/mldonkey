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
package net.mldonkey.g2gui.view.viewers;

import net.mldonkey.g2gui.comm.CoreCommunication;

import org.eclipse.jface.viewers.ViewerFilter;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;


/**
 * IGViewer
 *
 * @version $Id: IGViewer.java,v 1.1 2003/10/31 07:24:01 zet Exp $
 *
 */
public interface IGViewer {
    /**
     * @return ViewerFilter[]
     */
    ViewerFilter[] getFilters();

    /**
     * @param viewerFilter
     */
    void addFilter(ViewerFilter viewerFilter);

    /**
     * @param viewerFilter
     */
    void removeFilter(ViewerFilter viewerFilter);

    /**
     * refresh
     */
    void refresh();

    /**
     * resetColumns
     */
    void resetColumns();

    /**
     * @return Table
     */
    Table getTable();

    /**
     * @return Shell
     */
    Shell getShell();

    /**
     * @return Object
     */
    Object getViewer();

    /**
     * @return String
     */
    String getAllColumnIDs();

    /**
     * @return String[]
     */
    String[] getColumnLabels();

    /**
     * @return String
     */
    String getColumnIDs();

    /**
     * @return String
     */
    String getPreferenceString();

    /**
     * @return CoreCommunication
     */
    CoreCommunication getCore();

    /**
     * @param listener
     */
    void addDisposeListener(GPaneListener listener);
}


/*
$Log: IGViewer.java,v $
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
