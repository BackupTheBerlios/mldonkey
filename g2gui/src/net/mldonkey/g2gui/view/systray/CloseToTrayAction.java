/*
 * Created on 09.03.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.mldonkey.g2gui.view.systray;

import net.mldonkey.g2gui.view.pref.PreferenceLoader;

/**
 * @author joerg
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class CloseToTrayAction extends SystrayAction {

	private boolean toTray;

	/**
	 * @param tray
	 */
	protected CloseToTrayAction(SystemTray tray) {
		super(tray);
		setText("close to Tray");
		toTray = PreferenceLoader.loadBoolean("closeToTray");
		setChecked(toTray);
	}
	
	public void run() {
		PreferenceLoader.setValue("closeToTray",!toTray);
	}

}
