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
package net.mldonkey.g2gui.view.helper;

import net.mldonkey.g2gui.comm.CoreCommunication;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;

/**
 * PaneMenuListener
 *
 * @version $Id: PaneMenuListener.java,v 1.4 2003/10/21 17:00:45 lemmster Exp $ 
 *
 */
public class PaneMenuListener extends CMenuListener implements IMenuListener {

	/**
	 * Creates a new PaneMenuListener
	 * @param viewer The Viewer which is the parent of this objs
	 * @param core The parent core
	 */
	public PaneMenuListener( CoreCommunication core ) {
		super( core );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */
	public void menuAboutToShow( IMenuManager manager ) {
		super.menuAboutToShow( manager );
	}

}

/*
$Log: PaneMenuListener.java,v $
Revision 1.4  2003/10/21 17:00:45  lemmster
class hierarchy for tableviewer

Revision 1.3  2003/09/23 14:47:53  zet
rename MenuListener to avoid conflict with swt MenuListener

Revision 1.2  2003/09/18 10:04:57  lemmster
checkstyle

Revision 1.1  2003/09/14 13:24:30  lemmster
add header button to servertab

*/