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

import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.view.G2Gui;
import net.mldonkey.g2gui.view.MainWindow;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * MenuBar
 *
 * @version $Id: MainMenuBar.java,v 1.20 2003/12/04 08:47:31 lemmy Exp $ 
 *
 */
public class MainMenuBar {
	private MainWindow mainTab;
	private Shell shell;
	private Menu mainMenuBar, subMenu;
	private MenuItem menuItem;

	/**
	 * @param mainTab 
	 */
	public MainMenuBar( MainWindow mainTab ) {
		this.mainTab = mainTab;
		this.shell = mainTab.getShell();
		this.createContent();
	}
	
	private void createContent() {
		mainMenuBar = new Menu( shell, SWT.BAR );
		shell.setMenuBar( mainMenuBar );
		
		// File
		menuItem = new MenuItem ( mainMenuBar, SWT.CASCADE );
		menuItem.setText ( "File" );
		final Menu fileMenu = new Menu ( shell, SWT.DROP_DOWN );
		menuItem.setMenu ( fileMenu );

		// Build the fileMenu dynamically 
		fileMenu.addMenuListener( new MenuListener() {
			public void menuHidden( MenuEvent e ) { }
			public void menuShown( MenuEvent e ) {
				MenuItem [] menuItems = fileMenu.getItems ();
				for ( int i = 0; i < menuItems.length; i++ ) {
					menuItems[ i ].dispose();
				}
				
				// File>Kill core if connected && gui has not spawned the core
				if ( mainTab.getCore().isConnected()
					&& G2Gui.getCoreConsole() == null ) {
				
					menuItem = new MenuItem ( fileMenu, SWT.PUSH );
					menuItem.setText ( "&Kill core" );
					menuItem.setImage( G2GuiResources.getImage( "cancel" ) );
					menuItem.addListener ( SWT.Selection, new Listener () {
						public void handleEvent ( Event e ) {
							
							MessageBox confirm =
								new MessageBox( 
									shell,
									SWT.YES | SWT.NO | SWT.ICON_QUESTION );
		
							confirm.setMessage( G2GuiResources.getString( "MISC_AYS" ) );
				
							if ( confirm.open() == SWT.YES ) {
								Message killCore = new EncodeMessage( Message.S_KILL_CORE );
								killCore.sendMessage( mainTab.getCore() );
							}
						} 
					} );
					
				}
				
				// File>Separator
				menuItem = new MenuItem( fileMenu, SWT.SEPARATOR );
				
				// File>Exit
				menuItem = new MenuItem ( fileMenu, SWT.PUSH );
				menuItem.setText ( "E&xit\tCtrl+W" );
				menuItem.setImage( G2GuiResources.getImage( "X" ) );
				menuItem.setAccelerator ( SWT.CTRL + 'W' );
				menuItem.addListener ( SWT.Selection, new Listener () {
					public void handleEvent ( Event e ) {
						mainTab.getMinimizer().forceClose();
						shell.close();
					} 
				} );
				
				
			}
		} );

		// Tools			
		menuItem = new MenuItem ( mainMenuBar, SWT.CASCADE );
		menuItem.setText ( "Tools" );
		subMenu = new Menu( shell, SWT.DROP_DOWN );
		menuItem.setMenu( subMenu );
		
			// Tools>Preferences
			menuItem = new MenuItem( subMenu, SWT.PUSH );
			menuItem.setImage( G2GuiResources.getImage( "PreferencesButtonSmall" ) );
			menuItem.setText( "Preferences" );
			menuItem.addListener( SWT.Selection, new Listener() {
				public void handleEvent( Event event ) {	
					mainTab.openPreferences();
				}
			} );
			
	
		// Help
		menuItem = new MenuItem ( mainMenuBar, SWT.CASCADE );
		menuItem.setText ( "Help" );
		subMenu = new Menu ( shell, SWT.DROP_DOWN );
		menuItem.setMenu( subMenu );
		
			// Help>Homepage
			menuItem = new MenuItem( subMenu, SWT.PUSH );
			menuItem.setText( "Homepage" );
			menuItem.setImage( G2GuiResources.getImage( "globe" ) );
			menuItem.addListener( SWT.Selection, 
				new URLListener( "http://developer.berlios.de/projects/mldonkey/" )
			);
		
		
			// Help>Feedback
			menuItem = new MenuItem( subMenu, SWT.PUSH );
			menuItem.setText( "Feedback Forum" );
			menuItem.setImage( G2GuiResources.getImage( "globe" ) );
			menuItem.addListener( SWT.Selection, 
				new URLListener( "http://mldonkey.berlios.de/modules.php?name=Forums&file=viewforum&f=9" )
			);
			
			// Help>Bugs
			menuItem = new MenuItem( subMenu, SWT.PUSH );
			menuItem.setText( "Bugs/Suggestions" );
			menuItem.setImage( G2GuiResources.getImage( "globe" ) );
			menuItem.addListener( SWT.Selection, 
				new URLListener( "http://developer.berlios.de/bugs/?group_id=610" )
			);
			
			
			// Help>FAQ
			menuItem = new MenuItem( subMenu, SWT.PUSH );
			menuItem.setText( "FAQ" );
			menuItem.setImage( G2GuiResources.getImage( "globe" ) );
			menuItem.addListener( SWT.Selection, 
				new URLListener( 
					"http://openfacts.berlios.de/index-en.phtml?title=MLdonkey_G2gui" )
			);
			
			// Help>Separator
			menuItem = new MenuItem( subMenu, SWT.SEPARATOR );
	
			// Help>About
			menuItem = new MenuItem( subMenu, SWT.PUSH );
			menuItem.setText( "About" );
			menuItem.setImage( G2GuiResources.getImage( "commit_question" ) );
			menuItem.addListener( SWT.Selection, new Listener() {
				public void handleEvent( Event event ) {	
					About about = new About( shell );
					about.open();
				}
			} );
			
	}	
	
	/**
	 * Small listener class to launch URLs
	 */
	public class URLListener implements Listener {
		private String url;
		
		public URLListener( String url ) {
			this.url = url;
		}
		
		public void handleEvent( Event event ) {
			Program.launch( url );
		}
	}
	
}

/*
$Log: MainMenuBar.java,v $
Revision 1.20  2003/12/04 08:47:31  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.19  2003/11/29 17:01:09  zet
update for mainWindow

Revision 1.18  2003/11/07 17:47:46  zet
remove duplicate pref images

Revision 1.17  2003/10/15 18:24:44  zet
icons

Revision 1.16  2003/10/13 18:36:17  zet
fix file->exit (w/tray) bug

Revision 1.15  2003/09/26 14:17:00  zet
update faq url

Revision 1.14  2003/09/23 01:06:51  zet
add Homepage

Revision 1.13  2003/09/20 01:41:12  zet
*** empty log message ***

Revision 1.12  2003/09/19 03:38:53  zet
confirm kill

Revision 1.11  2003/09/18 15:29:46  zet
centralize writeStream in core
handle IOException rather than throwing it away

Revision 1.10  2003/09/18 10:12:53  lemmy
checkstyle

Revision 1.9  2003/09/16 15:12:07  zet
*** empty log message ***

Revision 1.8  2003/09/16 15:11:01  zet
build filemenu dynamically

Revision 1.7  2003/09/16 14:02:52  zet
revert

Revision 1.5  2003/09/16 01:15:43  zet
kill core command

Revision 1.4  2003/08/26 09:46:41  dek
about-dialog is now child of mainShell, instead of creating its own..

Revision 1.3  2003/08/25 20:28:56  dek
first sketch of an about-dialog, feel free to extend ;-)

Revision 1.2  2003/08/23 15:21:37  zet
remove @author

Revision 1.1  2003/08/23 14:58:38  lemmy
cleanup of MainTab, transferTree.* broken

*/