/*
 * Copyright 2003
 * g2gui Team
 * 
 * 
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.view;

import net.mldonkey.g2gui.view.helper.WidgetFactory;
import net.mldonkey.g2gui.view.shares.SharesViewFrame;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

import java.util.Observable;


/**
 * SharesTab is used to show all files which are shared, 
 * in a table with various statistics
 *
 * @author $user$
 * @version $Id: SharesTab.java,v 1.1 2004/01/22 21:28:03 psy Exp $ 
 *
 */
public class SharesTab extends GuiTab {

	/**
	 * 
	 */
	public SharesTab(MainWindow mainWindow) {
		super(mainWindow, "SharesButton");
	}

	protected void createContents(Composite composite) {
		String sashPrefString = "shareSash";
		SashForm sashForm = WidgetFactory.createSashForm(composite, sashPrefString);
		createSharesView(sashForm);
		WidgetFactory.loadSashForm(sashForm, sashPrefString);
	}
	
	private void createSharesView(SashForm sashForm) {
		String sashPrefString = "shareSash";
		SashForm sashForm2 = WidgetFactory.createSashForm(sashForm, sashPrefString);
		addViewFrame(new SharesViewFrame(sashForm2, "ST_SHARES", "SharesButtonSmall", this));
		//WidgetFactory.loadSashForm(sashForm2, sashPrefString);
	}

	
	public void setActive() {
		super.setActive();
		getCore().startTimer();
	}
	
	public void setInActive() {
		super.setInActive();
		getCore().stopTimer();
	}
	
	public void update(Observable o, Object obj) {
	}
}

/*
$Log: SharesTab.java,v $
Revision 1.1  2004/01/22 21:28:03  psy
renamed "uploads" to "shares" and moved it to a tab of its own.
"uploaders" are now called "uploads" for improved naming-consistency

*/