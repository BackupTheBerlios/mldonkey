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
package net.mldonkey.g2gui.view.helper;

import org.eclipse.swt.SWT;


/**
 * VersionCheck - Since fox returns "fox" to the getPlatform() method on all platforms
 *
 * @version $Id: VersionCheck.java,v 1.4 2004/02/05 20:44:43 psy Exp $
 *
 */
public class VersionCheck {
    /**
     * This method is used to determine the currently used SWT-platform.
     * @return Returns the SWT platform name. 
     * Examples: "win32", "win32-fox", "fox", "motif", "gtk", "photon", "carbon" 
     */
	public static String getSWTPlatform() {
        String platform;

        if ((platform = SWT.getPlatform()).equals("fox")) {
            if ((System.getProperty("os.name").length() > 7) &&
                    System.getProperty("os.name").substring(0, 7).equals("Windows"))
                return "win32-fox";
            else
                return "fox";
        } else
            return platform;
    }
    
    /**
     * This method is used to check if a ms-windows operating system is being used
     * @return Returns true if its a windows platform and false if not.
     */
	public static boolean isWin32 () {
    	if (getSWTPlatform().startsWith("win")) return true;
    	return false;
    }
	
	/**
	 * This method is used to check if a NON-ms-windows operating system is being used 
	 * @return Returns true if its a NON-windows platform and false if not.
	 */
	public static boolean isLinux() { 
		return !isWin32(); 
	}

	/**
	 * This method is used to check if the currently used widget-set is gtk
	 * @return Returns true if gtk is currently used, false if not
	 */
	public static boolean isGtk() {
		if (getSWTPlatform().startsWith("gtk")) return true;
		return false;
	}
}


/*
$Log: VersionCheck.java,v $
Revision 1.4  2004/02/05 20:44:43  psy
hopefully fixed dynamic column behaviour under gtk by introducing a
bogus column.

Revision 1.3  2004/01/08 21:48:33  psy
minor

Revision 1.2  2004/01/08 21:42:12  psy
introducing boolean isWin32()

Revision 1.1  2003/11/28 00:57:26  zet
initial

*/
