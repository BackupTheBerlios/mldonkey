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

import java.util.ArrayList;
import java.util.List;

import net.mldonkey.g2gui.model.FileInfo;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * FileExtensionFilter
 *
 * @version $Id: FileExtensionFilter.java,v 1.1 2003/10/29 16:56:21 lemmster Exp $ 
 *
 */
public class FileExtensionFilter extends ViewerFilter {
	private List fileExtensionsList;

	public FileExtensionFilter() {
		this.fileExtensionsList = new ArrayList();
	}

	public List getFileExtensionList() {
		return fileExtensionsList;
	}

	public boolean add( String[] extensions ) {
		return this.fileExtensionsList.add( extensions );
	}

	public boolean remove( String[] extensions ) {
		return this.fileExtensionsList.remove( extensions );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public boolean select( Viewer viewer, Object parentElement, Object element ) {
		if ( element instanceof FileInfo ) {
			FileInfo fileInfo = (FileInfo) element;
			for ( int i = 0; i < this.fileExtensionsList.size(); i++ ) {
				String[] fileExtensions = (String[]) fileExtensionsList.get( i );
				for ( int j = 0; j < fileExtensions.length; j++ ) {
					if ( fileInfo.getName().toLowerCase().endsWith( "." + fileExtensions[ j ] ) ) {
						return true;
					}
				}
			}
			return false;
		}
		return true;
	}
}

/*
$Log: FileExtensionFilter.java,v $
Revision 1.1  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

*/