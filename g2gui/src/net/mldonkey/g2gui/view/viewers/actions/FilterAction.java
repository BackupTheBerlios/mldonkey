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

import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.view.viewers.GView;
import net.mldonkey.g2gui.view.viewers.filters.FileExtensionFilter;
import net.mldonkey.g2gui.view.viewers.filters.GViewerFilter;
import net.mldonkey.g2gui.view.viewers.filters.NetworkGViewerFilter;
import net.mldonkey.g2gui.view.viewers.filters.StateGViewerFilter;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * GAction
 *
 * @version $Id: FilterAction.java,v 1.5 2003/11/04 21:06:35 lemmster Exp $ 
 *
 */
public abstract class FilterAction extends Action {
	protected GView gViewer;
	public FilterAction( String aString, int anInt, GView gViewer ) {
		super( aString, anInt );
		this.gViewer = gViewer;
	}

	protected void toggleFilter( ViewerFilter viewerFilter, boolean toggle ) {
		if ( toggle )
			gViewer.addFilter( viewerFilter );
		else
			gViewer.removeFilter( viewerFilter );
	}
	
	public static boolean isFiltered( GView aGViewer, NetworkInfo aNetworkInfo ) {
		NetworkGViewerFilter filter = (NetworkGViewerFilter) aGViewer.getFilter( NetworkGViewerFilter.class );
		if ( filter.matches( aNetworkInfo ) )
			return true;
		return false;
	}

	protected boolean isFiltered( NetworkInfo aNetworkInfo ) {
		GViewerFilter filter = (GViewerFilter) gViewer.getFilter( NetworkGViewerFilter.class );
		if ( filter.matches( aNetworkInfo ) )
			return true;
		return false;
	}

	public static boolean isFiltered( GView aGViewer, Enum state ) {
		GViewerFilter filter = ( GViewerFilter ) aGViewer.getFilter( StateGViewerFilter.class );
		if ( filter.matches( state ) )
			return true;
		return false;
	}

	protected boolean isFiltered( Enum state ) {
		GViewerFilter filter = ( GViewerFilter ) gViewer.getFilter( StateGViewerFilter.class );
		if ( filter.matches( state ) )
			return true;
		return false;
	}
	
	protected boolean isFiltered( String[] extensions ) {
		ViewerFilter[] viewerFilters = gViewer.getFilters();
		for ( int i = 0; i < viewerFilters.length; i++ ) {
			if ( viewerFilters[ i ] instanceof FileExtensionFilter ) {
				FileExtensionFilter filter = (FileExtensionFilter) viewerFilters[ i ];
				for ( int j = 0; j < filter.getFileExtensionList().size(); j++ ) {
					String[] fileExtensions = (String[]) filter.getFileExtensionList().get( j );
					if ( fileExtensions.equals( extensions ) ) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private static boolean isFiltered( GView gViewer, String[] extensions ) {
		ViewerFilter[] viewerFilters = gViewer.getFilters();
		for ( int i = 0; i < viewerFilters.length; i++ ) {
			if ( viewerFilters[ i ] instanceof FileExtensionFilter ) {
				FileExtensionFilter filter = (FileExtensionFilter) viewerFilters[ i ];
				for ( int j = 0; j < filter.getFileExtensionList().size(); j++ ) {
					String[] fileExtensions = (String[]) filter.getFileExtensionList().get( j );
					if ( fileExtensions.equals( extensions ) ) {
						return true;
					}
				}
			}
		}
		return false;
	}
}

/*
$Log: FilterAction.java,v $
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