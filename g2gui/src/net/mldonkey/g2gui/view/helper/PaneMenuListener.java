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
import org.eclipse.jface.viewers.StructuredViewer;

/**
 * PaneMenuListener
 *
 * @version $Id: PaneMenuListener.java,v 1.1 2003/09/14 13:24:30 lemmster Exp $ 
 *
 */
public class PaneMenuListener extends MenuListener implements IMenuListener {

	/**
	 * 
	 */
	public PaneMenuListener( StructuredViewer viewer, CoreCommunication core ) {
		super( viewer, core );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */
	public void menuAboutToShow(IMenuManager manager) {
		super.menuAboutToShow( manager );
	}

}

/*
$Log: PaneMenuListener.java,v $
Revision 1.1  2003/09/14 13:24:30  lemmster
add header button to servertab

*/