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
 * @author $Author: lemmster $
 * @version $Id: SharedFileInfo.java,v 1.4 2003/08/22 21:03:15 lemmster Exp $ 
 *
 */
public class SharedFileInfo implements SimpleInformation {
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
	 * @return The file md4
	 */
	public String getMd4() {
		return md4;
	}

	/**
	 * @return The network identifier of this file
	 */
	public int getNetworkId() {
		return networkId;
	}

	/**
	 * @return number of bytes uploaded of this file
	 */
	public long getNumOfBytesUploaded() {
		return numOfBytesUploaded;
	}

	/**
	 * @return Number of queries for this file
	 */
	public int getNumOfQueriesForFile() {
		return numOfQueriesForFile;
	}

	/**
	 * @return The file identifier
	 */
	public int getSharedFileId() {
		return sharedFileId;
	}

	/**
	 * @return The name of this file
	 */
	public String getSharedFileName() {
		return sharedFileName;
	}

	/**
	 * @return The size of this file
	 */
	public int getShareFileSize() {
		return shareFileSize;
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
		this.sharedFileId = messageBuffer.readInt32();
		this.networkId = messageBuffer.readInt32();
		this.sharedFileName = messageBuffer.readString();
		this.shareFileSize = messageBuffer.readInt32();
		this.numOfBytesUploaded = messageBuffer.readInt64();
		this.numOfQueriesForFile = messageBuffer.readInt32();
		this.md4 = messageBuffer.readBinary( 16 );
	}
}

/*
$Log: SharedFileInfo.java,v $
Revision 1.4  2003/08/22 21:03:15  lemmster
replace $user$ with $Author$

Revision 1.3  2003/07/05 20:04:02  lemmstercvs01
javadoc improved

Revision 1.2  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.1  2003/06/15 16:17:26  lemmstercvs01
opcode 48 added

*/