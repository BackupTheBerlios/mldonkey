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

/**
 * VersionInfo returns just a String with our current g2gui version number.
 *
 * @version $Id: VersionInfo.java,v 1.2 2003/11/04 16:16:55 vnc Exp $ 
 *
 */
public class VersionInfo {
	/**
	 * @return Our version number
	 */
	public static String getVersion() {
		return "0.1.1+ CVS";
	}
}

/*
$Log: VersionInfo.java,v $
Revision 1.2  2003/11/04 16:16:55  vnc
little version update

Revision 1.1  2003/10/09 09:59:19  lemmster
get the version number from the centralized class VersionInfo

*/