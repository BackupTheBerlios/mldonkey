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
 * G2GuiDisplayDownloads
 *
 * @version $Id: G2GuiDisplayDownloads.java,v 1.1 2003/10/15 22:06:13 zet Exp $ 
 *
 */
public class G2GuiDisplayDownloads extends PreferencePage {
	
	public G2GuiDisplayDownloads( String string, int i ) {
				super( string, i );
	}
		
    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
     */
    protected void createFieldEditors() {
		Composite composite = getFieldEditorParent();
		
		setupEditor( 
			new ExtendedColorFieldEditor( 
				"downloadsAvailableFileColor",
				G2GuiResources.getString( "PREF_DISPLAY_D_AVAILABLE" ),
				composite ) );
		
		setupEditor( 
			new ExtendedColorFieldEditor( 
				"downloadsUnAvailableFileColor",
				G2GuiResources.getString( "PREF_DISPLAY_D_UNAVAILABLE" ),
				composite ) );		
		
		setupEditor( 
			new ExtendedColorFieldEditor( 
				"downloadsPausedFileColor",
				G2GuiResources.getString( "PREF_DISPLAY_D_PAUSED" ),
				composite ) );
		
		setupEditor( 
			new ExtendedColorFieldEditor( 
				"downloadsQueuedFileColor",
				G2GuiResources.getString( "PREF_DISPLAY_D_QUEUED" ),
				composite ) );		
		
		setupEditor( 
			new ExtendedColorFieldEditor( 
				"downloadsDownloadedFileColor",
				G2GuiResources.getString( "PREF_DISPLAY_D_DOWNLOADED" ),
				composite ) );
	
		setupEditor( 
			new ExtendedColorFieldEditor( 
				"downloadsRateAbove20FileColor",
				G2GuiResources.getString( "PREF_DISPLAY_D_RATE20" ),
				composite ) );		
	
		setupEditor( 
			new ExtendedColorFieldEditor( 
				"downloadsRateAbove10FileColor",
				G2GuiResources.getString( "PREF_DISPLAY_D_RATE10" ),
				composite ) );
		
		setupEditor( 
			new ExtendedColorFieldEditor( 
				"downloadsRateAbove0FileColor",
				G2GuiResources.getString( "PREF_DISPLAY_D_RATE0" ),
				composite ) );		
    }

}

/*
$Log: G2GuiDisplayDownloads.java,v $
Revision 1.1  2003/10/15 22:06:13  zet
Split Console/Downloads pref pages.

*/