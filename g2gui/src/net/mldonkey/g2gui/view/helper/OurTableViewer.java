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
package net.mldonkey.g2gui.view.helper;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.transfer.CustomTableViewer;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableColumn;

/**
 * OurTableViewer
 *
 * @version $Id: OurTableViewer.java,v 1.1 2003/10/21 17:00:45 lemmster Exp $ 
 *
 */
public abstract class OurTableViewer {
	private boolean isGTK = SWT.getPlatform().equals("gtk");

	/**
	 * 
	 */
	protected TableViewer tableViewer;
	protected CoreCommunication core;
	protected Composite composite;

	/**
	 * 
	 */
	protected String[] tableColumns;
	protected int[] tableWidth;
	protected int[] tableAlign;
	protected int swtLayout;
	protected IContentProvider contentProvider;
	protected IBaseLabelProvider labelProvider;
	protected OurTableSorter tableSorter;
	protected TableMenuListener menuListener;

	/**
	 * 
	 */
	protected TableColumn tableColumn;
	protected boolean ascending = false;;

	/**
	 * 
	 * OurTableViewer
	 *
	 */
	public OurTableViewer( Composite aComposite, CoreCommunication aCore ) {
		this.composite = aComposite;
		this.core = aCore;
	}
	
	/**
	 * 
	 *
	 */
	protected void create() {
		// low level table options
		tableViewer = new CustomTableViewer( composite, swtLayout );
		tableViewer.getTable().setLayoutData( new GridData( GridData.FILL_BOTH ) );
		tableViewer.getTable().setLinesVisible( true );
		tableViewer.getTable().setHeaderVisible( true );
		tableViewer.setUseHashlookup( true );
		
		// ContentProvider
		tableViewer.setContentProvider( contentProvider );
		// Label Provider
		tableViewer.setLabelProvider( labelProvider );
		// TableSorter
		tableViewer.setSorter( tableSorter );
		// MenuListener
		menuListener.setTableViewer( tableViewer );		
		tableViewer.addSelectionChangedListener( menuListener );
		MenuManager popupMenu = new MenuManager( "" );
		popupMenu.setRemoveAllWhenShown( true );
		popupMenu.addMenuListener( menuListener );
		tableViewer.getTable().setMenu( popupMenu.createContextMenu( tableViewer.getTable() ) );

		// catch dispose events to save sorter order/sorter column
		tableViewer.getTable().addDisposeListener( new DisposeListener() {
			public synchronized void widgetDisposed( DisposeEvent e ) {
				( ( OurTableSorter ) tableViewer.getSorter() ).widgetDisposed( null );
			}
		} );
	}

	/**
	 * 
	 *
	 */
	protected void createTableColumns() {
		int w;
		for ( int i = 0; i < tableColumns.length; i++ ) {
			final PreferenceStore prefStore = PreferenceLoader.getPreferenceStore();
			prefStore.setDefault( tableColumns[ i ], tableWidth[ i ] );
			tableColumn = new TableColumn( tableViewer.getTable(), tableAlign[ i ] );
			tableColumn.setText( G2GuiResources.getString( tableColumns[ i ] ) );
			
			// gtk renders an error when setWidth == 0
			w = prefStore.getInt( tableColumns[ i ] );
			tableColumn.setWidth( ( ( w == 0 && isGTK ) ? ( tableWidth[ i ] == 0 ? 50 : tableWidth [ i ] ) : w ) );
			
			/* on dispose read the new tablewidth to the prefstore */
			final int j = i;
			tableColumn.addDisposeListener( new DisposeListener() {
				public synchronized void widgetDisposed( DisposeEvent e ) {
					// tablewidth
					TableColumn aColumn = ( TableColumn ) e.widget;
					prefStore.setValue( tableColumns[ j ] , aColumn.getWidth() );
				}
			} );

			/* adds a sort listener */
			final int columnIndex = i;
			tableColumn.addSelectionListener( new SelectionListener() {
				public void widgetSelected( SelectionEvent e ) {
					( ( OurTableSorter ) getTableViewer().getSorter() ).setColumnIndex( columnIndex );
					getTableViewer().refresh();
				}
				public void widgetDefaultSelected( SelectionEvent e ) { }
			} );
		}
	}
	
	protected void addMenuListener() {
		/*
		 * add a menulistener to set the first item to default
		 * sadly not possible with the MenuManager Class
		 * (Feature Request on eclipse?)
		 */
		Menu menu = tableViewer.getTable().getMenu();
		menu.addMenuListener( new MenuListener() {
				public void menuShown( MenuEvent e ) {
					Menu menu = tableViewer.getTable().getMenu();
					if ( !tableViewer.getSelection().isEmpty() )
						menu.setDefaultItem( menu.getItem( 0 ) );
				}

				public void menuHidden( MenuEvent e ) {
				}
			} );
	}
	
	/**
	 * @param obj
	 */
	public void setInput( Object obj ) {
		this.tableViewer.setInput( obj );
	}
	
	/**
	 * @return
	 */
	public TableViewer getTableViewer() {
		return tableViewer;
	}
}

/*
$Log: OurTableViewer.java,v $
Revision 1.1  2003/10/21 17:00:45  lemmster
class hierarchy for tableviewer

*/