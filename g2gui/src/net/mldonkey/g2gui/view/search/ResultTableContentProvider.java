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
package net.mldonkey.g2gui.view.search;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * ResultTableContentProvider
 *
 * @author $Author: lemmster $
 * @version $Id: ResultTableContentProvider.java,v 1.3 2003/08/22 21:10:57 lemmster Exp $ 
 *
 */
public class ResultTableContentProvider implements IStructuredContentProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#
	 * getElements(java.lang.Object)
	 */
	public Object[] getElements( Object arg0 ) {
		List aList = ( List ) arg0;
		return aList.toArray();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() { }

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#
	 * inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged( Viewer arg0, Object arg1, Object arg2 ) { }
}

/*
$Log: ResultTableContentProvider.java,v $
Revision 1.3  2003/08/22 21:10:57  lemmster
replace $user$ with $Author$

Revision 1.2  2003/07/24 16:20:10  lemmstercvs01
lots of changes

Revision 1.1  2003/07/23 16:56:28  lemmstercvs01
initial commit

*/