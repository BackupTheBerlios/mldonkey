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

import net.mldonkey.g2gui.model.Addr;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.ServerInfo;
import net.mldonkey.g2gui.model.enum.EnumState;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * TableLabelProvider
 *
 * @author $user$
 * @version $Id: TableLabelProvider.java,v 1.1 2003/08/05 13:50:10 lemmstercvs01 Exp $ 
 *
 */
public class TableLabelProvider implements ITableLabelProvider {
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage( Object element, int columnIndex ) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 *
	 * "network", "name", "desc", "address", "serverScore", "users", "files", "state", "favorite"
	 */
	public String getColumnText( Object element, int columnIndex ) {
		ServerInfo server = ( ServerInfo ) element;
		
		if ( columnIndex == 0 ) // network id
			return ( ( NetworkInfo ) server.getNetwork() ).getNetworkName();

		else if ( columnIndex == 1 ) // name
			return server.getNameOfServer();

		else if ( columnIndex == 2 ) // desc
			return server.getDescOfServer();

		else if ( columnIndex == 3 )  {// address
			Addr addr = server.getServerAddress();
			if ( addr.hasHostName() )
				return addr.getAddress().getHostName();
			else
				return addr.getAddress().getHostAddress();
		}

		else if ( columnIndex == 4 ) // port
			return new Integer( server.getServerPort() ).toString();

		else if ( columnIndex == 5 ) // score
			return new Integer( server.getServerScore() ).toString();

		else if ( columnIndex == 6 ) // users
			return new Integer( server.getNumOfUsers() ).toString();

		else if ( columnIndex == 7 ) // files
			return new Integer( server.getNumOfFilesShared() ).toString();

		else if ( columnIndex == 8 ) { //state
			EnumState enum = ( EnumState ) server.getConnectionState().getState();
			if ( enum == EnumState.CONNECTED )
				return "Connected";
			else if ( enum == EnumState.CONNECTED_AND_QUEUED )
				return "Connected and queued";
			else if ( enum == EnumState.CONNECTED_DOWNLOADING )
				return "Connected and downloading";
			else if ( enum == EnumState.CONNECTED_INITIATING )
				return "Connection initiated";
			else if ( enum == EnumState.BLACK_LISTED )
				return "Black listed";
			else if ( enum == EnumState.NEW_HOST )
				return "New Host";
			else if ( enum == EnumState.NOT_CONNECTED )
				return "Not connected";
			else if ( enum == EnumState.NOT_CONNECTED_WAS_QUEUED )
				return "Not connected was queued";
			else if ( enum == EnumState.REMOVE_HOST )
				return "Remove host";
		}
		
		else if ( columnIndex == 9 ) {
			if ( server.isFavorite() ) 
				return "true";
			else 
				return "false";		
		}
		return "";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void addListener( ILabelProviderListener listener ) { }

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	public void dispose() { }

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
}

/*
$Log: TableLabelProvider.java,v $
Revision 1.1  2003/08/05 13:50:10  lemmstercvs01
initial commit

*/