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

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.model.Download;
import net.mldonkey.g2gui.model.ResultInfo;
import net.mldonkey.g2gui.model.ResultInfoIntMap;
import net.mldonkey.g2gui.view.MainTab;
import net.mldonkey.g2gui.view.SearchTab;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.transferTree.CustomTableViewer;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackAdapter;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

/**
 * SearchResult
 *
 * @author $Author: lemmster $
 * @version $Id: SearchResult.java,v 1.26 2003/08/23 14:58:38 lemmster Exp $ 
 *
 */
public class SearchResult implements Observer, Runnable, DisposeListener {	
	private MainTab mainTab;
	private CTabFolder cTabFolder;
	private String searchString;
	private CoreCommunication core;
	private int searchId;
	private ResultInfoIntMap results;
	private CustomTableViewer table;
	private Label label;
	private CTabItem cTabItem;
	private TableColumn tableColumn;
	private String statusline;
	private ResultTableSorter resultTableSorter = new ResultTableSorter();
	
	private int count = 0;
	
	/* if you modify this, change the LayoutProvider and tableWidth */
	private String[] tableColumns = { G2GuiResources.getString( "SR_NETWORK" ), 
									   G2GuiResources.getString( "SR_NAME" ),
									   G2GuiResources.getString( "SR_SIZE" ),
									   G2GuiResources.getString( "SR_FORMAT" ),
									   G2GuiResources.getString( "SR_MEDIA" ),
									   G2GuiResources.getString( "SR_AVAIL" ), };
	/* 0 sets the tablewidth dynamcliy */
	private int[] tableWidth = { 45, 0, 65, 45, 50, 45 };
		
	/**
	 * Creates a new SearchResult to display all the results supplied by mldonkey
	 * @param aString The SearchString we are searching for
	 * @param parent The parent TabFolder, where we display our TabItem in
	 * @param core The core to communicate with
	 * @param searchId The identifier to this search
	 */
	protected SearchResult( String aString, CTabFolder parent,
							 CoreCommunication theCore, int searchId )
	{
								 	
		this.searchString = aString;
		this.cTabFolder = parent;
		this.searchId = searchId;
		core = theCore;
	
		/* draw the display */
		this.createContent();
		/* register ourself to the core */
		core.getResultInfoIntMap().addObserver( this );
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update( Observable o, final Object arg ) {
		/* if the tab is already disposed, dont update */
		if ( cTabItem.isDisposed() ) return;
		
		/* are we responsible for this update */		
		
		if ( ( ( ResultInfoIntMap )arg ).containsKey( searchId ) ) {
			 	this.results = ( ResultInfoIntMap )arg;			 	
				cTabFolder.getDisplay().asyncExec( this );
		}
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void run() {
		/* if the tab is already disposed, dont update */
		if ( cTabItem.isDisposed() ) return;
		
			List list = ( List ) results.get( searchId );
			if ( table == null ) {
				/* remove the old label "searching..." */
				label.dispose();
				this.createTable();
				table.setInput( list );				
				//this.modifyItems();
				this.setColumnWidth();
			} 
			else {
				/*
				 * has our result changed: 				
				 * only refresh the changed items, 
				 * look at API for refresh(false)
				 */		
				if ( list.size() != table.getTable().getItemCount() ) {									
					table.refresh( true );
				} 					
					
			}
		/* are we active? set the statusline text */
		if ( cTabFolder.getSelection() == cTabItem ) {
			SearchTab parent = ( SearchTab ) cTabFolder.getData();
			int itemCount = table.getTable().getItemCount();
			this.statusline = "Results: " + itemCount;
			parent.setRightLabel( "Results: " + itemCount );
			parent.getMainTab().getStatusline().update( this.statusline );
		}
	}
	
	/**
	 * unregister ourself (Observer) by the core
	 */
	private void unregister() {
		core.deleteObserver( this );
	}
	
	/**
	 * Display the string "searching..." for the time
	 * we are waiting for the resultinfo
	 */
	private void createContent() {
		/* first we need a CTabFolder item for the search result */		
		cTabItem = new CTabItem( cTabFolder, SWT.FLAT );
		cTabItem.setText( searchString );
		cTabItem.setToolTipText( G2GuiResources.getString( "SR_SEARCHINGFOR" ) + searchString );
		cTabItem.setImage( G2GuiResources.getImage( "SearchSmall" ) );
		cTabItem.setData( this );

		/* for the search delay, just draw a label */ 
		label = new Label( cTabFolder, SWT.NONE );
		label.setText( G2GuiResources.getString( "SR_SEARCHING" ) );
		cTabItem.setControl( label );
		
		/* sets the tabitem on focus */
		cTabFolder.setSelection( cTabItem );
		
		/* listen for dispose to close this open search */
		cTabItem.addDisposeListener( new DisposeListener() {
			public void widgetDisposed( DisposeEvent e ) {
				( ( SearchResult ) cTabItem.getData() ).widgetDisposed( null );
			} 
		} );
		
		/* display 0 searchresults for the moment */
		SearchTab parent = ( SearchTab ) cTabFolder.getData();
		parent.setRightLabel( "Results: 0" );
		this.statusline = "Results: 0";
		parent.getMainTab().getStatusline().update( this.statusline );
	}

	/**
	 * Build the whole table and the tablecolumns
	 */
	private void createTable() {
		/* set a new image for the ctabitem to show we found results */
		cTabItem.setImage( G2GuiResources.getImage( "SearchComplete" ) );
		
		/* create the result table */		
		table = new CustomTableViewer( cTabFolder, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI );
		table.getTable().setLayoutData( new GridData( GridData.FILL_BOTH ) );
		table.getTable().setLinesVisible( true );
		table.getTable().setHeaderVisible( true );

		table.setUseHashlookup( true );  // more mem, but faster
		table.setContentProvider( new ResultTableContentProvider() );
		table.setLabelProvider( new ResultTableLabelProvider() );
		table.setSorter( resultTableSorter );
		ResultTableMenuListener tableMenuListener =
				new ResultTableMenuListener( table, core, cTabItem );
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
					// duplicate to reset the sorter 
					ResultTableSorter rTS = new ResultTableSorter();
					rTS.setLastColumnIndex( resultTableSorter.getLastColumnIndex() );
					rTS.setLastSort( resultTableSorter.getLastSort() );
					resultTableSorter = rTS;
					// set the column to sort
					resultTableSorter.setColumnIndex( columnIndex );
					table.setSorter( resultTableSorter );
				}	
			} ); 
		}

		/* add a resize listener */
		cTabFolder.addControlListener( new ControlAdapter() {
		public void controlResized( ControlEvent e ) {
				SearchResult.this.setColumnWidth();
			}
		} );
		
		/* add a mouse-listener to catch double-clicks */
		table.getTable().addMouseListener( new MouseListener() {
			public void mouseDoubleClick( MouseEvent e ) {
				downloadSelected();
			}
			public void mouseDown( MouseEvent e ) { }
			public void mouseUp( MouseEvent e ) { }
		} );
		
		/*
		 * add a menulistener to set the first item to default
		 * sadly not possible with the MenuManager Class
		 * (Feature Request on eclipse?)
		 */
		Menu menu = table.getTable().getMenu();
		menu.addMenuListener( new MenuListener(){
			public void menuShown( MenuEvent e ) {
				Menu menu = table.getTable().getMenu();
				if ( !table.getSelection().isEmpty() )
					menu.setDefaultItem( menu.getItem( 0 ) );
			}
			public void menuHidden( MenuEvent e ) { }
		} );

		
		final ToolTipHandler tooltip = new ToolTipHandler( table.getTable().getShell() );
		tooltip.activateHoverHelp( table.getTable() );
		
		/* set the this table as the new CTabItem Control */
		cTabItem.setControl( table.getTable() );
		
	}
	
	private void downloadSelected() {				
		TableItem[] currentItems = table.getTable().getSelection();
		Download download = new Download( core );
		for ( int i = 0; i < currentItems.length;	 i ++ ) {
			ResultInfo result = ( ResultInfo ) currentItems[ i ].getData();
			download.setPossibleNames( result.getNames() );	
			download.setResultID( result.getResultID() );
			download.setForce( false );
			download.send();
		}
		download = null;
	}
	
	/**
	 * Sets the size for the columns
	 */
	private void setColumnWidth() {
		/* only do this, if this table has not been disposed, because strangely 
		 * this is also called if this searchresult has been disposed further 
		 * investigation needed??
		 */
		if ( !table.getTable().isDisposed() ) {
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
	}
	
	/**
	 * @return The string to display in the statusline
	 */
	public String getStatusLine() {
		return this.statusline;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.DisposeListener#
	 * widgetDisposed(org.eclipse.swt.events.DisposeEvent)
	 */
	public void widgetDisposed( DisposeEvent e ) {
		/* dispose the table */		
		if ( table != null )
			table.getTable().dispose();
			
		
		/* tell the core to forget the search */
		Object[] temp = { new Integer( searchId ), new Byte( ( byte ) 1 ) };
		Message message = new EncodeMessage( Message.S_CLOSE_SEARCH, temp );
		message.sendMessage( core.getConnection() );
		message = null;
					
		/* no longer receive results for this search */
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
		private CLabel  tipLabelImage;
		private Label tipLabelText;
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
			gridLayout.numColumns = 1;
			gridLayout.marginWidth = 2;
			gridLayout.marginHeight = 2;
			tipShell.setLayout( gridLayout );
	
			tipShell.setBackground( display.getSystemColor( SWT.COLOR_INFO_BACKGROUND ) );
			
			tipLabelImage = new CLabel( tipShell, SWT.NONE );
			tipLabelImage.setAlignment( SWT.LEFT );
			tipLabelImage.setForeground( display.getSystemColor( SWT.COLOR_INFO_FOREGROUND ) );
			tipLabelImage.setBackground( display.getSystemColor( SWT.COLOR_INFO_BACKGROUND ) );
			tipLabelImage.setLayoutData( 
				new GridData( GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING ) );
	
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
					if ( event.widget == null ) return;
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

				
					// Create the tooltip on demand
					if ( widget instanceof TableItem ) {
						TableItem tableItem = ( TableItem ) widget; 
						ResultInfo aResult = ( ResultInfo ) tableItem.getData();
						
						Image image = null;
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
							if ( data != null )
								if ( G2GuiResources.getImage( p.getName() ) == null ) 
									G2GuiResources.getImageRegistry().put( p.getName(), new Image( null, data ) );
						}			
				
						String imageText = aResult.getNames()[ 0 ];
						String aString = "";
						if ( !aResult.getFormat().equals( "" ) )
							aString += G2GuiResources.getString( "ST_TT_FORMAT" )
										+ aResult.getFormat() + "\n";
							aString += G2GuiResources.getString( "ST_TT_LINK" )
										+ aResult.getLink() + "\n";
							aString += G2GuiResources.getString( "ST_TT_NETWORK" )
										+ aResult.getNetwork().getNetworkName() + "\n";
							aString += G2GuiResources.getString( "ST_TT_SIZE" )
										+ aResult.getStringSize() + "\n";
							aString += G2GuiResources.getString( "ST_TT_SOURCES" )
										+ aResult.getTags()[ 0 ].getValue();
						if ( !aResult.getHistory() )
							aString = aString + "\n" + G2GuiResources.getString( "ST_TT_DOWNLOADED" );
						
					
					// set the text/image for the tooltip 
						if ( aString != null )
							tipLabelText.setText( aString );
						else
							tipLabelText.setText( "" );
						
						/* load the image only if we have a associated programm */
						if ( p != null )
							tipLabelImage.setImage( G2GuiResources.getImage( p.getName() ) );
						else
							tipLabelImage.setImage( null );	

						tipLabelImage.setText ( imageText ); 
					
					/* pack/layout the tooltip */
					tipShell.pack();
					tipPosition = control.toDisplay( pt );
					setHoverLocation( tipShell, tipPosition );
					
					tipShell.setVisible( true );
				}
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
Revision 1.26  2003/08/23 14:58:38  lemmster
cleanup of MainTab, transferTree.* broken

Revision 1.25  2003/08/23 10:02:02  lemmster
use supertype where possible

Revision 1.24  2003/08/23 08:30:07  lemmster
added defaultItem to the table

Revision 1.23  2003/08/22 21:10:57  lemmster
replace $user$ with $Author: lemmster $

Revision 1.22  2003/08/20 22:18:56  zet
Viewer updates

Revision 1.21  2003/08/20 10:05:56  lemmster
MenuListener added

Revision 1.20  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.19  2003/08/17 23:13:42  zet
centralize resources, move images

Revision 1.18  2003/08/17 09:34:35  dek
double-click starts download of selected items

Revision 1.17  2003/08/14 12:44:45  dek
searching works now without errors

Revision 1.16  2003/08/11 12:16:10  dek
hopefully solved crash at fast input

Revision 1.15  2003/08/11 11:27:11  lemmstercvs01
bugfix for closing searchresults

Revision 1.14  2003/08/10 19:31:15  lemmstercvs01
try to fix the root handle bug

Revision 1.13  2003/08/10 10:27:38  lemmstercvs01
bugfix and new image on table create

Revision 1.12  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.11  2003/08/01 17:21:19  lemmstercvs01
reworked observer/observable design, added multiversion support

Revision 1.10  2003/07/31 14:20:00  lemmstercvs01
statusline reworked

Revision 1.9  2003/07/31 04:10:28  zet
searchresult changes

Revision 1.7  2003/07/29 10:11:48  lemmstercvs01
moved icon folder out of src/

Revision 1.6  2003/07/29 09:44:45  lemmstercvs01
added support for the statusline

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