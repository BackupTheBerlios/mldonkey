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

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Message
 *
 * @author markus
 * @version $Id: Message.java,v 1.7 2003/06/12 10:45:44 lemmstercvs01 Exp $ 
 *
 */
public abstract class Message {

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
	 * Send Get Downloading Files (value is 45)
	 */
	public static final short S_GETDOWNLOADING_FILES = 45;
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
	 * Receive Bad Password (value is 45)
	 */
	public static final short R_BAD_PASSWORD = 45;
	/**
	 * Receive ClientStats (value is 49)
	 */
	public static final short R_CLIENT_STATS = 49;
	/**
	 * Receive a Download (value is 52)
	 */
	public static final short R_DOWNLOAD = 52;
	/**
	 * Receive a List of Downloads (value is 53)
	 */
	public static final short R_DOWNLOADING_LIST = 53;
	/**
	 * Receive a List of complete Downloads (value is 54)
	 */
	public static final short R_DOWNLOADED_LIST = 54;

	/**
	 * Reads a Byte from an InputStream
	 * @param inputStream Stream to read the byte from
	 * @return	short
	 * @throws IOException Error if read on inputStream failed
	 */
	public static byte readByte( InputStream inputStream ) throws IOException {
		byte result = ( byte ) inputStream.read();		
		return result;
	}

	/**
	 * Reads a Int8 from an InputStream
	 * @param inputStream Stream to read the byte from
	 * @return	short
	 * @throws IOException Error if read on inputStream failed
	 */
	public static short readInt8( InputStream inputStream ) throws IOException {
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
		return ( short ) ( readInt8( inputStream ) + 256 * readInt8( inputStream ) );
	}
	/**
	 * Reads an int32 from an InputStream
	 * @param inputStream Stream to read the int32 from
	 * @return int
	 * @throws IOException Error if read on inputStream failed
	 */
	public static int readInt32( InputStream inputStream ) throws IOException {
		return  ( readInt8( inputStream ) + 256
			  * ( readInt8( inputStream ) + 256
			  * ( readInt8( inputStream ) + 256
			  * ( readInt8( inputStream ) ) ) ) );
	}
	/**
	 * Reads a long from an InputStream
	 * @param inputStream Stream to read the int64 from
	 * @return long
	 * @throws IOException Error if read on inputStream failed
	 */
	public static long readInt64( InputStream inputStream ) throws IOException {
		return    ( readInt8( inputStream ) + 256
				* ( readInt8( inputStream ) + 256 
				* ( readInt8( inputStream ) + 256 
				* ( readInt8( inputStream ) + 256 
				* ( readInt8( inputStream ) + 256 
				* ( readInt8( inputStream ) + 256
				* ( readInt8( inputStream ) + 256
				* ( readInt8( inputStream ) ) ) ) ) ) ) ) );		
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
	 * Reads a String[] from an InputStream
	 * @param inputStream Stream to read from
	 * @return String[]
	 * @throws IOException Error if read on InputStream failed
	 */
	public static String[] readStringList( InputStream inputStream ) throws IOException {
		short listElem = readInt16( inputStream );
		String[] result = new String[ listElem ];
		for ( int i = 0; i < listElem; i++ ) {
			result[ i ] = readString( inputStream );
		}
		return result;
	}
	/**
	 * Creates a string from a binary message
	 * @param inputStream Stream to read from
	 * @param length Length of the binary message
	 * @return a String
	 * @throws IOException Error if read from Stream failed
	 */
	public static String readBinary( InputStream inputStream, int length ) throws IOException {
		String result = new String();
		for ( int i = 0; i < length; i++ ) {
		result += Integer.toHexString( inputStream.read() );
		}
		return result.toUpperCase();
	}
	
	/**
	 * Reads a int[] from an InputStream
	 * @param inputStream Stream to read from
	 * @return int[]
	 * @throws IOException Error if read on InputStream failed
	 */
	public static int[] readInt32List( InputStream inputStream ) throws IOException {
		short listElem = readInt16( inputStream );
		int[] result = new int[ listElem ];
		for ( int i = 0; i < listElem; i++ ) {
			result[ i ] = readInt32( inputStream ); 
		}
		return result;
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
	 * Sends a message to the core
	 * @param connection Socket to work with
	 * @return boolean true/false
	 */
	public abstract boolean sendMessage( Socket connection );
}

/*
$Log: Message.java,v $
Revision 1.7  2003/06/12 10:45:44  lemmstercvs01
R_BAD_PASSWORD added

Revision 1.6  2003/06/12 10:40:06  dek
readByte() --> readInt8 | readByte returns now: byte

Revision 1.5  2003/06/11 23:25:49  dek
fixed readInt32 /readInt64

Revision 1.4  2003/06/11 20:43:45  lemmstercvs01
setMd4() fixed

Revision 1.3  2003/06/11 16:41:19  lemmstercvs01
still a problem in setMd4()

Revision 1.2  2003/06/11 15:32:33  lemmstercvs01
still in progress

Revision 1.1  2003/06/11 12:56:10  lemmstercvs01
moved from model -> comm

Revision 1.4  2003/06/10 16:22:19  lemmstercvs01
create a class hierarchy for message

*/