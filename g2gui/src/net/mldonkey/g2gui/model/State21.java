/*
 * Copyright 2003 g2gui Team
 * 
 * 
 * This file is part of g2gui.
 * 
 * g2gui is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * g2gui is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with g2gui; if not,
 * write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 *  
 */
package net.mldonkey.g2gui.model;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.model.enum.EnumState;

/**
 * State21.java
 * 
 * @version $Id: State21.java,v 1.4 2004/03/25 18:07:24 dek Exp $
 *  
 */
public class State21 extends State {

	/**
	 * @param core
	 */
	State21(CoreCommunication core) {
		super(core);		
	}

	private int fileNumber = -2;

	/**
	 * Reads a State object from a MessageBuffer
	 * 
	 * @param messageBuffer
	 *            The MessageBuffer to read from
	 */
	public void readStream(MessageBuffer messageBuffer) {
		super.readStream(messageBuffer);
		if (this.getState() == EnumState.CONNECTED_DOWNLOADING) {
			fileNumber = messageBuffer.readInt32();
		}
	}
	
	public int getFileNumber(){
		return fileNumber;
	}
}

/*
 * $Log: State21.java,v $
 * Revision 1.4  2004/03/25 18:07:24  dek
 * profiling
 *
 * Revision 1.3  2004/03/22 19:17:58  dek
 * identified mysterious state-id as fileID currently beeing x-ferred
 *
 * Revision 1.2  2004/03/22 18:47:39  dek
 * Still some Gui-Protocoll enhancements
 * Revision 1.1 2004/03/21 21:00:50 dek implemented gui-Proto 21-25 !!!!!
 * 
 *  
 */