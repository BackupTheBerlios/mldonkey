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
 * EnumType
 *
 *
 * @version $Id: EnumType.java,v 1.5 2003/12/04 08:47:29 lemmy Exp $ 
 *
 */
public class EnumType extends Enum {

	/**
	 * unsigned int (value is 0)
	 */
	public static EnumType UNSIGNED_INT = new EnumType( 1 );
	/**
	 * signed int (value is 1)
	 */
	public static EnumType SIGNED_INT = new EnumType( 2 );
	/**
	 * string (value is 2)
	 */
	public static EnumType STRING = new EnumType( 4 );
	/**
	 * ip address (value is 3)
	 */
	public static EnumType IPADDRESS = new EnumType( 8 );
	
	/**
	 * Creates a new EnumType
	 * @param anInt The value
	 */
	private EnumType( int anInt ) {
		super( anInt );
	}
}

/*
$Log: EnumType.java,v $
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