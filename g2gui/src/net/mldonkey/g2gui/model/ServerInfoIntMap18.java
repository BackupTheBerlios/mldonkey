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
import net.mldonkey.g2gui.comm.Message;

/**
 * ServerInfoIntMap18
 *
 * @version $Id: ServerInfoIntMap18.java,v 1.1 2003/12/01 14:22:17 lemmster Exp $ 
 *
 */
public class ServerInfoIntMap18 extends ServerInfoIntMap {
	/**
	 * @param communication
	 */
	public ServerInfoIntMap18(CoreCommunication communication) {
		super(communication);
	}
	/**
	 * Sends Message.S_CONNECT_MORE to the core.
	 *
	 * @param network The <code>NetworkInfo</code> we want more connections for
	 */
	public void connectMore( NetworkInfo network ) {
		//TODO we are waiting for the new opcode to send connectMore with the networkid
		Message message = new EncodeMessage( Message.S_CONNECT_MORE );
		message.sendMessage( this.parent );
		message = null;
	}
}

/*
$Log: ServerInfoIntMap18.java,v $
Revision 1.1  2003/12/01 14:22:17  lemmster
ProtocolVersion handling completely rewritten

*/