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
 * InfoList
 * 
 * @author ${user}
 * @version $$Id: InfoCollection.java,v 1.3 2003/06/17 12:10:17 lemmstercvs01 Exp $$ 
 */
public interface InfoCollection {
	
	/**
	 * Reads a InfoCollection from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	void readStream( MessageBuffer messageBuffer );
	
	/**
	 * Updates a InfoCollection from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	void update( MessageBuffer messageBuffer );
	
	/**
	 * Retuns the size of this object
	 * @return int a size
	 */
	int size();
}
/*
$$Log: InfoCollection.java,v $
$Revision 1.3  2003/06/17 12:10:17  lemmstercvs01
$some methods added
$
$Revision 1.2  2003/06/16 15:32:43  lemmstercvs01
$changed some modifiers
$
$Revision 1.1  2003/06/15 16:18:41  lemmstercvs01
$new interface introduced
$
$Revision 1.3  2003/06/14 23:04:08  lemmstercvs01
$change from interface to abstract superclass
$
$Revision 1.2  2003/06/14 20:30:44  lemmstercvs01
$cosmetic changes
$$
*/