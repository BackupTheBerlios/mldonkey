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
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import net.mldonkey.g2gui.model.ModelFactory;
import net.mldonkey.g2gui.comm.Core;
import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.DisconnectListener;
import net.mldonkey.g2gui.helper.ObjectPool;
import net.mldonkey.g2gui.helper.RegExp;
import net.mldonkey.g2gui.helper.SocketPool;
import net.mldonkey.g2gui.helper.VersionInfo;
import net.mldonkey.g2gui.helper.DownloadSubmit;
import net.mldonkey.g2gui.view.console.ExecConsole;
import net.mldonkey.g2gui.view.helper.Splash;
import net.mldonkey.g2gui.view.helper.VersionCheck;
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
 * @version $Id: G2Gui.java,v 1.72 2004/03/14 17:37:59 dek Exp $
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
    // IP and port for serving a .torrent
    private static String submitHost;
    private static int submitPort;

    // list of links for submission
    private static List links;
    
    private static boolean runLocalCore = false;
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
    	String configfile = null;
    	
    	/*
    	 * without this sleep(0) gcj crashes right after start,
    	 * nobody knows why
    	 */
    	try { Thread.sleep(0); } catch (InterruptedException e1) {}
    	
    	display = new Display();
        links = new ArrayList();
        
		// parse args
		int optind;
		for (optind = 0; optind < argv.length; optind++) {
			// give additional verbosity/debug
			if (argv[optind].equals("-d") | argv[optind].equals("-v") )
				debug = true;
			// need help?
			if ( argv[optind].startsWith("--help") ) {
				printCommandlineHelp();
				System.exit(1);
			// ed2k/http link for submission
			} else if (argv[optind].toLowerCase().startsWith("ed2k://") | 
					argv[optind].toLowerCase().startsWith("http://")) {
				links.add( argv[optind] );
			// local (torrent) files
			} else if (new File(argv[optind]).exists()) {
				System.out.println("Adding a torrent to list");
				links.add( argv[optind] );
			// parameter with a value
			} else if ( argv.length > optind + 1 )	{
				// configfile-parameter
				if (argv[optind].equals("-c")) {
					try {
						configfile = (String) argv[++optind];
						PreferenceLoader.initialize( configfile );
						G2GuiResources.initialize();
					}
					catch (IOException e) {
						System.err.println(fileNotFound + ": " + configfile);
					} 
				// a link parameter for submission [deprecated]
				} else if (argv[optind].equals("-l")) {
					links.add( argv[++optind] );
				// hostname and optionally port
				} else if (argv[optind].equals("-h")) {
					String[] strings = RegExp.split(argv[++optind], ':');
					if ( strings.length == 2 ) {
						hostname = strings[0];
						port = new Integer( strings[1] ).intValue();
					} else if (strings.length == 1) {
						hostname = strings[0];
						port = 4001;
					}
				// torrentServer IP and optionally port
				} else if (argv[optind].equals("-s")) {
					System.out.println("We got a -s parameter!");
					String[] strings = RegExp.split(argv[++optind], ':');
					if ( strings.length == 2 ) {
						submitHost = strings[0];
						submitPort = new Integer( strings[1] ).intValue();
					} else if (strings.length == 1) {
						submitHost = strings[0];
					}
				// username
				} else if (argv[optind].equals("-u")) {
					username = argv[++optind];
				// password
				} else if (argv[optind].equals("-p")) {
					password = argv[++optind];
				// ? simply ignore
				} else if (argv[optind].equals("--")) {
					optind++;
					break;
				}
			} else {
				break;
			}
		}

		/* display generic VM and OS information */
		if (debug) System.out.println( VersionCheck.getInfoString() );
		
		// if there has not been specified a config-file, use default
		if (configfile == null) {
			G2GuiResources.initialize();
			try {
				PreferenceLoader.initialize();
			}
			catch (IOException e) {
				System.err.println(fileNotFound);
			}
		}
	
		// we received links? -> send them
		if ( links.size() > 0) {
			launch( links );
			return;
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
     * This method prints out out commandline-help, parameters, version and examples
     *
     */
    private static void printCommandlineHelp() {
    	if (debug) System.out.println( VersionCheck.getInfoString() );
    	System.out.println(
    		"G2gui " + VersionInfo.getVersion() + "\n" +
			"Usage: g2gui [params] [links] [.torrent-files]\n" +
    		"\n" + 
    		"   --help	this help\n" +
			"   -v		increase verbosiveness / debug output\n" +
			"   -c		use config file\n" +
			"   -h		host[:port]\n" +
			"   -u		username\n" +
			"   -p		password\n" +
			"   -s		host[:port]  (.torrent-submit host override)\n" +
			"\n" +
			"Examples:\n" +
    		"g2gui ed2k://...\n" +
    		"g2gui /tmp/file.torrent\n" +
			"g2gui -c /home/user/g2gui/g2gui.pref ed2k://...\n" + 
    		"g2gui -h server:4001 -u user -p password http://link/to/file.torrent\n" + 
			"g2gui -s 192.168.0.1:8000 /tmp/file.torrent\n\n" +
			"Config-file values and defaults are overridden by -h, -u, -p and -s\n" + 
			"Multiple links and torrent-files are possible.");
    }
    
    
    /**
     * Launch
     */
    private static void launch() {
        // create our main shell for displaying stuff
    	shell = new Shell(display);
    	//if (getCoreConsole() != null) getCoreConsole().setShell(shell);
    	
    	/* we are running, so we make this available to other instances */
        PreferenceLoader.setValue("running", true);
        PreferenceLoader.saveStore();
 
        myPrefs = new Preferences(PreferenceLoader.getPreferenceStore());

        // should the core be spawned by the gui?
        // if yes, set hostname to localhost
		runLocalCore = false;
		if (!PreferenceLoader.loadString("coreExecutable").equals("")) {
			if (debug) System.out.println("A coreExecutable has been given");
			runLocalCore = true;
			PreferenceLoader.setValue("hostname", "localhost");
		}
        
        // create the splash
        if (PreferenceLoader.loadBoolean("splashScreen"))
            if ( runLocalCore )
	            new Splash( display, 7 );
	        else
	         	new Splash( display, 5 );    

        // Needed on GTK to dispatch paintEvent.
        // SWT is idiotic.
        display.update();

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
			display.update();
        }
        
        // read parameters from our preferencestore
        readParams();
        if (debug) System.out.println("Host: " + hostname + ":" + port + " User: " + username);

		// launch the core if requested
		if (runLocalCore && !probeHost("localhost", 4001) ) {
			Splash.increaseSplashBar("launching core");
	        spawnCore();
		} else if (runLocalCore) {
			Splash.increaseSplashBar("using running core");
			if (debug) System.out.println("Using existing local core");
		}
		Splash.increaseSplashBar("initializing connection");


        // we're almost done with restarting, so set it to false
		PreferenceLoader.setRelaunching(false);
        PreferenceLoader.saveStore();
        
        if ( connectCore(false) ) {
			// launch the view
			Splash.increaseSplashBar("connection to core successfully created");
            new MainWindow(core, shell);
            // we're done - disconnect from the core
            core.disconnect();
        }

        /*
         * we are not running anymore, so we can allow another instance to
         * be started
         */
        PreferenceLoader.setValue("running", false);

        /* save our preferences */
        PreferenceLoader.saveStore();
        if (debug) System.out.println("G2gui shut down.");
    }
    
    /**
     * Launch to handle a link
     * @param aLink The link that should be send to the core
     */
    private static void launch( List aLink ) {

    	myPrefs = new Preferences(PreferenceLoader.getPreferenceStore());
    	shell = new Shell(display);
    	
    	readParams();
    	if (debug) System.out.println("Links for submission: " + links.toString());
    	if (debug) System.out.println("Host: " + hostname + " User: " + username);
    	
    	// handle some errors the core can cause
    	// connection denied
    	if ( connectCore(true) ) {
    		System.out.println("Connection fine, sending links!");
    		new DownloadSubmit( aLink, core, submitHost, submitPort );
    	}
    }

    /**
     * This method tries to establish a connection to the mldonkey core
     * @param mode true is pull-mode, false is push-mode
     * @return true if the connection and login is successful, false if not
     */
    private static boolean connectCore(boolean mode) {
        Object waiterObject = new Object();

        Socket socket = initializeSocket( false );
		if (socket == null ) return false;

        core = startCore( waiterObject, socket, mode );
        
		// handle some errors the core can cause
		// connection denied
        if (core.getConnectionDenied()) {
        	core.disconnect();
        	connectDeniedHandling();
 		// bad password
        } else if (core.getBadPassword()) {
            core.disconnect();
            badPasswordHandling();
        // connection is setup successfully
        } else {
        	return true;
        }
        return false;
    }
    
    /**
     * This method returns the local core-handler status
     * @return true if we want to manage the local core, false if not
     */
    public static boolean runLocalCore() {
    	return runLocalCore;
    }
    
    /**
	 * initialize the socket (push or poll mode)
	 */
    public static Socket initializeSocket( boolean mode ) {
		// create the socket connection to the core and handle the errors
        Socket aSocket = null;
    	try {
    		aSocket = initializeSocket();
    	} catch (UnknownHostException e) {
        	errorHandling(G2GuiResources.getString("G2_INVALID_ADDRESS"), 
        		G2GuiResources.getString("G2_ILLEGAL_ADDRESS"), mode, true);			
            return null;
        } catch (IOException e) {
        	errorHandling(G2GuiResources.getString("G2_IOEXCEPTION"),
        		G2GuiResources.getString("G2_CORE_NOT_RUNNING") + hostname + ":" + port + 
        		G2GuiResources.getString("G2_CORE_NOT_RUNNING2"), mode, true);
			return null;
        }  
        return aSocket;  
	}

    /**
     * Initialize the Socket 
     * @return A socket obj
     * @throws UnknownHostException
     * @throws IOException
     */
    public static Socket initializeSocket() throws UnknownHostException, IOException {
   		ObjectPool socketPool = new SocketPool(hostname, port);
   		return (Socket) socketPool.checkOut();
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
     * @param mode: true is pull-mode, false is push-mode
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
    		mldonkey.setName("CoreCommunication");
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
     * relaunch the launch method with killing the old one
     */
    public static void relaunchSelf() {
    	if (getCoreConsole() != null) { 
    		getCoreConsole().dispose();
    		execConsole = null;
    	}

    	shell.dispose();
    	PreferenceLoader.cleanUp();
    	
    	/* reset the factory to get rid of old lists and data */
    	ModelFactory.reset();
    	
    	/* reset connection values, to take them from the PreferenceStore later */
    	hostname = null;
        username = null;
        password = null;
        port = 0;

        try {
    		PreferenceLoader.initialize();
    	}
    	catch (IOException e) {
    		System.err.println("PreferenceLoader could not be initialized: " + e);
    	}
 
        /* when we wanted to submit a link, do not forget it */
        if ( links.size() > 0 ) {
        	launch( links );
        } else {
        	launch();
        }
       
    }

    /**
     * Raise an messagebox on badpassword exception
     * and send the password again
     *
     * @param args nothing to put inside
     */
    private static void badPasswordHandling() {
    	Splash.dispose();
    	if (debug) System.out.println("Error: Bad password!");

    	/* raise a warning message */
     	errorHandling(G2GuiResources.getString("G2_LOGIN_INVALID"),
    			G2GuiResources.getString("G2_USER_PASS"),
    			false, false);
    }

    /**
     * connectDeniedHandling
     */
    private static void connectDeniedHandling() {
        Splash.dispose();
        if (debug) System.out.println("Error: Connection to core has been denied!");
        
        /* raise a warning message */
        errorHandling(G2GuiResources.getString("G2_CONNECTION_DENIED"), 
        		G2GuiResources.getString("G2_CONNECTION_ATTEMPT_DENIED"),
        		false, false);
    }
    
    /**
     * This Method pops up a small message-window containing an error-message.
     * 
     * @param errorText little text which appears in the error-window headerbar
     * @param errorMsg text which gives more detailed information to the problem
     * @param mode
     * @param severe When set to true, SWT.ICON_WARNING will be used instead of SWT.ICON_ERROR,
     * resulting in a more intentensive "warning experience".
     * 
     */
    private static void errorHandling(String errorText, String errorMsg, boolean mode, boolean severe) {
    	if (!mode) {
			Splash.dispose();
			int icon = SWT.ICON_WARNING;
			if (severe) icon = SWT.ICON_ERROR;
			MessageBox box = new MessageBox(shell, icon | SWT.OK | SWT.CANCEL);
			box.setText(errorText);
			box.setMessage(G2GuiResources.getString("G2_CONNECTION_ERROR_HEADER") + " " + 
					getConnectionString() + "\n\n" + errorMsg);
	
			int rc = box.open();
			if (rc == SWT.CANCEL) {
				closeApp();
			} else if ( myPrefs.open(shell, null) == 0 ) {
				relaunchSelf();
			}
		} else {
			myPrefs = new Preferences(PreferenceLoader.getPreferenceStore());
			shell = new Shell();
			if ( myPrefs.open(shell, null) == 0 ) {
				relaunchSelf();
			}
		}
    }

	/**
	 * spawnCore() is used to spawn a local mlnet core and wait until
	 * a connection to localhost:4001 is possible (i.e. until the core is ready for use).
	 * It tries to connect the freshly spawned core once a second, up to 20 times, 
	 * then gives up
	 */
	private static void spawnCore() {
		File coreEXE = new File(PreferenceLoader.loadString("coreExecutable"));

		if ((execConsole == null) && coreEXE.exists() && coreEXE.isFile()) {
			execConsole = new ExecConsole();
			if (debug) System.out.println("Starting local core");
			
			/* try 20 times with 1 sec delays to connect the core */ 
			int counter = 0;
			boolean connected = false;
			while (!connected & counter++ < 20) {
				/* try to establish a connection to the core-port */
				connected = probeHost("localhost", 4001);
			}
			if (debug) {
				if (connected) System.out.println("Local core successfully started.");
			else System.out.println("Local core failed to start!");
			}
		}
	}

	/**
	 * This method checks if someone is listening on a certain host:port
	 * @param hostname the hostname which should be probed
	 * @param port the port which should be probed
	 * @return true is probe was successful
	 */
	private static boolean probeHost(String hostname, int port) {
		boolean connected = false;
		try {
			Socket socket = new Socket(InetAddress.getByName(hostname), port);
			connected = true;
			/* close the socket again directly after successful connect */
			try {
				socket.close();
			} catch (IOException e1) {
				if (debug) System.out.println("While closing probesocket: " + e1);
			}
		/* if no core has been found, we catch the exception and sleep 1 second */
		} catch (IOException e2) {
			System.out.println("trying...");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e3) {
				if (debug) System.out.println("Interrupted sleep: " + e3);
			}
		}
		return connected;
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

    /**
     * returns a human-readable text of the connection info
     * @return String in the format username@hostname:port
     */
    public static String getConnectionString() {
    	return username + "@" + hostname + ":" + port;
    }
}


/*
$Log: G2Gui.java,v $
Revision 1.72  2004/03/14 17:37:59  dek
Systray reloaded

Revision 1.71  2004/03/04 21:15:32  dek
yet another fix for gcj

Revision 1.70  2004/03/02 23:39:29  psy
replaced raw-socket link-submission

Revision 1.69  2004/03/01 21:00:31  psy
* Moved linksubmitter from G2gui.java to DownloadSubmit.java
* added .torrent http-server

Revision 1.68  2004/02/29 20:35:03  psy
simple link hex-decoding

Revision 1.67  2004/02/29 17:36:31  psy
improved local core handling

Revision 1.66  2004/02/17 22:49:58  psy
added more VM/OS/Version debug- and crash-info

Revision 1.65  2004/02/05 20:44:44  psy
hopefully fixed dynamic column behaviour under gtk by introducing a
bogus column.

Revision 1.64  2004/01/29 08:25:04  lemmy
removed duplicated code

Revision 1.63  2004/01/28 22:15:34  psy
* Properly handle disconnections from the core
* Fast inline-reconnect
* Ask for automatic relaunch if options have been changed which require it
* Improved the local core-controller

Revision 1.62  2004/01/23 22:12:46  psy
reconnection and local core probing improved, continuing work...

Revision 1.61  2004/01/13 22:57:25  psy
*** empty log message ***

Revision 1.60  2004/01/13 22:42:15  psy
commandline fixes

Revision 1.59  2003/12/30 13:48:08  psy
connection settings improvement

Revision 1.58  2003/12/28 15:32:46  dek
NPE fixed (when using -c switch)

Revision 1.57  2003/12/23 17:33:21  psy
cosmetic

Revision 1.56  2003/12/23 17:22:18  psy
more commandline handling stuff

Revision 1.55  2003/12/23 03:39:34  psy
commandline handling improved

Revision 1.54  2003/12/04 08:47:27  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.53  2003/12/03 22:19:11  lemmy
store g2gui.pref in ~/.g2gui/g2gui.pref instead of the program directory

Revision 1.52  2003/11/30 20:02:06  lemmy
check boundaries

Revision 1.51  2003/11/30 18:49:08  lemmy
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

Revision 1.43  2003/11/20 14:02:17  lemmy
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

Revision 1.29  2003/09/18 09:44:57  lemmy
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

Revision 1.23  2003/08/23 10:02:02  lemmy
use supertype where possible

Revision 1.22  2003/08/22 10:28:22  lemmy
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

Revision 1.17  2003/08/19 12:14:16  lemmy
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

Revision 1.8  2003/08/07 13:25:37  lemmy
ResourceBundle added

Revision 1.7  2003/08/06 17:41:20  lemmy
some fixes

Revision 1.6  2003/08/04 14:38:55  lemmy
splashscreen and error handling added (resending on badpassword doenst work atm)

Revision 1.5  2003/08/03 19:09:51  lemmy
better error handling

Revision 1.4  2003/08/01 17:21:19  lemmy
reworked observer/observable design, added multiversion support

Revision 1.3  2003/07/18 04:34:22  lemmy
checkstyle applied

Revision 1.2  2003/07/17 15:10:35  lemmy
foobar

Revision 1.1  2003/07/17 14:58:37  lemmy
refactored

Revision 1.9  2003/07/13 12:50:01  dek
now the socket is closed, when application is exited

Revision 1.8  2003/07/02 17:06:29  dek
Checkstyle, JavaDocs still have to be added

Revision 1.7  2003/06/27 11:23:48  dek
gui is no more children of applicationwindow

Revision 1.6  2003/06/27 10:39:37  lemmy
core is now Runnable

Revision 1.5  2003/06/26 21:11:10  dek
speed is shown

Revision 1.4  2003/06/26 12:04:59  dek
pref-dialog accessible in main-window

Revision 1.3  2003/06/25 18:04:53  dek
Console-Tab reworked

Revision 1.2  2003/06/25 10:42:36  dek
peferenences dialog at first start

Revision 1.1  2003/06/24 20:44:54  lemmy
refactored

Revision 1.1  2003/06/24 20:13:14  lemmy
initial commit

*/
