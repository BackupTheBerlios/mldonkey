/*
 * Copyright (C) 2003  Jörg Maisenbacher
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
 *  * Created on 09.06.2003
 */
 
package net.mldonkey.g2gui.comm;

import gnu.trove.TIntObjectHashMap;

import java.io.IOException;
import java.net.Socket;
import java.util.Observer;

import net.mldonkey.g2gui.model.*;

/**
 * CoreCommunication
 *
 * @author $user$
 * @version $Id: CoreCommunication.java,v 1.24 2003/08/01 17:21:19 lemmstercvs01 Exp $ 
 *
 */
public interface CoreCommunication {
	/**
	 * connect()
	 * Connects this object to given mldonkey-core @remote 
	 */	
	void connect();
	/**
	 * disables receiving messages from core, and should also disconnect tcp-connection
	 * in future, not done yet
	 */
	void disconnect();
	/**
	 * @return returns wether we are connected to a mldonkey or not
	 */
	boolean isConnected();
	/**
	 * run()
	 * starts the Core-Thread and begins to receive messages
	 * @throws IOException If read on stream failed 
	 */	
	void run() throws IOException;
	
	/**
	 * 
	 * @return the ConsoleMessage(-Buffer)
	 */
	ConsoleMessage getConsoleMessage();
	/**
	 * @return Infos about all the nice networks we know about
	 */
	NetworkInfoIntMap getNetworkInfoMap();
	/**
	 * @return all mldonkey-options
	 */
	OptionsInfoMap getOptionsInfoMap();
	/**
	 * @return all our current running downloads
	 */
	FileInfoIntMap getFileInfoIntMap();
	/**
	 * @return all the known servers
	 */
	ServerInfoIntMap getServerInfoIntMap();
	/**
	 * @return A Map with all the resultInfo obj
	 */
	ResultInfoIntMap getResultInfoIntMap();
	
	/**
	 * @return The clientstats of the core
	 */
	ClientStats getClientStats();
	
	/**
	 * Gets the socket the core is conntected through with mldonkey
	 * @return The socket
	 */
	Socket getConnection();
	
	/**
	 * The protocol version we are talking with the core
	 * @return the protocol version 
	 */
	int getProtoToUse();

	/**
	 * Adds an Observer to this object
	 * @param obj The Observer to add
	 */
	void addObserver( Observer obj );
	/**
	 * Removes an Observer from this object
	 * @param obj The Observer to remove
	 */
	void deleteObserver( Observer obj );
}
