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

import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.GPage;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * PaneGuiTab
 *
 * @version $Id: PaneGuiTab.java,v 1.1 2003/10/31 13:20:31 lemmster Exp $ 
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
		// create the menu to the button
		final MenuManager popupMenu = new MenuManager( "" );
		popupMenu.setRemoveAllWhenShown( true );
		popupMenu.addMenuListener( aMenuManager );

		// create the button itself
		ToolItem toolItem = new ToolItem( aToolBar, SWT.NONE );
		toolItem.setToolTipText( G2GuiResources.getString( "TT_MISC_BUTTON" ) );
		//TODO why is this fu**ing "dropdown" image not working? a color depth swt cant handle?
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
		} );
	}
	
	public abstract GPage getGPage();
}

/*
$Log: PaneGuiTab.java,v $
Revision 1.1  2003/10/31 13:20:31  lemmster
added PaneGuiTab and TableGuiTab
added "dropdown" button to all PaneGuiTabs (not finished yet, continue on monday)

*/