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

import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * ConsoleMessage
 * 
 *
 * @version $Id: ConsoleMessage.java,v 1.14 2003/12/04 08:47:25 lemmy Exp $
 */
public class ConsoleMessage extends SimpleInformation {
	
	/**
	 * String the core wants to display on the console
	 */
	private String consoleMessage;
	
	ConsoleMessage() {
		//prevent outer package instanciation
	}
	
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
		if ( consoleMessage == null )
			consoleMessage = string;	
		else 
			consoleMessage += string;
		this.setChanged();
		this.notifyObservers( this );
	}
	
	/**
	 * Reads a ConsoleMessage object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		this.setConsoleMessage( messageBuffer.readString() );
		this.setChanged();
		this.notifyObservers( this );						
	}

	/**
	 * 
	 */
	public void reset() {
		consoleMessage = "";
	}
}
/*
$Log: ConsoleMessage.java,v $
Revision 1.14  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.13  2003/12/01 14:22:17  lemmy
ProtocolVersion handling completely rewritten

Revision 1.12  2003/08/23 15:21:37  zet
remove @author

Revision 1.11  2003/08/22 21:03:15  lemmy
replace $user$ with $Author: lemmy $

Revision 1.10  2003/08/01 17:21:19  lemmy
reworked observer/observable design, added multiversion support

Revision 1.9  2003/07/04 10:26:03  lemmy
minor: just checkstyle

Revision 1.8  2003/06/27 17:12:36  lemmy
removed unneeded fields

Revision 1.7  2003/06/27 10:35:53  lemmy
removed unneeded calls

Revision 1.6  2003/06/25 18:04:53  dek
Console-Tab reworked

Revision 1.5  2003/06/20 15:15:22  dek
humm, some interface-changes, hope, it didn't break anything ;-)

Revision 1.4  2003/06/19 08:40:20  lemmy
checkstyle applied

Revision 1.3  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.2  2003/06/14 20:30:44  lemmy
cosmetic changes
*/