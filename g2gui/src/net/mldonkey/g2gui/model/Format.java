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
 * Format
 *
 * @author markus
 * @version $Id: Format.java,v 1.2 2003/06/12 22:23:06 lemmstercvs01 Exp $ 
 *
 */
public class Format implements Information {

	private byte format;

	private String extension = null;

	private String kind = null;

	private String codec = null;

	private int vwidth = 0;

	private int vheight = 0;

	private int vfps = 0;

	private int vrate = 0;

	private String title = null;

	private String artist = null;

	private String album = null;

	private String year = null;

	private String comment = null;

	private int tracknum = 0;

	private int genre = 0;

	/**
	 * @return a String
	 */
	public String getAlbum() {
		return album;
	}

	/**
	 * @return a String
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * @return a String
	 */
	public String getCodec() {
		return codec;
	}

	/**
	 * @return a String
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @return a String
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @return a byte
	 */
	public byte getFormat() {
		return format;
	}

	/**
	 * @return an int
	 */
	public int getGenre() {
		return genre;
	}

	/**
	 * @return a String
	 */
	public String getKind() {
		return kind;
	}

	/**
	 * @return a String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return an int
	 */
	public int getTracknum() {
		return tracknum;
	}

	/**
	 * @return an int
	 */
	public int getVfps() {
		return vfps;
	}

	/**
	 * @return an int
	 */
	public int getVheight() {
		return vheight;
	}

	/**
	 * @return an int
	 */
	public int getVrate() {
		return vrate;
	}

	/**
	 * @return an int
	 */
	public int getVwidth() {
		return vwidth;
	}

	/**
	 * @return a String
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @param string a String
	 */
	public void setAlbum( String string ) {
		album = string;
	}

	/**
	 * @param string a String
	 */
	public void setArtist( String string ) {
		artist = string;
	}

	/**
	 * @param string a String
	 */
	public void setCodec( String string ) {
		codec = string;
	}

	/**
	 * @param string a String
	 */
	public void setComment( String string ) {
		comment = string;
	}

	/**
	 * @param string a String
	 */
	public void setExtension( String string ) {
		extension = string;
	}

	/**
	 * @param b a byte
	 */
	public void setFormat( byte b ) {
		format = b;
	}

	/**
	 * @param i an int
	 */
	public void setGenre( int i ) {
		genre = i;
	}

	/**
	 * @param string a String
	 */
	public void setKind( String string ) {
		kind = string;
	}

	/**
	 * @param string a String
	 */
	public void setTitle( String string ) {
		title = string;
	}

	/**
	 * @param i an int
	 */
	public void setTracknum( int i ) {
		tracknum = i;
	}

	/**
	 * @param i an int
	 */
	public void setVfps( int i ) {
		vfps = i;
	}

	/**
	 * @param i an int
	 */
	public void setVheight( int i ) {
		vheight = i;
	}

	/**
	 * @param i an int
	 */
	public void setVrate( int i ) {
		vrate = i;
	}

	/**
	 * @param i an int
	 */
	public void setVwidth( int i ) {
		vwidth = i;
	}

	/**
	 * @param string a String
	 */
	public void setYear( String string ) {
		year = string;
	}
	
	/**
	 * Read a Format from a stream
	 * @param inputStream Stream to read from
	 * @throws IOException Error if read on stream failed
	 */
	public void readStream( InputStream inputStream ) throws IOException {
		this.setFormat( (  byte ) Message.readByte( inputStream ) );
		if ( this.getFormat() == 1 ) {
			this.setExtension( Message.readString( inputStream ) );
			this.setKind( Message.readString( inputStream ) );
		}
		else if ( this.getFormat() == 2 ) {
			this.setCodec( Message.readString( inputStream ) );
			this.setVwidth( Message.readInt32( inputStream ) );
			this.setVheight( Message.readInt32( inputStream ) );
			this.setVfps( Message.readInt32( inputStream ) );
			this.setVrate( Message.readInt32( inputStream ) );
		}
		else if ( this.getFormat() == 3 ) {
			this.setTitle( Message.readString( inputStream ) );
			this.setArtist( Message.readString( inputStream ) );
			this.setAlbum( Message.readString( inputStream ) );	
			this.setYear( Message.readString( inputStream ) );
			this.setComment( Message.readString( inputStream ) );
			this.setTracknum( Message.readInt32( inputStream ) );
			this.setGenre( Message.readInt32( inputStream ) );
		}
	}
}

/*
$Log: Format.java,v $
Revision 1.2  2003/06/12 22:23:06  lemmstercvs01
lots of changes

Revision 1.1  2003/06/11 12:54:44  lemmstercvs01
initial commit

*/