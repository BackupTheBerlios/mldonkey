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
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

/**
 * @author zet
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CustomTableViewer extends TableViewer {
	
	public CustomTableViewer(Composite parent, int style) {
	  super(parent, style);
	}

	protected void doUpdateItem(
		Widget widget,
		Object element,
		boolean fullMap) {
		if (widget instanceof TableItem) {
			TableItem item = (TableItem) widget;

			// remember element we are showing
			if (fullMap) {
				associate(element, item);
			} else {
				item.setData(element);
				mapElement(element, item);
			}

			IBaseLabelProvider prov = getLabelProvider();
			ITableLabelProvider tprov = null;
			ILabelProvider lprov = null;
			if (prov instanceof ITableLabelProvider) {
				tprov = (ITableLabelProvider) prov;
			} else {
				lprov = (ILabelProvider) prov;
			}
			int columnCount = this.getTable().getColumnCount();
			TableItem ti = (TableItem) item;
			// Also enter loop if no columns added.  See 1G9WWGZ: JFUIF:WINNT - TableViewer with 0 columns does not work
			for (int column = 0;
				column < columnCount || column == 0;
				column++) {
				// Similar code in TableTreeViewer.doUpdateItem()
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
				if (!text.equals(ti.getText(column))) {
					ti.setText(column, text);
				}
				if (ti.getImage(column) != image) {
					ti.setImage(column, image);
				}
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



}
/*
$Log: CustomTableViewer.java,v $
Revision 1.1  2003/08/20 22:18:56  zet
Viewer updates


*/