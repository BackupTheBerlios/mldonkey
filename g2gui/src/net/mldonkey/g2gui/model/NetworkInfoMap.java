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

/**
 * OptionsInfo
 *
 * @author $user$
 * @version $Id: NetworkInfoMap.java,v 1.1 2003/06/16 15:05:20 dek Exp $ 
 *
 */
public class NetworkInfoMap implements InfoCollection {
	/**
	 * Map containing option value pairs
	 */
	private TIntObjectHashMap networkInfoMap;
	
	/**
	 * Creates a new THashMap
	 */	
	public NetworkInfoMap() {
		this.networkInfoMap = new TIntObjectHashMap();
	}
	
	/**
	 * Reads an OptionsInfo object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		int network_id = messageBuffer.readInt32();
		
		if ( networkInfoMap.containsKey( network_id ) ) {
			//update existing NetworkInfo-Object
			NetworkInfo networkInfo = (NetworkInfo)networkInfoMap.get(network_id);
			networkInfo.readStream(messageBuffer,network_id);
		}
		else {
			//add a new NetworkInfo-Object to the Map
			NetworkInfo networkInfo = new NetworkInfo();
			networkInfo.readStream( messageBuffer,network_id );
			this.networkInfoMap.put( network_id, networkInfo );
		}
	}
	
	/**
	 * Does nothing!
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		// do nothing!
	}
	

}

/*
$Log: NetworkInfoMap.java,v $
Revision 1.1  2003/06/16 15:05:20  dek
*** empty log message ***

*/