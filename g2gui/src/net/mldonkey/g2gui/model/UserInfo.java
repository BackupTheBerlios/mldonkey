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
 * UserInfo
 *
 *
 * @version $Id: UserInfo.java,v 1.5 2003/12/01 14:22:17 lemmster Exp $ 
 *
 */
public class UserInfo extends SimpleInformation {
	/**
	 * User Identifier
	 */
	private int userId;
	/**
	 * MD4
	 */
	private String md4;
	/**
	 * User Name
	 */
	private String userName;
	/**
	 * IP Address
	 */
	private InetAddress ipAddress;
	/**
	 * Port
	 */
	private short port;
	/**
	 * User MetaData
	 */
	private Tag[] tags;
	/**
	 * Server Identifier
	 */
	private int serverId;

	UserInfo() {
		//prevent outer package instanciation
	}
	
	/**
	 * Reads a Query object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		/* 
		 * int32	User Identifier
		 * char[16]	User Md4 
		 * String	User Name 
	  	 * int32	IP address 
		 * int16	port 
		 * List of Tag	User Metadata 
		 * int32	Server Identifier 
		 */
		this.setUserId( messageBuffer.readInt32() );
		this.setMd4( messageBuffer.readBinary( 16 ) );
		this.setUserName( messageBuffer.readString() );

		/* receive the ip address */
		try {
			this.setIpAddress( messageBuffer.readInetAddress() );
		}
		/* do nothing, we get always a valid ip */
		catch ( UnknownHostException e ) { }
		
		this.setPort( messageBuffer.readInt16() );
		this.setTags( messageBuffer.readTagList() );		
		this.setServerId( messageBuffer.readInt32() );
	}

	/**
	 * @return The MD4
	 */
	public String getMd4() {
		return md4;
	}

	/**
	 * @return The Port
	 */
	public short getPort() {
		return port;
	}

	/**
	 * @return The ServerID
	 */
	public int getServerId() {
		return serverId;
	}

	/**
	 * @return The Metadata
	 */
	public Tag[] getTags() {
		return tags;
	}

	/**
	 * @return The UserID
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @return The UserName as a String
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param string The MD4 as a String
	 */
	public void setMd4( String string ) {
		md4 = string;
	}

	/**
	 * @param s The Port as a short
	 */
	public void setPort( short s ) {
		port = s;
	}

	/**
	 * @param i The ServerID as an int
	 */
	public void setServerId( int i ) {
		serverId = i;
	}

	/**
	 * @param tag The Metadata as a Tag[]
	 */
	public void setTags( Tag[] tag ) {
		tags = tag;
	}
	
	/**
	 * @param i The UserID as an int
	 */
	public void setUserId( int i ) {
		userId = i;
	}

	/**
	 * @param string The UserName as a String
	 */
	public void setUserName( String string ) {
		userName = string;
	}

	/**
	 * @return The IPAddress as an InetAddress
	 */
	public InetAddress getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param inet The IP Address as an InetAddress
	 */
	public void setIpAddress( InetAddress inet ) {
		ipAddress = inet;
	}
}

/*
$Log: UserInfo.java,v $
Revision 1.5  2003/12/01 14:22:17  lemmster
ProtocolVersion handling completely rewritten

Revision 1.4  2003/08/23 15:21:37  zet
remove @author

Revision 1.3  2003/08/22 21:03:15  lemmster
replace $user$ with $Author: lemmster $

Revision 1.2  2003/07/04 10:26:03  lemmstercvs01
minor: just checkstyle

Revision 1.1  2003/06/30 07:19:47  lemmstercvs01
initial commit (untested)

*/