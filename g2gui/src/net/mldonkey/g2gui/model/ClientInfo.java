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

import gnu.trove.THash;
import gnu.trove.TIntObjectHashMap;
import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.model.enum.EnumClientMode;
import net.mldonkey.g2gui.model.enum.EnumClientType;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

/**
 * ClientInfo
 *
 *
 * @version $Id: ClientInfo.java,v 1.25 2003/09/14 03:37:24 zet Exp $ 
 *
 */
public class ClientInfo extends Parent {
	/**
	 * Client Id
	 */
	private int clientid;
	/**
	 * Client Network Id
	 */
	private NetworkInfo clientnetworkid;
	/**
	 * Client Kind
	 */
	private ClientKind clientKind = new ClientKind();
	/**
	 * Client State
	 */
	private State state = new State();
	/**
	 * Client Type
	 */
	private Enum clientType;
	/**
	 * List of Tags
	 */
	private Tag[] tag;
	/**
	 * Client Name
	 */
	private String clientName;
	/**
	 * Client Rate
	 */
	private int clientRating;
	/**
	 * Client Chat Port (mlchat)
	 */
	private int clientChatPort;
	/**
	 * Availability of a file (int fileId, String availability)
	 */
	private THash avail = new TIntObjectHashMap();

	/**
	 * @return The client chat port
	 */
	public int getClientChatPort() {
		return clientChatPort;
	}

	/**
	 * @return The client identifier
	 */
	public int getClientid() {
		return clientid;
	}

	/**
	 * @return The client kind
	 */
	public ClientKind getClientKind() {
		return clientKind;
	}

	/**
	 * @return The client name
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * @return The client network identifier
	 */
	public NetworkInfo getClientnetworkid() {
		return clientnetworkid;
	}

	/**
	 * @return This clients Rating (not used yet)
	 */
	public int getClientRating() {		
		return clientRating;
	}
	
	/**
	 *returns a string with file-availability-Information
	 * @param fileInfo which file do you wnat the availability for?
	 * @return String representation of the avail.
	 */
	public String getFileAvailability( FileInfo fileInfo ) {
		return ( String ) ( ( TIntObjectHashMap ) this.avail ).get( fileInfo.getId() );
	}
	
	/**
	 * @param fileInfo
	 * @return int numberOfFullChunks
	 */
	public int getNumChunks( FileInfo fileInfo ) {
		int numChunks = 0;
		String availability = getFileAvailability ( fileInfo );
		if ( availability != null ) {
			for ( int i = 0; i < availability.length() ; i++ ) 
				if ( availability.charAt( i ) == '1' ) numChunks++;
		}
		return numChunks;
	}

	/**
	 * @return The client type
	 */
	public Enum getClientType() {
		return clientType;
	}

	/**
	 * @return The client state
	 */
	public State getState() {
		return state;
	}

	/**
	 * @return The client tags
	 */
	public Tag[] getTag() {
		return tag;
	}

	/**
	 * @param b a byte
	 */
	private void setClientType( byte b ) {
		if ( b == 0 )
			clientType = EnumClientType.SOURCE;
		else if ( b == 1 )
			clientType = EnumClientType.FRIEND;
		else if ( b == 2 )
			clientType = EnumClientType.BROWSED;		
	}
	
	/**
	 * @return String clientActivity
	 */
	public String getClientActivity() {
		if ( this.getState().getState() == EnumState.CONNECTED_DOWNLOADING )
			return G2GuiResources.getString( "TT_Transferring" ).toLowerCase();
		else 
			return G2GuiResources.getString( "TT_Rank" ).toLowerCase() + ": " + this.getState().getRank() ;
	}	
	
	/**
	 * @return String clientConnection
	 */
	public String getClientConnection() {
		if ( this.getClientKind().getClientMode() == EnumClientMode.FIREWALLED ) 
			return G2GuiResources.getString( "TT_Firewalled" ).toLowerCase();			
		else
			return G2GuiResources.getString( "TT_Direct" ).toLowerCase();	
	}
	
	/**
	 * @param core with this object, we make our main cimmunication (the main-Layer)
	 */
	public ClientInfo( CoreCommunication core ) {
		super( core );
	}

	/**
	 * Reads a ClientInfo object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		int clientID = messageBuffer.readInt32();
		readStream ( clientID, messageBuffer );
	}
	
	/**
	 * 
	 * @param clientID
	 * @param messageBuffer
	 */
	public void readStream( int clientID, MessageBuffer messageBuffer ) {
		this.clientid = clientID;
		
		this.clientnetworkid =
			( NetworkInfo ) this.parent.getNetworkInfoMap()
					.infoIntMap.get( messageBuffer.readInt32() );
		
		this.getClientKind().readStream( messageBuffer );
		Enum oldState = this.getState().getState();
		this.getState().readStream( messageBuffer );
		this.setClientType( messageBuffer.readByte() );
		this.tag = messageBuffer.readTagList();
		this.clientName = messageBuffer.readString();
		this.clientRating = messageBuffer.readInt32();
		this.clientChatPort = messageBuffer.readInt32();
		this.setChanged();
		
		Enum newState = getState().getState();
		if ( ( oldState != newState ) 
			&& ( oldState == EnumState.CONNECTED_DOWNLOADING 
			|| newState == EnumState.CONNECTED_DOWNLOADING ) ) {
				this.notifyObservers();
		} 
		else {
			this.notifyObservers( this );
		}
	}
	
	/**
	 * Updates the state of this object
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		
		Enum oldState = getState().getState();
		this.getState().update( messageBuffer );
		Enum newState = getState().getState();
				
		this.setChanged();
		
		if ( ( oldState != newState ) 
			&& ( oldState == EnumState.CONNECTED_DOWNLOADING 
				|| newState == EnumState.CONNECTED_DOWNLOADING ) ) {
				this.notifyObservers();
		}
		else {
			this.notifyObservers( this );
		}
	}
	
	/**
	 * Adds the availability of a file into this list of availability
	 * @param fileId The fileId
	 * @param avail The availability of this file
	 */
	public void putAvail( int fileId, String avail ) {
		( ( TIntObjectHashMap ) this.avail ).put( fileId, avail );
		this.setChanged();
		this.notifyObservers( this );
	}

	public static void addFriend( CoreCommunication core, int id ) {
		Message addFriend =
			new EncodeMessage( Message.S_ADD_CLIENT_FRIEND, new Integer( id ) );
		addFriend.sendMessage( core.getConnection() );
		addFriend = null;
	}

	public static void removeFriend( CoreCommunication core, int id ) {
		Message removeFriend =
			new EncodeMessage( Message.S_REMOVE_FRIEND, new Integer( id ) );
		removeFriend.sendMessage( core.getConnection() );
		removeFriend = null;
	}

	public static void removeAllFriends( CoreCommunication core ) {
		Message removeAllFriends =
			new EncodeMessage( Message.S_REMOVE_ALL_FRIENDS );
		removeAllFriends.sendMessage( core.getConnection() );
		removeAllFriends = null;
	}
}

/*
$Log: ClientInfo.java,v $
Revision 1.25  2003/09/14 03:37:24  zet
changedProperties in model

Revision 1.24  2003/09/13 22:22:59  zet
weak sets

Revision 1.23  2003/09/02 09:24:36  lemmster
checkstyle

Revision 1.22  2003/08/24 02:22:12  zet
*** empty log message ***

Revision 1.21  2003/08/23 15:21:37  zet
remove @author

Revision 1.20  2003/08/23 10:02:02  lemmster
use supertype where possible

Revision 1.19  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.18  2003/08/22 21:03:15  lemmster
replace $user$ with $Author: zet $

Revision 1.17  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.16  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging

Revision 1.15  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.14  2003/08/04 19:21:52  zet
trial tabletreeviewer

Revision 1.13  2003/08/03 17:34:45  zet
notify observers

Revision 1.12  2003/07/15 18:17:27  dek
checkstyle & javadoc

Revision 1.11  2003/07/12 14:11:51  dek
made the ClientInfo-availability easier

Revision 1.10  2003/07/12 13:59:39  dek
availability is strange: it doesn't seem to make it's way to clients...

Revision 1.9  2003/07/12 13:53:21  dek
some new methods, to be able to show the information

Revision 1.8  2003/07/06 12:47:22  lemmstercvs01
bugfix for fileUpdateAvailability

Revision 1.7  2003/07/06 09:33:50  lemmstercvs01
int networkid -> networkinfo networkid

Revision 1.6  2003/06/30 07:20:09  lemmstercvs01
changed to readTagList()

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