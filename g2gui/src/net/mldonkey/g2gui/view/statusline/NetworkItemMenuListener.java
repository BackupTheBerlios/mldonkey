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
package net.mldonkey.g2gui.view.statusline;

import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.view.GuiTab;
import net.mldonkey.g2gui.view.ServerTab;
import net.mldonkey.g2gui.view.StatusLine;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.GView;
import net.mldonkey.g2gui.view.viewers.actions.FilterAction;
import net.mldonkey.g2gui.view.viewers.actions.NetworkFilterAction;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;

/**
 * NetworkItemMenuListener
 *
 *
 * @version $Id: NetworkItemMenuListener.java,v 1.13 2003/10/31 16:02:57 zet Exp $
 *
 */
public class NetworkItemMenuListener implements IMenuListener {
    private static final boolean advanced = PreferenceLoader.loadBoolean( "advancedMode" );
	private static final boolean nodes = PreferenceLoader.loadBoolean( "displayNodes" );
    /**
     * The <code>StatusLine</code>
     */
    private StatusLine statusline;

    /**
     * The <code>NetworkInfo</code> to this <code>IMenuListener</code>
     */
    private NetworkInfo network;

    /**
     * The <code>GuiTab</code>
     */
    private GuiTab serverTab;

    /**
     * Creates a new NetworkItemMenuListener
     * @param network The <code>NetworkInfo</code> this obj belong to
     * @param serverTab The <code>GuiTab</code>
     * @param statusline The <code>StatusLine<code> we draw at
     */
    public NetworkItemMenuListener( NetworkInfo network, GuiTab serverTab, StatusLine statusline ) {
        super();
        this.network = network;
        this.serverTab = serverTab;
        this.statusline = statusline;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IMenuListener#
     * menuAboutToShow(org.eclipse.jface.action.IMenuManager)
     */
    public void menuAboutToShow( IMenuManager manager ) {
        if ( network.isEnabled() ) {
            if ( ( network.hasServers() && advanced )
            || network.hasSupernodes() && advanced && nodes )
                manager.add( new ManageAction() );
            if ( network.hasServers() || network.hasSupernodes() )
                manager.add( new ConnectMoreAction() );
            manager.add( new Separator() );
        }
        if ( !network.isVirtual() ) {
            if ( network.isEnabled() )
                manager.add( new DisableAction() );
            else
                manager.add( new EnableAction() );
        }
    }

    private class EnableAction extends Action {
        public EnableAction() {
            super();
            setText( G2GuiResources.getString( "NIML_ENABLE" ) );
        }

        public void run() {
            network.setEnabled();
        }
    }

    private class DisableAction extends Action {
        public DisableAction() {
            super();
            setText( G2GuiResources.getString( "NIML_DISABLE" ) );
        }

        public void run() {
            network.setEnabled();
        }
    }

    private class ManageAction extends Action {
        public ManageAction() {
            super();
            if ( network.hasServers() )
	            setText( G2GuiResources.getString( "NIML_MANAGE" ) );
	        else
	        	setText( G2GuiResources.getString( "NIML_NMANAGE" ) );    
        }

        public void run() {
            statusline.getMainTab().setActive( serverTab );
			GView gView = ( ( ServerTab ) serverTab ).getView();
			FilterAction action = new NetworkFilterAction( gView, network );
			( ( NetworkFilterAction ) action ).removeAllNetworkFilter();
			action.setChecked( true );
			action.run();
        }
    }

    private class ConnectMoreAction extends Action {
        public ConnectMoreAction() {
            super();
            setText( G2GuiResources.getString( "NIML_CONNECT" ) );
        }

        public void run() {
            network.getCore().getServerInfoIntMap().connectMore( network );
        }
    }
}

/*
$Log: NetworkItemMenuListener.java,v $
Revision 1.13  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.12  2003/10/31 10:42:47  lemmster
Renamed GViewer, GTableViewer and GTableTreeViewer to GPage... to avoid mix-ups with StructuredViewer...
Removed IGViewer because our abstract class GPage do the job
Use supertype/interface where possible to keep the design flexible!

Revision 1.11  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.10  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

Revision 1.9  2003/10/21 17:06:27  lemmster
fix manage servers from statusline

Revision 1.8  2003/10/17 03:36:43  zet
use toolbar

Revision 1.7  2003/09/23 05:24:04  lemmster
display "manage nodes" for FT/Gnut/Gnut2

Revision 1.6  2003/09/18 11:37:24  lemmster
checkstyle

Revision 1.5  2003/09/15 22:53:35  lemmster
bugfix [bug #912]

Revision 1.4  2003/09/12 16:28:21  lemmster
ResourceBundle added

Revision 1.3  2003/08/23 15:21:37  zet
remove @author

Revision 1.2  2003/08/22 19:00:25  lemmster
support for connectMore with network id

Revision 1.1  2003/08/21 13:13:10  lemmster
cleanup in networkitem

*/
