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

import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;

/**
 * CClabel - static class to return new CLabels
 *
 * @version $Id: CCLabel.java,v 1.2 2003/09/18 10:04:57 lemmster Exp $
 *
 */
public class CCLabel {
    // prevent instantiation
    private CCLabel() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param text DOCUMENT ME!
     * @param image DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static CLabel createCL( ViewForm parent, String text, String image ) {
        CLabel cLabel = new CLabel( parent, SWT.LEFT );
        cLabel.setText( G2GuiResources.getString( text ) );
        cLabel.setImage( G2GuiResources.getImage( image ) );
        cLabel.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        cLabel.setForeground( parent.getDisplay().getSystemColor( SWT.COLOR_TITLE_FOREGROUND ) );
        cLabel.setBackground( 
        	new Color[] { parent.getDisplay().getSystemColor( SWT.COLOR_TITLE_BACKGROUND ),
        		parent.getBackground() }, new int[] { 100 } );
        return cLabel;
    }
}

/*
$Log: CCLabel.java,v $
Revision 1.2  2003/09/18 10:04:57  lemmster
checkstyle

Revision 1.1  2003/08/29 22:11:47  zet
add CCLabel helper class


*/
