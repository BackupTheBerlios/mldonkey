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
 * EnumTreeNode
 *
 * @author markus
 * @version $Id: EnumTreeNode.java,v 1.1 2003/06/24 09:16:13 lemmstercvs01 Exp $ 
 *
 */
public class EnumTreeNode implements Enum {
	/**
	 * and (value is 0)
	 */
	public static EnumTreeNode AND = new EnumTreeNode( 0 );
	/**
	 * or (value is 1)
	 */
	public static EnumTreeNode OR = new EnumTreeNode( 1 );
	/**
	 * and not (value is 2)
	 */
	public static EnumTreeNode ANDNOT = new EnumTreeNode( 2 );
	/**
	 * module (value is 3)
	 */
	public static EnumTreeNode MODULE = new EnumTreeNode( 3 );
	/**
	 * keyword (value is 4)
	 */
	public static EnumTreeNode KEYWORD = new EnumTreeNode( 4 );
	/**
	 * min size (value is 5)
	 */
	public static EnumTreeNode MINSIZE = new EnumTreeNode( 5 );
	/**
	 * max size (value is 6)
	 */
	public static EnumTreeNode MAXSIZE = new EnumTreeNode( 6 );
	/**
	 * format (value is 7)
	 */
	public static EnumTreeNode FORMAT = new EnumTreeNode( 7 );
	/**
	 * media (value is 8)
	 */
	public static EnumTreeNode MEDIA = new EnumTreeNode( 8 );
	/**
	 * mp3 artist (value is 9)
	 */
	public static EnumTreeNode MP3ARTIST = new EnumTreeNode( 9 );
	/**
	 * mp3 title (value is 10)
	 */
	public static EnumTreeNode MP3TITLE = new EnumTreeNode( 10 );
	/**
	 * mp3 album (value is 11)
	 */
	public static EnumTreeNode MP3ALBUM = new EnumTreeNode( 11 );
	/**
	 * mp3 bitrate (value is 12)
	 */
	public static EnumTreeNode MP3BITRATE = new EnumTreeNode( 12 );
	/**
	 * hidden (value is 13)
	 */
	public static EnumTreeNode HIDDEN = new EnumTreeNode( 13 );
	
	/**
	 * Creates a new EnumTreeNode
	 * @param anInt The value
	 */
	private EnumTreeNode( int anInt ) {
	}
	
}

/*
$Log: EnumTreeNode.java,v $
Revision 1.1  2003/06/24 09:16:13  lemmstercvs01
better Enum added

*/