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

import gnu.trove.TIntArrayList;
import gnu.trove.TIntObjectIterator;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.model.enum.EnumNetwork;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;

/**
 * ServerInfoList
 *
 *
 * @version $Id: ServerInfoIntMap.java,v 1.32 2004/09/17 22:36:48 dek Exp $
 *
 */
public class ServerInfoIntMap extends InfoIntMap {
    /**
     * last serverinfos modified/added/removed
     */
    private List modified = new ArrayList();
    private List added = new ArrayList();
    private List removed = new ArrayList();

    /**
     * @param communication my parent
     */
    ServerInfoIntMap( CoreCommunication communication ) {
        super( communication );
    }

    /**
     * Store a key/value pair in this object
     * @param key The Key
     * @param value The FileInfo object
     */
    private void put( int key, ServerInfo value ) {
        /* skip empty values */
        if ( value == null )
            return;
        synchronized ( this ) {
            this.infoIntMap.put( key, value );
        }
        synchronized ( this.added ) {
            this.added.add( value );
        }
    }

    /**
     * Reads a List of ServerInfo objects from a MessageBuffer
     * @param messageBuffer The MessageBuffer to read from
     */
    public void readStream( MessageBuffer messageBuffer ) {
        int id = messageBuffer.readInt32();
        if ( !PreferenceLoader.loadBoolean( "displayNodes" ) ) {
            NetworkInfo network = this.parent.getNetworkInfoMap().get( messageBuffer.readInt32() );
            messageBuffer.setIterator( messageBuffer.getIterator() - 4 );

            /* ignore fasttrack and gnutella servers */
            if ( !network.hasServers() ) {	
            	messageBuffer.setIterator(messageBuffer.getBuffer().length);
                return;
            }
        }
        messageBuffer.setIterator( messageBuffer.getIterator() - 4 );
        ServerInfo server = this.get( id );
        if ( server != null ) {
            server.readStream( messageBuffer );

            /* ignore removed servers */
            if ( server.getState() != EnumState.REMOVE_HOST )
                synchronized ( this.modified ) {
                    this.modified.add( this.get( id ) );
                }
        }
        else {
        	ServerInfo serverInfo  = parent.getModelFactory().getServerInfo();            
            serverInfo.readStream( messageBuffer );
            if ( serverInfo.getState() != EnumState.REMOVE_HOST )
                this.put( serverInfo.getServerId(), serverInfo );
        }
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Get a ServerInfo object from this object by there id
     * @param id The FileInfo id
     * @return The FileInfo object
     */
    private ServerInfo get( int id ) {
        return ( ServerInfo ) this.infoIntMap.get( id );
    }

    /**
     * Updates a serverInfo
     * @param messageBuffer The MessageBuffer to read from
     */
    public void update( MessageBuffer messageBuffer ) {
        int id = messageBuffer.readInt32();
        ServerInfo server = this.get( id );
        if ( server != null ) {
            server.update( messageBuffer );

            /* ignore removed servers */
            if ( server.getState() != EnumState.REMOVE_HOST )
                synchronized ( this.modified ) {
                    this.modified.add( server );
                }
            this.setChanged();
            this.notifyObservers();
        }
        else{
        	messageBuffer.setIterator(messageBuffer.getBuffer().length);
        }
    }

    /**
     * @param messageBuffer
     * cleans up the used Trove-collections. Fills 'em with data the core (mldonkey) finds useful
     */
    public void clean( MessageBuffer messageBuffer ) {
        TIntArrayList usefulServers = new TIntArrayList( messageBuffer.readInt32List() );
        TIntArrayList oldServers = new TIntArrayList();

        /* find the old servers */
        synchronized ( this ) {
            TIntObjectIterator itr = this.infoIntMap.iterator();
            int size = this.infoIntMap.size();
            for ( ; size-- > 0;) {
                itr.advance();
                int key = itr.key();
                if ( !usefulServers.contains( key ) )
                    oldServers.add( key );
            }

            /* remove the oldServers from the infointmap */
            for ( int i = 0; i < oldServers.size(); i++ ) {
                int key = oldServers.get( i );
                this.removed.add( this.infoIntMap.get( key ) );
                this.infoIntMap.remove( key );
            }
        }
        usefulServers = null;
        oldServers = null;
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * returns an array with all networks known to this networkMap
     * @return all known networks
     */
    public Object[] getServers() {
        synchronized ( this ) {
            return this.infoIntMap.getValues();
        }
    }

    /**
     * @return The serverinfos who have been changed
     * clear this list, after the update in the view
     */
    public List getModified() {
        return this.modified;
    }

    /**
     * @return The serverinfos who have been added
     * clear this list, after the update in the view
     */
    public List getAdded() {
        return this.added;
    }

    /**
     * @return The serverinfos who have been removed
     * clear this list, after the update in the view
     */
    public List getRemoved() {
        return this.removed;
    }

    /**
     * Removes all entries in changed
     */
    public void clearModified() {
        synchronized ( this.modified ) {
            this.modified.clear();
        }
    }

    /**
     * Removes all entries in added
     */
    public void clearAdded() {
        synchronized ( this.added ) {
            this.added.clear();
        }
    }

    /**
     * Removes all entries in removed
     */
    public void clearRemoved() {
        synchronized ( this.removed ) {
            this.removed.clear();
        }
    }

    /**
     * @return The number of connected servers in this serverinfointmap
     */
    public int getConnected() {
        int result = 0;
        synchronized ( this ) {
            TIntObjectIterator itr = this.infoIntMap.iterator();
            int size = this.infoIntMap.size();
            for ( ; size-- > 0;) {
                itr.advance();
                ServerInfo server = ( ServerInfo ) itr.value();
                if ( ( server != null ) && server.isConnected() )
                    result++;
            }
        }
        return result;
    }

    /**
     * @param network The network for which we want the num of connected servers
     * @return The number of connected server in this serverinfointmap
     */
    public int getConnected( NetworkInfo network ) {
        int result = 0;
        synchronized ( this ) {
            TIntObjectIterator itr = this.infoIntMap.iterator();
            int size = this.infoIntMap.size();
            for ( ; size-- > 0;) {
                itr.advance();
                ServerInfo server = ( ServerInfo ) itr.value();
                if ( ( server != null ) && server.isConnected() && ( server.getNetwork() == network ) )
                    result++;
            }
        }
        return result;
    }

    /**
     * Sends Message.S_CONNECT_MORE to the core.
     *
     * @param network The <code>NetworkInfo</code> we want more connections for
     */
    public void connectMore( NetworkInfo network ) {
        Message message = new EncodeMessage( Message.S_CONNECT_MORE );
        message.sendMessage( this.parent );
        message = null;
    }

    /**
     * Sends Message.S_CLEAN_OLD to the core
     */
    public void cleanOld() {
        Message message = new EncodeMessage( Message.S_CLEAN_OLD );
        message.sendMessage( this.parent );
        message = null;
    }

    /**
     * Removes a old ServerInfo from this obj
     * @param key The ID of the ServerInfo to remove
     * @return true on success, false if this obj does not contain such an ID
     */
    public boolean remove( int key ) {
        if ( !this.infoIntMap.containsKey( key ) )
            return false;
        ( ( ServerInfo ) this.infoIntMap.get( key ) ).setState( EnumState.REMOVE_HOST );
        return true;
    }

    /**
     * removes a serverinfo from this obj.
     * (without sending a message to the core)
     * @param server The serverInfo to remove
     */
    protected void remove( ServerInfo server ) {
        synchronized ( this ) {
            this.infoIntMap.remove( server.getServerId() );
            this.removed.add( server );
        }
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * removes all server of the given networkinfo.enum
     * (without sending a message to the core)
     * use it to clean the serverlist if a network gets disabled
     * @param enum The networkinfo.enum
     */
    public void remove( EnumNetwork enum ) {
        Object[] servers = this.infoIntMap.getValues();
        for ( int i = 0; i < servers.length; i++ ) {
            ServerInfo server = ( ServerInfo ) servers[ i ];
            if ( server.getNetwork().equals( enum ) ) {
                synchronized ( this ) {
                    this.infoIntMap.remove( server.getServerId() );
                }
                synchronized ( this.removed ) {
                    this.removed.add( server );
                }
            }
        }
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * Connects the ServerInfo
     * @param key The ID of the ServerInfo to connect
     * @return true on success, false if this obj does not contain such an ID
     */
    public boolean connect( int key ) {
        if ( !this.infoIntMap.containsKey( key ) )
            return false;
        ( ( ServerInfo ) this.infoIntMap.get( key ) ).setState( EnumState.CONNECTING );
        return true;
    }

    /**
     * Disconnects the ServerInfo
     * @param key The ID of the ServerInfo to disconnect
     * @return true on success, false if this obj does not contain such an ID
     */
    public boolean disconnect( int key ) {
        if ( !this.infoIntMap.containsKey( key ) )
            return false;
        ( ( ServerInfo ) this.infoIntMap.get( key ) ).setState( EnumState.NOT_CONNECTED );
        return true;
    }

    /**
     * Adds a ServerInfo to this obj
     * @param network The NetworkInfo the ServerInfo belongs to
     * @param address The IP Address of the ServerInfo
     * @param port The Port of the ServerInfo
     */
    public void add( NetworkInfo network, InetAddress address, short port ) {
        if ( address == null )
            return;
        Object[] obj = new Object[ 3 ];
        obj[ 0 ] = new Integer( network.getNetwork() );
        obj[ 1 ] = address.getAddress();
        obj[ 2 ] = new Short( port );
        Message message = new EncodeMessage( Message.S_ADD_SERVER, obj );
        message.sendMessage( this.parent );
        message = null;
    }

    /**
     * Adds the server to the black list
     * @param key The serverid
     */
    public void addToBlackList( int key ) {
        ServerInfo server = ( ServerInfo ) this.infoIntMap.get( key );
        Message message = new EncodeMessage( Message.S_CONSOLEMSG, server.getServerAddress().toString() );
        message.sendMessage( this.parent );
        message = null;
    }

    /**
     * Add servers from a weblist to this obj
     * @param url The url to the server list
     */
    public void addServerList( String url ) {
        int index = url.lastIndexOf( "." );
        String suffix = url.substring( index );
        if ( suffix.equalsIgnoreCase( ".met" ) )
            suffix = "server.met";
        else
            suffix = "ocl";
        String aString = "add_url ";
        aString += ( suffix + " " );
        aString += url;
        Message message = new EncodeMessage( Message.S_CONSOLEMSG, aString );
        message.sendMessage( this.parent );
        message = null;
        aString = null;
    }

    /**
     * Returns a string representation of this obj
     * @return String The string
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        Object[] obj = this.getServers();
        for ( int i = 0; i < obj.length; i++ ) {
            ServerInfo server = ( ServerInfo ) obj[ i ];
            result.append( server.toString() + "\n" );
        }
        return result.toString();
    }
}

/*
$Log: ServerInfoIntMap.java,v $
Revision 1.32  2004/09/17 22:36:48  dek
update for gui-Protocol 29

Revision 1.31  2004/03/25 19:25:23  dek
yet more profiling

Revision 1.30  2004/03/20 01:34:02  dek
implemented gui-Proto 25 !!!!!

Revision 1.29  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.28  2003/12/01 14:22:17  lemmy
ProtocolVersion handling completely rewritten

Revision 1.27  2003/11/29 13:01:11  lemmy
Addr.getString() renamed to the more natural word name Addr.toString()

Revision 1.26  2003/11/23 17:58:03  lemmy
removed dead/unused code

Revision 1.25  2003/10/28 11:07:32  lemmy
move NetworkInfo.Enum -> enum.EnumNetwork
add MaskMatcher for "Enum[]"

Revision 1.24  2003/09/18 15:29:25  zet
centralize writeStream in core
handle IOException rather than throwing it away

Revision 1.23  2003/09/18 09:16:47  lemmy
checkstyle

Revision 1.22  2003/09/14 09:59:02  lemmy
fix  0 server in mouse-popup [bug #869]

Revision 1.21  2003/09/14 09:01:15  lemmy
show nodes on request

Revision 1.20  2003/09/12 16:29:02  lemmy
ignore FT/GNU/GNUT2 servers in proto >=18 as long as the core sends them

Revision 1.19  2003/08/23 15:21:37  zet
remove @author

Revision 1.18  2003/08/22 19:00:25  lemmy
support for connectMore with network id

Revision 1.17  2003/08/11 19:25:04  lemmy
bugfix at CleanTable

Revision 1.16  2003/08/09 13:51:42  dek
removed wrong comment (from cut'n paste)

Revision 1.15  2003/08/09 13:45:54  dek
added gnu.regexp for compiling with gcj
you can get it at:

ftp://ftp.tralfamadore.com/pub/java/gnu.regexp-1.1.4.tar.gz

Revision 1.14  2003/08/07 12:35:31  lemmy
cleanup, more efficient

Revision 1.13  2003/08/06 20:56:49  lemmy
cleanup, more efficient

Revision 1.12  2003/08/06 18:53:19  lemmy
changed renamed to modified (conflict with observable)

Revision 1.11  2003/08/06 17:38:38  lemmy
some actions still missing. but it should work for the moment

Revision 1.10  2003/08/06 09:47:29  lemmy
toString() added, some bugfixes

Revision 1.9  2003/08/05 13:43:18  lemmy
added some messages

Revision 1.8  2003/08/01 17:21:19  lemmy
reworked observer/observable design, added multiversion support

Revision 1.7  2003/07/30 19:28:42  lemmy
several changes

Revision 1.6  2003/07/06 20:09:21  dek
NPE fixed

Revision 1.5  2003/07/04 18:35:02  lemmy
foobar

Revision 1.4  2003/06/27 10:35:53  lemmy
removed unneeded calls

Revision 1.3  2003/06/26 17:57:46  lemmy
added workaround for bug in core proto

Revision 1.2  2003/06/20 15:15:22  dek
humm, some interface-changes, hope, it didn't break anything ;-)

Revision 1.1  2003/06/16 21:47:19  lemmy
just refactored (name changed)

Revision 1.8  2003/06/16 20:12:47  dek
debugging code removed

Revision 1.7  2003/06/16 20:08:38  lemmy
opcode 13 added

Revision 1.6  2003/06/16 18:05:26  dek
refactored cleanTable

Revision 1.5  2003/06/16 17:42:59  lemmy
minor changes in readStream()

Revision 1.4  2003/06/16 13:18:59  lemmy
checkstyle applied

Revision 1.3  2003/06/15 16:18:41  lemmy
new interface introduced

Revision 1.2  2003/06/14 23:04:08  lemmy
change from interface to abstract superclass

*/
