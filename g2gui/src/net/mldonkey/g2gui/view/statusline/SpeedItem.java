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

import org.eclipse.swt.widgets.Composite;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.ClientStats;

/**
 * SpeedItem
 *
 * @author $user$
 * @version $Id: SpeedItem.java,v 1.7 2003/06/28 09:37:18 lemmstercvs01 Exp $ 
 *
 */
public class SpeedItem extends StatusLineItem implements Observer {	

	private StatusLine statusline;
	private Composite parent;

	/**
	 * @param string
	 * @param i
	 * @param mldonkey
	 */
	public SpeedItem( StatusLine statusline, CoreCommunication mldonkey ) {
		super();
		this.parent = statusline.getStatusline();
		content = "";
		this.statusline = statusline;
		mldonkey.addObserver( this );
		
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update( Observable o, Object arg ) {
		if ( arg instanceof ClientStats ){
			final ClientStats temp = ( ClientStats ) arg;
			final float down = temp.getTcpDownRate();
			final float up   = temp.getTcpUpRate();
			if ( !parent.isDisposed() )				
			parent.getDisplay().asyncExec( new Runnable () {
				public void run() {
					statusline.update( position, " DL: " + down
										+ "kb/s - UL: " + up + "kb/s" );
					String toolTipText = "UDP-DL: " + temp.getUdpDownRate()
									   + "\nUDP-UL: " + temp.getUdpUpRate();						
					statusline.updateTooltip( position,toolTipText );
				}
			});
		}
	}
}

/*
$Log: SpeedItem.java,v $
Revision 1.7  2003/06/28 09:37:18  lemmstercvs01
syncExec() -> asyncExec()

Revision 1.6  2003/06/27 13:37:28  dek
tooltips added

Revision 1.5  2003/06/27 13:21:12  dek
added connected Networks

Revision 1.4  2003/06/27 11:34:00  lemmstercvs01
foobar

Revision 1.3  2003/06/27 11:07:52  lemmstercvs01
CoreCommunications implements addObserver(Observer)

Revision 1.2  2003/06/27 10:36:17  lemmstercvs01
changed notify to observer/observable

Revision 1.1  2003/06/26 21:11:10  dek
speed is shown

*/