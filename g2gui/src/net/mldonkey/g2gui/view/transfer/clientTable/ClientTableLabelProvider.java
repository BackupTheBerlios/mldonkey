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
package net.mldonkey.g2gui.view.transfer.clientTable;

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;


/**
 *
 * TableLabelProvider
 *
 *
 * @version $Id: ClientTableLabelProvider.java,v 1.5 2003/10/12 15:58:30 zet Exp $
 *
 */
public class ClientTableLabelProvider implements ITableLabelProvider, IColorProvider {
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
     */

    // Will transparent .pngs ever work?
    public Image getColumnImage( Object element, int columnIndex ) {
        ClientInfo clientInfo = (ClientInfo) element;

        switch ( columnIndex ) {
        case 0:
            return G2GuiResources.getClientImage( (EnumState) clientInfo.getState().getState() );

        case 2:
            return G2GuiResources.getNetworkImage( clientInfo.getClientnetworkid().getNetworkType() );

        default:
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     *
     * "network", "name", "desc", "address", "serverScore", "users", "files", "state", "favorite"
     */
    public String getColumnText( Object element, int columnIndex ) {
        ClientInfo clientInfo = (ClientInfo) element;

        switch ( columnIndex ) {
        case 0:
            return "" + clientInfo.getDetailedClientActivity( );

        case 1:
            return "" + clientInfo.getClientName();

        case 2:
            return "" + clientInfo.getClientnetworkid().getNetworkName();

        case 3:
            return "" + clientInfo.getClientConnection( );

        default:
            return "";
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
     */
    public void addListener( ILabelProviderListener listener ) {
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
     */
    public void dispose() {
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#
     * isLabelProperty(java.lang.Object, java.lang.String)
     */
    public boolean isLabelProperty( Object element, String property ) {
        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#
     * removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
     */
    public void removeListener( ILabelProviderListener listener ) {
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IColorProvider#getForeground(java.lang.Object)
     */
    public Color getForeground( Object arg0 ) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IColorProvider#getBackground(java.lang.Object)
     */
    public Color getBackground( Object element ) {
        return null;
    }
}


/*
$Log: ClientTableLabelProvider.java,v $
Revision 1.5  2003/10/12 15:58:30  zet
rewrite downloads table & more..

Revision 1.4  2003/09/24 03:07:43  zet
add # of active sources column

Revision 1.3  2003/09/23 01:28:23  zet
remove unneeded

Revision 1.2  2003/09/21 23:39:31  zet
displayTableColors preference

Revision 1.1  2003/09/20 14:39:21  zet
move transfer package

Revision 1.5  2003/09/18 14:11:01  zet
revert

Revision 1.3  2003/08/23 15:21:37  zet
remove @author

Revision 1.2  2003/08/22 21:17:25  lemmster
replace $user$ with $Author: zet $

Revision 1.1  2003/08/20 14:58:43  zet
sources clientinfo viewer



*/
