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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

/**
 * PreferenceLoader
 *
 *
 * @version $Id: PreferenceLoader.java,v 1.18 2003/09/03 18:19:40 zet Exp $
 */
public class PreferenceLoader {

	private static PreferenceStore preferenceStore = new PreferenceStore( "g2gui.pref" );
	private static Map fontMap = new Hashtable();
	private static Map colorMap = new Hashtable();
	private static List fontArray = new ArrayList();
	private static List colorArray = new ArrayList();
	
	// prevent instantiation
	private PreferenceLoader() {
	}
	
	/**
	 * @return
	 */
	static void loadStore() {
		try { preferenceStore.load(); } catch ( IOException e ) { }
		preferenceStore =  ( PreferenceStore ) setDefaults( preferenceStore );
	}
	/**
	 * @param preferenceStore
	 * @return
	 */
	static IPreferenceStore setDefaults(IPreferenceStore preferenceStore) {
		Display display = Display.getDefault();
	
		preferenceStore.setDefault( "initialized", false );
		preferenceStore.setDefault( "windowMaximized", false );
		preferenceStore.setDefault( "coolbarLocked", true );
		preferenceStore.setDefault( "toolbarSmallButtons", false );
		preferenceStore.setDefault( "flatInterface", false );
	
		PreferenceConverter.setDefault(preferenceStore, "consoleBackground", display.getSystemColor(SWT.COLOR_LIST_BACKGROUND).getRGB() );
		PreferenceConverter.setDefault(preferenceStore, "consoleForeground", display.getSystemColor(SWT.COLOR_LIST_FOREGROUND).getRGB() );
		PreferenceConverter.setDefault(preferenceStore, "consoleHighlight",  display.getSystemColor(SWT.COLOR_LIST_SELECTION).getRGB() );
		PreferenceConverter.setDefault(preferenceStore, "consoleInputBackground", display.getSystemColor(SWT.COLOR_LIST_BACKGROUND).getRGB() );
		PreferenceConverter.setDefault(preferenceStore, "consoleInputForeground", display.getSystemColor(SWT.COLOR_LIST_FOREGROUND).getRGB() );
		PreferenceConverter.setDefault(preferenceStore, "consoleFontData", JFaceResources.getTextFont().getFontData());
		
		preferenceStore.setDefault( "hostname", "localhost" );
		preferenceStore.setDefault( "username", "admin" );
		preferenceStore.setDefault( "password", "" );
		preferenceStore.setDefault( "port", "4001" );
		preferenceStore.setDefault( "advancedMode", false);

		preferenceStore.setDefault( "searchFilterPornography", false );
		preferenceStore.setDefault( "searchFilterProfanity", false );
		
		preferenceStore.setDefault( "maintainSortOrder", false );
		preferenceStore.setDefault( "displayAllServers", true );
		preferenceStore.setDefault( "displayChunkGraphs", false );
		preferenceStore.setDefault( "displayGridLines", true );
		preferenceStore.setDefault( "tableCellEditors", false );
		
		preferenceStore.setDefault( "coreExecutable", "" );
		
		return preferenceStore;
	}
	
	/**
	 * @param preferenceString
	 * @return
	 */
	static public Font loadFont( String preferenceString ) {
		if (preferenceStore.contains( preferenceString )) {
			Font newFont = new Font (null, PreferenceConverter.getFontDataArray( preferenceStore, preferenceString ));
			if (fontMap.containsKey(preferenceString)) {
				if (newFont.getFontData()[0].equals( ((Font) fontMap.get(preferenceString)).getFontData()[0] )) {
					newFont.dispose();
				} else {
					fontArray.add(newFont);
					fontMap.put(preferenceString, newFont);
				}
			} else {
				fontArray.add(newFont);
				fontMap.put(preferenceString, newFont);
			}
			return (Font) fontMap.get(preferenceString);
		}
		return null;
	}
	/**
	 * @param preferenceString
	 * @return
	 */
	static public Color loadColour (String preferenceString ) {
		if (preferenceStore.contains( preferenceString )) {
			Color newColor = new Color (null, PreferenceConverter.getColor( preferenceStore, preferenceString ));
			if (colorMap.containsKey(preferenceString)) {
				if (newColor.getRGB().equals( ((Color) colorMap.get(preferenceString)).getRGB())) {
					newColor.dispose();
				} else {
					colorArray.add(newColor);
					colorMap.put(preferenceString, newColor);
				}
			} else {
				colorArray.add(newColor);
				colorMap.put(preferenceString, newColor);
			}
			return (Color) colorMap.get(preferenceString);
		}
		return null;
	}
	
	/**
	 * @param preferenceString
	 * @return
	 */
	static public Rectangle loadRectangle (String preferenceString ) {
		if (preferenceStore.contains( preferenceString ))
			return PreferenceConverter.getRectangle(preferenceStore, preferenceString);
		return null;
	}

	/**
	 * @param preferenceString
	 * @return
	 */
	static public boolean loadBoolean (String preferenceString ) {
		if (preferenceStore.contains( preferenceString ))
			return preferenceStore.getBoolean( preferenceString );
		return true;
	}	
	
	/**
	 * @param preferenceString
	 * @return
	 */
	static public int  loadInteger(String preferenceString ) {
		if (preferenceStore.contains( preferenceString ))
			return preferenceStore.getInt( preferenceString );
		return 0;
	}
	
	static public String loadString(String preferenceString ) {
		if (preferenceStore.contains( preferenceString ))
			return preferenceStore.getString( preferenceString );
		return "";
	}
	
	static public PreferenceStore getPreferenceStore() {
		return preferenceStore;
	}
	static public void saveStore() {
		try { preferenceStore.save(); }
		catch ( IOException e2 ) {  }
	}
	static public boolean contains(String preferenceString) {
		return preferenceStore.contains(preferenceString);
	}
	static public void initialize() {
		loadStore();
	}
	static public void cleanUp() {
		Iterator fonts = fontArray.iterator();
		while(fonts.hasNext()){
		  ((Font) fonts.next()).dispose();
		}
		Iterator colors = colorArray.iterator();
		while(colors.hasNext()){
		  ((Color) colors.next()).dispose();
		}
	}
	
}
/*
$Log: PreferenceLoader.java,v $
Revision 1.18  2003/09/03 18:19:40  zet
remove unused

Revision 1.17  2003/09/03 14:49:07  zet
optionally spawn core from gui

Revision 1.16  2003/08/28 18:27:32  zet
configurable flat interface

Revision 1.15  2003/08/26 22:44:03  zet
basic filtering

Revision 1.14  2003/08/24 18:05:01  zet
handle cleanup of colors/fonts

Revision 1.13  2003/08/24 16:37:04  zet
combine the preference stores

Revision 1.12  2003/08/23 15:21:37  zet
remove @author

Revision 1.11  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.10  2003/08/22 21:10:57  lemmster
replace $user$ with $Author: zet $

Revision 1.9  2003/08/19 21:44:35  zet
PreferenceLoader updates

Revision 1.8  2003/08/19 17:12:56  zet
set defaults

Revision 1.7  2003/08/19 15:41:09  zet
fix crash when no advancedMode is set. (set a default)

Revision 1.6  2003/08/18 14:51:58  dek
some more jface-work

Revision 1.5  2003/08/17 21:05:38  zet
Use system default colours

Revision 1.4  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.3  2003/08/11 11:27:46  lemmstercvs01
display only connected servers added

Revision 1.2  2003/08/08 21:11:15  zet
set defaults in central location



*/