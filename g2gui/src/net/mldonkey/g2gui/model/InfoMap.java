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

import gnu.trove.THashMap;

/**
 * InfoMap
 *
 * @author $user$
 * @version $Id: InfoMap.java,v 1.2 2003/06/16 21:48:38 lemmstercvs01 Exp $ 
 *
 */
public abstract class InfoMap implements InfoCollection {
	/**
	 * Map containing option value pairs
	 */
	protected THashMap infoMap;
	
	/**
	 * Creates a new THashMap
	 */	
	public InfoMap() {
		this.infoMap = new THashMap();
	}
}

/*
$Log: InfoMap.java,v $
Revision 1.2  2003/06/16 21:48:38  lemmstercvs01
class hierarchy changed

*/