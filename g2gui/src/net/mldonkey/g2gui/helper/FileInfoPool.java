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
package net.mldonkey.g2gui.helper;

import net.mldonkey.g2gui.model.FileInfo;

/**
 * DownloadPool
 *
 * @author markus
 * @version $Id: FileInfoPool.java,v 1.1 2003/06/12 18:13:30 lemmstercvs01 Exp $ 
 *
 */
public class FileInfoPool extends ObjectPool {

	/**
	 * 
	 */
	public FileInfoPool() {
		super();
		/* spawn Downloads for min */
		for ( int i = 0; i < initial; i++ ) {
			this.unused.add( create() );
		}
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.helper.ObjectPool#create()
	 */
	protected Object create() {
		FileInfo download = null;
		download = new FileInfo();
		return download;
	}

}

/*
$Log: FileInfoPool.java,v $
Revision 1.1  2003/06/12 18:13:30  lemmstercvs01
initial commit

Revision 1.1  2003/06/12 13:09:52  lemmstercvs01
ObjectPool, DownloadPool, GuiMessagePool added;
class hierarchy under ObjectPool created

*/