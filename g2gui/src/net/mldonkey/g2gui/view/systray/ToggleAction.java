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

import net.mldonkey.g2gui.view.MainWindow;




 /**
 * @version $Id: ToggleAction.java,v 1.3 2004/03/10 10:31:44 dek Exp $ 
 *
 */
class ToggleAction extends SystrayAction {
	
	private MainWindow parent;
	private boolean isVisible=true;
	
	public ToggleAction(SystemTray tray) {
		super(tray);
		isVisible=shell.isVisible();		
		setText("toggle visibility");
		setChecked(isVisible);
	}
	public void run() {		
		shell.setVisible(!isVisible);		
		shell.setFocus();
		shell.forceActive();
		shell.setMinimized(false);
	}

}
/*
 $Log: ToggleAction.java,v $
 Revision 1.3  2004/03/10 10:31:44  dek
 added header and footer

 */