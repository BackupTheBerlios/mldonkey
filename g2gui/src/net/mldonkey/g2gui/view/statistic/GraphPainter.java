/*
 * Created on 07.07.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
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
	private Graph graphs[] = new Graph[10];
	 private GC drawBoard;
	final private Composite parent;
	
	public GraphPainter(Composite parent_)
	{
		System.out.println("GraphPainter added");
		
		parent = parent_;
	}
	
	public void setGraphicControl(GC gc)
	{
		drawBoard = gc;
	}
	
	public void setGraph(Graph graph_)
	{
		int where = getLastGraphIndex();
		graphs[where] = graph_;
		System.out.println("graph added to" +where);
	}
	
	public int getLastGraphIndex()
	{
		int i = 0;
		while (graphs[i]!=null)
		{
			i++;
		}
		return i;
		
	}
	
	public void paint() {
		//setting the Canvas Background to the parents Background
		
		drawBoard.setBackground(new Color(null,255,255,255));
		drawBoard.fillRectangle(0,0,parent.getBounds().width,parent.getBounds().height);
		//g2d.setColor(green);
		
		int which = 0;
		int maximum = 20;
		while (graphs[which]!=null)
		
		{
			int red = graphs[which].getGraphColor().getRed();
			int blue = graphs[which].getGraphColor().getBlue();
			int green = graphs[which].getGraphColor().getGreen();

			
			Color graphColor = new Color(null,red,blue,green);
			drawBoard.setForeground(graphColor);
			int width = parent.getBounds().width;
			int height = parent.getBounds().height;
			int fac = 0;
			

			StatisticPoint lastPoint = graphs[which].getLast();
			StatisticPoint actualPoint = lastPoint;
			int k = 0;
			int valueY = 0;
			while ( (k<=width) & (actualPoint.getPrev()!=null))
			{
				System.out.println("drawPoint");
				valueY = (int)(actualPoint.getValue())/10;
				if (valueY > maximum) 
				{
					maximum = valueY;
				
				} 
				fac = height/maximum; 
				System.out.println("valueY" + valueY + " - Maximum:"+ maximum);
				valueY = height - valueY*fac;
				
				drawBoard.drawLine(k,height,k,valueY);
				actualPoint = actualPoint.getPrev();
				k++;
			}
			double vv = (double)graphs[which].getLast().getValue()/100;
			
			
			System.out.println("wert:" + (height-(int)vv));
			int textPosition = height - graphs[which].getLast().getValue()/10*fac;
			Color aimColor = new Color(null,0,0,0);
			drawBoard.setForeground(aimColor);
			drawBoard.setBackground(new Color(null,255,255,255));
			drawBoard.fillRoundRectangle(10,textPosition-3,90,20,7,7);
			drawBoard.drawRoundRectangle(10,textPosition-3,90,20,7,7);
			drawBoard.drawText("wert: " +vv,0+20,textPosition);
			drawBoard.drawLine(10,textPosition,0,height - graphs[which].getLast().getValue()/10*fac);
	
		
			which++;
			
			
		}
		
		
		
		
		
		
		
	}

	/* (non-Javadoc)
	 * @see org.holongate.eclipse.j2d.IPaintable#redraw(org.eclipse.swt.widgets.Control, org.eclipse.swt.graphics.GC)
	 */
	public void redraw(Control arg0, GC arg1) {
		// TODO Auto-generated method stub
		
	}

}
