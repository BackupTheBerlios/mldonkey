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
package net.mldonkey.g2gui.model;


import java.util.ArrayList;

import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * Query
 *
 * @author $user$
 * @version $Id: Query.java,v 1.9 2003/07/04 16:53:56 dek Exp $ 
 *
 */
public class Query implements SimpleInformation {
	
	/**
	 * The Type of this tree node
	 */
	private byte node;
	
	/**
	 * Queries for AND or OR or Hidden
	 */
	private Query[] queries;
	/**
	 * First Argument of Andnot
	 */
	private Query fAndNot;
	/**
	 * Second Argument of Andnot
	 */
	private Query sAndNot;
	/**
	 * Name of Module
	 */
	private String module;
	/**
	 * Query inside Module
	 */
	private Query mQuery;
	/**
	 * Comment
	 */
	private String comment;
	/**
	 * Default Value
	 */
	private String defaultValue;

	/**
	 * Reads a Query object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		/*
		 * int8		The Type of this tree node 
	   	 * List of Query	Queries for AND or OR or Hidden 
   	 	 * Query	First Argument of Andnot (present ONLY IF Node Type = 2) 
   	 	 * Query	Second Argument of Andnot (present ONLY IF Node Type = 2) 
   	 	 * String	Name of Module (present ONLY IF Node Type = 3) 
   	 	 * Query	Query inside Module (present ONLY IF Node Type = 3) 
   	 	 * String	Comment (present ONLY IF Node Type = 4,5,6,7,8,9,10,11 or 12) 
   	 	 * String	Default Value (present ONLY IF Node Type = 4,5,6,7,8,9,10,11 or 12) 
		 */
		node = messageBuffer.readByte();

		if ( node == 0 || node == 1 || node == 13 ) {
			short listElem = messageBuffer.readInt16();
			for ( int i = 0; i < listElem; i++ ) {
				this.queries = new Query[ listElem ];
				Query aQuery = new Query();
				aQuery.readStream( messageBuffer );
				queries[ i ] = aQuery;
			}
		}
		else if ( node == 2 ) {
			Query fQuery = new Query();
			fQuery.readStream( messageBuffer );
			Query sQuery = new Query();
			sQuery.readStream( messageBuffer );
			this.setFAndNot( fQuery );
			this.setSAndNot( sQuery );
		}
		else if ( node == 3 ) {
			this.setModule( messageBuffer.readString() );
			Query aQuery = new Query();
			aQuery.readStream( messageBuffer );
			this.setMQuery( aQuery );
		}
		else {
			this.setComment( messageBuffer.readString() );
			this.setDefaultValue( messageBuffer.readString() );
		}
	}
	
	/**
	 * creates an byte-stream, representing this object (in order to send it to our beloved mldonkey
	 * via GUI-protocol)
	 * @return the byte-array
	 */
	public Object[] toObjectArray() {
		ArrayList output = new ArrayList();
		output.add( new Byte( node ) );
		
			 if ( node == 0 || node == 1 || node == 13 ) {
			 	
			 	/*
			 	 * List of Queries for AND or OR or Hidden, I assume, only 1-level searches are made
			 	 * So i don't need to implement multi-leve AND/OR (=>only comment / value pairs are read out from
			 	 * the Query-objects)
			 	 */			 	 
			 	Object[] listOfQueries = new Object[queries.length];
				 	for ( int i = 0; i < listOfQueries.length; i++ ) {	
				 		Object[] row =  {queries[ i ].getComment(), queries[ i ].getDefaultValue() };					 
						listOfQueries[ i ] = row;
						
						//[ 0 ] = queries[ i ].getComment();
						//listOfQueries[ i ][ 1 ] = queries[ i ].getDefaultValue();
					}
					
			 	output.add( listOfQueries );
			 }
			else if ( node == 2  ) {
				/*
				 * Query: First Argument of Andnot
				 */
					Object[] fTempArray = fAndNot.toObjectArray();
					for ( int i = 0; i < fTempArray.length; i++ ) {
						output.add( fTempArray [ i ] );
					}
				 
				 /*
				  * Query: Second Argument of Andnot
				  */
					Object[] sTempArray = sAndNot.toObjectArray();
					for ( int i = 0; i < sTempArray.length; i++ ) {
						output.add( sTempArray [ i ] );
					}
				
			}
			else if ( node == 3  ) {
				/*
				 * String: 	 Name of Module
				 */ 
					output.add( module );
				/* 
				 * Query  :	 Query inside Module
				 */
					Object[] tempArray = mQuery.toObjectArray();
					for ( int i = 0; i < tempArray.length; i++ ) {
						output.add( tempArray [ i ] );
					}
			}
			else {
				/*
				 * String: Comment
				 */ 
					output.add( comment );
				/* 
				 * String: DefaultValue
				 */				
					output.add( defaultValue );
			}
		 
		return output.toArray();
	}
	
	/**
	 * @return a Query
	 */
	public Query getFAndNot() {
		return fAndNot;
	}

	/**
	 * @return a Query[]
	 */
	public Query[] getQueries() {
		return queries;
	}

	/**
	 * @return a Query
	 */
	public Query getSAndNot() {
		return sAndNot;
	}

	/**
	 * @param query a Query
	 */
	public void setFAndNot( Query query ) {
		fAndNot = query;
	}

	/**
	 * @param queries a Query[]
	 */
	public void setQueries( Query[] queries ) {
		this.queries = queries;
	}

	/**
	 * @param query a Query
	 */
	public void setSAndNot( Query query ) {
		sAndNot = query;
	}

	/**
	 * @return a string
	 */
	public String getModule() {
		return module;
	}

	/**
	 * @return aQuery
	 */
	public Query getMQuery() {
		return mQuery;
	}

	/**
	 * @param string a string
	 */
	public void setModule( String string ) {
		module = string;
	}

	/**
	 * @param query a Query
	 */
	public void setMQuery( Query query ) {
		mQuery = query;
	}

	/**
	 * @return a string
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @return a string
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param string a string
	 */
	public void setComment( String string ) {
		comment = string;
	}

	/**
	 * @param string a string
	 */
	public void setDefaultValue( String string ) {
		defaultValue = string;
	}

	/**
	 * @return this querys type :
	 *  0 : And(x,y,...) -> x AND y AND ... 
	 *  1 : Or(x,y,...) -> x OR y OR ... 
	 *  2 : Andnot(x,y) -> x AND NOT y 
	 *  3 : Module (name, query) -> fields of 'query' to be display in a frame 'name' 
	 *  4 : Keywords(comment, default) -> a text field, display 'comment' in front, and default value is 'default' 
	 *  5 : Minsize(comment, default) 
	 *  6 : Maxsize(comment, default) 
	 *  7 : Format(comment, default) 
	 *  8 : Media(comment, default) 
	 *  9 : Mp3 Artist(comment, default) 
	 *  10 : Mp3 Title(comment, default) 
	 *  11 : Mp3 Album(comment, default) 
	 *  12 : Mp3 Bitrate(comment, default) 
	 *  13 : Hidden(fields) -> A list of fields whose values cannot be changed by the 
	 */
	public byte getNode() {
		return node;
	}

	/**
	 * set this querys type :<br>
	 *  0 : And(x,y,...) -> x AND y AND ... <br>
	 *  1 : Or(x,y,...) -> x OR y OR ... <br>
	 *  2 : Andnot(x,y) -> x AND NOT y <br>
	 *  3 : Module (name, query) -> fields of 'query' to be display in a frame 'name' <br>
	 *  4 : Keywords(comment, default) -> a text field, display 'comment' in front, and default value is 'default' <br>
	 *  5 : Minsize(comment, default) <br>
	 *  6 : Maxsize(comment, default) <br>
	 *  7 : Format(comment, default) <br>
	 *  8 : Media(comment, default) <br>
	 *  9 : Mp3 Artist(comment, default) <br>
	 *  10 : Mp3 Title(comment, default) <br>
	 *  11 : Mp3 Album(comment, default) <br>
	 *  12 : Mp3 Bitrate(comment, default) <br>
	 *  13 : Hidden(fields) -> A list of fields whose values cannot be changed by the <br>
	 * @param b the typ to set
	 */
	public void setNode( byte b ) {
		node = b;
	}

}

/*
$Log: Query.java,v $
Revision 1.9  2003/07/04 16:53:56  dek
searching started

Revision 1.8  2003/07/04 14:14:34  dek
added todo (but not today ;-))

Revision 1.7  2003/07/04 14:07:06  dek
*** empty log message ***

Revision 1.6  2003/07/04 13:27:50  dek
inserted attribute node-type

Revision 1.5  2003/06/24 09:18:51  lemmstercvs01
typo fixed

Revision 1.4  2003/06/24 09:16:48  lemmstercvs01
better Enum added

Revision 1.3  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.2  2003/06/17 12:07:37  lemmstercvs01
checkstyle applied

Revision 1.1  2003/06/16 21:48:57  lemmstercvs01
opcode 3 added

*/