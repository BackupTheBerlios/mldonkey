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
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;

/**
 * ClientInfoList
 * 
 * @author ${user}
 * @version $$Id: ClientInfoList.java,v 1.2 2003/06/14 20:30:44 lemmstercvs01 Exp $$ 
 */
public class ClientInfoList implements Information, InfoList {
	/**
	 * The intern representation of ClientInfoList
	 */
	private TIntObjectHashMap clientInfoList;
	
	/**
	 * Creates a new ClientInfoList
	 */
	public ClientInfoList() {
		this.clientInfoList = new TIntObjectHashMap();
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
		this.put( key, value );
	}
	
	/**
	 * Get the ClientInfo object value to the given key
	 * @param key The Key
	 * @return a ClientInfo object
	 */
	public ClientInfo get( int key ) {
		return this.get( key );
	}
	
	/**
	 * Get a ClientInfoList iterator
	 * @return an iterator
	 */
	public TIntObjectIterator iterator() {
		return this.clientInfoList.iterator();
	}
	
	/**
	 * Update a specific element in the List
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		int key = messageBuffer.readInt32();
		( ( ClientInfo ) this.get( key ) ).update( messageBuffer );
	}
}
/*
$$Log: ClientInfoList.java,v $
$Revision 1.2  2003/06/14 20:30:44  lemmstercvs01
$cosmetic changes
$$
*/