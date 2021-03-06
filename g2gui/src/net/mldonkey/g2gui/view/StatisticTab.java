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
 *
 */
package net.mldonkey.g2gui.view;

import java.util.Observable;

import net.mldonkey.g2gui.helper.RegExp;
import net.mldonkey.g2gui.model.ClientStats;
import net.mldonkey.g2gui.view.helper.WidgetFactory;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.statistic.GraphControl;
import net.mldonkey.g2gui.view.statistic.GraphViewFrame;
import net.mldonkey.g2gui.view.viewers.GView;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;


/**
 * Statistic Tab
 *
 * @version $Id: StatisticTab.java,v 1.51 2004/03/09 11:17:11 dek Exp $
 */
public class StatisticTab extends GuiTab {
    private GraphControl uploadsGraphControl;
    private GraphControl downloadsGraphControl;
    private CLabel uploadsHeaderCLabel;
    private CLabel downloadsHeaderCLabel;
    private long lastTimeStamp;
    
    /**
     * @param mainWindow
     */
    public StatisticTab(MainWindow mainWindow) {
        super(mainWindow, "StatisticsButton");
        getCore().getClientStats().addObserver(this);
    }

    /**
     * @param parent
     */
    protected void createContents(Composite parent) {
        createGraphSash(parent);
    }

    /**
     * @param parent
     */
    private void createGraphSash(Composite parent) {
        String graphSashPrefString = "graphSash";
        SashForm graphSash = WidgetFactory.createSashForm(parent, graphSashPrefString);

        createDownloadsGraph(graphSash);
        createUploadsGraph(graphSash);

        WidgetFactory.loadSashForm(graphSash, graphSashPrefString);
    }

    private void createDownloadsGraph(SashForm graphSash) {
        GraphViewFrame graphViewFrame = createGraph(graphSash, "TT_Downloads");
        downloadsGraphControl = graphViewFrame.getGraphControl();
        downloadsHeaderCLabel = graphViewFrame.getCLabel();
    }

    private void createUploadsGraph(SashForm graphSash) {
        GraphViewFrame graphViewFrame = createGraph(graphSash, "TT_Uploads");
        uploadsGraphControl = graphViewFrame.getGraphControl();
        uploadsHeaderCLabel = graphViewFrame.getCLabel();
    }

    private GraphViewFrame createGraph(SashForm graphSash, String titleResString) {
        return new GraphViewFrame(graphSash, titleResString, "StatisticsButtonSmall", this);
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable arg0, Object receivedInfo) {
        ClientStats clientStats = (ClientStats) receivedInfo;
        uploadsGraphControl.addPointToGraph(clientStats.getTcpUpRate());
        downloadsGraphControl.addPointToGraph(clientStats.getTcpDownRate());

        if (this.isActive()) {
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
            if ((uploadsHeaderCLabel == null) || uploadsHeaderCLabel.isDisposed())
                return;

            uploadsHeaderCLabel.getDisplay().asyncExec(new Runnable() {
                    public void run() {
                        if ((uploadsHeaderCLabel == null) || uploadsHeaderCLabel.isDisposed())
                            return;

                        uploadsHeaderCLabel.setText(G2GuiResources.getString("TT_Uploads") + ": " +
                            RegExp.calcStringSize(clientStats.getTotalUp()) + " " +
                            G2GuiResources.getString("TT_Total"));

                        downloadsHeaderCLabel.setText(G2GuiResources.getString("TT_Downloads") +
                            ": " + RegExp.calcStringSize(clientStats.getTotalDown()) + " " +
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
Revision 1.51  2004/03/09 11:17:11  dek
*** empty log message ***

Revision 1.50  2003/12/04 08:47:27  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.49  2003/11/29 19:28:38  zet
minor string move

Revision 1.48  2003/11/29 19:10:24  zet
small update.. continue later.
- mainwindow > tabs > viewframes(can contain gView)

Revision 1.47  2003/11/29 17:21:22  zet
minor cleanup

Revision 1.46  2003/11/29 17:01:00  zet
update for mainWindow

Revision 1.45  2003/11/29 01:51:53  zet
a few more viewframe changes.. will continue later.

Revision 1.44  2003/11/23 17:58:03  lemmy
removed dead/unused code

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

Revision 1.37  2003/10/31 13:20:31  lemmy
added PaneGuiTab and TableGuiTab
added "dropdown" button to all PaneGuiTabs (not finished yet, continue on monday)

Revision 1.36  2003/10/29 16:56:21  lemmy
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

Revision 1.26  2003/09/18 09:44:57  lemmy
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

Revision 1.17  2003/08/22 21:06:48  lemmy
replace $user$ with $Author: dek $

Revision 1.16  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.15  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging


*/
