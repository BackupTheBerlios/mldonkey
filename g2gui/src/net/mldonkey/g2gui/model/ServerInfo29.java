/*
 * Copyright 2003
 * g2gui Team
 * 
 * 
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.model;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * ServerInfo29.java
 *
 * @version $Id: ServerInfo29.java,v 1.1 2004/09/17 22:36:48 dek Exp $ 
 *
 */
public class ServerInfo29 extends ServerInfo28 {

	/**
	 * @param core
	 */
	ServerInfo29(CoreCommunication core) {
		super(core);	
	}
	
	/**
	 * @param messageBuffer
	 */
	protected void readPreferred(MessageBuffer messageBuffer) {
		byte temp = messageBuffer.readByte();	
		this.favorite = (temp == 1 ) ? true : false;
	}	

}


/*
 $Log: ServerInfo29.java,v $
 Revision 1.1  2004/09/17 22:36:48  dek
 update for gui-Protocol 29


 */