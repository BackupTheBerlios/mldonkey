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
 * State
 *
 * @author markus
 * @version $Id: FileState.java,v 1.2 2003/06/16 15:33:03 lemmstercvs01 Exp $ 
 *
 */
public class FileState implements Information {

	static final byte DOWNLOADING = 0;
	static final byte PAUSED = 1;
	static final byte DOWNLOADED = 2;
	static final byte SHARED = 3;
	static final byte CANCELLED = 4;
	static final byte NEW = 5;
	static final byte ABORTED = 6;
	static final byte QUEUED = 7;

	private byte state;

	private String reason = null;

	/**
	 * @return a String
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @return a byte
	 */
	public byte getState() {
		return state;
	}

	/**
	 * @param string a String
	 */
	public void setReason( String string ) {
		reason = string;
	}

	/**
	 * @param b a byte
	 */
	public void setState( byte b ) {
		if ( b == DOWNLOADING )
			state = DOWNLOADING;
		else if ( b == PAUSED )
			state = PAUSED;
		else if ( b == DOWNLOADED )
			state = DOWNLOADED;
		else if ( b == SHARED )
			state = SHARED;
		else if ( b == CANCELLED )
			state = CANCELLED;
		else if ( b == NEW )
			state = NEW;
		else if ( b == ABORTED )
			state = ABORTED;
		else if ( b == QUEUED )
			state = QUEUED;	

	}
	
	/**
	 * Reads a State from a MessageBuffer
	 * @param messageBuffer MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		this.setState( ( byte ) messageBuffer.readByte() );
		if ( this.getState() == ABORTED )
			this.setReason( messageBuffer.readString() );			
	}
}

/*
$Log: FileState.java,v $
Revision 1.2  2003/06/16 15:33:03  lemmstercvs01
some kind of enum added

Revision 1.1  2003/06/14 17:40:40  lemmstercvs01
initial commit

Revision 1.4  2003/06/14 12:47:27  lemmstercvs01
checkstyle applied

Revision 1.3  2003/06/13 11:03:41  lemmstercvs01
changed InputStream to MessageBuffer

Revision 1.2  2003/06/12 22:23:06  lemmstercvs01
lots of changes

Revision 1.1  2003/06/11 12:54:43  lemmstercvs01
initial commit

*/