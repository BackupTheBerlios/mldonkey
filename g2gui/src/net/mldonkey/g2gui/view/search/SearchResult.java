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
package net.mldonkey.g2gui.view.search;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.model.Download;
import net.mldonkey.g2gui.model.ResultInfo;
import net.mldonkey.g2gui.model.ResultInfoIntMap;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolderAdapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

/**
 * SearchResult
 *
 * @author $user$
 * @version $Id: SearchResult.java,v 1.2 2003/07/24 16:20:10 lemmstercvs01 Exp $ 
 *
 */
//TODO search timeout, add resource bundle, select importand columns, fake search								   
//TODO "button 3" copy link to clipboard, copy filename to clipboard, close this tab, copy link as html
public class SearchResult implements Observer, Runnable {
	private Composite composite;
	private String searchString;
	private CoreCommunication core;
	private int searchId;
	private int lastSortColumn = -1;
	
	private ResultInfoIntMap results;
	private ResultInfo currentResult;
	
	private TableViewer table;
	private Label label;
	private Image image;
	private CTabItem cTabItem;
	private TableColumn tableColumn;
	/* if you modify this, change the LayoutProvider and ResultComparator as well */
	private String[] tableColumns = { "Network", 
									   "Name",
									   "Size",
									   "Format",
									   "Type",
									   "Avail" };
		
	/**
	 * 
	 * @param aString
	 * @param parent
	 */
	protected SearchResult( String aString, Composite parent, CoreCommunication core, int searchId ) {
		this.searchString = aString;
		this.composite = parent;
		this.searchId = searchId;
		this.core = core;
		
		/* draw the display */
		this.createContent();
		/* register ourself to the core */
		this.core.addObserver( this );
	}
	
	/**
	 * Display the string "searching..." for the time
	 * we are waiting for the resultinfo
	 */
	private void createContent() {
		/* first we need a CTabFolder item for the search result */
		cTabItem = new CTabItem( ( CTabFolder ) composite, SWT.FLAT );
		cTabItem.setText( searchString );
		cTabItem.setToolTipText( "searching for " + searchString );
		image = new Image( composite.getDisplay(), "src/icons/search_small.png" );
		cTabItem.setImage( image );

		( ( CTabFolder ) composite ).addCTabFolderListener( new CTabFolderAdapter() {
			public void itemClosed( CTabFolderEvent event ) {
				SearchResult.this.dispose();
				/* close the tab item */
				event.item.dispose();
			}
		} );
		
		/* listen for dispose to close all open searches */
		composite.addDisposeListener( new DisposeListener() {
			public void widgetDisposed( DisposeEvent e ) {
				SearchResult.this.dispose();

				/* dispose the image */
				image.dispose();
			} 
		} );

		/* for the search delay, just draw a label */ 
		label = new Label( composite, SWT.NONE );
		label.setText( "searching..." );
		cTabItem.setControl( label );
		
		/* sets the tabitem on focus */
		( ( CTabFolder ) composite ).setSelection( cTabItem );
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update( Observable o, final Object arg ) {
		/* if the tab is already disposed, dont update */
		if ( cTabItem.isDisposed() ) return;
		
		/* are we responsible for this update */
		if ( arg instanceof ResultInfoIntMap 
		&& ( ( ResultInfoIntMap ) arg ).containsKey( searchId ) ) {
			 	this.results = ( ResultInfoIntMap ) arg;
				composite.getDisplay().asyncExec( this );
		}
	}
	
	/* (  non-Javadoc  )
	 * @see java.util.Observer#update(  java.util.Observable, java.lang.Object  )
	 */
	public void run() {
		/* if the tab is already disposed, dont update */
		if ( cTabItem.isDisposed() ) return;

			if ( table == null ) {
				/* remove the old label "searching..." */
				label.dispose();
			
				this.createTable();
			
				/* fill the table with content */
				table.setInput(  this.results.get( searchId ) );
				this.modifiyItems();
			} 
			else {
				/* has our result changed */
				int temp = ( ( List ) results.get( searchId ) ).size();
				if ( table.getTable().getItemCount() != temp ) {
					table.refresh();
					this.modifiyItems();
				}
			}
	}
	
	/**
	 * unregister ourself (Observer) by the core
	 */
	private void unregister() {
		this.core.deleteObserver( this );
	}
	
	/**
	 * Build the whole table and the tablecolumns
	 */
	private void createTable() {
		/* create the result table */		
		table = new TableViewer( composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI );
		table.getTable().setLayoutData( new GridData( GridData.FILL_BOTH ) );
		table.getTable().setLinesVisible( false );
		table.getTable().setHeaderVisible( true );
		table.getTable().setMenu( createRightClickMenu() );

		table.setContentProvider( new ResultTableContentProvider() );
		table.setLabelProvider( new ResultTableLabelProvider() );

		/* listen for right mouse click on item */
		table.getTable().addMouseListener( new MouseListener () {
			public void mouseDown( MouseEvent e ) {
				if ( e.button == 3 ) {
					Point pt = new Point( e.x, e.y );
					/* is a tableitem selected */
					if ( table.getTable().getItem( pt ) != null )
						currentResult = ( ResultInfo ) table.getTable().getItem( pt ).getData();
				}
			}
			public void mouseDoubleClick( MouseEvent e ) { }
			public void mouseUp( MouseEvent e ) { }
		} );
				
		/* create the columns */
		for ( int i = 0; i < tableColumns.length; i++ ) {
			tableColumn = new TableColumn( table.getTable(), SWT.LEFT );
			tableColumn.setText( tableColumns[ i ] );
			tableColumn.pack();
	
			/* listen for table sorting */
			final int columnIndex = i;
			tableColumn.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent e ) {
					sort( columnIndex );
				}
			} );
		}

		final ToolTipHandler tooltip = new ToolTipHandler( table.getTable().getShell() );
		tooltip.activateHoverHelp( table.getTable() );
		
		/* set the this table as the new CTabItem Control */
		cTabItem.setControl( table.getTable() );
	}
	
	/**
	 * Creates the "button 3" menu
	 * @return The menu added to the table
	 */
	private Menu createRightClickMenu() {
		Shell shell = table.getTable().getShell();
		Menu menu = new Menu( shell , SWT.POP_UP );

		MenuItem menuItem;
		/* Download */
		menuItem = new MenuItem( menu, SWT.PUSH );
		menuItem.setText( "download" );
		menuItem.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				Download download = new Download( core );
				download.setPossibleNames( currentResult.getNames() );
				download.setResultID( currentResult.getResultID() );
				download.setForce( false );
				download.send();
				download = null;
			}
		} );
		return menu;
	}
	
	/**
	 * Sets the foreground color of an item to green if its already downloaded,
	 * sets some data for the tooltiptext
	 */
	private void modifiyItems() {

		TableItem[] items = table.getTable().getItems();
		for ( int i = 0; i < items.length; i ++ ) {
			TableItem item = items[ i ];
			ResultInfo aResult = ( ( ResultInfo ) items[ i ].getData() );

			/* sets the color for each table item depending on its history */
			if ( !aResult.getHistory() )
				item.setForeground( new Color( items[ 0 ].getDisplay(), 41, 174, 57 ) );

			/* sets the tooltip image */
			Program p = Program.findProgram( aResult.getFormat() );
			if ( p != null ) {
				ImageData data = p.getImageData();
				if ( data != null ) {
					image = new Image( table.getTable().getDisplay(), data );
					item.setData( "TIP_IMAGE", image );
				}
			}
			
			/* sets the tooltip text */	
			String aString = "Filename: " + aResult.getNames()[ 0 ] + "\n";
			if ( !aResult.getFormat().equals( "" ) )
				aString = aString + "Format: " + aResult.getFormat() + "\n";
			aString = aString + "ed2k-link: " + "ed2k://|file|" + aResult.getNames()[ 0 ] + "|" + aResult.getSize() + "|" + aResult.getMd4() + "|/\n";
			aString = aString + "Network: " + aResult.getNetwork().getNetworkName() + "\n";
			aString = aString + "Size: " + aResult.getStringSize() + "\n";
			aString = aString + "Sources: " + aResult.getTags()[ 0 ].getValue();
			if ( !aResult.getHistory() )
				aString = aString + "You downloaded this file already";
			item.setData( "TIP_TEXT", aString );
		}
	}
	
	/**
	 * Sort a Column
	 * @param columnIndex The column to sort
	 */
	private void sort( int columnIndex ) {
		if ( table.getTable().getItemCount() <= 1 ) return;

		TableItem[] items = table.getTable().getItems();
		String[][] data = new String[ items.length ][ table.getTable().getColumnCount() ];
		for ( int i = 0; i < items.length; i++ ) {
			for ( int j = 0; j < table.getTable().getColumnCount(); j++ ) {
				data[ i ][ j ] = items[ i ].getText( j );
			}
		}
	
		Arrays.sort( data, new ResultComparator( columnIndex ) );
	
		if ( lastSortColumn != columnIndex ) {
			for ( int i = 0; i < data.length; i++ ) {
				items[ i ].setText( data[ i ] );
			}
			lastSortColumn = columnIndex;
		} else {
			// reverse order if the current column is selected again
			int j = data.length - 1;
			for ( int i = 0; i < data.length; i++ ) {
				items[i].setText( data[ j-- ] );
			}
			lastSortColumn = -1;
		}
	}	
		
	
	/**
	 * dispose this search result
	 */
	private void dispose() {
		/* tell the core to forget the search */
		Object[] temp = { new Integer( searchId ), new Byte( ( byte ) 1 ) };
		EncodeMessage message = new EncodeMessage( Message.S_CLOSE_SEARCH, temp );
		message.sendMessage( this.core.getConnection() );
		message = null;
					
		/* we want no longer receive result for this search */
		this.unregister();
	}
	
	/**
	 * Emulated tooltip handler
	 * Notice that we could display anything in a tooltip besides text and images.
	 * For instance, it might make sense to embed large tables of data or buttons linking
	 * data under inspection to material elsewhere, or perform dynamic lookup for creating
	 * tooltip text on the fly.
	 */
	protected static class ToolTipHandler {
		private Shell  tipShell;
		private Label  tipLabelImage, tipLabelText;
		private Widget tipWidget; // widget this tooltip is hovering over
		private Point  tipPosition; // the position being hovered over
	
		/**
		 * Creates a new tooltip handler
		 *
		 * @param parent the parent Shell
		 */	
		public ToolTipHandler( Composite parent ) {
			final Display display = parent.getDisplay();
	
			tipShell = new Shell( parent.getShell(), SWT.ON_TOP );
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			gridLayout.marginWidth = 2;
			gridLayout.marginHeight = 2;
			tipShell.setLayout( gridLayout );
	
			tipShell.setBackground( display.getSystemColor( SWT.COLOR_INFO_BACKGROUND ) );
			
			tipLabelImage = new Label( tipShell, SWT.NONE );
			tipLabelImage.setForeground( display.getSystemColor( SWT.COLOR_INFO_FOREGROUND ) );
			tipLabelImage.setBackground( display.getSystemColor( SWT.COLOR_INFO_BACKGROUND ) );
			tipLabelImage.setLayoutData( 
				new GridData( GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER ) );
	
			tipLabelText = new Label( tipShell, SWT.NONE );
			tipLabelText.setForeground( display.getSystemColor( SWT.COLOR_INFO_FOREGROUND ) );
			tipLabelText.setBackground( display.getSystemColor( SWT.COLOR_INFO_BACKGROUND ) );
			tipLabelText.setLayoutData( new GridData( 
				GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER ) );
		}
	
		/**
		 * Enables customized hover help for a specified control
		 * 
		 * @control the control on which to enable hoverhelp
		 */
		public void activateHoverHelp( final Control control ) {
			/*
			 * Get out of the way if we attempt to activate the control underneath the tooltip
			 */
			control.addMouseListener( new MouseAdapter () {
				public void mouseDown ( MouseEvent e ) {
					if ( tipShell.isVisible() ) tipShell.setVisible( false );
				}	
			} );
	
			/*
			 * Trap hover events to pop-up tooltip
			 */
			control.addMouseTrackListener( new MouseTrackAdapter () {
				public void mouseExit( MouseEvent e ) {
					if ( tipShell.isVisible() ) tipShell.setVisible( false );
					tipWidget = null;
				}
				public void mouseHover( MouseEvent event ) {
					Point pt = new Point( event.x, event.y );
					Widget widget = event.widget;

					/* get the selected item from the table */
					Table w = ( Table ) widget;
					widget = w.getItem ( pt );

					if ( widget == tipWidget || widget == null ) return;
					tipWidget = widget;

					/* get the data from the widget */					
					String text = ( String ) widget.getData( "TIP_TEXT" );
					Image image = ( Image ) widget.getData( "TIP_IMAGE" );
					
					/* set the text/image for the tooltip */
					if ( text != null )
						tipLabelText.setText( text );
					else
						tipLabelText.setText( "" );
					tipLabelImage.setImage( image ); // accepts null
					
					/* pack/layout the tooltip */
					tipShell.pack();
					tipPosition = control.toDisplay( pt );
					setHoverLocation( tipShell, tipPosition );
					
					tipShell.setVisible( true );
				}
			} );
		}
		
		/**
		 * Sets the location for a hovering shell
		 * @param shell the object that is to hover
		 * @param position the position of a widget to hover over
		 * @return the top-left location for a hovering box
		 */
		private void setHoverLocation( Shell shell, Point position ) {
			Rectangle displayBounds = shell.getDisplay().getBounds();
			Rectangle shellBounds = shell.getBounds();
			shellBounds.x = Math.max( Math.min( position.x, displayBounds.width - shellBounds.width ), 0 );
			shellBounds.y = Math.max( Math.min( position.y + 16, displayBounds.height - shellBounds.height ), 0 );
			shell.setBounds( shellBounds );
		}
	}
	
	/**
	 * To compare entries (rows) by the given column
	 */
	protected static class ResultComparator implements Comparator {
		private int column;

		/**
		 * @param columnIndex the column, which is responsible for the search order
		 */
		ResultComparator( int columnIndex ) {
			this.column = columnIndex ;
		}
		/**
		 * Compares two rows (type String[]) using the specified
		 * column entry.
		 * @param obj1 First row to compare
		 * @param obj2 Second row to compare
		 * @return negative if obj1 less than obj2, positive if
		 * 			obj1 greater than obj2, and zero if equal.
		 */
		public int compare( Object obj1, Object obj2 ) {
			String[] row1 = ( String[] ) obj1;
			String[] row2 = ( String[] ) obj2;
		
			return row1[ column ].compareTo( row2[ column ] );
		}
	}	
}

/*
$Log: SearchResult.java,v $
Revision 1.2  2003/07/24 16:20:10  lemmstercvs01
lots of changes

Revision 1.1  2003/07/23 16:56:28  lemmstercvs01
initial commit

*/