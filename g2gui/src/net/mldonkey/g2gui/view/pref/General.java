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

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.OptionsInfo;
import net.mldonkey.g2gui.model.OptionsInfoMap;

import org.eclipse.jface.preference.*;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.*;



/**
 * General
 *
 * @author $user$
 * @version $Id: General.java,v 1.4 2003/06/29 18:25:03 dek Exp $ 
 *
 */
public class General extends PreferencePage {
	OptionsInfoMap options;
	String clientName;
	CoreCommunication mldonkey;
	boolean connected;	
	public StringFieldEditor clientNameField;

	/**
	 * @param preferenceStore
	 */
	public General( PreferenceStore preferenceStore_, boolean connected, CoreCommunication mldonkey ) {
		super( "General Settings" );
		this.connected = connected;		
		this.mldonkey = mldonkey;
		if (connected){
			this.options = mldonkey.getOptions();		
			BusyIndicator.showWhile(null,new Runnable(){			
			public void run() {
				while (!(options.keySet().contains("client_name"))){					
					}				
				}});
			clientName = 
				( ( OptionsInfo ) options.get( "client_name" ) ).getValue();			
		}
		else clientName = "<no Connection to mldonkey>";
		
	}


	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents( Composite shell ) {	
					
		this.clientNameField = new StringFieldEditor("client_name", "Client Name",shell);
			clientNameField.setEnabled(connected,shell);
			clientNameField.setStringValue(clientName);				
		return null;
	}
	
	/* take care, that this tab has been initalized and then update mldonkey only with the
	 * options that have changed
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	public boolean performOk(){			
		 if (clientNameField!= null){		 
			if (!clientName.equals(clientNameField.getStringValue()))
		 		{mldonkey.setOption("client_name",clientNameField.getStringValue());
		 		}
		 }
		 //Dek - ([emule.de] AND [emule] suck)
		return super.performOk();		
	}
	
	protected void performApply() {		
		super.performApply();
		
	}
}

/*
$Log: General.java,v $
Revision 1.4  2003/06/29 18:25:03  dek
setting clientname now works

Revision 1.3  2003/06/27 18:05:46  dek
Client name is now an option, not saveable yet, but it's displayed ;-)

Revision 1.2  2003/06/26 14:09:20  dek
checkstyle

Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.1  2003/06/23 18:57:05  dek
jface

*/