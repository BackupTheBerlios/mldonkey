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
 * SharedFileInfoList
 *
 *
 * @version $Id: SharedFileInfoIntMap.java,v 1.4 2003/09/25 14:24:11 dek Exp $ 
 *
 */
public class SharedFileInfoIntMap extends InfoIntMap {
	
	/**
	 * @param communication my parent
	 */
	public SharedFileInfoIntMap( CoreCommunication communication ) {
		super( communication );		
	}
	
	/**
	 * Reads a SharedFileInfoList object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void readStream( MessageBuffer messageBuffer ) {		
		/*
		 * 	 int32  	 Shared File Identifier 
 		 * 	 int32  	 Network Identifier 
 		 * 	 String  	 Shared File Name 
 		 * 	 int32  	 Shared File Size 
		 * 	 int64  	 Number of Bytes Uploaded 
 		 * 	 int32  	 Number of Queries for that File 
 		 * 	 char[16]  	 Md4 
		 */
		int fileID = messageBuffer.readInt32();
		/* go 4bytes back in the MessageBuffer */
		messageBuffer.setIterator( messageBuffer.getIterator() - 4 );
		
		if ( this.infoIntMap.contains( fileID ) ) {
			this.update( messageBuffer );
		}
		else {
			SharedFileInfo sharedFileInfo = new SharedFileInfo();
			sharedFileInfo.readStream( messageBuffer );
			this.infoIntMap.put( fileID, sharedFileInfo );
			/*get the networkItem and assign it to this sharedInfo*/
			NetworkInfo temp = ( NetworkInfo ) this.parent.
							getNetworkInfoMap().infoIntMap.
							get( sharedFileInfo.getNetworkId() );
			sharedFileInfo.setNetwork( temp );
		}
		
	}

	/**
	 * updates the shared-Info with received Data.
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public void update( MessageBuffer messageBuffer ) {
		int fileID = messageBuffer.readInt32();		
		/* go 4bytes back in the MessageBuffer */
		messageBuffer.setIterator( messageBuffer.getIterator() - 4 );			
	
		//update existing NetworkInfo-Object
		SharedFileInfo sharedFileInfo = ( SharedFileInfo ) this.infoIntMap.get( fileID );
		if ( sharedFileInfo != null ) {
			if ( sharedFileInfo.update( messageBuffer ) ) { /*returns true, if Info has changed*/
					this.setChanged();
				}
		}
		else {
			/*unknown file -> do nothing*/
		}											
		this.notifyObservers( sharedFileInfo );		
	}
}

/*
$Log: SharedFileInfoIntMap.java,v $
Revision 1.4  2003/09/25 14:24:11  dek
sharedFile no has Network (not only networkID)

Revision 1.2  2003/09/18 09:16:47  lemmster
checkstyle

Revision 1.1  2003/09/17 13:49:09  dek
now the gui refreshes the upload-stats, add and observer to SharedFileInfoIntMap
to get notice of changes in # of requests and # of uploaded bytes

Revision 1.5  2003/08/23 15:21:37  zet
remove @author

Revision 1.4  2003/08/22 21:03:15  lemmster
replace $user$ with $Author: dek $

Revision 1.3  2003/07/05 20:04:02  lemmstercvs01
javadoc improved

Revision 1.2  2003/06/20 15:15:22  dek
humm, some interface-changes, hope, it didn't break anything ;-)

Revision 1.1  2003/06/15 16:17:26  lemmstercvs01
opcode 48 added

*/