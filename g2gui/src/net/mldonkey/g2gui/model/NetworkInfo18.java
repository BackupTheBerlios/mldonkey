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
import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * NetworkInfo18
 *
 * @version $Id: NetworkInfo18.java,v 1.2 2003/12/04 08:47:25 lemmy Exp $ 
 *
 */
public class NetworkInfo18 extends NetworkInfo {
	/**
	 * The number of servers we are currently
	 * connected to and the prev value
	 */
	private int connectedServers;
	/**
	 * NetworkHasServers (well known servers) 
	 */	
	private boolean networkHasServers;
	/**
	 * NetworkHasRooms (rooms to chat with other users)
	 */
	private boolean networkHasRooms;
	/**
	 * NetworkHasMultinet (files can be downloaded from several networks)
	 */
	private boolean networkHasMultinet;
	/**
	 * VirtualNetwork (not a real network)
	 */
	private boolean virtualNetwork;
	/**
	 * NetworkHasSearch (searches can be issued)
	 */
	private boolean networkHasSearch;
	/**
	 * NetworkHasChat (chat between two users) 
	 */
	private boolean networkHasChat;
	/**
	 * NetworkHasSupernodes (peers can become servers) 
	 */
	private boolean networkHasSupernodes;
	/**
	 * NetworkHasUpload (upload is implemented)
	 */
	private boolean networkHasUpload;
	
	/**
	 * @param core
	 */
	NetworkInfo18(CoreCommunication core) {
		super(core);
	}

	/**
	 * Reads a networkInfo object from the stream
	 * @param messageBuffer The buffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		/*
		 *	PayLoad:
		 *	int32	Network identifier (used in other messages for this network) 
		 *	String	Network name 
		 *	int8	Enabled(1) or Disabled(0) 
		 *	String	Name of network config file 
		 *	int64	Number of bytes uploaded on network 
		 *	int64	Number of bytes downloaded on network
		 *  
		 *  proto >= 18
		 *  int32   Number of connected servers
		 *  List of int16  Network flags
		 * 		0 : NetworkHasServers (well known servers) 
		 *		1 : NetworkHasRooms (rooms to chat with other users) 
		 *		2 : NetworkHasMultinet (files can be downloaded from several networks) 
		 *	 	3 : VirtualNetwork (not a real network) 
		 *	 	4 : NetworkHasSearch (searches can be issued) 
		 *		5 : NetworkHasChat (chat between two users) 
		 *		6 : NetworkHasSupernodes (peers can become servers) 
		 *	 	7 : NetworkHasUpload (upload is implemented) 
		 */
		this.network = messageBuffer.readInt32();
		this.networkName = messageBuffer.readString();
		this.setEnabled( messageBuffer.readInt8() );
		this.configFile = messageBuffer.readString();
		this.uploaded = messageBuffer.readInt64();
		this.downloaded = messageBuffer.readInt64();
		
		if ( parent.getProtoToUse() >= 18 ) {
			this.connectedServers = messageBuffer.readInt32();
			
			/* read the int16 list */
			short listElems = messageBuffer.readInt16();
			for ( int i = 0; i < listElems; i++ ) {
				short listElem = messageBuffer.readInt16();
				switch ( listElem ) {
					case 0 :
						this.networkHasServers = true;
						break;
					case 1 :
						this.networkHasRooms = true;
						break;
					case 2 :
						this.networkHasMultinet = true;
						break;
					case 3 :
						this.virtualNetwork = true;
						break;
					case 4 :
						this.networkHasSearch = true;
						break;
					case 5 :
						this.networkHasChat = true;
						break;
					case 6 :
						this.networkHasSupernodes = true;
						break;
					case 7 :
						this.networkHasUpload = true;
						break;
				}
				
			}
		}
		
		/* set the networktype by networkname */
		this.setNetworkType( this.networkName );

		/* remove the servers from the serverlist if the network is disabled */
		if ( !this.isEnabled() )
			this.parent.getServerInfoIntMap().remove( this.getNetworkType() );
	}
	
	/**
	 * @return VirtualNetwork (not a real network)
	 */
	public boolean isVirtual() {
		return this.virtualNetwork;
	}
	/**
	 * @return NetworkHasRooms (rooms to chat with other users)
	 */	
	public boolean hasRooms() {
		return this.hasRooms();
	}
	/**
	 * @return NetworkHasMultinet (files can be downloaded from several networks)
	 */
	public boolean isMultinet() {
		return this.networkHasMultinet;
	}
	/**
	 * @return NetworkHasChat (chat between two users)
	 */
	public boolean hasChat() {
		return this.networkHasChat;
	}
	/**
	 * @return NetworkHasSupernodes (peers can become servers)
	 */
	public boolean hasSupernodes() {
		return this.networkHasSupernodes;
	}
	/**
	 * @return NetworkHasUpload (upload is implemented)
	 */
	public boolean hasUpload() {
		return this.networkHasUpload;
	}
	/**
	 * @return Does this network use servers
	 */
	public boolean hasServers() {
		return this.networkHasServers;
	}
	/**
	 * Is this network searchable
	 * @return true on searchable
	 */
	public boolean isSearchable() {
		return this.networkHasSearch;
	}
	/**
	 * @return The number of connected Servers
	 */
	public int getConnectedServers() {
		return connectedServers;
	}
	/**
	 * @param i The new amount of connected servers for this network
	 */
	protected void setConnectedServers( int i ) {
		this.connectedServers = i;
	}
}

/*
$Log: NetworkInfo18.java,v $
Revision 1.2  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.1  2003/12/01 14:22:17  lemmy
ProtocolVersion handling completely rewritten

*/