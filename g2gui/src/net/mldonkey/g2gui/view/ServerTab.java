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
import java.util.ResourceBundle;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.ServerInfo;
import net.mldonkey.g2gui.model.ServerInfoIntMap;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.server.TableContentProvider;
import net.mldonkey.g2gui.view.server.TableLabelProvider;
import net.mldonkey.g2gui.view.server.TableMenuListener;
import net.mldonkey.g2gui.view.server.TableSorter;
import net.mldonkey.g2gui.view.server.TableMenuListener.EnumStateFilter;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * ServerTab
 *
 * @author $Author: lemmstercvs01 $
 * @version $Id: ServerTab.java,v 1.9 2003/08/11 19:25:04 lemmstercvs01 Exp $ 
 *
 */
public class ServerTab extends GuiTab implements Runnable, DisposeListener {
	private static ResourceBundle res = ResourceBundle.getBundle( "g2gui" );
	private boolean ascending = false;;
	private TableColumn tableColumn;
	private Combo combo;
	private GridData gridData;
	private Label label;
	private GridLayout gridLayout;
	private CoreCommunication core;
	private ServerInfoIntMap servers;
	private TableViewer table;
	private Composite composite;
	private Group group;
	private String statusText = "";

	/* if you modify this, change the LayoutProvider and tableWidth */
	private String[] tableColumns =
	{ 
		res.getString( "SVT_NETWORK" ),
		res.getString( "SVT_NAME" ),
		res.getString( "SVT_DESC" ),
		res.getString( "SVT_ADDRESS" ),
		res.getString( "SVT_PORT" ),
		res.getString( "SVT_SERVERSCORE" ),
		res.getString( "SVT_USERS" ),
		res.getString( "SVT_FILES" ),
		res.getString( "SVT_STATE" ),
		res.getString( "SVT_FAVORITES" )
	};
	/* 0 sets the tablewidth dynamcliy */
	private int[] tableWidth =
	{
		45, 120, 0, 97,
		50, 55, 45, 50,
		80, 50
	};
	
	/**
	 * @param gui The main gui tab
	 */
	public ServerTab( MainTab gui ) {
		super( gui );
		/* associate this tab with the corecommunication */
		this.core = gui.getCore();
		/* Set our name on the coolbar */
		createButton( "ServerButton", "Server",	"Server tab" );
		
		/* proto <= 16 does not support favorites */					
		if ( this.core.getProtoToUse() <= 16 ) {
			this.tableColumns = new String[]
			{
				res.getString( "SVT_NETWORK" ),
				res.getString( "SVT_NAME" ),
				res.getString( "SVT_DESC" ),
				res.getString( "SVT_ADDRESS" ),
				res.getString( "SVT_PORT" ),
				res.getString( "SVT_SERVERSCORE" ),
				res.getString( "SVT_USERS" ),
				res.getString( "SVT_FILES" ),
				res.getString( "SVT_STATE" ),
			};	
			this.tableWidth = new int[]
			{
				45, 120, 0, 97,
				50, 55, 45, 50,
				80
			};
		}

		/* create the tab content */
		this.createContents( this.subContent );
			
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.GuiTab#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected void createContents( Composite parent ) {
		this.composite = new Composite( parent, SWT.BORDER );
		composite.setLayout( new FillLayout() );
		this.createTable();
	}

	/**
	 * Creates the table
	 */
	private void createTable() {
		table = new TableViewer( composite, SWT.FULL_SELECTION | SWT.MULTI );
		table.getTable().setLayoutData( new GridData( GridData.FILL_BOTH ) );
		table.getTable().setLinesVisible( true );
		table.getTable().setHeaderVisible( true );
		table.setUseHashlookup( true );
		
		table.setContentProvider( new TableContentProvider() );
		table.setLabelProvider( new TableLabelProvider() );
		table.setSorter( new TableSorter() );
		TableMenuListener tableMenuListener = new TableMenuListener( table, core );
		table.addSelectionChangedListener( tableMenuListener );
		MenuManager popupMenu = new MenuManager( "" );
		popupMenu.setRemoveAllWhenShown( true );
		popupMenu.addMenuListener( tableMenuListener );
		table.getTable().setMenu( popupMenu.createContextMenu( table.getTable() ) );
		
		/* create the columns */
		for ( int i = 0; i < tableColumns.length; i++ ) {
			tableColumn = new TableColumn( table.getTable(), SWT.LEFT );
			tableColumn.setText( tableColumns[ i ] );
			tableColumn.pack();
			
			/* adds a sort listener */
			final int columnIndex = i;
			tableColumn.addListener( SWT.Selection, new Listener() {
				public void handleEvent( Event e ) {
					/* set the column to sort */
					( ( TableSorter ) table.getSorter() ).setColumnIndex( columnIndex );
					/* set the way to sort (ascending/descending) */
					( ( TableSorter ) table.getSorter() ).setLastSort( ascending );

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

		/* add a resize listener */
		composite.addControlListener( new ControlAdapter() {
		public void controlResized( ControlEvent e ) {
				ServerTab.this.setColumnWidht();
			}
		} );
		
		/* fill the table with content */
		servers = this.core.getServerInfoIntMap();
		this.table.setInput( servers );
		servers.clearAdded();
		
		/* set the state filter if preference says so */
		if ( PreferenceLoader.loadBoolean( "displayAllServers" ) )
			table.addFilter( new TableMenuListener.EnumStateFilter( EnumState.CONNECTED ) );
		
		int itemCount = table.getTable().getItemCount();
		setRightLabel("Total: " + itemCount);
		/* dont update the statusline, still null */
		this.statusText = res.getString( "SVT_SERVERS" ) + itemCount;
	}

	/**
	 * sets the columnwidth for each column
	 */
	private void setColumnWidht() {
		/* the total width of the table */
		int totalWidth = table.getTable().getSize().x - 25; //why is it 25 to width?
		/* our tablecolumns */
		TableColumn[] columns = table.getTable().getColumns();
		for ( int i = 0; i < tableWidth.length; i++ ) {
			TableColumn column = columns[ i ];
			int width = tableWidth[ i ];
			if ( width != 0 ) {
				column.setWidth( tableWidth[ i ] );
				totalWidth -= tableWidth[ i ];
			}
		}
		/* sets the size of the name (add each column you want to set dynamicly) */
		columns[ 2 ].setWidth( totalWidth );
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
		setRightLabel( "Total: " + itemCount );
		this.setStatusLine();
		
		/* refresh the table if "show connected servers only" is true and the filter is activated */
		if ( PreferenceLoader.loadBoolean( "displayAllServers" ) && this.servers.getConnected() != itemCount ) {
			ViewerFilter[] filters = table.getFilters();
			for ( int i = 0; i < filters.length; i++ )
				if ( filters[ i ] instanceof TableMenuListener.EnumStateFilter ) {
					TableMenuListener.EnumStateFilter filter = ( EnumStateFilter ) filters[ i ];
					if ( filter.getState() == EnumState.CONNECTED )
						table.refresh();
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
		this.statusText = res.getString( "SVT_SERVERS" ) + servers.getConnected();
		this.mainWindow.statusline.update( this.statusText );
		this.mainWindow.statusline.updateToolTip( "" );
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
	public void widgetDisposed(DisposeEvent e) {
		this.core.getServerInfoIntMap().deleteObserver( this );
	}
	
	/**
	 * Adds a <code>ViewerFilter</code> to the <code>TableViewer</code> by
	 * removing all present <code>TabelMenuListener.NetworkFilter</code> 
	 * from the table.
	 * @param enum
	 */
	public void setFilter( NetworkInfo.Enum enum ) {
		ViewerFilter[] filters = table.getFilters();
		for ( int i = 0; i < filters.length; i++ ) {
			if ( filters[ i ] instanceof TableMenuListener.NetworkFilter )
				table.removeFilter( filters[ i ] );
		}
		table.addFilter( new TableMenuListener.NetworkFilter( enum ) );
	}
	
	/**
	 * Updates this tab on preference close
	 */
	public void updateDisplay() {
		/* update the state filter */
		if ( PreferenceLoader.loadBoolean( "displayAllServers" ) )
			table.addFilter( new TableMenuListener.EnumStateFilter( EnumState.CONNECTED ) );
		else {
			ViewerFilter[] filters = table.getFilters();
			for ( int i = 0; i < filters.length; i++ ) {
				TableMenuListener.EnumStateFilter filter = ( EnumStateFilter ) filters[ i ];
				if ( filter instanceof TableMenuListener.EnumStateFilter
				&& filter.getState() == EnumState.CONNECTED )
					table.removeFilter( filter ); 				
			}
		}
		super.updateDisplay();
	}
}

/*
$Log: ServerTab.java,v $
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