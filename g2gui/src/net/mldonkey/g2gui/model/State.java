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

/**
 * State
 *
 * @author markus
 * @version $Id: State.java,v 1.5 2003/06/14 17:41:03 lemmstercvs01 Exp $ 
 *
 */
public class State implements Information {
	/**
	 * Connections State
	 */
	private byte state;
	/**
	 * Client Rank
	 */
	private int rank;
	
	/**
	 * @return an int
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @return a byte
	 */
	public byte getState() {
		return state;
	}

	/**
	 * @param i an int
	 */
	public void setRank( int i ) {
		rank = i;
	}

	/**
	 * @param b a byte
	 */
	public void setState( byte b ) {
		state = b;
	}
	
	/**
	 *  Reads a State object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		this.setState( messageBuffer.readByte() );
		if ( this.getState() == 5 || this.getState() == 9 )
			this.setRank( messageBuffer.readInt32() );
	}
	
	/**
	 * Updates the state of this object
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		this.readStream( messageBuffer );
	}
}

/*
$Log: State.java,v $
Revision 1.5  2003/06/14 17:41:03  lemmstercvs01
foobar

*/