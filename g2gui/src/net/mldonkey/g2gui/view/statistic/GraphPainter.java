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

import org.eclipse.swt.graphics.*;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;


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
	 
	 
	private int graph;
	private Graph graphs[] = new Graph[2];
	private GC drawBoard;
	final private Composite parent;
	
	public GraphPainter(Composite parent)
	{
		//System.out.println("GraphPainter added");
		this.parent = parent;
	}
	
	public void setGraphicControl(GC gc)
	{
		drawBoard = gc;
	}
	
	public void setGraph(Graph graph_)
	{
		int where = getLastGraphIndex();
		graphs[where] = graph_;
		//System.out.println("graph added to" +where);
	}
	
	public int getLastGraphIndex()
	{
		int i = 0;
		while (graphs[i]!=null)
			i++;
		return i;
		
	}
	
	public void paint() {
		//setting the Canvas Background to the parents Background
			
		if (parent.getClientArea().height < 45) return;
		
		Image imageBuffer = new Image(null, parent.getBounds());
		GC drawBoardBuffer = new GC(imageBuffer);
		drawBoardBuffer.setBackground(new Color(null,50,50,50));
		drawBoardBuffer.setForeground(new Color(null,0,0,0));
		drawBoardBuffer.fillGradientRectangle(0,0,parent.getClientArea().width,parent.getClientArea().height, true);
		
		//g2d.setColor(green);
		
		int startx = 20;
		
		int k = startx;
		int which = 0;
		float maximum = 20f;
		float height = (float) (parent.getClientArea().height - drawBoardBuffer.getFontMetrics().getHeight() - 2);
		int width = parent.getClientArea().width;
		int graphWidth = (width / 2) - startx;
		float zoom, valueY;
				
		while (graphs[which]!=null)
		{	
			StatisticPoint lastPoint = graphs[which].getLast();
			StatisticPoint actualPoint = lastPoint;
					
			int red = graphs[which].getGraphColor().getRed();
			int green = graphs[which].getGraphColor().getGreen();
			int blue = graphs[which].getGraphColor().getBlue();
										
			Color graphColor = new Color(null,red,green,blue);
			drawBoardBuffer.setBackground(graphColor);
			drawBoardBuffer.setForeground(new Color(null, red > 100 ? red - 100 : red,
														  green > 100 ? green - 100 : green, 
														  blue > 100 ? blue - 100 : blue));
			zoom = 0;
			valueY = 0;
		
			// find highest	
			while ( (k<=width/2) && (actualPoint.getPrev()!=null))
			{
				
				valueY = (float) (actualPoint.getValue()/10);
				if (valueY > maximum) maximum = valueY; 
				actualPoint = actualPoint.getPrev();
				k++;	
			}	
			
			// calculate zoom
			zoom = height / maximum ;
			actualPoint = lastPoint;	
			if ( ((float) (actualPoint.getValue()/10) * zoom)  >  ((height / 5f)*4f)) 
				zoom = (zoom / 5f)*4f;
			k=startx;
			
			// draw gradient lines
			while ( (k<=width/2) && (actualPoint.getPrev()!=null))	{
			
				
				valueY = (float) (actualPoint.getValue()/10);	
				valueY = height - (valueY * zoom);
				
				//drawBoardBuffer.drawLine(k,height,k,valueY);
				
				drawBoardBuffer.fillGradientRectangle(k,(int)height+1,1,(int)(valueY-height),true);
				actualPoint = actualPoint.getPrev();
				k++;

			}
			
			
			// draw grid
			drawBoardBuffer.setForeground(new Color(null,0,128,64));
			
			for (int i = startx -1 ; i < startx + graphWidth; i+=20) 
				drawBoardBuffer.drawLine(i,0,i,(int)height + 1);
							
			for (int i = (int)height + 1; i > 0; i-=20)		
				drawBoardBuffer.drawLine(startx,i,startx+graphWidth,i);
			
			
			// old grid scale
			// for (int dummy=0; dummy<1; dummy++) {
			//	int value=dummy*2;
			//	drawBoardBuffer.drawText("  " + value,0,height-8-(int)zoom*value*10, true);
			// }
			
			// just for temporary fun; this might overflow pretty quickly
						
			 drawBoardBuffer.setForeground(graphColor);
			 drawBoardBuffer.drawText(graphs[which].getName() + 
				 " avg: " + ((double)graphs[which].getAvg()/100) + " kb/s," +
				 " max: " + ((double)graphs[which].getMax()/100) + " kb/s",
				 20, parent.getClientArea().height-drawBoardBuffer.getFontMetrics().getHeight() ,true);
				
			// draw floating box
			
			double value = (double)graphs[which].getLast().getValue()/100;
			String boxString = String.valueOf(value) + " kb/s";
			
			int textPosition = (int) (height - (float) (graphs[which].getLast().getValue()/10) * zoom);
			int boxWidth = drawBoardBuffer.textExtent(boxString).x + 20;
			int boxHeight = drawBoardBuffer.textExtent(boxString).y + 5;	
			
			drawBoardBuffer.setForeground(new Color(null,0,0,0));
			drawBoardBuffer.setBackground(new Color(null,255,255,255));
			drawBoardBuffer.fillRoundRectangle(startx+10,textPosition-3,boxWidth,boxHeight,18,18);
			drawBoardBuffer.drawRoundRectangle(startx+10,textPosition-3,boxWidth,boxHeight,18,18);
			drawBoardBuffer.drawText(boxString,startx+20,textPosition);
			drawBoardBuffer.setForeground(new Color(null, 255,255,0));
			drawBoardBuffer.drawLine(startx+10,textPosition,startx,textPosition);
				
			which++;
		}
			
		drawBoard.drawImage(imageBuffer, 0,0);
		imageBuffer.dispose();
		drawBoardBuffer.dispose();
	}

	/* (non-Javadoc)
	 * @see org.holongate.eclipse.j2d.IPaintable#redraw(org.eclipse.swt.widgets.Control, org.eclipse.swt.graphics.GC)
	 */
	public void redraw(Control arg0, GC arg1) {
		// TODO Auto-generated method stub
	}

}
/*
$Log: GraphPainter.java,v $
Revision 1.18  2003/07/26 05:42:39  zet
cleanup



*/