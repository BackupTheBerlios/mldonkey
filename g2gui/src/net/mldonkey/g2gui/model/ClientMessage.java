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
 * @version $Id: ClientMessage.java,v 1.7 2003/12/01 14:22:17 lemmster Exp $	
 */
public class ClientMessage extends Parent {

	private int clientId;
	private String messageText;

	/**
	 * @param core The parent <code>Core</code>
	 */
	ClientMessage( CoreCommunication core ) {
		super( core );
	}
	/**
	 * @return The client identifier
	 */
	public int getId() {
		return clientId;
	}
	/**
	 * @return The client message
	 */
	public String getText() {
		return messageText;
	}
	
	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.model.SimpleInformation#readStream(net.mldonkey.g2gui.helper.MessageBuffer)
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		this.clientId = messageBuffer.readInt32();
		this.messageText = messageBuffer.readString();
	}

	/**
	 * @param core The parent core obj
	 * @param clientId The client identifier
	 * @param messageText The message to the client identifier
	 */
	public static void sendMessage( CoreCommunication core, int clientId, String messageText ) {
			Object[] obj = new Object[ 2 ];
			obj[ 0 ] = new Integer( clientId );
			obj[ 1 ] = messageText;
					
			Message messageToClient =
				new EncodeMessage( Message.S_MESSAGE_TO_CLIENT, obj );
			messageToClient.sendMessage( core );
			obj = null; 
			messageToClient = null;	
	}

}


/*
$Log: ClientMessage.java,v $
Revision 1.7  2003/12/01 14:22:17  lemmster
ProtocolVersion handling completely rewritten

Revision 1.6  2003/09/18 15:29:25  zet
centralize writeStream in core
handle IOException rather than throwing it away

Revision 1.5  2003/09/18 09:16:47  lemmster
checkstyle

Revision 1.4  2003/08/23 15:21:37  zet
remove @author

Revision 1.3  2003/08/23 10:02:02  lemmster
use supertype where possible

Revision 1.2  2003/08/22 21:03:15  lemmster
replace $user$ with $Author: lemmster $

Revision 1.1  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging
*/
