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
import net.mldonkey.g2gui.model.SearchQuery;
import net.mldonkey.g2gui.view.SearchTab;
import net.mldonkey.g2gui.view.helper.CGridLayout;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
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
 * @version $Id: MusicComplexSearch.java,v 1.11 2003/09/08 10:25:26 lemmster Exp $
 *
 */
public class MusicComplexSearch extends ComplexSearch {
    private Text artistText;
    private Text albumText;
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
        return G2GuiResources.getString( "MCS_NAME" );
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
 		
        this.inputText = this.createInputBox( aSubComposite, G2GuiResources.getString( "MCS_TITLE" ) );
        this.artistText = this.createInputBox( aSubComposite, G2GuiResources.getString( "MCS_ARTIST" )  );
        this.albumText = this.createInputBox( aSubComposite, G2GuiResources.getString( "MCS_ALBUM" )  );
 
 		Label s = new Label( aComposite, SWT.SEPARATOR|SWT.HORIZONTAL );
		gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 2;
		s.setLayoutData( gridData );
 
		Composite aSubComposite2 = new Composite( aComposite, SWT.NONE );
		aSubComposite2.setLayout(CGridLayout.createGL(2,0,0,2,2,false));
		gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 2;
		aSubComposite2.setLayoutData(gridData);

        /* the bitrate label */
        gridData = new GridData( GridData.HORIZONTAL_ALIGN_FILL );
        Label label = new Label( aSubComposite2, SWT.NONE );
        label.setLayoutData( gridData );
        label.setText( G2GuiResources.getString( "MCS_BITRATE" ) );

        /* the bitrate combo */
        gridData = new GridData( GridData.FILL_HORIZONTAL );
        String[] bitrateItems = { "", "32kb", "64", "96kb", "128kb", "196kb", "265kb" };
        this.bitrateCombo = new Combo( aSubComposite2, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
		this.bitrateCombo.setLayoutData( gridData );
		this.bitrateCombo.setItems( bitrateItems );
		this.bitrateCombo.select( 0 );
		this.bitrateCombo.addMouseListener( this );

        /* the network combo */
        this.createNetworkCombo( aSubComposite2, G2GuiResources.getString( "SS_NETWORK" ) );
 
        /* the result and size controls */
        String[] items = { "", "mp3", "ogg", "wav", "midi" };
        this.createExtensionCombo( aSubComposite2, items );
        
		s = new Label(aComposite, SWT.SEPARATOR|SWT.HORIZONTAL);
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
		
		this.createSearchButton( aComposite );

        return aComposite;
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update( Observable o, Object arg ) {
        super.update( o, arg );
        if ( artistText.isDisposed() )
            return;
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
        if ( inputText.getText().equals( "" )
             && artistText.getText().equals( "" )
             && albumText.getText().equals( "" ) ) return;

		/* create an empty query */
        query = new SearchQuery( core );

		/* set our input fields */
        query.setMp3Title( inputText.getText() );
        query.setMp3Album( albumText.getText() );
        query.setMp3Artist( artistText.getText() );

		/* the bitrate */
        String bitRateString = bitrateCombo.getText();
		if ( !bitRateString.equals( "" ) ) {
			int rate = new Integer( bitRateString.substring( 0, bitRateString.length() - 2 ) ).intValue();
			query.setMp3Bitrate( new Integer( rate * 1024 ).toString() );
		}

		/* set the remaining input fields and send the query */
        super.performSearch();

        /* draw the empty search result */
        String aString = inputText.getText() + " " + artistText.getText() + " " + albumText.getText();
        new SearchResult( aString, tab.getCTabFolder(), core, query.getSearchIdentifier() );
    }
}

/*
$Log: MusicComplexSearch.java,v $
Revision 1.11  2003/09/08 10:25:26  lemmster
OtherComplexSearch added, rest improved

Revision 1.10  2003/09/07 16:18:04  zet
combo

Revision 1.9  2003/09/07 08:21:50  lemmster
resourcebundle added

Revision 1.8  2003/09/05 23:49:07  zet
1 line per search option

Revision 1.7  2003/09/05 16:50:19  lemmster
set search button correctly, verify input for result count

Revision 1.6  2003/09/05 14:22:10  lemmster
working version

Revision 1.5  2003/09/04 22:04:07  lemmster
use always a new searchquery

Revision 1.4  2003/09/04 21:57:21  lemmster
still buggy, but enough for today

Revision 1.3  2003/09/04 16:06:45  lemmster
working in progress

Revision 1.2  2003/09/04 12:17:01  lemmster
lots of changes

Revision 1.1  2003/09/03 22:15:27  lemmster
advanced search introduced; not working and far from complete. just to see the design

*/
