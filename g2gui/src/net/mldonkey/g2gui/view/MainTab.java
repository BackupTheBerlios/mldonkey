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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.model.ClientStats;
import net.mldonkey.g2gui.view.helper.CGridLayout;
import net.mldonkey.g2gui.view.main.MainCoolBar;
import net.mldonkey.g2gui.view.main.MainMenuBar;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.pref.Preferences;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Gui
 *
 *
 * @version $Id: MainTab.java,v 1.65 2003/09/03 14:49:07 zet Exp $ 
 *
 */
public class MainTab implements Observer, ShellListener {

	private static final DecimalFormat decimalFormat = new DecimalFormat( "0.#" );

	private String titleBarText = "g2gui alpha";
	private StatusLine statusline;
	private Object waiterObject;
	private Shell shell;
	private CoreCommunication mldonkey;
	private Composite mainComposite, pageContainer;
	private List registeredTabs;
	private GuiTab activeTab;
	private GuiTab[] tabs;
	private MainCoolBar coolBar;
	
	/**
	 * @param core the most important thing of the gui: were do i get my data from
	 * @param shell were do we live?
	 */
	public MainTab( CoreCommunication core, Shell shell ) {
		this.registeredTabs = new ArrayList();
		this.mldonkey = core;
		
		this.shell = shell;
		final Shell mainShell = shell;	
		Display display = shell.getDisplay();
		shell.addShellListener( this );
		G2GuiResources.initialize();
		setTitleBarText( shell ); 
		shell.setLayout( new FillLayout() );
		createContents( shell );
		shell.pack ();
		/* close the splashShell from G2Gui.java */
		G2Gui.increaseBar( "" );
		G2Gui.getSplashShell().dispose();
		
		/* set the old size of this window - must be after pack() */
		setSizeLocation( shell );
		shell.open ();
		/* things we should do if we dispose */
		shell.addDisposeListener( new DisposeListener() {
			public synchronized void widgetDisposed( DisposeEvent e ) {
				/* save the size of this window */
				saveSizeLocation( mainShell );
				
				/* set all tabs to inactive */
				Iterator itr = registeredTabs.iterator();
				while ( itr.hasNext() ) {
					GuiTab aTab = ( GuiTab ) itr.next();
					aTab.dispose();
				}
			
				coolBar.getHandCursor().dispose();
				
				// If we have created the core, kill it
				if (G2Gui.getCoreConsole() != null) {
					Message killCore = new EncodeMessage( Message.S_KILL_CORE );
					killCore.sendMessage( mldonkey.getConnection() );
					G2Gui.getCoreConsole().dispose();
				}
				
				// disconnect from core
				( ( CoreCommunication )mldonkey ).disconnect();	
				
				// save preferences
				PreferenceLoader.saveStore();
				PreferenceLoader.cleanUp();
			}
		} );
		while ( !shell.isDisposed () ) {
			if ( !display.readAndDispatch () ) display.sleep ();
		} 
		display.dispose();
	} 
	
	/* ( non-Javadoc )
	 * @see org.eclipse.jface.window.Window#createContents( org.eclipse.swt.widgets.Composite )
	 */
	private void createContents( Shell parent ) {
		GridData gridData;	
		mainComposite = new Composite( parent, SWT.NONE );
				
		parent.setImage( G2GuiResources.getImage( "ProgramIcon" ) );
				
		new MainMenuBar( this );		
				
		GridLayout gridLayout = CGridLayout.createGL(1,0,0,0,1,false);
		mainComposite.setLayout( gridLayout ); 
			
		Label horLine = new Label( mainComposite, SWT.HORIZONTAL | SWT.SEPARATOR );
		gridData = new GridData( GridData.FILL_HORIZONTAL );
		horLine.setLayoutData( gridData );
		
		this.coolBar = new MainCoolBar( this, 
			PreferenceLoader.loadBoolean( "toolbarSmallButtons" ),
			PreferenceLoader.loadBoolean( "coolbarLocked" ) );
			
		pageContainer = new Composite( mainComposite, SWT.NONE );			
		pageContainer.setLayout( new StackLayout() );

		/* now we add all the tabs */
		this.addTabs();					

		gridData = new GridData( GridData.FILL_BOTH );
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;			
		pageContainer.setLayoutData( gridData );
		
		/* layout the coolbar, because we added icons */
		this.coolBar.layoutCoolBar();						

		statusline = new StatusLine( this );
	} 
	
	/**
	 * Here we add the tabs, they must extend G2GuiTab. They are responsible 
	 * for the content and their button.
	 */
	private void addTabs() {
		if ( PreferenceLoader.loadBoolean( "advancedMode" ) ) {
			this.tabs = new GuiTab[ 6 ];
			tabs[ 0 ] =	new TransferTab( this );
			tabs[ 1 ] =	new SearchTab( this );
			tabs[ 2 ] =	new ServerTab( this );
			tabs[ 3 ] =	new ConsoleTab( this );
			tabs[ 4 ] =	new StatisticTab( this );
			tabs[ 5 ] = new MessagesTab( this );
		}
		else {
			this.tabs = new GuiTab[ 3 ];
			tabs[ 0 ] =	new TransferTab( this );
			tabs[ 1 ] =	new SearchTab( this );
			tabs[ 2 ] =	new StatisticTab( this );
		}	

		/*setting TransferTab active if registered*/
		Iterator tabIterator = registeredTabs.iterator();
		while ( tabIterator.hasNext() ) {
			GuiTab tempTab = ( GuiTab )  tabIterator.next();
			/* set the default tab to active */
			if ( tempTab instanceof TransferTab )									
				tempTab.setActive();					 
		}		
	} 

	/**
	 * Registers a new Tab to this MainWindow
	 * @param newTab The GuiTab to register to this MainWindow
	 */
	public void registerTab( GuiTab newTab ) {
		registeredTabs.add( newTab );		
	} 

	/**
	 * Sets the given tab to the current active page
	 * @param activatedTab The tab to set active
	 */
	public void setActive( GuiTab activatedTab ) {	
		if (!getCore().isConnected()) return;	
		
		if ( activeTab != null ) {
			activeTab.getContent().setVisible( false );
			activeTab.setInActive();
		} 
		( ( StackLayout ) pageContainer.getLayout() ).topControl 
											= activatedTab.getContent();		
		pageContainer.layout();	
		activeTab = activatedTab;		
	} 

	/**
	 * The Gui's Title
	 * @param shell The shell to set the title on
	 * @param title The text to appear in the title-bar
	 */
	public void update( Observable arg0, Object receivedInfo ) {
		final ClientStats clientInfo = ( ClientStats ) receivedInfo;
		if ( !shell.isDisposed() )
			shell.getDisplay().syncExec( new Runnable() {
			   public void run() {
					if ( !shell.isDisposed() ) {
						String prependText = 
							"(D:" + decimalFormat.format( clientInfo.getTcpDownRate()) + ")" +
							"(U:" + decimalFormat.format( clientInfo.getTcpUpRate()) + ")";			
						shell.setText( prependText + " : " + titleBarText );
					}	
				}
			} );
	}

	/**
	 * Create the preference window
	 */
	public void openPreferences() {
		Shell prefshell = new Shell();
		Preferences myprefs = new Preferences( PreferenceLoader.getPreferenceStore() );					
		myprefs.open( prefshell, mldonkey );
		
		Iterator itr = registeredTabs.iterator();
		while ( itr.hasNext() ) {
			GuiTab aTab = ( GuiTab ) itr.next();
			aTab.updateDisplay();
		}
	}

	/**
	 * Sets the size to the old value of the given shell obj
	 * @param shell The shell to set the size from
	 */
	public void setSizeLocation( Shell shell ) {
				
		if (PreferenceLoader.contains( "windowBounds" ) ) {
			
			if( PreferenceLoader.loadBoolean( "windowMaximized" ) ) 
				shell.setMaximized( true );   
			else 
				shell.setBounds( PreferenceLoader.loadRectangle("windowBounds") );
		} 
	}

	/**
	 * Saves the size of the shell to a file
	 * @param shell The shell to save the size from
	 */
	public void saveSizeLocation( Shell shell ) {
		PreferenceStore p = PreferenceLoader.getPreferenceStore();
		
		p.setValue( "coolbarLocked", coolBar.isCoolbarLocked() );
		p.setValue( "toolbarSmallButtons", coolBar.isToolbarSmallButtons() );
	
		if ( shell.getMaximized() )
			p.setValue( "windowMaximized", shell.getMaximized() );
		else {
			PreferenceConverter.setValue(p, "windowBounds", shell.getBounds());
			p.setValue( "windowMaximized", shell.getMaximized() );
		}
	}
	
	/**
	 * @return
	 */
	public Composite getMainComposite() {
		return mainComposite;
	}
	
	/**
	 * @return
	 */
	public StatusLine getStatusline() {
		return statusline;
	}

	/**
	 * @return
	 */
	public GuiTab[] getTabs() {
		return this.tabs;
	}

	/**
	 * @return The PageContainer Composite
	 */
	public Composite getPageContainer() {		
		return pageContainer;
	}
	
	/**
	 * @return
	 */
	public CoreCommunication getCore() {		
		return mldonkey;
	} 

	/**
	 * @return
	 */	
	public Shell getShell() {
		return shell;
	}

	/**
	 * @return
	 */
	public MainCoolBar getCoolBar() {
		return coolBar;
	}
	public void setTitleBarText(Shell shell) {
		shell.setText( titleBarText + " " + G2GuiResources.getString("BUILD_INFORMATION") );					
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ShellListener#
	 * shellActivated(org.eclipse.swt.events.ShellEvent)
	 */
	public void shellActivated( ShellEvent e ) { }

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ShellListener#
	 * shellClosed(org.eclipse.swt.events.ShellEvent)
	 */
	public void shellClosed( ShellEvent e ) { }

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ShellListener#
	 * shellDeactivated(org.eclipse.swt.events.ShellEvent)
	 */
	public void shellDeactivated( ShellEvent e ) { }

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ShellListener#
	 * shellDeiconified(org.eclipse.swt.events.ShellEvent)
	 */
	public void shellDeiconified( ShellEvent e ) {
		this.mldonkey.getClientStats().deleteObserver( this );
		setTitleBarText( shell );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ShellListener#
	 * shellIconified(org.eclipse.swt.events.ShellEvent)
	 */
	public void shellIconified( ShellEvent e ) {
		this.mldonkey.getClientStats().addObserver( this );
	}
} 

/*
$Log: MainTab.java,v $
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

Revision 1.57  2003/08/23 16:18:44  lemmster
fixed locked/button size

Revision 1.56  2003/08/23 15:49:28  lemmster
fix for prefs and refactoring

Revision 1.55  2003/08/23 15:21:37  zet
remove @author

Revision 1.54  2003/08/23 15:15:11  lemmster
addTabs() proper

Revision 1.52  2003/08/23 09:56:15  lemmster
use supertype instead of Core

Revision 1.51  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.50  2003/08/21 16:26:15  lemmster
changed links in menubar

Revision 1.49  2003/08/21 16:07:16  zet
pref button

Revision 1.48  2003/08/21 13:13:10  lemmster
cleanup in networkitem

Revision 1.47  2003/08/21 11:19:15  lemmster
added bt and multinet image

Revision 1.46  2003/08/21 10:11:46  dek
removed umlaut for comling java-file with gcj

Revision 1.45  2003/08/20 22:16:33  lemmster
badconnect is display too. added some icons

Revision 1.44  2003/08/20 16:14:17  zet
menuitems

Revision 1.43  2003/08/20 14:58:43  zet
sources clientinfo viewer

Revision 1.42  2003/08/19 12:14:16  lemmster
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

Revision 1.33  2003/08/10 19:31:15  lemmstercvs01
try to fix the root handle bug

Revision 1.32  2003/08/10 12:59:01  lemmstercvs01
"manage servers" in NetworkItem implemented

Revision 1.31  2003/08/09 19:53:40  zet
feedback menuitem

Revision 1.30  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.29  2003/08/05 13:49:44  lemmstercvs01
support for servertab added

Revision 1.28  2003/08/04 14:38:55  lemmstercvs01
splashscreen and error handling added (resending on badpassword doenst work atm)

Revision 1.27  2003/08/02 17:19:22  zet
only print stats in titlebar while minimized (stats overkill)

Revision 1.26  2003/08/01 17:21:18  lemmstercvs01
reworked observer/observable design, added multiversion support

Revision 1.25  2003/07/31 17:11:07  zet
getShell

Revision 1.24  2003/07/29 10:10:14  lemmstercvs01
moved icon folder out of src/

Revision 1.23  2003/07/29 09:39:24  lemmstercvs01
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

Revision 1.15  2003/07/26 21:43:46  lemmstercvs01
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

Revision 1.9  2003/07/23 17:06:45  lemmstercvs01
add new SearchTab(this)

Revision 1.8  2003/07/23 04:08:07  zet
looks better with icons

Revision 1.7  2003/07/22 16:41:38  zet
register statistics tab

Revision 1.6  2003/07/22 16:22:56  zet
setSizeLocation after pack

Revision 1.5  2003/07/18 09:44:35  dek
using the right setActive, when choosing default-tab

Revision 1.4  2003/07/18 04:34:22  lemmstercvs01
checkstyle applied

Revision 1.3  2003/07/17 15:10:35  lemmstercvs01
foobar

Revision 1.2  2003/07/17 15:02:28  lemmstercvs01
changed visibility of createTransparentImage()

Revision 1.1  2003/07/17 14:58:37  lemmstercvs01
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

Revision 1.11  2003/06/27 17:14:32  lemmstercvs01
removed unneeded importer

Revision 1.10  2003/06/27 13:40:50  dek
*** empty log message ***

Revision 1.9  2003/06/27 11:34:56  lemmstercvs01
unnecessary importer removed

Revision 1.8  2003/06/27 11:23:48  dek
gui is no more children of applicationwindow

Revision 1.7  2003/06/26 21:31:29  lemmstercvs01
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

Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
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