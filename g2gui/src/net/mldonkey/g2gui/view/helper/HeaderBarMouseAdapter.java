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

import org.eclipse.jface.action.MenuManager;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;


/**
 * HeaderBarMouseAdapter - A helper for header bar mouse events & menus
 *
 * @version $Id: HeaderBarMouseAdapter.java,v 1.5 2003/12/04 08:47:31 lemmy Exp $
 *
 */
public class HeaderBarMouseAdapter extends MouseAdapter {
    private CLabel cLabel;
    private MenuManager menuManager;

    /**
     * Creates a new HeaderBarMouseAdapter
     * @param cLabel
     * @param menuManager
     */
    public HeaderBarMouseAdapter(CLabel cLabel, MenuManager menuManager) {
        this.cLabel = cLabel;
        this.menuManager = menuManager;
    }

    private boolean overImage(int x) {
        return x < cLabel.getImage().getBounds().width;
    }

    private void showMenu(Point p) {
        Menu menu = menuManager.createContextMenu(cLabel);
        menu.setLocation(p);
        menu.setVisible(true);
    }

    public void mouseDown(MouseEvent e) {
        if (((e.button == 1) && overImage(e.x)) || (e.button == 3)) {
            Point p;

            if (e.button == 1) {
                p = new Point(0, cLabel.getBounds().height);
            } else {
                p = new Point(e.x, e.y);
            }

            showMenu(((CLabel) e.widget).toDisplay(p));
        }
    }
}


/*
$Log: HeaderBarMouseAdapter.java,v $
Revision 1.5  2003/12/04 08:47:31  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.4  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.3  2003/09/20 01:27:31  zet
*** empty log message ***

Revision 1.2  2003/09/18 10:04:57  lemmy
checkstyle

Revision 1.1  2003/09/16 02:12:07  zet
initial


*/
