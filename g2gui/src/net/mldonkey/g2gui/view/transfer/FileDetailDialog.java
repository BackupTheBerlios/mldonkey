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
package net.mldonkey.g2gui.view.transfer;

import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.view.helper.CGridLayout;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;


/**
 *
 * FileDetailDialog
 *
 *
 * @version $Id: FileDetailDialog.java,v 1.3 2003/10/12 15:58:30 zet Exp $
 *
 */
public class FileDetailDialog implements Observer, DisposeListener {
    private Shell shell;
    private Display desktop = Display.getCurrent();
    private FileInfo fileInfo;
    private ArrayList chunkCanvases = new ArrayList();
    private Button fileActionButton;
    private Button fileCancelButton;
    private DecimalFormat df = new DecimalFormat( "0.0" );
    private CLabel clFileName;
    private CLabel clHash;
    private CLabel clSize;
    private CLabel clAge;
    private CLabel clSources;
    private CLabel clChunks;
    private CLabel clTransferred;
    private CLabel clRelativeAvail;
    private CLabel clLast;
    private CLabel clPriority;
    private CLabel clRate;
    private CLabel clETA;
    int leftColumn = 100;
    int rightColumn = leftColumn * 3;
    int width = leftColumn + rightColumn + 30;
    int height = 420;
    private List renameList;
    private Text renameText;

    public FileDetailDialog( final FileInfo fileInfo ) {
        this.fileInfo = fileInfo;
        createContents();
    }

    /**
     * Create the dialog contents
     */
    private void createContents() {
        shell = new Shell( SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL );

        shell.addDisposeListener( this );

        shell.setBounds( ( desktop.getBounds().width - width ) / 2, ( desktop.getBounds().height - height ) / 2, width, height );

        shell.setImage( G2GuiResources.getImage( "ProgramIcon" ) );
        shell.setText( G2GuiResources.getString( "TT_File" ) + " " + fileInfo.getId() + " " +
            G2GuiResources.getString( "TT_Details" ).toLowerCase() );

        shell.setLayout( CGridLayout.createGL( 1, 5, 5, 0, 5, false ) );
        shell.setLayoutData( new GridData( GridData.FILL_BOTH ) );

        createFileGeneralGroup( shell );

        createFileTransferGroup( shell );

        // MultiNet chunks or just chunks < proto 17
        createChunkGroup( shell, G2GuiResources.getString( "TT_DOWNLOAD_FD_CHUNKS_INFO" ), null );

        // Other network chunks
        if ( fileInfo.getAvails() != null ) {
            Iterator i = fileInfo.getAvails().keySet().iterator();

            while ( i.hasNext() ) {
                NetworkInfo networkInfo = (NetworkInfo) i.next();

                if ( networkInfo.isEnabled() ) {
                    createChunkGroup( shell, networkInfo.getNetworkName(), networkInfo );
                }
            }
        }

        createRenameGroup( shell );

        // Separator
        Label separator = new Label( shell, SWT.SEPARATOR | SWT.HORIZONTAL );
        separator.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

        createButtons( shell );

        updateLabels();
        fileInfo.addObserver( this );
        shell.pack();
        shell.open();
    }

    /**
     * Tell the core to rename the file
     */
    private void renameFile() {
        String newName = "";

        if ( !renameText.getText().equals( "" ) && !renameText.getText().equals( fileInfo.getName() ) ) {
            newName = renameText.getText();
        } else if ( ( renameList.getSelection().length > 0 ) && !renameList.getSelection()[ 0 ].equals( fileInfo.getName() ) ) {
            newName = renameList.getSelection()[ 0 ];
        }

        if ( !newName.equals( "" ) ) {
            fileInfo.setName( newName );
        }
    }

    /**
     * @param parent
     *
     * Create group relating to general file information
     */
    private void createFileGeneralGroup( Shell parent ) {
        Group fileGeneral = new Group( parent, SWT.SHADOW_ETCHED_OUT );

        fileGeneral.setText( G2GuiResources.getString( "TT_DOWNLOAD_FD_FILE_INFO" ) );
        fileGeneral.setLayout( CGridLayout.createGL( 4, 5, 0, 0, 0, false ) );
        fileGeneral.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

        clFileName = createLine( fileGeneral, G2GuiResources.getString( "TT_DOWNLOAD_FD_FILENAME" ), true );
        clHash = createLine( fileGeneral, G2GuiResources.getString( "TT_DOWNLOAD_FD_HASH" ), true );
        clSize = createLine( fileGeneral, G2GuiResources.getString( "TT_DOWNLOAD_FD_SIZE" ), false );
        clAge = createLine( fileGeneral, G2GuiResources.getString( "TT_DOWNLOAD_FD_AGE" ), false );
    }

    /**
     * @param parent
     *
     * Create group relating to the file transfer
     */
    private void createFileTransferGroup( Shell parent ) {
        Group fileTransfer = new Group( parent, SWT.SHADOW_ETCHED_OUT );

        fileTransfer.setText( G2GuiResources.getString( "TT_DOWNLOAD_FD_TRANSFER_INFO" ) );
        fileTransfer.setLayout( CGridLayout.createGL( 4, 5, 0, 0, 0, false ) );
        fileTransfer.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

        clSources = createLine( fileTransfer, G2GuiResources.getString( "TT_DOWNLOAD_FD_SOURCES" ), false );
        clChunks = createLine( fileTransfer, G2GuiResources.getString( "TT_DOWNLOAD_FD_CHUNKS" ), false );
        clTransferred = createLine( fileTransfer, G2GuiResources.getString( "TT_DOWNLOAD_FD_TRANSFERRED" ), false );
        clRelativeAvail = createLine( fileTransfer, G2GuiResources.getString( "TT_DOWNLOAD_FD_REL_AVAIL" ), false );
        clLast = createLine( fileTransfer, G2GuiResources.getString( "TT_DOWNLOAD_FD_LAST" ), false );
        clPriority = createLine( fileTransfer, G2GuiResources.getString( "TT_DOWNLOAD_FD_PRIORITY" ), false );
        clRate = createLine( fileTransfer, G2GuiResources.getString( "TT_DOWNLOAD_FD_RATE" ), false );
        clETA = createLine( fileTransfer, G2GuiResources.getString( "TT_DOWNLOAD_FD_ETA" ), false );
    }

    /**
     * @param parent
     * @param text
     * @param networkInfo
     *
     * Create chunk group (null networkInfo=MultiNet)
     *
     */
    private void createChunkGroup( Shell parent, String text, NetworkInfo networkInfo ) {
        Group chunkGroup = new Group( parent, SWT.SHADOW_ETCHED_OUT );

        int totalChunks = 0;

        if ( networkInfo == null ) {
            totalChunks = fileInfo.getAvail().length();
        } else {
            if ( fileInfo.getAvails().get( networkInfo ) instanceof String ) {
                totalChunks = ( (String) fileInfo.getAvails().get( networkInfo ) ).length();
            }
        }

        chunkGroup.setText( text + " (" + totalChunks + ")" );

        GridLayout gridLayout = CGridLayout.createGL( 1, 5, 2, 0, 0, false );
        chunkGroup.setLayout( gridLayout );

        chunkGroup.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

        ChunkCanvas chunkCanvas = new ChunkCanvas( chunkGroup, SWT.NO_BACKGROUND, null, fileInfo, networkInfo );
        fileInfo.addObserver( chunkCanvas );

        GridData canvasGD = new GridData( GridData.FILL_HORIZONTAL );
        canvasGD.heightHint = 18;
        chunkCanvas.setLayoutData( canvasGD );

        chunkCanvases.add( chunkCanvas );
    }

    /**
     * @param parent
     *
     * Create the rename group
     *
     */
    private void createRenameGroup( Shell parent ) {
        Group renameGroup = new Group( parent, SWT.SHADOW_ETCHED_OUT );

        renameGroup.setText( G2GuiResources.getString( "TT_DOWNLOAD_FD_ALTERNATIVE_FILENAMES" ) );
        renameGroup.setLayout( CGridLayout.createGL( 1, 1, 1, 0, 0, false ) );
        renameGroup.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

        Arrays.sort( fileInfo.getNames(), String.CASE_INSENSITIVE_ORDER );

        renameList = new List( renameGroup, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL );

        for ( int i = 0; i < fileInfo.getNames().length; i++ )
            renameList.add( fileInfo.getNames()[ i ] );

        GridData listGD = new GridData( GridData.FILL_HORIZONTAL );
        listGD.heightHint = 80;
        listGD.widthHint = 1;
        renameList.setLayoutData( listGD );
        renameList.addSelectionListener( new SelectionAdapter() {
                public void widgetSelected( SelectionEvent s ) {
                    String lItem = renameList.getSelection()[ 0 ];
                    renameText.setText( lItem );
                }
            } );

        Composite renameComposite = new Composite( parent, SWT.NONE );

        renameComposite.setLayout( CGridLayout.createGL( 2, 0, 0, 4, 0, false ) );
        renameComposite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

        renameText = new Text( renameComposite, SWT.BORDER );
        renameText.setText( fileInfo.getName() );
        renameText.setFont( PreferenceLoader.loadFont( "consoleFontData" ) );

        GridData data = new GridData( GridData.FILL_HORIZONTAL );
        data.widthHint = 1;
        renameText.setLayoutData( data );
        renameText.addKeyListener( new KeyAdapter() {
                public void keyPressed( KeyEvent e ) {
                    if ( e.character == SWT.CR ) {
                        renameFile();
                        renameText.setText( "" );
                    }
                }
            } );

        Button renameButton = new Button( renameComposite, SWT.NONE );
        renameButton.setText( G2GuiResources.getString( "TT_DOWNLOAD_FD_RENAME_BUTTON" ) );
        renameButton.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
        renameButton.addSelectionListener( new SelectionAdapter() {
                public void widgetSelected( SelectionEvent s ) {
                    renameFile();
                }
            } );
    }

    /**
     * @param parent
     * Create the dialog buttons
     */
    public void createButtons( Shell parent ) {
        Composite buttonComposite = new Composite( parent, SWT.NONE );
        buttonComposite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        buttonComposite.setLayout( CGridLayout.createGL( 3, 0, 0, 5, 0, false ) );

        if ( ( fileInfo.getState().getState() == EnumFileState.PAUSED ) || ( fileInfo.getState().getState() == EnumFileState.DOWNLOADING ) ||
                ( fileInfo.getState().getState() == EnumFileState.QUEUED ) ) {
            fileCancelButton = new Button( buttonComposite, SWT.NONE );
            fileCancelButton.setLayoutData( new GridData( GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END ) );
            fileCancelButton.setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_CANCEL" ) );
            fileCancelButton.addSelectionListener( new SelectionAdapter() {
                    public void widgetSelected( SelectionEvent s ) {
                        MessageBox reallyCancel = new MessageBox( fileCancelButton.getShell(), SWT.YES | SWT.NO | SWT.ICON_QUESTION );

                        reallyCancel.setMessage( G2GuiResources.getString( "TT_REALLY_CANCEL" ) );

                        if ( reallyCancel.open() == SWT.YES ) {
                            fileInfo.setState( EnumFileState.CANCELLED );
                            fileCancelButton.setEnabled( false );
                            fileActionButton.setEnabled( false );
                        }
                    }
                } );
        }

        fileActionButton = new Button( buttonComposite, SWT.NONE );

        GridData gridData = new GridData( GridData.HORIZONTAL_ALIGN_END );

        if ( fileInfo.getState().getState() == EnumFileState.DOWNLOADED ) {
            gridData.horizontalSpan = 2;
            gridData.grabExcessHorizontalSpace = true;
        }

        fileActionButton.setLayoutData( gridData );

        if ( ( fileInfo.getState().getState() == EnumFileState.PAUSED ) || ( fileInfo.getState().getState() == EnumFileState.QUEUED ) ) {
            fileActionButton.setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_RESUME" ) );
        } else if ( fileInfo.getState().getState() == EnumFileState.DOWNLOADING ) {
            fileActionButton.setText( "  " + G2GuiResources.getString( "TT_DOWNLOAD_MENU_PAUSE" ) + "  " );
        } else if ( fileInfo.getState().getState() == EnumFileState.DOWNLOADED ) {
            fileActionButton.setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_COMMIT" ) );
        }

        // until we have an unQueue function..
        if ( fileInfo.getState().getState() == EnumFileState.QUEUED ) {
            fileActionButton.setEnabled( false );
        }

        fileActionButton.addSelectionListener( new SelectionAdapter() {
                public void widgetSelected( SelectionEvent s ) {
                    if ( fileInfo.getState().getState() == EnumFileState.PAUSED ) {
                        fileInfo.setState( EnumFileState.DOWNLOADING );
                        fileActionButton.setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_PAUSE" ) );
                    } else if ( fileInfo.getState().getState() == EnumFileState.DOWNLOADING ) {
                        fileInfo.setState( EnumFileState.PAUSED );
                        fileActionButton.setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_RESUME" ) );
                    } else if ( fileInfo.getState().getState() == EnumFileState.DOWNLOADED ) {
                        if ( renameText.getText().equals( "" ) ) {
                            fileInfo.saveFileAs( fileInfo.getName() );
                        } else {
                            fileInfo.saveFileAs( renameText.getText() );
                        }

                        fileActionButton.setText( G2GuiResources.getString( "BTN_OK" ) );
                        fileActionButton.setEnabled( false );
                    }
                }
            } );

        Button closeButton = new Button( buttonComposite, SWT.NONE );
        closeButton.setFocus();
        closeButton.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
        closeButton.setText( G2GuiResources.getString( "BTN_CLOSE" ) );
        closeButton.addSelectionListener( new SelectionAdapter() {
                public void widgetSelected( SelectionEvent s ) {
                    shell.close();
                }
            } );
    }

    /**
     * @param composite
     * @param label
     * @param longlabel
     * @return CLabel
     *
     *        Create a Label/CLabel for information display
     */
    private CLabel createLine( Composite composite, String label, boolean longlabel ) {
        Label aLabel = new Label( composite, SWT.NONE );
        aLabel.setText( label );

        GridData lGD = new GridData();
        lGD.widthHint = leftColumn;
        aLabel.setLayoutData( lGD );

        CLabel aCLabel = new CLabel( composite, SWT.NONE );
        GridData clGD = new GridData();

        if ( longlabel ) {
            clGD.widthHint = rightColumn;
            clGD.horizontalSpan = 3;
        } else {
            clGD.widthHint = leftColumn;
        }

        aCLabel.setLayoutData( clGD );

        return aCLabel;
    }

    /**
     * Update the labels
     */
    public void updateLabels() {
        updateLabel( clFileName, fileInfo.getName() );
        updateLabel( clHash, fileInfo.getMd4().toUpperCase() );
        updateLabel( clSize, fileInfo.getStringSize() );
        updateLabel( clAge, fileInfo.getStringAge() );
        updateLabel( clSources, Integer.toString( fileInfo.getSources() ) );
        updateLabel( clChunks, Integer.toString( fileInfo.getNumChunks() ) + " / " + Integer.toString( fileInfo.getChunks().length() ) );
        updateLabel( clTransferred, fileInfo.getStringDownloaded() );
        updateLabel( clRelativeAvail, fileInfo.getRelativeAvail() + "%" );
        updateLabel( clLast, fileInfo.getStringOffset() );
        updateLabel( clPriority, fileInfo.getStringPriority() );

        if ( fileInfo.getState().getState() == EnumFileState.PAUSED ) {
            updateLabel( clRate, G2GuiResources.getString( "TT_Paused" ) );
        } else if ( fileInfo.getState().getState() == EnumFileState.QUEUED ) {
            updateLabel( clRate, G2GuiResources.getString( "TT_Queued" ) );
        } else {
            updateLabel( clRate, df.format( fileInfo.getRate() / 1000f ) + " KB/s" );
        }

        updateLabel( clETA, fileInfo.getStringETA() );
    }

    /**
     * @param cLabel
     * @param string
     *
     * Update a label
     */
    public void updateLabel( CLabel cLabel, String string ) {
        if ( !cLabel.isDisposed() ) {
            cLabel.setText( string );

            if ( string.length() > 10 ) {
                cLabel.setToolTipText( string );
            } else {
                cLabel.setToolTipText( "" );
            }
        }
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update( Observable o, Object arg ) {
        if ( o instanceof FileInfo && !shell.isDisposed() ) {
            shell.getDisplay().asyncExec( new Runnable() {
                    public void run() {
                        updateLabels();
                    }
                } );
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
     */
    public synchronized void widgetDisposed( DisposeEvent e ) {
        Iterator i = chunkCanvases.iterator();

        while ( i.hasNext() )
            ( (ChunkCanvas) i.next() ).dispose();

        fileInfo.deleteObserver( this );
    }
}


/*
$Log: FileDetailDialog.java,v $
Revision 1.3  2003/10/12 15:58:30  zet
rewrite downloads table & more..

Revision 1.2  2003/09/21 23:39:31  zet
displayTableColors preference

Revision 1.1  2003/09/20 14:39:21  zet
move transfer package

Revision 1.33  2003/09/18 14:11:01  zet
revert

Revision 1.31  2003/09/15 22:10:32  zet
add availability %, refresh delay option

Revision 1.30  2003/09/14 22:22:46  zet
use console font

Revision 1.29  2003/09/14 21:54:13  zet
fix rate

Revision 1.28  2003/09/14 16:23:56  zet
multi network avails

Revision 1.27  2003/09/12 15:08:06  zet
fix: [ Bug #897 ]

Revision 1.26  2003/09/08 19:48:00  lemmster
just repaired the log

Revision 1.25  2003/09/04 18:14:56  zet
cancel messagebox

Revision 1.24  2003/08/31 02:35:32  zet
setFocus

Revision 1.23  2003/08/31 02:21:33  zet
commit

Revision 1.22  2003/08/31 02:16:50  zet
cancel button

Revision 1.21  2003/08/31 01:54:25  zet
add spaces

Revision 1.20  2003/08/31 01:48:25  zet
*** empty log message ***

Revision 1.19  2003/08/31 01:46:33  zet
localise

Revision 1.17  2003/08/31 00:08:59  zet
add buttons

Revision 1.16  2003/08/28 22:44:30  zet
GridLayout helper class

Revision 1.15  2003/08/23 15:21:37  zet
remove @author

Revision 1.14  2003/08/23 15:13:00  zet
remove reference to static MainTab methods

Revision 1.13  2003/08/23 01:08:17  zet
*** empty log message ***

Revision 1.12  2003/08/23 01:00:02  zet
*** empty log message ***

Revision 1.11  2003/08/22 22:49:22  vaste
new todos (name + close button)

Revision 1.10  2003/08/22 21:22:58  lemmster

*/
