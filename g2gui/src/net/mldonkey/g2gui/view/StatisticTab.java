package net.mldonkey.g2gui.view;

import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.model.ClientStats;
import net.mldonkey.g2gui.view.statistic.GraphControl;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;



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
	
	GraphControl graphControl;

	
	/**
	 * default constructor
	 * @param gui The GUI-Objekt representing the top-level gui-layer
	 */
	public StatisticTab(MainTab gui) {
		super(gui);
		activeIm = 
		inActiveIm = MainTab.createTransparentImage ( 
						new Image(toolItem.getParent().getDisplay(), 
						"src/icons/statistics.png"),
					toolItem.getParent());
				
		toolItem.setText(bundle.getString("TT_StatisticsButton"));
		toolItem.setToolTipText(bundle.getString("TT_StatisticsButtonToolTip"));
		toolItem.setImage(inActiveIm); 
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
			
			graphControl.addPointToUploadGraph(clientInfo.getTcpUpRate());
			graphControl.addPointToDownloadGraph(clientInfo.getTcpDownRate());
			graphControl.redraw();
			
			
			//graphControl.setGraph((int)(clientInfo.getTcpUpRate()*10));
			
		
		
		}
	
	
	
	}


	public void widgetDisposed(DisposeEvent arg0) {
		// TODO store Column position
	}





}
