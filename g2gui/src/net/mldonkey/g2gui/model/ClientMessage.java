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
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.comm.Message;

/**
 *
 * @version $Id: ClientMessage.java,v 1.4 2003/08/23 15:21:37 zet Exp $	
 */
public class ClientMessage extends Parent {

	private int clientId;
	private String messageText;

	/**
	 * @param core
	 */
	public ClientMessage( CoreCommunication core ) {
		super( core );
	}
	/**
	 * @return
	 */
	public int getId() {
		return clientId;
	}
	/**
	 * @return
	 */
	public String getText() {
		return messageText;
	}
	
	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.model.SimpleInformation#readStream(net.mldonkey.g2gui.helper.MessageBuffer)
	 */
	public void readStream(MessageBuffer messageBuffer) {
		this.clientId = messageBuffer.readInt32();
		this.messageText = messageBuffer.readString();
	}

	/**
	 * @param core
	 * @param clientId
	 * @param messageText
	 */
	public static void sendMessage(CoreCommunication core, int clientId, String messageText) {
			Object[] obj = new Object[ 2 ];
			obj[ 0 ] = new Integer( clientId );
			obj[ 1 ] = messageText;
					
			Message messageToClient =
				new EncodeMessage( Message.S_MESSAGE_TO_CLIENT, obj );
			messageToClient.sendMessage( core.getConnection() );
			obj = null; 
			messageToClient = null;	
	}

}


/*
$Log: ClientMessage.java,v $
Revision 1.4  2003/08/23 15:21:37  zet
remove @author

Revision 1.3  2003/08/23 10:02:02  lemmster
use supertype where possible

Revision 1.2  2003/08/22 21:03:15  lemmster
replace $user$ with $Author: zet $

Revision 1.1  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging
*/
