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
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

/**
 * TableLabelProvider
 *
 *
 * @version $Id: ServerTableLabelProvider.java,v 1.4 2003/09/18 11:26:07 lemmster Exp $ 
 *
 */
public class ServerTableLabelProvider implements ITableLabelProvider, IColorProvider {
	private Color connectColor = new Color( null, 41, 174, 57 );
	private Color connectingColor = new Color( null, 255, 165, 0 );
	private Color disconnectColor = new Color( null, 192, 192, 192 );

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage( Object element, int columnIndex ) {
		if ( element instanceof ServerInfo && columnIndex == 0 ) {
			ServerInfo server = ( ServerInfo ) element;
			return G2GuiResources.getNetworkImage( server.getNetwork().getNetworkType() );
		}
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
			return " " + ( ( NetworkInfo ) server.getNetwork() ).getNetworkName();
		else if ( columnIndex == 1 ) // name
			return server.getNameOfServer();

		else if ( columnIndex == 2 ) // desc
			return server.getDescOfServer();

		else if ( columnIndex == 3 )  { // address
			try {
				Addr addr = server.getServerAddress();
				if ( addr.hasHostName() )
					return addr.getHostName();
				else
					return addr.getAddress().getHostAddress();
			}
			catch ( NullPointerException e ) {
				return "0.0.0.0";
			}
		}

		else if ( columnIndex == 4 ) // port
			return new Integer( server.getServerPort() ).toString();
		else if ( columnIndex == 5 ) // score
			return new Integer( server.getServerScore() ).toString();

		else if ( columnIndex == 6 ) // users
			return new Integer( server.getNumOfUsers() ).toString();

		else if ( columnIndex == 7 ) // files
			return new Integer( server.getNumOfFilesShared() ).toString();

		else if ( columnIndex == 8 ) //state
			return server.getConnectionState().getState().toString();

		else if ( columnIndex == 9 ) {
			if ( server.isFavorite() ) 
				return G2GuiResources.getString( "TLP_TRUE" );
			else 
				return G2GuiResources.getString( "TLP_FALSE" );		
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
	public void dispose() {
		connectColor.dispose();
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
			ServerInfo server = ( ServerInfo ) arg0;
			if ( server.isConnected() ) 
				return connectColor;
			if ( server.getConnectionState().getState() == EnumState.CONNECTING )
				return connectingColor;
			if ( server.getConnectionState().getState() == EnumState.NOT_CONNECTED )
				return disconnectColor;		
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
$Log: ServerTableLabelProvider.java,v $
Revision 1.4  2003/09/18 11:26:07  lemmster
checkstyle

Revision 1.3  2003/09/14 10:19:48  lemmster
different colors for the server state (forum request)

Revision 1.2  2003/08/23 15:21:37  zet
remove @author

Revision 1.1  2003/08/23 09:46:18  lemmster
superclass TableMenuListener added

Revision 1.9  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.8  2003/08/17 23:13:42  zet
centralize resources, move images

Revision 1.7  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.6  2003/08/11 19:25:04  lemmstercvs01
bugfix at CleanTable

Revision 1.5  2003/08/07 13:25:37  lemmstercvs01
ResourceBundle added

Revision 1.4  2003/08/07 12:35:31  lemmstercvs01
cleanup, more efficient

Revision 1.3  2003/08/06 18:53:45  lemmstercvs01
missed enum added

Revision 1.2  2003/08/06 17:38:38  lemmstercvs01
some actions still missing. but it should work for the moment

Revision 1.1  2003/08/05 13:50:10  lemmstercvs01
initial commit

*/