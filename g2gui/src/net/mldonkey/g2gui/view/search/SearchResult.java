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
import net.mldonkey.g2gui.model.ResultInfo;
import net.mldonkey.g2gui.model.ResultInfoIntMap;
import net.mldonkey.g2gui.view.GuiTab;
import net.mldonkey.g2gui.view.MainTab;
import net.mldonkey.g2gui.view.SearchTab;
import net.mldonkey.g2gui.view.helper.CGridLayout;
import net.mldonkey.g2gui.view.helper.WordFilter;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.transfer.CustomTableViewer;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TableViewer;
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
 *
 * @version $Id: SearchResult.java,v 1.54 2003/09/29 17:44:40 lemmster Exp $
 *
 */
public class SearchResult implements Observer, Runnable, DisposeListener {
    private ResultTableMenuListener resultTableMenuListener;
	private GuiTab search;
	private MainTab mainTab;
    private CTabFolder cTabFolder;
    private String searchString;
    private CoreCommunication core;
    private int searchId;
    private boolean stopped = false;
    private ResultInfoIntMap results;
    private TableViewer table;
    private Label label;
    private CTabItem cTabItem;
    private TableColumn tableColumn;
    private String statusline;
    private ResultTableSorter resultTableSorter = new ResultTableSorter();
	private boolean mustRefresh = false;
    private long lastRefreshTime = 0;
    private int count = 0;

    /* if you modify this, change the LayoutProvider and tableWidth */
    private String[] tableColumns = { G2GuiResources.getString( "SR_NETWORK" ),
								    	G2GuiResources.getString( "SR_NAME" ),
								    	G2GuiResources.getString( "SR_SIZE" ),
								    	G2GuiResources.getString( "SR_FORMAT" ),
								    	G2GuiResources.getString( "SR_MEDIA" ),
								    	G2GuiResources.getString( "SR_AVAIL" ), };

    /* 0 sets the tablewidth dynamcliy */
    private int[] tableWidth = { 58, 0, 65, 45, 50, 70 };
    
    private int[] tableAlign =
    {
    	SWT.LEFT, SWT.LEFT, SWT.RIGHT, 
		SWT.LEFT, SWT.LEFT, SWT.LEFT 
    };

    /**
     * Creates a new SearchResult to display all the results supplied by mldonkey
     * @param aString The SearchString we are searching for
     * @param parent The parent TabFolder, where we display our TabItem in
     * @param core The core to communicate with
     * @param searchId The identifier to this search
     */
    protected SearchResult( String aString, CTabFolder parent, CoreCommunication aCore, int searchId, GuiTab aTab ) {
        this.searchString = aString;
        this.cTabFolder = parent;
        this.searchId = searchId;
        this.core = aCore;
        this.search = aTab;

        /* draw the display */
        this.createContent();

        /* register ourself to the core */
        core.getResultInfoIntMap().addObserver( this );
    }

    /**
     * stops this search
     */
    public void stopSearch() {
        this.stopped = true;
    }

    /**
     * continues a stopped search
     */
    public void continueSearch() {
        this.stopped = false;
        cTabFolder.getDisplay().asyncExec( this );
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update( Observable o, final Object arg ) {
        /* if the tab is already disposed, dont update */
        if ( cTabItem.isDisposed() || this.stopped )
            return;
            
        if ( arg instanceof ResultInfo ) {
        	cTabFolder.getDisplay().asyncExec( new Runnable() {
				public void run() {
					table.update( arg, null );
				}
        	} );
        	return;
        }    

        /* are we responsible for this update */
        if ( ( ( ResultInfoIntMap ) arg ).containsKey( searchId ) )
            cTabFolder.getDisplay().asyncExec( this );
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void run() {
        /* if the tab is already disposed, dont update */
        if ( cTabItem.isDisposed() || this.stopped )
            return;
        this.results = this.core.getResultInfoIntMap();
        List list = ( List ) results.get( searchId );
        if ( table == null ) {
            /* remove the old label "searching..." */
            label.dispose();
            this.createTable();
            table.setInput( list );
            this.setColumnWidth();
        }
        else {
            /*
             * has our result changed:
             * only refresh the changed items,
             * look at API for refresh(false)
             */
            if ( list.size() != table.getTable().getItemCount() ) {
                mustRefresh = true;
                delayedRefresh();
            }
        }

        /* are we active? set the statusline text */
        if ( cTabFolder.getSelection() == cTabItem ) {
            SearchTab parent = ( SearchTab ) cTabFolder.getData();
            int itemCount = table.getTable().getItemCount();
            this.statusline = "Results: " + itemCount;
            parent.getMainTab().getStatusline().update( this.statusline );
        }
    }

    /**
     * simple attempt to buffer refreshes
     */
    private void delayedRefresh() {
        if ( System.currentTimeMillis() > ( lastRefreshTime + 2000 ) ) {
            lastRefreshTime = System.currentTimeMillis();
            table.refresh( true );
            mustRefresh = false;
        }
        else { // schedule an update so we don't miss one
            if ( mustRefresh )
                cTabItem.getDisplay().timerExec( 2500, this );
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
        cTabItem.addDisposeListener( this );
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

        /* display 0 searchresults for the moment */
        SearchTab parent = ( SearchTab ) cTabFolder.getData();

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
        table.setUseHashlookup( true ); // more mem, but faster
        table.setContentProvider( new ResultTableContentProvider() );
        table.setLabelProvider( new ResultTableLabelProvider() );
        table.setSorter( resultTableSorter );
        resultTableMenuListener = 
        	new ResultTableMenuListener( table, core, cTabItem );
        table.addSelectionChangedListener( resultTableMenuListener );
        MenuManager popupMenu = new MenuManager( "" );
        popupMenu.setRemoveAllWhenShown( true );
        popupMenu.addMenuListener( resultTableMenuListener );
        table.getTable().setMenu( popupMenu.createContextMenu( table.getTable() ) );
		
        // add optional filters
        if ( PreferenceLoader.loadBoolean( "searchFilterPornography" ) )
            table.addFilter( new WordFilter( WordFilter.PORNOGRAPHY_FILTER_TYPE ) );
        else if ( PreferenceLoader.loadBoolean( "searchFilterProfanity" ) )
            table.addFilter( new WordFilter( WordFilter.PROFANITY_FILTER_TYPE ) );

        /* create the columns */
        for ( int i = 0; i < tableColumns.length; i++ ) {
            tableColumn = new TableColumn( table.getTable(), tableAlign[ i ] );
            tableColumn.setText( tableColumns[ i ] );
            tableColumn.pack();

            /* adds a sort listener */
            final int columnIndex = i;
            tableColumn.addListener( SWT.Selection, new Listener() {
                    public void handleEvent( Event e ) {
                        resultTableSorter.setColumnIndex( columnIndex );
                        table.refresh();
                    }
                } );
        }

        /* add a resize listener */
        cTabFolder.addControlListener( new ControlAdapter() {
                public void controlResized( ControlEvent e ) {
                    SearchResult.this.setColumnWidth();
                }
            } );

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

                public void menuHidden( MenuEvent e ) {
                }
            } );
           
		/* add a mouse-listener to catch double-clicks */
		table.getTable().addMouseListener( new MouseListener() {
			public void mouseDoubleClick( MouseEvent e ) {
				resultTableMenuListener.downloadSelected();
			}
			public void mouseDown( MouseEvent e ) { 
				if ( stopped )
					( ( SearchTab ) search ).setContinueButton();
				else
					( ( SearchTab ) search ).setStopButton();
			}
			public void mouseUp( MouseEvent e ) { }
		} );

		/* just show tooltip on user request */    
		if ( PreferenceLoader.loadBoolean( "showSearchTooltip" ) ) {
	        final ToolTipHandler tooltip = new ToolTipHandler( table.getTable().getShell() );
	        tooltip.activateHoverHelp( table.getTable() );
		}
		
        /* set the this table as the new CTabItem Control */
        cTabItem.setControl( table.getTable() );
        
        /*load behaviour from preference-Store*/
		updateDisplay();
    }
    
	/**
	 * refreshes the Display with values from preference-Store.
	 * This method is called, when preference-Sotre is closed, that means, something
	 * might have benn changed.
	 */
	public void updateDisplay() {
		table.getTable().setLinesVisible(
						PreferenceLoader.loadBoolean( "displayGridLines" ) );
		
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

    /**
     * @return does this search has stopped
     */
    public boolean isStopped() {
        return stopped;
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.DisposeListener#
     * widgetDisposed(org.eclipse.swt.events.DisposeEvent)
     */
    public void widgetDisposed( DisposeEvent e ) {
		/* no longer receive results for this search */
		this.unregister();

        /* tell the core to forget the search */
        Object[] temp = { new Integer( searchId ), new Byte( ( byte ) 1 ) };
        Message message = new EncodeMessage( Message.S_CLOSE_SEARCH, temp );
        message.sendMessage( core );
        message = null;
    }
    
    /**
     * Emulated tooltip handler
     * Notice that we could display anything in a tooltip besides text and images.
     * For instance, it might make sense to embed large tables of data or buttons linking
     * data under inspection to material elsewhere, or perform dynamic lookup for creating
     * tooltip text on the fly.
     */
    private static class ToolTipHandler {
        private Shell tipShell;
        private CLabel tipLabelImage;
        private Label tipLabelText;
        private Widget tipWidget; // widget this tooltip is hovering over
        private Point tipPosition; // the position being hovered over

        /**
         * Creates a new tooltip handler
         *
         * @param parent the parent Shell
         */
        public ToolTipHandler( Composite parent ) {
            final Display display = parent.getDisplay();
            tipShell = new Shell( parent.getShell(), SWT.ON_TOP );
            GridLayout gridLayout = CGridLayout.createGL( 1, 2, 2, 0, 0, false );
            tipShell.setLayout( gridLayout );
            tipShell.setBackground( display.getSystemColor( SWT.COLOR_INFO_BACKGROUND ) );
            tipLabelImage = new CLabel( tipShell, SWT.NONE );
            tipLabelImage.setAlignment( SWT.LEFT );
            tipLabelImage.setForeground( display.getSystemColor( SWT.COLOR_INFO_FOREGROUND ) );
            tipLabelImage.setBackground( display.getSystemColor( SWT.COLOR_INFO_BACKGROUND ) );
            tipLabelImage.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
                                                       | GridData.HORIZONTAL_ALIGN_BEGINNING ) );
            tipLabelText = new Label( tipShell, SWT.NONE );
            tipLabelText.setForeground( display.getSystemColor( SWT.COLOR_INFO_FOREGROUND ) );
            tipLabelText.setBackground( display.getSystemColor( SWT.COLOR_INFO_BACKGROUND ) );
            tipLabelText.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
                                                      | GridData.VERTICAL_ALIGN_CENTER ) );
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
            control.addMouseListener( new MouseAdapter() {
                    public void mouseDown( MouseEvent e ) {
                        if ( tipShell.isVisible() )
                            tipShell.setVisible( false );
                    }
                } );

            /*
             * Trap hover events to pop-up tooltip
             */
            control.addMouseTrackListener( new MouseTrackAdapter() {
                    public void mouseExit( MouseEvent e ) {
                        if ( tipShell.isVisible() )
                            tipShell.setVisible( false );
                        tipWidget = null;
                    }

                    public void mouseHover( MouseEvent event ) {
                        if ( event.widget == null )
                            return;
                        Point pt = new Point( event.x, event.y );
                        Widget widget = event.widget;

                        /* get the selected item from the table */
                        Table w = ( Table ) widget;
                        widget = w.getItem( pt );
                        if ( widget == null ) {
                            tipShell.setVisible( false );
                            tipWidget = null;
                        }
                        if ( widget == tipWidget )
                            return;
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
                                String temp = aResult.getName();
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
                                        G2GuiResources.getImageRegistry().put( 
                                                                                 p.getName(),
                                                                                 new Image( null, data ) );
                            }
                            String imageText = aResult.getName();
                            String aString = "";
                            if ( !aResult.getFormat().equals( "" ) )
                                aString += ( 
                                               G2GuiResources.getString( "ST_TT_FORMAT" )
                                               + aResult.getFormat() + "\n"
                                            );
                            aString += ( 
                                               G2GuiResources.getString( "ST_TT_LINK" ) + aResult.getLink()
                                               + "\n"
                                            );
                            aString += ( 
                                               G2GuiResources.getString( "ST_TT_NETWORK" )
                                               + aResult.getNetwork().getNetworkName() + "\n"
                                            );
                            aString += ( 
                                               G2GuiResources.getString( "ST_TT_SIZE" )
                                               + aResult.getStringSize() + "\n"
                                            );
                            aString += ( 
                                               G2GuiResources.getString( "ST_TT_AVAIL" )
                                               + aResult.getAvail() + " "
											   + G2GuiResources.getString( "ST_TT_SOURCES" ) + "\n"
                                            );
                            if ( aResult.getType().equals( "Audio" ) ) {
                            	if ( !aResult.getBitrate().equals( "" ) )
	                            	aString += ( 
	                            				G2GuiResources.getString( "ST_TT_BITRATE" )
	                            				+ aResult.getBitrate() + "\n"
	                            				 );
								if ( !aResult.getLength().equals( "" ) )
	                            	aString += ( 
	                            				G2GuiResources.getString( "ST_TT_LENGTH" )
	                            				+ aResult.getLength() 
	                            				 );
                            }
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
                            tipLabelImage.setText( imageText );

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
            shellBounds.x = Math.max( Math.min( position.x, displayBounds.width - shellBounds.width ), 0 );
            shellBounds.y =
                Math.max( Math.min( position.y + 16, displayBounds.height - shellBounds.height ), 0 );
            shell.setBounds( shellBounds );
        }
    }
}
/*
$Log: SearchResult.java,v $
Revision 1.54  2003/09/29 17:44:40  lemmster
switch for search tooltips

Revision 1.53  2003/09/27 13:30:22  dek
all tables have now show-Gridlines-behaviour as descibed in  preferences

Revision 1.52  2003/09/25 17:58:41  lemmster
fixed crash bug

Revision 1.51  2003/09/22 20:20:09  lemmster
show # sources in search result avail table

Revision 1.50  2003/09/22 20:07:22  lemmster
right column align for integers [bug #934]

Revision 1.49  2003/09/20 14:38:51  zet
move transfer package

Revision 1.48  2003/09/19 17:51:39  lemmster
minor bugfix

Revision 1.47  2003/09/19 15:19:14  lemmster
reworked

Revision 1.46  2003/09/18 15:30:05  zet
centralize writeStream in core
handle IOException rather than throwing it away

Revision 1.45  2003/09/18 10:39:21  lemmster
checkstyle

Revision 1.44  2003/09/18 07:45:09  lemmster
fix [bug # 933]

Revision 1.43  2003/09/17 20:07:44  lemmster
avoid NPE´s in search

Revision 1.42  2003/09/16 09:24:11  lemmster
adjust source rating

Revision 1.41  2003/09/15 15:32:09  lemmster
reset state of canceled downloads from search [bug #908]

Revision 1.40  2003/09/08 12:38:00  lemmster
show bitrate/length for audio files in tooltip

Revision 1.39  2003/09/08 11:54:23  lemmster
added download button

Revision 1.38  2003/09/01 11:18:51  lemmster
show downloading files

Revision 1.37  2003/08/31 20:32:50  zet
active button states

Revision 1.36  2003/08/31 13:38:38  lemmster
delayedRefresh() is working again

Revision 1.35  2003/08/31 12:32:04  lemmster
major changes to search

Revision 1.34  2003/08/29 17:33:20  zet
remove headerbar

Revision 1.33  2003/08/29 00:54:42  zet
Move wordFilter public

Revision 1.32  2003/08/28 22:44:30  zet
GridLayout helper class

Revision 1.31  2003/08/28 11:54:41  lemmster
use getter methode for profanity/pornogaphic

Revision 1.30  2003/08/26 22:44:03  zet
basic filtering

Revision 1.29  2003/08/25 16:02:50  zet
remove duplicate code, move dblclick to menulistener

Revision 1.28  2003/08/24 02:34:16  zet
update sorter properly

Revision 1.27  2003/08/23 15:21:37  zet
remove @author

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
