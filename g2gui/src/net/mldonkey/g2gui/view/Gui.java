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

import java.util.List;
import java.util.ArrayList;

import net.mldonkey.g2gui.comm.Core;
import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.pref.Preferences;
import net.mldonkey.g2gui.view.statusline.*;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;



/**
 * Gui
 *
 * @author $user$
 * @version $Id: Gui.java,v 1.19 2003/07/02 17:04:46 dek Exp $ 
 *
 */
public class Gui implements IG2gui, Listener {	
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
	
		/**
		 * Layout for the page container.
		 * This class is from the JFace-Package, i hope they don't
		 * mind
		 */
		private class PageLayout extends Layout {
			public void layout( Composite composite, boolean force ) {
				Rectangle rect = composite.getClientArea();
				Control[] children = composite.getChildren();
				for ( int i = 0; i < children.length; i++ ) {
					children[ i ].setSize( rect.width, rect.height );
				} 
			} 
			public Point computeSize( Composite composite, int wHint, int hHint, boolean force ) {
				if ( wHint != SWT.DEFAULT && hHint != SWT.DEFAULT )
					return new Point( wHint, hHint );
				
				int x = 100;
				int y = 200;

				Control[] children = composite.getChildren();
				for ( int i = 0; i < children.length; i++ ) {
					Point size =
						children[ i ].computeSize( SWT.DEFAULT, SWT.DEFAULT, force );
					x = Math.max( x, size.x );
					y = Math.max( y, size.y );
				} 
				if ( wHint != SWT.DEFAULT )
					x = wHint;
				if ( hHint != SWT.DEFAULT )
					y = hHint;
				return new Point( x, y );
			} 
		} 



	/**
	 * @param arg0 You know, what a shell is, right?
	 */
	public Gui( CoreCommunication core, Shell shell ) {
		mainShell = shell;	
		Display display = shell.getDisplay();	
		
		this.mldonkey = core;
		shell.setLayout( new FillLayout() );
		createContents( shell );
		shell.pack ();
		shell.open ();
		shell.addDisposeListener( new DisposeListener() {
			public synchronized void widgetDisposed( DisposeEvent e ) {
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
				
		parent.setSize( 640, 480 );
		GridData gridData;		
		mainComposite = new Composite( parent, SWT.NONE );
		
		GridLayout mainLayout = new GridLayout();
			mainLayout.numColumns = 1;
			mainLayout.marginWidth = 0;
			mainLayout.marginHeight = 0;
			mainComposite.setLayout( mainLayout );
			
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
		pageContainer.setLayout( new PageLayout() );						
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
	 * @param src the non-transparent image we want to process
	 * @param control where is our image laid in, to check for the background-color
	 * @return
	 */
	public static Image createTransparentImage( Image src, Control control ) {
		int width = src.getBounds().width;
		int height = src.getBounds().height;
		
		Image result = new Image( control.getDisplay(), new Rectangle( 0, 0, width, height ) );		
		GC gc = new GC( result );
				gc.setBackground( control.getBackground(  ) );
				gc.fillRectangle( 0, 0, width, height );							
				gc.drawImage( src, 0, 0 );
			
						
				gc.dispose();		
		return result;
			
	} 
	
	 private CoolBar createCoolBar( Composite parent ) {
		GridData gridData;
		
		Composite coolBarPanel = new Composite( parent, SWT.NONE );
		coolBarPanel.setLayout( new FillLayout() );
		gridData = new GridData( GridData.FILL_HORIZONTAL );
		coolBarPanel.setLayoutData( gridData );
		
		CoolBar coolBar = new CoolBar ( coolBarPanel, SWT.FLAT );		
			gridData = new GridData( GridData.FILL_VERTICAL );
			gridData.grabExcessHorizontalSpace = true;		
			gridData.horizontalAlignment = GridData.BEGINNING;
			coolBar.setLayoutData( gridData );
				for ( int i = 0; i < 2; i++ ) { 			
					CoolItem item = new CoolItem ( coolBar, SWT.NONE );
					} 
		CoolItem[] items = coolBar.getItems ();
					
		this.mainTools = new ToolBar( coolBar, SWT.FLAT );			
			mainCoolItem = items [0];
			mainCoolItem.setControl ( mainTools );
			
			
		this.miscTools = new ToolBar( coolBar, SWT.FLAT );
			miscCoolItem = items [1];
			miscCoolItem.setControl ( miscTools );		
			
		return coolBar;
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
		new TransferTab( this );
		new ConsoleTab( this );		
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
		activatedTab.getContent().setVisible( true );
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

} 

/*
$Log: Gui.java,v $
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