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
import net.mldonkey.g2gui.view.viewers.IGViewer;
import net.mldonkey.g2gui.view.viewers.filters.StateGViewerFilter;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ViewerFilter;


/**
 * StateFilterAction
 *
 * @version $Id: StateFilterAction.java,v 1.2 2003/10/31 07:24:01 zet Exp $
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
    public StateFilterAction(String name, IGViewer gViewer, Enum state, boolean exclusion) {
        super(name, Action.AS_CHECK_BOX, gViewer);

        if (this.isFiltered(state) != exclusion) {
            setChecked(true);
        }

        this.state = state;
        this.exclusion = exclusion;
    }

    public StateFilterAction(String name, IGViewer gViewer, Enum state) {
        this(name, gViewer, state, false);
    }

    public void run() {
        if (!isChecked() != exclusion) {
            ViewerFilter[] viewerFilters = gViewer.getFilters();

            for (int i = 0; i < viewerFilters.length; i++) {
                if (viewerFilters[ i ] instanceof StateGViewerFilter) {
                    StateGViewerFilter filter = (StateGViewerFilter) viewerFilters[ i ];

                    if (filter.matches(state)) {
                        if (filter.count() == 1) {
                            toggleFilter(viewerFilters[ i ], false);
                        } else {
                            filter.remove(state);
                            gViewer.refresh();
                        }
                    }
                }
            }
        } else {
            ViewerFilter[] viewerFilters = gViewer.getFilters();

            for (int i = 0; i < viewerFilters.length; i++) {
                if (viewerFilters[ i ] instanceof StateGViewerFilter) {
                    StateGViewerFilter filter = (StateGViewerFilter) viewerFilters[ i ];
                    filter.add(state);
                    gViewer.refresh();

                    return;
                }
            }

            StateGViewerFilter filter = new StateGViewerFilter(exclusion);
            filter.add(state);
            toggleFilter(filter, true);
        }
    }

    public static void removeFilters(IGViewer gViewer) {
        ViewerFilter[] filters = gViewer.getFilters();

        for (int i = 0; i < filters.length; i++)
            if (filters[ i ] instanceof StateGViewerFilter) {
                gViewer.removeFilter(filters[ i ]);
            }
    }
}


/*
$Log: StateFilterAction.java,v $
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
