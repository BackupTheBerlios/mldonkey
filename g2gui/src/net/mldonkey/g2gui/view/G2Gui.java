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
 * ( at your option ) any later version.
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
import java.net.UnknownHostException;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import net.mldonkey.g2gui.comm.Core;
import net.mldonkey.g2gui.view.pref.Preferences;

/**
 * Starts the hole thing
 *
 * @author $user$
 * @version $Id: G2Gui.java,v 1.5 2003/08/03 19:09:51 lemmstercvs01 Exp $ 
 *
 */
public class G2Gui {
	private static Core mldonkey;
	private static MessageBox box;
	private static Shell shell;
	private static PreferenceStore preferenceStore;
	private static Preferences myPrefs;
	private static String hostname, username, password;
	private static int port;
	
	/**
	 * Starts a new Core and launch the Gui
	 * @param args Nothing to put inside
	 */
	public static void main( String[] args ) {
		shell = new Shell( new Display() );
		preferenceStore = new PreferenceStore( "g2gui.pref" );
		myPrefs = new Preferences( preferenceStore );
		try {
			myPrefs.initialize( preferenceStore );
		}
		catch ( IOException e ) { }		
		if ( !( preferenceStore.getBoolean( "initialized" ) ) ) {					
			myPrefs.open( shell, null );
		}
		port = preferenceStore.getInt( "port" );
		hostname = preferenceStore.getString( "hostname" );
		username = preferenceStore.getString( "username" );
		password = preferenceStore.getString( "password" );

		try {
			mldonkey  = new Core( hostname, port, username, password );
		}
		catch ( UnknownHostException e1 ) {
			box = new MessageBox( shell, SWT.ICON_ERROR | SWT.OK );
			box.setText( "Invalid Host Address" );
			box.setMessage( "Illegal Host Address" );
			box.open();
		}
		catch ( IOException e1 ) {
			box = new MessageBox( shell, SWT.ICON_ERROR | SWT.OK );
			box.setText( "IOException" );
			box.setMessage( "Core is not running on this Host/Port" );
			box.open();
		}
			
		MainTab g2gui = new MainTab( mldonkey, shell );
		mldonkey.disconnect();
	}

	/**
	 * @return The parent CoreCommuncation obj
	 */
	public static Core getMldonkey() {
		return mldonkey;
	}
}

/*
$Log: G2Gui.java,v $
Revision 1.5  2003/08/03 19:09:51  lemmstercvs01
better error handling

Revision 1.4  2003/08/01 17:21:19  lemmstercvs01
reworked observer/observable design, added multiversion support

Revision 1.3  2003/07/18 04:34:22  lemmstercvs01
checkstyle applied

Revision 1.2  2003/07/17 15:10:35  lemmstercvs01
foobar

Revision 1.1  2003/07/17 14:58:37  lemmstercvs01
refactored

Revision 1.9  2003/07/13 12:50:01  dek
now the socket is closed, when application is exited

Revision 1.8  2003/07/02 17:06:29  dek
Checkstyle, JavaDocs still have to be added

Revision 1.7  2003/06/27 11:23:48  dek
gui is no more children of applicationwindow

Revision 1.6  2003/06/27 10:39:37  lemmstercvs01
core is now Runnable

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