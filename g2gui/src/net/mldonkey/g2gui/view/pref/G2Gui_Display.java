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
import java.util.ArrayList;

import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
/**
 * G2Gui_Display
 *
 *
 * @version $Id: G2Gui_Display.java,v 1.18 2003/08/23 15:21:37 zet Exp $
 */
public class G2Gui_Display extends FieldEditorPreferencePage {
	private Composite parent;
	private ArrayList fieldEditorArray = new ArrayList();
	private int columns = 0;
	public G2Gui_Display( String string, int i ) {
		super( string, i );
	}
	protected void setupEditor( FieldEditor e ) {
		e.setPreferencePage( this );
		e.setPreferenceStore( getPreferenceStore() );
		e.load();
		computeColumn( e.getNumberOfControls() );
		addField( e );
		fieldEditorArray.add( e );
	}
	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.PreferencePage#setPreferenceStore( org.eclipse.jface.preference.IPreferenceStore )
	 */
	public void setPreferenceStore( IPreferenceStore store ) {
		super.setPreferenceStore( PreferenceLoader.setDefaults( store ) );
	}
	protected Control createContents( Composite myparent ) {
		this.parent = ( Composite ) super.createContents( myparent );
		return parent;
	}
	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	private void setHorizontalSpan( FieldEditor editor ) {
		if ( editor instanceof ExtendedColorFieldEditor ) {
			(( GridData )
				( ( ExtendedColorFieldEditor ) editor ).getChangeControl( parent ).getLayoutData() 
						).horizontalSpan = columns - 1;
			( ( GridData )
				( ( ExtendedColorFieldEditor ) editor ).getChangeControl( parent ).getLayoutData()
						).horizontalAlignment = GridData.FILL;
		} else {
			( ( GridData ) 
				( ( StringFieldEditor ) editor ).getTextControl( parent ).getLayoutData() 
						 ).horizontalSpan = columns - 1;
		}
		( ( GridLayout ) parent.getLayout() ).numColumns = columns;
	}
	
	private void arrangeFields() {
		for ( int i = 0; i < fieldEditorArray.size(); i++ ) {
			FieldEditor fieldEditor = ( FieldEditor ) fieldEditorArray.get( i );
			if ( fieldEditor instanceof ExtendedColorFieldEditor )
				setHorizontalSpan( ( ExtendedColorFieldEditor ) fieldEditor );
			else if ( 
				fieldEditor instanceof BooleanFieldEditor
					|| fieldEditor instanceof IntegerFieldEditor )
				fieldEditor.fillIntoGrid( parent, columns );
			else if ( fieldEditor instanceof ExtendedFontFieldEditor2 )
				( ( ExtendedFontFieldEditor2 ) fieldEditor ).adjustForNumColumns( 
					columns );
		}
	}
	/**
	 * @param i
	 */
	private void computeColumn( int i ) {
		if ( columns < i )
			columns = i;
	}
	/* ( non-Javadoc )
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		boolean advanced = getPreferenceStore().getBoolean( "advancedMode" );
		parent = getFieldEditorParent();
		if ( advanced ){		
			setupEditor( new ExtendedColorFieldEditor( "consoleBackground", G2GuiResources.getString("PREF_DISPLAY_CONSOLE_OBK"), parent ) );
			setupEditor( new ExtendedColorFieldEditor( "consoleForeground", G2GuiResources.getString("PREF_DISPLAY_CONSOLE_OFG"),parent ) );
			setupEditor( new ExtendedColorFieldEditor( "consoleHighlight",  G2GuiResources.getString("PREF_DISPLAY_CONSOLE_HL"), parent ) );
			setupEditor( new ExtendedColorFieldEditor( "consoleInputBackground", G2GuiResources.getString("PREF_DISPLAY_CONSOLE_IBK"), parent ) );
			setupEditor( new ExtendedColorFieldEditor( "consoleInputForeground", G2GuiResources.getString("PREF_DISPLAY_CONSOLE_IFG"), parent ) );
			setupEditor( new ExtendedFontFieldEditor2( "consoleFontData", G2GuiResources.getString("PREF_DISPLAY_CONSOLE_FONT"), "Sample", parent ) );
			setupEditor( new BooleanFieldEditor( "displayAllServers", G2GuiResources.getString("PREF_DISPLAY_CONN_SERV"), parent ) );
		}			
																								
		setupEditor( new BooleanFieldEditor( "displayHeaderBar", G2GuiResources.getString("PREF_DISPLAY_HEADER"), parent ) );
				
		setupEditor( new BooleanFieldEditor(  "displayChunkGraphs", G2GuiResources.getString("PREF_DISPLAY_CHUNK"), parent ) );
				
		setupEditor( new BooleanFieldEditor( "displayGridLines", G2GuiResources.getString("PREF_DISPLAY_GRID"), parent ) );
				
		setupEditor( new BooleanFieldEditor( "tableCellEditors", G2GuiResources.getString("PREF_DISPLAY_CELLEDITORS"), parent ) );
				
		// setupEditor( new BooleanFieldEditor( "forceRefresh", G2GuiResources.getString("PREF_DISPLAY_FULL_REFRESH"),parent ) );
			
		setupEditor( new BooleanFieldEditor( "maintainSortOrder", G2GuiResources.getString("PREF_DISPLAY_SORT_ORDER"),parent ) );	
				
	//	IntegerFieldEditor displayBuffer = new IntegerFieldEditor( "displayBuffer", G2GuiResources.getString("PREF_DISPLAY_UPDATE_BUFFER"),parent );
	//	displayBuffer.setValidRange( 0, 60 );
	//	setupEditor( displayBuffer );
		arrangeFields();
	}
}
/*
$Log: G2Gui_Display.java,v $
Revision 1.18  2003/08/23 15:21:37  zet
remove @author

Revision 1.17  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.16  2003/08/22 21:10:57  lemmster
replace $user$ with $Author: zet $

Revision 1.15  2003/08/19 22:02:15  zet
localise

Revision 1.14  2003/08/19 13:16:10  dek
advanced-Mode introduced

Revision 1.13  2003/08/18 14:51:58  dek
some more jface-work

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