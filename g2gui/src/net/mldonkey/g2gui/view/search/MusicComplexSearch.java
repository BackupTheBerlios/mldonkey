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
import net.mldonkey.g2gui.view.helper.WidgetFactory;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * MusicComplexSearch
 *
 * @version $Id: MusicComplexSearch.java,v 1.20 2003/11/24 08:56:22 lemmster Exp $
 *
 */
public class MusicComplexSearch extends ComplexSearch {
    private Combo artistText;
    private Combo albumText;
    private Combo bitrateCombo;

	/**
	 * Creates a new MusicComplexSearch obj
	 * 
	 * @param core The core obj with the <code>Information</code>
	 * @param tab The <code>GuiTab</code> we draw this obj inside
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
 		aSubComposite.setLayout( WidgetFactory.createGridLayout( 2, 0, 0, 2, 2, false ) );
		GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 2;
 		aSubComposite.setLayoutData( gridData );
 		
        this.inputText = this.createInputBox( aSubComposite, G2GuiResources.getString( "MCS_TITLE" ) );
        this.artistText = this.createInputBox( aSubComposite, G2GuiResources.getString( "MCS_ARTIST" )  );
        this.albumText = this.createInputBox( aSubComposite, G2GuiResources.getString( "MCS_ALBUM" )  );
 
 		Label s = new Label( aComposite, SWT.SEPARATOR | SWT.HORIZONTAL );
		gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 2;
		s.setLayoutData( gridData );
 
		Composite aSubComposite2 = new Composite( aComposite, SWT.NONE );
		aSubComposite2.setLayout( WidgetFactory.createGridLayout( 2, 0, 0, 2, 2, false ) );
		gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 2;
		aSubComposite2.setLayoutData( gridData );

        /* the bitrate label */
        gridData = new GridData( GridData.HORIZONTAL_ALIGN_FILL );
        Label label = new Label( aSubComposite2, SWT.NONE );
        label.setLayoutData( gridData );
        label.setText( G2GuiResources.getString( "MCS_BITRATE" ) );

        /* the bitrate combo */
        gridData = new GridData( GridData.FILL_HORIZONTAL );
        String[] bitrateItems = { "", "32kb", "64kb", "96kb", "128kb", "160kb", "192kb", "265kb" };
        this.bitrateCombo = new Combo( aSubComposite2, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
		this.bitrateCombo.setLayoutData( gridData );
		this.bitrateCombo.setItems( bitrateItems );
		this.bitrateCombo.select( 0 );
		this.bitrateCombo.addMouseListener( this );
		this.bitrateCombo.setToolTipText( G2GuiResources.getString( "MCS_TOOLHELP" ) );

        /* the network combo */
        this.createNetworkCombo( aSubComposite2, G2GuiResources.getString( "SS_NETWORK" ) );
 
        /* the result and size controls */
        String[] items = { "", "mp3", "ogg", "wav", "midi" };
        this.createExtensionCombo( aSubComposite2, items );
        
		s = new Label( aComposite, SWT.SEPARATOR | SWT.HORIZONTAL );
		gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 2;
		s.setLayoutData( gridData );
		
		Composite aSubComposite3 = new Composite( aComposite, SWT.NONE );
		aSubComposite3.setLayout( WidgetFactory.createGridLayout( 2, 0, 0, 2, 2, false ) );
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
        String input = this.inputText.getText();
        String album = this.albumText.getText();
        String artist = this.artistText.getText();
    	
    	if ( input.equals( "" )
             && artist.equals( "" )
             && album.equals( "" ) ) return;

    	// set the combo text
    	this.inputText.add( input, 0 );
    	this.albumText.add( album, 0 );
		this.artistText.add( artist, 0 );
		
		/* create an empty query */
        query = new SearchQuery( core );

		/* set our input fields */
        query.setMp3Title( input );
        query.setMp3Album( album );
        query.setMp3Artist( artist );

		/* the bitrate */
        String bitRateString = bitrateCombo.getText();
		if ( !bitRateString.equals( "" ) ) {
			int rate = new Integer( bitRateString.substring( 0, bitRateString.length() - 2 ) ).intValue();
			query.setMp3Bitrate( new Integer( rate ).toString() );
		}

		/* set the remaining input fields and send the query */
        super.performSearch();

        /* draw the empty search result */
        String aString = input + " " + artist + " " + album;
        new SearchResult( aString.trim(), tab.getCTabFolder(),
        	 core, query.getSearchIdentifier(), this.tab );
    }
}

/*
$Log: MusicComplexSearch.java,v $
Revision 1.20  2003/11/24 08:56:22  lemmster
fix [Bug #1132] search combo retains duplicates (better solution)

Revision 1.19  2003/11/24 08:37:24  lemmster
fix [Bug #1132] search combo retains duplicates

Revision 1.18  2003/11/23 19:22:35  lemmster
fixed: [ Bug #1119] Search field a combo holding previous searches

Revision 1.17  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

Revision 1.16  2003/09/19 15:19:14  lemmster
reworked

Revision 1.15  2003/09/18 10:39:21  lemmster
checkstyle

Revision 1.14  2003/09/08 13:00:20  lemmster
added tooltip

Revision 1.13  2003/09/08 12:37:39  lemmster
fix typo

Revision 1.12  2003/09/08 11:54:23  lemmster
added download button

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
