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
import net.mldonkey.g2gui.model.enum.*;

/**
 * Tag
 *
 * @author markus
 * @version $Id: Tag.java,v 1.6 2003/07/05 20:04:02 lemmstercvs01 Exp $ 
 *
 */
public class Tag implements SimpleInformation {
	
	/**
	 * Tag Name
	 */
	private String name;
	/**
	 * Tag Type
	 */
	private Enum type;
	/**
	 * Tag Value
	 */
	private int value;
	/**
	 * Tag Value (if type = 2)
	 */
	private String sValue;
	
	/**
	 * @return Tag name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Tag value (if != EnumType.STRING)
	 */
	public String getSValue() {
		return sValue;
	}

	/**
	 * @return Tag type
	 */
	public Enum getType() {
		return type;
	}

	/**
	 * @return Tag value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param b a byte
	 */
	private void setType( byte b ) {
		if ( b == 0 )
			type = EnumType.UNSIGNED_INT;
		else if ( b == 1 )
			type = EnumType.SIGNED_INT;
		else if ( b == 2 )
			type = EnumType.STRING;
		else if ( b == 3 )
			type = EnumType.IPADDRESS;
	}
	
	/**
	 * Reads a Tag object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		this.name = messageBuffer.readString();
		this.setType( messageBuffer.readByte() );
		if ( this.getType() != EnumType.STRING ) {
			this.value = messageBuffer.readInt32();
		}
		else {
			this.sValue = messageBuffer.readString();
		}
	}
}

/*
$Log: Tag.java,v $
Revision 1.6  2003/07/05 20:04:02  lemmstercvs01
javadoc improved

Revision 1.5  2003/06/24 09:29:57  lemmstercvs01
Enum more improved

Revision 1.4  2003/06/24 09:16:48  lemmstercvs01
better Enum added

Revision 1.3  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.2  2003/06/16 15:33:03  lemmstercvs01
some kind of enum added

Revision 1.1  2003/06/14 17:40:40  lemmstercvs01
initial commit

*/