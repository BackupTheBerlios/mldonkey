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
import java.util.Iterator;
import java.util.List;

import net.mldonkey.g2gui.comm.CoreCommunication;

/**
 * InfoList
 *
 * @author $user$
 * @version $Id: InfoList.java,v 1.8 2003/06/20 15:15:22 dek Exp $ 
 *
 */
public abstract class InfoList implements InfoCollection {
	
	protected CoreCommunication parent;

	/**
	 * the addsomeoptionlist 
	 */
	protected List infoList;
	
	/**
	 * Creates an empty AddSomeOptionList object
	 */
	public InfoList() {
		this.infoList = new ArrayList();
	}
	/**
	 * Creates an empty AddSomeOptionList object and registers parent
	 * @param core This lists parent, which can be notified on changes
	 */
	public InfoList( CoreCommunication core ) {
		this.infoList = new ArrayList();
		this.parent = core;
	}
	
	/**
	 * Get an Iterator for this list
	 * @return iterator An Iterator
	 */
	public Iterator iterator() {
		return this.infoList.iterator();
	}
	
	/**
	 * The number of elems in this object
	 * @return int a size
	 */
	public int size() {
		return this.infoList.size();
	}
	
}

/*
$Log: InfoList.java,v $
Revision 1.8  2003/06/20 15:15:22  dek
humm, some interface-changes, hope, it didn't break anything ;-)

Revision 1.7  2003/06/17 12:10:17  lemmstercvs01
some methods added

Revision 1.6  2003/06/16 15:32:43  lemmstercvs01
changed some modifiers

Revision 1.5  2003/06/16 13:18:59  lemmstercvs01
checkstyle applied

Revision 1.4  2003/06/15 16:18:41  lemmstercvs01
new interface introduced

*/