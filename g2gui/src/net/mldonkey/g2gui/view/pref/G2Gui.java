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
 * @version $Id: G2Gui.java,v 1.6 2003/06/29 18:58:57 dek Exp $ 
 *
 */
public class G2Gui extends PreferencePage  {
	private boolean connected;
	StringFieldEditor hostNameField,userNameField,passwordField;
	IntegerFieldEditor portField;
	PreferenceStore preferenceStore;
	/**
	 * @param preferenceStore
	 */
	public G2Gui(PreferenceStore preferenceStore_, boolean connected) {
		super("G2gui");
		this.connected = connected;		
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
				
		hostNameField = new StringFieldEditor("hostname", "Hostname",shell);	
			hostNameField.setStringValue(preferenceStore.getDefaultString("hostname"));
			hostNameField.setStringValue(preferenceStore.getString("hostname"));			
			
		portField = new IntegerFieldEditor("port","Port",shell);
			portField.setStringValue(preferenceStore.getDefaultString("port"));
			portField.setStringValue(preferenceStore.getString("port"));

		userNameField = new StringFieldEditor("username", "Username",shell);
			userNameField.setStringValue(preferenceStore.getDefaultString("username"));
			userNameField.setStringValue(preferenceStore.getString("username"));

		passwordField = new StringFieldEditor("password", "Password",shell);
			passwordField.getTextControl(shell).setEchoChar ('*');			
			passwordField.setStringValue(preferenceStore.getString("password"));

		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	public boolean performOk(){	
		/* only perform, if this tab has been 
		 * initialized (checked by the existance of the hostnamefield)
		 */ 	
		if (hostNameField!= null){	
		preferenceStore.setValue("initialized",true);
		preferenceStore.setValue("hostname",hostNameField.getStringValue());
		preferenceStore.setValue("username",userNameField.getStringValue());
		preferenceStore.setValue("port",portField.getIntValue());
		preferenceStore.setValue("password",passwordField.getStringValue());
		/* any more options go in here, you got the syntax??*/
		
			try {
				preferenceStore.save();
			} catch (IOException e) {
				System.out.println("Saving Preferences failed");
			}	
		}		
		return super.performOk();		
		
	}


	protected void performApply() {		
		super.performApply();
		
	}


	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {	
		
		hostNameField.setStringValue(preferenceStore.getDefaultString("hostname"));
		portField.setStringValue(preferenceStore.getDefaultString("port"));
		userNameField.setStringValue(preferenceStore.getDefaultString("username"));
						
		super.performDefaults();
	}

}


/*
$Log: G2Gui.java,v $
Revision 1.6  2003/06/29 18:58:57  dek
saving values to disk/mldonkey starts working

Revision 1.5  2003/06/27 18:05:46  dek
Client name is now an option, not saveable yet, but it's displayed ;-)

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