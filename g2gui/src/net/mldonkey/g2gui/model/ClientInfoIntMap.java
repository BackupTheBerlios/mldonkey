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
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.helper.ObjectWeakMap;
import net.mldonkey.g2gui.model.enum.EnumClientType;
import net.mldonkey.g2gui.view.G2Gui;

import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;
import gnu.trove.TObjectProcedure;

import java.util.Iterator;


/**
 * ClientInfoList
 *
 *
 * @version $Id: ClientInfoIntMap.java,v 1.17 2003/12/15 19:57:24 dek Exp $
 */
public class ClientInfoIntMap extends InfoIntMap {
    /**
     * A weak subset of ClientInfo's that are friends
     */
    private ObjectWeakMap friendsWeakMap = new ObjectWeakMap();

    /**
     * A weak subset of ClientInfo's that are uploaders
     */
    private ObjectWeakMap uploadersWeakMap = new ObjectWeakMap();

    /**
     * @param communication my parent
     */
    ClientInfoIntMap(CoreCommunication communication) {
        super(communication);
    }

    /**
     * Reads a clientInfo object from the stream
     * If the clientID exists, use the existing clientInfo object or we get dups
     * @param messageBuffer The MessageBuffer to read from
     */
    public void readStream(MessageBuffer messageBuffer) {
        int clientID = messageBuffer.readInt32();
        ClientInfo clientInfo;

        if (this.containsKey(clientID))
            clientInfo = this.get(clientID);
        else
            clientInfo = parent.getModelFactory().getClientInfo();

        clientInfo.readStream(clientID, messageBuffer);
        this.put(clientInfo.getClientid(), clientInfo);
    }

    /**
     * Store a key/value pair in this object
     * @param key The Key
     * @param value The ClientInfo object
     */
    public void put(int key, ClientInfo value) {
        this.infoIntMap.put(key, value);

        if (value.getClientType() == EnumClientType.FRIEND)
            friendsWeakMap.add(value);
        else
            friendsWeakMap.remove(value);

        if (value.isUploader() && value.isConnected())
            uploadersWeakMap.addOrUpdate(value);
        else
            uploadersWeakMap.remove(value);
    }

    /**
     * Get a ClientInfo object from this object by there id
     * @param key The ClientInfo id
     * @return The ClientInfo object
     */
    public ClientInfo get(int key) {
        return (ClientInfo) this.infoIntMap.get(key);
    }

    /**
     * Update a specific element in the List
     * @param messageBuffer The MessageBuffer to read from
     */
    public void update(MessageBuffer messageBuffer) {
        int key = messageBuffer.readInt32();

        if (this.infoIntMap.contains(key))
            ((ClientInfo) this.infoIntMap.get(key)).update(messageBuffer);
    }

    /**
     * @param messageBuffer
     * cleans up the used Trove-collections. Fills 'em with data the core (mldonkey) finds useful
     */
    public void clean(MessageBuffer messageBuffer) {
        TIntObjectHashMap tempClientInfoList = new TIntObjectHashMap();
        int[] usefulClients = messageBuffer.readInt32List();

        for (int i = 0; i < usefulClients.length; i++) {
            int clientID = usefulClients[ i ];

            if (this.containsKey(clientID)) // necessary check

                tempClientInfoList.put(clientID, this.get(clientID));
             /* only have useless clients in Map afterwards: */
            	infoIntMap.remove(clientID);
        }
        
        /*now iterate over useless clients in infoIntMap to remove them from all files*/
        
        TIntObjectHashMap files;
        if (G2Gui.debug){
	        /*
	         * ONLY DEGUGGING
	         */
	        
	        files = parent.getModelFactory().getFileInfoIntMap().infoIntMap;
	        files.forEachValue(new TObjectProcedure(){
	
				public boolean execute(Object arg0) {
					FileInfo file =(FileInfo) arg0; 
					System.out.println("before clean: \t"+file.getName()+"\t"+file.getSources());
					return true;
				}});
	        
	        /*
	         * DEBUGGIN FINISHED
	         */
        }
        
        
        infoIntMap.forEachValue(new TObjectProcedure(){
			public boolean execute(Object clientObj) {				
				ClientInfo client = (ClientInfo) clientObj;				
				TIntObjectHashMap files = parent.getModelFactory().getFileInfoIntMap().infoIntMap;
				TIntObjectIterator iterator = files.iterator();
				for (int i = files.size(); i-- > 0;) {    
					iterator.advance();     
					( ( FileInfo ) iterator.value()).removeClientInfo(client);					
				} 
				return true;
			}});
        
        /*now eversthing is done, useful clean list becomes default*/
        this.infoIntMap = tempClientInfoList;
        
        if (G2Gui.debug){
	        /*
	         * ONLY DEGUGGING
	         */
	        files = parent.getModelFactory().getFileInfoIntMap().infoIntMap;       
	        files.forEachValue(new TObjectProcedure(){
	
	        	public boolean execute(Object arg0) {
	        		FileInfo file =(FileInfo) arg0; 
	        		System.out.println("after clean: \t"+file.getName()+"\t"+file.getSources());
	        		return true;
	        	}});
	        /*
	         * DEBUGGIN FINISHED
	         */
        }
        
    }

    /**
     * @return friendsWeakMap
     */
    public ObjectWeakMap getFriendsWeakMap() {
        return friendsWeakMap;
    }

    /**
     * @return uploadersWeakMap
     */
    public ObjectWeakMap getUploadersWeakMap() {
        return uploadersWeakMap;
    }

    /**
     * @param core
     */
    public void updateUploaders(CoreCommunication core) {
		Message upstats;
        
        //	This is sort of crazy, but it is how new_gui does it...
        synchronized (uploadersWeakMap) {
            Iterator i = uploadersWeakMap.getKeySet().iterator();
            

            while (i.hasNext()) {
                ClientInfo clientInfo = (ClientInfo) i.next();
                Object[] num = new Object[ 1 ];
                num[ 0 ] = new Integer(clientInfo.getClientid());
                upstats = new EncodeMessage(Message.S_GET_CLIENT_INFO, num);
                upstats.sendMessage(core);
            }
        }
		upstats = null;
        
    }
}


/*
$Log: ClientInfoIntMap.java,v $
Revision 1.17  2003/12/15 19:57:24  dek
'Sources-Leak' reduced

Revision 1.16  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.15  2003/12/01 14:22:17  lemmy
ProtocolVersion handling completely rewritten

Revision 1.14  2003/11/27 00:12:10  zet
sync

Revision 1.13  2003/11/26 15:48:09  zet
minor

Revision 1.12  2003/11/26 07:42:06  zet
protocolVersion 19/timer

Revision 1.11  2003/09/18 09:16:47  lemmy
checkstyle

Revision 1.10  2003/09/13 22:22:59  zet
weak sets

Revision 1.9  2003/08/23 15:21:37  zet
remove @author

Revision 1.8  2003/08/22 21:03:15  lemmy
replace user with Author: lemmy

Revision 1.7  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.6  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging

Revision 1.5  2003/08/01 13:48:04  lemmy
removed debug

Revision 1.4  2003/07/06 09:33:50  lemmy
int networkid -> networkinfo networkid

Revision 1.3  2003/06/20 15:15:22  dek
humm, some interface-changes, hope, it didn't break anything ;-)

Revision 1.2  2003/06/19 08:40:47  lemmy
checkstyle applied

Revision 1.1  2003/06/16 21:47:19  lemmy
just refactored (name changed)

Revision 1.10  2003/06/16 20:13:06  dek
debugging code removed

Revision 1.9  2003/06/16 18:21:41  dek
NPE fix

Revision 1.8  2003/06/16 18:05:20  dek
refactored cleanTable

Revision 1.7  2003/06/16 17:46:57  dek
added clean(messageBuffer);

Revision 1.6  2003/06/16 13:18:59  lemmy
checkstyle applied

Revision 1.5  2003/06/15 16:18:41  lemmy
new interface introduced

Revision 1.4  2003/06/15 13:14:06  lemmy
fixed a bug in put()

Revision 1.3  2003/06/14 23:04:08  lemmy
change from interface to abstract superclass

Revision 1.2  2003/06/14 20:30:44  lemmy
cosmetic changes

*/
