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

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * ResultInfo
 *
 * @author $user$
 * @version $Id: ResultInfo.java,v 1.8 2003/07/23 23:49:22 lemmstercvs01 Exp $ 
 *
 */
public class ResultInfo extends Parent {
	private String comment;
	/**
	 * Result ID
	 */
	private int resultID;
	/**
	 * The Network this result comes from
	 */
	private NetworkInfo networkID;
	/**
	 * Possible names
	 */
	private String[] names;
	/**
	 * MD4
	 */
	private String md4;
	/**
	 * Size
	 */
	private int size;
	/**
	 * The size rounded with metric unit
	 */
	private String stringSize;
	/**
	 * Format
	 */
	private String format;
	/**
	 * Type
	 */
	private String type;
	/**
	 * Metatags
	 */
	private Tag[] tags;
	/**
	 * true = Normal, false = Already Downloaded 
	 */
	private boolean history;
	
	/**
	 * Creates a new ResultInfo
	 * @param core The parent
	 */
	public ResultInfo( CoreCommunication core ) {
		super( core );
	}
	
	/** (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = new String();
		result += "ID: " + resultID;
		result += "\n Network: " + networkID;		
		result += "\n filenames: ";
			for ( int i = 0; i < names.length; i++ ) {
				result += names[ i ] + " ";
			}
		result += "\n Md4: " + md4;
		result += "\n FileSize: " + size;
		result += "\n File-Format: " + format;
		result += "\n File-Typ: " + type;		
		result += "\n MetaData: ";
		for ( int i = 0; i < tags.length; i++ ) {
			result += tags[ i ].getName() + " - " + tags[ i ].getValue();
					}
		result += "\n comment: " + comment;
		result += "\n status: " + history;		
		return result;
	}

	/**
	 * Reads a ResulfInfo from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		/*
		 * int32		Result Identifier 
		 * int32		Result Network Identifier 
		 * List of 	String	Possible File Names 
		 * char[16]	File Md4 (binary) 
		 * int32		File Size 
		 * String		File Format 
		 * String		File Type 
		 * List of Tag	File Metadata 
		 * String		Comment 
		 * int8			0 = Normal, 1 = Already Downloaded
		 */
		this.resultID = messageBuffer.readInt32();
		this.setNetworkID( messageBuffer.readInt32() );
		this.names = messageBuffer.readStringList();
		this.md4 = messageBuffer.readBinary( 16 );
		this.size = messageBuffer.readInt32() / 1024;
		this.format = messageBuffer.readString();
		this.type = messageBuffer.readString();
		this.tags = messageBuffer.readTagList();		
		this.comment = messageBuffer.readString();		
		this.setHistory( messageBuffer.readByte() ); 

		this.stringSize = this.calcStringSize( this.getSize() );
	}

	/**
	 * creates a String from the size
	 * @param size The size
	 * @return a string represantation of this size
	 */	
	private String calcStringSize( int size ) {
		double temp = 0;
		String unit = null;
		if ( size > 1000000 ) {
			temp = ( size / 1024.0 / 1024.0 );			
			unit = "GB";
		}
		else if ( size > 1000 ) {
			temp = ( size / 1024.0 );
			unit = "MB";		
		}
		else {
			temp = size;
			unit = "KB";
		}
		/* round */
		temp = ( double )( ( int )( temp * 100 ) ) / 100;
		
		return new String( temp + " " + unit );
	}
	
	/**
	 * @param string file comment
	 */
	private void setComment( String string ) {
		this.comment = string;
	}

	/**
	 * @return The format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @return The History (true = normal, false = alread downloaded)
	 */
	public boolean getHistory() {
		return history;
	}

	/**
	 * @return The MD4
	 */
	public String getMd4() {
		return md4;
	}

	/**
	 * @return Possible Names
	 */
	public String[] getNames() {
		return names;
	}

	/**
	 * @return The Network id
	 */
	public NetworkInfo getNetworkID() {
		return networkID;
	}

	/**
	 * @return The result id
	 */
	public int getResultID() {
		return resultID;
	}

	/**
	 * @return The size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @return The Metadata
	 */
	public Tag[] getTags() {
		return tags;
	}

	/**
	 * @return The Type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @return
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param b The History as a byte
	 */
	private void setHistory( byte b ) {
		if ( b == 0 )
			history = true;
		else 
			history = false;
	}

	/**
	 * translate the int to networkinfo
	 * @param i the int
	 */
	private void setNetworkID( int i ) {
		this.networkID =
		( NetworkInfo ) this.parent.getNetworkInfoMap().infoIntMap.get( i );
	}

	/**
	 * @param info
	 */
	public void setNetworkID(NetworkInfo info) {
		networkID = info;
	}

	/**
	 * @return The size rounded with metric unit
	 */
	public String getStringSize() {
		return stringSize;
	}
}

/*
$Log: ResultInfo.java,v $
Revision 1.8  2003/07/23 23:49:22  lemmstercvs01
stringSize for better display design added

Revision 1.7  2003/07/23 17:01:24  lemmstercvs01
added getComment() and setNetworkID(Networkinfo info)

Revision 1.6  2003/07/06 08:49:33  lemmstercvs01
better oo added

Revision 1.5  2003/07/06 07:45:26  lemmstercvs01
checkstyle applied

Revision 1.4  2003/07/06 07:29:47  lemmstercvs01
javadoc improved

Revision 1.3  2003/07/05 13:14:50  dek
*** empty log message ***

Revision 1.2  2003/07/05 12:52:19  lemmstercvs01
bugfix in readStream()

Revision 1.1  2003/06/30 07:19:47  lemmstercvs01
initial commit (untested)

*/