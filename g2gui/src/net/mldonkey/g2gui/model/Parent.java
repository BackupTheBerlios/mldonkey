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

import java.util.HashMap;
import java.util.Map;
import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.helper.MessageBuffer;
import net.mldonkey.g2gui.model.enum.EnumExtension;

/**
 * Parent
 *
 *
 * @version $Id: Parent.java,v 1.7 2003/12/01 14:22:17 lemmster Exp $ 
 *
 */
public abstract class Parent extends SimpleInformation {
	/**
	 * The CoreCommunication parent
	 */
	protected CoreCommunication parent;
	/**
	 *  Reads a State object from a MessageBuffer
	 * @param messageBuffer The MessageBuffer to read from
	 */
	public abstract void readStream( MessageBuffer messageBuffer );
	/**
	 * Creates a new SimpleInformation
	 * @param core The parent CoreCommunication
	 */
	Parent( CoreCommunication core ) {
		this.parent = core;
	}
	/**
	 * The CoreCommunication associated with this obj
	 * @return The <code>CoreCommunication</code>
	 */
	public CoreCommunication getCore() {
		return this.parent;
	}
	/**
	 * A Map with links extension <> type
	 */
	protected static Map FILE_TYPES;
	
	// on the first instantiation of this obj we create our filetype map (just once and for all)
	static {
		FILE_TYPES = new HashMap();

		// audio types
		String[] audio = {
					"aac", "ape", "au", "flac", "mid", "mpc", "mp2", "mp3", "mp4", "wav", "ogg", "wma"
		};

		for (int i = 0; i < audio.length; i++)
			FILE_TYPES.put(audio[ i ], EnumExtension.AUDIO);

		// video types
		String[] video = {
					"avi", "mpg", "mpeg", "ram", "rm", "asf", "vob", "divx", "vivo", "ogm", "mov", "wmv"
		};

		for (int i = 0; i < video.length; i++)
			FILE_TYPES.put(video[ i ], EnumExtension.VIDEO);

		//archive types
		String[] archive = { "gz", "zip", "ace", "rar", "tar", "tgz", "bz2" };

		for (int i = 0; i < archive.length; i++)
			FILE_TYPES.put(archive[ i ], EnumExtension.ARCHIVE);

		// cdImage types
		String[] cdImage = {
					"ccd", "sub", "cue", "bin", "iso", "nrg", "img", "bwa", "bwi", "bws", "bwt", "mds",
					"mdf"
		};

		for (int i = 0; i < cdImage.length; i++)
			FILE_TYPES.put(cdImage[ i ], EnumExtension.CDIMAGE);

		// picture types
		String[] picture = { "jpg", "jpeg", "bmp", "gif", "tif", "tiff", "png" };

		for (int i = 0; i < picture.length; i++)
			FILE_TYPES.put(picture[ i ], EnumExtension.PICTURE);
	}
}

/*
$Log: Parent.java,v $
Revision 1.7  2003/12/01 14:22:17  lemmster
ProtocolVersion handling completely rewritten

Revision 1.6  2003/11/29 13:03:54  lemmster
ToolTip complete reworked (to be continued)

Revision 1.5  2003/08/23 15:21:37  zet
remove @author

Revision 1.4  2003/08/22 21:03:15  lemmster
replace $user$ with $Author: lemmster $

Revision 1.3  2003/08/21 13:13:10  lemmster
cleanup in networkitem

Revision 1.2  2003/08/01 17:21:19  lemmstercvs01
reworked observer/observable design, added multiversion support

Revision 1.1  2003/07/06 08:49:34  lemmstercvs01
better oo added

*/