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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

/**
 * @author  
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PreferenceLoader {

	public PreferenceLoader () {
		
	}
	
	/**
	 * @return
	 */
	static PreferenceStore loadStore() {
		PreferenceStore preferenceStore = new PreferenceStore( "g2gui.pref" );
		try { preferenceStore.load(); } catch ( IOException e ) { }
		
		preferenceStore.setDefault("displayGridLines", true);
		preferenceStore.setDefault("tableCellEditors", false);
		preferenceStore.setDefault("displayBuffer", 2);
		preferenceStore.setDefault("displayHeaderBar", true);
		
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
