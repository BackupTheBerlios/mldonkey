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
 *
 * @version $Id: OptionsInfo.java,v 1.25 2003/12/01 14:22:17 lemmster Exp $ 
 *
 */
public abstract class OptionsInfo extends Parent {
	/**
	 * The option type
	 */
	protected EnumTagType optionType;
	/**
	 * The optino description
	 */
	protected String description = "";
	/**
	 * The section to appear
	 */
	protected String sectionToAppear;
	/**
	 * The pluginToAppear
	 */
	protected String pluginToAppear;
	/**
	 * Options Name
	 */
	protected String key;
	/**
	 * Options Value
	 */
	protected String value;
	
	/**
	 * Creates a new optionsinfo
	 * @param core The corecommunication
	 */
	OptionsInfo( CoreCommunication core ) {
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
	
	protected void alterValue( String aString ) {
		this.value = aString;
	}

	/**
	 * @param aString the OptionValue
	 */
	public void setValue( String aString ) {
		Object[] obj = new Object[ 2 ];
		obj[ 0 ] = this.key;
		obj[ 1 ] = aString;
		Message message = new EncodeMessage( Message.S_SET_OPTION, obj );
		message.sendMessage( this.parent );
		message = null;
		obj = null;
	}
	
	/**
	 * Reads an OptionsInfo object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		this.key = messageBuffer.readString();
		this.value = messageBuffer.readString();
	}
	
	/**
	 * Reads the sectionToAppear from the MessageBuffer
	 * @param messageBuffer MessageBuffer to read from
	 */
	public abstract void addSectionToAppear( MessageBuffer messageBuffer );
	
	/**
	 * Reads the pluginToAppear from the MessageBuffer
	 * @param messageBuffer MessageBuffer to read from
	 */
	public abstract void addPluginToAppear( MessageBuffer messageBuffer );	

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
		Message consoleMessage = new EncodeMessage( Message.S_SET_OPTION, payLoad );
		consoleMessage.sendMessage( this.parent );
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
		return "";
	}
}

/*
$Log: OptionsInfo.java,v $
Revision 1.25  2003/12/01 14:22:17  lemmster
ProtocolVersion handling completely rewritten

Revision 1.24  2003/09/18 15:29:25  zet
centralize writeStream in core
handle IOException rather than throwing it away

Revision 1.23  2003/09/18 09:16:47  lemmster
checkstyle

Revision 1.22  2003/08/23 15:21:37  zet
remove @author

Revision 1.21  2003/08/23 10:02:02  lemmster
use supertype where possible

Revision 1.20  2003/08/19 21:02:16  lemmster
show all options in simple mode proto < 18

Revision 1.19  2003/08/19 12:47:57  lemmster
$user$ -> $Author: lemmster $

Revision 1.18  2003/08/19 12:46:02  lemmster
typo fixed

Revision 1.17  2003/08/19 12:45:17  lemmster
support proto >=18

Revision 1.16  2003/08/04 16:57:00  lemmstercvs01
better way to set the value

Revision 1.15  2003/08/03 20:00:14  lemmstercvs01
bugfix

Revision 1.14  2003/08/02 10:02:52  lemmstercvs01
bugfix in readStream()

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