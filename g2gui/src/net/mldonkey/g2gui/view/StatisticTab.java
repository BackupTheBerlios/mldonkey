/*
 * Copyright 2003
 * G2GUI Team
 * 
 * 
 * This file is part of G2GUI.
 *
 * G2GUI is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * ( at your option ) any later version.
 *
 * G2GUI is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2GUI; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */

package net.mldonkey.g2gui.view;

import java.util.Observable;

import net.mldonkey.g2gui.model.ClientStats;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.statistic.GraphControl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Statistic Tab
 * @author $Author: zet $
 * @version $Id: StatisticTab.java,v 1.18 2003/08/23 01:12:43 zet Exp $
 */

public class StatisticTab extends GuiTab {
	
	GraphControl uploadsGraphControl;
	String uploadsGraphName = "Uploads";
	Color uploadsGraphColor1 = new Color( null, 255, 0, 0 );
	Color uploadsGraphColor2 = new Color( null, 125, 0, 0 );

	GraphControl downloadsGraphControl;	
	String downloadsGraphName = "Downloads";
	Color downloadsGraphColor1 = new Color( null, 0, 0, 255 );
	Color downloadsGraphColor2 = new Color( null, 0, 0, 125 );

	
	/**
	 * default constructor
	 * @param gui The GUI-Objekt representing the top-level gui-layer
	 */
	public StatisticTab( MainTab gui ) {
		super( gui );
			
		createButton( "StatisticsButton", 
					G2GuiResources.getString( "TT_StatisticsButton" ),
					G2GuiResources.getString( "TT_StatisticsButtonToolTip" ) );
				
		createContents( this.subContent );
		gui.getCore().getClientStats().addObserver( this );
	}

	protected void createContents( Composite parent)  {
		SashForm mainSash = new SashForm( parent, SWT.VERTICAL );
		mainSash.setLayout (new FillLayout());
		
		// Top composite for other stats	
		Composite top = new Composite( mainSash, SWT.BORDER );
		top.setLayout( new FillLayout() );
				
		Label tmp = new Label( top, SWT.CENTER );
		tmp.setText("<gui protocol needs more stats>");
		
		// Bottom graph for Sash				
		SashForm graphSash = new SashForm ( mainSash, SWT.HORIZONTAL );
		graphSash.setLayout( new FillLayout() );
		
		Composite left = new Composite( graphSash, SWT.NONE );
		left.setLayout( new FillLayout() );
		Composite right = new Composite( graphSash, SWT.NONE );
		right.setLayout( new FillLayout() );
					
		downloadsGraphControl = new GraphControl( left, downloadsGraphName, 
									downloadsGraphColor1, downloadsGraphColor2 );
									
		uploadsGraphControl = new GraphControl( right, uploadsGraphName, 
						uploadsGraphColor1, uploadsGraphColor2 );
			 				
		// Until top composite has stats	 		
		mainSash.setWeights( new int[] { 0, 1111 } );	 		
	}

	public void mouseUp( MouseEvent arg0 ) {}

	public void run() {
	}

	public void update( Observable arg0, Object receivedInfo ) {
		ClientStats clientInfo = ( ClientStats ) receivedInfo;
		uploadsGraphControl.addPointToGraph( clientInfo.getTcpUpRate() );
		downloadsGraphControl.addPointToGraph( clientInfo.getTcpDownRate() );
		uploadsGraphControl.redraw();
		downloadsGraphControl.redraw();
	}
	
	public void setInActive( boolean removeObserver ) {
		// Do not remove Observer
		super.setInActive();
	}
	
	public void widgetDisposed( DisposeEvent arg0 ) {
		
	}

}
/*
$Log: StatisticTab.java,v $
Revision 1.18  2003/08/23 01:12:43  zet
remove todos

Revision 1.17  2003/08/22 21:06:48  lemmster
replace $user$ with $Author: zet $

Revision 1.16  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.15  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging


*/

