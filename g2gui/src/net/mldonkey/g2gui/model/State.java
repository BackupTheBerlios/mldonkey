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
import net.mldonkey.g2gui.model.enum.*;

/**
 * State
 *
 * @author $Author: lemmster $
 * @version $Id: State.java,v 1.11 2003/08/22 21:03:15 lemmster Exp $ 
 *
 */
public class State implements SimpleInformation {
	
	/**
	 * Connections State
	 */
	private Enum state;
	/**
	 * Client Rank
	 */
	private int rank;
	
	/**
	 * @return The current rank
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @return The current state
	 */
	public Enum getState() {
		return state;
	}

	/**
	 * @param b a byte
	 */
	protected void setState( byte b ) {
		if ( b == 0 )
			state = EnumState.NOT_CONNECTED;
		else if ( b == 1 )
			state = EnumState.CONNECTING;
		else if ( b == 2 )
			state = EnumState.CONNECTED_INITIATING;
		else if ( b == 3 )
			state = EnumState.CONNECTED_DOWNLOADING;
		else if ( b == 4 )
			state = EnumState.CONNECTED;
		else if ( b == 5 )
			state = EnumState.CONNECTED_AND_QUEUED;
		else if ( b == 6 )
			state = EnumState.NEW_HOST;
		else if ( b == 7 )
			state = EnumState.REMOVE_HOST;
		else if ( b == 8 )
			state = EnumState.BLACK_LISTED;
		else if ( b == 9 )
			state = EnumState.NOT_CONNECTED_WAS_QUEUED;
	}
	
	/**
	 *  Reads a State object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		this.setState( messageBuffer.readByte() );
		if ( this.getState() == EnumState.CONNECTED_AND_QUEUED
			 || this.getState() == EnumState.NOT_CONNECTED_WAS_QUEUED )
			this.rank = messageBuffer.readInt32();
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
Revision 1.11  2003/08/22 21:03:15  lemmster
replace $user$ with $Author$

Revision 1.10  2003/07/05 16:04:34  lemmstercvs01
javadoc improved

Revision 1.9  2003/06/24 09:29:57  lemmstercvs01
Enum more improved

Revision 1.8  2003/06/24 09:16:48  lemmstercvs01
better Enum added

Revision 1.7  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.6  2003/06/16 15:33:03  lemmstercvs01
some kind of enum added

Revision 1.5  2003/06/14 17:41:03  lemmstercvs01
foobar

*/