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
package net.mldonkey.g2gui.view.search;

import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.ResultInfo;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.table.GTableLabelProvider;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;


/**
 * ResultTableLabelProvider
 *
 *
 * @version $Id: ResultTableLabelProvider.java,v 1.26 2003/12/04 08:47:29 lemmy Exp $
 *
 */
public class ResultTableLabelProvider extends GTableLabelProvider implements IColorProvider {
    private Color alreadyDownloadedColor = new Color(null, 41, 174, 57);
    private Color containsFakeColor = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);

    public ResultTableLabelProvider(ResultTableView rTableViewer) {
        super(rTableViewer);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#
     * getColumnImage(java.lang.Object, int)
     */
    public Image getColumnImage(Object arg0, int columnIndex) {
        ResultInfo resultInfo = (ResultInfo) arg0;

        switch (cViewer.getColumnIDs()[ columnIndex ]) {
        case ResultTableView.NETWORK:

            if (!resultInfo.isDownloading()) {
                return G2GuiResources.getNetworkImage(resultInfo.getNetwork().getNetworkType());
            } else {
                return G2GuiResources.getImage("downloaded");
            }

        case ResultTableView.AVAILABILITY:
            return resultInfo.getRatingImage();

        default:
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#
     * getColumnText(java.lang.Object, int)
     */
    public String getColumnText(Object arg0, int columnIndex) {
        ResultInfo resultInfo = (ResultInfo) arg0;

        switch (cViewer.getColumnIDs()[ columnIndex ]) {
        case ResultTableView.NETWORK:
            return ((NetworkInfo) resultInfo.getNetwork()).getNetworkName();

        case ResultTableView.NAME:
            return resultInfo.getName();

        case ResultTableView.SIZE:
            return resultInfo.getStringSize();

        case ResultTableView.FORMAT:
            return resultInfo.getFormat();

        case ResultTableView.MEDIA:
            return resultInfo.getType();

        case ResultTableView.AVAILABILITY:
            return resultInfo.getRatingString();

        default:
            return "";
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
     */
    public void dispose() {
        alreadyDownloadedColor.dispose();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IColorProvider#getBackground(java.lang.Object)
     */
    public Color getBackground(Object element) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IColorProvider#getForeground(java.lang.Object)
     */
    public Color getForeground(Object element) {
        if (element instanceof ResultInfo) {
            ResultInfo resultInfo = (ResultInfo) element;
            if (!resultInfo.getHistory()) {
                return alreadyDownloadedColor;
            } else if ( resultInfo.containsFake() ) {
                return containsFakeColor;
            }
        }
        return null;
    }
}


/*
$Log: ResultTableLabelProvider.java,v $
Revision 1.26  2003/12/04 08:47:29  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.25  2003/11/10 08:35:13  lemmy
move getRating... into ResultInfo

Revision 1.24  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.23  2003/10/31 13:16:33  lemmy
Rename Viewer -> Page
Constructors changed

Revision 1.22  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.21  2003/10/22 23:43:36  zet
flag results that contain "fake" string

Revision 1.20  2003/10/22 14:38:32  dek
removed malformed UTF-8 char gcj complains about (was only in comment)

Revision 1.19  2003/10/22 01:37:45  zet
add column selector to server/search (might not be finished yet..)

Revision 1.18  2003/09/17 20:07:44  lemmy
avoid NPEs in search

Revision 1.17  2003/09/01 11:09:43  lemmy
show downloading files

Revision 1.16  2003/08/31 12:32:04  lemmy
major changes to search

Revision 1.15  2003/08/23 15:21:37  zet
remove @author

Revision 1.14  2003/08/22 21:10:57  lemmy
replace $user$ with $Author: lemmy $

Revision 1.13  2003/08/20 14:58:43  zet
sources clientinfo viewer

Revision 1.12  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.11  2003/08/17 23:13:42  zet
centralize resources, move images

Revision 1.10  2003/08/16 20:57:57  dek
searching works now without errors AGAIN ;-)

Revision 1.9  2003/08/14 14:48:11  vnc
added finer results availability rating

Revision 1.8  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.7  2003/08/14 12:44:44  dek
searching works now without errors

Revision 1.6  2003/08/12 07:26:03  lemmy
checkstyle applied

Revision 1.5  2003/08/08 23:33:49  zet
dispose color

Revision 1.4  2003/07/31 04:11:00  zet
searchresult changes

Revision 1.3  2003/07/27 18:45:47  lemmy
lots of changes

Revision 1.2  2003/07/24 16:20:10  lemmy
lots of changes

Revision 1.1  2003/07/23 16:56:28  lemmy
initial commit

*/
