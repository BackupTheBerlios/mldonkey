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
package net.mldonkey.g2gui.view.statistic;

import net.mldonkey.g2gui.view.GuiTab;
import net.mldonkey.g2gui.view.helper.SashViewFrame;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.custom.SashForm;


/**
 * GraphViewFrame
 *
 * @version $Id: GraphViewFrame.java,v 1.1 2003/11/29 01:51:53 zet Exp $
 *
 */
public class GraphViewFrame extends SashViewFrame {
    GraphControl graphControl;

    public GraphViewFrame(SashForm sashForm, String prefString, String prefImageString,
        GuiTab guiTab) {
        super(sashForm, prefString, prefImageString, guiTab);
        graphControl = new GraphControl(childComposite, G2GuiResources.getString(prefString));
        createPaneListener(new GraphPaneListener(this, graphControl,
                prefString.equals("TT_Uploads") ? "TT_Downloads" : "TT_Uploads"));
    }

    public GraphControl getGraphControl() {
        return graphControl;
    }
}


/*
$Log: GraphViewFrame.java,v $
Revision 1.1  2003/11/29 01:51:53  zet
a few more viewframe changes.. will continue later.


*/
