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

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * G2GuiResources
 *
 * @author $Author: lemmster $
 * @version $Id: G2GuiResources.java,v 1.5 2003/08/22 21:10:57 lemmster Exp $
 */
public class G2GuiResources {
	
	private static ImageRegistry imageRegistry = null;
	private static final ResourceBundle bundle = ResourceBundle.getBundle("g2gui");
	
	// prevent instantiation
	private G2GuiResources() {
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

}

/*
$Log: G2GuiResources.java,v $
Revision 1.5  2003/08/22 21:10:57  lemmster
replace $user$ with $Author$

Revision 1.4  2003/08/21 11:19:15  lemmster
added bt and multinet image

Revision 1.3  2003/08/20 14:58:43  zet
sources clientinfo viewer

Revision 1.2  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.1  2003/08/17 23:13:41  zet
centralize resources, move images



*/


