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
package net.mldonkey.g2gui.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Core
 *
 * @author $user$
 * @version $Id: Core.java,v 1.2 2003/06/09 22:08:23 dek Exp $ 
 *
 */
public class Core extends Thread implements CoreCommunication {

	private Socket connection;
	private boolean connected = false;
	private String username;
	private String password;
	private String host;
	private int port;
	private int coreProtocol = 0;

	/**
	 * Core()
	 * 
	 * @param host_ Hostname
	 * @param port_ Port
	 * @param username_ Username
	 * @param password_ Passwort
	 * @param connection_ the socket, where the whole thing takes place
	 */
	public Core( String host_, int port_, String username_, String password_, Socket connection_ ) {
		this.username = username_;
		this.password = password_;
		this.host = host_;
		this.port = port_;
		this.connection = connection_;
	}

	/**
	 * connect()
	 * Connects the Core to mldonkey @remote
	 */
	public void connect() {
		System.out.println( "connect to host " + host + ":" + port );
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
	public void run() {
		connect();
		InputStream i;
		try {
			i = connection.getInputStream();
			//getting length of message (No use for this so far??):	
			Message.readInt32(i);
			while (connected) {
				short opCode = Message.readInt16(i);
				decodeMessage(opCode, connection);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	/**
	 * @param opcode
	 * @param connection
	 * @param receivedMessage the thing to decode
	 * decodes the Message and fills the core-stuff with data
	 */
	private void decodeMessage(int opcode, Socket connection ) throws IOException {
				InputStream inputStream = connection.getInputStream();
				switch (opcode) {
					case Message.R_COREPROTOCOL :{
							/*
							 *	PayLoad:
							 *	int32	The maximal protocol version accepted by the core 
							 */	
							coreProtocol = Message.readInt32(inputStream);
							Object[] temp = new Object[1];
							temp[0] = new Integer(16);
							Message coreProtocolReply = new Message(Message.S_COREPROTOCOL,temp);
							coreProtocolReply.sendMessage(connection);							
																	
							Object[] extension = { new Integer(1), new Byte((byte) 1)};
							Object[][] a = { extension };
							Message guiExtension = new Message(Message.S_GUIEXTENSION,a);
							guiExtension.sendMessage(connection);							
							
							String[] aString = { this.password, this.username };
							Message password = new Message(Message.S_PASSWORD,aString);
							password.sendMessage(connection);
							break;
					}
						
					case Message.R_OPTIONS_INFO :{
						/*
						 *	PayLoad:
						 *	List of (String,String)	The list of options with their current value 
						 */						
						 break;
					}				
					case Message.R_FILE_UPDATE_AVAILABILITY : {
						/*
						 *	PayLoad:
						 *	int32	File Number
						 *	int32	client_num
						 *	String	Availability				
						 */						
						break;									
					}


					case Message.R_FILE_ADD_SOURCE :
						{
							/*
							 *	PayLoad:
							 *	int32	The File Identifier 
							 *	int32	The Source/Client Identifier 
							 */							
							break;
						}
				
					case Message.R_CLIENT_STATS :
						{
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
							break;
						}


					case Message.R_CONSOLE :
						{
							/*
							 *	PayLoad:
							 *	String	Data, the core wants to be displayed on the console 
							 */
							String payload_text =
							Message.readString(inputStream);
							System.out.println(payload_text);
							//receiving = false;
							break;
						}

					case Message.R_NETWORK_INFO :								
						{
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
							String network_name;
							String network_config;
							boolean status;
							/*
							 * Here is the right place to create the Network-Classes IMHO
							 * (not yet implemented)
							 */
							//get network_indetifier from payload:
							identifier = (int) Message.readInt32(inputStream);
							//get network_name from payload:
							network_name = Message.readString(inputStream);
							//get enabled|disabled from payload:							
							if (Message.readByte(inputStream) == 1) {
								status = true;
							} else {
								status = false;
							}
							//Now follows the decoding of network_config							
							network_config = Message.readString(inputStream);
							//Some up / download-stats:
							upload = Message.readInt64(inputStream);
							download = Message.readInt64(inputStream);			
							break;
						}

					default :
						{
							System.out.println("unknown OP-Code : " + opcode + "!!!");
							//receivedMessage.getStream().read(new byte[receivedMessage.getLength()], 0, receivedMessage.getLength());
							//String payload_ascii = new String(content, 0, receivedMessage.getLength());
							//byte[] content = new byte[receivedMessage.getLength()-2];
							//inputStream.read(content, 0, receivedMessage.getLength()-2);
							//String payload_ascii = new String(content, 0, receivedMessage.getLength()-2);
													//System.out.println("ASCII: "+ payload_ascii );							
							//for (int i = 0; i < receivedMessage.getContent().length-2; i++) {
							//	System.out.print(receivedMessage.getContent()[i]+",");							
							//}
							System.out.print("\n");
							break;
						}
		}				
	}

	/**
	* @return password
	*/
	public String getPassword() {
		return password;
	}

	/**
	 * @return username hallo
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param string Password for Core-Connection 
	 */
	public void setPassword( String string ) {
		password = string;
	}

	/**
	 * @param string Username for Core-Connection 
	 */
	public void setUsername( String string ) {
		username = string;
	}

	/**
	 * @return Connection
	 */
	public Socket getConnection() {
		return connection;
	}
	

}

/*
$Log: Core.java,v $
Revision 1.2  2003/06/09 22:08:23  dek
No time for checkstyle yet. Next step: trying to get the whole thing running...

Revision 1.1  2003/06/09 21:14:52  dek
broken atm, work in progress...

*/