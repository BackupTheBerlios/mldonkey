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

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.ServerInfo;
import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.view.viewers.GViewer;

/**
 * StateGViewerFilter
 *
 * @version $Id: StateGViewerFilter.java,v 1.1 2003/10/29 16:56:21 lemmster Exp $ 
 *
 */
public class StateGViewerFilter extends GViewerFilter {
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerFilter#
	 * select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public boolean select( Viewer viewer, Object parentElement, Object element ) {
		if ( element instanceof ServerInfo ) {
			ServerInfo server = ( ServerInfo ) element;
			if ( aMatcher.matches( server.getConnectionState().getState() ) )
				return true;
			return false;
		}
		if ( element instanceof FileInfo ) {
			FileInfo fileInfo = ( FileInfo ) element;
			if ( aMatcher.matches( fileInfo.getState().getState() ) )
				return true;
			return false;
		}
		return true;
	}
	
	public static boolean matches( GViewer gViewer, Enum enum ) {
		ViewerFilter[] filters = gViewer.getFilters();
		for ( int i = 0; i < filters.length; i++ ) {
			if ( filters[ i ] instanceof StateGViewerFilter ) {
				StateGViewerFilter filter =
					( StateGViewerFilter ) filters[ i ];
				if ( filter.matches( enum ) )
					return true;
			}
		}	
		return false;

	}
}

/*
$Log: StateGViewerFilter.java,v $
Revision 1.1  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

*/