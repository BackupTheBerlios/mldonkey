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
 * EnumClientMode
 *
 *
 * @version $Id: EnumClientMode.java,v 1.3 2003/08/23 15:21:37 zet Exp $ 
 *
 */
public class EnumClientMode implements Enum {
	/**
	 * direct (value is 0)
	 */
	public static EnumClientMode DIRECT = new EnumClientMode( 0 );
	/**
	 * firewalled (value is 1)
	 */
	public static EnumClientMode FIREWALLED = new EnumClientMode( 1 );
	
	/**
	 * Creates a new EnumClientMode
	 * @param anInt The value
	 */
	private EnumClientMode( int anInt ) {
	}
}

/*
$Log: EnumClientMode.java,v $
Revision 1.3  2003/08/23 15:21:37  zet
remove @author

Revision 1.2  2003/08/22 21:04:27  lemmster
replace $user$ with $Author: zet $

Revision 1.1  2003/06/24 09:29:33  lemmstercvs01
Enum more improved

Revision 1.1  2003/06/24 09:16:13  lemmstercvs01
better Enum added

*/