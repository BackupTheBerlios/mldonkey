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

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * Addr
 * 
 * @author ${user}
 * @version $$Id: Addr.java,v 1.6 2003/07/05 20:04:10 lemmstercvs01 Exp $$ 
 */
public class Addr implements SimpleInformation {
	/**
	 * Address Type
	 */
	private boolean addressType;
	/**
	 * IP Address (if type is true)
	 */
	private InetAddress ipAddress;
	/**
	 * Name Address (if type is false
	 */
	private String nameAddress;

	/**
	 * @return direct or indirekt address
	 */
	public boolean isAddressType() {
		return addressType;
	}

	/**
	 * @return The IP address
	 */
	public InetAddress getIpAddress() {
		return ipAddress;
	}

	/**
	 * @return The address name
	 */
	public String getNameAddress() {
		return nameAddress;
	}

	/**
	 * @param b a boolean
	 */
	private void setAddressType( byte b ) {
		if ( b == 0 ) 
			addressType = true;
		if ( b == 1 )
			addressType = false;
	}

	/**
	 * Reads an Addr object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		/*
		 * int8  	 Address Type: 0 = Address is IP, 1 = Address is Name 
 		 * int32  	 IP address (present only if Address Type = 0) 
 		 * String  	 Name address (present only if Address Type = 1) 
		 */
		this.setAddressType( messageBuffer.readByte() );
		if ( this.isAddressType() )
			try {
				this.ipAddress = messageBuffer.readInetAddress();
			}
			/* do nothing, we receive always a valid address */
			catch ( UnknownHostException e ) { }
		else
			this.nameAddress = messageBuffer.readString();
	}
}
/*
$$Log: Addr.java,v $
$Revision 1.6  2003/07/05 20:04:10  lemmstercvs01
$javadoc improved
$
$Revision 1.5  2003/07/04 10:26:03  lemmstercvs01
$minor: just checkstyle
$
$Revision 1.4  2003/06/30 07:21:27  lemmstercvs01
$changed ip from int to InetAddress
$
$Revision 1.3  2003/06/18 13:30:56  dek
$Improved Communication Layer view <--> model by introducing a super-interface
$
$Revision 1.2  2003/06/14 20:30:44  lemmstercvs01
$cosmetic changes
$$
*/
