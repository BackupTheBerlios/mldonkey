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
 * Information
 *
 * @author markus
 * @version $Id: SimpleInformation.java,v 1.2 2003/07/06 08:49:03 lemmstercvs01 Exp $ 
 *
 */
public interface SimpleInformation extends Information {
	/**
	 * Reads an object from a MessageBuffer object
	 * @param messageBuffer MessageBuffer to read from
	 */
	void readStream( MessageBuffer messageBuffer );
}

/*
$Log: SimpleInformation.java,v $
Revision 1.2  2003/07/06 08:49:03  lemmstercvs01
refactored

Revision 1.1  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.3  2003/06/14 12:47:27  lemmstercvs01
checkstyle applied

Revision 1.2  2003/06/13 11:03:41  lemmstercvs01
changed InputStream to MessageBuffer

Revision 1.1  2003/06/12 22:23:06  lemmstercvs01
lots of changes

*/