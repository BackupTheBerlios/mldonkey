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
import java.util.Observer;

import net.mldonkey.g2gui.model.*;

/**
 * CoreCommunication
 *
 * @author $user$
 * @version $Id: CoreCommunication.java,v 1.16 2003/07/02 15:53:41 dek Exp $ 
 *
 */
public interface CoreCommunication {
	/**
	 * connect()
	 * Connects this object to given mldonkey-core @remote 
	 */	
	void connect();
	/**
	 * run()
	 * starts the Core-Thread and begins to receive messages
	 * @throws IOException If read on stream failed 
	 */	
	void run() throws IOException;
	
	/**
	 * 
	 * @return the ConsoleMessage(-Buffer)
	 */
	ConsoleMessage getConsoleMessage();
	
	public NetworkInfoIntMap getNetworkinfoMap();
	
	/**
	 * Adds an Observer to this object
	 * @param obj The Observer to add
	 */
	void addObserver( Observer obj );
	/**
	 * @param string
	 */
	void sendConsoleMessage( String command );
	/**
	 * 
	 */
	void disconnect();
	/**
	 * @return
	 */
	boolean isConnected();
	/**
	 * 
	 */
	 OptionsInfoMap getOptions();
	 
	 /**
	  * Sets the option <b> name</b> to the <b>value</b>
	  * @param name name of the option
	  * @param value the value
	  */
	public void setOption( String name, String value );
}
