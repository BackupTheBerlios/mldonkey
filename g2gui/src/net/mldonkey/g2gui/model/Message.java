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
package net.mldonkey.g2gui.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Message
 *
 * @author ${user}
 * @version $Id: Message.java,v 1.3 2003/06/10 15:05:47 lemmstercvs01 Exp $ 
 *
 */
public class Message {
	/**
	 * Send CoreProtocol (value is 0)
	 */
	public static final short S_COREPROTOCOL = 0;

	/**
	 * Send ConsoleMessage (value is 29)
	 */
	public static final short S_CONSOLEMSG = 29;
	
	/**
	 * Send GuiExtension (value is 47)
	 */
	public static final short S_GUIEXTENSION = 47;
	
	/**
	 * Send Password (value is 52)
	 */
	public static final short S_PASSWORD = 52;
	
	/**
	 * Receive CoreProtocol (value is 0)
	 */
	public static final short R_COREPROTOCOL = 0;

	/**
	 * Receive OptionsInfo (value is 1)
	 */
	public static final short R_OPTIONS_INFO = 1;

	/**
	 * Receive FileUpdateAvailability (value is 9)
	 */
	public static final short R_FILE_UPDATE_AVAILABILITY = 9;

	/**
	 * Receive FileAddSource (value is 10)
	 */
	public static final short R_FILE_ADD_SOURCE = 10;

	/**
	 * Receive Console (value is 19)
	 */
	public static final short R_CONSOLE = 19;
	
	/**
	 * Receive NetworkInfo (value is 20)
	 */	
	public static final short R_NETWORK_INFO = 20;
	
	/**
	 * Receive ClientStats (value is 49)
	 */
	public static final short R_CLIENT_STATS = 49;
	
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
	
	private Message message;

	/**
	 * Generates a new message object
	 * @param opCode the opcode for the message
	 * @param content an object array with the message content
	 */
	public Message( short opCode, Object[] content ) {
		this.opCode = opCode;
		this.content = createMessage( content );
		/* message length = content length + opcode length */
		this.length = this.content.length + 2;
	}
	
	
	/**
	 * Reads the message into the socket
	 * @param connection a socket connection
	 * @return boolean true/false 
	 */
	public boolean sendMessage( Socket connection ) {
		try {

			OutputStream out = connection.getOutputStream();
			
			byte[] temp = merge( toBytes( this.length ), toBytes( this.opCode ) ); 
			byte[] temp2 = merge( temp, this.content );
			out.write( temp2 );			
			return true;
		}
		catch ( IOException e ) {
			return false;
		}
	}

	
	/**
	 * Reads a byte from an InputStream
	 * @param inputStream Stream to read the byte from
	 * @return	short
	 * @throws IOException Error if read on inputStream failed
	 */
	public static short readByte( InputStream inputStream ) throws IOException {
		short result = ( short ) inputStream.read();
		if ( result < 0 ) {
			result += 256;
		}
		return result;
	}
	
	/**
	 * Reads an int16 from an InputStream
	 * @param inputStream Stream to read the int16 from
	 * @return short
	 * @throws IOException Error if read on inputStream failed
	 */
	public static short readInt16( InputStream inputStream ) throws IOException {
		return ( short ) ( readByte( inputStream ) + 256 * readByte( inputStream ) );
	}
	
	/**
	 * Reads an int32 from an InputStream
	 * @param inputStream Stream to read the int32 from
	 * @return int
	 * @throws IOException Error if read on inputStream failed
	 */
	public static int readInt32( InputStream inputStream ) throws IOException {
		return ( readInt16( inputStream ) + 256 * readInt16( inputStream ) );
	}
	
	/**
	 * Reads a long from an InputStream
	 * @param inputStream Stream to read the int64 from
	 * @return long
	 * @throws IOException Error if read on inputStream failed
	 */
	public static long readInt64( InputStream inputStream ) throws IOException {
		return ( readInt32( inputStream ) + 256 * readInt32( inputStream ) );
	}
	
	/**
	 * Reads a String from an InputStream
	 * @param inputStream Stream to read the string from
	 * @return String
	 * @throws IOException Error if read on inputStream failed
	 */
	public static String readString( InputStream inputStream ) throws IOException {
		int value = readInt16( inputStream );
		if ( value > 0 ) {
			byte[] content = new byte[ value ];
			inputStream.read( content, 0, value );
			String result = new String( content, 0, value );
			return ( result );
		} 
		else
			return ( "" );
	}

	/**
	 * Creates a 2 byte array of int16 from a short
	 * @param aShort short object to create a byte array with
	 * @return a byte array of int16
	 */
	public static byte[] toBytes( Short aShort ) {
		short value = aShort.shortValue();
		byte[] result = new byte[ 2 ];
		for ( int j = 0; j < 2; j++ ) {
			result[ j ] = ( byte ) ( value % 256 );
			value = ( short ) ( value / 256 );
		}
		return result;
	}
	
	/**
	 * Creates a 2 byte array of int16 from a short object
	 * @param aShort short to create a byte array with
	 * @return a byte array of int16
	 */
	public static byte[] toBytes ( short aShort ) {		
		byte[] result = new byte[ 2 ];
		for ( int j = 0; j < 2; j++ ) {
			result[ j ] = ( byte ) ( aShort % 256 );
			aShort = ( short ) ( aShort / 256 );
		}
		return result;
	}
	
	/**
	 * Creates a 4 byte array of int32 from an int object
	 * @param anInt int object to create a byte array with
	 * @return a byte array of int32
	 */
	public static byte[] toBytes( Integer anInt ) {
		int temp = anInt.intValue();
		byte[] result = new byte[ 4 ];
		for ( int j = 0; j < 4; j++ ) {
			result[ j ] = ( byte ) ( temp % 256 );
			temp = temp / 256;
		}
		return result;
	}
	
	/**
	 * Creates a 4 byte array of int32 from an int
	 * @param anInt int to create a byte array with
	 * @return a byte array of int32
	 */
	public static byte[] toBytes( int anInt ) {			
			byte[] result = new byte[ 4 ];
			for ( int j = 0; j < 4; j++ ) {
				result[ j ] = ( byte ) ( anInt % 256 );
				anInt = anInt / 256;
			}
			return result;
		}

	/**
	 * Creates a 8 byte array of int64 from a long object
	 * @param aLong long object to create a byte array with
	 * @return a byte array of int64
	 */
	public static byte[] toBytes( Long aLong ) {
		long temp = aLong.longValue();
		byte[] result = new byte[ 8 ];
		for ( int j = 0; j < 8; j++ ) {
			result[ j ] = ( byte ) ( temp % 256 );
			temp = temp / 256;
		}
		return result;
	}
	
	/**
	 * Creates a 8 byte array of int64 from a long
	 * @param aLong long to create a byte array with
	 * @return a byte array of int64
	 */
	public static byte[] toBytes( long aLong ) {			
		byte[] result = new byte[ 8 ];
		for ( int j = 0; j < 8; j++ ) {
			result[ j ] = ( byte ) ( aLong % 256 );
			aLong = aLong / 256;
		}
		return result;
	}
	
	/**
	 * Merge two byte arrays together
	 * @param src The first byte array
	 * @param dest The second byte array
	 * @return A byte array with src on first and dest on second
	 */
	public static byte[] merge( byte[] src, byte[] dest ) {
		byte [] result =  new byte[ src.length + dest.length ];
		for ( int i = 0; i < src.length; i++ ) {
			result[ i ] = src[ i ];
		}
		for ( int i = 0; i < dest.length; i++ ) {
			result[ i + src.length ] = dest[ i ];
		}
		return result;
	}

	/**
	 * Creates the message payload from a given object array
	 * @param content object array which represense the payload
	 * @return a byte array 
	 */
	private static byte[] createMessage( Object[] content ) {
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
							msglength =
								msglength + content[ i ].toString().length() + 2;
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
				byte[] helper = toBytes( ( ( Short ) content[ i ] ).shortValue() );
				System.arraycopy( helper, 0, buffer, buffer_walker, helper.length );
				buffer_walker += helper.length;
			} 
			else if ( content[ i ] instanceof Integer ) {
				//converting Object to Int (is there some nicer way?)				
				byte[] helper = toBytes( ( ( Integer ) content[ i ] ).intValue() );
				System.arraycopy( helper, 0, buffer, buffer_walker, helper.length );
				buffer_walker += helper.length;
			}
			else if ( content[ i ] instanceof Long ) {
				//converting Object to Int (is there some nicer way?)				
				byte[] helper = toBytes( ( ( Long ) content[ i ] ).longValue() );
				System.arraycopy( helper, 0, buffer, buffer_walker, helper.length );
				buffer_walker += helper.length;
			}
			else if ( content[ i ] instanceof String ) {
				String temp = content[ i ].toString();

				byte[] prefix = toBytes( ( short ) temp.length() );
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
					byte[] temp = toBytes( listcount );
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
							byte[] helper = toBytes( ((Short)received[j]).shortValue() );
							System.arraycopy( helper, 0, buffer, buffer_walker, helper.length );
							buffer_walker += helper.length;
						}
						else if ( received[ j ] instanceof Integer ) {
							byte[] helper = toBytes( ((Integer)received[j]).intValue() );
							System.arraycopy( helper, 0, buffer, buffer_walker, helper.length );
							buffer_walker += helper.length;
						}
						else if ( received[ j ] instanceof Long ) {
							byte[] helper = toBytes( ((Long)received[j]).longValue() );
							System.arraycopy( helper, 0, buffer, buffer_walker, helper.length );
							buffer_walker += helper.length;
						}
						else if ( received[ j ] instanceof String ) {
							String temp = received[ j ].toString();

							byte[] prefix = toBytes( ( short ) temp.length() );
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
$Log: Message.java,v $
Revision 1.3  2003/06/10 15:05:47  lemmstercvs01
use System.arraycopy() instead of for loops

Revision 1.2  2003/06/09 23:02:49  lemmstercvs01
fixed a bug in merge() - counter position modified

Revision 1.1  2003/06/09 21:15:10  lemmstercvs01
initial release

*/