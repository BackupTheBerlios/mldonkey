/*
 * Copyright 2003 g2gui Team
 * 
 * 
 * This file is part of g2gui.
 * 
 * g2gui is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * g2gui is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * g2gui; if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA
 *  
 */
package net.mldonkey.g2gui.view.systray;

import net.mldonkey.g2gui.view.resource.G2GuiResources;

/**
 * @version $Id: ToggleAction.java,v 1.6 2004/03/14 17:37:59 dek Exp $
 *  
 */
class ToggleAction extends SystrayAction {

	public ToggleAction(SystemTray tray, String title) {
		super(tray, title);
		if (shell.isVisible())
			setImageDescriptor(G2GuiResources.getImageDescriptor("maximize"));
		else
			setImageDescriptor(G2GuiResources.getImageDescriptor("restore"));

	}

	public void run() {
		shell.setVisible(!shell.isVisible());
		shell.setFocus();
		shell.forceActive();
		shell.setMinimized(false);
	}

}
/*
$Log: ToggleAction.java,v $
Revision 1.6  2004/03/14 17:37:59  dek
Systray reloaded


 Revision 1.5 2004/03/11 13:11:43 dek
 Submenu for traymenu + some OO

 Revision 1.4 2004/03/11 12:35:39 dek externalized strings
 
 Revision 1.3 2004/03/10 10:31:44 dek added header and footer
  
 */