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
package net.mldonkey.g2gui.view;

import java.util.Observable;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.ServerInfo;
import net.mldonkey.g2gui.model.ServerInfoIntMap;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.helper.CCLabel;
import net.mldonkey.g2gui.view.helper.HeaderBarMouseAdapter;
import net.mldonkey.g2gui.view.helper.PaneMenuListener;
import net.mldonkey.g2gui.view.helper.TableMenuListener;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.server.ServerTableContentProvider;
import net.mldonkey.g2gui.view.server.ServerTableLabelProvider;
import net.mldonkey.g2gui.view.server.ServerTableMenuListener;
import net.mldonkey.g2gui.view.server.ServerTableSorter;
import net.mldonkey.g2gui.view.transfer.CustomTableViewer;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * ServerTab
 *
 *
 * @version $Id: ServerTab.java,v 1.36 2003/09/27 12:33:32 dek Exp $ 
 *
 */
public class ServerTab extends GuiTab implements Runnable, DisposeListener {
	private boolean oldValue = PreferenceLoader.loadBoolean( "displayAllServers" );
	private boolean oldValue2 = PreferenceLoader.loadBoolean( "displayTableColors" );
	private boolean ascending = false;;
	private TableColumn tableColumn;
	private Combo combo;
	private GridData gridData;
	private Label label;
	private CoreCommunication core;
	private ServerInfoIntMap servers;
	private CustomTableViewer table;
	private Composite composite;
	private Group group;
	private MenuManager popupMenu;
	private String statusText = "";

	/* if you modify this, change the LayoutProvider and tableWidth */
	private String[] tableColumns =
	{ 
		"SVT_NETWORK",
		"SVT_NAME",
		"SVT_DESC",
		"SVT_ADDRESS",
		"SVT_PORT",
		"SVT_SERVERSCORE",
		"SVT_USERS",
		"SVT_FILES",
		"SVT_STATE",
		"SVT_FAVORITES"
	};
	/* 0 sets the tablewidth dynamcliy */
	private int[] tableWidth =
	{
		70, 160, 0, 120, 50,
		55, 55, 60, 80, 50
	};
	
	private int[] tableAlign =
	{
		SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT,
		SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.LEFT, SWT.LEFT
	};
	
	/**
	 * @param gui The main gui tab
	 */
	public ServerTab( MainTab gui ) {
		super( gui );
		/* associate this tab with the corecommunication */
		this.core = gui.getCore();
		/* Set our name on the coolbar */
		createButton( "ServersButton", 
						G2GuiResources.getString( "TT_ServersButton" ),
						G2GuiResources.getString( "TT_ServersButtonToolTip" ) );
		/* proto <= 16 does not support favorites */					
		if ( this.core.getProtoToUse() <= 16 ) {
			this.tableColumns = new String[]
			{
				"SVT_NETWORK",
				"SVT_NAME",
				"SVT_DESC",
				"SVT_ADDRESS",
				"SVT_PORT",
				"SVT_SERVERSCORE",
				"SVT_USERS",
				"SVT_FILES",
				"SVT_STATE"
			};	
			this.tableWidth = new int[]
			{
				70, 160, 0, 120,
				50, 55, 55, 60,
				80
			};
			
			this.tableAlign = new int[]
			{
				SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT,
				SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.LEFT
			};

		}

		/* create the tab content */
		this.createContents( this.subContent );
		updateDisplay();			
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.GuiTab#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected void createContents( Composite parent ) {
		
		ViewForm viewForm = 
			new ViewForm( parent, SWT.BORDER
				| ( PreferenceLoader.loadBoolean( "flatInterface" ) ? SWT.FLAT : SWT.NONE ) );
		
		CLabel ccLabel = CCLabel.createCL( viewForm, "TT_ServersButton", "ServersButtonSmallTitlebar" );
			
		this.composite = new Composite( viewForm, SWT.NONE );
		composite.setLayout( new FillLayout() );
	
		viewForm.setContent( this.composite );
		viewForm.setTopLeft( ccLabel );

		this.createTable();
		
		popupMenu = new MenuManager( "" );
		popupMenu.setRemoveAllWhenShown( true );
		popupMenu.addMenuListener( new PaneMenuListener( table, core ) );
		
		ccLabel.addMouseListener( new HeaderBarMouseAdapter( ccLabel, popupMenu ) );
	}

	/**
	 * Creates the table
	 */
	private void createTable() {
		table = new CustomTableViewer( composite, SWT.FULL_SELECTION | SWT.MULTI );
		table.getTable().setLayoutData( new GridData( GridData.FILL_BOTH ) );
		table.getTable().setLinesVisible( true );
		table.getTable().setHeaderVisible( true );
		table.setUseHashlookup( true );
		
		table.setContentProvider( new ServerTableContentProvider() );
		table.setLabelProvider( new ServerTableLabelProvider() );
		table.setSorter( new ServerTableSorter() );
		ServerTableMenuListener tableMenuListener = new ServerTableMenuListener( table, core );
		table.addSelectionChangedListener( tableMenuListener );
		MenuManager popupMenu = new MenuManager( "" );
		popupMenu.setRemoveAllWhenShown( true );
		popupMenu.addMenuListener( tableMenuListener );
		table.getTable().setMenu( popupMenu.createContextMenu( table.getTable() ) );
		
		/* create the columns */
		for ( int i = 0; i < tableColumns.length; i++ ) {
			final PreferenceStore prefStore = PreferenceLoader.getPreferenceStore();
			prefStore.setDefault( tableColumns[ i ], tableWidth[ i ] );
			tableColumn = new TableColumn( table.getTable(), tableAlign[ i ] );
			tableColumn.setText( G2GuiResources.getString( tableColumns[ i ] ) );
			tableColumn.setWidth( prefStore.getInt( tableColumns[ i ] ) );
			
			/* read the new tablewidth to the prefstore */
			final int j = i;
			tableColumn.addDisposeListener( new DisposeListener() {
				public synchronized void widgetDisposed( DisposeEvent e ) {
					TableColumn aColumn = ( TableColumn ) e.widget;
					prefStore.setValue( tableColumns[ j ] , aColumn.getWidth() );
				}
			} );
			
			/* adds a sort listener */
			final int columnIndex = i;
			tableColumn.addListener( SWT.Selection, new Listener() {
				public void handleEvent( Event e ) {
					/* set the column to sort */
					( ( ServerTableSorter ) table.getSorter() ).setColumnIndex( columnIndex );
					/* set the way to sort (ascending/descending) */
					( ( ServerTableSorter ) table.getSorter() ).setLastSort( ascending );

					/* get the data for all tableitems */
					TableItem[] items = table.getTable().getItems();
					ServerInfo[] temp = new ServerInfo[ items.length ];
					for ( int i = 0; i < items.length; i++ )
							temp[ i ] = ( ServerInfo ) items[ i ].getData();

					/* reverse sorting way */
					ascending = ascending ? false : true;

					table.getSorter().sort( table, temp );
					table.refresh();
				}	
			} );
		}

		/*
		 * add a menulistener to set the first item to default
		 * sadly not possible with the MenuManager Class
		 * (Feature Request on eclipse?)
		 */
		Menu menu = table.getTable().getMenu();
		menu.addMenuListener( new MenuListener() {
			public void menuShown( MenuEvent e ) {
				Menu menu = table.getTable().getMenu();
				if ( !table.getSelection().isEmpty() )
					menu.setDefaultItem( menu.getItem( 0 ) );
			}
			public void menuHidden( MenuEvent e ) { }
		} );

		/* fill the table with content */
		servers = this.core.getServerInfoIntMap();
		this.table.setInput( servers );
		servers.clearAdded();
		
		/* set the state filter if preference says so */
		if ( PreferenceLoader.loadBoolean( "displayAllServers" ) ) {
			ServerTableMenuListener.EnumStateFilter filter = new ServerTableMenuListener.EnumStateFilter();
			filter.add( EnumState.CONNECTED );
			table.addFilter( filter );
		}
		
		int itemCount = table.getTable().getItemCount();
		//setRightLabel("Total: " + itemCount);
		/* dont update the statusline, still null */
		this.statusText = G2GuiResources.getString( "SVT_SERVERS" ) + itemCount;
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update( Observable o, Object arg ) {
		if ( table.getTable().isDisposed() ) return;
		this.content.getDisplay().asyncExec( this );
	}		

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		/* still running? */
		if ( table.getTable().isDisposed() ) return;
		
		synchronized ( this.servers.getRemoved() ) {
			table.remove( this.servers.getRemoved().toArray() );
			this.servers.clearRemoved();
		}
		synchronized ( this.servers.getAdded() ) {
			table.add( this.servers.getAdded().toArray() );
			this.servers.clearAdded();
		}
		synchronized ( this.servers.getModified() ) {
			table.update( this.servers.getModified().toArray(), null );
			this.servers.clearModified();
		}
		int itemCount = table.getTable().getItemCount();	
		this.setStatusLine();
		
		/* refresh the table if "show connected servers only" is true and the filter is activated */
		if ( PreferenceLoader.loadBoolean( "displayAllServers" )
		&& this.servers.getConnected() != itemCount ) {
			ViewerFilter[] filters = table.getFilters();
			for ( int i = 0; i < filters.length; i++ )
				if ( filters[ i ] instanceof ServerTableMenuListener.EnumStateFilter ) {
					TableMenuListener.EnumStateFilter filter =
						( TableMenuListener.EnumStateFilter ) filters[ i ];
					for ( int j = 0; j < filter.getEnumState().size(); j++ ) {
						if ( filter.getEnumState().get( j ) == EnumState.CONNECTED )
							table.refresh();
					}
				}
		}
	}
	
	/**
	 * unregister ourself at the observable
	 */
	public void setInActive() {
		this.core.getServerInfoIntMap().deleteObserver( this );
		super.setInActive();
	}
	
	/**
	 * register ourself at the observable
	 */
	public void setActive() {
		/* if we become active, refresh the table */
		this.servers = this.core.getServerInfoIntMap();
		table.getTable().getDisplay().asyncExec( this );
		this.setStatusLine();
		
		this.core.getServerInfoIntMap().addObserver( this );
		super.setActive();
	}
	
	/**
	 * Sets the statusline to the current value
	 */
	private void setStatusLine() {
		if ( !this.isActive() ) return;
		this.statusText = G2GuiResources.getString( "SVT_SERVERS" ) + servers.getConnected();
		this.mainWindow.getStatusline().update( this.statusText );
		this.mainWindow.getStatusline().updateToolTip( "" );
	}
	
	/**
	 * @return The text this tab wants to display in the statusline
	 */
	public String getStatusText() {
		return this.statusText;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.DisposeListener#
	 * widgetDisposed(org.eclipse.swt.events.DisposeEvent)
	 */
	public void widgetDisposed( DisposeEvent e ) {
		this.core.getServerInfoIntMap().deleteObserver( this );
	}
	
	/**
	 * Adds a <code>ViewerFilter</code> to the <code>TableViewer</code> by
	 * removing all present <code>TabelMenuListener.NetworkFilter</code> 
	 * from the table.
	 * @param enum The <code>NetworkInfo.Enum</code> we want to be filtered
	 */
	public void setFilter( NetworkInfo.Enum enum ) {
		ViewerFilter[] filters = table.getFilters();
		for ( int i = 0; i < filters.length; i++ ) {
			if ( filters[ i ] instanceof ServerTableMenuListener.NetworkFilter )
				table.removeFilter( filters[ i ] );
		}
		ServerTableMenuListener.NetworkFilter filter = new ServerTableMenuListener.NetworkFilter();
		filter.add( enum );
		table.addFilter( filter );
	}
	
	/**
	 * Updates this tab on preference close
	 */
	public void updateDisplay() {
		/* only update on pref change */
		boolean temp = PreferenceLoader.loadBoolean( "displayAllServers" );
		if ( oldValue != temp ) {
			/* update the state filter */
			if ( temp ) {
				/* first remove all EnumState filters */
				for ( int i = 0; i < table.getFilters().length; i++ ) {
					if ( table.getFilters()[ i ] instanceof ServerTableMenuListener.EnumStateFilter )
						table.removeFilter( table.getFilters()[ i ] );
				}
				/* now add the new one */
				ServerTableMenuListener.EnumStateFilter filter =
					 new ServerTableMenuListener.EnumStateFilter();
				filter.add( EnumState.CONNECTED );
				table.addFilter( filter );
			}
			else {
				ViewerFilter[] filters = table.getFilters();
				for ( int i = 0; i < filters.length; i++ ) {
					if ( table.getFilters()[ i ] instanceof ServerTableMenuListener.EnumStateFilter ) {
						TableMenuListener.EnumStateFilter filter =
							( TableMenuListener.EnumStateFilter ) filters[ i ];
						if ( filter.contains( EnumState.CONNECTED ) ) {
							if (  filter.getEnumState().size() == 1 )
								table.removeFilter( filter );
							else {
								filter.remove( EnumState.CONNECTED );
								table.refresh();
							}
						}
					}
				}
			}
			this.oldValue = temp;
		}
		/* displayTableColors changed? */
		boolean temp2 = PreferenceLoader.loadBoolean( "displayTableColors" );
		if ( oldValue2 != temp2 ) {
			( ( ServerTableLabelProvider ) table.getLabelProvider() ).setColors( temp2 );
			table.refresh();
			this.oldValue2 = temp2;
		}
		table.getTable().setLinesVisible(
				PreferenceLoader.loadBoolean( "displayGridLines" ) );
		super.updateDisplay();
	}
}

/*
$Log: ServerTab.java,v $
Revision 1.36  2003/09/27 12:33:32  dek
server-Table has now same show-Gridlines-behaviour as download-Table

Revision 1.35  2003/09/23 11:46:25  lemmster
displayTableColors for servertab

Revision 1.34  2003/09/22 20:02:34  lemmster
right column align for integers [bug #934]

Revision 1.33  2003/09/20 14:39:48  zet
move transfer package

Revision 1.32  2003/09/18 09:44:57  lemmster
checkstyle

Revision 1.31  2003/09/16 02:12:30  zet
match the menus with transfertab

Revision 1.30  2003/09/14 13:44:22  lemmster
set statusline only on active

Revision 1.29  2003/09/14 13:24:30  lemmster
add header button to servertab

Revision 1.28  2003/09/14 10:01:24  lemmster
save column width [bug #864]

Revision 1.27  2003/09/14 09:42:51  lemmster
save column width

Revision 1.26  2003/09/14 09:40:31  lemmster
save column width

Revision 1.25  2003/09/11 13:39:19  lemmster
check for disposed

Revision 1.24  2003/08/29 22:11:47  zet
add CCLabel helper class

Revision 1.23  2003/08/29 19:30:50  zet
font colour

Revision 1.22  2003/08/29 17:33:20  zet
remove headerbar

Revision 1.21  2003/08/29 16:06:54  zet
optional shadow

Revision 1.20  2003/08/28 22:44:30  zet
GridLayout helper class

Revision 1.19  2003/08/25 20:38:26  vnc
GTK layout cleanups, slight typo refactoring

Revision 1.18  2003/08/23 15:21:37  zet
remove @author

Revision 1.17  2003/08/23 15:01:32  lemmster
update the header

Revision 1.16  2003/08/23 14:58:38  lemmster
cleanup of MainTab, transferTree.* broken

Revision 1.15  2003/08/23 10:33:36  lemmster
updateDisplay() only on displayAllServers change

Revision 1.14  2003/08/23 09:46:18  lemmster
superclass TableMenuListener added

Revision 1.13  2003/08/23 08:30:07  lemmster
added defaultItem to the table

Revision 1.12  2003/08/20 22:18:56  zet
Viewer updates

Revision 1.11  2003/08/20 21:34:22  lemmster
additive filters

Revision 1.10  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.9  2003/08/11 19:25:04  lemmstercvs01
bugfix at CleanTable

Revision 1.8  2003/08/10 12:59:01  lemmstercvs01
"manage servers" in NetworkItem implemented

Revision 1.7  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.6  2003/08/07 13:25:37  lemmstercvs01
ResourceBundle added

Revision 1.5  2003/08/07 12:35:31  lemmstercvs01
cleanup, more efficient

Revision 1.4  2003/08/06 20:56:49  lemmstercvs01
cleanup, more efficient

Revision 1.3  2003/08/06 17:38:38  lemmstercvs01
some actions still missing. but it should work for the moment

Revision 1.2  2003/08/05 15:35:24  lemmstercvs01
minor: column width changed

Revision 1.1  2003/08/05 13:50:10  lemmstercvs01
initial commit

*/