/*
 * Created on 09.03.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.mldonkey.g2gui.view.systray;

import net.mldonkey.g2gui.view.MainWindow;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;



/**
 * @author joerg
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class MinimizeToTrayAction extends SystrayAction  {
	
	private MainWindow parent;
	private boolean toTray;
	
	public MinimizeToTrayAction(SystemTray tray) {
		super(tray);		
		setText("Minimize to Tray");
		toTray = PreferenceLoader.loadBoolean("minimizeToTray");
		setChecked(toTray);
	}
	public void run() {
		PreferenceLoader.setValue("minimizeToTray",!toTray);
	}

}
