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
package net.mldonkey.g2gui.view.transfer.downloadTable;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.view.TransferTab;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.transfer.ClientDetailDialog;
import net.mldonkey.g2gui.view.transfer.TreeClientInfo;
import net.mldonkey.g2gui.view.viewers.CustomTableTreeViewer;
import net.mldonkey.g2gui.view.viewers.GView;
import net.mldonkey.g2gui.view.viewers.tableTree.GTableTreeView;

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;


/**
 * DownloadTableTreeViewer
 *
 * @version $Id: DownloadTableTreeView.java,v 1.1 2003/10/31 22:41:59 zet Exp $
 *
 */
public class DownloadTableTreeView extends GTableTreeView implements ICellModifier,
    IDoubleClickListener {
    public static final String BASIC_COLUMNS = "ABCDFIK";
    public static final String NAME_COLUMN = "C";
    public static final String RATE_COLUMN = "I";
    public static final String CHUNK_COLUMN = "J";
    public static final int ID = 0;
    public static final int NETWORK = 1;
    public static final int NAME = 2;
    public static final int SIZE = 3;
    public static final int DOWNLOADED = 4;
    public static final int PERCENT = 5;
    public static final int SOURCES = 6;
    public static final int AVAIL = 7;
    public static final int RATE = 8;
    public static final int CHUNKS = 9;
    public static final int ETA = 10;
    public static final int PRIORITY = 11;
    public static final int LAST = 12;
    public static final int AGE = 13;
    private boolean advancedMode = false;
    private CellEditor[] cellEditors = null;
    private CustomTableTreeViewer tableTreeViewer;
    private GView clientView;
    private Composite parent;

    /**
     * Creates a new Viewer inside the composite parent
     * @param parent
     * @param mldonkey
     * @param page
     */
    public DownloadTableTreeView(Composite parent, GView clientTableView,
        final CoreCommunication core, TransferTab page) {
        super(parent, core);
        this.clientView = clientTableView;
        this.parent = parent;

        preferenceString = "download";
        columnLabels = new String[] {
                "TT_Download_Id", "TT_Download_Network", "TT_Download_Name", "TT_Download_Size",
                "TT_Download_Downloaded", "TT_Download_%", "TT_Download_Sources",
                "TT_Download_Avail", "TT_Download_Rate", "TT_Download_Chunks", "TT_Download_ETA",
                "TT_Download_Priority", "TT_Download_Last", "TT_Download_Age"
            };

        columnAlignment = new int[] {
                SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT,
                SWT.RIGHT, SWT.LEFT, SWT.RIGHT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT
            };

        columnDefaultWidths = new int[] { 50, 50, 250, 75, 75, 50, 50, 50, 50, 75, 75, 50, 75, 75 };

        gSorter = new DownloadTableTreeSorter(this);
        tableTreeContentProvider = new DownloadTableTreeContentProvider(this);
        tableLabelProvider = new DownloadTableTreeLabelProvider(this);
        tableTreeMenuListener = new DownloadTableTreeMenuListener(this, clientTableView);

        advancedMode = PreferenceLoader.loadBoolean("advancedMode");

        // createTableTreeViewer(parent, core);
        createContents(parent);
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.tableTree.GTableTreeView#createContents(org.eclipse.swt.widgets.Composite)
     */
    public void createContents(Composite parent) {
        super.createContents(parent);

        if (SWT.getPlatform().equals("gtk")) {
            getTable().getColumns()[ 0 ].pack();
        }

        addMenuListener();
        tableTreeViewer.addDoubleClickListener(this);
        tableTreeViewer.addSelectionChangedListener((DownloadTableTreeMenuListener) tableTreeMenuListener);
        tableTreeViewer.setInput(core.getFileInfoIntMap());
        core.getFileInfoIntMap().addObserver((DownloadTableTreeContentProvider) tableTreeContentProvider);
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.GView#loadColumnIDs()
     */
    public void loadColumnIDs() {
        if (advancedMode) {
            super.loadColumnIDs();
        } else {
            columnIDs = BASIC_COLUMNS;
        }
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.GView#createColumns()
     */
    public void createColumns() {
        super.createColumns();
        tableTreeViewer = getTableTreeViewer();
        tableTreeViewer.setColumnProperties(columnLabels);

        if (cellEditors != null) {
            for (int i = 0; i < cellEditors.length; i++) {
                if (cellEditors[ i ] != null) {
                    cellEditors[ i ].dispose();
                }
            }

            cellEditors = null;
        }

        if (columnIDs.indexOf(NAME_COLUMN) > 0) {
            cellEditors = new CellEditor[ columnIDs.length() ];
            cellEditors[ columnIDs.indexOf(NAME_COLUMN) ] = new TextCellEditor(getTable());
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
     */
    public boolean canModify(Object element, String property) {
        if (element instanceof FileInfo) {
            return true;
        }

        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
     */
    public Object getValue(Object element, String property) {
        FileInfo fileInfo = (FileInfo) element;

        return fileInfo.getName();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
     */
    public void modify(Object element, String property, Object value) {
        TableTreeItem item = (TableTreeItem) element;
        FileInfo fileInfo = (FileInfo) item.getData();
        String newName = ((String) value).trim();

        if (newName.length() > 0) {
            fileInfo.setName(newName);
        }
    }

    /**
     * Update display after pref changes
     */
    public void updateDisplay() {
        setPreferences();
        tableLabelProvider.updateDisplay();
        tableTreeContentProvider.updateDisplay();
        gSorter.updateDisplay();
        tableTreeViewer.refresh();
    }

    /**
     * Set Preferneces
     */
    public void setPreferences() {
        Table table = getTable();
        table.setBackground(PreferenceLoader.loadColour("downloadsBackgroundColor"));
        table.setFont(PreferenceLoader.loadFont("downloadsFontData"));
        table.setLinesVisible(PreferenceLoader.loadBoolean("displayGridLines"));

        if (PreferenceLoader.loadBoolean("tableCellEditors")) {
            tableTreeViewer.setCellEditors(cellEditors);
            tableTreeViewer.setCellModifier(this);
        } else {
            tableTreeViewer.setCellEditors(null);
            tableTreeViewer.setCellModifier(null);
        }

        if (advancedMode) {
            boolean b = PreferenceLoader.loadBoolean("displayChunkGraphs");

            if (columnIDs.indexOf(CHUNK_COLUMN) > 0) {
                tableTreeViewer.setChunksColumn(columnIDs.indexOf(CHUNK_COLUMN));
            }

            if ((b == false) && tableTreeViewer.getEditors()) {
                tableTreeViewer.closeAllTTE();

                if (columnIDs.indexOf(CHUNK_COLUMN) > 0) {
                    tableTreeViewer.setEditors(b);
                }
            } else if ((b == true) && !tableTreeViewer.getEditors()) {
                if (columnIDs.indexOf(CHUNK_COLUMN) > 0) {
                    tableTreeViewer.setEditors(true);
                    tableTreeViewer.openAllTTE();
                }
            }
        }
    }

    /**
     * @param b
     */
    public void updateClientsTable(boolean b) {
        ((DownloadTableTreeMenuListener) tableTreeMenuListener).updateClientsTable(b);
    }

    /**
     * @return boolean
     */
    public boolean clientsDisplayed() {
        if (clientView != null) {
            SashForm sashForm = (SashForm) parent.getParent().getParent();

            return sashForm.getWeights()[ 1 ] != 0;
        } else {
            return false;
        }
    }

    /**
     * toggle Clients table
     */
    public void toggleClientsTable() {
        if (clientView != null) {
            SashForm sashForm = (SashForm) parent.getParent().getParent();

            if (clientsDisplayed()) {
                sashForm.setWeights(new int[] { 1, 0 });
                updateClientsTable(false);
            } else {
                sashForm.setWeights(new int[] { 2, 1 });
            }
        }
    }

    /* (non-Javadoc)
    * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
    */
    public void doubleClick(DoubleClickEvent e) {
        IStructuredSelection sSel = (IStructuredSelection) e.getSelection();
        Object o = sSel.getFirstElement();

        if (o instanceof FileInfo) {
            FileInfo fileInfo = (FileInfo) o;

            if (tableTreeViewer.getExpandedState(fileInfo)) {
                tableTreeViewer.collapseToLevel(fileInfo, AbstractTreeViewer.ALL_LEVELS);
            } else {
                tableTreeViewer.expandToLevel(fileInfo, AbstractTreeViewer.ALL_LEVELS);
            }
        } else if (o instanceof TreeClientInfo) {
            TreeClientInfo treeClientInfo = (TreeClientInfo) o;
            new ClientDetailDialog(treeClientInfo.getFileInfo(), treeClientInfo.getClientInfo(),
                getCore());
        }
    }
}


/*
$Log: DownloadTableTreeView.java,v $
Revision 1.1  2003/10/31 22:41:59  zet
rename to View



*/
