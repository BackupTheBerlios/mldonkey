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
import net.mldonkey.g2gui.view.helper.OurTableViewer;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Label;

/**
 * SearchResult
 *
 *
 * @version $Id: SearchResult.java,v 1.55 2003/10/21 17:00:45 lemmster Exp $
 *
 */
public class SearchResult implements Observer, Runnable, DisposeListener {
	private GuiTab search;
	private MainTab mainTab;
    private CTabFolder cTabFolder;
    private String searchString;
    private CoreCommunication core;
    private int searchId;
    private boolean stopped = false;
    private ResultInfoIntMap results;
    private Label label;
    private CTabItem cTabItem;
    private String statusline;
	private boolean mustRefresh = false;
    private long lastRefreshTime = 0;
    private OurTableViewer ourTableViewer;

    /**
     * Creates a new SearchResult to display all the results supplied by mldonkey
     * @param aString The SearchString we are searching for
     * @param parent The parent TabFolder, where we display our TabItem in
     * @param core The core to communicate with
     * @param searchId The identifier to this search
     */
    protected SearchResult( String aString, CTabFolder parent, CoreCommunication aCore, int searchId, GuiTab aSearch ) {
        this.searchString = aString;
        this.cTabFolder = parent;
        this.searchId = searchId;
        this.core = aCore;
        this.search = aSearch;

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
					ourTableViewer.getTableViewer().update( arg, null );
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
        if ( ourTableViewer == null ) {
            /* remove the old label "searching..." */
            label.dispose();
            this.createTable();
            ourTableViewer.setInput( list );
        }
        else {
            /*
             * has our result changed:
             * only refresh the changed items,
             * look at API for refresh(false)
             */
            if ( list.size() != ourTableViewer.getTableViewer().getTable().getItemCount() ) {
                mustRefresh = true;
                delayedRefresh();
            }
        }

        /* are we active? set the statusline text */
        if ( cTabFolder.getSelection() == cTabItem ) {
            SearchTab parent = ( SearchTab ) cTabFolder.getData();
            int itemCount = ourTableViewer.getTableViewer().getTable().getItemCount();
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
			ourTableViewer.getTableViewer().refresh( true );
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
		this.ourTableViewer = new ResultTableViewer( cTabFolder, core, cTabItem, new MyMouseListener() );
		
        /* set the this table as the new CTabItem Control */
        cTabItem.setControl( ourTableViewer.getTableViewer().getTable() );
        
        /*load behaviour from preference-Store*/
		updateDisplay();
    }
    
	/**
	 * refreshes the Display with values from preference-Store.
	 * This method is called, when preference-Sotre is closed, that means, something
	 * might have benn changed.
	 */
	public void updateDisplay() {
		ourTableViewer.getTableViewer().getTable().setLinesVisible(
						PreferenceLoader.loadBoolean( "displayGridLines" ) );
		
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
     * 
     * MyMouseListener
     *
     */
    public class MyMouseListener implements MouseListener {
		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseDoubleClick( MouseEvent e ) {
			( ( ResultTableViewer ) ourTableViewer ).getMenuListener().downloadSelected();
		}
		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseDown( MouseEvent e ) {
			if ( stopped )
				( ( SearchTab ) search ).setContinueButton();
			else
				( ( SearchTab ) search ).setStopButton();
		}
		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseUp( MouseEvent e ) { }
    }
}
/*
$Log: SearchResult.java,v $
Revision 1.55  2003/10/21 17:00:45  lemmster
class hierarchy for tableviewer

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
