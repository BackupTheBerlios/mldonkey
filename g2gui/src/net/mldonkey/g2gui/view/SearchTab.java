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

import java.util.Observable;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.helper.CCLabel;
import net.mldonkey.g2gui.view.helper.CGridLayout;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.search.AlbumSearch;
import net.mldonkey.g2gui.view.search.Search;
import net.mldonkey.g2gui.view.search.SearchResult;
import net.mldonkey.g2gui.view.search.SimpleSearch;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolderAdapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;


/**
 * SearchTab
 *
 *
 * @version $Id: SearchTab.java,v 1.21 2003/09/03 14:59:23 zet Exp $ 
 *
 */
public class SearchTab extends GuiTab {
	private Composite tabFolderPage;
	private GridLayout gridLayout;
	private Group group;
	private CTabFolder tabFolder;
	private CTabFolder cTabFolder;
	private CoreCommunication core;

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
		return new Search[] {
			new SimpleSearch( core, this ),
			new AlbumSearch( core, this ),
		};	
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.GuiTab#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected void createContents( Composite parent ) {
		/* Create a two column page */		
		tabFolderPage = new Composite( parent, SWT.NONE );
		GridLayout gridLayout = CGridLayout.createGL(2,0,0,3,0,false);
		tabFolderPage.setLayout( gridLayout );
	
		/* Create the "Left" and "Right" columns */
		this.createLeftGroup ();
		this.createRightGroup ();
	}

	/**
	 * The search mask
	 */
	private void createLeftGroup() {
		
		ViewForm searchViewForm = new ViewForm( tabFolderPage , SWT.BORDER | (PreferenceLoader.loadBoolean("flatInterface") ? SWT.FLAT : SWT.NONE) );
		GridData gd = new GridData(GridData.FILL_VERTICAL);
		gd.widthHint = 150;
		searchViewForm.setLayoutData(gd);
		CLabel searchCLabel = CCLabel.createCL(searchViewForm, "TT_SearchButton", "SearchButtonSmallTitlebar");

		tabFolder = new CTabFolder( searchViewForm, SWT.NONE );
		tabFolder.setLayoutData( new GridData( GridData.FILL_VERTICAL ) );
		
		tabFolder.setSelectionBackground(new Color[]{tabFolder.getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND),
												tabFolder.getBackground() },
												new int[] {75});
		tabFolder.setSelectionForeground(tabFolder.getDisplay().getSystemColor(SWT.COLOR_TITLE_FOREGROUND));
				
		Search[] tabs = this.createTab();
		for ( int i = 0; i < tabs.length; i++ ) {
			CTabItem item = new CTabItem( tabFolder, SWT.NONE );
			item.setText( tabs[ i ].getTabName() );
			item.setControl( tabs[ i ].createTabFolderPage( tabFolder ) );
			item.setData( tabs[ i ] );			
		}
		tabFolder.setSelection( 0 );
		
		searchViewForm.setContent(tabFolder);
		searchViewForm.setTopLeft(searchCLabel);
		
	}
	

	/**
	 * The result mask
	 */
	private void createRightGroup() {
		/* right group */
		
		ViewForm searchResultsViewForm = new ViewForm( tabFolderPage , SWT.BORDER | (PreferenceLoader.loadBoolean("flatInterface") ? SWT.FLAT : SWT.NONE) );
		searchResultsViewForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		CLabel searchResultsCLabel = CCLabel.createCL(searchResultsViewForm, G2GuiResources.getString("ST_RESULTS"), "SearchButtonSmallTitlebar");
		
		cTabFolder = new CTabFolder( searchResultsViewForm, SWT.NONE );
		
		searchResultsViewForm.setContent(cTabFolder);
		searchResultsViewForm.setTopLeft(searchResultsCLabel);
		
		cTabFolder.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		/* set this as data, so our children in the ctabfolder know whos their dad ;) */
		cTabFolder.setData( this );
		cTabFolder.setSelectionBackground( new Color[]{ cTabFolder.getDisplay().getSystemColor( SWT.COLOR_TITLE_BACKGROUND ),
											cTabFolder.getBackground() },
											new int[] { 75 } );
		cTabFolder.setSelectionForeground( cTabFolder.getDisplay().getSystemColor( SWT.COLOR_TITLE_FOREGROUND ) );

		
		/* add a "X" and listen for close event */
		cTabFolder.addCTabFolderListener( new CTabFolderAdapter() {
			public void itemClosed( CTabFolderEvent event ) {
				CTabItem item = ( CTabItem ) event.item;
				( ( SearchResult ) item.getData() ).widgetDisposed( null );
	
				/* close the tab item */
				item.dispose();
	
				/* set the new statusline */
				if ( cTabFolder.getItemCount() != 0 ) {
					SearchResult nResult = ( SearchResult ) cTabFolder.getSelection().getData();
					//setRightLabel( nResult.getStatusLine() );
					mainWindow.getStatusline().update( nResult.getStatusLine() );
					mainWindow.getStatusline().updateToolTip( "" );
				}
				else {
					//setRightLabel( "" );
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
				CTabFolder item = ( CTabFolder ) e.widget;
				if ( item.getSelection() != null ) {
					SearchResult result = ( SearchResult ) item.getSelection().getData();
					mainWindow.getStatusline().update( result.getStatusLine() );
					mainWindow.getStatusline().updateToolTip( "" );
				}
			}
		} );
		
		cTabFolder.addMouseListener( new MouseListener() {
			public void mouseDoubleClick( MouseEvent e ) { }

			public void mouseDown( MouseEvent e ) {
				CTabFolder item = ( CTabFolder ) e.widget;
				if ( item.getSelection() != null ) {
					SearchResult result = ( SearchResult ) item.getSelection().getData();
					CTabItem tab = tabFolder.getItem( tabFolder.getSelectionIndex() );
					Search search = ( Search ) tab.getData();
					if ( result.isStopped() )
						search.setContinueButton();
					else
						search.setStopButton();
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
			SearchResult result = ( SearchResult ) item.getData();
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
	
	public SearchResult getSearchResult() {
		CTabItem item = cTabFolder.getSelection();
		SearchResult result = ( SearchResult ) item.getData();
		return result;
	}
	
	/**
	 * @return The parent maintab window
	 */
	public MainTab getMainTab() {
		return this.mainWindow;
	}
}

/*
$Log: SearchTab.java,v $
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
replace $user$ with $Author: zet $

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