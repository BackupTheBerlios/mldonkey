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
import gnu.trove.THashMap;

/**
 * OptionsInfo
 *
 * @author $user$
 * @version $Id: OptionsInfo.java,v 1.1 2003/06/14 23:07:20 lemmstercvs01 Exp $ 
 *
 */
public class OptionsInfo implements Information {
	/**
	 * Map containing option value pairs
	 */
	private THashMap optionsInfo;
	
	/**
	 * Creates a new THashMap
	 */	
	public OptionsInfo() {
		this.optionsInfo = new THashMap();
	}
	
	/**
	 * Reads an OptionsInfo object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		/*
		 * List of (String,String)	The list of options with their current value
		 */
		short listElem = messageBuffer.readInt16();
		for ( int i = 0; i < ( listElem / 2 ); i++ ) {
			String key = new String( messageBuffer.readString() );
			String value = new String( messageBuffer.readString() );
			this.optionsInfo.put( key, value );
		}
	}
}

/*
$Log: OptionsInfo.java,v $
Revision 1.1  2003/06/14 23:07:20  lemmstercvs01
added opcode 1

*/