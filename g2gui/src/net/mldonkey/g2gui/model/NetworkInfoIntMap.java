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
 * OptionsInfo
 *
 * @author $user$
 * @version $Id: NetworkInfoIntMap.java,v 1.2 2003/06/17 12:06:51 lemmstercvs01 Exp $ 
 *
 */
public class NetworkInfoIntMap extends InfoIntMap {
	/**
	 * Creates a new THashMap
	 */	
	public NetworkInfoIntMap() {
		super();
	}
	
	/**
	 * Reads an OptionsInfo object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		int id = messageBuffer.readInt32();
		
		if ( this.infoIntMap.containsKey( id ) ) {
			//update existing NetworkInfo-Object
			NetworkInfo networkInfo = ( NetworkInfo ) this.infoIntMap.get( id );
			networkInfo.readStream( messageBuffer, id );
		}
		else {
			//add a new NetworkInfo-Object to the Map
			NetworkInfo networkInfo = new NetworkInfo();
			networkInfo.readStream( messageBuffer, id );
			this.infoIntMap.put( id, networkInfo );
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
$Log: NetworkInfoIntMap.java,v $
Revision 1.2  2003/06/17 12:06:51  lemmstercvs01
wrong implementers removed

Revision 1.1  2003/06/16 21:47:19  lemmstercvs01
just refactored (name changed)

Revision 1.3  2003/06/16 20:10:45  lemmstercvs01
moved to the right place

Revision 1.2  2003/06/16 17:43:55  lemmstercvs01
checkstyle applied

Revision 1.1  2003/06/16 15:05:20  dek
*** empty log message ***

*/