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
package net.mldonkey.g2gui.view.statusline;

/**
 * SimpleStatusLineItem
 *
 * @author $user$
 * @version $Id: SimpleStatusLineItem.java,v 1.1 2003/06/26 14:04:59 dek Exp $ 
 *
 */
public class SimpleStatusLineItem extends StatusLineItem {

	/**
	 * 
	 */
	public SimpleStatusLineItem() {
		super();		
	}

	/**
	 * @param content
	 * @param alignment
	 */
	public SimpleStatusLineItem(String content, int alignment) {
		super(content, alignment);		
	}

}

/*
$Log: SimpleStatusLineItem.java,v $
Revision 1.1  2003/06/26 14:04:59  dek
Class statusline for easy information display

*/