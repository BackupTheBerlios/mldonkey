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
import net.mldonkey.g2gui.model.ModelFactory;
import net.mldonkey.g2gui.model.NetworkInfoIntMap;
import net.mldonkey.g2gui.model.OptionsInfoMap;
import net.mldonkey.g2gui.model.ResultInfoIntMap;
import net.mldonkey.g2gui.model.RoomInfoIntMap;
import net.mldonkey.g2gui.model.ServerInfoIntMap;
import net.mldonkey.g2gui.model.SharedFileInfoIntMap;

/**
 * CoreCommunication
 *
 *
 * @version $Id: CoreCommunication.java,v 1.46 2003/12/04 08:47:31 lemmy Exp $ 
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
	 * reconnect()
	 * reconnects the Core if socket was lost (for example)
	 */
	void reconnect();
	
	/**
	 * Sends the password message to the core. 
	 * used to resend the message if the first try failed
	 * @param username The username
	 * @param password The password
	 */
	void sendPassword();
		
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
	 * @return the Shared Files Map
	 */
	SharedFileInfoIntMap getSharedFileInfoIntMap();
	/**
	 * @return The model factory where we get model objs
	 */
	ModelFactory getModelFactory();
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
	 * start the timer
	 */
	void startTimer();
	/**
	 * stop the timer
	 */
	void stopTimer();
	/**
	 * Send a message
	 * @param messageHeader
	 * @param messageContent
	 */
	void sendMessage ( byte[] messageHeader, byte[] messageContent );
		
	
}
/*
$Log: CoreCommunication.java,v $
Revision 1.46  2003/12/04 08:47:31  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.45  2003/12/01 14:21:55  lemmy
ProtocolVersion handling completely rewritten

Revision 1.44  2003/11/29 20:16:30  zet
stop/start timer on tab (de)activation

Revision 1.43  2003/11/23 17:58:03  lemmy
removed dead/unused code

Revision 1.42  2003/11/20 17:51:53  dek
moved disconnect-listener out of core

Revision 1.41  2003/11/20 15:40:59  dek
reconnect started

Revision 1.40  2003/09/26 11:55:48  dek
right-mouse menue for upload-Table

Revision 1.39  2003/09/25 21:04:26  dek
first sketch of upload-Table not yet added to transferTab.

Revision 1.38  2003/09/18 15:29:25  zet
centralize writeStream in core
handle IOException rather than throwing it away

Revision 1.37  2003/09/02 10:00:33  lemmy
getDefineSearch()

Revision 1.36  2003/08/24 16:54:07  dek
RoomInfo is now read from stream to Map, ready for use to implement
all the room-stuff

Revision 1.35  2003/08/23 15:21:37  zet
remove @author

Revision 1.34  2003/08/22 20:54:05  lemmy
fix for the fix ;o

Revision 1.33  2003/08/22 20:53:00  lemmy
just include the log
 */
