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

/**
 * EnumClientType
 *
 * @author markus
 * @version $Id: EnumClientType.java,v 1.1 2003/06/24 09:16:13 lemmstercvs01 Exp $ 
 *
 */
public class EnumClientType implements Enum {
	/**
	 * Source (value is 0)
	 */
	public static EnumClientType SOURCE = new EnumClientType( 0 );
	/**
	 * Friend (value is 1)
	 */
	public static EnumClientType FRIEND = new EnumClientType( 1 );
	/**
	 * Browsed (value is 2)
	 */
	public static EnumClientType BROWSED = new EnumClientType( 2 );
	
	/**
	 * Creates a new EnumClientType
	 * @param anInt The value
	 */
	private EnumClientType( int anInt ) {
	}
}

/*
$Log: EnumClientType.java,v $
Revision 1.1  2003/06/24 09:16:13  lemmstercvs01
better Enum added

*/