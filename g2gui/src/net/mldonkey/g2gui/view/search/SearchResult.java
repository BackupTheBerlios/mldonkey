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

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolderAdapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;

/**
 * SearchResult
 *
 * @author $user$
 * @version $Id: SearchResult.java,v 1.1 2003/07/23 16:56:28 lemmstercvs01 Exp $ 
 *
 */
public class SearchResult implements Observer, Runnable {
	private Composite composite;
	private String searchString;
	private CoreCommunication core;
	private int searchId;
	
	private ResultInfoIntMap results;
	private ResultInfo currentResult;
	
	private TableViewer table;
	private Label label;
	private Image image;
	private CTabItem cTabItem;
	private TableColumn tableColumn;
	/* if you modify this, change the LayoutProvider as well */
	private String[] tableColumns = { "Network", "Name", "MD4", "Size", "Format", "Type", "Metadata", "Comment", "downloaded" };
		
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
		image = new Image( composite.getDisplay(), "src/icons/search_small.png");
		cTabItem.setImage( image );

		( ( CTabFolder ) composite ).addCTabFolderListener( new CTabFolderAdapter() {
			public void itemClosed( CTabFolderEvent event ) {
					/* tell the core to forget the search */
					Object[] temp = { new Integer( searchId ), new Byte( ( byte ) 1 ) };
					EncodeMessage message = new EncodeMessage( Message.S_CLOSE_SEARCH, temp );
					message.sendMessage( core.getConnection() );
					message = null;
					
					/* dispose the image */
					image.dispose();

					/* we want no longer receive result for this search */
					unregister();
					/* close the tab item */
					event.item.dispose();
			}
		});

		/* for the search delay, just draw a label */ 
		label = new Label( composite, SWT.NONE );
		label.setText( "searching..." );
		cTabItem.setControl( label );
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
			
				/* create the result table */		
				table = new TableViewer( composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI );
				table.getTable().setLayoutData( new GridData( GridData.FILL_BOTH ) );
				table.getTable().setLinesVisible( false );
				table.getTable().setHeaderVisible( true );
				table.getTable().setMenu( createRightClickMenu() );

				/* listen for right mouse click on item */
				table.getTable().addMouseListener( new MouseListener () {
					public void mouseDown( MouseEvent e ) {
						if ( e.button == 3 ) {
							Point pt = new Point( e.x, e.y );
							currentResult = ( ResultInfo ) table.getTable().getItem( pt ).getData();
						}
					}
					public void mouseDoubleClick(MouseEvent e) {}
					public void mouseUp(MouseEvent e) {}
				});

		
				table.setContentProvider( new ResultTableContentProvider() );
				table.setLabelProvider( new ResultTableLabelProvider() );
				
				/* create the columns */
				for ( int i = 0; i < tableColumns.length; i++ ) {
					tableColumn = new TableColumn( table.getTable(), SWT.LEFT );
					tableColumn.setText( tableColumns[ i ] );
					tableColumn.pack();
				}
				/* fill the table with content */
				table.setInput(  this.results.get( searchId ) );
						
				/* set the this table as the new CTabItem Control */
				cTabItem.setControl( table.getTable() );
						
				/* update the display to show our changes */
				composite.layout();
			} 
			else {
				/* has our result changed */
				int temp =  ( ( List ) results.get( searchId ) ).size();
				if( table.getTable().getItemCount() != temp )
					table.refresh();
			}
	}
	
	/**
	 * unregister ourself (Observer) by the core
	 */
	private void unregister() {
		this.core.deleteObserver( this );
	}
	
	/**
	 * 
	 * @return
	 */
	private Menu createRightClickMenu() {
		Shell shell = table.getTable().getShell();
		Menu menu = new Menu( shell , SWT.POP_UP );

		MenuItem menuItem;
		/* Download */
		menuItem = new MenuItem( menu, SWT.PUSH );
		menuItem.setText("download");
		menuItem.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				Download download = new Download( core );
				download.setPossibleNames( currentResult.getNames() );
				download.setResultID( currentResult.getResultID() );
				download.setForce( false );
				download.send();
				download = null;
			}
		});
		
		return menu;
	}
}

/*
$Log: SearchResult.java,v $
Revision 1.1  2003/07/23 16:56:28  lemmstercvs01
initial commit

*/