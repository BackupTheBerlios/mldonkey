/*
 * Copyright 2003
 * G2GUI Team
 *
 *
 * This file is part of G2GUI.
 *
 * G2GUI is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * ( at your option ) any later version.
 *
 * G2GUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2GUI; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package net.mldonkey.g2gui.view;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.helper.VersionInfo;
import net.mldonkey.g2gui.view.helper.Splash;
import net.mldonkey.g2gui.view.helper.VersionCheck;
import net.mldonkey.g2gui.view.helper.WebLauncher;
import net.mldonkey.g2gui.view.helper.WidgetFactory;
import net.mldonkey.g2gui.view.main.MainCoolBar;
import net.mldonkey.g2gui.view.main.MainMenuBar;
import net.mldonkey.g2gui.view.main.Minimizer;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.pref.Preferences;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.systray.SystemTray;


import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.MessageBox;



/**
 * MainTab
 *
 * @version $Id: MainWindow.java,v 1.13 2004/03/09 19:16:27 dek Exp $
 */
public class MainWindow implements ShellListener {
    private String titleBarText;
    private StatusLine statusline;
    private Shell shell;
    private CoreCommunication mldonkey;
    private Composite mainComposite;
    private Composite pageContainer;
    private List registeredTabs;
    private GuiTab activeTab;
    private List tabs;
    private MainCoolBar coolBar;
    private Minimizer minimizer;
	private SystemTray trayMenu;

    /**
     * @param core the most important thing of the gui: were do i get my data from
     * @param shell were do we live?
     */
    
   
    
    public MainWindow(CoreCommunication core, final Shell shell) {
    	
        this.titleBarText = "g2gui alpha (" + G2Gui.getConnectionString() + ")";
    	this.registeredTabs = new ArrayList();
        this.mldonkey = core;
        this.shell = shell;

        Display display = shell.getDisplay();
        shell.addShellListener(this);
        minimizer = new Minimizer(shell, core, titleBarText);
        minimizer.setTitleBarText();
        shell.setLayout(new FillLayout());
        Splash.increaseSplashBar("creating tabs");
        createContents(shell);
        shell.pack();

        /* close the splashShell from G2Gui.java */
        Splash.increaseSplashBar("view successfully started");
        Splash.dispose();

        /* set the old size of this window - must be after pack() */
        setSizeLocation(shell);

        shell.open();

        /* things we should do if we dispose */
        shell.addDisposeListener(new DisposeListener() {
                public synchronized void widgetDisposed(DisposeEvent e) {
                    if (G2Gui.debug) System.out.println("Disposing MainWindow");
                    /* save the size of this window */
                    saveSizeLocation(shell);

                    /* set all tabs to inactive */
                    Iterator itr = registeredTabs.iterator();

                    while (itr.hasNext()) {
                        GuiTab aTab = (GuiTab) itr.next();
                        aTab.dispose();
                    }

                    /* If we have created the core, kill it only if we _really_ shutdown */
                    if ( (G2Gui.runLocalCore() && !PreferenceLoader.isRelaunching() ) || 
                    		(G2Gui.runLocalCore() &&
                    		PreferenceLoader.loadString("coreExecutable").equals("") ) ) {
                        Message killCore = new EncodeMessage(Message.S_KILL_CORE);
                        killCore.sendMessage(mldonkey);
                        /* make sure to kill the CoreConsole, if we started one */
                        if ( G2Gui.getCoreConsole() != null )
							G2Gui.getCoreConsole().dispose();
                    }

                    /* disconnect from core */
                    mldonkey.disconnect();
                    
                    /* save preferences */
                    PreferenceLoader.saveStore();
                    
                    /* do not call cleanup here yet
                    PreferenceLoader.cleanUp(); */ 
                }
            });
         trayMenu = new SystemTray(this);


        try {
            while (!shell.isDisposed()) {
                if (!display.readAndDispatch())
                    display.sleep();
            }
        } catch (Exception e) {
            if (G2Gui.debug) {
                System.out.println(e);
            	e.printStackTrace();
            }
            else {
                /* getCause() seems always to be null unfortunately */
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw, true));

                ErrorDialog errorDialog = new ErrorDialog(new Shell(display), sw.toString());
                errorDialog.open(); 
            } 
        }
        
       

    }

    /* ( non-Javadoc )
     * @see org.eclipse.jface.window.Window#createContents( org.eclipse.swt.widgets.Composite )
     */
    private void createContents(Shell parent) {
        mainComposite = new Composite(parent, SWT.NONE);
        parent.setImage(G2GuiResources.getImage("ProgramIcon"));
        new MainMenuBar(this);

        // try a margin of 1 ?
        GridLayout gridLayout = WidgetFactory.createGridLayout(1, 1, 1, 0, 1, false);
        mainComposite.setLayout(gridLayout);

        Label horLine = new Label(mainComposite, SWT.HORIZONTAL | SWT.SEPARATOR);
        horLine.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        this.coolBar = new MainCoolBar(this, PreferenceLoader.loadBoolean("toolbarSmallButtons"),
                PreferenceLoader.loadBoolean("coolbarLocked"));
        pageContainer = new Composite(mainComposite, SWT.NONE);
        pageContainer.setLayout(new StackLayout());

        /* now we add all the tabs */
        this.addTabs();

        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        pageContainer.setLayoutData(gridData);

        /*restore coolbar-Layout from saved-state*/
        this.coolBar.layoutCoolBar();
        this.coolBar.restoreLayout();

        statusline = new StatusLine(this);
    }

    /**
     * Here we add the tabs, they must extend G2GuiTab. They are responsible
     * for the content and their button.
     */
    private void addTabs() {
        this.tabs = new ArrayList();

        if (PreferenceLoader.loadBoolean("advancedMode")) {
            tabs.add(new TransferTab(this));
            tabs.add(new SearchTab(this));
            tabs.add(new ServerTab(this));
            tabs.add(new SharesTab(this));
            tabs.add(new ConsoleTab(this));
            tabs.add(new StatisticTab(this));
            tabs.add(new MessagesTab(this));
            
        } else {
            tabs.add(new TransferTab(this));
            tabs.add(new SearchTab(this));
            tabs.add(new StatisticTab(this));
        }

        /*setting TransferTab active if registered*/
        Iterator tabIterator = registeredTabs.iterator();

        while (tabIterator.hasNext()) {
            GuiTab tempTab = (GuiTab) tabIterator.next();

            /* set the default tab to active */
            if (tempTab instanceof TransferTab)
                tempTab.setActive();
        }
    }

    /**
     * Registers a new Tab to this MainWindow
     * @param newTab The GuiTab to register to this MainWindow
     */
    public void registerTab(GuiTab newTab) {
        registeredTabs.add(newTab);
    }

    /**
     * Sets the given tab to the current active page
     * @param activatedTab The tab to set active
     */
    public void setActive(GuiTab activatedTab) {
        if (activeTab != null) {
            activeTab.getContent().setVisible(false);
            activeTab.setInActive();
        }

        ((StackLayout) pageContainer.getLayout()).topControl = activatedTab.getContent();
        pageContainer.layout();
        activeTab = activatedTab;
    }


    /**
     * Creates the preference window
     * @return true if restart will be done, false if not
     */
    public boolean openPreferences() {
    	Shell prefshell = new Shell(shell.getDisplay());
        Preferences myprefs = new Preferences(PreferenceLoader.getPreferenceStore());
        //TODO: properly dispose
        myprefs.open(prefshell, mldonkey); 
        
        // did the user change a restart-critical pref? do a relaunch if yes
        if (PreferenceLoader.needsRelaunch()) {
       		MessageBox box = new MessageBox( shell , SWT.ICON_WARNING | SWT.YES | SWT.NO );
       		box.setMessage( G2GuiResources.getString("PREF_DORESTART") );		
       		if(box.open() == SWT.YES)  {
       			PreferenceLoader.setRelaunching(true);
       		} else {
       			// the use denied our restart-request, do not ask again for these changes
       			PreferenceLoader.saveCritPrefs();
       		}
        }
        	
        // lets update our tabs with new options
        Iterator itr = registeredTabs.iterator();
        while (itr.hasNext()) {
            GuiTab aTab = (GuiTab) itr.next();
            aTab.updateDisplay();
        } 
        
        // if we want to do a relaunch, return the proper values
        if (PreferenceLoader.isRelaunching()) { 
        	System.out.println("A restart is necessary! sayeth MainWindow...");
        	G2Gui.relaunchSelf();
        	// this return true is very important to not provoke an SWTException in the caller
        	// Have a look there... maybe needs something better 
        	return true;
        }
        return false;
    }

    /**
     * Sets the size to the old value of the given shell obj
     * @param shell The shell to set the size from
     */
    public void setSizeLocation(Shell shell) {
        if (PreferenceLoader.contains("windowBounds")) {
            if (PreferenceLoader.loadBoolean("windowMaximized"))
                shell.setMaximized(true);
            else
                shell.setBounds(PreferenceLoader.loadRectangle("windowBounds"));
        }
    }

    /**
     * Saves the size of the shell to a file
     * @param shell The shell to save the size from
     */
    public void saveSizeLocation(Shell shell) {
        if (shell.getMaximized())
        	PreferenceLoader.setValue("windowMaximized", shell.getMaximized());
        else {
            PreferenceConverter.setValue(PreferenceLoader.getPreferenceStore(), "windowBounds", shell.getBounds());
            PreferenceLoader.setValue("windowMaximized", shell.getMaximized());
        }
    }

    /**
     * @return The main composite where we display our data in
     */
    public Composite getMainComposite() {
        return mainComposite;
    }

    /**
     * @return The statusline
     */
    public StatusLine getStatusline() {
        return statusline;
    }

    /**
     * @return A <code>List</code> holding our current running <code>GuiTab</code>
     */
    public List getTabs() {
        return this.tabs;
    }

    /**
     * @return The PageContainer Composite
     */
    public Composite getPageContainer() {
        return pageContainer;
    }

    /**
     * @return The core where we get our <code>Information</code> from
     */
    public CoreCommunication getCore() {
        return mldonkey;
    }

    /**
     * @return The main shell
     */
    public Shell getShell() {
        return shell;
    }

    /**
     * @return The CoolBar
     */
    public MainCoolBar getCoolBar() {
        return coolBar;
    }

    /**
     * @return minimizer
     */
    public Minimizer getMinimizer() {
        return minimizer;
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.ShellListener#
     * shellActivated(org.eclipse.swt.events.ShellEvent)
     */
    public void shellActivated(ShellEvent e) {
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.ShellListener#
     * shellClosed(org.eclipse.swt.events.ShellEvent)
     */
    public void shellClosed(ShellEvent e) {
    	if (PreferenceLoader.loadBoolean("closeToTray")){					
    		shell.setVisible(false);
    		e.doit=minimizer.close();
    	}

    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.ShellListener#
     * shellDeactivated(org.eclipse.swt.events.ShellEvent)
     */
    public void shellDeactivated(ShellEvent e) {
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.ShellListener#
     * shellDeiconified(org.eclipse.swt.events.ShellEvent)
     */
    public void shellDeiconified(ShellEvent e) {
        minimizer.restore();
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.ShellListener#
     * shellIconified(org.eclipse.swt.events.ShellEvent)
     */
    public void shellIconified(ShellEvent e) {
    	if (PreferenceLoader.loadBoolean("minimizeToTray")){
    		shell.setVisible(false);
    		e.doit=false;	
    	}
        else {
        	minimizer.minimize();
        }
    }

    /**
     * ErrorDialog
     */
    private class ErrorDialog extends Dialog {
        String string;

        public ErrorDialog(Shell shell, String string) {
            super(shell);
            this.string = string;
            setShellStyle(getShellStyle() | SWT.RESIZE);
        }

        protected void configureShell(Shell newShell) {
            super.configureShell(newShell);
            newShell.setSize(400, 300);
            newShell.setText("Bug detected!");
        }

        protected Control createDialogArea(Composite parent) {
            Composite composite = (Composite) super.createDialogArea(parent);

            Text textInfo = new Text(composite,
                    SWT.MULTI | SWT.READ_ONLY | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
            textInfo.setLayoutData(new GridData(GridData.FILL_BOTH));
            textInfo.setText("Please help us to improve this product!\n" +
                "Please submit a bug report detailing *exactly* what you were doing when this happened!!!\n" +
                "http://developer.berlios.de/bugs/?group_id=610\n\n" + 
				VersionInfo.getVersion() + "\n"  + 
				VersionCheck.getInfoString() + "\n" + "StackTrace:\n\n" +
                string);

            return composite;
        }

        protected Control createButtonBar(Composite parent) {
            Composite composite = new Composite(parent, SWT.NONE);
            composite.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 5, false));
            composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

            Button launch = new Button(composite, SWT.NONE);
            launch.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            launch.setText("Report this bug!");
            launch.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent s) {
                        WebLauncher.openLink("http://developer.berlios.de/bugs/?group_id=610");
                    }
                });

            Button close = new Button(composite, SWT.NONE);
            close.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
            close.setText(G2GuiResources.getString("BTN_CLOSE"));
            close.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent s) {
                        close();
                    }
                });

            return composite;
        }
    }
}


/*
$Log: MainWindow.java,v $
Revision 1.13  2004/03/09 19:16:27  dek
Now the Systray-Menu becomes usable, now featuring" tooltip"

Revision 1.12  2004/03/08 20:42:23  dek
a first version of a systray for windows

Revision 1.11  2004/02/29 17:36:31  psy
improved local core handling

Revision 1.10  2004/02/17 22:49:58  psy
added more VM/OS/Version debug- and crash-info

Revision 1.9  2004/02/05 20:44:44  psy
hopefully fixed dynamic column behaviour under gtk by introducing a
bogus column.

Revision 1.8  2004/01/28 22:15:34  psy
* Properly handle disconnections from the core
* Fast inline-reconnect
* Ask for automatic relaunch if options have been changed which require it
* Improved the local core-controller

Revision 1.7  2004/01/22 21:28:03  psy
renamed "uploads" to "shares" and moved it to a tab of its own.
"uploaders" are now called "uploads" for improved naming-consistency

Revision 1.6  2003/12/04 08:47:27  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.5  2003/12/03 22:19:11  lemmy
store g2gui.pref in ~/.g2gui/g2gui.pref instead of the program directory

Revision 1.4  2003/11/30 18:43:58  zet
latest swt-fox fixes display.dispose() bug.

Revision 1.3  2003/11/29 19:28:38  zet
minor string move

Revision 1.2  2003/11/29 19:10:24  zet
small update.. continue later.
- mainwindow > tabs > viewframes(can contain gView)

Revision 1.1  2003/11/29 17:00:34  zet
rename MainTab->MainWindow (since it isn't a Tab)

Revision 1.97  2003/11/28 00:57:11  zet
use Weblauncher

Revision 1.96  2003/11/25 17:06:50  dek
yet another test for coolBar

Revision 1.95  2003/11/24 19:01:00  dek
coolBar-Layout is now saved and restored

Revision 1.94  2003/11/24 15:24:30  zet
remove unneeded typecast

Revision 1.93  2003/11/24 08:29:11  lemmy
ErrorDialog shows up only when started without "-d"

Revision 1.92  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

Revision 1.91  2003/11/20 14:02:17  lemmy
G2Gui cleanup

Revision 1.90  2003/11/14 19:41:46  zet
include ver info in bug report dialog

Revision 1.89  2003/11/13 00:21:59  zet
*** empty log message ***

Revision 1.88  2003/11/10 20:55:37  zet
display.close/dispose locks up fox

Revision 1.87  2003/11/06 15:12:44  zet
check getProperty length

Revision 1.86  2003/11/06 03:27:11  zet
fox

Revision 1.85  2003/11/05 04:52:25  zet
fox junk

Revision 1.84  2003/11/05 01:54:13  zet
minor

Revision 1.83  2003/11/05 01:51:35  zet
resizable error dialog

Revision 1.82  2003/11/02 21:13:53  zet
fox workaround

Revision 1.81  2003/11/01 18:45:26  zet
shell.setSize

Revision 1.80  2003/10/27 00:19:14  zet
simple attempt to catch exceptions & inform the user instead of silently crashing
+ try a marginwidth of 1 (as per feedback)

Revision 1.79  2003/10/13 18:36:07  zet
fix file->exit (w/tray) bug

Revision 1.78  2003/10/12 20:39:26  zet
remove high ascii

Revision 1.77  2003/10/12 15:56:21  zet
nill

Revision 1.76  2003/10/11 21:32:39  zet
remove hand cursor (looks weird on gtk)

Revision 1.75  2003/09/25 01:03:24  zet
remove unused waiterObject

Revision 1.74  2003/09/25 00:51:23  zet
not much

Revision 1.73  2003/09/18 15:30:46  zet
centralize writeStream in core
handle IOException rather than throwing it away

Revision 1.72  2003/09/18 09:44:57  lemmy
checkstyle

Revision 1.71  2003/09/16 01:12:25  zet
// isConnected()

Revision 1.70  2003/09/13 11:02:45  lemmy
use List instead of GuiTab[] in addTabs()

Revision 1.69  2003/09/08 18:27:42  zet
*** empty log message ***

Revision 1.68  2003/09/08 18:25:37  zet
init resources in g2gui

Revision 1.67  2003/09/08 17:28:44  zet
more minimizer stuff

Revision 1.66  2003/09/08 15:39:35  zet
minimizer

Revision 1.65  2003/09/03 14:49:07  zet
optionally spawn core from gui

Revision 1.64  2003/08/28 22:44:30  zet
GridLayout helper class

Revision 1.63  2003/08/25 23:38:26  zet
decimalformat

Revision 1.62  2003/08/24 18:26:03  zet
save prefs after disposal of tabs

Revision 1.61  2003/08/24 18:06:41  zet
PreferenceLoader.cleanup

Revision 1.60  2003/08/24 17:15:47  zet
use preferenceloader

Revision 1.59  2003/08/24 16:37:04  zet
combine the preference stores

Revision 1.58  2003/08/23 23:50:26  zet
add build # to titlebar

Revision 1.57  2003/08/23 16:18:44  lemmy
fixed locked/button size

Revision 1.56  2003/08/23 15:49:28  lemmy
fix for prefs and refactoring

Revision 1.55  2003/08/23 15:21:37  zet
remove @author

Revision 1.54  2003/08/23 15:15:11  lemmy
addTabs() proper

Revision 1.52  2003/08/23 09:56:15  lemmy
use supertype instead of Core

Revision 1.51  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.50  2003/08/21 16:26:15  lemmy
changed links in menubar

Revision 1.49  2003/08/21 16:07:16  zet
pref button

Revision 1.48  2003/08/21 13:13:10  lemmy
cleanup in networkitem

Revision 1.47  2003/08/21 11:19:15  lemmy
added bt and multinet image

Revision 1.46  2003/08/21 10:11:46  dek
removed umlaut for comling java-file with gcj

Revision 1.45  2003/08/20 22:16:33  lemmy
badconnect is display too. added some icons

Revision 1.44  2003/08/20 16:14:17  zet
menuitems

Revision 1.43  2003/08/20 14:58:43  zet
sources clientinfo viewer

Revision 1.42  2003/08/19 12:14:16  lemmy
first try of simple/advanced mode

Revision 1.41  2003/08/18 06:00:28  zet
hand cursor when hovering toolitems

Revision 1.40  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.39  2003/08/17 23:52:09  zet
minor

Revision 1.38  2003/08/17 23:13:42  zet
centralize resources, move images

Revision 1.37  2003/08/17 13:19:41  dek
transparent gif as shell-icon

Revision 1.36  2003/08/15 22:05:58  zet
*** empty log message ***

Revision 1.35  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.34  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging

Revision 1.33  2003/08/10 19:31:15  lemmy
try to fix the root handle bug

Revision 1.32  2003/08/10 12:59:01  lemmy
"manage servers" in NetworkItem implemented

Revision 1.31  2003/08/09 19:53:40  zet
feedback menuitem

Revision 1.30  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.29  2003/08/05 13:49:44  lemmy
support for servertab added

Revision 1.28  2003/08/04 14:38:55  lemmy
splashscreen and error handling added (resending on badpassword doenst work atm)

Revision 1.27  2003/08/02 17:19:22  zet
only print stats in titlebar while minimized (stats overkill)

Revision 1.26  2003/08/01 17:21:18  lemmy
reworked observer/observable design, added multiversion support

Revision 1.25  2003/07/31 17:11:07  zet
getShell

Revision 1.24  2003/07/29 10:10:14  lemmy
moved icon folder out of src/

Revision 1.23  2003/07/29 09:39:24  lemmy
change modifier of statusline

Revision 1.22  2003/07/28 17:41:32  zet
static prefstore

Revision 1.21  2003/07/28 17:08:03  zet
lock coolbar

Revision 1.20  2003/07/28 14:49:36  zet
use prefconverter

Revision 1.19  2003/07/27 22:54:05  zet
coolbar small buttons

Revision 1.18  2003/07/27 22:39:36  zet
small buttons toggle (in popup) for main cool menu

Revision 1.17  2003/07/27 00:12:29  zet
check isDisposed

Revision 1.16  2003/07/26 23:10:14  zet
update titlebar with rates

Revision 1.15  2003/07/26 21:43:46  lemmy
createTransparentImage modifier changed to public

Revision 1.14  2003/07/26 00:51:43  zet
stats graph continues to observe when inactive

Revision 1.13  2003/07/25 03:50:53  zet
damn fontfield.. will continue

Revision 1.12  2003/07/25 02:41:22  zet
console window colour config in prefs / try different fontfieldeditor / pref page  (any worse?)

Revision 1.11  2003/07/24 02:35:04  zet
program icon

Revision 1.10  2003/07/24 02:22:46  zet
doesn't crash if no core is running

Revision 1.9  2003/07/23 17:06:45  lemmy
add new SearchTab(this)

Revision 1.8  2003/07/23 04:08:07  zet
looks better with icons

Revision 1.7  2003/07/22 16:41:38  zet
register statistics tab

Revision 1.6  2003/07/22 16:22:56  zet
setSizeLocation after pack

Revision 1.5  2003/07/18 09:44:35  dek
using the right setActive, when choosing default-tab

Revision 1.4  2003/07/18 04:34:22  lemmy
checkstyle applied

Revision 1.3  2003/07/17 15:10:35  lemmy
foobar

Revision 1.2  2003/07/17 15:02:28  lemmy
changed visibility of createTransparentImage()

Revision 1.1  2003/07/17 14:58:37  lemmy
refactored

Revision 1.33  2003/07/15 13:30:27  dek
shell.dispose() on exit, instead of shell.close()

Revision 1.32  2003/07/14 19:26:41  dek
done some clean.up work, since it seems,as if this view becomes reality..

Revision 1.31  2003/07/12 21:50:07  dek
transferTree is in experimantal preView-state...

Revision 1.30  2003/07/10 19:27:28  dek
some idle-race cleanup

Revision 1.29  2003/07/06 19:25:45  dek
*** empty log message ***

Revision 1.28  2003/07/06 19:22:27  dek
*** empty log message ***

Revision 1.27  2003/07/06 19:18:08  dek
hey, there is already a StackLayout....

Revision 1.26  2003/07/06 18:57:21  dek
*** empty log message ***

Revision 1.25  2003/07/05 14:04:50  dek
ups, now it works again

Revision 1.24  2003/07/05 14:01:43  dek
small changes only

Revision 1.23  2003/07/03 17:45:36  mitch
exchanged TransferTab by DownloadTab
experimental!!!

Revision 1.22  2003/07/03 01:56:45  zet
attempt(?) to save window size/pos & table column widths between sessions

Revision 1.21  2003/07/02 19:25:41  dek
transferTab is now default ;-)

Revision 1.20  2003/07/02 19:03:51  dek
Right-mouse-menue for lockable ToolBar

Revision 1.19  2003/07/02 17:04:46  dek
Checkstyle, JavaDocs still have to be added

Revision 1.18  2003/07/02 14:55:00  zet
quick & dirty menu bar

Revision 1.17  2003/07/02 11:21:59  dek
transfer Icon

Revision 1.16  2003/07/02 00:18:37  zet
layoutCoolBar changes - tested on xp/gtk

Revision 1.15  2003/07/01 18:09:51  dek
transparent ToolItems

Revision 1.14  2003/07/01 14:06:06  dek
now takes all Buttons in account for calculating height for CoolBar

Revision 1.13  2003/06/30 21:40:09  dek
CoolBar created

Revision 1.12  2003/06/27 18:20:44  dek
preferences

Revision 1.11  2003/06/27 17:14:32  lemmy
removed unneeded importer

Revision 1.10  2003/06/27 13:40:50  dek
*** empty log message ***

Revision 1.9  2003/06/27 11:34:56  lemmy
unnecessary importer removed

Revision 1.8  2003/06/27 11:23:48  dek
gui is no more children of applicationwindow

Revision 1.7  2003/06/26 21:31:29  lemmy
unnecessary importer removed

Revision 1.6  2003/06/26 21:11:10  dek
speed is shown

Revision 1.5  2003/06/26 14:08:03  dek
statusline created

Revision 1.4  2003/06/26 12:04:59  dek
pref-dialog accessible in main-window

Revision 1.3  2003/06/25 18:04:53  dek
Console-Tab reworked

Revision 1.2  2003/06/24 20:58:36  dek
removed border from Content-Composite

Revision 1.1  2003/06/24 20:44:54  lemmy
refactored

Revision 1.3  2003/06/24 19:18:45  dek
checkstyle apllied

Revision 1.2  2003/06/24 18:34:59  dek
removed senseless lines

Revision 1.1  2003/06/24 18:25:43  dek
working on main gui, without any connection to mldonkey atm, but the princip works
test with:
public class guitest {
        public static void main( String[] args ) {
        Gui g2gui = new Gui( null );
}

*/
