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

import net.mldonkey.g2gui.comm.Core;
import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.helper.ObjectPool;
import net.mldonkey.g2gui.helper.SocketPool;
import net.mldonkey.g2gui.view.console.ExecConsole;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.pref.Preferences;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

/**
 * Starts the whole thing
 *
 *
 * @version $Id: G2Gui.java,v 1.38 2003/10/08 18:11:35 zet Exp $
 *
 */
public class G2Gui {
    private static boolean processingLink = false;
    private static Socket socket;
    private static boolean notProcessingLink = true;
    private static boolean advancedMode;
    private static Process p;
    private static Thread mldonkey;
    private static Object waiterObject;
    private static ObjectPool socketPool;
    private static CoreCommunication core;
    private static PreferenceStore preferenceStore;
    private static Preferences myPrefs;
    private static String aString;
    private static String hostname;
    private static String username;
    private static String password;
    private static int port;
    private static int[] count;
    private static MessageBox box;
    private static Display display = null;
    private static Shell shell;
    private static Shell splashShell;
    private static ProgressBar progressBar;
    private static FormLayout formLayout;
    private static FormData formData;
    private static Rectangle shellRect;
    private static Rectangle displayRect;
    private static ExecConsole execConsole = null;

    /**
     * Starts a new Core and launch the Gui
     * @param args Nothing to put inside
     */
    public static void main( String[] args ) {
        display = new Display();
        G2GuiResources.initialize();
        
        // -c <path/preferenceFile>
        if ( args.length > 1 && args[ 0 ].equals( "-c" ) ) {
        	PreferenceLoader.initialize( args[ 1 ] );
        	String[] newArgs = new String[ args.length - 2 ];
        	for ( int i = 2; i < args.length; i++ ) {
        		newArgs[ i - 2 ] = args[ i ];
        	}
        	args = newArgs;
        	
        } else {
			PreferenceLoader.initialize();
        }
   
        preferenceStore = PreferenceLoader.getPreferenceStore();
        
		if ( containsLink( args ) ) {
			/*these two fields are only for easier if ( boolean )*/
			notProcessingLink = false;
			processingLink = true;
		}
		/*determine wether a new instance of G2gui is allowed: */
		if ( processingLink )	
			launch( args );        	
        else if ( PreferenceLoader.loadBoolean( "allowMultipleInstances" ) )
			launch( args );
		else if ( !PreferenceLoader.loadBoolean( "running" ) )
			launch( args );
		else {
			MessageBox alreadyRunning = new MessageBox( new Shell( display ),SWT.YES | SWT.NO | SWT.ICON_ERROR ) ;
			alreadyRunning.setText( G2GuiResources.getString ( "G2_MULTIPLE_WARNING_HEAD"  ) );
			alreadyRunning.setMessage( G2GuiResources.getString ( "G2_MULTIPLE_WARNING_MESSAGE" ) );
			if (alreadyRunning.open() == SWT.YES) {
				launch( args );
			} 
		}        
    }

    /**
     * spawn core
     */
    public static void spawnCore() {
        if ( PreferenceLoader.loadString( "coreExecutable" ).equals( "" ) )
            return;
        File coreEXE = new File( PreferenceLoader.loadString( "coreExecutable" ) );
        if ( ( execConsole == null ) && coreEXE.exists() && coreEXE.isFile() ) {
            execConsole = new ExecConsole();
            try {
                // wait while the core loads and opens the gui port? something better?
                Thread.sleep( 7777 );
            }
            catch ( InterruptedException e ) {
            }
        }
    }

    /**
     * Launch
     * @param args 
     */
    public static void launch( String[] args ) {
		 
		 /* we are running, so we make this available to other instances */		 
		PreferenceLoader.getPreferenceStore().setValue( "running", true );
		PreferenceLoader.saveStore();
		
        waiterObject = new Object();
        if ( notProcessingLink ) {
            myPrefs = new Preferences( preferenceStore );
            /* load the preferences */
            try {
                myPrefs.initialize( preferenceStore );
            }
            catch ( IOException e ) {
                System.out.println( "failed" );
            }
            shell = new Shell( display );
            splashShell = new Shell( display, SWT.NO_TRIM | SWT.NO_BACKGROUND | SWT.ON_TOP );
            box = new MessageBox( shell, SWT.ICON_ERROR | SWT.YES | SWT.NO );
        	final Image splashImage = G2GuiResources.getImage( "splashScreen" );
          
            splashShell.addPaintListener( new PaintListener() {
                public void paintControl( PaintEvent e ) {
                    e.gc.drawImage( splashImage, 0, 0 );
                }
            });
            
            progressBar = new ProgressBar(splashShell, SWT.NONE );
            count = new int[] { 3 };

            /* build the splash */
            progressBar.setMaximum( count[ 0 ] );
            FormLayout layout = new FormLayout();
            splashShell.setLayout( layout );
            formData = new FormData();
            formData.left = new FormAttachment( 0, 5 );
            formData.right = new FormAttachment( 100, -5 );
            formData.bottom = new FormAttachment( 100, -5 );
            progressBar.setLayoutData( formData );
          	splashShell.pack();
			
			Rectangle displayBounds = display.getBounds();
			Rectangle splashImageBounds = splashImage.getBounds();
		   	
		   	splashShell.setBounds(
				displayBounds.x + ( displayBounds.width - splashImageBounds.width ) / 2,
				displayBounds.y + ( displayBounds.height - splashImageBounds.height ) / 2,
				splashImageBounds.width,
				splashImageBounds.height
			);
            
            splashShell.open();
            
            // Needed on GTK to dispatch paintEvent.
            // SWT is idiotic.
            display.update();
            
            spawnCore();
            increaseBar( "Starting the model" );
        }
        /* if the gui isnt set up yet launch the preference window */
        if ( !( preferenceStore.getBoolean( "initialized" ) ) && notProcessingLink ) {
            preferenceStore.setValue( "initialized", true );
            splashShell.setVisible( false );
            myPrefs.open( shell, null );

            // crashes if you call myPrefs.open again 
            // if you don't create a new Preferenes() - Why?
            // a jface.Wizard would be better
            // temporary hack
            myPrefs = new Preferences( preferenceStore );
            splashShell.setVisible( true );
        }
        port = preferenceStore.getInt( "port" );
        hostname = preferenceStore.getString( "hostname" );
        username = preferenceStore.getString( "username" );
        password = preferenceStore.getString( "password" );
        advancedMode = preferenceStore.getBoolean( "advancedMode" );
        if ( processingLink )
            advancedMode = false;

        /* create the socket connection to the core */
        socket = null;
        try {
            socketPool = new SocketPool( hostname, port );
            socket = ( Socket ) socketPool.checkOut();
        }
        catch ( UnknownHostException e ) {
            if ( notProcessingLink ) {
                splashShell.dispose();
                box.setText( G2GuiResources.getString( "G2_INVALID_ADDRESS" ) );
                box.setMessage( G2GuiResources.getString( "G2_ILLEGAL_ADDRESS" ) );
                int rc = box.open();
                if ( rc == SWT.NO ) {
                    shell.dispose();
                    display.dispose();
                }
                else {
                    myPrefs.open( shell, null );
                    relaunchSelf( args );
                }
            }
            else {
                myPrefs = new Preferences( preferenceStore );
                shell = new Shell();
                myPrefs.open( shell, null );
                relaunchSelf( args );
            }
            return;
        }
        catch ( IOException e ) {
            if ( notProcessingLink ) {
                splashShell.dispose();
                box.setText( G2GuiResources.getString( "G2_IOEXCEPTION" ) );
                box.setMessage( G2GuiResources.getString( "G2_CORE_NOT_RUNNING" ) );
                int rc = box.open();
                if ( rc == SWT.NO ) {
                    shell.dispose();
                    display.dispose();
                }
                else {
                    myPrefs.open( shell, null );
                    relaunchSelf( args );
                }
            }
            else {
                myPrefs = new Preferences( preferenceStore );
                shell = new Shell();
                myPrefs.open( shell, null );
                relaunchSelf( args );
            }
            return;
        }

        /* launch the model */
        PreferenceLoader.saveStore();
        boolean pollMode = processingLink;

        /* wait as long as the core tells us to continue */
        synchronized ( waiterObject ) {
            core = new Core( socket, username, password, waiterObject, pollMode, advancedMode );
            core.connect();
            mldonkey = new Thread( core );
            mldonkey.setDaemon( true );
            mldonkey.start();
            try {
                waiterObject.wait();
            }
            catch ( InterruptedException e1 ) {
            }
        }

        /* did the core receive "bad password" */
        if ( core.getConnectionDenied() ) {
            if ( notProcessingLink )
                connectDeniedHandling();
        }
        else if ( core.getBadPassword() ) {
            core.disconnect();
            if ( notProcessingLink )
                badPasswordHandling( args );
        }
        else {
            if ( notProcessingLink ) {
                increaseBar( "Starting the view" );
                MainTab g2gui = new MainTab( core, shell );
            }
            else
                sendDownloadLink( args );
        }
        core.disconnect();
        
        /* 
         * we are not running anymore, so we can allow another instance to
         * be started
         */
        PreferenceLoader.getPreferenceStore().setValue( "running", false );
        PreferenceLoader.saveStore(); 
    }

    /**
     * @param args
     * @return
     */
    private static boolean containsLink( String[] args ) {
        /*TODO: regex-check wether the args[] contains an ed2k-link
         * this is for the handling of platform independent link-handling-hack
         * but this seems not to be important, as core ignores malformed links
         */
        if ( args.length != 0 )
            return true;
        else
            return false;
    }

    /**
     * Send a link - raw socket without a core
     */
    private static void sendDownloadLink( String[] args ) {
        	
        Object[] content = args;
        EncodeMessage link = new EncodeMessage( Message.S_DLLINK, content );
        
        try {
       		Message.writeStream( socket, link.getHeader(), link.getContent() );
        } catch ( IOException e ) {
        	e.printStackTrace();
        }

    }

    /**
     * relaunch the main method with killing the old one
     */
    private static void relaunchSelf( String[] args ) {
        shell.dispose();
        launch( args );
    }

    /**
     * Raise an messagebox on badpassword exception
     * and send the password again
     * 
     * @param args nothing to put inside
     */
    public static void badPasswordHandling( String[] args ) {
        splashShell.dispose();

        /* raise a warning msg */
        box = new MessageBox( shell, SWT.ICON_WARNING | SWT.YES | SWT.NO );
        box.setText( G2GuiResources.getString( "G2_LOGIN_INVALID" ) );
        box.setMessage( G2GuiResources.getString( "G2_USER_PASS" ) );
        int rc = box.open();
        if ( rc == SWT.NO ) {
            shell.dispose();
            display.dispose();
        }
        else {
            myPrefs.open( shell, null );
            relaunchSelf( args );
        }
    }

    /**
     * DOCUMENT ME!
     */
    public static void connectDeniedHandling() {
        splashShell.dispose();

        /* raise a warning msg */
        box = new MessageBox( shell, SWT.ICON_ERROR | SWT.OK );
        box.setText( G2GuiResources.getString( "G2_CONNECTION_DENIED" ) );
        box.setMessage( G2GuiResources.getString( "G2_CONNECTION_ATTEMPT_DENIED" ) );
        box.open();
        shell.dispose();
        display.dispose();
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

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ExecConsole getCoreConsole() {
        return execConsole;
    }
}

/*
$Log: G2Gui.java,v $
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
