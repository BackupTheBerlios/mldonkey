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

import java.util.Observable;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.SearchTab;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * MusicComplexSearch
 *
 * @version $Id: MusicComplexSearch.java,v 1.4 2003/09/04 21:57:21 lemmster Exp $ 
 *
 */
public class MusicComplexSearch extends ComplexSearch {
	private Text artistText, albumText;
	private Combo bitrateCombo;
	
	/**
	 * @param core
	 * @param tab
	 */
	public MusicComplexSearch( CoreCommunication core, SearchTab tab ) {
		super( core, tab );
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.ComplexSearch#getTabName()
	 */
	protected String getName() {
		return "Music";
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.ComplexSearch#createContent(org.eclipse.swt.widgets.Control)
	 */
	protected Control createContent(Control aControl) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		Composite composite = new Composite( ( Composite ) aControl, SWT.NONE );
		composite.setLayout( gridLayout );
		
		this.text = this.createInputBox( composite, "Title" );
		this.artistText = this.createInputBox( composite, "Artist" );
		this.albumText = this.createInputBox( composite, "Album" );
		
		/* the bitrate label */
		GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 2;
		Label label = new Label( composite, SWT.NONE );
		label.setLayoutData( gridData );
		label.setText( "Bitrate" );
		
		/* the bitrate combo */
		gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 2;
		String[] bitrateItems = { "whatever", "96kb", "128kb", "196kb" };
		bitrateCombo = new Combo( composite, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
		bitrateCombo.setLayoutData( gridData );
		bitrateCombo.setItems( bitrateItems );
		bitrateCombo.select( 0 );
		
		/* the network combo */
		this.combo = 
			this.createNetworkCombo( composite, G2GuiResources.getString( "SS_NETWORK" ) );

		/* the result and size controls */
		String[] items = { "", "mp3", "ogg", "wav", "midi" };
		Combo[] combos =
			this.createExtensionAndResultCombo( composite, items );
		this.extensionCombo = ( Combo ) combos[ 0 ];
		this.resultCombo = ( Combo ) combos[ 1 ];

		/* the min and max size text fields */
		Control[][] controls =
			this.createMaxMinSizeText( composite );
		this.maxText = ( Text ) controls[ 0 ][ 0 ];
		this.maxCombo = ( Combo ) controls[ 0 ][ 1 ];
		this.minText = ( Text ) controls[ 1 ][ 0 ];
		this.minCombo = ( Combo ) controls[ 1 ][ 1 ];

		/* the search button */
		Object[] obj =
			this.createSearchButton( composite );
  		this.stackLayout = ( StackLayout ) obj[ 0 ];
  		this.composite = ( Composite ) obj[ 1 ];
  
		return composite;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update( Observable o, Object arg ) {
		super.update( o, arg );
		if ( artistText.isDisposed() ) return;
		
		artistText.getDisplay().asyncExec( new Runnable() {
			public void run() {
				/* update the other text */
				 if ( core.getNetworkInfoMap().getEnabledAndSearchable() == 0 ) {
				 	artistText.setEnabled( false );
				 	albumText.setEnabled( false );
					bitrateCombo.setEnabled( false );
				 }
				 else {
					artistText.setEnabled( true );
					albumText.setEnabled( true );
				 	bitrateCombo.setEnabled( true );
				 }
			}
		} );
	}
	
	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.Search#performSearch()
	 */
	public void performSearch() {
		if ( text.getText().equals( "" )
		&& artistText.getText().equals( "" )
		&& albumText.getText().equals( "" ) ) return;
		
		query.setMp3Title( text.getText() );
		query.setMp3Album( albumText.getText() );
		query.setMp3Artist( artistText.getText() );
		query.setMp3Bitrate(
			bitrateCombo.getItem( bitrateCombo.getSelectionIndex() ) );
		
		super.performSearch();
		
		/* draw the empty search result */
		String aString = text.getText() + " "
						 + artistText.getText() + " "
						 + albumText.getText();
		new SearchResult( aString, tab.getCTabFolder(),
						  core, query.getSearchIdentifier() );	

	}

}

/*
$Log: MusicComplexSearch.java,v $
Revision 1.4  2003/09/04 21:57:21  lemmster
still buggy, but enough for today

Revision 1.3  2003/09/04 16:06:45  lemmster
working in progress

Revision 1.2  2003/09/04 12:17:01  lemmster
lots of changes

Revision 1.1  2003/09/03 22:15:27  lemmster
advanced search introduced; not working and far from complete. just to see the design

*/