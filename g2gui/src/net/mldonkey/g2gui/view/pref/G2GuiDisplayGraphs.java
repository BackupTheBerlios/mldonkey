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
 * G2GuiDisplayGraphs
 *
 * @version $Id: G2GuiDisplayGraphs.java,v 1.2 2003/10/23 01:51:23 zet Exp $ 
 *
 */
public class G2GuiDisplayGraphs extends PreferencePage {
	
	public G2GuiDisplayGraphs( String string, int i ) {
				super( string, i );
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		Composite composite = getFieldEditorParent();
	
		setupEditor( 
			new ExtendedColorFieldEditor( 
				"graphBackgroundColor",
				G2GuiResources.getString( "PREF_DISPLAY_G_BACKGROUND" ),
				composite ) );

		setupEditor( 
			new ExtendedColorFieldEditor( 
				"graphGridColor",
				G2GuiResources.getString( "PREF_DISPLAY_G_GRID" ),
				composite ) );
				
		setupEditor( 
			new ExtendedColorFieldEditor( 
				"graphTextColor",
				G2GuiResources.getString( "PREF_DISPLAY_G_TEXT" ),
				composite ) );		

		setupEditor( 
			new ExtendedColorFieldEditor( 
				"graphUploadsColor1",
				G2GuiResources.getString( "PREF_DISPLAY_G_UPLOADS1" ),
				composite ) );

		setupEditor( 
			new ExtendedColorFieldEditor( 
				"graphUploadsColor2",
				G2GuiResources.getString( "PREF_DISPLAY_G_UPLOADS2" ),
				composite ) );
				
		setupEditor( 
			new ExtendedColorFieldEditor( 
				"graphDownloadsColor1",
				G2GuiResources.getString( "PREF_DISPLAY_G_DOWNLOADS1" ),
				composite ) );

		setupEditor( 
			new ExtendedColorFieldEditor( 
				"graphDownloadsColor2",
				G2GuiResources.getString( "PREF_DISPLAY_G_DOWNLOADS2" ),
				composite ) );
		
		setupEditor( 
			new ExtendedColorFieldEditor( 
				"graphLabelBackgroundColor",
				G2GuiResources.getString( "PREF_DISPLAY_G_LABEL_BACKGROUND" ),
				composite ) );
				
		setupEditor( 
			new ExtendedColorFieldEditor( 
				"graphLabelTextColor",
				G2GuiResources.getString( "PREF_DISPLAY_G_LABEL_TEXT" ),
				composite ) );

		setupEditor( 
			new ExtendedColorFieldEditor( 
				"graphLabelLineColor",
				G2GuiResources.getString( "PREF_DISPLAY_G_LABEL_LINE" ),
				composite ) );
	
	}

}

/*
$Log: G2GuiDisplayGraphs.java,v $
Revision 1.2  2003/10/23 01:51:23  zet
typo

Revision 1.1  2003/10/17 15:35:48  zet
graph prefs

*/