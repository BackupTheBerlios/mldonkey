/*
 * Copyright 2003
 * g2gui Team
 *
 *
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package net.mldonkey.g2gui.view.transfer.clientTable;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.transfer.CustomTableViewer;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;


/**
 * ClientTableViewer
 *
 * @version $Id: ClientTableViewer.java,v 1.5 2003/10/19 21:38:54 zet Exp $
 *
 */
public class ClientTableViewer {
	public static final String ALL_COLUMNS = "ABCD";
	
	public static final int STATE = 0;
	public static final int NAME = 1;
	public static final int NETWORK = 2;
	public static final int KIND = 3;
	
	public boolean manualDispose;
    public static final String[] COLUMN_LABELS = { "TT_CT_STATE", "TT_CT_NAME", "TT_CT_NETWORK", "TT_CT_KIND" };
    final int[] COLUMN_ALIGNMENT = { SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.LEFT };
    final int[] COLUMN_DEFAULT_WIDTHS = { 200, 100, 75, 75 };
    private CustomTableViewer tableViewer;
    private CoreCommunication core;
	private String columnIDs;

    public ClientTableViewer( Composite parent, CoreCommunication core ) {
        this.core = core;
        createContents( parent );
    }

    public void createContents( Composite parent ) {
        tableViewer = new CustomTableViewer( parent, SWT.FULL_SELECTION | SWT.MULTI );

		Table table = tableViewer.getTable();
        updateDisplay();
        table.setLayoutData( new GridData( GridData.FILL_BOTH ) );
        tableViewer.getTable().setLinesVisible( PreferenceLoader.loadBoolean( "displayGridLines" ) );
        tableViewer.getTable().setHeaderVisible( true );

        createColumns();

        tableViewer.setContentProvider( new ClientTableContentProvider() );
        tableViewer.setLabelProvider( new ClientTableLabelProvider( tableViewer ) );

        ClientTableMenuListener tableMenuListener = new ClientTableMenuListener( tableViewer, core );
        tableViewer.addSelectionChangedListener( tableMenuListener );

        MenuManager popupMenu = new MenuManager( "" );
        popupMenu.setRemoveAllWhenShown( true );
        popupMenu.addMenuListener( tableMenuListener );
        tableViewer.getTable().setMenu( popupMenu.createContextMenu( tableViewer.getTable() ) );
        tableViewer.setSorter( new ClientTableSorter( tableViewer ) );
    }

    public CustomTableViewer getTableViewer() {
        return tableViewer;
    }
    
    public void createColumns() {
		Table table = tableViewer.getTable();
		TableColumn[] tableColumns = table.getColumns();
		
		manualDispose = true;
		for (int i = tableColumns.length - 1; i > -1; i--) {
			tableColumns[i].dispose();
		}
		manualDispose = false;

		String prefCols = PreferenceLoader.loadString( "clientTableColumns" );
		columnIDs = ( ( !prefCols.equals( "" )  && prefCols.length() <= ALL_COLUMNS.length() ) ? prefCols : ALL_COLUMNS );
		final PreferenceStore p = PreferenceLoader.getPreferenceStore();
		
		tableViewer.setColumnIDs( columnIDs );
		
		for ( int i = 0; i < columnIDs.length(); i++ ) {
		    
			final int arrayItem = columnIDs.charAt( i ) - 65;
			final int columnIndex = i;
			 
			TableColumn tableColumn = new TableColumn( table, COLUMN_ALIGNMENT[ arrayItem ] );
			p.setDefault( COLUMN_LABELS[ arrayItem ], COLUMN_DEFAULT_WIDTHS[ arrayItem ] );
			tableColumn.setText( G2GuiResources.getString( COLUMN_LABELS[ arrayItem ] ) );
			tableColumn.setWidth( p.getInt( COLUMN_LABELS[ arrayItem ] ) );
			
			 tableColumn.addDisposeListener( new DisposeListener() {
					 public synchronized void widgetDisposed( DisposeEvent e ) {
						 TableColumn thisColumn = (TableColumn) e.widget;
						 if (!manualDispose)
						 p.setValue( COLUMN_LABELS[ arrayItem ], thisColumn.getWidth() );
					 }
				 } );
			 tableColumn.addListener( SWT.Selection,
				 new Listener() {
					 public void handleEvent( Event e ) {
						 ( (ClientTableSorter) tableViewer.getSorter() ).setColumnIndex( columnIndex );
						 tableViewer.refresh();
					 }
				 } );
		 }
    }
	/**
	 * Reset columns
	 */
	public void resetColumns() {
		Object oldInput = tableViewer.getInput();
		tableViewer.setInput( null );
		( (ClientTableSorter) tableViewer.getSorter()).setColumnIndex( 0 );
		createColumns();
		tableViewer.setInput(oldInput);
		updateDisplay();
	}
    
    public void updateDisplay() {
    	Table table = tableViewer.getTable();
		table.setLinesVisible( PreferenceLoader.loadBoolean( "displayGridLines" ) );
		table.setBackground( PreferenceLoader.loadColour( "downloadsBackgroundColor" ) );
		table.setForeground( PreferenceLoader.loadColour( "downloadsAvailableColor" ) );
		table.setFont( PreferenceLoader.loadFont( "downloadsFontData" ) );
    }
    
}


/*
$Log: ClientTableViewer.java,v $
Revision 1.5  2003/10/19 21:38:54  zet
columnselector support

Revision 1.4  2003/10/16 16:09:56  zet
updateDisplay

Revision 1.3  2003/10/12 15:58:30  zet
rewrite downloads table & more..

Revision 1.2  2003/09/21 23:39:31  zet
displayTableColors preference

Revision 1.1  2003/09/20 14:39:21  zet
move transfer package

*/
