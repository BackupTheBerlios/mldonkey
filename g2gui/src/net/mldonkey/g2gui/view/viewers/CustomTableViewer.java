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
package net.mldonkey.g2gui.view.viewers;

import net.mldonkey.g2gui.view.G2Gui;

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
 *
 * CustomTableViewer
 *
 *
 * @version $Id: CustomTableViewer.java,v 1.5 2004/03/29 14:51:44 dek Exp $
 *
 */
public class CustomTableViewer extends TableViewer implements ICustomViewer {
    private int[] columnIDs;

    public CustomTableViewer(Composite parent, int style) {
        super(parent, style);
    }

    // Start ICustomViewer
    public void closeAllTTE() {}
    public void setEditors(boolean b) {}
    // End ICustomViewer
    
    public void setColumnIDs(String string) {
        columnIDs = new int[ string.length() ];

        for (int i = 0; i < string.length(); i++) {
            columnIDs[ i ] = string.charAt(i) - ColumnSelector.MAGIC_NUMBER;
        }
    }

    public int[] getColumnIDs() {
        return columnIDs;
    }

    protected void doUpdateItem(Widget widget, Object element, boolean fullMap) {
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
            TableItem ti = item;

            // Also enter loop if no columns added.  See 1G9WWGZ: JFUIF:WINNT - TableViewer with 0 columns does not work
            for (int column = 0; (column < columnCount) || (column == 0); column++) {
                // Similar code in TableTreeViewer.doUpdateItem()
                String text = G2Gui.emptyString; //$NON-NLS-1$
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
                if (!text.equals(ti.getText(column))) {
                    ti.setText(column, text);
                }

                // Apparently a problem to setImage to null if already null
                if (ti.getImage(column) != image) {
                    ti.setImage(column, image);
                }
            }

            if (prov instanceof IColorProvider) {
                IColorProvider cprov = (IColorProvider) prov;

                if (ti.getForeground() != cprov.getForeground(element)) {
                    if ((cprov.getForeground(element) != null) || (!ti.getParent().getForeground().getRGB().equals(ti.getForeground().getRGB()))) {
                        ti.setForeground(cprov.getForeground(element));
                    }
                }

                if (ti.getBackground() != cprov.getBackground(element)) {
                    if ((cprov.getBackground(element) != null) || (!ti.getParent().getBackground().getRGB().equals(ti.getBackground().getRGB()))) {
                        ti.setBackground(cprov.getBackground(element));
                    }
                }
            }
        }
    }
}


/*
$Log: CustomTableViewer.java,v $
Revision 1.5  2004/03/29 14:51:44  dek
some mem-improvements

Revision 1.4  2003/12/04 08:47:30  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.3  2003/11/23 17:58:03  lemmy
removed dead/unused code

Revision 1.2  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.1  2003/10/22 01:36:59  zet
add column selector to server/search (might not be finished yet..)


*/
