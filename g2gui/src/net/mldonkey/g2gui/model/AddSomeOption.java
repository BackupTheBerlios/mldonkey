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
 * AddSectionOption
 *
 * @author $user$
 * @version $Id: AddSomeOption.java,v 1.5 2003/07/06 09:37:41 lemmstercvs01 Exp $ 
 *
 */
public class AddSomeOption implements SimpleInformation {
	/**
	 * Section where Option should appear
	 */
	private String sectionToAppear; 
	/**
	 * Description
	 */
	private String description;
	/**
	 * Name of Option
	 */
	private String nameOfOption;
	/**
	 * The Type of the Option (to select which widget to use)
	 */
	private OptionType optionType = new OptionType();

	/**
	 * @return The description of the option
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return The name of the option
	 */
	public String getNameOfOption() {
		return nameOfOption;
	}

	/**
	 * @return The optiontype
	 */
	public OptionType getOptionType() {
		return optionType;
	}

	/**
	 * @return The section where this option should appear
	 */
	public String getSectionToAppear() {
		return sectionToAppear;
	}

	/**
	 * Reads an AddSomeOption object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		/*
		 * String  	 Section where Option should appear 
 		 * String  	 Description 
		 * String  	 Name of Option 
		 * OptionType  	 The Type of the Option (to select which widget to use)
		 */
		 this.sectionToAppear = messageBuffer.readString();
		 this.description = messageBuffer.readString();
		 this.nameOfOption = messageBuffer.readString();
		 this.getOptionType().readStream( messageBuffer );
	}
	
	/**
	 * String representation of this object
	 * @return string A string representation of thi object
	 */
	public String toString() {
		String result = new String( "\n" );
		result += "\t" + this.getNameOfOption();
		result += ": " + this.getDescription();
		result += ": " + this.getOptionType().toString();
		result += ": " + this.getSectionToAppear();
		return result;
	}
}

/*
$Log: AddSomeOption.java,v $
Revision 1.5  2003/07/06 09:37:41  lemmstercvs01
javadoc improved

Revision 1.4  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.3  2003/06/16 13:18:59  lemmstercvs01
checkstyle applied

Revision 1.2  2003/06/15 16:16:51  lemmstercvs01
toString() added, just for debugging

Revision 1.1  2003/06/15 09:58:04  lemmstercvs01
initial commit

*/