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

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;

 /**
 * @version $Id: SystrayAction.java,v 1.2 2004/03/10 10:31:44 dek Exp $ 
 *
 */
public abstract class SystrayAction extends Action {
	
	
	protected MainWindow parent;
	protected Shell shell;
	
	protected SystrayAction(SystemTray tray) {
		super();
		parent = tray.getParent();
		shell = parent.getShell();
	}


}
/*
 $Log: SystrayAction.java,v $
 Revision 1.2  2004/03/10 10:31:44  dek
 added header and footer

 */