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

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * @author zet
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
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
			return getImage("UnknownConnectedWhite");
	}	

}

/*
$Log: G2GuiResources.java,v $
Revision 1.2  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.1  2003/08/17 23:13:41  zet
centralize resources, move images



*/


