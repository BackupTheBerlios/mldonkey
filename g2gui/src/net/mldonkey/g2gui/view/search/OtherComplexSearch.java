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
import net.mldonkey.g2gui.model.SearchQuery;
import net.mldonkey.g2gui.view.SearchTab;
import net.mldonkey.g2gui.view.helper.CGridLayout;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * OtherComplexSearch
 *
 * @version $Id: OtherComplexSearch.java,v 1.5 2003/09/19 15:19:14 lemmster Exp $ 
 *
 */
public class OtherComplexSearch extends ComplexSearch {
	/**
	 * Creates a new OtherComplexSearch obj.
	 * 
	 * @param core The core obj with the <code>Information</code>
	 * @param tab The <code>GuiTab</code> we draw this obj inside
	 */
	public OtherComplexSearch( CoreCommunication core, SearchTab tab ) {
		super( core, tab );
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.ComplexSearch#getName()
	 */
	protected String getName() {
		return G2GuiResources.getString( "OCS_NAME" );
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.ComplexSearch#createContent(org.eclipse.swt.widgets.Control)
	 */
	protected Control createContent( Control aControl ) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		Composite aComposite = new Composite( ( Composite ) aControl, SWT.NONE );
		aComposite.setLayout( gridLayout );
		 		
		/* the input boxes */
		Composite aSubComposite = new Composite( aComposite, SWT.NONE );
		aSubComposite.setLayout( CGridLayout.createGL( 2, 0, 0, 2, 2, false ) );
		GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 2;
		aSubComposite.setLayoutData( gridData );

		this.inputText = this.createInputBox( aSubComposite, G2GuiResources.getString( "OCS_TITLE" ) );

		/* the network combo */
		this.createNetworkCombo( aSubComposite, G2GuiResources.getString( "SS_NETWORK" ) );
 
 		this.createMediaControl( aSubComposite, G2GuiResources.getString( "OCS_MEDIA" ), 0 );
 
		/* the result and size controls */
		String[] items = { "", "exe", "bin", "img", "gif", "jpg" };
		this.createExtensionCombo( aSubComposite, items );
        
		Label s = new Label( aComposite, SWT.SEPARATOR | SWT.HORIZONTAL );
		gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 2;
		s.setLayoutData( gridData );
		
		Composite aSubComposite3 = new Composite( aComposite, SWT.NONE );
		aSubComposite3.setLayout( CGridLayout.createGL( 2, 0, 0, 2, 2, false ) );
		gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 2;
		aSubComposite3.setLayoutData( gridData );
		
		/* the min and max size text fields */
		if ( PreferenceLoader.loadBoolean( "useCombo" ) )
			this.createMinMaxSizeText( aSubComposite3 );
		else
			this.createMaxMinSizeText( aComposite );
		
		this.createResultCombo( aSubComposite3 );

		aComposite.setData( this );

		return aComposite;
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.Search#performSearch()
	 */
	public void performSearch() {
		String aText = this.inputText.getText();
		if ( aText.equals( "" ) ) return;

		/* create an empty query */
		query = new SearchQuery( core );

		/* set our input fields */
		query.setSearchString( aText );
		
		/* which media is selected */							
		if ( selectedMedia != null )
			query.setMedia( selectedMedia );

		/* set the remaining input fields and send the query */
		super.performSearch();

		/* draw the empty search result */
		String aString = inputText.getText();
		new SearchResult( aText, tab.getCTabFolder(),
			 core, query.getSearchIdentifier(), this.tab );
	}
}

/*
$Log: OtherComplexSearch.java,v $
Revision 1.5  2003/09/19 15:19:14  lemmster
reworked

Revision 1.4  2003/09/18 10:39:21  lemmster
checkstyle

Revision 1.3  2003/09/08 11:54:22  lemmster
added download button

Revision 1.2  2003/09/08 10:25:26  lemmster
OtherComplexSearch added, rest improved

Revision 1.1  2003/09/03 22:15:27  lemmster
advanced search introduced; not working and far from complete. just to see the design

*/