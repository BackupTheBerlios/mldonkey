/*
 * Copyright 2003
 * G2Gui Team
 * 
 * 
 * This file is part of G2Gui.
 *
 * G2Gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2Gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2Gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.view.main;

import net.mldonkey.g2gui.view.MainTab;

import org.eclipse.swt.SWT;

import org.eclipse.swt.program.Program;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * MenuBar
 *
 *
 * @version $Id: MainMenuBar.java,v 1.4 2003/08/26 09:46:41 dek Exp $ 
 *
 */
public class MainMenuBar {
	private MainTab mainTab;
	private Shell shell;
	private Menu mainMenuBar, submenu;
	private MenuItem mItem, item;

	public MainMenuBar( MainTab mainTab ) {
		this.mainTab = mainTab;
		this.shell = mainTab.getShell();
		this.createContent();
	}
	
	private void createContent() {
		mainMenuBar = new Menu( shell, SWT.BAR );
		shell.setMenuBar( mainMenuBar );
		
		mItem = new MenuItem ( mainMenuBar, SWT.CASCADE );
		mItem.setText ( "File" );
		submenu = new Menu ( shell, SWT.DROP_DOWN );
		mItem.setMenu ( submenu );
		item = new MenuItem ( submenu, 0 );
		item.addListener ( SWT.Selection, new Listener () {
			public void handleEvent ( Event e ) {
				shell.dispose();
			} 
		} );
		item.setText ( "E&xit\tCtrl+W" );
		item.setAccelerator ( SWT.CTRL + 'W' );
			
		mItem = new MenuItem ( mainMenuBar, SWT.CASCADE );
		mItem.setText ( "Tools" );
		
		submenu = new Menu( shell, SWT.DROP_DOWN );
		item = new MenuItem( submenu, 0 );
		item.addListener( SWT.Selection, new Listener() {
			public void handleEvent( Event event ) {	
				mainTab.openPreferences();
			}
		} );
		
		item.setText( "Preferences" );
		mItem.setMenu( submenu );
	
		mItem = new MenuItem ( mainMenuBar, SWT.CASCADE );
		mItem.setText ( "Help" );
		
		submenu = new Menu ( shell, SWT.DROP_DOWN );
		item = new MenuItem( submenu, 0 );
		item.addListener( SWT.Selection, new Listener() {
			public void handleEvent ( Event event ) {
				Program.launch( "https://developer.berlios.de/docman/?group_id=610" );
			}
		} );
		item.setText( "FAQ" );
		
		
		mItem.setMenu( submenu );
		item = new MenuItem( submenu, 0 );
		item.addListener( SWT.Selection, new Listener() {
			public void handleEvent ( Event event ) {
				Program.launch( "http://mldonkey.berlios.de/modules.php?name=Forums&file=viewforum&f=9" );
			}
		} );
		item.setText( "Feedback Forum" );
		
		mItem.setMenu( submenu );
		item = new MenuItem( submenu, 0 );
		item.addListener( SWT.Selection, new Listener() {
			public void handleEvent ( Event event ) {
				Program.launch( "https://developer.berlios.de/bugs/?group_id=610" );
			}
		} );
		item.setText( "Bugs" );
		

		item = new MenuItem( submenu, 0 );
		item.addListener( SWT.Selection, new Listener() {
			public void handleEvent( Event event ) {	
				About about = new About(shell);
				about.open();
			}
		} );
		
		item.setText( "About" );
		mItem.setMenu( submenu );
	}	
}

/*
$Log: MainMenuBar.java,v $
Revision 1.4  2003/08/26 09:46:41  dek
about-dialog is now child of mainShell, instead of creating its own..

Revision 1.3  2003/08/25 20:28:56  dek
first sketch of an about-dialog, feel free to extend ;-)

Revision 1.2  2003/08/23 15:21:37  zet
remove @author

Revision 1.1  2003/08/23 14:58:38  lemmster
cleanup of MainTab, transferTree.* broken

*/