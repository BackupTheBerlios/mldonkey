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

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.ServerInfo;
import net.mldonkey.g2gui.model.ServerInfoIntMap;
import net.mldonkey.g2gui.view.server.TableContentProvider;
import net.mldonkey.g2gui.view.server.TableLabelProvider;
import net.mldonkey.g2gui.view.server.TableMenuListener;
import net.mldonkey.g2gui.view.server.TableSorter;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
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
 * @author $user$
 * @version $Id: ServerTab.java,v 1.1 2003/08/05 13:50:10 lemmstercvs01 Exp $ 
 *
 */
public class ServerTab extends GuiTab implements Runnable {
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
	
	private ResourceBundle bundle = ResourceBundle.getBundle( "g2gui" );

	/* if you modify this, change the LayoutProvider and tableWidth */
	private String[] tableColumns = { "network", "name", "desc", "address", "port", "serverScore", "users", "files", "state", "favorite" };
	/* 0 sets the tablewidth dynamcliy */
	private int[] tableWidth = { 45, 120, 0, 95, 55, 55, 45, 50, 80, 50 };
	
	/**
	 * @param gui The main gui tab
	 */
	public ServerTab( MainTab gui ) {
		super( gui );
		/* associate this tab with the corecommunication */
		this.core = gui.getCore();
		/* Set our name on the coolbar */
		createButton( "ServerButton", 
							"Server",
							"Server tab" );

		/* create the tab content */
		this.createContents( this.content );
		
		this.core.getServerInfoIntMap().addObserver( this );
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
		table.setInput(  this.core.getServerInfoIntMap() );
		this.statusText = "Server: " + table.getTable().getItemCount();
		this.setColumnWidht();
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
		/* are we responsible for this update */
		if ( arg instanceof ServerInfoIntMap ) {
			this.servers = ( ServerInfoIntMap ) arg;
			this.content.getDisplay().asyncExec( this );
		}
	}		

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		int itemCount = table.getTable().getItemCount();
		if ( itemCount != this.servers.size() ) {
			table.refresh();
			this.statusText = "Result: " + itemCount;
			this.mainWindow.statusline.update( statusText );
			this.mainWindow.statusline.updateToolTip( "" );
		}
		else {
			List changed = this.servers.getChanged();
			synchronized ( changed ) {
				Iterator itr = changed.iterator();
				int size = changed.size();
				for ( ; size > 0; size-- ) {
					ServerInfo server = ( ServerInfo ) itr.next();
					table.update( server, null );
				}
				this.servers.clearChanged();
			}	
		}
	}
	
	/**
	 * @return The text this tab wants to display in the statusline
	 */
	public String getStatusText() {
		return this.statusText;
	}
}

/*
$Log: ServerTab.java,v $
Revision 1.1  2003/08/05 13:50:10  lemmstercvs01
initial commit

*/