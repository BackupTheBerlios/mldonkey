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
 * EnumRating
 *
 * @version $Id: EnumRating.java,v 1.2 2003/11/10 08:54:47 lemmster Exp $ 
 *
 */
public class EnumRating extends Enum {
	private String string;
	
	public static EnumRating EXCELLENT = new EnumRating(1, G2GuiResources.getString("RTLP_EXCELLENT"));

	public static EnumRating VERYHIGH = new EnumRating(2, G2GuiResources.getString("RTLP_VERYHIGH"));

	public static EnumRating HIGH = new EnumRating(4, G2GuiResources.getString("RTLP_HIGH"));

	public static EnumRating NORMAL = new EnumRating(8, G2GuiResources.getString("RTLP_NORMAL"));

	public static EnumRating LOW = new EnumRating(16, G2GuiResources.getString("RTLP_LOW"));
	
	/**
	 * @param i
	 */
	private EnumRating(int i, String aString) {
		super(i);
		string = aString;
	}
	
	public String toString() {
		return string;
	}
}

/*
$Log: EnumRating.java,v $
Revision 1.2  2003/11/10 08:54:47  lemmster
rating filter in searchresult

Revision 1.1  2003/11/10 08:35:13  lemmster
move getRating... into ResultInfo

*/