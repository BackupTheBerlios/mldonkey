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

import net.mldonkey.g2gui.view.viewers.GView;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.swt.widgets.ToolBar;

/**
 * PaneGuiTab
 *
 * @version $Id: PaneGuiTab.java,v 1.4 2003/11/04 18:50:00 lemmster Exp $ 
 *
 */
public abstract class PaneGuiTab extends GuiTab {
	/**
	 * @param gui
	 */
	public PaneGuiTab( MainTab gui ) {
		super( gui );
	}
    
	/**
	 * @param aToolBar
	 */
	protected void createPaneToolBar( final ToolBar aToolBar, IMenuListener aMenuManager ) {
/*		// create the menu to the button
		final MenuManager popupMenu = new MenuManager( "" );
		popupMenu.setRemoveAllWhenShown( true );
		popupMenu.addMenuListener( aMenuManager );
		
		//TODO decide how the pane listener should work? read more...
		// "right click" and "image click" for table layout and buttons 
		// for "mldonkey controls" (eclipse style) or
		// windows style with all buttons on the right side...
		// this uncommented implementation adds the right "dropdown" button
		// to all tables.

		// create the button itself
		ToolItem toolItem = new ToolItem( aToolBar, SWT.NONE );
		toolItem.setToolTipText( G2GuiResources.getString( "TT_MISC_BUTTON" ) );
		toolItem.setImage( G2GuiResources.getImage( "dropdown" ) );
		toolItem.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected( SelectionEvent s ) {
				Rectangle rect = ( (ToolItem) s.widget ).getBounds();
				Menu menu = popupMenu.createContextMenu( aToolBar );
				Point pt = new Point( rect.x, rect.y + rect.height );
				pt = aToolBar.toDisplay( pt );
				menu.setLocation( pt.x, pt.y );
				menu.setVisible( true );
			}
		} );*/
	}
	
	public abstract GView getGView();
}

/*
$Log: PaneGuiTab.java,v $
Revision 1.4  2003/11/04 18:50:00  lemmster
added a TODO tag

Revision 1.3  2003/10/31 16:02:17  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.2  2003/10/31 15:14:56  zet
remove duplicate dropdown

Revision 1.1  2003/10/31 13:20:31  lemmster
added PaneGuiTab and TableGuiTab
added "dropdown" button to all PaneGuiTabs (not finished yet, continue on monday)

*/