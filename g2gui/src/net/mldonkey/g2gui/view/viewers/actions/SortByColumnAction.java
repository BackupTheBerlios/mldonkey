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
package net.mldonkey.g2gui.view.viewers.actions;

import net.mldonkey.g2gui.view.viewers.GView;

import org.eclipse.jface.action.Action;

import org.eclipse.swt.widgets.TableColumn;


/**
 * SortByColumnAction - for macOS
 *
 * @version $Id: SortByColumnAction.java,v 1.1 2003/11/14 00:46:04 zet Exp $
 *
 */
public class SortByColumnAction extends Action {
    GView gView;
    int column;

    public SortByColumnAction(GView gView, int column) {
        super(((TableColumn) gView.getTable().getColumn(column)).getText());
        this.gView = gView;
        this.column = column;
    }

    public void run() {
        gView.sortByColumn(column);
    }
}


/*
$Log: SortByColumnAction.java,v $
Revision 1.1  2003/11/14 00:46:04  zet
sort by column menu item (for macOS)

*/
