/*
 * Copyright 2003
 * g2gui-devel Team
 * 
 * 
 * This file is part of g2gui-devel.
 *
 * g2gui-devel is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui-devel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui-devel; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.comm;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * DisconnectListener
 *
 * @version $Id: DisconnectListener.java,v 1.4 2003/11/23 17:58:03 lemmster Exp $ 
 *
 */
public class DisconnectListener implements Observer, Runnable {

	private CoreCommunication core;
	private int result;
	private Shell shell;

	/**
	 * @param core handles all the connections to mldonkey
	 */
	public DisconnectListener( CoreCommunication core, Shell shell ) {
		this.core = core;
		this.shell = shell;
		core.addObserver( this );		
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update( Observable o, final Object arg ) {
		if ( arg instanceof IOException )
			Display.getDefault().syncExec( this );
		if ( result == SWT.YES ) {			
			synchronized ( core ) {				
				core.reconnect();				
			}
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {				
		MessageBox box = new MessageBox( shell , SWT.ICON_ERROR | SWT.YES | SWT.NO );
		box.setMessage( "disconnected : reconnect ?" );		
		result = box.open();		
	}
	
}



/*
$Log: DisconnectListener.java,v $
Revision 1.4  2003/11/23 17:58:03  lemmster
removed dead/unused code

Revision 1.3  2003/11/20 17:51:53  dek
moved disconnect-listener out of core

Revision 1.2  2003/11/20 15:54:50  dek
minor checkstyle and removed unescessary type-cast

Revision 1.1  2003/11/20 15:39:55  dek
A class that does some things on disconnection (mainly reconnecting atm)

*/