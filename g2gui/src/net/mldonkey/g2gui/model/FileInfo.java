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
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.model.enum.EnumPriority;

/**
 * Download
 *
 * @author markus
 * @version $Id: FileInfo.java,v 1.16 2003/07/05 15:46:52 lemmstercvs01 Exp $ 
 *
 */
public class FileInfo implements SimpleInformation {
	/**
	 * The CoreCommunication
	 */
	private CoreCommunication parent;
	/**
	 * File identifier
	 */
	private int id;
	/**
	 * File network identifier
	 */
	private NetworkInfo network;
	/**
	 * Possible file names
	 */
	private String[] names;
	/**
	 * File md4
	 */
	private String md4;
	/**
	 * File size
	 */
	private int size;
	/**
	 * Size already downloaded
	 */
	private int downloaded;
	/**
	 * Number of sources
	 */
	private int sources;
	/**
	 * Number of clients
	 */
	private int clients;
	/**
	 * Chunks
	 */
	private String chunks;
	/**
	 * Availibility
	 */
	private String avail;
	/**
	 * Download rate
	 */
	private float rate;
	/**
	 * File name
	 */
	private String name;
	/**
	 * File priority inside mldonkey
	 */
	private Enum priority;
	/**
	 * File last seen
	 */
	private int offset;
	/**
	 * last time each chunk has been seen
	 */
	private String[] chunkage;
	/**
	 * when download started
	 */
	private String age;
	/**
	 * File Format object
	 */
	private Format format = new Format();
	/**
	 * File State object
	 */
	private FileState state = new FileState();
	/**
	 * Percent (Downloaded/Size)*100
	 */
	private double perc;

	/**
	 * @return time when download started
	 */
	public String getAge() {
		return age;
	}
	/**
	 * @return The file availibility
	 */
	public String getAvail() {
		return avail;
	}
	/**
	 * @return last time each chunk has been seen
	 */
	public String[] getChunkage() {
		return chunkage;
	}
	/**
	 * @return Chunks
	 */
	public String getChunks() {
		return chunks;
	}
	/**
	 * @return Size already downloaded
	 */
	public int getDownloaded() {
		return downloaded;
	}
	/**
	 * @return The file Format
	 */
	public Format getFormat() {
		return format;
	}
	/**
	 * @return The file identifier
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return The file md4
	 */
	public String getMd4() {
		return md4;
	}
	/**
	 * @return The name of this file
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return Possible names for this file
	 */
	public String[] getNames() {
		return names;
	}
	/**
	 * @return File network identifier
	 */
	public NetworkInfo getNetwork() {
		return network;
	}
	/**
	 * @return File last seen
	 */
	public int getOffset() {
		return offset;
	}
	/**
	 * @return File priority
	 */
	public Enum getPriority() {
		return priority;
	}
	/**
	 * @return The rate this file is downloading
	 * 			(rounded to two decimal places)
	 */
	public float getRate() {
		return rate;
	}
	/**
	 * @return The overall size of this file
	 */
	public int getSize() {
		return size;
	}
	/**
	 * @return Number of sources for this file
	 */
	public int getSources() {
		return sources;
	}
	/**
	 * @return File status
	 */
	public FileState getState() {
		return state;
	}
	/**
	 * @return Number of clients receiving this file
	 */
	public int getClients() {
		return clients;
	}
	/**
	 * @return The percent this file is complete
	 */
	public double getPerc() {
		return perc;
	}
	/**
	 * @return The parent corecommunication
	 */
	private CoreCommunication getParent() {
		return parent;
	}
	/**
	 * Creates a new fileinfo object
	 * @param core The CoreCommunication parent
	 */
	public FileInfo( CoreCommunication core ) {
		this.parent = core;
	}

	/**
	 * Reads a FileInfo object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		/*
		* int32			File Identifier 
		* int32			File Network Identifier 
		* List of String	Possible File Names 
		* char[16]		File Md4 (binary) 
		* int32			File Size 
		* int32			File Downloaded 
		* int32			Number of Sources 
		* int32			Number of Clients 
		* FileState		Current State of the Download 
		* String		Chunks (one char by chunk '0'-'3') 
		* String		Availability (one char by chunk, 0-255 sources) 
		* Float			Download Rate 
		* List of Time	Chunks Ages (last time each chunk has been seen) 
		* Time			File Age (when download started) 
		* FileFormat	File Format 
		* String		File Preferred Name 
		* OffsetTime	File Last Seen 
		* int32			File Priority 
		*/ 
		this.id = messageBuffer.readInt32();
		this.setNetwork( messageBuffer.readInt32() );
		this.names = messageBuffer.readStringList();
		this.md4 = messageBuffer.readBinary( 16 );
		this.size = messageBuffer.readInt32();
		this.downloaded = messageBuffer.readInt32();
		this.sources = messageBuffer.readInt32();
		this.clients = messageBuffer.readInt32();
		
		/* File State */
		this.getState().readStream( messageBuffer );
		
		this.chunks = messageBuffer.readString();
		this.avail = messageBuffer.readString();
		/* translate to kb and round to two digits after comma */
		double d = new Double( messageBuffer.readString() ).doubleValue() / 1024;
		this.rate = ( float ) round( d );
		this.chunkage = messageBuffer.readStringList();
		this.age = messageBuffer.readString();
		
		/* File Format */
		this.getFormat().readStream( messageBuffer );
		
		this.name = messageBuffer.readString();
		this.offset = messageBuffer.readInt32();
		this.setPriority( messageBuffer.readInt32() );
		double d2 = round( ( ( double ) this.getDownloaded() / ( double ) this.getSize() ) * 100 );
		this.perc = d2;
	}
	
	/**
	 * Update a FileInfo object
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		this.downloaded = messageBuffer.readInt32();
		/* translate to kb and round to two digits after comma */
		double d = new Double( messageBuffer.readString() ).doubleValue() / 1024;
		this.rate = ( float ) round( d );
		this.offset = messageBuffer.readInt32();
		double d2 = round( ( ( double ) this.getDownloaded() / ( double ) this.getSize() ) * 100 );
		this.perc = d2;
	}
	
	/**
	 * Rounds a double to two decimal places
	 * @param d The double to round
	 * @return a rounden double
	 */
	private static double round( double d ) {
		d = ( double )( ( int )( d * 100 + 0.5 ) ) / 100;
		return d;
	}

	/**
	 * Compares two FileInfo objects by their md4
	 * @param fileInfo the fileinfo to compare with
	 * @return true if they are identical, otherwise false
	 */
	public boolean equals( FileInfo fileInfo ) {
		if ( this.getMd4() == fileInfo.getMd4() )
			return true;
		else
			return false;
	}
	
	/**
	 * translate the int to EnumPriority
	 * @param i the int
	 */
	private void setPriority( int i ) {
		if ( i < 0 )
			priority = EnumPriority.LOW;
		else if ( i > 0 )
			priority = EnumPriority.HIGH;
		else 
			priority = EnumPriority.NORMAL;		
	}
	
	/**
	 * translate the int to EnumNetwork
	 * @param i the int
	 */
	private void setNetwork( int i ) {
		this.network =
		( NetworkInfo ) this.getParent().getNetworkInfoMap().infoIntMap.get( i );
	}

	/**
	 * @param string The new name for this file
	 */
	public void setName( String string ) {
		Object[] content = new Object[ 2 ];
		content[ 0 ] = new Integer( this.getId() );
		content[ 1 ] = string;
		/* create the message content */
		EncodeMessage consoleMessage = new EncodeMessage( Message.S_CONSOLEMSG, content );
		consoleMessage.sendMessage( this.getParent().getConnection() );
		content = null;
		consoleMessage = null;
	}

	/**
	 * @param enum The new priority for this file (LOW/NORMAL/HIGH)
	 */
	public void setPriority( EnumPriority enum ) {
		Integer content;
		if ( enum == EnumPriority.LOW )
			content = new Integer( -10 );
		else if ( enum == EnumPriority.HIGH )
			content = new Integer( 10 );
		else
			content = new Integer( 0 );	
		EncodeMessage consoleMessage = new EncodeMessage( Message.S_SAVE_FILE_AS, content );
		consoleMessage.sendMessage( this.getParent().getConnection() );
		content = null;
		consoleMessage = null;
	}

	/**
	 * @param enum The new state of this file
	 */
	public void setState( EnumFileState enum ) {
		this.getState().setState( enum, this.getId(), this.getParent() );
	}
	
	/**
	 * Verify all chunks of this fileinfo
	 */
	public void verifyChunks() {
		EncodeMessage chunks =
			new EncodeMessage( Message.S_VERIFY_ALL_CHUNKS, new Integer( this.getId() ) );
		chunks.sendMessage( this.getParent().getConnection() );
	}
}	

/*
$Log: FileInfo.java,v $
Revision 1.16  2003/07/05 15:46:52  lemmstercvs01
javadoc improved

Revision 1.15  2003/07/05 11:33:08  lemmstercvs01
id -> List ids

Revision 1.14  2003/07/04 18:35:02  lemmstercvs01
foobar

Revision 1.13  2003/07/04 11:04:14  lemmstercvs01
add some opcodes

Revision 1.12  2003/07/03 21:16:16  lemmstercvs01
setName() and setPriority() added, Priority from int to Enum

Revision 1.11  2003/07/03 16:01:51  lemmstercvs01
setState() works now to set the filestate on the mldonkey side

Revision 1.10  2003/07/02 16:25:30  dek
Checkstyle

Revision 1.9  2003/06/29 17:51:44  lemmstercvs01
added some doku

Revision 1.8  2003/06/26 09:10:54  lemmstercvs01
added field for percent, store rate rounded

Revision 1.7  2003/06/25 00:57:49  lemmstercvs01
equals methode added

Revision 1.6  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.5  2003/06/14 17:41:03  lemmstercvs01
foobar

Revision 1.4  2003/06/14 12:47:40  lemmstercvs01
update() added

Revision 1.3  2003/06/13 11:03:41  lemmstercvs01
changed InputStream to MessageBuffer

Revision 1.2  2003/06/12 22:23:06  lemmstercvs01
lots of changes

Revision 1.1  2003/06/12 18:14:04  lemmstercvs01
initial commit

Revision 1.4  2003/06/12 10:36:04  lemmstercvs01
toString() added

Revision 1.3  2003/06/12 07:40:04  lemmstercvs01
added number of clients

Revision 1.2  2003/06/11 15:32:33  lemmstercvs01
still in progress

Revision 1.1  2003/06/11 12:54:44  lemmstercvs01
initial commit

*/