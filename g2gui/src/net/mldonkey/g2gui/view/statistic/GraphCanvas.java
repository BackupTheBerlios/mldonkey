/*
 * Created on 07.07.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.mldonkey.g2gui.view.statistic;


import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.widgets.Composite;
import net.mldonkey.g2gui.view.statistic.j2d.J2DCanvas;



/**
 * @author achim
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GraphCanvas extends J2DCanvas implements Runnable {

	Composite parent;
	
	public GraphCanvas(Composite parent_, GraphPainter graphPainter)
	 {
	super(parent_,graphPainter);
		parent = parent_;
	}




	public void doRedrawStrategy(PaintEvent evt) {

			super.doRedrawStrategy(evt);
			
			// Start a new cycle as soon as possible
			getDisplay().asyncExec(this);
			
		}
	
		/**
		 * When the dispatching thread dequeue the asynchExec call for this control,
		 * it immediatly starts a new paint image cycle.
		 */
		public void run() {
			
			if (!isDisposed()) {
				
				
				try {
				paintImage();
				}
				catch (org.eclipse.swt.SWTException e)
				{
					//Thread Access not allowed (yet)
				}
			}
		}




}
