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

import java.util.Date;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.helper.RegExp;
import net.mldonkey.g2gui.model.enum.Enum;

/**
 * ClientInfo20
 *
 * @version $Id: ClientInfo20.java,v 1.6 2004/03/22 18:47:39 dek Exp $ 
 *
 */
public class ClientInfo20 extends ClientInfo19 {
	/**
	 * clientConnectTime
	 */
	private int clientConnectTime;
	private String clientConnectTimeString = "";
	private String clientConnectTimePassedString = "";
	/**
	 * @param core
	 */
	public ClientInfo20(CoreCommunication core) {
		super(core);
	}
	/**
	 *
	 * @param clientID
	 * @param messageBuffer
	 */
	public void readStream(int clientID, MessageBuffer messageBuffer) {
		this.clientid = clientID;		
		this.clientNetwork = (NetworkInfo) this.parent.getNetworkInfoMap().infoIntMap.get(messageBuffer.readInt32());
		this.getClientKind().readStream(messageBuffer);

		Enum oldState = this.getState().getState();
		readState(messageBuffer);
		this.setClientType(messageBuffer.readByte());
		this.tag = messageBuffer.readTagList();
		this.clientName = messageBuffer.readString();		
		this.clientRating = messageBuffer.readInt32();
		this.clientSoftware = messageBuffer.readString();		
		this.clientDownloaded = messageBuffer.readInt64();
		this.clientUploaded = messageBuffer.readInt64();
		this.clientUploadFilename = messageBuffer.readString();

		// this needs to be fixed in the core soon since core/gui clocks can be out of sync rendering this useless
		// I've sent mail... 
		this.clientConnectTime = messageBuffer.readInt32();		
		
		this.clientConnectTimeString = "" +
		new Date(((long) clientConnectTime + 1000000000L) * 1000L);

		// human-readable time-passed-since string
		long connectseconds = (System.currentTimeMillis() - 
				((long) clientConnectTime + 1000000000L) * 1000L) / 1000;
		int hours = (int) connectseconds / 3600;
		int minutes = (int) (connectseconds - (3600*hours)) / 60;
		int seconds = (int) (connectseconds - (3600*hours) - (minutes*60));
		this.clientConnectTimePassedString = "";

		if (hours > 0) { 
			this.clientConnectTimePassedString += hours + "h "; 
		}
		if ((minutes > 0) || (hours > 0)) { 
			this.clientConnectTimePassedString += minutes + "m "; 
		}
		if ((seconds > 0) || (minutes > 0) || (hours > 0)) { 
			this.clientConnectTimePassedString += seconds + "s"; 
		} else {
			this.clientConnectTimePassedString = "-";
		}
		
		// update client upload and download information
		this.clientUploadedString = RegExp.calcStringSize(clientUploaded);
		this.clientDownloadedString = RegExp.calcStringSize(clientDownloaded);

		// Occasionally it seems the filename isn't reset to null when not uploading anymore
		this.isUploader = !clientUploadFilename.equals("");
		onChangedState(oldState);
	}

	/**
	 * @param messageBuffer
	 */
	protected void readState(MessageBuffer messageBuffer) {
		this.getState().readStream(messageBuffer);	
	}

	
	public int getClientConnectTime() {
		return clientConnectTime;
	}

	/**
	 * Delivers a full-blown string like "<strong>Fri Dec 19 22:11:06 CET 2003</strong>"
	 * 
	 * @return A full blown date-string
	 */
	public String getClientConnectTimeString() {
		return clientConnectTimeString;
	}

	/**
	 * Delivers a smaller string which shows the time passed since the last connection-initiation
	 * in a human-readable form like "<strong>5h 23m 42s<strong>"
	 * 
	 * @return A human-readable string of time since last connection-initiation
	 */
	public String getClientConnectTimePassedString() {
		return clientConnectTimePassedString;
	}
	
}

/*
$Log: ClientInfo20.java,v $
Revision 1.6  2004/03/22 18:47:39  dek
Still some Gui-Protocoll enhancements

Revision 1.5  2004/03/21 21:00:50  dek
implemented gui-Proto 21-25 !!!!!

Revision 1.4  2004/03/20 01:34:02  dek
implemented gui-Proto 25 !!!!!

Revision 1.3  2003/12/19 22:15:27  psy
added human readable value for connectiontime passed since connection initiation

Revision 1.2  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.1  2003/12/01 14:42:57  lemmy
ProtocolVersion handling completely rewritten

*/