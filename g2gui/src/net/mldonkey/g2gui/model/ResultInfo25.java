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
 * ResultInfo35.java
 *
 * @version $Id: ResultInfo25.java,v 1.2 2004/03/21 21:23:53 dek Exp $ 
 *
 */
public class ResultInfo25 extends ResultInfo {

	/**
	 * @param core
	 */
	ResultInfo25(CoreCommunication core) {
		super(core);
		
	}
	
	/**
	 * @param messageBuffer
	 */
	protected void setSize(MessageBuffer messageBuffer) {
		this.size = messageBuffer.readInt64();
		
	}

}


/*
 $Log: ResultInfo25.java,v $
 Revision 1.2  2004/03/21 21:23:53  dek
 some proto25 was still missing !!

 Revision 1.1  2004/03/21 21:20:26  dek
 some proto25 was still missing


 */