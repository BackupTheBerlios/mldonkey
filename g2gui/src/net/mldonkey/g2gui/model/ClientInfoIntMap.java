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

import gnu.trove.TIntObjectHashMap;
import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.helper.ObjectWeakMap;
import net.mldonkey.g2gui.model.enum.EnumClientType;


/**
 * ClientInfoList
 *
 *
 * @version $Id: ClientInfoIntMap.java,v 1.12 2003/11/26 07:42:06 zet Exp $
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
    public ClientInfoIntMap(CoreCommunication communication) {
        super(communication);
    }

    /**
     * Creates a new ClientInfoList
     */
    public ClientInfoIntMap() {
        super();
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
            clientInfo = new ClientInfo(this.parent);

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
        }

        this.infoIntMap = tempClientInfoList;
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
}


/*
$Log: ClientInfoIntMap.java,v $
Revision 1.12  2003/11/26 07:42:06  zet
protocolVersion 19/timer

Revision 1.11  2003/09/18 09:16:47  lemmster
checkstyle

Revision 1.10  2003/09/13 22:22:59  zet
weak sets

Revision 1.9  2003/08/23 15:21:37  zet
remove @author

Revision 1.8  2003/08/22 21:03:15  lemmster
replace user with Author: lemmster

Revision 1.7  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.6  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging

Revision 1.5  2003/08/01 13:48:04  lemmstercvs01
removed debug

Revision 1.4  2003/07/06 09:33:50  lemmstercvs01
int networkid -> networkinfo networkid

Revision 1.3  2003/06/20 15:15:22  dek
humm, some interface-changes, hope, it didn't break anything ;-)

Revision 1.2  2003/06/19 08:40:47  lemmstercvs01
checkstyle applied

Revision 1.1  2003/06/16 21:47:19  lemmstercvs01
just refactored (name changed)

Revision 1.10  2003/06/16 20:13:06  dek
debugging code removed

Revision 1.9  2003/06/16 18:21:41  dek
NPE fix

Revision 1.8  2003/06/16 18:05:20  dek
refactored cleanTable

Revision 1.7  2003/06/16 17:46:57  dek
added clean(messageBuffer);

Revision 1.6  2003/06/16 13:18:59  lemmstercvs01
checkstyle applied

Revision 1.5  2003/06/15 16:18:41  lemmstercvs01
new interface introduced

Revision 1.4  2003/06/15 13:14:06  lemmstercvs01
fixed a bug in put()

Revision 1.3  2003/06/14 23:04:08  lemmstercvs01
change from interface to abstract superclass

Revision 1.2  2003/06/14 20:30:44  lemmstercvs01
cosmetic changes

*/
