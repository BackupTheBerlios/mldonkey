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
package net.mldonkey.g2gui.view;

import java.util.Observable;

import net.mldonkey.g2gui.model.ServerInfoIntMap;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.server.ServerViewFrame;
import net.mldonkey.g2gui.view.viewers.GView;
import net.mldonkey.g2gui.view.viewers.actions.FilterAction;
import net.mldonkey.g2gui.view.viewers.filters.StateGViewerFilter;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;


/**
 * ServerTab
 *
 *
 * @version $Id: ServerTab.java,v 1.57 2004/03/25 18:07:25 dek Exp $
 *
 */
public class ServerTab extends GuiTab implements Runnable {
    private String statusText = G2Gui.emptyString;
    private ServerInfoIntMap servers;
    private GView gView;
	// TODO: Move view's content to its content provider, reg d
    
    /**
     * @param mainWindow
     */
    public ServerTab(MainWindow mainWindow) {
        super(mainWindow, "ServersButton");
        updateDisplay();
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.GuiTab#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected void createContents(Composite parent) {
        ServerViewFrame sVF = new ServerViewFrame(parent, "TT_ServersButton", "ServersButtonSmall", this);
		addViewFrame(sVF);
        gView = sVF.getGView();
        
        
        /* fill the table with content */
        servers = getCore().getServerInfoIntMap();
        gView.getViewer().setInput(servers);
        servers.clearAdded();

        int itemCount = gView.getTable().getItemCount();
        this.statusText = G2GuiResources.getString("SVT_SERVERS") + itemCount;
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable o, Object arg) {
        if (gView.getTable().isDisposed())
            return;

        getContent().getDisplay().asyncExec(this);
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        /* still running? */
        if (gView.getTable().isDisposed())
            return;

        synchronized (this.servers.getRemoved()) {
            ((TableViewer) gView.getViewer()).remove(this.servers.getRemoved().toArray());
            this.servers.clearRemoved();
        }

        synchronized (this.servers.getAdded()) {
            ((TableViewer) gView.getViewer()).add(this.servers.getAdded().toArray());
            this.servers.clearAdded();
        }

        synchronized (this.servers.getModified()) {
            ((TableViewer) gView.getViewer()).update(this.servers.getModified().toArray(), null);
            this.servers.clearModified();
        }

        int itemCount = gView.getTable().getItemCount();
        this.setStatusLine();

        /* refresh the table if "show connected servers only" is true and the filter is activated */
        if (FilterAction.isFiltered(this.gView, EnumState.CONNECTED) &&
                (this.servers.getConnected() != itemCount)) {
            if (StateGViewerFilter.matches(gView, EnumState.CONNECTED))
                gView.refresh();
        }
    }

    /**
     * unregister ourself at the observable
     */
    public void setInActive() {
        getCore().getServerInfoIntMap().deleteObserver(this);
        super.setInActive();
    }

    /**
     * register ourself at the observable
     */
    public void setActive() {
        /* if we become active, refresh the table */
        this.servers = getCore().getServerInfoIntMap();
        gView.getShell().getDisplay().asyncExec(this);
        this.setStatusLine();

        getCore().getServerInfoIntMap().addObserver(this);
        super.setActive();
    }

    /**
     * Sets the statusline to the current value
     */
    private void setStatusLine() {
        if (!this.isActive())
            return;

        this.statusText = G2GuiResources.getString("SVT_SERVERS") + " " + 
        	gView.getTable().getItemCount() + " / " + servers.size() + ", " + 
        	G2GuiResources.getString("ENS_CONNECTED") + ": " + servers.getConnected();
        this.getMainWindow().getStatusline().update(this.statusText);
        this.getMainWindow().getStatusline().updateToolTip("");
    }

    /**
     * @return The text this tab wants to display in the statusline
     */
    public String getStatusText() {
        return this.statusText;
    }

    /**
     * Updates this tab on preference close
     */
    public void updateDisplay() {
        gView.updateDisplay();
        super.updateDisplay();
    }
    
    public GView getGView() {
        return gView;
    }
}


/*
$Log: ServerTab.java,v $
Revision 1.57  2004/03/25 18:07:25  dek
profiling

Revision 1.56  2003/12/20 18:32:10  psy
increased verbosiveness of servertab statusline a little

Revision 1.55  2003/12/04 08:47:27  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.54  2003/11/29 19:28:38  zet
minor string move

Revision 1.53  2003/11/29 19:10:24  zet
small update.. continue later.
- mainwindow > tabs > viewframes(can contain gView)

Revision 1.52  2003/11/29 17:21:22  zet
minor cleanup

Revision 1.51  2003/11/29 17:01:00  zet
update for mainWindow

Revision 1.50  2003/11/29 01:51:53  zet
a few more viewframe changes.. will continue later.

Revision 1.49  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

Revision 1.48  2003/11/09 23:09:57  lemmy
remove "Show connected Servers only"
added filter saving in searchtab

Revision 1.47  2003/11/04 20:38:27  zet
update for transparent gifs

Revision 1.46  2003/10/31 16:02:17  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.45  2003/10/31 13:20:31  lemmy
added PaneGuiTab and TableGuiTab
added "dropdown" button to all PaneGuiTabs (not finished yet, continue on monday)

Revision 1.44  2003/10/31 10:42:47  lemmy
Renamed GViewer, GTableViewer and GTableTreeViewer to gView... to avoid mix-ups with StructuredViewer...
Removed IGViewer because our abstract class gView do the job
Use supertype/interface where possible to keep the design flexible!

Revision 1.43  2003/10/29 16:56:21  lemmy
added reasonable class hierarchy for panelisteners, viewers...

Revision 1.42  2003/10/28 11:07:32  lemmy
move NetworkInfo.Enum -> enum.EnumNetwork
add MaskMatcher for "Enum[]"

Revision 1.41  2003/10/22 01:36:59  zet
add column selector to server/search (might not be finished yet..)

Revision 1.40  2003/10/21 17:06:27  lemmy
fix manage servers from statusline

Revision 1.39  2003/10/21 17:00:45  lemmy
class hierarchy for tableviewer

Revision 1.38  2003/10/12 14:56:19  zet
*** empty log message ***

Revision 1.37  2003/10/11 20:19:18  zet
Don't TableColumn.setWidth(0) on gtk

Revision 1.36  2003/09/27 12:33:32  dek
server-Table has now same show-Gridlines-behaviour as download-Table

Revision 1.35  2003/09/23 11:46:25  lemmy
displayTableColors for servertab

Revision 1.34  2003/09/22 20:02:34  lemmy
right column align for integers [bug #934]

Revision 1.33  2003/09/20 14:39:48  zet
move transfer package

Revision 1.32  2003/09/18 09:44:57  lemmy
checkstyle

Revision 1.31  2003/09/16 02:12:30  zet
match the menus with transfertab

Revision 1.30  2003/09/14 13:44:22  lemmy
set statusline only on active

Revision 1.29  2003/09/14 13:24:30  lemmy
add header button to servertab

Revision 1.28  2003/09/14 10:01:24  lemmy
save column width [bug #864]

Revision 1.27  2003/09/14 09:42:51  lemmy
save column width

Revision 1.26  2003/09/14 09:40:31  lemmy
save column width

Revision 1.25  2003/09/11 13:39:19  lemmy
check for disposed

Revision 1.24  2003/08/29 22:11:47  zet
add CCLabel helper class

Revision 1.23  2003/08/29 19:30:50  zet
font colour

Revision 1.22  2003/08/29 17:33:20  zet
remove headerbar

Revision 1.21  2003/08/29 16:06:54  zet
optional shadow

Revision 1.20  2003/08/28 22:44:30  zet
GridLayout helper class

Revision 1.19  2003/08/25 20:38:26  vnc
GTK layout cleanups, slight typo refactoring

Revision 1.18  2003/08/23 15:21:37  zet
remove @author

Revision 1.17  2003/08/23 15:01:32  lemmy
update the header

Revision 1.16  2003/08/23 14:58:38  lemmy
cleanup of MainTab, transferTree.* broken

Revision 1.15  2003/08/23 10:33:36  lemmy
updateDisplay() only on displayAllServers change

Revision 1.14  2003/08/23 09:46:18  lemmy
superclass TableMenuListener added

Revision 1.13  2003/08/23 08:30:07  lemmy
added defaultItem to the table

Revision 1.12  2003/08/20 22:18:56  zet
Viewer updates

Revision 1.11  2003/08/20 21:34:22  lemmy
additive filters

Revision 1.10  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.9  2003/08/11 19:25:04  lemmy
bugfix at CleanTable

Revision 1.8  2003/08/10 12:59:01  lemmy
"manage servers" in NetworkItem implemented

Revision 1.7  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.6  2003/08/07 13:25:37  lemmy
ResourceBundle added

Revision 1.5  2003/08/07 12:35:31  lemmy
cleanup, more efficient

Revision 1.4  2003/08/06 20:56:49  lemmy
cleanup, more efficient

Revision 1.3  2003/08/06 17:38:38  lemmy
some actions still missing. but it should work for the moment

Revision 1.2  2003/08/05 15:35:24  lemmy
minor: column width changed

Revision 1.1  2003/08/05 13:50:10  lemmy
initial commit

*/
