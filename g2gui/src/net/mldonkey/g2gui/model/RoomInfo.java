/*
 * Copyright 2003
 * G2GUI Team
 * 
 * 
 * This file is part of G2GUI.
 *
 * G2GUI is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2GUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2GUI; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.model;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * RoomInfo
 *
 * @author $user$
 * @version $Id: RoomInfo.java,v 1.1 2003/08/24 16:54:07 dek Exp $ 
 *
 */
public class RoomInfo extends Parent {

	private Enum roomstate;

	private int numerOfUsers;

	private String RoomName;

	private NetworkInfo network;

	private int roomNumber;

	/**
	 * Creates a new RoomInfo
	 * @param core The parent
	 */
	public RoomInfo( CoreCommunication core ) {
		super( core );
	}

	/**
	 * Reads an RoomInfo object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		/*
		 * 	 int32		room_num 
		 * 	 int32		room_network 
		 * 	 String		room_name 
		 * 	 int8		room_state (0=Opened, 1=Closed, 2=Paused) 
		 * 	 int32		room_nusers (if proto < 3 return a static 0, do not read an int32
		 */
		 this.roomNumber = messageBuffer.readInt32();
		 this.network = parent.getNetworkInfoMap().get( messageBuffer.readInt32() ) ;
		 this.RoomName = messageBuffer.readString();
		 switch ( messageBuffer.readInt8() ) {
			case 0 :	
				this.roomstate = Enum.OPEN;		
				break;
			case 1 :
				this.roomstate = Enum.CLOSED;
				break;
			case 2 :
				this.roomstate = Enum.PAUSED;
				break;
			default :
				this.roomstate = Enum.UNKNOWN;
				break;			
		} 		  
		if ( parent.getProtoToUse() < 3 ) this.numerOfUsers = 0;
			else this.numerOfUsers = messageBuffer.readInt32();
		
	}
		/**
		 * Creates a new EnumRoom obj to differ RoomState
		 */
		public static class Enum {
			/**
			 * Room open
			 */
			public static Enum OPEN = new Enum();
			/**
			 * Room closed
			 */
			public static Enum CLOSED = new Enum();
			/**
			 * Room paused
			 */
			public static Enum PAUSED = new Enum();
			/**
			 * state unknown
			 */
			public static Enum UNKNOWN = new Enum();
			
			private Enum() { }
		}
	
}

/*
$Log: RoomInfo.java,v $
Revision 1.1  2003/08/24 16:54:07  dek
RoomInfo is now read from stream to Map, ready for use to implement
all the room-stuff

*/