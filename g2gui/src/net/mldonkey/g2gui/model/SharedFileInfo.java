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

import gnu.regexp.RE;
import gnu.regexp.REException;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.helper.RegExp;

/**
 * SharedFileInfo
 *
 *
 * @version $Id: SharedFileInfo.java,v 1.17 2003/11/23 17:58:03 lemmster Exp $ 
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
	private String sharedFileName = "";
	/**
	 * Shared File Size
	 */
	private long shareFileSize = 0;
	/**
	 * Number of bytes uploaded
	 */
	private long numOfBytesUploaded = 0;
	/**
	 * String representation of bytes uploaded
	 */
	private String stringOfBytesUploaded = "";

	/**
	 * Number of Queries for this File
	 */
	private int numOfQueriesForFile = 0;
	/**
	 * MD4
	 */
	private String md4;
	private NetworkInfo network;
	
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
	public String getName() {		 
		return sharedFileName;
	}

	/**
	 * @return The size of this file
	 */
	public long getSize() {
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
		
		try {
			/*  remove the whole path in front of filename
			 *  /path/to/file -> file 
			 */
			RE re = new RE( "[^/]*$" );
			this.sharedFileName = re.getMatch( this.sharedFileName ).toString();
		} catch ( REException e ) { System.out.println( "invalid RE in SharedFileInfo.java" ); }
		
		this.shareFileSize = messageBuffer.readInt32();
		this.numOfBytesUploaded = messageBuffer.readInt64();
		this.numOfQueriesForFile = messageBuffer.readInt32();
		this.md4 = messageBuffer.readBinary( 16 );
		
		// only calculate this once, not on every getUploadedString...
		// use a main calcStringSize() function. Why duplicate code?
		stringOfBytesUploaded = RegExp.calcStringSize( numOfBytesUploaded );
	}

	/**
	 * @param messageBuffer The <code>Messagebuffer</code> to read from
	 * @return wether the core updated this Information
	 */
	public boolean update( MessageBuffer messageBuffer ) {			
		boolean hasChanged = false;
		/* we don't need the fileId, as we already know it*/
		messageBuffer.setIterator( messageBuffer.getIterator() + 4 );
		
		/* we just update values*/		
		long myUpload = messageBuffer.readInt64();
		int myRequests = messageBuffer.readInt32();
		
		if (   ( myUpload != this.numOfBytesUploaded ) 
			|| ( myRequests != this.numOfQueriesForFile ) ) 
			{
				hasChanged = true;
				this.numOfBytesUploaded = myUpload;
				this.numOfQueriesForFile = myRequests;
				stringOfBytesUploaded = RegExp.calcStringSize( numOfBytesUploaded );			
			}			
		return hasChanged;
	}	
	
	/**
	 * reads out some more detailed Information from mesagebuffer. 
	 * Does a complete refresh of this object.
	 * @param messageBuffer The <code>Messagebuffer</code> to read from
	 * @return wether the core updated this Information
	 */
	public boolean detailedUpdate( MessageBuffer messageBuffer ) {
		boolean hasChanged = false;
		/* save previous values */
		long oldUpload = this.numOfBytesUploaded;
		int oldRequests = this.numOfQueriesForFile;
		
		readStream( messageBuffer );
		
		if (   ( oldUpload != this.numOfBytesUploaded ) 
			|| ( oldRequests != this.numOfQueriesForFile ) ) 
			{
				hasChanged = true;				
			}		
		return hasChanged;
	}

	/**
	 * translate the int to EnumNetwork
	 * @param i the int
	 */
	void setNetwork( NetworkInfo network ) {
		this.network = network;
	}
	
	/**
	 * 
	 * @return String representation of Upload
	 */
	public String getUploadedString() {
		return stringOfBytesUploaded;
	}

	/**
	 * @return the Network this file is uploaded to
	 */
	public NetworkInfo getNetwork() {
		return network;
	}
	/**
	 * you want to have a ed2k-link from this file: here it is
	 * @return a ed2k-link representation of this file
	 */
	public String getED2K() {
		return "ed2k://|file|" + this.getName() 
				+ "|" + this.getSize() 
				+ "|" + this.getMd4() 
				+ "|/";
	}



}

/*
$Log: SharedFileInfo.java,v $
Revision 1.17  2003/11/23 17:58:03  lemmster
removed dead/unused code

Revision 1.16  2003/09/30 15:28:36  dek
on some updates the wrong update() was called, as the core sometimes 
sends a complete SharedInfo, not only an update, now everything is fine

Revision 1.15  2003/09/27 00:02:37  dek
bugfixes, merged right-mouse-click menues (nothing is uglier than one-item-menues)

Revision 1.14  2003/09/26 18:01:46  zet
update string

Revision 1.13  2003/09/26 17:00:54  zet
remove duplicate code, calc string once per update

Revision 1.12  2003/09/26 11:55:48  dek
right-mouse menue for upload-Table

Revision 1.11  2003/09/25 21:50:16  dek
added icons for networks + TableSorter

Revision 1.10  2003/09/25 21:08:56  dek
some checkstyle

Revision 1.9  2003/09/25 21:06:58  dek
first sketch of upload-Table not yet added to transferTab.

Revision 1.8  2003/09/25 14:24:11  dek
sharedFile no has Network (not only networkID)

Revision 1.7  2003/09/18 09:16:47  lemmster
checkstyle

Revision 1.6  2003/09/17 13:49:09  dek
now the gui refreshes the upload-stats, add and observer to SharedFileInfoIntMap
to get notice of changes in # of requests and # of uploaded bytes

Revision 1.5  2003/08/23 15:21:37  zet
remove @author

Revision 1.4  2003/08/22 21:03:15  lemmster
replace $user$ with $Author: lemmster $

Revision 1.3  2003/07/05 20:04:02  lemmstercvs01
javadoc improved

Revision 1.2  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.1  2003/06/15 16:17:26  lemmstercvs01
opcode 48 added

*/