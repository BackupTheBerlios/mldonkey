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
 * @version $Id: ClientTableViewer.java,v 1.3 2003/10/12 15:58:30 zet Exp $
 *
 */
public class ClientTableViewer {
    final String[] COLUMN_LABELS = { "TT_CT_STATE", "TT_CT_NAME", "TT_CT_NETWORK", "TT_CT_KIND" };
    final int[] COLUMN_ALIGNMENT = { SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.LEFT };
    final int[] COLUMN_DEFAULT_WIDTHS = { 200, 100, 75, 75 };
    private CustomTableViewer clientTableViewer;
    private CoreCommunication core;

    public ClientTableViewer( Composite parent, CoreCommunication core ) {
        this.core = core;
        createContents( parent );
    }

    public void createContents( Composite parent ) {
        clientTableViewer = new CustomTableViewer( parent, SWT.FULL_SELECTION | SWT.MULTI );

        Table table = clientTableViewer.getTable();
        table.setLayoutData( new GridData( GridData.FILL_BOTH ) );
        clientTableViewer.getTable().setLinesVisible( PreferenceLoader.loadBoolean( "displayGridLines" ) );
        clientTableViewer.getTable().setHeaderVisible( true );

        for ( int i = 0; i < COLUMN_LABELS.length; i++ ) {
            PreferenceStore p = PreferenceLoader.getPreferenceStore();
            p.setDefault( COLUMN_LABELS[ i ], COLUMN_DEFAULT_WIDTHS[ i ] );

            TableColumn tableColumn = new TableColumn( table, COLUMN_ALIGNMENT[ i ] );
            tableColumn.setText( G2GuiResources.getString( COLUMN_LABELS[ i ] ) );
            tableColumn.setWidth( p.getInt( COLUMN_LABELS[ i ] ) );

            final int columnIndex = i;
            tableColumn.addDisposeListener( new DisposeListener() {
                    public synchronized void widgetDisposed( DisposeEvent e ) {
                        PreferenceStore p = PreferenceLoader.getPreferenceStore();
                        TableColumn thisColumn = (TableColumn) e.widget;
                        p.setValue( COLUMN_LABELS[ columnIndex ], thisColumn.getWidth() );
                    }
                } );
            tableColumn.addListener( SWT.Selection,
                new Listener() {
                    public void handleEvent( Event e ) {
                        ( (ClientTableSorter) clientTableViewer.getSorter() ).setColumnIndex( columnIndex );
                        clientTableViewer.refresh();
                    }
                } );
        }

        clientTableViewer.setContentProvider( new ClientTableContentProvider() );
        clientTableViewer.setLabelProvider( new ClientTableLabelProvider() );

        ClientTableMenuListener tableMenuListener = new ClientTableMenuListener( clientTableViewer, core );
        clientTableViewer.addSelectionChangedListener( tableMenuListener );

        MenuManager popupMenu = new MenuManager( "" );
        popupMenu.setRemoveAllWhenShown( true );
        popupMenu.addMenuListener( tableMenuListener );
        clientTableViewer.getTable().setMenu( popupMenu.createContextMenu( clientTableViewer.getTable() ) );
        clientTableViewer.setSorter( new ClientTableSorter() );
    }

    public CustomTableViewer getTableViewer() {
        return clientTableViewer;
    }
}


/*
$Log: ClientTableViewer.java,v $
Revision 1.3  2003/10/12 15:58:30  zet
rewrite downloads table & more..

Revision 1.2  2003/09/21 23:39:31  zet
displayTableColors preference

Revision 1.1  2003/09/20 14:39:21  zet
move transfer package

*/
