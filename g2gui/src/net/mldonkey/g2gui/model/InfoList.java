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

/**
 * InfoList
 *
 * @author $user$
 * @version $Id: InfoList.java,v 1.5 2003/06/16 13:18:59 lemmstercvs01 Exp $ 
 *
 */
public abstract class InfoList implements InfoCollection {
	/**
	 * the addsomeoptionlist 
	 */
	private List infoList;
	
	/**
	 * Creates an empty AddSomeOptionList object
	 */
	public InfoList() {
		this.infoList = new ArrayList();
	}
	
	/**
	 * Get an Iterator for this list
	 * @return iterator An Iterator
	 */
	public Iterator iterator() {
		return this.infoList.iterator();
	}
}

/*
$Log: InfoList.java,v $
Revision 1.5  2003/06/16 13:18:59  lemmstercvs01
checkstyle applied

Revision 1.4  2003/06/15 16:18:41  lemmstercvs01
new interface introduced

*/