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
import net.mldonkey.g2gui.model.enum.EnumNetwork;
import net.mldonkey.g2gui.view.viewers.GViewer;
import net.mldonkey.g2gui.view.viewers.filters.NetworkGViewerFilter;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * NetworkFilterAction
 *
 * @version $Id: NetworkFilterAction.java,v 1.1 2003/10/29 16:56:21 lemmster Exp $ 
 *
 */
public class NetworkFilterAction extends FilterAction {
	private EnumNetwork networkType;

	/**
	 * Creates a new NetworkFilterAction
	 * @param name The name we should display on the <code>MenuManager</code>
	 * @param networkType The <code>NetworkInfo.Enum</code> we should filter
	 */
	public NetworkFilterAction( GViewer gViewer, NetworkInfo network ) {
		super( network.getNetworkName(), Action.AS_CHECK_BOX, gViewer );
		this.networkType = network.getNetworkType();
		if ( this.isFiltered( network ) )
			setChecked( true );
	}

	public void run() {
		if ( !isChecked() ) {
			ViewerFilter[] viewerFilters = gViewer.getFilters();
			for ( int i = 0; i < viewerFilters.length; i++ ) {
				if ( viewerFilters[ i ] instanceof NetworkGViewerFilter ) {
					NetworkGViewerFilter filter = ( NetworkGViewerFilter ) viewerFilters[ i ];
					if ( filter.matches( networkType ) )
						if ( filter.count() == 1 )
							toggleFilter( viewerFilters[ i ], false );
						else {
							filter.remove( networkType );
							gViewer.refresh();
						}
				}
			}
		}
		else {
			ViewerFilter[] viewerFilters = gViewer.getFilters();
			for ( int i = 0; i < viewerFilters.length; i++ ) {
				if ( viewerFilters[ i ] instanceof NetworkGViewerFilter ) {
					NetworkGViewerFilter filter = ( NetworkGViewerFilter ) viewerFilters[ i ];
					filter.add( networkType );
					gViewer.refresh();
					return;
				}
			}
			NetworkGViewerFilter filter = new NetworkGViewerFilter();
			filter.add( networkType );
			toggleFilter( filter, true );
		}
	}
	
	public void removeAllNetworkFilter() {
		ViewerFilter[] viewerFilters = gViewer.getFilters();
		for ( int i = 0; i < viewerFilters.length; i++ )
			if ( viewerFilters[ i ] instanceof NetworkGViewerFilter )
				gViewer.removeFilter( viewerFilters[ i ] );
	}
}

/*
$Log: NetworkFilterAction.java,v $
Revision 1.1  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

*/