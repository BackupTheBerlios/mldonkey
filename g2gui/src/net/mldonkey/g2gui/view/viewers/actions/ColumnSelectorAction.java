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

import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.ColumnSelector;
import net.mldonkey.g2gui.view.viewers.GViewer;

import org.eclipse.jface.action.Action;

/**
 * ColumnSelectorAction
 *
 * @version $Id: ColumnSelectorAction.java,v 1.2 2003/10/29 16:56:21 lemmster Exp $
 *
 */
public class ColumnSelectorAction extends Action {
    private GViewer gViewer;

    public ColumnSelectorAction( GViewer gViewer ) {
        super( G2GuiResources.getString( "TT_ColumnSelector" ) );
        setImageDescriptor( G2GuiResources.getImageDescriptor( "table" ) );
        this.gViewer = gViewer;
    }

    public void run() {
        ColumnSelector cSelector =
            new ColumnSelector( gViewer.getShell(), gViewer.getColumnLabels(),
                                gViewer.getAllColumnIDs(), gViewer.getPreferenceString() );
        if ( cSelector.open() == ColumnSelector.OK ) {
            cSelector.savePrefs();
            gViewer.resetColumns();
        }
    }
}

/*
$Log: ColumnSelectorAction.java,v $
Revision 1.2  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

Revision 1.1  2003/10/22 16:28:52  zet
common actions


*/
