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
 * RoomInfoIntMap
 *
 * @author $user$
 * @version $Id: RoomInfoIntMap.java,v 1.3 2003/12/04 08:47:25 lemmy Exp $ 
 *
 */
public class RoomInfoIntMap extends InfoIntMap {

	/**
	 * @param communication my parent
	 */
	RoomInfoIntMap( CoreCommunication communication ) {
		super( communication );
	}

	/**
	 * Reads an OptionsInfo object from a MessageBuffer
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
		int roomNumber = messageBuffer.readInt32();

		/* go 4bytes back in the MessageBuffer */
		messageBuffer.setIterator( messageBuffer.getIterator() - 4 );

		RoomInfo roomInfo;
		if ( this.infoIntMap.containsKey( roomNumber ) ) {
			//update existing NetworkInfo-Object
			roomInfo = ( RoomInfo ) this.infoIntMap.get( roomNumber );
			roomInfo.readStream( messageBuffer );
		}
		else {
			//add a new NetworkInfo-Object to the Map
			roomInfo = parent.getModelFactory().getRoomInfo();
			roomInfo.readStream( messageBuffer );
			synchronized ( this ) {
				this.infoIntMap.put( roomNumber, roomInfo );
			}
		}
		this.setChanged();
		this.notifyObservers( roomInfo );
		
	}

	/**
	 * Does nothing!
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		//do nothing
		
	} }

/*
$Log: RoomInfoIntMap.java,v $
Revision 1.3  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.2  2003/12/01 14:22:17  lemmy
ProtocolVersion handling completely rewritten

Revision 1.1  2003/08/24 16:54:07  dek
RoomInfo is now read from stream to Map, ready for use to implement
all the room-stuff

*/