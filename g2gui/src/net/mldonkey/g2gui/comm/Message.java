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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Message
 *
 * @version $Id: Message.java,v 1.43 2004/04/14 09:49:57 dek Exp $
 *
 */
public abstract class Message {
    /**
     * Send CoreProtocol (value is 0)
     */
    public static final short S_COREPROTOCOL = 0;

    /**
     * Send Connect More (value is 1)
     */
    public static final short S_CONNECT_MORE = 1;

    /**
     * Clean Old (value is 2)
     */
    public static final short S_CLEAN_OLD = 2;

    /**
     * Send Kill Core (value is 3)
     */
    public static final short S_KILL_CORE = 3;

    /**
     * Send Extend Search (value is 4)
     */
    public static final short S_EXTEND_SEARCH = 4;

    /**
     * Send Download Link (value is 8)
     */
    public static final short S_DLLINK = 8;

    /**
     * Send Remove Server (value is 9)
     */
    public static final short S_REMOVE_SERVER = 9;

    /**
     * Send Save Option (value is 10)
     */
    public static final short S_SAVE_OPTION = 10;

    /**
     * Send Remove Download (value is 11)
     */
    public static final short S_REMOVE_DOWNLOAD = 11;

    /**
     * Send Get Server Users (value is 12)
     */
    public static final short S_GET_SERVER_USERS = 12;

    /**
     * Send Save File As (value is 13)
     */
    public static final short S_SAVE_FILE_AS = 13;

    /**
     * Send Add Client Friend (value is 14)
     */
    public static final short S_ADD_CLIENT_FRIEND = 14;

    /**
     * Send Add User Friend (value is 15)
     */
    public static final short S_ADD_USER_FRIEND = 15;

    /**
     * Send Remove Friend (value is 16)
     */
    public static final short S_REMOVE_FRIEND = 16;

    /**
     * Send Remove All Friends (value is 17)
     */
    public static final short S_REMOVE_ALL_FRIENDS = 17;

    /**
     * Send Connect Server (value is 21)
     */
    public static final short S_CONNECT_SERVER = 21;

    /**
     * Send Disconnect Server (value is 22)
     */
    public static final short S_DISCONNECT_SERVER = 22;

    /**
     * Send Switch Download (value is 23)
     */
    public static final short S_SWITCH_DOWNLOAD = 23;

    /**
     * Send Verify all Chunks (value is 24)
     */
    public static final short S_VERIFY_ALL_CHUNKS = 24;

    /**
     * Send Close Search (value is 27)
     * The difference between this and 53 is:
     * no boolean for forget
     */
    public static final short S_OLD_CLOSE_SEARCH = 27;

    /**
     * Send SetOption (value is 28)
     */
    public static final short S_SET_OPTION = 28;

    /**
     * Send Console Message (value is 29)
     */
    public static final short S_CONSOLEMSG = 29;
    
    /**
     * Send Preview (value is 30)
     */
    public static final short S_PREVIEW = 30;

    /**
     * Connect Friend (value is 31)
     */
    public static final short S_CONNECT_FRIEND = 31;
    
    /**
     * Get Client Info (value is 34)
     */
    public static final short S_GET_FILE_LOCATIONS = 34;

    /**
     * Get Client Info (value is 36)
     */
    public static final short S_GET_CLIENT_INFO = 36;
    
    /**
     * Get File Info (value is 37)
     */
    public static final short S_GET_FILE_INFO = 37;

    /**
     * Send Enable Network (value is 40)
     */
    public static final short S_ENABLE_NETWORK = 40;

    /**
     * Send Search Query (value is 42)
     */
    public static final short S_SEARCH_QUERY = 42;

    /**
     * Send Message To Client (value is 43)
     */
    public static final short S_MESSAGE_TO_CLIENT = 43;

    /**
     * Get Connected Servers (value is 44)
     */
    public static final short S_GET_CONNECTED_SERVERS = 44;

    /**
     * Send Get Downloading Files (value is 45)
     */
    public static final short S_GET_DOWNLOADING_FILES = 45;

    /**
     * Send Get Downloaded Files (value is 46)
     */
    public static final short S_GET_DOWNLOADED_FILES = 46;

    /**
     * Send GuiExtension (value is 47)
     */
    public static final short S_GUIEXTENSION = 47;

    /**
     * Send Refresh upload Stats (value is 49)
     */
    public static final short S_REFRESH_UPLOAD_STATS = 49;

    /**
     * Sends "add" Download
     */
    public static final short S_DOWNLOAD = 50;

    /**
     * Send Set File Priority (value is 51)
     */
    public static final short S_SET_FILE_PRIO = 51;

    /**
     * Send Password (value is 52)
     */
    public static final short S_PASSWORD = 52;

    /**
     * Send Close Search (value is 53)
     */
    public static final short S_CLOSE_SEARCH = 53;

    /**
     * Send Add Server (value is 54)
     */
    public static final short S_ADD_SERVER = 54;

    /**
     * Send Message Version (value is 55)
     */
    public static final short S_MESSAGE_VERSION = 55;
    
    /**
     * Rename File (value is 56)
     */
    public static final short S_RENAME_FILE = 56;
    
    /**
     * Receive Core Protocol (value is 0)
     */
    public static final short R_COREPROTOCOL = 0;

    /**
     * Receive Options Info (value is 1)
     */
    public static final short R_OPTIONS_INFO = 1;

    /**
     * Receive Define Search (value is 3)
     */
    public static final short R_DEFINE_SEARCH = 3;

    /**
     * Receive Result Info (value is 4)
     */
    public static final short R_RESULT_INFO = 4;

    /**
     * Recive Search Result (value is 5)
     */
    public static final short R_SEARCH_RESULT = 5;

    /**
     * Receive File Update Availability (value is 9)
     */
    public static final short R_FILE_UPDATE_AVAILABILITY = 9;

    /**
     * Receive File Add Source (value is 10)
     */
    public static final short R_FILE_ADD_SOURCE = 10;

    /**
     * Receive Server State (value is 13)
     */
    public static final short R_SERVER_STATE = 13;

    /**
     * Receive Client Info (value is 15)
     */
    public static final short R_CLIENT_INFO = 15;

    /**
     * Receive Client State (value is 16)
     */
    public static final short R_CLIENT_STATE = 16;

    /**
     * Receive Console (value is 19)
     */
    public static final short R_CONSOLE = 19;

    /**
     * Receive Network Info (value is 20)
     */
    public static final short R_NETWORK_INFO = 20;

    /**
     * Receive User Info (value is 21)
     */
    public static final short R_USER_INFO = 21;

    /**
     * Receive Server Info (value is 26)
     */
    public static final short R_SERVER_INFO = 26;

    /**
     * Receive Message From Client (value is 27)
     */
    public static final short R_MESSAGE_FROM_CLIENT = 27;

    /**
     * Receive RoomInfo (value is 31)
     */
    public static final short R_ROOM_INFO = 31;

    /**
     * Receive a Shared File Upload Info (value is 34):
     */
    public static final short R_SHARED_FILE_UPLOAD = 34;

    /**
     * Receive Add Section Option (value is 36)
     */
    public static final short R_ADD_SECTION_OPTION = 36;

    /**
     * Receive Add Plugin Option (value is 38)
     */
    public static final short R_ADD_PLUGIN_OPTION = 38;

    /**
     * Receive File Download Update (value is 46)
     */
    public static final short R_FILE_DOWNLOAD_UPDATE = 46;

    /**
     * Receive Bad Password (value is 47)
     */
    public static final short R_BAD_PASSWORD = 47;

    /**
     * Receive Shared File Info (value is 48)
     */
    public static final short R_SHARED_FILE_INFO = 48;

    /**
     * Receive Client Stats (value is 49)
     */
    public static final short R_CLIENT_STATS = 49;

    /**
     * Receive File Remove Source (value is 50)
     */
    public static final short R_FILE_REMOVE_SOURCE = 50;

    /**
     * Receive Clean Table (value is 51)
     */
    public static final short R_CLEAN_TABLE = 51;

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
	 * @param Socket socket
	 * @param byte[] messageHeader
	 * @param byte[] messageContent
	 * @throws IOException
	 * 
	 * Write a message (with optional content) to the socket
	 */
	public static void writeStream ( Socket socket, byte[] messageHeader, byte[] messageContent ) throws IOException {
		
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream( socket.getOutputStream() );
	
		bufferedOutputStream.write( messageHeader );
		if ( messageContent != null ) {
			bufferedOutputStream.write( messageContent );
		}
		bufferedOutputStream.flush();
		
	}

    /**
     * @param inputStream
     * @param length
     * @return byte[]
     * @throws IOException
     *
     * Read the stream, return byte[] of length.  Create an IOException on failure.
     */
    public static byte[] readStream( BufferedInputStream inputStream, int length ) throws IOException {
        byte[] b = new byte[ length ];
        int result;
        int pos = 0;
        while ( pos < length ) {
            result = inputStream.read( b, pos, length - pos );
            if ( result <= 0 )
                /* we disconnect, or... */
                throw new IOException();
            pos += result;
        }
        return b;
    }

    /**
     * @param inputStream
     * @return
     * @throws IOException
     * Read an in32 from InputStream
     */
    public static int readInt32( BufferedInputStream inputStream ) throws IOException {
        byte[] b = readStream( inputStream, 4 );
        
        return ( ( ( int ) b[ 0 ] ) & 0xFF )
        	+ ( ( ( ( int ) b[ 1 ] ) & 0xFF ) << 8 )
            + ( ( ( ( int ) b[ 2 ] ) & 0xFF ) << 16 )
            + ( ( ( ( int ) b[ 3 ] ) & 0xFF ) << 24 );
    }

    /**
     * Sends a message to the core
     * @param CoreCommunication to work with
     */
    public abstract void sendMessage( CoreCommunication core );
}

/*
$Log: Message.java,v $
Revision 1.43  2004/04/14 09:49:57  dek
best fit was _not_ stored , when unselected (no best sort anymore...) which is done now

removed some deprecated imports as well, some minor stuff

Revision 1.42  2003/12/04 08:47:31  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.41  2003/11/28 13:11:10  zet
support patch 2372

Revision 1.40  2003/11/26 07:41:43  zet
protocolVersion 19/timer

Revision 1.39  2003/09/23 00:10:44  zet
add Preview

Revision 1.38  2003/09/18 23:24:07  zet
use bufferedinputstream 
& mods for the annoying gcj project

Revision 1.37  2003/09/18 15:29:25  zet
centralize writeStream in core
handle IOException rather than throwing it away

Revision 1.36  2003/09/18 08:56:27  lemmy
checkstyle

Revision 1.35  2003/09/18 03:59:37  zet
rewrite to use ByteBuffer -- easier to read, and fixes sending of StringLists

Revision 1.34  2003/09/17 13:49:09  dek
now the gui refreshes the upload-stats, add and observer to SharedFileInfoIntMap
to get notice of changes in # of requests and # of uploaded bytes

Revision 1.33  2003/09/16 01:18:31  zet
try to handle socket disconnection in a central location

Revision 1.32  2003/09/02 09:24:36  lemmy
checkstyle

Revision 1.31  2003/08/24 16:54:38  dek
RoomInfo is now read from stream to Map, ready for use to implement
all the room-stuff

Revision 1.30  2003/08/23 15:21:37  zet
remove @author

Revision 1.29  2003/08/22 21:03:15  lemmy
replace $user$ with $Author: dek $

Revision 1.28  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging

Revision 1.27  2003/08/10 23:20:41  zet
signed ints

Revision 1.26  2003/08/02 09:32:07  lemmy
javadoc fixed

Revision 1.25  2003/08/01 13:46:24  lemmy
opcodes added

Revision 1.24  2003/07/23 17:01:55  lemmy
added S_DOWNLOAD (opcode 50)

Revision 1.23  2003/07/04 11:04:24  lemmy
added some opcodes

Revision 1.22  2003/07/03 21:13:04  lemmy
some opcodes added

Revision 1.21  2003/06/29 17:38:49  lemmy
opcodes added, "bad password" fixed

Revision 1.20  2003/06/21 13:20:36  dek
work on optiontree continued - one can already change client_name in General-leaf

Revision 1.19  2003/06/16 21:47:41  lemmy
opcode 3 added

Revision 1.18  2003/06/16 20:08:38  lemmy
opcode 13 added

Revision 1.17  2003/06/16 18:43:05  dek
opcode 51 added

Revision 1.16  2003/06/15 19:37:01  lemmy
fixed a bug in readInt32()

Revision 1.15  2003/06/15 16:18:58  lemmy
some opcodes added

Revision 1.14  2003/06/15 09:58:30  lemmy
some opcodes added

Revision 1.13  2003/06/14 19:30:50  lemmy
some opcodes added

Revision 1.12  2003/06/14 17:41:19  lemmy
added some opcodes

Revision 1.11  2003/06/14 12:48:57  lemmy
checkstyle applied, added a opcode, removed abstract methods

Revision 1.10  2003/06/13 11:23:24  lemmy
modified readInt32, deleted all other readXX()

Revision 1.9  2003/06/13 08:47:41  lemmy
changed String to StringBuffer in readBinary()

Revision 1.8  2003/06/12 18:14:41  lemmy
some abstract methodes added

Revision 1.7  2003/06/12 10:45:44  lemmy
R_BAD_PASSWORD added

Revision 1.6  2003/06/12 10:40:06  dek
readByte() --> readInt8 | readByte returns now: byte

Revision 1.5  2003/06/11 23:25:49  dek
fixed readInt32 /readInt64

Revision 1.4  2003/06/11 20:43:45  lemmy
setMd4() fixed

Revision 1.3  2003/06/11 16:41:19  lemmy
still a problem in setMd4()

Revision 1.2  2003/06/11 15:32:33  lemmy
still in progress

Revision 1.1  2003/06/11 12:56:10  lemmy
moved from model -> comm

Revision 1.4  2003/06/10 16:22:19  lemmy
create a class hierarchy for message

*/
