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

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.SearchTab;

import org.eclipse.swt.widgets.Control;

/**
 * VideoComplexSearch
 *
 * @version $Id: VideoComplexSearch.java,v 1.4 2003/12/04 08:47:29 lemmy Exp $ 
 *
 */
public class VideoComplexSearch extends ComplexSearch {

	/**
	 * Creates a new VideoComplexSearch obj
	 * 
	 * @param core The core obj with the <code>Information</code>
	 * @param tab The <code>GuiTab</code> we draw this obj inside
	 */
	public VideoComplexSearch( CoreCommunication core, SearchTab tab ) {
		super( core, tab );
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.ComplexSearch#getName()
	 */
	protected String getName() {
		return "Video";
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.ComplexSearch#createContent(org.eclipse.swt.widgets.Control)
	 */
	protected Control createContent( Control aControl ) {
		return null;
	}
}

/*
$Log: VideoComplexSearch.java,v $
Revision 1.4  2003/12/04 08:47:29  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.3  2003/09/18 11:18:46  lemmy
checkstyle

Revision 1.2  2003/09/04 12:17:01  lemmy
lots of changes

Revision 1.1  2003/09/03 22:15:27  lemmy
advanced search introduced; not working and far from complete. just to see the design

*/