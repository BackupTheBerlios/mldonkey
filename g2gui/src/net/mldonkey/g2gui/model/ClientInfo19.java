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
import net.mldonkey.g2gui.helper.RegExp;
import net.mldonkey.g2gui.model.enum.Enum;

/**
 * ClientInfo19
 *
 * @version $Id: ClientInfo19.java,v 1.1 2003/12/01 14:42:57 lemmster Exp $ 
 *
 */
public class ClientInfo19 extends ClientInfo {
	/**
	 * Client Software
	 */
	protected String clientSoftware = "";
	/**
	 * Client downloaded bytes
	 */
	protected long clientDownloaded = 0;
	protected String clientDownloadedString = "";
	/**
	 * Client uploaded bytes
	 */
	protected long clientUploaded = 0;
	protected String clientUploadedString = "";
	/**
	 * Filename being uploaded to client
	 */
	protected String clientUploadFilename = "";
	/**
	 * true if clientUploadFileName != ""
	 */
	protected boolean isUploader = false;

	
	/**
	 * @param core
	 */
	public ClientInfo19(CoreCommunication core) {
		super(core);
	}

	/**
	 *
	 * @param clientID
	 * @param messageBuffer
	 */
	public void readStream(int clientID, MessageBuffer messageBuffer) {
		this.clientid = clientID;

		this.clientnetworkid = (NetworkInfo) this.parent.getNetworkInfoMap().infoIntMap.get(messageBuffer.readInt32());

		this.getClientKind().readStream(messageBuffer);

		Enum oldState = this.getState().getState();
		this.getState().readStream(messageBuffer);
		this.setClientType(messageBuffer.readByte());
		this.tag = messageBuffer.readTagList();
		this.clientName = messageBuffer.readString();
		this.clientRating = messageBuffer.readInt32();
		this.clientSoftware = messageBuffer.readString();
		this.clientDownloaded = messageBuffer.readInt64();
		this.clientUploaded = messageBuffer.readInt64();
		this.clientUploadFilename = messageBuffer.readString();
		this.clientUploadedString = RegExp.calcStringSize(clientUploaded);
		this.clientDownloadedString = RegExp.calcStringSize(clientDownloaded);

		// Occasionally it seems the filename isn't reset to null when not uploading anymore
		this.isUploader = !clientUploadFilename.equals("");
		onChangedState(oldState);
	}
	/**
	 * @return clientSoftware
	 */
	public String getClientSoftware() {
		return clientSoftware;
	}

	/**
	 * @return clientUploaded Bytes
	 */
	public long getUploaded() {
		return clientUploaded;
	}

	/**
	 * @return clientDownloaded Bytes
	 */
	public long getDownloaded() {
		return clientDownloaded;
	}

	/**
	 * @return clientUploaded String
	 */
	public String getUploadedString() {
		return clientUploadedString;
	}

	/**
	 * @return clientDownloaded String
	 */
	public String getDownloadedString() {
		return clientDownloadedString;
	}

	/**
	 * @return clientUploadFilename (Filename currently being uploaded to client)
	 */
	public String getUploadFilename() {
		return clientUploadFilename;
	}
	public boolean isUploader() {
		return isUploader;
	}
}

/*
$Log: ClientInfo19.java,v $
Revision 1.1  2003/12/01 14:42:57  lemmster
ProtocolVersion handling completely rewritten

*/