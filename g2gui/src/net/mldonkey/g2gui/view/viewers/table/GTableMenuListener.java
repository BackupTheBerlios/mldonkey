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

import net.mldonkey.g2gui.view.viewers.CustomTableViewer;
import net.mldonkey.g2gui.view.viewers.GView;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;


/**
 * GenericTableMenuListener
 *
 * @version $Id: GTableMenuListener.java,v 1.5 2003/12/04 08:47:31 lemmy Exp $
 *
 */
public abstract class GTableMenuListener implements IMenuListener {
    protected GView gView;
    protected CustomTableViewer tableViewer;

    public GTableMenuListener(GView gView) {
        this.gView = gView;
    }

    /**
     * initialize after tableViewer creation
     */
    public void initialize() {
        if (gView instanceof GTableView)
		tableViewer = ((GTableView) gView).getTableViewer();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
     */
    public void menuAboutToShow(IMenuManager menuManager) {
    }
}


/*
$Log: GTableMenuListener.java,v $
Revision 1.5  2003/12/04 08:47:31  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.4  2003/10/31 16:30:49  zet
minor renames

Revision 1.3  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.2  2003/10/31 10:42:47  lemmy
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
