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
package net.mldonkey.g2gui.helper;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.Tag;

/**
 * MessageBuffer
 *
 *
 * @version $Id: MessageBuffer.java,v 1.27 2003/12/01 13:20:20 zet Exp $ 
 *
 */
public class MessageBuffer {
	/**
	 * If core.getProtoToUse() > proto 16
	 */
	private boolean newCore;
	/**
	 * The iterator for the byte[]
	 */
	private int iterator;
	/**
	 * The byte[] containing the InputStream
	 */
	private byte[] buffer = null;

	/**
	 * Generates a new empty MessageBuffer
	 */	
	public MessageBuffer( CoreCommunication aCore ) {
		this.newCore = aCore.getProtoToUse() > 16;
		this.iterator = 0;
	}

	/**
	 * @return an int
	 */
	public int getIterator() {
		return iterator;
	}

	/**
	 * @param i an int
	 */
	public void setIterator( int i ) {
		iterator = i;
	}
	
	/**
	 * Reads a Byte from an InputStream
	 * @return	byte a byte
	 */
	public byte readByte() {
		byte result = this.buffer[iterator];
		iterator++;	
		
		return result;
	}

	/**
	 * Reads an Int8 from MessageBuffer
	 * @return an int8
	 */
	public short readInt8() {
		
		short result = ( short ) this.buffer[iterator];
		if ( result < 0 ) {
			result += 256;
		}
		iterator++;
		return result;
	}
	/**
	 * Reads an int16 from from MessageBuffer
	 * @return an int16
	 */
	public short readInt16() {		
		return ( short ) ( readInt8() + 256 * readInt8() );		
	}
	/**
	 * Reads an int32 from from MessageBuffer	
	 * @return an int32 
	 */
	public  int readInt32() {
		return  ( readInt8() + 256
			  * ( readInt8() + 256
			  * ( readInt8() + 256
			  * ( readInt8() ) ) ) );
		
	}
	
	/**
	 * read an in16 and return an unsigned int
	 * (used for ClientKind.port)
	 * @return int
	 */
	public int readUnsignedInt16() {
	    int a = ( int ) ( readByte() & 0xFF );
	    int b = ( int ) ( readByte() & 0xFF );
	    
	    return (a + 256 * b);
	}

	
	/**
	 * Reads an signed Int32 from the MessageBuffer
	 * @return an int32
	 */
	public int readSignedInt32() {
		int result = 0;
		for ( int i = 0; i < 4; i++ ) 
			if ( newCore )
				result |= ( ( int ) ( readByte() & 0xFF ) << ( i * 8 ) );
			else
				result |= ( ( int ) ( readByte() ) << ( i * 8 ) );
		return result;
	}
	
	/**
	 * Reads a long from MessageBuffer
	 * @return an int64
	 */
	public long readInt64() {
		return    ( readInt8() + 256
				* ( readInt8() + 256 
				* ( readInt8() + 256 
				* ( readInt8() + 256 
				* ( readInt8() + 256 
				* ( readInt8() + 256
				* ( readInt8() + 256
				* ( readInt8() ) ) ) ) ) ) ) );		
	}
	/**
	 * Reads a String from MessageBuffer
	 * @return a string
	 */
	public String readString() {
		int stringLength = readInt16();					
		if ( stringLength > 0 ) {					
			String result = new String( buffer, iterator, stringLength );			
			this.iterator = this.iterator + stringLength;
			return result;
		} 
		else
			return ( "" );
	}
	
	/**
	 * Reads a String[] from MessageBuffer
	 * @return a string[]
	 */
	public String[] readStringList() {
		short listElem = readInt16();
		String[] result = new String[ listElem ];
		for ( int i = 0; i < listElem; i++ ) {
			result[ i ] = readString();
		}
		return result;
	}
	/**
	 * Creates a string from a binary message
	 * @return a string
	 * @param length The length to read
	 */
	public String readBinary( int length ) {
		StringBuffer result = new StringBuffer();
		for ( int i = 0; i < length; i++ ) {			
			short temp = readInt8();			
			if ( temp <= 0xf ) result.append( 0 );
			result.append( Integer.toHexString( temp ) );			
		}
		return result.toString();
	}
	
	/**
	 * Reads a int[] from MessageBuffer
	 * @return an int[]
	 */
	public int[] readInt32List() {
		int listElem = readInt16();
			//check, if this is a big unsigned int, and fix it:
			if ( listElem < 0 )
				listElem = ( int )( Short.MAX_VALUE * 2 + listElem ) + 2;
		int[] result = new int[ listElem ];
		for ( int i = 0; i < listElem; i++ ) {
			result[ i ] = readInt32(); 
		}
		return result;
	}
	
	/**
	 * Reads a Tag[] from a MessageBuffer
	 * @return a Tag[]
	 */
	public Tag[] readTagList() {
		/* read the list of tags */
		short listElem = this.readInt16();
		Tag[] result = new Tag[ listElem ];
		for ( int i = 0; i < listElem; i++ ) {
			Tag aTag = new Tag();
			aTag.readStream( this );
			result[ i ] = aTag;
		}
		return result;
	}
	
	/**
	 * Reads an IP Address from the MessageBuffer
	 * @return an InetAddress
	 * @throws UnknownHostException we dont't know you...
	 */
	public InetAddress readInetAddress() throws UnknownHostException {
		byte[] temp = new byte[ 4 ];
		for ( int i = 0; i < 4; i++ ) {
			temp[ i ] = readByte();
		}
		InetAddress result = InetAddress.getByName( numericToText( temp ) );
		return result;
	}
	
	/**
	 * Translate a 4 byte obscure ip into a real ip
	 * @param src four byte[] with obscure ip
	 * @return a string represantation of the ip
	 */
	private String numericToText( byte[] src ) {
		return ( src[ 0 ] & 0xff ) + "."
				+ ( src[ 1 ] & 0xff ) + "."
				+ ( src[ 2 ] & 0xff ) + "."
				+ ( src[ 3 ] & 0xff );
	}
	

	/**
	 * @return a byte[]
	 */
	public byte[] getBuffer() {
		return buffer;
	}

	/**
	 * @param bs a byte[]
	 */
	public void setBuffer( byte[] bs ) {
		iterator = 0;
		buffer = bs;
	}

}

/*
$Log: MessageBuffer.java,v $
Revision 1.27  2003/12/01 13:20:20  zet
readunsigned16

Revision 1.26  2003/10/13 19:30:57  zet
only check proto version on instantiation

Revision 1.25  2003/10/13 08:28:09  lemmster
use readSignednt32() depending of the core protocol version

Revision 1.24  2003/10/12 21:17:31  zet
compat priority w/2.5.4

Revision 1.23  2003/10/05 00:53:55  zet
read priority #s properly 
requires latest core dev version (fixed bug in guiEncoding)

Revision 1.22  2003/09/11 11:01:38  lemmster
fixed ip addi order [bug #883]

Revision 1.21  2003/09/11 10:47:29  lemmster
ip addresses in correct order

Revision 1.20  2003/09/02 09:24:36  lemmster
checkstyle

Revision 1.19  2003/08/23 15:21:37  zet
remove @author

Revision 1.18  2003/08/22 21:03:15  lemmster
replace $user$ with $Author: zet $

Revision 1.17  2003/08/10 23:20:26  zet
signed ints

Revision 1.16  2003/08/06 16:07:51  dek
reverted....

Revision 1.14  2003/07/06 07:39:49  lemmstercvs01
checkstyle applied

Revision 1.13  2003/07/02 16:23:30  dek
Checkstyle

Revision 1.12  2003/07/02 15:54:03  dek
checkstyle

Revision 1.11  2003/07/01 14:31:47  dek
bugfix for bugfix ;-)

Revision 1.10  2003/07/01 13:28:38  dek
fixed negative array-size (was a problem with a (short) Overflow

Revision 1.9  2003/06/30 07:20:44  lemmstercvs01
readTagList(), readInetAddress() added

Revision 1.8  2003/06/16 21:48:09  lemmstercvs01
debug statement removed

Revision 1.7  2003/06/15 21:44:57  dek
seems as if i fixed all bugs in readBinary, lemmy, please test ;-)

Revision 1.5  2003/06/15 21:26:19  dek
fixed readBinary

Revision 1.4  2003/06/14 12:47:51  lemmstercvs01
checkstyle applied

Revision 1.3  2003/06/13 12:02:47  dek
cleaned up code

Revision 1.2  2003/06/13 12:01:07  dek
bugFixed

Revision 1.1  2003/06/13 10:43:30  dek
introduced MessageBuffer for performance issues

*/