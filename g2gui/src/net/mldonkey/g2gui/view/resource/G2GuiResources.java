/*
 * Copyright 2003
 * G2Gui Team
 *
 *
 * This file is part of G2Gui.
 *
 * G2Gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2Gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2Gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package net.mldonkey.g2gui.view.resource;

import net.mldonkey.g2gui.model.enum.EnumNetwork;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.MainTab;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * G2GuiResources
 *
 *
 * @version $Id: G2GuiResources.java,v 1.48 2003/11/04 17:03:32 zet Exp $
 */
public class G2GuiResources {
    private static ImageRegistry imageRegistry = null;
    private static final ResourceBundle bundle = ResourceBundle.getBundle( "g2gui" );
    private static final String miscDirectory = "misc/";
    private static final String imagesDirectory = "images/";
    private static final String networksDirectory = "networks/";

    private G2GuiResources() {
    }

    /**
    * Initialize G2GuiResources
    */
    public static void initialize() {
        createImageRegistry();
    }

    /**
     * Get resource image
     * @param key
     * @return Image
     */
    public static Image getImage( String key ) {
        return getImageRegistry().get( key );
    }

    /**
     * Get resource imageDescriptor
     * @param key
     * @return
     */
    public static ImageDescriptor getImageDescriptor( String key ) {
        return getImageRegistry().getDescriptor( key );
    }

    /**
     * Get ImageRegistry
     * @return ImageRegistry
     */
    public static ImageRegistry getImageRegistry() {
        if ( imageRegistry == null ) {
            imageRegistry = new ImageRegistry();
        }

        return imageRegistry;
    }

    /**
     * Get resource string
     * @param key
     * @return String
     */
    public static String getString( String key ) {
        try {
            return bundle.getString( key );
        } catch ( MissingResourceException e ) {
            return key;
        }
    }

    /**
     * Get network image
     * @param networkType
     * @return Image
     */
    public static Image getNetworkImage( EnumNetwork networkType ) {
        if ( networkType == EnumNetwork.DONKEY ) {
            return getImage( "DKConnected" );
        } else if ( networkType == EnumNetwork.FT ) {
            return getImage( "FTConnected" );
        } else if ( networkType == EnumNetwork.GNUT ) {
            return getImage( "G1Connected" );
        } else if ( networkType == EnumNetwork.GNUT2 ) {
            return getImage( "G2Connected" );
        } else if ( networkType == EnumNetwork.SOULSEEK ) {
            return getImage( "SSConnected" );
        } else if ( networkType == EnumNetwork.DC ) {
            return getImage( "DCConnected" );
        } else if ( networkType == EnumNetwork.BT ) {
            return getImage( "BTConnected" );
        } else if ( networkType == EnumNetwork.MULTINET ) {
            return getImage( "MULTIConnected" );
        } else {
            return getImage( "UnknownConnected" );
        }
    }

    /**
     * Get Client Image
     * @param clientState
     * @return Image
     */
    public static Image getClientImage( EnumState clientState ) {
        if ( clientState == EnumState.CONNECTED_DOWNLOADING ) {
            return G2GuiResources.getImage( "epTransferring" );
        } else if ( ( clientState == EnumState.CONNECTING ) 
        		|| ( clientState == EnumState.CONNECTED_INITIATING ) ) {
            return G2GuiResources.getImage( "epConnecting" );
        } else if ( ( clientState == EnumState.CONNECTED_AND_QUEUED ) 
        		|| ( clientState == EnumState.NOT_CONNECTED_WAS_QUEUED ) ) {
            return G2GuiResources.getImage( "epAsking" );
        } else if ( clientState == EnumState.BLACK_LISTED ) {
            return G2GuiResources.getImage( "epNoNeeded" );
        } else {
            return G2GuiResources.getImage( "epUnknown" );
        }
    }

    /**
     * Get rating string
     * @param availability int
     * @return string
     */
    public static String getRatingString( int availability ) {
        String result = "";

        if ( availability > 100 ) {
            result += G2GuiResources.getString( "RTLP_EXCELLENT" );
        } else if ( availability > 50 ) {
            result += G2GuiResources.getString( "RTLP_VERYHIGH" );
        } else if ( availability > 10 ) {
            result += G2GuiResources.getString( "RTLP_HIGH" );
        } else if ( availability > 5 ) {
            result += G2GuiResources.getString( "RTLP_NORMAL" );
        } else {
            result += G2GuiResources.getString( "RTLP_LOW" );
        }

        return ( result + " (" + availability + ")" );
    }

    /**
     * Get rating image
     * @param availability int
     * @return Image
     *
     */
    public static Image getRatingImage( int availability ) {
        if ( availability < 0 ) {
            return G2GuiResources.getImage( "epRatingFake" );
        } else if ( availability > 100 ) {
            return G2GuiResources.getImage( "epRatingExcellent" );
        } else if ( availability > 50 ) {
            return G2GuiResources.getImage( "epRatingExcellent" );
        } else if ( availability > 10 ) {
            return G2GuiResources.getImage( "epRatingGood" );
        } else if ( availability > 5 ) {
            return G2GuiResources.getImage( "epRatingFair" );
        } else {
            return G2GuiResources.getImage( "epRatingPoor" );
        }
    }

    /**
     * Create the image registry
     */
    private static void createImageRegistry() {
        Color white = Display.getCurrent().getSystemColor( SWT.COLOR_WHITE );
        Color titlebar = Display.getCurrent().getSystemColor( SWT.COLOR_TITLE_BACKGROUND );

        ImageRegistry reg = G2GuiResources.getImageRegistry();

        reg.put( "splashScreen", createRawImage( "splash.png" ) );
        reg.put( "ProgramIcon", createRawImage( "mld_logo_48x48.gif" ) );
        reg.put( "G2GuiLogo", createTrans( "mld_logo_48x48.png" ) );
        reg.put( "G2GuiLogoSmall", createTrans( "mld_logo_12x12.png" ) );
        reg.put( "TrayIcon", createRawImage( "tray.ico" ) );

        String[] buttonNames = { "Preferences", "Statistics", "Console", "Transfers", "Search", "Servers", "Messages", "News" };
        String[] buttonFiles = { "preferences", "statistics", "console", "transfer3a", "search", "server", "messages", "news" };

        for ( int i = 0; i < buttonNames.length; i++ ) {
            reg.put( buttonNames[ i ] + "Button", createTrans( buttonFiles[ i ] + ".png" ) );
            reg.put( buttonNames[ i ] + "ButtonActive", createActive( buttonFiles[ i ] + ".png" ) );
            reg.put( buttonNames[ i ] + "ButtonSmall", createTrans( buttonFiles[ i ] + "-16.png" ) );
            reg.put( buttonNames[ i ] + "ButtonSmallActive", createActive( buttonFiles[ i ] + "-16.png" ) );
            reg.put( buttonNames[ i ] + "ButtonSmallTitlebar", createTrans( buttonFiles[ i ] + "-16.png", titlebar ) );
        }

        reg.put( "MessagesButtonSmallTrans", createRawImage( "messages-16.gif" ) );
        reg.put( "MessagesButtonSmallTransBW", createRawImage( "messages-16-bw.gif" ) );

        reg.put( "DownArrow", createTrans( "down.png" ) );
        reg.put( "UpArrow", createTrans( "up.png" ) );
       
        reg.put( "RedCrossSmall", createTrans( "red_cross-12.png" ) );
        reg.put( "downloaded", createTrans( "downloaded_arrow.png" ) );

        createNetworksIcons( reg );
        createMiscIcons( reg );
    }

    /**
     * @param reg
     */
    public static void createMiscIcons( ImageRegistry reg ) {
        reg.put( "epUnknown", createRawMImage( "ep_unknown.gif" ) );
        reg.put( "epTransferring", createRawMImage( "ep_transferring.gif" ) );
        reg.put( "epNoNeeded", createRawMImage( "ep_noneeded.gif" ) );
        reg.put( "epConnecting", createRawMImage( "ep_connecting.gif" ) );
        reg.put( "epAsking", createRawMImage( "ep_asking.gif" ) );
        reg.put( "epRatingPoor", createRawMImage( "ep_rating_poor.gif" ) );
        reg.put( "epRatingFair", createRawMImage( "ep_rating_fair.gif" ) );
        reg.put( "epRatingGood", createRawMImage( "ep_rating_good.gif" ) );
        reg.put( "epRatingExcellent", createRawMImage( "ep_rating_excellent.gif" ) );
        reg.put( "epRatingFake", createRawMImage("ep_rating_fake.gif" ) );
        
		reg.put( "SearchSmall", createRawMImage( "search_small.gif" ) );
		reg.put( "SearchComplete", createRawMImage( "search_complete.gif" ) );
        
        reg.put( "UpArrowGreen", createRawMImage( "up_arrow_green.gif" ) );
        reg.put( "DownArrowGreen", createRawMImage( "down_arrow_green.gif") );
        reg.put( "X", createRawMImage( "x.gif" ) );

        reg.put( "Jigle", createRawMImage( "jigle.gif" ) );
        reg.put( "Bitzi", createRawMImage( "bitzi.gif" ) );
        reg.put( "ShareReactor", createRawMImage( "sharereactor.gif" ) );
        reg.put( "info", createRawMImage( "info.gif" ) );
        reg.put( "cancel", createRawMImage( "cancel.gif" ) );
        reg.put( "resume", createRawMImage( "resume.gif" ) );
        reg.put( "pause", createRawMImage( "pause.gif" ) );
        reg.put( "preview", createRawMImage( "preview.gif" ) );
        reg.put( "verify", createRawMImage( "verify.gif" ) );
        reg.put( "commit", createRawMImage( "commit.gif" ) );
        reg.put( "commit_question", createRawMImage( "commit_question.gif" ) );
        reg.put( "edonkey", createRawMImage( "edonkey.gif" ) );
        reg.put( "globe", createRawMImage( "globe.gif" ) );
        reg.put( "preferences", createRawMImage( "preferences.gif" ) );
        reg.put( "rotate", createRawMImage( "rotate.gif" ) );
        reg.put( "collapseAll", createRawMImage( "collapseall.gif" ) );
        reg.put( "expandAll", createRawMImage( "expandall.gif") );
        reg.put( "plus" , createRawMImage( "plus.gif" ) );
        reg.put( "minus" , createRawMImage( "minus.gif" ) );
        reg.put( "maximize", createRawMImage( "maximize.gif" ) );
        reg.put( "restore", createRawMImage( "restore.gif" ) );
        reg.put( "table", createRawMImage( "table.gif" ) );
        reg.put( "split-table", createRawMImage( "split-table.gif" ) );
        reg.put( "copy", createRawMImage( "copy.gif" ) );
        reg.put( "clear", createRawMImage( "clear.gif" ) );
        reg.put( "graph", createRawMImage( "graph.gif" ) );
        reg.put( "dropdown", createRawMImage( "dropdown.gif" ) );
    }

    /**
    * @param reg
    */
    public static void createNetworksIcons( ImageRegistry reg ) {
        Color white = Display.getCurrent().getSystemColor( SWT.COLOR_WHITE );

        String[] shortNames = { "DC", "DK", "G1", "G2", "FT", "SS", "ONP", "Unknown" };
        String[] fileNames = { "directconnect", "edonkey2000", "gnutella", "gnutella2", "kazaa", "soulseek", "opennap", "unknown" };

        for ( int i = 0; i < shortNames.length; i++ ) {
            reg.put( shortNames[ i ] + "Connected", createRawNImage( fileNames[ i ] + "_connected.gif" ) );
            reg.put( shortNames[ i ] + "Disconnected", createRawNImage( fileNames[ i ] + "_disconnected.gif" ) );
            reg.put( shortNames[ i ] + "Disabled", createRawNImage( fileNames[ i ] + "_disabled.gif" ) );
            reg.put( shortNames[ i ] + "BadConnected", createRawNImage( fileNames[ i ] + "_badconnected.gif" ) );
        }

        /* some icons for networks without all states */
        reg.put( "BTConnected", createRawNImage( "bt_connected.gif" ) );
        reg.put( "BTDisabled", createRawNImage( "bt_disabled.gif" ) );
        reg.put( "MULTIConnected", createRawNImage( "multinet_connected.gif" ) );
        reg.put( "MULTIDisabled", createRawNImage( "multinet_disabled.gif" ) );
    }

    /**
     * Creates a Transparent imageobject with a given .png|.gif Image-Object
     * be aware, the the scr-image is disposed, so dont' use it any further
     *
     * @param src the non-transparent image we want to process
     * @param color the background-color
     * @return the transparent image
     */
    private static Image createTransparentImage( Image src, Color color ) {
        int width = src.getBounds().width;
        int height = src.getBounds().height;
        Image result = new Image( null, width, height );
        GC gc = new GC( result );
        gc.setBackground( color );
        gc.fillRectangle( 0, 0, width, height );
        gc.drawImage( src, 0, 0 );
        src.dispose();
        gc.dispose();

        return result;
    }

    /**
     * @param src
     * @param color
     * @return Image
     */
    private static Image createActiveImage( Image src, Color color ) {
        int width = src.getBounds().width;
        int height = src.getBounds().height;
        Image result = new Image( null, width, height );
        GC gc = new GC( result );
        gc.drawImage( src, 0, 0 );

        // let's try a border for now...	
        gc.setForeground( Display.getCurrent().getSystemColor( SWT.COLOR_TITLE_BACKGROUND ) );
        gc.drawLine( 0, 0, width - 1, 0 );
        gc.drawLine( 0, height - 1, width - 1, height - 1 );
        gc.drawLine( 0, 0, 0, height - 1 );
        gc.drawLine( width - 1, 0, width - 1, height - 1 );
        src.dispose();
        gc.dispose();

        return result;
    }

    /**
     * @param src
     * @param control
     * @return Image
     */
    private static Image createTransparentImage( Image src, Control control ) {
        return createTransparentImage( src, control.getBackground() );
    }

    /**
     * @param filename
     * @return Image
     */
    private static Image createTrans( String filename ) {
        return createTrans( filename, Display.getCurrent().getSystemColor( SWT.COLOR_WIDGET_BACKGROUND ) );
    }

    /**
     * @param filename
     * @return ImageDescriptor
     */
    private static ImageDescriptor createRawImage( String filename ) {
        return ImageDescriptor.createFromFile( MainTab.class, imagesDirectory + filename );
    }

    /**
     * @param filename
     * @return ImageDescriptor
     */
    private static ImageDescriptor createRawMImage( String filename ) {
        return createRawImage( miscDirectory + filename );
    }

    /**
     * @param filename
     * @param color
     * @return Image
     */
    private static Image createTrans( String filename, Color color ) {
        return createTransparentImage( ImageDescriptor.createFromFile( MainTab.class, imagesDirectory + filename ).createImage(), color );
    }

    /**
    * @param filename
    * @return Image
    */
    private static ImageDescriptor createRawNImage( String filename ) {
        return createRawImage( networksDirectory + filename );
    }

    /**
    * @param filename
    * @param color
    * @return Image
    */
    private static Image createNTrans( String filename, Color color ) {
        return createTrans( networksDirectory + filename, color );
    }

    /**
     * @param filename
     * @return Image
     */
    private static Image createActive( String filename ) {
        return createActiveImage( createTrans( filename ), Display.getCurrent().getSystemColor( SWT.COLOR_WIDGET_BACKGROUND ) );
    }
}


/*
$Log: G2GuiResources.java,v $
Revision 1.48  2003/11/04 17:03:32  zet
update for trans

Revision 1.47  2003/11/04 16:54:03  vnc
changed network mini-icons from png to gif

Revision 1.46  2003/10/31 16:35:08  zet
typo

Revision 1.45  2003/10/31 13:17:10  lemmster
dropdown.gif added

Revision 1.44  2003/10/28 11:07:32  lemmster
move NetworkInfo.Enum -> enum.EnumNetwork
add MaskMatcher for "Enum[]"

Revision 1.43  2003/10/22 23:43:13  zet
fake graphic

Revision 1.42  2003/10/22 01:37:35  zet
add column selector to server/search (might not be finished yet..)

Revision 1.41  2003/10/19 03:56:04  zet
rename tray icon

Revision 1.40  2003/10/17 03:35:52  zet
icons

Revision 1.39  2003/10/16 21:22:23  zet
icon

Revision 1.38  2003/10/15 19:44:18  zet
icons

Revision 1.37  2003/10/15 19:40:15  zet
icons

Revision 1.36  2003/10/15 18:24:51  zet
icons

Revision 1.35  2003/10/15 15:33:09  zet
split images directory

Revision 1.34  2003/10/15 04:16:37  zet
add images

Revision 1.33  2003/09/27 10:24:54  lemmster
news items added

Revision 1.32  2003/09/22 20:25:04  lemmster
show # sources in search result avail table

Revision 1.31  2003/09/22 20:20:09  lemmster
show # sources in search result avail table

Revision 1.30  2003/09/18 23:09:48  zet
*** empty log message ***

Revision 1.29  2003/09/18 10:26:28  lemmster
checkstyle

Revision 1.28  2003/09/17 14:40:31  zet
redcross

Revision 1.27  2003/09/16 20:21:59  zet
opennap

Revision 1.26  2003/09/16 09:24:11  lemmster
adjust source rating

Revision 1.25  2003/09/13 22:23:20  zet
*** empty log message ***

Revision 1.24  2003/09/08 18:26:48  zet
init in g2gui

Revision 1.23  2003/09/08 17:26:23  zet
ico

Revision 1.22  2003/09/03 14:49:07  zet
optionally spawn core from gui

Revision 1.21  2003/09/02 09:25:55  lemmster
jalopy run

Revision 1.20  2003/09/01 00:51:49  zet
*** empty log message ***

Revision 1.19  2003/09/01 00:44:21  zet
use hotimage

Revision 1.18  2003/08/31 20:32:50  zet
active button states

Revision 1.17  2003/08/31 15:37:30  zet
friend icons

Revision 1.14  2003/08/30 18:23:51  dek
added g2guiLogo (png)

Revision 1.13  2003/08/30 00:44:01  zet
move tabletree menu

Revision 1.12  2003/08/29 23:34:14  zet
close all tabs

Revision 1.11  2003/08/29 17:33:20  zet
remove headerbar

Revision 1.10  2003/08/28 17:07:03  zet
gif not png

Revision 1.9  2003/08/25 21:18:43  zet
localise/update friendstab

Revision 1.8  2003/08/25 12:24:09  zet
Toggleable link entry.  It should parse links from pasted HTML as well.

Revision 1.7  2003/08/23 15:49:28  lemmster
fix for prefs and refactoring

Revision 1.6  2003/08/23 15:21:37  zet
remove @author

Revision 1.5  2003/08/22 21:10:57  lemmster
replace $user$ with $Author: zet $

Revision 1.4  2003/08/21 11:19:15  lemmster
added bt and multinet image

Revision 1.3  2003/08/20 14:58:43  zet
sources clientinfo viewer

Revision 1.2  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.1  2003/08/17 23:13:41  zet
centralize resources, move images



*/
