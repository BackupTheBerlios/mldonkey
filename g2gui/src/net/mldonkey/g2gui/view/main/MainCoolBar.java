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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.mldonkey.g2gui.view.MainTab;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.toolbar.ToolButton;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * CoolBar
 *
 * @author $Author: lemmster $
 * @version $Id: MainCoolBar.java,v 1.1 2003/08/23 14:58:38 lemmster Exp $ 
 *
 */
public class MainCoolBar {
	private static PreferenceStore internalPrefStore = new PreferenceStore( "g2gui-internal.pref" );
	private final Cursor handCursor = new Cursor( Display.getDefault(), SWT.CURSOR_HAND );
	private boolean toolbarSmallButtons;
	private boolean coolbarLocked = true;
	private Shell shell;
	private MainTab mainTab;
	private Composite composite;
	private CoolBar coolbar;
	private ToolBar miscTools, mainTools;
	private List miscToolButtons;
	private List mainToolButtons;

	/**
	 * 
	 * @param mainTab
	 */
	public MainCoolBar( MainTab mainTab ) {
		this.mainTab = mainTab;
		this.shell = mainTab.getShell();
		this.mainToolButtons = new ArrayList();
		this.miscToolButtons = new ArrayList();
		this.createInternalPrefStore();
		this.getInternalPrefStoreSettings();
		this.createContent( mainTab.getMainComposite() );
	}
	
	/**
	 * Reads the preference store file from disk
	 */
	private void createInternalPrefStore() {
		try {			
			internalPrefStore.load();
		}
		catch ( IOException e ) { }
	}
	
	/**
	 * 
	 */
	public void getInternalPrefStoreSettings() {
		internalPrefStore.setDefault( "coolbarLocked", this.isCoolbarLocked() );
		
		this.setToolbarSmallButtons( internalPrefStore.getBoolean( "toolbarSmallButtons" ) );
		this.setCoolbarLocked( internalPrefStore.getBoolean( "coolbarLocked" ) );
	}
	
	/**
	 * @param composite
	 */	
	private void createContent( Composite parent ) {
		composite = new Composite( parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		composite.setLayout ( gridLayout );
		composite.setLayoutData( new GridData ( GridData.FILL_HORIZONTAL ) );
 
		createCoolBar();
		createToolBars();
		createCoolItems();
		createMiscTools();

	}

	/**
	 * Creates a new Coolbar obj
	 * @param parent The parent Composite to display in
	 * @return a new CoolBar obj
	 */
	private void createCoolBar() {
		coolbar = new CoolBar ( this.composite, SWT.FLAT );
	
		coolbar.addControlListener( new ControlListener() {
			public void controlMoved( ControlEvent e ) {
				composite.getParent().layout();
			}
			public void controlResized( ControlEvent e ) { 
				composite.getParent().layout(); 
			}
		} );
		GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
		coolbar.setLayoutData( gridData );
	}

	/**
	 * 
	 * @param toggle
	 */
	private void toggleSmallButtons( boolean toggle ) {
		toolbarSmallButtons = !toolbarSmallButtons;
		
		coolbar.dispose();
		mainTools.dispose();
		miscTools.dispose();
		
		createCoolBar();
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
				 tmp.useSmallButtons( toolbarSmallButtons );
			tmp.resetItem( miscTools );
		}	
		layoutCoolBar();
	}
	
	/**
	 * 
	 *
	 */
	private void createMiscTools() {
		final ToolButton prefButton = new ToolButton( miscTools, SWT.NONE );
		prefButton.setText( G2GuiResources.getString( "TT_PreferencesButton" ) );
		prefButton.setToolTipText( G2GuiResources.getString( "TT_PreferencesButtonToolTip" ) );
		
		Image bigImage = G2GuiResources.getImage( "PreferencesButton" );
		Image smallImage = G2GuiResources.getImage( "PreferencesButtonSmall" );
				
		prefButton.setBigActiveImage( bigImage );
		prefButton.setBigInactiveImage( bigImage );
		prefButton.setSmallActiveImage( smallImage );
		prefButton.setSmallInactiveImage( smallImage );
		prefButton.useSmallButtons( toolbarSmallButtons );		
		prefButton.setActive( false );
		this.miscToolButtons.add( prefButton );	
		
		prefButton.addListener( SWT.Selection, new Listener() {
			public void handleEvent( Event event ) {
				mainTab.openPreferences();	
				prefButton.setActive( false );
			}
		} );
	}

	/**
	 * creates a right-mouse-menue
	 * @return a right-Mouse menue
	 */
	private Menu createToolBarRMMenu( CoolBar coolBar ) {
		Menu menu = new Menu( shell , SWT.POP_UP );

		/* lock CoolBar */
		final MenuItem lockItem = new MenuItem( menu, SWT.CHECK );
		lockItem.setText( "Lock the Toolbars" );
		lockItem.setSelection(coolbarLocked);
		lockItem.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent e ) {
				coolbar.setLocked( lockItem.getSelection() );
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
	public void layoutCoolBar() {
		// This seems to work in xp/gtk - z			
		for ( int j = 0; j < coolbar.getItemCount(); j++ ) {	
			CoolItem tempCoolItem = coolbar.getItem( j );		
			ToolBar tempToolBar =  ( ToolBar ) tempCoolItem.getControl();
			Point pSize = tempToolBar.computeSize( SWT.DEFAULT, SWT.DEFAULT );
			pSize = tempCoolItem.computeSize( pSize.x, pSize.y );
			tempCoolItem.setSize( pSize );
			tempCoolItem.setMinimumSize( pSize );
		}
		coolbar.setLocked( coolbarLocked );
	} 
	
	/**
	 * 
	 *
	 */
	protected void createToolBars() {
		Menu toolmenu = createToolBarRMMenu( coolbar );
		
		mainTools = new ToolBar( coolbar, ( toolbarSmallButtons ? SWT.RIGHT : 0) | SWT.FLAT ) ;	
		mainTools.addMouseTrackListener( new ToolBarMouseTrackListener( mainTools ) );				
		mainTools.setMenu( toolmenu );
			
		miscTools = new ToolBar( coolbar, ( toolbarSmallButtons ? SWT.RIGHT : 0) | SWT.FLAT ) ;	
		miscTools.addMouseTrackListener (new ToolBarMouseTrackListener( miscTools ) );
		miscTools.setMenu( toolmenu );
	}
	
	/**
	 * 
	 *
	 */
	public void createCoolItems () {
		for ( int i = 0; i < 2; i++ ){
			CoolItem item = new CoolItem( coolbar, SWT.NONE );
		}
					
		CoolItem[] items = coolbar.getItems ();
						
		CoolItem mainCoolItem = items[ 0 ];
		mainCoolItem.setControl( mainTools );
				
		CoolItem miscCoolItem = items[ 1 ];
		miscCoolItem.setControl( miscTools );		
	} 

	/**
	 * @return
	 */
	public boolean isCoolbarLocked() {
		return coolbarLocked;
	}

	/**
	 * @return
	 */
	public List getMainToolButtons() {
		return mainToolButtons;
	}

	/**
	 * @return
	 */
	public boolean isToolbarSmallButtons() {
		return toolbarSmallButtons;
	}

	/**
	 * @return
	 */
	public ToolBar getMainTools() {
		return mainTools;
	}

	/**
	 * @return
	 */
	public Cursor getHandCursor() {
		return handCursor;
	}

	/**
	 * @param b
	 */
	public void setCoolbarLocked( boolean b ) {
		coolbarLocked = b;
	}

	/**
	 * @param b
	 */
	public void setToolbarSmallButtons( boolean b ) {
		toolbarSmallButtons = b;
	}

	/**
	 * 
	 * ToolBarMouseTrackListener
	 *
	 */
	public class ToolBarMouseTrackListener implements MouseTrackListener {
		ToolBar toolBar;

		public ToolBarMouseTrackListener( final ToolBar toolBar ) {
			this.toolBar = toolBar;
		}
		public void checkForItem( MouseEvent event ) {
			Point pt = new Point( event.x, event.y );
			ToolBar t = ( ToolBar ) event.widget;
			if ( t.getItem( pt ) instanceof ToolItem )
				toolBar.setCursor( handCursor );
			else
				toolBar.setCursor( null );
		}
		public void mouseHover( MouseEvent event ) {
			checkForItem( event );
		}
		public void mouseEnter( MouseEvent event ) {
			checkForItem( event );
		}
		public void mouseExit( MouseEvent event ) {
			toolBar.setCursor( null );
		}
	}
}

/*
$Log: MainCoolBar.java,v $
Revision 1.1  2003/08/23 14:58:38  lemmster
cleanup of MainTab, transferTree.* broken

*/