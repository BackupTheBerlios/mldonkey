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
 * @version $Id: DisconnectListener.java,v 1.1 2003/11/20 15:39:55 dek Exp $ 
 *
 */
public class DisconnectListener implements Observer, Runnable {

	private CoreCommunication core;
	private Object waiterObj;
	private int result;

	/**
	 * @param core
	 */
	public DisconnectListener(CoreCommunication core, Object waiterObj) {
		this.core = core;
		core.addObserver( this );
		this.waiterObj = waiterObj;		
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update( Observable o, final Object arg ) {
		if ( arg instanceof IOException )
			Display.getDefault().syncExec( this);
		if (result == SWT.YES){			
			synchronized ( core ){				
				((Core)core).reconnect();				
			}
		}
	}

	/* (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {		
		Shell errorShell = new Shell();
		MessageBox box = new MessageBox( errorShell , SWT.ICON_ERROR | SWT.YES | SWT.NO);
		box.setMessage("disconnected : reconnect ?");
		errorShell.dispose();	
		result = box.open();		
	}
	
}



/*
$Log: DisconnectListener.java,v $
Revision 1.1  2003/11/20 15:39:55  dek
A class that does some things on disconnection (mainly reconnecting atm)

*/