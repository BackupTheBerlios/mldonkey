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
package net.mldonkey.g2gui.view.transferTree.clientTable;

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.enum.EnumClientMode;
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
 * @version $Id: TableLabelProvider.java,v 1.4 2003/09/18 11:42:03 lemmster Exp $
 *
 */
public class TableLabelProvider implements ITableLabelProvider, IColorProvider {
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
     */

	/**
	 * DOCUMENT ME!
	 * 
	 * @param element DOCUMENT ME!
	 * @param columnIndex DOCUMENT ME!
	 * @return DOCUMENT ME! 
	 */
    public Image getColumnImage( Object element, int columnIndex ) {
        ClientInfo clientInfo = ( ClientInfo ) element;
        switch ( columnIndex ) {
        case 0:
            return G2GuiResources.getClientImage( ( EnumState ) clientInfo.getState().getState() );
        case 2:
            return G2GuiResources.getNetworkImage( clientInfo.getClientnetworkid().getNetworkType() );
        default:
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     */
    public String getColumnText( Object element, int columnIndex ) {
        ClientInfo clientInfo = ( ClientInfo ) element;
        switch ( columnIndex ) {
	        case 0:
	            return "" + getClientActivity( clientInfo );
	        case 1:
	            return "" + clientInfo.getClientName();
	        case 2:
	            return "" + clientInfo.getClientnetworkid().getNetworkName();
	        case 3:
	            return "" + getClientConnection( clientInfo );
	        default:
	            return "";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param clientInfo DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getClientConnection( ClientInfo clientInfo ) {
        if ( clientInfo.getClientKind().getClientMode() == EnumClientMode.FIREWALLED )
            return "firewalled";
        else
            return "direct";
    }

    /**
     * DOCUMENT ME!
     *
     * @param clientInfo DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getClientActivity( ClientInfo clientInfo ) {
        if ( ( clientInfo.getState().getState() == EnumState.CONNECTED_DOWNLOADING )
                 || ( clientInfo.getState().getRank() == 0 ) )
            return "" + clientInfo.getState().getState().toString();
        else
            return "" + clientInfo.getState().getState().toString() + " (Q: "
                   + clientInfo.getState().getRank() + ")";
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
$Log: TableLabelProvider.java,v $
Revision 1.4  2003/09/18 11:42:03  lemmster
checkstyle

Revision 1.3  2003/08/23 15:21:37  zet
remove @author

Revision 1.2  2003/08/22 21:17:25  lemmster
replace $user$ with $Author: lemmster $

Revision 1.1  2003/08/20 14:58:43  zet
sources clientinfo viewer



*/
