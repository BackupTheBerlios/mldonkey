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

import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * NetworkInfo
 *
 * @author $user$
 * @version $Id: NetworkInfo.java,v 1.3 2003/06/14 17:41:03 lemmstercvs01 Exp $ 
 *
 */
public class NetworkInfo implements Information {
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
		this.setNetwork( messageBuffer.readInt32() );
		this.setNetworkName( messageBuffer.readString() );
		this.setEnabled( messageBuffer.readInt8() );
		this.setConfigFile( messageBuffer.readString() );
		this.setUploaded( messageBuffer.readInt64() );
		this.setDownloaded( messageBuffer.readInt64() );
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
	 * @param l a long
	 */
	public void setDownloaded( long l ) {
		downloaded = l;
	}

	/**
	 * @param b a short
	 */
	public void setEnabled( short b ) {
		if ( b == 0 ) enabled = false;
		if ( b == 1 ) enabled = true;		
	}

	/**
	 * @param i an int
	 */
	public void setNetwork( int i ) {
		network = i;
	}

	/**
	 * @param string a string
	 */
	public void setNetworkName( String string ) {
		networkName = string;
	}

	/**
	 * @param l a long
	 */
	public void setUploaded( long l ) {
		uploaded = l;
	}

	/**
	 * @return a string
	 */
	public String getConfigFile() {
		return configFile;
	}

	/**
	 * @param string a string
	 */
	public void setConfigFile( String string )  {
		configFile = string;
	}

}



/*
$Log: NetworkInfo.java,v $
Revision 1.3  2003/06/14 17:41:03  lemmstercvs01
foobar

Revision 1.2  2003/06/14 12:47:27  lemmstercvs01
checkstyle applied

Revision 1.1  2003/06/13 16:02:11  dek
this Class holds informationa about the different networks

*/