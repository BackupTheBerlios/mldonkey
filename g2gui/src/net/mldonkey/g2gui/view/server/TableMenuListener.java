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
import gnu.regexp.REMatch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;


import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.ServerInfo;
import net.mldonkey.g2gui.model.ServerInfoIntMap;
import net.mldonkey.g2gui.model.enum.EnumState;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * TableMenuListener
 *
 * @author $user$
 * @version $Id: TableMenuListener.java,v 1.9 2003/08/10 12:59:12 lemmstercvs01 Exp $ 
 *
 */
public class TableMenuListener implements ISelectionChangedListener, IMenuListener {
	private static ResourceBundle res = ResourceBundle.getBundle( "g2gui" );
	private ServerInfo selectedServer;
	private List selectedServers;
	private ServerInfoIntMap serverInfoMap;
	private TableContentProvider tableContentProvider;
	private TableViewer tableViewer;
	private CoreCommunication core;

	/**
	 * Creates a new TableMenuListener
	 * @param The parent TableViewer
	 * @param The CoreCommunication supporting this with data
	 */
	public TableMenuListener( TableViewer tableViewer, CoreCommunication core ) {
		super();
		this.tableViewer = tableViewer;
		this.core = core;
		this.serverInfoMap = this.core.getServerInfoIntMap();
		this.tableContentProvider = ( TableContentProvider ) this.tableViewer.getContentProvider();
		this.selectedServers = new ArrayList();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
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
				selectedServers.add( ( ServerInfo ) o);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */
	public void menuAboutToShow( IMenuManager menuManager ) {
		/* disconnect */
		if ( selectedServer != null
			&& selectedServer.isConnected() )
			menuManager.add( new DisconnectAction() );

		/* connect */
		if ( selectedServer != null
			&& selectedServer.getConnectionState().getState() == EnumState.NOT_CONNECTED )
			menuManager.add( new ConnectAction() );

		/* add server/servers */
		MenuManager addManager = new MenuManager( res.getString( "TML_ADD_SERVER_BY" ) );
		addManager.add( new AddServerAction() );
		addManager.add( new AddServersAction() );
		menuManager.add( addManager );

		/* remove server */
		MenuManager removeManager = new MenuManager( res.getString( "TML_REMOVE_SERVER_BY" ) );
		removeManager.add( new RemoveServerAction() );
		removeManager.add( new RemoveServersAction() );
		menuManager.add( removeManager );
		
		/* blacklist server */
		if ( selectedServer != null )
			menuManager.add( new BlackListAction() );
			
		menuManager.add( new Separator() );

		/* add to favorites */
		if ( selectedServer != null && this.core.getProtoToUse() > 16 )
			menuManager.add( new FavoritesAction() );			
		
		/* columns toogle */
		MenuManager columnsSubMenu = new MenuManager( res.getString( "TML_COLUMN" ) );
		Table table = tableViewer.getTable();
		for ( int i = 0; i < table.getColumnCount(); i++ ) {
			ToggleColumnsAction tCA = new ToggleColumnsAction( i );
			if ( table.getColumn( i ).getResizable() ) tCA.setChecked( true );
			columnsSubMenu.add( tCA );
		}
		menuManager.add( columnsSubMenu );
			
		// filter submenu (select network to display)			
		MenuManager filterSubMenu = new MenuManager( res.getString( "TML_FILTER" ) );
		AllFiltersAction aFA = new AllFiltersAction();
		if ( tableViewer.getFilters().length == 0 ) aFA.setChecked( true );
		filterSubMenu.add( aFA );
		filterSubMenu.add( new Separator() );
			
		NetworkInfo[] networks = core.getNetworkInfoMap().getNetworks();
		
		for ( int i = 0; i < networks.length; i++ ) {
			NetworkInfo network = networks[ i ];
			if ( network.isEnabled() ) {
				NetworkFilterAction nFA = new NetworkFilterAction( network.getNetworkName(), network.getNetworkType() );
				if ( isFiltered( network.getNetworkType() ) ) nFA.setChecked( true );
				filterSubMenu.add( nFA );
			}
		}

		filterSubMenu.add( new Separator() );
		
		EnumState[] states = { EnumState.BLACK_LISTED, EnumState.CONNECTED, EnumState.CONNECTED_AND_QUEUED,
							   EnumState.CONNECTED_DOWNLOADING, EnumState.CONNECTED_INITIATING, 
							   EnumState.CONNECTING, EnumState.NEW_HOST, EnumState.NOT_CONNECTED,
							   EnumState.NOT_CONNECTED_WAS_QUEUED };
							   
		for ( int i = 0; i < states.length; i ++ ) {
			EnumState state = states[ i ];
			EnumStateFilterAction enFA = new EnumStateFilterAction( state.toString(), state );
			if ( isFiltered( state ) )
				enFA.setChecked( true );
			filterSubMenu.add( enFA );	 	
		}

		
		
		menuManager.add( filterSubMenu );

		menuManager.add( new Separator() );

		menuManager.add( new RefreshAction() );
	}
	
	public boolean isFiltered( NetworkInfo.Enum networkType ) {
		ViewerFilter[] viewerFilters = tableViewer.getFilters();
		for ( int i = 0; i < viewerFilters.length; i++ ) {
			if ( viewerFilters [ i ] instanceof NetworkFilter )
				if ( ( ( NetworkFilter ) viewerFilters[ i ] ).getNetworkType().equals(networkType ) )
					return true; 
	
		}
		return false;
	}

	public boolean isFiltered( EnumState state ) {
		ViewerFilter[] viewerFilters = tableViewer.getFilters();
		for ( int i = 0; i < viewerFilters.length; i++ ) {
			if ( viewerFilters [ i ] instanceof EnumStateFilter )
				if ( ( ( EnumStateFilter ) viewerFilters[ i ] ).getEnumState().equals( state ) )
					return true; 
	
		}
		return false;
	}

	public void toggleFilter( ViewerFilter viewerFilter, boolean toggle ) {
		if ( toggle ) 
			tableViewer.addFilter( viewerFilter );
		else 
			tableViewer.removeFilter( viewerFilter );
	}
	
	private class DisconnectAction extends Action {
		public DisconnectAction() {
			super();
			setText( res.getString( "TML_DISCONNECT" ) );
		}
		public void run() {
			for ( int i = 0; i < selectedServers.size(); i++ ) {
				ServerInfo server = ( ServerInfo ) selectedServers.get( i );
				serverInfoMap.disconnect( server.getServerId() );
			}
		}
	}
	
	private class ConnectAction extends Action {
		public ConnectAction() {
			super();
			setText( res.getString( "TML_CONNECT" ) );
		}
		public void run() {
			for ( int i = 0; i < selectedServers.size(); i++ ) {
				ServerInfo server = ( ServerInfo ) selectedServers.get( i );
				serverInfoMap.connect( server.getServerId() );
			}
		}
	}
	
	private class ConnectMoreAction extends Action {
		public ConnectMoreAction() {
			super();
			setText( res.getString( "TML_CONNECT_MORE" ) );
		}
		public void run() {
			serverInfoMap.connectMore();
		}
	}
	
	private class AddServerAction extends Action {
		private MyInputDialog dialog;
		public AddServerAction() {
			super();
			setText( res.getString( "TML_ADD_SERVER" ) );
		}
		public void run() {
			dialog = new MyInputDialog( tableViewer.getTable().getShell(),
										res.getString( "TML_ADD_SERVER" ),
										res.getString( "TML_HOSTNAME_PORT" ),
										res.getString( "TML_NETWORK" ),
										res.getString( "TML_FOOBAR2" ),
										new MyInputValidator() );
			dialog.open();
			if ( dialog.getReturnCode() == IDialogConstants.OK_ID ) {
				String text = dialog.getValue();				
				
				String[] strings = split(dialog.getValue(), ':' );
				InetAddress inetAddress = null;
				try {
					inetAddress = InetAddress.getByName( strings[0] );
				}
				catch ( UnknownHostException e ) {
					MessageBox box = new MessageBox( tableViewer.getTable().getShell(),
														 SWT.ICON_WARNING | SWT.OK );
					box.setText( res.getString( "TML_LOOKUP_ERROR" ) );
					box.setMessage( res.getString( "TML_CANNOT_RESOLVE" ) );
					box.open();
				}
				core.getServerInfoIntMap().add( dialog.getCombo(), inetAddress, new Short( strings[ 1 ] ).shortValue() );							
			}
		}
				
		private String[] split( String searchString, char delimiter ) {
				RE regex = null;
				String expression = "([^" + delimiter + "])*";		
				try {
					regex = new RE( expression );
				} catch ( REException e ) {
					e.printStackTrace();
				}
				ArrayList patterns = new ArrayList();		
				REMatch[] matches = regex.getAllMatches( searchString );
				for ( int i = 0; i < matches.length; i++ ) {
					String match = matches[ i ].toString();			
					if ( !match.equals( "" ) )
						patterns.add( match );
				}
				Object[] temp = patterns.toArray();
				String[] result = new String[ temp.length ];
				for ( int i = 0; i < temp.length; i++ ) {
					result[ i ] = ( String ) temp[ i ];
				}
				return result;
			}
		
		private class MyInputValidator implements IInputValidator {
			/* (non-Javadoc)
			 * @see org.eclipse.jface.dialogs.IInputValidator#isValid(java.lang.String)
			 */
			public String isValid( String newText ) {
				
				RE regex = null;				
				try {
					 regex = new RE( "([a-z0-9-.]*):[0-9]{1,5}" );
				} catch ( REException e ) {			
					e.printStackTrace();
				}
				
				if ( regex.isMatch( newText ) )				
					return null;
				else
					return res.getString( "TML_INVALID_INPUT" );
			}
			
		}
	}
	
	private class AddServersAction extends Action {
		private InputDialog dialog;
		public AddServersAction() {
			super();
			setText( res.getString( "TML_ADD_SERVERS" ) );
		}
		public void run() {
			dialog = new InputDialog( tableViewer.getTable().getShell(),
										res.getString( "TML_ADD_SERVERS" ),
										res.getString( "TML_LINK_TO_LIST" ),
										res.getString( "TML_FOOBAR" ),
										new MyInputValidator() );
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
				} catch (REException e) {					
					e.printStackTrace();
				}				
				if ( regex.isMatch( newText ) )
					return null;
				else
					return res.getString( "TML_INVALID_INPUT" );
			}
		}
	}
	
	private class RemoveServerAction extends Action {
		public RemoveServerAction() {
			super();
			setText( res.getString( "TML_REMOVE_SERVER" ) );
		}
		public void run() {
			for ( int i = 0; i < selectedServers.size(); i++ ) {
				ServerInfo server = ( ServerInfo ) selectedServers.get( i );
				serverInfoMap.remove( server.getServerId() );
			}
		}
	}
	
	private class RemoveServersAction extends Action {
		public RemoveServersAction() {
			super();
			setText( res.getString( "TML_REMOVE_SERVERS" ) );
		}
		public void run() {
			serverInfoMap.cleanOld();
		}
	}
	
	private class BlackListAction extends Action {
		public BlackListAction() {
			super();
			setText( res.getString( "TML_ADD_TO_BLACKLIST" ) );
		}
		public void run() {
			for ( int i = 0; i < selectedServers.size(); i++ ) {
				ServerInfo server = ( ServerInfo ) selectedServers.get( i );
				serverInfoMap.addToBlackList( server.getServerId() );
			}
		}
	}
	
	private class ToggleColumnsAction extends Action {
		private int column;
		private Table table = tableViewer.getTable();
		private TableColumn tableColumn;
		
		public ToggleColumnsAction( int column ) {
			super( "", Action.AS_CHECK_BOX );
			this.column = column;
			tableColumn = table.getColumn( column );
			setText( tableColumn.getText() );
		}
		
		public  void run () {
			if ( !isChecked() ) {
				tableColumn.setWidth( 0 );
				tableColumn.setResizable( false );
			} else {
				tableColumn.setResizable( true );
				tableColumn.setWidth( 100 );
			}
		}
	}
	
	private class AllFiltersAction extends Action {
		public AllFiltersAction() {
			super();
			setText( res.getString( "TML_NO_FILTERS" ) );
		}
		public void run() {
			ViewerFilter[] viewerFilters = tableViewer.getFilters();
			for ( int i = 0; i < viewerFilters.length; i++ ) 
				toggleFilter( viewerFilters[ i ], false );
		}
	}
	
	private class NetworkFilterAction extends Action {
		private NetworkInfo.Enum networkType;

		public NetworkFilterAction( String name, NetworkInfo.Enum networkType ) {
			super(name, Action.AS_CHECK_BOX);
			this.networkType = networkType;
		}
		public void run() {
			if ( !isChecked() ) {
				ViewerFilter[] viewerFilters = tableViewer.getFilters();
				for ( int i = 0; i < viewerFilters.length; i++ ) {
					if ( viewerFilters[i] instanceof NetworkFilter )
						if ( ( ( NetworkFilter ) viewerFilters[ i ] ).getNetworkType() == networkType ) {
							toggleFilter( viewerFilters[ i ], false );
						}
				}
			} else {
				toggleFilter( new NetworkFilter( networkType ), true );
			}
		}
	}
	
	private class EnumStateFilterAction extends Action {
		private EnumState state;
		
		public EnumStateFilterAction( String name, EnumState state ) {
			super( name, Action.AS_CHECK_BOX );
			this.state = state;			
		}
		
		public void run() {
			if ( !isChecked() ) {
				ViewerFilter[] viewerFilters = tableViewer.getFilters();
				for ( int i = 0; i < viewerFilters.length; i++ ) {
					if ( viewerFilters[i] instanceof EnumStateFilter )
						if ( ( ( EnumStateFilter ) viewerFilters[ i ] ).getEnumState() == state ) {
							toggleFilter( viewerFilters[ i ], false );
						}
				}
			} else {
				toggleFilter( new EnumStateFilter( state ), true );
			}
		}		
	}
	
	private class RefreshAction extends Action {
		public RefreshAction() {
			super();
			setText( "Manual Refresh (for developing)" );
		}
		public void run() {
			tableViewer.getTable().getDisplay().asyncExec( new Runnable() {
				public void run() {
					tableViewer.refresh();
				}
			} );
		}		
	}
	
	private class FavoritesAction extends Action {
		public FavoritesAction() {
			super();
			setText( res.getString( "TML_FAVORITES" ) );
		}
		public void run() {
			for ( int i = 0; i < selectedServers.size(); i++ ) {
				ServerInfo server = ( ServerInfo ) selectedServers.get( i );
				server.setFavorites();
			}
		}	
	}		
	
	public static class NetworkFilter extends ViewerFilter {
		private NetworkInfo.Enum networkType;
		
		public NetworkFilter( NetworkInfo.Enum enum ) {
			this.networkType = enum;	
		}	
		
		public NetworkInfo.Enum getNetworkType () {
			return networkType;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public boolean select( Viewer viewer, Object parentElement, Object element ) {
			if ( element instanceof ServerInfo ) {
				ServerInfo server = ( ServerInfo ) element;
				if ( server.getNetwork().getNetworkType() == networkType )
					return true;
				else 
					return false;
			}
			return true;
		}
	}
	
	private class EnumStateFilter extends ViewerFilter {
		private EnumState state;
		
		public EnumStateFilter( EnumState state ) {
			this.state = state;
		}
		
		public EnumState getEnumState() {
			return this.state;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ViewerFilter#
		 * select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if ( element instanceof ServerInfo ) {
				ServerInfo server = ( ServerInfo ) element;
				if ( server.getConnectionState().getState() == state )
					return true;
				else 
					return false;
			}
			return true;
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
		public MyInputDialog( Shell parentShell, String dialogTitle,
							   String dialogMessage, String comboMessage, String initialValue, IInputValidator validator ) {
			super( parentShell, dialogTitle, dialogMessage, initialValue, validator );
			this.comboMessage = comboMessage;
		}

		protected Control createDialogArea( Composite parent ) {
			composite = ( Composite ) super.createDialogArea( parent );

			/* create the combo message */
			if ( comboMessage != null) {
				Label label = new Label( composite, SWT.WRAP );
				label.setText( comboMessage );
				GridData data = new GridData(
					GridData.GRAB_HORIZONTAL |
					GridData.GRAB_VERTICAL |
					GridData.HORIZONTAL_ALIGN_FILL |
					GridData.VERTICAL_ALIGN_CENTER );
				data.widthHint = convertHorizontalDLUsToPixels( IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH );
				label.setLayoutData( data );
				label.setFont( parent.getFont() );
			}

			/* now our combo for the networks */
			combo = new Combo( composite, SWT.NONE );
			combo.setLayoutData( new GridData(
				GridData.GRAB_HORIZONTAL |
				GridData.HORIZONTAL_ALIGN_FILL ) );
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
			if ( buttonId == IDialogConstants.OK_ID ) {
				this.networkInfo = ( NetworkInfo ) combo.getData( combo.getItem( combo.getSelectionIndex() ) );
			}
			else {
				this.networkInfo = null;
			}
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
$Log: TableMenuListener.java,v $
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