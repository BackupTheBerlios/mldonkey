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
import org.eclipse.jface.preference.*;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;


/**
 * G2Gui
 *
 * @author $user$
 * @version $Id: G2Gui.java,v 1.1 2003/06/24 20:44:54 lemmstercvs01 Exp $ 
 *
 */
public class G2Gui extends PreferencePage  {
	StringFieldEditor hostname,username,password;
	IntegerFieldEditor port;
	PreferenceStore preferenceStore;
	/**
	 * @param preferenceStore
	 */
	public G2Gui(PreferenceStore preferenceStore_) {
		super("G2gui");		
		this.preferenceStore = preferenceStore_;

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite shell) {
		
				
		hostname = new StringFieldEditor("hostname", "Hostname",shell);
		port = new IntegerFieldEditor("port","Port",shell);
		username = new StringFieldEditor("username", "Username",shell);
		password = new StringFieldEditor("password", "Password",shell);
			password.getTextControl(shell).setEchoChar ('*');			
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	public boolean performOk() {		
		return super.performOk();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performCancel()
	 */
	public boolean performCancel() {		
		return super.performCancel();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performApply()
	 */
	protected void performApply() {		
		super.performApply();
		
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {		
		super.performDefaults();
	}

}


/*
$Log: G2Gui.java,v $
Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.1  2003/06/23 18:57:05  dek
jface

*/