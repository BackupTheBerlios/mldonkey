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

import gnu.trove.TIntArrayList;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;


/**
 * GraphHistory
 *
 * @version $Id: GraphHistory.java,v 1.4 2003/11/23 17:58:03 lemmster Exp $
 *
 */
public class GraphHistory implements PaintListener {
	//TODO move strings into resource bundle
    private Shell shell;
    private Graph graph;
    private TIntArrayList maxList;
    private TIntArrayList avgList;
	
	private Color gridColor = PreferenceLoader.loadColour( "graphGridColor" );
	private Color backgroundColor = PreferenceLoader.loadColour( "graphBackgroundColor" );
	private Color textColor = PreferenceLoader.loadColour( "graphTextColor" );
   
    public GraphHistory( Graph graph ) {
        this.graph = graph;
        this.maxList = graph.getMaxList();
        this.avgList = graph.getAvgList();

        createContents();
    }

    public void createContents() {
        shell = new Shell( SWT.MAX | SWT.CLOSE | SWT.TITLE | SWT.RESIZE | SWT.BORDER | SWT.APPLICATION_MODAL );
        shell.setImage( G2GuiResources.getImage( "ProgramIcon" ) );
        shell.setText( graph.getName() );
        shell.setLayout( new FillLayout() );
        shell.addDisposeListener( new DisposeListener() {
                public synchronized void widgetDisposed( DisposeEvent e ) {
                    PreferenceStore p = PreferenceLoader.getPreferenceStore();
                    PreferenceConverter.setValue( p, "graphHistoryWindowBounds", shell.getBounds() );
                }
            } );

        shell.addPaintListener( this );

        if ( PreferenceLoader.contains( "graphHistoryWindowBounds" ) ) {
            shell.setBounds( PreferenceLoader.loadRectangle( "graphHistoryWindowBounds" ) );
        }

        shell.open();
    }

    public void paintControl( PaintEvent e ) {
        if ( ( shell.getClientArea().width < 5 ) || ( shell.getClientArea().height < 5 ) ) {
            return;
        }

        int height = shell.getClientArea().height;
        int width = shell.getClientArea().width;

        Image imageBuffer = new Image( shell.getDisplay(), shell.getClientArea() );
        GC gc = new GC( imageBuffer );

        gc.setBackground( backgroundColor );
        gc.fillRectangle( 0, 0, shell.getClientArea().width, shell.getClientArea().height );

        // draw grid
        gc.setForeground( gridColor );

        // vertical lines
        for ( int i = 0; i < width; i += 20 )
            gc.drawLine( i, 0, i, height + 1 );

        // horizontal lines					
        for ( int i = height + 1; i > 0; i -= 20 )
            gc.drawLine( 0, i, width + 1, i );

        // looks worse when under the grid     
        drawGraph( gc );

        e.gc.drawImage( imageBuffer, 0, 0 );
        gc.dispose();
        imageBuffer.dispose();
    }

    private void drawGraph( GC gc ) {
        if ( maxList.size() == 0 ) {
            gc.setForeground( textColor );
            gc.drawText( "No hourly history available yet...", 0, 0 );

            return;
        }

        int lineHeight = gc.getFontMetrics().getHeight() + 2;

        int width = shell.getClientArea().width;
        int barWidth = width / maxList.size();
        float height = (float) shell.getClientArea().height - ( lineHeight * 3 );

        float maxValueY;
        float avgValueY;
        int maxValue;
        int avgValue;

        float maximum = 2;

        // maxList.max() crashes.. 
        for ( int i = 0; i < maxList.size(); i++ ) {
            if ( ( maxList.getQuick( i ) / 10 ) > maximum ) {
                maximum = (float) maxList.getQuick( i ) / 10;
            }
        }

        float zoom = ( height - 10f ) / maximum;
        int xCoord = 0;

        gc.setForeground( textColor );

        for ( int i = 0; i < maxList.size(); i++ ) {
            maxValue = maxList.getQuick( i );
            avgValue = avgList.getQuick( i );

            xCoord = i * barWidth;

            maxValueY = maxValue / 10;
            avgValueY = avgValue / 10;

            maxValueY = maxValueY * zoom;
            avgValueY = avgValueY * zoom;

            gc.setBackground( graph.getColor2() );
            gc.fillRectangle( xCoord, shell.getClientArea().height + 1, barWidth - 2, -(int) maxValueY );

            gc.setBackground( graph.getColor1() );
            gc.fillRectangle( xCoord, shell.getClientArea().height + 1, barWidth - 2, -(int) avgValueY );

            gc.drawText( "Hour: " + ( i + 1 ), xCoord, 0, true );
            gc.drawText( "Avg: " + ( (double) avgValue / 100 ) + "kb/s", xCoord, lineHeight, true );
            gc.drawText( "Max: " + ( (double) maxValue / 100 ) + "kb/s", xCoord, ( 2 * lineHeight ), true );
        }
    }
}


/*
$Log: GraphHistory.java,v $
Revision 1.4  2003/11/23 17:58:03  lemmster
removed dead/unused code

Revision 1.3  2003/11/06 14:59:06  lemmster
clean up

Revision 1.2  2003/10/17 15:47:14  zet
graph colour prefs

Revision 1.1  2003/09/20 22:08:41  zet
basic graph hourly history

*/
