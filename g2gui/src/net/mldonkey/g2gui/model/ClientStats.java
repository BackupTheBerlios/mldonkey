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
 * @version $Id: ClientStats.java,v 1.8 2003/06/27 10:43:11 dek Exp $ 
 *
 */
public class ClientStats implements SimpleInformation {
	
	private CoreCommunication parent;
	/**
	 * @param core my parent, to send messages to
	 */
	public ClientStats( CoreCommunication core ) {
		
		this.parent = core;
	}

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
	private int[] connectedNetworks;
	
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
	 * @return an int[]
	 */
	public int[] getConnectedNetworks() {
		return connectedNetworks;
	}

	/**
	 * @return an int
	 */
	public int getNumCurrDownload() {
		return numCurrDownload;
	}

	/**
	 * @return an int
	 */
	public int getNumOfShare() {
		return numOfShare;
	}

	/**
	 * @return an int
	 */
	public float getTcpDownRate() {
		return tcpDownRate;
	}

	/**
	 * @return an int
	 */
	public float getTcpUpRate() {
		return tcpUpRate;
	}

	/**
	 * @return a long
	 */
	public long getTotalDown() {
		return totalDown;
	}

	/**
	 * @return a long
	 */
	public long getTotalShared() {
		return totalShared;
	}

	/**
	 * @return a long
	 */
	public long getTotalUp() {
		return totalUp;
	}

	/**
	 * @return an int
	 */
	public float getUdpDownRate() {
		return udpDownRate;
	}

	/**
	 * @return an int
	 */
	public float getUdpUpRate() {
		return udpUpRate;
	}

	/**
	 * @param is an int[]
	 */
	public void setConnectedNetworks( int[] is ) {
		connectedNetworks = is;
	}

	/**
	 * @param i an int
	 */
	public void setNumCurrDownload( int i ) {
		numCurrDownload = i;
	}

	/**
	 * @param i an int
	 */
	public void setNumOfShare( int i ) {
		numOfShare = i;
	}

	/**
	 * @param i an int
	 */
	public void setTcpDownRate( float i ) {
		tcpDownRate = i;
	}

	/**
	 * @param i an int
	 */
	public void setTcpUpRate( float i ) {
		tcpUpRate = i;
	}

	/**
	 * @param l a long
	 */
	public void setTotalDown( long l ) {
		totalDown = l;
	}

	/**
	 * @param l a long
	 */
	public void setTotalShared( long l ) {
		totalShared = l;
	}

	/**
	 * @param l a long
	 */
	public void setTotalUp( long l ) {
		totalUp = l;
	}

	/**
	 * @param i an int
	 */
	public void setUdpDownRate( float i ) {
		udpDownRate = i;
	}

	/**
	 * @param i an int
	 */
	public void setUdpUpRate( float i ) {
		udpUpRate = i;
	}

	/**
	 * @return an int
	 */
	public int getNumDownloadFinished() {
		return numDownloadFinished;
	}

	/**
	 * @param i an int
	 */
	public void setNumDownloadFinished( int i ) {
		numDownloadFinished = i;
	}

	/**
	 * Reads a ClientState object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {		
		this.setTotalUp( messageBuffer.readInt64() );
		this.setTotalDown( messageBuffer.readInt64() );
		this.setTotalShared( messageBuffer.readInt64() );
		this.setNumOfShare( messageBuffer.readInt32() );
		this.setTcpUpRate( round( messageBuffer.readInt32()/(float)1024 ) );		
		this.setTcpDownRate( round( messageBuffer.readInt32()/(float)1024 ) );
		this.setUdpUpRate( messageBuffer.readInt32() );
		this.setUdpDownRate( messageBuffer.readInt32() );
		this.setNumCurrDownload( messageBuffer.readInt32() );
		this.setNumDownloadFinished( messageBuffer.readInt32() );
		this.setConnectedNetworks( messageBuffer.readInt32List() );		
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