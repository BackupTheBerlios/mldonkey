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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Text;

/**
 * MusicComplexSearch
 *
 * @version $Id: MusicComplexSearch.java,v 1.2 2003/09/04 12:17:01 lemmster Exp $ 
 *
 */
public class MusicComplexSearch extends ComplexSearch {
	private Text artistText, albumText;
	
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
		artistText = this.createInputBox( composite, "Artist" );
		albumText = this.createInputBox( composite, "Album" );
		
		/* the network combo */
		this.combo = 
			this.createNetworkCombo( composite, G2GuiResources.getString( "SS_NETWORK" ) );

		/* the result and size controls */
		String[] items = { "mp3", "ogg", "wav", "midi" };
		Control[] controls =
			this.createExtensionAndResultBox( composite, items );
		this.extensionCombo = ( Combo ) controls[ 0 ];
		this.resultSlider = ( Slider ) controls[ 1 ];

		/* the min and max size text fields */
		Text[] texts =
			this.createMaxMinSizeText( composite );
		this.maxText = texts[ 0 ];
		this.minText = texts[ 1 ];

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
		/* update the other text */
		 if ( core.getNetworkInfoMap().getEnabledAndSearchable() == 0 ) {
			 artistText.setEnabled( false );
			 albumText.setEnabled( false );
		 }
		 else {
			artistText.setEnabled( true );
			albumText.setEnabled( true );
		 }
	}
	
	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.Search#performSearch()
	 */
	public void performSearch() {
		if ( text.getText().equals( "" ) ) return;
		super.performSearch();
	}

}

/*
$Log: MusicComplexSearch.java,v $
Revision 1.2  2003/09/04 12:17:01  lemmster
lots of changes

Revision 1.1  2003/09/03 22:15:27  lemmster
advanced search introduced; not working and far from complete. just to see the design

*/