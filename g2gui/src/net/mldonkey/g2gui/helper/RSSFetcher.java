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
package net.mldonkey.g2gui.helper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;

import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import churchillobjects.rss4j.RssDocument;
import churchillobjects.rss4j.parser.RssParseException;
import churchillobjects.rss4j.parser.RssParser;

/**
 * NewsCreator
 *
 * @version $Id: RSSFetcher.java,v 1.1 2003/09/27 12:09:32 lemmster Exp $
 *
 */
public class RSSFetcher extends Observable implements Runnable {
    private long refreshTime;
    private boolean connected;
    private Map newsSource;

    public RSSFetcher()
        throws MalformedURLException {
        this.connected = true;
        this.newsSource = createURLMap();
        this.refreshTime = PreferenceLoader.loadInteger( "newsRefreshTime" );
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        while ( connected ) {
            Iterator itr = newsSource.keySet().iterator();
            while ( itr.hasNext() ) {
                URL anUrl = ( URL ) itr.next();
                try {
                    RssDocument doc = RssParser.parseRss( anUrl.openStream() );
                    newsSource.put( anUrl, doc );
                }
                catch ( RssParseException e ) {
                    /* remove the incorrect url from the map */
                    newsSource.remove( anUrl );

                    /* notify the observer */
                    handleException( e );
                }
                catch ( IOException e ) {
                    /* remove the incorrect url from the map */
                    newsSource.remove( anUrl );

                    /* notify the observer */
                    handleException( e );
                }
            }

            /* notify the observer about the new news */
            this.setChanged();
            this.notifyObservers();
            try {
                Thread.sleep( refreshTime );
            }
            catch ( InterruptedException e ) {
                handleException( e );
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws MalformedURLException DOCUMENT ME!
     */
    private Map createURLMap()
        throws MalformedURLException {
        String[] aString = OurTools.split( PreferenceLoader.loadString( "newsSource" ), ';' );
        Map aMap = new HashMap();
        for ( int i = 0; i < aString.length; i++ ) {
            URL anUrl = new URL( aString[ i ] );
            aMap.put( anUrl, null );
        }
        return aMap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    private void handleException( Exception e ) {
        this.connected = false;
        this.setChanged();
        this.notifyObservers( e );
    }

    /**
     * @param connected
     */
    public void disconnect() {
        this.connected = false;
    }

	/**
	 * @return
	 */
	public Map getNewsSource() {
		return newsSource;
	}
}

/*
$Log: RSSFetcher.java,v $
Revision 1.1  2003/09/27 12:09:32  lemmster
initial commit

*/
