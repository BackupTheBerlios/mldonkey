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
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * SocketPool
 *
 * @author ${user}
 * @version $Id: SocketPool.java,v 1.3 2003/06/11 15:32:33 lemmstercvs01 Exp $ 
 *
 */
public class SocketPool {
	
	/**
	 * This Object represents the currently <CODE>used</CODE> sockets
	 */
	protected List used;
	
	/**
	 * This Object represents the currently <CODE>unused<CODE> sockets
	 */
	protected List unused;
	
	/**
	 * Address of sockets in the pool
	 */
	protected String address;

	/**
	 * Port of sockets in the pool
	 */
	protected int port;
	
	/**
	 * Amount of Socket Connections to initial create
	 */
	protected int min = 3;
	
	/**
	 * Maximum amount of Socket Connections to hold
	 */
	protected int max = 6;

	/**
	 * Initialize the SocketPool by hostname and port
	 * @param address The address to connect to
	 * @param port The port to connect to
	 */
	public SocketPool( String address, int port ) {
		this.address = address;
		this.port = port;
	
		this.used = new ArrayList();
		this.unused = new ArrayList();
		
		/* spawn Sockets for min */
		for ( int i = 0; i < min; i++ ) {
			this.unused.add( create() );
		}
	}
	
	/**
	 * Creates a new Socket
	 * @return
	 * @throws IOException
	 */
	private Socket create() {
		Socket socket = null;
		try {
			socket = new Socket( this.address, this.port );
	
		}
		catch ( IOException e ) {
		}
			return socket;
	}
	
	/**
	 * Receive a Socket from the Pool 
	 * @return Socket
	 */
	public synchronized Socket getSocket() {
		Socket socket = null;
		
		/* iterate over the list to find a socket */
		Iterator itr = this.unused.iterator();
		while ( itr.hasNext() ) {
			Object elem = itr.next();
			/* if we found a socket */
			if ( !( elem == null ) ) {
				/* remove the socket from our unused list */
				this.unused.remove( elem );
				/* add the socket to our used list */
				this.used.add( elem );
				
				socket = ( Socket ) elem;
				
				/* check if there are enough sockets left in unused 
				 * if false, generate spawn -1 new sockets */
				if ( this.unused.size() == 1 ) {
						for ( int i = 0; i < ( min - 1 ); i++ ) {
							this.unused.add( create() );
						}
				}
				/* we´ve done all our work, so leave the while loop */
				break;
			}
		}
		return socket;
	}
	
	/**
	 * Returns the Socket to the SocketPool 
	 * @param socket The socket which is returned
	 */
	public synchronized void returnSocket( Socket socket ) {
		/* remove the socket from the used list */
		this.used.remove( socket );

		/* get rid of unneeded connections */
		if ( this.unused.size() > max ) {
			/* close the socket */
			try {
				socket.close();
			}
			catch ( IOException e ) {
				e.printStackTrace();
			}
		} 
		else
			/* put the socket in the unused list */
			this.unused.add( socket );
	}
}

/*
$Log: SocketPool.java,v $
Revision 1.3  2003/06/11 15:32:33  lemmstercvs01
still in progress

Revision 1.2  2003/06/09 17:33:21  lemmstercvs01
checkstyle applied

*/