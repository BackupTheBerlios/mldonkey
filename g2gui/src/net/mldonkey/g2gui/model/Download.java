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

/**
 * Download
 *
 * @author markus
 * @version $Id: Download.java,v 1.1 2003/06/11 12:54:44 lemmstercvs01 Exp $ 
 *
 */
public class Download {

	private int id;
	private int network;
	private String[] names;
	private String md4;
	private int size;
	private int downloaded;
	private int sources;
	private String chunks;
	private String avail;
	private float rate;
	private String name;
	private int priority;
	private int offset;
	private String[] chunkage;
	private String age;
	private Format format;
	private State state;

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

}

/*
$Log: Download.java,v $
Revision 1.1  2003/06/11 12:54:44  lemmstercvs01
initial commit

*/