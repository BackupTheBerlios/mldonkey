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
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.helper.VersionCheck;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
/**
 * G2GuiPref
 *
 *
 * @version $Id: G2GuiPref.java,v 1.21 2004/01/28 22:15:34 psy Exp $ 
 *
 */
public class G2GuiPref extends PreferencePage {
	
	/**
	 * @param string name
	 * @param i style (SWT.XYZ)
	 */
	public G2GuiPref( String string, int i ) {
		super( string, i );
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		
		Composite composite = getFieldEditorParent();
			StringFieldEditor hostNameField =
					new StringFieldEditor( "hostname", "Hostname", composite );
					
				hostNameField.setPreferenceStore( this.getPreferenceStore() );
				hostNameField.fillIntoGrid( composite, 2 );
				addField( hostNameField );
				hostNameField.load();
			
			StringFieldEditor portField =
					new StringFieldEditor( "port", "Port", composite );
				portField.setPreferenceStore( this.getPreferenceStore() );
				portField.fillIntoGrid( composite, 2 );
				addField( portField );
				portField.load();
			
			StringFieldEditor userNameField =
					new StringFieldEditor( "username", "Username", composite );
				userNameField.setPreferenceStore( this.getPreferenceStore() );
				userNameField.fillIntoGrid( composite, 2 );
				addField( userNameField );
				userNameField.load();
			
			StringFieldEditor passwordField =
					new StringFieldEditor( "password", "Password", composite );
				passwordField.getTextControl( composite ).setEchoChar( '*' );
				passwordField.fillIntoGrid( composite, 2 );
				passwordField.setPreferenceStore( this.getPreferenceStore() );
				addField( passwordField );
				passwordField.load();
		
			GCJFileFieldEditor executableField =
					new GCJFileFieldEditor( "coreExecutable", 
						G2GuiResources.getString( "PREF_CORE_EXEC" ), true, composite );
					executableField.setPreferenceStore( this.getPreferenceStore() );
					addField( executableField );
					executableField.load();	
			
			if (VersionCheck.isWin32()) {
			    executableField.setFileExtensions( new String[] { "*.exe;*.bat" } );
			} else {
			    executableField.setFileExtensions( new String[] {"*"});
			}
			    
			FieldEditor mulitpleInstancesEditor =
				new BooleanFieldEditor( "allowMultipleInstances",
					G2GuiResources.getString( "PREF_ALLOW_MULIPLE" ), composite );
				mulitpleInstancesEditor.setPreferenceStore( this.getPreferenceStore() );
				mulitpleInstancesEditor.fillIntoGrid( composite, 2 );
				addField( mulitpleInstancesEditor );
				mulitpleInstancesEditor.load();
		
			ExtendedBooleanFieldEditor advancedModeEditor =
				new ExtendedBooleanFieldEditor( "advancedMode",
					"Advanced user mode (*)", composite );
				advancedModeEditor.setPreferenceStore( this.getPreferenceStore() );
				advancedModeEditor.fillIntoGrid( composite, 2 );

				addField( advancedModeEditor );
				advancedModeEditor.load();

				// Make this a little more obvious
				Button b = advancedModeEditor.getChangeControl( composite );
				b.setFont( JFaceResources.getBannerFont());
				b.setToolTipText( G2GuiResources.getString("PREF_ADVANCED_TOOLTIP"));
				b.setForeground( composite.getDisplay().getSystemColor(SWT.COLOR_BLUE));
				
			( ( GridLayout )composite.getLayout() ).numColumns = 2;
	}
}
/*
$Log: G2GuiPref.java,v $
Revision 1.21  2004/01/28 22:15:34  psy
* Properly handle disconnections from the core
* Fast inline-reconnect
* Ask for automatic relaunch if options have been changed which require it
* Improved the local core-controller

Revision 1.20  2004/01/08 21:42:12  psy
introducing boolean isWin32()

Revision 1.19  2003/12/04 08:47:27  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.18  2003/11/13 01:00:15  zet
fix #1079

Revision 1.17  2003/11/11 17:31:10  zet
tooltip

Revision 1.16  2003/11/04 22:58:47  zet
Make "advanced user" option more noticeable

Revision 1.15  2003/10/21 03:11:45  zet
circumvent gcj bug

Revision 1.14  2003/10/12 16:27:01  lemmy
minor changes

Revision 1.13  2003/10/01 20:56:27  lemmy
add class hierarchy

Revision 1.12  2003/09/28 13:10:31  dek
Added Option, wether multiple Instances of G2Gui are allowed or not[bug #867]

Revision 1.11  2003/09/26 04:19:06  zet
drag&drop

Revision 1.10  2003/09/18 10:23:48  lemmy
checkstyle

Revision 1.9  2003/09/03 18:22:45  dek
removed unused import

Revision 1.8  2003/09/03 18:22:26  dek
fixed size from now on

Revision 1.7  2003/09/03 14:49:07  zet
optionally spawn core from gui

Revision 1.6  2003/08/29 20:24:42  dek
icon for preferences, and simple frame (group)

Revision 1.5  2003/08/29 18:30:29  dek
removed name, added shadow to mldonkey-options for previewing

Revision 1.4  2003/08/29 17:13:29  dek
all content is now within a group, do you like it, or should i revert changes?

Revision 1.3  2003/08/23 15:21:37  zet
remove @author

Revision 1.2  2003/08/22 21:10:57  lemmy
replace $user$ with $Author: psy $

Revision 1.1  2003/08/20 11:51:52  dek
renamed pref.g2gui to pref.g2guiPref for not having 2 classes with same name

Revision 1.26  2003/08/19 17:12:56  zet
set defaults

Revision 1.25  2003/08/19 12:14:15  lemmy
first try of simple/advanced mode

Revision 1.24  2003/08/18 14:51:58  dek
some more jface-work

Revision 1.23  2003/08/18 12:41:54  dek
checkstyle

Revision 1.22  2003/08/18 12:33:37  dek
moved default-value declaration to right place...

Revision 1.21  2003/08/18 12:31:53  dek
changed default-hostname to localhost

Revision 1.20  2003/08/18 12:22:28  dek
g2gui-pref-page is now fully JFace-approved ;- )

Revision 1.19  2003/08/17 23:13:42  zet
centralize resources, move images

Revision 1.18  2003/07/25 14:47:28  zet
replace string.split

Revision 1.17  2003/07/25 02:56:52  zet
*** empty log message ***

Revision 1.16  2003/07/25 02:41:22  zet
console window colour config in prefs / try different fontfieldeditor / pref page  ( any worse? )

Revision 1.15  2003/07/03 16:27:51  lemmy
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
small unimportant bugfixes ( if bugfixes can be unimportant... )

Revision 1.8  2003/06/30 21:42:45  dek
and removed debugging  system.out.println

Revision 1.7  2003/06/30 19:11:10  dek
work in progress, committing before i make a huge mistake

Revision 1.6  2003/06/29 18:58:57  dek
saving values to disk/mldonkey starts working

Revision 1.5  2003/06/27 18:05:46  dek
Client name is now an option, not saveable yet, but it's displayed ;- )

Revision 1.4  2003/06/26 12:06:12  dek
removed debugging output

Revision 1.3  2003/06/26 12:04:44  dek
pref-dialog accessible in main-window

Revision 1.2  2003/06/25 10:42:36  dek
peferenences dialog at first start

Revision 1.1  2003/06/24 20:44:54  lemmy
refactored

Revision 1.1  2003/06/23 18:57:05  dek
jface

*/