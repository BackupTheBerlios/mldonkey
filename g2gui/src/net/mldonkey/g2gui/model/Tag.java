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
import net.mldonkey.g2gui.model.enum.Enum;

/**
 * Tag
 *
 *
 * @version $Id: Tag.java,v 1.12 2005/01/02 17:01:21 lemmy Exp $ 
 *
 */
public class Tag extends SimpleInformation {
	
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
	
	Tag() {
		//prevent outer package instanciation
	}
	
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
Revision 1.12  2005/01/02 17:01:21  lemmy
explicitly import our enum classes to avoid problems with java 1.5 enums

Revision 1.11  2004/03/21 21:00:50  dek
implemented gui-Proto 21-25 !!!!!

Revision 1.10  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.9  2003/12/01 14:22:17  lemmy
ProtocolVersion handling completely rewritten

Revision 1.8  2003/08/23 15:21:37  zet
remove @author

Revision 1.7  2003/08/22 21:03:15  lemmy
replace $user$ with $Author: lemmy $

Revision 1.6  2003/07/05 20:04:02  lemmy
javadoc improved

Revision 1.5  2003/06/24 09:29:57  lemmy
Enum more improved

Revision 1.4  2003/06/24 09:16:48  lemmy
better Enum added

Revision 1.3  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.2  2003/06/16 15:33:03  lemmy
some kind of enum added

Revision 1.1  2003/06/14 17:40:40  lemmy
initial commit

*/