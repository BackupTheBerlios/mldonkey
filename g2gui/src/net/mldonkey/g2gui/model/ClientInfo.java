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



import gnu.trove.THash;
import gnu.trove.TIntArrayList;
import gnu.trove.TIntObjectHashMap;
import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.model.enum.EnumClientMode;
import net.mldonkey.g2gui.model.enum.EnumClientType;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.G2Gui;
import net.mldonkey.g2gui.view.resource.G2GuiResources;


/**
 * ClientInfo
 *
 *
 * @version $Id: ClientInfo.java,v 1.47 2004/09/10 18:09:17 lemmy Exp $
 *
 */
public class ClientInfo extends Parent {
    /**
     * Client Id
     */
    protected int clientid;

    /**
     * Client Network Id
     */
    protected NetworkInfo clientNetwork;

    /**
     * Client Kind
     */
    private ClientKind clientKind = new ClientKind();

    /**
     * Client State
     */
    //private State state = parent.getModelFactory().getState(); 

    /**
     * Client Type
     */
    protected Enum clientType;

    /**
     * List of Tags
     */
    protected Tag[] tag;

    /**
     * Client Name
     */
    protected String clientName;

    /**
     * Client Rate
     */
    protected int clientRating;

    /**
     * Client Chat Port (mlchat)
     */
    private int clientChatPort;

    /**
     * Client Software
     */
    private String clientSoftware = G2Gui.emptyString;   

    /**
     * Client downloaded bytes
     */
    private long clientDownloaded = 0;
    private String clientDownloadedString = G2Gui.emptyString;

    /**
     * Client uploaded bytes
     */
    private long clientUploaded = 0;
    private String clientUploadedString = G2Gui.emptyString;
   
    /**
     * Filename being uploaded to client
     */
    private String clientUploadFilename = G2Gui.emptyString;

    /**
     * clientConnectTime
     */
    private int clientConnectTime;
    private String clientConnectTimeString = G2Gui.emptyString;

    /**
     * true if clientUploadFileName != ""
     */
    private boolean isUploader = false;

    /**
     * Availability of a file (int fileId, String availability)
     * small initial capacity
     */
    private THash avail = new TIntObjectHashMap(1);

	private TIntArrayList fileInfosTobeNotified;

	

	private int rank;
	/**
	 * the file this client is uploading to us -2 / -1 if 
	 */
	private int fileNumber = -2;

	protected Enum state;

    /**
     * @param core with this object, we make our main cimmunication (the main-Layer)
     */
    ClientInfo(CoreCommunication core) {
        super(core);
    }

    /**
     * @return The client chat port
     */
    public int getClientChatPort() {
        return clientChatPort;
    }

    /**
     * @return The client identifier
     */
    public int getClientid() {
        return clientid;
    }

    /**
     * @return The client kind
     */
    public ClientKind getClientKind() {
        return clientKind;
    }

    /**
     * @return The client name
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @return The client network identifier
     */
    public NetworkInfo getClientnetworkid() {
        return clientNetwork;
    }

    /**
     * @return This clients Rating (not used yet)
     */
    public int getClientRating() {
        return clientRating;
    }

    /**
     *returns a string with file-availability-Information
     * @param fileInfo which file do you wnat the availability for?
     * @return String representation of the avail.
     */
    public String getFileAvailability(FileInfo fileInfo) {
        return (String) ((TIntObjectHashMap) this.avail).get(fileInfo.getId());
    }

    /**
     * @param fileInfo The <code>FileInfo</code> obj
     * @return int numberOfFullChunks
     */
    public int getNumChunks(FileInfo fileInfo) {
        int numChunks = 0;
        String availability = getFileAvailability(fileInfo);

        if (availability != null) {
            for (int i = 0; i < availability.length(); i++)
                if (availability.charAt(i) == '1')
                    numChunks++;
        }

        return numChunks;
    }

    /**
     * @return The client type
     */
    public Enum getClientType() {
        return clientType;
    }

    /**
     * @return The client state
     */
    public Enum getState() {    	
        return state;
    }

    /**
     * @return The client tags
     */
    public Tag[] getTag() {
        return tag;
    }

    /**
     * @param b a byte
     */
    protected void setClientType(byte b) {
        if (b == 0)
            clientType = EnumClientType.SOURCE;
        else if (b == 1)
            clientType = EnumClientType.FRIEND;
        else if (b == 2)
            clientType = EnumClientType.BROWSED;
    }

    /**
     * @return String clientActivity
     */
    public String getClientActivity() {
        if (this.getState() == EnumState.CONNECTED_DOWNLOADING)
            return G2GuiResources.getString("TT_Transferring").toLowerCase();
        else

            return G2GuiResources.getString("TT_Rank").toLowerCase() + ": " +
            this.rank;
    }

    public boolean isConnected() {
        Enum enumState = this.getState();

        return ((enumState == EnumState.CONNECTED_DOWNLOADING) ||
        (enumState == EnumState.CONNECTED_INITIATING) ||
        (enumState == EnumState.CONNECTED_AND_QUEUED) || (enumState == EnumState.CONNECTED));
    }

    /**
    * @param clientInfo
    * @return String clientDetailedActivity
    */
    public String getDetailedClientActivity() {
        if ((this.getState() == EnumState.CONNECTED_DOWNLOADING) ||
                (this.rank == 0))
            return G2Gui.emptyString + this.getState().toString();
        else

            return G2Gui.emptyString + this.getState().toString() + " (Q: " +
            this.rank + ")";
    }

    /**
     * @return String clientConnection
     */
    public String getClientConnection() {
        if (this.getClientKind().getClientMode() == EnumClientMode.FIREWALLED)
            return G2GuiResources.getString("TT_Firewalled").toLowerCase();
        else

            return G2GuiResources.getString("TT_Direct").toLowerCase();
    }

    /**
     * @return clientSoftware
     */
    public String getClientSoftware() {
    	return G2Gui.emptyString;
    }

    /**
     * @return clientUploaded Bytes
     */
    public long getUploaded() {
    	return 0;
    }

    /**
     * @return clientDownloaded Bytes
     */
    public long getDownloaded() {
    	return 0;
    }

    /**
     * @return clientUploaded String
     */
    public String getUploadedString() {
    	return G2Gui.emptyString;
    }

    /**
     * @return clientDownloaded String
     */
    public String getDownloadedString() {
    	return G2Gui.emptyString;
    }
    
    /**
     * @return clientUploadFilename (Filename currently being uploaded to client)
     */
    public String getUploadFilename() {
    	return G2Gui.emptyString;
    }

    public int getClientConnectTime() {
    	return 0;
    }

    public String getClientConnectTimeString() {
        return G2Gui.emptyString;
    }

    public String getClientConnectTimePassedString() {
    	return G2Gui.emptyString;
    }
    
    
    /**
     * Reads a ClientInfo object from a MessageBuffer
     * @param messageBuffer The MessageBuffer to read from
     */
    public void readStream(MessageBuffer messageBuffer) {
        int clientID = messageBuffer.readInt32();
        readStream(clientID, messageBuffer);
    }

    /**
     *
     * @param clientID
     * @param messageBuffer
     */
    public void readStream(int clientID, MessageBuffer messageBuffer) {
        this.clientid = clientID;

        this.clientNetwork = (NetworkInfo) this.parent.getNetworkInfoMap().get(messageBuffer.readInt32());

        this.getClientKind().readStream(messageBuffer);

        Enum oldState = this.getState();
        //this.stateByte = me
        //this.getState().readStream(messageBuffer);
        readState(messageBuffer);
        this.setClientType(messageBuffer.readByte());
        this.tag = messageBuffer.readTagList();
        this.clientName = messageBuffer.readString();
        this.clientRating = messageBuffer.readInt32();
        this.clientChatPort = messageBuffer.readInt32();
        onChangedState(oldState);
    }

    /**
	 * @param messageBuffer
	 */
	protected void readState(MessageBuffer messageBuffer) {
		this.state = StateHandler.getStatefromByte(messageBuffer.readByte());
		
		
		if ( this.getState() == EnumState.CONNECTED_AND_QUEUED)
			this.rank = messageBuffer.readInt32();
		
		if ( this.getState() == EnumState.NOT_CONNECTED_WAS_QUEUED )
			this.rank = messageBuffer.readInt32();
		
	}

	public boolean isUploader() {
        return false;
    }

    /**
     * Updates the state of this object
     * @param messageBuffer The MessageBuffer to read from
     */
    public void update(MessageBuffer messageBuffer) {
        Enum oldState = getState();
        readState(messageBuffer);     

        onChangedState(oldState);
    }

    /**
    * @param oldState
    */
    public void onChangedState(Enum oldState) {
        Enum newState = getState();

        this.setChanged();

        if (oldState != newState) {
            if (newState == EnumState.CONNECTED_DOWNLOADING)
                this.notifyObservers(Boolean.TRUE);
            else if (oldState == EnumState.CONNECTED_DOWNLOADING)
                this.notifyObservers(Boolean.FALSE);
            else
                this.notifyObservers(this);
        } else
            this.notifyObservers(this);
    }

    /**
     * Adds the availability of a file into this list of availability
     * @param fileId The fileId
     * @param avail The availability of this file
     */
    public void putAvail(int fileId, String avail) {
        ((TIntObjectHashMap) this.avail).put(fileId, avail);
        this.setChanged();
        this.notifyObservers(this);
    }

    /**
     * Adds a friend to the list of friends
     * @param core The core the list is stored at
     * @param id The friend id
     */
    public static void addFriend(CoreCommunication core, int id) {
        Message addFriend = new EncodeMessage(Message.S_ADD_CLIENT_FRIEND, new Integer(id));
        addFriend.sendMessage(core);
        addFriend = null;
    }

    /**
     * Removes a friend from the list of friends
     * @param core The core the list is stored at
     * @param id The friend id
     */
    public static void removeFriend(CoreCommunication core, int id) {
        Message removeFriend = new EncodeMessage(Message.S_REMOVE_FRIEND, new Integer(id));
        removeFriend.sendMessage(core);
        removeFriend = null;
    }

    /**
     * Removes all friends from the list of friends
     * @param core The core the list is stored at
     */
    public static void removeAllFriends(CoreCommunication core) {
        Message removeAllFriends = new EncodeMessage(Message.S_REMOVE_ALL_FRIENDS);
        removeAllFriends.sendMessage(core);
        removeAllFriends = null;
    }

	/**
	 * @return
	 */
	public int getRank() {		
		return rank;
	}    
	
	
	
	public int getFileNumber(){
		return -1;
	}
    

	
}


/*
$Log: ClientInfo.java,v $
Revision 1.47  2004/09/10 18:09:17  lemmy
use the get(int i) method of networkinfointmap instead of working on the TIntObjectMap directly

Revision 1.46  2004/03/26 18:11:03  dek
some more profiling and mem-saving option (hopefully)  introduced

Revision 1.45  2004/03/25 19:25:23  dek
yet more profiling

Revision 1.44  2004/03/25 18:07:24  dek
profiling

Revision 1.43  2004/03/21 21:00:50  dek
implemented gui-Proto 21-25 !!!!!

Revision 1.42  2004/03/20 01:34:02  dek
implemented gui-Proto 25 !!!!!

Revision 1.41  2003/12/19 22:15:27  psy
added human readable value for connectiontime passed since connection initiation

Revision 1.40  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.39  2003/12/01 16:17:53  zet
*** empty log message ***

Revision 1.38  2003/12/01 16:04:52  lemmy
removed broken cvs tag

Revision 1.37  2003/12/01 14:43:45  lemmy
ProtocolVersion handling completely rewritten

Revision 1.36  2003/12/01 13:28:02  zet
updates for ipaddr

Revision 1.35  2003/11/30 23:42:56  zet
updates for latest mldonkey cvs

Revision 1.34  2003/11/28 08:23:28  lemmy
use Addr instead of String

Revision 1.33  2003/11/26 07:46:36  zet
init variables (maybe needed w proto < 19?)

Revision 1.32  2003/11/26 07:42:07  zet
protocolVersion 19/timer

Revision 1.31  2003/11/08 22:47:06  zet
update client table header

Revision 1.30  2003/10/23 05:11:56  zet
try to fix rare duplicate treeclientinfos

Revision 1.29  2003/09/24 03:09:57  zet
add # of active sources column

Revision 1.28  2003/09/18 15:29:25  zet
centralize writeStream in core
handle IOException rather than throwing it away

Revision 1.27  2003/09/18 09:16:47  lemmy
checkstyle

Revision 1.26  2003/09/18 03:52:12  zet
smaller map initial capacities

Revision 1.25  2003/09/14 03:37:24  zet
changedProperties in model

Revision 1.24  2003/09/13 22:22:59  zet
weak sets

Revision 1.23  2003/09/02 09:24:36  lemmy
checkstyle

Revision 1.22  2003/08/24 02:22:12  zet
*** empty log message ***

Revision 1.21  2003/08/23 15:21:37  zet
remove @author

Revision 1.20  2003/08/23 10:02:02  lemmy
use supertype where possible

Revision 1.19  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.18  2003/08/22 21:03:15  lemmy
replace $user$ with $Author: lemmy $

Revision 1.17  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.16  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging

Revision 1.15  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.14  2003/08/04 19:21:52  zet
trial tabletreeviewer

Revision 1.13  2003/08/03 17:34:45  zet
notify observers

Revision 1.12  2003/07/15 18:17:27  dek
checkstyle & javadoc

Revision 1.11  2003/07/12 14:11:51  dek
made the ClientInfo-availability easier

Revision 1.10  2003/07/12 13:59:39  dek
availability is strange: it doesn't seem to make it's way to clients...

Revision 1.9  2003/07/12 13:53:21  dek
some new methods, to be able to show the information

Revision 1.8  2003/07/06 12:47:22  lemmy
bugfix for fileUpdateAvailability

Revision 1.7  2003/07/06 09:33:50  lemmy
int networkid -> networkinfo networkid

Revision 1.6  2003/06/30 07:20:09  lemmy
changed to readTagList()

Revision 1.5  2003/06/24 09:29:57  lemmy
Enum more improved

Revision 1.4  2003/06/24 09:16:48  lemmy
better Enum added

Revision 1.3  2003/06/18 13:30:56  dek
Improved Communication Layer view <--> model by introducing a super-interface

Revision 1.2  2003/06/16 15:33:03  lemmy
some kind of enum added

Revision 1.1  2003/06/14 17:40:40  lemmy
initial commit

*/
