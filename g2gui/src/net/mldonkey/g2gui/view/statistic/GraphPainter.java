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
		drawBoardBuffer.fillGradientRectangle(0,0,parent.getBounds().width,parent.getBounds().height, true);
		
		//g2d.setColor(green);
		
		int startx = 20;
		int k = startx;
		int which = 0;
		float maximum = 20f;
		float height = (float) (parent.getClientArea().height - drawBoardBuffer.getFontMetrics().getHeight() - 2);
		int width = parent.getBounds().width;
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
			
			zoom = height / maximum ;
			actualPoint = lastPoint;	
			if ( ((float) (actualPoint.getValue()/10) * zoom)  >  ((height / 5f)*4f)) 
				zoom = (zoom / 5f)*4f;
			k=startx;
			
			// draw
			while ( (k<=width/2) && (actualPoint.getPrev()!=null))	{
			
				
				valueY = (float) (actualPoint.getValue()/10);	
				valueY = height - (valueY * zoom);
				
				//drawBoardBuffer.drawLine(k,height,k,valueY);
				
				drawBoardBuffer.fillGradientRectangle(k,(int)height,1,(int)(valueY-height),true);
				actualPoint = actualPoint.getPrev();
				k++;

			}
			drawBoardBuffer.setForeground(new Color(null,0,128,64));
			drawBoardBuffer.drawLine(startx,(int)(height+1f),width-40,(int)(height+1));
						
			for (int i = 0; i < 10; i++) 
				drawBoardBuffer.drawLine(startx+i*(width/20),0,startx+i*(width/20),(int)height);
					
			for (int dummy=(int)(height/10);dummy<(int)height;dummy=dummy+(int)(height/10f))
				drawBoardBuffer.drawLine(20,(int)height-dummy,width/2,(int)height-dummy);
			
			
			// TODO: fix this
			// for (int dummy=0; dummy<1; dummy++) {
			//	int value=dummy*2;
			//	drawBoardBuffer.drawText("  " + value,0,height-8-(int)zoom*value*10, true);
			// }
			
//			just for temporary fun .. this will overflow pretty quickly
			
			
			 drawBoardBuffer.setForeground(graphColor);
			 drawBoardBuffer.drawText(graphs[which].getName() + 
				 " avg: " + ((double)graphs[which].getAvg()/100) + " kb/s," +
				 " max: " + ((double)graphs[which].getMax()/100) + " kb/s",
				 20, parent.getClientArea().height-drawBoardBuffer.getFontMetrics().getHeight() ,true);
			
			double vv = (double)graphs[which].getLast().getValue()/100;
			String vvs = String.valueOf(vv);
			int textPosition = (int) (height - (float) (graphs[which].getLast().getValue()/10) * zoom);
			
			int boxWidth = drawBoardBuffer.getFontMetrics().getAverageCharWidth() * (vvs.length() + 10)  ;
						
			drawBoardBuffer.setForeground(new Color(null,0,0,0));
			drawBoardBuffer.setBackground(new Color(null,255,255,255));
			drawBoardBuffer.fillRoundRectangle(startx+10,textPosition-3,boxWidth,20,7,7);
			drawBoardBuffer.drawRoundRectangle(startx+10,textPosition-3,boxWidth,20,7,7);
			drawBoardBuffer.drawText(vv + " kb/s",startx+20,textPosition);
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
