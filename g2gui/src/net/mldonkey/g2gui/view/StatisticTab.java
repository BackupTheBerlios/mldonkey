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
import net.mldonkey.g2gui.view.helper.CCLabel;
import net.mldonkey.g2gui.view.helper.HeaderBarMenuListener;
import net.mldonkey.g2gui.view.helper.MaximizeSashMouseAdapter;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.statistic.Graph;
import net.mldonkey.g2gui.view.statistic.GraphControl;
import net.mldonkey.g2gui.view.statistic.GraphHistory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;


/**
 * Statistic Tab
 *
 * @version $Id: StatisticTab.java,v 1.35 2003/10/22 01:36:59 zet Exp $
 */
public class StatisticTab extends GuiTab {
    private GraphControl uploadsGraphControl;
    private GraphControl downloadsGraphControl;

    /**
     * default constructor
     * @param gui object representing the top level gui layer
     */
    public StatisticTab( MainTab gui ) {
        super( gui );
        createButton( "StatisticsButton", G2GuiResources.getString( "TT_StatisticsButton" ), G2GuiResources.getString( "TT_StatisticsButtonToolTip" ) );
        createContents( this.subContent );
        gui.getCore(  ).getClientStats(  ).addObserver( this );
    }

    /**
     * @param parent
     */
    protected void createContents( Composite parent ) {
        SashForm mainSash = new SashForm( parent, SWT.VERTICAL );
        mainSash.setLayout( new FillLayout(  ) );

        /* Top composite for other stats */
        createStatsComposite( mainSash );

        /* Bottom Sash for graphs */
        final SashForm graphSash = new SashForm( mainSash, (PreferenceLoader.loadBoolean("graphSashHorizontal") ? SWT.HORIZONTAL : SWT.VERTICAL) );
		graphSash.addDisposeListener( new DisposeListener() { 
			public void widgetDisposed(DisposeEvent e) {
				PreferenceStore p = PreferenceLoader.getPreferenceStore();
				p.setValue("graphSashHorizontal", ( graphSash.getOrientation() == SWT.HORIZONTAL ? true : false) );
			}
		});

        downloadsGraphControl = createGraph( graphSash, "TT_Downloads" );
        uploadsGraphControl = createGraph( graphSash, "TT_Uploads" );

        /* Until top composite has stats */
        mainSash.setWeights( new int[] { 0, 10 } );
    }

    /**
     * Create stats sash
     * @param mainSash
     */
    private void createStatsComposite( final SashForm mainSash ) {
        ViewForm statsViewForm = new ViewForm( mainSash, SWT.BORDER | ( PreferenceLoader.loadBoolean( "flatInterface" ) ? SWT.FLAT : SWT.NONE ) );
        statsViewForm.setLayoutData( new GridData( GridData.FILL_BOTH ) );

        CLabel statsCLabel = CCLabel.createCL( statsViewForm, "TT_StatisticsButton", "StatisticsButtonSmallTitlebar" );

        Composite statsComposite = new Composite( statsViewForm, SWT.NONE );
        statsComposite.setLayout( new FillLayout(  ) );

        Button b = new Button( statsComposite, SWT.NONE );
        b.setText( "<gui protocol needs more stats>" );
        b.addSelectionListener( new SelectionAdapter(  ) {
                public void widgetSelected( SelectionEvent s ) {
                    mainSash.setWeights( new int[] { 0, 100 } );
                }
            } );

        statsViewForm.setTopLeft( statsCLabel );
        statsViewForm.setContent( statsComposite );
    }

    /**
    * Create a graph
    *
    * @param graphSash
    * @param titleText
    * @param graphName
    * @param color1
    * @param color2
    * @return GraphControl
    */
    public GraphControl createGraph( SashForm graphSash, String titleResString ) {
        final MenuManager popupMenu = new MenuManager(  );
        popupMenu.setRemoveAllWhenShown( true );

        String graphName = G2GuiResources.getString( titleResString );

        ViewForm graphViewForm = new ViewForm( graphSash, SWT.BORDER | ( PreferenceLoader.loadBoolean( "flatInterface" ) ? SWT.FLAT : SWT.NONE ) );
        CLabel cLabel = CCLabel.createCL( graphViewForm, titleResString, "StatisticsButtonSmallTitlebar" );

        GraphControl graphControl = new GraphControl( graphViewForm, graphName );

        popupMenu.addMenuListener( new GraphMenuListener( graphSash, graphViewForm, graphControl ) );

        graphViewForm.setTopLeft( cLabel );
        graphViewForm.setContent( graphControl );

        cLabel.addMouseListener( new MaximizeSashMouseAdapter( cLabel, popupMenu, graphSash, graphViewForm ) );

        return graphControl;
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update( Observable arg0, Object receivedInfo ) {
        ClientStats clientInfo = (ClientStats) receivedInfo;
        uploadsGraphControl.addPointToGraph( clientInfo.getTcpUpRate(  ) );
        downloadsGraphControl.addPointToGraph( clientInfo.getTcpDownRate(  ) );
        uploadsGraphControl.redraw(  );
        downloadsGraphControl.redraw(  );
    }

    public void setInActive( boolean removeObserver ) {
        // Do not remove Observer
        super.setInActive(  );
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.GuiTab#dispose()
     */
    public void dispose(  ) {
        super.dispose(  );
    }

	public void updateDisplay() {
		super.updateDisplay();
		uploadsGraphControl.updateDisplay();
		downloadsGraphControl.updateDisplay();
	}


    /**
     * GraphMenuListener
     */
    public class GraphMenuListener extends HeaderBarMenuListener {
        GraphControl graphControl;

        public GraphMenuListener( SashForm sashForm, Control control, GraphControl graphControl ) {
        	super(sashForm, control, null);
            this.graphControl = graphControl;
        }

        public void menuAboutToShow( IMenuManager menuManager ) {
            menuManager.add( new GraphHistoryAction( graphControl.getGraph(  ) ) );
            menuManager.add( new ClearGraphHistoryAction( graphControl.getGraph(  ) ) );
            super.menuAboutToShow( menuManager );
        }
    }

    /**
    * GraphHistoryAction
    */
    public class GraphHistoryAction extends Action {
        Graph graph;

        public GraphHistoryAction( Graph graph ) {
            super( "Hourly graph history" );
			setImageDescriptor( G2GuiResources.getImageDescriptor( "graph" ) );
            this.graph = graph;
        }

        public void run(  ) {
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

        public void run(  ) {
			MessageBox confirm =
				new MessageBox( 
					downloadsGraphControl.getShell(),
					SWT.YES | SWT.NO | SWT.ICON_QUESTION );

			confirm.setMessage( G2GuiResources.getString( "MISC_AYS" ) );

			if ( confirm.open() == SWT.YES ) {
				graph.clearHistory();
			}
        }
    }
}


/*
$Log: StatisticTab.java,v $
Revision 1.35  2003/10/22 01:36:59  zet
add column selector to server/search (might not be finished yet..)

Revision 1.34  2003/10/17 15:36:08  zet
graph colour prefs

Revision 1.33  2003/10/15 19:44:24  zet
icons

Revision 1.32  2003/10/12 15:56:52  zet
save sash orientation

Revision 1.31  2003/09/27 00:48:11  zet
+word

Revision 1.30  2003/09/27 00:26:41  zet
put menu back

Revision 1.29  2003/09/26 16:08:02  zet
dblclick header to maximize/restore

Revision 1.28  2003/09/20 22:08:50  zet
basic graph hourly history

Revision 1.27  2003/09/20 01:24:25  zet
*** empty log message ***

Revision 1.26  2003/09/18 09:44:57  lemmster
checkstyle

Revision 1.25  2003/09/16 01:58:25  zet
maximize on dblclick

Revision 1.24  2003/09/13 22:24:42  zet
int[] array

Revision 1.23  2003/08/29 22:13:44  zet
fix icon

Revision 1.22  2003/08/29 22:11:47  zet
add CCLabel helper class

Revision 1.21  2003/08/29 21:42:11  zet
add shadow

Revision 1.20  2003/08/25 22:20:25  zet
*** empty log message ***

Revision 1.19  2003/08/23 15:21:37  zet
remove @author

Revision 1.18  2003/08/23 01:12:43  zet
remove todos

Revision 1.17  2003/08/22 21:06:48  lemmster
replace $user$ with $Author: zet $

Revision 1.16  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.15  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging


*/
