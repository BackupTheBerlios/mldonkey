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
 * EnumPriority
 *
 *
 * @version $Id: EnumPriority.java,v 1.7 2003/12/04 08:47:29 lemmy Exp $ 
 *
 */
public class EnumPriority extends Enum {
	/**
	 * Very Low priority (value is -20)
	 */
	public static EnumPriority VERY_LOW = new EnumPriority( 1 );
	/**
	 * Low priority (value is -10)
	 */
	public static EnumPriority LOW = new EnumPriority( 2 );
	/**
	 * Normal priority (value is 0)
	 */
	public static EnumPriority NORMAL = new EnumPriority( 4 );
	/**
	 * High priority (value is 10)
	 */
	public static EnumPriority HIGH = new EnumPriority( 8 );
	/**
	 * Very High priority (value is 20)
	 */
	public static EnumPriority VERY_HIGH = new EnumPriority( 16 );
	/**
	 * @param i the int
	 */
	private EnumPriority( int anInt ) {
		super( anInt );
	}
}

/*
$Log: EnumPriority.java,v $
Revision 1.7  2003/12/04 08:47:29  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.6  2003/10/28 11:07:32  lemmy
move NetworkInfo.Enum -> enum.EnumNetwork
add MaskMatcher for "Enum[]"

Revision 1.5  2003/10/12 19:42:59  zet
very high & low priorities

Revision 1.4  2003/08/23 15:21:37  zet
remove @author

Revision 1.3  2003/08/22 21:04:27  lemmy
replace $user$ with $Author: lemmy $

Revision 1.2  2003/07/04 11:41:49  lemmy
bugfix

Revision 1.1  2003/07/03 21:12:03  lemmy
initial commit

*/