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

import gnu.trove.TIntObjectHashMap;
import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * ServerInfoList
 *
 * @author $user$
 * @version $Id: ServerInfoList.java,v 1.7 2003/06/16 20:08:38 lemmstercvs01 Exp $ 
 *
 */
public class ServerInfoList extends InfoMap {
	
	/**
	 * Generates a empty ServerInfoList object
	 */
	public ServerInfoList() {
		super();
	}

	/**
	 * Store a key/value pair in this object
	 * @param key The Key
	 * @param value The FileInfo object
	 */
	public void put( int key, ServerInfo value ) {
		this.infoMap.put( key, value );
	}

	/**
	 * Reads a List of ServerInfo objects from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		int id = messageBuffer.readInt32();
		messageBuffer.setIterator( messageBuffer.getIterator() - 4 );
		if ( this.infoMap.containsKey( id ) ) {
			this.get( id ).readStream( messageBuffer );
		}
		else {
			ServerInfo serverInfo = new ServerInfo();
			serverInfo.readStream( messageBuffer );
			this.put( serverInfo.getServerId(), serverInfo );
		}

	}
	
	/**
	 * Get a ServerInfo object from this object by there id
	 * @param id The FileInfo id
	 * @return The FileInfo object
	 */
	public ServerInfo get( int id ) {
		return ( ServerInfo ) this.infoMap.get( id );
	}

	/**
	 * Does nothing!
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		int id = messageBuffer.readInt32();
		this.get( id ).update( messageBuffer );
	}

	/**
	 * @param messageBuffer
	 * cleans up the used Trove-collections. Fills 'em with data the core (mldonkey) finds useful
	 */
	public void clean( MessageBuffer messageBuffer ) {	
		System.out.println( "cleaning up ServerInfoList" );	
		TIntObjectHashMap tempServerInfoList = new TIntObjectHashMap();			
		int[] usefulServers = messageBuffer.readInt32List();		
		for ( int i = 0; i < usefulServers.length; i++ ) {
			int clientID = usefulServers[i];
			tempServerInfoList.put( clientID, this.get( clientID ) );			
		}
		this.infoMap = tempServerInfoList;		
	}
}

/*
$Log: ServerInfoList.java,v $
Revision 1.7  2003/06/16 20:08:38  lemmstercvs01
opcode 13 added

Revision 1.6  2003/06/16 18:05:26  dek
refactored cleanTable

Revision 1.5  2003/06/16 17:42:59  lemmstercvs01
minor changes in readStream()

Revision 1.4  2003/06/16 13:18:59  lemmstercvs01
checkstyle applied

Revision 1.3  2003/06/15 16:18:41  lemmstercvs01
new interface introduced

Revision 1.2  2003/06/14 23:04:08  lemmstercvs01
change from interface to abstract superclass

*/