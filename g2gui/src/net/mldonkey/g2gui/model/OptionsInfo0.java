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

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.model.enum.EnumTagType;

/**
 * OptionsInfo0
 *
 * @version $Id: OptionsInfo0.java,v 1.1 2003/12/01 14:22:17 lemmster Exp $ 
 *
 */
public class OptionsInfo0 extends OptionsInfo {

	/**
	 * @param core
	 */
	public OptionsInfo0(CoreCommunication core) {
		super(core);
	}
	/**
	 * Reads the sectionToAppear from the MessageBuffer
	 * @param messageBuffer MessageBuffer to read from
	 */
	public void addSectionToAppear( MessageBuffer messageBuffer ) {
		/*
		 * String Name of Option 
		 * String Description 
		 * String Section where Option should appear 
		 * String Type of Option ("Bool", "Filename", ...) 
		 * String Option Help 
		 * String Current Option Value 
		 * String Option Default Value 
		 * int8 Advanced (0 -> simple, 1 -> advanced) 
		 */		
		this.sectionToAppear = messageBuffer.readString();
		this.description = messageBuffer.readString();
		this.key = messageBuffer.readString();
		this.setOptionType( messageBuffer.readByte() );
	}

	/**
	 * Reads the pluginToAppear from the MessageBuffer
	 * @param messageBuffer MessageBuffer to read from
	 */
	public void addPluginToAppear( MessageBuffer messageBuffer ) {
		/*
		 * String Name of Option 
		 * String Description 
		 * String Plugin where Option should appear 
		 * String Type of Option ("Bool", "Filename", ...) 
		 * String Option Help 
		 * String Current Option Value 
		 * String Option Default Value 
		 * int8 Advanced (0 -> simple, 1 -> advanced) 
		 */		
		this.pluginToAppear = messageBuffer.readString();
		this.description = messageBuffer.readString();
		this.key = messageBuffer.readString();
		this.setOptionType( messageBuffer.readByte() );
	}

	/**
	 * @param optionType what kind of option do we have: String, Int od Boolean
	 */
	private void setOptionType( byte optionType ) {		
		if ( optionType == 0 )
			this.optionType = EnumTagType.STRING;
		else if ( optionType == 1 )
			this.optionType = EnumTagType.BOOL;
		else if ( optionType == 2 )
			this.optionType = EnumTagType.FILE;
	}
}

/*
$Log: OptionsInfo0.java,v $
Revision 1.1  2003/12/01 14:22:17  lemmster
ProtocolVersion handling completely rewritten

*/