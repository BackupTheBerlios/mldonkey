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

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.viewers.GView;

/**
 * TableGuiTab
 *
 * @version $Id: TableGuiTab.java,v 1.4 2003/11/29 01:51:53 zet Exp $ 
 *
 */
public abstract class TableGuiTab extends PaneGuiTab {
	protected CoreCommunication cfore;
	protected GView gView;

	/**
	 * @param gui
	 */
	public TableGuiTab(MainTab gui) {
		super(gui);
	}
	
	public GView getGView() {
		return gView;
	}
}

/*
$Log: TableGuiTab.java,v $
Revision 1.4  2003/11/29 01:51:53  zet
a few more viewframe changes.. will continue later.

Revision 1.3  2003/10/31 16:02:17  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.2  2003/10/31 13:20:31  lemmster
added PaneGuiTab and TableGuiTab
added "dropdown" button to all PaneGuiTabs (not finished yet, continue on monday)

Revision 1.1  2003/10/31 10:42:47  lemmster
Renamed GViewer, GTableViewer and GTableTreeViewer to GPage... to avoid mix-ups with StructuredViewer...
Removed IGViewer because our abstract class GPage do the job
Use supertype/interface where possible to keep the design flexible!

*/