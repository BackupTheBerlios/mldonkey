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

import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.view.viewers.GView;

import org.eclipse.jface.viewers.Viewer;

/**
 * FalseGViewerFilter this filter returns always null for all methods
 *
 * @version $Id: AlwaysFalseGViewerFilter.java,v 1.3 2003/12/04 08:47:30 lemmy Exp $ 
 *
 */
public class AlwaysFalseGViewerFilter extends GViewerFilter {
	/**
	 * @param gView
	 */
	public AlwaysFalseGViewerFilter(GView gView) {
		super(gView);
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.viewers.filters.GViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.viewers.filters.GViewerFilter#matches(net.mldonkey.g2gui.model.enum.Enum)
	 */
	public boolean matches( Enum enum ) {
		return false;        	
	}
	
	public boolean matches( NetworkInfo aNetworkInfo ) {
		return false;
	}
	
	public boolean isNotAlwaysFalse() {
		return false;
	}
}

/*
$Log: AlwaysFalseGViewerFilter.java,v $
Revision 1.3  2003/12/04 08:47:30  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.2  2003/11/06 13:52:32  lemmy
filters back working

Revision 1.1  2003/11/04 21:06:35  lemmy
enclouse iteration of getFilters() to getFilter(someClass) into GView. Next step is optimisation of getFilter(someClass) in GView

*/