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
 * SharedFileInfo
 *
 * @author $user$
 * @version $Id: SharedFileInfo.java,v 1.1 2003/06/15 16:17:26 lemmstercvs01 Exp $ 
 *
 */
public class SharedFileInfo implements Information {
	/**
	 * Shared File Identifier
	 */
	private int sharedFileId;
	/**
	 * Network Identifier
	 */
	private int networkId;
	/**
	 * Shared File Name
	 */
	private String sharedFileName;
	/**
	 * Shared File Size
	 */
	private int shareFileSize;
	/**
	 * Number of bytes uploaded
	 */
	private long numOfBytesUploaded;
	/**
	 * Number of Queries for this File
	 */
	private int numOfQueriesForFile;
	/**
	 * MD4
	 */
	private String md4;
	
	/**
	 * @return a string
	 */
	public String getMd4() {
		return md4;
	}

	/**
	 * @return an int
	 */
	public int getNetworkId() {
		return networkId;
	}

	/**
	 * @return a long
	 */
	public long getNumOfBytesUploaded() {
		return numOfBytesUploaded;
	}

	/**
	 * @return an int
	 */
	public int getNumOfQueriesForFile() {
		return numOfQueriesForFile;
	}

	/**
	 * @return an int
	 */
	public int getSharedFileId() {
		return sharedFileId;
	}

	/**
	 * @return a string
	 */
	public String getSharedFileName() {
		return sharedFileName;
	}

	/**
	 * @return an int
	 */
	public int getShareFileSize() {
		return shareFileSize;
	}

	/**
	 * @param string a string
	 */
	public void setMd4( String string ) {
		md4 = string;
	}

	/**
	 * @param i an int
	 */
	public void setNetworkId( int i ) {
		networkId = i;
	}

	/**
	 * @param l a long
	 */
	public void setNumOfBytesUploaded( long l ) {
		numOfBytesUploaded = l;
	}

	/**
	 * @param i an int
	 */
	public void setNumOfQueriesForFile( int i ) {
		numOfQueriesForFile = i;
	}

	/**
	 * @param i an int
	 */
	public void setSharedFileId( int i ) {
		sharedFileId = i;
	}

	/**
	 * @param string a string
	 */
	public void setSharedFileName( String string ) {
		sharedFileName = string;
	}

	/**
	 * @param i an int
	 */
	public void setShareFileSize( int i ) {
		shareFileSize = i;
	}

	/**
	 * Reads a SharedFileInfo object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		/*
		 * int32  	 Shared File Identifier 
		 * int32  	 Network Identifier 
		 * String  	 Shared File Name 
		 * int32  	 Shared File Size 
		 * int64  	 Number of Bytes Uploaded 
		 * int32  	 Number of Queries for that File 
		 * char[16]  	 Md4 
		 */
		this.setSharedFileId( messageBuffer.readInt32() );
		this.setNetworkId( messageBuffer.readInt32() );
		this.setSharedFileName( messageBuffer.readString() );
		this.setShareFileSize( messageBuffer.readInt32() );
		this.setNumOfBytesUploaded( messageBuffer.readInt64() );
		this.setNumOfQueriesForFile( messageBuffer.readInt32() );
		this.setMd4( messageBuffer.readBinary( 16 ) );
	}
}

/*
$Log: SharedFileInfo.java,v $
Revision 1.1  2003/06/15 16:17:26  lemmstercvs01
opcode 48 added

*/