/*
 * Copyright 2003
 * g2gui Team
 * 
 * 
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.comm;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import net.mldonkey.g2gui.helper.ObjectPool;
import net.mldonkey.g2gui.model.*;

/**
 * CoreMessage
 *
 * @author markus
 * @version $Id: CoreMessage.java,v 1.7 2003/06/12 18:15:20 lemmstercvs01 Exp $ 
 *
 */
public class CoreMessage extends Message {
	
	/**
	 * Reads a Download from an InputStream
	 * @param inputStream Stream to read from
	 * @return Download
	 * @throws IOException Error if read on stream failed
	 */
	public static FileInfo readDownload( InputStream inputStream, FileInfo aDownload ) throws IOException {
		aDownload.setId( readInt32( inputStream ) );
		aDownload.setNetwork( readInt32( inputStream ) );
		aDownload.setNames( readStringList( inputStream ) );
		aDownload.setMd4( readBinary( inputStream, 16 ) );
		aDownload.setSize( readInt32( inputStream ) );
		aDownload.setDownloaded( readInt32( inputStream ) );
		aDownload.setSources( readInt32( inputStream ) );
		aDownload.setClients( readInt32( inputStream ) );
		
		/* File State */
		State state = new State();
		state.setState( ( byte ) readByte( inputStream ) );
		if ( state.getState() == 6 )
			state.setReason( readString( inputStream ) );			
		aDownload.setState( state );
		
		aDownload.setChunks( readString( inputStream ) );
		aDownload.setAvail( readString( inputStream ) );
		aDownload.setRate( new Float( readString( inputStream ) ).floatValue() );
		aDownload.setChunkage( readStringList( inputStream ) );
		aDownload.setAge( readString( inputStream ) );
		
		/* File Format */
		Format format = new Format();
		format.setFormat( (  byte ) readByte( inputStream ) );
		if ( format.getFormat() == 1 ) {
			format.setExtension( readString( inputStream ) );
			format.setKind( readString( inputStream ) );
		}
		else if ( format.getFormat() == 2 ) {
			format.setCodec( readString( inputStream ) );
			format.setVwidth( readInt32( inputStream ) );
			format.setVheight( readInt32( inputStream ) );
			format.setVfps( readInt32( inputStream ) );
			format.setVrate( readInt32( inputStream ) );
		}
		else if ( format.getFormat() == 3 ) {
			format.setTitle( readString( inputStream ) );
			format.setArtist( readString( inputStream ) );
			format.setAlbum( readString( inputStream ) );
			format.setYear( readString( inputStream ) );
			format.setComment( readString( inputStream ) );
			format.setTracknum( readInt32( inputStream ) );
			format.setGenre( readInt32( inputStream ) );
		}
		aDownload.setFormat( format );
		
		aDownload.setName( readString( inputStream ) );
		aDownload.setOffset( readInt32( inputStream ) );
		aDownload.setPriority( readInt32( inputStream ) );
	
		return aDownload;
	
	}
	
	/**
	 * Reads a List of running Downloads
	 * @param inputStream Stream to read from
	 * @return a List filled with complete Downloads
	 * @throws IOException Error if read from stream failed
	 */
	public static FileInfoList readDownloadingList( InputStream inputStream, ObjectPool aPool ) throws IOException {
		FileInfoList result =  new FileInfoList();
		short listElem = readInt16( inputStream );
		for ( int i = 0; i < listElem; i++ ) {
			result.add( readDownload( inputStream, ( FileInfo ) aPool.checkOut() ) );
		}
		return result;
	}
	
	/**
	 * Reads a List of finished Downloads
	 * @param inputStream Stream to read from
	 * @return a List filled with complete Downloads
	 * @throws IOException Error ir read from stream failed
	 */
	public static FileInfoList readDownloadedList( InputStream inputStream, ObjectPool aPool ) throws IOException {
		return readDownloadingList( inputStream, aPool );
	}
	
	/**
	 * Reads a ClientState object from an InputStream
	 * @param inputStream Stream to read from
	 * @return a ClientState object
	 * @throws IOException Error if read from stream failed
	 */
	public static ClientStats readClientStats( InputStream inputStream ) throws IOException {
		ClientStats clientStats = new ClientStats();
		
		clientStats.setTotalUp( readInt64( inputStream ) );
		clientStats.setTotalDown( readInt64( inputStream ) );
		clientStats.setTotalShared( readInt64( inputStream ) );
		clientStats.setNumOfShare( readInt32( inputStream ) );
		clientStats.setTcpUpRate( readInt32( inputStream ) );
		clientStats.setTcpDownRate( readInt32( inputStream ) );
		clientStats.setUdpUpRate( readInt32( inputStream ) );
		clientStats.setUdpDownRate( readInt32( inputStream ) );
		clientStats.setNumCurrDownload( readInt32( inputStream ) );
		clientStats.setNumDownloadFinished(  readInt32( inputStream ) );
		clientStats.setConnectedNetworks( readInt32List( inputStream ) );
		
		return clientStats;
	}
	
	public static FileAddSource readFileAddSource( InputStream inputStream ) throws IOException {
		FileAddSource fileAddSource =  new FileAddSource();
		fileAddSource.setId( readInt32( inputStream ) );
		fileAddSource.setSourceid( readInt32( inputStream ) );
		return fileAddSource;
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.Message#setMessage(short, java.lang.Object[])
	 */
	public void setMessage(short opCode, Object[] content) {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.Message#setMessage(short)
	 */
	public void setMessage(short opCode) {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.comm.Message#sendMessage(java.net.Socket)
	 */
	public boolean sendMessage(Socket connection) {
		// do nothing
		return false;
	}
}

/*
$Log: CoreMessage.java,v $
Revision 1.7  2003/06/12 18:15:20  lemmstercvs01
changed SocketPool to ObjectPool

Revision 1.6  2003/06/12 10:36:58  lemmstercvs01
FileAddSource added

Revision 1.5  2003/06/12 07:40:22  lemmstercvs01
fixed bug in file info decoding

Revision 1.4  2003/06/11 20:43:45  lemmstercvs01
setMd4() fixed

Revision 1.3  2003/06/11 16:41:20  lemmstercvs01
still a problem in setMd4()

Revision 1.2  2003/06/11 15:32:33  lemmstercvs01
still in progress

Revision 1.1  2003/06/11 12:56:10  lemmstercvs01
moved from model -> comm

Revision 1.1  2003/06/10 16:22:19  lemmstercvs01
create a class hierarchy for message

*/