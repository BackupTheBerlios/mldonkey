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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.model.ClientInfoIntMap;
import net.mldonkey.g2gui.model.ClientMessage;
import net.mldonkey.g2gui.model.ClientStats;
import net.mldonkey.g2gui.model.ConsoleMessage;
import net.mldonkey.g2gui.model.DefineSearchMap;
import net.mldonkey.g2gui.model.FileInfoIntMap;
import net.mldonkey.g2gui.model.ModelFactory;
import net.mldonkey.g2gui.model.NetworkInfoIntMap;
import net.mldonkey.g2gui.model.OptionsInfoMap;
import net.mldonkey.g2gui.model.ResultInfo;
import net.mldonkey.g2gui.model.ResultInfoIntMap;
import net.mldonkey.g2gui.model.RoomInfoIntMap;
import net.mldonkey.g2gui.model.ServerInfoIntMap;
import net.mldonkey.g2gui.model.SharedFileInfoIntMap;
import net.mldonkey.g2gui.model.UserInfo;
import net.mldonkey.g2gui.view.G2Gui;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;

/**
 * Core
 *
 *
 * @version $Id: Core.java,v 1.127 2004/05/10 18:12:21 dek Exp $ 
 *
 */
public class Core extends Observable implements Runnable, CoreCommunication {
	/**
	 * helper field which change to <code>true</code> if
	 * the core denies our connection attempt
	 */
	private boolean connectionDenied;
	/**
	 * A Factory where we get Model objects
	 */
	private ModelFactory modelFactory;
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
	 * will the gui auto poll for upStats 
	 */
	private boolean pollUpStats;
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
	private static final int PROTOCOL_VERSION = 26;
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
	
	private Timer timer;
	
	/**
	 * Some helper maps
	 */
	private TIntObjectHashMap userInfo = new TIntObjectHashMap(),
							   resultInfo = new TIntObjectHashMap();

	/**
	 * Username and Password to work with
	 */
	private String username, password;
	private int maxOpCodeToGui;
	private int maxOpCodeFromGui;	
	
	/**
	 * Connects the Core to mldonkey @remote
	 */
	public void connect() {		
		this.connected = true;
	}
	
	/**
	 * disConnects the Core from mldonkey @remote
	 */
	public void disconnect() {		
		this.connected = false;
	}

	/* (non-Javadoc)
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
		this.pollUpStats = PreferenceLoader.loadBoolean( "pollUpStats" );	
	}

	/**
	 * A timer to run every 5 seconds
	 * update uploader stats
	 */
	public void startTimer() {
	    if (!pollUpStats) 
	        return;
	    
	    if (timer != null) 
	        timer.cancel();
	    
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				if (!isConnected()) { 
				    this.cancel();
				} else 
				requestUpstats();
			}
		}, 0L, 5000L);
	    
	}
	
	public void stopTimer() {
	    if (timer != null)
	        timer.cancel();
	}
	
	
	/**
	 * run()
	 * starts the Core and begin receiving messages	 * 
	 */
	public void  run() {

		/* send the initial protocol version */
		this.sendProtocolVersion();
		
		MessageBuffer messageBuffer = new MessageBuffer( this );		
		int messageLength;
		short opCode;		
		BufferedInputStream i;
		try {
			i = new BufferedInputStream( connection.getInputStream() );		
			byte[] messageContent;
			
			while ( connected ) {	
				/* getting length of message */
				messageLength = Message.readInt32( i );

				// get message content
				messageContent = Message.readStream( i, messageLength );
			
				messageBuffer.setBuffer( messageContent );				
				opCode = messageBuffer.readInt16();	
				messageBuffer.setOpcode(opCode);
				
				/* decode the message content */			
				this.decodeMessage( opCode, messageLength, messageBuffer, pollModeEnabled );
			}
		}	
		catch ( SocketException e ) {
			if ( !initialized ) {
				/* expect the core denies our connection attempt */
				connected = false;
				this.connectionDenied = true;	
				synchronized ( waiterObj ) {
					waiterObj.notify();
				}
			}
			// for the moment, just print the error. next step -> reconnect
			else
				e.printStackTrace();
		}
		catch ( IOException e ) {
			onIOException( e );
		}	
		stopTimer();
	}
	
	/**
	 * sends a Message to the core
	 */
	public void sendMessage ( byte[] messageHeader, byte[] messageContent ) {
		try {
			Message.writeStream( this.getConnection(), messageHeader, messageContent );
		} 
		catch ( IOException e ) {
			onIOException( e );
		}
	}
	
	/**
	 * handle an Exception from the run() method
	 * @param e The Exception to handle
	 */
	public void onIOException( IOException e ) {
		this.disconnect();		
		this.setChanged();
		this.notifyObservers( e );
	}
	
	/**
	 * reconnects to mldonkey after connection-loss
	 */
	public boolean reconnect() {		
		if (G2Gui.debug) System.out.println("Inline Reconnect");
		Socket newSocket = null;
		try {
			newSocket = G2Gui.initializeSocket();
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		
		// if we pass this point, newsocket cant be null
		this.connection = newSocket;
		this.connected = true;
		Thread restarted = new Thread( this );
		restarted.start();
		return true;
	}

	/**
	 * @param opcode
	 * @param connection
	 * @param receivedMessage the thing to decode
	 * decodes the Message and fills the core-stuff with data
	 */
	private void decodeMessage( short opcode, int messageLength, MessageBuffer messageBuffer, boolean pollModeEnabled ) {
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
					this.sendPassword();
					this.modelFactory = ModelFactory.getFactory( this.usingVersion, this );
					this.maxOpCodeToGui = messageBuffer.readInt32();
					this.maxOpCodeFromGui = messageBuffer.readInt32();
					if (G2Gui.debug){
						System.out.println("core/gui "+coreProtocol+"/"+PROTOCOL_VERSION);
						System.out.println("GUI Protocol-Version: "+this.usingVersion);
					}
					break;
					
			case Message.R_DEFINE_SEARCH :
					this.getModelFactory().getDefineSearchMap().readStream( messageBuffer );
					break;		

			case Message.R_RESULT_INFO :
					ResultInfo result = getModelFactory().getResultInfo();
					result.readStream( messageBuffer );
					this.resultInfo.put( result.getResultID(), result );
					break;
					
			case Message.R_SEARCH_RESULT :
					this.getModelFactory().getResultInfoIntMap().readStream( messageBuffer );		
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
					this.getModelFactory().getOptionsInfoMap().readStream( messageBuffer );			
					break;
				
			case Message.R_FILE_UPDATE_AVAILABILITY :
					int fileId = messageBuffer.readInt32();
					int clientId = messageBuffer.readInt32();
					String availability = messageBuffer.readString();
					if ( ( ( ClientInfoIntMap ) this.getModelFactory().getClientInfoIntMap() ).containsKey( clientId ) )
						( ( ClientInfoIntMap ) this.getModelFactory().getClientInfoIntMap() ).get( clientId )
							.putAvail( fileId, availability );
					break;

					
			case Message.R_FILE_ADD_SOURCE :
					int fileIdentifier = messageBuffer.readInt32();
					int clientIdentifier = messageBuffer.readInt32();
					/*check for null-objects...*/
					if ( ( ( FileInfoIntMap )this.getModelFactory().getFileInfoIntMap()   ).contains( fileIdentifier ) )					
						if ( ( ( ClientInfoIntMap )this.getModelFactory().getClientInfoIntMap() ).get( clientIdentifier ) != null ) {						
						/*everything's fine, we can execute:*/
							( ( FileInfoIntMap ) this.getModelFactory().getFileInfoIntMap() ).get( fileIdentifier )
								.addClientInfo( ( ( ClientInfoIntMap ) this.getModelFactory().getClientInfoIntMap() )
									.get( clientIdentifier ) );
						}
					break;
			
					
			case Message.R_SERVER_STATE :
					if ( advancedMode ) {
						this.getModelFactory().getServerInfoIntMap().update( messageBuffer );
					}
					break;		
					
			case Message.R_CLIENT_INFO :
				this.getModelFactory().getClientInfoIntMap().readStream( messageBuffer );
					break;
			
			case Message.R_ADD_SECTION_OPTION :
					( ( OptionsInfoMap )this.getModelFactory().getOptionsInfoMap() ).readGeneralOptionDetails( messageBuffer );
					break;
					
			case Message.R_ADD_PLUGIN_OPTION :
					( ( OptionsInfoMap )this.getModelFactory().getOptionsInfoMap() ).readPluginOptionDetails( messageBuffer );
					break;		
					
			case Message.R_CLIENT_STATE :
					this.getModelFactory().getClientInfoIntMap().update( messageBuffer );
					break;	
					
			case Message.R_ROOM_INFO :
					this.getModelFactory().getRoomInfoIntMap().readStream( messageBuffer );			
					break;		
					
			case Message.R_SHARED_FILE_UPLOAD :
					this.getModelFactory().getSharedFileInfoIntMap().update( messageBuffer );	
					break;
					
			case Message.R_BAD_PASSWORD :
					/* tell the master thread to continue */
					synchronized ( waiterObj ) {
						waiterObj.notify();
					}
					break;
										
			case Message.R_SHARED_FILE_INFO :
					this.getModelFactory().getSharedFileInfoIntMap().readStream( messageBuffer );
					break;		

			case Message.R_FILE_DOWNLOAD_UPDATE :
					this.getModelFactory().getFileInfoIntMap().update( messageBuffer );
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
					this.getModelFactory().getClientStats().readStream( messageBuffer );
					break;	
					
			case Message.R_DOWNLOAD :
					( ( FileInfoIntMap )this.getModelFactory().getFileInfoIntMap() ).add( messageBuffer );
					break;					

			case Message.R_CONSOLE :	
					if ( advancedMode ) {
						this.getModelFactory().getConsoleMessage().readStream( messageBuffer );
					}
					break;
				
			case Message.R_NETWORK_INFO :
					this.getModelFactory().getNetworkInfoIntMap().readStream( messageBuffer );
					break;
					
			case Message.R_USER_INFO :
					UserInfo user = this.getModelFactory().getUserInfo();
					user.readStream( messageBuffer );
					this.userInfo.put( user.getUserId(), user );
					break;		
					
			case Message.R_SERVER_INFO :
					if ( advancedMode ) {
						this.getModelFactory().getServerInfoIntMap().readStream( messageBuffer );
					}	
					break;
							
			case Message.R_DOWNLOADING_LIST :
					this.getModelFactory().getFileInfoIntMap().readStream( messageBuffer );
					break;
					 
			case Message.R_DOWNLOADED_LIST :
					 break;
					 
			case Message.R_CLEAN_TABLE :
					( ( ClientInfoIntMap )this.getModelFactory().getClientInfoIntMap() ).clean( messageBuffer );
					( ( ServerInfoIntMap )this.getModelFactory().getServerInfoIntMap() ).clean( messageBuffer );
					break;
					
			case Message.R_MESSAGE_FROM_CLIENT :
					if ( advancedMode ) {
						ClientMessage clientMessage = this.getModelFactory().getClientMessage();
						clientMessage.readStream( messageBuffer );
						this.setChanged();
						this.notifyObservers( clientMessage );
					}
					break;
			case Message.R_FILE_REMOVE_SOURCE :
					getFileInfoIntMap().fileRemoveSource(messageBuffer);				
				break;

			default :
					if ( G2Gui.debug )		
						System.out.println( "unknown opcode: " + opcode + " length: " + messageLength );
					break;				
		}
	}
	
	/**
	 * @param pushmodeEnabled
	 */
	private void sendPullmode( boolean pollmodeEnabled ) {
		if ( pollmodeEnabled ) {			
			List output = new ArrayList();
			/*Header: We have only one entry*/
			output.add( new Short( ( short )1 ) );
			
			/*Content: We have 2 columns:*/
			output.add( new Integer( 1 ) );
			output.add( new Byte( ( byte )1 ) ); 
			
			Message pushmode = new EncodeMessage( Message.S_GUIEXTENSION, output.toArray() );
			pushmode.sendMessage( this );			
		}
		
	}

	/**
	 * Sends our protocol version to the core
	 */	
	private void sendProtocolVersion() {
		/* send the core protocol version */
		Object[] temp = new Object[ 1 ];
		temp[ 0 ] = new Integer( PROTOCOL_VERSION );
		Message coreProtocolMsg =
					new EncodeMessage( Message.S_COREPROTOCOL, temp );
		coreProtocolMsg.sendMessage( this );
		coreProtocolMsg = null;		
	}
	
	/**
	 * request the upstats on the core
	 */
	private void requestUpstats() {
		/* requesting upload-statistics */
		Message upstats = new EncodeMessage( Message.S_REFRESH_UPLOAD_STATS );
		upstats.sendMessage( this );
		upstats = null;
		
		getClientInfoIntMap().updateUploaders( this );
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
		return ( FileInfoIntMap ) this.getModelFactory().getFileInfoIntMap();
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getOptions()
	 */
	public OptionsInfoMap getOptionsInfoMap() {
		return ( OptionsInfoMap ) this.getModelFactory().getOptionsInfoMap();
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getServerInfoIntMap()
	 */
	public ServerInfoIntMap getServerInfoIntMap() {
		return ( ServerInfoIntMap ) this.getModelFactory().getServerInfoIntMap();
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
		return (ClientStats) this.getModelFactory().getClientStats();
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
	public void sendPassword() {
		/* send the password/username */
		String[] aString = { this.password, this.username };
		Message message = new EncodeMessage( Message.S_PASSWORD, aString );
		message.sendMessage( this );
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
		return ( ResultInfoIntMap ) this.getModelFactory().getResultInfoIntMap();
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getNetworkInfoMap()
	 */
	public NetworkInfoIntMap getNetworkInfoMap() {
		return ( NetworkInfoIntMap ) this.getModelFactory().getNetworkInfoIntMap();
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getConsoleMessage()
	 */
	public ConsoleMessage getConsoleMessage() {
		return ( ConsoleMessage ) this.getModelFactory().getConsoleMessage();
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
		return ( RoomInfoIntMap ) this.getModelFactory().getRoomInfoIntMap();
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getDefineSearch()
	 */
	public DefineSearchMap getDefineSearch() {
		return ( DefineSearchMap ) this.getModelFactory().getDefineSearchMap();
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getClientInfoIntMap()
	 */
	public ClientInfoIntMap getClientInfoIntMap() {
		return ( ClientInfoIntMap ) this.getModelFactory().getClientInfoIntMap();
	}
	/*
	 * (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getSharedFileInfoIntMap()
	 */
	public SharedFileInfoIntMap getSharedFileInfoIntMap() {
		return ( SharedFileInfoIntMap ) this.getModelFactory().getSharedFileInfoIntMap();
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getModelFactory()
	 */
	public ModelFactory getModelFactory() {
		return this.modelFactory;
	}

}

/*
$Log: Core.java,v $
Revision 1.127  2004/05/10 18:12:21  dek
What is really new in Gui Proto 26 i didn't find out, so i just increased proto number...

Revision 1.126  2004/03/20 01:34:02  dek
implemented gui-Proto 25 !!!!!

Revision 1.125  2004/03/16 17:08:43  dek
implemented opCode 50

Revision 1.124  2004/01/29 08:25:04  lemmy
removed duplicated code

Revision 1.123  2004/01/28 22:15:34  psy
* Properly handle disconnections from the core
* Fast inline-reconnect
* Ask for automatic relaunch if options have been changed which require it
* Improved the local core-controller

Revision 1.122  2004/01/23 22:12:45  psy
reconnection and local core probing improved, continuing work...

Revision 1.121  2003/12/28 14:15:43  dek
debug output: gui proto-version

Revision 1.120  2003/12/04 08:47:31  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.119  2003/12/01 14:21:55  lemmy
ProtocolVersion handling completely rewritten

Revision 1.118  2003/11/30 18:49:08  lemmy
better link handling, handle more than one link simultaneously

Revision 1.117  2003/11/29 20:16:30  zet
stop/start timer on tab (de)activation

Revision 1.116  2003/11/28 13:11:10  zet
support patch 2372

Revision 1.115  2003/11/26 15:48:09  zet
minor

Revision 1.114  2003/11/26 07:41:43  zet
protocolVersion 19/timer

Revision 1.113  2003/11/23 17:58:03  lemmy
removed dead/unused code

Revision 1.112  2003/11/20 17:51:53  dek
moved disconnect-listener out of core

Revision 1.111  2003/11/20 15:54:50  dek
minor checkstyle and removed unescessary type-cast

Revision 1.110  2003/11/20 15:39:26  dek
reconnect started

Revision 1.109  2003/11/20 14:02:17  lemmy
G2Gui cleanup

Revision 1.108  2003/11/07 09:25:42  lemmy
small javadoc fix

Revision 1.107  2003/10/13 08:28:09  lemmy
use readSignednt32() depending of the core protocol version

Revision 1.106  2003/10/12 15:55:18  zet
remove clean

Revision 1.105  2003/09/27 00:36:54  zet
auto poll for upstats preference

Revision 1.104  2003/09/26 11:55:48  dek
right-mouse menue for upload-Table

Revision 1.103  2003/09/25 21:04:26  dek
first sketch of upload-Table not yet added to transferTab.

Revision 1.102  2003/09/25 00:51:03  zet
reset active sources on clean_tables

Revision 1.101  2003/09/18 23:24:07  zet
use bufferedinputstream 
& mods for the annoying gcj project

Revision 1.100  2003/09/18 15:29:25  zet
centralize writeStream in core
handle IOException rather than throwing it away

Revision 1.99  2003/09/18 08:56:27  lemmy
checkstyle

Revision 1.98  2003/09/17 14:40:13  zet
notify on IOException

Revision 1.97  2003/09/17 13:49:09  dek
now the gui refreshes the upload-stats, add and observer to SharedFileInfoIntMap
to get notice of changes in # of requests and # of uploaded bytes

Revision 1.96  2003/09/16 01:18:31  zet
try to handle socket disconnection in a central location

Revision 1.95  2003/09/14 16:22:58  zet
null check

Revision 1.94  2003/09/08 18:25:16  zet
*** empty log message ***

Revision 1.93  2003/09/02 10:00:33  lemmy
getDefineSearch()

Revision 1.92  2003/09/02 09:24:36  lemmy
checkstyle

Revision 1.91  2003/08/26 19:28:40  dek
different first messages for push / pull-mode, so the gui starts faster in each case

Revision 1.90  2003/08/24 16:54:07  dek
RoomInfo is now read from stream to Map, ready for use to implement
all the room-stuff

Revision 1.89  2003/08/23 15:21:37  zet
remove @author

Revision 1.88  2003/08/23 10:02:02  lemmy
use supertype where possible

Revision 1.87  2003/08/22 19:51:09  lemmy
added just javadoc

Revision 1.86  2003/08/22 10:28:22  lemmy
catch wrong "allowed_ips" values (connection denied)

Revision 1.85  2003/08/20 14:26:19  dek
work on build-in-link handler, now sendig out poll-mode request, 
not only creating it...

Revision 1.84  2003/08/19 12:14:16  lemmy
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

Revision 1.78  2003/08/04 14:38:13  lemmy
splashscreen and error handling added

Revision 1.77  2003/08/03 19:12:47  lemmy
DisposeListener and DisposeEvent removed (needed somewhere?)

Revision 1.76  2003/08/03 19:09:39  lemmy
better error handling

Revision 1.75  2003/08/02 10:07:57  lemmy
synchronized readded

Revision 1.74  2003/08/02 09:58:34  lemmy
synchronized on decodeMessage() removed

Revision 1.73  2003/08/01 17:21:19  lemmy
reworked observer/observable design, added multiversion support

Revision 1.72  2003/07/31 14:09:39  lemmy
notify observers on networkinfo

Revision 1.71  2003/07/28 17:16:51  lemmy
getServerInfoIntMap() added

Revision 1.70  2003/07/27 22:39:36  zet
small buttons toggle (in popup) for main cool menu

Revision 1.69  2003/07/24 02:22:46  zet
doesn't crash if no core is running

Revision 1.68  2003/07/23 17:39:59  vnc
made core instance connect properly under linux

Revision 1.67  2003/07/23 17:04:26  lemmy
modified SEARCH_RESULT and RESULT_INFO

Revision 1.66  2003/07/22 16:03:24  zet
There is no IOException if core is not open

Revision 1.65  2003/07/17 15:10:22  lemmy
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

Revision 1.58  2003/07/06 12:47:22  lemmy
bugfix for fileUpdateAvailability

Revision 1.57  2003/07/06 12:31:45  lemmy
fileUpdateAvailability added

Revision 1.56  2003/07/06 11:56:36  lemmy
fileAddSource added

Revision 1.55  2003/07/06 10:04:39  lemmy
clientstats() -> clienstats( this )

Revision 1.54  2003/07/06 08:50:12  lemmy
ResultInfo() -> ResultInfo( this )

Revision 1.53  2003/07/05 15:37:48  lemmy
UserInfo added

Revision 1.52  2003/07/05 14:04:12  dek
all in order for searching

Revision 1.51  2003/07/04 17:46:28  lemmy
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

Revision 1.43  2003/06/30 07:24:19  lemmy
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

Revision 1.34  2003/06/27 10:41:19  lemmy
changed notify to observer/observable

Revision 1.33  2003/06/26 23:07:32  lemmy
added removeListeners()

Revision 1.32  2003/06/26 17:34:40  dek
added client-Stat to Information-broadcast

Revision 1.31  2003/06/25 18:04:53  dek
Console-Tab reworked

Revision 1.30  2003/06/24 20:52:49  lemmy
refactored

Revision 1.29  2003/06/21 13:20:36  dek
work on optiontree continued - one can already change client_name in General-leaf

Revision 1.28  2003/06/20 15:15:22  dek
humm, some interface-changes, hope, it didn't break anything ;-)

Revision 1.27  2003/06/19 08:40:00  lemmy
checkstyle applied

Revision 1.26  2003/06/18 19:45:55  dek
added getConnection

Revision 1.25  2003/06/18 14:14:17  lemmy
running :)

Revision 1.24  2003/06/18 13:49:54  lemmy
interface location changed

Revision 1.23  2003/06/18 13:31:30  dek
foooooooooo, who cares ;-) ??

Revision 1.22  2003/06/17 13:27:32  lemmy
added listener managment for the view

Revision 1.21  2003/06/17 12:11:02  lemmy
this.run() in constructor removed, getter method for fileInfoList added

Revision 1.20  2003/06/16 21:47:41  lemmy
opcode 3 added

Revision 1.19  2003/06/16 20:08:38  lemmy
opcode 13 added

Revision 1.18  2003/06/16 18:05:12  dek
refactored cleanTable

Revision 1.17  2003/06/16 15:26:21  dek
NetworkInfoMap added

Revision 1.16  2003/06/16 12:13:26  lemmy
opcode 52 added

Revision 1.15  2003/06/15 16:19:29  lemmy
some opcodes added

Revision 1.14  2003/06/15 09:58:30  lemmy
some opcodes added

Revision 1.13  2003/06/14 23:03:26  lemmy
added opcode 1

Revision 1.12  2003/06/14 19:31:02  lemmy
some opcodes added

Revision 1.11  2003/06/14 17:41:33  lemmy
added some opcodes

Revision 1.10  2003/06/13 16:01:36  dek
OpCode NetworkInfo added

Revision 1.9  2003/06/13 15:41:33  dek
Jippieh, problem finally solved. Thx to Jmoule for the inspiration

Revision 1.8  2003/06/13 13:09:45  dek
including many debug-output

Revision 1.7  2003/06/13 12:03:33  lemmy
changed to use MessageBuffer

Revision 1.6  2003/06/12 22:23:06  lemmy
lots of changes

Revision 1.5  2003/06/12 18:22:56  dek
*** empty log message ***

Revision 1.4  2003/06/12 18:16:00  lemmy
DownloadList -> FileInfoList

Revision 1.3  2003/06/12 10:37:56  lemmy
added some opcodes

Revision 1.2  2003/06/11 15:32:33  lemmy
still in progress

Revision 1.1  2003/06/11 12:56:10  lemmy
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