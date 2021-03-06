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

import net.mldonkey.g2gui.model.enum.EnumExtension;
import net.mldonkey.g2gui.view.viewers.GView;
import net.mldonkey.g2gui.view.viewers.filters.FileExtensionGViewerFilter;
import net.mldonkey.g2gui.view.viewers.filters.GViewerFilter;

import org.eclipse.jface.action.Action;

/**
 * ExtensionFilterAction
 *
 * @version $Id: ExtensionFilterAction.java,v 1.6 2003/12/04 08:47:30 lemmy Exp $ 
 *
 */
public class ExtensionFilterAction extends FilterAction {
	public EnumExtension extensions;
	/**
	 * @param aString
	 * @param anInt
	 * @param gViewer
	 */
	public ExtensionFilterAction(String name, GView gViewer, EnumExtension extensions ) {
		super( name, Action.AS_CHECK_BOX, gViewer );
		this.extensions = extensions;
		if ( this.isFiltered( this.extensions ) )
			setChecked( true );
	 }

	public void run() {
		GViewerFilter filter = gViewer.getFilter( FileExtensionGViewerFilter.class );
		if ( !isChecked() ) {
			if ( filter.matches( extensions ) )
				removeFilter( filter, extensions );
		}
		else {
			if (filter.isNotAlwaysFalse())
				addFilter( filter, extensions );
			else
				addFilter(new FileExtensionGViewerFilter(gViewer), extensions);
		}	
	}
}

/*
$Log: ExtensionFilterAction.java,v $
Revision 1.6  2003/12/04 08:47:30  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.5  2003/11/06 13:52:33  lemmy
filters back working

Revision 1.4  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.3  2003/10/31 10:42:47  lemmy
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

Revision 1.1  2003/10/29 16:56:21  lemmy
added reasonable class hierarchy for panelisteners, viewers...

*/