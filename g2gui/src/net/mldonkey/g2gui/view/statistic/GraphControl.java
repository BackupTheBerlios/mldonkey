/*
 * Created on 07.07.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.mldonkey.g2gui.view.statistic;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.*;


public class GraphControl extends Composite{

private GraphCanvas graphCanvas;
	/**
	 * 
	 */
	Composite parent;
	GraphPainter graphPainter;
	Graph uploadGraph,downloadGraph;
	
	
	public GraphControl(Composite _parent) {
		super(_parent,SWT.BORDER);
		parent = _parent;
		graphPainter = new GraphPainter();
		graphCanvas = new GraphCanvas(this,graphPainter);
		Thread gcThread = new Thread(graphCanvas);
		gcThread.start();

	
		System.out.println("parent widget" + parent.getBounds());
		
		int uploadColor[] = {1,3,6 };
		uploadGraph = new Graph(uploadColor);
		
		int downloadColor[] = {122,3,0};
		downloadGraph = new Graph(downloadColor);
		
		this.setSize(400,200);
		
		
		
		
		graphPainter.setGraph(downloadGraph);
		graphPainter.setGraph(uploadGraph);


		
		System.out.println("The Image Canvas:" + graphCanvas.getBounds());
		graphCanvas.paintImage();
		
		
		addControlListener(new ControlAdapter() {
				 public void controlResized(ControlEvent e) {
					GraphControl.this.controlResized(e);					
				 }

			 });

		 

	}
	
	/**
	 * Resizes the Canvas everyTime the Control gets resized
	 */
	protected void controlResized(ControlEvent e) {
		System.out.println("controlResized"+ this.getBounds());
		graphCanvas.setSize(this.getBounds().width,this.getBounds().height);
		
		
		
	}

	public void redraw()
	{
		System.out.println("redraw on ImageLabel is called");
		//imageCanvas.redraw();
		
	}
	

	
	public void addPointToUploadGraph(float value)
	{
		uploadGraph.addPoint(value);

	}

	
	public void addPointToDownloadGraph(float value) {
		downloadGraph.addPoint(value);

		
		
	}
	
	
}