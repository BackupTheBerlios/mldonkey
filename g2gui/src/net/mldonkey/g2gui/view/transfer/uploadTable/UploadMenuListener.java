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
package net.mldonkey.g2gui.view.transfer.uploadTable;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.GViewer;
import net.mldonkey.g2gui.view.viewers.SashGPaneListener;
import net.mldonkey.g2gui.view.viewers.actions.AllFilterAction;
import net.mldonkey.g2gui.view.viewers.actions.ColumnSelectorAction;
import net.mldonkey.g2gui.view.viewers.actions.FlipSashAction;
import net.mldonkey.g2gui.view.viewers.actions.MaximizeAction;
import net.mldonkey.g2gui.view.viewers.actions.RefreshUploadsAction;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Control;

/**
 * UploadMenuListener
 *
 * @version $Id: UploadMenuListener.java,v 1.1 2003/10/29 16:56:21 lemmster Exp $ 
 *
 */

/**
 * UploadsMenuListener
 */
public class UploadMenuListener extends SashGPaneListener {
	/**
	 * @param gViewer
	 * @param core
	 * @param aSashForm
	 * @param aControl
	 */
	public UploadMenuListener(GViewer gViewer, CoreCommunication core, SashForm aSashForm, Control aControl) {
		super( gViewer, core, aSashForm, aControl );
	}

	public void menuAboutToShow(IMenuManager menuManager) {
		boolean advancedMode = PreferenceLoader.loadBoolean("advancedMode");
		// columnSelector
		if ( advancedMode )
			menuManager.add( new ColumnSelectorAction(gViewer) );

		// filter submenu			
		MenuManager filterSubMenu = new MenuManager( G2GuiResources.getString( "TT_DOWNLOAD_MENU_FILTER" ) );

		// all filters
		filterSubMenu.add( new AllFilterAction( gViewer ) );
		filterSubMenu.add( new Separator() );

		// network filters
		createNetworkFilterSubMenu( filterSubMenu );

		menuManager.add( filterSubMenu );

		// flip sash/maximize sash
		menuManager.add( new Separator() );
		menuManager.add( new FlipSashAction( this.sashForm ) );
		menuManager.add( new MaximizeAction( this.sashForm, this.control ) );

		// refresh table
		menuManager.add( new Separator() );
		menuManager.add( new RefreshUploadsAction( gViewer ) );
	}
}

/*
$Log: UploadMenuListener.java,v $
Revision 1.1  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

*/