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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;


/**
 * @author achim
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GraphPainter {

	/* (non-Javadoc)
	 * paint gets called from Canvas and Graphics2D is able to draw Transparent Images
	 * for this you need the awt.Color Package because SWT.graphics.color doesn't have an alpha channel
	 */
	private Graph graph;
	private GC drawBoard;
	final private Composite parent;
	
	public GraphPainter(Composite parent)
	{
		this.parent = parent;
	}
	
	public void setGraphicControl(GC gc)
	{
		drawBoard = gc;
	}
	
	public void setGraph(Graph graph)
	{
		this.graph = graph;
	}
	
	public void paint() {
			
		Display display = parent.getDisplay();
		
		Color black = display.getSystemColor(SWT.COLOR_BLACK);
		Color white = display.getSystemColor(SWT.COLOR_WHITE);
		Color yellow = display.getSystemColor(SWT.COLOR_YELLOW);
				
		// create a buffer
		Image imageBuffer = new Image(null, parent.getBounds());
		GC drawBoardBuffer = new GC(imageBuffer);
				
		// set canvas background color
		Color canvasBackgroundColor = new Color(null, 50,50, 50);
		drawBoardBuffer.setBackground(canvasBackgroundColor);
		drawBoardBuffer.setForeground(black);
		drawBoardBuffer.fillGradientRectangle(0,0,parent.getClientArea().width,parent.getClientArea().height, true);
		
		//g2d.setColor(green);
		
		int startx = 20;
		int k = startx;
		
		int bottomSpace = drawBoardBuffer.getFontMetrics().getHeight() + 2;
		float maximum = 20f;
		float height = (float) (parent.getClientArea().height - bottomSpace);
		
		int width = parent.getClientArea().width;
		int graphWidth = width - startx;
		float zoom = 0, valueY = 0;
		
		StatisticPoint lastPoint = graph.getLast();
		StatisticPoint actualPoint = lastPoint;
		
		drawBoardBuffer.setBackground(graph.graphColor1);
		drawBoardBuffer.setForeground(graph.graphColor2);
				
		// find highest	onscreen point
		for ( ; (k<=width) && (actualPoint.getPrev()!=null); k++, actualPoint = actualPoint.getPrev() )
		{
			valueY = (float) (actualPoint.getValue()/10);
			if (valueY > maximum) maximum = valueY; 
			
		}	
			
		// calculate zoom
		zoom = height / maximum ;
		actualPoint = lastPoint;	
		if ( ((float) (actualPoint.getValue()/10) * zoom)  >  ((height / 5f)*4f)) 
			zoom = (zoom / 5f)*4f;
				
		// draw graph gradient lines
		for ( k=startx ; (k<=width) && (actualPoint.getPrev() != null) ; k++, actualPoint = actualPoint.getPrev())
		{
			valueY = (float) (actualPoint.getValue()/10);	
			valueY = height - (valueY * zoom);
			drawBoardBuffer.fillGradientRectangle(k,(int)height+1,1,(int)(valueY-height),true);
		}
					
		// draw grid
		Color gridColor = new Color(null, 0, 128, 64);
		drawBoardBuffer.setForeground(gridColor);
		
		// vertical lines
		for (int i = startx -1 ; i < startx + graphWidth; i+=20) 
			drawBoardBuffer.drawLine(i,0,i,(int)height + 1);
		
		// horizontal lines					
		for (int i = (int)height + 1; i > 0; i-=20)		
			drawBoardBuffer.drawLine(startx,i,startx+graphWidth,i);
				
		// old grid scale
		// for (int dummy=0; dummy<1; dummy++) {
		//	int value=dummy*2;
		//	drawBoardBuffer.drawText("  " + value,0,height-8-(int)zoom*value*10, true);
		// }
			
		// just for temporary fun; this might overflow pretty quickly
						
		Color textColor = new Color(null, 250, 250, 250);
		drawBoardBuffer.setForeground(textColor);
		drawBoardBuffer.drawText(graph.getName() + 
		 " avg: " + ((double)graph.getAvg()/100) + " kb/s," +
		 " max: " + ((double)graph.getMax()/100) + " kb/s",
		 20, parent.getClientArea().height-drawBoardBuffer.getFontMetrics().getHeight() ,true);
				
		// draw floating box
		double value = (double)graph.getLast().getValue()/100;
		String boxString = String.valueOf(value) + " kb/s";
			
		int linePosition =  (int) (height - (float) (graph.getLast().getValue()/10) * zoom);
		int linePositionEnd = linePosition;
		int textPosition = linePosition - 6;
		if (textPosition + bottomSpace >= (int) height) {
			textPosition = (int) height - bottomSpace - 3;
			linePositionEnd = linePositionEnd - 6;
		}
				
		int boxWidth = drawBoardBuffer.textExtent(boxString).x + 20;
		int boxHeight = drawBoardBuffer.textExtent(boxString).y + 5;	
			
		drawBoardBuffer.setForeground(black);
		drawBoardBuffer.setBackground(white);
		drawBoardBuffer.fillRoundRectangle(startx+10,textPosition,boxWidth,boxHeight,18,18);
		drawBoardBuffer.drawRoundRectangle(startx+10,textPosition,boxWidth,boxHeight,18,18);
		drawBoardBuffer.drawText(boxString,startx+20,textPosition+2);
		drawBoardBuffer.setForeground(yellow);
		drawBoardBuffer.drawLine(startx+10,linePositionEnd,startx,linePosition);
				
		// output buffer to the display
		drawBoard.drawImage(imageBuffer, 0,0);
		imageBuffer.dispose();
		drawBoardBuffer.dispose();
		canvasBackgroundColor.dispose();
		gridColor.dispose();
		textColor.dispose();
	}

	/* (non-Javadoc)
	 * @see org.holongate.eclipse.j2d.IPaintable#redraw(org.eclipse.swt.widgets.Control, org.eclipse.swt.graphics.GC)
	 */
	public void redraw(Control arg0, GC arg1) {
		// TOD Auto-generated method stub
	}

	

}
/*
$Log: GraphPainter.java,v $
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