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

import gnu.trove.TIntObjectHashMap;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.model.enum.EnumState;

/**
 * ServerInfoList
 *
 * @author $user$
 * @version $Id: ServerInfoIntMap.java,v 1.11 2003/08/06 17:38:38 lemmstercvs01 Exp $ 
 *
 */
public class ServerInfoIntMap extends InfoIntMap {
	/**
	 * last serverinfos changed/added/removed
	 */
	private List changed = new ArrayList();
	private List added = new ArrayList();
	private List removed = new ArrayList();
	
	/**
	 * @param communication my parent
	 */
	public ServerInfoIntMap( CoreCommunication communication ) {		
		super( communication );
	}

	/**
	 * Store a key/value pair in this object
	 * @param key The Key
	 * @param value The FileInfo object
	 */
	private void put( int key, ServerInfo value ) {
		synchronized ( this ) {
			this.infoIntMap.put( key, value );
		}
		synchronized ( this.added ) {
			this.added.add( value );
		}
	}

	/**
	 * Reads a List of ServerInfo objects from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		int id = messageBuffer.readInt32();
		messageBuffer.setIterator( messageBuffer.getIterator() - 4 );
		if ( this.containsKey( id ) ) {
			this.get( id ).readStream( messageBuffer );
			synchronized ( this.changed ) {
				this.changed.add( this.get( id ) );
			}
		}
		else {
			ServerInfo serverInfo = new ServerInfo( this.parent );
			serverInfo.readStream( messageBuffer );
			if ( serverInfo.getConnectionState().getState() != EnumState.REMOVE_HOST ) {
				this.put( serverInfo.getServerId(), serverInfo );
			}
		}
		this.setChanged();
		this.notifyObservers( this );
	}
	
	/**
	 * Removes all entries in changed
	 */
	public void clearChanged() {
		synchronized ( this.changed ) {
			this.changed.clear();
		}
	}
	/**
	 * Removes all entries in added
	 */
	public void clearAdded() {
		synchronized ( this.added ) {
			this.added.clear();
		}
	}
	/**
	 * Removes all entries in removed
	 */
	public void clearRemoved() {
		synchronized ( this.removed ) {
			this.removed.clear();
		}
	}
	
	/**
	 * Get a ServerInfo object from this object by there id
	 * @param id The FileInfo id
	 * @return The FileInfo object
	 */
	private ServerInfo get( int id ) {
		return ( ServerInfo ) this.infoIntMap.get( id );
	}

	/**
	 * Does nothing!
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		int id = messageBuffer.readInt32();
		if ( this.infoIntMap.contains( id ) ) {
			ServerInfo server = ( ServerInfo ) this.get( id );
			server.update( messageBuffer );
			synchronized ( this.changed ) {
				this.changed.add( server );
			}
			this.setChanged();
			this.notifyObservers( this );
		}
	}

	/**
	 * @param messageBuffer
	 * cleans up the used Trove-collections. Fills 'em with data the core (mldonkey) finds useful
	 */
	public void clean( MessageBuffer messageBuffer ) {
		TIntObjectHashMap tempServerInfoList = new TIntObjectHashMap();			
		int[] usefulServers = messageBuffer.readInt32List();		
		for ( int i = 0; i < usefulServers.length; i++ ) {
			int clientID = usefulServers[ i ];
			tempServerInfoList.put( clientID, this.get( clientID ) );			
		}
		synchronized ( this ) {
			this.infoIntMap = tempServerInfoList;		
		}
		this.setChanged();
		this.notifyObservers( this );
	}
	
	/**
	 * returns an array with all networks known to this networkMap
	 * @return all known networks
	 */
	public Object[] getServers() {
		synchronized ( this ) {
			return this.infoIntMap.getValues();
		}
	}
	
	/**
	 * @return The serverinfos who have been changed
	 * clear this list, after the update in the view
	 */
	public List getChanged() {
		return this.changed;
	}
	
	/**
	 * @return The serverinfos who have been added
	 * clear this list, after the update in the view
	 */
	public List getAdded() {
		return this.added;
	}
	
	/**
	 * @return The serverinfos who have been removed
	 * clear this list, after the update in the view
	 */
	public List getRemoved() {
		return this.removed;
	}
	
	/**
	 * Sends Message.S_CONNECT_MORE to the core.
	 */
	public void connectMore() {
		Message message = new EncodeMessage( Message.S_CONNECT_MORE );
		message.sendMessage( this.parent.getConnection() );
		message = null;
	}

	/**
	 * Sends Message.S_CLEAN_OLD to the core
	 */	
	public void cleanOld() {
		Message message = new EncodeMessage( Message.S_CLEAN_OLD );
		message.sendMessage( this.parent.getConnection() );
		message = null;
	}

	/**
	 * Removes a old ServerInfo from this obj
	 * @param key The ID of the ServerInfo to remove
	 * @return true on success, false if this obj does not contain such an ID
	 */	
	public boolean remove( int key ) {
		if ( !this.infoIntMap.containsKey( key ) ) return false;

		( ( ServerInfo ) this.infoIntMap.get( key ) ).setState( EnumState.REMOVE_HOST );
		return true;
	}
	
	/**
	 * removes a serverinfo from this obj.
	 * (without sending a message to the core)
	 * @param server The serverInfo to remove
	 */
	protected void remove( ServerInfo server ) {
		synchronized( this ) {
			this.infoIntMap.remove( server.getServerId() );
			this.removed.add( server );
		}
		this.setChanged();
		this.notifyObservers( this );
	}
	
	/**
	 * removes all server of the given networkinfo.enum
	 * (without sending a message to the core)
	 * use it to clean the serverlist if a network gets disabled
	 * @param enum The networkinfo.enum
	 */
	public void remove( NetworkInfo.Enum enum ) {
		Object[] servers = this.infoIntMap.getValues();
		for ( int i = 0; i < servers.length; i++ ) {
			ServerInfo server = ( ServerInfo ) servers[ i ];
			if ( server.getNetwork().getNetworkType() == enum ) {
				synchronized ( this ) {
					this.infoIntMap.remove( server.getServerId() );
				}
				synchronized ( this.removed ) {
					this.removed.add( server );
				}
			}
		}
		this.setChanged();
		this.notifyObservers( this );
	}
	
	/**
	 * Connects the ServerInfo
	 * @param key The ID of the ServerInfo to connect
	 * @return true on success, false if this obj does not contain such an ID
	 */
	public boolean connect( int key ) {
		if ( !this.infoIntMap.containsKey( key ) ) return false;
		
		( ( ServerInfo ) this.infoIntMap.get( key ) ).setState( EnumState.CONNECTING );
		return true;
	}

	/**
	 * Disconnects the ServerInfo
	 * @param key The ID of the ServerInfo to disconnect
	 * @return true on success, false if this obj does not contain such an ID
	 */
	public boolean disconnect( int key ) {
		if ( !this.infoIntMap.containsKey( key ) ) return false;
		
		( ( ServerInfo ) this.infoIntMap.get( key ) ).setState( EnumState.NOT_CONNECTED );
		return true;
	}
	
	/**
	 * Adds a ServerInfo to this obj
	 * @param network The NetworkInfo the ServerInfo belongs to
	 * @param address The IP Address of the ServerInfo
	 * @param port The Port of the ServerInfo
	 */
	public void add( NetworkInfo network, InetAddress address, short port ) {
		Object[] obj = new Object[ 3 ];
		obj[ 0 ] = new Integer( network.getNetwork() );
		obj[ 1 ] = address.getAddress();
		obj[ 2 ] = new Short( port );
		
		Message message = new EncodeMessage( Message.S_ADD_SERVER, obj );
		message.sendMessage( this.parent.getConnection() );
		message = null;
	}
	
	/**
	 * Adds the server to the black list
	 * @param key The serverid
	 */
	public void addToBlackList( int key ) {
		ServerInfo server = ( ServerInfo ) this.infoIntMap.get( key );
		String ip = server.getServerAddress().getAddress().toString();
		Message message = new EncodeMessage( Message.S_CONSOLEMSG, ip );
		message.sendMessage( this.parent.getConnection() );
		ip = null;
		message = null;
	}
	
	/**
	 * Add servers from a weblist to this obj
	 * @param url The url to the server list
	 */
	public void addServerList( String url ) {
		int index = url.lastIndexOf( "." );
		String suffix = url.substring( index );
		if ( suffix.equalsIgnoreCase( ".met" ) )
			suffix = "server.met";
		else
			suffix = "ocl";	

		String aString = "add_url ";
		aString += suffix + " ";
		aString += url;
		Message message = new EncodeMessage( Message.S_CONSOLEMSG, aString );
		message.sendMessage( this.parent.getConnection() );
		message = null;
		aString = null;
	}
	
	/**
	 * Returns a string representation of this obj
	 * @return String The string
	 */
	public String toString() {
		StringBuffer result = new StringBuffer();
		Object[] obj = this.getServers();
		for ( int i = 0; i < obj.length; i++ ) {
			ServerInfo server = ( ServerInfo ) obj[ i ];
			result.append(  server.toString() + "\n" );
		}
		return result.toString();
	}
}

/*
$Log: ServerInfoIntMap.java,v $
Revision 1.11  2003/08/06 17:38:38  lemmstercvs01
some actions still missing. but it should work for the moment

Revision 1.10  2003/08/06 09:47:29  lemmstercvs01
toString() added, some bugfixes

Revision 1.9  2003/08/05 13:43:18  lemmstercvs01
added some messages

Revision 1.8  2003/08/01 17:21:19  lemmstercvs01
reworked observer/observable design, added multiversion support

Revision 1.7  2003/07/30 19:28:42  lemmstercvs01
several changes

Revision 1.6  2003/07/06 20:09:21  dek
NPE fixed

Revision 1.5  2003/07/04 18:35:02  lemmstercvs01
foobar

Revision 1.4  2003/06/27 10:35:53  lemmstercvs01
removed unneeded calls

Revision 1.3  2003/06/26 17:57:46  lemmstercvs01
added workaround for bug in core proto

Revision 1.2  2003/06/20 15:15:22  dek
humm, some interface-changes, hope, it didn't break anything ;-)

Revision 1.1  2003/06/16 21:47:19  lemmstercvs01
just refactored (name changed)

Revision 1.8  2003/06/16 20:12:47  dek
debugging code removed

Revision 1.7  2003/06/16 20:08:38  lemmstercvs01
opcode 13 added

Revision 1.6  2003/06/16 18:05:26  dek
refactored cleanTable

Revision 1.5  2003/06/16 17:42:59  lemmstercvs01
minor changes in readStream()

Revision 1.4  2003/06/16 13:18:59  lemmstercvs01
checkstyle applied

Revision 1.3  2003/06/15 16:18:41  lemmstercvs01
new interface introduced

Revision 1.2  2003/06/14 23:04:08  lemmstercvs01
change from interface to abstract superclass

*/