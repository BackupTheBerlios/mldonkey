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
import net.mldonkey.g2gui.model.ResultInfo;
import net.mldonkey.g2gui.model.ServerInfo;
import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.view.viewers.GView;

import org.eclipse.jface.viewers.Viewer;


/**
 * StateGViewerFilter
 *
 * @version $Id: StateGViewerFilter.java,v 1.10 2003/11/28 00:58:06 zet Exp $
 *
 */
public class StateGViewerFilter extends GViewerFilter {
    private boolean exclusion;

    public StateGViewerFilter(GView gView) {
        this(gView, false);
    }

    public StateGViewerFilter(GView gView, boolean exclusion) {
        super(gView);
        this.exclusion = exclusion;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ViewerFilter#
     * select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (element instanceof ServerInfo) {
            ServerInfo server = (ServerInfo) element;
            if (aMatcher.matches(server.getConnectionState().getState()))
                return !exclusion;
            return exclusion;
        }
        if (element instanceof FileInfo) {
            FileInfo fileInfo = (FileInfo) element;
            if (aMatcher.matches(fileInfo.getState().getState()))
                return !exclusion;
            return exclusion;
        }
		if (element instanceof ClientInfo) {
			ClientInfo clientInfo = (ClientInfo) element;
			if (aMatcher.matches(clientInfo.getState().getState()))
				return !exclusion;
			return exclusion;
		}
		if (element instanceof ResultInfo) {
			ResultInfo resultInfo = (ResultInfo) element;
			if (aMatcher.matches(resultInfo.getRating()))
				return !exclusion;
			return exclusion;
		} 
        
        return !exclusion;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ViewerFilter#isFilterProperty(java.lang.Object, java.lang.String)
     */
    public boolean isFilterProperty(Object element, String property) {
        if (element instanceof FileInfo) 
            return property.equals(FileInfo.CHANGED_RATE);
        return true;
    }

    /**
     * @param gViewer
     * @param enum
     * @return boolean
     */
    public static boolean matches(GView gViewer, Enum enum) {
	    GViewerFilter filter = gViewer.getFilter( StateGViewerFilter.class );
	    return filter.matches(enum);
    }
}


/*
$Log: StateGViewerFilter.java,v $
Revision 1.10  2003/11/28 00:58:06  zet
*** empty log message ***

Revision 1.9  2003/11/23 17:58:03  lemmster
removed dead/unused code

Revision 1.8  2003/11/10 08:54:47  lemmster
rating filter in searchresult

Revision 1.7  2003/11/06 13:52:32  lemmster
filters back working

Revision 1.6  2003/11/04 21:06:35  lemmster
enclouse iteration of getFilters() to getFilter(someClass) into GView. Next step is optimisation of getFilter(someClass) in GView

Revision 1.5  2003/10/31 16:48:51  zet
minor

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
