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

import java.util.ArrayList;
import java.util.List;

import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * FileInfoList
 *
 * @author markus
 * @version $Id: FileInfoList.java,v 1.3 2003/06/13 11:03:41 lemmstercvs01 Exp $ 
 *
 */
public class FileInfoList implements Information {
	public List fileInfoList;
	
	public FileInfoList() {
		this.fileInfoList = new ArrayList();
	}

	/**
	 * Reads a List of running Downloads
	 * @param inputStream Stream to read from
	 * @return a List filled with complete Downloads
	 * @throws IOException Error if read from stream failed
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		short listElem = messageBuffer.readInt16();

		/* trim the list to a correct size */		
		if ( this.fileInfoList.size() < listElem ) {
			while ( listElem > this.fileInfoList.size() ) 
				this.fileInfoList.add( new FileInfo() );
		}
		else {
			int i = 0;
			while ( listElem < this.fileInfoList.size() )
				this.fileInfoList.remove( i++ );
		}
		
		for ( int i = 0; i < listElem; i++ ) {
			( ( FileInfo ) this.fileInfoList.get( i ) ).readStream( messageBuffer );
		}
	}
}

/*
$Log: FileInfoList.java,v $
Revision 1.3  2003/06/13 11:03:41  lemmstercvs01
changed InputStream to MessageBuffer

Revision 1.2  2003/06/12 22:23:06  lemmstercvs01
lots of changes

Revision 1.1  2003/06/12 18:16:20  lemmstercvs01
initial commit

*/