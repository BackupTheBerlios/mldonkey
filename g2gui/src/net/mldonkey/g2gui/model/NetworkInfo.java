/*
 * Copyright 2003
 * G2GUI Team
 * 
 * 
 * This file is part of G2GUI.
 *
 * G2GUI is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2GUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2GUI; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.model;

import org.eclipse.swt.graphics.Image;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.model.enum.EnumNetwork;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

/**
 * NetworkInfo
 *
 *
 * @version $Id: NetworkInfo.java,v 1.34 2004/02/17 21:23:53 psy Exp $ 
 *
 */
public class NetworkInfo extends Parent {
	/**
	 * File network identifier
	 */
	protected int network;
	/**
	 * Network name 
	 */	
	protected String networkName;
	/**
	 *Network activated?
	 */	
	private boolean enabled;	
	/**
	 * Name of network config file 
	 */	
	protected String configFile;
	/**
	 * Number of bytes uploaded on network 
	 */	
	protected long uploaded;
	/**
	 * Number of bytes downloaded on network
	 */
	protected long downloaded;
	/**
	 * Represents the network type
	 */
	private EnumNetwork networkType;

	/**
	 * disable
	 */
	private static Byte disable = new Byte( ( byte ) 0 );
	/**
	 * enable
	 */
	private static Byte enable = new Byte( ( byte ) 1 );
	
	/**
	 * Creates a new NetworkInfo
	 * @param core The parent
	 */
	NetworkInfo( CoreCommunication core ) {
		super( core );
	}
	
	/**
	 * Reads a networkInfo object from the stream
	 * @param messageBuffer The buffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		/*
		 *	PayLoad:
		 *	int32	Network identifier (used in other messages for this network) 
		 *	String	Network name 
		 *	int8	Enabled(1) or Disabled(0) 
		 *	String	Name of network config file 
		 *	int64	Number of bytes uploaded on network 
		 *	int64	Number of bytes downloaded on network
		 *  
		 *  proto >= 18
		 *  int32   Number of connected servers
		 *  List of int16  Network flags
		 * 		0 : NetworkHasServers (well known servers) 
 		 *		1 : NetworkHasRooms (rooms to chat with other users) 
 		 *		2 : NetworkHasMultinet (files can be downloaded from several networks) 
		 *	 	3 : VirtualNetwork (not a real network) 
		 *	 	4 : NetworkHasSearch (searches can be issued) 
		 *		5 : NetworkHasChat (chat between two users) 
		 *		6 : NetworkHasSupernodes (peers can become servers) 
		 *	 	7 : NetworkHasUpload (upload is implemented) 
 		 */
		this.network = messageBuffer.readInt32();
		this.networkName = messageBuffer.readString();
		this.setEnabled( messageBuffer.readInt8() );
		this.configFile = messageBuffer.readString();
		this.uploaded = messageBuffer.readInt64();
		this.downloaded = messageBuffer.readInt64();
		
		/* set the networktype by networkname */
		this.setNetworkType( this.networkName );

		/* remove the servers from the serverlist if the network is disabled */
		if ( !this.isEnabled() )
			this.parent.getServerInfoIntMap().remove( this.getNetworkType() );
	}
	
	/**
	 * @return Traffic downloaded from this network
	 */
	public long getDownloaded() {
		return downloaded;
	}
	
	/**
	 * @return The state of this network
	 */
	public boolean isEnabled() {
		return enabled;
	}
	/**
	 * @return VirtualNetwork (not a real network)
	 */
	public boolean isVirtual() {
		// proto < 18 has no virtual networks
		return false;
	}
	/**
	 * @return NetworkHasRooms (rooms to chat with other users)
	 */	
	public boolean hasRooms() {
		/* soulseek and directconnect have rooms */
		if ( this.networkType == EnumNetwork.SOULSEEK
		|| this.networkType == EnumNetwork.DC ) 
			return true;
		return false;		
	}
	/**
	 * @return NetworkHasMultinet (files can be downloaded from several networks)
	 */
	public boolean isMultinet() {
		/* no multinet proto < 18 */
		return false;
	}
	/**
	 * @return NetworkHasChat (chat between two users)
	 */
	public boolean hasChat() {
		if ( this.networkType == EnumNetwork.DONKEY
		|| this.networkType == EnumNetwork.OV )
			return true;
		return false;
	}
	/**
	 * @return NetworkHasSupernodes (peers can become servers)
	 */
	public boolean hasSupernodes() {
		/* proto < 18 */
		if ( this.networkType == EnumNetwork.FT
		|| this.networkType == EnumNetwork.GNUT
		|| this.networkType == EnumNetwork.GNUT2 )
			return true;
		return false;	
	}
	/**
	 * @return NetworkHasUpload (upload is implemented)
	 */
	public boolean hasUpload() {
		if ( this.networkType == EnumNetwork.DONKEY
		|| this.networkType == EnumNetwork.OV
		|| this.networkType == EnumNetwork.BT
		|| this.networkType == EnumNetwork.GNUT
		|| this.networkType == EnumNetwork.DC )
			return true;
		return false;	
	}
	/**
	 * @return Does this network use servers
	 */
	public boolean hasServers() {
		/* proto < 18 */
		/* have no servers */
		if ( this.networkType == EnumNetwork.BT
		|| this.networkType == EnumNetwork.FT
		|| this.networkType == EnumNetwork.GNUT ) 
			return false;
		/* all others do */	
		return true;	
	}
	/**
	 * Is this network searchable
	 * @return true on searchable
	 */
	public boolean isSearchable() {
		/* proto < 18 */
		if ( this.networkType == EnumNetwork.BT )
			return false;
		return true;
	}

	/**
	 * @return The network identifier
	 */
	public int getNetwork() {
		return network;
	}

	/**
	 * @return The network name
	 */
	public String getNetworkName() {
		return networkName;
	}
	
	/**
	 * @return a short represantation of the networkname
	 */
	public String getNetworkShortName() {
		if ( this.networkType == EnumNetwork.DONKEY )
			return "DK";
		else if ( this.networkType == EnumNetwork.FT )
			return "FT";
		else if ( this.networkType == EnumNetwork.SOULSEEK )
			return "SS";
		else if ( this.networkType == EnumNetwork.BT )
			return "BT";
		else if ( this.networkType == EnumNetwork.OV )
			return "OV";
		else if ( this.networkType == EnumNetwork.GNUT )
			return "G1";
		else if ( this.networkType == EnumNetwork.GNUT2 )
			return "G2";
		else if ( this.networkType == EnumNetwork.DC )
			return "DC";
		else if ( this.networkType == EnumNetwork.OPENNP )
			return "ONP";
		else if ( this.networkType == EnumNetwork.FILETP )
			return "FILE";
		else if ( this.networkType == EnumNetwork.MULTINET )
			return "MULTI"; 
		else
			return "";	
	}

	/**
	 * @return Traffic uploaded to this network
	 */
	public long getUploaded() {
		return uploaded;
	}
	
	/**
	 * @return The number of connected Servers
	 */
	public int getConnectedServers() {
		return parent.getServerInfoIntMap().getConnected( this );	
	}

	/**
	 * @param b a short
	 */
	protected void setEnabled( short b ) {
		if ( b == 0 ) enabled = false;
		if ( b == 1 ) enabled = true;		
	}

	/**
	 * @return The configfile for this network
	 */
	public String getConfigFile() {
		return configFile;
	}

	/**
	 * Enable/Disable this network
	 */
	public void setEnabled() {
		Object[] temp = new Object[ 2 ];
		temp[ 0 ] = new Integer( this.getNetwork() );
		if ( this.isEnabled() ) 
			temp[ 1 ] = disable;
		else 
			temp[ 1 ] = enable;
		Message netinfo =
			new EncodeMessage( Message.S_ENABLE_NETWORK, temp );
		netinfo.sendMessage( this.parent );
		netinfo = null;
		temp = null;			
	}
	
	/**
	 * @return The type of this network
	 */
	public EnumNetwork getNetworkType() {
		return networkType;
	}

	/**
	 * @param string the network name, to deceide which network
	 */
	protected void setNetworkType( String string ) {
		if ( string.equals( "Donkey" ) )
			networkType = EnumNetwork.DONKEY;
		else if ( string.equals( "Fasttrack" ) )
			networkType = EnumNetwork.FT;
		else if ( string.equals( "Soulseek" ) )
			networkType = EnumNetwork.SOULSEEK;
		else if ( string.equals( "BitTorrent" ) )
			networkType = EnumNetwork.BT;
		else if ( string.equals( "Overnet" ) )
			networkType = EnumNetwork.OV;
		else if ( string.equals( "Gnutella" ) )
			networkType = EnumNetwork.GNUT;
		else if ( string.equals( "G2" ) )
			networkType = EnumNetwork.GNUT2;	
		else if ( string.equals( "Gnutella2" ) )
			networkType = EnumNetwork.GNUT2;
		else if ( string.equals( "Direct Connect" ) )
			networkType = EnumNetwork.DC;
		else if ( string.equals( "Open Napster" ) )
			networkType = EnumNetwork.OPENNP;
		else if ( string.equals( "MultiNet" ) )
			networkType = EnumNetwork.MULTINET;
		else if ( string.equals( "FileTP" ) )
			networkType = EnumNetwork.FILETP;
		else if ( string.equals( "Global Shares" ) )
			networkType = EnumNetwork.MULTINET;
	}
	
	/**
	 * @param i The new amount of connected servers for this network
	 */
	protected void setConnectedServers( int i ) {
		// just ignore: proto < 18 has no connected servers
	}

	/**
	 * set the proper image for the network
	 * @return an image representing the network status
	 */
	public Image getImage() {
		/* proto < 18 just support enabled/disabled */
		if ( parent.getProtoToUse() < 18 ) {
			if ( this.isEnabled() )
				return G2GuiResources.getImage( getNetworkShortName() + "Connected" );
			else
				return G2GuiResources.getImage( getNetworkShortName() + "Disabled" );
		}

		/* proto >= 18 is a little bit more complex */
		
		/* networks without servers/nodes and virutal nets have just this two images */		
		if ( ( !this.hasServers() && !this.hasSupernodes() ) || this.isVirtual() ) {
			if ( this.isEnabled() )
				return G2GuiResources.getImage( getNetworkShortName() + "Connected" );
			else
				return G2GuiResources.getImage( getNetworkShortName() + "Disabled" );
		}

		/* enabled */
		if ( this.isEnabled() ) {
			/* we need the max_connected_servers from the optionsinfo */	
			int maxConnnectedServers = parent.getOptionsInfoMap().getMaxConnectedServers( this );
			int currentConnectedServers = getConnectedServers();

			/* max_connected_servers == currentConnectedServers */
			if ( currentConnectedServers >= maxConnnectedServers )
				return G2GuiResources.getImage( getNetworkShortName() + "Connected" );
			/* max_connected_servers > currentConnectedServers */
			else if ( currentConnectedServers == 0 )
				return G2GuiResources.getImage( getNetworkShortName() + "Disconnected" );				
			/* connected to zero servers */
			else
				return G2GuiResources.getImage( getNetworkShortName() + "BadConnected" );			
		}
		/* disabled */
		else {
			return G2GuiResources.getImage( getNetworkShortName() + "Disabled" );					
		}
	}
	
	/**
	 * Compares this object with a NetworkInfo.Enum obj
	 * @param enum The NetworkInfo.Enum to compare with
	 * @return true on equals, false otherwise
	 */
	public boolean equals( EnumNetwork enum ) {
		if ( this.getNetworkType() == enum )
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */	
	public boolean equals( Object obj ) {
		NetworkInfo aNetworkInfo;
		if ( obj instanceof NetworkInfo )
			aNetworkInfo = ( NetworkInfo ) obj;
		else
			return false;
		
		if ( this.equals( aNetworkInfo.getNetworkType() ) )
			return true;
		else 
			return false;
	}
}

/*
$Log: NetworkInfo.java,v $
Revision 1.34  2004/02/17 21:23:53  psy
added FileTP as a new network

Revision 1.33  2003/12/04 09:32:16  lemmy
set the correct networktype for new gnutella2 and global shares

Revision 1.32  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.31  2003/12/01 14:22:17  lemmy
ProtocolVersion handling completely rewritten

Revision 1.30  2003/10/28 11:07:32  lemmy
move NetworkInfo.Enum -> enum.EnumNetwork
add MaskMatcher for "Enum[]"

Revision 1.29  2003/09/18 15:29:25  zet
centralize writeStream in core
handle IOException rather than throwing it away

Revision 1.28  2003/09/18 09:16:47  lemmy
checkstyle

Revision 1.27  2003/09/14 09:59:02  lemmy
fix  0 server in mouse-popup [bug #869]

Revision 1.26  2003/08/24 16:55:37  dek
toString added, returns network Name

Revision 1.25  2003/08/23 15:21:37  zet
remove @author

Revision 1.24  2003/08/23 10:02:02  lemmy
use supertype where possible

Revision 1.23  2003/08/22 21:03:15  lemmy
replace $user$ with $Author: psy $

Revision 1.22  2003/08/21 13:13:10  lemmy
cleanup in networkitem

Revision 1.21  2003/08/20 22:20:08  lemmy
badconnect is display too. added some icons

Revision 1.20  2003/08/19 12:15:05  lemmy
support for proto=18

Revision 1.19  2003/08/11 11:23:49  lemmy
added GNUT and FT to networks without servers and added isSearchable()

Revision 1.18  2003/08/10 10:19:58  lemmy
return ONP instead of OFT in getNetworkShortName()

Revision 1.17  2003/08/06 09:45:52  lemmy
remove servers from serverinfointmap if network switches to off

Revision 1.16  2003/08/04 15:06:24  lemmy
bugfix

Revision 1.15  2003/08/02 09:55:16  lemmy
observers changed

Revision 1.14  2003/08/01 17:21:19  lemmy
reworked observer/observable design, added multiversion support

Revision 1.13  2003/07/31 14:08:54  lemmy
added gnutella2, edited setEnable(), added getNetworkShortName()

Revision 1.12  2003/07/30 19:26:19  lemmy
hasServers() added, use it to exclude networks in servertab without servers

Revision 1.11  2003/07/28 08:18:17  lemmy
added NetworkInfo.Enum to differ the networks

Revision 1.10  2003/07/06 08:49:33  lemmy
better oo added

Revision 1.9  2003/07/06 07:45:26  lemmy
checkstyle applied

Revision 1.8  2003/07/05 15:58:05  lemmy
javadoc improved

Revision 1.7  2003/07/04 18:35:02  lemmy
foobar

Revision 1.6  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.5  2003/06/16 15:33:45  dek
checkstyle applied

Revision 1.4  2003/06/16 15:21:01  dek
*** empty log message ***

Revision 1.3  2003/06/14 17:41:03  lemmy
foobar

Revision 1.2  2003/06/14 12:47:27  lemmy
checkstyle applied

Revision 1.1  2003/06/13 16:02:11  dek
this Class holds informationa about the different networks

*/