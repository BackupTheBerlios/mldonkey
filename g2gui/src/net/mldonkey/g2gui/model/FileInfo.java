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

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.TreeSet;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.helper.ObjectWeakMap;
import net.mldonkey.g2gui.helper.RegExp;
import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.model.enum.EnumExtension;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.model.enum.EnumPriority;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.transfer.TreeClientInfo;


/**
 * FileInfo
 *
 * @version $Id: FileInfo.java,v 1.83 2003/12/01 14:22:17 lemmster Exp $
 *
 */
public class FileInfo extends Parent implements Observer {
    /**
     * Static strings used internally for tableviewer updates
     */
    public static final String CHANGED_RATE = "rate";
    public static final String CHANGED_DOWNLOADED = "downloaded";
    public static final String CHANGED_PERCENT = "percent";
    public static final String CHANGED_ETA = "eta";
    public static final String CHANGED_LAST = "last";
    public static final String CHANGED_AVAIL = "avail";
    public static final String CHANGED_ACTIVE = "active";
    public static final String[] ALL_PROPERTIES = {
        CHANGED_RATE, CHANGED_DOWNLOADED, CHANGED_PERCENT, CHANGED_AVAIL, CHANGED_ETA, CHANGED_LAST,
        CHANGED_ACTIVE
    };

    /**
     * A set (no duplicates) of changed properties
     */
    private Set changedProperties = Collections.synchronizedSet(new HashSet());

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
     * Number of active sources
     */
    private int activeSources;

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
    protected String avail;
    private int relativeAvail = 0;

    /**
     * Download rate
     */
    private float rate;

    /**
     * File name
     */
    private String name;

    /**
     * File priority inside mldonkey
     */
    private Enum priorityEnum;
    private int priority;

    /**
     * File last seen
     */
    private int offset;
    private String stringOffset = "";

    /**
     * last time each chunk has been seen
     */
    private String[] chunkAges;

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
    private int perc;

    /**
     * A set of active clients associated with this file
     */
    private Set treeClientInfoSet = Collections.synchronizedSet(new HashSet());

    /**
     * A weak keyset of clients associated with this file
     */
    private ObjectWeakMap clientInfoWeakMap = new ObjectWeakMap();

    /**
     * String size
     */
    private String stringSize = "";

    /**
     * String downloaded
     */
    private String stringDownloaded = "";

    /**
     * String ETA
     */
    private String stringETA = "";

    /**
     * ETA seconds
     */
    private long etaSeconds;

    /**
     * Creates a new fileinfo object
     * @param core The CoreCommunication parent
     */
    FileInfo(CoreCommunication core) {
        super(core);
    }

    /**
     * @return String time when download started
     */
    public String getAge() {
        return age;
    }

    /**
     * @return String The file availability
     */
    public String getAvail() {
        return avail;
    }
    
    public boolean hasAvails() {
    	return false;
    }
    
    public String getAvails( NetworkInfo network ) {
    	return "";
    }
    
    public Set getAllAvailNetworks() {
    	return new TreeSet();
    }

    /**
     * @return int relative availability %
     */
    public int getRelativeAvail() {
        return relativeAvail;
    }

    /**
     * set the relative avail
     */
    public void setRelativeAvail() {
        int oldRelativeAvail = relativeAvail;
        relativeAvail = 0;

        int neededChunks = 0;
        int availChunks = 0;

        if ((avail.length() > 0) && (avail.length() == chunks.length())) {
            for (int i = 0; i < avail.length(); i++) {
                if ((chunks.charAt(i) == '0') || (chunks.charAt(i) == '1')) {
                    neededChunks++;

                    if (avail.charAt(i) > 0)
                        availChunks++;
                }
            }

            if (neededChunks > 0)
                relativeAvail = (int) (((float) availChunks / (float) neededChunks) * 100f);
        }

        if (oldRelativeAvail != relativeAvail)
            changedProperties.add(CHANGED_AVAIL);
    }

    /**
     * @return last time each chunk has been seen
     */
    public String[] getChunkAges() {
        return chunkAges;
    }

    /**
     * @return Chunks
     */
    public String getChunks() {
        return chunks;
    }

    /**
        * @return numChunks
        */
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
        long result = (downloaded & 0xFFFFFFFFL);

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
     * @return File priorityEnum
     */
    public Enum getPriorityEnum() {
        return priorityEnum;
    }

    /**
    * @return File Priority
    */
    public int getPriority() {
        return priority;
    }

    /**
     * @return Priority as a string
     */
    public String getStringPriority() {
        if (priority > 19)
            return G2GuiResources.getString("TT_PRIO_Very_High") + "(" + priority + ")";
        else if (priority > 0)
            return G2GuiResources.getString("TT_PRIO_High") + "(" + priority + ")";
        else if (priority < -19)
            return G2GuiResources.getString("TT_PRIO_Very_Low") + "(" + priority + ")";
        else if (priority < 0)
            return G2GuiResources.getString("TT_PRIO_Low") + "(" + priority + ")";
        else if (priority == 0)
            return G2GuiResources.getString("TT_PRIO_Normal");
        else

            return "?" + priority;
    }

    /**
     * @return The rate this file is downloading
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
        long result = (size & 0xFFFFFFFFL);

        return result;
    }

    /**
     * @return Number of sources for this file
     */
    public int getSources() {
        // return sources; 
        // TODO: use "sources" when core sends it
        return clientInfoWeakMap.size();
    }

    /**
     * @return number of actively transferring sources
     */
    public int getActiveSources() {
        return activeSources;
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
    public int getPerc() {
        return perc;
    }

    /**
     * @return The clients serving this file
     */
    public ObjectWeakMap getClientInfoWeakMap() {
        return clientInfoWeakMap;
    }

    /**
     * @return treeClientInfoSet
     */
    public Set getTreeClientInfoSet() {
        return treeClientInfoSet;
    }

    public Enum getFileType() {
        int index = name.lastIndexOf(".");
        String extension = name.substring(index + 1, name.length());
        Enum aEnum = (Enum) Parent.FILE_TYPES.get(extension);

        if (aEnum != null)
            return aEnum;
        else

            return EnumExtension.UNKNOWN;
    }

    /**
     * Reads a FileInfo object from a MessageBuffer
     * @param messageBuffer The MessageBuffer to read from
     */
    public void readStream(MessageBuffer messageBuffer) {
        /*
        * int32                        File Identifier
        * int32                        File Network Identifier
        * List of String        Possible File Names
        * char[16]                File Md4 (binary)
        * int32                        File Size
        * int32                        File Downloaded
        * int32                        Number of Sources
        * int32                        Number of Clients
        * FileState                Current State of the Download
        * String                Chunks (one char by chunk '0'-'3')
        * String                Availability (one char by chunk, 0-255 sources)
        * Float                        Download Rate
        * List of Time        Chunks Ages (last time each chunk has been seen)
        * Time                        File Age (when download started)
        * FileFormat        File Format
        * String                File Preferred Name
        * OffsetTime        File Last Seen
        * int32                        File Priority
        */
        this.id = messageBuffer.readInt32();
        this.setNetwork(messageBuffer.readInt32());
        this.names = messageBuffer.readStringList();
        this.md4 = messageBuffer.readBinary(16);
        this.size = messageBuffer.readInt32();
        setDownloaded(messageBuffer.readInt32());
        this.sources = messageBuffer.readInt32();
        this.clients = messageBuffer.readInt32();

        /* File State */
        Enum oldState = this.state.getState();
        this.getState().readStream(messageBuffer);

        if (oldState != this.state.getState())
            changedProperties.add(CHANGED_RATE);

        setChunks(messageBuffer.readString());
        setAvailability(messageBuffer);
        setRate(new Double(messageBuffer.readString()).doubleValue()); // use float?
        this.chunkAges = messageBuffer.readStringList();
        this.age = messageBuffer.readString();

        /* File Format */
        this.getFormat().readStream(messageBuffer);
        this.name = messageBuffer.readString();
        setOffset(messageBuffer.readInt32());
        this.setPriority(messageBuffer.readSignedInt32());
        this.stringSize = RegExp.calcStringSize(this.getSize());
        updateETA();
        this.stringAge = RegExp.calcStringOfSeconds((System.currentTimeMillis() / 1000) -
                Long.parseLong(this.age));
        notifyChangedProperties();
    }

    /**
     * Update a FileInfo object
     *
     * int32        Downloaded
     * Float        Rate
     * int32        Number of seconds since last seen
     *
     * @param messageBuffer The MessageBuffer to read from
     */
    public void update(MessageBuffer messageBuffer) {
        setDownloaded(messageBuffer.readInt32());
        setRate(new Double(messageBuffer.readString()).doubleValue());
        setOffset(messageBuffer.readInt32());
        updateETA();
        notifyChangedProperties();
    }

    /**
     * notify tableviewer of the changed properties
     */
    public void notifyChangedProperties() {
        String[] changed = new String[ changedProperties.size() ];
        changedProperties.toArray(changed);
        changedProperties.clear();
        this.setChanged();
        this.notifyObservers(changed);
    }

    /**
     * Put the client into this list of clientinfos
     * @param clientInfo The clientInfo to put into this map
     */
    public void addClientInfo(ClientInfo clientInfo) {
        this.clientInfoWeakMap.add(clientInfo);
        clientInfo.addObserver(this);

        if (clientInfo.getState().getState() == EnumState.CONNECTED_DOWNLOADING) {
            setActiveSources(+1);

            if (findTreeClientInfo(clientInfo) == null)
                treeClientInfoSet.add(new TreeClientInfo(this, clientInfo));
        }

        this.setChanged();
        this.notifyObservers(clientInfo);
    }

    /**
     * Removes a clientinfo from this list of clientinfos
     * @param clientInfo The clientinfo obj to remove
         */
    public void removeClientInfo(ClientInfo clientInfo) {
        this.clientInfoWeakMap.remove(clientInfo);
        clientInfo.deleteObserver(this);
        removeTreeClientInfo(clientInfo);
        setActiveSources(0);
        this.setChanged();
        this.notifyObservers(clientInfo);
    }

    /**
     * Compares two FileInfo objects by their md4
     * @param fileInfo the fileinfo to compare with
     * @return true if they are identical, otherwise false
     */
    public boolean equals(FileInfo fileInfo) {
        if (this.getMd4().equalsIgnoreCase(fileInfo.getMd4()))
            return true;
        else

            return false;
    }

    /**
     * translate the int to EnumPriority
     * @param i the int
     */
    private void setPriority(int i) {
        priority = i;

        if (i < -19)
            priorityEnum = EnumPriority.VERY_LOW;
        else if (i < 0)
            priorityEnum = EnumPriority.LOW;
        else if (i > 19)
            priorityEnum = EnumPriority.VERY_HIGH;
        else if (i > 0)
            priorityEnum = EnumPriority.HIGH;
        else
            priorityEnum = EnumPriority.NORMAL;
    }

    /**
     * translate the int to EnumNetwork
     * @param i the int
     */
    private void setNetwork(int i) {
        this.network = (NetworkInfo) this.parent.getNetworkInfoMap().infoIntMap.get(i);
    }

    /**
     * @param messageBuffer
     *
     * read a list of int32(networkid) and string(avail)
     */
    protected void setAvailability(MessageBuffer messageBuffer) {
        this.avail = messageBuffer.readString();
        setRelativeAvail();
    }

    /**
     * set chunks string and count numChunks
     *
     * @param string
     */
    private void setChunks(String s) {
        this.chunks = s;
        numChunks = 0;

        for (int i = 0; i < chunks.length(); i++) {
            if ((chunks.charAt(i) == '2') || (chunks.charAt(i) == '3'))
                numChunks++;
        }
    }

    /**
     * set downloaded
     *
     * @param int
     */
    private void setDownloaded(int i) {
        this.downloaded = i;

        String oldStringDownloaded = stringDownloaded;
        this.stringDownloaded = RegExp.calcStringSize(this.getDownloaded());

        if (!oldStringDownloaded.equals(stringDownloaded))
            changedProperties.add(CHANGED_DOWNLOADED);

        int oldPercent = this.perc;

        if (this.getSize() > 0)
            this.perc = (int) ((float) this.getDownloaded() / (float) this.getSize() * 100f);
        else
            this.perc = 0;

        if (oldPercent != this.perc)
            changedProperties.add(CHANGED_PERCENT);
    }

    /**
     * setRate
     *
     * @param double
     */
    private void setRate(double d) {
        if (this.rate != (float) d)
            changedProperties.add(CHANGED_RATE);

        this.rate = (float) d;
    }

    /**
     * set offset (last seen)
     *
     * @param int
     */
    private void setOffset(int i) {
        this.offset = i;

        String oldStringOffset = stringOffset;
        this.stringOffset = RegExp.calcStringOfSeconds(this.offset);

        if (!oldStringOffset.equals(stringOffset))
            changedProperties.add(CHANGED_LAST);
    }

    /**
     * Calculate # of active sources
     */
    public void setActiveSources(int i) {
        int oldActiveSources = activeSources;

        if (i == 0) {
            activeSources = 0;

            Iterator it = clientInfoWeakMap.getKeySet().iterator();

            while (it.hasNext()) {
                ClientInfo clientInfo = (ClientInfo) it.next();

                if (clientInfo.getState().getState() == EnumState.CONNECTED_DOWNLOADING)
                    activeSources++;
            }
        } else
            activeSources += i;

        if (oldActiveSources != activeSources) {
            changedProperties.add(CHANGED_ACTIVE);
            notifyChangedProperties();
        }
    }

    /**
     * update ETA
     */
    private void updateETA() {
        if (this.rate == 0)
            this.etaSeconds = Long.MAX_VALUE;
        else
            this.etaSeconds = (long) ((getSize() - getDownloaded()) / (this.rate + 1));

        String oldStringETA = stringETA;
        this.stringETA = RegExp.calcStringOfSeconds(this.etaSeconds);

        if (stringETA.equals(""))
            stringETA = "-";

        if (!oldStringETA.equals(stringETA))
            changedProperties.add(CHANGED_ETA);
    }

    /**
     * @param string The new name for this file
     */
    public void setName(String string) {
        string = "rename " + this.getId() + " \"" + string + "\"";

        /* create the message content */
        Message consoleMessage = new EncodeMessage(Message.S_CONSOLEMSG, string);
        consoleMessage.sendMessage(this.parent);
        consoleMessage = null;
    }

    /**
     * @param enum The new priority for this file (LOW/NORMAL/HIGH)
     */
    public void sendPriority(EnumPriority enum) {
        int newPriority;

        if (enum == EnumPriority.LOW)
            newPriority = -10;
        else if (enum == EnumPriority.HIGH)
            newPriority = 10;
        else if (enum == EnumPriority.VERY_HIGH)
            newPriority = 20;
        else if (enum == EnumPriority.VERY_LOW)
            newPriority = -20;
        else
            newPriority = 0;

        sendPriority(false, newPriority);
    }

    /**
     * Send new priority
     * @param relative
     * @param i
     */
    public void sendPriority(boolean relative, int i) {
        Object[] obj = new Object[ 2 ];
        obj[ 0 ] = new Integer(this.getId());

        if (relative)
            i += priority;

        obj[ 1 ] = new Integer(i);

        Message priorityMessage = new EncodeMessage(Message.S_SET_FILE_PRIO, obj);
        priorityMessage.sendMessage(this.parent);
    }

    /**
     * @param enum The new state of this file
     */
    public void setState(EnumFileState enum) {
        this.getState().setState(enum, this.getId(), this.parent);
    }

    /**
     * Verify all chunks of this fileinfo
     */
    public void verifyChunks() {
        Message chunksMsg = new EncodeMessage(Message.S_VERIFY_ALL_CHUNKS, new Integer(this.getId()));
        chunksMsg.sendMessage(this.parent);
    }

    /**
     * Preview the file
     */
    public void preview() {
        Message preview = new EncodeMessage(Message.S_PREVIEW, new Integer(this.getId()));
        preview.sendMessage(this.parent);
    }

    /**
     * @param name Save file as (name)
     */
    public void saveFileAs(String aName) {
        Object[] obj = new Object[ 2 ];
        obj[ 0 ] = new Integer(this.getId());
        obj[ 1 ] = aName;

        Message saveAs = new EncodeMessage(Message.S_SAVE_FILE_AS, obj);
        saveAs.sendMessage(this.parent);
        obj = null;
        saveAs = null;
    }

    /**
     * @return stringSize
     */
    public String getStringSize() {
        return stringSize;
    }

    /**
     * @return stringDownloaded
     */
    public String getStringDownloaded() {
        return stringDownloaded;
    }

    /**
      * @return stringETA
      */
    public String getStringETA() {
        if ((getState().getState() == EnumFileState.QUEUED) ||
                (getState().getState() == EnumFileState.DOWNLOADED) ||
                (getState().getState() == EnumFileState.PAUSED))
            return "-";

        return stringETA;
    }

    /**
     * @return stringOffset (Last seen complete)
     */
    public String getStringOffset() {
        return stringOffset;
    }

    /**
     * @return stringAge (Age of download)
     */
    public String getStringAge() {
        return stringAge;
    }

    /**
      * @return eta
      */
    public long getETA() {
        return etaSeconds;
    }

    /**
     * @return ed2kLink
     */
    public String getED2K() {
        return "ed2k://|file|" + this.getName() + "|" + this.getSize() + "|" + this.getMd4() +
        "|/";
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable o, Object obj) {
        if (o instanceof ClientInfo) {
            if (obj instanceof Boolean) {
                ClientInfo clientInfo = (ClientInfo) o;

                // this client is now interesting.. notify the viewer
                if (((Boolean) obj).equals(Boolean.TRUE)) {
                    setActiveSources(+1);

                    TreeClientInfo treeClientInfo = findTreeClientInfo(clientInfo);

                    if (treeClientInfo == null) {
                        treeClientInfo = new TreeClientInfo(this, clientInfo);
                        treeClientInfoSet.add(treeClientInfo);
                    }

                    this.setChanged();
                    this.notifyObservers(treeClientInfo);
                } else {
                    TreeClientInfo foundTreeClientInfo;

                    if ((foundTreeClientInfo = removeTreeClientInfo(clientInfo)) != null) {
                        foundTreeClientInfo.setDelete();
                        setActiveSources(-1);
                        this.setChanged();
                        this.notifyObservers(foundTreeClientInfo);
                    }
                }
            } else {
                this.setChanged();
                this.notifyObservers(obj);
            }
        }
    }

    /**
        * @return boolean if this FileInfo is interesting to display in downloadsTable
        */
    public boolean isInteresting() {
        if ((getState().getState() == EnumFileState.DOWNLOADING) ||
                (getState().getState() == EnumFileState.PAUSED) ||
                (getState().getState() == EnumFileState.DOWNLOADED) ||
                (getState().getState() == EnumFileState.QUEUED))
            return true;
        else

            return false;
    }

    /**
     * @param clientInfo
     * @return treeClientInfo
     */
    public TreeClientInfo removeTreeClientInfo(ClientInfo clientInfo) {
        TreeClientInfo foundTreeClientInfo = findTreeClientInfo(clientInfo);

        if (foundTreeClientInfo != null)
            treeClientInfoSet.remove(foundTreeClientInfo);

        return foundTreeClientInfo;
    }

    /**
     * @param clientInfo
     * @return treeClientInfo
     */
    public TreeClientInfo findTreeClientInfo(ClientInfo clientInfo) {
        Iterator i = treeClientInfoSet.iterator();

        while (i.hasNext()) {
            TreeClientInfo treeClientInfo = (TreeClientInfo) i.next();

            if (clientInfo == treeClientInfo.getClientInfo())
                return treeClientInfo;
        }

        return null;
    }
}


/*
$Log: FileInfo.java,v $
Revision 1.83  2003/12/01 14:22:17  lemmster
ProtocolVersion handling completely rewritten

Revision 1.82  2003/11/30 23:42:56  zet
updates for latest mldonkey cvs

Revision 1.81  2003/11/29 13:05:25  lemmster
ToolTip complete reworked (to be continued)

Revision 1.80  2003/11/29 12:49:22  zet
update this.avail properly

Revision 1.79  2003/11/28 13:11:10  zet
support patch 2372

Revision 1.78  2003/11/26 23:32:35  zet
sync

Revision 1.77  2003/11/23 20:29:25  dek
re-added comment...

Revision 1.75  2003/11/23 20:21:05  dek
MULTINET can overwirte avail

Revision 1.74  2003/11/23 17:58:03  lemmster
removed dead/unused code

Revision 1.73  2003/11/20 23:30:29  dek
fixed bug with new core (protoVersion >17)

Revision 1.72  2003/11/08 22:47:06  zet
update client table header

Revision 1.71  2003/11/06 13:52:33  lemmster
filters back working

Revision 1.70  2003/10/28 11:07:32  lemmster
move NetworkInfo.Enum -> enum.EnumNetwork
add MaskMatcher for "Enum[]"

Revision 1.69  2003/10/23 05:11:56  zet
try to fix rare duplicate treeclientinfos

Revision 1.68  2003/10/13 21:36:04  zet
Use "-" for blank ETAs

Revision 1.67  2003/10/12 21:17:38  zet
compat priority w/2.5.4

Revision 1.66  2003/10/12 19:42:59  zet
very high & low priorities

Revision 1.65  2003/10/12 15:57:11  zet
store active clients

Revision 1.64  2003/10/05 00:55:13  zet
set priority as any #

Revision 1.63  2003/09/26 04:18:53  zet
getEd2k

Revision 1.62  2003/09/25 00:51:03  zet
reset active sources on clean_tables

Revision 1.61  2003/09/24 03:09:57  zet
add # of active sources column

Revision 1.60  2003/09/23 00:10:44  zet
add Preview

Revision 1.59  2003/09/20 14:38:40  zet
move transfer package

Revision 1.58  2003/09/20 01:31:36  zet
*** empty log message ***

Revision 1.57  2003/09/18 15:29:25  zet
centralize writeStream in core
handle IOException rather than throwing it away

Revision 1.56  2003/09/18 09:16:47  lemmster
checkstyle

Revision 1.55  2003/09/18 03:52:12  zet
smaller map initial capacities

Revision 1.54  2003/09/16 15:56:13  zet
(int) perc

Revision 1.53  2003/09/16 15:54:15  zet
(int) perc

Revision 1.52  2003/09/16 01:26:44  zet
fix #916 bigfile

Revision 1.51  2003/09/15 22:10:46  zet
add relative availability

Revision 1.50  2003/09/14 16:23:23  zet
getAvails

Revision 1.49  2003/09/14 03:37:24  zet
changedProperties in model

Revision 1.48  2003/09/13 22:22:59  zet
weak sets

Revision 1.47  2003/09/10 14:46:19  zet
sources

Revision 1.46  2003/09/05 23:47:14  zet
*** empty log message ***

Revision 1.45  2003/08/31 01:46:33  zet
localise

Revision 1.44  2003/08/24 20:37:51  zet
minor

Revision 1.43  2003/08/24 14:27:36  lemmster
mutlinet avail is overall avail

Revision 1.42  2003/08/23 15:21:37  zet
remove @author

Revision 1.41  2003/08/23 10:02:02  lemmster
use supertype where possible

Revision 1.40  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.39  2003/08/22 21:03:15  lemmster
replace $user$ with $Author: lemmster $

Revision 1.38  2003/08/22 14:28:56  dek
more failsafe hack ;-)

Revision 1.37  2003/08/16 20:20:21  zet
minor

Revision 1.36  2003/08/15 22:05:58  zet
*** empty log message ***

Revision 1.35  2003/08/10 23:20:15  zet
signed int

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
