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

import net.mldonkey.g2gui.view.resource.G2GuiResources;

/**
 * EnumExtension
 *
 * @version $Id: EnumExtension.java,v 1.3 2003/12/17 13:06:04 lemmy Exp $ 
 *
 */
public class EnumExtension extends Enum {
	private String name;
	
	// this enum is simply unknown and the matcher returns false
	public static EnumExtension UNKNOWN = new EnumExtension( 0, "" );
	
	public static EnumExtension AUDIO = new EnumExtension( 1,	
											G2GuiResources.getString( "TT_DOWNLOAD_FILTER_AUDIO" ) );

	public static EnumExtension VIDEO = new EnumExtension( 2, 
											G2GuiResources.getString( "TT_DOWNLOAD_FILTER_VIDEO" ) );

	public static EnumExtension ARCHIVE = new EnumExtension( 4, 
											G2GuiResources.getString( "TT_DOWNLOAD_FILTER_ARCHIVE" ) );

	public static EnumExtension CDIMAGE = new EnumExtension( 8, 
											G2GuiResources.getString( "TT_DOWNLOAD_FILTER_CDIMAGE" ) );

	public static EnumExtension PICTURE = new EnumExtension( 16,
											G2GuiResources.getString( "TT_DOWNLOAD_FILTER_PICTURE" ) );

	// no instanciation
	private EnumExtension(int i, String name) {
		super(i);
		this.name = name;
	}
	
	public String toString() {
		return this.name;
	}
}

/*
$Log: EnumExtension.java,v $
Revision 1.3  2003/12/17 13:06:04  lemmy
save all panelistener states correctly to the prefstore

Revision 1.2  2003/12/04 08:47:29  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.1  2003/11/06 13:52:33  lemmy
filters back working

*/