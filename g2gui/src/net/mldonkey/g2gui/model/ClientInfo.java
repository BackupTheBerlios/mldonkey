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
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.model.enum.*;

/**
 * ClientInfo
 *
 * @author markus
 * @version $Id: ClientInfo.java,v 1.14 2003/08/04 19:21:52 zet Exp $ 
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
	 * Availability of a file
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
		String availability =
		  ( String ) ( ( TIntObjectHashMap ) this.avail ).get( fileInfo.getId() );
		  
		return availability;
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
		this.clientid = messageBuffer.readInt32();
		
		this.clientnetworkid =
			( NetworkInfo ) this.parent.getNetworkInfoMap()
					.infoIntMap.get( messageBuffer.readInt32() );
		
		this.getClientKind().readStream( messageBuffer );
		this.getState().readStream( messageBuffer );
		this.setClientType( messageBuffer.readByte() );
		this.tag = messageBuffer.readTagList();
		this.clientName = messageBuffer.readString();
		this.clientRating = messageBuffer.readInt32();
		this.clientChatPort = messageBuffer.readInt32();
	}
	
	/**
	 * Updates the state of this object
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		this.getState().update( messageBuffer );
		this.setChanged();
		this.notifyObservers( this );
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
}

/*
$Log: ClientInfo.java,v $
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