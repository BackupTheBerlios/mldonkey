/*
 * Created on 09.03.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.mldonkey.g2gui.view.systray;

import net.mldonkey.g2gui.view.MainWindow;


import org.eclipse.swt.widgets.Shell;


/**
 * @author joerg
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
class ToggleAction extends SystrayAction {
	
	private MainWindow parent;
	
	public ToggleAction(SystemTray tray) {
		super(tray);
		parent = tray.getParent();
		setText("toggle visibility");
		setChecked(parent.getShell().isVisible());
	}
	public void run() {
		Shell shell = parent.getShell();
		shell.setVisible(!shell.isVisible());
		setChecked(parent.getShell().isVisible());
		shell.setFocus();
	}

}