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
 * @version $Id: ModelFactory18.java,v 1.4 2003/12/04 08:47:25 lemmy Exp $ 
 *
 */
public class ModelFactory18 extends ModelFactory17 {
	/**
	 * 
	 */
	public ModelFactory18() {
		super();
	}
	public OptionsInfoMap getOptionsInfoMap() {
		if ( optionsInfoMap == null )
			optionsInfoMap = new OptionsInfoMap18( core );
		return (OptionsInfoMap) optionsInfoMap;
	}
	public FileInfo getFileInfo() {
		return new FileInfo18( core );
	}
	public NetworkInfo getNetworkInfo() {
		return new NetworkInfo18( core );
	}
	public ClientStats getClientStats() {
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
Revision 1.4  2003/12/04 08:47:25  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.3  2003/12/03 09:22:53  lemmy
create the correct NetworkInfo obj in the factory for Protocol >=18

Revision 1.2  2003/12/01 16:19:50  lemmy
fix options

Revision 1.1  2003/12/01 14:22:17  lemmy
ProtocolVersion handling completely rewritten

*/