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
import net.mldonkey.g2gui.model.enum.EnumState;

/**
 * ClientInfo21.java
 *
 * @version $Id: ClientInfo21.java,v 1.3 2004/03/25 19:25:23 dek Exp $ 
 *
 */
public class ClientInfo21 extends ClientInfo20 {

	/**
	 * @param core
	 */
	public ClientInfo21(CoreCommunication core) {
		super(core);		
	}
	
	private String eMulemod;
	private int fileNumber = -2;
	
	public int getFileNumber(){
		return fileNumber;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see net.mldonkey.g2gui.model.ClientInfo#getClientName()
	 */
	public String getClientName() {
		String result = super.getClientName();
		if (!eMulemod.equals(""))
			result = result+" ("+eMulemod+")";		
		return result;
	}
	
	
	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.model.ClientInfo20#readStream(int, net.mldonkey.g2gui.helper.MessageBuffer)
	 */
	public void readStream(int clientID, MessageBuffer messageBuffer) {		
		super.readStream(clientID, messageBuffer);
		eMulemod = messageBuffer.readString();			
	}
	
	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.model.ClientInfo20#readState(net.mldonkey.g2gui.helper.MessageBuffer)
	 */
	protected void readState(MessageBuffer messageBuffer) {		
		super.readState(messageBuffer);
		if (this.getState() == EnumState.CONNECTED_DOWNLOADING) {
			fileNumber = messageBuffer.readInt32();
		}		
		
	}
	

	

	
}


/*
 $Log: ClientInfo21.java,v $
 Revision 1.3  2004/03/25 19:25:23  dek
 yet more profiling

 Revision 1.2  2004/03/22 18:47:39  dek
 Still some Gui-Protocoll enhancements

 Revision 1.1  2004/03/21 21:00:50  dek
 implemented gui-Proto 21-25 !!!!!


 */