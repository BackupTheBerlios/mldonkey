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


import java.io.IOException;
import java.net.Socket;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import net.mldonkey.g2gui.comm.Core;
import net.mldonkey.g2gui.helper.SocketPool;
import net.mldonkey.g2gui.view.pref.Preferences;

/**
 * G2GuiTest
 *
 * @author $user$
 * @version $Id: Main.java,v 1.5 2003/06/26 21:11:10 dek Exp $ 
 *
 */
public class Main {
	
	private static Core mldonkey;
	
	/**
	 * Starts a new Core and launch the Gui
	 * @param args
	 */
	public static void main(String[] args) {
		PreferenceStore preferenceStore = new PreferenceStore("g2gui.pref");
		Preferences myprefs = new Preferences(preferenceStore);
		try {
			myprefs.initialize(preferenceStore);
		} catch (IOException e) {			
			
		}		
		if (!(preferenceStore.getBoolean("initialized"))){					
			myprefs.open(new Shell(new Display()),null);
			}
		int port = preferenceStore.getInt("port");
		String hostname = preferenceStore.getString("hostname");
		String username = preferenceStore.getString("username");
		String password = preferenceStore.getString("password");

	
		SocketPool socketPool = new SocketPool( hostname, port, username, password );
		try {
			mldonkey  = new Core( ( Socket ) socketPool.checkOut() );
			mldonkey.start();
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}	
			
		Gui g2gui = new Gui(mldonkey);
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
Revision 1.5  2003/06/26 21:11:10  dek
speed is shown

Revision 1.4  2003/06/26 12:04:59  dek
pref-dialog accessible in main-window

Revision 1.3  2003/06/25 18:04:53  dek
Console-Tab reworked

Revision 1.2  2003/06/25 10:42:36  dek
peferenences dialog at first start

Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.1  2003/06/24 20:13:14  lemmstercvs01
initial commit

*/