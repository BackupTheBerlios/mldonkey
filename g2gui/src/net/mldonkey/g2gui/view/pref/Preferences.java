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

import java.io.IOException;
import net.mldonkey.g2gui.comm.CoreCommunication;

import org.eclipse.jface.preference.*;
import org.eclipse.swt.widgets.Shell;

/**
 * OptionTree2
 *
 * @author $user$
 * @version $Id: Preferences.java,v 1.3 2003/06/26 12:04:44 dek Exp $ 
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
	public Preferences(PreferenceStore preferenceStore) {	
		this.preferenceStore = 	preferenceStore;
		myprefs = new PreferenceManager();		
		myprefs.addToRoot(new PreferenceNode("G2gui", new G2Gui(preferenceStore)));
		myprefs.addToRoot(new PreferenceNode("mldonkey", new General(preferenceStore)));
		myprefs.addToRoot(new PreferenceNode("eDonkey", new Edonkey(preferenceStore)));		

		
	}
	
	public void open(Shell shell, CoreCommunication mldonkey) {
		try {
			initialize(preferenceStore);
		} catch (IOException e) {
			System.out.println("initalizing Preferences Dialog failed due to IOException");
		}
		prefdialog = new PreferenceDialog(shell, myprefs){
			/* (non-Javadoc)
			 * @see org.eclipse.jface.preference.PreferenceDialog#cancelPressed()
			 */
			protected void cancelPressed() {				
				prefdialog.close();
			}
			};
	
		prefdialog.open();
	}

	public void initialize(PreferenceStore preferenceStore) throws IOException {
		try {			
			preferenceStore.load();
		} catch (IOException e) {
			preferenceStore.save();
			preferenceStore.load();
		}
	}	


	/**
	 * @return
	 */
	public PreferenceManager getMyprefs() {
		return myprefs;
	}

}

/*
$Log: Preferences.java,v $
Revision 1.3  2003/06/26 12:04:44  dek
pref-dialog accessible in main-window

Revision 1.2  2003/06/25 10:42:36  dek
peferenences dialog at first start

Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.1  2003/06/23 18:57:05  dek
jface

*/