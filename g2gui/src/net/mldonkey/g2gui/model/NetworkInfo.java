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

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * NetworkInfo
 *
 * @author $user$
 * @version $Id: NetworkInfo.java,v 1.16 2003/08/04 15:06:24 lemmstercvs01 Exp $ 
 *
 */
public class NetworkInfo extends Parent {
	/**
	 * File network identifier
	 */
	private int network;
	/**
	 * Network name 
	 */	
	private String	networkName;
	/**
	 *Network activated?
	 */	
	private boolean enabled;	
	/**
	 * Name of network config file 
	 */	
	private String configFile;
	/**
	 * Number of bytes uploaded on network 
	 */	
	private long uploaded;
	/**
	 * Number of bytes downloaded on network
	 */
	private long downloaded;
	/**
	 * Represents the network type
	 */
	private Enum networkType;
	/**
	 * The number of servers we are currently
	 * connected to and the prev value
	 */
	private int connectedServers;
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
	public NetworkInfo( CoreCommunication core ) {
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
		 */
		this.network = messageBuffer.readInt32();
		this.networkName = messageBuffer.readString();
		this.setEnabled( messageBuffer.readInt8() );
		this.configFile = messageBuffer.readString();
		this.uploaded = messageBuffer.readInt64();
		this.downloaded = messageBuffer.readInt64();
		
		if ( parent.getProtoToUse() >= 18 )
			this.connectedServers = messageBuffer.readInt32();
		
		/* set the networktype by networkname */
		this.setNetworkType( this.networkName );
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
	 * @return Does this network use servers
	 */
	public boolean hasServers() {
		/* bittorrent has no servers */
		if ( this.networkType == Enum.BT ) 
			return false;
		/* all others do */	
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
		if ( this.networkType == Enum.DONKEY )
			return "DK";
		else if ( this.networkType == Enum.FT )
			return "FT";
		else if ( this.networkType == Enum.SOULSEEK )
			return "SS";
		else if ( this.networkType == Enum.BT )
			return "BT";
		else if ( this.networkType == Enum.OV )
			return "OV";
		else if ( this.networkType == Enum.GNUT )
			return "G1";
		else if ( this.networkType == Enum.GNUT2 )
			return "G2";
		else if ( this.networkType == Enum.DC )
			return "DC";
		else if ( this.networkType == Enum.OPENNP )
			return "OFT";
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
		return connectedServers;
	}

	/**
	 * @param b a short
	 */
	private void setEnabled( short b ) {
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
	 * @param bool true == enable/false == disable
	 */
	public void setEnabled() {
		Object[] temp = new Object[ 2 ];
		temp[ 0 ] = new Integer( this.getNetwork() );
		if ( this.isEnabled() ) 
			temp[ 1 ] = disable;
		else 
			temp[ 1 ] = enable;
		EncodeMessage netinfo =
			new EncodeMessage( Message.S_ENABLE_NETWORK, temp );
		netinfo.sendMessage( this.parent.getConnection() );
		netinfo = null;
		temp = null;			
	}
	
	/**
	 * @return The type of this network
	 */
	public Enum getNetworkType() {
		return networkType;
	}

	/**
	 * @param string the network name, to deceide which network
	 */
	private void setNetworkType( String string ) {
		if ( string.equals( "Donkey" ) )
			networkType = Enum.DONKEY;
		else if ( string.equals( "Fasttrack" ) )
			networkType = Enum.FT;
		else if ( string.equals( "Soulseek" ) )
			networkType = Enum.SOULSEEK;
		else if ( string.equals( "BitTorrent" ) )
			networkType = Enum.BT;
		else if ( string.equals( "Overnet" ) )
			networkType = Enum.OV;
		else if ( string.equals( "Gnutella" ) )
			networkType = Enum.GNUT;
		else if ( string.equals( "Gnutella2" ) )
			networkType = Enum.GNUT2;	
		else if ( string.equals( "Direct Connect" ) )
			networkType = Enum.DC;
		else if ( string.equals( "Open Napster" ) )
			networkType = Enum.OPENNP;
	}
	
	/**
	 * @param i The new amount of connected servers for this network
	 */
	protected void setConnectedServers( int i ) {
		this.connectedServers = i;
	}
	
	/**
	 * Creates a new EnumNetwork obj to differ each network
	 */
	public static class Enum {
		/**
		 * eDonkey2000
		 */
		public static Enum DONKEY = new Enum();
		/**
		 * SoulSeek
		 */
		public static Enum SOULSEEK = new Enum();
		/**
		 * Gnutella 1
		 */
		public static Enum GNUT = new Enum();
		/**
		 * Gnutella 2
		 */
		public static Enum GNUT2 = new Enum();
		/**
		 * Overnet
		 */
		public static Enum OV = new Enum();
		/**
		 * Bittorrent
		 */
		public static Enum BT = new Enum();
		/**
		 * Fasttrack
		 */
		public static Enum FT = new Enum();
		/**
		 * OpenNapster
		 */
		public static Enum OPENNP = new Enum();
		/**
		 * DirectConnect
		 */
		public static Enum DC  = new Enum();
		
		/**
		 * Creates a new EnumNetwork
		 */
		private Enum() { }
	}
}



/*
$Log: NetworkInfo.java,v $
Revision 1.16  2003/08/04 15:06:24  lemmstercvs01
bugfix

Revision 1.15  2003/08/02 09:55:16  lemmstercvs01
observers changed

Revision 1.14  2003/08/01 17:21:19  lemmstercvs01
reworked observer/observable design, added multiversion support

Revision 1.13  2003/07/31 14:08:54  lemmstercvs01
added gnutella2, edited setEnable(), added getNetworkShortName()

Revision 1.12  2003/07/30 19:26:19  lemmstercvs01
hasServers() added, use it to exclude networks in servertab without servers

Revision 1.11  2003/07/28 08:18:17  lemmstercvs01
added NetworkInfo.Enum to differ the networks

Revision 1.10  2003/07/06 08:49:33  lemmstercvs01
better oo added

Revision 1.9  2003/07/06 07:45:26  lemmstercvs01
checkstyle applied

Revision 1.8  2003/07/05 15:58:05  lemmstercvs01
javadoc improved

Revision 1.7  2003/07/04 18:35:02  lemmstercvs01
foobar

Revision 1.6  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.5  2003/06/16 15:33:45  dek
checkstyle applied

Revision 1.4  2003/06/16 15:21:01  dek
*** empty log message ***

Revision 1.3  2003/06/14 17:41:03  lemmstercvs01
foobar

Revision 1.2  2003/06/14 12:47:27  lemmstercvs01
checkstyle applied

Revision 1.1  2003/06/13 16:02:11  dek
this Class holds informationa about the different networks

*/