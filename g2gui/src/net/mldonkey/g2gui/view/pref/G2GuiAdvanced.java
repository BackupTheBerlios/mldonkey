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
 * (  at your option  ) any later version.
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
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.SWT;
/**
 * G2Gui_Display
 *
 *
 * @version $Id: G2GuiAdvanced.java,v 1.1 2003/10/01 20:56:27 lemmster Exp $
 */
public class G2GuiAdvanced extends PreferencePage {

	/**
	 * @param string The name of the OptionsPage
	 * @param i Style: SWT.XXXX
	 */
	public G2GuiAdvanced( String string, int i ) {
		super( string, i );
	}

	/* (  non-Javadoc  )
	 * @see org.eclipse.jface.preference.PreferencePage#
	 * setPreferenceStore(  org.eclipse.jface.preference.IPreferenceStore  )
	 */
	public void setPreferenceStore( IPreferenceStore store ) {
		super.setPreferenceStore( PreferenceLoader.setDefaults( store ) );
	}
	
	/* (   non-Javadoc   )
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		boolean advanced = getPreferenceStore().getBoolean( "advancedMode" );

		composite = getFieldEditorParent();
		
		if ( SWT.getPlatform().equals("win32") ) {
			setupEditor( 
				new BooleanFieldEditor( 
					"minimizeOnClose",
					G2GuiResources.getString( "PREF_DISPLAY_MIN_CLOSE" ),
					composite ) );
		
		}	
		
		setupEditor( 
			new BooleanFieldEditor( 
				"flatInterface",
				G2GuiResources.getString( "PREF_DISPLAY_FLAT_INTERFACE" ),
				composite ) );
		
		setupEditor( 
			new BooleanFieldEditor( 
				"pollUpStats",
				G2GuiResources.getString( "PREF_DISPLAY_POLL_UPSTATS" ),
				composite ) );

		if ( advanced ) {
		
			setupEditor( 
				new BooleanFieldEditor( 
					"displayAllServers",
					G2GuiResources.getString( "PREF_DISPLAY_CONN_SERV" ),
					composite ) );
						
			setupEditor( 
				new BooleanFieldEditor( 
					"displayNodes",
					G2GuiResources.getString( "PREF_DISPLAY_NODES" ),
					composite ) );
	
		}
		setupEditor(
			new BooleanFieldEditor(
			"showSearchTooltip",
			G2GuiResources.getString( "PREF_SEARCH_SHOW_TOOLTIP" ),
			composite ) );
	
		setupEditor( 
			new BooleanFieldEditor( 
				"searchFilterPornography",
				G2GuiResources.getString( "PREF_SEARCH_FILTER_PORNOGRAPHY" ),
				composite ) );

		setupEditor( 
			new BooleanFieldEditor( 
				"searchFilterProfanity",
				G2GuiResources.getString( "PREF_SEARCH_FILTER_PROFANITY" ),
				composite ) );

		setupEditor( 
				new BooleanFieldEditor( 
				"displayTableColors",
				G2GuiResources.getString( "PREF_DISPLAY_TABLE_COLORS" ),
				composite ) );

		if ( advanced ) setupEditor( 
			new BooleanFieldEditor( 
				"displayChunkGraphs",
				G2GuiResources.getString( "PREF_DISPLAY_CHUNK" ),
				composite ) );

		setupEditor( 
			new BooleanFieldEditor( 
				"displayGridLines",
				G2GuiResources.getString( "PREF_DISPLAY_GRID" ),
				composite ) );

		setupEditor( 
			new BooleanFieldEditor( 
				"tableCellEditors",
				G2GuiResources.getString( "PREF_DISPLAY_CELLEDITORS" ),
				composite ) );
		
		setupEditor( 
				new BooleanFieldEditor( 
				"dragAndDrop",
				G2GuiResources.getString( "PREF_DISPLAY_DRAG_AND_DROP" ),
				composite ) );
				
		setupEditor( 
			new BooleanFieldEditor( 
				"maintainSortOrder",
				G2GuiResources.getString( "PREF_DISPLAY_SORT_ORDER" ),
				composite ) );

		IntegerFieldEditor updateDelayEditor = new IntegerFieldEditor ( 
				"updateDelay",
				G2GuiResources.getString( "PREF_DISPLAY_UPDATE_DELAY" ),
				composite ) ;
		updateDelayEditor.setValidRange( 0, 600 );
		setupEditor( updateDelayEditor );
	}
}
/*
$Log: G2GuiAdvanced.java,v $
Revision 1.1  2003/10/01 20:56:27  lemmster
add class hierarchy

Revision 1.7  2003/09/29 17:44:40  lemmster
switch for search tooltips

Revision 1.6  2003/09/27 00:36:54  zet
auto poll for upstats preference

Revision 1.5  2003/09/26 04:19:06  zet
drag&drop

Revision 1.4  2003/09/21 23:40:01  zet
displayTableColors preference

Revision 1.3  2003/09/19 14:25:55  zet
min to systray option

Revision 1.2  2003/09/18 10:23:48  lemmster
checkstyle

Revision 1.1  2003/09/15 22:06:19  zet
split preferences


*/