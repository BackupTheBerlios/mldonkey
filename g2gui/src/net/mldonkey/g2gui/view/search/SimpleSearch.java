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
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * SimpleSearch
 *
 *
 * @version $Id: SimpleSearch.java,v 1.11 2003/08/31 12:32:04 lemmster Exp $ 
 *
 */
public class SimpleSearch extends Search {
	private GridLayout gridLayout;
	private Composite group;
	private Button ok, clear, all, audio, video, image, software;
	private GridData gridData;
	private String selectedMedia;

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
	public Control createTabFolderPage( CTabFolder tabFolder ) {		
		/* set the minimum width so, that the whole title is visible */
		tabFolder.MIN_TAB_WIDTH = tabFolder.computeSize( SWT.DEFAULT, SWT.DEFAULT ).y;
		/* the input field */
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 2;
		gridData.horizontalIndent = 12;
		gridData.widthHint=500;
		group = new Composite( tabFolder, SWT.NONE );
		group.setLayout( gridLayout );
		group.setLayoutData( gridData );
			
			this.createInputBox( group, G2GuiResources.getString( "SS_STRING" ) );
			this.createNetworkBox( group, G2GuiResources.getString( "SS_NETWORK" ) );
			
			/* media select */
			gridData = new GridData();
			gridData.horizontalSpan = 2;
			all = new Button( group, SWT.RADIO );
			all.setLayoutData( gridData );
			all.setText( G2GuiResources.getString( "SS_ALL" ) );
			/* we want a default selection */
			all.setSelection( true );
			all.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent event ) {
					selectedMedia = null;
				}	
			} );

			gridData = new GridData();
			gridData.horizontalSpan = 2;
			audio = new Button( group, SWT.RADIO );
			audio.setLayoutData( gridData );
			audio.setText( G2GuiResources.getString( "SS_AUDIO" ) );
			audio.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent event ) {
					selectedMedia = "Audio";
				}	
			} );
			
			gridData = new GridData();
			gridData.horizontalSpan = 2;
			video = new Button( group, SWT.RADIO );
			video.setLayoutData( gridData );
			video.setText( G2GuiResources.getString( "SS_VIDEO" ) );
			video.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent event ) {
					selectedMedia = "Video";
				}	
			} );
			
			gridData = new GridData();
			gridData.horizontalSpan = 2;
			image = new Button( group, SWT.RADIO );
			image.setLayoutData( gridData );
			image.setText( G2GuiResources.getString( "SS_IMAGE" ) );
			image.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent event ) {
					selectedMedia = "Image";
				}	
			} );
			
			gridData = new GridData();
			gridData.horizontalSpan = 2;
			software = new Button( group, SWT.RADIO );
			software.setLayoutData( gridData );
			software.setText( G2GuiResources.getString( "SS_Software" ) );
			software.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent event ) {
					selectedMedia = "Software";
				}	
			} );
			this.createSearchButton( group );
		return group;		
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.Search#performSearch()
	 */
	public void performSearch() {
		if ( ! text.getText().equals( "" ) ) {
			/* generate a new search query */
			SearchQuery query = new SearchQuery( core );
			
			/* the query string */
			query.setSearchString( text.getText() );
					
			/* get the network id for this query */
			Object obj = combo.getData( combo.getItem( combo.getSelectionIndex() ) );
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
			new SearchResult( text.getText(), tab.getCTabFolder(),
							   core, query.getSearchIdentifier() );	

			query = null;
			text.setText( "" );
			
			this.setStopButton();
		}
	}
}

/*
$Log: SimpleSearch.java,v $
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