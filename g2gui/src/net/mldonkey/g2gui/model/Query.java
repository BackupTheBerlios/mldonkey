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
import java.util.List;

import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.model.enum.EnumQuery;

/**
 * Query
 *
 * @author $Author: lemmster $
 * @version $Id: Query.java,v 1.17 2003/08/22 21:03:15 lemmster Exp $ 
 *
 */
public class Query implements SimpleInformation {
	/**
	 * The Type of this tree node
	 */
	private Enum node;
	/**
	 * Queries for AND or OR or Hidden
	 */
	private List queries;
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
	 * creates an empty Query-Object
	 */
	public Query() {
		this.queries = new ArrayList();
	}
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
		this.setNode( messageBuffer.readByte() );

		if ( node == EnumQuery.AND || node == EnumQuery.OR || node == EnumQuery.HIDDEN ) {
			short listElem = messageBuffer.readInt16();
			for ( int i = 0; i < listElem; i++ ) {				
				Query aQuery = new Query();
				aQuery.readStream( messageBuffer );
				queries.add( aQuery ) ;
			}
		}
		else if ( node == EnumQuery.AND_NOT ) {
			Query fQuery = new Query();
			fQuery.readStream( messageBuffer );
			Query sQuery = new Query();
			sQuery.readStream( messageBuffer );
			this.fAndNot = fQuery;
			this.sAndNot = sQuery;
		}
		else if ( node == EnumQuery.MODULE ) {
			this.module = messageBuffer.readString();
			Query aQuery = new Query();
			aQuery.readStream( messageBuffer );
			this.mQuery = aQuery;
		}
		else {
			this.comment = messageBuffer.readString();
			this.defaultValue = messageBuffer.readString();
		}
	}
	
	/** (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "";
		result += "\n\t" + node;
			if ( node == EnumQuery.AND || node == EnumQuery.OR || node == EnumQuery.HIDDEN ) {
				for ( int i = 0; i < queries.size(); i++ ) {	
					result += queries.get( i ).toString();					
					}
				}
			else if ( node == EnumQuery.AND_NOT  ) { result += "\nAND_NOT_QUERY"; }
			else if ( node == EnumQuery.MODULE  ) { result += "\nMODULE_QUERY"; }
			else {
				result += "\n\t Comment: " + comment;
				result += "\n\t Value: " + defaultValue;
			}
		return result;
	}

	
	/**
	 * creates an byte-stream, representing this object (in order to send it to our beloved mldonkey
	 * via GUI-protocol)
	 * @return the byte-array
	 */
	protected Object[] toObjectArray() {
		List output = new ArrayList();
		output.add( new Byte( EnumQuery.getValue( ( EnumQuery ) node ) ) );
		
			if ( node == EnumQuery.AND
				|| node == EnumQuery.OR
				|| node == EnumQuery.HIDDEN )
			{			 	
			 	/*
			 	 * List of Queries for AND or OR or Hidden.
			 	 */	
			 	 
			 	/* setting List header */
				output.add( new Short( ( short ) queries.size() ) );	 

				/*now setting an entry for each Query in queries[]*/			 	
				for ( int i = 0; i < queries.size(); i++ ) {	
					Object[] querieArray = ( ( Query )queries.get( i ) ).toObjectArray();
					for ( int j = 0; j < querieArray.length; j++ ) {
						output.add( querieArray [ j ] );
					}
				}
			}
			else if ( node == EnumQuery.AND_NOT  ) {
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
			else if ( node == EnumQuery.MODULE  ) {
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
		/*creating the Query[], as a direct ClassCast to Query[] doesn't work..:-(*/
		Object[] temp = queries.toArray();
		Query[] result = new Query[temp.length];
		for ( int i = 0; i < result.length; i++ ) {
				result[ i ] = ( Query ) temp[ i ];
			}
		return result;		
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
	 * @param query a Query to be added to our query-list
	 */
	public void addQuery( Query query ) {
		this.queries.add( query );
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
	 * @return this querys type :
	 */
	public Enum getNode() {
		return node;
	}

	/**
	 * @param b the typ to set
	 */
	private void setNode( byte b ) {
		/*	
 	  	 * 0 And(x,y,...)
 		 * 1: Or(x,y,...)
		 * 2: Andnot(x,y)
		 * 3: Module (name, query)
		 * 4: Keywords(comment, default)
		 * 5: Minsize(comment, default)>
		 * 6: Maxsize(comment, default)
		 * 7: Format(comment, default)
		 * 8: Media(comment, default)
		 * 9: Mp3 Artist(comment, default)
		 * 10: Mp3 Title(comment, default)
		 * 11: Mp3 Album(comment, default)
		 * 12: Mp3 Bitrate(comment, default)
		 * 13: Hidden(fields)
		 */
		if ( b == 0 )
			node = EnumQuery.AND;
		else if ( b == 1 )
			node = EnumQuery.OR;	
		else if ( b == 2 )
			node = EnumQuery.AND_NOT;	
		else if ( b == 3 )
			node = EnumQuery.MODULE;	
		else if ( b == 4 )
			node = EnumQuery.KEYWORDS;	
		else if ( b == 5 )
			node = EnumQuery.MINSIZE;	
		else if ( b == 6 )
			node = EnumQuery.MAXSIZE;	
		else if ( b == 7 )
			node = EnumQuery.FORMAT;	
		else if ( b == 8 )
			node = EnumQuery.MEDIA;	
		else if ( b == 9 )
			node = EnumQuery.MP3_ARTIST;	
		else if ( b == 10 )
			node = EnumQuery.MP3_TITLE;	
		else if ( b == 11 )
			node = EnumQuery.MP3_ALBUM;	
		else if ( b == 12 )
			node = EnumQuery.MP3_BITRATE;	
		else if ( b == 13 )
			node = EnumQuery.HIDDEN;
	}
	/**
	 * @param string The comment to set
	 */
	public void setComment( String string ) {
		this.comment = string;
	}
	/**
	 * @param pattern The search pattern to set
	 */
	public void setDefaultValue( String pattern ) {
		this.defaultValue = pattern;
	}
	/**
	 * 
	 * @param enum The EnumQuery to set
	 */
	public void setNode( EnumQuery enum ) {
		this.node = enum;		
	}
}

/*
$Log: Query.java,v $
Revision 1.17  2003/08/22 21:03:15  lemmster
replace $user$ with $Author$

Revision 1.16  2003/07/10 10:59:21  dek
toString() created (still very raw-output, but one can see, what's inside..)

Revision 1.15  2003/07/06 14:13:22  dek
"one-word-searching" now also works, only little bug

Revision 1.14  2003/07/06 07:45:26  lemmstercvs01
checkstyle applied

Revision 1.13  2003/07/06 07:36:42  lemmstercvs01
EnumQuery added

Revision 1.12  2003/07/05 14:04:12  dek
all in order for searching

Revision 1.11  2003/07/05 09:52:49  dek
searching rocks ;-)

Revision 1.10  2003/07/04 22:59:15  dek
now works without work-around

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