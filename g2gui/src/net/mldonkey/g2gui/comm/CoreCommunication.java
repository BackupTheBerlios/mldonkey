/*
 * Copyright (C) 2003  Jörg Maisenbacher
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *
 *  * Created on 09.06.2003
 */
 
package net.mldonkey.g2gui.comm;

import java.io.IOException;

/**
 * CoreCommunication
 *
 * @author $user$
 * @version $Id: CoreCommunication.java,v 1.3 2003/06/18 13:31:30 dek Exp $ 
 *
 */
public interface CoreCommunication {
	/**
	 * connect()
	 *
	 * @author $user$
	 * @version $Id: CoreCommunication.java,v 1.3 2003/06/18 13:31:30 dek Exp $ 
	 * Connects this object to given mldonkey-core @remote 
	 *
	 */	
	void connect();
	/**
	 * run()
	 *
	 * @author $user$
	 * @version $Id: CoreCommunication.java,v 1.3 2003/06/18 13:31:30 dek Exp $ 
	 * starts the Core-Thread and begins to receive messages 
	 *
	 */	
	void run() throws IOException;
	
	boolean registerListener( net.mldonkey.snippets.InterFaceUI anInterFaceUI );
	
	
	
	

}
