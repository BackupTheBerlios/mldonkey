/*
 * Copyright 2003
 * G2GUI Team
 * 
 * 
 * This file is part of G2GUI.
 *
 * G2GUI is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2GUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2GUI; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.view.pref;

import net.mldonkey.g2gui.comm.Core;

import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * OptionTree2
 *
 * @author $user$
 * @version $Id: Preferences.java,v 1.1 2003/06/24 20:44:54 lemmstercvs01 Exp $ 
 *
 */
public class Preferences extends PreferenceManager {	

	private PreferenceManager myprefs;
	private PreferenceDialog prefdialog;
	private PreferenceStore preferenceStore;
	/**
	 * @param shell
	 * @param mldonkey
	 * @param display
	 */
	public Preferences() {
		preferenceStore = new PreferenceStore("g2gui.pref");
		myprefs = new PreferenceManager();		
		myprefs.addToRoot(new PreferenceNode("G2gui", new G2Gui(preferenceStore)));
		myprefs.addToRoot(new PreferenceNode("mldonkey", new General(preferenceStore)));
		myprefs.addToRoot(new PreferenceNode("eDonkey", new Edonkey(preferenceStore)));		

		
	}
	
	public void open(Shell shell, Core mldonkey, Display display) {

		prefdialog = new PreferenceDialog(shell, myprefs);
		prefdialog.create();
		prefdialog.open();
		
		
	}
	

}

/*
$Log: Preferences.java,v $
Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.1  2003/06/23 18:57:05  dek
jface

*/