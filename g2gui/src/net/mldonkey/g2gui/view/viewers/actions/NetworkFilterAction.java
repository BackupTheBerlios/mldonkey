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
import net.mldonkey.g2gui.view.viewers.GView;
import net.mldonkey.g2gui.view.viewers.filters.NetworkGViewerFilter;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * NetworkFilterAction
 *
 * @version $Id: NetworkFilterAction.java,v 1.4 2003/10/31 16:02:57 zet Exp $ 
 *
 */
public class NetworkFilterAction extends FilterAction {
	private EnumNetwork networkType;

	/**
	 * Creates a new NetworkFilterAction
	 * @param name The name we should display on the <code>MenuManager</code>
	 * @param networkType The <code>NetworkInfo.Enum</code> we should filter
	 */
	public NetworkFilterAction( GView gViewer, NetworkInfo network ) {
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