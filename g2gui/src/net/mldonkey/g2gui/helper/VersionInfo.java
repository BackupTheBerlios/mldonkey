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
 * @version $Id: VersionInfo.java,v 1.7 2004/03/29 16:44:15 dek Exp $ 
 *
 */
public class VersionInfo {
	/**
	 * @return Our version number
	 */
	public static String getVersion() {
		return "0.3.0-pre1";
	}
}

/*
$Log: VersionInfo.java,v $
Revision 1.7  2004/03/29 16:44:15  dek
	*** Release version 0.3.0-pre1 ***

Revision 1.6  2004/01/28 22:15:35  psy
* Properly handle disconnections from the core
* Fast inline-reconnect
* Ask for automatic relaunch if options have been changed which require it
* Improved the local core-controller

Revision 1.5  2003/12/04 08:47:28  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.4  2003/11/24 17:03:29  zet
*** empty log message ***

Revision 1.3  2003/11/11 17:02:58  zet
ver

Revision 1.2  2003/11/04 16:16:55  vnc
little version update

Revision 1.1  2003/10/09 09:59:19  lemmy
get the version number from the centralized class VersionInfo

*/