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
package net.mldonkey.g2gui.view.friends;

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

/**
 * TableLabelProvider
 *
 * @author $Author: lemmster $
 * @version $Id: TableLabelProvider.java,v 1.4 2003/08/22 21:10:57 lemmster Exp $
 */
public class TableLabelProvider implements ITableLabelProvider, IColorProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	 
	 // Will transparent .pngs ever work?
	public Image getColumnImage( Object element, int columnIndex ) {
		return G2GuiResources.getImage("MessagesButtonSmallWhite");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 *
	 * "network", "name", "desc", "address", "serverScore", "users", "files", "state", "favorite"
	 */
	public String getColumnText( Object element, int columnIndex ) {
		ClientInfo clientInfo = ( ClientInfo ) element;
		
		switch (columnIndex) {
			
			case 0: // name 
				return clientInfo.getClientName();
			
			default:
				return "??";
			
		}
	}


	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void addListener( ILabelProviderListener listener ) { }

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
	public void removeListener( ILabelProviderListener listener ) { }

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
Revision 1.4  2003/08/22 21:10:57  lemmster
replace $user$ with $Author$

Revision 1.3  2003/08/17 23:13:42  zet
centralize resources, move images

Revision 1.2  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.1  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging



*/