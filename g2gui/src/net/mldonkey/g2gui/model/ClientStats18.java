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
 * ClientStats18
 *
 * @version $Id: ClientStats18.java,v 1.2 2003/12/04 08:47:25 lemmy Exp $ 
 *
 */
public class ClientStats18 extends ClientStats {
	/**
	 * @param core
	 */
	ClientStats18(CoreCommunication core) {
		super(core);
	}

	/**
	 * Reads a ClientState object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {		
		this.totalUp = messageBuffer.readInt64();
		this.totalDown = messageBuffer.readInt64();
		this.totalShared = messageBuffer.readInt64();
		this.numOfShare = messageBuffer.readInt32();
		this.tcpUpRate = round( messageBuffer.readInt32() / ( float )1024 );		
		this.tcpDownRate = round( messageBuffer.readInt32() / ( float )1024 );
		this.udpUpRate = round( messageBuffer.readInt32() / ( float )1024 );
		this.udpDownRate = round( messageBuffer.readInt32() / ( float )1024 );
		this.numCurrDownload = messageBuffer.readInt32();
		this.numDownloadFinished = messageBuffer.readInt32();

		/* create the ConnectedNetworks[] */
		int listElem = messageBuffer.readInt16();

		if ( listElem < 0 ) //check, if this is a big unsigned int, and fix it:
			listElem = Short.MAX_VALUE * 2 + listElem + 2;

		NetworkInfo[] temp = new NetworkInfo[ listElem ];
		for ( int i = 0; i < listElem; i++ ) {
			int key = messageBuffer.readInt32();
			NetworkInfo network = this.parent.getNetworkInfoMap().get( key );
			this.parent.getNetworkInfoMap().setConnectedServers( 
					messageBuffer.readInt32(), network );
			temp[ i ] = network;
		}
		this.connectedNetworks = temp;
		this.setChanged();
		this.notifyObservers( this );
	}
}

/*
$Log: ClientStats18.java,v $
Revision 1.2  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.1  2003/12/01 14:22:17  lemmy
ProtocolVersion handling completely rewritten

*/