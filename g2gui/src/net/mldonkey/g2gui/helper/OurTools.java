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
package net.mldonkey.g2gui.helper;

import java.util.ArrayList;
import java.util.List;

import gnu.regexp.RE;
import gnu.regexp.REException;
import gnu.regexp.REMatch;

/**
 * MyTools
 *
 * @version $Id: OurTools.java,v 1.1 2003/09/27 10:43:13 lemmster Exp $ 
 *
 */
public class OurTools {

	public static String[] split( String aString, char delimiter ) {
			RE regex = null;
			String expression = "([^" + delimiter + "])*";		
			try {
				regex = new RE( expression );
			} catch ( REException e ) {
				e.printStackTrace();
			}
			List patterns = new ArrayList();		
			REMatch[] matches = regex.getAllMatches( aString );
			for ( int i = 0; i < matches.length; i++ ) {
				String match = matches[ i ].toString();			
				if ( !match.equals( "" ) )
					patterns.add( match );
			}
			Object[] temp = patterns.toArray();
			String[] result = new String[ temp.length ];
			for ( int i = 0; i < temp.length; i++ ) {
				result[ i ] = ( String ) temp[ i ];
			}
			return result;
		}
}

/*
$Log: OurTools.java,v $
Revision 1.1  2003/09/27 10:43:13  lemmster
OurTools added for static helper methods

*/