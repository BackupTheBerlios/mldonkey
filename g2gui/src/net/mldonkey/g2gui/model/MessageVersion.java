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

import java.util.ArrayList;
import java.util.List;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;

/**
 * MessageVersion
 *
 * @author $Author: lemmster $
 * @version $Id: MessageVersion.java,v 1.3 2003/08/22 21:03:15 lemmster Exp $ 
 *
 */
public class MessageVersion implements Sendable {
	/*
	 *  List of (int32, int8, int32) 
	 *	The first int32 is an opcode message,
	 *  the second one is 0 for FromTheCore or 1 for FromTheGui,
	 *	and the last one the protocol version to use for that message. 
	 */
	private List versions;
	private CoreCommunication core;
		
	public MessageVersion( CoreCommunication core ) {
		this.versions = new ArrayList();
		this.core = core;
	}
	 
	public void add( int opcode, int version, boolean toCore ) {
		Object[] obj = new Object[ 3 ];
		/* the message */
		obj[ 0 ] = new Integer( opcode );
		/* the direction */
		if ( toCore )
			obj[ 1 ] = new Byte( ( byte ) 1 );
		else
			obj[ 1 ] = new Byte( ( byte ) 0 );	
		/* the version to use */
		obj[ 2 ] = new Integer( version );

		/* add it to our array */
		this.versions.add( obj );
	}
	
	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.model.Sendable#send()
	 */
	public void send() {
		Message message = new EncodeMessage( Message.S_MESSAGE_VERSION, this.versions.toArray() );
		message.sendMessage( this.core.getConnection() );
		message = null;
	}
}

/*
$Log: MessageVersion.java,v $
Revision 1.3  2003/08/22 21:03:15  lemmster
replace $user$ with $Author$

Revision 1.2  2003/08/02 09:54:17  lemmstercvs01
replaced socket with corecommunication

Revision 1.1  2003/08/01 13:34:31  lemmstercvs01
initial commit

*/