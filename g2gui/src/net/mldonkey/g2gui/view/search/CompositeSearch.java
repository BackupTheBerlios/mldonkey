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

import java.util.List;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.SearchTab;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * CompositeSearch
 *
 * @version $Id: CompositeSearch.java,v 1.3 2003/09/05 14:22:10 lemmster Exp $
 *
 */
public class CompositeSearch extends Search {
    private List aList;
    private Control[] searches;
    private int size;

    /**
     * @param core
     * @param tab
     */
    public CompositeSearch( CoreCommunication core, SearchTab tab, List aList ) {
        super( core, tab );
		this.aList = aList;
        
        /* we dont need to update anything, so deregister on der observable */
        core.getNetworkInfoMap().deleteObserver( this );
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.search.Search#getTabName()
     */
    public String getTabName() {
        return "Advanced Search";
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.search.Search#createTabFolderPage(org.eclipse.swt.custom.CTabFolder)
     */
    public Control createTabFolderPage( CTabFolder tabFolder ) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
        gridData.horizontalSpan = 2;
        gridData.horizontalIndent = 12;
        gridData.widthHint = 500;
        Composite mainComposite = new Composite( tabFolder, SWT.NONE );
        mainComposite.setLayout( gridLayout );
        mainComposite.setLayoutData( gridData );

        /* search type (music/video/other...) */
        gridData = new GridData( GridData.FILL_HORIZONTAL );
        gridData.horizontalSpan = 2;
        Label label = new Label( mainComposite, SWT.NONE );
        label.setLayoutData( gridData );
        label.setText( "Search Type" );

        /* store the names of the complexsearches in a string[] */
        size = aList.size();
        String[] searchNames = new String[ size ];
        for ( int i = 0; i < size; i++ ) {
            ComplexSearch aComplexSearch = ( ComplexSearch ) aList.get( i );
            searchNames[ i ] = aComplexSearch.getName();
        }
        gridData = new GridData( GridData.FILL_HORIZONTAL );
        gridData.horizontalSpan = 2;
        Combo typeCombo = new Combo( mainComposite, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
        typeCombo.setLayoutData( gridData );
        typeCombo.setItems( searchNames );
        typeCombo.select( 0 );
        typeCombo.addSelectionListener( new SelectionListener() {
			public void widgetDefaultSelected( SelectionEvent e ) { }
            public void widgetSelected( SelectionEvent e ) {
                Combo combo = ( Combo ) e.widget;
                String aString = combo.getItem( combo.getSelectionIndex() );
                for ( int i = 0; i < size; i++ ) {
                    ComplexSearch aComplexSearch = ( ComplexSearch ) aList.get( i );
                    if ( aComplexSearch.getName().equals( aString ) ) {
						stackLayout.topControl = searches[ i ];
						composite.layout();
						return;
                    }
                }
            }
        } );

        /* a horizontal line */
        gridData = new GridData( GridData.FILL_HORIZONTAL );
        gridData.horizontalSpan = 2;
        Label bar = new Label( mainComposite, SWT.SEPARATOR | SWT.HORIZONTAL );
        bar.setLayoutData( gridData );
        
        /* the different search masks */
        StackLayout aStackLayout = new StackLayout();
        gridData = new GridData( GridData.FILL_HORIZONTAL );
        gridData.horizontalSpan = 2;
        composite = new Composite( mainComposite, SWT.NONE );
        composite.setLayout( aStackLayout );
        composite.setLayoutData( gridData );

        /* store the controls of the complexsearches in a control[] */
        searches = new Control[ size ];
        for ( int i = 0; i < size; i++ ) {
            ComplexSearch aComplexSearch = ( ComplexSearch ) aList.get( i );
            searches[ i ] = aComplexSearch.createContent( composite );
        }
		aStackLayout.topControl = searches[ 0 ];
        
        return mainComposite;
    }
    
    public void setStopButton() {
    	for ( int i = 0; i < size; i++ ) {
			ComplexSearch aComplexSearch = ( ComplexSearch ) aList.get( i );
			aComplexSearch.setStopButton();
    	}
    }
    
    public void setContinueButton() {
		for ( int i = 0; i < size; i++ ) {
			ComplexSearch aComplexSearch = ( ComplexSearch ) aList.get( i );
			aComplexSearch.setContinueButton();
		}
    }

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.Search#performSearch()
	 */
	public void performSearch() { }
}

/*
$Log: CompositeSearch.java,v $
Revision 1.3  2003/09/05 14:22:10  lemmster
working version

Revision 1.2  2003/09/04 12:17:01  lemmster
lots of changes

Revision 1.1  2003/09/03 22:15:27  lemmster
advanced search introduced; not working and far from complete. just to see the design

*/
