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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;

/**
 * SimpleSearch
 *
 * @author $user$
 * @version $Id: SimpleSearch.java,v 1.2 2003/07/23 19:49:17 zet Exp $ 
 *
 */
public class SimpleSearch extends Search {
	private GridLayout gridLayout;
	private Group group;
	private Button ok, clear, all, audio, video, image, software;
	private GridData gridData;
	private String selectedMedia;

	/**
	 * @param core
	 */
	public SimpleSearch( CoreCommunication core, SearchTab tab ) {
		super( core, tab );
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.Search#getTabName()
	 */
	public String getTabName() {
		return "SimpleSearch";
	}
	
	public void performSearch() {
	
		if ( ! text.getText().equals( "" ) ) {
			/* generate a new search query */
			SearchQuery query = new SearchQuery( core );
				
			/* the query string */
			query.setSearchString( text.getText() );
						
			/* get the network id for this query */
			NetworkInfo[] temp = core.getNetworkInfoMap().getNetworks();
			for ( int i = 0; i < temp.length; i++ ) {
				if ( temp[ i ].getNetworkName().equals( combo.getText() ) ) {
					query.setNetwork( temp[ i ].getNetwork() );
					break;										
				}
			}
			/* which media is selected */							
			if ( selectedMedia != null )
			query.setMedia( selectedMedia );
			/* now the query is ready to be send */
			query.send();
						
			/* draw the empty search result */
			new SearchResult( text.getText(), tab.getCTabFolder(), core, query.getSearchIdentifier() );	
			text.setText("");
		}
		
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.Search#createTabFolderPage(org.eclipse.swt.widgets.TabFolder)
	 */
	public Control createTabFolderPage(TabFolder tabFolder) {

		/* the input field */
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 2;
		gridData.horizontalIndent = 12;
		group = new Group( tabFolder, SWT.NONE );
		group.setLayout( gridLayout );
		group.setLayoutData( gridData );
			
			this.createInputBox( group, "Search String" );
			this.createNetworkBox( group, "Network" );
			
			/* media select */
			gridData = new GridData();
			gridData.horizontalSpan = 2;
			all = new Button( group, SWT.RADIO );
			all.setLayoutData( gridData );
			all.setText( "All" );
			/* we want a default selection */
			all.setSelection( true );
			all.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent event ) {
					selectedMedia = null;
				}	
			});

			gridData = new GridData();
			gridData.horizontalSpan = 2;
			audio = new Button( group, SWT.RADIO );
			audio.setLayoutData( gridData );
			audio.setText( "Audio" );
			audio.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent event ) {
					selectedMedia = "Audio";
				}	
			});
			
			gridData = new GridData();
			gridData.horizontalSpan = 2;
			video = new Button( group, SWT.RADIO );
			video.setLayoutData( gridData );
			video.setText( "Video" );
			video.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent event ) {
					selectedMedia = "Video";
				}	
			});
			
			gridData = new GridData();
			gridData.horizontalSpan = 2;
			image = new Button( group, SWT.RADIO );
			image.setLayoutData( gridData );
			image.setText( "Image" );
			image.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent event ) {
					selectedMedia = "Image";
				}	
			});
			
			gridData = new GridData();
			gridData.horizontalSpan = 2;
			software = new Button( group, SWT.RADIO );
			software.setLayoutData( gridData );
			software.setText( "Software" );
			software.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent event ) {
					selectedMedia = "Software";
				}	
			});

			/* search button */
			ok = new Button( group, SWT.PUSH );
			ok.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			ok.setText( "Search" );
			ok.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent event ) {
					performSearch();
				}		
			});
		return group;		
	}
}

/*
$Log: SimpleSearch.java,v $
Revision 1.2  2003/07/23 19:49:17  zet
press enter

Revision 1.1  2003/07/23 16:56:28  lemmstercvs01
initial commit

*/