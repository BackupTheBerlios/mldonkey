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
import java.util.Observable;

import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.model.*;

/**
 * Core
 *
 * @author $user$
 * @version $Id: Core.java,v 1.54 2003/07/06 08:50:12 lemmstercvs01 Exp $ 
 *
 */
public class Core extends Observable implements Runnable, CoreCommunication {
	/**
	 * 
	 */
	private Thread thisThread;
	/**
	 * 
	 */
	private Socket connection;
	/**
	 * 
	 */
	private boolean connected = false;
	/**
	 * 
	 */
	private int coreProtocol = 0;
	/**
	 * 
	 */
	
	private SimpleInformation fileAddSources = new FileAddSource(),
						clientStats          = new ClientStats(),
						consoleMessage       = new ConsoleMessage(),
						searchResult		 = new SearchResult();
	/**
	 * 
	 */
	private InfoCollection clientInfoList = new ClientInfoIntMap( ( CoreCommunication )this ),
					 fileInfoMap          = new FileInfoIntMap( ( CoreCommunication )this ),
					 serverInfoMap        = new ServerInfoIntMap( ( CoreCommunication )this ),
					 addSectionOptionList = new AddSomeOptionList( ( CoreCommunication )this ),
					 addPluginOptionList  = new AddSomeOptionList( ( CoreCommunication )this ),
					 sharedFileInfoList   = new SharedFileInfoList( ( CoreCommunication )this ),	
					 optionsInfoMap       = new OptionsInfoMap( ( CoreCommunication )this ),
					 networkinfoMap       = new NetworkInfoIntMap( ( CoreCommunication )this ),
					 defineSearchMap      = new DefineSearchMap( ( CoreCommunication )this );

	/**
	 * 
	 */
	private TIntObjectHashMap resultInfo = new TIntObjectHashMap(),
							   userInfo = new TIntObjectHashMap();

	/**
	 * 
	 */
	private boolean badPassword = false;
	
	
	/**
	 * Core()
	 * 
	 * @param connection the socket, where the whole thing takes place
	 */
	public Core( Socket connection ) {
		this.connection = connection;
		thisThread = new Thread( this );
		thisThread.start();
	}

	/**
	 * connect()
	 * Connects the Core to mldonkey @remote
	 */
	public void connect() {		
		this.connected = true;
	}
	/**
	 * disconnect()
	 * disConnects the Core from mldonkey @remote	 * 
	 */
	public void disconnect() {
		this.connected = false;
	}

	/** (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#isConnected()
	 */
	public boolean isConnected() {		
		return connected;
	}

	/**
	 * run()
	 * starts the Core and begin receiving messages	 * 
	 */
	public void  run() {
		this.connect();		
		MessageBuffer messageBuffer = new MessageBuffer();		
		int messageLength;
		int position = 0;
		short opCode;		
		InputStream i;
		try {
			i = connection.getInputStream();
			
			byte[] content;
		 		
			while ( connected ) {	
				/* getting length of message */
				messageLength = Message.readInt32( i );							
				content = new byte[messageLength];
				position = 0;
				/* read out the message from stream, re-read if no bytes are waiting, 
				 * untill message is completly read (thx to Jmoule for this idea ;-)
				 */ 
				while ( position < messageLength )
					position += i.read( content, position, ( int ) messageLength - position );
				
				position = 0;					
				messageBuffer.setBuffer( content );
				opCode = messageBuffer.readInt16();		
				
				/* decode the message content */			
				this.decodeMessage( opCode, messageBuffer );
			}			
		} catch ( IOException e ) {
			e.printStackTrace();
		}			
	}
					
	/**
	 * @param opcode
	 * @param connection
	 * @param receivedMessage the thing to decode
	 * decodes the Message and fills the core-stuff with data
	 */
	private synchronized void  decodeMessage( 
				short opcode, 
				MessageBuffer 
				messageBuffer ) throws IOException 
		{
		switch ( opcode ) {
			case Message.R_COREPROTOCOL :				
					coreProtocol = messageBuffer.readInt32();
					/* send a request for FileInfoList */					
					break;
					
			case Message.R_DEFINE_SEARCH :
					this.defineSearchMap.readStream( messageBuffer );
					break;		

			case Message.R_RESULT_INFO :
					ResultInfo result = new ResultInfo( this );
					result.readStream( messageBuffer );
					this.resultInfo.put( result.getResultID(), result );
					this.setChanged();	
					this.notifyObservers( result );
					break;
					
			case Message.R_SEARCH_RESULT :
					this.searchResult.readStream( messageBuffer );		
					this.setChanged();	
					this.notifyObservers( searchResult );
					break;
					
			case Message.R_OPTIONS_INFO :
					this.optionsInfoMap.readStream( messageBuffer );
					this.setChanged();	
					this.notifyObservers( optionsInfoMap );
					break;
				
			case Message.R_FILE_UPDATE_AVAILABILITY :				
					break;

			case Message.R_FILE_ADD_SOURCE :				
					this.fileAddSources.readStream( messageBuffer );
					break;
					
			case Message.R_SERVER_STATE : 
					this.serverInfoMap.update( messageBuffer );
					break;		
					
			case Message.R_CLIENT_INFO :
					this.clientInfoList.readStream( messageBuffer );
					break;
			
			case Message.R_ADD_SECTION_OPTION :
					this.addSectionOptionList.readStream( messageBuffer );
					break;
					
			case Message.R_ADD_PLUGIN_OPTION :
					this.addPluginOptionList.readStream( messageBuffer );
					break;		
					
			case Message.R_CLIENT_STATE :
					this.clientInfoList.update( messageBuffer );
					break;		
					
			case Message.R_BAD_PASSWORD :
					this.badPassword = true;						
					this.disconnect();
					break;
					
			case Message.R_SHARED_FILE_INFO :
					this.sharedFileInfoList.readStream( messageBuffer );
					break;		

			case Message.R_FILE_DOWNLOAD_UPDATE :
					this.fileInfoMap.update( messageBuffer );
					break;	
					
			case Message.R_CLIENT_STATS :				
					clientStats.readStream( messageBuffer );
					this.setChanged();	
					this.notifyObservers( clientStats );
					break;	
					
			case Message.R_DOWNLOAD :
					( ( FileInfoIntMap )this.fileInfoMap ).add( messageBuffer );
					this.setChanged();
					this.notifyObservers( fileInfoMap );
					break;					

			case Message.R_CONSOLE :	
					this.consoleMessage.readStream( messageBuffer );
					this.setChanged();
					this.notifyObservers( consoleMessage );						
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

			default :				
					System.out.println( "unknown OP-Code : " + opcode + "!" );
					break;				
		}
	}
	
	/**
	 * returns the socket
	 * @return a Socket
	 */
	public Socket getConnection() {
		return connection;
	}

	/** 
	 * returns the actual Console-message Buffer
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getConsoleMessage()
	 */
	public ConsoleMessage getConsoleMessage() {
		return ( ConsoleMessage ) this.consoleMessage;
	}
	/**
	 * @return the Infos about all the nice networks we have 
	 */
	public NetworkInfoIntMap getNetworkInfoMap() {
		return ( NetworkInfoIntMap ) networkinfoMap;
	}

	/** (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.CoreCommunication#getOptions()
	 */
	public OptionsInfoMap getOptionsInfoMap() {
		return ( OptionsInfoMap ) optionsInfoMap;
	}
}

/*
$Log: Core.java,v $
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