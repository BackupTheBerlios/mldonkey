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
 * ClientInfo
 *
 * @author markus
 * @version $Id: ClientInfo.java,v 1.1 2003/06/14 17:40:40 lemmstercvs01 Exp $ 
 *
 */
public class ClientInfo implements Information {
	
	/**
	 * Client Id
	 */
	private int clientid;
	/**
	 * Client Network Id
	 */
	private int clientnetworkid;
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
	private byte clientType;
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
	private int clientRate;
	/**
	 * Client Chat Port (mlchat)
	 */
	private int clientChatPort;

	/**
	 * @return an int
	 */
	public int getClientChatPort() {
		return clientChatPort;
	}

	/**
	 * @return an int
	 */
	public int getClientid() {
		return clientid;
	}

	/**
	 * @return a ClientKind object
	 */
	public ClientKind getClientKind() {
		return clientKind;
	}

	/**
	 * @return a string
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * @return an int
	 */
	public int getClientnetworkid() {
		return clientnetworkid;
	}

	/**
	 * @return an int
	 */
	public int getClientRate() {
		return clientRate;
	}

	/**
	 * @return a byte
	 */
	public byte getClientType() {
		return clientType;
	}

	/**
	 * @return a state object
	 */
	public State getState() {
		return state;
	}

	/**
	 * @return an array of tags
	 */
	public Tag[] getTag() {
		return tag;
	}

	/**
	 * @param i an int
	 */
	public void setClientChatPort( int i ) {
		clientChatPort = i;
	}

	/**
	 * @param i an int
	 */
	public void setClientid( int i ) {
		clientid = i;
	}

	/**
	 * @param kind a ClientKind object
	 */
	public void setClientKind( ClientKind kind ) {
		clientKind = kind;
	}

	/**
	 * @param string a string
	 */
	public void setClientName( String string ) {
		clientName = string;
	}

	/**
	 * @param i an int
	 */
	public void setClientnetworkid( int i ) {
		clientnetworkid = i;
	}

	/**
	 * @param i an int
	 */
	public void setClientRate( int i ) {
		clientRate = i;
	}

	/**
	 * @param b a byte
	 */
	public void setClientType( byte b ) {
		clientType = b;
	}

	/**
	 * @param state a state object
	 */
	public void setState( State state ) {
		this.state = state;
	}

	/**
	 * @param tags an array of tags
	 */
	public void setTag( Tag[] tags ) {
		tag = tags;
	}
	
	/**
	 * Reads a ClientInfo object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		this.setClientid( messageBuffer.readInt32() );
		this.setClientnetworkid( messageBuffer.readInt32() );
		this.getClientKind().readStream( messageBuffer );
		this.getState().readStream( messageBuffer );
		this.setClientType( messageBuffer.readByte() );

		short listElem = messageBuffer.readInt16();
		tag = new Tag[ listElem ];
		for ( int i = 0; i < listElem; i++ ) {
			Tag aTag = new Tag();
			aTag.readStream( messageBuffer );
			this.tag[ i ] = aTag;
		}

		this.setClientName( messageBuffer.readString() );
		this.setClientRate( messageBuffer.readInt32() );
		this.setClientChatPort( messageBuffer.readInt32() );
	}
	
	/**
	 * Updates the state of this object
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		this.getState().update( messageBuffer );
	}

}

/*
$Log: ClientInfo.java,v $
Revision 1.1  2003/06/14 17:40:40  lemmstercvs01
initial commit

*/