package net.mldonkey.g2gui.view;

import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;


import net.mldonkey.g2gui.model.ClientStats;
import net.mldonkey.g2gui.view.statistic.GraphCanvas;
import net.mldonkey.g2gui.view.statistic.GraphControl;



import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


// rz,
// TODO status-field
// TODO file progress field

/**
 * 
 * DownloadTab
 *
 * Implements the view-layer for all download-related actions.
 * 
 * @author mitch
 * @version 0.1 
 * @todo Implement table-sorting, status-fields, progress-bars
 *
 */

public class StatisticTab
	extends G2guiTab
	implements Observer {


	ResourceBundle bundle = ResourceBundle.getBundle("g2gui");
	GraphControl graphControl;

	
	/**
	 * default constructor
	 * @param gui The GUI-Objekt representing the top-level gui-layer
	 */
	public StatisticTab(Gui gui) {
		super(gui);
		toolItem.setText(bundle.getString("TT_StatisticButton"));
		/*toolItem.setImage(
			Gui.createTransparentImage(
				new Image(
					toolItem.getParent().getDisplay(),
					"src/icons/"),
				toolItem.getParent()));*/
		createContents(this.content);
		

		gui.getCore().addObserver(this);
	}
	

	
	protected void createContents(Composite parent) {
		System.out.println(parent.getBounds());
		graphControl = new GraphControl(parent);
		 
		
	}

	

	public void mouseUp(MouseEvent arg0) {}

	public void run() {
		
	}

	public void update(Observable arg0, Object receivedInfo) {
		if (receivedInfo instanceof ClientStats){
			ClientStats clientInfo = (ClientStats) receivedInfo;
			System.out.println(clientInfo.getTcpUpRate());
			System.out.println(clientInfo.getTcpDownRate());
			
			graphControl.addPointToUploadGraph(clientInfo.getTcpUpRate());
			graphControl.addPointToDownloadGraph(clientInfo.getTcpDownRate());
			
			
			//graphControl.setGraph((int)(clientInfo.getTcpUpRate()*10));
			
		
		
		}
	
	
	
	}


	public void widgetDisposed(DisposeEvent arg0) {
		// TODO store Column position
	}





}
