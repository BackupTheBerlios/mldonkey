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
package net.mldonkey.g2gui.view.transferTree;

import java.util.ArrayList;
import java.util.Iterator;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.model.enum.EnumPriority;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 *
 * DownloadTableTreeMenuListener
 *
 * @version $Id: DownloadTableTreeMenuListener.java,v 1.32 2003/09/18 13:04:36 lemmster Exp $
 *
 */
public class DownloadTableTreeMenuListener implements ISelectionChangedListener, IMenuListener {
    private FileInfo lastSelectedFile;
    private FileInfo selectedFile;
    private TreeClientInfo selectedClient;
    private ArrayList selectedClients = new ArrayList();
    private ArrayList selectedFiles = new ArrayList();
    private TableTreeViewer tableTreeViewer;
    private TableViewer clientTableViewer;
    private DownloadTableTreeContentProvider tableTreeContentProvider;
    private CoreCommunication mldonkey;
    private boolean createClientTable = false;

    /**
     * DOCUMENT ME!
     *
     * @param tableTreeViewer DOCUMENT ME!
     * @param clientTableViewer DOCUMENT ME!
     * @param mldonkey DOCUMENT ME!
     */
    public DownloadTableTreeMenuListener( TableTreeViewer tableTreeViewer, TableViewer clientTableViewer,
                                          CoreCommunication mldonkey ) {
        this.tableTreeViewer = tableTreeViewer;
        this.clientTableViewer = clientTableViewer;
        this.mldonkey = mldonkey;
        tableTreeContentProvider =
            ( DownloadTableTreeContentProvider ) tableTreeViewer.getContentProvider();

        /*this is to delete the selection, if one clicks in an empty row of the table*/
        tableTreeViewer.getTableTree().getTable().addMouseListener( new MouseListener() {
                public void mouseDoubleClick( MouseEvent e ) {
                }

                public void mouseDown( MouseEvent e ) {
                    Table table = ( Table ) e.widget;
                    TableItem item = table.getItem( new Point( e.x, e.y ) );
                    if ( item == null ) {
                        table.setSelection( new int[ 0 ] );
                        selectedFiles.clear();
                        selectedClients.clear();
                        selectedClient = null;
                        selectedFile = null;
                    }
                }

                public void mouseUp( MouseEvent e ) {
                }
            } );
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void selectionChanged( SelectionChangedEvent e ) {
        IStructuredSelection sSel = ( IStructuredSelection ) e.getSelection();
        Object o = sSel.getFirstElement();
        if ( o instanceof FileInfo ) {
            FileInfo fileInfo = ( FileInfo ) o;
            selectedFile = fileInfo;
            if ( createClientTable
                     && ( ( lastSelectedFile == null ) || ( lastSelectedFile != selectedFile ) ) )
                clientTableViewer.setInput( fileInfo );
            lastSelectedFile = selectedFile;
        }
        else
            selectedFile = null;
        if ( o instanceof TreeClientInfo )
            selectedClient = ( TreeClientInfo ) o;
        else
            selectedClient = null;
        selectedClients.clear();
        selectedFiles.clear();
        for ( Iterator it = sSel.iterator(); it.hasNext();) {
            o = it.next();
            if ( o instanceof FileInfo )
                selectedFiles.add( ( FileInfo ) o );
            else if ( o instanceof TreeClientInfo )
                selectedClients.add( ( TreeClientInfo ) o );
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void updateClientsTable( boolean b ) {
        if ( b ) {
            if ( createClientTable != b )
                clientTableViewer.setInput( lastSelectedFile );
        }
        else
            clientTableViewer.setInput( null );
        createClientTable = b;
    }

    /* ( non-Javadoc )
     * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow( org.eclipse.jface.action.IMenuManager )
     */
    public void menuAboutToShow( IMenuManager menuManager ) {
        if ( ( selectedFile != null ) && selectedFileListContains( EnumFileState.DOWNLOADED ) )
            menuManager.add( new CommitAction() );
        if ( ( selectedFile != null )
                 && ( selectedFile.getState().getState() == EnumFileState.DOWNLOADED ) ) {
            MenuManager commitAsSubMenu =
                new MenuManager( G2GuiResources.getString( "TT_DOWNLOAD_MENU_COMMIT_AS" ) );
            commitAsSubMenu.add( new CommitAction( true ) );
            for ( int i = 0; i < selectedFile.getNames().length; i++ )
                commitAsSubMenu.add( new CommitAction( selectedFile.getNames()[ i ] ) );
            menuManager.add( commitAsSubMenu );
        }
        if ( selectedFile != null )
            menuManager.add( new FileDetailAction() );
        if ( ( selectedFile != null ) && selectedFileListContains( EnumFileState.DOWNLOADING ) )
            menuManager.add( new PauseAction() );
        if ( ( selectedFile != null ) && selectedFileListContains( EnumFileState.PAUSED ) )
            menuManager.add( new ResumeAction() );
        if ( ( selectedFile != null ) && selectedFileListContainsOtherThan( EnumFileState.DOWNLOADED ) )
            menuManager.add( new CancelAction() );
        if ( ( selectedFile != null ) && selectedFileListContainsOtherThan( EnumFileState.DOWNLOADED ) ) {
            MenuManager prioritySubMenu =
                new MenuManager( G2GuiResources.getString( "TT_DOWNLOAD_MENU_PRIORITY" ) );
            prioritySubMenu.add( new PriorityAction( EnumPriority.HIGH ) );
            prioritySubMenu.add( new PriorityAction( EnumPriority.NORMAL ) );
            prioritySubMenu.add( new PriorityAction( EnumPriority.LOW ) );
            menuManager.add( prioritySubMenu );
        }
        if ( ( selectedFile != null ) && PreferenceLoader.loadBoolean( "advancedMode" ) )
            menuManager.add( new VerifyChunksAction() );
        if ( ( selectedClient != null ) && PreferenceLoader.loadBoolean( "advancedMode" ) )
            menuManager.add( new AddFriendAction() );
        if ( selectedClient != null )
            menuManager.add( new ClientDetailAction() );
        if ( selectedFile != null ) {
            menuManager.add( new LinkToClipboardAction( false ) );
            menuManager.add( new LinkToClipboardAction( true ) );
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean selectedFileListContains( EnumFileState e ) {
        for ( int i = 0; i < selectedFiles.size(); i++ )
            if ( ( ( FileInfo ) selectedFiles.get( i ) ).getState().getState() == e )
                return true;
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean selectedFileListContainsOtherThan( EnumFileState e ) {
        for ( int i = 0; i < selectedFiles.size(); i++ )
            if ( ( ( FileInfo ) selectedFiles.get( i ) ).getState().getState() != e )
                return true;
        return false;
    }

    /**
     * VerifyChunksAction
     */
    private class VerifyChunksAction extends Action {
        public VerifyChunksAction() {
            super();
            setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_VERIFY_CHUNKS" ) );
        }

        /**
         * DOCUMENT ME!
         */
        public void run() {
            for ( int i = 0; i < selectedFiles.size(); i++ )
                ( ( FileInfo ) selectedFiles.get( i ) ).verifyChunks();
        }
    }

    private class FileDetailAction extends Action {
        public FileDetailAction() {
            super();
            setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_FILE_DETAILS" ) );
        }

        /**
         * DOCUMENT ME!
         */
        public void run() {
            new FileDetailDialog( selectedFile );
        }
    }

    private class AddFriendAction extends Action {
        public AddFriendAction() {
            super();
            setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_ADD_FRIEND" ) );
        }

        /**
         * DOCUMENT ME!
         */
        public void run() {
            for ( int i = 0; i < selectedClients.size(); i++ ) {
                TreeClientInfo selectedClientInfo = ( TreeClientInfo ) selectedClients.get( i );
                ClientInfo.addFriend( mldonkey, selectedClientInfo.getClientInfo().getClientid() );
            }
        }
    }

    private class ClientDetailAction extends Action {
        public ClientDetailAction() {
            super();
            setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_CLIENT_DETAILS" ) );
        }

        /**
         * DOCUMENT ME!
         */
        public void run() {
            new ClientDetailDialog( selectedClient.getFileInfo(), selectedClient.getClientInfo(), mldonkey );
        }
    }

    private class PauseAction extends Action {
        public PauseAction() {
            super();
            setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_PAUSE" ) );
        }

        /**
         * DOCUMENT ME!
         */
        public void run() {
            for ( int i = 0; i < selectedFiles.size(); i++ ) {
                FileInfo fileInfo = ( FileInfo ) selectedFiles.get( i );
                if ( fileInfo.getState().getState() == EnumFileState.DOWNLOADING )
                    fileInfo.setState( EnumFileState.PAUSED );
            }
        }
    }

    private class CommitAction extends Action {
        private String commitAs;
        private boolean manualInput = false;

        public CommitAction() {
            super( G2GuiResources.getString( "TT_DOWNLOAD_MENU_COMMIT_SELECTED" ) );
        }

        public CommitAction( String commitAs ) {
            super( commitAs );
            this.commitAs = commitAs;
        }

        public CommitAction( boolean b ) {
            super( G2GuiResources.getString( "TT_DOWNLOAD_MENU_COMMIT_INPUT" ) );
            manualInput = b;
        }

        /**
         * DOCUMENT ME!
         */
        public void run() {
            if ( ( commitAs == null ) && !manualInput ) {
                for ( int i = 0; i < selectedFiles.size(); i++ ) {
                    FileInfo selectedFileInfo = ( FileInfo ) selectedFiles.get( i );
                    if ( selectedFileInfo.getState().getState() == EnumFileState.DOWNLOADED )
                        selectedFileInfo.saveFileAs( selectedFileInfo.getName() );
                }
            }
            else {
                if ( manualInput ) {
                    InputDialog inputDialog =
                        new InputDialog( tableTreeViewer.getTableTree().getShell(),
                                         G2GuiResources.getString( "TT_DOWNLOAD_MENU_COMMIT_AS" ),
                                         G2GuiResources.getString( "TT_DOWNLOAD_MENU_COMMIT_AS" ),
                                         selectedFile.getName(), null );
                    if ( inputDialog.open() == InputDialog.OK ) {
                        String newFileName = inputDialog.getValue();
                        if ( !newFileName.equals( "" ) )
                            selectedFile.saveFileAs( newFileName );
                    }
                }
                else
                    selectedFile.saveFileAs( commitAs );
            }
        }
    }

    private class ResumeAction extends Action {
        public ResumeAction() {
            super();
            setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_RESUME" ) );
        }

        /**
         * DOCUMENT ME!
         */
        public void run() {
            for ( int i = 0; i < selectedFiles.size(); i++ ) {
                FileInfo fileInfo = ( FileInfo ) selectedFiles.get( i );
                if ( fileInfo.getState().getState() == EnumFileState.PAUSED )
                    fileInfo.setState( EnumFileState.DOWNLOADING );
            }
        }
    }

    private class CancelAction extends Action {
        public CancelAction() {
            super();
            setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_CANCEL" ) );
        }

        /**
         * DOCUMENT ME!
         */
        public void run() {
            MessageBox reallyCancel =
                new MessageBox( tableTreeViewer.getTableTree().getShell(),
                                SWT.YES | SWT.NO | SWT.ICON_QUESTION );
            reallyCancel.setMessage( G2GuiResources.getString( "TT_REALLY_CANCEL" ) + " ("
                                     + selectedFiles.size() + ")" );
            int answer = reallyCancel.open();
            if ( answer == SWT.YES ) {
                for ( int i = 0; i < selectedFiles.size(); i++ ) {
                    FileInfo fileInfo = ( FileInfo ) selectedFiles.get( i );
                    if ( fileInfo.getState().getState() != EnumFileState.DOWNLOADED )
                        fileInfo.setState( EnumFileState.CANCELLED );
                    mldonkey.getResultInfoIntMap().setDownloading( fileInfo, false );
                }
            }
        }
    }

    private class PriorityAction extends Action {
        private EnumPriority enumPriority;

        public PriorityAction( EnumPriority e ) {
            super( "", Action.AS_CHECK_BOX );
            enumPriority = e;
            if ( e == EnumPriority.HIGH )
                setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_PRIORITY_HIGH" ) );
            else if ( e == EnumPriority.NORMAL )
                setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_PRIORITY_NORMAL" ) );
            else if ( e == EnumPriority.LOW )
                setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_PRIORITY_LOW" ) );
        }

        /**
         * DOCUMENT ME!
         */
        public void run() {
            for ( int i = 0; i < selectedFiles.size(); i++ ) {
                FileInfo fileInfo = ( FileInfo ) selectedFiles.get( i );
                if ( fileInfo.getState().getState() != EnumFileState.DOWNLOADED )
                    fileInfo.setPriority( enumPriority );
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean isChecked() {
            return ( selectedFile.getPriority() == enumPriority );
        }
    }

    private class LinkToClipboardAction extends Action {
        private boolean useHTML = false;

        public LinkToClipboardAction( boolean useHTML ) {
            super();
            this.useHTML = useHTML;
            setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_LINKTO" ) + ( useHTML ? " ( html )" : "" ) );
        }

        /**
         * DOCUMENT ME!
         */
        public void run() {
            Clipboard clipBoard = new Clipboard( tableTreeViewer.getTableTree().getDisplay() );
            String link = "";
            for ( int i = 0; i < selectedFiles.size(); i++ ) {
                FileInfo aFileInfo = ( FileInfo ) selectedFiles.get( i );
				if ( link.length() > 0 ) 
					link += ( SWT.getPlatform().equals( "win32" ) ? "\r\n" : "\n" );
			 	
				link += ( useHTML ? "<a href=\"" : "" ) 
					+ "ed2k://|file|"
					+ aFileInfo.getName()
					+ "|"
					+ aFileInfo.getSize()
					+ "|"
					+ aFileInfo.getMd4()
					+ "|/" 
					+ ( useHTML ? "\">" + aFileInfo.getName() + "</a>" : "" );
            }
            clipBoard.setContents( new Object[] { link }, new Transfer[] { TextTransfer.getInstance() } );
            clipBoard.dispose();
        }
    }
}

/*
$Log: DownloadTableTreeMenuListener.java,v $
Revision 1.32  2003/09/18 13:04:36  lemmster
checkstyle

Revision 1.31  2003/09/18 12:43:29  lemmster
checkstyle

Revision 1.30  2003/09/16 16:58:03  zet
commit as

Revision 1.29  2003/09/15 15:32:09  lemmster
reset state of canceled downloads from search [bug #908]

Revision 1.28  2003/09/14 03:37:43  zet
changedProperties

Revision 1.27  2003/08/31 00:08:59  zet
add buttons

Revision 1.26  2003/08/30 00:44:01  zet
move tabletree menu

Revision 1.25  2003/08/29 23:33:57  zet
*** empty log message ***

Revision 1.24  2003/08/24 18:22:37  zet
*** empty log message ***

Revision 1.23  2003/08/24 18:19:02  zet
still fixing...

Revision 1.22  2003/08/24 16:37:04  zet
combine the preference stores

Revision 1.21  2003/08/24 16:16:26  zet
more fixing....

Revision 1.20  2003/08/24 16:11:24  zet
do not superclass this if you are going to break it...

Revision 1.19  2003/08/23 22:35:55  zet
combine priority actions

Revision 1.18  2003/08/23 20:33:01  zet
multi select actions

Revision 1.17  2003/08/23 15:21:37  zet
remove @author

Revision 1.16  2003/08/23 15:13:00  zet
remove reference to static MainTab methods

Revision 1.15  2003/08/23 09:46:18  lemmster
superclass TableMenuListener added

Revision 1.14  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.13  2003/08/22 21:16:36  lemmster
replace $user$ with $Author: lemmster $

Revision 1.12  2003/08/22 14:30:45  lemmster
verify chunks added

Revision 1.11  2003/08/21 00:59:57  zet
doubleclick expand

Revision 1.10  2003/08/20 22:18:56  zet
Viewer updates

Revision 1.9  2003/08/20 14:58:43  zet
sources clientinfo viewer

Revision 1.8  2003/08/19 12:14:16  lemmster
first try of simple/advanced mode

Revision 1.7  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.6  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging

Revision 1.5  2003/08/11 00:30:10  zet
show queued files

Revision 1.4  2003/08/08 20:51:11  zet
localise strings

Revision 1.3  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.2  2003/08/06 17:14:50  zet
file details

Revision 1.1  2003/08/04 19:22:08  zet
trial tabletreeviewer


*/
