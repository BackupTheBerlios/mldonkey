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
import java.net.Socket;

import net.mldonkey.g2gui.view.widgets.InterFaceUI;



/**
 * CoreCommunication
 *
 * @author $user$
 * @version $Id: CoreCommunication.java,v 1.4 2003/06/18 19:46:34 dek Exp $ 
 *
 */
public interface CoreCommunication {
	/**
	 * connect()
	 *
	 * @author $user$
	 * @version $Id: CoreCommunication.java,v 1.4 2003/06/18 19:46:34 dek Exp $ 
	 * Connects this object to given mldonkey-core @remote 
	 *
	 */	
	void connect();
	/**
	 * run()
	 *
	 * @author $user$
	 * @version $Id: CoreCommunication.java,v 1.4 2003/06/18 19:46:34 dek Exp $ 
	 * starts the Core-Thread and begins to receive messages 
	 *
	 */	
	void run() throws IOException;
	
	void registerListener( InterFaceUI anInterFaceUI );
	Socket getConnection();
	
	
	
	

}
