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

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.enum.EnumClientType;
import net.mldonkey.g2gui.view.helper.CGridLayout;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;


/**
 *
 * ClientDetailDialog
 *
 * @version $Id: ClientDetailDialog.java,v 1.2 2003/09/21 23:39:31 zet Exp $
 *
 */
public class ClientDetailDialog implements Observer, DisposeListener {
    private Shell shell;
    private CoreCommunication core;
    private Display desktop = Display.getDefault(  );
    private FileInfo fileInfo;
    private ClientInfo clientInfo;
    private CLabel clName;
    private CLabel clRating;
    private CLabel clActivity;
    private CLabel clKind;
    private CLabel clNetwork;
    private ArrayList chunkCanvases = new ArrayList(  );
    int leftColumn = 100;
    int rightColumn = leftColumn * 3;
    int width = leftColumn + rightColumn + 30;
    int height = 420;

    public ClientDetailDialog( FileInfo fileInfo, final ClientInfo clientInfo, final CoreCommunication core ) {
        this.fileInfo = fileInfo;
        this.clientInfo = clientInfo;
        this.core = core;
        createContents(  );
    }

    /**
     * Create dialog contents
     */
    public void createContents(  ) {
        shell = new Shell( SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL );
        shell.addDisposeListener( this );
        shell.setBounds( ( desktop.getBounds(  ).width - width ) / 2, ( desktop.getBounds(  ).height - height ) / 2, width, height );

        shell.setImage( G2GuiResources.getImage( "ProgramIcon" ) );

        shell.setText( G2GuiResources.getString( "TT_Client" ) + " " + clientInfo.getClientid(  ) + " " +
            G2GuiResources.getString( "TT_Details" ).toLowerCase(  ) );

        GridLayout gridLayout = CGridLayout.createGL( 1, 5, 5, 0, 5, false );

        shell.setLayout( gridLayout );
        shell.setLayoutData( new GridData( GridData.FILL_BOTH ) );

        createGeneralGroup( shell );

        createChunkGroup( shell, G2GuiResources.getString( "TT_DOWNLOAD_CD_LOCAL_CHUNKS" ), null );
        createChunkGroup( shell, G2GuiResources.getString( "TT_DOWNLOAD_CD_CLIENT_CHUNKS" ), clientInfo );

        createButtons( shell );

        updateLabels(  );
        fileInfo.addObserver( this );
        clientInfo.addObserver( this );
        shell.pack(  );
        shell.open(  );
    }

    /**
     * @param parent
     *
     * Create general client information
     */
    public void createGeneralGroup( Shell parent ) {
        Group clientGeneral = new Group( parent, SWT.SHADOW_ETCHED_OUT );
        clientGeneral.setText( G2GuiResources.getString( "TT_DOWNLOAD_CD_CLIENT_INFO" ) );

        clientGeneral.setLayout( CGridLayout.createGL( 4, 5, 2, 0, 0, false ) );

        clName = createLine( clientGeneral, G2GuiResources.getString( "TT_DOWNLOAD_CD_NAME" ), true );
        clNetwork = createLine( clientGeneral, G2GuiResources.getString( "TT_DOWNLOAD_CD_NETWORK" ), false );
        clRating = createLine( clientGeneral, G2GuiResources.getString( "TT_DOWNLOAD_CD_RATING" ), false );
        clActivity = createLine( clientGeneral, G2GuiResources.getString( "TT_DOWNLOAD_CD_ACTIVITY" ), false );
        clKind = createLine( clientGeneral, G2GuiResources.getString( "TT_DOWNLOAD_CD_KIND" ), false );

        clientGeneral.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
    }

    /**
     * @param shell
     * @param text
     * @param clientInfo
     *
     * Create chunk group (clientInfo=null to display fileInfo chunks)
     */
    public void createChunkGroup( Shell shell, String text, ClientInfo clientInfo ) {
        Group chunkGroup = new Group( shell, SWT.SHADOW_ETCHED_OUT );

        String totalChunks = "";

        // clientInfo.getFileAvail is not synched. TIntObjHash.. 
        if ( clientInfo == null ) {
            totalChunks = " (" + fileInfo.getAvail(  ).length(  ) + ")";
        }

        chunkGroup.setText( text + totalChunks );
        chunkGroup.setLayout( CGridLayout.createGL( 1, 5, 5, 0, 0, false ) );
        chunkGroup.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

        ChunkCanvas chunkCanvas = new ChunkCanvas( chunkGroup, SWT.NO_BACKGROUND, clientInfo, fileInfo, null );

        if ( clientInfo == null ) {
            fileInfo.addObserver( chunkCanvas );
        } else {
            clientInfo.addObserver( chunkCanvas );
        }

        chunkCanvases.add( chunkCanvas );

        GridData canvasGD = new GridData( GridData.FILL_HORIZONTAL );
        canvasGD.heightHint = 28;
        chunkCanvas.setLayoutData( canvasGD );
    }

    /**
     * @param parent
     *
     * Create dialog buttons
     *
     */
    private void createButtons( Shell parent ) {
        Composite buttonComposite = new Composite( parent, SWT.NONE );
        buttonComposite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        buttonComposite.setLayout( CGridLayout.createGL( 2, 0, 0, 5, 0, false ) );

        final Button addFriendButton = new Button( buttonComposite, SWT.NONE );
        addFriendButton.setLayoutData( new GridData( GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END ) );
        addFriendButton.setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_ADD_FRIEND" ) );

        if ( clientInfo.getClientType(  ) == EnumClientType.FRIEND ) {
            addFriendButton.setEnabled( false );
        }

        addFriendButton.addSelectionListener( new SelectionAdapter(  ) {
                public void widgetSelected( SelectionEvent s ) {
                    ClientInfo.addFriend( core, clientInfo.getClientid(  ) );
                    addFriendButton.setText( G2GuiResources.getString( "BTN_OK" ) );
                    addFriendButton.setEnabled( false );
                }
            } );

        Button closeButton = new Button( buttonComposite, SWT.NONE );
        closeButton.setFocus(  );
        closeButton.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
        closeButton.setText( G2GuiResources.getString( "BTN_CLOSE" ) );
        closeButton.addSelectionListener( new SelectionAdapter(  ) {
                public void widgetSelected( SelectionEvent s ) {
                    shell.close(  );
                }
            } );
    }

    /**
     * @param composite
     * @param label
     * @param longlabel
     * @return CLabel
     *
     * Create a Label/CLabel combination for information display
     *
     */
    private CLabel createLine( Composite composite, String label, boolean longlabel ) {
        Label aLabel = new Label( composite, SWT.NONE );
        aLabel.setText( label );

        GridData lGD = new GridData(  );
        lGD.widthHint = leftColumn;
        aLabel.setLayoutData( lGD );

        CLabel aCLabel = new CLabel( composite, SWT.NONE );
        GridData clGD = new GridData(  );

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
     * Update labels
     */
    public void updateLabels(  ) {
        updateLabel( clName, clientInfo.getClientName(  ) );
        updateLabel( clRating, "" + clientInfo.getClientRating(  ) );
        updateLabel( clActivity, clientInfo.getClientActivity(  ) );
        updateLabel( clKind, clientInfo.getClientConnection(  ) );
        updateLabel( clNetwork, clientInfo.getClientnetworkid(  ).getNetworkName(  ) );
    }

    /**
     * @param cLabel
     * @param string
     *
     * Update a label
     *
     */
    public void updateLabel( CLabel cLabel, String string ) {
        if ( !cLabel.isDisposed(  ) ) {
            cLabel.setText( string );

            if ( string.length(  ) > 10 ) {
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
        if ( o instanceof FileInfo && !shell.isDisposed(  ) ) {
            shell.getDisplay(  ).asyncExec( new Runnable(  ) {
                    public void run(  ) {
                        updateLabels(  );
                    }
                } );
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
     */
    public synchronized void widgetDisposed( DisposeEvent e ) {
        Iterator i = chunkCanvases.iterator(  );

        while ( i.hasNext(  ) )
            ( (ChunkCanvas) i.next(  ) ).dispose(  );

        clientInfo.deleteObserver( this );
        fileInfo.deleteObserver( this );
    }
}


/*
$Log: ClientDetailDialog.java,v $
Revision 1.2  2003/09/21 23:39:31  zet
displayTableColors preference

Revision 1.1  2003/09/20 14:39:21  zet
move transfer package

Revision 1.27  2003/09/18 14:11:01  zet
revert

Revision 1.25  2003/09/14 21:54:13  zet
fix rate

Revision 1.24  2003/09/14 16:23:56  zet
multi network avails

Revision 1.23  2003/09/14 03:37:43  zet
changedProperties

Revision 1.22  2003/09/13 22:26:44  zet
weak sets & !rawrate

Revision 1.21  2003/08/31 15:37:45  zet
check if already a friend

Revision 1.20  2003/08/31 02:35:32  zet
setFocus

Revision 1.19  2003/08/31 02:16:55  zet
*** empty log message ***

Revision 1.18  2003/08/31 01:48:25  zet
*** empty log message ***

Revision 1.17  2003/08/31 01:46:33  zet
localise

Revision 1.14  2003/08/31 00:08:59  zet
add buttons

Revision 1.13  2003/08/28 22:44:30  zet
GridLayout helper class

Revision 1.12  2003/08/23 15:21:37  zet
remove @author

Revision 1.11  2003/08/23 15:13:00  zet
remove reference to static MainTab methods

Revision 1.10  2003/08/23 01:00:10  zet
*** empty log message ***

Revision 1.9  2003/08/22 22:54:04  vaste
new todo (close button)

Revision 1.8  2003/08/22 21:22:58  lemmster

*/
