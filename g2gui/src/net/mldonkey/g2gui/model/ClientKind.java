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
import net.mldonkey.g2gui.model.enum.*;

/**
 * ClientKind
 *
 * @author markus
 * @version $Id: ClientKind.java,v 1.6 2003/07/06 09:37:41 lemmstercvs01 Exp $ 
 *
 */
public class ClientKind implements SimpleInformation {
	/**
	 * Client Type (direct/firewalled)
	 */
	private Enum clientMode;
	/**
	 * Ip Address (present only if client type = 0)
	 */
	private int ipAddress;
	/**
	 * Port (present only if client type = 0)
	 */
	private short port;
	/**
	 * Client Name (present only if client type = 1)
	 */
	private String clientName;
	/**
	 * Client Hash (present only if client type = 1)
	 */
	private String clientHash;

	/**
	 * @return The client hash identifier
	 */
	public String getClientHash() {
		return clientHash;
	}

	/**
	 * @return The client name
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * @return The clientmode
	 */
	public Enum getClientMode() {
		return clientMode;
	}

	/**
	 * @return The client ip address
	 * (if ClientMode == EnumClientMode.DIRECT)
	 */
	public int getIpAddress() {
		return ipAddress;
	}

	/**
	 * @return The client port
	 * (if ClientMode == EnumClientMode.DIRECT)
	 */
	public short getPort() {
		return port;
	}

	/**
	 * @param b Sets the client mode
	 */
	private void setClientMode( byte b ) {
		if ( b == 0 )
			clientMode = EnumClientMode.DIRECT;
		else if ( b == 1 )
			clientMode = EnumClientMode.FIREWALLED;
	}

	/**
	 * Reads a Client Kind object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		this.setClientMode( messageBuffer.readByte() );
		if ( this.getClientMode() == EnumClientMode.DIRECT ) {
			this.ipAddress = messageBuffer.readInt32();
			this.port = messageBuffer.readInt16();
		}
		else {
			this.clientName = messageBuffer.readString();
			this.clientHash = messageBuffer.readBinary( 16 );
		}
	}
}

/*
$Log: ClientKind.java,v $
Revision 1.6  2003/07/06 09:37:41  lemmstercvs01
javadoc improved

Revision 1.5  2003/06/24 09:29:57  lemmstercvs01
Enum more improved

Revision 1.4  2003/06/24 09:16:48  lemmstercvs01
better Enum added

Revision 1.3  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.2  2003/06/16 15:33:03  lemmstercvs01
some kind of enum added

Revision 1.1  2003/06/14 17:40:40  lemmstercvs01
initial commit

*/