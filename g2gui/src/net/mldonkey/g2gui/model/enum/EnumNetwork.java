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
package net.mldonkey.g2gui.model.enum;

/**
 * EnumNetwork
 *
 * @version $Id: EnumNetwork.java,v 1.2 2003/12/04 08:47:29 lemmy Exp $ 
 *
 */
public class EnumNetwork extends Enum {
	/**
	 * eDonkey2000
	 */
	public static EnumNetwork DONKEY = new EnumNetwork( 1 );
	/**
	 * SoulSeek
	 */
	public static EnumNetwork SOULSEEK = new EnumNetwork( 2 );
	/**
	 * Gnutella 1
	 */
	public static EnumNetwork GNUT = new EnumNetwork( 4 );
	/**
	 * Gnutella 2
	 */
	public static EnumNetwork GNUT2 = new EnumNetwork( 8 );
	/**
	 * Overnet
	 */
	public static EnumNetwork OV = new EnumNetwork( 16 );
	/**
	 * Bittorrent
	 */
	public static EnumNetwork BT = new EnumNetwork( 32 );
	/**
	 * Fasttrack
	 */
	public static EnumNetwork FT = new EnumNetwork( 64 );
	/**
	 * OpenNapster
	 */
	public static EnumNetwork OPENNP = new EnumNetwork( 128 );
	/**
	 * DirectConnect
	 */
	public static EnumNetwork DC  = new EnumNetwork( 256 );
	/**
	 * MultiNet
	 */
	public static EnumNetwork MULTINET = new EnumNetwork( 512 );
	
	/**
	 * Creates a new EnumNetworkNetwork
	 */
	private EnumNetwork( int anInt ) {
		super( anInt );
	}
}

/*
$Log: EnumNetwork.java,v $
Revision 1.2  2003/12/04 08:47:29  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.1  2003/10/28 11:07:32  lemmy
move NetworkInfo.Enum -> enum.EnumNetwork
add MaskMatcher for "Enum[]"

*/