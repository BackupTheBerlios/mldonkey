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



 /**
 * @version $Id: ExitAction.java,v 1.4 2004/03/11 13:11:43 dek Exp $ 
 *
 */
class ExitAction extends SystrayAction {	
	
	public ExitAction(SystemTray tray,String title) {
		super(tray, title);			
		//setImageDescriptor(G2GuiResources.getImageDescriptor("X"));
	}
	
	
	public void run() {		
		parent.getMinimizer().forceClose();
		shell.close();
	}

}
/*
 $Log: ExitAction.java,v $
 Revision 1.4  2004/03/11 13:11:43  dek
 Submenu for traymenu + some OO

 Revision 1.3  2004/03/11 12:35:39  dek
 exteranlized strings

 Revision 1.2  2004/03/10 10:31:44  dek
 added header and footer

 */