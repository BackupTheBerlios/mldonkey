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
 * OptionType
 *
 * @author $user$
 * @version $Id: OptionType.java,v 1.7 2003/06/24 09:29:57 lemmstercvs01 Exp $ 
 *
 */
public class OptionType implements SimpleInformation {
	/**
	 * Tag Type
	 */
	private Enum tagType;
	/**
	 * @return a string
	 */
	public Enum getTagType() {
		return tagType;
	}

	/**
	 * @param b a byte
	 */
	public void setTagType( byte b ) {
		if ( b == 0 )
			tagType = EnumTagType.STRING;
		else if ( b == 1 )
			tagType = EnumTagType.BOOL;
		else if ( b == 2 )
			tagType = EnumTagType.FILE;
	}
	
	/**
	 * Read an OptionType from a MessageStream
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		this.setTagType( messageBuffer.readByte() ); 
	}
}

/*
$Log: OptionType.java,v $
Revision 1.7  2003/06/24 09:29:57  lemmstercvs01
Enum more improved

Revision 1.6  2003/06/24 09:16:47  lemmstercvs01
better Enum added

Revision 1.5  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.4  2003/06/16 15:33:03  lemmstercvs01
some kind of enum added

Revision 1.3  2003/06/16 13:18:59  lemmstercvs01
checkstyle applied

Revision 1.2  2003/06/15 16:18:41  lemmstercvs01
new interface introduced

Revision 1.1  2003/06/15 09:58:04  lemmstercvs01
initial commit

*/