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

import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.view.viewers.GView;

/**
 * GViewerFilter
 *
 * @version $Id: GViewerFilter.java,v 1.4 2003/11/23 17:58:03 lemmster Exp $ 
 *
 */
public abstract class GViewerFilter extends ViewerFilter {
	protected Enum.MaskMatcher aMatcher;
	protected GView gView;
	/**
	 * Creates a new EnumStateFilter
	 */
	public GViewerFilter( GView gView ) {
		this.gView = gView;
		this.aMatcher = new Enum.MaskMatcher();
	}

	public void add( Enum enum ) {
		this.aMatcher.add( enum );
		if ( aMatcher.count() > 1 )
			gView.refresh();
	}

	public void remove( Enum state ) {
		this.aMatcher.remove( state );
		if ( aMatcher.count() > 1 )
			gView.refresh();
	}
        
	public boolean matches( Enum enum ) {
		return this.aMatcher.matches( enum );        	
	}
	
	public boolean matches( NetworkInfo network ) {
		return this.aMatcher.matches( network.getNetworkType() );
		//TODO why was this false?
		//return false;
	}
        
	public int count() {
		return this.aMatcher.count();
	}
	public boolean isNotAlwaysFalse() {
		return true;
	}
		
	public abstract boolean select( Viewer viewer, Object parentElement, Object element );
}

/*
$Log: GViewerFilter.java,v $
Revision 1.4  2003/11/23 17:58:03  lemmster
removed dead/unused code

Revision 1.3  2003/11/06 13:52:33  lemmster
filters back working

Revision 1.2  2003/11/04 21:06:35  lemmster
enclouse iteration of getFilters() to getFilter(someClass) into GView. Next step is optimisation of getFilter(someClass) in GView

Revision 1.1  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

*/