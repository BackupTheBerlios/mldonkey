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
package net.mldonkey.g2gui.view.systray;

import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;



 /**
 * @version $Id: ExitAction.java,v 1.2 2004/03/10 10:31:44 dek Exp $ 
 *
 */
class ExitAction extends SystrayAction {	
	
	public ExitAction(SystemTray tray) {
		super(tray);
		PreferenceLoader.setValue("forceClose",false);
		setText("Exit");
		setImageDescriptor(G2GuiResources.getImageDescriptor("X"));
	}
	
	
	public void run() {
		PreferenceLoader.setValue("forceClose",true);
		parent.getMinimizer().forceClose();
		shell.close();
	}

}
/*
 $Log: ExitAction.java,v $
 Revision 1.2  2004/03/10 10:31:44  dek
 added header and footer

 */