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

/**
 * 
 * GraphControl
 *
 * @author $Author: lemmster $
 * @version $Id: GraphControl.java,v 1.10 2003/08/22 21:13:11 lemmster Exp $ 
 *
 */
public class GraphControl extends Composite{

	private GraphCanvas graphCanvas;

	private Composite parent;
	private GraphPainter graphPainter;
	private Graph graph;
		
	public GraphControl(Composite parent, String name, Color color1, Color color2) {
		super(parent,SWT.BORDER);
		this.parent = parent;
		graphCanvas = new GraphCanvas(this);
		setLayout(new FillLayout());
		layout(true);
			
		graph = new Graph(name, color1, color2);
		
		// this.setSize(400,200);
		//graphCanvas1.redraw();
		//graphCanvas2.redraw();
		graphCanvas.setGraph(graph);
		
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
							if (!graphCanvas.isDisposed()) graphCanvas.redraw();
						//	if (!graphCanvas2.isDisposed()) graphCanvas2.redraw();
						}
					});
	}
	
	public void addPointToGraph(float value)
	{
		graph.addPoint((int)(value*100));
	}
}

/*
$Log: GraphControl.java,v $
Revision 1.10  2003/08/22 21:13:11  lemmster
replace $user$ with $Author$

Revision 1.9  2003/07/26 17:54:14  zet
fix pref's illegal setParent, redo graphs, other

Revision 1.8  2003/07/26 05:42:39  zet
cleanup



*/