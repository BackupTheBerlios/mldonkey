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

import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * AddSomeOptionList
 *
 * @author $user$
 * @version $Id: AddSomeOptionList.java,v 1.1 2003/06/15 09:58:04 lemmstercvs01 Exp $ 
 *
 */
public class AddSomeOptionList extends InfoList {
	List addSomeOptionsList;
	
	public AddSomeOptionList() {
		this.addSomeOptionsList = new ArrayList();
	}
	
	/**
	 * Does nothing!
	 * @param messageBuffer The MessageBuffer which is untouched
	 */
	public void readStream(MessageBuffer messageBuffer) {
		AddSomeOption someOption = new AddSomeOption();
		someOption.readStream( messageBuffer );
		this.addSomeOptionsList.add( someOption );
	}
	
	/**
	 * Does nothing!
	 * @param messageBuffer The MessageBuffer which is untouched
	 */
	public void update(MessageBuffer messageBuffer) {
		// do nothing
	}
}

/*
$Log: AddSomeOptionList.java,v $
Revision 1.1  2003/06/15 09:58:04  lemmstercvs01
initial commit

*/