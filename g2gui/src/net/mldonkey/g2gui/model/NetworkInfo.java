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
 * @version $Id: NetworkInfo.java,v 1.7 2003/07/04 18:35:02 lemmstercvs01 Exp $ 
 *
 */
public class NetworkInfo implements SimpleInformation {
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
	 * Thsi parent
	 */
	private CoreCommunication parent;
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
	 * @param parent
	 */
	public NetworkInfo( CoreCommunication parent ) {
		this.parent = parent; 
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
	}
	
	/**
	 * @return a long
	 */
	public long getDownloaded() {
		return downloaded;
	}

	/**
	 * @return a boolean
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @return an int
	 */
	public int getNetwork() {
		return network;
	}

	/**
	 * @return a string
	 */
	public String getNetworkName() {
		return networkName;
	}

	/**
	 * @return a long
	 */
	public long getUploaded() {
		return uploaded;
	}

	/**
	 * @param b a short
	 */
	private void setEnabled( short b ) {
		if ( b == 0 ) enabled = false;
		if ( b == 1 ) enabled = true;		
	}

	/**
	 * @return a string
	 */
	public String getConfigFile() {
		return configFile;
	}

	/**
	 * Enable/Disable this network
	 * @param bool true == enable/false == disable
	 */
	public void setEnabled( boolean bool ) {
		Object[] temp = new Object[ 2 ];
		temp[ 0 ] = new Integer( this.getNetwork() );
		if ( bool ) 
			temp[ 1 ] = enable;
		else 
			temp[ 1 ] = disable;
		EncodeMessage netinfo =
			new EncodeMessage( Message.S_ENABLE_NETWORK, temp );
		netinfo.sendMessage( this.parent.getConnection() );
		netinfo = null;
		temp = null;			
	}
}



/*
$Log: NetworkInfo.java,v $
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