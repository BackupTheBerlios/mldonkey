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

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.model.*;

/**
 * Core
 *
 * @author $user$
 * @version $Id: Core.java,v 1.17 2003/06/16 15:26:21 dek Exp $ 
 *
 */
public class Core extends Thread implements CoreCommunication {
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
	private Information fileAddSources = new FileAddSource(),
						clientStats = new ClientStats(),
						consoleMessage = new ConsoleMessage();
	/**
	 * 
	 */
	private InfoCollection clientInfoList = new ClientInfoList(),
					 fileInfoMap = new FileInfoList(),
					 serverInfoMap = new ServerInfoList(),
					 addSectionOptionList = new AddSomeOptionList(),
					 addPluginOptionList = new AddSomeOptionList(),
					 sharedFileInfoList = new SharedFileInfoList(),	
					 optionsInfoMap = new OptionsInfoMap(),
					 networkinfoMap = new NetworkInfoMap();

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
		this.run();
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
	private void decodeMessage( short opcode, MessageBuffer messageBuffer ) throws IOException 
		{
		switch ( opcode ) {
			case Message.R_COREPROTOCOL :				
					coreProtocol = messageBuffer.readInt32();
					/* send a request for FileInfoList */					
					this.requestFileInfoList();
					break;

			case Message.R_OPTIONS_INFO :
					this.optionsInfoMap.readStream( messageBuffer );				
					break;
				
			case Message.R_FILE_UPDATE_AVAILABILITY :				
					break;

			case Message.R_FILE_ADD_SOURCE :				
					this.fileAddSources.readStream( messageBuffer );
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
					break;	
					
			case Message.R_DOWNLOAD :
					( ( FileInfoList )this.fileInfoMap ).add( messageBuffer );
					break;					

			case Message.R_CONSOLE :				
					this.consoleMessage.readStream( messageBuffer );
					break;
				
			case Message.R_NETWORK_INFO :
					this.networkinfoMap.readStream( messageBuffer );
					break;
					
			case Message.R_SERVER_INFO :
					this.serverInfoMap.readStream( messageBuffer );
					break;
							
			case Message.R_DOWNLOADING_LIST :
					this.fileInfoMap.readStream( messageBuffer );
					break;
					 
			case Message.R_DOWNLOADED_LIST :
					 break;

			default :				
					System.out.println( "unknown OP-Code : " + opcode + "!" );
					break;				
		}
	}
	
	/**
	 * Sends a FileInfoList request to the core
	 */
	public void requestFileInfoList() {
		Message downloadingFiles = new EncodeMessage( Message.S_GETDOWNLOADING_FILES );
		downloadingFiles.sendMessage( connection );
		downloadingFiles = null;
	}
	
/*	public void requestFileDownloadUpdate() {
		Message downloadUpdate = new EncodeMessage( Message.S_FILE_DOWNLOAD_UPDATE );
		downloadUpdate.sendMessage( connection );
		downloadUpdate = null;
	}*/
}

/*
$Log: Core.java,v $
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