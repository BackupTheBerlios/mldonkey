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
 * ResultInfo
 *
 * @author $user$
 * @version $Id: ResultInfo.java,v 1.1 2003/06/30 07:19:47 lemmstercvs01 Exp $ 
 *
 */
public class ResultInfo implements SimpleInformation {
	/**
	 * Result ID
	 */
	private int resultID;
	/**
	 * Network ID
	 */
	private int networkID;
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
		this.setResultID( messageBuffer.readInt32() );
		this.setNetworkID( messageBuffer.readInt32() );
		this.setNames( messageBuffer.readStringList() );
		this.setMd4( messageBuffer.readBinary( 16 ) );
		this.setFormat( messageBuffer.readString() );
		this.setType( messageBuffer.readString() );
		this.setTags( messageBuffer.readTagList() );		
		this.setHistory( messageBuffer.readByte() ); 
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
	public int getNetworkID() {
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
	 * @param string The format as a String
	 */
	private void setFormat( String string ) {
		format = string;
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
	 * @param string The md4 as a String
	 */
	private void setMd4( String string ) {
		md4 = string;
	}

	/**
	 * @param strings The possible Names as a String[]
	 */
	private void setNames( String[] strings ) {
		names = strings;
	}

	/**
	 * @param i The network id as an int
	 */
	private void setNetworkID( int i ) {
		networkID = i;
	}

	/**
	 * @param i The result id as an int
	 */
	private void setResultID( int i ) {
		resultID = i;
	}

	/**
	 * @param i The size as an int
	 */
	private void setSize( int i ) {
		size = i;
	}

	/**
	 * @param tags The Metatags as a Tag[]
	 */
	private void setTags( Tag[] tags ) {
		this.tags = tags;
	}

	/**
	 * @param string The Type as a String
	 */
	private void setType( String string ) {
		type = string;
	}
}

/*
$Log: ResultInfo.java,v $
Revision 1.1  2003/06/30 07:19:47  lemmstercvs01
initial commit (untested)

*/