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
 * DefineSearchMap
 *
 * @author $Author: lemmster $
 * @version $Id: DefineSearchMap.java,v 1.5 2003/08/22 21:03:15 lemmster Exp $ 
 *
 */
public class DefineSearchMap extends InfoMap {
	
	/**
	 * @param communication my parent
	 */
	public DefineSearchMap( CoreCommunication communication ) {
		super( communication );
	}
	
	/**
	 * Reads a DefineSearch object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		short listElem = messageBuffer.readInt16();
		for ( int i = 0; i < listElem; i++ ) {
			String aString = messageBuffer.readString();
			Query aQuery = new Query();
			aQuery.readStream( messageBuffer );
			this.infoMap.put( aString, aQuery );
		}
	}

	/**
	 * Does nothing!
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		// do nothing
	}
}

/*
$Log: DefineSearchMap.java,v $
Revision 1.5  2003/08/22 21:03:15  lemmster
replace $user$ with $Author$

Revision 1.4  2003/07/06 09:38:10  lemmstercvs01
useless constructor removed

Revision 1.3  2003/06/20 15:15:22  dek
humm, some interface-changes, hope, it didn't break anything ;-)

Revision 1.2  2003/06/17 12:07:37  lemmstercvs01
checkstyle applied

Revision 1.1  2003/06/16 21:48:57  lemmstercvs01
opcode 3 added

*/