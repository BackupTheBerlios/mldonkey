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

import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.helper.VersionInfo;
import net.mldonkey.g2gui.model.ClientStats;
import net.mldonkey.g2gui.view.MainWindow;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import com.gc.systray.SystemTrayIconListener;
import com.gc.systray.SystemTrayIconManager;

 /**
 * @version $Id: SystemTray.java,v 1.7 2004/03/10 10:31:44 dek Exp $ 
 *
 */
public class SystemTray implements SystemTrayIconListener, Runnable,Observer {

	private Menu menu;
	private MenuManager popupMenu;
	private MainWindow parent;
	private SystemTrayIconManager systemTrayManager;
	private static final DecimalFormat decimalFormat = new DecimalFormat( "0.#" );
	private String titleBarText;
	private int icon;
	
	/* the following attributes are a hack to "smoothen Tooltip-updates in tray#
	 * because tooltip is not updated but killed and redrawn if text changes
	 * maye a native-code hacker can change this ;-)
	 */
	private float[] uploadrate = new float[5];
	private float[] downloadrate = new float[5];
	private int counter=0;
	/** holds the status if the native library is loaded */
	private boolean libLoaded=false;

	/**
	 * SystemTrayIconListener implementation
	 */
	public void mouseClickedLeftButton( final int x, final int y, SystemTrayIconManager source) {
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
	public void mouseClickedRightButton( final int x, final int y, final SystemTrayIconManager source) {

		parent.getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				/* check for widget disposed */
				if (parent.getShell().isDisposed())
					return;
				
				if ( menu != null && menu.isVisible() ){
					menu.setVisible(false);
					return;
					}
				menu = popupMenu.createContextMenu(parent.getShell());
				menu.setLocation(x-2, y-2);
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
		tray.setDaemon(true);
		tray.run();
	}

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		// try to load the library
		try {
			System.loadLibrary("DesktopIndicator");
		} catch (UnsatisfiedLinkError e) {
			// thrown when the library is not found or can't be loaded
			libLoaded=false;
			return;
		} catch (SecurityException e) {
			// thrown because of security reasons, check policies
			libLoaded=false;
			return;
		}
		// the lib is loaded an ready to use
		libLoaded=true;
		parent.getCore().getClientStats().addObserver(this);
		System.out.println(getClass().getName() + "g2gui.ico");
		icon = SystemTrayIconManager.loadImage("g2gui.ico");
		if (icon == -1) {
			System.out.println("image icon.ico error");
			return;
		}

		systemTrayManager = new SystemTrayIconManager(icon, titleBarText);
		systemTrayManager.addSystemTrayIconListener(this);
		systemTrayManager.setVisible(true);

		IMenuListener manager = new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				manager.add(new Separator());
				manager.add(new CloseToTrayAction(SystemTray.this));
				manager.add(new MinimizeToTrayAction(SystemTray.this));
				manager.add(new ToggleAction(SystemTray.this));
				manager.add(new Separator());
				manager.add(new ExitAction(SystemTray.this));

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
	public MainWindow getParent() {
		return parent;
	}
	
	public void update( Observable arg0, Object receivedInfo ) {
		// check if the library was loaded
		if (!libLoaded) {
			// no, so there is nothing what we can update
			return;
		}
		ClientStats clientInfo = ( ClientStats ) receivedInfo;
		Shell shell = parent.getShell();
		
		if ( counter < uploadrate.length ){
			uploadrate[counter]=clientInfo.getTcpUpRate();
			downloadrate[counter]=clientInfo.getTcpDownRate();	
			counter++;
		}
		else {
		/* median transfer-Rates: */
			float medianUpRate=0;
			float medianDownRate=0;
			
			for (int i = 0; i < uploadrate.length; i++) {
				medianUpRate = medianUpRate+uploadrate[i];
				medianDownRate = medianDownRate+downloadrate[i];			
			}
			medianUpRate = medianUpRate / uploadrate.length;
			medianDownRate = medianDownRate / downloadrate.length;
			
			String transferRates =
					 "\nDL:" + decimalFormat.format(medianDownRate) + 
					 " / UL:" + decimalFormat.format(medianUpRate  );
			
			systemTrayManager.update(icon,titleBarText+transferRates);			
			counter=0;		
		}
					
			

	}
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */


}
/*
 $Log: SystemTray.java,v $
 Revision 1.7  2004/03/10 10:31:44  dek
 added header and footer

 */