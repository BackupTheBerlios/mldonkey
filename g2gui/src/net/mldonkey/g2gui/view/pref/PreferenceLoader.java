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

import java.io.IOException;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

/**
 * @author  
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PreferenceLoader {

	/**
	 * @return
	 */
	static PreferenceStore loadStore() {
		PreferenceStore preferenceStore = new PreferenceStore( "g2gui.pref" );
		try { preferenceStore.load(); } catch ( IOException e ) { }
		
		preferenceStore = setDefaults(preferenceStore);
		return preferenceStore;
	}
	
	/**
	 * @param preferenceStore
	 * @return
	 */
	static PreferenceStore setDefaults(PreferenceStore preferenceStore) {
		Display display = Display.getDefault();
	
		PreferenceConverter.setDefault(preferenceStore, "consoleBackground", display.getSystemColor(SWT.COLOR_LIST_BACKGROUND).getRGB() );
		PreferenceConverter.setDefault(preferenceStore, "consoleForeground", display.getSystemColor(SWT.COLOR_LIST_FOREGROUND).getRGB() );
		PreferenceConverter.setDefault(preferenceStore, "consoleHighlight",  display.getSystemColor(SWT.COLOR_LIST_SELECTION).getRGB() );
		PreferenceConverter.setDefault(preferenceStore, "consoleInputBackground", display.getSystemColor(SWT.COLOR_LIST_BACKGROUND).getRGB() );
		PreferenceConverter.setDefault(preferenceStore, "consoleInputForeground", display.getSystemColor(SWT.COLOR_LIST_FOREGROUND).getRGB() );
		
		preferenceStore.setDefault( "displayAllServers", true );
		preferenceStore.setDefault( "displayChunkGraphs", false );
		preferenceStore.setDefault( "displayGridLines", true );
		preferenceStore.setDefault( "tableCellEditors", false );
		preferenceStore.setDefault( "displayBuffer", 2 );
		preferenceStore.setDefault( "displayHeaderBar", true );
		preferenceStore.setDefault( "forceRefresh", false );
		return preferenceStore;
	}
	
	/**
	 * @param preferenceString
	 * @return
	 */
	static public Font loadFont( String preferenceString ) {
		PreferenceStore preferenceStore = loadStore();
		
		if (preferenceStore.contains( preferenceString )) 	
			return new Font (null, PreferenceConverter.getFontDataArray( preferenceStore, preferenceString ) ); 
		return null;
	}
	/**
	 * @param preferenceString
	 * @return
	 */
	static public Color loadColour (String preferenceString ) {
		PreferenceStore preferenceStore = loadStore();
		
		if (preferenceStore.contains( preferenceString ))
			return new Color( null, PreferenceConverter.getColor(preferenceStore, preferenceString ) );
		return null;
	}
	/**
	 * @param preferenceString
	 * @return
	 */
	static public boolean loadBoolean (String preferenceString ) {
		PreferenceStore preferenceStore = loadStore();
				
		if (preferenceStore.contains( preferenceString ))
			return preferenceStore.getBoolean( preferenceString );
		return true;
	}	
	
	/**
	 * @param preferenceString
	 * @return
	 */
	static public int  loadInteger(String preferenceString ) {
		PreferenceStore preferenceStore = loadStore();
		
		if (preferenceStore.contains( preferenceString ))
			return preferenceStore.getInt( preferenceString );
		return 0;
	}
}
/*
$Log: PreferenceLoader.java,v $
Revision 1.5  2003/08/17 21:05:38  zet
Use system default colours

Revision 1.4  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.3  2003/08/11 11:27:46  lemmstercvs01
display only connected servers added

Revision 1.2  2003/08/08 21:11:15  zet
set defaults in central location



*/