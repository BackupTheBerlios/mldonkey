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
import net.mldonkey.g2gui.view.SearchTab;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * ComplexSearch
 *
 *
 * @version $Id: ComplexSearch.java,v 1.3 2003/09/04 16:06:45 lemmster Exp $
 *
 */
public abstract class ComplexSearch extends Search implements Listener {
    protected Text maxText;
    protected Text minText;
    protected Combo extensionCombo;
    protected Combo resultCombo;

    /**
     * @param core
     * @param tab
     */
    public ComplexSearch( CoreCommunication core, SearchTab tab ) {
        super( core, tab );
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected abstract String getName();

    /**
     * DOCUMENT ME!
     *
     * @param aControl DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected abstract Control createContent( Control aControl );

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.search.Search#
     * createTabFolderPage(org.eclipse.swt.widgets.TabFolder)
     */
    public Control createTabFolderPage( CTabFolder tabFolder ) {
        return null;
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.search.Search#getTabName()
     */
    public String getTabName() {
        return null;
    }

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.Search#performSearch()
	 */
	public void performSearch() {
		if ( !maxText.getText().equals( "" ) )
			query.setMaxSize( new Long( maxText.getText() ).longValue() );
		if ( !minText.getText().equals( "" ) )
			query.setMinSize( new Long( minText.getText() ).longValue() );
		if ( !resultCombo.getItem( resultCombo.getSelectionIndex() ).equals( "" ) )
			query.setMaxSearchResults( 
				new Integer( resultCombo.getItem( resultCombo.getSelectionIndex()) ).intValue() );
		if ( !extensionCombo.getItem( extensionCombo.getSelectionIndex() ).equals( "" ) )
			query.setFormat( extensionCombo.getItem( extensionCombo.getSelectionIndex() ) );

		/* get the network id for this query */
		Object obj = combo.getData( combo.getItem( combo.getSelectionIndex() ) );
		if ( obj != null ) { // if != All
			NetworkInfo temp = ( NetworkInfo ) obj;
			query.setNetwork( temp.getNetwork() );
		}

		/* now the query is ready to be send */
		query.send();
						
		/* draw the empty search result */
		new SearchResult( text.getText(), tab.getCTabFolder(),
						  core, query.getSearchIdentifier() );	

		text.setText( "" );
			
		this.setStopButton();
	}

    /**
     * DOCUMENT ME!
     *
     * @param group DOCUMENT ME!
     * @param items DOCUMENT ME!
     */
    protected Combo[] createExtensionAndResultCombo( Composite group, String[] items ) {
        /* the max result label */
        GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
        gridData.horizontalSpan = 1;
        Label label = new Label( group, SWT.NONE );
        label.setLayoutData( gridData );
        label.setText( "File Extension" );

        /* the extension label */
        gridData = new GridData( GridData.FILL_HORIZONTAL );
        gridData.horizontalSpan = 1;
        label = new Label( group, SWT.NONE );
        label.setLayoutData( gridData );
        label.setText( "Max Results" );

        /* the extension box */
        gridData = new GridData( GridData.FILL_HORIZONTAL );
        gridData.horizontalSpan = 1;
        extensionCombo = new Combo( group, SWT.SINGLE | SWT.BORDER );
        extensionCombo.setLayoutData( gridData );
        extensionCombo.setItems( items );
        extensionCombo.select( 0 );

        /* the max result box */
        gridData = new GridData( GridData.FILL_HORIZONTAL );
        gridData.horizontalSpan = 1;
        String[] resultItems = { "50", "100", "200", "400" };
		resultCombo = new Combo( group, SWT.SINGLE | SWT.BORDER );
		resultCombo.setLayoutData( gridData );
		resultCombo.setItems( resultItems );
		resultCombo.select( 0 );
		/* only allow digits for this combo */
		resultCombo.addListener( SWT.Verify, this );

		return new Combo[] { extensionCombo, resultCombo };
    }

    /**
     * DOCUMENT ME!
     *
     * @param group DOCUMENT ME!
     */
    protected Text[] createMaxMinSizeText( Composite group ) {
		/* the max size label */
        GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
        Label label = new Label( group, SWT.NONE );
        label.setLayoutData( gridData );
        label.setText( "Max Size" );

		/* the min size label */
		gridData = new GridData( GridData.FILL_HORIZONTAL );
		label = new Label( group, SWT.NONE );
		label.setLayoutData( gridData );
		label.setText( "Min Size" );
        
		/* the max size text */
        gridData = new GridData( GridData.FILL_HORIZONTAL );
        maxText = new Text( group, SWT.SINGLE | SWT.BORDER );
        maxText.setLayoutData( gridData );
		maxText.addListener( SWT.Verify, this );

        /* the min size text */
        gridData = new GridData( GridData.FILL_HORIZONTAL );
		minText = new Text( group, SWT.SINGLE | SWT.BORDER );
		minText.setLayoutData( gridData );
		minText.addListener( SWT.Verify, this );
		
		return new Text[] { maxText, minText };
    }
    
    public void handleEvent( Event e ) {
		String aString = e.text;
		char[] chars = new char[ aString.length() ];
		aString.getChars( 0, chars.length, chars, 0 );
		for ( int i = 0; i < chars.length; i++ ) {
			if ( !( '0' <= chars[ i ] && chars[ i ] <= '9' ) ) {
				e.doit = false;
				return;
			}
		}
   }
}

/*
$Log: ComplexSearch.java,v $
Revision 1.3  2003/09/04 16:06:45  lemmster
working in progress

Revision 1.2  2003/09/04 12:17:01  lemmster
lots of changes

Revision 1.1  2003/09/03 22:15:27  lemmster
advanced search introduced; not working and far from complete. just to see the design

Revision 1.8  2003/09/01 11:09:43  lemmster
show downloading files

Revision 1.7  2003/08/29 19:09:25  dek
new look'n feel

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
