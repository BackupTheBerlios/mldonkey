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

import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;

import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * FileInfoList
 *
 * @author markus
 * @version $Id: FileInfoList.java,v 1.6 2003/06/14 19:30:41 lemmstercvs01 Exp $ 
 *
 */
public class FileInfoList implements Information, InfoList {
	/**
	 * 
	 */
	private TIntObjectHashMap fileInfoList;
	
	/**
	 * Generates a empty FileInfoList object
	 */
	public FileInfoList() {
		this.fileInfoList = new TIntObjectHashMap();
	}
	
	/**
	 * Store a key/value pair in this object
	 * @param key The Key
	 * @param value The FileInfo object
	 */
	public void put( int key, FileInfo value ) {
		this.fileInfoList.put( key, value );
	}

	/**
	 * Reads a List of FileInfo objects from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		/* get the element count from the MessageBuffer */
		short listElem = messageBuffer.readInt16();

		/* clear the list */
		this.fileInfoList.clear();

		/* insert the new FileInfo objects */
		for ( int i = 0; i < listElem; i++ ) {
			FileInfo fileInfo = new FileInfo();
			fileInfo.readStream( messageBuffer );
			this.put( fileInfo.getId(), fileInfo );
		}
	}
	
	/**
	 * Update a specific element in the List
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		this.get( messageBuffer.readInt32() ).update( messageBuffer );
	}
	
	/**
	 * Get a FileInfo object from this object by there id
	 * @param id The FileInfo id
	 * @return The FileInfo object
	 */
	public FileInfo get( int id ) {
		return ( FileInfo ) this.fileInfoList.get( id );
	}
	
	/**
	 * Get an Iterator
	 * @return an Iterator
	 */
	public TIntObjectIterator iterator() {
		return this.fileInfoList.iterator();
	}
}

/*
$Log: FileInfoList.java,v $
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