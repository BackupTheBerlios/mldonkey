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
import net.mldonkey.g2gui.model.enum.EnumNetwork;

/**
 * OptionsInfoMap18
 *
 * @version $Id: OptionsInfoMap18.java,v 1.2 2003/12/04 08:47:25 lemmy Exp $ 
 *
 */
public class OptionsInfoMap18 extends OptionsInfoMap {
	/**
	 * @param communication
	 */
	OptionsInfoMap18(CoreCommunication communication) {
		super(communication);
	}
	/**
	 * Gets the value max_connected_servers/ultrapeers for the given network
	 * @param network The network
	 * @return max_connected_servers/ultrapeers
	 */
	public int getMaxConnectedServers( NetworkInfo network ) {
		try {
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
		catch ( NullPointerException e ) {
			return 0;
		}	
	}
}

/*
$Log: OptionsInfoMap18.java,v $
Revision 1.2  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.1  2003/12/01 14:22:17  lemmy
ProtocolVersion handling completely rewritten

*/