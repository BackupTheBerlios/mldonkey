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
package net.mldonkey.g2gui.view.server;

import net.mldonkey.g2gui.model.Addr;
import net.mldonkey.g2gui.model.ServerInfo;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.helper.OurTableSorter;

import org.eclipse.jface.viewers.Viewer;

/**
 * TableSorter
 *
 *
 * @version $Id: ServerTableSorter.java,v 1.4 2003/10/21 17:00:45 lemmster Exp $
 *
 */
public class ServerTableSorter extends OurTableSorter {

	public ServerTableSorter() {
		super( "ServerSorter", 9 );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
    public int compare( Viewer viewer, Object obj1, Object obj2 ) {
        ServerInfo server1 = ( ServerInfo ) obj1;
        ServerInfo server2 = ( ServerInfo ) obj2;
        /* network */
        if ( columnIndex == 0 ) {
            String aString1 = server1.getNetwork().getNetworkName();
            String aString2 = server2.getNetwork().getNetworkName();
			return compareStrings( aString1, aString2 );
        }

        /* name */
        if ( columnIndex == 1 ) {
            String aString1 = server1.getNameOfServer();
            String aString2 = server2.getNameOfServer();
			return compareStrings( aString1, aString2 );
        }

        /* desc */
        if ( columnIndex == 2 ) {
            String aString1 = server1.getDescOfServer();
            String aString2 = server2.getDescOfServer();
			return compareStrings( aString1, aString2 );
        }

        /* address */
        if ( columnIndex == 3 ) {
            try {
                Addr addr1 = server1.getServerAddress();
                Addr addr2 = server2.getServerAddress();
                if ( lastSort )
                    return addr1.compareTo( addr2 );
                else
                    return addr2.compareTo( addr1 );
            }
            catch ( NullPointerException e ) {
                return 0;
            }
        }

        /* port */
        if ( columnIndex == 4 ) {
            Integer int1 = new Integer( server1.getServerPort() );
            Integer int2 = new Integer( server2.getServerPort() );
            if ( lastSort )
                return int1.compareTo( int2 );
            else
                return int2.compareTo( int1 );
        }

        /* serverScore */
        if ( columnIndex == 5 ) {
            Integer int1 = new Integer( server1.getServerScore() );
            Integer int2 = new Integer( server2.getServerScore() );
            if ( lastSort )
                return int1.compareTo( int2 );
            else
                return int2.compareTo( int1 );
        }

        /* server users */
        if ( columnIndex == 6 ) {
            Integer int1 = new Integer( server1.getNumOfUsers() );
            Integer int2 = new Integer( server2.getNumOfUsers() );
            if ( lastSort )
                return int1.compareTo( int2 );
            else
                return int2.compareTo( int1 );
        }

        /* server files */
        if ( columnIndex == 7 ) {
            Integer int1 = new Integer( server1.getNumOfFilesShared() );
            Integer int2 = new Integer( server2.getNumOfFilesShared() );
            if ( lastSort )
                return int1.compareTo( int2 );
            else
                return int2.compareTo( int1 );
        }

        /* server state */
        if ( columnIndex == 8 ) {
            EnumState state1 = ( EnumState ) server1.getConnectionState().getState();
            EnumState state2 = ( EnumState ) server2.getConnectionState().getState();
            if ( lastSort )
                return state1.compareTo( state2 );
            else
                return state2.compareTo( state1 );
        }

        /* favorites */
        if ( columnIndex == 9 ) {
            boolean bool1 = server1.isFavorite();
            boolean bool2 = server2.isFavorite();
            if ( lastSort )
                if ( ( bool1 && bool2 ) || ( ( bool1 == false ) && ( bool2 == false ) ) )
                    return 0;
            if ( bool1 && ( bool2 == false ) )
                return 1;
            if ( ( bool1 == false ) && bool2 )
                return -1;
            else if ( ( bool2 && bool1 ) || ( ( bool2 == false ) && ( bool1 == false ) ) )
                return 0;
            if ( bool2 && ( bool1 == false ) )
                return 1;
            if ( ( bool2 == false ) && bool1 )
                return -1;
        }
        return 0;
    }
}

/*
$Log: ServerTableSorter.java,v $
Revision 1.4  2003/10/21 17:00:45  lemmster
class hierarchy for tableviewer

Revision 1.3  2003/09/18 11:26:07  lemmster
checkstyle

Revision 1.2  2003/08/23 15:21:37  zet
remove @author

Revision 1.1  2003/08/23 09:46:18  lemmster
superclass TableMenuListener added

Revision 1.4  2003/08/11 19:25:04  lemmstercvs01
bugfix at CleanTable

Revision 1.3  2003/08/07 12:35:31  lemmstercvs01
cleanup, more efficient

Revision 1.2  2003/08/06 17:38:38  lemmstercvs01
some actions still missing. but it should work for the moment

Revision 1.1  2003/08/05 13:50:10  lemmstercvs01
initial commit

*/
