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

import org.eclipse.jface.preference.*;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;


/**
 * G2Gui
 *
 * @author $user$
 * @version $Id: G2Gui.java,v 1.4 2003/06/26 12:06:12 dek Exp $ 
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
		preferenceStore.setDefault("hostname","localhost");
		preferenceStore.setDefault("username","admin");
		preferenceStore.setDefault("password","");
		preferenceStore.setDefault("port",(int)4001);

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite shell) {		
				
		hostname = new StringFieldEditor("hostname", "Hostname",shell);	
			hostname.setStringValue(preferenceStore.getDefaultString("hostname"));
			hostname.setStringValue(preferenceStore.getString("hostname"));			
			
		port = new IntegerFieldEditor("port","Port",shell);
			port.setStringValue(preferenceStore.getDefaultString("port"));
			port.setStringValue(preferenceStore.getString("port"));

		username = new StringFieldEditor("username", "Username",shell);
			username.setStringValue(preferenceStore.getDefaultString("username"));
			username.setStringValue(preferenceStore.getString("username"));

		password = new StringFieldEditor("password", "Password",shell);
			password.getTextControl(shell).setEchoChar ('*');			
			password.setStringValue(preferenceStore.getString("password"));

		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	public boolean performOk(){	
		preferenceStore.setValue("initialized",true);
		preferenceStore.setValue("hostname",hostname.getStringValue());
		preferenceStore.setValue("username",username.getStringValue());
		preferenceStore.setValue("port",port.getIntValue());
		preferenceStore.setValue("password",password.getStringValue());
		try {
			preferenceStore.save();
		} catch (IOException e) {
			System.out.println("Saving Preferences failed");
		}	
		return super.performOk();		
	}


	protected void performApply() {		
		super.performApply();
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performCancel()
	 */
	public boolean performCancel() {
		System.out.println("cancel");
		//return super.performCancel();
		return false;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {	
		
		hostname.setStringValue(preferenceStore.getDefaultString("hostname"));
		port.setStringValue(preferenceStore.getDefaultString("port"));
		username.setStringValue(preferenceStore.getDefaultString("username"));
						
		super.performDefaults();
	}

}


/*
$Log: G2Gui.java,v $
Revision 1.4  2003/06/26 12:06:12  dek
removed debugging output

Revision 1.3  2003/06/26 12:04:44  dek
pref-dialog accessible in main-window

Revision 1.2  2003/06/25 10:42:36  dek
peferenences dialog at first start

Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.1  2003/06/23 18:57:05  dek
jface

*/