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

import java.io.BufferedInputStream;
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
 * @version $Id: Core.java,v 1.7 2003/06/13 12:03:33 lemmstercvs01 Exp $ 
 *
 */
public class Core extends Observable {
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
	private FileInfoList fileInfoList = new FileInfoList();
	/**
	 * 
	 */
	private ClientStats clientStats = new ClientStats();
	/**
	 * 
	 */
	private FileAddSource fileAddSources = new FileAddSource();
	
	/**
	 * Core()
	 * 
	 * @param connection_ the socket, where the whole thing takes place
	 */
	public Core( Socket connection ) throws IOException {
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
	public void run() throws IOException {
		this.connect();
		
		MessageBuffer messageBuffer = new MessageBuffer();
		
		int messageLength;
		short opCode;
		
		InputStream i = connection.getInputStream();
		BufferedInputStream bufferStream;
		 		
		while ( connected ) {
			/* getting length of message */
			messageLength = Message.readInt32( i );
			byte[] content = new byte[messageLength];
			i.read(content);
			messageBuffer.setBuffer(content);
			opCode = messageBuffer.readInt16();
			/* decode the message content */
			this.decodeMessage( opCode, messageBuffer);
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
					/*
					 *	PayLoad:
					 *	int32	The maximal protocol version accepted by the core 
					 */
					coreProtocol = messageBuffer.readInt32();

					/* send a request for FileInfoList */					
					this.requestFileInfoList();
										
					break;

			case Message.R_OPTIONS_INFO :				
					/*
					 *	PayLoad:
					 *	List of (String,String)	The list of options with their current value 
					 */
					break;
				
			case Message.R_FILE_UPDATE_AVAILABILITY :				
					/*
					 *	PayLoad:
					 *	int32	File Number
					 *	int32	client_num
					 *	String	Availability				
					 */
					break;
				

			case Message.R_FILE_ADD_SOURCE :				
					/*
					 *	PayLoad:
					 *	int32	The File Identifier 
					 *	int32	The Source/Client Identifier 
					 */
					this.fileAddSources.readStream( messageBuffer );
					break;
			
			case Message.R_BAD_PASSWORD :
					/*
					 * No Payload:
					 * Just display a message and stop receiving
					 */
					System.out.println("Bad Password");						
					this.disconnect();
					break;
					
			case Message.R_CLIENT_STATS :				
					/*
					 *	PayLoad:
					 *	int64	Total uploaded 
					 *	int64	Total downloaded 
					 *	int64	Total shared 
					 *	int32	Number of shared files 
					 *	int32	Tcp Upload Rate 
					 *	int32	Tcp Download Rate 
					 *	int32	Udp Upload Rate 
					 *	int32	Udp Download Rate 
					 *	int32	Number of current downloads 
					 *	int32	Number of downloads finished 
					 *	List of int32  	 Connected Networks 
					 */
					clientStats.readStream( messageBuffer );
					System.out.println(clientStats.toString() );
					break;				

			case Message.R_CONSOLE :				
					/*
					 *	PayLoad:
					 *	String	Data, the core wants to be displayed on the console 
					 */
					String payloadText = messageBuffer.readString();
					System.out.println( payloadText );
					break;
				

			case Message.R_NETWORK_INFO :				
					/*
					 *	PayLoad:
					 *	int32	Network identifier (used in other messages for this network) 
					 *	String	Network name 
					 *	int8	Enabled(1) or Disabled(0) 
					 *	String	Name of network config file 
					 *	int64	Number of bytes uploaded on network 
					 *	int64	Number of bytes downloaded on network 
					 */
					int identifier;
					int stringlength;
					long upload;
					long download;
					String networkName;
					String networkConfig;
					boolean status;
					/*
					 * Here is the right place to create the Network-Classes IMHO
					 * (not yet implemented)
					 */
					//get network_indetifier from payload:
					identifier = ( int ) messageBuffer.readInt32();
					//get network_name from payload:
					networkName = messageBuffer.readString();
					//get enabled|disabled from payload:							
					if ( messageBuffer.readByte() == 1 ) {
						status = true;
					} else {
						status = false;
					}
					//Now follows the decoding of network_config							
					networkConfig = messageBuffer.readString();
					//Some up / download-stats:
					upload = messageBuffer.readInt64();
					download = messageBuffer.readInt64();
					break;
					
			case Message.R_DOWNLOADING_LIST :
					/*
					 * Payload:
					 * a List of running Downloads (FileInfo)
					 */
					 this.fileInfoList.readStream( messageBuffer );
					 this.requestFileInfoList();
					 break;
					 
			case Message.R_DOWNLOADED_LIST :
					/*
					 * Payload:
					 * a List of complete Downloads (FileInfo)
					 */
					 break;
		 

			default :				
					System.out.println( "unknown OP-Code : " + opcode + "!" );
					break;				
		}
	}
	
	public void requestFileInfoList() {
		Message downloadingFiles = new EncodeMessage( Message.S_GETDOWNLOADING_FILES );
		downloadingFiles.sendMessage( connection );
		downloadingFiles = null;
	}
}

/*
$Log: Core.java,v $
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