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
import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * RoomInfo3
 *
 * @version $Id: RoomInfo3.java,v 1.2 2003/12/04 08:47:25 lemmy Exp $ 
 *
 */
public class RoomInfo3 extends RoomInfo {
	private int numerOfUsers;
	/**
	 * @param core
	 */
	RoomInfo3(CoreCommunication core) {
		super(core);
	}

	/**
	 * Reads an RoomInfo object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		/*
		 *          int32                room_num
		 *          int32                room_network
		 *          String                room_name
		 *          int8                room_state (0=Opened, 1=Closed, 2=Paused)
		 *          int32                room_nusers (if proto < 3 return a static 0, do not read an int32
		 */
		this.roomNumber = messageBuffer.readInt32();
		this.network = parent.getNetworkInfoMap().get( messageBuffer.readInt32() );
		this.RoomName = messageBuffer.readString();
		switch ( messageBuffer.readInt8() ) {
			case 0:
				this.roomstate = Enum.OPEN;
				break;
			case 1:
				this.roomstate = Enum.CLOSED;
				break;
			case 2:
				this.roomstate = Enum.PAUSED;
				break;
			default:
				this.roomstate = Enum.UNKNOWN;
				break;
		}
		this.numerOfUsers = messageBuffer.readInt32();
	}
	
}

/*
$Log: RoomInfo3.java,v $
Revision 1.2  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.1  2003/12/01 14:22:17  lemmy
ProtocolVersion handling completely rewritten

*/