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
import org.eclipse.swt.events.SelectionListener;
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
 * @version $Id: Search.java,v 1.27 2003/09/18 10:39:21 lemmster Exp $
 *
 */
public abstract class Search implements Observer {
	protected CoreCommunication core;
    protected SearchTab tab;
    protected SearchQuery query;
	protected Combo networkCombo;
	protected Text inputText;
	protected StackLayout stackLayout;
	protected Composite composite;
	protected Button[] buttons;
	protected String selectedMedia;

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
		this.stackLayout.topControl = this.buttons[ 2 ];
        this.composite.layout();
    }

    /**
     * DOCUMENT ME!
     */
    public void setContinueButton() {
		this.stackLayout.topControl = this.buttons[ 1 ];
        this.composite.layout();
    }

    /**
     * DOCUMENT ME!
     */
    public void setStopButton() {
		this.stackLayout.topControl = this.buttons[ 0 ];
        this.composite.layout();
    }

	/**
	 * DOCUMENT ME!
	 */
	public void setDownloadButton() {
		this.stackLayout.topControl = this.buttons[ 3 ];
		this.composite.layout();
	}

    /**
     * DOCUMENT ME!
     *
     * @param group DOCUMENT ME!
     */
    protected void createSearchButton( Composite group ) {
        this.stackLayout = new StackLayout();
        this.composite = new Composite( group, SWT.NONE );
		GridData gridData = new GridData( GridData.FILL_HORIZONTAL ) ;
		gridData.horizontalSpan = 2;
        composite.setLayoutData( gridData );
		this.composite.setLayout( this.stackLayout );
        this.buttons = new Button[ 4 ];
        Button stopButton = new Button( this.composite, SWT.PUSH );
        stopButton.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        stopButton.setText( G2GuiResources.getString( "SS_STOP" ) );
        stopButton.addSelectionListener( new SelectionAdapter() {
                public void widgetSelected( SelectionEvent event ) {
                    tab.getSearchResult().stopSearch();
                    setContinueButton();
                }
            } );
        buttons[ 0 ] = stopButton;
		Button continueButton = new Button( this.composite, SWT.PUSH );
        continueButton.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        continueButton.setText( G2GuiResources.getString( "SS_CONTINUE" ) );
        continueButton.addSelectionListener( new SelectionAdapter() {
                public void widgetSelected( SelectionEvent event ) {
                    tab.getSearchResult().continueSearch();
                    setStopButton();
                }
            } );
        buttons[ 1 ] = continueButton;
		Button okButton = new Button( this.composite, SWT.PUSH );
		okButton.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        okButton.setText( G2GuiResources.getString( "SS_SEARCH" ) );
        okButton.addSelectionListener( new SelectionAdapter() {
                public void widgetSelected( SelectionEvent event ) {
                    performSearch();
                }
            } );
        buttons[ 2 ] = okButton;
		Button downloadButton = new Button( this.composite, SWT.PUSH );
		downloadButton.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		downloadButton.setText( "Download" );
		downloadButton.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent event ) {
					tab.getSearchResult().getMenuListener().downloadSelected();
				}
			} );
		buttons[ 3 ] = downloadButton;
        
        
		this.stackLayout.topControl = buttons[ 2 ];
	}

    /**
     * Creates a blank input field for search strings
     * @param group The Group to display the box in
     * @param aString The Box header
     */
    protected Text createInputBox( Composite group, String aString ) {
        /* the box label */
        GridData gridData = new GridData( GridData.HORIZONTAL_ALIGN_FILL );
        Label label = new Label( group, SWT.NONE );
        label.setLayoutData( gridData );
        label.setText( aString );

        /* the box */
        gridData = new GridData( GridData.FILL_HORIZONTAL );
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
            aText.setText( G2GuiResources.getString( "S_UNAVAILABLE" ) );
            aText.setEnabled( false );
        }
		return aText;        
    }
    
    protected void createMediaControl( Composite group, String title, int style ) {
		if ( style == 0 ) {
		String[] items = { 
					G2GuiResources.getString( "SS_ALL" ), 
					G2GuiResources.getString( "SS_AUDIO" ),
					G2GuiResources.getString( "SS_VIDEO" ),
					G2GuiResources.getString( "SS_IMAGE" ),
					G2GuiResources.getString( "SS_SOFTWARE" ) 
		};
			
		Label fileTypeLabel = new Label( group, SWT.NONE );
		fileTypeLabel.setText( title );
		fileTypeLabel.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_FILL ) );
		
		final Combo fileTypeCombo = new Combo( group, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
		fileTypeCombo.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		fileTypeCombo.setItems( items );
		fileTypeCombo.select( 0 );
		fileTypeCombo.addSelectionListener( new SelectionListener() {
				public void widgetDefaultSelected( SelectionEvent e ) { }
				public void widgetSelected( SelectionEvent e ) {
					switch ( fileTypeCombo.getSelectionIndex() ) {
						case 1: selectedMedia = "Audio"; 
							break;
						case 2: selectedMedia = "Video";
							break;
						case 3: selectedMedia = "Image";
							break;
						case 4: selectedMedia = "Software";
							break;
						default: selectedMedia = null;
							break;
					}
				}
			} );	
		} 
		else {
			/* media select */
			GridData gridData = new GridData();
			gridData.horizontalSpan = 2;
			Button all = new Button( group, SWT.RADIO );
			all.setLayoutData( gridData );
			all.setText( G2GuiResources.getString( "SS_ALL" ) );
			/* we want a default selection */
			all.setSelection( true );
			all.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent event ) {
					selectedMedia = null;
				}	
			} );

			gridData = new GridData();
			gridData.horizontalSpan = 2;
			Button audio = new Button( group, SWT.RADIO );
			audio.setLayoutData( gridData );
			audio.setText( G2GuiResources.getString( "SS_AUDIO" ) );
			audio.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent event ) {
					selectedMedia = "Audio";
				}	
			} );
			
			gridData = new GridData();
			gridData.horizontalSpan = 2;
			Button video = new Button( group, SWT.RADIO );
			video.setLayoutData( gridData );
			video.setText( G2GuiResources.getString( "SS_VIDEO" ) );
			video.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent event ) {
					selectedMedia = "Video";
				}	
			} );
			
			gridData = new GridData();
			gridData.horizontalSpan = 2;
			Button image = new Button( group, SWT.RADIO );
			image.setLayoutData( gridData );
			image.setText( G2GuiResources.getString( "SS_IMAGE" ) );
			image.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent event ) {
					selectedMedia = "Image";
				}	
			} );
			
			gridData = new GridData();
			gridData.horizontalSpan = 2;
			Button software = new Button( group, SWT.RADIO );
			software.setLayoutData( gridData );
			software.setText( G2GuiResources.getString( "SS_SOFTWARE" ) );
			software.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent event ) {
					selectedMedia = "Software";
				}	
			} );
		}
    }

    /**
     * Creates a DropDown Box with all activated Networks inside
     * (SWT.READ_ONLY)
     * @param group The Group to display the box in
     * @param aString The Box header
     */
    protected void createNetworkCombo( Composite group, String aString ) {
		/* the combo label */
        GridData gridData = new GridData( GridData.HORIZONTAL_ALIGN_FILL );
        Label label = new Label( group, SWT.NONE );
        label.setLayoutData( gridData );
        label.setText( aString + ":" );
        
        /* the combo itself */
        gridData = new GridData( GridData.FILL_HORIZONTAL );
        this.networkCombo = new Combo( group, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY );
		this.networkCombo.setLayoutData( gridData );
        
        /* fill the combo with values */
        fillNetworkCombo( this.networkCombo );
    }

    /**
     * fill the combo box with the networks
     */
    private void fillNetworkCombo( Combo aCombo ) {
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
        if ( this.networkCombo.isDisposed() ) return;
        
        this.networkCombo.getDisplay().asyncExec( new Runnable() {
            public void run() {
                /* update the combo */
				networkCombo.removeAll();
                fillNetworkCombo( networkCombo );
				
                /* update the text */
				if ( core.getNetworkInfoMap().getEnabledAndSearchable() == 0 ) {
					inputText.setText( G2GuiResources.getString( "S_UNAVAILABLE" ) );
					inputText.setEnabled( false );
				}
				else {
					inputText.setText( "" );
					inputText.setEnabled( true );
				}
            }
        } );
    }
}

/*
$Log: Search.java,v $
Revision 1.27  2003/09/18 10:39:21  lemmster
checkstyle

Revision 1.26  2003/09/08 11:54:22  lemmster
added download button

Revision 1.25  2003/09/08 10:25:26  lemmster
OtherComplexSearch added, rest improved

Revision 1.24  2003/09/07 08:21:50  lemmster
resourcebundle added

Revision 1.23  2003/09/05 23:49:07  zet
1 line per search option

Revision 1.22  2003/09/05 14:22:10  lemmster
working version

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
