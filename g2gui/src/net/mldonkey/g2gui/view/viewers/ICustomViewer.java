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

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IInputProvider;
import org.eclipse.jface.viewers.ViewerSorter;


/**
 * ICustomViewer
 *
 * @version $Id: ICustomViewer.java,v 1.1 2003/10/31 07:24:01 zet Exp $
 *
 */
public interface ICustomViewer extends IInputProvider {
    int[] getColumnIDs();

    void setColumnIDs(String string);

    void setContentProvider(IContentProvider provider);

    void setLabelProvider(IBaseLabelProvider labelProvider);

    void setSorter(ViewerSorter sorter);

    void setInput(Object input);

    void setEditors(boolean b);

    void closeAllTTE();
}


/*
$Log: ICustomViewer.java,v $
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
