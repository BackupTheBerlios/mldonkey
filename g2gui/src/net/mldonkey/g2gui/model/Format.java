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
 * Format
 *
 * @author markus
 * @version $Id: Format.java,v 1.5 2003/06/16 15:33:03 lemmstercvs01 Exp $ 
 *
 */
public class Format implements Information {
	
	public static final byte UNKNOWN_FORMAT = 0;
	public static final byte GENERIC_FORMAT = 1;
	public static final byte AVI_FILE = 2;
	public static final byte MP3_FILE = 3;

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
		if ( b == UNKNOWN_FORMAT )
			format = UNKNOWN_FORMAT;
		else if ( b == GENERIC_FORMAT )
			format = GENERIC_FORMAT;
		else if ( b == AVI_FILE )
			format = AVI_FILE;
		else if ( b == MP3_FILE )
			format = MP3_FILE;	
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
	 * Read a Format from a MessgaeBuffer
	 * @param messageBuffer MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		this.setFormat( ( byte ) messageBuffer.readByte() );
		if ( this.getFormat() == 1 ) {
			this.setExtension( messageBuffer.readString() );
			this.setKind( messageBuffer.readString() );
		}
		else if ( this.getFormat() == 2 ) {
			this.setCodec( messageBuffer.readString() );
			this.setVwidth( messageBuffer.readInt32() );
			this.setVheight( messageBuffer.readInt32() );
			this.setVfps( messageBuffer.readInt32() );
			this.setVrate( messageBuffer.readInt32() );
		}
		else if ( this.getFormat() == 3 ) {
			this.setTitle( messageBuffer.readString() );
			this.setArtist( messageBuffer.readString() );
			this.setAlbum( messageBuffer.readString() );	
			this.setYear( messageBuffer.readString() );
			this.setComment( messageBuffer.readString() );
			this.setTracknum( messageBuffer.readInt32() );
			this.setGenre( messageBuffer.readInt32() );
		}
	}
}

/*
$Log: Format.java,v $
Revision 1.5  2003/06/16 15:33:03  lemmstercvs01
some kind of enum added

Revision 1.4  2003/06/14 12:47:27  lemmstercvs01
checkstyle applied

Revision 1.3  2003/06/13 11:03:41  lemmstercvs01
changed InputStream to MessageBuffer

Revision 1.2  2003/06/12 22:23:06  lemmstercvs01
lots of changes

Revision 1.1  2003/06/11 12:54:44  lemmstercvs01
initial commit

*/