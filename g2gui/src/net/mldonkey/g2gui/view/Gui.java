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
import java.io.IOException;

import net.mldonkey.g2gui.comm.Core;
import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.pref.Preferences;
import net.mldonkey.g2gui.view.statusline.*;

import org.eclipse.jface.preference.PreferenceStore;
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
 * @version $Id: Gui.java,v 1.29 2003/07/06 19:25:45 dek Exp $ 
 *
 */
public class Gui implements IG2gui, Listener {	
	private StackLayout pageContainerLayout;
	private ToolBar miscTools;
	private CoolItem miscCoolItem;
	private CoolItem mainCoolItem;
	private ToolBar mainTools;
	private StatusLine statusline;
	private Composite miscButtonRow;
	private Composite buttonRow;
	private CoreCommunication mldonkey;
	private Composite mainComposite, 
					tabButtonRow, 
					pageContainer;
	private List registeredTabs = new ArrayList();
	private G2guiTab activeTab;
	private Menu mainMenuBar;
	private Shell mainShell;
	private PreferenceStore internalPrefStore = new PreferenceStore("g2gui-internal.pref");
	
	/**
	 * @param core the most important thing of the gui: were do i get my data from
	 * @param shell were do we live?
	 */
	public Gui( CoreCommunication core, Shell shell ) {
		mainShell = shell;	
		Display display = shell.getDisplay();	
				
		this.mldonkey = core;
		shell.setLayout( new FillLayout() );
		createInternalPrefStore();
		createContents( shell );
		setSizeLocation( shell );
		shell.pack ();
		shell.open ();
		shell.addDisposeListener( new DisposeListener() {
			public synchronized void widgetDisposed( DisposeEvent e ) {
				saveSizeLocation( mainShell );
				saveInternalPrefStore();
				( ( Core )mldonkey ).disconnect();				
			} } );
		while ( !shell.isDisposed () ) {
			if ( !display.readAndDispatch () ) display.sleep ();
		} 
		display.dispose();
	
		
	} 
	
	/* ( non-Javadoc )
	 * @see org.eclipse.jface.window.Window#createContents( org.eclipse.swt.widgets.Composite )
	 */
	private void createContents( Composite parent ) {
		setTitleBar( "g2gui alpha" );
		createMenuBar();
		CoolBar coolbar = null;				
		GridData gridData;		
		mainComposite = new Composite( parent, SWT.NONE );
		
		GridLayout mainLayout = new GridLayout();
			mainLayout.numColumns = 1;
			mainLayout.marginWidth = 0;
			mainLayout.marginHeight = 0;
			mainLayout.verticalSpacing = 1;
			mainComposite.setLayout( mainLayout ); 
			
		Label horLine = new Label( mainComposite, SWT.HORIZONTAL | SWT.SEPARATOR );
			gridData = new GridData( GridData.FILL_HORIZONTAL );
			horLine.setLayoutData( gridData );
		
		coolbar = createCoolBar( mainComposite );				
			
		ToolItem pref = new ToolItem( miscTools, SWT.NONE );				
			pref.setText( "Preferences" );	
			pref.setImage( Gui.createTransparentImage( 
					new Image( pref.getParent().getDisplay(), 
							"src/icons/preferences.png" ), 
							  pref.getParent() ) );
			pref.addListener( SWT.Selection, new Listener() {
				public void handleEvent( Event event ) {	
					Shell prefshell = new Shell();
					Preferences myprefs = new Preferences( new PreferenceStore( "g2gui.pref" ) );					
					myprefs.open( prefshell, mldonkey );
			} } );
		
		pageContainer = new Composite( mainComposite, SWT.NONE );
		this.pageContainerLayout = new StackLayout();
		pageContainer.setLayout( pageContainerLayout );
							
		gridData = new GridData( GridData.FILL_BOTH );
			gridData.grabExcessHorizontalSpace = true;
			gridData.grabExcessVerticalSpace = true;			
			pageContainer.setLayoutData( gridData );
						
		addTabs();				
		statusline = new StatusLine( mainComposite, mldonkey );
				
		layoutCoolBar( coolbar );			

				

	} 
	
	private void createMenuBar () {
		mainMenuBar = new Menu( mainShell, SWT.BAR );
		mainShell.setMenuBar( mainMenuBar );
		
		MenuItem mItem = new MenuItem ( mainMenuBar, SWT.CASCADE );
		mItem.setText ( "File" );
			Menu submenu = new Menu ( mainShell, SWT.DROP_DOWN );
			mItem.setMenu ( submenu );
			MenuItem item = new MenuItem ( submenu, 0 );
			item.addListener ( SWT.Selection, new Listener () {
					public void handleEvent ( Event e ) {
						mainShell.close();
					} 
			} );
			item.setText ( "E&xit\tCtrl+W" );
			item.setAccelerator ( SWT.CTRL + 'W' );
			
		mItem = new MenuItem ( mainMenuBar, SWT.CASCADE );
		mItem.setText ( "View" );
		
		mItem = new MenuItem ( mainMenuBar, SWT.CASCADE );
		mItem.setText ( "Tools" );
		
			submenu = new Menu ( mainShell, SWT.DROP_DOWN );
			mItem.setMenu ( submenu );
			item = new MenuItem( submenu, 0 );
			item.addListener( SWT.Selection, new Listener() {
			public void handleEvent( Event event ) {	
				Shell prefshell = new Shell();
				Preferences myprefs = new Preferences( new PreferenceStore( "g2gui.pref" ) );					
				myprefs.open( prefshell, mldonkey );
			} } );
			item.setText ( "Preferences" );
							
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
	
	 private CoolBar createCoolBar( Composite parent ) {
		GridData gridData;
		
		/*now follows the creation of the CoolBar*/		
		final CoolBar coolBar = new CoolBar ( parent, SWT.FLAT );
			gridData = new GridData( GridData.FILL_HORIZONTAL );
			
		coolBar.addControlListener( new ControlListener() {
			public void controlMoved( ControlEvent e ) { mainComposite.layout(); }
			public void controlResized( ControlEvent e ) { mainComposite.layout(); }
			} );
			
			coolBar.setLayoutData( gridData );
		Menu toolmenu = createToolBarRMMenu( coolBar );
				for ( int i = 0; i < 2; i++ ) { 			
					CoolItem item = new CoolItem ( coolBar, SWT.NONE );
					} 
					
		CoolItem[] items = coolBar.getItems ();
					
		this.mainTools = new ToolBar( coolBar, SWT.FLAT );			
			mainCoolItem = items [0];
			mainCoolItem.setControl ( mainTools );
			mainTools.setMenu( toolmenu );
			
			
		this.miscTools = new ToolBar( coolBar, SWT.FLAT );
			miscCoolItem = items [1];
			miscCoolItem.setControl ( miscTools );		
			miscTools.setMenu( toolmenu );
		
		return coolBar;
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
		lockItem.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				thiscoolBar.setLocked( lockItem.getSelection() );
			}
		} );
		
		
		return menu;
	}

	private void layoutCoolBar( CoolBar coolBar ) {
	/*	int CoolBarWidth = this.mainComposite.getBounds().width;
		
		int CoolBarHeight = 0;
		for ( int j = 0; j < coolBar.getItemCount(); j++ ) {			
			ToolBar tempToolBar =  ( ToolBar ) coolBar.getItems()[j].getControl();			
			
			for ( int i = 0; i < tempToolBar.getItems().length; i++ ) {
				if ( tempToolBar.getItems()[i].getBounds().height > CoolBarHeight )
					CoolBarHeight = tempToolBar.getItems()[i].getBounds().height;	
				if ( tempToolBar.getItems()[i].getBounds().width > CoolBarWidth )	
					CoolBarWidth = tempToolBar.getItems()[i].getBounds().width;
				} 
		} 
		CoolBarWidth = CoolBarWidth*mainTools.getItemCount();		
		this.mainCoolItem.setSize ( mainCoolItem.computeSize ( CoolBarWidth, CoolBarHeight ) );	
		*/
		// This seems to work in xp/gtk - z			
		for ( int j = 0; j < coolBar.getItemCount(); j++ ) {	
			CoolItem tempCoolItem = coolBar.getItem( j );		
			ToolBar tempToolBar =  ( ToolBar ) tempCoolItem.getControl();			
			Point pSize = tempToolBar.computeSize( SWT.DEFAULT, SWT.DEFAULT );
			pSize = tempCoolItem.computeSize( pSize.x, pSize.y );
			tempCoolItem.setSize( pSize );
			tempCoolItem.setMinimumSize( pSize );
		}
		
	} 

	

	/**
	 * Here do we add the tabs, they must extend G2GuiTab. They are responsible 
	 * for the content and their button.
	 */
	private void addTabs() {
		// new TransferTab( this );
		new DownloadTab( this );		
		new ConsoleTab( this );
		/*setting TransferTab active if registered*/
		Iterator tabIterator = registeredTabs.iterator();
			while ( tabIterator.hasNext() ) {
				G2guiTab tempTab = ( G2guiTab )  tabIterator.next();
				if ( tempTab instanceof DownloadTab ) {									
					 this.setActive( tempTab );					 
					 }
					 					
			}		
	} 
	
	

	/* ( non-Javadoc )
	 * @see net.mldonkey.g2gui.view.widgets.Gui.IG2gui#registerTab( net.mldonkey.g2gui.view.widgets.Gui.G2guiTab )
	 */
	public void registerTab( G2guiTab newTab ) {
		registeredTabs.add( newTab );		
	} 

	/* ( non-Javadoc )
	 * @see org.eclipse.swt.widgets.Listener#handleEvent( org.eclipse.swt.widgets.Event )
	 */
	public void handleEvent( Event event ) {
		System.out.println( event.widget.toString() );
		
	} 
	/* ( non-Javadoc )
	 * @see net.mldonkey.g2gui.view.widgets.Gui.IG2gui#getButtonRow()
	 */
	public Composite getButtonRow() {
	
		return tabButtonRow;
	} 

	/* ( non-Javadoc )
	 * @see net.mldonkey.g2gui.view.widgets.Gui.IG2gui#getPageContainer()
	 */
	public Composite getPageContainer() {		
		return pageContainer;
	} 

	/* ( non-Javadoc )
	 * @see net.mldonkey.g2gui.view.widgets.Gui.IG2gui#setActive( net.mldonkey.g2gui.view.widgets.Gui.G2guiTab )
	 */
	public void setActive( G2guiTab activatedTab ) {		
		if ( activeTab != null ) activeTab.getContent().setVisible( false );
		pageContainerLayout.topControl = activatedTab.getContent();
		//activatedTab.getContent().setVisible( true );
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
	 * @return
	 */
	public ToolBar getMainTools() {
		return mainTools;
	} 

	public void setTitleBar( String title ) {
		mainShell.setText( title );
	} 
	public void setSizeLocation (Shell shell) {
			
		int windowW = internalPrefStore.getInt( "windowWidth" );
		int windowH = internalPrefStore.getInt( "windowHeight" );
		int windowPX = internalPrefStore.getInt ( "windowX" );
		int windowPY = internalPrefStore.getInt ( "windowY" );
		boolean windowMaximized = internalPrefStore.getBoolean ("windowMaximized");
		
		if (internalPrefStore.contains("windowX")) {
			if (windowMaximized) {
				shell.setMaximized(true);
			} else {
				shell.setBounds ( windowPX, windowPY, windowW, windowH );	
			}
		}
	}		

	public void saveSizeLocation (Shell shell) {
				
		Rectangle shellBounds = shell.getBounds();
							
		if (shell.getMaximized()) {
			internalPrefStore.setValue( "windowMaximized", shell.getMaximized());	
		} else {
			internalPrefStore.setValue( "windowWidth", shellBounds.width );
			internalPrefStore.setValue( "windowHeight", shellBounds.height );
			internalPrefStore.setValue( "windowX", shellBounds.x );
			internalPrefStore.setValue( "windowY", shellBounds.y);
			internalPrefStore.setValue( "windowMaximized", shell.getMaximized());
		}
	}

	public void createInternalPrefStore() {
		try {			
			internalPrefStore.load();
		} catch ( IOException e ) {
		}
	}

	public void saveInternalPrefStore() {
		try {
			internalPrefStore.save();
		} catch ( IOException e ) {
			System.out.println( "Saving g2gui-internal preferences failed" );
		}	
	}

	public PreferenceStore getStore() {
		return internalPrefStore;
	}

} 

/*
$Log: Gui.java,v $
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
f�r preferences

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