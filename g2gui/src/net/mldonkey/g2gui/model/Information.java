/*
 * Copyright 2003
 * G2GUI Team
 * 
 * 
 * This file is part of G2GUI.
 *
 * G2GUI is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2GUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2GUI; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.model;
import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * Information
 *
 * @author $user$
 * @version $Id: Information.java,v 1.5 2003/06/20 15:15:22 dek Exp $ 
 *
 */
public interface Information {
	/**
	 * Reads an object from a MessageBuffer object
	 * @param messageBuffer MessageBuffer to read from
	 */
	void readStream( MessageBuffer messageBuffer );

}

/*
$Log: Information.java,v $
Revision 1.5  2003/06/20 15:15:22  dek
humm, some interface-changes, hope, it didn't break anything ;-)

Revision 1.4  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

*/