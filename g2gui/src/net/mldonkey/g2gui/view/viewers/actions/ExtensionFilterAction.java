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

import net.mldonkey.g2gui.view.viewers.IGViewer;
import net.mldonkey.g2gui.view.viewers.filters.FileExtensionFilter;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * ExtensionFilterAction
 *
 * @version $Id: ExtensionFilterAction.java,v 1.2 2003/10/31 07:24:01 zet Exp $ 
 *
 */
public class ExtensionFilterAction extends FilterAction {
	public String[] extensions;
	/**
	 * @param aString
	 * @param anInt
	 * @param gViewer
	 */
	public ExtensionFilterAction(String name, IGViewer gViewer, String[] extensions ) {
		super( name, Action.AS_CHECK_BOX, gViewer );
		this.extensions = extensions;
		if ( this.isFiltered( this.extensions ) )
			setChecked( true );
	 }

	public void run() {
		if ( !isChecked() ) {
			ViewerFilter[] viewerFilters = gViewer.getFilters();
			for ( int i = 0; i < viewerFilters.length; i++ ) {
				if ( viewerFilters[ i ] instanceof FileExtensionFilter ) {
					FileExtensionFilter filter = (FileExtensionFilter) viewerFilters[ i ];

					for ( int j = 0; j < filter.getFileExtensionList().size(); j++ ) {
						String[] fileExtensions = (String[]) filter.getFileExtensionList().get( j );
						if ( fileExtensions.equals( extensions ) ) {
							if ( filter.getFileExtensionList().size() == 1 ) {
								toggleFilter( viewerFilters[ i ], false );
							} 
							else {
								filter.remove( fileExtensions );
								gViewer.refresh();
							}
						}
					}
				}
			}
		}
		else {
			ViewerFilter[] viewerFilters = gViewer.getFilters();
			for ( int i = 0; i < viewerFilters.length; i++ ) {
				if ( viewerFilters[ i ] instanceof FileExtensionFilter ) {
					FileExtensionFilter filter = (FileExtensionFilter) viewerFilters[ i ];
					filter.add( extensions );
					gViewer.refresh();
					return;
				}
			}
			FileExtensionFilter filter = new FileExtensionFilter();
			filter.add( extensions );
			toggleFilter( filter, true );
		}
	}
}

/*
$Log: ExtensionFilterAction.java,v $
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