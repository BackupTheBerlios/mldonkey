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
package net.mldonkey.g2gui.view.viewers.actions;

import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.view.viewers.GView;
import net.mldonkey.g2gui.view.viewers.filters.GViewerFilter;
import net.mldonkey.g2gui.view.viewers.filters.StateGViewerFilter;

import org.eclipse.jface.action.Action;


/**
 * StateFilterAction
 *
 * @version $Id: StateFilterAction.java,v 1.6 2003/11/06 13:52:33 lemmster Exp $
 *
 */
public class StateFilterAction extends FilterAction {
    //TODO check for slowness
    private Enum state;
    private boolean exclusion;

    /**
     * @param name
     * @param gViewer
     * @param state
     * @param exclusion
     */
    public StateFilterAction(String name, GView gViewer, Enum state, boolean exclusion) {
        super(name, Action.AS_CHECK_BOX, gViewer);

        if (this.isFiltered(state) != exclusion) {
            setChecked(true);
        }

        this.state = state;
        this.exclusion = exclusion;
    }

    public StateFilterAction(String name, GView gViewer, Enum state) {
        this(name, gViewer, state, false);
    }

    public void run() {
		GViewerFilter filter = gViewer.getFilter( StateGViewerFilter.class );
		if ( isChecked() == exclusion ) {
			if ( filter.matches( state ) )
				removeFilter( filter, state );
		}
		else {
			if (filter.isNotAlwaysFalse())
				addFilter( filter, state );
			else
				addFilter(new StateGViewerFilter(gViewer, exclusion), state);
		}	

    }

    public static void removeFilters(GView gViewer) {
        GViewerFilter filter = gViewer.getFilter( StateGViewerFilter.class );
        if (filter.isNotAlwaysFalse())
        	gViewer.removeFilter(filter);
    }
}


/*
$Log: StateFilterAction.java,v $
Revision 1.6  2003/11/06 13:52:33  lemmster
filters back working

Revision 1.5  2003/11/04 21:06:35  lemmster
enclouse iteration of getFilters() to getFilter(someClass) into GView. Next step is optimisation of getFilter(someClass) in GView

Revision 1.4  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.3  2003/10/31 10:42:47  lemmster
Renamed GViewer, GTableViewer and GTableTreeViewer to GPage... to avoid mix-ups with StructuredViewer...
Removed IGViewer because our abstract class GPage do the job
Use supertype/interface where possible to keep the design flexible!

Revision 1.2  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.1  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

*/
