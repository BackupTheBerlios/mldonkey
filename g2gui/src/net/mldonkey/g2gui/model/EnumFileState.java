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
 * EnumFileState
 *
 * @author markus
 * @version $Id: EnumFileState.java,v 1.1 2003/06/24 09:16:13 lemmstercvs01 Exp $ 
 *
 */
public class EnumFileState implements Enum {
	/**
	 * downloading (value is 0)
	 */
	public static EnumFileState DOWNLOADING = new EnumFileState( 0 );
	/**
	 * paused (value is 1)
	 */
	public static EnumFileState PAUSED = new EnumFileState( 1 );
	/**
	 * downloaded (value is 2)
	 */
	public static EnumFileState DOWNLOADED = new EnumFileState( 2 );
	/**
	 * shared (value is 3)
	 */
	public static EnumFileState SHARED = new EnumFileState( 3 );
	/**
	 * cancelled (value is 4)
	 */
	public static EnumFileState CANCELLED = new EnumFileState( 4 );
	/**
	 * new (value is 5)
	 */
	public static EnumFileState NEW = new EnumFileState( 5 );
	/**
	 * aborted (value is 6)
	 */
	public static EnumFileState ABORTED = new EnumFileState( 6 );
	/**
	 * queued (value is 7)
	 */
	public static EnumFileState QUEUED = new EnumFileState( 7 );
	
	/**
	 * Creates a new EnumFileState
	 * @param anInt The value
	 */
	private EnumFileState( int anInt ) {
	}

}

/*
$Log: EnumFileState.java,v $
Revision 1.1  2003/06/24 09:16:13  lemmstercvs01
better Enum added

*/