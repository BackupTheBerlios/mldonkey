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

import java.net.URL;

import churchillobjects.rss4j.RssDocument;

/**
 * NewsFeedURL
 *
 * @version $Id: NewsFeedURL.java,v 1.1 2003/10/13 08:18:17 lemmster Exp $ 
 *
 */
public class NewsFeedURL {
	/**
	 * The URL to this newsfeed
	 */
	private URL url;
	/**
	 * The title of this newsfeed
	 */
	private String title;
	/**
	 * The newsfeed
	 */
	private RssDocument rssDocument;

	public NewsFeedURL( URL anURL, String aTitle ) {
		this.url = anURL;
		this.title = aTitle;
	}

	/**
	 * @return
	 */
	public RssDocument getRssDocument() {
		return rssDocument;
	}

	/**
	 * @param rssDocument
	 */
	public void setRssDocument( RssDocument rssDocument ) {
		this.rssDocument = rssDocument;
	}

	/**
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 */
	public void setTitle( String title ) {
		this.title = title;
	}

	/**
	 * @return
	 */
	public URL getUrl() {
		return url;
	}

	/**
	 * @param url
	 */
	public void setUrl( URL url ) {
		this.url = url;
	}
}

/*
$Log: NewsFeedURL.java,v $
Revision 1.1  2003/10/13 08:18:17  lemmster
foobar

*/