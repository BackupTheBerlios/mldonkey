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
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.view.helper.MaximizeSashMouseAdapter;
import net.mldonkey.g2gui.view.helper.WidgetFactory;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.statistic.GraphControl;
import net.mldonkey.g2gui.view.statistic.GraphPaneListener;
import net.mldonkey.g2gui.view.viewers.GPaneListener;
import net.mldonkey.g2gui.view.viewers.GView;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;


/**
 * Statistic Tab
 *
 * @version $Id: StatisticTab.java,v 1.43 2003/11/22 02:24:29 zet Exp $
 */
public class StatisticTab extends PaneGuiTab {
    private GraphControl uploadsGraphControl;
    private GraphControl downloadsGraphControl;
    private CLabel uploadsHeaderCLabel;
    private CLabel downloadsHeaderCLabel;
    private long lastTimeStamp;

    /**
     * default constructor
     * @param gui object representing the top level gui layer
     */
    public StatisticTab(MainTab gui) {
        super(gui);
        createButton("StatisticsButton");
        createContents(this.subContent);
        gui.getCore().getClientStats().addObserver(this);
    }

    /**
     * @param parent
     */
    protected void createContents(Composite parent) {
        SashForm mainSash = new SashForm(parent, SWT.VERTICAL);
        mainSash.setLayout(new FillLayout());

        // Top composite for stats (when available)
        createStatsComposite(mainSash);

        // Graph sash
        createGraphSash(mainSash);

        // Keep top sash hidden until it is useful 
        mainSash.setWeights(new int[] { 0, 10 });
    }

    /**
     * Create stats sash
     * @param mainSash
     */
    private void createStatsComposite(final SashForm mainSash) {
        ViewForm statsViewForm = WidgetFactory.createViewForm(mainSash);

        CLabel statsCLabel = WidgetFactory.createCLabel(statsViewForm, "TT_StatisticsButton",
                "StatisticsButtonSmall");
        Composite statsComposite = new Composite(statsViewForm, SWT.NONE);
        statsComposite.setLayout(new FillLayout());

        Button b = new Button(statsComposite, SWT.NONE);
        b.setText("<gui protocol needs more stats>");
        b.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent s) {
                    mainSash.setWeights(new int[] { 0, 100 });
                }
            });
        statsViewForm.setTopLeft(statsCLabel);
        statsViewForm.setContent(statsComposite);
    }

    /**
     * @param parent
     */
    private void createGraphSash(SashForm parent) {
        String graphSashPrefString = "graphSash";
        SashForm graphSash = WidgetFactory.createSashForm(parent, graphSashPrefString);

        downloadsGraphControl = createGraph(graphSash, "TT_Downloads");
        uploadsGraphControl = createGraph(graphSash, "TT_Uploads");

        WidgetFactory.loadSashForm(graphSash, graphSashPrefString);
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
    public GraphControl createGraph(SashForm graphSash, String titleResString) {
        final MenuManager popupMenu = new MenuManager();
        popupMenu.setRemoveAllWhenShown(true);

        String graphName = G2GuiResources.getString(titleResString);
        ViewForm graphViewForm = WidgetFactory.createViewForm(graphSash);
        CLabel cLabel = WidgetFactory.createCLabel(graphViewForm, titleResString, "StatisticsButtonSmall");

        String showResString = "TT_Uploads";

        if (titleResString.equals(showResString)) {
            uploadsHeaderCLabel = cLabel;
            showResString = "TT_Downloads";
        } else {
            downloadsHeaderCLabel = cLabel;
        }

        GraphControl graphControl = new GraphControl(graphViewForm, graphName);
        GPaneListener aListener = new GraphPaneListener(graphSash, graphViewForm, graphControl,
                showResString);

        popupMenu.addMenuListener(aListener);
        graphViewForm.setTopLeft(cLabel);
        graphViewForm.setContent(graphControl);
        cLabel.addMouseListener(new MaximizeSashMouseAdapter(cLabel, popupMenu, graphSash,
                graphViewForm));

        return graphControl;
    }
    

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable arg0, Object receivedInfo) {
        ClientStats clientStats = (ClientStats) receivedInfo;
        uploadsGraphControl.addPointToGraph(clientStats.getTcpUpRate());
        downloadsGraphControl.addPointToGraph(clientStats.getTcpDownRate());
        if ( this.isActive() ) {
        	uploadsGraphControl.redraw();
        	downloadsGraphControl.redraw();
        }
        updateHeaderLabels(clientStats);
    }

    /**
     * Force a min 5 second interval
     * @param clientStats
     */
    public void updateHeaderLabels(final ClientStats clientStats) {
        if (System.currentTimeMillis() > (lastTimeStamp + 5000)) {
            if ((uploadsHeaderCLabel == null) || uploadsHeaderCLabel.isDisposed()) {
                return;
            }

            uploadsHeaderCLabel.getDisplay().asyncExec(new Runnable() {
                    public void run() {
                        if ((uploadsHeaderCLabel == null) || uploadsHeaderCLabel.isDisposed()) {
                            return;
                        }

                        uploadsHeaderCLabel.setText(G2GuiResources.getString("TT_Uploads") + ": " +
                            FileInfo.calcStringSize(clientStats.getTotalUp()) + " " +
                            G2GuiResources.getString("TT_Total"));

                        downloadsHeaderCLabel.setText(G2GuiResources.getString("TT_Downloads") +
                            ": " + FileInfo.calcStringSize(clientStats.getTotalDown()) + " " +
                            G2GuiResources.getString("TT_Total"));
                    }
                });
            lastTimeStamp = System.currentTimeMillis();
        }
    }

    public void setInActive(boolean removeObserver) {
        // Do not remove Observer
        super.setInActive();
    }

    public void updateDisplay() {
        super.updateDisplay();
        uploadsGraphControl.updateDisplay();
        downloadsGraphControl.updateDisplay();
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.PaneGuiTab#getGPage()
     */
    public GView getGView() {
        // we dont have a GPage so we return null. its checked in GPage
        return null;
    }
}


/*
$Log: StatisticTab.java,v $
Revision 1.43  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

Revision 1.42  2003/11/21 19:17:04  vnc
big cpu leak: draw statistic graphs only when tab is active

Revision 1.41  2003/11/15 21:15:29  zet
Label restore action

Revision 1.40  2003/11/09 02:18:37  zet
put some info in the headers

Revision 1.39  2003/11/04 20:38:27  zet
update for transparent gifs

Revision 1.38  2003/10/31 16:02:17  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.37  2003/10/31 13:20:31  lemmster
added PaneGuiTab and TableGuiTab
added "dropdown" button to all PaneGuiTabs (not finished yet, continue on monday)

Revision 1.36  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

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
