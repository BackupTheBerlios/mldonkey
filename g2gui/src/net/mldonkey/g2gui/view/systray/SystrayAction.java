/*
 * Created on 09.03.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.mldonkey.g2gui.view.systray;

import net.mldonkey.g2gui.view.MainWindow;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;

/**
 * @author joerg
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
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
