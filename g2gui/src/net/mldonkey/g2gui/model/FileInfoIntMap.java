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
 * @author $Author: lemmster $
 * @version $Id: FileInfoIntMap.java,v 1.22 2003/08/22 21:03:15 lemmster Exp $ 
 *
 */
public class FileInfoIntMap extends InfoIntMap {
	/**
	 * id of last file changed in the map
	 */
	private List ids = new ArrayList();

	/**
	 * @param communication my parent
	 */
	public FileInfoIntMap( CoreCommunication communication ) {
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
			FileInfo fileInfo = new FileInfo( this.parent );
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
	public void update(MessageBuffer messageBuffer) {
		int id = messageBuffer.readInt32();
		if (this.infoIntMap.containsKey(id)) {
			this.get(id).update(messageBuffer);
			this.setChanged();
			this.notifyObservers( this );
		}
	}
	
	/**
	 * Reads a single FileInfo object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void add( MessageBuffer messageBuffer ) {
		synchronized ( this ) {
			int id = messageBuffer.readInt32();
			synchronized ( this.ids ) {
				this.ids.add( new Integer( id ) );
			}
		
			/* go 4bytes back in the MessageBuffer */
			messageBuffer.setIterator( messageBuffer.getIterator() - 4 );
			if ( this.infoIntMap.containsKey( id ) ) {
				FileInfo fileInfo = (FileInfo) this.get( id );
				EnumFileState oldFileState = (EnumFileState) fileInfo.getState().getState();
				fileInfo.readStream( messageBuffer );
				EnumFileState newFileState = (EnumFileState) fileInfo.getState().getState();
				
				// viewer.refresh
				if ((newFileState == EnumFileState.CANCELLED
					|| newFileState == EnumFileState.SHARED
					|| newFileState == EnumFileState.ABORTED
					|| newFileState == EnumFileState.NEW
					) && oldFileState != newFileState) {
					this.setChanged();
					this.notifyObservers();
				// viewer.update
				} else {
					this.setChanged();
					this.notifyObservers( this );
				}
									
			} else {
				FileInfo fileInfo = new FileInfo( this.parent );
				fileInfo.readStream( messageBuffer );
				this.put( fileInfo.getId(), fileInfo );
				this.setChanged();
				this.notifyObservers ( fileInfo );
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
	 * A list of fileinfo ids, which changed since last update.
	 * Use clearIds() after update
	 * Need manual refresh() of the tableviewer
	 * @return The fileinfo id which changed last
	 */
	public List getIds() {
		return ids;
	}
	
	/**
	 * Removes all entries in ids
	 */
	public void clearIds() {
		synchronized ( this.ids ) {
			this.ids.clear();
		}
	}

	/**
	 * Adds a new download to this map
	 * @param url The url of the new download
	 */
	public void add( String url ) {
		EncodeMessage dllink =
			new EncodeMessage( Message.S_DLLINK, url );
		dllink.sendMessage( this.parent.getConnection() );
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
				if ( aFileInfo.getState().getState() == EnumFileState.SHARED
					|| aFileInfo.getState().getState() == EnumFileState.CANCELLED )
				{
					temp.add( new Integer( itr.key() ) );
				}
			}
			Iterator itr2 = temp.iterator();
			while ( itr2.hasNext() ) {
				this.infoIntMap.remove( ( ( Integer ) itr2.next() ).intValue() );
			}
		}
		this.setChanged();
		this.notifyObservers();
	}
}

/*
$Log: FileInfoIntMap.java,v $
Revision 1.22  2003/08/22 21:03:15  lemmster
replace $user$ with $Author$

Revision 1.21  2003/08/15 22:05:58  zet
*** empty log message ***

Revision 1.20  2003/08/11 00:30:10  zet
show queued files

Revision 1.19  2003/08/04 19:21:52  zet
trial tabletreeviewer

Revision 1.18  2003/08/01 17:21:19  lemmstercvs01
reworked observer/observable design, added multiversion support

Revision 1.17  2003/07/30 19:27:01  lemmstercvs01
ids.removeAll( ids ) -> ids.clear()

Revision 1.16  2003/07/07 15:08:39  lemmstercvs01
removeObsolete() finally fixed

Revision 1.15  2003/07/06 07:45:26  lemmstercvs01
checkstyle applied

Revision 1.14  2003/07/05 15:43:48  lemmstercvs01
javadoc improved

Revision 1.13  2003/07/05 15:38:20  lemmstercvs01
debug removed

Revision 1.12  2003/07/05 11:33:08  lemmstercvs01
id -> List ids

Revision 1.10  2003/07/04 13:29:15  lemmstercvs01
removeObsolete() fixed

Revision 1.9  2003/07/04 12:28:46  dek
checkstyle

Revision 1.8  2003/07/04 12:15:17  lemmstercvs01
removeObsolete renamed/modified

Revision 1.7  2003/07/04 11:04:14  lemmstercvs01
add some opcodes

Revision 1.6  2003/07/03 19:21:11  lemmstercvs01
javadoc added

Revision 1.5  2003/07/03 16:01:51  lemmstercvs01
setState() works now to set the filestate on the mldonkey side

Revision 1.4  2003/07/02 16:26:51  dek
minor checkstyle

Revision 1.3  2003/06/27 17:15:16  lemmstercvs01
added private int id for easier table update

Revision 1.2  2003/06/20 15:15:22  dek
humm, some interface-changes, hope, it didn't break anything ;-)

Revision 1.1  2003/06/16 21:47:19  lemmstercvs01
just refactored (name changed)

Revision 1.11  2003/06/16 12:13:26  lemmstercvs01
opcode 52 added

Revision 1.10  2003/06/15 20:37:23  lemmstercvs01
fixed a bug in a bugfix ;)

Revision 1.9  2003/06/15 20:15:50  lemmstercvs01
added a workaround in update() for a core bug

Revision 1.8  2003/06/15 16:18:41  lemmstercvs01
new interface introduced

Revision 1.7  2003/06/14 23:04:08  lemmstercvs01
change from interface to abstract superclass

Revision 1.6  2003/06/14 19:30:41  lemmstercvs01
interface added

Revision 1.5  2003/06/14 17:41:03  lemmstercvs01
foobar

Revision 1.4  2003/06/14 12:47:40  lemmstercvs01
update() added

Revision 1.3  2003/06/13 11:03:41  lemmstercvs01
changed InputStream to MessageBuffer

Revision 1.2  2003/06/12 22:23:06  lemmstercvs01
lots of changes

Revision 1.1  2003/06/12 18:16:20  lemmstercvs01
initial commit

*/