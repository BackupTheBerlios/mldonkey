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
 * EnumRating
 *
 * @version $Id: EnumRating.java,v 1.1 2003/11/10 08:35:13 lemmster Exp $ 
 *
 */
public class EnumRating extends Enum {
	
	public static EnumRating EXCELLENT = new EnumRating(1);

	public static EnumRating VERYHIGH = new EnumRating(2);

	public static EnumRating HIGH = new EnumRating(4);

	public static EnumRating NORMAL = new EnumRating(8);

	public static EnumRating LOW = new EnumRating(16);
	
	/**
	 * @param i
	 */
	private EnumRating(int i) {
		super(i);
	}
}

/*
$Log: EnumRating.java,v $
Revision 1.1  2003/11/10 08:35:13  lemmster
move getRating... into ResultInfo

*/