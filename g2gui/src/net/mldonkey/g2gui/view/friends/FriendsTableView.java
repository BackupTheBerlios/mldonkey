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
package net.mldonkey.g2gui.view.friends;

import net.mldonkey.g2gui.view.helper.ViewFrame;
import net.mldonkey.g2gui.view.viewers.table.GTableView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;


/**
 * FriendsTableView
 *
 * @version $Id: FriendsTableView.java,v 1.1 2003/11/29 14:29:27 zet Exp $
 *
 */
public class FriendsTableView extends GTableView {
    public static final int NAME = 0;

    public FriendsTableView(ViewFrame viewFrame) {
        super(viewFrame);

        preferenceString = "friends";
        columnLabels = new String[] { "FR_FRIENDS" };
        columnAlignment = new int[] { SWT.LEFT };
        columnDefaultWidths = new int[] { 150 };

        gSorter = new FriendsTableSorter(this);
        tableContentProvider = new FriendsTableContentProvider(this);
        tableLabelProvider = new FriendsTableLabelProvider(this);
        tableMenuListener = new FriendsTableMenuListener(this);

        createContents(viewFrame.getChildComposite());
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.GTableViewer#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected void createContents(Composite parent) {
        super.createContents(parent);
        sViewer.setInput(getCore().getClientInfoIntMap().getFriendsWeakMap());
        sViewer.addSelectionChangedListener((FriendsTableMenuListener) tableMenuListener);
        addMenuListener();
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.GView#updateDisplay()
     */
    public void updateDisplay() {
        getTable().setLinesVisible(false);
    }
}


/*
$Log: FriendsTableView.java,v $
Revision 1.1  2003/11/29 14:29:27  zet
small viewframe updates

*/
