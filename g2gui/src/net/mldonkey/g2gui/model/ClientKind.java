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
 * ClientKind
 *
 * @author markus
 * @version $Id: ClientKind.java,v 1.1 2003/06/14 17:40:40 lemmstercvs01 Exp $ 
 *
 */
public class ClientKind implements Information {
	/**
	 * Client Type (direct/firewalled)
	 */
	private byte clientType;
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
	 * @return a String
	 */
	public String getClientHash() {
		return clientHash;
	}

	/**
	 * @return a string
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * @return a byte
	 */
	public byte getClientType() {
		return clientType;
	}

	/**
	 * @return an int
	 */
	public int getIpAddress() {
		return ipAddress;
	}

	/**
	 * @return a short
	 */
	public short getPort() {
		return port;
	}

	/**
	 * @param string a string
	 */
	public void setClientHash( String string ) {
		clientHash = string;
	}

	/**
	 * @param string a string
	 */
	public void setClientName( String string ) {
		clientName = string;
	}

	/**
	 * @param b a byte
	 */
	public void setClientType( byte b ) {
		clientType = b;
	}

	/**
	 * @param i an int
	 */
	public void setIpAddress( int i ) {
		ipAddress = i;
	}

	/**
	 * @param s a short
	 */
	public void setPort( short s ) {
		port = s;
	}

	/**
	 * Reads a Client Kind object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		this.setClientType( messageBuffer.readByte() );
		if ( this.getClientType() == 0 ) {
			this.setIpAddress( messageBuffer.readInt32() );
			this.setPort( messageBuffer.readInt16() );
		}
		else {
			this.setClientName( messageBuffer.readString() );
			this.setClientHash( messageBuffer.readBinary( 16 ) );
		}
	}
	
	

}

/*
$Log: ClientKind.java,v $
Revision 1.1  2003/06/14 17:40:40  lemmstercvs01
initial commit

*/