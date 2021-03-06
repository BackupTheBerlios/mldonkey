/*
 * Copyright 2003
 * g2gui Team
 *
 *
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package net.mldonkey.g2gui.view.helper;

import org.eclipse.swt.custom.SashForm;


/**
 * ViewFrameListener
 *
 * @version $Id: SashViewFrameListener.java,v 1.3 2003/12/07 19:40:19 lemmy Exp $
 *
 */
public abstract class SashViewFrameListener extends ViewFrameListener {
	protected SashForm sashForm;

	public SashViewFrameListener(SashViewFrame sashViewFrame) {
	    super(sashViewFrame);
		this.sashForm = sashViewFrame.getParentSashForm();
	}
}


/*
$Log: SashViewFrameListener.java,v $
Revision 1.3  2003/12/07 19:40:19  lemmy
[Bug #1156] Allow a certain column to be 100% by pref

Revision 1.2  2003/11/29 01:51:53  zet
a few more viewframe changes.. will continue later.

Revision 1.1  2003/11/28 01:06:21  zet
not much- slowly expanding viewframe - will continue later

Revision 1.2  2003/11/24 01:33:27  zet
move some classes

Revision 1.1  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

*/
