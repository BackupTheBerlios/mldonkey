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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * RegExp
 *
 * @version $Id: RegExp.java,v 1.3 2003/11/30 23:42:56 zet Exp $
 *
 */
public class RegExp {
	/**
	 * Decimal format for calcStringSize
	 */
	private static final DecimalFormat df = new DecimalFormat("0.#");

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

	/**
	 * creates a String from the size
	 * @param size The size
	 * @return a string represantation of this size
	 */
	public static String calcStringSize(long size) {
		float k = 1024f;
		float m = k * k;
		float g = m * k;
		float t = g * k;
		float fsize = size;

		if (fsize >= t) {
			return new String(df.format(fsize / t) + " TB");
		} else if (fsize >= g) {
			return new String(df.format(fsize / g) + " GB");
		} else if (fsize >= m) {
			return new String(df.format(fsize / m) + " MB");
		} else if (fsize >= k) {
			return new String(df.format(fsize / k) + " KB");
		} else {
			return new String(size + "");
		}
	}
	
	public static String calcStringOfSeconds(long inSeconds) {
			if (inSeconds < 60)
				return "0m";

			long days = inSeconds / 60 / 60 / 24;
			long rest = inSeconds - (days * 60 * 60 * 24);
			long hours = rest / 60 / 60;
			rest = rest - (hours * 60 * 60);

			long minutes = rest / 60;

			if (days > 9999)
				return "";

			if (days > 0)
				return "" + days + "d";

			if (hours > 0)
				return "" + hours + "h" + ((minutes > 0) ? (" " + minutes + "m") : "");

			return "" + minutes + "m";
		}
	
	
}


/*
$Log: RegExp.java,v $
Revision 1.3  2003/11/30 23:42:56  zet
updates for latest mldonkey cvs

Revision 1.2  2003/11/23 17:58:03  lemmster
removed dead/unused code

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
