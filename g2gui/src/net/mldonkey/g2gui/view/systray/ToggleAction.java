/*
 * Created on 09.03.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.mldonkey.g2gui.view.systray;

import net.mldonkey.g2gui.view.MainWindow;




/**
 * @author joerg
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
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