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
package net.mldonkey.g2gui.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.helper.CCLabel;
import net.mldonkey.g2gui.view.helper.HeaderBarMouseAdapter;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.search.CompositeSearch;
import net.mldonkey.g2gui.view.search.MusicComplexSearch;
import net.mldonkey.g2gui.view.search.OtherComplexSearch;
import net.mldonkey.g2gui.view.search.ResultPaneListener;
import net.mldonkey.g2gui.view.search.Search;
import net.mldonkey.g2gui.view.search.SearchResult;
import net.mldonkey.g2gui.view.search.SimpleSearch;
import net.mldonkey.g2gui.view.viewers.GView;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolderAdapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;

/**
 * SearchTab
 *
 *
 * @version $Id: SearchTab.java,v 1.41 2003/11/16 10:26:25 lemmster Exp $ 
 *
 */
public class SearchTab extends PaneGuiTab {
	private SashForm mainSash;
	private CTabFolder tabFolder;
	private CTabFolder cTabFolder;
	private CoreCommunication core;
	private StackLayout stackLayout;
	private Composite composite;
	private Button[] buttons;
	private MenuManager resultsPopupMenu;
	
	/**
	 * Create a new Search Tab
	 * @param gui The parent Tab Page
	 */
	public SearchTab( MainTab gui ) {
		super( gui );
		/* associate this tab with the corecommunication */
		this.core = gui.getCore();
		/* Set our name on the coolbar */
		createButton( "SearchButton", 
							G2GuiResources.getString( "TT_SearchButton" ),
							G2GuiResources.getString( "TT_SearchButtonToolTip" ) );
		/* create the tab content */
		this.createContents( this.subContent );
	}

	/**
	 * Create all tabs we want to display inside the TabFolder
	 * @return The tabs to display
	 */	
	private Search[] createTab() {
		if ( PreferenceLoader.loadBoolean( "advancedMode" ) ) {
			List aList = new ArrayList();
			aList.add( new MusicComplexSearch( core, this ) );
			aList.add( new OtherComplexSearch( core, this ) );
			
			return new Search[] {
				new SimpleSearch( core, this ),
				new CompositeSearch( core, this, aList )
			};
		}
		else
			return new Search[] { new SimpleSearch( core, this ) };	
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.GuiTab#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected void createContents( Composite parent ) {
		/* Create a sashForm */	
		mainSash = new SashForm( parent, SWT.HORIZONTAL );
		
		/* Create the "Left" and "Right" columns */
		this.createLeftGroup ();
		this.createRightGroup ();
		
		mainSash.setWeights( new int[] { 1, 5 } );
	}

	/**
	 * The search mask
	 */
	private void createLeftGroup() {
		ViewForm viewForm = 
				new ViewForm( mainSash , SWT.BORDER
				| ( PreferenceLoader.loadBoolean( "flatInterface" ) ? SWT.FLAT : SWT.NONE ) );
		GridData gd = new GridData( GridData.FILL_VERTICAL );
		gd.widthHint = 150;
		viewForm.setLayoutData( gd );
		CLabel searchCLabel = 
			CCLabel.createCL( viewForm, "TT_SearchButton", "SearchButtonSmall" );

		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		Composite aComposite = new Composite( viewForm, SWT.NONE );
		aComposite.setLayout( gridLayout );

		tabFolder = new CTabFolder( aComposite, SWT.NONE );
		tabFolder.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
				
		tabFolder.setSelectionBackground( 
			new Color[] { tabFolder.getDisplay().getSystemColor( 
					SWT.COLOR_TITLE_BACKGROUND ), tabFolder.getBackground() }, new int[] { 75 } );
		tabFolder.setSelectionForeground( 
			tabFolder.getDisplay().getSystemColor( SWT.COLOR_TITLE_FOREGROUND ) );
				
		Search[] searchTabs = this.createTab();
		Control aControl;
		for ( int i = 0; i < searchTabs.length; i++ ) {
			CTabItem item = new CTabItem( tabFolder, SWT.NONE );
			
			item.setText( searchTabs[ i ].getTabName() );
			aControl = searchTabs[ i ].createTabFolderPage( tabFolder );
			if ( i == 0 ) 
				item.setControl( aControl );
			item.setData( "myControl", aControl );
			item.setData( searchTabs[ i ] );			
		}
		
		/* setControl on selection */
        tabFolder.addSelectionListener( new SelectionListener() {
            public void widgetDefaultSelected( SelectionEvent e ) { }
            public void widgetSelected( SelectionEvent e ) {
				CTabItem cTabItem = (CTabItem) e.item;
				cTabItem.setControl( (Control) cTabItem.getData( "myControl" ) );

                for ( int i = 0; i < tabFolder.getItems().length; i++ ) {
                	if ( tabFolder.getItems()[ i ] != e.item ) 
	                    tabFolder.getItems()[ i ].setControl( null );
                }
				tabFolder.getParent().layout();
            }
        } );
		tabFolder.setSelection( 0 );

		gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		Composite anotherComposite = new Composite( aComposite, SWT.NONE );
		anotherComposite.setLayout( gridLayout );
		anotherComposite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		createSearchButton( anotherComposite );
		
		viewForm.setContent( aComposite );
		viewForm.setTopLeft( searchCLabel );
	}
	

	/**
	 * The result mask
	 */
	private void createRightGroup() {
		/* right group */
		ViewForm searchResultsViewForm = 
			new ViewForm( mainSash , SWT.BORDER
			| ( PreferenceLoader.loadBoolean( "flatInterface" ) ? SWT.FLAT : SWT.NONE ) );
		searchResultsViewForm.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		
		CLabel searchResultsCLabel = 
			CCLabel.createCL( searchResultsViewForm,
								 G2GuiResources.getString( "ST_RESULTS" ), "SearchButtonSmall" );
		
		cTabFolder = new CTabFolder( searchResultsViewForm, SWT.NONE );
		
		resultsPopupMenu = new MenuManager( "" );
		resultsPopupMenu.setRemoveAllWhenShown( true );
		resultsPopupMenu.addMenuListener(new ResultPaneListener(cTabFolder, core));
		searchResultsCLabel.addMouseListener( new HeaderBarMouseAdapter( searchResultsCLabel, resultsPopupMenu ) );
		
		searchResultsViewForm.setContent( cTabFolder );
		searchResultsViewForm.setTopLeft( searchResultsCLabel );
		
		cTabFolder.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		/* set this as data, so our children in the ctabfolder know whos their dad ;) */
		cTabFolder.setData( this );
		cTabFolder.setSelectionBackground(
			new Color[] { cTabFolder.getDisplay().getSystemColor( SWT.COLOR_TITLE_BACKGROUND ),
				cTabFolder.getBackground() }, new int[] { 75 } );
		cTabFolder.setSelectionForeground(
			cTabFolder.getDisplay().getSystemColor( SWT.COLOR_TITLE_FOREGROUND ) );
		
		/* add a "X" and listen for close event */
		cTabFolder.addCTabFolderListener( new CTabFolderAdapter() {
			public void itemClosed( CTabFolderEvent event ) {
				/* dispose the items control (to avoid arrayindexoutofbound?) */
				CTabItem item = (CTabItem) event.item;
				item.getControl().dispose();
				
				/* set the new statusline */
				if ( cTabFolder.getItemCount() != 0 ) {
					SearchResult nResult = (SearchResult) cTabFolder.getSelection().getData();
					mainWindow.getStatusline().update( nResult.getStatusLine() );
					mainWindow.getStatusline().updateToolTip( "" );
					setSearchButton();
				}
				else {
					mainWindow.getStatusline().update( "" );
					mainWindow.getStatusline().updateToolTip( "" );
				}
			}
		} );
		
		/* add a focus listener to set the status line */
		cTabFolder.addFocusListener( new FocusListener () {
			public void focusGained( FocusEvent e ) { }

			public void focusLost( FocusEvent e ) { 
				/* we are in focus, set our result count */
				CTabFolder item = (CTabFolder) e.widget;
				if ( item.getSelection() != null ) {
					SearchResult result = (SearchResult) item.getSelection().getData();
					mainWindow.getStatusline().update( result.getStatusLine() );
					mainWindow.getStatusline().updateToolTip( "" );
				}
			}
		} );
		
		/* add a mouse listener to set the correct button when the item gets focus */
		cTabFolder.addMouseListener( new MouseListener() {
			public void mouseDoubleClick( MouseEvent e ) { }

			public void mouseDown( MouseEvent e ) {
				CTabFolder item = (CTabFolder) e.widget;
				if ( item.getSelection() != null ) {
					SearchResult result = (SearchResult) item.getSelection().getData();
					if ( result.isStopped() )
						setContinueButton();
					else
						setStopButton();
				}		
			}
			public void mouseUp( MouseEvent e ) { }
		} );
	}

	private void createPaneToolBar( ViewForm aViewForm, IMenuListener menuListener ) {
		ToolBar toolBar = new ToolBar(aViewForm, SWT.RIGHT | SWT.FLAT);

		super.createPaneToolBar( toolBar, menuListener );   

		aViewForm.setTopRight( toolBar );
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update( Observable o, Object arg ) { }
	
	/**
	 *@return The string for the statusline
	 */
	public String getStatusText() {
		/* we have items */
		if ( cTabFolder.getItemCount() != 0 ) {
			CTabItem item = cTabFolder.getSelection();
			SearchResult result = (SearchResult) item.getData();
			return result.getStatusLine();
		}
		return "";
	}

	/**
	 * @return The TabFolder where you can add TabItems
	 */
	public CTabFolder getCTabFolder() {
		return cTabFolder;
	}
	
	/**
	 * @return The current active <code>SearchResult</code>
	 */
	private SearchResult getSearchResult() {
		CTabItem item = cTabFolder.getSelection();
		SearchResult result = (SearchResult) item.getData();
		return result;
	}
	
	/**
	 * @return The search masks
	 */
	private Search getSearch() {
		CTabItem item = tabFolder.getSelection();
		Search result = (Search) item.getData();
		return result;
	}
	
	/**
	 * @return The parent maintab window
	 */
	public MainTab getMainTab() {
		return this.mainWindow;
	}

	/**
	 * Set all <code>Button</code> in the <code>Search</code>
	 * to the search-state
	 */
	public void setSearchButton() {
		this.stackLayout.topControl = this.buttons[ 2 ];
		this.composite.layout();
	}

	/**
	 * Set all <code>Button</code> in the <code>Search</code>
	 * to the continue-state
	 */
	public void setContinueButton() {
		this.stackLayout.topControl = this.buttons[ 1 ];
		this.composite.layout();
	}

	/**
	 * Set all <code>Button</code> in the <code>Search</code>
	 * to the stop-state
	 */
	public void setStopButton() {
		this.stackLayout.topControl = this.buttons[ 0 ];
		this.composite.layout();
	}

	/**
	 * Creates a <code>StackLayout</code> with three buttons. Search/Stop/Continue
	 *
	 * @param group The <code>Composite</code> to draw the buttons in
	 */
	private void createSearchButton( Composite group ) {
		this.stackLayout = new StackLayout();
		this.composite = new Composite( group, SWT.NONE );
		GridData gridData = new GridData( GridData.FILL_HORIZONTAL ) ;
		gridData.horizontalSpan = 2;
		this.composite.setLayoutData( gridData );
		this.composite.setLayout( stackLayout );

		this.buttons = new Button[ 3 ];
		Button stopButton = new Button( this.composite, SWT.PUSH );
		stopButton.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		stopButton.setText( G2GuiResources.getString( "SS_STOP" ) );
		stopButton.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent event ) {
					getSearchResult().stopSearch();
					setContinueButton();
				}
			} );
		this.buttons[ 0 ] = stopButton;

		Button continueButton = new Button( this.composite, SWT.PUSH );
		continueButton.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		continueButton.setText( G2GuiResources.getString( "SS_CONTINUE" ) );
		continueButton.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent event ) {
					getSearchResult().continueSearch();
					setStopButton();
				}
			} );
		this.buttons[ 1 ] = continueButton;

		Button searchButton = new Button( this.composite, SWT.PUSH );
		searchButton.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		searchButton.setText( G2GuiResources.getString( "SS_SEARCH" ) );
		searchButton.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected( SelectionEvent event ) {
					getSearch().performSearch();
				}			
			} );
		this.buttons[ 2 ] = searchButton;
        
		this.stackLayout.topControl = buttons[ 2 ];
	}
	
	/**
	 * refreshes the Display with values from preference-Store.
	 * This method is called, when preference-Sotre is closed, that means, something
	 * might have benn changed.
	 */
	public void updateDisplay() {		
		super.updateDisplay();
		
		/* update all search-results with changed preferences */		
		CTabItem[] items = cTabFolder.getItems();
		for ( int i = 0; i < items.length; i++ ) {
			if ( items[ i ].getData() instanceof SearchResult ) {
				SearchResult temp = (SearchResult) items[ i ].getData();
				temp.updateDisplay();				
			}			
		}
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.PaneGuiTab#getGPage()
	 */
	public GView getGView() {
		CTabItem item = this.getCTabFolder().getSelection();
		SearchResult searchResult = (SearchResult) item.getData();
		return searchResult.getGView();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.GuiTab#setActive()
	 */
	public void setActive() {
		super.setActive();
		
		// The the current selected Search inputText to focus
		this.getSearch().setFocus();
	}
}

/*
$Log: SearchTab.java,v $
Revision 1.41  2003/11/16 10:26:25  lemmster
fix: [Bug #1080] Searchbox should get focused when switching to servertab

Revision 1.40  2003/11/04 20:38:27  zet
update for transparent gifs

Revision 1.39  2003/10/31 16:02:17  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.38  2003/10/31 13:20:31  lemmster
added PaneGuiTab and TableGuiTab
added "dropdown" button to all PaneGuiTabs (not finished yet, continue on monday)

Revision 1.37  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

Revision 1.36  2003/10/28 00:35:58  zet
move columnselector into the pane

Revision 1.35  2003/10/22 01:36:59  zet
add column selector to server/search (might not be finished yet..)

Revision 1.34  2003/09/27 13:30:22  dek
all tables have now show-Gridlines-behaviour as descibed in  preferences

Revision 1.33  2003/09/26 07:13:41  lemmster
checkstyle!

Revision 1.32  2003/09/25 20:45:06  zet
remove that annoying empty space.........

Revision 1.31  2003/09/25 17:58:41  lemmster
fixed crash bug

Revision 1.30  2003/09/22 19:50:07  lemmster
searchbutton a little bit more beautiful.

Revision 1.29  2003/09/19 17:51:39  lemmster
minor bugfix

Revision 1.28  2003/09/19 15:19:14  lemmster
reworked

Revision 1.27  2003/09/18 09:44:57  lemmster
checkstyle

Revision 1.26  2003/09/08 10:25:26  lemmster
OtherComplexSearch added, rest improved

Revision 1.25  2003/09/05 23:49:07  zet
1 line per search option

Revision 1.24  2003/09/04 16:06:45  lemmster
working in progress

Revision 1.23  2003/09/04 02:35:24  zet
try a sash ?

Revision 1.22  2003/09/03 22:15:27  lemmster
advanced search introduced; not working and far from complete. just to see the design

Revision 1.21  2003/09/03 14:59:23  zet
remove margin

Revision 1.20  2003/09/03 14:47:54  zet
add headers

Revision 1.19  2003/09/01 11:09:43  lemmster
show downloading files

Revision 1.18  2003/08/31 12:32:04  lemmster
major changes to search

Revision 1.17  2003/08/29 19:09:25  dek
new look'n feel

Revision 1.16  2003/08/29 17:33:20  zet
remove headerbar

Revision 1.15  2003/08/23 15:21:37  zet
remove @author

Revision 1.14  2003/08/23 14:58:38  lemmster
cleanup of MainTab, transferTree.* broken

Revision 1.13  2003/08/22 21:06:48  lemmster
replace $user$ with $Author: lemmster $

Revision 1.12  2003/08/18 05:22:27  zet
remove image.dispose

Revision 1.11  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.10  2003/08/10 10:27:38  lemmstercvs01
bugfix and new image on table create

Revision 1.9  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.8  2003/07/31 14:23:57  lemmstercvs01
statusline reworked

Revision 1.7  2003/07/29 10:10:22  lemmstercvs01
moved icon folder out of src/

Revision 1.6  2003/07/29 09:56:52  lemmstercvs01
some importers removed

Revision 1.5  2003/07/29 09:42:46  lemmstercvs01
added support for the statusline

Revision 1.4  2003/07/27 22:39:36  zet
small buttons toggle (in popup) for main cool menu

Revision 1.3  2003/07/27 18:45:47  lemmstercvs01
lots of changes

Revision 1.2  2003/07/24 16:20:10  lemmstercvs01
lots of changes

Revision 1.1  2003/07/23 16:56:28  lemmstercvs01
initial commit

*/