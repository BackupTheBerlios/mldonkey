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

import java.util.Observer;

import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * InfoList
 * 
 *
 * @version $Id: InfoCollection.java,v 1.8 2003/12/04 08:47:25 lemmy Exp $
 */
public interface InfoCollection extends Information {
	
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
	
	/**
	 * Adds an Observer to this object
	 * @param obj The Observer to add
	 */
	void addObserver( Observer obj );
	/**
	 * Removes an Observer from this object
	 * @param obj The Observer to remove
	 */
	void deleteObserver( Observer obj );
}
/*
$Log: InfoCollection.java,v $
Revision 1.8  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.7  2003/08/23 15:21:37  zet
remove @author

Revision 1.6  2003/08/22 21:03:15  lemmy
replace $user$ with $Author: lemmy $

Revision 1.5  2003/08/02 09:55:55  lemmy
observers changed

Revision 1.4  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.3  2003/06/17 12:10:17  lemmy
some methods added

Revision 1.2  2003/06/16 15:32:43  lemmy
changed some modifiers

Revision 1.1  2003/06/15 16:18:41  lemmy
new interface introduced

Revision 1.3  2003/06/14 23:04:08  lemmy
change from interface to abstract superclass

Revision 1.2  2003/06/14 20:30:44  lemmy
cosmetic changes
*/