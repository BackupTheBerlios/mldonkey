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

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.comm.Core;
import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.pref.Preferences;
import net.mldonkey.g2gui.model.ClientStats;
import net.mldonkey.g2gui.view.toolbar.ToolButton;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Gui
 *
 * @author $user$
 * @version $Id: MainTab.java,v 1.22 2003/07/28 17:41:32 zet Exp $ 
 *
 */
public class MainTab implements Listener, Observer {
	private CoolBar coolbar;
	private ToolBar miscTools;
	private ToolBar mainTools;
	public static List miscToolButtons = new ArrayList();
	public static List mainToolButtons = new ArrayList();
	public static boolean toolbarSmallButtons = false;
	private boolean coolbarLocked = true;
	private StatusLine statusline;
	private CoreCommunication mldonkey;
	private Composite mainComposite, coolbarComposite, pageContainer;
	private List registeredTabs;
	private GuiTab activeTab;
	private Menu mainMenuBar;	
	private static PreferenceStore internalPrefStore = new PreferenceStore( "g2gui-internal.pref" );
	private static ImageRegistry imageRegistry = new ImageRegistry();
	private ResourceBundle bundle = ResourceBundle.getBundle("g2gui");
	private Shell thisShell = null;
	private final String titleBarText = "g2gui alpha";
	/**
	 * @param core the most important thing of the gui: were do i get my data from
	 * @param shell were do we live?
	 */
	public MainTab( CoreCommunication core, Shell shell ) {
		this.registeredTabs = new ArrayList();
		this.mldonkey = core;
		this.mldonkey.addObserver(this);
		thisShell = shell;
		final Shell mainShell = shell;	
		Display display = shell.getDisplay();					
		shell.setLayout( new FillLayout() );

		createInternalPrefStore();
		createImageRegistry();
		getInternalPrefStoreSettings();
		createContents( shell );
					
		shell.pack ();
		/* set the old size of this window - must be after pack() */
		setSizeLocation( shell );
		shell.open ();
		/* things we should do if we dispose */
		shell.addDisposeListener( new DisposeListener() {
			public synchronized void widgetDisposed( DisposeEvent e ) {
				/* save the size of this window */
				saveSizeLocation( mainShell );
				
				/* save the preferences */
				saveInternalPrefStore();
				
				/* set all tabs to inactive */
				Iterator itr = registeredTabs.iterator();
				while ( itr.hasNext() ) {
					GuiTab aTab = ( GuiTab ) itr.next();
					aTab.dispose();
				}
				
				/* kill the core communication */
				( ( Core )mldonkey ).disconnect();				
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
				
		parent.setImage (getImageFromRegistry("ProgramIcon"));
				
		createMenuBar( parent );					
				
		GridLayout mainLayout = new GridLayout();
		mainLayout.numColumns = 1;
		mainLayout.marginWidth = 0;
		mainLayout.marginHeight = 0;
		mainLayout.verticalSpacing = 1;
		mainComposite.setLayout( mainLayout ); 
			
		Label horLine = new Label( mainComposite, SWT.HORIZONTAL | SWT.SEPARATOR );
		gridData = new GridData( GridData.FILL_HORIZONTAL );
		horLine.setLayoutData( gridData );
		
		coolbarComposite = new Composite (mainComposite, SWT.NONE);
		coolbarComposite.setLayout (new GridLayout ());
		coolbarComposite.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		
		coolbar = createCoolBar( coolbarComposite );
		createToolBars();
		createCoolItems();
		createMiscTools();				
			
		pageContainer = new Composite( mainComposite, SWT.NONE );			
		pageContainer.setLayout( new StackLayout() );

		/* now we add all the tabs */
		this.addTabs();					

		gridData = new GridData( GridData.FILL_BOTH );
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;			
		pageContainer.setLayoutData( gridData );						

		statusline = new StatusLine( mainComposite, mldonkey );
		layoutCoolBar( coolbar );
	} 
	
	protected void openPreferences() {
	
		Shell prefshell = new Shell();
		Preferences myprefs = new Preferences( new PreferenceStore( "g2gui.pref" ) );					
		myprefs.open( prefshell, mldonkey );
		
		Iterator itr = registeredTabs.iterator();
		while ( itr.hasNext() ) {
			GuiTab aTab = ( GuiTab ) itr.next();
			aTab.updateDisplay();
		}
	}
	/**
	 * Create the MenuBar
	 * @param shell The shell to create the MenuBar in
	 */
	private void createMenuBar( final Shell shell ) {
		mainMenuBar = new Menu( shell, SWT.BAR );
		shell.setMenuBar( mainMenuBar );
		
		MenuItem mItem = new MenuItem ( mainMenuBar, SWT.CASCADE );
		mItem.setText ( "File" );
		Menu submenu = new Menu ( shell, SWT.DROP_DOWN );
		mItem.setMenu ( submenu );
		MenuItem item = new MenuItem ( submenu, 0 );
		item.addListener ( SWT.Selection, new Listener () {
			public void handleEvent ( Event e ) {
				shell.dispose();
			} 
		} );
		item.setText ( "E&xit\tCtrl+W" );
		item.setAccelerator ( SWT.CTRL + 'W' );
			
		mItem = new MenuItem ( mainMenuBar, SWT.CASCADE );
		mItem.setText ( "View" );
		
		mItem = new MenuItem ( mainMenuBar, SWT.CASCADE );
		mItem.setText ( "Tools" );
		
		submenu = new Menu( shell, SWT.DROP_DOWN );
		item = new MenuItem( submenu, 0 );
		item.addListener( SWT.Selection, new Listener() {
			public void handleEvent( Event event ) {	
				openPreferences();
			}
		} );
		item.setText( "Preferences" );

		mItem.setMenu( submenu );
		mItem = new MenuItem ( mainMenuBar, SWT.CASCADE );
		mItem.setText ( "Help" );
	} 
	
	/**
	 * Creates a Transparent imageobject with a given .png|.gif Image-Object
	 * be aware, the the scr-image is disposed, so dont' use it any further
	 * 
	 * @param src the non-transparent image we want to process
	 * @param control where is our image laid in, to check for the background-color
	 * @return the transparent image
	 */
	public static Image createTransparentImage( Image src, Control control ) {
		int width = src.getBounds().width;
		int height = src.getBounds().height;
		
		Image result = new Image( control.getDisplay(), new Rectangle( 0, 0, width, height ) );		
		GC gc = new GC( result );
		gc.setBackground( control.getBackground(  ) );
		gc.fillRectangle( 0, 0, width, height );							
		gc.drawImage( src, 0, 0 );
			
		src.dispose();		
		gc.dispose();		

		return result;
	} 
	
	/**
	 * Creates a new Coolbar obj
	 * @param parent The parent Composite to display in
	 * @return a new CoolBar obj
	 */
	private CoolBar createCoolBar( final Composite parent ) {
		GridData gridData;
		
		/*now follows the creation of the CoolBar*/		
		final CoolBar coolBar = new CoolBar ( parent, SWT.FLAT );
		gridData = new GridData( GridData.FILL_HORIZONTAL );
			
		coolBar.addControlListener( new ControlListener() {
			public void controlMoved( ControlEvent e ) {
				 parent.getParent().layout();
			}
			public void controlResized( ControlEvent e ) { 
				parent.getParent().layout(); 
			}
		} );
			
		coolBar.setLayoutData( gridData );
		return coolBar;
	}
	// Create the CoolItems
	public void createCoolItems () {
		
		for ( int i = 0; i < 2; i++ ) { 			
			CoolItem item = new CoolItem ( coolbar, SWT.NONE );
		} 
					
		CoolItem[] items = coolbar.getItems ();
						
		CoolItem mainCoolItem = items [0];
		mainCoolItem.setControl ( mainTools );
				
		CoolItem miscCoolItem = items [1];
		miscCoolItem.setControl ( miscTools );		
	} 
	// create the Tool Bars
	protected void createToolBars() {
		Menu toolmenu = createToolBarRMMenu( coolbar );
		
		mainTools = new ToolBar( coolbar, (toolbarSmallButtons ? SWT.RIGHT : 0) | SWT.FLAT) ;	
		mainTools.setMenu(toolmenu);
			
		miscTools = new ToolBar( coolbar, (toolbarSmallButtons ? SWT.RIGHT : 0) | SWT.FLAT) ;	
		miscTools.setMenu(toolmenu);
				
	}
	
	/**
	 * creates a right-mouse-menue
	 * @return a right-Mouse menue
	 */
	private Menu createToolBarRMMenu( CoolBar coolBar ) {
		final CoolBar thiscoolBar = coolBar;
		Shell shell = coolBar.getShell();
		Menu menu = new Menu( shell , SWT.POP_UP );
		

		/* lock CoolBar */
		final MenuItem lockItem = new MenuItem( menu, SWT.CHECK );
		lockItem.setText( "Lock the Toolbars" );
		lockItem.setSelection(coolbarLocked);
		lockItem.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				thiscoolBar.setLocked( lockItem.getSelection() );
				coolbarLocked = lockItem.getSelection() ;
			}
		} );
		
		/* toggle small buttons */
		final MenuItem toggleButtonsItem = new MenuItem( menu, SWT.CHECK );
		toggleButtonsItem.setText( "Small buttons" );
		toggleButtonsItem.setSelection(toolbarSmallButtons);
		toggleButtonsItem.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				toggleSmallButtons( toggleButtonsItem.getSelection() );
			}
		} );
		
		return menu;
	}

	/**
	 * 
	 * @param coolBar
	 */
	private void layoutCoolBar( CoolBar coolBar ) {
		// This seems to work in xp/gtk - z			
		for ( int j = 0; j < coolBar.getItemCount(); j++ ) {	
			CoolItem tempCoolItem = coolBar.getItem( j );		
			ToolBar tempToolBar =  ( ToolBar ) tempCoolItem.getControl();
			Point pSize = tempToolBar.computeSize( SWT.DEFAULT, SWT.DEFAULT );
			pSize = tempCoolItem.computeSize( pSize.x, pSize.y );
			tempCoolItem.setSize( pSize );
			tempCoolItem.setMinimumSize( pSize );
		}
		coolBar.setLocked(coolbarLocked);
	} 
	
	private void toggleSmallButtons(boolean toggle) {
		
		toolbarSmallButtons = !toolbarSmallButtons;
		
		coolbar.dispose();
		mainTools.dispose();
		miscTools.dispose();
		
		coolbar = createCoolBar (coolbarComposite);
		createToolBars();
		createCoolItems();
					
		Iterator toolButtonIterator = mainToolButtons.iterator();	
		while ( toolButtonIterator.hasNext() ) {
			ToolButton tmp = ( ToolButton )  toolButtonIterator.next();
			if ( tmp instanceof ToolButton )									
				 tmp.useSmallButtons(toolbarSmallButtons);
				 tmp.resetItem(mainTools);
		 
		}	
		toolButtonIterator = miscToolButtons.iterator();	
		while ( toolButtonIterator.hasNext() ) {
			ToolButton tmp = ( ToolButton )  toolButtonIterator.next();
			if ( tmp instanceof ToolButton )									
				 tmp.useSmallButtons(toolbarSmallButtons);
				 tmp.resetItem(miscTools);
						 
		}	
					
		layoutCoolBar (coolbar);
		
	}
		
	private void createMiscTools() {
	
		ToolButton prefButton = new ToolButton ( miscTools, SWT.NONE );
		prefButton.setText(bundle.getString("TT_PreferencesButton"));
		prefButton.setToolTipText(bundle.getString("TT_PreferencesButtonToolTip"));
		
		Image bigImage = MainTab.getImageFromRegistry("PreferencesButton");
		Image smallImage = MainTab.getImageFromRegistry("PreferencesButtonSmall");
				
		prefButton.setBigActiveImage(bigImage);
		prefButton.setBigInactiveImage(bigImage);
		prefButton.setSmallActiveImage(smallImage);
		prefButton.setSmallInactiveImage(smallImage);
		prefButton.useSmallButtons(toolbarSmallButtons);		
		prefButton.setActive(false);
		MainTab.miscToolButtons.add( prefButton );	
		
		prefButton.addListener( SWT.Selection, new Listener() {
			public void handleEvent( Event event ) {
				openPreferences();	
			}
		} );
	
	}

	/**
	 * Here do we add the tabs, they must extend G2GuiTab. They are responsible 
	 * for the content and their button.
	 */
	private void addTabs() {
		new TransferTab( this );
		new SearchTab( this );
		new ConsoleTab( this );
		new StatisticTab( this );
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

	/* ( non-Javadoc )
	 * @see org.eclipse.swt.widgets.Listener#handleEvent( org.eclipse.swt.widgets.Event )
	 */
	public void handleEvent( Event event ) {
		System.out.println( event.widget.toString() );
	} 

	/**
	 * @return The PageContainer Composite
	 */
	public Composite getPageContainer() {		
		return pageContainer;
	} 

	/**
	 * Sets the given tab to the current active page
	 * @param activatedTab The tab to set active
	 */
	public void setActive( GuiTab activatedTab ) {	
		
		if (!getCore().isConnected()) return;	
		
		if ( activeTab != null ) {
			activeTab.getContent().setVisible( false );
			activeTab.setInActive(true);
		} 
		( ( StackLayout ) pageContainer.getLayout() ).topControl 
											= activatedTab.getContent();		
		pageContainer.layout();	
		activeTab = activatedTab;		
	} 

	/* ( non-Javadoc )
	 * @see net.mldonkey.g2gui.view.IG2gui#getCore()
	 */
	public CoreCommunication getCore() {		
		return mldonkey;
	} 
	
	/**
	 * @return The MainToolBar
	 */
	public ToolBar getMainTools() {
		return mainTools;
	} 
	public ToolBar getMiscTools() {
		return miscTools;
	}

	/**
	 * The Gui's Title
	 * @param shell The shell to set the title on
	 * @param title The text to appear in the title-bar
	 */
	public void update(Observable arg0, Object receivedInfo) {
				if (receivedInfo instanceof ClientStats){
					final ClientStats clientInfo = (ClientStats) receivedInfo;
					if (!thisShell.isDisposed())
					thisShell.getDisplay().asyncExec(new Runnable() {
						   public void run() {
								if (!thisShell.isDisposed()) thisShell.setText(
									"(D:" + String.valueOf(clientInfo.getTcpDownRate()) + ")" +
									"(U:" + String.valueOf(clientInfo.getTcpUpRate()) + ")" +
									": " + titleBarText
									);
						  }
					});
				}
	}
	/**
	 * Reads the preference store file from disk
	 */
	public void createInternalPrefStore() {
		try {			
			internalPrefStore.load();
		}
		catch ( IOException e ) { }
	}

	/**
	 * Reads the preference store file to disk
	 */
	public void saveInternalPrefStore() {
		try {
			internalPrefStore.save();
		}
		catch ( IOException e ) {
			System.out.println( "Saving g2gui-internal preferences failed" );
		}	
	}

	/**
	 * The preference store file
	 * @return A PreferenceStore file with our Information
	 */
	public static PreferenceStore getStore() {
		return internalPrefStore;
	}
	
	/**
	 * Sets the size to the old value of the given shell obj
	 * @param shell The shell to set the size from
	 */
	public void setSizeLocation( Shell shell ) {
				
		if (internalPrefStore.contains( "windowBounds" ) ) {
			
			if( internalPrefStore.getBoolean( "windowMaximized" ) ) 
				shell.setMaximized( true );   
			else 
				shell.setBounds( PreferenceConverter.getRectangle(internalPrefStore, "windowBounds") );
		} 
	}
	public void getInternalPrefStoreSettings() {
		internalPrefStore.setDefault("coolbarLocked", coolbarLocked);
		
		toolbarSmallButtons = internalPrefStore.getBoolean("toolbarSmallButtons");
		coolbarLocked = internalPrefStore.getBoolean("coolbarLocked");
	}
	
	/**
	 * Saves the size of the shell to a file
	 * @param shell The shell to save the size from
	 */
	public void saveSizeLocation( Shell shell ) {
		
		internalPrefStore.setValue( "coolbarLocked", coolbarLocked );
		internalPrefStore.setValue( "toolbarSmallButtons", toolbarSmallButtons );
	
		if ( shell.getMaximized() )
			internalPrefStore.setValue( "windowMaximized", shell.getMaximized() );
		else {
			PreferenceConverter.setValue(internalPrefStore, "windowBounds", shell.getBounds());
			internalPrefStore.setValue( "windowMaximized", shell.getMaximized() );
		}
	}
	
	// find something better
	private void createImageRegistry () {
		
		imageRegistry.put("ProgramIcon", createTrans("mld_logo_48x48.png"));
	
		imageRegistry.put("PreferencesButton", createTrans("preferences.png"));
		imageRegistry.put("PreferencesButtonSmall", createTrans("preferences-16.png"));
		
		imageRegistry.put("StatisticsButton", createTrans("statistics.png"));
		imageRegistry.put("StatisticsButtonSmall", createTrans("statistics-16.png"));
	
		imageRegistry.put("ConsoleButton", createTrans("console.png"));
		imageRegistry.put("ConsoleButtonSmall", createTrans("console-16.png"));
	
		imageRegistry.put("TransfersButton", createTrans("transfer2.png"));
		imageRegistry.put("TransfersButtonSmall", createTrans("transfer2-16.png"));
		
		imageRegistry.put("SearchButton", createTrans("search.png"));
		imageRegistry.put("SearchButtonSmall", createTrans("search-16.png")); 
			
	}
	// transparent pngs on gtk still require this, but it is noticable on win :(
	// will all buttons require the same background colour as thisShell? not sure yet...
	private Image createTrans(String filename) {
		String imagePath = "src/icons/";
		return createTransparentImage(new Image(null, imagePath + filename), thisShell);
	}
	public static Image getImageFromRegistry (String key) {
		return imageRegistry.get(key);
	}
} 

/*
$Log: MainTab.java,v $
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
für preferences

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