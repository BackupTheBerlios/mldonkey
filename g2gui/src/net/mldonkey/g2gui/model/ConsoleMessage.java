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
 * ConsoleMessage
 * 
 *  * @author ${user}
 * @version $$Id: ConsoleMessage.java,v 1.5 2003/06/20 15:15:22 dek Exp $$ 
 */
public class ConsoleMessage implements SimpleInformation {
	
	private CoreCommunication parent;
	/**
	 * @param core my parent, to send messages to
	 */
	public ConsoleMessage( CoreCommunication core ) {		
		this.parent = core;
	}
	
	/**
	 * String the core wants to display on the console
	 */
	private String consoleMessage;
	
	/**
	 * @return a string
	 */
	public synchronized String getConsoleMessage() {
		return consoleMessage;
	}

	/**
	 * @param string a string
	 */
	public void setConsoleMessage( String string ) {
		consoleMessage = string;
		/* tell our parent, that we have changed, and that this should be
		 * said to all the listeners
		 */		
		parent.notifyListeners( this );
	}
	
	/**
	 * Reads a ConsoleMessage object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		this.setConsoleMessage( messageBuffer.readString() );
	}
	

}
/*
$$Log: ConsoleMessage.java,v $
$Revision 1.5  2003/06/20 15:15:22  dek
$humm, some interface-changes, hope, it didn't break anything ;-)
$
$Revision 1.4  2003/06/19 08:40:20  lemmstercvs01
$checkstyle applied
$
$Revision 1.3  2003/06/18 13:30:56  dek
$Improved Communication Layer view <--> model by introducing a super-interface
$
$Revision 1.2  2003/06/14 20:30:44  lemmstercvs01
$cosmetic changes
$$
*/