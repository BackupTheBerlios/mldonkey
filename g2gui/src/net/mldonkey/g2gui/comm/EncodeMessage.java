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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * EncodeMessage
 *
 * @version $Id: EncodeMessage.java,v 1.8 2003/09/18 03:59:37 zet Exp $ 
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
	private ByteArrayOutputStream content;
	
	/**
	 * Generates a new message object
	 * @param opCode the opcode for the message
	 * @param content an object array with the message content
	 */
	public EncodeMessage( short opCode, Object[] content ) {
		this.opCode = opCode;
		this.length = 2;
		this.content = new ByteArrayOutputStream();
	
		if ( content != null ) {
		   createContent( content );
		   this.length += this.content.size();
		}
	}
	
	/**
	 * Generates a new message object
	 * @param opcode the opcode for the message
	 * @param content an object as message content
	 */
	public EncodeMessage( short opCode, Object content ) {
		this( opCode, new Object[]{ content } );
	}
	
	/**
	 * Generates a new message object without content
	 * @param opCode the opcode for the message
	 */
	public EncodeMessage( short opCode ) {
		this( opCode, null );
	}
	
	/**
	 * Reads the message into the socket
	 * @param connection a socket connection
	 * @return boolean true/false 
	 */
	public boolean sendMessage( Socket connection ) {
		try {
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream( connection.getOutputStream() );
			
			// messageHeader: (int) messageLength (short) opCode 
			ByteBuffer messageHeader = ByteBuffer.allocate( 6 );
			messageHeader.order( ByteOrder.LITTLE_ENDIAN ); 
			messageHeader.putInt( this.length  ).putShort( this.opCode );
			
			// write the message to the stream
			bufferedOutputStream.write( messageHeader.array() );
			if ( this.content.size() > 0) {
				bufferedOutputStream.write( this.content.toByteArray() );
				this.content.reset();
			}
			bufferedOutputStream.flush();
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
	private void createContent( Object[] content ) {
		
		// Cycle through content array
		for ( int i = 0; i < content.length; i++ ) {
			// If content object is an array, cycle through it
			if ( content[ i ].getClass().isArray() ) {
				Object[] objectArray = (Object[]) content[ i ];
				
				// Append the (short) array length to the message
				appendNumber( new Short( ( short ) objectArray.length ) );
			
				// Append the array contents to the message
				for ( int j = 0; j < objectArray.length; j++ ) {
					appendObject( objectArray[ j ] );	
				}	
			// Append the content to the message
			} else {	
				appendObject( content[ i ] );	
			}		
		}	
	}
	
	/**
	 * @param object
	 * 
	 * Append an object to the content
	 */
	public void appendObject( Object object ) {
	
		if ( object instanceof Byte ) {
			this.content.write( ( ( Byte ) object ).byteValue() );
		}
		else if ( object instanceof Short 
				|| object instanceof Integer
				|| object instanceof Long ) {
		
			appendNumber( object );
		
		}
		else if ( object instanceof String ) {
			String string = (String) object;
			
			// Append the (short) length of the string, and then the string
			appendNumber( new Short( (short) string.length() ) );
			this.content.write( string.getBytes(), 0, string.length() );
		}
	}

	/**
	 * @param object
	 * 
	 * Append a number to the content in LITTLE_ENDIAN 
	 * must be one of: Short, Integer, or Long
	 */
	public void appendNumber( Object object ) { 
		
		ByteBuffer byteBuffer;
		
		if ( object instanceof Short ) {
				byteBuffer = ByteBuffer.allocate( 2 );
				byteBuffer.order( ByteOrder.LITTLE_ENDIAN ); 
			 	byteBuffer.putShort( (( Short ) object).shortValue() );
		} else if ( object instanceof Integer ) {
				byteBuffer = ByteBuffer.allocate( 4 );
				byteBuffer.order( ByteOrder.LITTLE_ENDIAN ); 
				byteBuffer.putInt( (( Integer ) object).intValue() );
		} else {
				byteBuffer = ByteBuffer.allocate( 8 );
				byteBuffer.order( ByteOrder.LITTLE_ENDIAN ); 
				byteBuffer.putLong( (( Long ) object).longValue() );
		}
		
		this.content.write( byteBuffer.array(), 0, byteBuffer.capacity() );
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
Revision 1.8  2003/09/18 03:59:37  zet
rewrite to use ByteBuffer -- easier to read, and fixes sending of StringLists

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