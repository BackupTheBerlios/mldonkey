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

import java.io.ByteArrayOutputStream;


/**
 * EncodeMessage
 *
 * @version $Id: EncodeMessage.java,v 1.13 2003/11/05 00:09:50 zet Exp $
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
     * The message header
     */
    private byte[] header;

    /**
     * The message content
     */
    private byte[] content;

    /**
     * A dynamically sized byteArray
     */
    private ByteArrayOutputStream byteArray;

    /**
     * Generates a new message object
     * @param opCode the opcode for the message
     * @param content an object array with the message content
     */
    public EncodeMessage(short opCode, Object[] content) {
        this.opCode = opCode;
        this.length = 2;
        this.byteArray = new ByteArrayOutputStream();

        if (content != null) {
            this.content = createContent(content);
            this.length += this.content.length;
        }

        this.header = createHeader();
    }

    /**
     * Generates a new message object
     * @param opCode the opcode for the message
     * @param content an object as message content
     */
    public EncodeMessage(short opCode, Object content) {
        this(opCode, new Object[] { content });
    }

    /**
     * Generates a new message object without content
     * @param opCode the opcode for the message
     */
    public EncodeMessage(short opCode) {
        this(opCode, null);
    }

    /**
     * Reads the message into the socket
     * @param connection a socket connection
     */
    public void sendMessage(CoreCommunication core) {
        core.sendMessage(this.header, this.content);
        this.byteArray.reset();
    }

    /**
     * @return byte[]
     *
     * Create and return the header based on the content size
     */
    private byte[] createHeader() {
        byte[] byteBuffer = new byte[ 6 ];

        byteBuffer = toBytes(new Integer(this.length), byteBuffer, 0);
        byteBuffer = toBytes(new Short(this.opCode), byteBuffer, 4);

        return byteBuffer;
    }

    /**
     * Creates the message payload from a given object array
     * @param content object array which represense the payload
     * @return a byte array
     */
    private byte[] createContent(Object[] content) {
        /* Cycle through content array */
        for (int i = 0; i < content.length; i++) {
            /* Write raw byteArray */
            if (content[ i ] instanceof byte[]) {
                byte[] byteArray = (byte[]) content[i];
				this.byteArray.write(byteArray, 0, byteArray.length);
            }
            /* If content object is an array, cycle through it */
            else if (content[ i ].getClass().isArray()) {
                Object[] objectArray = (Object[]) content[ i ];

                /* Append the (short) array length to the message */
                appendNumber(new Short((short) objectArray.length));

                /* Append the array contents to the message */
                for (int j = 0; j < objectArray.length; j++) {
                    appendObject(objectArray[ j ]);
                }
            }
            /* Append the content to the message */
            else {
                appendObject(content[ i ]);
            }
        }

        return byteArray.toByteArray();
    }

    /**
     * @param object
     *
     * Append an object to the content
     */
    private void appendObject(Object object) {
        if (object instanceof Byte) {
            this.byteArray.write(((Byte) object).byteValue());
        } else if (object instanceof Number) {
            appendNumber((Number) object);
        } else if (object instanceof String) {
            String string = (String) object;

            /* Append the (short) length of the string, and then the string */
            appendNumber(new Short((short) string.length()));
            this.byteArray.write(string.getBytes(), 0, string.length());
        }
    }

    /**
     * @param number
     *
     * Append a number to the content in LITTLE_ENDIAN
     * must be one of: Short, Integer, or Long
     */
    private void appendNumber(Number number) {
        byte[] byteBuffer;

        if (number instanceof Short) {
            byteBuffer = new byte[ 2 ];
            byteBuffer = toBytes((Short) number, byteBuffer, 0);
        } else if (number instanceof Integer) {
            byteBuffer = new byte[ 4 ];
            byteBuffer = toBytes((Integer) number, byteBuffer, 0);
        } else {
            byteBuffer = new byte[ 8 ];
            byteBuffer = toBytes((Long) number, byteBuffer, 0);
        }

        this.byteArray.write(byteBuffer, 0, byteBuffer.length);
    }

    // for gcj
    private static byte[] toBytes(Short aShort, byte[] byteBuffer, int offset) {
        short value = aShort.shortValue();

        for (int j = 0; j < 2; j++) {
            byteBuffer[ j + offset ] = (byte) (value % 256);
            value = (short) (value / 256);
        }

        return byteBuffer;
    }

    private static byte[] toBytes(Integer i, byte[] byteBuffer, int offset) {
        int anInt = i.intValue();
        byteBuffer[ 0 + offset ] = (byte) (anInt & 0xFF);
        byteBuffer[ 1 + offset ] = (byte) ((anInt & 0xFFFF) >> 8);
        byteBuffer[ 2 + offset ] = (byte) ((anInt & 0xFFFFFF) >> 16);
        byteBuffer[ 3 + offset ] = (byte) ((anInt & 0x7FFFFFFF) >> 24);

        return byteBuffer;
    }

    private static byte[] toBytes(Long aLong, byte[] byteBuffer, int offset) {
        long temp = aLong.longValue();

        for (int j = 0; j < 8; j++) {
            byteBuffer[ j + offset ] = (byte) (temp % 256);
            temp = temp / 256;
        }

        return byteBuffer;
    }

    /**
     * @return header
     */
    public byte[] getHeader() {
        return header;
    }

    /**
     * @return content
     */
    public byte[] getContent() {
        return content;
    }
}


/*
$Log: EncodeMessage.java,v $
Revision 1.13  2003/11/05 00:09:50  zet
write raw byte arrays

Revision 1.12  2003/09/18 23:24:07  zet
use bufferedinputstream
& mods for the annoying gcj project

Revision 1.11  2003/09/18 15:29:25  zet
centralize writeStream in core
handle IOException rather than throwing it away

Revision 1.10  2003/09/18 08:56:27  lemmster
checkstyle

Revision 1.9  2003/09/18 04:41:19  zet
use Number

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
