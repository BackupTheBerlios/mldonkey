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

import java.util.Iterator;

import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * AddSomeOptionList
 *
 * @author $user$
 * @version $Id: AddSomeOptionList.java,v 1.2 2003/06/15 16:18:41 lemmstercvs01 Exp $ 
 *
 */
public class AddSomeOptionList extends InfoList {
	/**
	 * 
	 */
	public AddSomeOptionList() {
		super();
	}
	
	/**
	 * Reads an AddSomeOptionList from a MessageBuffer
	 * @param messageBuffer The MessageBuffer which is untouched
	 */
	public void readStream(MessageBuffer messageBuffer) {
		AddSomeOption someOption = new AddSomeOption();
		someOption.readStream( messageBuffer );
		this.infoList.add( someOption );
	}
	
	/**
	 * Does nothing!
	 * @param messageBuffer The MessageBuffer which is untouched
	 */
	public void update(MessageBuffer messageBuffer) {
		// do nothing
	}
	
	/**
	 * Returns a string representation of this object
	 * @return String a string
	 */
	public String toString() {
		String result = new String();
		Iterator itr = this.infoList.iterator();
		while ( itr.hasNext() ) {
			result += ( AddSomeOption ) itr.next();
		}
		return result;
	}
}

/*
$Log: AddSomeOptionList.java,v $
Revision 1.2  2003/06/15 16:18:41  lemmstercvs01
new interface introduced

Revision 1.1  2003/06/15 09:58:04  lemmstercvs01
initial commit

*/