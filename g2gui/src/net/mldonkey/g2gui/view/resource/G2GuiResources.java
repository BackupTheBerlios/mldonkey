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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.mldonkey.g2gui.model.NetworkInfo.Enum;
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

/**
 * G2GuiResources
 *
 *
 * @version $Id: G2GuiResources.java,v 1.30 2003/09/18 23:09:48 zet Exp $
 */
public class G2GuiResources {
    private static ImageRegistry imageRegistry = null;
    private static final ResourceBundle bundle = ResourceBundle.getBundle( "g2gui" );
	private static final String imagesDirectory = "images/";

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
     * Get ImageRegistry
     * @return ImageRegistry
     */
    public static ImageRegistry getImageRegistry() {
        if ( imageRegistry == null )
            imageRegistry = new ImageRegistry();
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
        }
        catch ( MissingResourceException e ) {
            return key;
        }
    }

    /**
     * Get network image
     * @param networkType 
     * @return Image
     */
    public static Image getNetworkImage( Enum networkType ) {
        if ( networkType == Enum.DONKEY )
            return getImage( "DKConnectedWhite" );
        if ( networkType == Enum.FT )
            return getImage( "FTConnectedWhite" );
        if ( networkType == Enum.GNUT )
            return getImage( "G1ConnectedWhite" );
        if ( networkType == Enum.GNUT2 )
            return getImage( "G2ConnectedWhite" );
        if ( networkType == Enum.SOULSEEK )
            return getImage( "SSConnectedWhite" );
        if ( networkType == Enum.DC )
            return getImage( "DCConnectedWhite" );
        if ( networkType == Enum.BT )
            return getImage( "BTConnectedWhite" );
        if ( networkType == Enum.MULTINET )
            return getImage( "MULTIConnectedWhite" );
        return getImage( "UnknownConnectedWhite" );
    }

    /**
     * Get Client Image
     * @param clientState 
     * @return Image
     */
    public static Image getClientImage( EnumState clientState ) {
        if ( clientState == EnumState.CONNECTED_DOWNLOADING )
            return G2GuiResources.getImage( "epTransferring" );
        if ( ( clientState == EnumState.CONNECTING ) || ( clientState == EnumState.CONNECTED_INITIATING ) )
            return G2GuiResources.getImage( "epConnecting" );
        if ( ( clientState == EnumState.CONNECTED_AND_QUEUED )
                 || ( clientState == EnumState.NOT_CONNECTED_WAS_QUEUED ) )
            return G2GuiResources.getImage( "epAsking" );
        if ( clientState == EnumState.BLACK_LISTED )
            return G2GuiResources.getImage( "epNoNeeded" );
        return G2GuiResources.getImage( "epUnknown" );
    }

    /**
     * Get rating string
     * @param availability int
     * @return string 
     */
    public static String getRatingString( int availability ) {
        if ( availability > 100 )
            return "" + G2GuiResources.getString( "RTLP_EXCELLENT" );
        else if ( availability > 50 )
            return "" + G2GuiResources.getString( "RTLP_VERYHIGH" );
        else if ( availability > 10 )
            return "" + G2GuiResources.getString( "RTLP_HIGH" );
        else if ( availability > 5 )
            return "" + G2GuiResources.getString( "RTLP_NORMAL" );
        else
            return "" + G2GuiResources.getString( "RTLP_LOW" );
    }

    /**
     * Get rating image
     * @param availability int
     * @return Image
     * 
     */
	public static Image getRatingImage( int availability ) {
		if ( availability > 100 )
			return G2GuiResources.getImage( "epRatingExcellent" );
		else if ( availability > 50 )
			return G2GuiResources.getImage( "epRatingExcellent" );
		else if ( availability > 10 )
			return G2GuiResources.getImage( "epRatingGood" );
		else if ( availability > 5 )
			return G2GuiResources.getImage( "epRatingFair" );
		else
			return G2GuiResources.getImage( "epRatingPoor" );
	}

    /**
     * Create the image registry
     */
    private static void createImageRegistry() {
        Color white = Display.getCurrent().getSystemColor( SWT.COLOR_WHITE );
        Color titlebar = Display.getCurrent().getSystemColor( SWT.COLOR_TITLE_BACKGROUND );

		ImageRegistry reg = G2GuiResources.getImageRegistry();

		reg.put( "splashScreen" , createRawImage( "splash.png" ) );
        reg.put( "ProgramIcon", createRawImage( "mld_logo_48x48.gif" ) );
        reg.put( "G2GuiLogo", createTrans( "mld_logo_48x48.png" ) );
		reg.put( "G2GuiLogoSmall", createTrans( "mld_logo_12x12.png" ) );
		reg.put( "G2GuiICO" , createRawImage ( "g2gui.ico" ) );
        
        String[] buttonNames =
        { "Preferences", "Statistics", "Console", "Transfers", "Search", "Servers", "Messages" };
        String[] buttonFiles =
        { "preferences", "statistics", "console", "transfer3a", "search", "server", "messages" };

        // still thinking about Active state buttons...							
        for ( int i = 0; i < buttonNames.length; i++ ) {
            reg.put( buttonNames[ i ] + "Button", createTrans( buttonFiles[ i ] + ".png" ) );
            reg.put( buttonNames[ i ] + "ButtonActive", createActive( buttonFiles[ i ] + ".png" ) );
            reg.put( buttonNames[ i ] + "ButtonSmall", createTrans( buttonFiles[ i ] + "-16.png" ) );
            reg.put( buttonNames[ i ] + "ButtonSmallActive",
                               createActive( buttonFiles[ i ] + "-16.png" ) );
            reg.put( buttonNames[ i ] + "ButtonSmallTitlebar",
                               createTrans( buttonFiles[ i ] + "-16.png", titlebar ) );
        }
        String[] shortNames = { "DC", "DK", "G1", "G2", "FT", "SS", "ONP", "Unknown" };
        String[] fileNames = { "directconnect", "edonkey2000", "gnutella", "gnutella2",
        					   "kazaa", "soulseek", "opennap", "unknown" };
        for ( int i = 0; i < shortNames.length; i++ ) {
            reg.put( shortNames[ i ] + "Connected", createTrans( fileNames[ i ] + "_connected.png" ) );
            reg.put( shortNames[ i ] + "Disconnected",
                               createTrans( fileNames[ i ] + "_disconnected.png" ) );
            reg.put( shortNames[ i ] + "Disabled", createTrans( fileNames[ i ] + "_disabled.png" ) );
            reg.put( shortNames[ i ] + "BadConnected",
                               createTrans( fileNames[ i ] + "_badconnected.png" ) );
            reg.put( shortNames[ i ] + "ConnectedWhite",
                               createTrans( fileNames[ i ] + "_connected.png", white ) );
        }

        /* some icons for networks without all states */
        reg.put( "BTConnected", createTrans( "bt_connected.png" ) );
        reg.put( "BTConnectedWhite", createTrans( "bt_connected.png", white ) );
        reg.put( "BTDisabled", createTrans( "bt_disabled.png" ) );
        reg.put( "MULTIConnected", createTrans( "multinet_connected.png" ) );
        reg.put( "MULTIConnectedWhite", createTrans( "multinet_connected.png", white ) );
        reg.put( "MULTIDisabled", createTrans( "multinet_disabled.png" ) );
        reg.put( "MessagesButtonSmallTrans", createRawImage( "messages-16.gif" ) );
        reg.put( "MessagesButtonSmallTransBW", createRawImage( "messages-16-bw.gif" ) );
        reg.put( "DownArrow", createTrans( "down.png" ) );
        reg.put( "UpArrow", createTrans( "up.png" ) );
        reg.put( "UpArrowBlue", createRawImage( "up_blue.gif" ) );
        reg.put( "SearchSmall", createTrans( "search_small.png" ) );
        reg.put( "SearchComplete", createTrans( "search_complete.png" ) );
        reg.put( "RedCrossSmall", createTrans( "red_cross-12.png" ) );
        reg.put( "X", createRawImage( "x.gif" ) );
        reg.put( "DropDown", createRawImage( "dropdown.gif" ) );
        reg.put( "epUnknown", createImageDescriptor( "ep_unknown.gif" ) );
        reg.put( "epTransferring", createImageDescriptor( "ep_transferring.gif" ) );
        reg.put( "epNoNeeded", createImageDescriptor( "ep_noneeded.gif" ) );
        reg.put( "epConnecting", createImageDescriptor( "ep_connecting.gif" ) );
        reg.put( "epAsking", createImageDescriptor( "ep_asking.gif" ) );
        reg.put( "epRatingPoor", createImageDescriptor( "ep_rating_poor.gif" ) );
        reg.put( "epRatingFair", createImageDescriptor( "ep_rating_fair.gif" ) );
        reg.put( "epRatingGood", createImageDescriptor( "ep_rating_good.gif" ) );
        reg.put( "epRatingExcellent", createImageDescriptor( "ep_rating_excellent.gif" ) );
        reg.put( "downloaded", createTrans( "downloaded_arrow.png" ) );
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

   private static Image createActiveImage( Image src, Color color ) {
        int width = src.getBounds().width;
        int height = src.getBounds().height;
        Image result = new Image( null, width, height );
        GC gc = new GC( result );
        gc.drawImage( src, 0, 0 );

        // let's try a border...	
        gc.setForeground( Display.getCurrent().getSystemColor( SWT.COLOR_TITLE_BACKGROUND ) );
        gc.drawLine( 0, 0, width - 1, 0 );
        gc.drawLine( 0, height - 1, width - 1, height - 1 );
        gc.drawLine( 0, 0, 0, height - 1 );
        gc.drawLine( width - 1, 0, width - 1, height - 1 );
        src.dispose();
        gc.dispose();
        return result;
    }
   
    private static Image createTransparentImage( Image src, Control control ) {
        return createTransparentImage( src, control.getBackground() );
    }

    // transparent pngs just don't work with swt 
    private static Image createTrans( String filename ) {
        return createTrans( filename, Display.getCurrent().getSystemColor( SWT.COLOR_WIDGET_BACKGROUND ) );
    }
    private static ImageDescriptor createRawImage ( String filename ) {
    	
    	return ImageDescriptor.createFromFile( MainTab.class, imagesDirectory + filename );
    }
  
    private static Image createTrans( String filename, Color color ) {
        return createTransparentImage( 
        	ImageDescriptor.createFromFile(
        		MainTab.class, imagesDirectory + filename ).createImage(), color );
    }

    private static ImageDescriptor createImageDescriptor( String filename ) {
        return ImageDescriptor.createFromFile( MainTab.class, imagesDirectory + filename );
    }
 
    private static Image createActive( String filename ) {
        return createActiveImage( createTrans( filename ),
                                  Display.getCurrent().getSystemColor( SWT.COLOR_WIDGET_BACKGROUND ) );
    }
}

/*
$Log: G2GuiResources.java,v $
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
