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

import java.net.Socket;

/**
 * Core
 *
 * @author $user$
 * @version $Id: Core.java,v 1.1 2003/06/09 21:14:52 dek Exp $ 
 *
 */
public class Core extends Thread implements CoreCommunication {

	private Socket connection;
	private boolean connected = false;
	private String username;
	private String password;
	private String host;
	private int port;

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
		Message receivedMessage = new Message( connection );
		while ( connected ) {
			int opcode = receivedMessage.getOpCode();			
			receivedMessage.getContent();
			decodeMessage(opcode,connection);			
		}
	}

	/**
	 * @param opcode
	 * @param connection
	 * decodes the Message and fills the core-stuff with data
	 */
	private void decodeMessage( int opcode, Socket connection ) {
				switch (opcode) {
					case MSG.R_COREPROTOCOL :
						{
							/*
							 *	PayLoad:
							 *	int32	The maximal protocol version accepted by the core 
							 */											
							
							coreprotocoll = MSG.readInt32(receivedMessage.getStream());
							MSG.sendMessage(MSG.S_COREPROTOCOL,	MSG.createInt32(16), connection);						
							Object[] row1 = { new Integer(1), new Byte((byte) 1)};
							Object[][] a = { row1 };
							//MSG.sendMessage(MSG.S_GUIEXTENSION,MSG.createMessage(a),connection);
							String[] aString = { this.password, this.username };
							MSG.sendMessage(
								MSG.S_PASSWORD,
								MSG.createMessage(aString),
								connection);

							break;
						}
					case MSG.R_OPTIONS_INFO :{
						/*
						 *	PayLoad:
						 *	List of (St
						 *ring,String)	The list of options with their current value 
						 */
						Main.debug(5,"R_OPTIONS_INFO received");
						 break;
					}
				
					case MSG.R_FILE_UPDATE_AVAILABILITY : {
						/*
						 *	PayLoad:
						 *	int32	File Number
						 *	int32	client_num
						 *	String	Availability				
						 */
						Main.debug(5,"R_FILE_UPDATE_AVAILABILITY received");
						break;									
					}


					case MSG.R_FILE_ADD_SOURCE :
						{
							/*
							 *	PayLoad:
							 *	int32	The File Identifier 
							 *	int32	The Source/Client Identifier 
							 */
							Main.debug(5,"R_FILE_ADD_SOURCE received");
							break;
						}
				
					case MSG.R_CLIENT_STATS :
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
							Main.debug(5,"R_CLIENT_STATS received");
							break;
						}


					case MSG.R_CONSOLE :
						{
							/*
							 *	PayLoad:
							 *	String	Data, the core wants to be displayed on the console 
							 */
							String payload_text =
								MSG.readString(receivedMessage.getStream());
							Main.debug(10, payload_text);
							//receiving = false;
							break;
						}

					case MSG.R_NETWORK_INFO :								
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
							identifier = (int) MSG.readInt32(receivedMessage.getStream());
							//get network_name from payload:
							network_name = MSG.readString(receivedMessage.getStream());
							//get enabled|disabled from payload:							
							if (MSG.readByte(receivedMessage.getStream()) == 1) {
								status = true;
							} else {
								status = false;
							}
							//Now follows the decoding of network_config							
							network_config = MSG.readString(receivedMessage.getStream());
							//Some up / download-stats:
							upload = MSG.readInt64(receivedMessage.getStream());
							download = MSG.readInt64(receivedMessage.getStream());
							// Now we are through the message and can show it the world:
							// And put it all on STDOUT	
							Main.debug(
								9,
								"Network-Identifier: "
									+ network_name
									+ "("
									+ String.valueOf(identifier)
									+ ")"
									+ Boolean.toString(status)
									+ "["
									+ network_config
									+ "]"
									+ "(UP: "
									+ String.valueOf(upload)
									+ "DOWN: "
									+ String.valueOf(download)
									+ ")");
							break;
						}

					default :
						{
							Main.debug(6, "unknown OP-Code : " + opcode + "!!!");
							//receivedMessage.getStream().read(new byte[receivedMessage.getLength()], 0, receivedMessage.getLength());
							//String payload_ascii = new String(content, 0, receivedMessage.getLength());
							byte[] content = new byte[receivedMessage.getLength()-2];
							receivedMessage.getStream().read(content, 0, receivedMessage.getLength()-2);
							String payload_ascii = new String(content, 0, receivedMessage.getLength()-2);
						
							Main.debug("ASCII: "+ payload_ascii );
							Main.debug("dec.: ");
							//for (int i = 0; i < receivedMessage.getContent().length-2; i++) {
							//	System.out.print(receivedMessage.getContent()[i]+",");							
							//}
							System.out.print("\n");
							try {
								sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							break;
						}
				}
			}
			socketpool.returnSocket(connection);
			connection = null;
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
Revision 1.1  2003/06/09 21:14:52  dek
broken atm, work in progress...

*/