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

import java.io.IOException;
import java.io.InputStream;

import net.mldonkey.g2gui.comm.Message;

/**
 * FileAddSource
 *
 * @author markus
 * @version $Id: FileAddSource.java,v 1.2 2003/06/12 22:23:06 lemmstercvs01 Exp $ 
 *
 */
public class FileAddSource implements Information {
	
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
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public void readStream( InputStream inputStream ) throws IOException {
		this.setId( Message.readInt32( inputStream ) );
		this.setSourceid( Message.readInt32( inputStream ) );
	}

}

/*
$Log: FileAddSource.java,v $
Revision 1.2  2003/06/12 22:23:06  lemmstercvs01
lots of changes

Revision 1.1  2003/06/12 10:36:20  lemmstercvs01
new Class for FileAddSources

*/