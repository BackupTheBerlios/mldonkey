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
import net.mldonkey.g2gui.view.G2Gui;
import net.mldonkey.g2gui.view.MainTab;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * G2GuiResources
 *
 *
 * @version $Id: G2GuiResources.java,v 1.19 2003/09/01 00:44:21 zet Exp $
 */
public class G2GuiResources {
	
	private static ImageRegistry imageRegistry = null;
	private static final ResourceBundle bundle = ResourceBundle.getBundle("g2gui");
	
	// prevent instantiation
	private G2GuiResources() {
	}
	
	public static void initialize() {
		createImageRegistry();
	}
	
	public static Image getImage(String key) {
		return getImageRegistry().get(key);
	}
	public static ImageRegistry getImageRegistry() {
		if (imageRegistry == null)
			imageRegistry = new ImageRegistry();
		return imageRegistry;
	}
	public static String getString(String key) {
		try {
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}
	// helper method
	public static Image getNetworkImage(Enum networkType) {
			if (networkType == Enum.DONKEY)
				return getImage("DKConnectedWhite");
			if (networkType == Enum.FT)
				return getImage("FTConnectedWhite");
			if (networkType == Enum.GNUT)
				return getImage("G1ConnectedWhite");
			if (networkType == Enum.GNUT2)
				return getImage("G2ConnectedWhite");	
			if (networkType == Enum.SOULSEEK)
				return getImage("SSConnectedWhite");
			if (networkType == Enum.DC)
				return getImage("DCConnectedWhite");
			if ( networkType == Enum.BT )
				return getImage( "BTConnectedWhite");
			if ( networkType == Enum.MULTINET )
				return getImage( "MULTIConnectedWhite" );		
			return getImage("UnknownConnectedWhite");
	}	

	public static Image getClientImage(EnumState clientState) {
		if (clientState == EnumState.CONNECTED_DOWNLOADING)
			return G2GuiResources.getImage("epTransferring");
		if (clientState == EnumState.CONNECTING
			|| clientState == EnumState.CONNECTED_INITIATING)	
			return G2GuiResources.getImage("epConnecting");	
		if (clientState == EnumState.CONNECTED_AND_QUEUED
			|| clientState == EnumState.NOT_CONNECTED_WAS_QUEUED)
			return G2GuiResources.getImage("epAsking");		
		if (clientState == EnumState.BLACK_LISTED)
			return G2GuiResources.getImage("epNoNeeded");
		return G2GuiResources.getImage("epUnknown");
	}
	
	public static String getRatingString(int availability) {
		if ( availability > 400 )
			return "" + G2GuiResources.getString( "RTLP_EXCELLENT" );
		else if ( availability > 100 )
			return "" + G2GuiResources.getString( "RTLP_VERYHIGH" );
		else if ( availability > 25 )
			return "" + G2GuiResources.getString( "RTLP_HIGH" );
		else if ( availability > 10 )
			return "" + G2GuiResources.getString( "RTLP_NORMAL" );
		else
			return "" + G2GuiResources.getString( "RTLP_LOW" );
	}

	public static Image getRatingImage(int availability) {
		if ( availability > 400 )
			return G2GuiResources.getImage( "epRatingExcellent" );
		else if ( availability > 100 )
			return G2GuiResources.getImage( "epRatingExcellent" );
		else if ( availability > 25 )
			return G2GuiResources.getImage( "epRatingGood" );
		else if ( availability > 10 )
			return G2GuiResources.getImage( "epRatingFair" );
		else
			return G2GuiResources.getImage( "epRatingPoor" );
	}

	private static void createImageRegistry () {
		Color white = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		Color titlebar = Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND);
		
		// hack to use transparent .gif in titlebar
		ImageData source = ImageDescriptor.createFromFile(G2Gui.class, "images/mld_logo_48x48.gif").getImageData();
		ImageData mask = source.getTransparencyMask();
		Image icon = new Image( Display.getCurrent(), source,mask);		
		imageRegistry.put("ProgramIcon",icon);	
		
		imageRegistry.put( "G2GuiLogo", createTrans( "mld_logo_48x48.png") );	
		
		String[] buttonNames = { "Preferences", "Statistics", "Console",
									"Transfers", "Search", "Servers", "Messages" };
		String[] buttonFiles = { "preferences", "statistics", "console",
									"transfer3a", "search", "server", "messages" };							
				
		// still thinking about Active state buttons...							
		for (int i=0; i < buttonNames.length; i++) {
			imageRegistry.put(buttonNames[i] + "Button", createTrans(buttonFiles[i] + ".png"));
			imageRegistry.put(buttonNames[i] + "ButtonActive", createActive(buttonFiles[i] + ".png"));
			imageRegistry.put(buttonNames[i] + "ButtonSmall",  createTrans(buttonFiles[i] + "-16.png") );
			imageRegistry.put(buttonNames[i] + "ButtonSmallActive", createActive(buttonFiles[i] + "-16.png"));
			imageRegistry.put(buttonNames[i] + "ButtonSmallTitlebar", createTrans(buttonFiles[i] + "-16.png", titlebar));
		}
		
		String[] shortNames = { "DC", "DK", "G1", "G2", "FT", "SS", "ONP", "Unknown" };
		String[] fileNames = { "directconnect", "edonkey2000", "gnutella", "gnutella2",
								"kazaa", "soulseek", "unknown", "unknown" };
								
		for ( int i = 0; i < shortNames.length; i++ ) {
			imageRegistry.put( shortNames[i] + "Connected", createTrans( fileNames[i] + "_connected.png" ) );
			imageRegistry.put( shortNames[i] + "Disconnected", createTrans( fileNames[i] + "_disconnected.png" ) );
			imageRegistry.put( shortNames[i] + "Disabled", createTrans( fileNames[i] + "_disabled.png" ) );
			imageRegistry.put( shortNames[i] + "BadConnected", createTrans( fileNames[i] + "_badconnected.png" ) );
			imageRegistry.put( shortNames[i] + "ConnectedWhite", createTrans( fileNames[i] + "_connected.png", white ) );
		}
		/* some icons for networks without all states */
		imageRegistry.put( "BTConnected", createTrans( "bt_connected.png" ) );
		imageRegistry.put( "BTConnectedWhite", createTrans( "bt_connected.png", white ) );
		imageRegistry.put( "BTDisabled", createTrans( "bt_disabled.png" ) );
		imageRegistry.put( "MULTIConnected", createTrans( "multinet_connected.png" ) );
		imageRegistry.put( "MULTIConnectedWhite", createTrans( "multinet_connected.png", white ) );
		imageRegistry.put( "MULTIDisabled", createTrans( "multinet_disabled.png" ) );
		
		imageRegistry.put( "MessagesButtonSmallTrans",ImageDescriptor.createFromFile(MainTab.class, "images/messages-16.gif") );	
		imageRegistry.put( "MessagesButtonSmallTransBW",ImageDescriptor.createFromFile(MainTab.class, "images/messages-16-bw.gif") );	
		
		imageRegistry.put( "DownArrow", createTrans( "down.png" ) );
		imageRegistry.put( "UpArrow", createTrans( "up.png" ) );
		imageRegistry.put( "UpArrowBlue", ImageDescriptor.createFromFile(MainTab.class, "images/up_blue.gif") );
		
		imageRegistry.put( "SearchSmall", createTrans( "search_small.png" )) ;
		imageRegistry.put( "SearchComplete", createTrans( "search_complete.png" )) ;
		
		imageRegistry.put( "X", ImageDescriptor.createFromFile(MainTab.class, "images/x.gif") );
		imageRegistry.put( "DropDown", ImageDescriptor.createFromFile(MainTab.class, "images/dropdown.gif") );
		
		imageRegistry.put( "epUnknown", createImageDescriptor("ep_unknown.gif") );
		imageRegistry.put( "epTransferring", createImageDescriptor("ep_transferring.gif") );
		imageRegistry.put( "epNoNeeded", createImageDescriptor("ep_noneeded.gif") );
		imageRegistry.put( "epConnecting", createImageDescriptor("ep_connecting.gif") );
		imageRegistry.put( "epAsking", createImageDescriptor("ep_asking.gif") );
		
		imageRegistry.put( "epRatingPoor", createImageDescriptor("ep_rating_poor.gif") );
		imageRegistry.put( "epRatingFair", createImageDescriptor("ep_rating_fair.gif") );
		imageRegistry.put( "epRatingGood", createImageDescriptor("ep_rating_good.gif") );
		imageRegistry.put( "epRatingExcellent", createImageDescriptor("ep_rating_excellent.gif") );
		imageRegistry.put( "downloaded", createTrans( "downloaded_arrow.png" ) );
	}
	
	/**
	 * Creates a Transparent imageobject with a given .png|.gif Image-Object
	 * be aware, the the scr-image is disposed, so dont' use it any further
	 * 
	 * @param src the non-transparent image we want to process
	 * @param control where is our image laid in, to check for the background-color
	 * @return the transparent image
	 */
	public static Image createTransparentImage( Image src, Color color ) {
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
	 * Convert an image to grayscale, excluding the color specified
	 * 
	 * @param src
	 * @param color
	 * @return
	 */
	public static Image createActiveImage(Image src, Color color) {
		int width = src.getBounds().width;
		int height = src.getBounds().height;
		
		Image result = new Image( null, width, height );		
		GC gc = new GC( result );
		gc.drawImage( src, 0, 0 );
		
		// let's try a border...	
		gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));
		gc.drawLine(0,0,width-1,0);	
		gc.drawLine(0,height-1,width-1,height-1);
		gc.drawLine(0,0,0,height-1);
		gc.drawLine(width-1,0,width-1,height-1);
			
		src.dispose();		
		gc.dispose();		
		return result;
	}
	
	 
	/**
	 * @param src
	 * @param control
	 * @return
	 */
	public static Image createTransparentImage( Image src, Control control) {
		return createTransparentImage(src, control.getBackground() );
	}

	// transparent pngs just don't work with swt 
	private static Image createTrans(String filename) {
		return createTrans( filename, Display.getCurrent().getSystemColor( SWT.COLOR_WIDGET_BACKGROUND ));
	}
	private static Image createTrans(String filename, Color color) {
		return createTransparentImage( ImageDescriptor.createFromFile(MainTab.class, "images/" + filename).createImage(), color);
	}
	private static ImageDescriptor createImageDescriptor(String filename) {
		return ImageDescriptor.createFromFile(MainTab.class, "images/" + filename);
	}
	private static Image createActive(String filename) {
		return createActiveImage( createTrans( filename ), Display.getCurrent().getSystemColor( SWT.COLOR_WIDGET_BACKGROUND ) );
	}
}

/*
$Log: G2GuiResources.java,v $
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


