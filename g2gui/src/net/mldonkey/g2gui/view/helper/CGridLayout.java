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

import org.eclipse.swt.layout.GridLayout;

/**
 * CGridLayout - static class to return new GridLayouts
 *
 * @version $Id: CGridLayout.java,v 1.2 2003/09/18 10:04:57 lemmster Exp $
 *
 */
public class CGridLayout {
    // prevent instantiation
    private CGridLayout() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param numColumns DOCUMENT ME!
     * @param marginWidth DOCUMENT ME!
     * @param marginHeight DOCUMENT ME!
     * @param horizontalSpacing DOCUMENT ME!
     * @param verticalSpacing DOCUMENT ME!
     * @param makeColumnsEqualWidth DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static GridLayout createGL( int numColumns, int marginWidth, int marginHeight,
                                       int horizontalSpacing, int verticalSpacing,
                                       boolean makeColumnsEqualWidth ) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = numColumns;
        gridLayout.marginWidth = marginWidth;
        gridLayout.marginHeight = marginHeight;
        gridLayout.horizontalSpacing = horizontalSpacing;
        gridLayout.verticalSpacing = verticalSpacing;
        gridLayout.makeColumnsEqualWidth = makeColumnsEqualWidth;
        return gridLayout;
    }
}

/*
$Log: CGridLayout.java,v $
Revision 1.2  2003/09/18 10:04:57  lemmster
checkstyle

Revision 1.1  2003/08/28 22:44:30  zet
GridLayout helper class



*/
