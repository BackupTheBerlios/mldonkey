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
package net.mldonkey.g2gui.view.transferTree;

import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Widget;

/**
 * @author z
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CustomTableTreeViewer extends TableTreeViewer {
	
		public CustomTableTreeViewer(Composite parent, int style) {
			super (parent, style);
		}
	
		public Widget objectToItem(Object element) {
			return findItem(element);
		}
		
		public Item getParentItem(Item item) {
			return super.getParentItem(item);
		}
	
		public void updatePlus(Item item, Object element) {
			super.updatePlus(item, element);
		}
		
		public Item[] getItems(Item item) {
			return super.getItems(item);
		}
}
