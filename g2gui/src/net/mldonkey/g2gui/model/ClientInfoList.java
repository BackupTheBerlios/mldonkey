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
public class ClientInfoList implements Information, InfoList {
	/**
	 * The intern representation of ClientInfoList
	 */
	private TIntObjectHashMap clientInfoList;
	
	/**
	 * Creates a new ClientInfoList
	 */
	public ClientInfoList() {
		this.clientInfoList = new TIntObjectHashMap();
	}
	
	/**
	 * Reads a networkInfo object from the stream
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		ClientInfo clientInfo = new ClientInfo();
		clientInfo.readStream( messageBuffer );
		this.put( clientInfo.getClientid(), clientInfo );
	}
	
	/**
	 * Store a key/value pair in this object
	 * @param key The Key
	 * @param value The ClientInfo object
	 */
	public void put( int key, ClientInfo value ) {
		this.put( key, value );
	}
	
	/**
	 * Get the ClientInfo object value to the given key
	 * @param key The Key
	 * @return a ClientInfo object
	 */
	public ClientInfo get( int key ) {
		return this.get( key );
	}
	
	/**
	 * Get a ClientInfoList iterator
	 * @return an iterator
	 */
	public TIntObjectIterator iterator() {
		return this.clientInfoList.iterator();
	}
	
	/**
	 * Update a specific element in the List
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		int key = messageBuffer.readInt32();
		( ( ClientInfo ) this.get( key ) ).update( messageBuffer );
	}
}
