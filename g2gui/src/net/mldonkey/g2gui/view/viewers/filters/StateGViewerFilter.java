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
package net.mldonkey.g2gui.view.viewers.filters;

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.ServerInfo;
import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.view.viewers.IGViewer;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;


/**
 * StateGViewerFilter
 *
 * @version $Id: StateGViewerFilter.java,v 1.2 2003/10/31 07:24:01 zet Exp $
 *
 */
public class StateGViewerFilter extends GViewerFilter {
    private boolean exclusion = false;

    public StateGViewerFilter() {
    }

    public StateGViewerFilter(boolean exclusion) {
        this.exclusion = exclusion;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ViewerFilter#
     * select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (element instanceof ServerInfo) {
            ServerInfo server = (ServerInfo) element;

            if (aMatcher.matches(server.getConnectionState().getState())) {
                return !exclusion;
            }

            return exclusion;
        }

        if (element instanceof FileInfo) {
            FileInfo fileInfo = (FileInfo) element;

            if (aMatcher.matches(fileInfo.getState().getState())) {
                return !exclusion;
            }

            return exclusion;
        }

		if (element instanceof ClientInfo) {
			ClientInfo clientInfo = (ClientInfo) element;

			if (aMatcher.matches(clientInfo.getState().getState())) {
				return !exclusion;
			}

			return exclusion;
		}
        
        
        
        return !exclusion;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ViewerFilter#isFilterProperty(java.lang.Object, java.lang.String)
     */
    public boolean isFilterProperty(Object element, String property) {
        if (element instanceof FileInfo) {
            if (property.equals(FileInfo.CHANGED_RATE))
                return true;
            return false;
        }
        return true;
    }

    /**
     * @param gViewer
     * @param enum
     * @return boolean
     */
    public static boolean matches(IGViewer gViewer, Enum enum) {
        ViewerFilter[] filters = gViewer.getFilters();

        for (int i = 0; i < filters.length; i++) {
            if (filters[ i ] instanceof StateGViewerFilter) {
                StateGViewerFilter filter = (StateGViewerFilter) filters[ i ];

                if (filter.matches(enum)) {
                    return true;
                }
            }
        }

        return false;
    }
}


/*
$Log: StateGViewerFilter.java,v $
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
