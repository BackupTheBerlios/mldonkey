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
 * ( at your option ) any later version.
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

import java.util.ResourceBundle;

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
 * @version $Id: General.java,v 1.12 2003/07/06 15:38:56 dek Exp $ 
 *
 */
public class General extends PreferencePage {	
	private ExtendedBooleanFieldEditor autoCommitField;
	private IntegerFieldEditor maxHardDownloadRateField;
	private StringFieldEditor maxHardUploadRateField;
	private static ResourceBundle res = ResourceBundle.getBundle( "g2gui" );
	private OptionsInfoMap options;
	private String 	clientName,
			maxHardUploadRate,
			maxHardDownloadRate;
	private boolean autoCommit;
	private CoreCommunication mldonkey;
	private boolean connected;	
	private StringFieldEditor clientNameField;


	/**
	 * @param preferenceStore_ where to store the Data
	 * @param connected are we connected to mldonkey-core
	 * @param mldonkey the core
	 */
	public General( PreferenceStore preferenceStore_, boolean connected, CoreCommunication mldonkey ) {
		super( "General Settings" );	
		
		this.connected = connected;		
		this.mldonkey = mldonkey;			
		
		
		
	}
	private void getOptionsFromCore( 
		boolean connected,
		CoreCommunication mldonkey ) {
		if ( connected ) {
			this.options = mldonkey.getOptionsInfoMap();		
			BusyIndicator.showWhile( null, new Runnable() {			
			public void run() {
				while ( !( options.keySet().contains( "client_name" ) ) ) {					
					}				
				} } );
			clientName = 
				( ( OptionsInfo ) options.get( "client_name" ) ).getValue();
			
			maxHardUploadRate = 	
				( ( OptionsInfo ) options.get( "max_hard_upload_rate" ) ).getValue();
			
			maxHardDownloadRate = 	
				( ( OptionsInfo ) options.get( "max_hard_download_rate" ) ).getValue();
			
			Boolean hell = new Boolean( "false" );
			
			autoCommit = ( new Boolean ( 
				( ( OptionsInfo ) options.get( "auto_commit" ) ).getValue() ) 
										 ).booleanValue();			
		}
		else {
			clientName = res.getString( "OPTIONS_NOT_CONNECTED" );
			maxHardUploadRate = res.getString( "OPTIONS_NOT_CONNECTED" );
			maxHardDownloadRate = res.getString( "OPTIONS_NOT_CONNECTED" );
		}
	}		

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.PreferencePage#createContents( org.eclipse.swt.widgets.Composite )
	 */
	protected Control createContents( Composite shell ) {
		
			getOptionsFromCore( connected, mldonkey );
		 	createClientNameField( shell );
			createDownloadRateField( shell );
			createUploadRateField( shell );
			createAutoCommitField( shell );	
					
		return null;
	}
	
	private void createAutoCommitField( Composite shell ) {
		this.autoCommitField = new ExtendedBooleanFieldEditor( "auto_commit", "Auto Commit", shell );
			autoCommitField.setEnabled( connected, shell );
			autoCommitField.setSelection( autoCommit );
			autoCommitField.setToolTipText( "Uncheck if you don't want mldonkey to automatically put completed files in incoming directory" );
	}
	
	
	private void createUploadRateField( Composite shell ) {
		this.maxHardUploadRateField = new IntegerFieldEditor( "max_hard_upload_rate", "Max. Upload ( in kb/s, 0 is unlimited )", shell );
			maxHardUploadRateField.setEnabled( connected, shell );
			maxHardUploadRateField.setStringValue( maxHardUploadRate );		 	
			maxHardUploadRateField.getTextControl( shell ).setToolTipText( "The maximal upload rate you can tolerate on your link in kBytes/s ( 0 = no limit )\n"
				+ "The limit will apply on all your connections ( clients and servers ) and both\n"
				+ " control and data messages." );
			maxHardUploadRateField.getLabelControl( shell ).setToolTipText( "The maximal upload rate you can tolerate on your link in kBytes/s ( 0 = no limit )\n" 
				+ "The limit will apply on all your connections ( clients and servers ) and both\n" 
				+ " control and data messages." );
	}
	
	private void createDownloadRateField( Composite shell ) {
		this.maxHardDownloadRateField = new IntegerFieldEditor( "max_hard_download_rate", "Max. Download ( in kb/s, 0 is unlimited )", shell );
			maxHardDownloadRateField.setEnabled( connected, shell );
			maxHardDownloadRateField.setStringValue( maxHardDownloadRate );
			maxHardDownloadRateField.getTextControl( shell ).setToolTipText( "The maximal upload rate you can tolerate on your link in kBytes/s ( 0 = no limit )\n"
				+ "The limit will apply on all your connections ( clients and servers ) and both\n"
				+ " control and data messages." );
			maxHardDownloadRateField.getLabelControl( shell ).setToolTipText( "The maximal upload rate you can tolerate on your link in kBytes/s ( 0 = no limit )\n"
				+ "The limit will apply on all your connections ( clients and servers ) and both\n"
				+ " control and data messages." );
	}
	
	private void createClientNameField( Composite shell ) {
		this.clientNameField = new StringFieldEditor( "client_name", "Client Name", shell );
			clientNameField.setEnabled( connected, shell );
			clientNameField.setStringValue( clientName );			
			clientNameField.getTextControl( shell ).setToolTipText( "Small name of client" );
			clientNameField.getLabelControl( shell ).setToolTipText( "Small name of client" );			
	}
	


	public boolean performOk() {	
		/* only perform, if this tab has been 
		 * initialized ( checked by the existance of the clientNameField )
		 */		
		 if ( clientNameField != null ) {		
		 	
		 	/* This is the structure all Options have to ve handled to avoid senseless traffic
		 	 * for setting options, that have not changed.
		 	 */		 	 
			if ( !clientName.equals( clientNameField.getStringValue() ) ) {
				OptionsInfo option = ( OptionsInfo ) mldonkey.getOptionsInfoMap().get("client_name");
				option.setValue( clientNameField.getStringValue() );
				option.send();
			}
		 		
			if ( !maxHardUploadRate.equals( maxHardUploadRateField.getStringValue() ) ) {
				OptionsInfo option = ( OptionsInfo ) mldonkey.getOptionsInfoMap().get( "max_hard_upload_rate" );
				option.setValue( maxHardUploadRateField.getStringValue() );
				option.send();
			}
			
							
			if ( !maxHardDownloadRate.equals( maxHardDownloadRateField.getStringValue() ) ) {
				OptionsInfo option = ( OptionsInfo ) mldonkey.getOptionsInfoMap().get( "max_hard_download_rate" );
				option.setValue( maxHardDownloadRateField.getStringValue() );
				option.send();
			}
			
				
			if ( autoCommitField.hasChanged() ) {
				OptionsInfo option = ( OptionsInfo ) mldonkey.getOptionsInfoMap().get( "auto_commit" );
				option.setValue( autoCommitField.getValue() );
				option.send();
			}
			
				
		 	/*any more settings in here, got the syntax?*/	
			
		 }		
		return super.performOk();		
	}
	
	protected void performApply() {		
		super.performApply();
		
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
	}

}

/*
$Log: General.java,v $
Revision 1.12  2003/07/06 15:38:56  dek
worked changes in

Revision 1.11  2003/07/04 18:06:47  dek
*** empty log message ***

Revision 1.10  2003/07/02 16:16:47  dek
extensive Checkstyle applying

Revision 1.9  2003/07/02 15:57:23  dek
Checkstyle

Revision 1.8  2003/07/01 13:52:31  dek
small unimportant bugfixes ( if bugfixes can be unimportant... )

Revision 1.7  2003/06/30 17:29:54  dek
Saving all the options in General works now ( not validated for strings/int/yet )

Revision 1.6  2003/06/29 20:23:41  dek
how the hell do i get the value out of a booleanFieldeditor???

Revision 1.5  2003/06/29 18:58:57  dek
saving values to disk/mldonkey starts working

Revision 1.4  2003/06/29 18:25:03  dek
setting clientname now works

Revision 1.3  2003/06/27 18:05:46  dek
Client name is now an option, not saveable yet, but it's displayed ;- )

Revision 1.2  2003/06/26 14:09:20  dek
checkstyle

Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.1  2003/06/23 18:57:05  dek
jface

*/