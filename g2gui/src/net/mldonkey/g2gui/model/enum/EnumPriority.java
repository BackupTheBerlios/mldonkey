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
 * @author $Author: lemmster $
 * @version $Id: EnumPriority.java,v 1.3 2003/08/22 21:04:27 lemmster Exp $ 
 *
 */
public class EnumPriority implements Enum {
	/**
	 * Low priority (value is -10)
	 */
	public static EnumPriority LOW = new EnumPriority( -10 );
	/**
	 * Normal priority (value is 0)
	 */
	public static EnumPriority NORMAL = new EnumPriority( 0 );
	/**
	 * High priority (value is 10)
	 */
	public static EnumPriority HIGH = new EnumPriority( 10 );

	/**
	 * @param i the int
	 */
	private EnumPriority( int i ) {
	}
}

/*
$Log: EnumPriority.java,v $
Revision 1.3  2003/08/22 21:04:27  lemmster
replace $user$ with $Author$

Revision 1.2  2003/07/04 11:41:49  lemmstercvs01
bugfix

Revision 1.1  2003/07/03 21:12:03  lemmstercvs01
initial commit

*/