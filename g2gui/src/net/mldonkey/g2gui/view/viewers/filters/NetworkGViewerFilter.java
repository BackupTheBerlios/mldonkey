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
package net.mldonkey.g2gui.view.viewers.filters;

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.ResultInfo;
import net.mldonkey.g2gui.model.ServerInfo;
import net.mldonkey.g2gui.view.viewers.GView;

import org.eclipse.jface.viewers.Viewer;


/**
 * NetworkGViewerFilter
 *
 * @version $Id: NetworkGViewerFilter.java,v 1.4 2003/12/04 08:47:30 lemmy Exp $
 *
 */
public class NetworkGViewerFilter extends GViewerFilter {
    /**
	 * @param gView
	 */
	public NetworkGViewerFilter(GView gView) {
		super(gView);
	}

	public boolean matches(NetworkInfo networkInfo) {
        return super.matches(networkInfo.getNetworkType());
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (element instanceof ServerInfo) {
            ServerInfo server = (ServerInfo) element;

            if (matches(server.getNetwork())) {
                return true;
            }

            return false;
        }
        else if (element instanceof ResultInfo) {
            ResultInfo result = (ResultInfo) element;

            if (matches(result.getNetwork())) {
                return true;
            }

            return false;
        } else if (element instanceof FileInfo) {
            FileInfo fileInfo = (FileInfo) element;

            if (matches(fileInfo.getNetwork())) {
                return true;
            }

            return false;
        } else if (element instanceof ClientInfo) {
            ClientInfo clientInfo = (ClientInfo) element;

            if (matches(clientInfo.getClientnetworkid())) {
                return true;
            }

            return false;
        }

        return true;
    }
}


/*
$Log: NetworkGViewerFilter.java,v $
Revision 1.4  2003/12/04 08:47:30  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.3  2003/11/06 13:52:32  lemmy
filters back working

Revision 1.2  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.1  2003/10/29 16:56:21  lemmy
added reasonable class hierarchy for panelisteners, viewers...

*/
