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

/**
 * ClientStats
 *
 * @author markus
 * @version $Id: ClientStats.java,v 1.1 2003/06/11 12:54:44 lemmstercvs01 Exp $ 
 *
 */
public class ClientStats {
	
	private long totalUp;
	private long totalDown;
	private long totalShared;
	private int numOfShare;
	private int tcpUpRate;
	private int tcpDownRate;
	private int udpUpRate;
	private int udpDownRate;
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
	public int getTcpDownRate() {
		return tcpDownRate;
	}

	/**
	 * @return an int
	 */
	public int getTcpUpRate() {
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
	public int getUdpDownRate() {
		return udpDownRate;
	}

	/**
	 * @return an int
	 */
	public int getUdpUpRate() {
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
	public void setTcpDownRate( int i ) {
		tcpDownRate = i;
	}

	/**
	 * @param i an int
	 */
	public void setTcpUpRate( int i ) {
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
	public void setUdpDownRate( int i ) {
		udpDownRate = i;
	}

	/**
	 * @param i an int
	 */
	public void setUdpUpRate( int i ) {
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

}

/*
$Log: ClientStats.java,v $
Revision 1.1  2003/06/11 12:54:44  lemmstercvs01
initial commit

*/