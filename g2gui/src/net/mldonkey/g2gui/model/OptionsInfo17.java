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
 * OptionsInfo17
 *
 * @version $Id: OptionsInfo17.java,v 1.3 2004/04/19 17:34:41 dek Exp $ 
 *
 */
public class OptionsInfo17 extends OptionsInfo {
	/**
	 * Advanced option;
	 */
	private boolean advanced;
	/**
	 * The option defaultValue
	 */
	private String defaultValue;
	/**
	 * The HelpText to this optionsinfo
	 */
	private String optionHelp;
	
	/**
	 * @param core
	 */
	public OptionsInfo17(CoreCommunication core) {
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
		this.setOptionType( messageBuffer.readString() );
		this.optionHelp = messageBuffer.readString();
		this.value = messageBuffer.readString();
		this.defaultValue = messageBuffer.readString();
		this.setAdvanced( messageBuffer.readByte() );
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
		this.setOptionType( messageBuffer.readString() );
		this.optionHelp = messageBuffer.readString();
		this.value = messageBuffer.readString();
		this.defaultValue = messageBuffer.readString();
		this.setAdvanced( messageBuffer.readByte() );
	}

	/**
	 * Advanced or Beginner Option
	 * @param b the byte
	 */
	private void setAdvanced( byte b ) {
		if ( b == 0 )
			this.advanced = false;
		else
			this.advanced = true;
	}

	/**
	 * Sets the EnumTagType by String (proto > 17)
	 * @param aString The string representation of the TagType
	 */
	private void setOptionType( String aString ) {
		if ( aString.equalsIgnoreCase( "String" ) )
			this.optionType = EnumTagType.STRING;
		else if ( aString.equalsIgnoreCase( "Ip List" ) )
			this.optionType = EnumTagType.IP_LIST;
		else if ( aString.equalsIgnoreCase( "Int" ) )
			this.optionType = EnumTagType.INT;
		else if ( aString.equalsIgnoreCase( "Bool" ) )
			this.optionType = EnumTagType.BOOL;
		else if ( aString.equalsIgnoreCase( "Ip" ) )
			this.optionType = EnumTagType.IP;	
		else if ( aString.equalsIgnoreCase( "Addr" ) )
			this.optionType = EnumTagType.ADDR;
		else if ( aString.equalsIgnoreCase( "Integer" ) )
			this.optionType = EnumTagType.INT;
		else if ( aString.equalsIgnoreCase( "Float" ) )
			this.optionType = EnumTagType.FLOAT;
		else if ( aString.equalsIgnoreCase( "Md4" ) )
			this.optionType = EnumTagType.MD4;
		else if ( aString.equalsIgnoreCase( "Sha1" ) )			
			this.optionType = EnumTagType.SHA1;
		else if ( aString.equalsIgnoreCase( "Int64" ) )			
			this.optionType = EnumTagType.INT64;
		else {
			this.optionType = EnumTagType.STRING;						
			System.out.println( "Unknown EnumTagType: " + aString ); 					
		}
	}
	/**
	 * The help string for this option.
	 * Proto < 18 returns the description
	 * @return The option help text
	 */
	public String getOptionHelp() {
		return this.description;			
	}
	
	/**
	 * is this option advanced
	 * Proto < 18 returns always true
	 * @return true if this option is advanced
	 */
	public boolean isAdvanced() {
		return false;
	}
	
	/**
	 * The default value for this option
	 * Proto < 18 returns value
	 * @return the default value for this option
	 */
	public String getDefaultValue() {
		return this.value;	
	}
}

/*
$Log: OptionsInfo17.java,v $
Revision 1.3  2004/04/19 17:34:41  dek
New EnumTagType: Int64 and some debugging info for wrong opcode-interpretation

Revision 1.2  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.1  2003/12/01 14:22:17  lemmy
ProtocolVersion handling completely rewritten

*/