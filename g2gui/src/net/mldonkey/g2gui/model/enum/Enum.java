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

import java.util.ArrayList;
import java.util.List;

/**
 * Enum
 *
 *
 * @version $Id: Enum.java,v 1.4 2003/10/28 11:07:32 lemmster Exp $ 
 *
 */
public abstract class Enum {
	/**
	 * The intern value of the enum, be sure to never change it!
	 */
	protected int field;

	// no instanciation
	protected Enum( int i ) {
		field = i;
	}

	/**
	 * MaskMatcher Holds a bitmask of a collection of <code>Enum</code>s. 
	 * You can verify that you Enum is in the collection with matches(Enum anEnum).
	 */
	public static class MaskMatcher {
		/**
		 * Containing the Enums. 
		 */
		private List enumList;
		/**
		 * Representing the mask of the Enum. (2^x for each Enum)
		 */
		private int enumMask;
		/**
		 * Creates a new MaskMatcher obj
		 */		
		public MaskMatcher() {
			this.enumList = new ArrayList();
		}
		/**
		 * Adds an new <code>Enum</code> to the EnumMask
  		 * @param enum
		 */
		public boolean add( Enum enum ) {
			if ( !this.enumList.contains( enum ) ) {
				this.enumList.add( enum );
				this.enumMask += enum.field;
				return true;				
			}
			return false;
		}
		/**
		 * Removes an <code>Enum</code> from the EnumMask
 		 * @param enum
		 */
		public boolean remove( Enum enum ) {
			if ( this.enumList.contains( enum ) ) {
				this.enumList.remove( enum );
				this.enumMask -= enum.field;
				return true;				
			}
			return false;
		}
		/**
		 * @return The number of Enums in this obj
		 */
		public int count() {
			return this.enumList.size();
		}
		/**
		 * Does the supported <code>Enum<code> belongs to our Enum collection
		 * @param enum The Enum to use
		 * @return true for match
		 */
		public boolean matches( Enum enum ) {
			int result = this.enumMask & enum.field;
			if ( result != 0 )
				return true;
			return false;
		}
	}
}

/*
$Log: Enum.java,v $
Revision 1.4  2003/10/28 11:07:32  lemmster
move NetworkInfo.Enum -> enum.EnumNetwork
add MaskMatcher for "Enum[]"

Revision 1.3  2003/08/23 15:21:37  zet
remove @author

Revision 1.2  2003/08/22 21:04:27  lemmster
replace $user$ with $Author: lemmster $

Revision 1.1  2003/06/24 09:29:33  lemmstercvs01
Enum more improved

Revision 1.1  2003/06/24 09:16:13  lemmstercvs01
better Enum added

*/