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
import java.util.StringTokenizer;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.view.SearchTab;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
 * @version $Id: ComplexSearch.java,v 1.20 2003/11/26 05:40:06 zet Exp $
 *
 */
public abstract class ComplexSearch extends Search implements Listener, MouseListener {
    protected Text maxText;
    protected Text minText;
    protected Combo maxCombo;
    protected Combo minCombo;
    protected Combo extensionCombo;
    protected Combo resultCombo;

    /**
     * Creates a new ComplexSearch obj
     * 
     * @param core The core obj with the <code>Information</code>
     * @param tab The <code>GuiTab</code> we draw this obj inside
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
    public Control createTabFolderPage( Composite tabFolder ) {
        return null;
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.search.Search#getTabName()
     */
    public String getTabName() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param combo DOCUMENT ME!
     */
    public void addQueryMinMax( Combo combo ) {
        String tokenOne = "";
        String tokenTwo = "MB";
        StringTokenizer st = new StringTokenizer( combo.getText() );
        tokenOne = st.nextToken();
        if ( st.hasMoreTokens() )
            tokenTwo = st.nextToken();
        if ( combo == minCombo )
            query.setMinSize( tokenOne, tokenTwo );
        else
            query.setMaxSize( tokenOne, tokenTwo );
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.search.Search#performSearch()
     */
    public void performSearch() {
        if ( PreferenceLoader.loadBoolean( "useCombo" ) ) {
            if ( !minCombo.getText().equals( "" ) )
                addQueryMinMax( minCombo );
            if ( !maxCombo.getText().equals( "" ) )
                addQueryMinMax( maxCombo );
        }
        else {
            if ( !maxText.getText().equals( "" ) )
                query.setMaxSize( maxText.getText(), maxCombo.getText() );
            if ( !minText.getText().equals( "" ) )
                query.setMinSize( minText.getText(), minCombo.getText() );
        }

        /* the max results */
        if ( !resultCombo.getText().equals( "" ) )
            query.setMaxSearchResults( new Integer( resultCombo.getText() ).intValue() );
		
		/* the extension combo */
        if ( !extensionCombo.getText().equals( "" ) )
            query.setFormat( extensionCombo.getText() );

        /* get the network id for this query */
        Object obj = 
        	networkCombo.getData( networkCombo.getText() );
        if ( obj != null ) { // if != All
            NetworkInfo temp = ( NetworkInfo ) obj;
            query.setNetwork( temp.getNetwork() );
        }

        /* now the query is ready to be send */
        query.send();

		/* set the button to stop */
        tab.setStopButton();
    }

    /**
     * DOCUMENT ME!
     *
     * @param group DOCUMENT ME!
     * @param items DOCUMENT ME!
     */
    protected void createExtensionCombo( Composite group, String[] items ) {
        /* the max result label */
        GridData gridData = new GridData( GridData.HORIZONTAL_ALIGN_FILL );
        Label label = new Label( group, SWT.NONE );
        label.setLayoutData( gridData );
        label.setText( G2GuiResources.getString( "CS_EXTENSION" ) );
        label.setToolTipText( G2GuiResources.getString( "CS_TOOLHELP" ) );

        /* the extension box */
        gridData = new GridData( GridData.FILL_HORIZONTAL );
        this.extensionCombo = new Combo( group, SWT.SINGLE | SWT.BORDER );
        this.extensionCombo.setLayoutData( gridData );
        this.extensionCombo.setItems( items );
        this.extensionCombo.select( 0 );
        this.extensionCombo.addMouseListener( this );
        this.extensionCombo.setToolTipText( G2GuiResources.getString( "CS_TOOLHELP" ) );
    }

    /**
     * DOCUMENT ME!
     *
     * @param group DOCUMENT ME!
     */
    protected void createResultCombo( Composite group ) {
        /* the extension label */
        GridData gridData = new GridData( GridData.HORIZONTAL_ALIGN_FILL );
        Label label = new Label( group, SWT.NONE );
        label.setLayoutData( gridData );
        label.setText( G2GuiResources.getString( "CS_MAXRESULTS" ) );
        label.setToolTipText( G2GuiResources.getString( "CS_TOOLHELP" ) );

        /* the max result box */
        gridData = new GridData( GridData.FILL_HORIZONTAL );
        String[] resultItems = { "", "50", "100", "200", "400" };
        this.resultCombo = new Combo( group, SWT.SINGLE | SWT.BORDER );
        this.resultCombo.setLayoutData( gridData );
        this.resultCombo.setItems( resultItems );
        this.resultCombo.select( 0 );
        this.resultCombo.setToolTipText( G2GuiResources.getString( "CS_TOOLHELP" ) );
        this.resultCombo.addMouseListener( this );

        /* add a keylistener to deny unvalid input */
        this.resultCombo.addKeyListener( new KeyListener() {
                public void keyReleased( KeyEvent e ) {
                }

                public void keyPressed( KeyEvent e ) {
                    Combo aCombo = ( Combo ) e.widget;
                    String aString = aCombo.getText();
                    try {
                        Integer.parseInt( aString );
                    }
                    catch ( NumberFormatException nE ) {
                        if ( aString.length() > 1 )
                            aCombo.setText( aString.substring( 0, aString.length() - 1 ) );
                        else
                            aCombo.setText( "" );
                    }
                }
            } );
    }

    /**
     * DOCUMENT ME!
     *
     * @param group DOCUMENT ME!
     */
    protected void createMaxMinSizeText( Composite group ) {
        /* the max size label */
        GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
        Label label = new Label( group, SWT.NONE );
        label.setLayoutData( gridData );
        label.setText( G2GuiResources.getString( "CS_MAXSIZE" ) );
        label.setToolTipText( G2GuiResources.getString( "CS_TOOLHELP" ) );

        /* the min size label */
        gridData = new GridData( GridData.FILL_HORIZONTAL );
        label = new Label( group, SWT.NONE );
        label.setLayoutData( gridData );
        label.setText( G2GuiResources.getString( "CS_MINSIZE" ) );
        label.setToolTipText( G2GuiResources.getString( "CS_TOOLHELP" ) );
        /* a new composite with numColumns = 4 */
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        gridLayout.makeColumnsEqualWidth = true;
        gridData = new GridData( GridData.FILL_HORIZONTAL );
        gridData.horizontalSpan = 2;
        Composite aComposite = new Composite( group, SWT.NONE );
        aComposite.setLayout( gridLayout );
        aComposite.setLayoutData( gridData );
        /* the items for the combos */
        String[] items = { "", "KB", "MB", "GB" };

        /* the max size text */
        gridData = new GridData( GridData.FILL_HORIZONTAL );
        this.maxText = new Text( aComposite, SWT.SINGLE | SWT.BORDER );
        this.maxText.setLayoutData( gridData );
        this.maxText.addListener( SWT.Verify, this );
        this.maxText.addMouseListener( this );
        this.maxText.setToolTipText( G2GuiResources.getString( "CS_TOOLHELP" ) );

        /* the max size combo */
        gridData = new GridData( GridData.FILL_HORIZONTAL );
        gridData.horizontalIndent = -5;
        this.maxCombo = new Combo( aComposite, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
        this.maxCombo.setLayoutData( gridData );
        this.maxCombo.setItems( items );
        this.maxCombo.select( 0 );
        this.maxCombo.addMouseListener( this );

        /* the min size text */
        gridData = new GridData( GridData.FILL_HORIZONTAL );
        this.minText = new Text( aComposite, SWT.SINGLE | SWT.BORDER );
        this.minText.setLayoutData( gridData );
        this.minText.addListener( SWT.Verify, this );
        this.minText.addMouseListener( this );
        this.minText.setToolTipText( G2GuiResources.getString( "CS_TOOLHELP" ) );

        /* the max size combo */
        gridData = new GridData( GridData.FILL_HORIZONTAL );
        gridData.horizontalIndent = -5;
        this.minCombo = new Combo( aComposite, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
        this.minCombo.setLayoutData( gridData );
        this.minCombo.setItems( items );
        this.minCombo.select( 0 );
        this.minCombo.addMouseListener( this );
    }

    /**
     * DOCUMENT ME!
     *
     * @param group DOCUMENT ME!
     */
    protected void createMinMaxSizeText( Composite group ) {
        createSizeText( group, G2GuiResources.getString( "CS_MINSIZE" ), 1 );
        createSizeText( group, G2GuiResources.getString( "CS_MAXSIZE" ), 2 );
    }

    /**
     * DOCUMENT ME!
     *
     * @param group DOCUMENT ME!
     * @param labelText DOCUMENT ME!
     * @param type DOCUMENT ME!
     */
    private void createSizeText( Composite group, String labelText, int type ) {
        /* the max size label */
        GridData gridData = new GridData( GridData.HORIZONTAL_ALIGN_FILL );
        Label label = new Label( group, SWT.NONE );
        label.setLayoutData( gridData );
        label.setText( labelText );
        label.setToolTipText( G2GuiResources.getString( "CS_TOOLHELP" ) );
        /* the items for the combos */
        String[] items = { "", "100 KB", "200 KB", "500 KB", "1 MB",
        				   "5 MB", "10 MB", "50 MB", "100 MB", "500 MB",
        				   "1 GB", "2 GB"
        };

        /* the max size combo */
        gridData = new GridData( GridData.FILL_HORIZONTAL );
        Combo aCombo;
        if ( type == 1 ) {
            minCombo = new Combo( group, SWT.SINGLE | SWT.BORDER );
            aCombo = minCombo;
        }
        else {
            maxCombo = new Combo( group, SWT.SINGLE | SWT.BORDER );
            aCombo = maxCombo;
        }
        aCombo.setLayoutData( gridData );
        aCombo.setItems( items );
        aCombo.select( 0 );
        aCombo.addMouseListener( this );
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void handleEvent( Event e ) {
        Text aText = ( Text ) e.widget;
        String aString = "";
        try {
            if ( aText.getText() != null )
                aString = aText.getText();
            Float.parseFloat( aString + e.text );
        }
        catch ( NumberFormatException nE ) {
            e.doit = false;
        }
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update( Observable o, Object arg ) {
    	if ( resultCombo == null || resultCombo.isDisposed() ) return;
       	resultCombo.getDisplay().asyncExec( new Runnable() {
                public void run() {
                	if ( resultCombo.isDisposed() ) return;
                    /* update the other text */
                    if ( core.getNetworkInfoMap().getEnabledAndSearchable() == 0 ) {
                        maxText.setEnabled( false );
                        minText.setEnabled( false );
                        resultCombo.setEnabled( false );
                        extensionCombo.setEnabled( false );
                    }
                    else {
                        maxText.setEnabled( true );
                        minText.setEnabled( true );
                        resultCombo.setEnabled( true );
                        extensionCombo.setEnabled( true );
                    }
                }
            } );
        super.update( o, arg );
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseDoubleClick( MouseEvent e ) { }
    /* (non-Javadoc)
     * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseUp( MouseEvent e ) { }
    /* (non-Javadoc)
     * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseDown( MouseEvent e ) {
        tab.setSearchButton();
    }
}

/*
$Log: ComplexSearch.java,v $
Revision 1.20  2003/11/26 05:40:06  zet
fix npe

Revision 1.19  2003/11/23 17:58:03  lemmster
removed dead/unused code

Revision 1.18  2003/09/26 14:35:54  lemmster
check for null

Revision 1.17  2003/09/24 05:53:00  lemmster
CTabFolder -> Composite

Revision 1.16  2003/09/21 09:57:01  lemmster
protected -> private

Revision 1.15  2003/09/19 15:19:14  lemmster
reworked

Revision 1.14  2003/09/18 10:39:21  lemmster
checkstyle

Revision 1.13  2003/09/11 12:37:35  lemmster
fixed crash with combo boxes [bug #886]

Revision 1.12  2003/09/10 14:54:37  zet
still a null (with useCombo)..

Revision 1.11  2003/09/10 14:47:43  zet
nullpointer

Revision 1.10  2003/09/08 10:25:26  lemmster
OtherComplexSearch added, rest improved

Revision 1.9  2003/09/07 19:50:06  zet
use min/max combo

Revision 1.8  2003/09/07 08:21:50  lemmster
resourcebundle added

Revision 1.7  2003/09/05 23:49:07  zet
1 line per search option

Revision 1.6  2003/09/05 16:50:19  lemmster
set search button correctly, verify input for result count

Revision 1.5  2003/09/05 14:22:10  lemmster
working version

Revision 1.4  2003/09/04 21:57:21  lemmster
still buggy, but enough for today

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
