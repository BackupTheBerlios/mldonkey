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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.jface.preference.BooleanFieldEditor;

/**
 * G2GuiDisplayDownloads
 *
 * @version $Id: G2GuiDisplayDownloads.java,v 1.5 2004/03/01 21:12:21 psy Exp $ 
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
		
		if ( advancedMode() ) {
			setupEditor( 
					new BooleanFieldEditor( 
						"maintainSortOrder",
						G2GuiResources.getString( "PREF_DISPLAY_SORT_ORDER" ),
						composite ) );
			
			setupEditor( 
					new BooleanFieldEditor( 
						"displayChunkGraphs",
						G2GuiResources.getString( "PREF_DISPLAY_CHUNK" ),
						composite ) );
		}
		Label bar1 = new Label( composite, SWT.SEPARATOR | SWT.HORIZONTAL );
			bar1.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		
		Label bar2 = new Label( composite, SWT.SEPARATOR | SWT.HORIZONTAL );
			bar2.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

			
		setupEditor( 
			new ExtendedColorFieldEditor( 
				"downloadsBackgroundColor",
				G2GuiResources.getString( "PREF_DISPLAY_D_BACKGROUND" ),
				composite ) );
		
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
Revision 1.5  2004/03/01 21:12:21  psy
removed download-table font config (use global one instead)
started re-arranging the preferences (to be continued...)

Revision 1.4  2003/11/01 17:26:55  zet
remove fox exclusion

Revision 1.3  2003/10/31 23:33:01  zet
disable font selector on "fox"

Revision 1.2  2003/10/16 16:10:14  zet
background/font

Revision 1.1  2003/10/15 22:06:13  zet
Split Console/Downloads pref pages.

*/