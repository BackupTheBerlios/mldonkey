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

import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.view.viewers.GView;

import org.eclipse.jface.viewers.Viewer;

/**
 * FileExtensionFilter
 *
 * @version $Id: FileExtensionGViewerFilter.java,v 1.2 2003/12/04 08:47:30 lemmy Exp $ 
 *
 */
public class FileExtensionGViewerFilter extends GViewerFilter {

	public FileExtensionGViewerFilter(GView gView) {
		super(gView);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public boolean select( Viewer viewer, Object parentElement, Object element ) {
		if (element instanceof FileInfo) {
			FileInfo fileInfo = (FileInfo) element;
			if (aMatcher.matches(fileInfo.getFileType()))
				return true;
			return false;
		}
		return true;
	}
}

/*
$Log: FileExtensionGViewerFilter.java,v $
Revision 1.2  2003/12/04 08:47:30  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.1  2003/11/06 13:52:32  lemmy
filters back working

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

Revision 1.1  2003/10/29 16:56:21  lemmy
added reasonable class hierarchy for panelisteners, viewers...

*/