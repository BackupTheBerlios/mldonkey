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
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.model.enum.EnumState;

/**
 * ServerInfo
 * 
 * @author ${user}
 * @version $$Id: ServerInfo.java,v 1.13 2003/08/06 09:46:42 lemmstercvs01 Exp $$ 
 */
public class ServerInfo extends Parent {
	/**
	 * Server Identifier
	 */
	private int serverId;
	/**
	 * Server Network
	 */
	private NetworkInfo network;
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
	 * @return The server connection state
	 */
	public State getConnectionState() {
		return connectionState;
	}
	/**
	 * @return The description of this server
	 */
	public String getDescOfServer() {
		return descOfServer;
	}
	/**
	 * @return The name of this server
	 */
	public String getNameOfServer() {
		if ( nameOfServer == null ) 
			return "<unknown>";
		else if ( nameOfServer.equals( "" ) )
			return "<unknown>";
		else
			return nameOfServer;	
	}
	/**
	 * @return The number of files shared on this server
	 */
	public int getNumOfFilesShared() {
		return numOfFilesShared;
	}
	/**
	 * @return The number of users on this server
	 */
	public int getNumOfUsers() {
		return numOfUsers;
	}
	/**
	 * @return The server ip address
	 */
	public Addr getServerAddress() {
		return serverAddress;
	}
	/**
	 * @return The server identifier
	 */
	public int getServerId() {
		return serverId;
	}
	/**
	 * @return The server metadata
	 */
	public Tag[] getServerMetadata() {
		return serverMetadata;
	}
	/**
	 * @return The server network identifier
	 */
	public NetworkInfo getNetwork() {
		return network;
	}
	/**
	 * @return The server port
	 */
	public int getServerPort() {
		/*
		 * NOTE: port is an unsigned short and should
		 * be AND'ed with 0xFFFFL so that it can be treated
		 * as an signed integer.
		 */
		int result = ( int ) ( serverPort & 0xFFFFL );
		return result;
	}
	/**
	 * @return (not used)
	 */
	public int getServerScore() {
		return serverScore;
	}
	/**
	 * @param state a State
	 */
	private void setConnectionState( State state ) {
		connectionState = state;
	}
	
	/**
	 * Creates a new serverinfo object 
	 * @param core The parent objct
	 */
	public ServerInfo( CoreCommunication core ) {
		super( core );
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
		this.serverId = messageBuffer.readInt32();
		this.setNetwork( messageBuffer.readInt32() );
		this.getServerAddress().readStream( messageBuffer );
		this.serverPort = messageBuffer.readInt16();
		this.serverScore = messageBuffer.readInt32();
		this.serverMetadata = messageBuffer.readTagList();		
		this.numOfUsers = messageBuffer.readInt32();
		this.numOfFilesShared = messageBuffer.readInt32();
		this.getConnectionState().readStream( messageBuffer );
		/* if the state is REMOVE_HOST, we delete the serverinfo from the serverinfointmap */
		if ( this.getConnectionState().getState() == EnumState.REMOVE_HOST ) {
			this.parent.getServerInfoIntMap().remove( this );
			return;
		}
		this.nameOfServer = messageBuffer.readString();
		this.descOfServer = messageBuffer.readString();
	}
	
	/**
	 * translate the int to EnumNetwork
	 * @param i the int
	 */
	private void setNetwork( int i ) {
		this.network =
		( NetworkInfo ) this.parent.getNetworkInfoMap().infoIntMap.get( i );
	}
	
	/**
	 * Updates the state of this object
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		byte state = messageBuffer.readByte();
		this.getConnectionState().setState( state );
		/* if this state change to REMOVE_HOST -> remove from serverintmap */
		if ( this.getConnectionState().getState() == EnumState.REMOVE_HOST ) {
			this.parent.getServerInfoIntMap().remove( this );
		}
	}
	
	/**
	 * sets the new state of this server (connecting/disconnect/remove)
	 * @param enum The EnumQuery
	 */
	public void setState( EnumState enum ) {
		Integer anInt = new Integer( this.getServerId() );
		EncodeMessage state = null;
		/* connect server */
		if ( enum == EnumState.CONNECTING ) {
			state =	new EncodeMessage( Message.S_CONNECT_SERVER, anInt );
		}
		/* remove server */
		else if ( enum == EnumState.REMOVE_HOST ) {
			state =	new EncodeMessage( Message.S_REMOVE_SERVER, anInt );
		}
		/* disconnect server */
		else if ( enum == EnumState.NOT_CONNECTED ) {
			state =	new EncodeMessage( Message.S_DISCONNECT_SERVER, anInt );
		}
		/* unvalid input */
		else {
			anInt = null;
			return;
		}
			
		state.sendMessage( this.parent.getConnection() );
		state = null;
		anInt = null;
	}
	
	/**
	 * @return true/false if the server is in our favorites list
	 */
	public boolean isFavorite() {
		return true;
	}
	
	/**
	 * A string representation of this ojb
	 * @return String The String
	 */
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append( "ServerID: " + getServerId() + "\n" );
		result.append( "NetworkID: " + getNetwork() + "\n" );
		result.append( "ServerAddress: " + getServerAddress().getAddress().toString() + "\n" );
		result.append( "ServerPort: " + getServerPort() + "\n" );
		result.append( "ServerScore: " + getServerAddress() + "\n" );
		result.append( "NumOfUsers: " + getNumOfUsers() + "\n" );
		result.append( "NumOfFiles: " + getNumOfFilesShared() + "\n" );
		result.append( "connectionState: " + getConnectionState().getState().toString() + "\n" );
		result.append( "nameOfServer: " + getNameOfServer() + "\n" );
		result.append( "DescOfServer: " + getDescOfServer() + "\n" );
		return result.toString();
	}
}
/*
$$Log: ServerInfo.java,v $
$Revision 1.13  2003/08/06 09:46:42  lemmstercvs01
$toString() added, some bugfixes
$
$Revision 1.12  2003/08/01 17:21:19  lemmstercvs01
$reworked observer/observable design, added multiversion support
$
$Revision 1.11  2003/07/30 19:29:36  lemmstercvs01
$public short getServerPort() -> public int getServerPort() to fix negativ ports
$
$Revision 1.10  2003/07/29 09:47:13  lemmstercvs01
$networkid -> networkInfo, no servename -> "<unknown>"
$
$Revision 1.9  2003/07/06 08:57:09  lemmstercvs01
$getParent() removed
$
$Revision 1.8  2003/07/06 08:49:33  lemmstercvs01
$better oo added
$
$Revision 1.7  2003/07/06 07:45:26  lemmstercvs01
$checkstyle applied
$
$Revision 1.6  2003/07/04 18:35:02  lemmstercvs01
$foobar
$
$Revision 1.5  2003/06/30 07:20:09  lemmstercvs01
$changed to readTagList()
$
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