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
package net.mldonkey.g2gui.view.main;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.mldonkey.g2gui.helper.RegExp;
import net.mldonkey.g2gui.view.MainWindow;
import net.mldonkey.g2gui.view.helper.WidgetFactory;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.toolbar.ToolButton;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;

/**
 * CoolBar
 *
 *
 * @version $Id: MainCoolBar.java,v 1.27 2004/03/26 01:15:03 psy Exp $
 *
 */
public class MainCoolBar {
    private boolean toolbarSmallButtons;
    private boolean coolbarLocked = true;
    private Shell shell;
    private MainWindow mainTab;
    private Composite composite;
    private CoolBar coolbar;
    private ToolBar miscTools;
    private ToolBar mainTools;
    private List miscToolButtons;
    private List mainToolButtons;
    protected int[] order = { 0, 1 };
    protected Point[] itemsizes = { new Point( 0, 0 ), new Point( 0, 0 ) };
	protected boolean layoutChanged = false;

	/**
	 * @param mainTab 
	 * @param size 
	 * @param locked 
	 */
    public MainCoolBar( MainWindow mainTab, boolean size, boolean locked ) {
        this.toolbarSmallButtons = size;
        this.coolbarLocked = locked;
        this.mainTab = mainTab;
        this.shell = mainTab.getShell();
        this.mainToolButtons = new ArrayList();
        this.miscToolButtons = new ArrayList();
        this.createContent( mainTab.getMainComposite() );
    }

    /**
     * @param parent 
     */
    private void createContent( Composite parent ) {
        composite = new Composite( parent, SWT.NONE );
        GridLayout gridLayout = WidgetFactory.createGridLayout( 1, 0, 0, 0, 0, false );
        composite.setLayout( gridLayout );
        composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
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
        coolbar = new CoolBar( this.composite, SWT.FLAT );
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
            ToolButton tmp = ( ToolButton ) toolButtonIterator.next();
            tmp.useSmallButtons( toolbarSmallButtons );
            tmp.resetItem( mainTools );
        }
        toolButtonIterator = miscToolButtons.iterator();
        while ( toolButtonIterator.hasNext() ) {
            ToolButton tmp = ( ToolButton ) toolButtonIterator.next();
            tmp.useSmallButtons( toolbarSmallButtons );
            tmp.resetItem( miscTools );
        }
        layoutCoolBar();
    }

    private void createMiscTools() {
        final ToolButton prefButton = new ToolButton( miscTools, SWT.NONE );
        prefButton.setText( G2GuiResources.getString( "TT_PreferencesButton" ) );
        prefButton.setToolTipText( G2GuiResources.getString( "TT_PreferencesButtonToolTip" ) );
        prefButton.setBigActiveImage( G2GuiResources.getImage( "PreferencesButtonActive" ) );
        prefButton.setBigInactiveImage( G2GuiResources.getImage( "PreferencesButton" ) );
        prefButton.setSmallActiveImage( G2GuiResources.getImage( "PreferencesButtonSmallActive" ) );
        prefButton.setSmallInactiveImage( G2GuiResources.getImage( "PreferencesButtonSmall" ) );
        prefButton.useSmallButtons( toolbarSmallButtons );
        prefButton.setActive( false );
        prefButton.resetImage();
        this.miscToolButtons.add( prefButton );
        prefButton.addListener( SWT.Selection,
                                new Listener() {
                public void handleEvent( Event event ) {
                    prefButton.setActive( true );
                    mainTab.openPreferences();
                    prefButton.setActive( false );
                }
            } );
    }

    /**
     * @param coolBar 
     * @return 
     */
    private Menu createToolBarRMMenu() {
        Menu menu = new Menu( shell, SWT.POP_UP );
        /* lock CoolBar */
        final MenuItem lockItem = new MenuItem( menu, SWT.CHECK );
        lockItem.setText( "Lock the Toolbars" );
        lockItem.setSelection( coolbarLocked );
        lockItem.addSelectionListener( new SelectionAdapter() {
                public void widgetSelected( SelectionEvent e ) {
                    coolbar.setLocked( lockItem.getSelection() );
                    coolbarLocked = lockItem.getSelection();
                }
            } );

        /* toggle small buttons */
        final MenuItem toggleButtonsItem = new MenuItem( menu, SWT.CHECK );
        toggleButtonsItem.setText( "Small buttons" );
        toggleButtonsItem.setSelection( toolbarSmallButtons );
        toggleButtonsItem.addSelectionListener( new SelectionAdapter() {
                public void widgetSelected( SelectionEvent e ) {
                    toggleSmallButtons( toggleButtonsItem.getSelection() );
                }
            } );
        return menu;
    }

    public void layoutCoolBar() {
        // This seems to work in xp/gtk - z			
        for ( int j = 0; j < coolbar.getItemCount(); j++ ) {
            CoolItem tempCoolItem = coolbar.getItem( j );
            ToolBar tempToolBar = ( ToolBar ) tempCoolItem.getControl();
            Point pSize = tempToolBar.computeSize( SWT.DEFAULT, SWT.DEFAULT );
            pSize = tempCoolItem.computeSize( pSize.x, pSize.y );
            tempCoolItem.setSize( pSize );
            tempCoolItem.setMinimumSize( pSize );
        }
        coolbar.setLocked( coolbarLocked );
    }

    protected void createToolBars() {
        Menu toolmenu = createToolBarRMMenu();
        mainTools = new ToolBar( coolbar, ( toolbarSmallButtons ? SWT.RIGHT : 0 ) | SWT.FLAT );
        mainTools.setMenu( toolmenu );
        miscTools = new ToolBar( coolbar, ( toolbarSmallButtons ? SWT.RIGHT : 0 ) | SWT.FLAT );
        miscTools.setMenu( toolmenu );
    }

    public void createCoolItems() {
        for ( int i = 0; i < 2; i++ ) {
            new CoolItem( coolbar, SWT.NONE );
        }
        CoolItem[] items = coolbar.getItems();
        CoolItem mainCoolItem = items[ 0 ];
        mainCoolItem.setControl( mainTools );
        CoolItem miscCoolItem = items[ 1 ];
        miscCoolItem.setControl( miscTools );
        
        addListeners();
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
     * resores the saved Layout and applies it to the coolBar
     */
    public void restoreLayout() {
    	PreferenceLoader.setDefault( "coolBarSizes", "0-0|0-0|" );    	
    	PreferenceLoader.setDefault( "coolBarOrder", "0|1|" );
    	
    	String sizesString = PreferenceLoader.getString( "coolBarSizes" );
    	String orderString = PreferenceLoader.getString( "coolBarOrder" );
    	
    	String[] sizes = RegExp.split( sizesString, '|' );
    	Point[] itemSizes = new Point[ sizes.length ];
    	
    	int[] wrap = {0,0};

    	int[] order = new int[ sizes.length ];
    	
    	/*
    	 * we have sizes.length coolBarItems, and sizes are stored in Points
    	 * recreating Point[] from String[]:
    	 */
    	
    	for ( int i = 0; i < sizes.length; i++ ) {
    		String[] coordinates = RegExp.split( sizes[ i ], '-' );
    		int x = Integer.parseInt( coordinates[ 0 ] );
    		int y = Integer.parseInt( coordinates[ 1 ] );
    		itemSizes[ i ] = new Point( x, y );     		
    	}    	
    	
    	String[] orders = RegExp.split( orderString, '|' );
    	for ( int i = 0; i < orders.length; i++ ) {
    		order[ i ] = Integer.parseInt( orders[ i ] );
    	} 
    	
    	layoutCoolBar();
    	coolbar.setItemLayout( order, wrap, itemSizes );
    	if ( PreferenceLoader.isDefault( "coolBarSizes" ) ) layoutCoolBar();
    }
    
    /**
     *  saves the Layout of the coolbar when beein disposed
     */
    public void saveLayout() {
    	PreferenceLoader.setValue( "coolbarLocked", isCoolbarLocked() );
    	PreferenceLoader.setValue( "toolbarSmallButtons", isToolbarSmallButtons() );
    	
    	StringBuffer sizesBuffer = new StringBuffer();    	
    	
    	for ( int i = 0; i < itemsizes.length; i++ ) {
    		sizesBuffer.append( itemsizes[ i ].x+"-"+itemsizes[ i ].y+"|" );			
    	}				
    	
    	
    	StringBuffer orderBuffer = new StringBuffer();
    	for ( int i = 0; i < order.length; i++ ) {    		
    		orderBuffer.append( order[ i ]+"|" );
    		
    	}
    	
    	/*
    	 * check if items have been moved, if not, don't save, because
    	 * this would save a 0,0-size for all items, so that they are not visible 
    	 * the next startup
    	 */
    	if ( layoutChanged ) {
    		PreferenceLoader.setValue( "coolBarSizes", sizesBuffer.toString() );	    	
    		PreferenceLoader.setValue( "coolBarOrder", orderBuffer.toString() );
    	}
    	
    }

    protected void udateLayoutStore() {
    	itemsizes = coolbar.getItemSizes();
    	order = coolbar.getItemOrder();
    	layoutChanged = true;
    }


    private void addListeners() {
    	/*save the Layout, if anyone even clicks in coolBar*/
    	
    	coolbar.addMouseListener( new MouseListener(){

    		public void mouseDoubleClick( MouseEvent e ) {
    			udateLayoutStore();    			
    		}

    		public void mouseDown( MouseEvent e ) {
    			udateLayoutStore();    			
    		}

    		public void mouseUp( MouseEvent e ) {
    			udateLayoutStore();    			
    		}
				
			} );
    	
    	coolbar.addDisposeListener( new DisposeListener(){

			public void widgetDisposed( DisposeEvent e ) {
				saveLayout();
				
			}} );

    }


}

/*


$Log: MainCoolBar.java,v $
Revision 1.27  2004/03/26 01:15:03  psy
more intelligent way to handle relaunches and quittings

Revision 1.26  2004/01/28 22:15:35  psy
* Properly handle disconnections from the core
* Fast inline-reconnect
* Ask for automatic relaunch if options have been changed which require it
* Improved the local core-controller

Revision 1.25  2003/12/04 08:47:31  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.24  2003/12/03 22:19:35  lemmy
store g2gui.pref in ~/.g2gui/g2gui.pref instead of the program directory

Revision 1.23  2003/11/29 17:01:09  zet
update for mainWindow

Revision 1.22  2003/11/25 17:24:30  dek
some refactoring and minor checkstyle

Revision 1.21  2003/11/25 17:17:51  dek
Cool-Bar saving finally works

Revision 1.20  2003/11/25 17:06:50  dek
yet another test for coolBar

removed some testing-comments, so don't care if they are missing ;- )

Revision 1.12  2003/11/23 17:58:03  lemmy
removed dead/unused code

Revision 1.11  2003/11/22 02:24:30  zet
widgetfactory & save sash postions/states between sessions

Revision 1.10  2003/10/11 21:32:32  zet
remove hand cursor ( looks weird on gtk )

Revision 1.9  2003/09/18 10:12:53  lemmy
checkstyle

Revision 1.8  2003/09/01 00:44:21  zet
use hotimage

Revision 1.7  2003/08/31 20:32:50  zet
active button states

Revision 1.6  2003/08/28 22:44:30  zet
GridLayout helper class

Revision 1.5  2003/08/23 16:18:44  lemmy
fixed locked/button size

Revision 1.4  2003/08/23 15:55:23  zet
remove unneeded import

Revision 1.3  2003/08/23 15:49:28  lemmy
fix for prefs and refactoring

Revision 1.2  2003/08/23 15:21:37  zet
remove @author

Revision 1.1  2003/08/23 14:58:38  lemmy
cleanup of MainTab, transferTree.* broken

*/
