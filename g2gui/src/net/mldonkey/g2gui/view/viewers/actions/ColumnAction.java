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

import net.mldonkey.g2gui.view.viewers.GView;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.TableColumn;

/**
 * ColumnAction
 *
 * @version $Id: ColumnAction.java,v 1.1 2003/12/07 19:40:19 lemmy Exp $ 
 *
 */
public abstract class ColumnAction extends Action {
	protected GView gView;
	protected int column;

	public ColumnAction(GView view, int column) {
		super(((TableColumn) view.getTable().getColumn(column)).getText());
		this.gView = view;
		this.column = column;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public abstract void run();
}

/*
$Log: ColumnAction.java,v $
Revision 1.1  2003/12/07 19:40:19  lemmy
[Bug #1156] Allow a certain column to be 100% by pref

*/