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
 * Enum
 *
 *
 * @version $Id: Enum.java,v 1.6 2003/10/31 07:24:01 zet Exp $
 *
 */
public abstract class Enum {
    /**
     * The intern value of the enum, be sure to never change it!
     */
    protected int field;

    // no instanciation
    protected Enum(int i) {
        field = i;
    }

    /**
     * MaskMatcher Holds a bitmask of a collection of <code>Enum</code>s.
     * You can verify that you Enum is in the collection with matches(Enum anEnum).
     */
    public static class MaskMatcher {
        /**
         * Counting the number of enums
         */
        private int enumCounter = 0;

        /**
         * Representing the mask of the Enum. (2^x for each Enum)
         */
        private int enumMask;

        /**
         * Adds an new <code>Enum</code> to the EnumMask
           * @param enum
         */
        public void add(Enum enum) {
            if (!matches(enum)) {
                this.enumMask += enum.field;
                this.enumCounter++;
            }
        }

        /**
         * Removes an <code>Enum</code> from the EnumMask
          * @param enum
         */
        public void remove(Enum enum) {
            if (matches(enum)) {
                this.enumMask -= enum.field;
                this.enumCounter--;
            }
        }

        /**
         * @return The number of Enums in this obj
         */
        public int count() {
            return this.enumCounter;
        }

        /**
         * Does the supported <code>Enum<code> belongs to our Enum collection
         * @param enum The Enum to use
         * @return true for match
         */
        public boolean matches(Enum enum) {
            int result = this.enumMask & enum.field;

            return result != 0;
        }
    }
}


/*
$Log: Enum.java,v $
Revision 1.6  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.5  2003/10/29 16:54:42  lemmster
use int instead of List for internal counter

Revision 1.4  2003/10/28 11:07:32  lemmster
move NetworkInfo.Enum -> enum.EnumNetwork
add MaskMatcher for "Enum[]"

Revision 1.3  2003/08/23 15:21:37  zet
remove @author

Revision 1.2  2003/08/22 21:04:27  lemmster
replace $user$ with $Author: zet $

Revision 1.1  2003/06/24 09:29:33  lemmstercvs01
Enum more improved

Revision 1.1  2003/06/24 09:16:13  lemmstercvs01
better Enum added

*/
