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
 * @version $Id: VersionCheck.java,v 1.1 2003/11/28 00:57:26 zet Exp $
 *
 */
public class VersionCheck {
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
    
}


/*
$Log: VersionCheck.java,v $
Revision 1.1  2003/11/28 00:57:26  zet
initial

*/
