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
package net.mldonkey.g2gui.view.transferTree;

import org.eclipse.swt.widgets.Menu;


/**
 * IItemHasMenue
 *
 * @author $user$
 * @version $Id: IItemHasMenue.java,v 1.1 2003/07/15 13:25:41 dek Exp $ 
 *
 */
public interface IItemHasMenue {


	/**
	 * @param menu the Menu, where the items should be created onto
	 */
	void createMenu( Menu menu );
}

/*
$Log: IItemHasMenue.java,v $
Revision 1.1  2003/07/15 13:25:41  dek
right-mouse menu and some action to hopefully avoid flickering table

*/