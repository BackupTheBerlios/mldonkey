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

import net.mldonkey.g2gui.view.pref.PreferenceLoader;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;


/**
 *
 * GraphPainter
 *
 *
 * @version $Id: GraphPainter.java,v 1.35 2003/11/22 02:24:30 zet Exp $
 *
 */
public class GraphPainter {
    /* (non-Javadoc)
     * paint gets called from Canvas and Graphics2D is able to draw Transparent Images
     * for this you need the awt.Color Package because SWT.graphics.color doesn't have an alpha channel
     */
    private Graph graph;
    private GC drawBoard;
    final private Composite parent;
    private Color backgroundColor;
    private Color gridColor;
    private Color textColor;
    private Color labelTextColor;
    private Color labelLineColor;
    private Color labelBackgroundColor;

    public GraphPainter(Composite parent) {
        this.parent = parent;
        updateDisplay();
    }

    public void setGraphicControl(GC gc) {
        drawBoard = gc;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void paint(Image imageBuffer) {
        // might help with rare: java.lang.IllegalArgumentException: Argument not valid	?
        if ((parent.getBounds().height < 5) || (parent.getBounds().width < 5)) {
            return;
        }

        // create a buffer
        GC drawBoardBuffer = new GC(imageBuffer);

        // set canvas background color
        drawBoardBuffer.setBackground(backgroundColor);
        drawBoardBuffer.setForeground(backgroundColor);
        drawBoardBuffer.fillRectangle(0, 0, parent.getClientArea().width,
            parent.getClientArea().height);

        int startx = 1;
        int k = startx;

        int bottomSpace = drawBoardBuffer.getFontMetrics().getHeight() + 2;
        float height = (float) (parent.getClientArea().height - bottomSpace);

        int width = parent.getClientArea().width;
        int graphWidth = width - startx;
        float zoom = 0;
        float valueY = 0;
        float maximum = (float) (graph.findMax(width) / 10);
        zoom = (height - 10f) / maximum;

        // draw graph gradiant lines (switch outside the for loop)
        switch (graph.getGraphType()) {
        case 1:
            drawBarGraph(startx, width, height, zoom, drawBoardBuffer);

            break;

        case 2:
            drawLineGraph(startx, width, height, zoom, drawBoardBuffer);

            break;

        // fillGradiantRectangle is an expensive call creating many RGB objects.
        default:
            drawGradiantGraph(startx, width, height, zoom, drawBoardBuffer);

            break;
        }

        // draw grid
        drawBoardBuffer.setForeground(gridColor);

        // vertical lines
        for (int i = startx - 1; i < (startx + graphWidth); i += 20)
            drawBoardBuffer.drawLine(i, 0, i, (int) height + 1);

        // horizontal lines					
        for (int i = (int) height + 1; i > 0; i -= 20)
            drawBoardBuffer.drawLine(startx, i, startx + graphWidth, i);

        // just for temporary fun; this might overflow pretty quickly
        drawBoardBuffer.setForeground(textColor);
        drawBoardBuffer.drawText(graph.getName() + " avg: " + ((double) graph.getAvg() / 100) +
            " kb/s," + " max: " + ((double) graph.getMax() / 100) + " kb/s", startx,
            parent.getClientArea().height - drawBoardBuffer.getFontMetrics().getHeight(), true);

        // draw floating box
        double value = (double) graph.getNewestPoint() / 100;
        String boxString = String.valueOf(value) + " kb/s";

        int linePosition = (int) (height - ((float) (graph.getNewestPoint() / 10) * zoom));
        int linePositionEnd = linePosition;
        int textPosition = linePosition - 6;

        if ((textPosition + bottomSpace) >= (int) height) {
            textPosition = (int) height - bottomSpace - 3;
            linePositionEnd = linePositionEnd - 6;
        }

        int boxWidth = drawBoardBuffer.textExtent(boxString).x + 20;
        int boxHeight = drawBoardBuffer.textExtent(boxString).y + 5;

        drawBoardBuffer.setForeground(labelTextColor);
        drawBoardBuffer.setBackground(labelBackgroundColor);
        drawBoardBuffer.fillRoundRectangle(startx + 10, textPosition, boxWidth, boxHeight, 18, 18);
        drawBoardBuffer.drawRoundRectangle(startx + 10, textPosition, boxWidth, boxHeight, 18, 18);
        drawBoardBuffer.drawText(boxString, startx + 20, textPosition + 2);
        drawBoardBuffer.setForeground(labelLineColor);
        drawBoardBuffer.drawLine(startx + 10, linePositionEnd, startx, linePosition);

        // output buffer to the display
        drawBoard.drawImage(imageBuffer, 0, 0);
        drawBoardBuffer.dispose();
    }

    private void drawGradiantGraph(int startx, int width, float height, float zoom,
        GC drawBoardBuffer) {
        int positionInArray = graph.getInsertAt() - 1;
        int validPoints = ((Graph.MAX_POINTS > graph.getAmount()) ? graph.getAmount()
                                                                  : Graph.MAX_POINTS);
        float valueY;

        drawBoardBuffer.setBackground(graph.getColor1());
        drawBoardBuffer.setForeground(graph.getColor2());

        for (int k = startx; (k < width) && (validPoints > 0); k++, validPoints--) {
            if (positionInArray < 0) {
                positionInArray = Graph.MAX_POINTS - 1;
            }

            valueY = (float) (graph.getPointAt(positionInArray) / 10);
            valueY = height - (valueY * zoom);

            drawBoardBuffer.fillGradientRectangle(k, (int) height + 1, 1, (int) (valueY - height),
                true);
            positionInArray--;
        }
    }

    private void drawBarGraph(int startx, int width, float height, float zoom, GC drawBoardBuffer) {
        int positionInArray = graph.getInsertAt() - 1;
        int validPoints = ((Graph.MAX_POINTS > graph.getAmount()) ? graph.getAmount()
                                                                  : Graph.MAX_POINTS);
        float valueY;
        drawBoardBuffer.setForeground(graph.getColor1());

        for (int k = startx; (k < width) && (validPoints > 0); k++, validPoints--) {
            if (positionInArray < 0) {
                positionInArray = Graph.MAX_POINTS - 1;
            }

            valueY = (float) (graph.getPointAt(positionInArray) / 10);
            valueY = height - (valueY * zoom);

            drawBoardBuffer.drawLine(k, (int) height + 1, k, (int) (valueY));
            positionInArray--;
        }
    }

    private void drawLineGraph(int startx, int width, float height, float zoom, GC drawBoardBuffer) {
        int positionInArray = graph.getInsertAt() - 1;
        int validPoints = ((Graph.MAX_POINTS > graph.getAmount()) ? graph.getAmount()
                                                                  : Graph.MAX_POINTS);
        float valueY;
        float lastY = -1;
        drawBoardBuffer.setForeground(graph.getColor1());
        drawBoardBuffer.setLineWidth(3);

        for (int k = startx; (k < width) && (validPoints > 0); k++, validPoints--) {
            if (positionInArray < 0) {
                positionInArray = Graph.MAX_POINTS - 1;
            }

            valueY = (float) (graph.getPointAt(positionInArray) / 10);
            valueY = height - (valueY * zoom);

            if (lastY == -1) {
                lastY = valueY;
            }

            drawBoardBuffer.drawLine(k - 1, (int) lastY, k, (int) (valueY));
            lastY = valueY;
            positionInArray--;
        }

        drawBoardBuffer.setLineWidth(1);
    }

    public void updateDisplay() {
        backgroundColor = PreferenceLoader.loadColour("graphBackgroundColor");
        gridColor = PreferenceLoader.loadColour("graphGridColor");
        textColor = PreferenceLoader.loadColour("graphTextColor");

        labelBackgroundColor = PreferenceLoader.loadColour("graphLabelBackgroundColor");
        labelTextColor = PreferenceLoader.loadColour("graphLabelTextColor");
        labelLineColor = PreferenceLoader.loadColour("graphLabelLineColor");
    }
}


/*
$Log: GraphPainter.java,v $
Revision 1.35  2003/11/22 02:24:30  zet
widgetfactory & save sash postions/states between sessions

Revision 1.34  2003/10/17 15:36:02  zet
graph colour prefs

Revision 1.33  2003/09/20 22:08:41  zet
basic graph hourly history

Revision 1.30  2003/09/16 01:18:52  zet
min size/check bounds

Revision 1.29  2003/09/14 22:22:55  zet
*** empty log message ***

Revision 1.28  2003/09/13 22:23:55  zet
use int array instead of creating stat point objects

Revision 1.27  2003/08/29 21:42:11  zet
add shadow

Revision 1.26  2003/08/23 15:21:37  zet
remove @author

Revision 1.25  2003/08/22 21:13:11  lemmster
replace $user$ with $Author: zet $

Revision 1.24  2003/08/18 13:42:43  zet
*** empty log message ***

Revision 1.23  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.22  2003/08/09 00:42:04  zet
dispose colors

Revision 1.21  2003/07/27 00:07:53  zet
minor

Revision 1.20  2003/07/26 21:49:59  zet
white text

Revision 1.19  2003/07/26 17:54:14  zet
fix pref's illegal setParent, redo graphs, other

Revision 1.18  2003/07/26 05:42:39  zet
cleanup



*/
