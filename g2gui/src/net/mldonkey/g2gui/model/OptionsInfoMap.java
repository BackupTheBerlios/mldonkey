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

import java.util.Iterator;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * OptionsInfo
 *
 * @author $user$
 * @version $Id: OptionsInfoMap.java,v 1.11 2003/07/07 15:32:43 dek Exp $ 
 *
 */
public class OptionsInfoMap extends InfoMap {
	/**
	 * @param communication my parent
	 */
	public OptionsInfoMap( CoreCommunication communication ) {
		super( communication );
	}
	
	/**
	 * Reads an OptionsInfo object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		/*
		 * List of (String,String)	The list of options with their current value
		 * 
		 */
		short listElem = messageBuffer.readInt16();		
		for ( int i = 0; i < ( listElem ); i++ ) {
			OptionsInfo optionsInfo = new OptionsInfo( this.parent );
			optionsInfo.readStream( messageBuffer );
			this.infoMap.put( optionsInfo.getKey(), optionsInfo );			
		}
	}
	
	/**
	 * This one tells me, to which section an Option belongs. 
	 * sectionToAppear is <b>null</b>, if the value belongs to a plugin and is not
	 * a general Option
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readGeneralOptionDetails( MessageBuffer messageBuffer ) {
		/*
		 * String  	 Section where Option should appear 
		 * String  	 Description 
		 * String  	 Name of Option 
		 * OptionType  	 The Type of the Option (to select which widget to use)
		 */
		 String sectionToAppear = messageBuffer.readString();
		 String description = messageBuffer.readString();
		 String nameOfOption = messageBuffer.readString();
		 byte optionType = messageBuffer.readByte();
		 
		 /*
		  * now get the specific Option and set the values:
		  */
		( ( OptionsInfo ) infoMap.get( nameOfOption ) ).setDescription( description );
		( ( OptionsInfo ) infoMap.get( nameOfOption ) ).setSectionToAppear( sectionToAppear );
		( ( OptionsInfo ) infoMap.get( nameOfOption ) ).setOptionType( optionType );
		 
	}
	
	/**
	 * This one tells me, to which plugin an Option belongs. 
	 * pluginToAppear is <b>null</b>, if the value doesn't belong to a specific plugin
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readPluginOptionDetails( MessageBuffer messageBuffer ) {
		/*
		 * 	 String  	 Plugin where Option should appear 
		 * 	 String  	 Description 
		 * 	 String  	 Name of Option 
		 * 	 OptionType  	 The Type of the Option (to select which widget to use)
		 */
		String pluginToAppear = messageBuffer.readString();
		String description = messageBuffer.readString();
		String nameOfOption = messageBuffer.readString();
		byte optionType = messageBuffer.readByte();
		 
		/*
		 * now get the specific Option and set the values:
		 */
	   ( ( OptionsInfo ) infoMap.get( nameOfOption ) ).setDescription( description );
	   ( ( OptionsInfo ) infoMap.get( nameOfOption ) ).setPluginToAppear( pluginToAppear );
	   ( ( OptionsInfo ) infoMap.get( nameOfOption ) ).setOptionType( optionType );	   
		
	}
	

	/**
	 * String representation of this object
	 * @return string A string representation of thi object
	 */
	public String toString() {
		String result = new String();
		Iterator itr = this.infoMap.values().iterator();
		while ( itr.hasNext() ) {
			result += itr.next().toString();
		}
		return result;
	}

	/** (non-Javadoc)
	 * @see net.mldonkey.g2gui.model.InfoCollection#update(net.mldonkey.g2gui.helper.MessageBuffer)
	 */
	public void update( MessageBuffer messageBuffer ) {
		/*
		 * does nothing
		 */
		
	}


}

/*
$Log: OptionsInfoMap.java,v $
Revision 1.11  2003/07/07 15:32:43  dek
made Option-handling more natural

Revision 1.10  2003/07/06 08:49:33  lemmstercvs01
better oo added

Revision 1.9  2003/07/01 13:31:42  dek
now it does read out the complete Optionslist, not only the first half...

Revision 1.8  2003/06/27 10:35:53  lemmstercvs01
removed unneeded calls

Revision 1.7  2003/06/24 09:15:27  lemmstercvs01
checkstyle applied

Revision 1.6  2003/06/21 13:20:36  dek
work on optiontree continued - one can already change client_name in General-leaf

Revision 1.5  2003/06/20 15:15:22  dek
humm, some interface-changes, hope, it didn't break anything ;-)

Revision 1.4  2003/06/17 12:06:51  lemmstercvs01
wrong implementers removed

Revision 1.3  2003/06/16 21:48:38  lemmstercvs01
class hierarchy changed

Revision 1.2  2003/06/16 13:18:59  lemmstercvs01
checkstyle applied

Revision 1.1  2003/06/15 16:18:41  lemmstercvs01
new interface introduced

Revision 1.1  2003/06/14 23:07:20  lemmstercvs01
added opcode 1

*/