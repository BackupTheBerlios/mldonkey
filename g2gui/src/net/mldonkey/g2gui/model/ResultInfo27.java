/*
 * Copyright 2003
 * g2gui Team
 * 
 * 
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.model;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.helper.RegExp;
import net.mldonkey.g2gui.view.G2Gui;

/**
 * ResultInfo27.java
 *
 * @version $Id: ResultInfo27.java,v 1.1 2004/09/17 22:36:48 dek Exp $ 
 *
 */
public class ResultInfo27 extends ResultInfo25 {
	
	/**
	 * @param core
	 */
	ResultInfo27(CoreCommunication core) {
		super(core);
	}
	
	/**
	 * @param messageBuffer
	 */
	protected void readMD4(MessageBuffer messageBuffer) {		
		String[] infoList = messageBuffer.readStringList();
		for ( int i = 0; i < infoList.length; i++ ) {
			// Format: "urn:ed2k:41D0E26A3572B2C1318083D0B1E2A279"
			String[] parts = RegExp.split( infoList[i], ':' );
			// The second 3rd of the string is the md4: 
			this.md4=parts[2];			
		}
		
	}
	
	/**
	 * @param messageBuffer
	 */
	protected void readDate(MessageBuffer messageBuffer) {
		/* TODO find out, what this date-field is about....		
		 * up to now, we only read the int, and throw it away...
		 */
		messageBuffer.readInt32();
	}

}


/*
 $Log: ResultInfo27.java,v $
 Revision 1.1  2004/09/17 22:36:48  dek
 update for gui-Protocol 29


 */