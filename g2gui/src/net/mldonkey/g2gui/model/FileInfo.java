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

import java.io.IOException;
import java.io.InputStream;

import net.mldonkey.g2gui.comm.Message;

/**
 * Download
 *
 * @author markus
 * @version $Id: FileInfo.java,v 1.2 2003/06/12 22:23:06 lemmstercvs01 Exp $ 
 *
 */
public class FileInfo implements Information {
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
	private State state = new State();

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
	public State getState() {
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
	public void setState( State state ) {
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
	 * @return A string representation of this object
	 */
	public String toString() {
		String result =  new String(this.getName());
		return result;
	}

	/**
	 * Reads a Download from an InputStream
	 * @param inputStream Stream to read from
	 * @return Download
	 * @throws IOException Error if read on stream failed
	 */
	public void readStream( InputStream inputStream ) throws IOException {
		this.setId( Message.readInt32( inputStream ) );
		this.setNetwork( Message.readInt32( inputStream ) );
		this.setNames( Message.readStringList( inputStream ) );
		this.setMd4( Message.readBinary( inputStream, 16 ) );
		this.setSize( Message.readInt32( inputStream ) );
		this.setDownloaded( Message.readInt32( inputStream ) );
		this.setSources( Message.readInt32( inputStream ) );
		this.setClients( Message.readInt32( inputStream ) );
		
		/* File State */
		this.getState().readStream( inputStream );
		
		this.setChunks( Message.readString( inputStream ) );
		this.setAvail( Message.readString( inputStream ) );
		this.setRate( new Float( Message.readString( inputStream ) ).floatValue() );
		this.setChunkage( Message.readStringList( inputStream ) );
		this.setAge( Message.readString( inputStream ) );
		
		/* File Format */
		this.getFormat().readStream( inputStream );
		
		this.setName( Message.readString( inputStream ) );
		this.setOffset( Message.readInt32( inputStream ) );
		this.setPriority( Message.readInt32( inputStream ) );
	}

}

/*
$Log: FileInfo.java,v $
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