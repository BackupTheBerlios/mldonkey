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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;

/**
 * AlbumSearch
 *
 * @author $user$
 * @version $Id: AlbumSearch.java,v 1.3 2003/08/11 19:03:53 lemmstercvs01 Exp $ 
 *
 */
public class AlbumSearch extends Search {

	/**
	 * @param core
	 */
	public AlbumSearch( CoreCommunication core, SearchTab tab ) {
		super( core, tab );

	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.Search#getTabName()
	 */
	public String getTabName() {
		return "AlbumSearch";
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.Search#
	 * createTabFolderPage(org.eclipse.swt.widgets.TabFolder)
	 */
	public Control createTabFolderPage( TabFolder tabFolder ) {
		// create a invisible networkbox to avoid a npe in update(Observable o, Object arg);
		this.createNetworkBox( new Group( tabFolder, SWT.NONE ), bundle.getString( "SS_NETWORK" ) );
		return null;
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.Search#performSearch()
	 */
	public void performSearch() {
	}
}

/*
$Log: AlbumSearch.java,v $
Revision 1.3  2003/08/11 19:03:53  lemmstercvs01
update networkcombo when a networkinfo status changes

Revision 1.2  2003/07/24 16:20:10  lemmstercvs01
lots of changes

Revision 1.1  2003/07/23 16:56:28  lemmstercvs01
initial commit

*/