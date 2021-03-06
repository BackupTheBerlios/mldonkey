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

import net.mldonkey.g2gui.view.helper.SashViewFrame;
import net.mldonkey.g2gui.view.helper.WidgetFactory;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.search.CompositeSearch;
import net.mldonkey.g2gui.view.search.MusicComplexSearch;
import net.mldonkey.g2gui.view.search.OtherComplexSearch;
import net.mldonkey.g2gui.view.search.ResultViewFrame;
import net.mldonkey.g2gui.view.search.Search;
import net.mldonkey.g2gui.view.search.SearchResult;
import net.mldonkey.g2gui.view.search.SimpleSearch;
import net.mldonkey.g2gui.view.viewers.GView;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolderAdapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * SearchTab
 *
 *
 * @version $Id: SearchTab.java,v 1.51 2004/01/12 20:59:16 psy Exp $ 
 *
 */
public class SearchTab extends GuiTab {
	private CTabFolder tabFolder;
	private CTabFolder cTabFolder;
	private StackLayout stackLayout;
	private Composite composite;
	private Button[] buttons;
	private MenuManager resultsPopupMenu;
	
	/**
	 * @param mainWindow
	 */
	public SearchTab( MainWindow mainWindow ) {
		super(mainWindow, "SearchButton");
	}

	/**
	 * Create all tabs we want to display inside the TabFolder
	 * @return The tabs to display
	 */	
	private Search[] createTab() {
		if ( PreferenceLoader.loadBoolean( "advancedMode" ) ) {
			List aList = new ArrayList();
			aList.add( new MusicComplexSearch( getCore(), this ) );
			aList.add( new OtherComplexSearch( getCore(), this ) );
			
			return new Search[] {
				new SimpleSearch( getCore(), this ),
				new CompositeSearch( getCore(), this, aList )
			};
		}
		else
			return new Search[] { new SimpleSearch( getCore(), this ) };	
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.GuiTab#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected void createContents( Composite parent ) {
		/* Create a sashForm */	
		SashForm mainSash = new SashForm( parent, SWT.HORIZONTAL );
		
		/* Create the "Left" and "Right" columns */
		this.createLeftGroup (mainSash);
		this.createRightGroup (mainSash);
		mainSash.setWeights( PreferenceLoader.getIntArray( "searchSashWeights" ) );
	}

	/**
	 * The search mask
	 */
	private void createLeftGroup(SashForm mainSash) {
	    SashViewFrame searchViewFrame = new SashViewFrame( mainSash, "TT_SearchButton", "SearchButtonSmall", this);
	    // this is a little bit "hacky" but getWeights() on a SashForm in a DisposeListener returns always an empty intarray
	    // so we are listening for the viewform moving and store the size in this very moment
	    final SashForm aSash = mainSash;
	    searchViewFrame.getViewForm().addControlListener( new ControlListener() {
			public void controlMoved(ControlEvent e) { }
			public void controlResized(ControlEvent e) {
				PreferenceLoader.setValue( "searchSashWeights", aSash.getWeights() );
			}
	    } );
	    
	    Composite aComposite = searchViewFrame.getChildComposite();
		aComposite.setLayout( WidgetFactory.createGridLayout(1,0,0,0,0,false));
		tabFolder = new CTabFolder( aComposite, SWT.NONE );
		tabFolder.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
				
		if (PreferenceLoader.loadBoolean("useGradient")) {
			tabFolder.setSelectionBackground( 
					new Color[] { tabFolder.getDisplay().getSystemColor( 
							SWT.COLOR_TITLE_BACKGROUND ), tabFolder.getBackground() }, new int[] { 75 } );
			tabFolder.setSelectionForeground( 
					tabFolder.getDisplay().getSystemColor( SWT.COLOR_TITLE_FOREGROUND ) );
		}
			
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

		Composite anotherComposite = new Composite( aComposite, SWT.NONE );
		anotherComposite.setLayout( WidgetFactory.createGridLayout(1,0,0,0,0,false) );
		anotherComposite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		createSearchButton( anotherComposite );
	}
	

	/**
	 * The result mask
	 */
	private void createRightGroup(SashForm mainSash) {
		/* right group */
	    
	    ResultViewFrame resultViewFrame = new ResultViewFrame( mainSash, "ST_RESULTS", "SearchButtonSmall", this );
		cTabFolder = resultViewFrame.getCTabFolder();
		
		cTabFolder.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		/* set this as data, so our children in the ctabfolder know whos their dad ;) */
		cTabFolder.setData( this );
		if (PreferenceLoader.loadBoolean("useGradient")) {
			cTabFolder.setSelectionBackground(
				new Color[] { cTabFolder.getDisplay().getSystemColor( SWT.COLOR_TITLE_BACKGROUND ),
					cTabFolder.getBackground() }, new int[] { 75 } );
			cTabFolder.setSelectionForeground(
				cTabFolder.getDisplay().getSystemColor( SWT.COLOR_TITLE_FOREGROUND ) );
		}
		/* add a "X" and listen for close event */
		cTabFolder.addCTabFolderListener( new CTabFolderAdapter() {
			public void itemClosed( CTabFolderEvent event ) {
				/* dispose the items control (to avoid arrayindexoutofbound?) */
				CTabItem item = (CTabItem) event.item;
				item.getControl().dispose();
				
				/* set the new statusline */
				if ( cTabFolder.getItemCount() != 0 ) {
					SearchResult nResult = (SearchResult) cTabFolder.getSelection().getData();
					getMainWindow().getStatusline().update( nResult.getStatusLine() );
					getMainWindow().getStatusline().updateToolTip( "" );
					setSearchButton();
				}
				else {
					getMainWindow().getStatusline().update( "" );
					getMainWindow().getStatusline().updateToolTip( "" );
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
					getMainWindow().getStatusline().update( result.getStatusLine() );
					getMainWindow().getStatusline().updateToolTip( "" );
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
Revision 1.51  2004/01/12 20:59:16  psy
outsourced' label string

Revision 1.50  2003/12/07 19:36:55  lemmy
[Bug #1162] Search tab's pane position always reset to default

Revision 1.49  2003/12/04 08:47:27  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.48  2003/11/29 19:28:38  zet
minor string move

Revision 1.47  2003/11/29 19:10:24  zet
small update.. continue later.
- mainwindow > tabs > viewframes(can contain gView)

Revision 1.46  2003/11/29 17:21:22  zet
minor cleanup

Revision 1.45  2003/11/29 17:01:00  zet
update for mainWindow

Revision 1.44  2003/11/28 13:36:17  lemmy
useGradient in headerbars

Revision 1.43  2003/11/28 13:24:17  lemmy
useGradient in headerbars

Revision 1.42  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

Revision 1.41  2003/11/16 10:26:25  lemmy
fix: [Bug #1080] Searchbox should get focused when switching to servertab

Revision 1.40  2003/11/04 20:38:27  zet
update for transparent gifs

Revision 1.39  2003/10/31 16:02:17  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.38  2003/10/31 13:20:31  lemmy
added PaneGuiTab and TableGuiTab
added "dropdown" button to all PaneGuiTabs (not finished yet, continue on monday)

Revision 1.37  2003/10/29 16:56:21  lemmy
added reasonable class hierarchy for panelisteners, viewers...

Revision 1.36  2003/10/28 00:35:58  zet
move columnselector into the pane

Revision 1.35  2003/10/22 01:36:59  zet
add column selector to server/search (might not be finished yet..)

Revision 1.34  2003/09/27 13:30:22  dek
all tables have now show-Gridlines-behaviour as descibed in  preferences

Revision 1.33  2003/09/26 07:13:41  lemmy
checkstyle!

Revision 1.32  2003/09/25 20:45:06  zet
remove that annoying empty space.........

Revision 1.31  2003/09/25 17:58:41  lemmy
fixed crash bug

Revision 1.30  2003/09/22 19:50:07  lemmy
searchbutton a little bit more beautiful.

Revision 1.29  2003/09/19 17:51:39  lemmy
minor bugfix

Revision 1.28  2003/09/19 15:19:14  lemmy
reworked

Revision 1.27  2003/09/18 09:44:57  lemmy
checkstyle

Revision 1.26  2003/09/08 10:25:26  lemmy
OtherComplexSearch added, rest improved

Revision 1.25  2003/09/05 23:49:07  zet
1 line per search option

Revision 1.24  2003/09/04 16:06:45  lemmy
working in progress

Revision 1.23  2003/09/04 02:35:24  zet
try a sash ?

Revision 1.22  2003/09/03 22:15:27  lemmy
advanced search introduced; not working and far from complete. just to see the design

Revision 1.21  2003/09/03 14:59:23  zet
remove margin

Revision 1.20  2003/09/03 14:47:54  zet
add headers

Revision 1.19  2003/09/01 11:09:43  lemmy
show downloading files

Revision 1.18  2003/08/31 12:32:04  lemmy
major changes to search

Revision 1.17  2003/08/29 19:09:25  dek
new look'n feel

Revision 1.16  2003/08/29 17:33:20  zet
remove headerbar

Revision 1.15  2003/08/23 15:21:37  zet
remove @author

Revision 1.14  2003/08/23 14:58:38  lemmy
cleanup of MainTab, transferTree.* broken

Revision 1.13  2003/08/22 21:06:48  lemmy
replace $user$ with $Author: psy $

Revision 1.12  2003/08/18 05:22:27  zet
remove image.dispose

Revision 1.11  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.10  2003/08/10 10:27:38  lemmy
bugfix and new image on table create

Revision 1.9  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.8  2003/07/31 14:23:57  lemmy
statusline reworked

Revision 1.7  2003/07/29 10:10:22  lemmy
moved icon folder out of src/

Revision 1.6  2003/07/29 09:56:52  lemmy
some importers removed

Revision 1.5  2003/07/29 09:42:46  lemmy
added support for the statusline

Revision 1.4  2003/07/27 22:39:36  zet
small buttons toggle (in popup) for main cool menu

Revision 1.3  2003/07/27 18:45:47  lemmy
lots of changes

Revision 1.2  2003/07/24 16:20:10  lemmy
lots of changes

Revision 1.1  2003/07/23 16:56:28  lemmy
initial commit

*/