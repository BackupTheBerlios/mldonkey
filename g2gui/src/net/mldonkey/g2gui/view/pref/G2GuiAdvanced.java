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
import net.mldonkey.g2gui.view.helper.VersionCheck;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
/**
 * G2Gui_Display
 *
 *
 * @version $Id: G2GuiAdvanced.java,v 1.13 2004/03/01 21:12:21 psy Exp $
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
		//boolean advanced = getPreferenceStore().getBoolean( "advancedMode" );

		Composite composite = getFieldEditorParent();
		
		if ( SWT.getPlatform().equals("win32") ) {
			setupEditor( 
				new BooleanFieldEditor( 
					"minimizeOnClose",
					G2GuiResources.getString( "PREF_DISPLAY_MIN_CLOSE" ),
					composite ) );
		
		}	
		
		setupEditor( 
			new BooleanFieldEditor( 
				"pollUpStats",
				G2GuiResources.getString( "PREF_DISPLAY_POLL_UPSTATS" ),
				composite ) );

		if ( advancedMode() ) {
		
			setupEditor( 
				new BooleanFieldEditor( 
					"displayNodes",
					G2GuiResources.getString( "PREF_DISPLAY_NODES" ),
					composite ) );
	
		}
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
				"tableCellEditors",
				G2GuiResources.getString( "PREF_DISPLAY_CELLEDITORS" ),
				composite ) );
		
		setupEditor( 
				new BooleanFieldEditor( 
				"dragAndDrop",
				G2GuiResources.getString( "PREF_DISPLAY_DRAG_AND_DROP" ),
				composite ) );
				


		IntegerFieldEditor updateDelayEditor = new IntegerFieldEditor ( 
				"updateDelay",
				G2GuiResources.getString( "PREF_DISPLAY_UPDATE_DELAY" ),
				composite ) ;
		updateDelayEditor.setValidRange( 0, 600 );
		setupEditor( updateDelayEditor );
		
		
		GCJFileFieldEditor defaultBrowserField =
				new GCJFileFieldEditor( "defaultWebBrowser", 
				G2GuiResources.getString( "PREF_DISPLAY_DEFAULT_BROWSER" ), 
		        false, 
		        composite, true );
			
		if ( VersionCheck.isWin32() ) {
			defaultBrowserField.setFileExtensions( new String[] { "*.exe;*.bat" } );
		} else {
			defaultBrowserField.setFileExtensions( new String[] {"*"});
		}
				
		setupEditor(defaultBrowserField);		
	
	}
}
/*
$Log: G2GuiAdvanced.java,v $
Revision 1.13  2004/03/01 21:12:21  psy
removed download-table font config (use global one instead)
started re-arranging the preferences (to be continued...)

Revision 1.12  2004/01/08 21:44:40  psy
make use of isWin32()

Revision 1.11  2003/12/04 08:47:27  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.10  2003/11/29 13:03:54  lemmy
ToolTip complete reworked (to be continued)

Revision 1.9  2003/11/27 20:45:51  zet
alwaysValid option

Revision 1.8  2003/11/27 20:32:38  zet
no abs paths

Revision 1.7  2003/11/27 15:49:24  zet
filebrowser

Revision 1.6  2003/11/27 15:33:24  zet
defWebBrowser

Revision 1.5  2003/11/09 23:09:57  lemmy
remove "Show connected Servers only"
added filter saving in searchtab

Revision 1.4  2003/10/16 22:02:45  zet
move some options

Revision 1.3  2003/10/12 16:27:01  lemmy
minor changes

Revision 1.2  2003/10/08 01:12:16  zet
useGradient preference

Revision 1.1  2003/10/01 20:56:27  lemmy
add class hierarchy

Revision 1.7  2003/09/29 17:44:40  lemmy
switch for search tooltips

Revision 1.6  2003/09/27 00:36:54  zet
auto poll for upstats preference

Revision 1.5  2003/09/26 04:19:06  zet
drag&drop

Revision 1.4  2003/09/21 23:40:01  zet
displayTableColors preference

Revision 1.3  2003/09/19 14:25:55  zet
min to systray option

Revision 1.2  2003/09/18 10:23:48  lemmy
checkstyle

Revision 1.1  2003/09/15 22:06:19  zet
split preferences


*/