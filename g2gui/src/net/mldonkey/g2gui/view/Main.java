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
package net.mldonkey.g2gui.view;

import java.net.Socket;

import net.mldonkey.g2gui.comm.Core;
import net.mldonkey.g2gui.helper.SocketPool;

/**
 * G2GuiTest
 *
 * @author $user$
 * @version $Id: Main.java,v 1.1 2003/06/24 20:44:54 lemmstercvs01 Exp $ 
 *
 */
public class Main {
	
	private static Core mldonkey;
	
	/**
	 * Starts a new Core and launch the Gui
	 * @param args
	 */
	public static void main(String[] args) {
		int port = 4001;
		String hostname = "";
		String username = "admin";
		String password = "";
	
		SocketPool socketPool = new SocketPool( hostname, port, username, password );
		try {
			mldonkey  = new Core( ( Socket ) socketPool.checkOut() );
			mldonkey.start();
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}	
			
		Gui g2gui = new Gui(null);
		mldonkey.disconnect();
	}
	
	/**
	 * @return a Core
	 */
	public static Core getMldonkey() {
		return mldonkey;
	}

}

/*
$Log: Main.java,v $
Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.1  2003/06/24 20:13:14  lemmstercvs01
initial commit

*/