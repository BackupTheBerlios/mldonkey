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

import gnu.trove.TIntObjectIterator;

import java.util.Observable;
import java.util.Observer;
import org.eclipse.swt.widgets.Composite;
import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.*;


/**
 * NetworkItem
 *
 * @author $user$
 * @version $Id: NetworkItem.java,v 1.2 2003/06/27 13:37:28 dek Exp $ 
 *
 */
public class NetworkItem extends StatusLineItem implements Observer {
	
	private CoreCommunication core;
	private StatusLine statusline;
	private Composite parent;


	/**
	 * @param statusline the Statusline in which this item should appear
	 * @param line
	 * @param mldonkey
	 */
	public NetworkItem(StatusLine statusline, CoreCommunication mldonkey) {
	super();
	this.parent = statusline.getStatusline();
	content = "";
	this.statusline = statusline;
	mldonkey.addObserver( this );	
	this.core = mldonkey;	
	}


	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {
		/*
		 * 	private long totalUp;
			private long totalDown;
			private long totalShared;
			private int numOfShare;
			private float tcpUpRate;
			private float tcpDownRate;
			private float udpUpRate;
			private float udpDownRate;
			private int numCurrDownload;
			private int numDownloadFinished;
			private int[] connectedNetworks;
		 */
		
		if (arg instanceof ClientStats){
			ClientStats temp = (ClientStats) arg;
			final int[] networks = temp.getConnectedNetworks();
			if (!parent.isDisposed())						
				parent.getDisplay().syncExec( new Runnable () {
					public void run() {
						statusline.update(position," connected to "+networks.length+" networks");
						String toolTipText = "";									
						TIntObjectIterator it = ((NetworkInfoIntMap) core.getNetworkinfoMap()).iterator();
						while (it.hasNext()){
							it.advance();
							
							if (((NetworkInfo)it.value()).isEnabled())	{	
								if (toolTipText!="") toolTipText+="\n";					
								toolTipText += (((NetworkInfo)it.value()).getNetworkName());							
							}
								
						}
						statusline.updateTooltip(position,toolTipText);
					}
				});
		}
	}

}

/*
$Log: NetworkItem.java,v $
Revision 1.2  2003/06/27 13:37:28  dek
tooltips added

Revision 1.1  2003/06/27 13:21:12  dek
added connected Networks

*/