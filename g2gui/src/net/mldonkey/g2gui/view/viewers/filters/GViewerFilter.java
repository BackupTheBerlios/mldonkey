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

import net.mldonkey.g2gui.model.enum.Enum;

/**
 * GViewerFilter
 *
 * @version $Id: GViewerFilter.java,v 1.1 2003/10/29 16:56:21 lemmster Exp $ 
 *
 */
public abstract class GViewerFilter extends ViewerFilter {
	protected Enum.MaskMatcher aMatcher;
	/**
	 * Creates a new EnumStateFilter
	 */
	public GViewerFilter() {
		this.aMatcher = new Enum.MaskMatcher();
	}

	public void add( Enum enum ) {
		this.aMatcher.add( enum );
	}

	public void remove( Enum state ) {
		this.aMatcher.remove( state );
	}
        
	public boolean matches( Enum enum ) {
		return this.aMatcher.matches( enum );        	
	}
        
	public int count() {
		return this.aMatcher.count();
	}	
	public abstract boolean select( Viewer viewer, Object parentElement, Object element );
}

/*
$Log: GViewerFilter.java,v $
Revision 1.1  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

*/