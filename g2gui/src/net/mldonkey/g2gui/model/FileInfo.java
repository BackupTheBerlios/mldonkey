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

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.model.enum.EnumPriority;

/**
 * Download
 *
 * @author markus
 * @version $Id: FileInfo.java,v 1.34 2003/08/06 17:45:18 zet Exp $ 
 *
 */
public class FileInfo extends Parent implements Observer {
	/**
	 * File identifier
	 */
	private int id;
	/**
	 * File network identifier
	 */
	private NetworkInfo network;
	/**
	 * Possible file names
	 */
	private String[] names;
	/**
	 * File md4
	 */
	private String md4;
	/**
	 * File size
	 */
	private int size;
	/**
	 * Size already downloaded
	 */
	private int downloaded;
	/**
	 * Number of sources
	 */
	private int sources;
	/**
	 * Number of clients
	 */
	private int clients;
	/**
	 * Chunks
	 */
	private String chunks;
	private int numChunks;
	/**
	 * Availibility
	 */
	private String avail;
	/**
	 * Availibility for each network
	 * only in protocol > 17
	 */
	private Map avails;
	/**
	 * Download rate
	 */
	private float rate;
	private float rawRate;
	/**
	 * File name
	 */
	private String name;
	/**
	 * File priority inside mldonkey
	 */
	private Enum priority;
	/**
	 * File last seen
	 */
	private int offset;
	private String stringOffset;
	/**
	 * last time each chunk has been seen
	 */
	private String[] chunkage;
	/**
	 * when download started
	 */
	private String age;
	private String stringAge;
	/**
	 * File Format object
	 */
	private Format format = new Format();
	/**
	 * File State object
	 */
	private FileState state = new FileState();
	/**
	 * Percent (Downloaded/Size)*100
	 */
	private double perc;
	/**
	 * Which clients this file have
	 */
	private Set clientInfos = Collections.synchronizedSet( new HashSet() );

	private String stringSize;
	private String stringDownloaded;
	private String stringETA;
	private long etaSeconds;

	
	/**
	 * @return time when download started
	 */
	public String getAge() {
		return age;
	}
	/**
	 * @return The file availibility
	 */
	public String getAvail() {
		return avail;
	}
	/**
	 * @return last time each chunk has been seen
	 */
	public String[] getChunkage() {
		return chunkage;
	}
	/**
	 * @return Chunks
	 */
	public String getChunks() {
		return chunks;
	}
	public int getNumChunks() {
		return numChunks;
	}
	
	/**
	 * @return Size already downloaded
	 */
	public long getDownloaded() {
		/*
		 * NOTE: downloaded is an unsigned integer and should
		 * be AND'ed with 0xFFFFFFFFL so that it can be treated
		 * as a signed long.
		 */
		long result = ( downloaded & 0xFFFFFFFFL );
		return result;
	}
	/**
	 * @return The file Format
	 */
	public Format getFormat() {
		return format;
	}
	/**
	 * @return The file identifier
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return The file md4
	 */
	public String getMd4() {
		return md4;
	}
	/**
	 * @return The name of this file
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return Possible names for this file
	 */
	public String[] getNames() {
		return names;
	}
	/**
	 * @return File network identifier
	 */
	public NetworkInfo getNetwork() {
		return network;
	}
	/**
	 * @return File last seen
	 */
	public int getOffset() {
		return offset;
	}
	/**
	 * @return File priority
	 */
	public Enum getPriority() {
		return priority;
	}
	
	// localise ..
	public String getStringPriority() {
		if (priority == EnumPriority.HIGH)
			return "High";
		else if (priority == EnumPriority.LOW)
			return "Low";
		else if (priority == EnumPriority.NORMAL)
			return "Normal";
		else 
			return "???";
	}
	/**
	 * @return The rate this file is downloading
	 * 			(rounded to two decimal places)
	 */
	public float getRate() {
		return rate;
	}
	/**
	 * @return The overall size of this file
	 */
	public long getSize() {
		/*
		 * NOTE: size is an unsigned integer and should
		 * be AND'ed with 0xFFFFFFFFL so that it can be treated
		 * as a signed long.
		 */
		long result = ( size & 0xFFFFFFFFL );
		return result;
	}
	/**
	 * @return Number of sources for this file
	 */
	public int getSources() {
		return sources;
	}
	/**
	 * @return File status
	 */
	public FileState getState() {
		return state;
	}
	/**
	 * @return Number of clients receiving this file
	 */
	public int getClients() {
		return clients;
	}
	/**
	 * @return The percent this file is complete
	 */
	public double getPerc() {
		return perc;
	}
	/**
	 * @return The clients serving this file
	 */
	public Set getClientInfos() {
		return clientInfos;
	}

	/**
	 * Creates a new fileinfo object
	 * @param core The CoreCommunication parent
	 */
	public FileInfo( CoreCommunication core ) {
		super( core );
	}

	/**
	 * Reads a FileInfo object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		/*
		* int32			File Identifier 
		* int32			File Network Identifier 
		* List of String	Possible File Names 
		* char[16]		File Md4 (binary) 
		* int32			File Size 
		* int32			File Downloaded 
		* int32			Number of Sources 
		* int32			Number of Clients 
		* FileState		Current State of the Download 
		* String		Chunks (one char by chunk '0'-'3') 
		* String		Availability (one char by chunk, 0-255 sources) 
		* Float			Download Rate 
		* List of Time	Chunks Ages (last time each chunk has been seen) 
		* Time			File Age (when download started) 
		* FileFormat	File Format 
		* String		File Preferred Name 
		* OffsetTime	File Last Seen 
		* int32			File Priority 
		*/ 
		this.id = messageBuffer.readInt32();
		this.setNetwork( messageBuffer.readInt32() );
		this.names = messageBuffer.readStringList();
		this.md4 = messageBuffer.readBinary( 16 );
		this.size = messageBuffer.readInt32();
		this.downloaded = messageBuffer.readInt32();
		this.sources = messageBuffer.readInt32();
		this.clients = messageBuffer.readInt32();
		
		/* File State */
		this.getState().readStream( messageBuffer );
		
		this.chunks = messageBuffer.readString();
		
		int cnt = 0;
		for (int i = 0; i < chunks.length(); i++) {
			if (chunks.charAt(i) == '2' || chunks.charAt(i) == '3')
				cnt++;
		}
		numChunks = cnt;

		/* read a list of int32(networkid) and string(avail) */
		if ( parent.getProtoToUse() > 17 ) {
			this.avails = new HashMap();
			int listElem = messageBuffer.readInt16();
			for ( int i = 0; i < listElem; i++ ) {
				int networkID = messageBuffer.readInt32();
				String aString = messageBuffer.readString();
				NetworkInfo network = parent.getNetworkInfoMap().get( networkID );
				this.avails.put( network, aString );
				this.avail = aString; //TODO just hack until overall avail is sent by the core
			}
		}
		else
			this.avail = messageBuffer.readString();
	
		/* translate to kb and round to two digits after comma */
		double d = new Double( messageBuffer.readString() ).doubleValue();
		this.rate = ( float ) round( d / 1024 );
		this.rawRate = ( float ) d;
		this.chunkage = messageBuffer.readStringList();
		this.age = messageBuffer.readString();
		
		/* File Format */
		this.getFormat().readStream( messageBuffer );
		String oldname = this.name;
		this.name = messageBuffer.readString();
		this.offset = messageBuffer.readInt32();
		this.setPriority( messageBuffer.readInt32() );
		double d2 = round( ( ( double ) this.getDownloaded() / ( double ) this.getSize() ) * 100 );
		this.perc = d2;
	
		this.stringSize = calcStringSize( this.size );
		this.stringDownloaded = calcStringSize( this.downloaded );

		if (rawRate == 0) this.etaSeconds = Long.MAX_VALUE;
		else this.etaSeconds = (long) ((getSize() - getDownloaded()) / (rawRate + 1));
		
		this.stringETA = calcStringOfSeconds ( this.etaSeconds );
		this.stringAge = calcStringOfSeconds ( System.currentTimeMillis() / 1000 - Long.parseLong(this.age)  );
		this.stringOffset = calcStringOfSeconds( this.offset );	
			
		if (this.state.getState() == EnumFileState.DOWNLOADING
			|| this.state.getState() == EnumFileState.PAUSED) {
				this.setChanged();
				this.notifyObservers( this );
		}
		
	
	}
	
	/**
	 * Update a FileInfo object
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		this.downloaded = messageBuffer.readInt32();
		/* translate to kb and round to two digits after comma */
		double d = new Double( messageBuffer.readString() ).doubleValue() / 1024;
		this.rate = ( float ) round( d );
		this.offset = messageBuffer.readInt32();
		double d2 = round( ( ( double ) this.getDownloaded() / ( double ) this.getSize() ) * 100 );
		this.perc = d2;
	}

	/**
	 * Put the client into this list of clientinfos
	 * @param clientInfo The clientInfo to put into this map
	 * @return true if adding is successful
	 */
	public boolean addClientInfo( ClientInfo clientInfo ) {
		boolean result = this.clientInfos.add( clientInfo );
		clientInfo.addObserver( this );
		this.setChanged();
		this.notifyObservers( clientInfo );
		return result;
	}
	
	/**
	 * Removes a clientinfo from this list of clientinfos
	 * @param clientInfo The clientinfo obj to remove
	 * @return true if removing is successful
	 */
	public boolean removeClientInfo( ClientInfo clientInfo ) {
		boolean result = this.clientInfos.remove( clientInfo );
		clientInfo.deleteObserver( this );
		this.setChanged();
		this.notifyObservers( clientInfo );
		return result;
	}
	
	/**
	 * Rounds a double to two decimal places
	 * @param d The double to round
	 * @return a rounden double
	 */
	private static double round( double d ) {
		d = ( double )( ( int )( d * 100 + 0.5 ) ) / 100;
		return d;
	}

	/**
	 * Compares two FileInfo objects by their md4
	 * @param fileInfo the fileinfo to compare with
	 * @return true if they are identical, otherwise false
	 */
	public boolean equals( FileInfo fileInfo ) {
		if ( this.getMd4().equalsIgnoreCase( fileInfo.getMd4() ) )
			return true;
		else
			return false;
	}
	
	/**
	 * translate the int to EnumPriority
	 * @param i the int
	 */
	private void setPriority( int i ) {
		if ( i < 0 )
			priority = EnumPriority.LOW;
		else if ( i > 0 )
			priority = EnumPriority.HIGH;
		else 
			priority = EnumPriority.NORMAL;		
	}
	
	/**
	 * translate the int to EnumNetwork
	 * @param i the int
	 */
	private void setNetwork( int i ) {
		this.network =
		( NetworkInfo ) this.parent.getNetworkInfoMap().infoIntMap.get( i );
	}

	/**
	 * @param string The new name for this file
	 */
	public void setName( String string ) {
		string = "rename " + this.getId() + " \""+ string + "\"";
		/* create the message content */
		EncodeMessage consoleMessage = new EncodeMessage( Message.S_CONSOLEMSG, string );
		consoleMessage.sendMessage( this.parent.getConnection() );
		consoleMessage = null;
	}

	/**
	 * @param enum The new priority for this file (LOW/NORMAL/HIGH)
	 */
	public void setPriority( EnumPriority enum ) {
		/* first the fileid */
		Object[] obj = new Object[ 2 ];
		obj[ 0 ] = new Integer( this.getId() );
		
		/* now the new prio */
		Integer content;
		if ( enum == EnumPriority.LOW )
			content = new Integer( -10 );
		else if ( enum == EnumPriority.HIGH )
			content = new Integer( 10 );
		else
			content = new Integer( 0 );
		obj[ 1 ] = content;		
		
		/* create and send the message */
		EncodeMessage consoleMessage = new EncodeMessage( Message.S_SET_FILE_PRIO, obj );
		consoleMessage.sendMessage( this.parent.getConnection() );
		content = null;
		obj = null;
		consoleMessage = null;
	}

	/**
	 * @param enum The new state of this file
	 */
	public void setState( EnumFileState enum ) {
		this.getState().setState( enum, this.getId(), this.parent );
	}
	
	/**
	 * Verify all chunks of this fileinfo
	 */
	public void verifyChunks() {
		EncodeMessage chunks =
			new EncodeMessage( Message.S_VERIFY_ALL_CHUNKS, new Integer( this.getId() ) );
		chunks.sendMessage( this.parent.getConnection() );
	}
	
	/**
	 * creates a String from the size
	 * @param size The size
	 * @return a string represantation of this size
	 */	
	private String calcStringSize( long size ) {
			float k = 1024f;
			float m = k * k;
			float g = m * k;
			float t = g * k;
		
			float fsize = (float) size;
		
			DecimalFormat df = new DecimalFormat( "0.#" );
			
			if ( fsize > t ) 
				return new String ( df.format(fsize / t) + " TB" );
			else if ( fsize > g ) 
				return new String ( df.format(fsize / g) + " GB" );	
			else if ( fsize > m ) 
				return new String ( df.format(fsize / m) + " MB" );
			else if ( fsize > k ) 
				return new String ( df.format(fsize / k) + " KB" );
			else
				return new String ( size + "" );	
	}
	public String getStringSize () {
		return stringSize;
	}
	public String getStringDownloaded () {
		return stringDownloaded;
	}
	private String calcStringOfSeconds (long inSeconds) {
		
		if (inSeconds < 1) return "";
				
		long days = inSeconds / 60 / 60 / 24;
		long rest = inSeconds - days * 60 * 60 * 24;
		long hours = rest / 60 / 60;
		rest = rest - hours * 60 * 60;
		long minutes = rest / 60;
		long seconds = rest - minutes * 60;
		
		if (days > 100) return "";
		if (days > 0) return "" + days + "d";
		if (hours > 0) return "" + hours + "h" + (minutes > 0 ? " " + minutes + "m" : "");
		return "" + minutes + "m" + (seconds > 0 ? " " + seconds + "s" : "");
	}
	public String getStringETA () {
		return stringETA;
	}

	public String getStringOffset () {
		return stringOffset;	
	}
	public String getStringAge() {
		return stringAge;
	}
	
	public long getETA() {
		return etaSeconds;
	}
	
	// hack
	public void update (Observable o, Object obj) {
		if (o instanceof ClientInfo) {
			
			ClientInfo clientInfo = (ClientInfo) o;
			this.setChanged();
			this.notifyObservers( clientInfo );
			
		}
	}
	
}	

/*
$Log: FileInfo.java,v $
Revision 1.34  2003/08/06 17:45:18  zet
fix rename

Revision 1.33  2003/08/06 17:09:48  zet
string types added

Revision 1.32  2003/08/05 15:38:29  lemmstercvs01
set obj=null after message is send

Revision 1.31  2003/08/05 08:55:04  lemmstercvs01
setPrio() fixed

Revision 1.30  2003/08/04 19:21:52  zet
trial tabletreeviewer

Revision 1.29  2003/08/04 16:56:16  lemmstercvs01
use the correct opcode for msg sending

Revision 1.28  2003/08/01 17:21:19  lemmstercvs01
reworked observer/observable design, added multiversion support

Revision 1.27  2003/07/31 17:10:14  zet
stringsize

Revision 1.26  2003/07/21 17:32:21  dek
added comment for calculating the signed long out of the unsigned int

Revision 1.25  2003/07/21 17:30:11  dek
filesize is not negative anymore, since it is converted to long

Revision 1.24  2003/07/18 04:34:22  lemmstercvs01
checkstyle applied

Revision 1.23  2003/07/11 08:39:46  dek
String should be compared with .equals, not with ==
(in this.equals())

Revision 1.22  2003/07/07 15:09:28  lemmstercvs01
HashSet() instead of ArrayList()

Revision 1.21  2003/07/06 12:47:22  lemmstercvs01
bugfix for fileUpdateAvailability

Revision 1.20  2003/07/06 12:31:45  lemmstercvs01
fileUpdateAvailability added

Revision 1.19  2003/07/06 11:56:36  lemmstercvs01
fileAddSource added

Revision 1.18  2003/07/06 08:57:09  lemmstercvs01
getParent() removed

Revision 1.17  2003/07/06 08:49:33  lemmstercvs01
better oo added

Revision 1.16  2003/07/05 15:46:52  lemmstercvs01
javadoc improved

Revision 1.15  2003/07/05 11:33:08  lemmstercvs01
id -> List ids

Revision 1.14  2003/07/04 18:35:02  lemmstercvs01
foobar

Revision 1.13  2003/07/04 11:04:14  lemmstercvs01
add some opcodes

Revision 1.12  2003/07/03 21:16:16  lemmstercvs01
setName() and setPriority() added, Priority from int to Enum

Revision 1.11  2003/07/03 16:01:51  lemmstercvs01
setState() works now to set the filestate on the mldonkey side

Revision 1.10  2003/07/02 16:25:30  dek
Checkstyle

Revision 1.9  2003/06/29 17:51:44  lemmstercvs01
added some doku

Revision 1.8  2003/06/26 09:10:54  lemmstercvs01
added field for percent, store rate rounded

Revision 1.7  2003/06/25 00:57:49  lemmstercvs01
equals methode added

Revision 1.6  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.5  2003/06/14 17:41:03  lemmstercvs01
foobar

Revision 1.4  2003/06/14 12:47:40  lemmstercvs01
update() added

Revision 1.3  2003/06/13 11:03:41  lemmstercvs01
changed InputStream to MessageBuffer

Revision 1.2  2003/06/12 22:23:06  lemmstercvs01
lots of changes

Revision 1.1  2003/06/12 18:14:04  lemmstercvs01
initial commit

Revision 1.4  2003/06/12 10:36:04  lemmstercvs01
toString() added

Revision 1.3  2003/06/12 07:40:04  lemmstercvs01
added number of clients

Revision 1.2  2003/06/11 15:32:33  lemmstercvs01
still in progress

Revision 1.1  2003/06/11 12:54:44  lemmstercvs01
initial commit

*/