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
 * (at your option) any later version.
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
package net.mldonkey.g2gui.view.transferTree;

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;

/**
 *
 * TreeClientInfo
 *
 *
 * @version $Id: TreeClientInfo.java,v 1.7 2003/09/18 12:12:23 lemmster Exp $
 *
 */
public class TreeClientInfo {
    private FileInfo fileInfo;
    private ClientInfo clientInfo;

	/**
	 * DOCUMENT ME!
	 *  
	 * @param fileInfo DOCUMENT ME!
	 * @param clientInfo DOCUMENT ME!
	 */
    public TreeClientInfo( FileInfo fileInfo, ClientInfo clientInfo ) {
        this.fileInfo = fileInfo;
        this.clientInfo = clientInfo;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public FileInfo getFileInfo(  ) {
        return fileInfo;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ClientInfo getClientInfo(  ) {
        return clientInfo;
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals( Object o ) {
        if ( o instanceof TreeClientInfo ) {
            ClientInfo c = ( ClientInfo ) ( ( TreeClientInfo ) o ).getClientInfo(  );
            FileInfo f = ( FileInfo ) ( ( TreeClientInfo ) o ).getFileInfo(  );
            if ( c.equals( this.getClientInfo(  ) ) && f.equals( this.getFileInfo(  ) ) )
                return true;
            else
                return false;
        }
        else
            return false;
    }
}

/*
$Log: TreeClientInfo.java,v $
Revision 1.7  2003/09/18 12:12:23  lemmster
checkstyle

Revision 1.6  2003/09/08 19:49:16  lemmster
just repaired the log

Revision 1.5  2003/08/23 15:21:37  zet
remove @author

Revision 1.4  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.3  2003/08/22 21:22:58  lemmster

*/
