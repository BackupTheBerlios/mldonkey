/*
 * Copyright 2003
 * g2gui Team
 *
 *
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package net.mldonkey.g2gui.helper;

import gnu.regexp.RE;
import gnu.regexp.REException;
import gnu.regexp.REMatch;

import java.util.ArrayList;
import java.util.List;


/**
 * RegExp
 *
 * @version $Id: RegExp.java,v 1.1 2003/10/31 07:24:01 zet Exp $
 *
 */
public class RegExp {
    /**
     * @param aString
     * @param delimiter
     * @return String[]
     */
    public static String[] split(String aString, char delimiter) {
        RE regex = null;
        String expression = "([^" + delimiter + "])*";

        try {
            regex = new RE(expression);
        } catch (REException e) {
            e.printStackTrace();
        }

        List stringList = new ArrayList();
        REMatch[] matches = regex.getAllMatches(aString);

        for (int i = 0; i < matches.length; i++) {
            String match = matches[ i ].toString();

            if (!match.equals("")) {
                stringList.add(match);
            }
        }

        String[] resultArray = new String[ stringList.size() ];
        stringList.toArray(resultArray);

        return resultArray;
    }

    /**
     * @param input
     * @param toBeReplaced
     * @param replaceWith
     * @return String
     */
    public static String replaceAll(String input, String toBeReplaced, String replaceWith) {
        RE regex = null;

        try {
            regex = new RE(toBeReplaced);
        } catch (REException e) {
            e.printStackTrace();
        }

        return regex.substituteAll(input, replaceWith);
    }
}


/*
$Log: RegExp.java,v $
Revision 1.1  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

*/
