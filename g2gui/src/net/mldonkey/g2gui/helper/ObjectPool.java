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
package net.mldonkey.g2gui.helper;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ObjectPool
 *
 * @author markus
 * @version $Id: ObjectPool.java,v 1.3 2003/08/21 10:13:14 dek Exp $ 
 *
 */
public abstract class ObjectPool {
	/**
	 * Number of Objects to create
	 */
	protected final short max = 70;
	/**
	 * Number of Objects minimal in the Pool
	 */
	protected final short initial = 50;
	/**
	 * Number of Objects minimal in the Pool
	 */
	protected final short min = 20;
	
	/**
	 * This Object represents the currently <CODE>used</CODE> sockets
	 */
	protected List used;
	/**
	 * This Object represents the currently <CODE>unused<CODE> sockets
	 */
	protected List unused;
	
	/**
	 * Generates a new ObjectPool
	 */
	public ObjectPool() {
		this.used = new ArrayList();
		this.unused = new ArrayList();
	}

	/**
	 * Creates a new Object
	 * @return an Object
	 */
	protected abstract Object create() throws UnknownHostException, IOException ;
	
	
	/**
	 * Get a Object from the Pool
	 * @return an Object 
	 */
	public synchronized Object checkOut() throws UnknownHostException, IOException {
		Object obj = null;
		
		/* iterate over the list to find an obj */
		Iterator itr = this.unused.iterator();
		while ( itr.hasNext() ) {
			Object elem = itr.next();
			/* if we found an obj */
			if ( !( elem == null ) ) {
				/* remove the obj from our unused list */
				this.unused.remove( elem );
				/* add the obj to our used list */
				this.used.add( elem );
				
				obj = ( Object ) elem;
				
				/* check if there are enough obj left in unused 
				 * if false, generate spawn -1 new obj */
				if ( this.unused.size() == 1 ) {
						for ( int i = 0; i < ( initial - min ); i++ ) {
							this.unused.add( create() );
						}
				}
				/* we have done all our work, so leave the while loop */
				break;
			}
		}
		return obj;
	}

	
	/**
	 * Returns the Object to the ObjectPool 
	 * @param obj The Object which is returned
	 */
	public synchronized void checkIn( Object obj ) {
		/* remove the obj from the used list */
		this.used.remove( obj );

		/* get rid of unneeded obj */
		if ( this.unused.size() > max ) {
			/* free obj for gc */
			obj = null;
		} 
		else
			/* put the obj in the unused list */
			this.unused.add( obj );
	}
}

/*
$Log: ObjectPool.java,v $
Revision 1.3  2003/08/21 10:13:14  dek
removed " malformed UTF-8 character" (only in comment)

Revision 1.2  2003/08/04 14:38:13  lemmstercvs01
splashscreen and error handling added

Revision 1.1  2003/06/12 13:09:52  lemmstercvs01
ObjectPool, DownloadPool, GuiMessagePool added;
class hierarchy under ObjectPool created

*/