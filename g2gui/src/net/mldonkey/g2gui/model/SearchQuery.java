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

import gnu.regexp.RE;
import gnu.regexp.REException;
import gnu.regexp.REMatch;

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
 *
 * @version $Id: SearchQuery.java,v 1.19 2003/09/04 12:35:57 dek Exp $ 
 *
 */
public class SearchQuery implements Sendable {
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
		
		/* this is, what we need to fill with values
		 * 
		 * The normal search-type should be AND (= type 0)
		 * could be changed by setter-method
		 */
		searchQuery = new Query();
		searchQuery.setNode( EnumQuery.AND );
		
		/*and this are the options for our search:*/
		searchOptions = new Query();
		searchOptions.setNode( EnumQuery.AND );
				
		
		/*we want to search remotely, so we can set this field here*/
		searchType = 1;
		

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
		String[] patterns = split( searchString, ' ' );
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
	 * Setting the Bitrate of the MP3-File for the search
	 * @param bitrate of the mp3-file
	 */
	public void setMp3Bitrate( String bitrate ) {
		Query bitrateQuery = new Query();
		bitrateQuery.setNode( EnumQuery.MP3_BITRATE );
		bitrateQuery.setComment( "MP3-Bitrate" );
		bitrateQuery.setDefaultValue( bitrate );
		/*
		 * add this query to the list in searchOptions
		 */
		searchOptions.addQuery( bitrateQuery );
	}
	
	/**
	 * Setting the Artist of the MP3-File for the search
	 * @param artist of the mp3-file
	 */
	public void setMp3Artist( String artist ) {
		Query artistQuery = new Query();
		artistQuery.setNode( EnumQuery.MP3_ARTIST );
		artistQuery.setComment( "MP3-Artist" );
		artistQuery.setDefaultValue( artist );
		/*
		 * add this query to the list in searchOptions
		 */
		searchOptions.addQuery( artistQuery );
	}
	
	/**
	 * Setting the Album of the search
	 * @param album of the mp3-file
	 */
	public void setMp3Album( String album ) {
		Query albumQuery = new Query();
		albumQuery.setNode( EnumQuery.MP3_TITLE );
		albumQuery.setComment( "MP3-Album" );
		albumQuery.setDefaultValue( album );
		/*
		 * add this query to the list in searchOptions
		 */
		searchOptions.addQuery( albumQuery );
	}
	
	/**
	 * Setting the MP3-Title of the search
	 * @param title of the mp3-file
	 */
	public void setMp3Title( String title ) {
		Query titleQuery = new Query();
		titleQuery.setNode( EnumQuery.MP3_TITLE );
		titleQuery.setComment( "MP3-Title" );
		titleQuery.setDefaultValue( title );
		/*
		 * add this query to the list in searchOptions
		 */
		searchOptions.addQuery( titleQuery );
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
	 * Sets the Media-Type of this search-query
	 * it is 
	 * <br>Audio (type = 1) or
	 * <br>Video (type = 2) all other values are ignored: nothing happens
	 * @param aString  Audio / Video / ...
	 */
	public void setMedia( String aString ) {
		Query format = new Query();
		format.setNode( EnumQuery.MEDIA );
		format.setComment( "Media: " );		
		/*
		 * add this query to the list in searchOptions
		 */		
		format.setDefaultValue( aString );
		searchOptions.addQuery( format );
	}
	
	/**
	 * Sets the type of this search-query (AND / OR)
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
		/* if we have only one SearchQuery or SearchOptions,
		 * we don't need to build the whole tree-structure
		 * and we can directly set the searchQuery /searchOptions
		 */ 
		if ( searchQuery.getQueries().length == 1 )
			searchQuery = searchQuery.getQueries()[ 0 ];
					
		if ( searchOptions.getQueries().length == 1 )
			searchOptions = searchOptions.getQueries()[ 0 ];
		
		/* now we do know, what our search exactly looks like, 
		 * we can build the whole thing correct
		 *
		 * This is the wrapper for options & search-query
		 */
		mainQuery = new Query();
		mainQuery.setNode( EnumQuery.AND );	
		mainQuery.addQuery( searchOptions );			
		mainQuery.addQuery( searchQuery );
			
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
		Message consoleMessage =
			new EncodeMessage( Message.S_SEARCH_QUERY, content.toArray() );
		consoleMessage.sendMessage( this.getParent().getConnection() );
	}

	/**
	 * @return our master, the coreCommunication-interface
	 */
	private CoreCommunication getParent() {
		return parent;
	}
	
	/**
	 * @return The identifier for this searchquery
	 */
	public int getSearchIdentifier() {
		return searchIdentifier;
	}
	
	private String[] split( String searchString, char delimiter ) {
			RE regex = null;
			String expression = "([^" + delimiter + "])*";		
			try {
				regex = new RE( expression );
			} catch ( REException e ) {
				e.printStackTrace();
			}
			ArrayList patterns = new ArrayList();		
			REMatch[] matches = regex.getAllMatches( searchString );
			for ( int i = 0; i < matches.length; i++ ) {
				String match = matches[ i ].toString();			
				if ( !match.equals( "" ) )
					patterns.add( match );
			}
			Object[] temp = patterns.toArray();
			String[] result = new String[ temp.length ];
			for ( int i = 0; i < temp.length; i++ ) {
				result[ i ] = ( String ) temp[ i ];
			}
			return result;
		}
	
}

/*
$Log: SearchQuery.java,v $
Revision 1.19  2003/09/04 12:35:57  dek
added setter

Revision 1.18  2003/08/23 15:21:37  zet
remove @author

Revision 1.17  2003/08/23 10:02:02  lemmster
use supertype where possible

Revision 1.16  2003/08/22 21:03:14  lemmster
replace $user$ with $Author: dek $

Revision 1.15  2003/08/09 15:32:45  dek
removed unused import

Revision 1.14  2003/08/09 15:32:14  dek
added gnu.regexp for compiling with gcj
you can get it at:

ftp://ftp.tralfamadore.com/pub/java/gnu.regexp-1.1.4.tar.gz

Revision 1.13  2003/07/25 14:46:50  zet
replace string.split

Revision 1.12  2003/07/23 17:01:40  lemmstercvs01
modified setMedia()

Revision 1.11  2003/07/21 16:25:10  dek
setMedia(AUDIO | VIDEO) added

Revision 1.10  2003/07/06 14:13:22  dek
"one-word-searching" now also works, only little bug

Revision 1.9  2003/07/06 08:49:33  lemmstercvs01
better oo added

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