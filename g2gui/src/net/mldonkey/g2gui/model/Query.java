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

import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * Query
 *
 * @author $user$
 * @version $Id: Query.java,v 1.1 2003/06/16 21:48:57 lemmstercvs01 Exp $ 
 *
 */
public class Query implements Information {
	public static final byte AND = 0;
	public static final byte OR = 1; 
	public static final byte ANDNOT = 2;
	public static final byte MODULE = 3;
	public static final byte KEYWORD = 4;
	public static final byte MINSIZE = 5;
	public static final byte MAXSIZE = 6;
	public static final byte FORMAT = 7;
	public static final byte MEDIA = 8;
	public static final byte MP3ARTIST = 9;
	public static final byte MP3TITLE = 10;
	public static final byte MP3ALBUM = 11;
	public static final byte MP3BITRATE = 12;
	public static final byte HIDDEN = 13;
	
	/**
	 * The Type of this tree node 
	 */
	private byte treenode;
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
	public void readStream(MessageBuffer messageBuffer) {
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
		byte node = messageBuffer.readByte();

		if ( node == AND || node == OR || node == HIDDEN ) {
			short listElem = messageBuffer.readInt16();
			for ( int i = 0; i < listElem; i++ ) {
				this.queries = new Query[ listElem ];
				Query aQuery = new Query();
				aQuery.readStream( messageBuffer );
				queries[ i ] = aQuery;
			}
		}
		else if ( node == ANDNOT ) {
			Query fQuery = new Query();
			fQuery.readStream( messageBuffer );
			Query sQuery = new Query();
			sQuery.readStream( messageBuffer );
			this.setFAndNot( fQuery );
			this.setSAndNot( sQuery );
		}
		else if ( node == MODULE ) {
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
	 * @return a byte
	 */
	public byte getTreenode() {
		return treenode;
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
	 * @param b a byte
	 */
	public void setTreenode( byte b ) {
		treenode = b;
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

}

/*
$Log: Query.java,v $
Revision 1.1  2003/06/16 21:48:57  lemmstercvs01
opcode 3 added

*/