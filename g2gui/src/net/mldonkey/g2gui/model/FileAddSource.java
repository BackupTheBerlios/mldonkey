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

import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * FileAddSource
 *
 * @author markus
 * @version $Id: FileAddSource.java,v 1.5 2003/06/18 13:30:56 dek Exp $ 
 *
 */
public class FileAddSource implements SimpleInformation {
	
	/**
	 * The File Identifier 
	 */
	private int id;
	/**
	 * The Source/Client Identifier
	 */
	private int sourceid;
	
	/**
	 * @return an int
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return an int
	 */
	public int getSourceid() {
		return sourceid;
	}

	/**
	 * @param i an int
	 */
	public void setId( int i ) {
		id = i;
	}

	/**
	 * @param i an int
	 */
	public void setSourceid( int i ) {
		sourceid = i;
	}
	
	/**
	 * @return A string representation of this object
	 */
	public String toString() {
		String result = new String();
		result += this.getId() + "\n";
		result += this.getSourceid();
		return result;
	}

	/**
	 * Reads a FileAddSource object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from 
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		this.setId( messageBuffer.readInt32() );
		this.setSourceid( messageBuffer.readInt32() );
	}

}

/*
$Log: FileAddSource.java,v $
Revision 1.5  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.4  2003/06/14 12:47:27  lemmstercvs01
checkstyle applied

Revision 1.3  2003/06/13 11:03:41  lemmstercvs01
changed InputStream to MessageBuffer

Revision 1.2  2003/06/12 22:23:06  lemmstercvs01
lots of changes

Revision 1.1  2003/06/12 10:36:20  lemmstercvs01
new Class for FileAddSources

*/