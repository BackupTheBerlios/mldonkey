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

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * FileInfoList
 *
 * @author markus
 * @version $Id: FileInfoIntMap.java,v 1.5 2003/07/03 16:01:51 lemmstercvs01 Exp $ 
 *
 */
public class FileInfoIntMap extends InfoIntMap {
	private int id;
	
	/**
	 * @param communication my parent
	 */
	public FileInfoIntMap( CoreCommunication communication ) {
		super( communication );
	}

	/**
	 * Generates a empty FileInfoList object
	 */
	public FileInfoIntMap() {
		super();
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
	}
	
	/**
	 * Update a specific element in the List
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		int id = messageBuffer.readInt32();
		if ( this.infoIntMap.containsKey( id ) )
			this.get( id ).update( messageBuffer );
	}
	
	/**
	 * Reads a single FileInfo object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void add( MessageBuffer messageBuffer ) {
		id = messageBuffer.readInt32();
		/* go 4bytes back in the MessageBuffer */
		messageBuffer.setIterator( messageBuffer.getIterator() - 4 );
		if ( this.infoIntMap.containsKey( id ) )
			this.get( id ).readStream( messageBuffer );
		else {
			FileInfo fileInfo = new FileInfo( this.parent );
			fileInfo.readStream( messageBuffer );
			this.put( fileInfo.getId(), fileInfo );
		}
	}
	
	/**
	 * Get a FileInfo object from this object by there id
	 * @param id The FileInfo id
	 * @return The FileInfo object
	 */
	public FileInfo get( int id ) {
		return ( FileInfo ) this.infoIntMap.get( id );
	}
	
	/**
	 * @return this objects id
	 */
	public int getId() {
		return id;
	}

}

/*
$Log: FileInfoIntMap.java,v $
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