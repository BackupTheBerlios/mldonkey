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
 * @version $Id: General.java,v 1.6 2003/06/29 20:23:41 dek Exp $ 
 *
 */
public class General extends PreferencePage {
	private BooleanFieldEditor autoCommitField;
	private IntegerFieldEditor maxHardDownloadRateField;
	private StringFieldEditor maxHardUploadRateField;
	OptionsInfoMap options;
	String 	clientName,
			maxHardUploadRate,
			maxHardDownloadRate;
			
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
			
			maxHardUploadRate = 	
				( ( OptionsInfo ) options.get( "max_hard_upload_rate" ) ).getValue();
			
			maxHardDownloadRate = 	
				( ( OptionsInfo ) options.get( "max_hard_download_rate" ) ).getValue();	
		}
		else {
			clientName = "<no Connection to mldonkey>";
			maxHardUploadRate = "<no Connection to mldonkey>";
			maxHardDownloadRate = "<no Connection to mldonkey>";
		}
		
		
	}		

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents( Composite shell ) {					
		createClientNameField(shell);
		createDownloadRateField(shell);
		createUploadRateField(shell);
		this.autoCommitField = new BooleanFieldEditor("auto_commit","Auto Commit",1, shell);
		
		return null;
	}
	
	
	private void createUploadRateField(Composite shell) {
		this.maxHardUploadRateField = new IntegerFieldEditor("max_hard_upload_rate", "Max. Upload (in kb/s, 0 is unlimited)",shell);
			maxHardUploadRateField.setEnabled(connected,shell);
			maxHardUploadRateField.setStringValue(maxHardUploadRate);
			maxHardUploadRateField.getTextControl(shell).setToolTipText("The maximal upload rate you can tolerate on your link in kBytes/s (0 = no limit)\n" +
				"The limit will apply on all your connections (clients and servers) and both\n" +
				" control and data messages.");
			maxHardUploadRateField.getLabelControl(shell).setToolTipText("The maximal upload rate you can tolerate on your link in kBytes/s (0 = no limit)\n" +
				"The limit will apply on all your connections (clients and servers) and both\n" +
				" control and data messages.");
	}
	
	private void createDownloadRateField(Composite shell) {
		this.maxHardDownloadRateField = new IntegerFieldEditor("max_hard_download_rate", "Max. Download (in kb/s, 0 is unlimited)",shell);
			maxHardDownloadRateField.setEnabled(connected,shell);
			maxHardDownloadRateField.setStringValue(maxHardDownloadRate);
			maxHardDownloadRateField.getTextControl(shell).setToolTipText("The maximal upload rate you can tolerate on your link in kBytes/s (0 = no limit)\n" +
				"The limit will apply on all your connections (clients and servers) and both\n" +
				" control and data messages.");
			maxHardDownloadRateField.getLabelControl(shell).setToolTipText("The maximal upload rate you can tolerate on your link in kBytes/s (0 = no limit)\n" +
				"The limit will apply on all your connections (clients and servers) and both\n" +
				" control and data messages.");
	}
	
	private void createClientNameField(Composite shell) {
		this.clientNameField = new StringFieldEditor("client_name", "Client Name",shell);
			clientNameField.setEnabled(connected,shell);
			clientNameField.setStringValue(clientName);
			clientNameField.getTextControl(shell).setToolTipText("Small name of client");
			clientNameField.getLabelControl(shell).setToolTipText("Small name of client");			
	}
	
	/* take care, that this tab has been initalized and then update mldonkey only with the
	 * options that have changed
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	public boolean performOk(){	
		/* only perform, if this tab has been 
		 * initialized (checked by the existance of the clientNameField)
		 */		
		 if (clientNameField!= null){		
		 	
		 	/* This is the structure all Options have to ve handled to avoid senseless traffic
		 	 * for setting options, that have not changed.
		 	 */		 	 
			if (!clientName.equals(clientNameField.getStringValue()))
		 		{mldonkey.setOption("client_name",clientNameField.getStringValue());
		 		}
		 	/*any more settings in here, got the syntax?*/	
		 		
		 }		
		return super.performOk();		
	}
	
	protected void performApply() {		
		super.performApply();
		
	}
}

/*
$Log: General.java,v $
Revision 1.6  2003/06/29 20:23:41  dek
how the hell do i get the value out of a booleanFieldeditor???

Revision 1.5  2003/06/29 18:58:57  dek
saving values to disk/mldonkey starts working

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