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
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.SearchQuery;
import net.mldonkey.g2gui.view.SearchTab;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * SimpleSearch
 *
 *
 * @version $Id: SimpleSearch.java,v 1.25 2003/11/24 08:37:24 lemmster Exp $ 
 *
 */
public class SimpleSearch extends Search {
	/**
	 * @param core The parent CoreCommunication
	 * @param tab The parent SearchTab
	 */
	public SimpleSearch( CoreCommunication core, SearchTab tab ) {
		super( core, tab );
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.Search#getTabName()
	 */
	public String getTabName() {
		return G2GuiResources.getString( "SS_TITLE" );
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.Search#
	 * createTabFolderPage(org.eclipse.swt.widgets.TabFolder)
	 */
	public Control createTabFolderPage( Composite tabFolder ) {
		/* set the minimum width so, that the whole title is visible */
		( ( CTabFolder ) tabFolder ).MIN_TAB_WIDTH =
				( ( CTabFolder ) tabFolder ).computeSize( SWT.DEFAULT, SWT.DEFAULT ).y;
		/* the input field */
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 2;
		gridData.horizontalIndent = 12;
		gridData.widthHint = 500;
		Composite group = new Composite( tabFolder, SWT.NONE );
		group.setLayout( gridLayout );
		group.setLayoutData( gridData );
			
		this.inputText = 
			this.createInputBox( group, G2GuiResources.getString( "SS_STRING" ) );
		this.createNetworkCombo( group, G2GuiResources.getString( "SS_NETWORK" ) );
		
		if ( PreferenceLoader.loadBoolean( "useCombo" ) )	
			this.createMediaControl( group, G2GuiResources.getString( "SS_MEDIA" ), 0 );
		else
			this.createMediaControl( group, null, 1 );	

		return group;		
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.Search#performSearch()
	 */
	public void performSearch() {
		
		if ( ! inputText.getText().equals( "" ) ) {

			String aText = this.inputText.getText();
			if ( this.inputText.indexOf( aText ) != -1 )
				this.inputText.remove( aText );
			this.inputText.add( aText, 0 );

			query = new SearchQuery( core );
			/* the query string */
			query.setSearchString( aText );
					
			/* get the network id for this query */
			Object obj = networkCombo.getData( networkCombo.getItem( networkCombo.getSelectionIndex() ) );
			if ( obj != null ) { // if != All
				NetworkInfo temp = ( NetworkInfo ) obj;
				query.setNetwork( temp.getNetwork() );
			}

			/* which media is selected */							
			if ( selectedMedia != null )
				query.setMedia( selectedMedia );
			/* now the query is ready to be send */
			query.send();
						
			/* draw the empty search result */
			new SearchResult( aText, tab.getCTabFolder(),
							  core, query.getSearchIdentifier(), this.tab );	

			inputText.setText( "" );
			
			tab.setStopButton();
		}
	}
}

/*
$Log: SimpleSearch.java,v $
Revision 1.25  2003/11/24 08:37:24  lemmster
fix [Bug #1132] search combo retains duplicates

Revision 1.24  2003/11/23 19:22:35  lemmster
fixed: [ Bug #1119] Search field a combo holding previous searches

Revision 1.23  2003/09/24 05:53:00  lemmster
CTabFolder -> Composite

Revision 1.22  2003/09/19 15:19:14  lemmster
reworked

Revision 1.21  2003/09/18 10:39:21  lemmster
checkstyle

Revision 1.20  2003/09/08 11:54:23  lemmster
added download button

Revision 1.19  2003/09/08 10:25:26  lemmster
OtherComplexSearch added, rest improved

Revision 1.18  2003/09/07 16:12:20  zet
combo

Revision 1.17  2003/09/07 08:10:56  lemmster
back to radiobuttons

Revision 1.15  2003/09/05 14:22:10  lemmster
working version

Revision 1.14  2003/09/04 22:04:07  lemmster
use always a new searchquery

Revision 1.13  2003/09/04 16:06:45  lemmster
working in progress

Revision 1.12  2003/09/04 12:17:01  lemmster
lots of changes

Revision 1.11  2003/08/31 12:32:04  lemmster
major changes to search

Revision 1.10  2003/08/29 19:09:25  dek
new look'n feel

Revision 1.9  2003/08/23 15:21:37  zet
remove @author

Revision 1.8  2003/08/22 21:10:57  lemmster
replace $user$ with $Author: lemmster $

Revision 1.7  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.6  2003/08/11 19:03:53  lemmstercvs01
update networkcombo when a networkinfo status changes

Revision 1.5  2003/07/28 08:19:20  lemmstercvs01
get NetworkInfo by Enum instead of NetworkName

Revision 1.4  2003/07/27 18:45:47  lemmstercvs01
lots of changes

Revision 1.3  2003/07/24 16:20:10  lemmstercvs01
lots of changes

Revision 1.2  2003/07/23 19:49:17  zet
press enter

Revision 1.1  2003/07/23 16:56:28  lemmstercvs01
initial commit

*/