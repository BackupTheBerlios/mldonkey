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
 * SearchResult
 *
 * @author $user$
 * @version $Id: SearchResult.java,v 1.3 2003/07/06 07:29:47 lemmstercvs01 Exp $ 
 *
 */
public class SearchResult implements SimpleInformation {
	
	/**
	 * The Search Identifier
	 */
	private int searchID;
	/**
	 * The Result Identifier (from a ResultInfo)
	 */
	private int resultID;

	/** (non-Javadoc)
	 * @see net.mldonkey.g2gui.model.Information#readStream(net.mldonkey.g2gui.helper.MessageBuffer)
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		/*
		 * int32	The Search Identifier 
 		 * int32	The Result Identifier 
		 */
		this.searchID = messageBuffer.readInt32();
		this.resultID = messageBuffer.readInt32();
	}

	/**
	 * @return The result id from a result info
	 */
	public int getResultID() {
		return resultID;
	}

	/**
	 * @return The search id
	 */
	public int getSearchID() {
		return searchID;
	}
}

/*
$Log: SearchResult.java,v $
Revision 1.3  2003/07/06 07:29:47  lemmstercvs01
javadoc improved

Revision 1.2  2003/07/04 12:30:09  dek
checkstyle

Revision 1.1  2003/06/30 07:19:47  lemmstercvs01
initial commit (untested)

*/