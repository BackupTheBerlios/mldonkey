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
 * EnumQuery
 *
 *
 * @version $Id: EnumQuery.java,v 1.4 2003/08/23 15:21:37 zet Exp $ 
 *
 */
public class EnumQuery implements Enum {
	/**
	 * 0: And(x,y,...) (value is 0)
	 */
	public static EnumQuery AND = new EnumQuery(); 
	/**
	 * 1: Or(x,y,...) (value is 1)
	 */
	public static EnumQuery OR = new EnumQuery(); 
	/**
	 * 2: Andnot(x,y)  (value is 2)
	 */
	public static EnumQuery AND_NOT = new EnumQuery(); 
	/**
	 * 3: Module (name, query)  (value is 3)
	 */
	public static EnumQuery MODULE = new EnumQuery(); 
	/**
	 * 4: Keywords(comment, default) (value is 4)
	 */
	public static EnumQuery KEYWORDS = new EnumQuery(); 
	/**
	 * 5: Minsize(comment, default)  (value is 5)
	 */
	public static EnumQuery MINSIZE = new EnumQuery(); 
	/**
	 * 6: Maxsize(comment, default) (value is 6)
	 */
	public static EnumQuery MAXSIZE = new EnumQuery(); 
	/**
	 * 7: Format(comment, default)  (value is 7)
	 */
	public static EnumQuery FORMAT = new EnumQuery(); 
	/**
	 * 8: Media(comment, default)  (value is 8)
	 */
	public static EnumQuery MEDIA = new EnumQuery(); 
	/**
	 * 9: Mp3 Artist(comment, default) (value is 9)
	 */
	public static EnumQuery MP3_ARTIST = new EnumQuery(); 
	/**
	 * 10: Mp3 Title(comment, default) (value is 10)
	 */
	public static EnumQuery MP3_TITLE = new EnumQuery();
	/**
	 * 11: Mp3 Album(comment, default) (value is 11)
	 */
	public static EnumQuery MP3_ALBUM = new EnumQuery(); 
	/**
	 * 12: Mp3 Bitrate(comment, default)  (value is 12)
	 */
	public static EnumQuery MP3_BITRATE = new EnumQuery(); 
	/**
	 * 13: Hidden(fields) (value is 13)
	 */
	public static EnumQuery HIDDEN = new EnumQuery();

	/**
	 * Creates a new EnumQuery object
	 */	
	private EnumQuery() {
	}
	
	/**
	 * translate the EnumQuery back to byte
	 * @param enum The EnumQuery
	 * @return a byte represantation of this object
	 */
	public static byte getValue( EnumQuery enum ) {
		if ( enum.equals( EnumQuery.AND ) )
			return 0;
		else if ( enum.equals( EnumQuery.OR ) )
			return 1;
		else if ( enum.equals( EnumQuery.AND_NOT ) )
			return 2;
		else if ( enum.equals( EnumQuery.MODULE ) )
			return 3;
		else if ( enum.equals( EnumQuery.KEYWORDS ) )
			return 4;
		else if ( enum.equals( EnumQuery.MINSIZE ) )
			return 5;
		else if ( enum.equals( EnumQuery.MAXSIZE ) )
			return 6;
		else if ( enum.equals( EnumQuery.FORMAT ) )
			return 7;
		else if ( enum.equals( EnumQuery.MEDIA ) )
			return 8;
		else if ( enum.equals( EnumQuery.MP3_ARTIST ) )
			return 9;
		else if ( enum.equals(  EnumQuery.MP3_TITLE ) )
			return 10;
		else if ( enum.equals( EnumQuery.MP3_ALBUM ) )
			return 11;
		else if ( enum.equals( EnumQuery.MP3_BITRATE ) )
			return 12;
		else
			return 13;
	}
}

/*
$Log: EnumQuery.java,v $
Revision 1.4  2003/08/23 15:21:37  zet
remove @author

Revision 1.3  2003/08/22 21:04:27  lemmster
replace $user$ with $Author: zet $

Revision 1.2  2003/07/06 07:45:26  lemmstercvs01
checkstyle applied

Revision 1.1  2003/07/06 07:36:42  lemmstercvs01
EnumQuery added

*/