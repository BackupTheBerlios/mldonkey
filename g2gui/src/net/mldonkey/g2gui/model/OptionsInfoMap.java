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
import net.mldonkey.g2gui.model.enum.EnumNetwork;

/**
 * OptionsInfo
 *
 *
 * @version $Id: OptionsInfoMap.java,v 1.23 2003/10/28 11:07:32 lemmster Exp $ 
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
			/*now we must take care, wether the option already exists, so that we don't
			 * overwrite this option*/
			if ( !this.infoMap.contains( optionsInfo.getKey() ) )
				this.infoMap.put( optionsInfo.getKey(), optionsInfo );
			else {
				/*get the already existing option and change the values:*/
				OptionsInfo existingOption = ( OptionsInfo ) this.infoMap.get( optionsInfo.getKey() );
				existingOption.alterValue( optionsInfo.getValue() );
			}
		}
		this.setChanged();
		this.notifyObservers( this );
	}
	
	/**
	 * This one tells me, to which section an Option belongs. 
	 * sectionToAppear is <b>null</b>, if the value belongs to a plugin and is not
	 * a general Option
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readGeneralOptionDetails( MessageBuffer messageBuffer ) {
		/* read the name of option and reset the iterator */
		int itr = messageBuffer.getIterator();
		String sectionToAppear = messageBuffer.readString();
		String description = messageBuffer.readString();
		String nameOfOption = messageBuffer.readString();
		messageBuffer.setIterator( itr );

		if ( this.infoMap.contains( nameOfOption ) ) {
			OptionsInfo option = ( OptionsInfo ) this.infoMap.get( nameOfOption );			
			option.addSectionToAppear( messageBuffer );
		}
		else {
			OptionsInfo option = new OptionsInfo( this.parent );
			option.addSectionToAppear( messageBuffer );
			this.infoMap.put( nameOfOption, option );
		}
		/* we dont need the strings anymomre */
		sectionToAppear = null;
		description = null;
		nameOfOption = null;

		this.setChanged();
		this.notifyObservers( this );
	}
	
	/**
	 * This one tells me, to which plugin an Option belongs. 
	 * pluginToAppear is <b>null</b>, if the value doesn't belong to a specific plugin
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readPluginOptionDetails( MessageBuffer messageBuffer ) {
		/* read the name of option and reset the iterator */
		int itr = messageBuffer.getIterator();
		String pluginToAppear = messageBuffer.readString();
		String description = messageBuffer.readString();
		String nameOfOption = messageBuffer.readString();
		messageBuffer.setIterator( itr );

		if ( this.infoMap.contains( nameOfOption ) ) {
			OptionsInfo option = ( OptionsInfo ) this.infoMap.get( nameOfOption );			
			option.addPluginToAppear( messageBuffer );
		}
		else {
			OptionsInfo option = new OptionsInfo( this.parent );
			option.addPluginToAppear( messageBuffer );
			this.infoMap.put( nameOfOption, option );
		}
		/* we dont need the strings anymore */
		pluginToAppear = null;
		description = null;
		nameOfOption = null;

		this.setChanged();
		this.notifyObservers( this );
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

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.model.InfoCollection#
	 * update(net.mldonkey.g2gui.helper.MessageBuffer)
	 */
	public void update( MessageBuffer messageBuffer ) {
		/*
		 * does nothing
		 */
	}
	
	/**
	 * Gets the value max_connected_servers/ultrapeers for the given network
	 * @param network The network
	 * @return max_connected_servers/ultrapeers
	 */
	public int getMaxConnectedServers( NetworkInfo network ) {
		if ( parent.getProtoToUse() < 18 ) return 0;
	
		/* does this networkinfo has servers/supernodes */
		if ( network.hasServers() || network.hasSupernodes() ) {
			OptionsInfo option = null;
			String prefix = null;
			if ( network.equals( EnumNetwork.FT ) ) {
				/* first the option-prefix */
				option = ( OptionsInfo ) this.infoMap.get( "FT-options_prefix" );
				prefix = option.getValue();
				/* now the max_connected_server/peers */
				option = ( OptionsInfo ) this.infoMap.get( prefix + "max_ultrapeers" );
				return new Integer( option.getValue() ).intValue();
			}
			if ( network.equals( EnumNetwork.GNUT ) ) {
				/* first the option-prefix */
				option = ( OptionsInfo ) this.infoMap.get( "GNUT-options_prefix" );
				prefix = option.getValue();
				/* now the max_connected_server/peers */
				option = ( OptionsInfo ) this.infoMap.get( prefix + "g1_max_ultrapeers" );
				return new Integer( option.getValue() ).intValue();
			}
			if ( network.equals( EnumNetwork.GNUT2 ) ) {
				/* first the option-prefix */
				option = ( OptionsInfo ) this.infoMap.get( "G2-options_prefix" );
				prefix = option.getValue();
				/* now the max_connected_server/peers */
				option = ( OptionsInfo ) this.infoMap.get( prefix + "max_ultrapeers" );
				return new Integer( option.getValue() ).intValue();
			}
			if ( network.equals( EnumNetwork.DC ) ) {
				/* first the option-prefix */
				option = ( OptionsInfo ) this.infoMap.get( "DC-options_prefix" );
				prefix = option.getValue();
				/* now the max_connected_server/peers */
				option = ( OptionsInfo ) this.infoMap.get( prefix + "max_connected_servers" );
				return new Integer( option.getValue() ).intValue();
			}
			if ( network.equals( EnumNetwork.OPENNP ) ) {
				/* first the option-prefix */
				option = ( OptionsInfo ) this.infoMap.get( "OpenNap-options_prefix" );
				prefix = option.getValue();
				/* now the max_connected_server/peers */
				option = ( OptionsInfo ) this.infoMap.get( prefix + "max_connected_servers" );
				return new Integer( option.getValue() ).intValue();
			}
			if ( network.equals( EnumNetwork.SOULSEEK ) ) {
				/* first the option-prefix */
				option = ( OptionsInfo ) this.infoMap.get( "slsk-options_prefix" );
				prefix = option.getValue();
				/* now the max_connected_server/peers */
				option = ( OptionsInfo ) this.infoMap.get( prefix + "max_connected_servers" );
				return new Integer( option.getValue() ).intValue();
			}
			if ( network.equals( EnumNetwork.DONKEY ) ) {
				/* now the max_connected_server/peers */
				option = ( OptionsInfo ) this.infoMap.get( "max_connected_servers" );
				return new Integer( option.getValue() ).intValue();
			}
		}
		return 0;
	}
}

/*
$Log: OptionsInfoMap.java,v $
Revision 1.23  2003/10/28 11:07:32  lemmster
move NetworkInfo.Enum -> enum.EnumNetwork
add MaskMatcher for "Enum[]"

Revision 1.22  2003/08/23 15:21:37  zet
remove @author

Revision 1.21  2003/08/21 20:24:37  lemmster
adjustment for the latest devel core

Revision 1.20  2003/08/21 13:13:10  lemmster
cleanup in networkitem

Revision 1.19  2003/08/20 22:41:08  lemmster
bugfix for getMaxConnectedServers()

Revision 1.18  2003/08/20 22:20:08  lemmster
badconnect is display too. added some icons

Revision 1.17  2003/08/04 16:57:00  lemmstercvs01
better way to set the value

Revision 1.16  2003/08/03 20:00:14  lemmstercvs01
bugfix

Revision 1.15  2003/08/02 09:27:39  lemmstercvs01
added support for proto > 16

Revision 1.14  2003/08/01 17:21:19  lemmstercvs01
reworked observer/observable design, added multiversion support

Revision 1.13  2003/07/09 08:55:19  lemmstercvs01
bugfix for missing optionInfos

Revision 1.12  2003/07/07 18:30:29  dek
saving options now also works

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