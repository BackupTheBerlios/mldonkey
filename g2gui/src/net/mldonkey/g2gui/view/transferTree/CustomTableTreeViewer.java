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

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Widget;

/**
 * 
 * CustomTableTreeViewer
 *
 *
 * @version $Id: CustomTableTreeViewer.java,v 1.6 2003/08/23 15:21:37 zet Exp $ 
 *
 */
public class CustomTableTreeViewer extends TableTreeViewer {

	public CustomTableTreeViewer(Composite parent, int style) {
		super(parent, style);
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
	protected void doUpdateItem(Item item, Object element) {
		// update icon and label
		// Similar code in TableTreeViewer.doUpdateItem()
		IBaseLabelProvider prov = getLabelProvider();
		ITableLabelProvider tprov = null;
		ILabelProvider lprov = null;
		if (prov instanceof ITableLabelProvider)
			tprov = (ITableLabelProvider) prov;
		else
			lprov = (ILabelProvider) prov;
		int columnCount = this.getTableTree().getTable().getColumnCount();
		TableTreeItem ti = (TableTreeItem) item;
		// Also enter loop if no columns added.  See 1G9WWGZ: JFUIF:WINNT - TableViewer with 0 columns does not work
		for (int column = 0; column < columnCount || column == 0; column++) {
			String text = ""; //$NON-NLS-1$
			Image image = null;
			if (tprov != null) {
				text = tprov.getColumnText(element, column);
				image = tprov.getColumnImage(element, column);
			} else {
				if (column == 0) {
					text = lprov.getText(element);
					image = lprov.getImage(element);
				}
			}
			// Only set text if it changes
			if (!text.equals(ti.getText(column))) 
				ti.setText(column, text);
			// Apparently a problem to setImage to null if already null
			if (ti.getImage(column) != image) 
				ti.setImage(column, image);
		}
		if (prov instanceof IColorProvider) {
			IColorProvider cprov = (IColorProvider) prov;
			if (ti.getForeground() != cprov.getForeground(element)
				&& cprov.getForeground(element) != null) {
				ti.setForeground(cprov.getForeground(element));
			}
			if (ti.getBackground() != cprov.getBackground(element) 
				&& cprov.getBackground(element) != null) {
				ti.setBackground(cprov.getBackground(element));
			}
		}
	}
}
/*
$Log: CustomTableTreeViewer.java,v $
Revision 1.6  2003/08/23 15:21:37  zet
remove @author

Revision 1.5  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.4  2003/08/22 21:16:36  lemmster
replace $user$ with $Author: zet $

Revision 1.3  2003/08/20 22:18:56  zet
Viewer updates



*/
