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
 * ClientInfoList
 * 
 * @author ${user}
 * @version $$Id: ClientInfoList.java,v 1.4 2003/06/15 13:14:06 lemmstercvs01 Exp $$ 
 */
public class ClientInfoList extends InfoList {
	/**
	 * Creates a new ClientInfoList
	 */
	public ClientInfoList() {
		super();
	}
	
	/**
	 * Reads a networkInfo object from the stream
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		ClientInfo clientInfo = new ClientInfo();
		clientInfo.readStream( messageBuffer );
		this.put( clientInfo.getClientid(), clientInfo );
	}
	
	/**
	 * Store a key/value pair in this object
	 * @param key The Key
	 * @param value The ClientInfo object
	 */
	public void put( int key, ClientInfo value ) {
		this.infoList.put( key, value );
	}
	
	/**
	 * Update a specific element in the List
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		int key = messageBuffer.readInt32();
		( ( ClientInfo ) this.infoList.get( key ) ).update( messageBuffer );
	}
}
/*
$$Log: ClientInfoList.java,v $
$Revision 1.4  2003/06/15 13:14:06  lemmstercvs01
$fixed a bug in put()
$
$Revision 1.3  2003/06/14 23:04:08  lemmstercvs01
$change from interface to abstract superclass
$
$Revision 1.2  2003/06/14 20:30:44  lemmstercvs01
$cosmetic changes
$$
*/