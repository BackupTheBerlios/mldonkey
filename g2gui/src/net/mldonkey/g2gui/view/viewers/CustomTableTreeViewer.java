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

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.view.transfer.CTableTreeEditor;
import net.mldonkey.g2gui.view.transfer.ChunkCanvas;
import net.mldonkey.g2gui.view.transfer.TreeClientInfo;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableTreeViewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Widget;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;


/**
 *
 * CustomTableTreeViewer
 *
 *
 * @version $Id: CustomTableTreeViewer.java,v 1.7 2003/12/04 08:47:30 lemmy Exp $
 *
 */
public class CustomTableTreeViewer extends TableTreeViewer implements ICustomViewer {
    private Hashtable tableTreeEditors = new Hashtable();
    private boolean activeEditors = false;
    private int chunksColumn = -1;
    private int[] columnIDs;

    public CustomTableTreeViewer(Composite parent, int style) {
        super(parent, style);
    }

    // 3rd attempt to hack in TTEs
    public void setChunksColumn(int i) {
        chunksColumn = i;
    }

    public int getChunksColumn() {
        return chunksColumn;
    }

    public void setEditors(boolean b) {
        activeEditors = b;
    }

    public boolean getEditors() {
        return activeEditors;
    }

    public void nudgeColumn() {
        if (activeEditors) {
            TableColumn c = this.getTableTree().getTable().getColumn(chunksColumn);
            c.setWidth(c.getWidth());
        }
    }

    public void closeAllTTE() {
        Iterator i = tableTreeEditors.keySet().iterator();

        while (i.hasNext()) {
            Object object = i.next();
            disposeTTE((CTableTreeEditor) tableTreeEditors.get(object));
        }

        tableTreeEditors.clear();
    }

    public void openAllTTE() {
        TableTreeItem[] parents = this.getTableTree().getItems();

        for (int i = 0; i < parents.length; i++) {
            associateTTE(parents[ i ].getData(), parents[ i ], true);

            TableTreeItem[] children = parents[ i ].getItems();

            for (int j = 0; j < children.length; j++) {
                if (children[ j ].getData() != null)
                    associateTTE(children[ j ].getData(), children[ j ], true);
            }
        }
    }

    private void disposeTTE(CTableTreeEditor tableTreeEditor) {
        if (tableTreeEditor != null) {
            if (tableTreeEditor.getEditor() != null)
                tableTreeEditor.getEditor().dispose();

            tableTreeEditor.dispose();
        }
    }

    public void remove(Object[] elements) {
        super.remove(elements);

        if (activeEditors)
            removeTTE(elements);
    }

    public void removeTTE(Object[] elements) {
        for (int i = 0; i < elements.length; i++) {
            disposeTTE((CTableTreeEditor) tableTreeEditors.get(elements[ i ]));
            tableTreeEditors.remove(elements[ i ]);
        }

        nudgeColumn();
    }

    public void add(Object parentElement, Object[] childElements) {
        super.add(parentElement, childElements);
        nudgeColumn();
    }

    private void associateTTE(Object element, Item item, boolean forceUpdate) {
        if ((item.getData() != element) || forceUpdate) {
            if (!tableTreeEditors.containsKey(element)) {
                FileInfo fileInfo = null;
                ClientInfo clientInfo = null;

                if (element instanceof FileInfo)
                    fileInfo = (FileInfo) element;
                else if (element instanceof TreeClientInfo) {
                    TreeClientInfo treeClientInfo = (TreeClientInfo) element;
                    fileInfo = treeClientInfo.getFileInfo();
                    clientInfo = treeClientInfo.getClientInfo();
                }

                CTableTreeEditor tableTreeEditor = new CTableTreeEditor(this.getTableTree());
                tableTreeEditor.horizontalAlignment = SWT.LEFT;
                tableTreeEditor.grabHorizontal = true;

                ChunkCanvas chunkCanvas = new ChunkCanvas(this.getTableTree().getTable(),
                        SWT.NO_BACKGROUND, clientInfo, fileInfo, null);
                tableTreeEditor.setEditor(chunkCanvas, (TableTreeItem) item, chunksColumn);

                if (clientInfo != null)
                    clientInfo.addObserver(chunkCanvas);
                else
                    fileInfo.addObserver(chunkCanvas);

                tableTreeEditors.put(element, tableTreeEditor);
            } else {
                CTableTreeEditor tableTreeEditor = (CTableTreeEditor) tableTreeEditors.get(element);
                tableTreeEditor.setItem((TableTreeItem) item);
            }
        }
    }

    protected void mapElement(Object element, Widget item) {
        super.mapElement(element, item);
        if (activeEditors) 
            associateTTE(element, (Item) item, true);
    }

    protected void unmapElement(Object element) {
        super.unmapElement(element);
        if (activeEditors)
            disassociateTTE(element);
    }

    private void disassociateTTE(Object element) {
        // Fully dispose if filters are active, or processing child items
        //  if ((getFilters().length > 0) || item.getData() instanceof TreeClientInfo) {
        
        disposeTTE((CTableTreeEditor) tableTreeEditors.get(element));
        tableTreeEditors.remove(element);

        //        } else {
        //            CTableTreeEditor tableTreeEditor = (CTableTreeEditor) tableTreeEditors.get(element);
        //
        //            if (tableTreeEditor != null)
        //                tableTreeEditor.setItem(null);
        //        }
    }
    
    public void refresh() {
        super.refresh();
		if (activeEditors)
			nudgeColumn();
    }

    public void expandAll() {
        super.expandAll();
        nudgeColumn();
    }

    public void collapseAll() {
        super.collapseAll();
        nudgeColumn();
    }

    public void collapseToLevel(Object element, int level) {
        super.collapseToLevel(element, level);
        nudgeColumn();
    }

    public void expandToLevel(Object element, int level) {
        super.expandToLevel(element, level);
        nudgeColumn();
    }

    // End TTE hack	
    public void setColumnIDs(String string) {
        columnIDs = new int[ string.length() ];

        for (int i = 0; i < string.length(); i++)
            columnIDs[ i ] = string.charAt(i) - ColumnSelector.MAGIC_NUMBER;
    }

    public int[] getColumnIDs() {
        return columnIDs;
    }

    public void setSelectionToWidget(List v, boolean reveal) {
        super.setSelectionToWidget(v, reveal);
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
        for (int column = 0; (column < columnCount) || (column == 0); column++) {
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

            if (ti.getForeground() != cprov.getForeground(element)) {
                if ((cprov.getForeground(element) != null) ||
                        (!ti.getParent().getForeground().getRGB().equals(ti.getForeground().getRGB())))
                    ti.setForeground(cprov.getForeground(element));
            }

            if (ti.getBackground() != cprov.getBackground(element)) {
                if ((cprov.getBackground(element) != null) ||
                        (!ti.getParent().getBackground().getRGB().equals(ti.getBackground().getRGB())))
                    ti.setBackground(cprov.getBackground(element));
            }
        }
    }
}


/*
$Log: CustomTableTreeViewer.java,v $
Revision 1.7  2003/12/04 08:47:30  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.6  2003/11/29 20:37:04  zet
try nudge in refresh instead of internalrefresh

Revision 1.5  2003/11/28 20:51:52  zet
small method move

Revision 1.4  2003/11/23 17:58:03  lemmy
removed dead/unused code

Revision 1.3  2003/11/22 20:00:00  zet
expand/collapseToLevel

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
