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
 * ServerInfo
 * 
 * @author ${user}
 * @version $$Id: ServerInfo.java,v 1.4 2003/06/18 13:30:56 dek Exp $$ 
 */
public class ServerInfo implements SimpleInformation {
	/**
	 * Server Identifier
	 */
	private int serverId;
	/**
	 * Server Network Identifier
	 */
	private int serverNetworkId;
	/**
	 * Server Address
	 */
	private Addr serverAddress = new Addr();
	/**
	 * Server Port
	 */
	private short serverPort;
	/**
	 * Server Score
	 */
	private int serverScore;
	/**
	 * Server Metadata
	 */
	private Tag[] serverMetadata;
	/**
	 * Number of Users
	 */
	private int numOfUsers;
	/**
	 * Number of Files shared
	 */
	private int numOfFilesShared;
	/**
	 * Connection State
	 */
	private State connectionState = new State();
	/**
	 * Name of Server
	 */
	private String nameOfServer;
	/**
	 * Description of Server
	 */
	private String descOfServer;

	/**
	 * @return a State
	 */
	public State getConnectionState() {
		return connectionState;
	}

	/**
	 * @return a string
	 */
	public String getDescOfServer() {
		return descOfServer;
	}

	/**
	 * @return a string
	 */
	public String getNameOfServer() {
		return nameOfServer;
	}

	/**
	 * @return an int
	 */
	public int getNumOfFilesShared() {
		return numOfFilesShared;
	}

	/**
	 * @return an int
	 */
	public int getNumOfUsers() {
		return numOfUsers;
	}

	/**
	 * @return an Addr
	 */
	public Addr getServerAddress() {
		return serverAddress;
	}

	/**
	 * @return an int
	 */
	public int getServerId() {
		return serverId;
	}

	/**
	 * @return a Tag[]
	 */
	public Tag[] getServerMetadata() {
		return serverMetadata;
	}

	/**
	 * @return an int
	 */
	public int getServerNetworkId() {
		return serverNetworkId;
	}

	/**
	 * @return a short
	 */
	public short getServerPort() {
		return serverPort;
	}

	/**
	 * @return an int
	 */
	public int getServerScore() {
		return serverScore;
	}

	/**
	 * @param state a State
	 */
	public void setConnectionState( State state ) {
		connectionState = state;
	}

	/**
	 * @param string a String
	 */
	public void setDescOfServer( String string ) {
		descOfServer = string;
	}

	/**
	 * @param string a String
	 */
	public void setNameOfServer( String string ) {
		nameOfServer = string;
	}

	/**
	 * @param i an int
	 */
	public void setNumOfFilesShared( int i ) {
		numOfFilesShared = i;
	}

	/**
	 * @param i an int
	 */
	public void setNumOfUsers( int i ) {
		numOfUsers = i;
	}

	/**
	 * @param addr an Addr
	 */
	public void setServerAddress( Addr addr ) {
		serverAddress = addr;
	}

	/**
	 * @param i an int
	 */
	public void setServerId( int i ) {
		serverId = i;
	}

	/**
	 * @param tags a Tag[]
	 */
	public void setServerMetadata( Tag[] tags ) {
		serverMetadata = tags;
	}

	/**
	 * @param i an int
	 */
	public void setServerNetworkId( int i ) {
		serverNetworkId = i;
	}

	/**
	 * @param s a short
	 */
	public void setServerPort( short s ) {
		serverPort = s;
	}

	/**
	 * @param i an int
	 */
	public void setServerScore( int i ) {
		serverScore = i;
	}
	
	/**
	 * Reads a ServerInfo object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		/*
		 * int32  	 Server identifier 
 		 * int32  	 Server Network Identifier 
 		 * Addr  	 Server Address 
 		 * int16  	 Server Port 
 		 * int32  	 Server Score (not used) 
 		 * List of Tag  	 Server Metadata 
 		 * int32  	 Number of users 
 		 * int32  	 Number of files shared 
 		 * State  	 Connection State 
 		 * String  	 Name of Server 
 		 * String  	 Description of Server
		 */
		this.setServerId( messageBuffer.readInt32() );
		this.setServerNetworkId( messageBuffer.readInt32() );
		this.getServerAddress().readStream( messageBuffer );
		this.setServerPort( messageBuffer.readInt16() );
		this.setServerScore( messageBuffer.readInt32() );
		
		short listElem = messageBuffer.readInt16();
		serverMetadata = new Tag[ listElem ];
		for ( int i = 0; i < listElem; i++ ) {
			Tag aTag = new Tag();
			aTag.readStream( messageBuffer );
			this.serverMetadata[ i ] = aTag;
		}
		
		this.setNumOfUsers( messageBuffer.readInt32() );
		this.setNumOfFilesShared( messageBuffer.readInt32() );
		this.getConnectionState().readStream( messageBuffer );
		this.setNameOfServer( messageBuffer.readString() );
		this.setDescOfServer( messageBuffer.readString() );
	}
	
	/**
	 * Updates the state of this object
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		byte state = messageBuffer.readByte();
		this.getConnectionState().setState( state );
	}
}
/*
$$Log: ServerInfo.java,v $
$Revision 1.4  2003/06/18 13:30:56  dek
$Improved Communication Layer view <--> model by introducing a super-interface
$
$Revision 1.3  2003/06/16 20:08:38  lemmstercvs01
$opcode 13 added
$
$Revision 1.2  2003/06/14 20:30:44  lemmstercvs01
$cosmetic changes
$$
*/