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

import java.util.Observable;

import net.mldonkey.g2gui.comm.CoreCommunication;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;

/**
 * InfoMap
 *
 *
 * @version $Id: InfoIntMap.java,v 1.11 2003/12/04 08:47:25 lemmy Exp $ 
 *
 */
public abstract class InfoIntMap extends Observable implements InfoCollection {
	/**
	 * 
	 */
	protected CoreCommunication parent;
	/**
	 * 
	 */
	protected TIntObjectHashMap infoIntMap;
	
	/**
	 * Generates a empty ServerInfoList object
	 */
	InfoIntMap() {
		this.infoIntMap = new TIntObjectHashMap();
	}
	
	/**
	 * @param communication my parent
	 */	
	InfoIntMap( CoreCommunication communication ) {	
	    this();
		this.parent = communication;
	}
	
	/**
	 * Get an Iterator
	 * @return an Iterator
	 */
	public TIntObjectIterator iterator() {
		return this.infoIntMap.iterator();
	}

	/**
	 * The number of elems in this object
	 * @return int the size
	 */
	public int size() {
		return this.infoIntMap.size();
	}
	
	/**
	 * Does this Map contains the given key
	 * @param key The key which should be contained
	 * @return true/false if contains
	 */
	public boolean containsKey( int key ) {
		return this.infoIntMap.contains( key );
	}
}

/*
$Log: InfoIntMap.java,v $
Revision 1.11  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.10  2003/12/01 14:22:17  lemmy
ProtocolVersion handling completely rewritten

Revision 1.9  2003/10/22 21:02:11  zet
use this()

Revision 1.8  2003/08/23 15:21:37  zet
remove @author

Revision 1.7  2003/08/22 21:03:15  lemmy
replace $user$ with $Author: lemmy $

Revision 1.6  2003/08/01 17:21:19  lemmy
reworked observer/observable design, added multiversion support

Revision 1.5  2003/07/23 17:04:51  lemmy
added containsKey(int key)

Revision 1.4  2003/07/06 07:45:26  lemmy
checkstyle applied

Revision 1.3  2003/06/20 15:15:22  dek
humm, some interface-changes, hope, it didn't break anything ;-)

Revision 1.2  2003/06/17 12:10:17  lemmy
some methods added

Revision 1.1  2003/06/16 21:47:19  lemmy
just refactored (name changed)

Revision 1.1  2003/06/15 16:18:41  lemmy
new interface introduced

*/