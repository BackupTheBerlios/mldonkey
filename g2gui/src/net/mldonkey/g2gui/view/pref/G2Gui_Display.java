/*
 * Created on Jul 24, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.mldonkey.g2gui.view.pref;

import org.eclipse.jface.preference.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.layout.*;

/**
 * @author  
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class G2Gui_Display extends PreferencePage  {
	private Composite controlshell;
	private ExtendedColorFieldEditor consoleBackground, consoleForeground,
				consoleInputBackground, consoleInputForeground;
	private ExtendedFontFieldEditor2 consoleFontData;
	private int columns = 0;
	
	public G2Gui_Display(PreferenceStore preferenceStore, boolean connected) {
		super( "Display" );
		setPreferenceStore(preferenceStore);
	}
	
	protected void setupEditor(FieldEditor e) {
		e.setPreferencePage(this);
		e.setPreferenceStore( getPreferenceStore() );
		e.load();
		computeColumn( e.getNumberOfControls() );
	}
	
	protected Control createContents( Composite shell ) {	
		this.controlshell = shell;			
						
		consoleBackground = new ExtendedColorFieldEditor("consoleBackground", "Console window background colour", shell);
		setupEditor(consoleBackground);
			
		consoleForeground = new ExtendedColorFieldEditor("consoleForeground", "Console window foreground colour", shell);
		setupEditor(consoleForeground);
	
		consoleInputBackground = new ExtendedColorFieldEditor("consoleInputBackground", "Console input background colour", shell);
		setupEditor(consoleInputBackground);
			
		consoleInputForeground = new ExtendedColorFieldEditor("consoleInputForeground", "Console input foreground colour", shell);
		setupEditor(consoleInputForeground);
	
		consoleFontData = new ExtendedFontFieldEditor2("consoleFontData", "Console window font", "Sample",  shell);
		setupEditor(consoleFontData);
		
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
		setHorizontalSpan(consoleForeground);
		setHorizontalSpan(consoleBackground);
		setHorizontalSpan(consoleInputForeground);
		setHorizontalSpan(consoleInputBackground);
		consoleFontData.adjustForNumColumns( columns );
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
	 	if (consoleBackground != null) { 
	 		consoleBackground.store();
			consoleForeground.store();
			consoleInputBackground.store();
			consoleInputForeground.store();
			consoleFontData.store();
		}	
	 	return super.performOk();
	 	 	
	}

}

