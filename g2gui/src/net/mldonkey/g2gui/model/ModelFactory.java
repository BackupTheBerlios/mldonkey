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

/**
 * ProtocolVersionFactory
 *
 * @version $Id: ModelFactory.java,v 1.2 2003/12/04 08:47:25 lemmy Exp $ 
 *
 */
public class ModelFactory {
	protected static ModelFactory factory;
	protected static CoreCommunication core;
	
	// this classes should be singletons by design
	protected SimpleInformation clientStats, consoleMessage;
	protected InfoCollection clientInfoIntMap, defineSearchMap, fileInfoIntMap, networkInfoIntMap,
							  optionsInfoMap, resultInfoIntMap, roomInfoIntMap, serverInfoIntMap,
							  sharedFileInfoIntMap;
	
	protected ModelFactory() {
		// prevent extern instanciation
	}
	
	public static ModelFactory getFactory( int protocolVersion, CoreCommunication aCore ) {
		if ( factory == null ) {
			if ( protocolVersion >= 20 )
				factory = new ModelFactory20();
			else if ( protocolVersion >= 19 )
				factory = new ModelFactory19();
			else if ( protocolVersion >= 18 )
				factory = new ModelFactory18();
			else if ( protocolVersion >= 17 ) 
				factory = new ModelFactory17();
			else if ( protocolVersion >= 3 )
				factory = new ModelFactory3();
		}
		core = aCore;
		return factory;	
	}
	
	// SimpleInformation
	public Addr getAddr() {
		return new Addr();
	}
	public ClientInfo getClientInfo() {
		return new ClientInfo( core );
	}
	public ClientKind getClientKind() {
		return new ClientKind();
	}
	public ClientMessage getClientMessage() {
		return new ClientMessage( core );
	}
	public ClientStats getClientStats() {
		// should be Singleton
		if ( clientStats == null )
			clientStats = new ClientStats( core );
		return (ClientStats) clientStats;
	}
	public ConsoleMessage getConsoleMessage() {
		// should be Singleton
		if ( consoleMessage == null )
			consoleMessage = new ConsoleMessage();
		return (ConsoleMessage) consoleMessage;
	}
	public FileInfo getFileInfo() {
		return new FileInfo( core );
	}
	public FileState getFileState() {
		return new FileState();
	}
	public Format getFormat() {
		return new Format();
	}
	public NetworkInfo getNetworkInfo() {
		return new NetworkInfo( core );
	}
	public OptionsInfo getOptionsInfo() {
		return new OptionsInfo0( core );
	}
	public Query getQuery() {
		return new Query();
	}
	public ResultInfo getResultInfo() {
		return new ResultInfo( core );
	}
	public RoomInfo getRoomInfo() {
		return new RoomInfo( core );
	}
	public SearchResult getSearchResult() {
		return new SearchResult();
	}
	public ServerInfo getServerInfo() {
		return new ServerInfo( core );
	}
	public SharedFileInfo getSharedFileInfo() {
		return new SharedFileInfo();
	}
	public State getState() {
		return new State();
	}
	public Tag getTag() {
		return new Tag();
	}
	public UserInfo getUserInfo() {
		return new UserInfo();
	}
	// InfoCollection (should be all singleton)
	public ClientInfoIntMap getClientInfoIntMap() {
		if ( clientInfoIntMap == null )
			clientInfoIntMap = new ClientInfoIntMap( core );
		return (ClientInfoIntMap) clientInfoIntMap;
	}
	public DefineSearchMap getDefineSearchMap() {
		if ( defineSearchMap == null )
			defineSearchMap = new DefineSearchMap( core );
		return (DefineSearchMap) defineSearchMap;
	}
	public FileInfoIntMap getFileInfoIntMap() {
		if ( fileInfoIntMap == null )
			fileInfoIntMap = new FileInfoIntMap( core );
		return (FileInfoIntMap) fileInfoIntMap;
	}
	public NetworkInfoIntMap getNetworkInfoIntMap() {
		if ( networkInfoIntMap == null )
			networkInfoIntMap = new NetworkInfoIntMap( core );
		return (NetworkInfoIntMap) networkInfoIntMap;
	}
	public OptionsInfoMap getOptionsInfoMap() {
		if ( optionsInfoMap == null )
			optionsInfoMap = new OptionsInfoMap( core );
		return (OptionsInfoMap) optionsInfoMap;
	}
	public ResultInfoIntMap getResultInfoIntMap() {
		if ( resultInfoIntMap == null )
			resultInfoIntMap = new ResultInfoIntMap( core );
		return (ResultInfoIntMap) resultInfoIntMap;
	}
	public RoomInfoIntMap getRoomInfoIntMap() {
		if ( roomInfoIntMap == null )
			roomInfoIntMap = new RoomInfoIntMap( core );
		return (RoomInfoIntMap) roomInfoIntMap;
	}
	public ServerInfoIntMap getServerInfoIntMap() {
		if ( serverInfoIntMap == null )
			serverInfoIntMap = new ServerInfoIntMap( core );
		return (ServerInfoIntMap) serverInfoIntMap;
	}
	public SharedFileInfoIntMap getSharedFileInfoIntMap() {
		if ( sharedFileInfoIntMap == null )
			sharedFileInfoIntMap = new SharedFileInfoIntMap( core );
		return (SharedFileInfoIntMap) sharedFileInfoIntMap;
	}
	// Sendable
	public Download getDownload() {
		return new Download( core );
	}
	public MessageVersion getMessageVersion() {
		return new MessageVersion( core );
	}
	public SearchQuery getSearchQuery() {
		return new SearchQuery( core );
	}
}

/*
$Log: ModelFactory.java,v $
Revision 1.2  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.1  2003/12/01 14:22:17  lemmy
ProtocolVersion handling completely rewritten

*/