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
		//System.out.println("graph added to" +where);
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
		
	
		if (parent.getClientArea().height < 25) return;
		
		Image imageBuffer = new Image(null, parent.getBounds());
		GC drawBoardBuffer = new GC(imageBuffer);
		drawBoardBuffer.setBackground(new Color(null,50,50,50));
		drawBoardBuffer.setForeground(new Color(null,0,0,0));
						
		drawBoardBuffer.fillGradientRectangle(0,0,parent.getBounds().width,parent.getBounds().height, true);
		//g2d.setColor(green);
		
		int which = 0;
		int maximum = 20;
		while (graphs[which]!=null)
		
		{
			int red = graphs[which].getGraphColor().getRed();
			int blue = graphs[which].getGraphColor().getBlue();
			int green = graphs[which].getGraphColor().getGreen();

			
			Color graphColor = new Color(null,red,blue,green);
			drawBoardBuffer.setBackground(graphColor);
			drawBoardBuffer.setForeground(new Color(null, red > 100 ? red - 100 : red,
														  blue > 100 ? blue - 100 : blue, 
														  green > 100 ? green - 100 : green));
			int width = parent.getBounds().width;
			int height = parent.getBounds().height-20;
			int fac = 0;
			

			StatisticPoint lastPoint = graphs[which].getLast();
			StatisticPoint actualPoint = lastPoint;
			
			int startx = 20;
			int k = startx;
			int valueY = 0;
			while ( (k<=width) && (actualPoint.getPrev()!=null))
			{
				//System.out.println("drawPoint");
				valueY = (int)(actualPoint.getValue())/10;
				if (valueY > maximum) 
				{
					maximum = valueY;
				
				} 
				fac = height/maximum; 
				
				valueY = height - valueY*fac;
				
				//drawBoardBuffer.drawLine(k,height,k,valueY);
				
				drawBoardBuffer.fillGradientRectangle(k,height,1,valueY-height,true);
				actualPoint = actualPoint.getPrev();
				k++;

			}
			drawBoardBuffer.setForeground(new Color(null,0,128,64));
			drawBoardBuffer.drawLine(startx,height+1,width-40,height+1);
			
			for (int i = 0; i < 10; i++) 
				drawBoardBuffer.drawLine(startx+i*(width/20),0,startx+i*(width/20),height);
					
			for (int dummy=height/10;dummy<height;dummy=dummy+height/10)
				drawBoardBuffer.drawLine(20,height-dummy,width/2,height-dummy);
			
			
			// TODO: fix this
			for (int dummy=0; dummy<1; dummy++) {
			int value=dummy*2;
			drawBoardBuffer.drawText("  " + value,0,height-8-fac*value*10, true);
			}
			
			double vv = (double)graphs[which].getLast().getValue()/100;
			
			
			//System.out.println("wert:" + (height-(int)vv));
			int textPosition = height - graphs[which].getLast().getValue()/10*fac;
			Color aimColor = new Color(null,0,0,0);
			drawBoardBuffer.setForeground(aimColor);
			drawBoardBuffer.setBackground(new Color(null,255,255,255));
			drawBoardBuffer.fillRoundRectangle(startx+10,textPosition-3,80,20,7,7);
			drawBoardBuffer.drawRoundRectangle(startx+10,textPosition-3,80,20,7,7);
			drawBoardBuffer.drawText(vv + " KB/s",startx+20,textPosition);
			drawBoardBuffer.setForeground(new Color(null, 255,255,0));
			drawBoardBuffer.drawLine(startx+10,textPosition,startx,height - graphs[which].getLast().getValue()/10*fac);
	
		
			which++;
			
			
		};
		
		
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
