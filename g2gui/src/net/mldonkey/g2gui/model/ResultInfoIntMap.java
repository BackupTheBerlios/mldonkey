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

import gnu.trove.TIntObjectIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.mldonkey.g2gui.comm.Core;
import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.MessageBuffer;

/**
 * ResultInfoIntMap
 *
 *
 * @version $Id: ResultInfoIntMap.java,v 1.6 2003/08/23 15:21:37 zet Exp $ 
 *
 */
public class ResultInfoIntMap extends InfoIntMap {
	/**
	 * @param communication my parent
	 */
	public ResultInfoIntMap( CoreCommunication communication ) {
		super( communication );
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.model.InfoCollection#readStream(net.mldonkey.g2gui.helper.MessageBuffer)
	 */
	public void readStream( MessageBuffer messageBuffer ) {
		int searchID = messageBuffer.readInt32();
		int resultID = messageBuffer.readInt32();
		
		/* get the result from the core resultinfo map */
		ResultInfo result = ( ResultInfo ) ( ( Core ) parent ).getResultInfo().get( resultID );
		
		/* if we didnt get a result, we done this search already and must look in this map instead */
		if ( result == null ) {
			/* firts iterate over all map entries */
			TIntObjectIterator itr = this.infoIntMap.iterator();
			int size = this.infoIntMap.size();
			for ( int i = 0; i < size; i++ ) {
				itr.advance();
				/* the values of this map are Lists */
				List temp = ( List ) itr.value();
				/* Iterate over the List */
				Iterator itr2 = temp.iterator();
				while ( itr2.hasNext() ) {
					ResultInfo temp2 = ( ResultInfo ) itr2.next();
					/* search for the resultID */
					if ( temp2.getResultID() == resultID )
						result = temp2;
				}
			}
		}
		else {
			/* remove the resultinfo from core resultinfo map */
			( ( Core ) parent ).getResultInfo().remove( resultID );
		}
		
		if ( this.infoIntMap.containsKey( searchID ) ) {
			( ( List ) this.infoIntMap.get( searchID ) ).add( result );
		}
		else {
			List aList = new ArrayList();
			aList.add( result );
			this.infoIntMap.put( searchID, aList );
		}
		this.setChanged();
		this.notifyObservers( this );
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.model.InfoCollection#update(net.mldonkey.g2gui.helper.MessageBuffer)
	 */
	public void update( MessageBuffer messageBuffer ) { }
	
	/**
	 * Get the value to the given key
	 * @param key The key which should be used
	 * @return The obj to the given key
	 */
	public Object get( int key ) {
		return this.infoIntMap.get( key );
	}

}

/*
$Log: ResultInfoIntMap.java,v $
Revision 1.6  2003/08/23 15:21:37  zet
remove @author

Revision 1.5  2003/08/22 21:03:15  lemmster
replace $user$ with $Author: zet $

Revision 1.4  2003/08/01 17:30:18  lemmstercvs01
search works again

Revision 1.3  2003/08/01 17:21:19  lemmstercvs01
reworked observer/observable design, added multiversion support

Revision 1.2  2003/07/23 23:08:56  lemmstercvs01
searching for the same string twice added

Revision 1.1  2003/07/23 16:56:28  lemmstercvs01
initial commit

*/