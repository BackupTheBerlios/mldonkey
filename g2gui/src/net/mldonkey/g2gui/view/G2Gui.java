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
 * ( at your option ) any later version.
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
package net.mldonkey.g2gui.view;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import net.mldonkey.g2gui.comm.Core;
import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.ObjectPool;
import net.mldonkey.g2gui.helper.SocketPool;
import net.mldonkey.g2gui.view.pref.Preferences;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

/**
 * Starts the hole thing
 *
 * @author $user$
 * @version $Id: G2Gui.java,v 1.8 2003/08/07 13:25:37 lemmstercvs01 Exp $ 
 *
 */
public class G2Gui {
	private static ResourceBundle res = ResourceBundle.getBundle( "g2gui" );
	private static Process p;
	private static Thread mldonkey;
	private static Object waiterObject;
	private static ObjectPool socketPool;
	private static CoreCommunication core;
	private static PreferenceStore preferenceStore;
	private static Preferences myPrefs;
	private static String aString, hostname, username, password;
	private static int port;
	private static int[] count;
	private static MessageBox box;
	private static Display display;
	private static Shell shell, splashShell;
	private static ProgressBar progressBar;
	private static FormLayout formLayout;
	private static FormData formData;
	private static Rectangle shellRect, displayRect;
	private static Label label;
	private static Image image;
	
	/**
	 * Starts a new Core and launch the Gui
	 * @param args Nothing to put inside
	 */
	public static void main( String[] args ) {
		/* initializing */
		display = new Display();
		shell = new Shell( display );
		splashShell = new Shell( display, SWT.ON_TOP );
		preferenceStore = new PreferenceStore( "g2gui.pref" );
		myPrefs = new Preferences( preferenceStore );
		waiterObject = new Object();
		box = new MessageBox( shell, SWT.ICON_ERROR | SWT.OK );
		progressBar = new ProgressBar( splashShell, SWT.NONE );
		count = new int[] { 3 };

		/* build the splash */
		progressBar.setMaximum( count[0] );
		image = new Image( display, "icons/splash.png" );
		label = new Label( splashShell, SWT.NONE );
		label.setImage( image );
		FormLayout layout = new FormLayout();
		splashShell.setLayout( layout );
		formData = new FormData();
		formData.left = new FormAttachment( 0, 5 );
		formData.right = new FormAttachment( 100, -5 );
		formData.bottom = new FormAttachment( 100, -5 );
		progressBar.setLayoutData( formData );
		splashShell.pack();
		shellRect = splashShell.getBounds();
		displayRect = display.getBounds();
		int x = ( displayRect.width - shellRect.width ) / 2;
		int y = ( displayRect.height - shellRect.height ) / 2;
		splashShell.setLocation( x, y );
		splashShell.open(); //TODO disabled for developing

		increaseBar( "Starting the model" );

		/* load the preferences */
		try {
			myPrefs.initialize( preferenceStore );
		}
		catch ( IOException e ) { }		
	
		/* if the gui isnt set up yet launch the preference window */
		if ( !( preferenceStore.getBoolean( "initialized" ) ) ) {					
			splashShell.setVisible( false );
			myPrefs.open( shell, null );
			splashShell.setVisible( true );
		}
		
		port = preferenceStore.getInt( "port" );
		hostname = preferenceStore.getString( "hostname" );
		username = preferenceStore.getString( "username" );
		password = preferenceStore.getString( "password" );
		
		/* create the socket connection to the core */
		Socket socket = null;
		try {
			socketPool = new SocketPool( hostname, port );
			socket = ( Socket ) socketPool.checkOut();
		}
		catch ( UnknownHostException e ) {
			splashShell.dispose();
			image.dispose();
			box.setText( res.getString( "G2_INVALID_ADDRESS") );
			box.setMessage( res.getString( "G2_ILLEGAL_ADDRESS" ) );
			box.open();
			myPrefs.open( shell, null );
			relaunchSelf();
		}
		catch ( IOException e ) {
			splashShell.dispose();
			image.dispose();
			box.setText( res.getString( "G2_IOEXCEPTION") );
			box.setMessage( res.getString( "G2_CORE_NOT_RUNNING" ) );
			box.open();
			myPrefs.open( shell, null );
			relaunchSelf();
		}
		
		/* launch the model */
		core = new Core( socket, username, password, waiterObject );
		core.connect();
		mldonkey = new Thread( core );
		mldonkey.setDaemon( true );
		mldonkey.start();
		
		while ( true ) {
			/* wait as long as the core tells us to continue */
			synchronized ( waiterObject ) {
				try {
					waiterObject.wait();
				}
				catch ( InterruptedException e1 ) { }
			}
			/* did the core receive "bad password" */
			if ( core.getBadPassword() ) {
				badPasswordHandling();
			}
			/* user/pass was valid, leave the loop */
			else
				break;
		}
		
		increaseBar( "Starting the view" ); 
		
		/* launch the view */	
		MainTab g2gui = new MainTab( core, shell );
		core.disconnect();
	}
	
	/**
	 * relaunch the main method with killing the old one
	 */
	private static void relaunchSelf() {
		shell.dispose();
		display.dispose();
		G2Gui.main( null );
	}
	
	
	/**
	 * Raise an messagebox on badpassword exception
	 * and send the password again
	 */
	public static void badPasswordHandling() {
		splashShell.dispose();
		image.dispose();
		/* raise a warning msg */
		box = new MessageBox( shell, SWT.ICON_WARNING | SWT.OK );
		box.setText( res.getString( "G2_LOGIN_INVALID" ) );
		box.setMessage( res.getString( "G2_USER_PASS" ) );
		box.open();
		/* dont launch the gui but launch the password box */
		myPrefs.open( shell, null );
		/* send the password again */
		relaunchSelf();
	}

	/**
	 * Increse the ProgressBar and updates the label text
	 * @param aString The new label text
	 */	
	static void increaseBar( final String aString ) {
		display.syncExec( new Runnable() {
			public void run() {
				int selection = progressBar.getSelection();
				progressBar.setSelection( selection + 1 );
			}	
		} );
	}

	/**
	 * @return The splashShell image
	 */
	public static Image getImage() {
		return image;
	}
	/**
	 * @return The splashShell
	 */
	public static Shell getSplashShell() {
		return splashShell;
	}
	/**
	 * @return The parent CoreCommuncation obj
	 */
	public static CoreCommunication getMldonkey() {
		return core;
	}
}

/*
$Log: G2Gui.java,v $
Revision 1.8  2003/08/07 13:25:37  lemmstercvs01
ResourceBundle added

Revision 1.7  2003/08/06 17:41:20  lemmstercvs01
some fixes

Revision 1.6  2003/08/04 14:38:55  lemmstercvs01
splashscreen and error handling added (resending on badpassword doenst work atm)

Revision 1.5  2003/08/03 19:09:51  lemmstercvs01
better error handling

Revision 1.4  2003/08/01 17:21:19  lemmstercvs01
reworked observer/observable design, added multiversion support

Revision 1.3  2003/07/18 04:34:22  lemmstercvs01
checkstyle applied

Revision 1.2  2003/07/17 15:10:35  lemmstercvs01
foobar

Revision 1.1  2003/07/17 14:58:37  lemmstercvs01
refactored

Revision 1.9  2003/07/13 12:50:01  dek
now the socket is closed, when application is exited

Revision 1.8  2003/07/02 17:06:29  dek
Checkstyle, JavaDocs still have to be added

Revision 1.7  2003/06/27 11:23:48  dek
gui is no more children of applicationwindow

Revision 1.6  2003/06/27 10:39:37  lemmstercvs01
core is now Runnable

Revision 1.5  2003/06/26 21:11:10  dek
speed is shown

Revision 1.4  2003/06/26 12:04:59  dek
pref-dialog accessible in main-window

Revision 1.3  2003/06/25 18:04:53  dek
Console-Tab reworked

Revision 1.2  2003/06/25 10:42:36  dek
peferenences dialog at first start

Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.1  2003/06/24 20:13:14  lemmstercvs01
initial commit

*/