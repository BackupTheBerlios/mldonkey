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
package net.mldonkey.g2gui.view.statistic;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;


/**
 *
 * GraphControl
 *
 *
 * @version $Id: GraphControl.java,v 1.17 2003/10/17 15:36:01 zet Exp $
 *
 */
public class GraphControl extends Composite {
    private GraphCanvas graphCanvas;
    private Composite parent;
    private Graph graph;

    public GraphControl( Composite parent, String name ) {
        super( parent, SWT.NONE );
        this.parent = parent;
        graphCanvas = new GraphCanvas( this );
        setLayout( new FillLayout(  ) );

        graph = new Graph( name );
        graphCanvas.setGraph( graph );
    }

    public void redraw(  ) {
        if ( !parent.isDisposed(  ) ) {
            parent.getDisplay(  ).asyncExec( new Runnable(  ) {
                    public void run(  ) {
                        if ( !graphCanvas.isDisposed(  ) ) {
                            graphCanvas.redraw(  );
                        }
                    }
                } );

        }
    }

    public void addPointToGraph( float value ) {
        graph.addPoint( (int) ( value * 100 ) );
    }

    public Graph getGraph(  ) {
        return graph;
    }
    
    public void updateDisplay() {
    	graph.updateDisplay();	
		graphCanvas.updateDisplay();
    }
    
}


/*
$Log: GraphControl.java,v $
Revision 1.17  2003/10/17 15:36:01  zet
graph colour prefs

Revision 1.16  2003/09/20 22:08:41  zet
basic graph hourly history

Revision 1.13  2003/09/13 22:23:55  zet
use int array instead of creating stat point objects

Revision 1.12  2003/08/29 21:42:11  zet
add shadow

Revision 1.11  2003/08/23 15:21:37  zet
remove @author

Revision 1.10  2003/08/22 21:13:11  lemmster
replace $user$ with $Author: zet $

Revision 1.9  2003/07/26 17:54:14  zet
fix pref's illegal setParent, redo graphs, other

Revision 1.8  2003/07/26 05:42:39  zet
cleanup



*/
