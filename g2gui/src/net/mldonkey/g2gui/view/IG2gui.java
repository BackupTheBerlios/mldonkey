/*
 * Copyright 2003
 * G2GUI Team
 * 
 * 
 * This file is part of G2GUI.
 *
 * G2GUI is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2GUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2GUI; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.view;

import net.mldonkey.g2gui.comm.CoreCommunication;

import org.eclipse.swt.widgets.Composite;

/**
 * IG2gui
 *
 * @author $user$
 * @version $Id: IG2gui.java,v 1.2 2003/06/25 18:04:53 dek Exp $ 
 *
 */
public interface IG2gui {
	
	/**
	 * 
	 * @param newTab The Tab to register
	 */
	
	void registerTab( G2guiTab newTab );
	
	/**
	 * @return the Row of the buttons
	 */
	Composite getButtonRow();

	/**
	 * @return the Content-Area
	 */
	Composite getPageContainer();
	
	CoreCommunication getCore();

	/**
	 * @param tab I want to be active!!!!
	 */
	void setActive( G2guiTab tab );
}



/*
$Log: IG2gui.java,v $
Revision 1.2  2003/06/25 18:04:53  dek
Console-Tab reworked

Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.2  2003/06/24 19:18:45  dek
checkstyle apllied

Revision 1.1  2003/06/24 18:25:43  dek
working on main gui, without any connection to mldonkey atm, but the princip works
test with:
public class guitest{
	public static void main(String[] args) {
	Gui g2gui = new Gui(null);
}

*/