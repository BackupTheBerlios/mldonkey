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
package net.mldonkey.g2gui.model.enum;

/**
 * EnumState
 *
 * @author markus
 * @version $Id: EnumState.java,v 1.2 2003/07/30 19:30:21 lemmstercvs01 Exp $ 
 *
 */
public class EnumState implements Enum {
	private String state;
	/**
	 * not connected
	 */
	public static EnumState NOT_CONNECTED = new EnumState( "not_connected" );
	/**
	 * connecting
	 */
	public static EnumState CONNECTING =  new EnumState( "connecting" );
	/**
	 * connected initiating
	 */
	public static EnumState CONNECTED_INITIATING = new EnumState( "connected_initiated" );
	/**
	 * connected downloading
	 */
	public static EnumState CONNECTED_DOWNLOADING = new EnumState( "connected_downloading" );
	/**
	 * connected
	 */
	public static EnumState CONNECTED = new EnumState( "connected" );
	/**
	 * connected and queued
	 */
	public static EnumState CONNECTED_AND_QUEUED = new EnumState( "connected_and_queued" );
	/**
	 * new host
	 */
	public static EnumState NEW_HOST = new EnumState( "new_host" );
	/**
	 * remove host
	 */
	public static EnumState REMOVE_HOST = new EnumState( "remove_host" );
	/**
	 * black listed
	 */
	public static EnumState BLACK_LISTED = new EnumState( "black_listed" );
	/**
	 * not connected was queued
	 */
	public static EnumState NOT_CONNECTED_WAS_QUEUED = new EnumState( "not_connected_was_queued" );
	
	/**
	 * Creates a new EnumState
	 * @param aString The value
	 */
	private EnumState( String aString ) {
		state = aString;
	}
	/**
	 * @param state2
	 * @return
	 */
	public int compareTo( EnumState state2 ) {
		return this.state.compareTo( state2.state );
	}
}

/*
$Log: EnumState.java,v $
Revision 1.2  2003/07/30 19:30:21  lemmstercvs01
compareTo(EnumState enum) added

Revision 1.1  2003/06/24 09:29:33  lemmstercvs01
Enum more improved

Revision 1.1  2003/06/24 09:16:13  lemmstercvs01
better Enum added

*/