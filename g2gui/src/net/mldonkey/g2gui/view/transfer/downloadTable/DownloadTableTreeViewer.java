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
import net.mldonkey.g2gui.view.viewers.GPage;
import net.mldonkey.g2gui.view.viewers.tableTree.GTableTreePage;

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
 * @version $Id: DownloadTableTreeViewer.java,v 1.19 2003/10/31 15:16:38 zet Exp $
 *
 */
public class DownloadTableTreeViewer extends GTableTreePage implements ICellModifier,
    IDoubleClickListener {
    public static final String ALL_COLUMNS = "ABCDEFGHIJKLMN";
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
    private GPage gPage;
    private Composite parent;

    /**
     * Creates a new Viewer inside the composite parent
     * @param parent
     * @param mldonkey
     * @param page
     */
    public DownloadTableTreeViewer(Composite parent, GPage clientTableViewer,
        final CoreCommunication core, TransferTab page) {
        super(parent, core);
        this.gPage = clientTableViewer;
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
        tableTreeMenuListener = new DownloadTableTreeMenuListener(this, clientTableViewer);

        advancedMode = PreferenceLoader.loadBoolean("advancedMode");

        // createTableTreeViewer(parent, core);
        createContents(parent);
    }

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

    public void loadColumnIDs() {
        if (advancedMode) {
            super.loadColumnIDs();
        } else {
            columnIDs = BASIC_COLUMNS;
        }
    }

    /**
     * Create columns
     */
    public void createColumns() {
		tableTreeViewer = getTableTreeViewer();
        
        if (advancedMode) {
            loadColumnIDs();
        } else {
            columnIDs = BASIC_COLUMNS;
        }
        
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

        super.createColumns();
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
        if (gPage != null) {
            SashForm sashForm = (SashForm) parent.getParent().getParent();

            return sashForm.getWeights()[ 1 ] != 0;
        } else {
            return false;
        }
    }

    public void toggleClientsTable() {
        if (gPage != null) {
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
$Log: DownloadTableTreeViewer.java,v $
Revision 1.19  2003/10/31 15:16:38  zet
use a local

Revision 1.18  2003/10/31 10:42:47  lemmster
Renamed GViewer, GTableViewer and GTableTreeViewer to GPage... to avoid mix-ups with StructuredViewer...
Removed IGViewer because our abstract class GPage do the job
Use supertype/interface where possible to keep the design flexible!

Revision 1.17  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.16  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

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
