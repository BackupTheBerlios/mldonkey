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
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;

/**
 * InfoList
 * 
 * @author ${user}
 * @version $$Id: InfoList.java,v 1.3 2003/06/14 23:04:08 lemmstercvs01 Exp $$ 
 */
public abstract class InfoList {
	/**
	 * 
	 */
	protected TIntObjectHashMap infoList;
	
	/**
	 * Generates a empty ServerInfoList object
	 */
	public InfoList() {
		this.infoList = new TIntObjectHashMap();
	}
	
	/**
	 * Get an Iterator
	 * @return an Iterator
	 */
	public TIntObjectIterator iterator() {
		return this.infoList.iterator();
	}
	
	public abstract void readStream( MessageBuffer messageBuffer );

	public abstract void update( MessageBuffer messageBuffer );
}
/*
$$Log: InfoList.java,v $
$Revision 1.3  2003/06/14 23:04:08  lemmstercvs01
$change from interface to abstract superclass
$
$Revision 1.2  2003/06/14 20:30:44  lemmstercvs01
$cosmetic changes
$$
*/