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

import java.util.Observable;

import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * Information
 *
 *
 * @version $Id: SimpleInformation.java,v 1.6 2003/12/04 08:47:25 lemmy Exp $ 
 *
 */
public abstract class SimpleInformation extends Observable implements Information {
	/**
	 * Reads an object from a MessageBuffer object
	 * @param messageBuffer MessageBuffer to read from
	 */
	public abstract void readStream( MessageBuffer messageBuffer );
	
	SimpleInformation() {
		//prevent outer package instanciation
	}
}

/*
$Log: SimpleInformation.java,v $
Revision 1.6  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.5  2003/12/01 14:22:17  lemmy
ProtocolVersion handling completely rewritten

Revision 1.4  2003/08/23 15:21:37  zet
remove @author

Revision 1.3  2003/08/22 21:03:15  lemmy
replace $user$ with $Author: lemmy $

Revision 1.2  2003/07/06 08:49:03  lemmy
refactored

Revision 1.1  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.3  2003/06/14 12:47:27  lemmy
checkstyle applied

Revision 1.2  2003/06/13 11:03:41  lemmy
changed InputStream to MessageBuffer

Revision 1.1  2003/06/12 22:23:06  lemmy
lots of changes

*/