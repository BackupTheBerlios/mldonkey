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

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;

/**
 * SearchQuery is needed to transmit a search to the mldonkey. It needs to be created, and then filled
 * with setter(). When complete, it can be sent with this.send().
 *
 * @author $user$
 * @version $Id: SearchQuery.java,v 1.2 2003/07/04 16:54:30 dek Exp $ 
 *
 */
public class SearchQuery {
	private byte search;
	private String searchString;
	private final byte AND = 0;
	private final byte OR = 1;
	private final byte ANDNOT = 2;
	
	 
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
		
		/*we want to search remotely, so we can set this field here*/
		searchType = 1;
		
		/*The normal search-type should be AND (= type 0)
		 * could be changed by setter-method
		 */
		searchQuery.setNode( AND );
		search = AND;
		
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
		Query[] queries = new Query[patterns.length];
		for ( int i = 0; i < patterns.length; i++ ) {
			String pattern = patterns[i];
			queries[i] = new Query();
			queries[i].setNode( ( byte )4 );
			queries[i].setComment( "Search-pattern:" );
			queries[i].setDefaultValue( pattern );
		}
		searchQuery.setQueries( queries );
	}
	
	/**
	 * Setting the maximum-size of search-results
	 * @param size the size in bytes(??)
	 */
	public void setMaxSize( long size ) {
		Query maxSize = new Query();
		maxSize.setNode( ( byte ) 6 );
		maxSize.setComment( "Maximum size" );
		maxSize.setDefaultValue( String.valueOf( size ) );
		/* I do not know yet, what to do with this query, 
		 * so we give it back to the gc ;-
		 */
	}

	/**
	 * Setting the minimum of search-results
	 * @param size the size in bytes(??)
	 */
	public void setMinSize( long size ) {
		Query minSize = new Query();
		minSize.setNode( ( byte ) 6 );
		minSize.setComment( "Minimum size" );
		minSize.setDefaultValue( String.valueOf( size ) );
		/* I do not know yet, what to do with this query, 
		 * so we give it back to the gc ;-
		 */
	}
	/**
	 * Setting the file-format of search-results
	 * @param format_ the file-format (.avi, .mp3, .zip, .gif)
	 */
	public void setFormat( String format_ ) {
		Query format = new Query();
		format.setNode( ( byte ) 6 );
		format.setComment( "Format: " );
		format.setDefaultValue( format_ );
		/* I do not know yet, what to do with this query, 
		 * so we give it back to the gc ;-
		 */
	}

	/**
	 * @param i the Network we want to search in (see NetworkInfo for identifier)<br>
	 * (0=All)
	 */
	public void setNetwork( int i ) {
		network = i;
	}
	
	/**
	 * Sets the type of this search-query <br>
	 *  0 = AND<br>
	 *  1 = OR <br>
	 * ( 2 = AND NOT ) not yet implemented
	 * @param type The Type of this search
	 */
	public void setSearchType( byte type ) {
		if ( ( type == AND ) || ( type == OR )  ) {		
				searchQuery.setNode( type );
				this.search = type;
		}
				
	}
	
	/**
	 * sends out the search-query and hopefully get any results....
	 * 
	 */
	public void send() {
		ArrayList content = new ArrayList();
		content.add( new Integer( searchIdentifier ) );
		
//		/* now we need to get the Query sorted out in kind of Objects[] */
//			Object[] tempArray = this.searchQuery.toObjectArray();
//			for ( int i = 0; i < tempArray.length; i++ ) {
//				content.add( tempArray [ i ] );
//			}

		/* did we succeed, don't know yet, must doing some testing,
		 * something must be wrong, as mldonkey says: 
		 * 		decoding gui proto[16]: exception Invalid_argument("out-of-bound array or string access"), opcode 42
		 *		ascii: [ *(0)(1)(0)(0)(0)(0)(1)(0)(0)(0)(0)(0)(1)(0)(0)(0)(0)]
		 *		dec: [(42)(0)(1)(0)(0)(0)(0)(1)(0)(0)(0)(0)(0)(1)(0)(0)(0)(0)]
		 *
		 *So i do it the most simple way, which works indeed ;-)
		*/
		content.add( new Byte( ( byte )4 ) );
		content.add( "Comment" );
		content.add( this.searchString );
		
		
		content.add( new Integer( maxSearchResults ) );
		content.add( new Byte( searchType ) );
		content.add( new Integer( network ) );
		

		/* create the message content */
		
		Object[] temp = content.toArray();
		EncodeMessage consoleMessage = new EncodeMessage( ( short )42, content.toArray() );
		consoleMessage.sendMessage( this.getParent().getConnection() );
		//content = null;
		//consoleMessage = null;
	}

	/**
	 * @return our master, the coreCommunication-interface
	 */
	public CoreCommunication getParent() {
		return parent;
	}

}

/*
$Log: SearchQuery.java,v $
Revision 1.2  2003/07/04 16:54:30  dek
searching works ( with workaround)

Revision 1.1  2003/07/04 14:06:41  dek
first sketch of a search-query. still missing: convertig it to message and send out the thing

*/