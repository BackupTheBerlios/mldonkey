/*
 * Copyright 2003
 * g2gui Team
 * 
 * 
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.comm;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Message
 *
 *
 * @version $Id: EncodeMessage.java,v 1.7 2003/08/23 15:21:37 zet Exp $ 
 *
 */
public class EncodeMessage extends Message {
	/**
	 * The message opcode
	 */
	private short opCode;
	
	/**
	 * The message length
	 */
	private int length;
	
	/**
	 * The message content
	 */
	private byte[] content;
	
	/**
	 * Generates a new and empty message opject
	 */
	public EncodeMessage() {
	}

	/**
	 * Generates a new message object
	 * @param opCode the opcode for the message
	 * @param content an object array with the message content
	 */
	public EncodeMessage( short opCode, Object[] content ) {
		this.opCode = opCode;
		this.content = createContent( content );
		/* message length = content length + opcode length */
		this.length = this.content.length + 2;
	}
	
	/**
	 * Generates a new message object
	 * @param opcode the opcode for the message
	 * @param content an object as message content
	 */
	public EncodeMessage( short opcode, Object content ) {
		this.opCode = opcode;
		Object[] temp = { content };	
		this.content = createContent( temp );
		/* message length = content length + opcode length */
		this.length = this.content.length + 2;
	}
	
	/**
	 * Generates a new message object without content
	 * @param opCode the opcode for the message
	 */
	public EncodeMessage( short opCode ) {
		this.opCode = opCode;
		this.content = null;
		this.length = 2;
	}
	
	/**
	 * Reads the message into the socket
	 * @param connection a socket connection
	 * @return boolean true/false 
	 */
	public boolean sendMessage( Socket connection ) {
		try {
			// null pointer on this when no core running
			BufferedOutputStream bOut = new BufferedOutputStream( connection.getOutputStream() );
			
			byte[] temp = Message.merge( Message.toBytes( this.length ), 
										  Message.toBytes( this.opCode ) );
			byte[] temp2;
			if ( this.content != null )
				temp2 = Message.merge( temp, this.content );
			else
				temp2 = temp;
			
			temp = null;
			bOut.write( temp2 );
			temp2 = null;
			bOut.flush();
			return true;
		}
		catch ( IOException e ) {
			return false;
		}
	}
	
	/**
	 * Creates the message payload from a given object array
	 * @param content object array which represense the payload
	 * @return a byte array 
	 */
	private static byte[] createContent( Object[] content ) {
		int msglength = 0;
		short listcount = 0;
		//First of All, we calculate the length (msglength) of the PayLoad: 
		for ( int i = 0; i < content.length; i++ ) {
			if ( content[ i ] instanceof Byte )
				msglength++;
			else if ( content[ i ] instanceof Short )
				msglength += 2;
			else if ( content[ i ] instanceof Integer )
				msglength += 4;
			else if ( content[ i ] instanceof Long )
				msglength += 8;
			else if ( content[ i ] instanceof String )
				msglength = msglength + content[ i ].toString().length() + 2;
			else if ( content[ i ].getClass().isArray() ) {
				if ( listcount == 0 )	msglength += 2;
				//On first array-entry add 2 bytes for List-header
				listcount++;
				Object[] received;
				//getting the length of listentrys				
				if ( content[ i ] instanceof Object[] ) {
					received = ( Object[] ) content[ i ];
					for ( int j = 0; j < received.length; j++ ) {
						if ( received[ j ] instanceof Byte )
							msglength++;
						else if ( received[ j ] instanceof Short )
							msglength += 2;
						else if ( received[ j ] instanceof Integer )
							msglength += 4;
						else if ( received[ j ] instanceof Long )
							msglength += 8;
						else if ( received[ j ] instanceof String ) 
							msglength = msglength + received[ j ].toString().length() + 2;
					} //end for loop
				} // end if
			} // end last else if 
		} // end for loop
		//Then we re-run the whole Thing again, creating the result:
		byte[] buffer;
		buffer = new byte[ msglength ];
		int buffer_walker = 0;
		boolean list_head_written = false;

		for ( int i = 0; i < content.length; i++ ) {
			if ( content[ i ] instanceof Byte ) {
				byte temp = ( ( Byte ) content[ i ] ).byteValue();
				buffer[ buffer_walker++ ] = temp;
			}
			if ( content[ i ] instanceof Short ) {
				//converting Object to short (is there some nicer way?)				
				byte[] helper = Message.toBytes( ( ( Short ) content[ i ] ).shortValue() );
				System.arraycopy( helper, 0, buffer, buffer_walker, helper.length );
				buffer_walker += helper.length;
			} 
			else if ( content[ i ] instanceof Integer ) {
				//converting Object to Int (is there some nicer way?)				
				byte[] helper = Message.toBytes( ( ( Integer ) content[ i ] ).intValue() );
				System.arraycopy( helper, 0, buffer, buffer_walker, helper.length );
				buffer_walker += helper.length;
			}
			else if ( content[ i ] instanceof Long ) {
				//converting Object to Int (is there some nicer way?)				
				byte[] helper = Message.toBytes( ( ( Long ) content[ i ] ).longValue() );
				System.arraycopy( helper, 0, buffer, buffer_walker, helper.length );
				buffer_walker += helper.length;
			}
			else if ( content[ i ] instanceof String ) {
				String temp = content[ i ].toString();

				byte[] prefix = Message.toBytes( ( short ) temp.length() );
				buffer[ buffer_walker++ ] = prefix[ 0 ];
				buffer[ buffer_walker++ ] = prefix[ 1 ];

				byte[] helper = temp.getBytes();
				System.arraycopy( helper, 0, buffer, buffer_walker, helper.length );
				buffer_walker += helper.length;
			}
			else if ( content[ i ].getClass().isArray() ) {
				Object[] received;
				/*
				 * Hey, we've got a list, let's get to work
				 * First of all, we type in List-header (number of _ROWS_ in the List)
				 */
				if ( !list_head_written ) {
					byte[] temp = Message.toBytes( listcount );
					System.arraycopy( temp, 0, buffer, buffer_walker, temp.length );
					buffer_walker += temp.length;
					list_head_written = true;
				}
				/*
				 * Now we add the current _ROW_ to the Buffer
				 */
				if ( content[i] instanceof Object[] ) {
					received = ( Object[] ) content[ i ];
					for ( int j = 0; j < received.length; j++ ) {
						if ( received[ j ] instanceof Byte ) {
							buffer[ buffer_walker++ ] = ( ( Byte ) received[ j ] ).byteValue();
						}
						else if ( received[ j ] instanceof Short ) {
							byte[] helper = Message.toBytes( ((Short)received[j]).shortValue() );
							System.arraycopy( helper, 0, buffer, buffer_walker, helper.length );
							buffer_walker += helper.length;
						}
						else if ( received[ j ] instanceof Integer ) {
							byte[] helper = Message.toBytes( ((Integer)received[j]).intValue() );
							System.arraycopy( helper, 0, buffer, buffer_walker, helper.length );
							buffer_walker += helper.length;
						}
						else if ( received[ j ] instanceof Long ) {
							byte[] helper = Message.toBytes( ((Long)received[j]).longValue() );
							System.arraycopy( helper, 0, buffer, buffer_walker, helper.length );
							buffer_walker += helper.length;
						}
						else if ( received[ j ] instanceof String ) {
							String temp = received[ j ].toString();

							byte[] prefix = Message.toBytes( ( short ) temp.length() );
							buffer[ buffer_walker++ ] = prefix[ 0 ];
							buffer[ buffer_walker++ ] = prefix[ 1 ];

							byte[] helper = temp.getBytes();
							System.arraycopy( helper, 0, buffer, buffer_walker, helper.length );
							buffer_walker += helper.length;
						}
					}//End of processing Single List-Entry 
				}//End of processing Array_ROW_
			} //End of processing array
		} //End of creating the result
		return buffer;
	}

	/**
	 * @return
	 */
	private byte[] getContent() {
		return content;
	}

	/**
	 * @return
	 */
	private int getLength() {
		return length;
	}

	/**
	 * @return
	 */
	private short getOpCode() {
		return opCode;
	}
}

/*
$Log: EncodeMessage.java,v $
Revision 1.7  2003/08/23 15:21:37  zet
remove @author

Revision 1.6  2003/08/22 21:03:15  lemmster
replace $user$ with $Author: zet $

Revision 1.5  2003/08/04 19:22:21  zet
trial tabletreeviewer

Revision 1.4  2003/07/24 10:33:13  lemmstercvs01
bugfix in msglength calc of a String[]

Revision 1.3  2003/07/03 21:12:50  lemmstercvs01
new EncodeMessage(short, Object) added

Revision 1.2  2003/06/13 11:03:06  lemmstercvs01
changed OutputStream to BufferedOutputStream

Revision 1.1  2003/06/12 22:23:06  lemmstercvs01
lots of changes

Revision 1.3  2003/06/12 13:20:26  lemmstercvs01
minor changes to provide GuiMessagePool

Revision 1.2  2003/06/11 15:32:33  lemmstercvs01
still in progress

Revision 1.1  2003/06/11 12:56:10  lemmstercvs01
moved from model -> comm

Revision 1.1  2003/06/10 16:22:19  lemmstercvs01
create a class hierarchy for message

Revision 1.3  2003/06/10 15:05:47  lemmstercvs01
use System.arraycopy() instead of for loops

Revision 1.2  2003/06/09 23:02:49  lemmstercvs01
fixed a bug in merge() - counter position modified

Revision 1.1  2003/06/09 21:15:10  lemmstercvs01
initial release

*/