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
 * EnumFileState
 *
 *
 * @version $Id: EnumFileState.java,v 1.6 2003/12/07 19:38:10 lemmy Exp $ 
 *
 */
public class EnumFileState extends Enum {
	private String state;
	/**
	 * downloading (value is 0)
	 */
	public static EnumFileState DOWNLOADING = new EnumFileState( 1, G2GuiResources.getString( "TT_Downloading" ) );
	/**
	 * paused (value is 1)
	 */
	public static EnumFileState PAUSED = new EnumFileState( 2, G2GuiResources.getString( "TT_Paused" ) );
	/**
	 * downloaded (value is 2)
	 */
	public static EnumFileState DOWNLOADED = new EnumFileState( 4, G2GuiResources.getString( "TT_Downloaded" ) );
	/**
	 * shared (value is 3)
	 */
	public static EnumFileState SHARED = new EnumFileState( 8, G2GuiResources.getString( "TT_Shared" ) );
	/**
	 * cancelled (value is 4)
	 */
	public static EnumFileState CANCELLED = new EnumFileState( 16, G2GuiResources.getString( "TT_Cancelled" ) );
	/**
	 * new (value is 5)
	 */
	public static EnumFileState NEW = new EnumFileState( 32, G2GuiResources.getString( "TT_New" ) );
	/**
	 * aborted (value is 6)
	 */
	public static EnumFileState ABORTED = new EnumFileState( 64, G2GuiResources.getString( "TT_Aborted" ) );
	/**
	 * queued (value is 7)
	 */
	public static EnumFileState QUEUED = new EnumFileState( 128, G2GuiResources.getString( "TT_Queued" ) );
	
	/**
	 * Creates a new EnumFileState
	 * @param anInt The value
	 */
	private EnumFileState( int anInt, String aString ) {
		super( anInt );
		state = aString;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return state;
	}
}

/*
$Log: EnumFileState.java,v $
Revision 1.6  2003/12/07 19:38:10  lemmy
refactoring

Revision 1.5  2003/12/04 08:47:29  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.4  2003/10/28 11:07:32  lemmy
move NetworkInfo.Enum -> enum.EnumNetwork
add MaskMatcher for "Enum[]"

Revision 1.3  2003/08/23 15:21:37  zet
remove @author

Revision 1.2  2003/08/22 21:04:27  lemmy
replace $user$ with $Author: lemmy $

Revision 1.1  2003/06/24 09:29:33  lemmy
Enum more improved

Revision 1.1  2003/06/24 09:16:13  lemmy
better Enum added

*/