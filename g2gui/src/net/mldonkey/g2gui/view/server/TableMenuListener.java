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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * TableMenuListener
 *
 * @author $user$
 * @version $Id: TableMenuListener.java,v 1.1 2003/08/05 13:50:10 lemmstercvs01 Exp $ 
 *
 */
public class TableMenuListener implements ISelectionChangedListener, IMenuListener {
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
			&& selectedServer.getConnectionState().getState() == EnumState.CONNECTED )
			menuManager.add( new DisconnectAction() );

		/* connect */
		if ( selectedServer != null
			&& selectedServer.getConnectionState().getState() == EnumState.NOT_CONNECTED )
			menuManager.add( new ConnectAction() );

		/* add server/servers */
		MenuManager addManager = new MenuManager( "Add Server/Server.met" );
		addManager.add( new AddServerAction() );
		addManager.add( new AddServersAction() );
		menuManager.add( addManager );

		/* remove server */
		MenuManager removeManager = new MenuManager( "Remove Server/Servers" );
		removeManager.add( new RemoveServerAction() );
		removeManager.add( new RemoveServersAction() );
		menuManager.add( removeManager );
		
		if ( selectedServer != null )
			menuManager.add( new BlackListAction() );
			
		menuManager.add( new Separator() );
		
		/* columns toogle */
		MenuManager columnsSubMenu = new MenuManager( "Columns" );
		Table table = tableViewer.getTable();
		for ( int i = 0; i < table.getColumnCount(); i++ ) {
			ToggleColumnsAction tCA = new ToggleColumnsAction( i );
			if ( table.getColumn( i ).getResizable() ) tCA.setChecked( true );
			columnsSubMenu.add( tCA );
		}
		menuManager.add( columnsSubMenu );
			
		// filter submenu (select network to display)			
		MenuManager filterSubMenu = new MenuManager( "Filters" );
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
		
		menuManager.add( filterSubMenu );
	}
	
	public boolean isFiltered( NetworkInfo.Enum networkType ) {
		ViewerFilter[] viewerFilters = tableViewer.getFilters();
		for (int i = 0; i < viewerFilters.length; i++) {
			if ( viewerFilters [ i ] instanceof NetworkFilter )
				if (((NetworkFilter) viewerFilters[ i ]).getNetworkType().equals(networkType))
					return true; 
	
		}
		return false;
	}
	
	private class DisconnectAction extends Action {
		public DisconnectAction() {
			super();
			setText( "Disconnect Server" );
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
			setText( "Connect Server" );
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
			setText( "Connect More Servers" );
		}
		public void run() {
			serverInfoMap.connectMore();
		}
	}
	
	private class AddServerAction extends Action {
		public AddServerAction() {
			super();
			setText( "Add Server" );
		}
		public void run() {
		}
	}
	
	private class AddServersAction extends Action {
		private InputDialog dialog;
		public AddServersAction() {
			super();
			setText( "Add Serverlist" );
		}
		public void run() {
			dialog = new InputDialog( tableViewer.getTable().getShell(),
										"Add Serverlist",
										"The Link to the ServerList (must end either on .met or .ocl)",
										"http://foobar.tld/serverlist.foo",
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
				Pattern pattern = Pattern.compile("[a-z/:]*[.met|.ocl]"); //TODO correct pattern
				Matcher m = pattern.matcher( newText );
				if ( m.find() )
					return null;
				else
					return "Invalid Input";	
			}
		}
	}
	
	private class RemoveServerAction extends Action {
		public RemoveServerAction() {
			super();
			setText( "Remove Server" );
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
			setText( "Remove Old" );
		}
		public void run() {
			serverInfoMap.cleanOld();
		}
	}
	
	private class BlackListAction extends Action {
		public BlackListAction() {
			super();
			setText( "Add to BlackList" );
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
			setText( "All Filters" );
		}
		public void run() {
		}
	}
	
	private class NetworkFilterAction extends Action {
		private NetworkInfo.Enum networkType;

		public NetworkFilterAction( String name, NetworkInfo.Enum networkType ) {
			super(name, Action.AS_CHECK_BOX);
			this.networkType = networkType;
		}
		public void run() {
		}
	}
	
	private class NetworkFilter extends ViewerFilter {
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
		public boolean select(Viewer viewer, Object parentElement, Object element) {
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
}

/*
$Log: TableMenuListener.java,v $
Revision 1.1  2003/08/05 13:50:10  lemmstercvs01
initial commit

*/