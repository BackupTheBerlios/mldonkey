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
package net.mldonkey.g2gui.view.transfer;

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;


/**
 *
 * TreeClientInfo
 *
 *
 * @version $Id: TreeClientInfo.java,v 1.4 2003/11/11 02:31:42 zet Exp $
 *
 */
public class TreeClientInfo {
    private FileInfo fileInfo;
    private ClientInfo clientInfo;
    private boolean deleteFlag = false;

    public TreeClientInfo(FileInfo fileInfo, ClientInfo clientInfo) {
        this.fileInfo = fileInfo;
        this.clientInfo = clientInfo;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public boolean equals(Object o) {
        if (o instanceof TreeClientInfo) {
            ClientInfo c = (ClientInfo) ((TreeClientInfo) o).getClientInfo();
            FileInfo f = (FileInfo) ((TreeClientInfo) o).getFileInfo();

            if (c.equals(this.getClientInfo()) && f.equals(this.getFileInfo())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void setDelete() {
        deleteFlag = true;
    }

    public boolean getDelete() {
        return deleteFlag;
    }
}


/*
$Log: TreeClientInfo.java,v $
Revision 1.4  2003/11/11 02:31:42  zet
cleanup

Revision 1.3  2003/10/12 15:58:30  zet
rewrite downloads table & more..

Revision 1.2  2003/09/21 23:39:31  zet
displayTableColors preference

Revision 1.1  2003/09/20 14:39:21  zet
move transfer package

Revision 1.8  2003/09/18 14:11:01  zet
revert

Revision 1.6  2003/09/08 19:49:16  lemmster
just repaired the log

Revision 1.5  2003/08/23 15:21:37  zet
remove @author

Revision 1.4  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.3  2003/08/22 21:22:58  lemmster

*/
