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
package net.mldonkey.g2gui.view.news;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.helper.RSSFetcher;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import churchillobjects.rss4j.RssDocument;

/**
 * NewsManager
 *
 * @version $Id: NewsManager.java,v 1.1 2003/09/27 12:09:32 lemmster Exp $
 *
 */
public class NewsManager extends News implements Observer {
    private RSSFetcher aRSSFetcher;
	private Text stubs;
	private Map aMap;
	private Thread aThread;

    /**
     * @param aControl
     */
    public NewsManager( Control aControl ) {
        super( aControl );
        
        this.aMap = new HashMap();
        
        /* add a dispose listerner to catch this event and stop the rssfetcher then */
        control.getShell().addDisposeListener( new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				aRSSFetcher.deleteObservers();
				aThread.interrupt();
				aRSSFetcher.disconnect();
			}
        } );

		try {
			/* create a new news generator */ 
			aRSSFetcher = new RSSFetcher();
	        aThread = new Thread( aRSSFetcher );
	        aThread.start();
		}
		catch ( MalformedURLException e ) {
			e.printStackTrace();
		}

		/* register on the newsgenerator */
        aRSSFetcher.addObserver( this );

		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		control = new Composite( ( Composite ) control, SWT.NONE );
		( ( Composite ) control ).setLayout( gridLayout );

        /* a temp stubs obj to show something in the meantime */ 
        stubs = new Text( ( Composite ) control, SWT.NONE );
        stubs.setText( "fetching and parsing newsfeeds..." );
    }

    /**
     * DOCUMENT ME!
     */
    private void create() {
    	/* dispose the stubs obj. we dont need it anymore */
    	stubs.dispose();
    	
 		/* create the main underlaying swt components */
        CTabFolder cTabFolder = new CTabFolder( ( Composite ) control, SWT.NONE );
        cTabFolder.setLayoutData( new GridData( GridData.FILL_BOTH ) );
        cTabFolder.setSelectionBackground( 
        	new Color[] { cTabFolder.getDisplay().getSystemColor( SWT.COLOR_TITLE_BACKGROUND ),
        		cTabFolder.getBackground() }, new int[] { 75 } );
        cTabFolder.setSelectionForeground( cTabFolder.getDisplay().getSystemColor( SWT.COLOR_TITLE_FOREGROUND ) );

		Iterator itr = aRSSFetcher.getNewsSource().entrySet().iterator();
		while( itr.hasNext() ) {
			Map.Entry anEntry = ( Map.Entry ) itr.next();
			URL anUrl = ( URL ) anEntry.getKey();
			RssDocument aDoc = ( RssDocument ) anEntry.getValue();
			aMap.put( anUrl, new NewsFeed( cTabFolder, aDoc, anUrl.getHost() ) );
		}
		
		cTabFolder.setSelection( 0 );
		
		( ( Composite ) control ).layout();
    }

    /**
     * @return
     */
    public Control getContent() {
        return control;
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update( Observable o, Object arg ) {
		if ( arg instanceof Exception ) {
			//TODO handel different exceptions
			MessageBox box = new MessageBox( control.getShell(), SWT.ICON_WARNING );
			box.setText( G2GuiResources.getString( "G2_LOGIN_INVALID" ) );
			box.setMessage( "an error occured! sorry no better error handling yet!" );
			box.open();
			return;
		}
		
		/* create the content */
		if ( stubs != null ) {
			control.getDisplay().asyncExec( new Runnable() {
				public void run() {
					create();
				}
			} );
		}		
    }
}

/*
$Log: NewsManager.java,v $
Revision 1.1  2003/09/27 12:09:32  lemmster
initial commit

*/
