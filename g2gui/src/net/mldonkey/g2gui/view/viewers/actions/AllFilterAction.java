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

import org.eclipse.jface.viewers.ViewerFilter;

import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.search.ResultTableView;
import net.mldonkey.g2gui.view.viewers.GView;
import net.mldonkey.g2gui.view.viewers.filters.WordViewerFilter;
/**
 * AllFilterAction
 *
 * @version $Id: AllFilterAction.java,v 1.8 2003/12/04 08:47:30 lemmy Exp $ 
 *
 */
public class AllFilterAction extends FilterAction {
	/**
	 * Creates a new AllFiltersAction
	 */
	public AllFilterAction( GView gViewer ) {
		super( G2GuiResources.getString( "TML_NO_FILTERS" ), 0, gViewer );
		if ( gViewer != null && gViewer.getFilters().length == 0 )
			this.setChecked( true );
	}

	public void run() {
		// remove all viewerfilters except the wordfilter from the searchtable
		if ( gViewer instanceof ResultTableView ) {
			ViewerFilter[] filters = gViewer.getFilters();
			for ( int i = 0; i < filters.length; i++ ) {
				if ( !( filters[ i ] instanceof WordViewerFilter ) )
					gViewer.removeFilter( filters[ i ] );
			}
		}
		else
			gViewer.resetFilters();
	}
}

/*
$Log: AllFilterAction.java,v $
Revision 1.8  2003/12/04 08:47:30  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.7  2003/11/06 20:02:39  lemmy
move WordFilter
fix AllFilterAction

Revision 1.6  2003/11/06 13:52:33  lemmy
filters back working

Revision 1.5  2003/11/04 21:06:35  lemmy
enclouse iteration of getFilters() to getFilter(someClass) into GView. Next step is optimisation of getFilter(someClass) in GView

Revision 1.4  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.3  2003/10/31 10:42:47  lemmy
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

Revision 1.1  2003/10/29 16:56:21  lemmy
added reasonable class hierarchy for panelisteners, viewers...

*/