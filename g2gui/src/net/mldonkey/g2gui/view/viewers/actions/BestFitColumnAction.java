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
package net.mldonkey.g2gui.view.viewers.actions;

import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;

import net.mldonkey.g2gui.view.viewers.GView;

/**
 * FavoriteColumnAction
 *
 * @version $Id: BestFitColumnAction.java,v 1.1 2003/12/07 19:40:19 lemmy Exp $ 
 *
 */
public class BestFitColumnAction extends ColumnAction implements ControlListener {
	public BestFitColumnAction(GView gView, int column) {
		super(gView,column);
		setChecked( gView.getColumnControlListenerIsOn() == column );
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.viewers.actions.ColumnAction#run()
	 */
	public void run() {
		if ( gView.getColumnControlListenerIsOn() == column )
			this.gView.addControlListener( null );
		else
			this.gView.addControlListener( this );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt.events.ControlEvent)
	 */
	public void controlMoved(ControlEvent e) { }

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.ControlListener#controlResized(org.eclipse.swt.events.ControlEvent)
	 */
	public void controlResized(ControlEvent e) {
		gView.setColumnWidth( column );
	}
	
	public int getColumnId() {
		return this.column;
	}
}
/*
$Log: BestFitColumnAction.java,v $
Revision 1.1  2003/12/07 19:40:19  lemmy
[Bug #1156] Allow a certain column to be 100% by pref

*/