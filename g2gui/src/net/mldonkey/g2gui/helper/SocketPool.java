/*
 * Copyright (C) 2003  Markus Kuppe
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *
 *  * Created on 03.06.2003
 */
 
package net.mldonkey.g2gui.helper;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Markus Kuppe <lemmy@berlios.de>
 */
public class SocketPool {
	
	/**
	 * This Object represents the currently <CODE>used</CODE> socket connections
	 * @author Markus Kuppe <lemmy@berlios.de>
	 */
	protected List used;
	
	/**
	 * This Object represents the currently <CODE>unused<CODE> socket connections
	 * @author Markus Kuppe <lemmy@berlios.de>
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
	 * @author Markus Kuppe <lemmy@berlios.de>
	 */
	protected int min = 3;
	
	/**
	 * Maximum amount of Socket Connections to hold
	 * @author Markus Kuppe <lemmy@berlios.de>
	 */
	protected int max = 6;

	/**
	 * Initialize the SocketPool by hostname and port
	 * @param hostname
	 * @param port
	 */
	public SocketPool(String address, int port) {
		this.address = address;
		this.port = port;
		
		this.used = new ArrayList();
		this.unused = new ArrayList();
		
		/* spawn Sockets for min */
		for(int i=0; i < min; i++) {
			this.unused.add(create());
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
			socket = new Socket(this.address, this.port);
			
		}
		catch (IOException e) {
			//e.printStackTrace();
			net.mldonkey.snippets.Main.debug(3,"Host "+this.address+":"+this.port+" unreachable");
			
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
		while(itr.hasNext())
		{
			Object elem = itr.next();
			/* if we found a socket */
			if(!(elem == null)) {
				/* remove the socket from our unused list */
				this.unused.remove(elem);
				/* add the socket to our used list */
				this.used.add(elem);
				
				socket = (Socket)elem;
				
				/* check if there are enough sockets left in unused 
				 * if false, generate spawn -1 new sockets */
				if(this.unused.size() == 1) {
					for(int i=0; i < (min - 1); i++) {
						this.unused.add(create());
					}
				}
				/* we�ve done all our work, so leave the while loop */
				break;
			}
		}
		return socket;
	}
	
	/**
	 * Returns the Socket to the SocketPool 
	 * @param socket
	 */
	public synchronized void returnSocket(Socket socket) {
		/* remove the socket from the used list */
		this.used.remove(socket);

		/* get rid of unneeded connections */
		if (this.unused.size() > max) {
			/* close the socket */
			try {
				socket.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		} 
		else
			/* put the socket in the unused list */
			this.unused.add(socket);
	}
	
}
