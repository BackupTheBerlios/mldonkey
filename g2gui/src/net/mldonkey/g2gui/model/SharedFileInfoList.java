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
 * SharedFileInfoList
 *
 * @author $user$
 * @version $Id: SharedFileInfoList.java,v 1.3 2003/07/05 20:04:02 lemmstercvs01 Exp $ 
 *
 */
public class SharedFileInfoList extends InfoList {
	
	/**
	 * @param communication my parent
	 */
	public SharedFileInfoList( CoreCommunication communication ) {
		super( communication );		
	}
	
	/**
	 * Reads a SharedFileInfoList object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		SharedFileInfo sharedFileInfo = new SharedFileInfo();
		sharedFileInfo.readStream( messageBuffer );
		this.infoList.add( sharedFileInfo );
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
$Log: SharedFileInfoList.java,v $
Revision 1.3  2003/07/05 20:04:02  lemmstercvs01
javadoc improved

Revision 1.2  2003/06/20 15:15:22  dek
humm, some interface-changes, hope, it didn't break anything ;-)

Revision 1.1  2003/06/15 16:17:26  lemmstercvs01
opcode 48 added

*/