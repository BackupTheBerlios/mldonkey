/*
 * Copyright 2003
 * g2gui Team
 * 
 * 
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.view.pref;

import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.widgets.Composite;

/**
 * G2GuiDisplayConsole
 *
 * @version $Id: G2GuiDisplayConsole.java,v 1.1 2003/10/15 22:06:13 zet Exp $ 
 *
 */
public class G2GuiDisplayConsole extends PreferencePage {
	
	public G2GuiDisplayConsole( String string, int i ) {
				super( string, i );
	}
    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
     */
    protected void createFieldEditors() {
		Composite composite = getFieldEditorParent();
    	
		setupEditor( 
			new ExtendedColorFieldEditor( 
				"consoleBackground",
				G2GuiResources.getString( "PREF_DISPLAY_CONSOLE_OBK" ),
				composite ) );

		setupEditor( 
			new ExtendedColorFieldEditor( 
				"consoleForeground",
				G2GuiResources.getString( "PREF_DISPLAY_CONSOLE_OFG" ),
				composite ) );

		setupEditor( 
			new ExtendedColorFieldEditor( 
				"consoleHighlight",
				G2GuiResources.getString( "PREF_DISPLAY_CONSOLE_HL" ),
				composite ) );

		setupEditor( 
			new ExtendedColorFieldEditor( 
				"consoleInputBackground",
				G2GuiResources.getString( "PREF_DISPLAY_CONSOLE_IBK" ),
				composite ) );

		setupEditor( 
			new ExtendedColorFieldEditor( 
				"consoleInputForeground",
				G2GuiResources.getString( "PREF_DISPLAY_CONSOLE_IFG" ),
				composite ) );

		setupEditor( 
			new ExtendedFontFieldEditor2( 
				"consoleFontData",
				G2GuiResources.getString( "PREF_DISPLAY_CONSOLE_FONT" ),
				G2GuiResources.getString( "PREF_DISPLAY_SAMPLE" ),
				composite ) );

    }

}

/*
$Log: G2GuiDisplayConsole.java,v $
Revision 1.1  2003/10/15 22:06:13  zet
Split Console/Downloads pref pages.

*/