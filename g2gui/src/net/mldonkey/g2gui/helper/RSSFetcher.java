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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import churchillobjects.rss4j.parser.RssParseException;
import churchillobjects.rss4j.parser.RssParser;

/**
 * NewsCreator
 *
 * @version $Id: RSSFetcher.java,v 1.4 2003/10/13 08:18:17 lemmster Exp $
 *
 */
public class RSSFetcher extends Observable implements Runnable {
    private long refreshTime;
    private boolean connected;
    private List newsSource;

	/**
	 * 
	 * @throws MalformedURLException
	 */
    public RSSFetcher()
        throws MalformedURLException {
        this.connected = true;
        this.newsSource = createNewsFeedURLList();
        this.refreshTime = PreferenceLoader.loadInteger( "newsFeedRefreshTime" );
        
		this.setProxy( PreferenceLoader.loadString( "proxyHost"), 
					   new Integer( PreferenceLoader.loadInteger( "proxyPort" ) ).toString() );
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        while ( connected ) {
            Iterator itr = newsSource.iterator();
            while ( itr.hasNext() ) {
                NewsFeedURL aNewsFeedURL = ( NewsFeedURL ) itr.next();
                try {
                    aNewsFeedURL.setRssDocument( 
                    	RssParser.parseRss( aNewsFeedURL.getUrl().openStream() ) );
                }
                catch ( RssParseException e ) {
                    /* remove the incorrect url from the map */
                    newsSource.remove( aNewsFeedURL );

                    /* notify the observer */
                    handleException( e );
                }
                catch ( IOException e ) {
                    /* remove the incorrect url from the map */
                    newsSource.remove( aNewsFeedURL );

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
    public static List createNewsFeedURLList() throws MalformedURLException {
		List result = new ArrayList();

		/* read the urls from the preferenceLoader */
		int i = 1;
		while ( true ) {
			/* read the hostname (break if preferences returns null */
			String aHostname = PreferenceLoader.loadString( "newsFeedURL" + i );
			if ( aHostname.equals( "" ) ) break;
			URL anUrl = new URL( aHostname );

			/* read the title (if preferences return null use hostname */
			String aTitle = PreferenceLoader.loadString( "newsFeedTitle" + i++ );
			if ( aTitle.equals( "" ) ) aTitle = anUrl.getHost();

			result.add( new NewsFeedURL( anUrl, aTitle ) );	
		}
        return result;
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
	 * 
	 * @param anUrl
	 */
	public void add( NewsFeedURL aNewsFeedURL ) {
		this.newsSource.add( aNewsFeedURL );
	}
	
	/**
	 * 
	 * @param anUrl
	 */
	public void remove( URL anUrl ) {
		this.newsSource.remove( anUrl );
	}

    public void stop() {
        this.connected = false;
    }
    
    public void setProxy( String hostname, String port ) {
		if (  hostname == null || hostname.equals( "" ) ) {
			System.getProperties().put( "proxySet", "false" );
			return;
		}
				
		System.getProperties().put( "proxySet", "true" );
		System.getProperties().put( "proxyHost", hostname );
		System.getProperties().put( "proxyPort", port );
    }

	/**
	 * @return
	 */
	public List getNewsSource() {
		return newsSource;
	}
}

/*
$Log: RSSFetcher.java,v $
Revision 1.4  2003/10/13 08:18:17  lemmster
foobar

Revision 1.3  2003/10/04 08:49:25  lemmster
foobar

Revision 1.2  2003/09/29 14:05:45  lemmster
update & add still not working

Revision 1.1  2003/09/27 12:09:32  lemmster
initial commit

*/
