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

import java.util.Iterator;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * OptionsInfo
 *
 * @author $user$
 * @version $Id: OptionsInfoMap.java,v 1.8 2003/06/27 10:35:53 lemmstercvs01 Exp $ 
 *
 */
public class OptionsInfoMap extends InfoMap {
	/**
	 * @param communication my parent
	 */
	public OptionsInfoMap( CoreCommunication communication ) {
		super( communication );
	}

	/**
	 * Creates a new THashMap
	 */	
	public OptionsInfoMap() {
		super();
	}
	
	/**
	 * Reads an OptionsInfo object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		/*
		 * List of (String,String)	The list of options with their current value
		 */
		short listElem = messageBuffer.readInt16();
		for ( int i = 0; i < ( listElem / 2 ); i++ ) {
			OptionsInfo optionsInfo = new OptionsInfo();
			optionsInfo.readStream( messageBuffer );
			this.infoMap.put( optionsInfo.getKey(), optionsInfo );
		}
		
	}
	
	/**
	 * Does nothing!
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		// do nothing!
	}
	/**
	 * When an option is updated, this is to be called
	 * @param name optionname
	 * @param value value
	 */
	public void update( String name, String value ) {
		( ( OptionsInfo )infoMap.get( name ) ).setValue( value );
	}
	
	/**
	 * String representation of this object
	 * @return string A string representation of thi object
	 */
	public String toString() {
		String result = new String();
		Iterator itr = this.infoMap.values().iterator();
		while ( itr.hasNext() ) {
			result += itr.next().toString();
		}
		return result;
	}
}

/*
$Log: OptionsInfoMap.java,v $
Revision 1.8  2003/06/27 10:35:53  lemmstercvs01
removed unneeded calls

Revision 1.7  2003/06/24 09:15:27  lemmstercvs01
checkstyle applied

Revision 1.6  2003/06/21 13:20:36  dek
work on optiontree continued - one can already change client_name in General-leaf

Revision 1.5  2003/06/20 15:15:22  dek
humm, some interface-changes, hope, it didn't break anything ;-)

Revision 1.4  2003/06/17 12:06:51  lemmstercvs01
wrong implementers removed

Revision 1.3  2003/06/16 21:48:38  lemmstercvs01
class hierarchy changed

Revision 1.2  2003/06/16 13:18:59  lemmstercvs01
checkstyle applied

Revision 1.1  2003/06/15 16:18:41  lemmstercvs01
new interface introduced

Revision 1.1  2003/06/14 23:07:20  lemmstercvs01
added opcode 1

*/