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
 * @version $Id: VersionCheck.java,v 1.7 2004/03/01 20:58:27 psy Exp $
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
    
	public static int getSWTVersion() {
		return SWT.getVersion();
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

	/**
	 * This method is used to check if the currently used widget-set is motif
	 * @return Returns true if motif is currently used, false if not
	 */
	public static boolean isMotif() {
		if (getSWTPlatform().startsWith("motif")) return true;
		return false;
	}
	
	/**
	 * This method gives us java.vendor, java.vm.name and java.version
	 * @return a combined String with java information
	 */
	public static String getJavaVersion() {
		return System.getProperty("java.vendor") + " " + System.getProperty("java.vm.name") + 
			" " + System.getProperty("java.version");
	}

	/**
	 * This method gives us os.name, os.arch and os.version
	 * @return a combined string with OS information
	 */
	public static String getOsVersion() {
		return System.getProperty("os.name") + " " + System.getProperty("os.arch") + 
		" " + System.getProperty("os.version");
	}

	/**
	 * This method gives us a bunch of OS/VM/SWT information
	 * @return a String which contains info about SWTplatform, SWTversion,
	 * JavaVersion and OsVersion 
	 */
	public static String getInfoString() {
		return getJavaVersion() + "\n" + getOsVersion() + ", " +
			getSWTPlatform() + "/" + getSWTVersion();
	}
}


/*
$Log: VersionCheck.java,v $
Revision 1.7  2004/03/01 20:58:27  psy
minor cosmetic change

Revision 1.6  2004/02/18 13:23:25  psy
added check for motif

Revision 1.5  2004/02/17 22:49:58  psy
added more VM/OS/Version debug- and crash-info

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
