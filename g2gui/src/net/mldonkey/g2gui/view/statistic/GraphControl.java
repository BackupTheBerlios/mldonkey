/*
 * Created on 07.07.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.mldonkey.g2gui.view.statistic;


import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.*;


public class GraphControl extends Composite{

	private GraphCanvas graphCanvas1,graphCanvas2;

	private Composite parent;
	private GraphPainter graphPainter;
	private Graph uploadGraph,downloadGraph;
	
	
	public GraphControl(Composite parent) {
		super(parent,SWT.BORDER);
		this.parent = parent;
		graphCanvas1 = new GraphCanvas(this);
		graphCanvas2 = new GraphCanvas(this);
		setLayout(new FillLayout());
		layout(true);
			
		System.out.println("parent widget" + parent.getBounds());
		uploadGraph = new Graph( new Color(null,244,0,0) );
		downloadGraph = new Graph(new Color(null,0,244,0) );
		
		// this.setSize(400,200);
		//graphCanvas1.redraw();
		//graphCanvas2.redraw();
		graphCanvas1.setGraph(downloadGraph);
		graphCanvas2.setGraph(uploadGraph);

		
		
	/*	addControlListener(new ControlAdapter() {
				 public void controlResized(ControlEvent e) {
					GraphControl.this.controlResized(e);					
				 }

			 }); */

		 

	}
	
	/**
	 * Resizes the Canvas everyTime the Control gets resized
	 */
	/*protected void controlResized( ) {
		System.out.println("controlResized"+ this.getBounds());
		graphCanvas1.setSize(this.getBounds().width,this.getBounds().height/2);
		graphCanvas2.setSize(this.getBounds().width,this.getBounds().height/2);
		
		
	}*/

	public void redraw()
	{
		if ( !parent.isDisposed() )				
					parent.getDisplay().asyncExec( new Runnable () {
						public void run() {
							graphCanvas1.redraw();
							graphCanvas2.redraw();
						}
					});
	}
	

	

	
	public void addPointToUploadGraph(float value)
	{
		uploadGraph.addPoint((int)(value*100));

	}

	
	public void addPointToDownloadGraph(float value) {
		downloadGraph.addPoint((int)(value*100));		
		
	}
	
	
}