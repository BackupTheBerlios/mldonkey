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
 * ModelFactory18
 *
 * @version $Id: ModelFactory18.java,v 1.1 2003/12/01 14:22:17 lemmster Exp $ 
 *
 */
public class ModelFactory18 extends ModelFactory17 {
	/**
	 * 
	 */
	public ModelFactory18() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OptionsInfoMap getOptionsInfoMap() {
		return new OptionsInfoMap18( core );
	}
	public FileInfo getFileInfo() {
		return new FileInfo18( core );
	}
	public ClientStats getClientStats() {
		// should be Singleton
		if ( clientStats == null )
			clientStats = new ClientStats18( core );
		return (ClientStats) clientStats;
	}
	public ServerInfoIntMap getServerInfoIntMap() {
		if ( serverInfoIntMap == null )
			serverInfoIntMap = new ServerInfoIntMap18( core );
		return (ServerInfoIntMap) serverInfoIntMap;
	}
}

/*
$Log: ModelFactory18.java,v $
Revision 1.1  2003/12/01 14:22:17  lemmster
ProtocolVersion handling completely rewritten

*/