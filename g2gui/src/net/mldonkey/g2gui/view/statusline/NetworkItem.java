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
package net.mldonkey.g2gui.view.statusline;

import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.ClientStats;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.view.StatusLine;

import org.eclipse.swt.widgets.Composite;


/**
 * NetworkItem
 *
 * @author $user$
 * @version $Id: NetworkItem.java,v 1.6 2003/07/17 14:58:56 lemmstercvs01 Exp $ 
 *
 */
public class NetworkItem extends StatusLineItem implements Observer {
	private static ResourceBundle res = ResourceBundle.getBundle("g2gui");
	private CoreCommunication core;
	private StatusLine statusline;
	private Composite parent;


	/**
	 * @param statusline the Statusline in which this item should appear
	 * @param line
	 * @param mldonkey
	 */
	public NetworkItem( StatusLine statusline, CoreCommunication mldonkey ) {
		super();
		this.parent = statusline.getStatusline();
		this.statusline = statusline;
		this.core = mldonkey;	
		content = "";

		mldonkey.addObserver( this );	
	}


	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update( Observable o, Object arg ) {
		if ( arg instanceof ClientStats ) {
			ClientStats temp = ( ClientStats ) arg;
			final NetworkInfo[] networks = temp.getConnectedNetworks();
			if ( !parent.isDisposed() )						
				parent.getDisplay().asyncExec( new Runnable () {
					public void run() {
						statusline.update( position, res.getString( "NI_ConnectedTo" ) + " "
													 + networks.length + " "
													 + res.getString( "NI_Networks" ) );
						String toolTipText = "";
						
						for ( int i = 0; i < networks.length; i++ ) {						
								if ( toolTipText != "" ) toolTipText += "\n";					
								toolTipText += networks[ i ].getNetworkName();
							}
						statusline.updateTooltip( position, toolTipText );
					}
				});
		}
	}
}

/*
$Log: NetworkItem.java,v $
Revision 1.6  2003/07/17 14:58:56  lemmstercvs01
refactored

Revision 1.5  2003/07/06 13:19:39  dek
small change

Revision 1.4  2003/07/04 17:48:57  dek
refactor

Revision 1.3  2003/06/28 09:50:00  lemmstercvs01
hasNext() optimized

Revision 1.2  2003/06/27 13:37:28  dek
tooltips added

Revision 1.1  2003/06/27 13:21:12  dek
added connected Networks

*/