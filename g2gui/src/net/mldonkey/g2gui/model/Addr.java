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
 *
 * @version $Id: Addr.java,v 1.15 2003/11/07 15:12:14 zet Exp $
 */
public class Addr implements SimpleInformation {
	/**
	 * Address Type
	 */
	private boolean addressType;
	/**
	 * IP Address
	 */
	private InetAddress address;
	/**
	 * Host Name
	 */
	private String hostName;

	/**
	 * @return direct/true or indirekt/false address
	 */
	public boolean hasHostName() {
		return addressType;
	}

	/**
	 * @return The address
	 */
	public InetAddress getAddress() {
		return this.address;
	}
	/**
	 * @return The HostName
	 */
	public String getHostName() {
		return this.hostName;
	}

	/**
	 * @param b a boolean
	 */
	private void setAddressType( byte b ) {
		if ( b == 0 ) 
			addressType = false;
		if ( b == 1 )
			addressType = true;
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
		if ( this.hasHostName() )
			this.hostName = messageBuffer.readString();	
		else
			try {
				this.address = messageBuffer.readInetAddress();
			}
			catch ( UnknownHostException e ) { }
	}

	/**
	 * @param anAddress The address to compare
	 * @return an Int
	 */
	public int compareTo( Addr anAddress ) {
		/* compare between hasHostName() */
		if ( this.hasHostName() && !anAddress.hasHostName() )
			return 1;
		if ( !this.hasHostName() && anAddress.hasHostName() )
			return -1;
		if ( this.hasHostName() && anAddress.hasHostName() )
			return this.getHostName().compareToIgnoreCase( anAddress.getHostName() );
			
		return this.compareTo( this.address.getAddress(), anAddress.address.getAddress(), 0 );
	}
	
	private int compareTo( byte[] aByte1, byte[] aByte2, int index ) {
		// break condition (assume aByte1 and aByte2 have same length)
		if ( index >= aByte1.length )
			return 0;
			
		int i1 = aByte1[ index ] & 0xff;
		int i2 = aByte2[ index ] & 0xff;	
			
		// 1 > 2	
		if ( i1 > i2 )
			return 1;
			
		// 1 < 2	
		if ( i1 < i2 )
			return -1;

		// equal -> recusion
		return this.compareTo( aByte1, aByte2, ++index );
	}
}
/*
$Log: Addr.java,v $
Revision 1.15  2003/11/07 15:12:14  zet
fix log

Revision 1.14  2003/10/20 19:30:13  lemmster
fixed Bug #1006

Revision 1.13  2003/09/02 09:24:36  lemmster
checkstyle

Revision 1.12  2003/08/23 15:21:37  zet
remove @author

Revision 1.11  2003/08/22 21:03:15  lemmster
replace user with Author: lemmster 

Revision 1.10  2003/08/11 11:23:06  lemmstercvs01
fix sort by string

Revision 1.9  2003/08/09 13:42:24  dek
added gnu.regexp for compiling with gcj
you can get it at:

ftp://ftp.tralfamadore.com/pub/java/gnu.regexp-1.1.4.tar.gz

Revision 1.8  2003/08/06 17:36:06  lemmstercvs01
save a string address as string and not as InetAddress anymore (speed!!)

Revision 1.7  2003/07/30 19:27:49  lemmstercvs01
address is always an InetAddress instead of just a String

Revision 1.6  2003/07/05 20:04:10  lemmstercvs01
javadoc improved

Revision 1.5  2003/07/04 10:26:03  lemmstercvs01
minor: just checkstyle

Revision 1.4  2003/06/30 07:21:27  lemmstercvs01
changed ip from int to InetAddress

Revision 1.3  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.2  2003/06/14 20:30:44  lemmstercvs01
cosmetic changes
*/
