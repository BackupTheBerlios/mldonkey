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
 * EnumTagType
 *
 *
 * @version $Id: EnumTagType.java,v 1.7 2004/04/19 17:34:41 dek Exp $ 
 *
 */
public class EnumTagType extends Enum {
	/**
	 * display an int
	 */
	public static EnumTagType INT = new EnumTagType( 1<<0 );
	/**
	 * string (value is 0)
	 */	
	public static EnumTagType	STRING = new EnumTagType( 1<<1 );
	/**
	 * bool (value is 1)
	 */
	public static EnumTagType BOOL = new EnumTagType( 1<<2 );
	/**
	 * file (value is 2)
	 */
	public static EnumTagType FILE = new EnumTagType( 1<<3 );
	/**
	 * display an ip list
	 */
	public static EnumTagType IP_LIST = new EnumTagType( 1<<4 );
	/**
	 * display an ip
	 */
	public static EnumTagType IP = new EnumTagType( 1<<5 );
	/**
	 * display an address
	 */
	public static EnumTagType ADDR = new EnumTagType( 1<<6 );
	/**
	 * display a float
	 */
	public static EnumTagType FLOAT = new EnumTagType( 1<<7 );
	/**
	 * display a md4
	 */
	public static EnumTagType MD4 = new EnumTagType( 1<<8 );
	/**
	 * display a sha1
	 */
	public static EnumTagType SHA1 = new EnumTagType( 1<<9 );
	/**
	 * display an int64
	 */
	public static EnumTagType INT64= new EnumTagType( 1<<10 );
	/**
	 * Creates a new EnumTagType
	 * @param anInt The value
	 */
	private EnumTagType( int anInt ) {
		super( anInt );
	}
}

/*
$Log: EnumTagType.java,v $
Revision 1.7  2004/04/19 17:34:41  dek
New EnumTagType: Int64 and some debugging info for wrong opcode-interpretation

Revision 1.6  2003/12/04 08:47:29  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.5  2003/10/28 11:07:32  lemmy
move NetworkInfo.Enum -> enum.EnumNetwork
add MaskMatcher for "Enum[]"

Revision 1.4  2003/08/23 15:21:37  zet
remove @author

Revision 1.3  2003/08/22 21:04:27  lemmy
replace $user$ with $Author: dek $

Revision 1.2  2003/08/02 09:27:39  lemmy
added support for proto > 16

Revision 1.1  2003/06/24 09:29:33  lemmy
Enum more improved

Revision 1.1  2003/06/24 09:16:13  lemmy
better Enum added

*/