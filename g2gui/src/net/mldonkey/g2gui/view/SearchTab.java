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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolderAdapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.search.*;

/**
 * SearchTab
 *
 * @author $user$
 * @version $Id: SearchTab.java,v 1.4 2003/07/27 22:39:36 zet Exp $ 
 *
 */
public class SearchTab extends GuiTab {
	private Composite tabFolderPage;
	private GridLayout gridLayout;
	private Group group;
	private TabFolder tabFolder;
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
		createButton("SearchButton", 
							bundle.getString("TT_SearchButton"),
							bundle.getString("TT_SearchButtonToolTip"));
		/* create the tab content */
		this.createContents( this.content );
	}

	/**
	 * Create all tabs we want to display inside the TabFolder
	 * @return The tabs to display
	 */	
	public Search[] createTab() {
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
		GridLayout gridLayout = new GridLayout();
		tabFolderPage.setLayout( gridLayout );
		gridLayout.numColumns = 2;
	
		/* Create the "Left" and "Right" columns */
		this.createLeftGroup ();
		this.createRightGroup ();
	}

	/**
	 * The search mask
	 */
	private void createLeftGroup() {
		tabFolder = new TabFolder( tabFolderPage, SWT.NONE );
		tabFolder.setLayoutData( new GridData( GridData.FILL_VERTICAL ) );
		Search[] tabs = this.createTab();
		for ( int i = 0; i < tabs.length; i++ ) {
			TabItem item = new TabItem( tabFolder, SWT.NONE );
			item.setText( tabs[ i ].getTabName() );
			item.setControl( tabs[ i ].createTabFolderPage( tabFolder ) );
			item.setData( tabs[ i ] );
		}
	}

	/**
	 * The result mask
	 */
	private void createRightGroup() {
		/* right group */
		cTabFolder = new CTabFolder( tabFolderPage, SWT.NONE );
		cTabFolder.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		cTabFolder.marginWidth = 5;
		
		/* add a "X" and listen for close event */
		cTabFolder.addCTabFolderListener( new CTabFolderAdapter() {
			public void itemClosed( CTabFolderEvent event ) {
				SearchResult result = ( SearchResult ) event.item.getData();
				result.dispose();
				result = null;

				/* dispose the tabitem image */
				Image image = ( ( CTabItem ) event.item ).getImage();	
				if ( image != null )
					image.dispose();

				/* close the tab item */
				if ( event.item != null )
					event.item.dispose();
			}
		} );

		//VNC need real colors
/*		Color fgColor = new Color( cTabFolder.getDisplay(), 10, 10, 10 );
		Color[] bgColors { };
		int[] bgPercents = { 1, 2, 3 };

		cTabFolder.setSelectionForeground( fgColor );
		cTabFolder.setSelectionBackground( bgColors, bgPercents );
*/
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update( Observable o, Object arg ) { }

	/**
	 * @return The TabFolder where you can add TabItems
	 */
	public CTabFolder getCTabFolder() {
		return cTabFolder;
	}
}

/*
$Log: SearchTab.java,v $
Revision 1.4  2003/07/27 22:39:36  zet
small buttons toggle (in popup) for main cool menu

Revision 1.3  2003/07/27 18:45:47  lemmstercvs01
lots of changes

Revision 1.2  2003/07/24 16:20:10  lemmstercvs01
lots of changes

Revision 1.1  2003/07/23 16:56:28  lemmstercvs01
initial commit

*/