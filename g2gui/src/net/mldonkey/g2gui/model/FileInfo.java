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
 * Download
 *
 * @author markus
 * @version $Id: FileInfo.java,v 1.10 2003/07/02 16:25:30 dek Exp $ 
 *
 */
public class FileInfo implements SimpleInformation {
	/**
	 * File identifier
	 */
	private int id;
	/**
	 * File network identifier
	 */
	private int network;
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
	private int priority;
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
	 * @return a String
	 */
	public String getAge() {
		return age;
	}

	/**
	 * @return a String
	 */
	public String getAvail() {
		return avail;
	}

	/**
	 * @return a String[]
	 */
	public String[] getChunkage() {
		return chunkage;
	}

	/**
	 * @return a String
	 */
	public String getChunks() {
		return chunks;
	}

	/**
	 * @return an int
	 */
	public int getDownloaded() {
		return downloaded;
	}

	/**
	 * @return a format object
	 */
	public Format getFormat() {
		return format;
	}

	/**
	 * @return an int
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return a String
	 */
	public String getMd4() {
		return md4;
	}

	/**
	 * @return a String
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return a String[]
	 */
	public String[] getNames() {
		return names;
	}

	/**
	 * @return an int
	 */
	public int getNetwork() {
		return network;
	}

	/**
	 * @return an int
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @return an int
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @return a float
	 */
	public float getRate() {
		return rate;
	}

	/**
	 * @return an int
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @return an int
	 */
	public int getSources() {
		return sources;
	}

	/**
	 * @return a state object
	 */
	public FileState getState() {
		return state;
	}

	/**
	 * @param string a String
	 */
	public void setAge( String string ) {
		this.age = string;
	}

	/**
	 * @param string a String
	 */
	public void setAvail( String string ) {
		this.avail = string;
	}

	/**
	 * @param string a String
	 */
	public void setChunkage( String[] string ) {
		this.chunkage = string;
	}

	/**
	 * @param string a String
	 */
	public void setChunks( String string ) {
		this.chunks = string;
	}

	/**
	 * @param i an int
	 */
	public void setDownloaded( int i ) {
		this.downloaded = i;
	}

	/**
	 * @param format a format object
	 */
	public void setFormat( Format format ) {
		this.format = format;
	}

	/**
	 * @param i an int
	 */
	public void setId( int i ) {
		this.id = i;
	}

	/**
	 * @param string a String
	 */
	public void setMd4( String string ) {
		this.md4 = string;
	}

	/**
	 * @param string a String
	 */
	public void setName( String string ) {
		this.name = string;
	}

	/**
	 * @param strings a string[]
	 */
	public void setNames( String[] strings ) {
		this.names = strings;
	}

	/**
	 * @param i an int
	 */
	public void setNetwork( int i ) {
		this.network = i;
	}

	/**
	 * @param i an int
	 */
	public void setOffset( int i ) {
		this.offset = i;
	}

	/**
	 * @param i an int
	 */
	public void setPriority( int i ) {
		this.priority = i;
	}

	/**
	 * @param f a float
	 */
	public void setRate( float f ) {
		this.rate = f;
	}

	/**
	 * @param i an int
	 */
	public void setSize( int i ) {
		this.size = i;
	}

	/**
	 * @param i an int
	 */
	public void setSources( int i ) {
		this.sources = i;
	}

	/**
	 * @param state a state object
	 */
	public void setState( FileState state ) {
		this.state = state;
	}

	/**
	 * @return an int
	 */
	public int getClients() {
		return clients;
	}

	/**
	 * @param i an int
	 */
	public void setClients( int i ) {
		clients = i;
	}
	
	/**
	 * @return a double
	 */
	public double getPerc() {
		return perc;
	}

	/**
	 * @param d a double
	 */
	public void setPerc( double d ) {
		perc = d;
	}

	
	/**
	 * @return A string representation of this object
	 */
	public String toString() {
		String result =  new String( this.getName() );
		return result;
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
		this.setId( messageBuffer.readInt32() );
		this.setNetwork( messageBuffer.readInt32() );
		this.setNames( messageBuffer.readStringList() );
		this.setMd4( messageBuffer.readBinary( 16 ) );
		this.setSize( messageBuffer.readInt32() );
		this.setDownloaded( messageBuffer.readInt32() );
		this.setSources( messageBuffer.readInt32() );
		this.setClients( messageBuffer.readInt32() );
		
		/* File State */
		this.getState().readStream( messageBuffer );
		
		this.setChunks( messageBuffer.readString() );
		this.setAvail( messageBuffer.readString() );
		/* translate to kb and round to two digits after comma */
		double d = new Double( messageBuffer.readString() ).doubleValue() / 1024;
		this.setRate( ( float ) round( d ) );
		this.setChunkage( messageBuffer.readStringList() );
		this.setAge( messageBuffer.readString() );
		
		/* File Format */
		this.getFormat().readStream( messageBuffer );
		
		this.setName( messageBuffer.readString() );
		this.setOffset( messageBuffer.readInt32() );
		this.setPriority( messageBuffer.readInt32() );
		double d2 = round( ( ( double )this.getDownloaded() / ( double )this.getSize() ) * 100 );
		this.setPerc( d2 );
	}
	
	/**
	 * Update a FileInfo object
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		this.setDownloaded( messageBuffer.readInt32() );
		/* translate to kb and round to two digits after comma */
		double d = new Double( messageBuffer.readString() ).doubleValue() / 1024;
		this.setRate( ( float ) round( d ) );
		this.setOffset( messageBuffer.readInt32() );
		double d2 = round( ( ( double )this.getDownloaded() / ( double )this.getSize() ) * 100 );
		this.setPerc( d2 );
	}
	
	/**
	 * Rounds a double to two decimal places
	 * @param d The double to round
	 * @return a rounden double
	 */
	public static double round( double d ) {
		d = ( double )( ( int )( d * 100 + 0.5 ) ) / 100;
		return d;
	}

	/**
	 * Compares two FileInfo objects by their md4
	 * @param fileInfo the fileinfo to compare with
	 * @return if they are identical
	 */
	public boolean equals( FileInfo fileInfo ) {
		if ( this.getMd4() == fileInfo.getMd4() )
			return true;
		else
			return false;
	}

}

/*
$Log: FileInfo.java,v $
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