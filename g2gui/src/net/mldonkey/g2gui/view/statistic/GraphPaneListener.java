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

import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.SashGPaneListener;
import net.mldonkey.g2gui.view.viewers.actions.FlipSashAction;
import net.mldonkey.g2gui.view.viewers.actions.MaximizeAction;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;

/**
 * GraphPaneListener
 *
 * @version $Id: GraphPaneListener.java,v 1.2 2003/10/31 13:16:33 lemmster Exp $ 
 *
 */
public class GraphPaneListener extends SashGPaneListener {
	//TODO move strings into resource bundle
	private GraphControl graphControl;
	public GraphPaneListener( SashForm sashForm, Control control, GraphControl graphControl ) {
		super( null, null, sashForm, control );
		this.graphControl = graphControl;
	}

	public void menuAboutToShow( IMenuManager menuManager ) {
		menuManager.add( new HourlyGraphHistoryAction( graphControl.getGraph() ) );
		menuManager.add( new ClearGraphHistoryAction( graphControl.getGraph() ) );

		// flip sash/maximize sash
		menuManager.add( new Separator() );
		menuManager.add( new FlipSashAction( this.sashForm ) );
		menuManager.add( new MaximizeAction( this.sashForm, this.control ) );
	}


	/**
	 * GraphHistoryAction
	 */
	public class HourlyGraphHistoryAction extends Action {
		Graph graph;

		public HourlyGraphHistoryAction( Graph graph ) {
			super( "Hourly graph history" );
			setImageDescriptor( G2GuiResources.getImageDescriptor( "graph" ) );
			this.graph = graph;
		}

		public void run() {
			new GraphHistory( graph );
		}
	}

	/**
	 * ClearGraphHistoryAction
	 */
	public class ClearGraphHistoryAction extends Action {
		Graph graph;

		public ClearGraphHistoryAction( Graph graph ) {
			super( "Clear graph history" );
			setImageDescriptor( G2GuiResources.getImageDescriptor( "clear" ) );
			this.graph = graph;
		}

		public void run() {
			MessageBox confirm =
				new MessageBox( graphControl.getShell(), SWT.YES | SWT.NO | SWT.ICON_QUESTION );
			confirm.setMessage( G2GuiResources.getString( "MISC_AYS" ) );
			if ( confirm.open() == SWT.YES )
				graph.clearHistory();
		}
	}
}

/*
$Log: GraphPaneListener.java,v $
Revision 1.2  2003/10/31 13:16:33  lemmster
Rename Viewer -> Page
Constructors changed

Revision 1.1  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

*/