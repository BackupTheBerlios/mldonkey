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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import net.mldonkey.g2gui.helper.RegExp;
import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.helper.VersionCheck;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.server.ServerPaneListener;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

/**
 * PreferenceLoader
 *
 *
 * @version $Id: PreferenceLoader.java,v 1.70 2004/11/20 23:41:43 lemmy Exp $
 */
public class PreferenceLoader {
    private static boolean restart = false;
	private static PreferenceStore preferenceStore;
    private static Map fontMap = new Hashtable();
    private static Map colorMap = new Hashtable();
    private static List fontArray = new ArrayList();
    private static List colorArray = new ArrayList();

    private static Map critPrefsMap = new HashMap();
    
    // critical preferences which need a restart after change
    private static final String critPrefsList[] = {"hostname", "username", "password", "port", 
    		"advancedMode", "flatInterface", "useGraident", "pollUpStats", "displayNodes", 
			"dragAndDrop", "coreExecutable","allClients"};
    
    // our current state, are we in the progress of relaunching or quitting?
    private static boolean relaunching = false;
    private static boolean quitting = false;

    
    // prevent instantiation
    private PreferenceLoader() {
    }
    
    /**
     * @return
     */
    public static void initialize() throws IOException {
    	String userhome = System.getProperty("user.home");
    	String fileSep = System.getProperty("file.separator");
    	String pathToConf = userhome + fileSep + ".g2gui" + fileSep;
    	initialize( ( userHomeExists() ? pathToConf : "" ) + "g2gui.pref" );
    }

    public static void initialize( String file ) throws IOException {
    	if ( preferenceStore == null ) {
    		System.out.println("Trying to use preferences: " + file);
    		preferenceStore = new PreferenceStore( file );
    		
    	}
    	
    	try {
    		preferenceStore.load();
    	}
    	catch ( IOException e ) {
    		// no pref file is created -> lets create one 
    		// (including all needed directories when under linux)
    		if (userHomeExists()) 
    			new File( new File( file ).getParent() ).mkdirs();
    		preferenceStore.save();
    		System.out.println("Created new configfile.");
    	}
    	preferenceStore = (PreferenceStore) setDefaults( preferenceStore );    	
    	
    	// save the critical preferences in a hashmap for later use with needsRestart()
    	saveCritPrefs();
    	
    }
    
    private static boolean userHomeExists() {
    	if (VersionCheck.isWin32() && System.getProperty("user.home").equals("\\"))
    		return false;
    	
    	return new File(System.getProperty("user.home")).exists(); 
    }
    
    /**
     * @param preferenceStore
     * @return
     */
    static IPreferenceStore setDefaults( IPreferenceStore preferenceStore ) {
        Display display = Display.getDefault();
        preferenceStore.setDefault( "initialized", false );
        preferenceStore.setDefault( "windowMaximized", false );
        preferenceStore.setDefault( "coolbarLocked", true );
        preferenceStore.setDefault( "toolbarSmallButtons", false );
        preferenceStore.setDefault( "flatInterface", false );
        preferenceStore.setDefault( "useGraident", true );
        preferenceStore.setDefault( "allClients", false);
        preferenceStore.setDefault( "splashScreen", true );
        
        PreferenceConverter.setDefault( preferenceStore, "windowBounds", new Rectangle(0,0,640,480) );
        
        PreferenceConverter.setDefault( preferenceStore, "consoleBackground",
                                        display.getSystemColor( SWT.COLOR_LIST_BACKGROUND ).getRGB() );
        PreferenceConverter.setDefault( preferenceStore, "consoleForeground",
                                        display.getSystemColor( SWT.COLOR_LIST_FOREGROUND ).getRGB() );
        PreferenceConverter.setDefault( preferenceStore, "consoleHighlight",
                                        display.getSystemColor( SWT.COLOR_LIST_SELECTION ).getRGB() );
        PreferenceConverter.setDefault( preferenceStore, "consoleInputBackground",
                                        display.getSystemColor( SWT.COLOR_LIST_BACKGROUND ).getRGB() );
        PreferenceConverter.setDefault( preferenceStore, "consoleInputForeground",
                                        display.getSystemColor( SWT.COLOR_LIST_FOREGROUND ).getRGB() );
        PreferenceConverter.setDefault( preferenceStore, "consoleFontData",
                                        JFaceResources.getTextFont().getFontData() );
             
		PreferenceConverter.setDefault( preferenceStore, "downloadsBackgroundColor", display.getSystemColor(SWT.COLOR_LIST_BACKGROUND).getRGB() );               
        PreferenceConverter.setDefault( preferenceStore, "downloadsAvailableFileColor", display.getSystemColor( SWT.COLOR_BLACK ).getRGB() );
		PreferenceConverter.setDefault( preferenceStore, "downloadsUnAvailableFileColor", new RGB(128,128,128) );
		PreferenceConverter.setDefault( preferenceStore, "downloadsDownloadedFileColor", display.getSystemColor( SWT.COLOR_BLUE ).getRGB() );
		PreferenceConverter.setDefault( preferenceStore, "downloadsQueuedFileColor", display.getSystemColor( SWT.COLOR_GRAY ).getRGB() );
		PreferenceConverter.setDefault( preferenceStore, "downloadsPausedFileColor", display.getSystemColor( SWT.COLOR_DARK_RED ).getRGB() );
		PreferenceConverter.setDefault( preferenceStore, "downloadsRateAbove20FileColor", new RGB(0,160,0) );
		PreferenceConverter.setDefault( preferenceStore, "downloadsRateAbove10FileColor", new RGB(0,140,0) );
		PreferenceConverter.setDefault( preferenceStore, "downloadsRateAbove0FileColor", new RGB(0,110,0) );
               
		PreferenceConverter.setDefault( preferenceStore, "graphUploadsColor1", display.getSystemColor( SWT.COLOR_BLUE ).getRGB() );
		PreferenceConverter.setDefault( preferenceStore, "graphUploadsColor2", new RGB(0,0,125) );
		PreferenceConverter.setDefault( preferenceStore, "graphDownloadsColor1", display.getSystemColor( SWT.COLOR_GREEN ).getRGB() );
		PreferenceConverter.setDefault( preferenceStore, "graphDownloadsColor2", new RGB(0,125,0) );
		PreferenceConverter.setDefault( preferenceStore, "graphBackgroundColor", display.getSystemColor( SWT.COLOR_BLACK ).getRGB() );
		PreferenceConverter.setDefault( preferenceStore, "graphGridColor", new RGB(0,128,64) );
		PreferenceConverter.setDefault( preferenceStore, "graphTextColor", display.getSystemColor( SWT.COLOR_WHITE ).getRGB() );
		PreferenceConverter.setDefault( preferenceStore, "graphLabelBackgroundColor", display.getSystemColor( SWT.COLOR_WHITE ).getRGB() );
		PreferenceConverter.setDefault( preferenceStore, "graphLabelLineColor", display.getSystemColor( SWT.COLOR_YELLOW ).getRGB() );
		PreferenceConverter.setDefault( preferenceStore, "graphLabelTextColor", display.getSystemColor( SWT.COLOR_BLACK ).getRGB() );
				
		
		preferenceStore.setDefault( "defaultWebBrowser", "");
        preferenceStore.setDefault( "hostname", "localhost" );
        preferenceStore.setDefault( "username", "admin" );
        preferenceStore.setDefault( "password", "" );
        preferenceStore.setDefault( "port", "4001" );
        preferenceStore.setDefault( "advancedMode", false );
        preferenceStore.setDefault( "searchFilterPornography", false );
        preferenceStore.setDefault( "searchFilterProfanity", false );
        preferenceStore.setDefault( "maintainSortOrder", false );
        preferenceStore.setDefault( "updateDelay", 0 );
        preferenceStore.setDefault( "useGradient", true );
        preferenceStore.setDefault( "displayNodes", false );
        preferenceStore.setDefault( "displayChunkGraphs", false );
        preferenceStore.setDefault( "displayGridLines", true );
        preferenceStore.setDefault( "tableCellEditors", false );
        preferenceStore.setDefault( "displayTableColors" , true );
        preferenceStore.setDefault( "displayFontAA" , false );
        preferenceStore.setDefault( "coreExecutable", "" );
        preferenceStore.setDefault( "useCombo", false );
        preferenceStore.setDefault( "minimizeOnClose", false );
        preferenceStore.setDefault( "dragAndDrop", true );
        preferenceStore.setDefault( "pollUpStats", true );
		preferenceStore.setDefault( "running", false );
		preferenceStore.setDefault( "allowMultipleInstances", false );
		preferenceStore.setDefault( "downloadsFilterQueued", false );
		preferenceStore.setDefault( "downloadsFilterPaused", false );
		
		preferenceStore.setDefault( "graphSashOrientation", SWT.HORIZONTAL );
		preferenceStore.setDefault( "graphSashMaximized", -1 );
		
		preferenceStore.setDefault( "clientSashOrientation", SWT.HORIZONTAL );
		preferenceStore.setDefault( "clientSashMaximized", 0 );
		
		preferenceStore.setDefault( "transferSashOrientation", SWT.VERTICAL );
		preferenceStore.setDefault( "transferSashMaximized", -1 );
		
		preferenceStore.setDefault( "uploadsSashOrientation", SWT.HORIZONTAL );
		preferenceStore.setDefault( "uploadsSashMaximized", -1 );
		
		PreferenceConverter.setDefault( preferenceStore, "viewerFontData", JFaceResources.getDefaultFont().getFontData() );     
		
		preferenceStore.setDefault( G2GuiResources.getString( "ENS_NOT_CONNECTED" ), false );		
		preferenceStore.setDefault( G2GuiResources.getString( "ENS_CONNECTING" ), false );		
		preferenceStore.setDefault( G2GuiResources.getString( "ENS_CONNECTED_INITIATING" ), false );		
		preferenceStore.setDefault( G2GuiResources.getString( "ENS_CONNECTED_DOWNLOADING" ), false );		
		preferenceStore.setDefault( G2GuiResources.getString( "ENS_CONNECTED" ), false );		
		preferenceStore.setDefault( G2GuiResources.getString( "ENS_CONNECTED_AND_QUEUED" ), false );		
		preferenceStore.setDefault( G2GuiResources.getString( "ENS_NEW_HOST" ), false );		
		preferenceStore.setDefault( G2GuiResources.getString( "ENS_REMOVE_HOST" ), false );		
		preferenceStore.setDefault( G2GuiResources.getString( "ENS_BLACK_LISTED" ), false );		
		preferenceStore.setDefault( G2GuiResources.getString( "ENS_NOT_CONNECTED_WAS_QUEUED" ), false );		
		
		// set the default filter for server tab to connected, connecting and connection initiated
		preferenceStore.setDefault( Enum.getPrefName( EnumState.CONNECTED, ServerPaneListener.class ), true );
		preferenceStore.setDefault( Enum.getPrefName( EnumState.CONNECTING, ServerPaneListener.class ), true );
		preferenceStore.setDefault( Enum.getPrefName( EnumState.CONNECTED_INITIATING, ServerPaneListener.class ), true );

		preferenceStore.setDefault( "searchSashWeights", "1,5" );

		preferenceStore.setDefault( "UploadersPaneListenerBestFit", "-1" );
		preferenceStore.setDefault( "ClientPaneListenerBestFit", "-1" );
		preferenceStore.setDefault( "UploadPaneListenerBestFit", "-1" );
		preferenceStore.setDefault( "DownloadPaneListenerBestFit", "-1" );
		preferenceStore.setDefault( "ServerPaneListenerBestFit", "-1" );
		
		// we want this in the g2gui.pref file without a frontend in the gui
		preferenceStore.setDefault( "WEBSERVICE.URL.BITZI", "http://bitzi.com/lookup/" );
		preferenceStore.setDefault( "WEBSERVICE.URL.FILEDONKEY", "http://www.filedonkey.com/file.html?md4=" );
		preferenceStore.setDefault( "WEBSERVICE.URL.DONKEYFAKES", "http://donkeyfakes.gambri.net/index.php?action=search&ed2k=" );
		
        return preferenceStore;
    }

	public static Font loadFont( String preferenceString ) {
        if ( preferenceStore.contains( preferenceString ) ) {
            Font newFont =
                new Font( null, PreferenceConverter.getFontDataArray( preferenceStore, preferenceString ) );
            if ( fontMap.containsKey( preferenceString ) ) {
                if ( newFont.getFontData()[ 0 ].equals( ( ( Font ) fontMap.get( preferenceString ) )
                                                              .getFontData()[ 0 ] ) )
                    newFont.dispose();
                else {
                    fontArray.add( newFont );
                    fontMap.put( preferenceString, newFont );
                }
            }
            else {
                fontArray.add( newFont );
                fontMap.put( preferenceString, newFont );
            }
            return (Font) fontMap.get( preferenceString );
        }
        return null;
    }
 
	public static Color loadColour( String preferenceString ) {
        if ( preferenceStore.contains( preferenceString ) ) {
            Color newColor =
                new Color( null, PreferenceConverter.getColor( preferenceStore, preferenceString ) );
            if ( colorMap.containsKey( preferenceString ) ) {
                if ( newColor.getRGB().equals( ( ( Color ) colorMap.get( preferenceString ) ).getRGB() ) )
                    newColor.dispose();
                else {
                    colorArray.add( newColor );
                    colorMap.put( preferenceString, newColor );
                }
            }
            else {
                colorArray.add( newColor );
                colorMap.put( preferenceString, newColor );
            }
            return ( Color ) colorMap.get( preferenceString );
        }
        return null;
    }

	public static Rectangle loadRectangle( String preferenceString ) {
        if ( preferenceStore.contains( preferenceString ) )
            return PreferenceConverter.getRectangle( preferenceStore, preferenceString );
        return null;
    }

	public static boolean loadBoolean( String preferenceString ) {
        if ( preferenceStore.contains( preferenceString ) )
            return preferenceStore.getBoolean( preferenceString );
        // TODO return false instead of true by default does hurt somebody/somewhere?
        return false;
    }
   
	public static int loadInteger( String preferenceString ) {
        if ( preferenceStore.contains( preferenceString ) )
            return preferenceStore.getInt( preferenceString );
        return 0;
    }
  
	public static String loadString( String preferenceString ) {
        if ( preferenceStore.contains( preferenceString ) )
            return preferenceStore.getString( preferenceString );
        return "";
    }

	public static PreferenceStore getPreferenceStore() {
        return preferenceStore;
    }

	/**
	 * This method writes the PreferenceStore to disk
	 *
	 */
    public static void saveStore() {
        try {
            preferenceStore.save();
        }
        catch ( IOException e2 ) {
        }
    }
   
	public static boolean contains( String preferenceString ) {
        return preferenceStore.contains( preferenceString );
    }

	public static void cleanUp() {
        Iterator fonts = fontArray.iterator();
        while ( fonts.hasNext() )
            ( ( Font ) fonts.next() ).dispose();
        Iterator colors = colorArray.iterator();
        while ( colors.hasNext() )
            ( ( Color ) colors.next() ).dispose();
        
        fontMap.clear();
        colorMap.clear();
        fontArray.clear();
        colorArray.clear(); 
        critPrefsMap.clear();
		
        preferenceStore = null;
	}

	public static boolean getBoolean(String string) {
		return getPreferenceStore().getBoolean(string);
	}

	public static void setValue(String string, boolean b) {
		getPreferenceStore().setValue(string, b);
	}

	public static int getInt(String string) {
		return getPreferenceStore().getInt(string);
	}

	public static String getString(String string) {
		return getPreferenceStore().getString(string);
	}

	public static int getDefaultInt(String orientationPrefString) {
		return getPreferenceStore().getDefaultInt(orientationPrefString);
	}

	public static void setValue(String orientationPrefString, int i) {
		getPreferenceStore().setValue(orientationPrefString, i);
	}

	public static void setDefault(String string, String string2) {
		getPreferenceStore().setDefault(string, string2);
	}

	public static void setValue(String string, String string2) {
		getPreferenceStore().setValue(string, string2);
	}

	public static boolean isDefault(String string) {
		return getPreferenceStore().isDefault(string);
	}

	public static void setDefault(String string, boolean b) {
		getPreferenceStore().setDefault(string,b);
	}

	public static void setDefault(String string, int i) {
		getPreferenceStore().setDefault(string, i);
	}

	public static void setValue(String string, int[] is) {
		String result = "";
		for ( int i = 0; i < is.length; i++ ) {
			result += is[ i ] + ",";
		}
		getPreferenceStore().setValue( string, result.substring( 0, result.length() - 1 ) );
	}

	public static int[] getIntArray(String string) {
		String temp = getPreferenceStore().getString( string ); 
		String[] strings = RegExp.split( temp, ',' );
		int[] ints = new int[ strings.length ];
		for ( int i = 0; i < strings.length; i++ ) {
			ints[ i ] = new Integer( strings[ i ] ).intValue();
		}
		return ints;
	}

	/**
	 * This method stores the critical preferences (which need a restart on change)
	 * into our temporary map. This method is intended to be called after a request for
	 * restart after changing crittical preferences has been denied by the user.
	 *
	 */
	public static void saveCritPrefs() {
		critPrefsMap.clear();
		for (int i = 0; i < critPrefsList.length; i++)
			critPrefsMap.put(critPrefsList[i], preferenceStore.getString(critPrefsList[i]));	
	}
	
	/**
	 * This method checks if a "critical option" which needs a restart has been changed since the
	 * initialiuation of the preferenceStore
	 * @return true if a restart is necessary for changes to take effact, false if not
	 */
	public static boolean needsRelaunch() {
		for (int i = 0; i < critPrefsList.length; i++) {
			if (critPrefsMap.get(critPrefsList[i]) != preferenceStore.getString(critPrefsList[i]) ) {
				System.out.println("An critPref changed: " + critPrefsMap.get(critPrefsList[i]) + 
						" to " + preferenceStore.getString(critPrefsList[i]));
				return true;
			}
		}
		return false;
	}

	/**
	 * Are we in the progress of relaunching g2gui?
	 * @return true if yes, false if not
	 */
	public static boolean isRelaunching() {
		return relaunching;
	}

	/**
	 * Are we in the progress of shutting down g2gui?
	 * @return true if yes, false if not
	 */
	public static boolean isQuitting() {
		return quitting;
	}

	
	/**
	 * Set the relaunching state
	 * @param b true if we're relaunching, false if not
	 */
	public static void setRelaunching(boolean b) {
		relaunching = b;
	}
	
	/**
	 * Set the quitting state
	 * @param b true if we're quitting, false if not
	 */
	public static void setQuitting(boolean b) {
		quitting = b;
	}


}

/*
$Log: PreferenceLoader.java,v $
Revision 1.70  2004/11/20 23:41:43  lemmy
fix: [Bug #2803] Make WebServices Configurable

Revision 1.69  2004/03/26 22:34:39  dek
.torrents are now correctly stored in win-registry

Revision 1.68  2004/03/26 18:38:37  dek
now the "surpress-received-clients" option is working

Revision 1.67  2004/03/26 18:11:04  dek
some more profiling and mem-saving option (hopefully)  introduced

Revision 1.66  2004/03/26 01:13:51  psy
added quitting-status methods

Revision 1.65  2004/03/24 19:00:28  dek
homedir \ under windows is invalid and treated like it don't exist

Revision 1.64  2004/03/23 20:47:38  psy
only use homedir if it exists

Revision 1.63  2004/03/23 20:27:31  psy
only create dirs for g2gui.pref if not under win32

Revision 1.62  2004/03/23 20:21:37  psy
fixed little mistake

Revision 1.61  2004/03/23 19:58:41  psy
under windows, use cwd for g2gui.pref storage

Revision 1.60  2004/03/01 21:12:21  psy
removed download-table font config (use global one instead)
started re-arranging the preferences (to be continued...)

Revision 1.59  2004/02/29 17:08:50  psy
little javadoc

Revision 1.58  2004/01/29 15:06:36  lemmy
use supertype (programm against the interface, not the concrete class)

Revision 1.57  2004/01/28 22:15:34  psy
* Properly handle disconnections from the core
* Fast inline-reconnect
* Ask for automatic relaunch if options have been changed which require it
* Improved the local core-controller

Revision 1.56  2004/01/08 21:53:58  psy
default for displayFontAA=false

Revision 1.55  2003/12/23 03:40:33  psy
minor verbosity increase

Revision 1.54  2003/12/07 19:36:54  lemmy
[Bug #1162] Search tab's pane position always reset to default

Revision 1.53  2003/12/04 08:47:27  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.52  2003/12/03 22:19:11  lemmy
store g2gui.pref in ~/.g2gui/g2gui.pref instead of the program directory

Revision 1.51  2003/11/29 13:03:54  lemmy
ToolTip complete reworked (to be continued)

Revision 1.50  2003/11/27 15:33:24  zet
defWebBrowser

Revision 1.49  2003/11/26 07:42:37  zet
uploadsSash

Revision 1.48  2003/11/24 21:13:39  zet
default viewer font

Revision 1.47  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

Revision 1.46  2003/11/21 22:02:42  vnc
color-adjustments to fit the general theme (green==downloads)

Revision 1.45  2003/11/20 14:02:17  lemmy
G2Gui cleanup

Revision 1.44  2003/11/12 16:16:33  zet
set intial window size

Revision 1.43  2003/11/11 17:14:35  lemmy
default filters for servertab

Revision 1.42  2003/11/10 09:58:38  lemmy
return false by default instead of true in loadBoolean(someString)

Revision 1.41  2003/11/09 23:09:57  lemmy
remove "Show connected Servers only"
added filter saving in searchtab

Revision 1.40  2003/11/07 00:31:39  zet
option to disable splash screen  #1064

Revision 1.39  2003/10/17 15:35:48  zet
graph prefs

Revision 1.38  2003/10/16 22:02:45  zet
move some options

Revision 1.37  2003/10/16 18:20:18  zet
red

Revision 1.36  2003/10/16 16:10:14  zet
background/font

Revision 1.35  2003/10/15 22:06:13  zet
Split Console/Downloads pref pages.

Revision 1.34  2003/10/12 15:57:27  zet
sashes

Revision 1.33  2003/10/08 01:12:15  zet
useGradient preference

Revision 1.32  2003/09/29 17:44:40  lemmy
switch for search tooltips

Revision 1.31  2003/09/28 13:10:31  dek
Added Option, wether multiple Instances of G2Gui are allowed or not[bug #867]

Revision 1.30  2003/09/27 00:36:54  zet
auto poll for upstats preference

Revision 1.29  2003/09/26 20:17:48  zet
*** empty log message ***

Revision 1.28  2003/09/26 04:19:06  zet
drag&drop

Revision 1.27  2003/09/25 03:41:03  zet
-c <pref file>

Revision 1.26  2003/09/21 23:40:01  zet
displayTableColors preference

Revision 1.25  2003/09/20 01:29:21  zet
*** empty log message ***

Revision 1.24  2003/09/19 14:25:55  zet
min to systray option

Revision 1.23  2003/09/18 10:23:48  lemmy
checkstyle

Revision 1.22  2003/09/15 22:06:19  zet
split preferences

Revision 1.21  2003/09/14 09:40:31  lemmy
save column width

Revision 1.20  2003/09/14 09:01:15  lemmy
show nodes on request

Revision 1.19  2003/09/07 16:12:33  zet
combo

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

Revision 1.10  2003/08/22 21:10:57  lemmy
replace $user$ with $Author: lemmy $

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

Revision 1.3  2003/08/11 11:27:46  lemmy
display only connected servers added

Revision 1.2  2003/08/08 21:11:15  zet
set defaults in central location



*/
