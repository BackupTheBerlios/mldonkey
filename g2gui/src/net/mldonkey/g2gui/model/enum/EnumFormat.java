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
 * EnumFormat
 *
 *
 * @version $Id: EnumFormat.java,v 1.5 2003/12/04 08:47:29 lemmy Exp $ 
 *
 */
public class EnumFormat extends Enum {
	/**
	 * unknow format (value is 0)
	 */
	public static EnumFormat UNKNOWN_FORMAT = new EnumFormat( 1 );
	/**
	 * generic format (value is 1)
	 */
	public static EnumFormat GENERIC_FORMAT = new EnumFormat( 2 );
	/**
	 * avi file (value is 2)
	 */
	public static EnumFormat AVI_FILE = new EnumFormat( 4 );
	/**
	 * mp3 file (value is 3)
	 */
	public static EnumFormat MP3_FILE = new EnumFormat( 8 );
	
	/**
	 * Creates a new EnumFormat
	 * @param anInt
	 */
	private EnumFormat( int anInt ) {
		super( anInt );
	}
}

/*
$Log: EnumFormat.java,v $
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