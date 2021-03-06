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

import net.mldonkey.g2gui.view.resource.G2GuiResources;

/**
 * EnumState
 *
 *
 * @version $Id: EnumState.java,v 1.10 2003/12/04 08:47:29 lemmy Exp $ 
 *
 */
public class EnumState extends Enum {
	private String state;
	/**
	 * not connected
	 */
	public static EnumState NOT_CONNECTED = new EnumState( 1, G2GuiResources.getString( "ENS_NOT_CONNECTED" ) );
	/**
	 * connecting
	 */
	public static EnumState CONNECTING =  new EnumState( 2, G2GuiResources.getString( "ENS_CONNECTING" ) );
	/**
	 * connected initiating
	 */
	public static EnumState CONNECTED_INITIATING = new EnumState( 4, G2GuiResources.getString( "ENS_CONNECTED_INITIATING" ) );
	/**
	 * connected downloading
	 */
	public static EnumState CONNECTED_DOWNLOADING = new EnumState( 8, G2GuiResources.getString( "ENS_CONNECTED_DOWNLOADING" ) );
	/**
	 * connected
	 */
	public static EnumState CONNECTED = new EnumState( 16, G2GuiResources.getString( "ENS_CONNECTED" ) );
	/**
	 * connected and queued
	 */
	public static EnumState CONNECTED_AND_QUEUED = new EnumState( 32, G2GuiResources.getString( "ENS_CONNECTED_AND_QUEUED" ) );
	/**
	 * new host
	 */
	public static EnumState NEW_HOST = new EnumState( 64, G2GuiResources.getString( "ENS_NEW_HOST" ) );
	/**
	 * remove host
	 */
	public static EnumState REMOVE_HOST = new EnumState( 128, G2GuiResources.getString( "ENS_REMOVE_HOST" ) );
	/**
	 * black listed
	 */
	public static EnumState BLACK_LISTED = new EnumState( 256, G2GuiResources.getString( "ENS_BLACK_LISTED" ) );
	/**
	 * not connected was queued
	 */
	public static EnumState NOT_CONNECTED_WAS_QUEUED = new EnumState( 512, G2GuiResources.getString( "ENS_NOT_CONNECTED_WAS_QUEUED" ) );
	
	/**
	 * Creates a new EnumState
	 * @param aString The value
	 */
	private EnumState( int anInt, String aString ) {
		super( anInt );
		state = aString;
	}
	/**
	 * @param state2 The EnumState we compare to
	 * @return the result of this compareTo operation
	 */
	public int compareTo( EnumState state2 ) {
		return this.state.compareTo( state2.state );
	}
	
	/**
	 * The String representation of this obj
	 * @return String The String
	 */
	public String toString() {
		return state;
	}
}

/*
$Log: EnumState.java,v $
Revision 1.10  2003/12/04 08:47:29  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.9  2003/10/28 11:07:32  lemmy
move NetworkInfo.Enum -> enum.EnumNetwork
add MaskMatcher for "Enum[]"

Revision 1.8  2003/09/18 09:18:01  lemmy
checkstyle

Revision 1.7  2003/08/23 15:21:37  zet
remove @author

Revision 1.6  2003/08/22 21:04:27  lemmy
replace $user$ with $Author: lemmy $

Revision 1.5  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.4  2003/08/07 13:25:37  lemmy
ResourceBundle added

Revision 1.3  2003/08/06 09:44:54  lemmy
toString() added

Revision 1.2  2003/07/30 19:30:21  lemmy
compareTo(EnumState enum) added

Revision 1.1  2003/06/24 09:29:33  lemmy
Enum more improved

Revision 1.1  2003/06/24 09:16:13  lemmy
better Enum added

*/