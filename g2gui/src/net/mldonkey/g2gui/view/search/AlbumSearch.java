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
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;

/**
 * AlbumSearch
 *
 *
 * @version $Id: AlbumSearch.java,v 1.6 2003/08/23 15:21:37 zet Exp $ 
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
		this.createNetworkBox( new Group( tabFolder, SWT.NONE ), G2GuiResources.getString( "SS_NETWORK" ) );
		this.createInputBox( new Group( tabFolder, SWT.NONE ), G2GuiResources.getString( "SS_NETWORK" ) );
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
Revision 1.6  2003/08/23 15:21:37  zet
remove @author

Revision 1.5  2003/08/20 10:04:41  lemmster
inputbox disabled when zero searchable networks are enabled

Revision 1.4  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.3  2003/08/11 19:03:53  lemmstercvs01
update networkcombo when a networkinfo status changes

Revision 1.2  2003/07/24 16:20:10  lemmstercvs01
lots of changes

Revision 1.1  2003/07/23 16:56:28  lemmstercvs01
initial commit

*/