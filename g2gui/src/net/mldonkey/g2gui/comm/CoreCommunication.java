/*
 * Copyright 2003
 * G2GUI Team
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

import java.net.Socket;
import java.util.Observer;

import net.mldonkey.g2gui.model.ClientInfoIntMap;
import net.mldonkey.g2gui.model.ClientStats;
import net.mldonkey.g2gui.model.ConsoleMessage;
import net.mldonkey.g2gui.model.DefineSearchMap;
import net.mldonkey.g2gui.model.FileInfoIntMap;
import net.mldonkey.g2gui.model.NetworkInfoIntMap;
import net.mldonkey.g2gui.model.OptionsInfoMap;
import net.mldonkey.g2gui.model.ResultInfoIntMap;
import net.mldonkey.g2gui.model.RoomInfoIntMap;
import net.mldonkey.g2gui.model.ServerInfoIntMap;

/**
 * CoreCommunication
 *
 *
 * @version $Id: CoreCommunication.java,v 1.38 2003/09/18 15:29:25 zet Exp $ 
 *
 */
public interface CoreCommunication extends Runnable {
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
	 */	
	void run();
	
	/**
	 * Sends the password message to the core. 
	 * used to resend the message if the first try failed
	 * @param username The username
	 * @param password The password
	 */
	void sendPassword( String username, String password );
		
	/**
	 * @return the ConsoleMessage(-Buffer)
	 */
	ConsoleMessage getConsoleMessage();
	/**
	 * @return A map defining the search masks
	 */
	DefineSearchMap getDefineSearch();
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
	 * @return The map containing the clientinfo objs
	 */
	ClientInfoIntMap getClientInfoIntMap();
	/**
	 * @return The map containing the RoomInfo objs
	 */
	RoomInfoIntMap getRoomInfoIntMap();	
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
	 * returns true if bad password was received
	 * @return the boolean for bad password
	 */
	boolean getBadPassword();
	
	/**
	 * @return true if the connection was denied by the core
	 */	
	boolean getConnectionDenied();
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
	
	/**
	 * Send a message
	 * @param messageHeader
	 * @param messageContent
	 */
	void sendMessage ( byte[] messageHeader, byte[] messageContent );
		
	
}
/*
$Log: CoreCommunication.java,v $
Revision 1.38  2003/09/18 15:29:25  zet
centralize writeStream in core
handle IOException rather than throwing it away

Revision 1.37  2003/09/02 10:00:33  lemmster
getDefineSearch()

Revision 1.36  2003/08/24 16:54:07  dek
RoomInfo is now read from stream to Map, ready for use to implement
all the room-stuff

Revision 1.35  2003/08/23 15:21:37  zet
remove @author

Revision 1.34  2003/08/22 20:54:05  lemmster
fix for the fix ;o

Revision 1.33  2003/08/22 20:53:00  lemmster
just include the log
 */
