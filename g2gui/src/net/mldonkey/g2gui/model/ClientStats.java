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
import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * ClientStats
 *
 * @author markus
 * @version $Id: ClientStats.java,v 1.12 2003/07/06 10:05:17 lemmstercvs01 Exp $ 
 *
 */
public class ClientStats extends Parent {

	private long totalUp;
	private long totalDown;
	private long totalShared;
	private int numOfShare;
	private float tcpUpRate;
	private float tcpDownRate;
	private float udpUpRate;
	private float udpDownRate;
	private int numCurrDownload;
	private int numDownloadFinished;
	private NetworkInfo[] connectedNetworks;
	
	/**
	 * Creates a new ClientStats object
	 * @param core The parent corecommunication
	 */
	public ClientStats( CoreCommunication core ) {
		super( core );
	}
	
	/**
	 * A string representation of this object
	 *  @return a String
	 */
	public String toString() {
		String result = "";
		
		result += this.getClass();
		result += "\n Total Up: " + this.getTotalUp();
		result += "\n Total Down: " + this.getTotalDown();
		result += "\n Total Shared: " + this.getTotalShared();
		result += "\n Num Shares: " + this.getNumOfShare();
		result += "\n Tcp Up: " + this.getTcpUpRate();
		result += "\n Tcp Down: " + this.getTcpDownRate();
		result += "\n Udp Up: " + this.getUdpUpRate();
		result += "\n Udp Down: " + this.getUdpDownRate();
		result += "\n Finished: " + this.getNumDownloadFinished();

		return result;
	}

	/**
	 * @return The connected networks
	 */
	public NetworkInfo[] getConnectedNetworks() {
		return connectedNetworks;
	}

	/**
	 * @return The number of current downloads
	 */
	public int getNumCurrDownload() {
		return numCurrDownload;
	}

	/**
	 * @return The number of shares
	 */
	public int getNumOfShare() {
		return numOfShare;
	}

	/**
	 * @return The tcp download rate
	 * (rounded to two decimal places)
	 */
	public float getTcpDownRate() {
		return tcpDownRate;
	}

	/**
	 * @return The tcp upload rate
	 * (rounded to two decimal places)
	 */
	public float getTcpUpRate() {
		return tcpUpRate;
	}

	/**
	 * @return The traffic total downloaded
	 */
	public long getTotalDown() {
		return totalDown;
	}

	/**
	 * @return The traffic total shared
	 */
	public long getTotalShared() {
		return totalShared;
	}

	/**
	 * @return The total traffic uploaded
	 */
	public long getTotalUp() {
		return totalUp;
	}

	/**
	 * @return The udp download rate
	 * (rounded to two decimal places)
	 */
	public float getUdpDownRate() {
		return udpDownRate;
	}

	/**
	 * @return The udp upload rate
	 * (rounded to two decimal places)
	 */
	public float getUdpUpRate() {
		return udpUpRate;
	}

	/**
	 * @return Number of finished downloads
	 */
	public int getNumDownloadFinished() {
		return numDownloadFinished;
	}

	/**
	 * Reads a ClientState object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {		
		this.totalUp = messageBuffer.readInt64();
		this.totalDown = messageBuffer.readInt64();
		this.totalShared = messageBuffer.readInt64();
		this.numOfShare = messageBuffer.readInt32();
		this.tcpUpRate = round( messageBuffer.readInt32() / ( float )1024 );		
		this.tcpDownRate = round( messageBuffer.readInt32() / ( float )1024 );
		this.udpUpRate = round( messageBuffer.readInt32() / ( float )1024 );
		this.udpDownRate = round( messageBuffer.readInt32() / ( float )1024 );
		this.numCurrDownload = messageBuffer.readInt32();
		this.numDownloadFinished = messageBuffer.readInt32();

		/* create the ConnectedNetworks[] */
		int listElem = messageBuffer.readInt16();

		if ( listElem < 0 ) //check, if this is a big unsigned int, and fix it:
			listElem = ( int )( Short.MAX_VALUE * 2 + listElem ) + 2;

		NetworkInfo[] temp = new NetworkInfo[ listElem ];
		for ( int i = 0; i < listElem; i++ ) {
			temp[ i ] = ( NetworkInfo ) this.parent.getNetworkInfoMap()
							.infoIntMap.get( messageBuffer.readInt32() );		
		}
		this.connectedNetworks = temp;
	}
	
	/**
	 * Rounds a double to two decimal places
	 * @param d The double to round
	 * @return a rounden double
	 */
	public static float round( float d ) {
		d = ( float )( Math.round( d * 100 ) ) / 100;
		return d;
	}
}

/*
$Log: ClientStats.java,v $
Revision 1.12  2003/07/06 10:05:17  lemmstercvs01
int[] connectedNetworks -> NetworkInfo[] connectedNetworks

Revision 1.11  2003/07/02 16:24:14  dek
Checkstyle

Revision 1.10  2003/06/27 17:12:36  lemmstercvs01
removed unneeded fields

Revision 1.9  2003/06/27 13:38:38  dek
UL/DL rates in (float)  kb/s

Revision 1.8  2003/06/27 10:43:11  dek
TCP UP/DOWN finally rounded to 2 decimals !!!!

Revision 1.7  2003/06/26 21:11:35  dek
TCP UP/DOWN rounded to 2 decimals

Revision 1.6  2003/06/20 15:15:22  dek
humm, some interface-changes, hope, it didn't break anything ;-)

Revision 1.5  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.4  2003/06/14 12:47:27  lemmstercvs01
checkstyle applied

Revision 1.3  2003/06/13 11:03:41  lemmstercvs01
changed InputStream to MessageBuffer

Revision 1.2  2003/06/12 22:23:06  lemmstercvs01
lots of changes

Revision 1.1  2003/06/11 12:54:44  lemmstercvs01
initial commit

*/