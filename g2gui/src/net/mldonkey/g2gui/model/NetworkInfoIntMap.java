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

import gnu.trove.TIntObjectIterator;
import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * OptionsInfo
 *
 * @author $user$
 * @version $Id: NetworkInfoIntMap.java,v 1.9 2003/08/01 17:21:19 lemmstercvs01 Exp $ 
 *
 */
public class NetworkInfoIntMap extends InfoIntMap {
	
	/**
	 * @param communication my parent
	 */
	public NetworkInfoIntMap( CoreCommunication communication ) {
		super( communication );
	}
	
	/**
	 * Reads an OptionsInfo object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		int id = messageBuffer.readInt32();

		/* go 4bytes back in the MessageBuffer */
		messageBuffer.setIterator( messageBuffer.getIterator() - 4 );

		if ( this.infoIntMap.containsKey( id ) ) {
			//update existing NetworkInfo-Object
			NetworkInfo networkInfo = ( NetworkInfo ) this.infoIntMap.get( id );
			networkInfo.readStream( messageBuffer );
		}
		else {
			//add a new NetworkInfo-Object to the Map
			NetworkInfo networkInfo = new NetworkInfo( this.parent );
			networkInfo.readStream( messageBuffer );
			synchronized ( this ) {
				this.infoIntMap.put( id, networkInfo );
			}
		}
		this.setChanged();
		this.notifyObservers( this );
	}
	
	/**
	 * Does nothing!
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		// do nothing!
	}

	/**
	 * Get the networkinfo to this key
	 * @param key the key of the networkinfo
	 * @return the networkinfo to the key
	 */	
	public NetworkInfo get( int key ) {
		return ( NetworkInfo ) this.infoIntMap.get( key );
	}
	
	/**
	 * returns an array with all networks known to this NetworkMap
	 * (remember, an Iterator is much faster. so use it instead if you iterate often)
	 * @return all known networks
	 */
	public NetworkInfo[] getNetworks() {
		Object[] temp = this.infoIntMap.getValues();
		NetworkInfo[] result = new NetworkInfo[ temp.length ];
		for ( int i = 0; i < temp.length; i++ ) {
			result[ i ] = ( NetworkInfo ) temp [ i ];
		}
		return result;		
	}
	
	/**
	 * Get a network by the NetworkInfo.Enum or null i network is not presented
	 * @param enum The NetworkInfo.Enum to search for
	 * @return The corresponding NetworkInfo
	 */
	public NetworkInfo getByEnum( NetworkInfo.Enum enum ) {
		TIntObjectIterator itr = this.infoIntMap.iterator();
		int size = this.infoIntMap.size();
		synchronized ( this ) { 
			for ( ; size > 0; size-- ) {
				itr.advance();
				NetworkInfo elem = ( NetworkInfo ) itr.value();
				if ( elem.getNetworkType() == enum ) {
					return elem;
				}
			}
		}
		return null;
	}
}

/*
$Log: NetworkInfoIntMap.java,v $
Revision 1.9  2003/08/01 17:21:19  lemmstercvs01
reworked observer/observable design, added multiversion support

Revision 1.8  2003/07/31 14:09:09  lemmstercvs01
synchronized(...) added

Revision 1.7  2003/07/28 08:17:41  lemmstercvs01
added getByEnum()

Revision 1.6  2003/07/07 20:01:35  dek
some useful method introduced: Networkitem[] getNetworks()

Revision 1.5  2003/07/05 15:58:05  lemmstercvs01
javadoc improved

Revision 1.4  2003/07/04 18:35:02  lemmstercvs01
foobar

Revision 1.3  2003/06/20 15:15:22  dek
humm, some interface-changes, hope, it didn't break anything ;-)

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