/*
 * Copyright 2003 g2gui Team
 * 
 * 
 * This file is part of g2gui.
 * 
 * g2gui is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * g2gui is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * g2gui; if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA
 *  
 */
package net.mldonkey.g2gui.view.systray;

import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.helper.VersionInfo;
import net.mldonkey.g2gui.model.ClientStats;
import net.mldonkey.g2gui.view.MainWindow;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import com.gc.systray.SystemTrayIconListener;
import com.gc.systray.SystemTrayIconManager;

/**
 * @version $Id: SystemTray.java,v 1.16 2004/03/14 17:37:59 dek Exp $
 *  
 */
public class SystemTray implements SystemTrayIconListener, Observer, Runnable {

	private Menu menu;
	private MenuManager popupMenu;
	private MainWindow parent;
	private SystemTrayIconManager systemTrayManager;
	private static final DecimalFormat decimalFormat = new DecimalFormat("0.#");
	private String titleBarText;
	private int icon;

	/*
	 * the following attributes are a hack to "smoothen Tooltip-updates in
	 * tray# because tooltip is not updated but killed and redrawn if text
	 * changes maye a native-code hacker can change this ;-)
	 */
	private float[] uploadrate = new float[5];
	private float[] downloadrate = new float[5];
	private int counter = 0;

	/* holds the status if the native library is loaded */
	private boolean libLoaded = false;

	/* SystemTrayIconListener implementation */
	public void mouseClickedLeftButton(
		final int x,
		final int y,
		SystemTrayIconManager source) {
		parent.getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				Shell shell = parent.getShell();
				/* check for widget disposed */
				if (parent.getShell().isDisposed())
					return;
				shell.setVisible(!shell.isVisible());
				shell.setFocus();
				shell.forceActive();
				shell.setMinimized(false);
			}
		});

	}
	public void mouseClickedRightButton(
		final int x,
		final int y,
		final SystemTrayIconManager source) {

		parent.getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				/* check for widget disposed */
				if (parent.getShell().isDisposed())
					return;

				if (menu != null && menu.isVisible()) {
					menu.setVisible(false);
					return;
				}
				menu = popupMenu.createContextMenu(parent.getShell());
				menu.setLocation(x - 2, y - 2);

				menu.addMenuListener(new MenuAdapter() {
					public void menuShown(MenuEvent e) {
						/*
						 * set the default menue entry, this makes problems
						 * with motif if nothing in the table has been selected
						 */
						if (menu.getItem(0) != null)
							menu.setDefaultItem(menu.getItem(0));
					}
				});
				menu.setVisible(true);
			}
		});

	}
	public void mouseLeftDoubleClicked(
		final int x,
		final int y,
		SystemTrayIconManager source) {

	}
	public void mouseRightDoubleClicked(
		final int x,
		final int y,
		SystemTrayIconManager source) {

	}

	/**
	 * @param window
	 */
	public SystemTray(MainWindow window) {
		this.titleBarText = "g2gui v " + VersionInfo.getVersion();
		parent = window;
		Thread tray = new Thread(this);		
		tray.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
		libLoaded = SystemTrayIconManager.libLoaded;

		parent.getCore().getClientStats().addObserver(this);

		icon = G2GuiResources.getImageDescriptor("TrayIcon").createImage().handle;
		System.out.println(icon);
		
		systemTrayManager = new SystemTrayIconManager(icon, titleBarText);
		systemTrayManager.addSystemTrayIconListener(this);
		systemTrayManager.setVisible(true);

		IMenuListener manager = new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				String toggle = "";
				if (parent.getShell().isVisible())
					toggle = G2GuiResources.getString("TRAY_TOGGLE_HIDE");
				else
					toggle = G2GuiResources.getString("TRAY_TOGGLE_SHOW");
				manager.add(new ToggleAction(SystemTray.this, toggle));

				MenuManager settingsSubMenu =
					new MenuManager(G2GuiResources.getString("TRAY_SETTINGS"));
				settingsSubMenu.add(
					new CloseToTrayAction(
						SystemTray.this,
						G2GuiResources.getString("TRAY_CLOSE_TO")));
				settingsSubMenu.add(
					new MinimizeToTrayAction(
						SystemTray.this,
						G2GuiResources.getString("TRAY_MINIMIZE_TO")));
				manager.add(settingsSubMenu);
				
				

				manager.add(new Separator());

				manager.add(
					new ExitAction(
						SystemTray.this,
						G2GuiResources.getString("TRAY_EXIT")));

			}
		};
		popupMenu = new MenuManager("");
		popupMenu.setRemoveAllWhenShown(true);
		popupMenu.addMenuListener(manager);

		parent.getShell().addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				systemTrayManager.finalize();
			}
		});

	}
	/**
	 * @return Returns the parent.
	 */
	MainWindow getParent() {
		return parent;
	}

	public void update(Observable arg0, Object receivedInfo) {
		// check if the library was loaded
		if (!libLoaded) {
			// no, so there is nothing what we can update
			return;
		}
		ClientStats clientInfo = (ClientStats) receivedInfo;
		Shell shell = parent.getShell();

		if (counter < uploadrate.length) {
			uploadrate[counter] = clientInfo.getTcpUpRate();
			downloadrate[counter] = clientInfo.getTcpDownRate();
			counter++;
		} else {
			/* median transfer-Rates: */
			float medianUpRate = 0;
			float medianDownRate = 0;

			for (int i = 0; i < uploadrate.length; i++) {
				medianUpRate = medianUpRate + uploadrate[i];
				medianDownRate = medianDownRate + downloadrate[i];
			}
			medianUpRate = medianUpRate / uploadrate.length;
			medianDownRate = medianDownRate / downloadrate.length;

			final String transferRates =
				"\nDL:"
					+ decimalFormat.format(medianDownRate)
					+ " / UL:"
					+ decimalFormat.format(medianUpRate);

			if (parent.getShell().isDisposed())
				return;

			parent.getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					Shell shell = parent.getShell();
					/* check for widget disposed */
					if (parent.getShell().isDisposed())
						return;
					systemTrayManager.update(
						icon,
						titleBarText + transferRates);

				}
			});
			counter = 0;
		}

	}


}
/*
 $Log: SystemTray.java,v $
 Revision 1.16  2004/03/14 17:37:59  dek
 Systray reloaded

 Revision 1.15  2004/03/13 14:34:12  dek
 gui thread was not a good thing for gui [TM] it crashed with gcj

 Revision 1.14  2004/03/11 13:37:38  dek
 now icon is loaded from G2GUIRESSOURCES

 Revision 1.13  2004/03/11 13:16:14  dek
 lod-appendix looks now right

 Revision 1.12  2004/03/11 13:15:23  dek
 *** empty log message ***

 Revision 1.11 2004/03/11 13:11:43 dek 
 Submenu for traymenu + some OO
 
 Revision 1.10 2004/03/11 12:35:39 dek 
 exteranlized strings
 
 Revision 1.9 2004/03/10 11:08:29 dek 
 Systray s now running in gui thread
 
 Revision 1.8 2004/03/10 10:36:07 dek 
 javadoc comments -> normal comments
 
 Revision 1.7 2004/03/10 10:31:44 dek 
 added header and footer
  
*/