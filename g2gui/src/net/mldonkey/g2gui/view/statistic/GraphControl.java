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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 *
 * GraphControl
 *
 *
 * @version $Id: GraphControl.java,v 1.15 2003/09/18 11:30:18 lemmster Exp $
 *
 */
public class GraphControl extends Composite {
    private GraphCanvas graphCanvas;
    private Composite parent;
    private GraphPainter graphPainter;
    private Graph graph;

	/**
	 *  DOCUMENT ME!
	 * 
	 * @param parent DOCUMENT ME!
	 * @param name DOCUMENT ME!
	 * @param color1 DOCUMENT ME!
	 * @param color2 DOCUMENT ME!
	 */
    public GraphControl( Composite parent, String name, Color color1, Color color2 ) {
        super( parent, SWT.NONE );
        this.parent = parent;
        graphCanvas = new GraphCanvas( this );
        setLayout( new FillLayout() );
        graph = new Graph( name, color1, color2 );
        graphCanvas.setGraph( graph );
    }

    /**
     * DOCUMENT ME!
     */
    public void redraw() {
        if ( !parent.isDisposed() )
            parent.getDisplay().asyncExec( new Runnable() {
                    public void run() {
                        if ( !graphCanvas.isDisposed() )
                            graphCanvas.redraw();
                    }
                } );

    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    public void addPointToGraph( float value ) {
        graph.addPoint( ( int ) ( value * 100 ) );
    }
}

/*
$Log: GraphControl.java,v $
Revision 1.15  2003/09/18 11:30:18  lemmster
checkstyle

Revision 1.14  2003/09/18 11:28:51  lemmster
checkstyle

Revision 1.13  2003/09/13 22:23:55  zet
use int array instead of creating stat point objects

Revision 1.12  2003/08/29 21:42:11  zet
add shadow

Revision 1.11  2003/08/23 15:21:37  zet
remove @author

Revision 1.10  2003/08/22 21:13:11  lemmster
replace $user$ with $Author: lemmster $

Revision 1.9  2003/07/26 17:54:14  zet
fix pref's illegal setParent, redo graphs, other

Revision 1.8  2003/07/26 05:42:39  zet
cleanup



*/
