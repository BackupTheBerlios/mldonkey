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
package net.mldonkey.g2gui.view.server;

import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.ServerInfo;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.table.GTableLabelProvider;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;


/**
 * ServerTableLabelProvider
 *
 *
 * @version $Id: ServerTableLabelProvider.java,v 1.13 2004/03/25 19:25:23 dek Exp $
 *
 */
public class ServerTableLabelProvider extends GTableLabelProvider implements IColorProvider {
    private boolean colors = PreferenceLoader.loadBoolean("displayTableColors");
    private Color connectColor = new Color(null, 41, 174, 57);
    private Color connectingColor = new Color(null, 255, 165, 0);
    private Color disconnectColor = new Color(null, 192, 192, 192);

    public ServerTableLabelProvider(ServerTableView sTableViewer) {
        super(sTableViewer);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
     */
    public Image getColumnImage(Object element, int columnIndex) {
        switch (cViewer.getColumnIDs()[ columnIndex ]) {
        case ServerTableView.NETWORK:

            ServerInfo server = (ServerInfo) element;

            return G2GuiResources.getNetworkImage(server.getNetwork().getNetworkType());

        default:
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     *
     * "network", "name", "desc", "address", "serverScore", "users", "files", "state", "favorite"
     */
    public String getColumnText(Object element, int columnIndex) {
        ServerInfo server = (ServerInfo) element;

        switch (cViewer.getColumnIDs()[ columnIndex ]) {
        case ServerTableView.NETWORK:
            return " " + ((NetworkInfo) server.getNetwork()).getNetworkName();

        case ServerTableView.NAME:
            return server.getNameOfServer();

        case ServerTableView.DESCRIPTION:
            return server.getDescOfServer();

        case ServerTableView.ADDRESS:
            return server.getServerAddress().toString();
        case ServerTableView.PORT:
            return "" + server.getServerPort();

        case ServerTableView.SCORE:
            return "" + server.getServerScore();

        case ServerTableView.USERS:
            return "" + server.getNumOfUsers();

        case ServerTableView.FILES:
            return "" + server.getNumOfFilesShared();

        case ServerTableView.STATE:
            return server.getState().toString();

        case ServerTableView.FAVORITE:
            return G2GuiResources.getString(server.isFavorite() ? "TLP_TRUE" : "TLP_FALSE");

        default:
            return "";
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
     */
    public void dispose() {
        connectColor.dispose();
        connectingColor.dispose();
        disconnectColor.dispose();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IColorProvider#getForeground(java.lang.Object)
     */
    public Color getForeground(Object arg0) {
        if (!colors) {
            return null;
        }

        ServerInfo server = (ServerInfo) arg0;

        if (server.isConnected()) {
            return connectColor;
        } else if (server.getState() == EnumState.CONNECTING) {
            return connectingColor;
        } else if (server.getState() == EnumState.NOT_CONNECTED) {
            return disconnectColor;
        }

        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IColorProvider#getBackground(java.lang.Object)
     */
    public Color getBackground(Object element) {
        return null;
    }

    /**
     * @param colors Should a color for each tableitem be used
     */
    public void setColors(boolean colors) {
        this.colors = colors;
    }
}


/*
$Log: ServerTableLabelProvider.java,v $
Revision 1.13  2004/03/25 19:25:23  dek
yet more profiling

Revision 1.12  2003/12/04 08:47:30  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.11  2003/11/29 13:01:11  lemmy
Addr.getString() renamed to the more natural word name Addr.toString()

Revision 1.10  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.9  2003/10/31 13:16:32  lemmy
Rename Viewer -> Page
Constructors changed

Revision 1.8  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.7  2003/10/22 01:37:55  zet
add column selector to server/search (might not be finished yet..)

Revision 1.6  2003/10/12 00:21:34  zet
dispose colors

Revision 1.5  2003/09/23 11:46:25  lemmy
displayTableColors for servertab

Revision 1.4  2003/09/18 11:26:07  lemmy
checkstyle

Revision 1.3  2003/09/14 10:19:48  lemmy
different colors for the server state (forum request)

Revision 1.2  2003/08/23 15:21:37  zet
remove @author

Revision 1.1  2003/08/23 09:46:18  lemmy
superclass TableMenuListener added

Revision 1.9  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.8  2003/08/17 23:13:42  zet
centralize resources, move images

Revision 1.7  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.6  2003/08/11 19:25:04  lemmy
bugfix at CleanTable

Revision 1.5  2003/08/07 13:25:37  lemmy
ResourceBundle added

Revision 1.4  2003/08/07 12:35:31  lemmy
cleanup, more efficient

Revision 1.3  2003/08/06 18:53:45  lemmy
missed enum added

Revision 1.2  2003/08/06 17:38:38  lemmy
some actions still missing. but it should work for the moment

Revision 1.1  2003/08/05 13:50:10  lemmy
initial commit

*/
