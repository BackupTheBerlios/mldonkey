/*
 * Copyright 2003
 * g2gui Team
 *
 *
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package net.mldonkey.g2gui.view.viewers.actions;

import net.mldonkey.g2gui.view.helper.WebLauncher;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.action.Action;


/**
 * WebServicesAction
 *
 * @version $Id: WebServicesAction.java,v 1.7 2003/11/28 00:57:03 zet Exp $
 *
 */
public class WebServicesAction extends Action {
    public static final int JIGLE = 0;
    public static final int BITZI = 1;
    public static final int FILEDONKEY = 2;
    public static final int SHAREREACTOR = 3;
    public static final int DONKEY_FAKES = 4;
    private String string;
    private long fileSize;
    private int type;

    public WebServicesAction(int type, long fileSize, String string) {
        super();
        this.type = type;
        this.fileSize = fileSize;
        this.string = string;

        switch (type) {
        case JIGLE:
            setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_WEB_JIGLE_LOOKUP"));
            setImageDescriptor(G2GuiResources.getImageDescriptor("Jigle"));

            break;

        case BITZI:
            setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_WEB_BITZI_LOOKUP"));
            setImageDescriptor(G2GuiResources.getImageDescriptor("Bitzi"));

            break;

        case FILEDONKEY:
            setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_WEB_FILEDONKEY_LOOKUP"));
            setImageDescriptor(G2GuiResources.getImageDescriptor("edonkey"));

            break;

        case SHAREREACTOR:
            setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_WEB_SR_FAKECHECK"));
            setImageDescriptor(G2GuiResources.getImageDescriptor("ShareReactor"));

            break;

        case DONKEY_FAKES:
            setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_WEB_DONKEY_FAKES"));
            setImageDescriptor(G2GuiResources.getImageDescriptor("edonkey"));

            break;
        }
    }

    public void run() {
        switch (type) {
        case JIGLE:
            WebLauncher.openLink("http://www.jigle.com/search?p=ed2k:" + fileSize + ":" + string);

            break;

        case BITZI:
            WebLauncher.openLink("http://bitzi.com/lookup/" + string);

            break;

        case FILEDONKEY:
            WebLauncher.openLink("http://www.filedonkey.com/file.html?md4=" + string);

            break;

        case SHAREREACTOR:
            WebLauncher.openLink("http://www.sharereactor.com/fakesearch.php?search=" + string);

            break;

        case DONKEY_FAKES:
            WebLauncher.openLink("http://donkeyfakes.gambri.net/fakecheck/update/fakecheck.php?ed2k=" + string);

            break;
        }
    }
}


/*
$Log: WebServicesAction.java,v $
Revision 1.7  2003/11/28 00:57:03  zet
use Weblauncher

Revision 1.6  2003/11/27 16:07:40  zet
mozfirebird

Revision 1.5  2003/11/27 15:39:02  zet
bla

Revision 1.4  2003/11/27 15:33:10  zet
extend for multi platform support

Revision 1.3  2003/11/25 01:13:13  zet
include filesize for webservice>jigle lookup

Revision 1.2  2003/10/24 21:26:20  zet
add donkey fakes web service

Revision 1.1  2003/10/22 16:28:52  zet
common actions

*/
