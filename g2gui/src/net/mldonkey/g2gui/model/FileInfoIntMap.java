/*
 * Copyright 2003
 * G2Gui Team
 *
 *
 * This file is part of G2Gui.
 *
 * G2Gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2Gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2Gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package net.mldonkey.g2gui.model;

import gnu.trove.TIntObjectIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.model.enum.EnumFileState;

/**
 * FileInfoList
 *
 *
 * @version $Id: FileInfoIntMap.java,v 1.34 2004/03/16 17:08:44 dek Exp $
 *
 */
public class FileInfoIntMap extends InfoIntMap {
    /**
     * @param communication my parent
     */
    FileInfoIntMap( CoreCommunication communication ) {
        super( communication );
    }

    /**
     * Store a key/value pair in this object
     * @param key The Key
     * @param value The FileInfo object
     */
    public void put( int key, FileInfo value ) {
        this.infoIntMap.put( key, value );
    }

    /**
     * true/false if this contains the object
     * @param key The key to the object
     * @return boolean
     */
    public boolean contains( int key ) {
        return this.infoIntMap.contains( key );
    }

    /**
     * Reads a List of FileInfo objects from a MessageBuffer
     * @param messageBuffer The MessageBuffer to read from
     */
    public void readStream( MessageBuffer messageBuffer ) {
        /* get the element count from the MessageBuffer */
        short listElem = messageBuffer.readInt16();

        /* clear the list */
        this.infoIntMap.clear();
        /* insert the new FileInfo objects */
        for ( int i = 0; i < listElem; i++ ) {
            FileInfo fileInfo = parent.getModelFactory().getFileInfo();
            fileInfo.readStream( messageBuffer );
            this.put( fileInfo.getId(), fileInfo );
        }
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Update a specific element in the List
     * @param messageBuffer The MessageBuffer to read from
     */
    public void update( MessageBuffer messageBuffer ) {
        int id = messageBuffer.readInt32();
        if ( this.infoIntMap.containsKey( id ) )
            this.get( id ).update( messageBuffer );
        this.setChanged();
        this.notifyObservers( this );
    }

    /**
     * Reads a single FileInfo object from a MessageBuffer
     * @param messageBuffer The MessageBuffer to read from
     */
    public void add( MessageBuffer messageBuffer ) {
        synchronized ( this ) {
            int id = messageBuffer.readInt32();
            this.setChanged();
            this.notifyObservers( this );

            /* go 4bytes back in the MessageBuffer */
            messageBuffer.setIterator( messageBuffer.getIterator() - 4 );
            if ( this.infoIntMap.containsKey( id ) ) {
                FileInfo fileInfo = this.get( id );
                fileInfo.readStream( messageBuffer );
            }
            else {
                FileInfo fileInfo = parent.getModelFactory().getFileInfo();
                fileInfo.readStream( messageBuffer );
                this.put( fileInfo.getId(), fileInfo );
                this.setChanged();
                this.notifyObservers( fileInfo );
            }
        }
    }

    /**
     * Get a FileInfo object from this object by id
     * @param id The FileInfo id
     * @return The FileInfo object
     */
    public FileInfo get( int id ) {
        return ( FileInfo ) this.infoIntMap.get( id );
    }

    /**
     * Adds a new download to this map
     * @param url The url of the new download
     */
    public void add( String url ) {
        Message dllink = new EncodeMessage( Message.S_DLLINK, url );
        dllink.sendMessage( this.parent );
        dllink = null;
    }

    /**
     * Change the of a fileinfo to cancelled
     * @param key The fileinfo id to change
     */
    public void remove( int key ) {
        this.get( key ).setState( EnumFileState.CANCELLED );
        this.setChanged();
        this.notifyObservers( this );
    }
    
    /**
     * Commit all
     */
    public void commitAll() {
		TIntObjectIterator itr = this.iterator();
		   int collsize = this.size();
		   for ( ; collsize-- > 0;) {
			   itr.advance();
			   FileInfo aFileInfo = ( FileInfo ) itr.value();
				if (aFileInfo.getState().getState() == EnumFileState.DOWNLOADED) {
					aFileInfo.saveFileAs( aFileInfo.getName() );
				}
		   }
    }

    /**
     * Removes all compelte and canceled downloads from this map
     * (EnumFileState == DOWNLOADED || EnumFileState == CANCELED )
     * Needs manual refresh() of the tableviewer.
     */
    public void removeObsolete() {
        synchronized ( this ) {
            List temp = new ArrayList();
            TIntObjectIterator itr = this.iterator();
            int collsize = this.size();
            for ( ; collsize-- > 0;) {
                itr.advance();
                FileInfo aFileInfo = ( FileInfo ) itr.value();

                /* if EnumFileState.DOWNLOADED, remove the fileinfo from this */
                if ( ( aFileInfo.getState().getState() == EnumFileState.SHARED )
                         || ( aFileInfo.getState().getState() == EnumFileState.CANCELLED ) )
                    temp.add( new Integer( itr.key() ) );
            }
            Iterator itr2 = temp.iterator();
            while ( itr2.hasNext() )
                this.infoIntMap.remove( ( ( Integer ) itr2.next() ).intValue() );
        }
        this.setChanged();
        this.notifyObservers();
    }

	/**
	 * processes a File_Remove_From_source opCode
	 * @param messageBuffer
	 */
	public void fileRemoveSource(MessageBuffer messageBuffer) {
		int fileId = messageBuffer.readInt32();
		int clientId = messageBuffer.readInt32();

		ClientInfoIntMap clientMap = parent.getClientInfoIntMap();

		if (this.infoIntMap.contains(fileId) && clientMap.containsKey(clientId)) {
			ClientInfo client = clientMap.get(clientId);
			FileInfo file = get(fileId);
			file.removeClientInfo(client);
			this.setChanged();
			this.notifyObservers();
		}

	}
    
}

/*
$Log: FileInfoIntMap.java,v $
Revision 1.34  2004/03/16 17:08:44  dek
implemented opCode 50

Revision 1.33  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.32  2003/12/01 14:22:17  lemmy
ProtocolVersion handling completely rewritten

Revision 1.31  2003/11/23 17:58:03  lemmy
removed dead/unused code

Revision 1.30  2003/10/16 19:57:43  zet
commitAll

Revision 1.29  2003/10/12 15:55:28  zet
remove clean

Revision 1.28  2003/09/25 00:51:03  zet
reset active sources on clean_tables

Revision 1.27  2003/09/18 15:29:25  zet
centralize writeStream in core
handle IOException rather than throwing it away

Revision 1.26  2003/09/18 09:16:47  lemmy
checkstyle

Revision 1.25  2003/08/23 15:21:37  zet
remove @author

Revision 1.24  2003/08/23 10:02:02  lemmy
use supertype where possible

Revision 1.23  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.22  2003/08/22 21:03:15  lemmy
replace $user$ with $Author: dek $

Revision 1.21  2003/08/15 22:05:58  zet
*** empty log message ***

Revision 1.20  2003/08/11 00:30:10  zet
show queued files

Revision 1.19  2003/08/04 19:21:52  zet
trial tabletreeviewer

Revision 1.18  2003/08/01 17:21:19  lemmy
reworked observer/observable design, added multiversion support

Revision 1.17  2003/07/30 19:27:01  lemmy
ids.removeAll( ids ) -> ids.clear()

Revision 1.16  2003/07/07 15:08:39  lemmy
removeObsolete() finally fixed

Revision 1.15  2003/07/06 07:45:26  lemmy
checkstyle applied

Revision 1.14  2003/07/05 15:43:48  lemmy
javadoc improved

Revision 1.13  2003/07/05 15:38:20  lemmy
debug removed

Revision 1.12  2003/07/05 11:33:08  lemmy
id -> List ids

Revision 1.10  2003/07/04 13:29:15  lemmy
removeObsolete() fixed

Revision 1.9  2003/07/04 12:28:46  dek
checkstyle

Revision 1.8  2003/07/04 12:15:17  lemmy
removeObsolete renamed/modified

Revision 1.7  2003/07/04 11:04:14  lemmy
add some opcodes

Revision 1.6  2003/07/03 19:21:11  lemmy
javadoc added

Revision 1.5  2003/07/03 16:01:51  lemmy
setState() works now to set the filestate on the mldonkey side

Revision 1.4  2003/07/02 16:26:51  dek
minor checkstyle

Revision 1.3  2003/06/27 17:15:16  lemmy
added private int id for easier table update

Revision 1.2  2003/06/20 15:15:22  dek
humm, some interface-changes, hope, it didn't break anything ;-)

Revision 1.1  2003/06/16 21:47:19  lemmy
just refactored (name changed)

Revision 1.11  2003/06/16 12:13:26  lemmy
opcode 52 added

Revision 1.10  2003/06/15 20:37:23  lemmy
fixed a bug in a bugfix ;)

Revision 1.9  2003/06/15 20:15:50  lemmy
added a workaround in update() for a core bug

Revision 1.8  2003/06/15 16:18:41  lemmy
new interface introduced

Revision 1.7  2003/06/14 23:04:08  lemmy
change from interface to abstract superclass

Revision 1.6  2003/06/14 19:30:41  lemmy
interface added

Revision 1.5  2003/06/14 17:41:03  lemmy
foobar

Revision 1.4  2003/06/14 12:47:40  lemmy
update() added

Revision 1.3  2003/06/13 11:03:41  lemmy
changed InputStream to MessageBuffer

Revision 1.2  2003/06/12 22:23:06  lemmy
lots of changes

Revision 1.1  2003/06/12 18:16:20  lemmy
initial commit

*/
