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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.model.enum.EnumNetwork;

/**
 * FileInfo18
 *
 * @version $Id: FileInfo18.java,v 1.2 2003/12/04 08:47:25 lemmy Exp $ 
 *
 */
public class FileInfo18 extends FileInfo {
	/**
	 * Availibility for each network
	 * only in protocol >= 18
	 */
	private Map avails;

	/**
	 * @param core
	 */
	FileInfo18(CoreCommunication core) {
		super(core);
	}

	/**
	 * @param messageBuffer
	 *
	 * read a list of int32(networkid) and string(avail)
	 */
	protected void setAvailability(MessageBuffer messageBuffer) {
		int listElem = messageBuffer.readInt16();
		this.avails = new HashMap(listElem);

		boolean foundMultiNet = false;

		for (int i = 0; i < listElem; i++) {
			int networkID = messageBuffer.readInt32();
			NetworkInfo aNetwork = parent.getNetworkInfoMap().get(networkID);
			/* multinet avail is the overall avail */
			if (aNetwork.getNetworkType() == EnumNetwork.MULTINET) {
				this.avail = messageBuffer.readString();
				foundMultiNet = true;
			} else {
				/*
				 * Set this.avail until multiNet is found.
				 * if you only check for null, it will not be updated
				 * by subsequent updates.
				 */
				String tempAvail = messageBuffer.readString();
				if (!foundMultiNet)
					this.avail = tempAvail;
				this.avails.put(aNetwork, tempAvail);
			}
		}
		setRelativeAvail();
	}

	public boolean hasAvails() {
		return true;
	}
	/**
	 * @return The file availability map
	 */
	public String getAvails( NetworkInfo networkInfo ) {
		return (String) avails.get( networkInfo );
	}
	
	public Set getAllAvailNetworks() {
		return this.avails.keySet();
	}
}

/*
$Log: FileInfo18.java,v $
Revision 1.2  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.1  2003/12/01 14:22:17  lemmy
ProtocolVersion handling completely rewritten

*/