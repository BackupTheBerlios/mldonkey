package net.mldonkey.g2gui.view;

import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.model.ClientStats;
import net.mldonkey.g2gui.view.statistic.GraphControl;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.graphics.Color;

// rz,
// TODO status-field
// TODO file progress field

/**
 * Statistic Tab
 *
 */

public class StatisticTab
	extends GuiTab
	implements Observer {
	
	GraphControl uploadsGraphControl;
	String uploadsGraphName = "Uploads";
	Color uploadsGraphColor1 = new Color(null, 255,0,0);
	Color uploadsGraphColor2 = new Color(null, 125,0,0);

	GraphControl downloadsGraphControl;	
	String downloadsGraphName = "Downloads";
	Color downloadsGraphColor1 = new Color(null, 0,0,255);
	Color downloadsGraphColor2 = new Color(null, 0,0,125);

	
	/**
	 * default constructor
	 * @param gui The GUI-Objekt representing the top-level gui-layer
	 */
	public StatisticTab(MainTab gui) {
		super(gui);
			
		createButton("StatisticsButton", 
					bundle.getString("TT_StatisticsButton"),
					bundle.getString("TT_StatisticsButtonToolTip"));
				
		createContents(this.content);
		gui.getCore().addObserver(this);
	}

	protected void createContents(Composite parent) {
		SashForm mainSash = new SashForm( parent, SWT.VERTICAL );
		mainSash.setLayout (new FillLayout());
		
		// Top composite for other stats	
		Composite top = new Composite( mainSash, SWT.BORDER );
		top.setLayout( new FillLayout() );
				
		Label tmp = new Label(top, SWT.CENTER);
		tmp.setText("<gui protocol needs more stats>");
		
		// Bottom graph for Sash				
		SashForm graphSash = new SashForm (mainSash, SWT.HORIZONTAL );
		graphSash.setLayout(new FillLayout());
		
		Composite left = new Composite (graphSash, SWT.NONE );
		left.setLayout (new FillLayout() );
		Composite right = new Composite (graphSash, SWT.NONE );
		right.setLayout (new FillLayout() );
					
		downloadsGraphControl = new GraphControl(left, downloadsGraphName, 
									downloadsGraphColor1, downloadsGraphColor2);
									
		uploadsGraphControl = new GraphControl(right, uploadsGraphName, 
						uploadsGraphColor1, uploadsGraphColor2);
			 				
		// Until top composite has stats	 		
		mainSash.setWeights( new int[] {1,11});	 		
			 		
	}

	public void mouseUp(MouseEvent arg0) {}

	public void run() {
	}

	public void update(Observable arg0, Object receivedInfo) {
		if (receivedInfo instanceof ClientStats){
			ClientStats clientInfo = (ClientStats) receivedInfo;
			
			uploadsGraphControl.addPointToGraph(clientInfo.getTcpUpRate());
			downloadsGraphControl.addPointToGraph(clientInfo.getTcpDownRate());
			uploadsGraphControl.redraw();
			downloadsGraphControl.redraw();
							
		}
	}
	
	public void setInActive(boolean removeObserver) {
		// Do not remove Observer
		super.setInActive(false);
	}
	
	public void widgetDisposed(DisposeEvent arg0) {
		// TODO store Column position
	}

}
