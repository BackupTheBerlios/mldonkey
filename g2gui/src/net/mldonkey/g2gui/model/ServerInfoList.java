/*
 * Created on 14.06.2003
 */
package net.mldonkey.g2gui.model;

import net.mldonkey.g2gui.helper.MessageBuffer;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;

/**
 * @author markus
 */
public class ServerInfoList implements InfoList {
	/**
	 * 
	 */
	private TIntObjectHashMap serverInfoList;
	
	/**
	 * Generates a empty ServerInfoList object
	 */
	public ServerInfoList() {
		this.serverInfoList = new TIntObjectHashMap();
	}
	
	/**
	 * Store a key/value pair in this object
	 * @param key The Key
	 * @param value The FileInfo object
	 */
	public void put( int key, ServerInfo value ) {
		this.serverInfoList.put( key, value );
	}

	/**
	 * Reads a List of ServerInfo objects from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		ServerInfo serverInfo = new ServerInfo();
		serverInfo.readStream( messageBuffer );
		this.put( serverInfo.getServerId(), serverInfo );
	}
	
	/**
	 * Update a specific element in the List
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		//this.get( messageBuffer.readInt32() ).update( messageBuffer );
	}
	
	/**
	 * Get a ServerInfo object from this object by there id
	 * @param id The FileInfo id
	 * @return The FileInfo object
	 */
	public ServerInfo get( int id ) {
		return ( ServerInfo ) this.serverInfoList.get( id );
	}
	
	/**
	 * Get an Iterator
	 * @return an Iterator
	 */
	public TIntObjectIterator iterator() {
		return this.serverInfoList.iterator();
	}

}
