/*
 * Copyright 2003
 * G2GUI Team
 * 
 * 
 * This file is part of G2GUI.
 *
 * G2GUI is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2GUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2GUI; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.model;

import java.util.ArrayList;
import java.util.List;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.model.enum.EnumQuery;

/**
 * SearchQuery is needed to transmit a search to the mldonkey.
 * It needs to be created, and then filled with setter().
 * When complete, it can be sent with this.send().
 *
 * @author $user$
 * @version $Id: SearchQuery.java,v 1.8 2003/07/06 07:45:26 lemmstercvs01 Exp $ 
 *
 */
public class SearchQuery {
	/**
	 * some options for the search : minFileSize, maxFileSize, etc...
	 */
	private Query searchOptions;
	
	/**
	 * This is the wrapper for options & search-query
	 */
	private Query mainQuery;
	
	/**
	 * The String we want to search
	 */
	private String searchString;	
	 
	/**
	 * The CoreCommunication
	 */	
	private CoreCommunication parent;
	
	/**
	 * static counter to be sure to generate a unique search identifier for every new search
	 */
	private static int counter = 0;
	
	/**
	 * every search needs a unique search-identifier
	 */
	private int searchIdentifier;
	
	/**
	 * what do we want to search, this is the field we have to fill
	 * (Query where default fields are filled with the values entered by the user)
	 */
	private Query searchQuery;
	
	/** 
	 * Maximal number of results (used ?) 
	 */
	private int maxSearchResults;
	
	/**
	 * Search type (0=local, 1=remote, 2=subscription) 
	 */
	private byte searchType;
	
	/**
	 * Network (0=All)
	 */
	private int network;
	
	/**
	 * Default constructor for creating a empty SearchQuery
	 * @param core The CoreCommunication parent
	 */

	public SearchQuery( CoreCommunication core ) {
		this.parent = core;
		/* create a unique searchIdentifier */
		searchIdentifier = counter + 1;
		counter++;
		
		/*this is, what we need to fill with values*/
		searchQuery = new Query();
		
		/*and this are the options for our search:*/
		searchOptions = new Query();
		searchOptions.setNode( EnumQuery.AND );
				
		/*This is the wrapper for options & search-query*/
		mainQuery = new Query();
		mainQuery.setNode( EnumQuery.AND );
		mainQuery.addQuery( searchOptions );			
		mainQuery.addQuery( searchQuery );			
		
		/*we want to search remotely, so we can set this field here*/
		searchType = 1;
		
		/*The normal search-type should be AND (= type 0)
		 * could be changed by setter-method
		 */
		searchQuery.setNode( EnumQuery.AND );

		/*
		 * default is searching in all networks:
		 */
		network = 0;
	}
	/**
	 * @param i the maximal number of wanted searchresults
	 */
	public void setMaxSearchResults( int i ) {
		maxSearchResults = i;
	}

	/**
	 * This informs the receiver what should be searched.
	 * @param searchString simple String, with space-separated Search-patterns
	 */
	public void setSearchString( String searchString ) {
		this.searchString = searchString;
		String[] patterns = searchString.split( " " );
		/* now we have to generate a query-Object for each search pattern */
		Query newQuery;
		for ( int i = 0; i < patterns.length; i++ ) {				
			newQuery = new Query();
			String pattern = patterns[i];			
			newQuery.setNode( EnumQuery.KEYWORDS );
			newQuery.setComment( "Search-pattern:" );
			newQuery.setDefaultValue( pattern );
			searchQuery.addQuery( newQuery );
		}
	}
	
	/**
	 * Setting the maximum-size of search-results
	 * @param size the size in bytes(??)
	 */
	public void setMaxSize( long size ) {
		Query maxSize = new Query();
		maxSize.setNode( EnumQuery.MAXSIZE );
		maxSize.setComment( "Maximum size" );
		maxSize.setDefaultValue( String.valueOf( size ) );
		/*
		 * add this query to the list in searchOptions
		 */
		searchOptions.addQuery( maxSize );
	}

	/**
	 * Setting the minimum of search-results
	 * @param size the size in bytes(??)
	 */
	public void setMinSize( long size ) {
		Query minSize = new Query();
		minSize.setNode( EnumQuery.MINSIZE );
		minSize.setComment( "Minimum size" );
		minSize.setDefaultValue( String.valueOf( size ) );		
		/*
		 * add this query to the list in searchOptions
		 */
		searchOptions.addQuery( minSize );
	}
	/**
	 * Setting the file-format of search-results
	 * @param format_ the file-format (.avi, .mp3, .zip, .gif)
	 */
	public void setFormat( String format_ ) {
		Query format = new Query();
		format.setNode( EnumQuery.FORMAT );
		format.setComment( "Format: " );
		format.setDefaultValue( format_ );
		/*
		 * add this query to the list in searchOptions
		 */
		searchOptions.addQuery( format );
	}

	/**
	 * @param i the Network we want to search in (see NetworkInfo for identifier)<br>
	 * (0=All)
	 */
	public void setNetwork( int i ) {
		network = i;
	}
	
	/**
	 * Sets the type of this search-query
	 * @param enum The Type of this search
	 */
	public void setSearchType( EnumQuery enum ) {
		if ( ( enum == EnumQuery.AND ) || ( enum == EnumQuery.OR )  ) {		
				searchQuery.setNode( enum );				
		}
	}
	
	/**
	 * sends out the search-query and hopefully get any results....
	 * 
	 */
	public void send() {
		List content = new ArrayList();
		content.add( new Integer( searchIdentifier ) );
		
		/* now we need to get the Query sorted out in kind of Objects[] */
		Object[] tempArray = this.mainQuery.toObjectArray();
		for ( int i = 0; i < tempArray.length; i++ ) {				
			content.add( tempArray [ i ] );
		}
		
		content.add( new Integer( maxSearchResults ) );
		content.add( new Byte( searchType ) );
		content.add( new Integer( network ) );
		
		/* create the message content */
		EncodeMessage consoleMessage =
			new EncodeMessage( Message.S_SEARCH_QUERY, content.toArray() );
		consoleMessage.sendMessage( this.getParent().getConnection() );
	}

	/**
	 * @return our master, the coreCommunication-interface
	 */
	private CoreCommunication getParent() {
		return parent;
	}
}

/*
$Log: SearchQuery.java,v $
Revision 1.8  2003/07/06 07:45:26  lemmstercvs01
checkstyle applied

Revision 1.7  2003/07/06 07:36:42  lemmstercvs01
EnumQuery added

Revision 1.6  2003/07/05 14:04:12  dek
all in order for searching

Revision 1.5  2003/07/05 13:25:43  dek
removed old comments

Revision 1.4  2003/07/05 09:52:49  dek
searching rocks ;-)

Revision 1.3  2003/07/04 22:59:15  dek
now works without work-around

Revision 1.2  2003/07/04 16:54:30  dek
searching works ( with workaround)

Revision 1.1  2003/07/04 14:06:41  dek
first sketch of a search-query. still missing: convertig it to message and send out the thing

*/