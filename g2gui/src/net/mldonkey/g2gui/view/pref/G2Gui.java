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
 * @version $Id: G2Gui.java,v 1.20 2003/08/18 12:22:28 dek Exp $ 
 *
 */
public class G2Gui extends FieldEditorPreferencePage  {	
	private Composite parent;
	
	/**
	 * @param string
	 * @param i
	 */
	public G2Gui(String string, int i) {
		super(string,i);		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents( Composite myparent ) {		
		this.parent = ( Composite ) super.createContents(myparent);			
		getPreferenceStore().setDefault( "hostname", "192.168.1.100" );
		getPreferenceStore().setDefault( "username", "admin" );
		getPreferenceStore().setDefault( "password", "" );
		getPreferenceStore().setDefault( "port", "4001" );	
		
		createFieldEditors();
		
		return parent;
	}

	


	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		if (parent != null){
			StringFieldEditor hostNameField = new StringFieldEditor( "hostname", "Hostname", parent );	
				hostNameField.setPreferenceStore(this.getPreferenceStore());
				hostNameField.fillIntoGrid( parent, 2 );
				addField( hostNameField );								
				hostNameField.load();
			
			StringFieldEditor portField = new StringFieldEditor( "port", "Port", parent );
				portField.setPreferenceStore(this.getPreferenceStore());	
				portField.fillIntoGrid( parent, 2 );
				addField( portField );
				portField.load();

			StringFieldEditor userNameField = new StringFieldEditor( "username", "Username", parent );
				userNameField.setPreferenceStore(this.getPreferenceStore());
				userNameField.fillIntoGrid( parent, 2 );
				addField( userNameField );
				userNameField.load();

			StringFieldEditor passwordField = new StringFieldEditor( "password", "Password", parent );
				passwordField.getTextControl( parent ).setEchoChar ( '*' );	
				passwordField.fillIntoGrid( parent, 2 );		
				passwordField.setPreferenceStore(this.getPreferenceStore());	
				addField( passwordField );		
				passwordField.load();		
		}
	}
}


/*
$Log: G2Gui.java,v $
Revision 1.20  2003/08/18 12:22:28  dek
g2gui-pref-page is now fully JFace-approved ;-)

Revision 1.19  2003/08/17 23:13:42  zet
centralize resources, move images

Revision 1.18  2003/07/25 14:47:28  zet
replace string.split

Revision 1.17  2003/07/25 02:56:52  zet
*** empty log message ***

Revision 1.16  2003/07/25 02:41:22  zet
console window colour config in prefs / try different fontfieldeditor / pref page  (any worse?)

Revision 1.15  2003/07/03 16:27:51  lemmstercvs01
nonsense importer removed

Revision 1.14  2003/07/03 10:23:20  dek
OK, the font-thing finally works

Revision 1.13  2003/07/03 10:14:56  dek
saving font now works

Revision 1.12  2003/07/03 08:58:34  dek
how the hell do i store a font on disk...

Revision 1.11  2003/07/02 16:16:47  dek
extensive Checkstyle applying

Revision 1.10  2003/07/02 15:56:37  dek
Checkstyle

Revision 1.9  2003/07/01 13:52:31  dek
small unimportant bugfixes (if bugfixes can be unimportant...)

Revision 1.8  2003/06/30 21:42:45  dek
and removed debugging  system.out.println

Revision 1.7  2003/06/30 19:11:10  dek
work in progress, committing before i make a huge mistake

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