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
import net.mldonkey.g2gui.model.enum.*;

/**
 * Format
 *
 * @author markus
 * @version $Id: Format.java,v 1.10 2003/07/06 09:41:10 lemmstercvs01 Exp $ 
 *
 */
public class Format implements SimpleInformation {
	private Enum format;

	private String extension;

	private String kind;

	private String codec;

	private int vwidth;

	private int vheight;

	private int vfps;

	private int vrate;

	private String title;

	private String artist;

	private String album;

	private String year;

	private String comment;

	private int tracknum;

	private int genre;

	/**
	 * @return Mp3 Album (present only if File Format = EnumFormat.MP3)
	 */
	public String getAlbum() {
		return album;
	}

	/**
	 * @return MP3 artist (present only if File Format = EnumFormat.MP3)
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * @return The codec
	 */
	public String getCodec() {
		return codec;
	}

	/**
	 * @return Mp3 Comment (present only if File Format = EnumFormat.MP3) 
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @return Format extension [mp3 for example]
	 * (present only if File Format = EnumFormat.GENERIC_FORMAT)
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @return EnumFormat
	 */
	public Enum getFormat() {
		return format;
	}

	/**
	 * @return MP3 genre (present only if File Format = EnumFormat.MP3)
	 */
	public int getGenre() {
		return genre;
	}

	/**
	 * @return Format kind [Audio for example]
	 * (present only if File Format = EnumFormat.GENERIC_FORMAT)
	 */
	public String getKind() {
		return kind;
	}

	/**
	 * @return MP3 title (present only if File Format = EnumFormat.MP3)
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return MP3 tracknum (present only if File Format = EnumFormat.MP3)
	 */
	public int getTracknum() {
		return tracknum;
	}

	/**
	 * @return Video fps (present only if File Format = EnumFormat.AVI_FILE)
	 */
	public int getVfps() {
		return vfps;
	}

	/**
	 * @return Video height (present only if File Format = EnumFormat.AVI_FILE)
	 */
	public int getVheight() {
		return vheight;
	}

	/**
	 * @return Video rate (present only if File Format = EnumFormat.AVI_FILE)
	 */
	public int getVrate() {
		return vrate;
	}

	/**
	 * @return Video width (present only if File Format = EnumFormat.AVI_FILE)
	 */
	public int getVwidth() {
		return vwidth;
	}

	/**
	 * @return MP3 year (present only if File Format = EnumFormat.MP3)
	 */
	public String getYear() {
		return year;
	}

	/**
	 * Read a Format from a MessgaeBuffer
	 * @param messageBuffer MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		this.setFormat( ( byte ) messageBuffer.readByte() );
		if ( this.getFormat() == EnumFormat.GENERIC_FORMAT ) {
			this.extension = messageBuffer.readString();
			this.kind = messageBuffer.readString();
		}
		else if ( this.getFormat() == EnumFormat.AVI_FILE ) {
			this.codec = messageBuffer.readString();
			this.vwidth = messageBuffer.readInt32();
			this.vheight = messageBuffer.readInt32();
			this.vfps = messageBuffer.readInt32();
			this.vrate = messageBuffer.readInt32();
		}
		else if ( this.getFormat() == EnumFormat.MP3_FILE ) {
			this.title = messageBuffer.readString();
			this.artist = messageBuffer.readString();
			this.album = messageBuffer.readString();	
			this.year = messageBuffer.readString();
			this.comment = messageBuffer.readString();
			this.tracknum = messageBuffer.readInt32();
			this.genre = messageBuffer.readInt32();
		}
	}

	/**
	 * @param b a byte
	 */
	private void setFormat( byte b ) {
		if ( b == 0 )
			format = EnumFormat.UNKNOWN_FORMAT;
		else if ( b == 1 )
			format = EnumFormat.GENERIC_FORMAT;
		else if ( b == 2 )
			format = EnumFormat.AVI_FILE;
		else if ( b == 3 )
			format = EnumFormat.MP3_FILE;
	}
}

/*
$Log: Format.java,v $
Revision 1.10  2003/07/06 09:41:10  lemmstercvs01
useless initialisation removed

Revision 1.9  2003/07/05 15:58:05  lemmstercvs01
javadoc improved

Revision 1.8  2003/06/24 09:29:57  lemmstercvs01
Enum more improved

Revision 1.7  2003/06/24 09:16:48  lemmstercvs01
better Enum added

Revision 1.6  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

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