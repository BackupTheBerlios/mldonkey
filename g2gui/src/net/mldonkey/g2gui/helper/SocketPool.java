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

import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;

/**
 * SocketPool
 *
 * @author ${user}
 * @version $Id: SocketPool.java,v 1.7 2003/06/13 10:45:23 lemmstercvs01 Exp $ 
 *
 */
public class SocketPool extends ObjectPool {
	
	private int protocolver = 16;

	private String username;

	private String password;

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
	protected short initial = 3;
	
	/**
	 * Maximum amount of Socket Connections to hold
	 */
	protected short max = 6;
	
	/**
	 * Amount of Sockets minimal in the Pool
	 */
	protected short min = 1;

	/**
	 * Initialize the SocketPool by hostname and port
	 * @param address The address to connect to
	 * @param port The port to connect to
	 */
	public SocketPool( String address, int port, String username, String password ) {
		super();

		this.address = address;
		this.port = port;
		this.username = username;
		this.password = password;
		
		/* spawn Sockets for min */
		for ( int i = 0; i < initial; i++ ) {
			this.unused.add( create() );
		}
	}
	
	/**
	 * Creates a new Socket
	 * @return
	 * @throws IOException
	 */
	protected Object create() {
		Socket socket = null;
		try {
			socket = new Socket( this.address, this.port );
			
			/* send the core protocol version */
			Object[] temp = new Object[ 1 ];
			temp[ 0 ] = new Integer( protocolver );		
			Message coreProtocol =
				new EncodeMessage( Message.S_COREPROTOCOL, temp );			
			coreProtocol.sendMessage( socket );
			coreProtocol = null;
			
			/* send the gui extensions (poll mode) */			
			Object[] extension = { new Integer( 1 ), new Byte( ( byte ) 1 )};
			Object[][] a = { extension };
			Message guiExtension = new EncodeMessage( Message.S_GUIEXTENSION, a );
			guiExtension.sendMessage( socket );
			guiExtension = null;

			/* send the password/username */
			String[] aString = { this.password, this.username };
			Message password = new EncodeMessage( Message.S_PASSWORD, aString );
			password.sendMessage( socket );
			password = null;
		}
		catch ( IOException e ) {
		}
			return socket;
	}
	
	
	/**
	 * Returns the Socket to the SocketPool 
	 * @param socket The socket which is returned
	 */
	public synchronized void checkIn( Socket socket ) {
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
Revision 1.7  2003/06/13 10:45:23  lemmstercvs01
send protocol, user/pass, gui extension in socketpool

Revision 1.6  2003/06/12 22:23:06  lemmstercvs01
lots of changes

Revision 1.5  2003/06/12 18:19:29  dek
*** empty log message ***

Revision 1.4  2003/06/12 13:09:52  lemmstercvs01
ObjectPool, DownloadPool, GuiMessagePool added;
class hierarchy under ObjectPool created

Revision 1.3  2003/06/11 15:32:33  lemmstercvs01
still in progress

Revision 1.2  2003/06/09 17:33:21  lemmstercvs01
checkstyle applied

*/