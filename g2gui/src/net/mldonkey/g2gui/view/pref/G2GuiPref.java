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

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
/**
 * G2GuiPref
 *
 *
 * @version $Id: G2GuiPref.java,v 1.9 2003/09/03 18:22:45 dek Exp $ 
 *
 */
public class G2GuiPref extends FieldEditorPreferencePage {
	
	/**
	 * @param string name
	 * @param i style (SWT.XYZ)
	 */
	public G2GuiPref( String string, int i ) {
		super( string, i );
	}

	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createContents( org.eclipse.swt.widgets.Composite )
	 */
	protected Control createContents( Composite myparent ) {
		
		Group group = new Group( myparent, SWT.NONE );
			GridLayout gl = new GridLayout( 1, false );
			group.setLayout( gl );
			group.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		
		Composite sub = new Composite( group, SWT.NONE ) {		
			public Point computeSize( int wHint, int hHint, boolean changed ) 
			/* This method prevents the window from becoming huge (as in hight and width) 
			 * when reopening "General" (or equivalents)
			 */ 
				{ return new Point( SWT.DEFAULT, SWT.DEFAULT ); }
		};
		
		sub.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		sub.setLayout( new FillLayout() );
		
		Composite parent = ( Composite ) super.createContents( sub );
		parent.setLayoutData( new GridData( GridData.FILL_BOTH ) );		
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		
		parent.layout();
		
		return parent; 
	}
	
	/** ( non-Javadoc )
	 * @see org.eclipse.jface.preference.PreferencePage#setPreferenceStore( org.eclipse.jface.preference.IPreferenceStore )
	 */
	public void setPreferenceStore( IPreferenceStore store ) {
		super.setPreferenceStore( store );
		store = PreferenceLoader.setDefaults(store);
	}
	
	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();
			StringFieldEditor hostNameField =
					new StringFieldEditor( "hostname", "Hostname", parent );
				hostNameField.setPreferenceStore( this.getPreferenceStore() );
				hostNameField.fillIntoGrid( parent, 2 );
				addField( hostNameField );
				hostNameField.load();
			
			StringFieldEditor portField =
					new StringFieldEditor( "port", "Port", parent );
				portField.setPreferenceStore( this.getPreferenceStore() );
				portField.fillIntoGrid( parent, 2 );
				addField( portField );
				portField.load();
			
			StringFieldEditor userNameField =
					new StringFieldEditor( "username", "Username", parent );
				userNameField.setPreferenceStore( this.getPreferenceStore() );
				userNameField.fillIntoGrid( parent, 2 );
				addField( userNameField );
				userNameField.load();
			
			StringFieldEditor passwordField =
					new StringFieldEditor( "password", "Password", parent );
				passwordField.getTextControl( parent ).setEchoChar( '*' );
				passwordField.fillIntoGrid( parent, 2 );
				passwordField.setPreferenceStore( this.getPreferenceStore() );
				addField( passwordField );
				passwordField.load();
		
			FileFieldEditor executableField =
					new FileFieldEditor( "coreExecutable", G2GuiResources.getString("PREF_CORE_EXEC"), true, parent );
					executableField.setPreferenceStore( this.getPreferenceStore() );
					addField( executableField );
					executableField.load();	
			
			String[] winExtensions = { "*.exe;*.bat" };
			if (SWT.getPlatform().equals("win32")) executableField.setFileExtensions(winExtensions);
		
			FieldEditor booleanEditor =
				new BooleanFieldEditor( "advancedMode", "Advanced Mode (Please restart the gui to change the Mode)", parent );
				booleanEditor.setPreferenceStore( this.getPreferenceStore() );
				booleanEditor.fillIntoGrid( parent, 2 );
				addField( booleanEditor );
				booleanEditor.load();
				
			( ( GridLayout )parent.getLayout() ).numColumns = 2;
	}
}
/*
$Log: G2GuiPref.java,v $
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

Revision 1.2  2003/08/22 21:10:57  lemmster
replace $user$ with $Author: dek $

Revision 1.1  2003/08/20 11:51:52  dek
renamed pref.g2gui to pref.g2guiPref for not having 2 classes with same name

Revision 1.26  2003/08/19 17:12:56  zet
set defaults

Revision 1.25  2003/08/19 12:14:15  lemmster
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

Revision 1.1  2003/06/24 20:44:54  lemmstercvs01
refactored

Revision 1.1  2003/06/23 18:57:05  dek
jface

*/