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
	private BooleanFieldEditor displayChunkGraphs, displayGridLines,
							tableCellEditors, displayHeaderBar, displayAllServers;
	private IntegerFieldEditor displayBuffer;
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

		displayAllServers = new BooleanFieldEditor( "displayAllServers", "Show only connected Servers", shell );
		setupEditor( displayAllServers );		
		
		displayHeaderBar = new BooleanFieldEditor("displayHeaderBar", "Display header bar", shell);
		setupEditor(displayHeaderBar);
		
		displayChunkGraphs = new BooleanFieldEditor("displayChunkGraphs", "Display chunk graphs", shell);
		setupEditor(displayChunkGraphs);
		
		displayGridLines = new BooleanFieldEditor("displayGridLines", "Display grid lines", shell);
		setupEditor(displayGridLines);
		
		tableCellEditors = new BooleanFieldEditor("tableCellEditors", "Activate table cell editors", shell);
		setupEditor(tableCellEditors);
		
		displayBuffer = new IntegerFieldEditor("displayBuffer", "GUI update buffer (0-60 seconds)", shell);
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
		setHorizontalSpan(consoleForeground);
		setHorizontalSpan(consoleBackground);
		setHorizontalSpan(consoleInputForeground);
		setHorizontalSpan(consoleInputBackground);
		displayHeaderBar.fillIntoGrid(controlshell, columns);
		displayChunkGraphs.fillIntoGrid(controlshell, columns);
		displayGridLines.fillIntoGrid(controlshell, columns);
		tableCellEditors.fillIntoGrid(controlshell, columns);
		displayBuffer.fillIntoGrid(controlshell, columns);
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
			displayAllServers.store();
			displayHeaderBar.store();
			displayChunkGraphs.store();
			displayGridLines.store();
			tableCellEditors.store();
			displayBuffer.store();
		}	
	 	return super.performOk();
	 	 	
	}

}

/*
$Log: G2Gui_Display.java,v $
Revision 1.7  2003/08/11 11:27:46  lemmstercvs01
display only connected servers added

Revision 1.6  2003/08/08 21:11:15  zet
set defaults in central location



*/