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

import java.util.Collection;
import java.util.Set;

import gnu.trove.THashMap;

/**
 * InfoMap
 *
 * @author $user$
 * @version $Id: InfoMap.java,v 1.4 2003/06/18 13:30:56 dek Exp $ 
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
	
	/**
	 * An EntrySet of this Map
	 * @return An EntrySet of this Map
	 */
	public Set entrySet() {
		return this.infoMap.entrySet();
	}
	
	/**
	 * A Set of keys
	 * @return A Set of keys
	 */
	public Set keySet() {
		return this.infoMap.keySet();
	}
	
	/**
	 * A Collection of values
	 * @return A Collection of values
	 */
	public Collection values() {
		return this.infoMap.values();
	}
	
	/**
	 * Get a specific Information object 
	 * @param key An object key
	 * @return An Information object
	 */
	public SimpleInformation get( Object key ) {
		return ( SimpleInformation ) this.infoMap.get( key );
	}
	
	/**
	 * The number of elems in this object
	 * @return int a size
	 */
	public int size() {
		return this.infoMap.size();
	}
	
}

/*
$Log: InfoMap.java,v $
Revision 1.4  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.3  2003/06/17 12:10:17  lemmstercvs01
some methods added

Revision 1.2  2003/06/16 21:48:38  lemmstercvs01
class hierarchy changed

*/