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

import churchillobjects.rss4j.RssDocument;

import churchillobjects.rss4j.parser.RssParseException;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.helper.RSSFetcher;

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

/**
 * NewsManager
 *
 * @version $Id: NewsManager.java,v 1.3 2003/10/04 08:49:25 lemmster Exp $
 *
 */
public class NewsManager extends News implements Observer {
    private RSSFetcher aRSSFetcher;
    private Text stubs;
    private Map aMap;
    private Thread aThread;
    private CTabFolder cTabFolder;

    /**
     * @param aControl
     */
    public NewsManager( Control aControl ) {
        super( aControl );
        this.aMap = new HashMap();

        /* add a dispose listerner to catch this event and stop the rssfetcher then */
        control.getShell().addDisposeListener( new DisposeListener() {
                public void widgetDisposed( DisposeEvent e ) {
                    aRSSFetcher.deleteObservers();
                    aThread.interrupt();
                    aRSSFetcher.stop();
                }
            } );
        /* create a new news generator */
        try {
            aRSSFetcher = new RSSFetcher();
            aThread = new Thread( aRSSFetcher );
            aThread.start();
        }
        catch ( MalformedURLException e ) {
            /* we expect to receive always valid urls */
        }

        /* register on the newsgenerator */
        aRSSFetcher.addObserver( this );
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        control = new Composite( ( Composite ) control, SWT.NONE );
        ( ( Composite ) control ).setLayout( gridLayout );

        /* a temp stubs obj to show something in the meantime */
        stubs = new Text( ( Composite ) control, SWT.NONE | SWT.READ_ONLY );
        stubs.setText( "fetching and parsing newsfeeds..." );
    }

    /**
     * DOCUMENT ME!
     */
    private void create() {
        /* dispose the stubs obj. we dont need it anymore */
        stubs.dispose();

        /* create the main underlaying swt components */
        cTabFolder = new CTabFolder( ( Composite ) control, SWT.NONE );
        cTabFolder.setLayoutData( new GridData( GridData.FILL_BOTH ) );
        cTabFolder.setSelectionBackground( new Color[] { cTabFolder.getDisplay().getSystemColor( SWT.COLOR_TITLE_BACKGROUND ), cTabFolder
                                                                                                                                 .getBackground() },
                                           new int[] { 75 } );
        cTabFolder.setSelectionForeground( cTabFolder.getDisplay().getSystemColor( SWT.COLOR_TITLE_FOREGROUND ) );

        /* create the children */
        this.fill();

        /* set the default selection and layout() us */
        cTabFolder.setSelection( 0 );
        ( ( Composite ) control ).layout();
    }

    /**
     * DOCUMENT ME!
     */
    private void fill() {
        Iterator itr = aRSSFetcher.getNewsSource().entrySet().iterator();
        while ( itr.hasNext() ) {
            Map.Entry anEntry = ( Map.Entry ) itr.next();
            URL anUrl = ( URL ) anEntry.getKey();
            RssDocument aDoc = ( RssDocument ) anEntry.getValue();
            if ( aMap.containsKey( anUrl ) ) {
                NewsFeed aNewsFeed = ( NewsFeed ) aMap.get( anUrl );
                /* only update the newsfeed if the hash of the rssdoc has changed */
                if ( aNewsFeed.getARssDocument().hashCode() != aDoc.hashCode() )
                    aNewsFeed.update( aDoc );
            }
            else
                aMap.put( anUrl, new NewsFeed( cTabFolder, aDoc, anUrl.getHost() ) );
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    private void handleException( Exception e ) {
        MessageBox box = new MessageBox( control.getShell(), SWT.ICON_WARNING );
        if ( e instanceof RssParseException ) {
            box.setText( "RssParseException" );
            box.setMessage( "RssParseException" );
        }
        else if ( e instanceof IOException ) {
            box.setText( "IOException" );
	        box.setMessage( "IOException" );
        }
        else {
        	box.setText( "UnknownException" );
        	box.setMessage( "UnknownException" );
        }
        box.open();
    }

    /**
     * DOCUMENT ME!
     */
    private void update() {
        this.fill();
		( ( Composite ) control ).layout();
    }

    /**
     *
     * @param title A title for this newsfeed. if <code>null</code> the hostname is used
     * @param anUrl The <code>URL</code> of this newsfeed file
     */
    public void add( String title, URL anUrl ) {
        this.aRSSFetcher.add( anUrl );
        aThread.interrupt();
    }

    /**
     *
     * @param anUrl
     */
    public void remove( URL anUrl ) {
        this.aRSSFetcher.remove( anUrl );
        aThread.interrupt();
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
        /* handle the error if we receive one */
        if ( arg instanceof Exception )
            this.handleException( ( Exception ) arg );
        /* create the content if we still have the stubs obj */
        else if ( !stubs.isDisposed() )
            control.getDisplay().asyncExec( new Runnable() {
                    public void run() {
                        create();
                    }
                } );

        /* or update the content */
        else
            control.getDisplay().asyncExec( new Runnable() {
                    public void run() {
                        update();
                    }
                } );

    }
}

/*
$Log: NewsManager.java,v $
Revision 1.3  2003/10/04 08:49:25  lemmster
foobar

Revision 1.2  2003/09/29 14:05:45  lemmster
update & add still not working

Revision 1.1  2003/09/27 12:09:32  lemmster
initial commit

*/
