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
 * Tag
 *
 * @author markus
 * @version $Id: Tag.java,v 1.3 2003/06/18 13:30:56 dek Exp $ 
 *
 */
public class Tag implements SimpleInformation {
	
	public static final short UNSIGNED_INT = 0;
	public static final short SIGNED_INT = 1;
	public static final short STRING = 2;
	public static final short IPADDRESS = 3;
	
	/**
	 * Tag Name
	 */
	private String name;
	/**
	 * Tag Type
	 */
	private byte type;
	/**
	 * Tag Value
	 */
	private int value;
	/**
	 * Tag Value (if type = 2)
	 */
	private String sValue;
	
	/**
	 * @return a string
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return a string
	 */
	public String getSValue() {
		return sValue;
	}

	/**
	 * @return a byte
	 */
	public byte getType() {
		return type;
	}

	/**
	 * @return an int
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param string a string
	 */
	public void setName( String string ) {
		name = string;
	}

	/**
	 * @param string a string
	 */
	public void setSValue( String string ) {
		sValue = string;
	}

	/**
	 * @param b a byte
	 */
	public void setType( byte b ) {
		if ( b == 0 )
			type = UNSIGNED_INT;
		else if ( b == 1 )
			type = SIGNED_INT;
		else if ( b == 2 )
			type = STRING;
		else if ( b == 3 )
			type = IPADDRESS;
	}

	/**
	 * @param i an int
	 */
	public void setValue( int i ) {
		value = i;
	}
	
	/**
	 * Reads a Tag object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		this.setName( messageBuffer.readString() );
		this.setType( messageBuffer.readByte() );
		if ( this.getType() != STRING ) {
			this.setValue( messageBuffer.readInt32() );
		}
		else {
			this.setSValue( messageBuffer.readString() );
		}
	}

}

/*
$Log: Tag.java,v $
Revision 1.3  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.2  2003/06/16 15:33:03  lemmstercvs01
some kind of enum added

Revision 1.1  2003/06/14 17:40:40  lemmstercvs01
initial commit

*/