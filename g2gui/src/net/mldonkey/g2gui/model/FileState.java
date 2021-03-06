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
import net.mldonkey.g2gui.model.enum.*;
import net.mldonkey.g2gui.model.enum.Enum;

/**
 * State
 *
 *
 * @version $Id: FileState.java,v 1.20 2005/01/02 17:01:21 lemmy Exp $ 
 *
 */
public class FileState extends SimpleInformation {
	
	/**
	 * The EnumFileState
	 */
	private Enum state;
	
	/**
	 * The reason for state
	 */
	private String reason = null;
	/**
	 * MessageContent to pause a file
	 */
	private static Byte pause = new Byte( ( byte ) 0 );
	/**
	 * MessageContent to resume a file
	 */
	private static Byte resume = new Byte( ( byte ) 1 );

	FileState() {
		//prevent outer package instanciation
	}
	
	/**
	 * @return The reason for the state
	 * 			(only if state is ABORTED)
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @return The file state (downloading,...)
	 */
	public Enum getState() {
		return state;
	}

	/**
	 * @param b a byte
	 */
	private void setState( byte b ) {
		if ( b == 0 )
			state = EnumFileState.DOWNLOADING;
		else if ( b == 1 )
			state = EnumFileState.PAUSED;
		else if ( b == 2 )
			state = EnumFileState.DOWNLOADED;
		else if ( b == 3 )
			state = EnumFileState.SHARED;
		else if ( b == 4 )
			state = EnumFileState.CANCELLED;
		else if ( b == 5 )
			state = EnumFileState.NEW;
		else if ( b == 6 )
			state = EnumFileState.ABORTED;
		else if ( b == 7 )
			state = EnumFileState.QUEUED;	
	}
	
	/**
	 * Reads a State from a MessageBuffer
	 * @param messageBuffer MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		this.setState( messageBuffer.readByte() );
		if ( this.getState() == EnumFileState.ABORTED )
			this.reason = messageBuffer.readString();			
	}
	/**
	 * @param The new file state (paused,...)
	 */
	protected void setState( EnumFileState state, int id, CoreCommunication core ) {
		Message sendState = null;
		Object[] content = new Object[ 2 ];
		short opcode = Message.S_SWITCH_DOWNLOAD;
		content[ 0 ] = new Integer( id );
			
		/* unpause */
		if ( this.getState() == EnumFileState.PAUSED 
		&& state == EnumFileState.DOWNLOADING )
				{
				content[ 1 ] = resume;
				}
		/* pause */
		else if ( this.getState() == EnumFileState.DOWNLOADING
			  && state == EnumFileState.PAUSED ) 
				{
				content[ 1 ] = pause;
				}
		/* cancel */
		else if ( state == EnumFileState.CANCELLED ) {
			/* to cancel the dl we need a different opcode */
			opcode = Message.S_REMOVE_DOWNLOAD;
			content = new Object[ 1 ];
			content[ 0 ] = new Integer( id );
		}
		/* unvalid input */
		else {
			content = null;
			return;			
		}
		/* generate and send the message */
		sendState = new EncodeMessage( opcode, content );
		sendState.sendMessage( core );
		/* little gc by ourself */
		sendState = null;
		content = null;
	}
}

/*
$Log: FileState.java,v $
Revision 1.20  2005/01/02 17:01:21  lemmy
explicitly import our enum classes to avoid problems with java 1.5 enums

Revision 1.19  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.18  2003/12/01 14:22:17  lemmy
ProtocolVersion handling completely rewritten

Revision 1.17  2003/11/23 17:58:03  lemmy
removed dead/unused code

Revision 1.16  2003/09/18 15:29:25  zet
centralize writeStream in core
handle IOException rather than throwing it away

Revision 1.15  2003/08/23 15:21:37  zet
remove @author

Revision 1.14  2003/08/23 10:02:02  lemmy
use supertype where possible

Revision 1.13  2003/08/22 21:03:14  lemmy
replace $user$ with $Author: lemmy $

Revision 1.12  2003/07/04 12:29:39  dek
checkstyle

Revision 1.11  2003/07/04 11:52:40  lemmy
bugfix

Revision 1.10  2003/07/04 11:04:14  lemmy
add some opcodes

Revision 1.9  2003/07/03 21:15:06  lemmy
opcodes now from Message

Revision 1.8  2003/07/03 18:43:19  lemmy
cancel should work. untested

Revision 1.7  2003/07/03 16:12:39  lemmy
setState() to protected. not needed outside model.*

Revision 1.6  2003/07/03 16:01:51  lemmy
setState() works now to set the filestate on the mldonkey side

Revision 1.5  2003/06/24 09:29:57  lemmy
Enum more improved

Revision 1.4  2003/06/24 09:22:44  lemmy
better Enum added

Revision 1.3  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.2  2003/06/16 15:33:03  lemmy
some kind of enum added

Revision 1.1  2003/06/14 17:40:40  lemmy
initial commit

Revision 1.4  2003/06/14 12:47:27  lemmy
checkstyle applied

Revision 1.3  2003/06/13 11:03:41  lemmy
changed InputStream to MessageBuffer

Revision 1.2  2003/06/12 22:23:06  lemmy
lots of changes

Revision 1.1  2003/06/11 12:54:43  lemmy
initial commit

*/