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

import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.model.enum.EnumState;

/**
 * StateHandler.java
 *
 * @version $Id: StateHandler.java,v 1.1 2004/03/25 19:25:23 dek Exp $ 
 *
 */
public class StateHandler {
	
	static Enum getStatefromByte( byte b ) {
		Enum state  = null;
		
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
		else if ( b == 10 )
			state = EnumState.CONNECTED;
		
		return state;
	}

}


/*
 $Log: StateHandler.java,v $
 Revision 1.1  2004/03/25 19:25:23  dek
 yet more profiling


 */