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
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.transfer.ClientDetailDialog;
import net.mldonkey.g2gui.view.transfer.TreeClientInfo;
import net.mldonkey.g2gui.view.transfer.clientTable.ClientTableViewer;
import net.mldonkey.g2gui.view.viewers.CustomTableTreeViewer;
import net.mldonkey.g2gui.view.viewers.GTableViewer;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;


/**
 * DownloadTableTreeViewer
 *
 * @version $Id: DownloadTableTreeViewer.java,v 1.15 2003/10/22 21:20:33 zet Exp $
 *
 */
public class DownloadTableTreeViewer implements ICellModifier, IDoubleClickListener {
    private static boolean displayChunkGraphs = false;
    public static final String ALL_COLUMNS = "ABCDEFGHIJKLMN";
    public static final String BASIC_COLUMNS = "ABCDFIK";
    public static final String NAME_COLUMN = "C";
    public static final String RATE_COLUMN = "I";
    public static final String CHUNK_COLUMN = "J";
    public static final String[] COLUMN_LABELS = {
        "TT_Download_Id", "TT_Download_Network", "TT_Download_Name", "TT_Download_Size", "TT_Download_Downloaded", "TT_Download_%",
        "TT_Download_Sources", "TT_Download_Avail", "TT_Download_Rate", "TT_Download_Chunks", "TT_Download_ETA", "TT_Download_Priority",
        "TT_Download_Last", "TT_Download_Age"
    };
    private static final int[] COLUMN_DEFAULT_WIDTHS = { 50, 50, 250, 75, 75, 50, 50, 50, 50, 75, 75, 50, 75, 75 };
    private static final int[] COLUMN_ALIGNMENT = {
        SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.LEFT, SWT.RIGHT, SWT.LEFT, SWT.RIGHT,
        SWT.RIGHT
    };
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
    private CustomTableTreeViewer tableTreeViewer;
    private TableTree tableTree;
    private Table table;
    private DownloadTableTreeSorter tableTreeSorter;
    private DownloadTableTreeContentProvider tableTreeContentProvider;
    private DownloadTableTreeLabelProvider tableTreeLabelProvider;
    private DownloadTableTreeMenuListener tableTreeMenuListener;
    private FileInfo selectedFile = null;
    private MenuManager popupMenu;
    private boolean advancedMode = false;
    private boolean manualDispose = false;
    private CoreCommunication mldonkey;
    private CellEditor[] cellEditors = null;
    private ClientTableViewer clientTableViewer;
    private String columnIDs;
    private Composite parent;

    /**
     * Creates a new Viewer inside the composite parent
     * @param parent
     * @param mldonkey
     * @param page
     */
    public DownloadTableTreeViewer(Composite parent, ClientTableViewer clientTableViewer, final CoreCommunication mldonkey, TransferTab page) {
        this.clientTableViewer = clientTableViewer;
        this.parent = parent;
        this.mldonkey = mldonkey;

        advancedMode = PreferenceLoader.loadBoolean("advancedMode");
        createTableTreeViewer(parent, mldonkey);
    }

    /**
     * @param parent
     * @param mldonkey
     */
    public void createTableTreeViewer(Composite parent, final CoreCommunication mldonkey) {
        tableTreeViewer = new CustomTableTreeViewer(parent, SWT.MULTI | SWT.FULL_SELECTION);
        tableTree = tableTreeViewer.getTableTree();
        table = tableTree.getTable();
        tableTree.setLayoutData(new GridData(GridData.FILL_BOTH));

        createColumns();

        // SWT3: SWT.RIGHT labels don't show up on gtk until a resize.. why? 
        if (SWT.getPlatform().equals("gtk")) {
            table.getColumns()[ 0 ].pack();
        }

        tableTreeLabelProvider = new DownloadTableTreeLabelProvider();
        tableTreeLabelProvider.setTableTreeViewer(tableTreeViewer);

        tableTreeSorter = new DownloadTableTreeSorter();
        tableTreeSorter.setTableTreeViewer(tableTreeViewer);

        tableTreeViewer.setLabelProvider(tableTreeLabelProvider);

        tableTreeContentProvider = new DownloadTableTreeContentProvider();
        tableTreeContentProvider.setDownloadTableTreeViewer(this);

        tableTreeViewer.setContentProvider(tableTreeContentProvider);
        tableTreeViewer.setUseHashlookup(true);

        tableTreeViewer.addDoubleClickListener(this);

        tableTreeMenuListener = new DownloadTableTreeMenuListener(this, clientTableViewer, mldonkey);

        tableTreeViewer.addSelectionChangedListener(tableTreeMenuListener);

        popupMenu = new MenuManager();
        popupMenu.setRemoveAllWhenShown(true);
        popupMenu.addMenuListener(tableTreeMenuListener);

        tableTree.setMenu(popupMenu.createContextMenu(tableTree));

        String savedSort = PreferenceLoader.loadString("downloadsTableLastSort");

        if (!savedSort.equals("") && (columnIDs.indexOf(savedSort) != -1)) {
            tableTreeSorter.setColumnIndex(columnIDs.indexOf(savedSort));
        } else {
            if (columnIDs.indexOf(RATE_COLUMN) != -1) {
                tableTreeSorter.setColumnIndex(columnIDs.indexOf(RATE_COLUMN));
            }
        }

        tableTreeViewer.setSorter(tableTreeSorter);

        tableTreeViewer.getTableTree().addDisposeListener(new DisposeListener() {
                public void widgetDisposed(DisposeEvent e) {
                    int col = ((DownloadTableTreeSorter) tableTreeViewer.getSorter()).getLastColumnIndex();
                    PreferenceStore p = PreferenceLoader.getPreferenceStore();
                    p.setValue("downloadsTableLastSort", String.valueOf(columnIDs.charAt(col)));
                }
            });

        setPreferences();
        tableTreeViewer.setInput(mldonkey.getFileInfoIntMap());
        mldonkey.getFileInfoIntMap().addObserver(tableTreeContentProvider);
    }

    /**
     * Create columns
     */
    public void createColumns() {
        TableColumn[] tableColumns = tableTreeViewer.getTableTree().getTable().getColumns();

        // The SWT TableColumn DisposeEvent.widget returns a width of 0 when manually disposing
        manualDispose = true;

        for (int i = tableColumns.length - 1; i > -1; i--) {
            tableColumns[ i ].dispose();
        }

        manualDispose = false;

        if (advancedMode) {
            String prefCols = PreferenceLoader.loadString("downloadTableColumns");

            if (GTableViewer.validColumnIDs(prefCols, ALL_COLUMNS)) {
                columnIDs = prefCols;
            } else {
                columnIDs = ALL_COLUMNS;

                PreferenceStore p = PreferenceLoader.getPreferenceStore();
                p.setValue("downloadTableColumns", columnIDs);
            }
        } else {
            columnIDs = BASIC_COLUMNS;
        }

        tableTreeViewer.setColumnIDs(columnIDs);
        tableTreeViewer.setColumnProperties(COLUMN_LABELS);
        table.setHeaderVisible(true);

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
            cellEditors[ columnIDs.indexOf(NAME_COLUMN) ] = new TextCellEditor(table);
        }

        final PreferenceStore p = PreferenceLoader.getPreferenceStore();

        for (int i = 0; i < columnIDs.length(); i++) {
            final int columnIndex = i;
            final int arrayItem = columnIDs.charAt(i) - 65;

            TableColumn tableColumn = new TableColumn(table, COLUMN_ALIGNMENT[ arrayItem ]);
            p.setDefault(COLUMN_LABELS[ arrayItem ], COLUMN_DEFAULT_WIDTHS[ arrayItem ]);
            tableColumn.setText(G2GuiResources.getString(COLUMN_LABELS[ arrayItem ]));
            tableColumn.setWidth(p.getInt(COLUMN_LABELS[ arrayItem ]));

            tableColumn.addDisposeListener(new DisposeListener() {
                    public synchronized void widgetDisposed(DisposeEvent e) {
                        TableColumn thisColumn = (TableColumn) e.widget;

                        if (!manualDispose) {
                            p.setValue(COLUMN_LABELS[ arrayItem ], thisColumn.getWidth());
                        }
                    }
                });

            tableColumn.addListener(SWT.Selection,
                new Listener() {
                    public void handleEvent(Event e) {
                        tableTreeSorter.setColumnIndex(columnIndex);
                        tableTreeViewer.refresh();
                    }
                });
        }
    }

    /**
     * Reset columns
     */
    public void resetColumns() {
        tableTreeViewer.setInput(null);
        tableTreeViewer.setEditors(false);
        tableTreeSorter.setColumnIndex(0);
        tableTreeViewer.closeAllTTE();
        createColumns();
        tableTreeViewer.setInput(mldonkey.getFileInfoIntMap());
        updateDisplay();
    }

    /**
     * @return CustomTableTreeViewer
     */
    public CustomTableTreeViewer getTableTreeViewer() {
        return tableTreeViewer;
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
        tableTreeLabelProvider.updateDisplay();
        tableTreeViewer.refresh();
    }

    /**
     * Set Preferneces
     */
    public void setPreferences() {
        table.setBackground(PreferenceLoader.loadColour("downloadsBackgroundColor"));
        table.setFont(PreferenceLoader.loadFont("downloadsFontData"));
        table.setLinesVisible(PreferenceLoader.loadBoolean("displayGridLines"));
        tableTreeLabelProvider.displayColors(PreferenceLoader.loadBoolean("displayTableColors"));
        tableTreeContentProvider.setUpdateDelay(PreferenceLoader.loadInteger("updateDelay"));

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

        if (tableTreeViewer.getSorter() != null) {
            ((DownloadTableTreeSorter) tableTreeViewer.getSorter()).setMaintainSortOrder(PreferenceLoader.loadBoolean("maintainSortOrder"));
        }
    }

    /**
     * @param b
     */
    public void updateClientsTable(boolean b) {
        tableTreeMenuListener.updateClientsTable(b);
    }

    /**
     * @return boolean
     */
    public boolean clientsDisplayed() {
        if (clientTableViewer != null) {
            SashForm sashForm = (SashForm) parent.getParent().getParent();

            return sashForm.getWeights()[ 1 ] != 0;
        } else {
            return false;
        }
    }

    public void toggleClientsTable() {
        if (clientTableViewer != null) {
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
            new ClientDetailDialog(treeClientInfo.getFileInfo(), treeClientInfo.getClientInfo(), mldonkey);
        }
    }
}


/*
$Log: DownloadTableTreeViewer.java,v $
Revision 1.15  2003/10/22 21:20:33  zet
static validate

Revision 1.14  2003/10/22 16:50:21  zet
validate ids

Revision 1.13  2003/10/22 01:38:31  zet
*** empty log message ***

Revision 1.12  2003/10/20 21:58:19  zet
SWT.RIGHT labels cheap workaround..

Revision 1.11  2003/10/19 21:39:32  zet
nil

Revision 1.10  2003/10/16 23:56:44  zet
not much

Revision 1.9  2003/10/16 19:58:03  zet
icons

Revision 1.8  2003/10/16 16:09:56  zet
updateDisplay

Revision 1.7  2003/10/15 22:06:32  zet
colours

Revision 1.6  2003/10/15 21:13:21  zet
show/hide clients from rtclk menu

Revision 1.5  2003/10/12 15:58:29  zet
rewrite downloads table & more..

Revision 1.4  2003/09/24 03:07:43  zet
add # of active sources column

Revision 1.3  2003/09/23 21:46:36  zet
not much

Revision 1.2  2003/09/21 23:39:31  zet
displayTableColors preference

Revision 1.1  2003/09/20 14:39:21  zet
move transfer package

Revision 1.26  2003/09/18 14:11:01  zet
revert

Revision 1.24  2003/09/16 01:19:14  zet
null

Revision 1.23  2003/09/15 22:10:32  zet
add availability %, refresh delay option

Revision 1.22  2003/08/31 00:08:59  zet
add buttons

Revision 1.21  2003/08/30 00:44:01  zet
move tabletree menu

Revision 1.20  2003/08/24 19:38:31  zet
minor

Revision 1.19  2003/08/24 16:37:04  zet
combine the preference stores

Revision 1.18  2003/08/24 02:34:16  zet
update sorter properly

Revision 1.17  2003/08/23 22:47:03  zet
remove println

Revision 1.16  2003/08/23 19:44:12  zet
split transfer table to basic/advanced modes

Revision 1.15  2003/08/23 15:21:37  zet
remove @author

Revision 1.14  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.13  2003/08/22 21:16:36  lemmster
replace $user$ with $Author: zet $

Revision 1.12  2003/08/22 13:47:56  dek
selection is removed with click on empty-row

Revision 1.11  2003/08/21 00:59:57  zet
doubleclick expand

Revision 1.10  2003/08/20 14:58:43  zet
sources clientinfo viewer

Revision 1.9  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.8  2003/08/17 16:48:08  zet
prevent rapid tree expansion from triggering double click

Revision 1.7  2003/08/17 16:32:41  zet
doubleclick

Revision 1.6  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.5  2003/08/08 20:16:13  zet
central PreferenceLoader, abstract Console

Revision 1.4  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.3  2003/08/06 19:31:06  zet
configurable cell editors

Revision 1.2  2003/08/06 17:16:09  zet
cell modifiers

Revision 1.1  2003/08/04 19:22:08  zet
trial tabletreeviewer


*/
