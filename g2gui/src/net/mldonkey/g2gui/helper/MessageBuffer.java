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

/**
 * MessageBuffer
 *
 * @author $user$
 * @version $Id: MessageBuffer.java,v 1.2 2003/06/13 12:01:07 dek Exp $ 
 *
 */
public class MessageBuffer {
	int iterator;
	byte[] buffer = null;
	
	public MessageBuffer(){
		
		this.iterator = 0;
		
	}

	/**
	 * @return
	 */
	public int getIterator() {
		return iterator;
	}

	/**
	 * @param i
	 */
	public void setIterator(int i) {
		iterator = i;
	}
	
	/**
	 * Reads a Byte from an InputStream
	 * @param inputStream Stream to read the byte from
	 * @return	short
	 * @throws IOException Error if read on inputStream failed
	 */
	public byte readByte() {
		byte result = this.buffer[iterator];
		iterator++;	
		
		return result;
	}

	/**
	 * Reads a Int8 from MessageBuffer
	 */
	public short  readInt8() {
		short result = ( short ) this.buffer[iterator];
		if ( result < 0 ) {
			result += 256;
		}
		iterator++;
		return result;
	}
	/**
	 * Reads an int16 from from MessageBuffer
	 
	 */
	public short readInt16(){		
		return ( short ) ( readInt8() + 256 * readInt8() );		
	}
	/**
	 * Reads an int32 from from MessageBuffer	 
	 */
	public  int readInt32() {
		return  ( readInt8() + 256
			  * ( readInt8() + 256
			  * ( readInt8() + 256
			  * ( readInt8() ) ) ) );
		
	}
	/**
	 * Reads a long from MessageBuffer
	 */
	public long readInt64(){
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
	 */
	public  String readString() {
		int stringLength = readInt16();		
		byte[] content = new byte[ stringLength ];				
		if ( stringLength > 0 ) {					
			String result = new String(buffer,iterator,stringLength);			
			this.iterator= this.iterator + stringLength;
			return result;
		} 
		else
			return ( "" );
			
		
	}
	
	
/*
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
 */
	
	/**
	 * Reads a String[] from MessageBuffer	
	 */
	public  String[] readStringList(){
		short listElem = readInt16();
		String[] result = new String[ listElem ];
		for ( int i = 0; i < listElem; i++ ) {
			result[ i ] = readString();
		}
		return result;
	}
	/**
	 * Creates a string from a binary message
	 */
	public  String readBinary(int length ){
		StringBuffer result = new StringBuffer();
		for ( int i = 0; i < length; i++ ) {
			result.append( Integer.toHexString(readByte() ) );
		}
		return result.toString();
	}
	
	/**
	 * Reads a int[] from MessageBuffer	
	 */
	public  int[] readInt32List() {
		short listElem = readInt16();
		int[] result = new int[ listElem ];
		for ( int i = 0; i < listElem; i++ ) {
			result[ i ] = readInt32(); 
		}
		return result;
	}
	

	/**
	 * @return
	 */
	public byte[] getBuffer() {
		return buffer;
	}

	/**
	 * @param bs
	 */
	public void setBuffer(byte[] bs) {
		iterator = 0;
		buffer = bs;
	}

}

/*
$Log: MessageBuffer.java,v $
Revision 1.2  2003/06/13 12:01:07  dek
bugFixed

Revision 1.1  2003/06/13 10:43:30  dek
introduced MessageBuffer for performance issues

*/