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

import org.eclipse.swt.SWT;

/**
 * StatuslineItem This class is intended to be subclassed for all the items, that should appear in 
 * the statusline.
 *
 * @author $user$
 * @version $Id: StatusLineItem.java,v 1.3 2003/06/28 09:50:12 lemmstercvs01 Exp $ 
 *
 */
abstract class StatusLineItem {
	protected int position;
	protected String content;
	private int alignment = SWT.NONE;

	/**
	 * 
	 */
	public StatusLineItem() {
		content = "";

	}

	/**
	 * @param content what this Label should show
	 * @param alignment how it is aligned (SWT.LEFT,SWT.CENTER,SWT.RIGHT)
	 */
	public StatusLineItem(String content, int alignment) {
		this.content = content;
		this.alignment = alignment;			
	}

	/**
	 * @return
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param string
	 */
	public void setContent(String string) {
		content = string;
	}

	

	/**
	 * @return
	 */
	public int getAlignment() {
		return alignment;
	}

	/**
	 * @param i
	 */
	public void setAlignment(int i) {
		alignment = i;
	}

	/**
	 * @param i
	 */
	public void setIndex(int i) {
		this.position = i;
		
	}
}

/*
$Log: StatusLineItem.java,v $
Revision 1.3  2003/06/28 09:50:12  lemmstercvs01
ResourceBundle added

Revision 1.2  2003/06/26 21:11:10  dek
speed is shown

Revision 1.1  2003/06/26 14:04:59  dek
Class statusline for easy information display

*/