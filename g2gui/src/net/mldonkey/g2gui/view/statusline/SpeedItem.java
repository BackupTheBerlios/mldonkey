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

import net.mldonkey.g2gui.comm.Core;
import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.ClientStats;

/**
 * SpeedItem
 *
 * @author $user$
 * @version $Id: SpeedItem.java,v 1.2 2003/06/27 10:36:17 lemmstercvs01 Exp $ 
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
	public SpeedItem(Composite parent,StatusLine statusline, CoreCommunication mldonkey) {
		super();
		this.parent = parent;
		content = "";
		this.statusline = statusline;
		( ( Core ) mldonkey ).addObserver( this );
		
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {
		if (arg instanceof ClientStats){
			ClientStats temp = (ClientStats) arg;
			final float down = temp.getTcpDownRate();
			final float up = 	temp.getTcpUpRate();
						
			parent.getDisplay().syncExec( new Runnable () {
				public void run() {					
					statusline.update(position," UL: "+up+" DL: "+down);
				}
			});
		}
	}

}

/*
$Log: SpeedItem.java,v $
Revision 1.2  2003/06/27 10:36:17  lemmstercvs01
changed notify to observer/observable

Revision 1.1  2003/06/26 21:11:10  dek
speed is shown

*/