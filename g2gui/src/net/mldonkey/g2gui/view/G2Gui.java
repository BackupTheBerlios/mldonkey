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

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import net.mldonkey.g2gui.comm.Core;
import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.DisconnectListener;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.helper.ObjectPool;
import net.mldonkey.g2gui.helper.RegExp;
import net.mldonkey.g2gui.helper.SocketPool;
import net.mldonkey.g2gui.view.console.ExecConsole;
import net.mldonkey.g2gui.view.helper.Splash;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.pref.Preferences;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;


/**
 * Starts the whole thing
 *
 *
 * @version $Id: G2Gui.java,v 1.53 2003/12/03 22:19:11 lemmy Exp $
 *
 */
public class G2Gui {
	//TODO externalize strigns

	// this flag switches debugging output on/off
    public static boolean debug = false;
    
	// the mode of the gui
	private static boolean advancedMode = false;

	// our prefs
    private static Preferences myPrefs;

	// connection infos
    private static String hostname;
    private static String username;
    private static String password;
    private static int port;

	private static CoreCommunication core;

    private static Display display;
	private static Shell shell;
    private static ExecConsole execConsole = null;

    /**
     * Starts a new Core and launch the Gui
     * @param args Nothing to put inside
     */
    public static void main(String[] argv) {
    	String fileNotFound = "preference file not found on disk";
    	display = new Display();
        List links = new ArrayList();
        
		// parse args
		int optind;
		for (optind = 0; optind < argv.length; optind++) {
			if (argv[optind].startsWith("-h") || argv[optind].startsWith("--h")) {
				System.out.println(
				"Usage: g2gui [[-c path/to/pref] | [-l link] [-H host:port] [-U user] [-P passwd]]");
				System.exit(1);
			} else if (argv[optind].equals("-d")) {
				debug = true;
			} else if ( argv.length > optind + 1 )	{
				if (argv[optind].equals("-c")) {
					try {
						PreferenceLoader.initialize((String) argv[optind]);
					}
					catch (IOException e) {
						System.err.println(fileNotFound);
					} 
				} else if (argv[optind].equals("-l")) {
					links.add( argv[++optind] );
				} else if (argv[optind].equals("-H")) {
					String[] strings = RegExp.split(argv[++optind], ':');
					if ( strings.length == 2 ) {
						hostname = strings[0];
						port = port = new Integer( strings[1] ).intValue();
					}
				} else if (argv[optind].equals("-U")) {
					username = argv[++optind];
				} else if (argv[optind].equals("-P")) {
					password = argv[++optind];
				} else if (argv[optind].equals("--")) {
					optind++;
					break;
				}
			} else {
				break;
			}
		}
		
		// we received links? -> send them
		if ( links.size() > 0 ) {
			launch( links );
			return;
		}	
			
		G2GuiResources.initialize();
		try {
			PreferenceLoader.initialize();
		}
		catch (IOException e) {
			System.err.println(fileNotFound);
		}

        /*determine wether a new instance of G2gui is allowed: */
        if (PreferenceLoader.loadBoolean("allowMultipleInstances")
	      || !PreferenceLoader.loadBoolean("running") || debug) {
            launch();
        } else {
            MessageBox alreadyRunning = new MessageBox(new Shell(display),
                    SWT.YES | SWT.NO | SWT.ICON_ERROR);
            alreadyRunning.setText(G2GuiResources.getString("G2_MULTIPLE_WARNING_HEAD"));
            alreadyRunning.setMessage(G2GuiResources.getString("G2_MULTIPLE_WARNING_MESSAGE"));

            if (alreadyRunning.open() == SWT.YES) {
                launch();
            }
        }
    }

    /**
     * Launch
     */
    private static void launch() {
        /* we are running, so we make this available to other instances */
        PreferenceLoader.setValue("running", true);
        PreferenceLoader.saveStore();

        Object waiterObject = new Object();

        myPrefs = new Preferences(PreferenceLoader.getPreferenceStore());

        shell = new Shell(display);

        // should the core be spawned by the gui?
		boolean runCore = false;
		if (!PreferenceLoader.loadString("coreExecutable").equals(""))
			runCore = true;

		// create the splash
        if (PreferenceLoader.loadBoolean("splashScreen"))
            if ( runCore )
	            new Splash( display, 7 );
	        else
	         	new Splash( display, 5 );    

        // Needed on GTK to dispatch paintEvent.
        // SWT is idiotic.
        display.update();

		// launch the core if requested
		if (runCore) {
			Splash.increaseSplashBar("launching core");
	        spawnCore();
		}
			
		Splash.increaseSplashBar("initializing connection");

        /* if the gui isnt set up yet launch the preference window */
        if (!(PreferenceLoader.getBoolean("initialized"))) {
        	PreferenceLoader.setValue("initialized", true);
            Splash.setVisible(false);
            myPrefs.open(shell, null);

            // crashes if you call myPrefs.open again 
            // if you don't create a new Preferenes() - Why?
            // a jface.Wizard would be better
            // temporary hack
            myPrefs = new Preferences(PreferenceLoader.getPreferenceStore());
			Splash.setVisible(true);
        }

        readParams();

        Socket socket = initializeSocket( false );
		if (socket == null ) return;

        PreferenceLoader.saveStore();

        core = startCore( waiterObject, socket, false );
        
		// handle some errors the core can cause
		// connection denied
        if (core.getConnectionDenied()) {
            connectDeniedHandling();
 		// bad password
        } else if (core.getBadPassword()) {
            core.disconnect();
            badPasswordHandling();
        // connection is setup successfully
        } else {
			// launch the view
			Splash.increaseSplashBar("connection to core successfully created");
            new MainWindow(core, shell);
        }

        core.disconnect();

        /*
         * we are not running anymore, so we can allow another instance to
         * be started
         */
        PreferenceLoader.setValue("running", false);
        PreferenceLoader.saveStore();
    }
    
    /**
     * Launch to handle a link
     * @param aLink The link that should be send to the core
     */
    private static void launch( List aLink ) {
    	Object waiterObject = new Object();
    	
    	readParams();

    	Socket socket = initializeSocket( true );
    	if (socket == null ) return;

    	core = startCore( waiterObject, socket, true );
    	
    	// handle some errors the core can cause
    	// connection denied
    	if (core.getConnectionDenied()) {
    		connectDeniedHandling();
   		// bad password
    	} else if (core.getBadPassword()) {
    		core.disconnect();
    	// connection is setup successfully
    	} else {
    		sendDownloadLink( aLink, socket );
    	}
    }

    /**
	 * initialize the socket
	 */
    public static Socket initializeSocket( boolean mode ) {
		// create the socket connection to the core and handle the errors
        Socket aSocket = null;
    	try {
            ObjectPool socketPool = new SocketPool(hostname, port);
            aSocket = (Socket) socketPool.checkOut();
        } catch (UnknownHostException e) {
        	errorHandling(G2GuiResources.getString("G2_INVALID_ADDRESS"),G2GuiResources.getString("G2_ILLEGAL_ADDRESS"), mode);			
            return null;
        } catch (IOException e) {
			errorHandling(G2GuiResources.getString("G2_IOEXCEPTION"),
        			G2GuiResources.getString("G2_CORE_NOT_RUNNING") + hostname + ":" + port + 
        			G2GuiResources.getString("G2_CORE_NOT_RUNNING2"), mode);
			return null;
        }  
        return aSocket;  
	}

    /**
     * reads the prefs from the preferenceStore if its not read from the cmd line
     */
    private static void readParams() {
    	// read the value as long as no cmd line params were specified
    	if ( port == 0 )
    		port = PreferenceLoader.getInt("port");
    	if ( hostname == null )
    		hostname = PreferenceLoader.getString("hostname");
    	if ( username == null )
    		username = PreferenceLoader.getString("username");
    	if ( password == null )
    		password = PreferenceLoader.getString("password");
   		advancedMode = PreferenceLoader.getBoolean("advancedMode");
    }
    
    /**
     * Starts the core thread
     * @param waiterObject The waiterobj we synchronize with
     * @param aSocket The socket to communicate over
     * @param mode pull/push mode
     */
    private static CoreCommunication startCore( Object waiterObject, Socket aSocket, boolean mode ) {
    	/* wait as long till the core tells us to continue */
    	CoreCommunication aCore;
    	synchronized (waiterObject) {
    		Splash.increaseSplashBar("connecting to the core");
    		aCore = new Core(aSocket, username, password, waiterObject, mode, advancedMode);
    		aCore.connect();
    		Thread mldonkey = new Thread(aCore);
    		mldonkey.setDaemon(true);
    		mldonkey.start();
    		try {
    			waiterObject.wait();
    		} catch (InterruptedException e1) {
    			Thread.currentThread().interrupt();
    		}
    	}
    	aCore.addObserver( new DisconnectListener( aCore,shell ) );
    	
    	return aCore;
    }
    
	/**
     * Send a link - raw socket without a core
     */
    private static void sendDownloadLink( List aLink, Socket aSocket ) {
        for ( int i = 0; i < aLink.size(); i++ ) {
			EncodeMessage linkMessage = new EncodeMessage(Message.S_DLLINK, aLink.get(i));
		    try {
		        Message.writeStream(aSocket, linkMessage.getHeader(), linkMessage.getContent());
		    } 
		    catch (IOException e) {
		        e.printStackTrace();
		    }
		    linkMessage = null;
        }
    }

    /**
     * relaunch the main method with killing the old one
     */
    private static void relaunchSelf() {
        closeApp();
        launch();
    }

    /**
     * Raise an messagebox on badpassword exception
     * and send the password again
     *
     * @param args nothing to put inside
     */
    private static void badPasswordHandling() {
		Splash.dispose();

        /* raise a warning msg */
        MessageBox box = new MessageBox(shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
        box.setText(G2GuiResources.getString("G2_LOGIN_INVALID"));
        box.setMessage(G2GuiResources.getString("G2_USER_PASS"));

        int rc = box.open();

        if (rc == SWT.NO) {
            closeApp();
        } else {
            myPrefs.open(shell, null);
            relaunchSelf();
        }
    }

    /**
     * connectDeniedHandling
     */
    private static void connectDeniedHandling() {
        Splash.dispose();

        /* raise a warning msg */
        MessageBox box = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
        box.setText(G2GuiResources.getString("G2_CONNECTION_DENIED"));
        box.setMessage(G2GuiResources.getString("G2_CONNECTION_ATTEMPT_DENIED"));
        box.open();
        closeApp();
    }
    
    /**
     * 
     * @param errorText
     * @param errorMsg
     */
    private static void errorHandling(String errorText, String errorMsg, boolean mode) {
		if (!mode) {
			Splash.dispose();
			MessageBox box = new MessageBox(shell, SWT.ICON_ERROR | SWT.YES | SWT.NO);
			box.setText(errorText);
			box.setMessage(errorMsg);
	
			int rc = box.open();
	
			if (rc == SWT.NO) {
				closeApp();
			} else {
				myPrefs.open(shell, null);
				relaunchSelf();
			}
		} else {
			myPrefs = new Preferences(PreferenceLoader.getPreferenceStore());
			shell = new Shell();
			myPrefs.open(shell, null);
			relaunchSelf();
		}
    }

	/**
	 * spawn core
	 */
	private static void spawnCore() {
		File coreEXE = new File(PreferenceLoader.loadString("coreExecutable"));

		if ((execConsole == null) && coreEXE.exists() && coreEXE.isFile()) {
			execConsole = new ExecConsole();

			try {
				// wait while the core loads and opens the gui port? something better?
				Thread.sleep(7777);
			} catch (InterruptedException e) {
			}
		}
	}

    /**
     * closeApp
     */
    private static void closeApp() {
        shell.dispose();
    }

    /**
     * @return The parent CoreCommuncation obj
     */
    public static CoreCommunication getMldonkey() {
        return core;
    }

    /**
     * @return ExecConsole
     */
    public static ExecConsole getCoreConsole() {
        return execConsole;
    }
}


/*
$Log: G2Gui.java,v $
Revision 1.53  2003/12/03 22:19:11  lemmy
store g2gui.pref in ~/.g2gui/g2gui.pref instead of the program directory

Revision 1.52  2003/11/30 20:02:06  lemmster
check boundaries

Revision 1.51  2003/11/30 18:49:08  lemmster
better link handling, handle more than one link simultaneously

Revision 1.50  2003/11/30 18:14:55  dek
multiple instances are allowed if in debug-mode

Revision 1.49  2003/11/29 17:01:00  zet
update for mainWindow

Revision 1.48  2003/11/24 19:16:42  dek
fast-link processing enabled

Revision 1.47  2003/11/24 17:56:58  vnc
added --h[elp] param

Revision 1.46  2003/11/20 17:51:53  dek
moved disconnect-listener out of core

Revision 1.45  2003/11/20 15:56:02  dek
missed one small thing

Revision 1.44  2003/11/20 15:42:04  dek
reconnect started

Revision 1.43  2003/11/20 14:02:17  lemmster
G2Gui cleanup

Revision 1.42  2003/11/10 20:55:37  zet
display.close/dispose locks up fox

Revision 1.41  2003/11/08 23:53:46  zet
fox

Revision 1.40  2003/11/07 00:31:39  zet
option to disable splash screen  #1064

Revision 1.39  2003/10/19 16:39:36  zet
Centre splash on multi monitor displays

Revision 1.38  2003/10/08 18:11:35  zet
use display.update()

Revision 1.37  2003/09/28 21:38:23  dek
removed red cross for empty expression by replacing ";" with "{ }"

Revision 1.36  2003/09/28 18:04:46  zet
try while loop

Revision 1.35  2003/09/28 17:26:36  zet
Yes/No check on "load if running"...

Revision 1.34  2003/09/28 17:05:46  zet
hopefully display splashImage on GTK

Revision 1.33  2003/09/28 15:24:18  zet
remove println

Revision 1.32  2003/09/28 13:10:31  dek
Added Option, wether multiple Instances of G2Gui are allowed or not[bug #867]

Revision 1.31  2003/09/25 03:41:12  zet
-c <pref file>

Revision 1.30  2003/09/18 15:30:27  zet
centralize writeStream in core
handle IOException rather than throwing it away

Revision 1.29  2003/09/18 09:44:57  lemmster
checkstyle

Revision 1.28  2003/09/08 18:25:45  zet
init resources in g2gui

Revision 1.27  2003/09/03 15:50:39  zet
workaround for garbage windows gcc: java.io.IOException: GetFullPathName failed

Revision 1.26  2003/09/03 14:49:07  zet
optionally spawn core from gui

Revision 1.25  2003/08/24 16:37:04  zet
combine the preference stores

Revision 1.24  2003/08/23 15:21:37  zet
remove @author

Revision 1.23  2003/08/23 10:02:02  lemmster
use supertype where possible

Revision 1.22  2003/08/22 10:28:22  lemmster
catch wrong "allowed_ips" values (connection denied)

Revision 1.21  2003/08/20 14:26:19  dek
work on build-in-link handler, now sendig out poll-mode request,
not only creating it...

Revision 1.20  2003/08/20 11:51:52  dek
renamed pref.g2gui to pref.g2guiPref for not having 2 classes with same name

Revision 1.19  2003/08/19 21:44:35  zet
PreferenceLoader updates

Revision 1.18  2003/08/19 15:39:40  dek
changed some statements for build-in link-handling

Revision 1.17  2003/08/19 12:14:16  lemmster
first try of simple/advanced mode

Revision 1.16  2003/08/18 22:22:02  zet
attempt to fix "wait-forever" state, and loop on bad passwords..

Revision 1.15  2003/08/18 12:34:44  dek
now the initialized-stuff works again

Revision 1.14  2003/08/18 12:22:53  dek
g2gui-pref-page is now fully JFace-approved ;-)

Revision 1.13  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.12  2003/08/17 23:13:42  zet
centralize resources, move images

Revision 1.11  2003/08/17 17:30:20  zet
remove splashscreen from taskbar

Revision 1.10  2003/08/16 13:52:49  dek
ed2k-link handling-hack continued

Revision 1.9  2003/08/15 22:39:53  dek
ed2k-link handling-hack started

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
