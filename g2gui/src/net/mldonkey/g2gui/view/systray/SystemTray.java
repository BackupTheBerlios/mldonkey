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


import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.helper.RegExp;
import net.mldonkey.g2gui.helper.VersionInfo;
import net.mldonkey.g2gui.model.ClientStats;
import net.mldonkey.g2gui.view.MainWindow;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TrayItem;


/**
 * @version $Id: SystemTray.java,v 1.21 2004/04/02 17:25:46 dek Exp $
 *  
 */
public class SystemTray implements Listener, Observer, Runnable {


	private Menu menu;
	private MainWindow parent;
	private MenuManager popupMenu;
	private String titleBarText;

	private TrayItem systray;

	/**
	 * @param window
	 */
	public SystemTray(MainWindow window) {
		this.titleBarText = "g2gui v " + VersionInfo.getVersion();
		parent = window;
		run();
	}
	/**
	 * @return Returns the parent.
	 */
	MainWindow getParent() {
		return parent;
	}
	
	public void handleEvent(Event event) {
		Point cursorLocation = Display.getCurrent().getCursorLocation();
		
		switch (event.type) {
			case SWT.Selection :
				mouseClickedLeftButton(cursorLocation.x,cursorLocation.y);
				
				break;
			case SWT.MenuDetect:
				mouseClickedRightButton(cursorLocation.x,cursorLocation.y);
				break;

			default :
				break;
		}
	}

	public void mouseClickedLeftButton(final int x, final int y) {
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
	public void mouseClickedRightButton( final int x, final int y) {

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
						 * set the default menue entry, this makes problems with motif if nothing
						 * in the table has been selected
						 */
						if (menu.getItem(0) != null)
							menu.setDefaultItem(menu.getItem(0));
					}
				});
				menu.setVisible(true);
			}
		});

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
		/* 
		 * for this implemetation, one needs SWT from 3.0M8
		 * i.e. swt version 3044
		 */
		systray = new TrayItem(parent.getShell().getDisplay().getSystemTray(),SWT.NONE);		
		
		parent.getCore().getClientStats().addObserver(this);
		
		systray.setText("Text");
		systray.setToolTipText("Tooltip");
		
		/*
		 * this adds a Listener for right Mouse-clicks,
		 * I discovered this while browsing SWT-sources, this is not
		 * mentioned in the API, or did i just miss it?
		 * the second line is standard-left-click-listener
		 */
		systray.addListener(SWT.MenuDetect,this);
		systray.addListener(SWT.Selection,this);	
		
		systray.setImage( G2GuiResources.getImageDescriptor("TrayIcon").createImage());
		parent.getShell().addDisposeListener(new DisposeListener(){
			public void widgetDisposed(DisposeEvent e) {
				systray.dispose();
			}});
		
		
				
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
	}

	public void update(Observable arg0, Object receivedInfo) {		

		ClientStats clientStats = (ClientStats) receivedInfo;
		Shell shell = parent.getShell();
		final String transferRates =
			"\nDL:"
				+ RegExp.getDecimalFormat(clientStats.getTcpDownRate())
				+ " / UL:"
				+ RegExp.getDecimalFormat(clientStats.getTcpUpRate());

		if (parent.getShell().isDisposed())
			return;

		parent.getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				Shell shell = parent.getShell();
				/* check for widget disposed */
				if (parent.getShell().isDisposed())
					return;
				systray.setToolTipText(titleBarText + transferRates);
			}
		});

	}

	


}
/*
 $Log: SystemTray.java,v $
 Revision 1.21  2004/04/02 17:25:46  dek
 introduced SWTs Tray-support, now we need swt from M08, i.e. version 3044

 Revision 1.20  2004/03/19 16:53:26  dek
 Makefile magic for win-only feature

 Revision 1.19  2004/03/19 14:16:03  dek
 *** empty log message ***

 Revision 1.18  2004/03/14 20:54:02  dek
 *** empty log message ***

 Revision 1.17  2004/03/14 19:43:56  dek
 *** empty log message ***

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