/*
 * Copyright 2003
 * G2GUI Team
 *
 *
 * This file is part of G2GUI.
 *
 * G2GUI is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * ( at your option ) any later version.
 *
 * G2GUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2GUI; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package net.mldonkey.g2gui.view;

import gnu.trove.TIntObjectIterator;

import java.text.DecimalFormat;
import java.util.Observable;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.FileInfoIntMap;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.view.helper.CCLabel;
import net.mldonkey.g2gui.view.helper.CGridLayout;
import net.mldonkey.g2gui.view.helper.HeaderBarMenuListener;
import net.mldonkey.g2gui.view.helper.MaximizeSashMouseAdapter;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.transfer.CustomTableViewer;
import net.mldonkey.g2gui.view.transfer.DownloadPaneMenuListener;
import net.mldonkey.g2gui.view.transfer.clientTable.ClientTableViewer;
import net.mldonkey.g2gui.view.transfer.downloadTable.DownloadTableTreeContentProvider;
import net.mldonkey.g2gui.view.transfer.downloadTable.DownloadTableTreeViewer;
import net.mldonkey.g2gui.view.transfer.uploadTable.UploadTableViewer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * TransferTab.java
 *
 * @version $Id: TransferTab.java,v 1.66 2003/09/27 12:30:40 dek Exp $
 *
 */
public class TransferTab extends GuiTab {
    private static DecimalFormat decimalFormat = new DecimalFormat( "0.0" );
    private CLabel downloadCLabel;
    private CoreCommunication mldonkey;
    private DownloadTableTreeViewer downloadTableTreeViewer = null;
    private CustomTableViewer clientTableViewer = null;
    private Composite downloadComposite;
    private MenuManager popupMenuDL, popupMenuUL;
	private UploadTableViewer uploadTableViewer;

    /**
     * @param gui where this tab belongs to
     */
    public TransferTab( MainTab gui ) {
        super( gui );
        this.mldonkey = gui.getCore();
        createButton( "TransfersButton", G2GuiResources.getString( "TT_TransfersButton" ),
                      G2GuiResources.getString( "TT_TransfersButtonToolTip" ) );
        createContents( this.subContent );
    }

    /* ( non-Javadoc )
     * @see net.mldonkey.g2gui.view.G2guiTab#createContents( org.eclipse.swt.widgets.Composite )
     */
    protected void createContents( Composite parent ) {
        SashForm mainSashForm = new SashForm( parent, SWT.VERTICAL );
        Control downloadParent = mainSashForm;
        if ( PreferenceLoader.loadBoolean( "advancedMode" ) ) {
            downloadParent = new SashForm( mainSashForm, SWT.HORIZONTAL );
        }
        ViewForm downloadViewForm =
            new ViewForm( (SashForm) downloadParent,
                          SWT.BORDER
                          | ( PreferenceLoader.loadBoolean( "flatInterface" ) ? SWT.FLAT : SWT.NONE ) );
                          
        if ( PreferenceLoader.loadBoolean( "advancedMode" ) ) {
            createClientViewForm( (SashForm) downloadParent );
        } else {
        	downloadParent = downloadViewForm;
        }
        createDownloadHeader( downloadViewForm, mainSashForm, downloadParent );
        downloadComposite = new Composite( downloadViewForm, SWT.NONE );
        downloadComposite.setLayout( new FillLayout() );
        downloadViewForm.setContent( downloadComposite );
        createUploads( mainSashForm );
        downloadTableTreeViewer =
            new DownloadTableTreeViewer( downloadComposite, clientTableViewer, mldonkey, this );
        popupMenuDL.addMenuListener( new DownloadPaneMenuListener( downloadTableTreeViewer.getTableTreeViewer(),
                                                                 mldonkey ) );
        mainSashForm.setWeights( new int[] { 1, 1 } );
        mainSashForm.setMaximizedControl( downloadParent );
        mldonkey.getFileInfoIntMap().addObserver( this );
    }

    /**
     * Create the download pane header with popup menu
     * 
     * @param parentViewForm 
     */
    public void createDownloadHeader( ViewForm parentViewForm, final SashForm mainSashForm, final Control downloadParent ) {
        popupMenuDL = new MenuManager( "" );
        popupMenuDL.setRemoveAllWhenShown( true );
        downloadCLabel = CCLabel.createCL( parentViewForm, "TT_Downloads", "TransfersButtonSmallTitlebar" );
        downloadCLabel.addMouseListener( new MaximizeSashMouseAdapter( downloadCLabel, popupMenuDL, mainSashForm, downloadParent  ) );
        parentViewForm.setTopLeft( downloadCLabel );
    }

    /**
     * Create the uploads window
     *
     * @param mainSashForm 
     */
    public void createUploads( final SashForm mainSashForm ) {
        ViewForm uploadsViewForm =
            new ViewForm( mainSashForm,
                          SWT.BORDER
                          | ( PreferenceLoader.loadBoolean( "flatInterface" ) ? SWT.FLAT : SWT.NONE ) );
        uploadsViewForm.setLayoutData( new GridData( GridData.FILL_BOTH ) );
       
       	createUploadHeader( uploadsViewForm, mainSashForm, uploadsViewForm);
        Composite uploadersComposite = new Composite( uploadsViewForm, SWT.NONE );
        uploadersComposite.setLayout( new FillLayout() );
        uploadTableViewer = new UploadTableViewer(uploadersComposite,mldonkey,this);
        uploadsViewForm.setContent( uploadersComposite );
    }
	public void createUploadHeader( ViewForm parentViewForm, final SashForm mainSashForm, final Control uploadParent ) {
		popupMenuUL = new MenuManager( "" );
		popupMenuUL.setRemoveAllWhenShown( true );
		popupMenuUL.addMenuListener( new UploadsMenuListener(mainSashForm, parentViewForm) );
		CLabel uploadsCLabel = CCLabel.createCL( parentViewForm, "TT_Uploads", "TransfersButtonSmallTitlebar" );
		uploadsCLabel.addMouseListener( new MaximizeSashMouseAdapter( uploadsCLabel, popupMenuUL, mainSashForm, uploadParent  ) );
		parentViewForm.setTopLeft( uploadsCLabel );
	}
    
    
    

    /**
     * Create the hidden client view form
     *
     * @param parentSash 
     */
    public void createClientViewForm( SashForm parentSash ) {
        ViewForm clientViewForm =
            new ViewForm( parentSash,
                          SWT.BORDER
                          | ( PreferenceLoader.loadBoolean( "flatInterface" ) ? SWT.FLAT : SWT.NONE ) );
        CLabel clientCLabel =
            CCLabel.createCL( clientViewForm, "TT_Clients", "TransfersButtonSmallTitlebar" );
        Composite downloadClients = new Composite( clientViewForm, SWT.NONE );
        downloadClients.setLayout( CGridLayout.createGL( 1, 0, 0, 0, 0, false ) );
        clientViewForm.setContent( downloadClients );
        clientViewForm.setTopLeft( clientCLabel );
        downloadClients.addControlListener( new ControlAdapter() {
                public void controlResized( ControlEvent e ) {
                    Composite c = ( Composite ) e.widget;
                    int width = c.getBounds().width;
                    if ( ( width > 0 ) && ( downloadTableTreeViewer != null ) )
                        downloadTableTreeViewer.updateClientsTable( true );
                }
            } );
        createClientTableViewer( downloadClients, parentSash );
        parentSash.setWeights( new int[] { 100, 0 } );
    }

    /**
     * Create the hidden client table viewer
     *
     * @param parent 
     * @param parentSash 
     */
    public void createClientTableViewer( Composite parent, final SashForm parentSash ) {
     
     	ClientTableViewer cTV = new ClientTableViewer( parent, mldonkey );
     	clientTableViewer = cTV.getTableViewer();
        
        Composite bottomBar = new Composite( parent, SWT.NONE );
        GridLayout gridLayout = CGridLayout.createGL( 1, 0, 0, 0, 0, false );
        bottomBar.setLayout( gridLayout );
        bottomBar.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        Label separator1 = new Label( bottomBar, SWT.SEPARATOR | SWT.HORIZONTAL );
        separator1.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        Button hideButton = new Button( bottomBar, SWT.NONE );
        hideButton.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        hideButton.setText( ">>>" );
        hideButton.addSelectionListener( new SelectionAdapter() {
                public void widgetSelected( SelectionEvent s ) {
                    parentSash.setWeights( new int[] { 10, 0 } );
                    if ( downloadTableTreeViewer != null )
                        downloadTableTreeViewer.updateClientsTable( false );
                }
        } );
    }

    /**
     * Update the label, in the gui thread
     * 
     * @param text 
     */
    public void runLabelUpdate( final String text ) {
        if ( !downloadCLabel.isDisposed() )
            downloadCLabel.getDisplay().asyncExec( new Runnable() {
                    public void run() {
                        if ( !downloadCLabel.isDisposed() )
                            downloadCLabel.setText(
                            	G2GuiResources.getString( "TT_Downloads" ) + ": " + text );
                    }
                } );

    }

    /* ( non-Javadoc )
     * @see java.util.Observer#update( java.util.Observable, java.lang.Object )
     */
    public void update( Observable o, Object arg ) {
        float totalRate = 0;
        int totalFiles = 0;
        int totalQueued = 0;
        int totalDownloaded = 0;
        if ( o instanceof FileInfoIntMap ) {
            synchronized ( o ) {
                FileInfoIntMap files = ( FileInfoIntMap ) o;
                TIntObjectIterator it = files.iterator();
                while ( it.hasNext() ) {
                    it.advance();
                    FileInfo fileInfo = ( FileInfo ) it.value();
                    totalRate += fileInfo.getRate();
                    if ( DownloadTableTreeContentProvider.isInteresting( fileInfo ) )
                        totalFiles++;
                    if ( fileInfo.getState().getState() == EnumFileState.QUEUED )
                        totalQueued++;
                    else if ( fileInfo.getState().getState() == EnumFileState.DOWNLOADED )
                        totalDownloaded++;
                }
            }
        }
        String extra = "";
		if ( totalQueued > 0 || totalDownloaded > 0 ) {
			extra = " (";
			if ( totalQueued > 0 )
				extra += G2GuiResources.getString( "TT_Queued" ).toLowerCase() + ": " + totalQueued;
			if ( totalQueued > 0 && totalDownloaded > 0 )
				extra += ", ";
			if ( totalDownloaded > 0 )
				extra += G2GuiResources.getString( "TT_Downloaded" ).toLowerCase() + ": " + totalDownloaded;
			extra += ")";
		}
		runLabelUpdate( totalFiles + " " + G2GuiResources.getString( "TT_Files_At" )
						+ " " + decimalFormat.format( totalRate / 1000f ) + " KB/s" + extra );

    }

    /* ( non-Javadoc )
     * @see net.mldonkey.g2gui.view.GuiTab#updateDisplay()
     */
    public void updateDisplay() {
        downloadTableTreeViewer.updateDisplay();
        uploadTableViewer.updateDisplay();
        if ( clientTableViewer != null )
            clientTableViewer.getTable().setLinesVisible(
            	PreferenceLoader.loadBoolean( "displayGridLines" ) );
        super.updateDisplay();
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.GuiTab#dispose()
     */
    public void dispose() {
        super.dispose();
        popupMenuDL.dispose();
        popupMenuUL.dispose();
    }
    
	/**
     * UploadsMenuListener
     */
    public class UploadsMenuListener extends HeaderBarMenuListener {
    	
    	public UploadsMenuListener(SashForm sashForm, Control control) {
    		super(sashForm, control);
    	}

		public void menuAboutToShow( IMenuManager menuManager ) {
			menuManager.add( new RefreshUploadsAction( ) );
			super.menuAboutToShow(menuManager);
		}
	}
	/**
     * RefreshUploadsAction
     */
    public class RefreshUploadsAction extends Action {
	   public RefreshUploadsAction( ) {
		   super( "Refresh" );
	   }

	   public void run(  ) {
			Message refresh = new EncodeMessage( Message.S_REFRESH_UPLOAD_STATS );
			refresh.sendMessage( mldonkey );
	   }
   }
    
}

/*
$Log: TransferTab.java,v $
Revision 1.66  2003/09/27 12:30:40  dek
upload-Table has now same show-Gridlines-behaviour as download-Table

Revision 1.65  2003/09/27 00:26:41  zet
put menu back

Revision 1.63  2003/09/26 17:19:55  zet
add refresh menuitem

Revision 1.62  2003/09/26 16:08:02  zet
dblclick header to maximize/restore

Revision 1.61  2003/09/26 15:45:59  dek
we now have upload-stats (well, kind of...)

Revision 1.60  2003/09/20 14:39:48  zet
move transfer package

Revision 1.59  2003/09/20 01:23:18  zet
*** empty log message ***

Revision 1.58  2003/09/18 13:01:23  lemmster
checkstyle

Revision 1.57  2003/09/16 02:12:17  zet
headerbar menu

Revision 1.56  2003/09/15 01:25:06  zet
move menu

Revision 1.55  2003/09/13 22:25:01  zet
rate, !rawrate

Revision 1.54  2003/09/04 20:40:49  vnc
removed ugly "@" sign, added "files at" text instead

Revision 1.53  2003/08/31 20:32:50  zet
active button states

Revision 1.52  2003/08/31 01:46:33  zet
localise

Revision 1.51  2003/08/30 13:55:10  zet
*** empty log message ***

Revision 1.50  2003/08/30 11:44:29  dek
client-Table is now own ViewForm, and some checkstyle work

Revision 1.49  2003/08/30 00:44:01  zet
move tabletree menu

Revision 1.48  2003/08/29 22:11:47  zet
add CCLabel helper class

Revision 1.47  2003/08/29 21:21:46  zet
sash_width

Revision 1.46  2003/08/29 19:30:50  zet
font colour

Revision 1.45  2003/08/29 18:28:42  zet
remove import Shell

Revision 1.44  2003/08/29 18:24:24  zet
localise

Revision 1.43  2003/08/29 17:45:35  zet
localise text

Revision 1.42  2003/08/29 17:33:20  zet
remove headerbar

Revision 1.41  2003/08/29 16:06:54  zet
optional shadow

Revision 1.40  2003/08/29 15:43:43  zet
try gradient headerbar

Revision 1.39  2003/08/28 22:44:30  zet
GridLayout helper class

Revision 1.38  2003/08/25 22:17:12  zet
*** empty log message ***

Revision 1.37  2003/08/24 16:37:04  zet
combine the preference stores

Revision 1.36  2003/08/24 02:34:16  zet
update sorter properly

Revision 1.35  2003/08/23 15:21:37  zet
remove @author

Revision 1.34  2003/08/23 14:58:38  lemmster
cleanup of MainTab, transferTree.* broken

Revision 1.33  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.32  2003/08/22 21:06:48  lemmster
replace $user$ with $Author: dek $

Revision 1.31  2003/08/21 10:12:10  dek
removed empty expression

Revision 1.30  2003/08/20 22:18:56  zet
Viewer updates

Revision 1.29  2003/08/20 18:35:16  zet
uploaders label

Revision 1.28  2003/08/20 14:58:43  zet
sources clientinfo viewer

Revision 1.27  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.26  2003/08/15 22:05:58  zet
*** empty log message ***

Revision 1.25  2003/08/11 00:30:10  zet
show queued files

Revision 1.24  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.23  2003/08/06 17:10:50  zet
check for shell.isdisposed

Revision 1.22  2003/08/04 19:22:08  zet
trial tabletreeviewer

Revision 1.21  2003/07/27 22:39:36  zet
small buttons toggle ( in popup ) for main cool menu

Revision 1.20  2003/07/24 02:22:46  zet
doesn't crash if no core is running

Revision 1.19  2003/07/23 04:08:07  zet
looks better with icons

Revision 1.18  2003/07/18 09:58:00  dek
checkstyle

Revision 1.17  2003/07/17 14:58:37  lemmstercvs01
refactored

Revision 1.3  2003/07/15 18:14:47  dek
Wow, nice piece of work already done, it works, looks nice, but still lots of things to do

Revision 1.2  2003/07/14 19:26:40  dek
done some clean.up work, since it seems, as if this view becomes reality..

Revision 1.1  2003/07/11 17:53:51  dek
Tree for Downloads - still unstable

*/
