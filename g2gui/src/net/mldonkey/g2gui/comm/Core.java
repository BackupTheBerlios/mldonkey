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
package net.mldonkey.g2gui.comm;

import gnu.trove.TIntObjectHashMap;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Observable;

import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.model.ClientInfoIntMap;
import net.mldonkey.g2gui.model.ClientMessage;
import net.mldonkey.g2gui.model.ClientStats;
import net.mldonkey.g2gui.model.ConsoleMessage;
import net.mldonkey.g2gui.model.DefineSearchMap;
import net.mldonkey.g2gui.model.FileInfoIntMap;
import net.mldonkey.g2gui.model.InfoCollection;
import net.mldonkey.g2gui.model.NetworkInfoIntMap;
import net.mldonkey.g2gui.model.OptionsInfoMap;
import net.mldonkey.g2gui.model.ResultInfo;
import net.mldonkey.g2gui.model.ResultInfoIntMap;
import net.mldonkey.g2gui.model.RoomInfoIntMap;
import net.mldonkey.g2gui.model.SearchResult;
import net.mldonkey.g2gui.model.ServerInfoIntMap;
import net.mldonkey.g2gui.model.SharedFileInfoList;
import net.mldonkey.g2gui.model.SimpleInformation;
import net.mldonkey.g2gui.model.UserInfo;

/**
 * Core
 *
 *
 * @version $Id: Core.java,v 1.96 2003/09/16 01:18:31 zet Exp $ 
 *
 */
public class Core extends Observable implements Runnable, CoreCommunication {
	/**
	 * helper field which change to <code>true</code> if
	 * the core denies our connection attempt
	 */
	private boolean connectionDenied;
	/**
	 * Should we use poll or push mode
	 */
	private boolean pollModeEnabled;
	/**
	 * just a little helper field
	 */
	private boolean initialized;
	/**
	 * is the gui running in advancedMode
	 */
	private boolean advancedMode;
	/**
	 * Set this variable to false when we receive the first message after we
	 * expected "bad password"
	 */
	private boolean badPassword = true;
	/**
	 * An waiterobj who is waiting for us in the main thread to be notified if a
	 * specific stage is reached
	 */
	private Object waiterObj;
	/**
	 * The protocol version we maximal speak
	 */
	private static final int PROTOCOL_VERSION = 18;
	/**
	 * The protocol the core speaks
	 */
	private int coreProtocol = 0;
	/**
	 * The protcol version we are using
	 */
	private int usingVersion;
	/**
	 * The socket to work with
	 */
	private Socket connection;
	/**
	 * showing our connection state
	 */
	private boolean connected = false;
	/**
	 * Store the simple informations from the core here
	 */
	private SimpleInformation clientStats = new ClientStats( this ),
							   consoleMessage = new ConsoleMessage(),
							   searchResult = new SearchResult();
	/**
	 * Store the complex informations from the core here
	 */
	private InfoCollection clientInfoList = new ClientInfoIntMap( this ),
					 fileInfoMap          = new FileInfoIntMap( this ),
					 serverInfoMap        = new ServerInfoIntMap( this ),
					 sharedFileInfoList   = new SharedFileInfoList( this ),	
					 optionsInfoMap       = new OptionsInfoMap( this ),
					 networkinfoMap       = new NetworkInfoIntMap( this ),
					 defineSearchMap      = new DefineSearchMap( this ),
					 resultInfoMap		  = new ResultInfoIntMap( this ),
					 roomInfoIntMap       = new RoomInfoIntMap( this );

	/**
	 * Some helper maps
	 */
	private TIntObjectHashMap userInfo = new TIntObjectHashMap(),
							   resultInfo = new TIntObjectHashMap();

	/**
	 * Username and Password to work with
	 */
	private String username, password;
	
	/**
	 * Connects the Core to mldonkey @remote
	 */
	public void connect() {		
		this.connected = true;
	}
	
	/**
	 * disConnects the Core from mldonkey @remote	 * 
	 */
	public synchronized void disconnect() {
		this.connected = false;
	}

	/** (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#isConnected()
	 */
	public boolean isConnected() {	
		return connected;
	}
	
	/**
	 * Creates a new Core obj
	 * @param socket The socket to work with
	 * @param username The username for the core
	 * @param password The password for the core
	 * @param waiterObj The waiterobj to notify if we reach a specific stage
	 * @param pollModeEnabled does this core uses pull or push?
	 * @param advancedMode simple or advanced mode of gui?
	 */
	public Core( Socket socket, String username, String password, Object waiterObj, boolean pollModeEnabled, boolean advancedMode ) {
		this.connection = socket;
		this.username = username;
		this.password = password;
		this.waiterObj = waiterObj;
		this.pollModeEnabled = pollModeEnabled;
		this.advancedMode = advancedMode;
	}

	/**
	 * run()
	 * starts the Core and begin receiving messages	 * 
	 */
	public void  run() {

		/* send the initial protocol version */
		this.sendProtocolVersion();
		
		MessageBuffer messageBuffer = new MessageBuffer();		
		int messageLength;
		int position = 0;
		short opCode;		
		InputStream i;
		try {
			i = connection.getInputStream();
		
			byte[] messageContent;
			
			while ( connected ) {	
				/* getting length of message */
				messageLength = Message.readInt32( i );

				// get message content
				messageContent = Message.readStream( i, messageLength );
			
				messageBuffer.setBuffer( messageContent );
				opCode = messageBuffer.readInt16();		
				
				/* decode the message content */			
				this.decodeMessage( opCode, messageLength, messageBuffer, pollModeEnabled );
			}
		}	
		catch ( SocketException e ) {
			/* expect the core denies our connection attempt */
			connected = false;
			this.connectionDenied = true;
			synchronized ( waiterObj ) {
				waiterObj.notify();
			}
		}
		catch ( IOException e ) {
			// we disconnect.. now what?
			connected = false;
			//e.printStackTrace();
		}		
	}
					
	/**
	 * @param opcode
	 * @param connection
	 * @param receivedMessage the thing to decode
	 * decodes the Message and fills the core-stuff with data
	 */
	private synchronized void decodeMessage( short opcode, int messageLength, MessageBuffer messageBuffer, boolean pollModeEnabled ) {
		switch ( opcode ) {
			case Message.R_COREPROTOCOL :				
					coreProtocol = messageBuffer.readInt32();
					/* can we speak the coreprotocol? */
					int i = coreProtocol - PROTOCOL_VERSION;
					if ( i <= 0 )
						this.usingVersion = coreProtocol;
					else
						this.usingVersion = PROTOCOL_VERSION;
					this.sendPullmode( pollModeEnabled );
					this.sendPassword( this.username, this.password );	
					break;
					
			case Message.R_DEFINE_SEARCH :
					this.defineSearchMap.readStream( messageBuffer );
					break;		

			case Message.R_RESULT_INFO :
					ResultInfo result = new ResultInfo( this );
					result.readStream( messageBuffer );
					this.resultInfo.put( result.getResultID(), result );
					break;
					
			case Message.R_SEARCH_RESULT :
					this.resultInfoMap.readStream( messageBuffer );		
					break;
					
			case Message.R_OPTIONS_INFO :
					/*
					 * when we first receive this msg in push-mode,
					 * we passed the badPassword msg so release the waiterobj 
					 * that the G2Gui.main() can continue
					 */
					if ( !initialized ) {
						badPassword = false;
						synchronized ( waiterObj ) {
							waiterObj.notify();
						}
						initialized = true;
					}	
					this.optionsInfoMap.readStream( messageBuffer );			
					break;
				
			case Message.R_FILE_UPDATE_AVAILABILITY :
					int fileId = messageBuffer.readInt32();
					int clientId = messageBuffer.readInt32();
					String availability = messageBuffer.readString();
					if (( ( ClientInfoIntMap ) this.clientInfoList ).containsKey( clientId ))
						( ( ClientInfoIntMap ) this.clientInfoList ).get( clientId )
							.putAvail( fileId, availability );
					break;

					
			case Message.R_FILE_ADD_SOURCE :
					int fileIdentifier = messageBuffer.readInt32();
					int clientIdentifier = messageBuffer.readInt32();
					/*check for null-objects...*/
					if ( ( ( FileInfoIntMap  )this.fileInfoMap   ).contains( fileIdentifier   ) )					
						if ( ( ( ClientInfoIntMap )this.clientInfoList ).get( clientIdentifier ) != null ) {						
						/*everything's fine, we can execute:*/
							( ( FileInfoIntMap ) this.fileInfoMap ).get( fileIdentifier )
								.addClientInfo( ( ( ClientInfoIntMap ) this.clientInfoList )
									.get( clientIdentifier ) );
						}
					break;
			
					
			case Message.R_SERVER_STATE :
					if ( advancedMode )
						this.serverInfoMap.update( messageBuffer );
					break;		
					
			case Message.R_CLIENT_INFO :
					this.clientInfoList.readStream( messageBuffer );
					break;
			
			case Message.R_ADD_SECTION_OPTION :
					( ( OptionsInfoMap )this.optionsInfoMap ).readGeneralOptionDetails( messageBuffer );
					break;
					
			case Message.R_ADD_PLUGIN_OPTION :
					( ( OptionsInfoMap )this.optionsInfoMap ).readPluginOptionDetails( messageBuffer );
					break;		
					
			case Message.R_CLIENT_STATE :
					this.clientInfoList.update( messageBuffer );
					break;	
					
			case Message.R_ROOM_INFO :
					this.roomInfoIntMap.readStream( messageBuffer );			
					break;				
					
			case Message.R_BAD_PASSWORD :
					/* tell the master thread to continue */
					synchronized ( waiterObj ) {
						waiterObj.notify();
					}
					break;
										
			case Message.R_SHARED_FILE_INFO :
					this.sharedFileInfoList.readStream( messageBuffer );
					break;		

			case Message.R_FILE_DOWNLOAD_UPDATE :
					this.fileInfoMap.update( messageBuffer );
					break;	
					
			case Message.R_CLIENT_STATS :
					/*
					 * when we first receive this msg in poll-mode,
					 * we passed the badPassword msg so release the waiterobj 
					 * that the G2Gui.main() can continue
					 */
					if ( !initialized ) {
						badPassword = false;
						synchronized ( waiterObj ) {
							waiterObj.notify();
						}
						initialized = true;
					}
					clientStats.readStream( messageBuffer );
					break;	
					
			case Message.R_DOWNLOAD :
					( ( FileInfoIntMap )this.fileInfoMap ).add( messageBuffer );
					break;					

			case Message.R_CONSOLE :	
					if ( advancedMode )
						this.consoleMessage.readStream( messageBuffer );
					break;
				
			case Message.R_NETWORK_INFO :
					this.networkinfoMap.readStream( messageBuffer );
					break;
					
			case Message.R_USER_INFO :
					UserInfo user = new UserInfo();
					user.readStream( messageBuffer );
					this.userInfo.put( user.getUserId(), user );
					break;		
					
			case Message.R_SERVER_INFO :
					if ( advancedMode )
						this.serverInfoMap.readStream( messageBuffer );
					break;
							
			case Message.R_DOWNLOADING_LIST :
					this.fileInfoMap.readStream( messageBuffer );
					break;
					 
			case Message.R_DOWNLOADED_LIST :
					 break;
					 
			case Message.R_CLEAN_TABLE :
					( ( ClientInfoIntMap )this.clientInfoList ).clean( messageBuffer );
					( ( ServerInfoIntMap )this.serverInfoMap ).clean( messageBuffer );
					break;
					
			case Message.R_MESSAGE_FROM_CLIENT :
					if ( advancedMode ) {
						ClientMessage clientMessage = new ClientMessage( this );
						clientMessage.readStream( messageBuffer );
						this.setChanged();
						this.notifyObservers( clientMessage );
					}
					break;

			default :				
					System.out.println( "unknown opcode: " + opcode + " length: " + messageLength );
					break;				
		}
	}
	
	/**
	 * @param pushmodeEnabled
	 */
	private void sendPullmode( boolean pollmodeEnabled ) {
		if ( pollmodeEnabled ) {			
			ArrayList output = new ArrayList();
			/*Header: We have only one entry*/
			output.add( new Short( ( short )1 ) );
			
			/*Content: We have 2 columns:*/
			output.add( new Integer( 1 ) );
			output.add( new Byte( ( byte )1 ) ); 
			
			Message pushmode = new EncodeMessage( Message.S_GUIEXTENSION, output.toArray() );
			pushmode.sendMessage( getConnection() );			
		}
		
	}

	/**
	 * Sends our protocol version to the core
	 */	
	private void sendProtocolVersion() {
		/* send the core protocol version */
		Object[] temp = new Object[ 1 ];
		temp[ 0 ] = new Integer( PROTOCOL_VERSION );
		Message coreProtocol =
					new EncodeMessage( Message.S_COREPROTOCOL, temp );
		coreProtocol.sendMessage( connection );
		coreProtocol = null;
		
	}
	
	/**
	 * return this temp resultinfo map
	 * @return The temp resultinfo map
	 */
	public TIntObjectHashMap getResultInfo() {
		return this.resultInfo;
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getFileInfoIntMap()
	 */
	public FileInfoIntMap getFileInfoIntMap() {
		return ( FileInfoIntMap ) this.fileInfoMap;
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getOptions()
	 */
	public OptionsInfoMap getOptionsInfoMap() {
		return ( OptionsInfoMap ) optionsInfoMap;
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getServerInfoIntMap()
	 */
	public ServerInfoIntMap getServerInfoIntMap() {
		return ( ServerInfoIntMap ) this.serverInfoMap;
	}

	public ClientInfoIntMap getClientInfoIntMap() {
		return (ClientInfoIntMap) this.clientInfoList;
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getUsingVersion()
	 */
	public int getProtoToUse() {
		return usingVersion;
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getClientStats()
	 */
	public ClientStats getClientStats() {
		return ( ClientStats ) this.clientStats;
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getBadPassword()
	 */
	public boolean getBadPassword() {
		return badPassword;
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#sendPassword(java.lang.String, java.lang.String)
	 */
	public void sendPassword(String username, String password) {
		/* send the password/username */
		String[] aString = { password, username };
		Message message = new EncodeMessage( Message.S_PASSWORD, aString );
		message.sendMessage( connection );
		message = null;
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getConnectionDenied()
	 */
	public boolean getConnectionDenied() {
		return this.connectionDenied;
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getResultInfoIntMap()
	 */
	public ResultInfoIntMap getResultInfoIntMap() {
		return ( ResultInfoIntMap ) this.resultInfoMap;
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getNetworkInfoMap()
	 */
	public NetworkInfoIntMap getNetworkInfoMap() {
		return ( NetworkInfoIntMap ) networkinfoMap;
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getConsoleMessage()
	 */
	public ConsoleMessage getConsoleMessage() {
		return ( ConsoleMessage ) this.consoleMessage;
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getConnection()
	 */
	public Socket getConnection() {
		return connection;
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getRoomInfoIntMap()
	 */
	public RoomInfoIntMap getRoomInfoIntMap() {		
		return ( RoomInfoIntMap ) this.roomInfoIntMap;
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getDefineSearch()
	 */
	public DefineSearchMap getDefineSearch() {
		return ( DefineSearchMap ) this.defineSearchMap;
	}
}

/*
$Log: Core.java,v $
Revision 1.96  2003/09/16 01:18:31  zet
try to handle socket disconnection in a central location

Revision 1.95  2003/09/14 16:22:58  zet
null check

Revision 1.94  2003/09/08 18:25:16  zet
*** empty log message ***

Revision 1.93  2003/09/02 10:00:33  lemmster
getDefineSearch()

Revision 1.92  2003/09/02 09:24:36  lemmster
checkstyle

Revision 1.91  2003/08/26 19:28:40  dek
different first messages for push / pull-mode, so the gui starts faster in each case

Revision 1.90  2003/08/24 16:54:07  dek
RoomInfo is now read from stream to Map, ready for use to implement
all the room-stuff

Revision 1.89  2003/08/23 15:21:37  zet
remove @author

Revision 1.88  2003/08/23 10:02:02  lemmster
use supertype where possible

Revision 1.87  2003/08/22 19:51:09  lemmster
added just javadoc

Revision 1.86  2003/08/22 10:28:22  lemmster
catch wrong "allowed_ips" values (connection denied)

Revision 1.85  2003/08/20 14:26:19  dek
work on build-in-link handler, now sendig out poll-mode request, 
not only creating it...

Revision 1.84  2003/08/19 12:14:16  lemmster
first try of simple/advanced mode

Revision 1.83  2003/08/16 20:15:21  dek
removed unused import

Revision 1.82  2003/08/16 13:51:09  dek
ed2k-link handling-hack continued

Revision 1.81  2003/08/15 22:39:44  dek
ed2k-link handling-hack started

Revision 1.80  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging

Revision 1.79  2003/08/08 13:09:34  zet
cosmetic

Revision 1.78  2003/08/04 14:38:13  lemmstercvs01
splashscreen and error handling added

Revision 1.77  2003/08/03 19:12:47  lemmstercvs01
DisposeListener and DisposeEvent removed (needed somewhere?)

Revision 1.76  2003/08/03 19:09:39  lemmstercvs01
better error handling

Revision 1.75  2003/08/02 10:07:57  lemmstercvs01
synchronized readded

Revision 1.74  2003/08/02 09:58:34  lemmstercvs01
synchronized on decodeMessage() removed

Revision 1.73  2003/08/01 17:21:19  lemmstercvs01
reworked observer/observable design, added multiversion support

Revision 1.72  2003/07/31 14:09:39  lemmstercvs01
notify observers on networkinfo

Revision 1.71  2003/07/28 17:16:51  lemmstercvs01
getServerInfoIntMap() added

Revision 1.70  2003/07/27 22:39:36  zet
small buttons toggle (in popup) for main cool menu

Revision 1.69  2003/07/24 02:22:46  zet
doesn't crash if no core is running

Revision 1.68  2003/07/23 17:39:59  vnc
made core instance connect properly under linux

Revision 1.67  2003/07/23 17:04:26  lemmstercvs01
modified SEARCH_RESULT and RESULT_INFO

Revision 1.66  2003/07/22 16:03:24  zet
There is no IOException if core is not open

Revision 1.65  2003/07/17 15:10:22  lemmstercvs01
remove this.connect() in run()

Revision 1.64  2003/07/15 18:16:40  dek
coreThread is now a daemon-thread (look inside Java-API to see, what this means)

Revision 1.63  2003/07/12 14:11:43  dek
made the ClientInfo-availability easier

Revision 1.62  2003/07/08 17:44:59  dek
*** empty log message ***

Revision 1.61  2003/07/07 15:33:09  dek
made Option-handling more natural

Revision 1.60  2003/07/06 17:56:49  dek
small change

Revision 1.59  2003/07/06 16:40:03  dek
NPE fixed

Revision 1.58  2003/07/06 12:47:22  lemmstercvs01
bugfix for fileUpdateAvailability

Revision 1.57  2003/07/06 12:31:45  lemmstercvs01
fileUpdateAvailability added

Revision 1.56  2003/07/06 11:56:36  lemmstercvs01
fileAddSource added

Revision 1.55  2003/07/06 10:04:39  lemmstercvs01
clientstats() -> clienstats( this )

Revision 1.54  2003/07/06 08:50:12  lemmstercvs01
ResultInfo() -> ResultInfo( this )

Revision 1.53  2003/07/05 15:37:48  lemmstercvs01
UserInfo added

Revision 1.52  2003/07/05 14:04:12  dek
all in order for searching

Revision 1.51  2003/07/04 17:46:28  lemmstercvs01
removed obsolet methods

Revision 1.50  2003/07/02 17:12:11  dek
removed Senseless ToDo-Tag for dummy-method

Revision 1.49  2003/07/02 16:22:41  dek
extensive Checkstyle applying

Revision 1.48  2003/07/02 16:18:15  dek
Checkstyle

Revision 1.47  2003/07/01 17:09:33  dek
search(String) was added, without any function yet

Revision 1.46  2003/07/01 13:42:05  dek
removed someting useless in setOptions

Revision 1.45  2003/07/01 13:33:16  dek
sending optionslist to observers when changed

Revision 1.44  2003/07/01 13:13:27  dek
decode Message is synchronized to prevent it from sending notify()s to disposed gui

Revision 1.43  2003/06/30 07:24:19  lemmstercvs01
some opcodes added

Revision 1.42  2003/06/29 18:33:30  dek
and removed debugging  system.out.println

Revision 1.41  2003/06/29 18:32:23  dek
updating changed option in local copy of OptionList

Revision 1.40  2003/06/27 18:19:11  dek
small changes only

Revision 1.39  2003/06/27 16:23:01  dek
getOptions() added

Revision 1.38  2003/06/27 12:47:19  dek
getNetWorkInfoMap() added - update ;-)

Revision 1.37  2003/06/27 12:45:01  dek
getNetWorkInfoMap() added

Revision 1.36  2003/06/27 12:04:02  dek
consoleMessageFix

Revision 1.35  2003/06/27 11:56:23  dek
sending out consolemesages to observers

Revision 1.34  2003/06/27 10:41:19  lemmstercvs01
changed notify to observer/observable

Revision 1.33  2003/06/26 23:07:32  lemmstercvs01
added removeListeners()

Revision 1.32  2003/06/26 17:34:40  dek
added client-Stat to Information-broadcast

Revision 1.31  2003/06/25 18:04:53  dek
Console-Tab reworked

Revision 1.30  2003/06/24 20:52:49  lemmstercvs01
refactored

Revision 1.29  2003/06/21 13:20:36  dek
work on optiontree continued - one can already change client_name in General-leaf

Revision 1.28  2003/06/20 15:15:22  dek
humm, some interface-changes, hope, it didn't break anything ;-)

Revision 1.27  2003/06/19 08:40:00  lemmstercvs01
checkstyle applied

Revision 1.26  2003/06/18 19:45:55  dek
added getConnection

Revision 1.25  2003/06/18 14:14:17  lemmstercvs01
running :)

Revision 1.24  2003/06/18 13:49:54  lemmstercvs01
interface location changed

Revision 1.23  2003/06/18 13:31:30  dek
foooooooooo, who cares ;-) ??

Revision 1.22  2003/06/17 13:27:32  lemmstercvs01
added listener managment for the view

Revision 1.21  2003/06/17 12:11:02  lemmstercvs01
this.run() in constructor removed, getter method for fileInfoList added

Revision 1.20  2003/06/16 21:47:41  lemmstercvs01
opcode 3 added

Revision 1.19  2003/06/16 20:08:38  lemmstercvs01
opcode 13 added

Revision 1.18  2003/06/16 18:05:12  dek
refactored cleanTable

Revision 1.17  2003/06/16 15:26:21  dek
NetworkInfoMap added

Revision 1.16  2003/06/16 12:13:26  lemmstercvs01
opcode 52 added

Revision 1.15  2003/06/15 16:19:29  lemmstercvs01
some opcodes added

Revision 1.14  2003/06/15 09:58:30  lemmstercvs01
some opcodes added

Revision 1.13  2003/06/14 23:03:26  lemmstercvs01
added opcode 1

Revision 1.12  2003/06/14 19:31:02  lemmstercvs01
some opcodes added

Revision 1.11  2003/06/14 17:41:33  lemmstercvs01
added some opcodes

Revision 1.10  2003/06/13 16:01:36  dek
OpCode NetworkInfo added

Revision 1.9  2003/06/13 15:41:33  dek
Jippieh, problem finally solved. Thx to Jmoule for the inspiration

Revision 1.8  2003/06/13 13:09:45  dek
including many debug-output

Revision 1.7  2003/06/13 12:03:33  lemmstercvs01
changed to use MessageBuffer

Revision 1.6  2003/06/12 22:23:06  lemmstercvs01
lots of changes

Revision 1.5  2003/06/12 18:22:56  dek
*** empty log message ***

Revision 1.4  2003/06/12 18:16:00  lemmstercvs01
DownloadList -> FileInfoList

Revision 1.3  2003/06/12 10:37:56  lemmstercvs01
added some opcodes

Revision 1.2  2003/06/11 15:32:33  lemmstercvs01
still in progress

Revision 1.1  2003/06/11 12:56:10  lemmstercvs01
moved from model -> comm

Revision 1.6  2003/06/10 16:43:17  dek
removed senseless parameters from constructor

Revision 1.5  2003/06/10 16:29:25  dek
let's rock: everything is working ;-)

Revision 1.4  2003/06/10 16:24:20  dek
Checkstyle-cleaned

Revision 1.3  2003/06/10 16:06:36  dek
now working

Revision 1.2  2003/06/09 22:08:23  dek
No time for checkstyle yet. Next step: trying to get the whole thing running...

Revision 1.1  2003/06/09 21:14:52  dek
broken atm, work in progress...

*/