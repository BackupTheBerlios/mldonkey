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

import java.util.Date;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.view.G2Gui;

/**
 * ClientInfo24.java
 *
 * @version $Id: ClientInfo24.java,v 1.1 2004/03/27 17:00:01 dek Exp $ 
 *
 */
public class ClientInfo24 extends ClientInfo21 {

	/**
	 * @param core
	 */
	public ClientInfo24(CoreCommunication core) {
		super(core);			
	}
	
	protected void readConnectTime(MessageBuffer messageBuffer) {		
		// this needs to be fixed in the core soon since core/gui clocks can be out of sync rendering this useless
		// I've sent mail... 
		this.clientConnectTime = messageBuffer.readInt32();		
		
		/*
		 * this is now[unix-time stamp] - clientConnectTime
		 * that means: this client has been connected "clientConnectTime" seconds ago
		 */	
		this.clientConnectTimeString = G2Gui.noDescription+this.clientConnectTime;		
		this.clientConnectTimeString = G2Gui.emptyString +
		new Date( System.currentTimeMillis() - (clientConnectTime*1000) );
		
		
		/*
		 * this client is conected since "clientConnectTime" seconds
		 * now we build "06h 30m 21s"
		 */		
		int hours = (int) clientConnectTime / 3600;
		int minutes = (int) (clientConnectTime - (3600*hours)) / 60;
		int seconds = (int) (clientConnectTime - (3600*hours) - (minutes*60));
		
		StringBuffer temp = new StringBuffer();		
		
		if (hours > 0) { 
			temp.append(hours).append('h').append(' '); 
		}
		if ((minutes > 0) || (hours > 0)) { 
			temp.append(minutes).append('m').append(' '); 			
		}
		if ((seconds > 0) || (minutes > 0) || (hours > 0)) { 
			temp.append(seconds).append('s').append(' '); 			
		} else {
			temp.append('-');
		}
		this.clientConnectTimePassedString = temp.toString();
	}

}


/*
 $Log: ClientInfo24.java,v $
 Revision 1.1  2004/03/27 17:00:01  dek
 new date format in newer gui-Protos


 */