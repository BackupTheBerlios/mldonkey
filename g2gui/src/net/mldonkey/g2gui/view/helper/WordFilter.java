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
package net.mldonkey.g2gui.view.helper;

import net.mldonkey.g2gui.model.ResultInfo;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * WordFilter
 *
 * @version $Id: WordFilter.java,v 1.2 2003/09/18 10:04:57 lemmster Exp $ 
 *
 */
public class WordFilter extends ViewerFilter {
	private int wordFilterType = 0;
	public static final int PROFANITY_FILTER_TYPE = 1;
	public static final int PORNOGRAPHY_FILTER_TYPE = 2;
	
	/**
	 * DOCUMENT ME!
	 * 
	 * @param type DOCUMENT ME!
	 */	
	public WordFilter( int type ) {
		wordFilterType = type;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerFilter#
	 * select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public boolean select( Viewer viewer, Object parentElement, Object element ) {
		if ( element instanceof ResultInfo ) {
			ResultInfo resultInfo = ( ResultInfo ) element;
			if ( ( wordFilterType == PROFANITY_FILTER_TYPE && resultInfo.containsProfanity() )
			|| ( wordFilterType == PORNOGRAPHY_FILTER_TYPE && resultInfo.containsPornography() ) )
					return false;
			return true;
		}
		return true;
	}
}
/*
$Log: WordFilter.java,v $
Revision 1.2  2003/09/18 10:04:57  lemmster
checkstyle

Revision 1.1  2003/08/29 00:54:42  zet
Move wordFilter public



*/