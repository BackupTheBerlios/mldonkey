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

import java.util.ArrayList;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author z
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class G2Gui_Display extends PreferencePage  {
	private Composite controlshell;

	private ArrayList fieldEditorArray = new ArrayList();
	private int columns = 0;
	
	public G2Gui_Display(PreferenceStore preferenceStore, boolean connected) {
		super( "Display" );
		setPreferenceStore(preferenceStore);
		preferenceStore = PreferenceLoader.setDefaults(preferenceStore);
	}
	
	protected void setupEditor(FieldEditor e) {
		e.setPreferencePage(this);
		e.setPreferenceStore( getPreferenceStore() );
		e.load();
		computeColumn( e.getNumberOfControls() );
		fieldEditorArray.add(e);
	}
	
	protected Control createContents( Composite shell ) {	

		this.controlshell = shell;			
						
		setupEditor(new ExtendedColorFieldEditor("consoleBackground", "Console window background colour", shell));
			
		setupEditor(new ExtendedColorFieldEditor("consoleForeground", "Console window foreground colour", shell));
	
		setupEditor(new ExtendedColorFieldEditor("consoleHighlight", "Console window highlight colour", shell));
	
		setupEditor(new ExtendedColorFieldEditor("consoleInputBackground", "Console input background colour", shell));
			
		setupEditor(new ExtendedColorFieldEditor("consoleInputForeground", "Console input foreground colour", shell));
	
		setupEditor(new ExtendedFontFieldEditor2("consoleFontData", "Console window font", "Sample",  shell));

		setupEditor(new BooleanFieldEditor( "displayAllServers", "Show only connected servers", shell ));
		
		setupEditor(new BooleanFieldEditor("displayHeaderBar", "Display header bar", shell));
		
		setupEditor(new BooleanFieldEditor("displayChunkGraphs", "Display chunk graphs", shell));
		
		setupEditor(new BooleanFieldEditor("displayGridLines", "Display grid lines", shell));
		
		setupEditor(new BooleanFieldEditor("tableCellEditors", "Activate table cell editors", shell));
		
		setupEditor(new BooleanFieldEditor("forceRefresh", "Force full table refresh at each update (maintains sort order, might flicker)", shell));
				
		IntegerFieldEditor displayBuffer = new IntegerFieldEditor("displayBuffer", "GUI update buffer (0-60 seconds)", shell);
		displayBuffer.setValidRange(0,60);
		setupEditor(displayBuffer);
		
		arrangeFields();
		return null;
	}	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	private void setHorizontalSpan( FieldEditor editor ) {
		
		if (editor instanceof ExtendedColorFieldEditor) {
			( ( GridData ) ((ExtendedColorFieldEditor) editor).getChangeControl ( controlshell ).getLayoutData() )
				 .horizontalSpan = columns - 1;
			( ( GridData ) ((ExtendedColorFieldEditor) editor).getChangeControl( controlshell ).getLayoutData() )
				.horizontalAlignment = GridData.FILL;
		} else {
			( ( GridData ) ((StringFieldEditor) editor).getTextControl( controlshell ).getLayoutData() )
				.horizontalSpan = columns - 1;
		}
								  
		( ( GridLayout )controlshell.getLayout() ).numColumns = columns;
		
	} 
	
	private void arrangeFields() {
		
		for (int i = 0; i < fieldEditorArray.size(); i++) {
			FieldEditor fieldEditor = (FieldEditor) fieldEditorArray.get(i);
					
			if (fieldEditor instanceof ExtendedColorFieldEditor)
				setHorizontalSpan( (ExtendedColorFieldEditor) fieldEditor );
			else if (fieldEditor instanceof BooleanFieldEditor
					|| fieldEditor instanceof IntegerFieldEditor)
				fieldEditor.fillIntoGrid(controlshell, columns);
			else if (fieldEditor instanceof ExtendedFontFieldEditor2)
				((ExtendedFontFieldEditor2) fieldEditor).adjustForNumColumns( columns );
		}
	}

	/**
	 * @param i
	 */
	private void computeColumn( int i ) {
		if ( columns < i ) columns = i;		
	}
	protected void performApply() {		
		super.performApply();
		
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {	
		super.performDefaults();
	}
	public boolean performOk() {
 		for (int i = 0; i < fieldEditorArray.size(); i++)
	 		((FieldEditor) fieldEditorArray.get(i) ).store();
	 	return super.performOk();
	}
}

/*
$Log: G2Gui_Display.java,v $
Revision 1.12  2003/08/18 00:24:44  zet
cleanup

Revision 1.11  2003/08/17 23:13:42  zet
centralize resources, move images

Revision 1.10  2003/08/17 16:57:55  zet
*** empty log message ***

Revision 1.9  2003/08/14 12:59:47  zet
fix typo

Revision 1.8  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.7  2003/08/11 11:27:46  lemmstercvs01
display only connected servers added

Revision 1.6  2003/08/08 21:11:15  zet
set defaults in central location



*/