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
import net.mldonkey.g2gui.view.viewers.GTableLabelProvider;

import org.eclipse.jface.viewers.IColorProvider;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;


/**
 * ResultTableLabelProvider
 *
 *
 * @version $Id: ResultTableLabelProvider.java,v 1.19 2003/10/22 01:37:45 zet Exp $
 *
 */
public class ResultTableLabelProvider extends GTableLabelProvider implements IColorProvider {
    private Color alreadyDownloadedColor = new Color(null, 41, 174, 57);

    public ResultTableLabelProvider(ResultTableViewer rTableViewer) {
        super(rTableViewer);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#
     * getColumnImage(java.lang.Object, int)
     */
    public Image getColumnImage(Object arg0, int columnIndex) {
        ResultInfo resultInfo = (ResultInfo) arg0;

        switch (tableViewer.getColumnIDs()[ columnIndex ]) {
        case ResultTableViewer.NETWORK:

            if (!resultInfo.isDownloading()) {
                return G2GuiResources.getNetworkImage(resultInfo.getNetwork().getNetworkType());
            } else {
                return G2GuiResources.getImage("downloaded");
            }

        case ResultTableViewer.AVAILABILITY:
            return G2GuiResources.getRatingImage(resultInfo.getAvail());

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

        switch (tableViewer.getColumnIDs()[ columnIndex ]) {
        case ResultTableViewer.NETWORK:
            return ((NetworkInfo) resultInfo.getNetwork()).getNetworkName();

        case ResultTableViewer.NAME:
            return resultInfo.getName();

        case ResultTableViewer.SIZE:
            return resultInfo.getStringSize();

        case ResultTableViewer.FORMAT:
            return resultInfo.getFormat();

        case ResultTableViewer.MEDIA:
            return resultInfo.getType();

        case ResultTableViewer.AVAILABILITY:
            return G2GuiResources.getRatingString(resultInfo.getAvail());

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
            }
        }

        return null;
    }
}


/*
$Log: ResultTableLabelProvider.java,v $
Revision 1.19  2003/10/22 01:37:45  zet
add column selector to server/search (might not be finished yet..)

Revision 1.18  2003/09/17 20:07:44  lemmster
avoid NPE´s in search

Revision 1.17  2003/09/01 11:09:43  lemmster
show downloading files

Revision 1.16  2003/08/31 12:32:04  lemmster
major changes to search

Revision 1.15  2003/08/23 15:21:37  zet
remove @author

Revision 1.14  2003/08/22 21:10:57  lemmster
replace $user$ with $Author: zet $

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

Revision 1.6  2003/08/12 07:26:03  lemmstercvs01
checkstyle applied

Revision 1.5  2003/08/08 23:33:49  zet
dispose color

Revision 1.4  2003/07/31 04:11:00  zet
searchresult changes

Revision 1.3  2003/07/27 18:45:47  lemmstercvs01
lots of changes

Revision 1.2  2003/07/24 16:20:10  lemmstercvs01
lots of changes

Revision 1.1  2003/07/23 16:56:28  lemmstercvs01
initial commit

*/
