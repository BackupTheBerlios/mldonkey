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
 * Download
 *
 *
 * @version $Id: Download.java,v 1.14 2003/12/01 14:22:17 lemmster Exp $ 
 *
 */
public class Download implements Sendable {
	/**
	 * All known names for the file 
	 */
	private String[] possibleNames;
	/**
	 * Identifier of the result to download
	 */
	private int resultID;
	/**
	 * 0 = Try, 1 = Force Download
	 */
	private byte force;
	/**
	 * The parent CoreCommunication object
	 */
	private CoreCommunication core;

	/**
	 * Creates a new download
	 * @param core The parent core obj
	 */
	Download( CoreCommunication core ) {
		this.core = core;		
	}

	/**
	 * @param b false = try, true = force download
	 */
	public void setForce( boolean b ) {
		if ( b ) 
			force = 1;
		else
			force = 0;	
	}
	/**
	 * @param strings All possible FileNames
	 */
	public void setPossibleNames( String[] strings ) {
		possibleNames = strings;
	}
	/**
	 * @param i The result id for this download
	 */
	public void setResultID( int i ) {
		resultID = i;
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.model.Sendable#send()
	 */
	public void send() {
		Object[] temp = new Object[ 3 ];
		temp[ 0 ] = possibleNames;
		temp[ 1 ] = new Integer( resultID );
		temp[ 2 ] = new Byte( force );
		Message message = new EncodeMessage( Message.S_DOWNLOAD, temp );
		message.sendMessage( this.core );
		message = null;
	}
}

/*
$Log: Download.java,v $
Revision 1.14  2003/12/01 14:22:17  lemmster
ProtocolVersion handling completely rewritten

Revision 1.13  2003/11/23 17:58:03  lemmster
removed dead/unused code

Revision 1.12  2003/09/18 15:29:25  zet
centralize writeStream in core
handle IOException rather than throwing it away

Revision 1.11  2003/09/18 03:51:27  zet
reverse setforce

Revision 1.10  2003/09/02 09:26:52  lemmster
checkstyle

Revision 1.9  2003/08/23 15:21:37  zet
remove @author

Revision 1.8  2003/08/23 10:02:02  lemmster
use supertype where possible

Revision 1.7  2003/08/22 21:03:15  lemmster
replace $user$ with $Author: lemmster $

Revision 1.6  2003/07/23 16:56:28  lemmstercvs01
initial commit

*/