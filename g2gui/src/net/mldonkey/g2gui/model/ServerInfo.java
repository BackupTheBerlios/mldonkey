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
import net.mldonkey.g2gui.model.enum.EnumNetwork;
import net.mldonkey.g2gui.model.enum.EnumState;

/**
 * ServerInfo
 * 
 *
 * @version $Id: ServerInfo.java,v 1.25 2003/11/29 13:01:32 lemmster Exp $
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
	private String nameOfServer = "";
	/**
	 * Description of Server
	 */
	private String descOfServer = "";
	/**
	 * Is this server a favorite
	 */
	private boolean favorite;

	/**
	 * @return The server connection state
	 */
	public State getConnectionState() {
		return connectionState;
	}
	
	/**
	 * For the moment just ed2k supported
	 * @return A String with a link for this server
	 */
	public String getLink() {
		if ( this.network.equals( EnumNetwork.DONKEY ) ) {
			/* |server|ip|port|preference */
			String aString = "ed2k://|" + this.getNameOfServer();
			aString += "|" + this.getServerAddress().toString();
			aString += "|" + this.getServerPort();				
			return aString;
		}			
		return "only ed2k links are working atm! more to come";		
	}
	
	/**
	 * @return The description of this server
	 */
	public String getDescOfServer() {
		if ( nameOfServer == null || nameOfServer.equals( "" ) ) 
			return "<no description>";
		else
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
		this.nameOfServer = messageBuffer.readString();
		this.descOfServer = messageBuffer.readString();
		/*
		 *  if the state is REMOVE_HOST, we delete the serverinfo from the serverinfointmap
		 * (this is a workaround for donkey, because the ServerState msg isnt send
		 */
		this.checkForRemove();
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
		this.checkForRemove();
	}
	
	/**
	 * Check this for state "REMOVE_HOST" and remove this from the serverinfointmap
	 * and add it to the removed list
	 */
	private void checkForRemove() {
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
		Message state = null;
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
			
		state.sendMessage( this.parent );
		state = null;
		anInt = null;
	}
	
	/**
	 * convert the favorites state
	 */
	public void setFavorites() {
		//TODO add favorites (need new opcode)
	}
	
	/**
	 * @return true/false if the server is in our favorites list
	 */
	public boolean isFavorite() {
		return this.favorite;
	}
	
	/**
	 * is this server connected atm
	 * @return true when connected, false otherwise
	 */
	public boolean isConnected() {
		if ( this.connectionState.getState() == EnumState.CONNECTED )
			return true;
		return false;	
	}
	
	/**
	 * A string representation of this ojb
	 * @return String The String
	 */
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append( "ServerID: " + getServerId() + "\n" );
		result.append( "NetworkID: " + getNetwork() + "\n" );
		result.append( "ServerAddress: " + getServerAddress().toString() + "\n" );
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
$Log: ServerInfo.java,v $
Revision 1.25  2003/11/29 13:01:32  lemmster
Addr.getString() renamed to the more natural word name Addr.toString()

Revision 1.24  2003/10/28 11:07:32  lemmster
move NetworkInfo.Enum -> enum.EnumNetwork
add MaskMatcher for "Enum[]"

Revision 1.23  2003/09/24 09:35:57  lemmster
serverlink in menulistener

Revision 1.22  2003/09/18 15:29:25  zet
centralize writeStream in core
handle IOException rather than throwing it away

Revision 1.21  2003/09/18 09:16:47  lemmster
checkstyle

Revision 1.20  2003/08/23 15:21:37  zet
remove @author

Revision 1.19  2003/08/23 10:02:02  lemmster
use supertype where possible

Revision 1.18  2003/08/22 21:03:15  lemmster
replace $user$ with $Author: lemmster $

Revision 1.17  2003/08/11 11:22:53  lemmstercvs01
avoid npes

Revision 1.16  2003/08/07 12:35:31  lemmstercvs01
cleanup, more efficient

Revision 1.15  2003/08/06 20:56:49  lemmstercvs01
cleanup, more efficient

Revision 1.14  2003/08/06 17:38:38  lemmstercvs01
some actions still missing. but it should work for the moment

Revision 1.13  2003/08/06 09:46:42  lemmstercvs01
toString() added, some bugfixes

Revision 1.12  2003/08/01 17:21:19  lemmstercvs01
reworked observer/observable design, added multiversion support

Revision 1.11  2003/07/30 19:29:36  lemmstercvs01
public short getServerPort() -> public int getServerPort() to fix negativ ports

Revision 1.10  2003/07/29 09:47:13  lemmstercvs01
networkid -> networkInfo, no servename -> "<unknown>"

Revision 1.9  2003/07/06 08:57:09  lemmstercvs01
getParent() removed

Revision 1.8  2003/07/06 08:49:33  lemmstercvs01
better oo added

Revision 1.7  2003/07/06 07:45:26  lemmstercvs01
checkstyle applied

Revision 1.6  2003/07/04 18:35:02  lemmstercvs01
foobar

Revision 1.5  2003/06/30 07:20:09  lemmstercvs01
changed to readTagList()

Revision 1.4  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.3  2003/06/16 20:08:38  lemmstercvs01
opcode 13 added

Revision 1.2  2003/06/14 20:30:44  lemmstercvs01
cosmetic changes
*/