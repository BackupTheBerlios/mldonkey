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
 * @version $Id: AddSomeOption.java,v 1.1 2003/06/15 09:58:04 lemmstercvs01 Exp $ 
 *
 */
public class AddSomeOption implements Information {
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
	 * @return a string
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return a string
	 */
	public String getNameOfOption() {
		return nameOfOption;
	}

	/**
	 * @return an OptionType object
	 */
	public OptionType getOptionType() {
		return optionType;
	}

	/**
	 * @return a string
	 */
	public String getSectionToAppear() {
		return sectionToAppear;
	}

	/**
	 * @param string a string
	 */
	public void setDescription( String string ) {
		description = string;
	}

	/**
	 * @param string a string
	 */
	public void setNameOfOption( String string ) {
		nameOfOption = string;
	}

	/**
	 * @param type
	 */
	public void setOptionType( OptionType type ) {
		optionType = type;
	}

	/**
	 * @param string
	 */
	public void setSectionToAppear( String string ) {
		sectionToAppear = string;
	}
	
	public void readStream( MessageBuffer messageBuffer ) {
		/*
		 * String  	 Section where Option should appear 
 		 * String  	 Description 
		 * String  	 Name of Option 
		 * OptionType  	 The Type of the Option (to select which widget to use)
		 */
		 this.setSectionToAppear( messageBuffer.readString() );
		 this.setDescription( messageBuffer.readString() );
		 this.setNameOfOption( messageBuffer.readString() );
		 this.getOptionType().readStream( messageBuffer );
	}

}

/*
$Log: AddSomeOption.java,v $
Revision 1.1  2003/06/15 09:58:04  lemmstercvs01
initial commit

*/