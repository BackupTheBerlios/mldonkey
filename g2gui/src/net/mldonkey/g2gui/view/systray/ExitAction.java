/*
 * Created on 09.03.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.mldonkey.g2gui.view.systray;

import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;



/**
 * @author joerg
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
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