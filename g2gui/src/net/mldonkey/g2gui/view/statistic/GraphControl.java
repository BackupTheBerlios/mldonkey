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
			
	//	System.out.println("parent widget" + parent.getBounds());
		uploadGraph = new Graph(new Color(null,244,0,0), "Uploads" );
		downloadGraph = new Graph(new Color(null,0,0,244), "Downloads" );
		
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
							if (!graphCanvas1.isDisposed()) graphCanvas1.redraw();
							if (!graphCanvas2.isDisposed()) graphCanvas2.redraw();
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

/*
$Log: GraphControl.java,v $
Revision 1.8  2003/07/26 05:42:39  zet
cleanup



*/