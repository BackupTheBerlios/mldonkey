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
package net.mldonkey.g2gui.view.server;

import gnu.regexp.RE;
import gnu.regexp.REException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.RegExp;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.ServerInfo;
import net.mldonkey.g2gui.model.ServerInfoIntMap;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.helper.TableMenuListener;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
/**
 * ServerTableMenuListener
 *
 *
 * @version $Id: ServerTableMenuListener.java,v 1.17 2003/10/31 16:30:49 zet Exp $
 *
 */
public class ServerTableMenuListener extends TableMenuListener { 
    private ServerInfo selectedServer;
    private List selectedServers;
    private ServerInfoIntMap serverInfoMap;
    private CoreCommunication core;

    /**
     * Creates a new TableMenuListener
     * @param tableViewer The TableViewer
     * @param core The CoreCommunication supporting this with data
     */
    public ServerTableMenuListener( ServerTableView sTableViewer) {
        super( sTableViewer);
        
    }    
    
    public void initialize() {
        super.initialize();
        this.core = gView.getCore();
        this.serverInfoMap = this.core.getServerInfoIntMap();
        this.selectedServers = new ArrayList();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#
     * selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    public void selectionChanged( SelectionChangedEvent event ) {
        IStructuredSelection sSel = ( IStructuredSelection ) event.getSelection();
        Object o = sSel.getFirstElement();
        if ( o instanceof ServerInfo )
            selectedServer = ( ServerInfo ) o;
        else
            selectedServer = null;
        selectedServers.clear();
        for ( Iterator it = sSel.iterator(); it.hasNext(); ) {
            o = it.next();
            if ( o instanceof ServerInfo )
                selectedServers.add( ( ServerInfo ) o );
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
     */
    public void menuAboutToShow( IMenuManager menuManager ) {
        /* disconnect */
        if ( ( selectedServer != null ) && selectedServer.isConnected() )
            menuManager.add( new DisconnectAction() );
        /* connect */
        if ( ( selectedServer != null )
                 && ( selectedServer.getConnectionState().getState() == EnumState.NOT_CONNECTED ) )
            menuManager.add( new ConnectAction() );
        /* connect more (with the network) */
        if ( selectedServer != null )
            menuManager.add( new ConnectMoreAction() );
            
        /* copy server name to clipboard */
        menuManager.add( new CopyServerLink() );
            
        /* add server/servers */
        MenuManager addManager = new MenuManager( G2GuiResources.getString( "TML_ADD_SERVER_BY" ) );
        addManager.add( new AddServerAction() );
        addManager.add( new AddServersAction() );
        menuManager.add( addManager );
        /* remove server */
        MenuManager removeManager = new MenuManager( G2GuiResources.getString( "TML_REMOVE_SERVER_BY" ) );
        removeManager.add( new RemoveServerAction() );
        removeManager.add( new RemoveServersAction() );
        menuManager.add( removeManager );
        /* blacklist server */
        if ( selectedServer != null )
            menuManager.add( new BlackListAction() );

        /* add to favorites */
        /*                if ( selectedServer != null && this.core.getProtoToUse() > 16 )
        not yet                menuManager.add( new FavoritesAction() );
        */
        super.menuAboutToShow( menuManager );
    }


    private class DisconnectAction extends Action {
        public DisconnectAction() {
            super( G2GuiResources.getString( "TML_DISCONNECT" ) );
            setImageDescriptor( G2GuiResources.getImageDescriptor( "cancel" ));
        }

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.action.IAction#run()
		 */
        public void run() {
            for ( int i = 0; i < selectedServers.size(); i++ ) {
                ServerInfo server = ( ServerInfo ) selectedServers.get( i );
                serverInfoMap.disconnect( server.getServerId() );
            }
        }
    }
    
    private class CopyServerLink extends Action {
    	public CopyServerLink() {
    		super( G2GuiResources.getString( "TML_COPYTO" ) );
    		setImageDescriptor( G2GuiResources.getImageDescriptor( "copy" ));
    	}
    	
    	/*
    	 * (non-Javadoc)
    	 * @see org.eclipse.jface.action.IAction#run()
    	 */
    	public void run() {
			Clipboard clipboard =
				new Clipboard( ( ( TableViewer ) tableViewer ).getTable().getDisplay() );
			String aString = "";
			for ( int i = 0; i < selectedServers.size(); i++ ) {
				ServerInfo server = ( ServerInfo ) selectedServers.get( i );
				if ( aString.length() > 0 )
					aString += ( SWT.getPlatform().equals( "win32" ) ? "\r\n" : "\n" );
				aString += server.getLink();
			}
			// System.out.println("str" + aString);
			clipboard.setContents( new Object[] { aString },
								new Transfer[] { TextTransfer.getInstance() } );
			clipboard.dispose();						
    	}
    }

    private class ConnectAction extends Action {
        public ConnectAction() {
            super( G2GuiResources.getString( "TML_CONNECT" ) );
        }

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.action.IAction#run()
		 */
        public void run() {
            for ( int i = 0; i < selectedServers.size(); i++ ) {
                ServerInfo server = ( ServerInfo ) selectedServers.get( i );
                serverInfoMap.connect( server.getServerId() );
            }
        }
    }

    private class ConnectMoreAction extends Action {
        public ConnectMoreAction() {
            super( G2GuiResources.getString( "TML_CONNECT_MORE" ) );
			setImageDescriptor( G2GuiResources.getImageDescriptor( "plus" ));
        }

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.action.IAction#run()
		 */
        public void run() {
            for ( int i = 0; i < selectedServers.size(); i++ ) {
                ServerInfo server = ( ServerInfo ) selectedServers.get( i );
                serverInfoMap.connectMore( server.getNetwork() );
            }
        }
    }

    private class AddServerAction extends Action {
        private MyInputDialog dialog;

        public AddServerAction() {
            super( G2GuiResources.getString( "TML_ADD_SERVER" ) );
			setImageDescriptor( G2GuiResources.getImageDescriptor( "plus" ));
        }

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.action.IAction#run()
		 */
        public void run() {
            dialog =
                new MyInputDialog( ( ( TableViewer ) tableViewer ).getTable().getShell(),
                                   G2GuiResources.getString( "TML_ADD_SERVER" ),
                                   G2GuiResources.getString( "TML_HOSTNAME_PORT" ),
                                   G2GuiResources.getString( "TML_NETWORK" ),
                                   G2GuiResources.getString( "TML_FOOBAR2" ), new MyInputValidator() );
            dialog.open();
            if ( dialog.getReturnCode() == IDialogConstants.OK_ID ) {
                String text = dialog.getValue();
                String[] strings = RegExp.split( dialog.getValue(), ':' );
                InetAddress inetAddress = null;
                try {
                    inetAddress = InetAddress.getByName( strings[ 0 ] );
                }
                catch ( UnknownHostException e ) {
                    MessageBox box =
                        new MessageBox( ( ( TableViewer ) tableViewer ).getTable().getShell(),
                                        SWT.ICON_WARNING | SWT.OK );
                    box.setText( G2GuiResources.getString( "TML_LOOKUP_ERROR" ) );
                    box.setMessage( G2GuiResources.getString( "TML_CANNOT_RESOLVE" ) );
                    box.open();
                }
                core.getServerInfoIntMap().add( dialog.getCombo(), inetAddress,
                                                  new Short( strings[ 1 ] ).shortValue() );
            }
        }

        private class MyInputValidator implements IInputValidator {
            /* (non-Javadoc)
             * @see org.eclipse.jface.dialogs.IInputValidator#isValid(java.lang.String)
             */
            public String isValid( String newText ) {
                RE regex = null;
                try {
                    regex = new RE( "([a-z0-9-.]*):[0-9]{1,5}" );
                }
                catch ( REException e ) {
                    e.printStackTrace();
                }
                if ( regex.isMatch( newText ) )
                    return null;
                else
                    return G2GuiResources.getString( "TML_INVALID_INPUT" );
            }
        }
    }

    private class AddServersAction extends Action {
        private InputDialog dialog;

        public AddServersAction() {
            super( G2GuiResources.getString( "TML_ADD_SERVERS" ) );
			setImageDescriptor( G2GuiResources.getImageDescriptor( "plus" ));
        }

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.action.IAction#run()
		 */
        public void run() {
            dialog =
                new InputDialog( ( ( TableViewer ) tableViewer ).getTable().getShell(),
                                 G2GuiResources.getString( "TML_ADD_SERVERS" ),
                                 G2GuiResources.getString( "TML_LINK_TO_LIST" ),
                                 G2GuiResources.getString( "TML_FOOBAR" ), new MyInputValidator() );
            dialog.open();
            String result = dialog.getValue();
            if ( result != null )
                serverInfoMap.addServerList( result );
        }

        private class MyInputValidator implements IInputValidator {
            /* (non-Javadoc)
             * @see org.eclipse.jface.dialogs.IInputValidator#isValid(java.lang.String)
             */
            public String isValid( String newText ) {
                RE regex = null;
                try {
                    regex = new RE( "(http://|https://)([a-z0-9-./?=!+]*)" );
                }
                catch ( REException e ) {
                    e.printStackTrace();
                }
                if ( regex.isMatch( newText ) )
                    return null;
                else
                    return G2GuiResources.getString( "TML_INVALID_INPUT" );
            }
        }
    }

    private class RemoveServerAction extends Action {
        public RemoveServerAction() {
            super( G2GuiResources.getString( "TML_REMOVE_SERVER" ) );
			setImageDescriptor( G2GuiResources.getImageDescriptor( "minus" ));
        }

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.action.IAction#run()
		 */
        public void run() {
            for ( int i = 0; i < selectedServers.size(); i++ ) {
                ServerInfo server = ( ServerInfo ) selectedServers.get( i );
                serverInfoMap.remove( server.getServerId() );
            }
        }
    }

    private class RemoveServersAction extends Action {
        public RemoveServersAction() {
            super( G2GuiResources.getString( "TML_REMOVE_SERVERS" ) );
			setImageDescriptor( G2GuiResources.getImageDescriptor( "minus" ));
        }

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.action.IAction#run()
		 */
        public void run() {
            serverInfoMap.cleanOld();
        }
    }

    private class BlackListAction extends Action {
        public BlackListAction() {
            super();
            setText( G2GuiResources.getString( "TML_ADD_TO_BLACKLIST" ) );
        }

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.action.IAction#run()
		 */
        public void run() {
            for ( int i = 0; i < selectedServers.size(); i++ ) {
                ServerInfo server = ( ServerInfo ) selectedServers.get( i );
                serverInfoMap.addToBlackList( server.getServerId() );
            }
        }
    }

    private class RefreshAction extends Action {
        public RefreshAction() {
            super();
            setText( "Manual Refresh (for developing)" );
        }

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.action.IAction#run()
		 */
        public void run() {
            ( ( TableViewer ) tableViewer ).getTable().getDisplay().asyncExec( new Runnable() {
                    public void run() {
                        tableViewer.refresh();
                    }
                } );
        }
    }

    private class FavoritesAction extends Action {
        public FavoritesAction() {
            super();
            setText( G2GuiResources.getString( "TML_FAVORITES" ) );
        }

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.jface.action.IAction#run()
		 */
        public void run() {
            for ( int i = 0; i < selectedServers.size(); i++ ) {
                ServerInfo server = ( ServerInfo ) selectedServers.get( i );
                server.setFavorites();
            }
        }
    }

    private class MyInputDialog extends InputDialog {
        private Combo combo;
        private Composite composite;
        private String comboMessage;
        private NetworkInfo networkInfo;

        /**
         * Creates an input dialog with OK and Cancel buttons.
         * Note that the dialog will have no visual representation (no widgets)
         * until it is told to open.
         * <p>
         * Note that the <code>open</code> method blocks for input dialogs.
         * </p>
         *
         * @param parentShell the parent shell
         * @param dialogTitle the dialog title, or <code>null</code> if none
         * @param dialogMessage the dialog message, or <code>null</code> if none
         * @param comboMessage the combo message, or <code>null</code> if none
         * @param initialValue the initial input value, or <code>null</code> if none
         *  (equivalent to the empty string)
         * @param validator an input validator, or <code>null</code> if none
         */
        public MyInputDialog( Shell parentShell, String dialogTitle, String dialogMessage,
                              String comboMessage, String initialValue, IInputValidator validator ) {
            super( parentShell, dialogTitle, dialogMessage, initialValue, validator );
            this.comboMessage = comboMessage;
        }

        /**
         * DOCUMENT ME!
         *
         * @param parent DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        protected Control createDialogArea( Composite parent ) {
            composite = ( Composite ) super.createDialogArea( parent );
            /* create the combo message */
            if ( comboMessage != null ) {
                Label label = new Label( composite, SWT.WRAP );
                label.setText( comboMessage );
                GridData data =
                    new GridData( GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL
                                  | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER );
                data.widthHint = convertHorizontalDLUsToPixels( IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH );
                label.setLayoutData( data );
                label.setFont( parent.getFont() );
            }

            /* now our combo for the networks */
            combo = new Combo( composite, SWT.NONE );
            combo.setLayoutData( new GridData( GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL ) );
            NetworkInfo[] networks = core.getNetworkInfoMap().getNetworks();
            for ( int i = 0; i < networks.length; i++ ) {
                NetworkInfo network = networks[ i ];
                if ( network.isEnabled() && network.hasServers() ) {
                    combo.add( network.getNetworkName() );
                    combo.setData( network.getNetworkName(), network );
                }
            }
            combo.select( 0 );
            return composite;
        }

        /* (non-Javadoc)
         * Method declared on Dialog.
         */
        protected void buttonPressed( int buttonId ) {
            if ( buttonId == IDialogConstants.OK_ID )
                this.networkInfo =
                    ( NetworkInfo ) combo.getData( combo.getItem( combo.getSelectionIndex() ) );
            else
                this.networkInfo = null;
            super.buttonPressed( buttonId );
        }

        /**
         * @return The networkinfo to the selected combo item
         */
        public NetworkInfo getCombo() {
            return this.networkInfo;
        }
    }
}

/*
$Log: ServerTableMenuListener.java,v $
Revision 1.17  2003/10/31 16:30:49  zet
minor renames

Revision 1.16  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.15  2003/10/31 13:16:32  lemmster
Rename Viewer -> Page
Constructors changed

Revision 1.14  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.13  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

Revision 1.12  2003/10/28 11:07:32  lemmster
move NetworkInfo.Enum -> enum.EnumNetwork
add MaskMatcher for "Enum[]"

Revision 1.11  2003/10/22 01:37:55  zet
add column selector to server/search (might not be finished yet..)

Revision 1.10  2003/10/21 17:00:45  lemmster
class hierarchy for tableviewer

Revision 1.9  2003/09/24 09:35:57  lemmster
serverlink in menulistener

Revision 1.8  2003/09/18 11:31:03  lemmster
checkstyle

Revision 1.7  2003/09/18 11:26:07  lemmster
checkstyle

Revision 1.6  2003/09/16 10:17:07  lemmster
add server to favorites disabled

Revision 1.5  2003/09/14 13:24:30  lemmster
add header button to servertab

Revision 1.4  2003/09/14 11:38:50  lemmster
connectMore in right click menu

Revision 1.3  2003/09/08 15:43:34  lemmster
work in progress

Revision 1.2  2003/08/23 15:21:37  zet
remove @author

Revision 1.1  2003/08/23 09:46:18  lemmster
superclass TableMenuListener added

Revision 1.14  2003/08/22 19:00:25  lemmster
support for connectMore with network id

Revision 1.13  2003/08/22 00:11:07  lemmster
filter: display only networks with server

Revision 1.12  2003/08/20 21:34:22  lemmster
additive filters

Revision 1.11  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.10  2003/08/11 19:25:04  lemmstercvs01
bugfix at CleanTable

Revision 1.9  2003/08/10 12:59:12  lemmstercvs01
"manage servers" in NetworkItem implemented

Revision 1.8  2003/08/09 16:03:45  dek
added gnu.regexp for compiling with gcj
you can get it at:

ftp://ftp.tralfamadore.com/pub/java/gnu.regexp-1.1.4.tar.gz

Revision 1.7  2003/08/09 15:35:04  dek
added gnu.regexp for compiling with gcj
you can get it at:

ftp://ftp.tralfamadore.com/pub/java/gnu.regexp-1.1.4.tar.gz

Revision 1.6  2003/08/09 13:51:02  dek
added gnu.regexp for compiling with gcj
you can get it at:

ftp://ftp.tralfamadore.com/pub/java/gnu.regexp-1.1.4.tar.gz

Revision 1.5  2003/08/07 13:25:37  lemmstercvs01
ResourceBundle added

Revision 1.4  2003/08/07 12:35:31  lemmstercvs01
cleanup, more efficient

Revision 1.3  2003/08/06 09:42:34  lemmstercvs01
manual refresh for testing

Revision 1.2  2003/08/05 15:34:51  lemmstercvs01
network filter added

Revision 1.1  2003/08/05 13:50:10  lemmstercvs01
initial commit

*/
