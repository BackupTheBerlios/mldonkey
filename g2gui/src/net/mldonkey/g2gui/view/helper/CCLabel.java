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
package net.mldonkey.g2gui.view.helper;

import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;

/**
 * CClabel - static class to return new CLabels
 *
 * @version $Id: CCLabel.java,v 1.6 2003/10/22 01:37:10 zet Exp $
 *
 */
public class CCLabel {
    // prevent instantiation
    private CCLabel() {
    }

    /**
     * @param parent 
     * @param text 
     * @param image 
     *
     * @return CLabel
     */
    public static CLabel createCL( ViewForm parent, String text, String image ) {
        // GTK/SWT3-M4: (<unknown>:5346): GLib-GObject-CRITICAL **: file gtype.c: line 1942 (g_type_add_interface_static): assertion `g_type_parent (interface_type) == G_TYPE_INTERFACE' failed
        CLabel cLabel = new CLabel( parent, SWT.LEFT );
        cLabel.setText( G2GuiResources.getString( text ) );
        
        cLabel.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        
        if (PreferenceLoader.loadBoolean( "useGradient" )) {
			cLabel.setImage( G2GuiResources.getImage( image ) );
			cLabel.setForeground( parent.getDisplay().getSystemColor( SWT.COLOR_TITLE_FOREGROUND ) );
        	cLabel.setBackground( 
        		new Color[] { parent.getDisplay().getSystemColor( SWT.COLOR_TITLE_BACKGROUND ),
        			parent.getBackground() }, new int[] { 100 } );
        } else {
        	if (image.endsWith("Titlebar")) {
        		image = image.substring( 0, image.length() - 8 ); 
        		cLabel.setImage( G2GuiResources.getImage( image ) );
        	
        	} else  
				cLabel.setImage( G2GuiResources.getImage( image ) );
        }
        return cLabel;
    }
}

/*
$Log: CCLabel.java,v $
Revision 1.6  2003/10/22 01:37:10  zet
add column selector to server/search (might not be finished yet..)

Revision 1.5  2003/10/16 22:02:33  zet
*** empty log message ***

Revision 1.4  2003/10/08 01:12:16  zet
useGradient preference

Revision 1.3  2003/09/20 01:34:08  zet
*** empty log message ***

Revision 1.2  2003/09/18 10:04:57  lemmster
checkstyle

Revision 1.1  2003/08/29 22:11:47  zet
add CCLabel helper class


*/
