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
import java.util.Observer;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.SearchQuery;
import net.mldonkey.g2gui.view.SearchTab;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Search
 *
 *
 * @version $Id: Search.java,v 1.21 2003/09/04 22:04:07 lemmster Exp $
 *
 */
public abstract class Search implements Observer {
	protected CoreCommunication core;
    protected SearchTab tab;
    protected SearchQuery query;
	protected Combo combo;
	protected Text text;
	protected StackLayout stackLayout;
	protected Composite composite;
    private Button okButton;
    private Button stopButton;
    private Button continueButton;
    private Button[] buttons;

    /**
     *
     * @param core The parent corecommunication
     * @param tab The parent searchtab
     */
    public Search( CoreCommunication core, SearchTab tab ) {
        this.core = core;
        this.tab = tab;
        this.core.getNetworkInfoMap().addObserver( this );
    }

    /**
     * The string to display as the Tabname
     * @return The string name
     */
    public abstract String getTabName();

    /**
     * @param tabFolder The tabfolder to create the control in
     * @return a Control filled with the content of this obj
     */
    public abstract Control createTabFolderPage( CTabFolder tabFolder );

    /**
     * create a searchquery, fill it and send it to mldonkey
     */
    public abstract void performSearch();

    /**
     * DOCUMENT ME!
     */
    public void setSearchButton() {
        stackLayout.topControl = buttons[ 2 ];
        composite.layout();
    }

    /**
     * DOCUMENT ME!
     */
    public void setContinueButton() {
        stackLayout.topControl = buttons[ 1 ];
        composite.layout();
    }

    /**
     * DOCUMENT ME!
     */
    public void setStopButton() {
        stackLayout.topControl = buttons[ 0 ];
        composite.layout();
    }

    /**
     * DOCUMENT ME!
     *
     * @param group DOCUMENT ME!
     */
    protected Object[] createSearchButton( Composite group ) {
        stackLayout = new StackLayout();
        composite = new Composite( group, SWT.NONE );
        composite.setLayout( stackLayout );
        buttons = new Button[ 3 ];
        stopButton = new Button( composite, SWT.PUSH );
        stopButton.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        stopButton.setText( G2GuiResources.getString( "SS_STOP" ) );
        stopButton.addSelectionListener( new SelectionAdapter() {
                public void widgetSelected( SelectionEvent event ) {
                    tab.getSearchResult().stopSearch();
                    setContinueButton();
                }
            } );
        buttons[ 0 ] = stopButton;
        continueButton = new Button( composite, SWT.PUSH );
        continueButton.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        continueButton.setText( G2GuiResources.getString( "SS_CONTINUE" ) );
        continueButton.addSelectionListener( new SelectionAdapter() {
                public void widgetSelected( SelectionEvent event ) {
                    tab.getSearchResult().continueSearch();
                    setStopButton();
                }
            } );
        buttons[ 1 ] = continueButton;
        okButton = new Button( composite, SWT.PUSH );
        okButton.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        okButton.setText( G2GuiResources.getString( "SS_SEARCH" ) );
        okButton.addSelectionListener( new SelectionAdapter() {
                public void widgetSelected( SelectionEvent event ) {
                    performSearch();
                }
            } );
        buttons[ 2 ] = okButton;
        stackLayout.topControl = buttons[ 2 ];
        
        return new Object[] { stackLayout, composite };
    }

    /**
     * Creates a blank input field for search strings
     * @param group The Group to display the box in
     * @param aString The Box header
     */
    protected Text createInputBox( Composite group, String aString ) {
        /* the box label */
        GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
        gridData.horizontalSpan = 2;
        Label label = new Label( group, SWT.NONE );
        label.setLayoutData( gridData );
        label.setText( aString );

        /* the box */
        gridData = new GridData( GridData.FILL_HORIZONTAL );
        gridData.horizontalSpan = 2;
        Text aText = new Text( group, SWT.SINGLE | SWT.BORDER );
        aText.setLayoutData( gridData );
        aText.setFont( JFaceResources.getTextFont() );
        aText.addMouseListener( new MouseListener() {
            public void mouseDoubleClick( MouseEvent e ) { }
			public void mouseUp( MouseEvent e ) { }
            public void mouseDown( MouseEvent e ) {
                    setSearchButton();
            }
        } );
        aText.addKeyListener( new KeyAdapter() {
            public void keyPressed( KeyEvent e ) {
                if ( e.character == SWT.CR )
            		performSearch();
            }
        } );
        if ( core.getNetworkInfoMap().getEnabledAndSearchable() == 0 ) {
            aText.setText( "no searchable network enabled" );
            aText.setEnabled( false );
        }

		return aText;        
    }

    /**
     * Creates a DropDown Box with all activated Networks inside
     * (SWT.READ_ONLY)
     * @param group The Group to display the box in
     * @param aString The Box header
     */
    protected Combo createNetworkCombo( Composite group, String aString ) {
		/* the combo label */
        GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
        gridData.horizontalSpan = 2;
        Label label = new Label( group, SWT.NONE );
        label.setLayoutData( gridData );
        label.setText( aString );
        
        /* the combo itself */
        gridData = new GridData( GridData.FILL_HORIZONTAL );
        gridData.horizontalSpan = 2;
        Combo aCombo = new Combo( group, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
        aCombo.setLayoutData( gridData );
        
        /* fill the combo with values */
        fillCombo( aCombo );
        
        return aCombo;
    }

    /**
     * fill the combo box with the networks
     */
    private void fillCombo( Combo aCombo ) {
        /* get all activated networks and display them in the combo */
        NetworkInfo[] networks = core.getNetworkInfoMap().getNetworks();
        for ( int i = 0; i < networks.length; i++ ) {
            NetworkInfo network = networks[ i ];
            if ( network.isEnabled() && network.isSearchable() ) {
				aCombo.add( network.getNetworkName() );
				aCombo.setData( network.getNetworkName(), network );
            }
        }
        if ( aCombo.getItemCount() > 1 ) {
			aCombo.add( G2GuiResources.getString( "S_ALL" ) );
			aCombo.setData( G2GuiResources.getString( "S_ALL" ), null );
			aCombo.select( aCombo.indexOf( G2GuiResources.getString( "S_ALL" ) ) );
			aCombo.setEnabled( true );
        }
        else {
			aCombo.select( 0 );
			aCombo.setEnabled( false );
        }
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update( Observable o, Object arg ) {
        if ( combo.isDisposed() ) return;
        
        this.combo.getDisplay().asyncExec( new Runnable() {
            public void run() {
                /* update the combo */
                combo.removeAll();
                fillCombo( combo );
				
                /* update the text */
				if ( core.getNetworkInfoMap().getEnabledAndSearchable() == 0 ) {
					text.setText( "no searchable network enabled" );
					text.setEnabled( false );
				}
				else {
					text.setText( "" );
					text.setEnabled( true );
				}
            }
        } );
    }
}

/*
$Log: Search.java,v $
Revision 1.21  2003/09/04 22:04:07  lemmster
use always a new searchquery

Revision 1.20  2003/09/04 21:57:21  lemmster
still buggy, but enough for today

Revision 1.19  2003/09/04 12:17:01  lemmster
lots of changes

Revision 1.18  2003/09/03 22:15:27  lemmster
advanced search introduced; not working and far from complete. just to see the design

Revision 1.17  2003/09/01 11:09:43  lemmster
show downloading files

Revision 1.16  2003/08/31 12:32:04  lemmster
major changes to search

Revision 1.15  2003/08/29 19:09:25  dek
new look'n feel

Revision 1.14  2003/08/23 15:21:37  zet
remove @author

Revision 1.13  2003/08/22 23:35:40  zet
use JFace font registry

Revision 1.12  2003/08/22 14:33:06  vaste
temporarily changed font to monospaced as an example (ugly hack)

Revision 1.11  2003/08/20 10:04:41  lemmster
inputbox disabled when zero searchable networks are enabled

Revision 1.10  2003/08/19 15:03:43  lemmster
bugfix in update

Revision 1.9  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.8  2003/08/11 19:03:53  lemmstercvs01
update networkcombo when a networkinfo status changes

Revision 1.7  2003/07/30 19:32:02  lemmstercvs01
using hasServers() instead of direct compare

Revision 1.6  2003/07/28 08:19:20  lemmstercvs01
get NetworkInfo by Enum instead of NetworkName

Revision 1.5  2003/07/27 18:45:47  lemmstercvs01
lots of changes

Revision 1.4  2003/07/24 16:20:10  lemmstercvs01
lots of changes

Revision 1.3  2003/07/24 02:22:46  zet
doesn't crash if no core is running

Revision 1.2  2003/07/23 19:49:17  zet
press enter

Revision 1.1  2003/07/23 16:56:28  lemmstercvs01
initial commit

*/
