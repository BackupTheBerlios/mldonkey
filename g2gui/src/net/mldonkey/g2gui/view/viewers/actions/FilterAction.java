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
import net.mldonkey.g2gui.view.viewers.GViewer;
import net.mldonkey.g2gui.view.viewers.filters.FileExtensionFilter;
import net.mldonkey.g2gui.view.viewers.filters.NetworkGViewerFilter;
import net.mldonkey.g2gui.view.viewers.filters.StateGViewerFilter;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * GAction
 *
 * @version $Id: FilterAction.java,v 1.1 2003/10/29 16:56:21 lemmster Exp $ 
 *
 */
public abstract class FilterAction extends Action {
	protected GViewer gViewer;
	public FilterAction( String aString, int anInt, GViewer gViewer ) {
		super( aString, anInt );
		this.gViewer = gViewer;
	}

	protected void toggleFilter( ViewerFilter viewerFilter, boolean toggle ) {
		if ( toggle )
			gViewer.addFilter( viewerFilter );
		else
			gViewer.removeFilter( viewerFilter );
	}
	
	public static boolean isFiltered( GViewer aGViewer, NetworkInfo aNetworkInfo ) {
		ViewerFilter[] viewerFilters = aGViewer.getFilters();
		for ( int i = 0; i < viewerFilters.length; i++ ) {
			if ( viewerFilters[ i ] instanceof NetworkGViewerFilter ) {
				NetworkGViewerFilter filter = ( NetworkGViewerFilter ) viewerFilters[ i ];
				if ( filter.matches( aNetworkInfo ) )
						return true;
			}
		}
		return false;
	}

	protected boolean isFiltered( NetworkInfo aNetworkInfo ) {
		ViewerFilter[] viewerFilters = gViewer.getFilters();
		for ( int i = 0; i < viewerFilters.length; i++ ) {
			if ( viewerFilters[ i ] instanceof NetworkGViewerFilter ) {
				NetworkGViewerFilter filter = ( NetworkGViewerFilter ) viewerFilters[ i ];
				if ( filter.matches( aNetworkInfo ) )
						return true;
			}
		}
		return false;
	}

	public static boolean isFiltered( GViewer aGViewer, Enum state ) {
		ViewerFilter[] viewerFilters = aGViewer.getFilters();
		for ( int i = 0; i < viewerFilters.length; i++ ) {
			if ( viewerFilters[ i ] instanceof StateGViewerFilter ) {
				StateGViewerFilter filter = ( StateGViewerFilter ) viewerFilters[ i ];
				if ( filter.matches( state ) )
					return true;
			}
		}
		return false;
	}

	protected boolean isFiltered( Enum state ) {
		ViewerFilter[] viewerFilters = gViewer.getFilters();
		for ( int i = 0; i < viewerFilters.length; i++ ) {
			if ( viewerFilters[ i ] instanceof StateGViewerFilter ) {
				StateGViewerFilter filter = ( StateGViewerFilter ) viewerFilters[ i ];
				if ( filter.matches( state ) )
					return true;
			}
		}
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
	
	private static boolean isFiltered( GViewer gViewer, String[] extensions ) {
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
Revision 1.1  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

*/