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
package net.mldonkey.g2gui.model;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * FileInfo25.java
 *
 * @version $Id: FileInfo25.java,v 1.3 2004/03/21 21:08:58 dek Exp $ 
 *
 */
public class FileInfo25 extends FileInfo24 {
	
	/**
	 * @param core
	 */
	FileInfo25(CoreCommunication core) {
		super(core);
	}

	/**
	 * @param messageBuffer
	 */
	protected void setSize(MessageBuffer messageBuffer) {		
		setSize( messageBuffer.readInt64());
		
	}
	
	/**
	 * @param messageBuffer
	 */
	protected void setDownloaded(MessageBuffer messageBuffer) {		
		setDownloaded(messageBuffer.readInt64());
	}

}


/*
 $Log: FileInfo25.java,v $
 Revision 1.3  2004/03/21 21:08:58  dek
 removed stubs

 Revision 1.2  2004/03/21 21:00:50  dek
 implemented gui-Proto 21-25 !!!!!


 */