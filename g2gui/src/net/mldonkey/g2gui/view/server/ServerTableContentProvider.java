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

import net.mldonkey.g2gui.model.ServerInfoIntMap;
import net.mldonkey.g2gui.view.viewers.table.GTableContentProvider;

/**
 * ServerTableContentProvider
 *
 *
 * @version $Id: ServerTableContentProvider.java,v 1.4 2003/10/31 07:24:01 zet Exp $ 
 *
 */
public class ServerTableContentProvider extends GTableContentProvider {

    public ServerTableContentProvider( ServerTableViewer sTableViewer) {
        super(sTableViewer);
    }
    
    /* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements( Object inputElement ) {
		ServerInfoIntMap servers = ( ServerInfoIntMap ) inputElement;
		return servers.getServers();
	}
}

/*
$Log: ServerTableContentProvider.java,v $
Revision 1.4  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.3  2003/10/22 01:37:55  zet
add column selector to server/search (might not be finished yet..)

Revision 1.2  2003/08/23 15:21:37  zet
remove @author

Revision 1.1  2003/08/23 09:46:18  lemmster
superclass TableMenuListener added

Revision 1.2  2003/08/11 19:25:04  lemmstercvs01
bugfix at CleanTable

Revision 1.1  2003/08/05 13:50:10  lemmstercvs01
initial commit

*/