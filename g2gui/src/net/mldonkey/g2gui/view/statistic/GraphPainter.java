/*
 * Created on 07.07.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.mldonkey.g2gui.view.statistic;

import java.awt.Graphics2D;
import java.awt.Color;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Control;
import net.mldonkey.g2gui.view.statistic.j2d.IPaintable;

/**
 * @author achim
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GraphPainter implements IPaintable {

	/* (non-Javadoc)
	 * paint gets called from Canvas and Graphics2D is able to draw Transparent Images
	 * for this you need the awt.Color Package because SWT.graphics.color doesn't have an alpha channel
	 */
	 
	 
	private int graph;
	private Graph graphs[] = new Graph[10];
	
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
	
	public void paint(Control control, Graphics2D g2d) {
		//setting the Canvas Background to the parents Background
		Color transparent = new Color(control.getBackground().getGreen(),control.getBackground().getGreen(),control.getBackground().getGreen());
		//Color green = new Color(0,255,0);
		g2d.setColor(transparent);
		g2d.fillRect(0,0,control.getBounds().width,control.getBounds().height);
		//g2d.setColor(green);
		int which = 0;
		while (graphs[which]!=null)
		
		{
			int red = graphs[which].getGraphColor()[0];
			int blue = graphs[which].getGraphColor()[1];
			int green = graphs[which].getGraphColor()[2];
			Color graphColor = new Color(red,blue,green);
			g2d.setColor(graphColor);
			int valueY = (int)(graphs[which].getLast().getValue()*10);
			g2d.drawLine(0,valueY,control.getBounds().width,valueY);
			System.out.println("Male Graph" + which +" hat den wert" + valueY);
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
