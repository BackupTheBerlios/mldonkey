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

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.model.Download;
import net.mldonkey.g2gui.model.ResultInfo;
import net.mldonkey.g2gui.model.ResultInfoIntMap;
import net.mldonkey.g2gui.view.MainTab;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
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
 * @version $Id: SearchResult.java,v 1.5 2003/07/27 18:45:47 lemmstercvs01 Exp $ 
 *
 */
//TODO search timeout, add resource bundle, add image handle, fake search, real links depending on network								   
public class SearchResult implements Observer, Runnable {
	private CTabFolder cTabFolder;
	private String searchString;
	private CoreCommunication core;
	private int searchId;
	private ResultInfoIntMap results;
	private TableViewer table;
	private Label label;
	private Image image;
	private CTabItem cTabItem;
	private TableColumn tableColumn;
	private boolean ascending = false;
	
	private ResourceBundle bundle = ResourceBundle.getBundle( "g2gui" );

	/* if you modify this, change the LayoutProvider and tableWidth */
	private String[] tableColumns = { bundle.getString( "SR_NETWORK" ), 
									   bundle.getString( "SR_NAME" ),
									   bundle.getString( "SR_SIZE" ),
									   bundle.getString( "SR_FORMAT" ),
									   bundle.getString( "SR_MEDIA" ),
									   bundle.getString( "SR_AVAIL" ), };
	/* 0 sets the tablewidth dynamcliy */
	private int[] tableWidth = { 45, 0, 65, 45, 50, 45 };
		
	/**
	 * Creates a new SearchResult to display all the results supplied by mldonkey
	 * @param aString The SearchString we are searching for
	 * @param parent The parent TabFolder, where we display our TabItem in
	 * @param core The core to communicate with
	 * @param searchId The identifier to this search
	 */
	protected SearchResult( String aString, CTabFolder parent, CoreCommunication core, int searchId ) {
		this.searchString = aString;
		this.cTabFolder = parent;
		this.searchId = searchId;
		this.core = core;
		
		/* draw the display */
		this.createContent();
		/* register ourself to the core */
		this.core.addObserver( this );
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
				cTabFolder.getDisplay().asyncExec( this );
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
			this.setColumnWidht();
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
	 * Display the string "searching..." for the time
	 * we are waiting for the resultinfo
	 */
	private void createContent() {
		/* first we need a CTabFolder item for the search result */
		cTabItem = new CTabItem( cTabFolder, SWT.FLAT );
		cTabItem.setText( searchString );
		cTabItem.setToolTipText( bundle.getString( "SR_SEARCHINGFOR" ) + searchString );
		image = new Image( cTabFolder.getDisplay(), "src/icons/search_small.png" );
		cTabItem.setImage( MainTab.createTransparentImage( image, cTabItem.getParent() ) );
		cTabItem.setData( this );

		/* for the search delay, just draw a label */ 
		label = new Label( cTabFolder, SWT.NONE );
		label.setText( bundle.getString( "SR_SEARCHING" ) );
		cTabItem.setControl( label );
		
		/* sets the tabitem on focus */
		cTabFolder.setSelection( cTabItem );
		
		/* listen for dispose to close this open search */
		cTabItem.addDisposeListener( new DisposeListener() {
			public void widgetDisposed( DisposeEvent e ) {
				( ( SearchResult ) cTabItem.getData() ).dispose();
			} 
		} );
	}

	/**
	 * Build the whole table and the tablecolumns
	 */
	private void createTable() {
		/* create the result table */		
		table = new TableViewer( cTabFolder, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI );
		table.getTable().setLayoutData( new GridData( GridData.FILL_BOTH ) );
		table.getTable().setLinesVisible( false );
		table.getTable().setHeaderVisible( true );
		table.getTable().setMenu( createRightClickMenu() );

		table.setContentProvider( new ResultTableContentProvider() );
		table.setLabelProvider( new ResultTableLabelProvider() );
		table.setSorter( new ResultTableSorter() );
		
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
					( ( ResultTableSorter ) table.getSorter() ).setColumnIndex( columnIndex );
					/* set the way to sort (ascending/descending) */
					( ( ResultTableSorter ) table.getSorter() ).setLastSort( ascending );

					/* get the data for all tableitems */
					TableItem[] items = table.getTable().getItems();
					ResultInfo[] temp = new ResultInfo[ items.length ];
					for ( int i = 0; i < items.length; i++ )
							temp[ i ] = ( ResultInfo ) items[ i ].getData();

					/* reverse sorting way */
					ascending = ascending ? false : true;

					table.getSorter().sort( table, temp );
					table.refresh();
				}	
			} );
		}

		/* add a resize listener */
		cTabFolder.addControlListener( new ControlAdapter() {
		public void controlResized( ControlEvent e ) {
				SearchResult.this.setColumnWidht();
			}
		} );
		
		
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
		final Clipboard cb = new Clipboard( shell.getDisplay() );
		Menu menu = new Menu( shell , SWT.POP_UP );

		MenuItem dlItem, rmItem, closeItem;
		final MenuItem cpFileItem, cpLinkItem, webItem;

		/* Download */
		dlItem = new MenuItem( menu, SWT.PUSH );
		dlItem.setText( bundle.getString( "ST_DOWNLOAD" ) );
		dlItem.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				TableItem[] currentItems = table.getTable().getSelection();
				for ( int i = 0; i < currentItems.length;	 i ++ ) {
					ResultInfo result = ( ResultInfo ) currentItems[ i ].getData();
					Download download = new Download( core );
					download.setPossibleNames( result.getNames() );	
					download.setResultID( result.getResultID() );
					download.setForce( false );
					download.send();
					download = null;
				}
			}
		} );
		
		new MenuItem( menu, SWT.SEPARATOR );
		
		/* copy filename */
		cpFileItem = new MenuItem( menu, SWT.PUSH );
		cpFileItem.setText( bundle.getString( "ST_COPYNAME" ) );
		cpFileItem.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				TableItem[] currentItems = table.getTable().getSelection();
				ResultInfo result = ( ResultInfo ) currentItems[ 0 ].getData();
				TextTransfer textTransfer = TextTransfer.getInstance();
				cb.setContents( new Object[] { result.getNames()[ 0 ] },
								new Transfer[] { textTransfer } );
			}
		} );
				
		/* Copy link as */
		cpLinkItem = new MenuItem( menu, SWT.CASCADE );
		cpLinkItem.setText( bundle.getString( "ST_COPYLINK" ) );
		Menu copy = new Menu( menu );
			MenuItem copyItem = new MenuItem( copy, SWT.PUSH );
			copyItem.setText( bundle.getString( "ST_ASPLAIN" ) );
			copyItem.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent e ) {
					TableItem[] currentItems = table.getTable().getSelection();
					ResultInfo result = ( ResultInfo ) currentItems[ 0 ].getData();
					TextTransfer textTransfer = TextTransfer.getInstance();
					cb.setContents( new Object[] { result.getLink() }, 
									new Transfer[] { textTransfer } );
				}
			} );
			copyItem = new MenuItem( copy, SWT.PUSH );
			copyItem.setText( bundle.getString( "ST_ASHTML" ) );
			copyItem.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent e ) {	
					TableItem[] currentItems = table.getTable().getSelection();
					ResultInfo result = ( ResultInfo ) currentItems[ 0 ].getData();
					String aString = "<a href=\"" + result.getLink() + "\">"
									 + result.getNames()[ 0 ] + "</a>";
					TextTransfer textTransfer = TextTransfer.getInstance();
					cb.setContents( new Object[] { aString }, 
									new Transfer[] { textTransfer } );
				}
			} );
		cpLinkItem.setMenu( copy );

		new MenuItem( menu, SWT.SEPARATOR );

		/* Webservices */
		webItem = new MenuItem( menu, SWT.CASCADE );
		webItem.setText( bundle.getString( "ST_WEBSERVICES" ) );
		Menu web = new Menu( menu );
			MenuItem webSItem = new MenuItem( web, SWT.PUSH );
			webSItem.setText( bundle.getString( "ST_WEBSERVICE1" ) );
			webSItem.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent e ) {
					//TODO add fake search
				}
			} );
			webSItem = new MenuItem( web, SWT.PUSH );
			webSItem.setText( bundle.getString( "ST_WEBSERVICE1" ) );
			webSItem.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent e ) {
					//TODO add fake search
				}
			} );
		webItem.setMenu( web );

		new MenuItem( menu, SWT.SEPARATOR );

		/* remove this item */
		rmItem = new MenuItem( menu, SWT.PUSH );
		rmItem.setText( bundle.getString( "ST_REMOVE" ) );
		rmItem.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				table.getTable().remove( table.getTable().getSelectionIndices() );
			}
		} );

		/* close this search */
		closeItem = new MenuItem( menu, SWT.PUSH );
		closeItem.setText( bundle.getString( "ST_CLOSE" ) );
		closeItem.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				cTabItem.dispose();
			}
		} );
		
		/* disable some items if currentItems.length > 1 */
		menu.addListener( SWT.Show, new Listener () {
			public void handleEvent( Event event ) {
				TableItem[] currentItems = table.getTable().getSelection();
				if ( currentItems.length > 1 ) {
					cpFileItem.setEnabled( false );
					cpLinkItem.setEnabled( false );
					webItem.setEnabled( false );	
				}
				else {
					cpFileItem.setEnabled( true );
					cpLinkItem.setEnabled( true );
					webItem.setEnabled( true );	
				}
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
			Program p;
			if ( !aResult.getFormat().equals( "" ) )
				p = Program.findProgram( aResult.getFormat() );
			else {
				String temp = aResult.getNames()[ 0 ];
				int index = temp.lastIndexOf( "." );
				try {
					temp = temp.substring( index );
				}
				catch ( Exception e ) {
					p = null;
				}
				p = Program.findProgram( temp );
			}
				
			if ( p != null ) {
				ImageData data = p.getImageData();
				if ( data != null ) {
					image = new Image( table.getTable().getDisplay(), data );
					item.setData( "TIP_IMAGE", image );
				}
			}
			
			/* sets the tooltip text */	
			String aString = bundle.getString( "ST_TT_NAME" ) + aResult.getNames()[ 0 ] + "\n";
			if ( !aResult.getFormat().equals( "" ) )
				aString += bundle.getString( "ST_TT_FORMAT" ) + aResult.getFormat() + "\n";
				aString += bundle.getString( "ST_TT_LINK" ) + aResult.getLink() + "\n";
				aString += bundle.getString( "ST_TT_NETWORK" )
												 + aResult.getNetwork().getNetworkName() + "\n";
				aString += bundle.getString( "ST_TT_SIZE" ) + aResult.getStringSize() + "\n";
				aString += bundle.getString( "ST_TT_SOURCES" ) + aResult.getTags()[ 0 ].getValue();
			if ( !aResult.getHistory() )
				aString = aString + "\n" + bundle.getString( "ST_TT_DOWNLOADED" );
			item.setData( "TIP_TEXT", aString );
		}
	}
	
	/**
	 * Sets the size for the columns
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
		columns[ 1 ].setWidth( totalWidth );
	}	
	
	/**
	 * dispose this search result
	 */
	public void dispose() {
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

					if ( widget == null ) {
						tipShell.setVisible( false );
						tipWidget = null;
					}

					if ( widget == tipWidget ) return;
					
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
			shellBounds.x = Math.max( Math.min( 
				position.x, displayBounds.width - shellBounds.width ), 0 );
			shellBounds.y = Math.max( Math.min( 
				position.y + 16, displayBounds.height - shellBounds.height ), 0 );
			shell.setBounds( shellBounds );
		}
	}
}

/*
$Log: SearchResult.java,v $
Revision 1.5  2003/07/27 18:45:47  lemmstercvs01
lots of changes

Revision 1.4  2003/07/27 16:38:41  vnc
cosmetics in tooltip

Revision 1.3  2003/07/25 22:34:51  lemmstercvs01
lots of changes

Revision 1.2  2003/07/24 16:20:10  lemmstercvs01
lots of changes

Revision 1.1  2003/07/23 16:56:28  lemmstercvs01
initial commit

*/