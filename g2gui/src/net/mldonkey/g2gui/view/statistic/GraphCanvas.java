/*
 * Created on 07.07.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.mldonkey.g2gui.view.statistic;


import org.eclipse.swt.SWT;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;




/**
 * @author achim
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GraphCanvas extends Canvas {

	Composite parent;
	private Thread thread;
	Image canvas;
	final private GraphPainter gp;
	
	public GraphCanvas(Composite parent_)
	 {
	super(parent_,SWT.NO_BACKGROUND);
		parent = parent_;
		gp = new GraphPainter(parent);

	
	addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
			   GraphCanvas.this.paintControl(e);

			}

		});
	 }
		
		private void paintControl(PaintEvent e)
		{
			GC gc = e.gc;
			gp.setGraphicControl(gc);
			gp.paint();		
		}
		
	public void setGraph(Graph graph)
	{
		gp.setGraph(graph);
	}

			
		




}
