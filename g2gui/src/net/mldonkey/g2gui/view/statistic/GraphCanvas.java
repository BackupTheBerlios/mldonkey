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
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;


/**
 * GraphCanvas
 *
 *
 * @version $Id: GraphCanvas.java,v 1.11 2003/09/20 22:08:41 zet Exp $
 */
public class GraphCanvas extends Canvas {
    private Composite parent;
    private Image imageBuffer;
    final private GraphPainter graphPainter;
    private boolean needNewBuffer;

    public GraphCanvas( Composite parent ) {
        super( parent, SWT.NO_BACKGROUND );
        this.parent = parent;
        graphPainter = new GraphPainter( parent );

        addPaintListener( new PaintListener(  ) {
                public void paintControl( PaintEvent e ) {
                    GraphCanvas.this.paintControl( e );
                }
            } );

        addControlListener( new ControlListener(  ) {
                public void controlResized( ControlEvent e ) {
                    needNewBuffer = true;
                }

                public void controlMoved( ControlEvent e ) {
                }
            } );

        addDisposeListener( new DisposeListener(  ) {
                public void widgetDisposed( DisposeEvent e ) {
                    if ( imageBuffer != null ) {
                        imageBuffer.dispose(  );
                    }
                }
            } );
    }

    private void paintControl( PaintEvent e ) {
        if ( needNewBuffer ) {
            if ( imageBuffer != null ) {
                imageBuffer.dispose(  );
            }

            imageBuffer = new Image( null, parent.getBounds(  ) );
        }

        graphPainter.setGraphicControl( e.gc );
        graphPainter.paint( imageBuffer );

        needNewBuffer = false;
    }

    public void setGraph( final Graph graph ) {
        graphPainter.setGraph( graph );

        addMouseListener( new MouseAdapter(  ) {
                public void mouseDoubleClick( MouseEvent e ) {
                    graph.toggleDisplay(  );
                }
            } );
    }
}


/*
$Log: GraphCanvas.java,v $
Revision 1.11  2003/09/20 22:08:41  zet
basic graph hourly history

Revision 1.8  2003/08/23 15:21:37  zet
remove @author

Revision 1.7  2003/08/22 21:13:11  lemmster
replace $user$ with $Author: zet $

Revision 1.6  2003/07/26 05:42:39  zet
cleanup



*/
