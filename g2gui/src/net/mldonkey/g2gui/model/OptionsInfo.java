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
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.model.enum.EnumTagType;

/**
 * OptionsInfo
 *
 * @author $user$
 * @version $Id: OptionsInfo.java,v 1.13 2003/08/02 09:32:23 lemmstercvs01 Exp $ 
 *
 */
public class OptionsInfo extends Parent {
	/**
	 * The option type
	 */
	private EnumTagType optionType;
	/**
	 * The optino description
	 */
	private String description = "";
	/**
	 * The section to appear
	 */
	private String sectionToAppear;
	/**
	 * The pluginToAppear
	 */
	private String pluginToAppear;
	/**
	 * Options Name
	 */
	private String key;
	/**
	 * Options Value
	 */
	private String value;
	/**
	 * The option defaultValue
	 */
	private String defaultValue;
	/**
	 * The HelpText to this optionsinfo
	 */
	private String optionHelp;
	/**
	 * Advanced option;
	 */
	private boolean advanced;
	
	/**
	 * Creates a new optionsinfo
	 * @param core The corecommunication
	 */
	public OptionsInfo( CoreCommunication core ) {
		super( core );
	}
	
	/**
	 * @return The key to this optionsinfo value
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return The value of this optionsinfo
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param string the OptionValue
	 */
	public void setValue( String aString ) {
		Object[] obj = new Object[ 2 ];
		obj[ 0 ] = this.key;
		obj[ 1 ] = aString;
		Message message = new EncodeMessage( Message.S_SET_OPTION, obj );
		message.sendMessage( this.parent.getConnection() );
		message = null;
		obj = null;
	}
	
	/**
	 * Reads an OptionsInfo object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		this.key = messageBuffer.readString();
		this.setValue( messageBuffer.readString() );
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
		if ( this.parent.getProtoToUse() > 16 ) {
			this.setOptionType( messageBuffer.readString() );
			this.optionHelp = messageBuffer.readString();
			this.value = messageBuffer.readString();
			this.defaultValue = messageBuffer.readString();
			this.setAdvanced( messageBuffer.readByte() );
		}
		else
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
		if ( this.parent.getProtoToUse() > 16 ) {
			this.setOptionType( messageBuffer.readString() );
			this.optionHelp = messageBuffer.readString();
			this.value = messageBuffer.readString();
			this.defaultValue = messageBuffer.readString();
			this.setAdvanced( messageBuffer.readByte() );
		}
		else
			this.setOptionType( messageBuffer.readByte() );
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
	 * return a string representation of this object
	 * @return a string
	 */
	public String toString() {		
		String result = new String(  );		
		result +=  this.getKey();
		result += "\n  Value:\t\t " + this.getValue() ;
		result += "\n  Description:\t " + this.getDescription();
		result += "\n  Type:\t\t\t " + this.getOptionType();
		result += "\n  Section:\t\t " + this.getSectionToAppear();
		result += "\n  Plugin:\t\t " + this.getPluginToAppear();
		return result;		
	}
 
	/**
	 * Sends the optionsinfo to the core
	 */
	public void send() {
		String[] payLoad = { this.getKey(), this.getValue() };
		EncodeMessage consoleMessage = new EncodeMessage( Message.S_SET_OPTION, payLoad );
		consoleMessage.sendMessage( this.parent.getConnection() );
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
	
	/**
	 * Sets the EnumTagType by String (proto > 17)
	 * @param aString The string representation of the TagType
	 */
	private void setOptionType( String aString ) {
		if ( aString.equals( "String" ) )
			this.optionType = EnumTagType.STRING;
		else if ( aString.equals( "Ip List" ) )
			this.optionType = EnumTagType.IP_LIST;
		else if ( aString.equals( "Int") )
			this.optionType = EnumTagType.INT;
		else if ( aString.equals( "Bool" ) )
			this.optionType = EnumTagType.BOOL;
		else if ( aString.equals( "Ip" ) )
			this.optionType = EnumTagType.IP;	
		else if ( aString.equals( "Addr" ) )
			this.optionType = EnumTagType.ADDR;
		else if ( aString.equals( "Integer" ) )
			this.optionType = EnumTagType.INT;
		else if ( aString.equals( "Float" ) )
			this.optionType = EnumTagType.FLOAT;
		else if ( aString.equals( "Md4" ) )
			this.optionType = EnumTagType.MD4;
		else if ( aString.equals( "Sha1" ) )			
			this.optionType = EnumTagType.SHA1;
		else {
			this.optionType = EnumTagType.STRING;						
			System.out.println( "Unknown EnumTagType: " + aString );					
		}
	}

	/**
	 * @return Some more detailed Information about this option
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return what kind of option do we have: String, Int od Boolean
	 */
	public EnumTagType getOptionType() {
		return optionType;
	}

	/**
	 * this method returns <b>null</b> if the Option is a pluginOption and therefore doesn't
	 * have a sectionToAppear but a pluginToAppear
	 * @return in which general Section this Option fits best
	 */
	public String getSectionToAppear() {
		return sectionToAppear;
	}

	/**
	 * this method returns <b>null</b> if the Option is a GeneralOption and therefore doesn't
	 * have a pluginToAppear but a sectionToAppear
	 * @return in which plugin Section this Option fits best
	 */
	public String getPluginToAppear() {
		return pluginToAppear;
	}
}

/*
$Log: OptionsInfo.java,v $
Revision 1.13  2003/08/02 09:32:23  lemmstercvs01
setOption added

Revision 1.12  2003/08/02 09:27:39  lemmstercvs01
added support for proto > 16

Revision 1.11  2003/07/09 08:54:29  lemmstercvs01
void setKey() added

Revision 1.10  2003/07/09 08:48:58  dek
*** empty log message ***

Revision 1.9  2003/07/07 18:30:29  dek
saving options now also works

Revision 1.8  2003/07/07 17:39:18  dek
removed debugging System.out.....

Revision 1.7  2003/07/07 15:32:43  dek
made Option-handling more natural

Revision 1.6  2003/07/06 08:49:33  lemmstercvs01
better oo added

Revision 1.5  2003/07/04 18:03:13  dek
now has a send()-method

Revision 1.4  2003/06/20 15:15:22  dek
humm, some interface-changes, hope, it didn't break anything ;-)

Revision 1.3  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.2  2003/06/15 16:18:41  lemmstercvs01
new interface introduced

*/