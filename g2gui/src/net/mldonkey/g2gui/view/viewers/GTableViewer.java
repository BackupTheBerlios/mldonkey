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
package net.mldonkey.g2gui.view.viewers;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.PreferenceStore;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;


/**
 * Generic Table Viewer
 *
 * @version $Id: GTableViewer.java,v 1.2 2003/10/22 16:39:27 zet Exp $
 *
 */
public class GTableViewer {
    protected boolean manualDispose;
    protected GTableSorter tableSorter;
    protected GTableLabelProvider tableLabelProvider;
    protected GTableContentProvider tableContentProvider;
    protected GTableMenuListener tableMenuListener;
    protected String[] columnLabels;
    protected int[] columnAlignment;
    protected int[] columnDefaultWidths;
    protected String allColumns = "";
    protected String columnIDs;
    protected String preferenceString;
    protected CustomTableViewer tableViewer;
    protected CoreCommunication core;

    public GTableViewer(Composite parent, CoreCommunication core) {
        this.core = core;
    }

    /**
     * Create the contents
     * @param parent
     */
    protected void createContents(Composite parent) {
        for (int i = 0; i < columnLabels.length; i++) {
            allColumns += String.valueOf((char) (ColumnSelector.MAGIC_NUMBER + i));
        }

        tableViewer = new CustomTableViewer(parent, SWT.FULL_SELECTION | SWT.MULTI);

        Table table = tableViewer.getTable();
        table.setLayoutData(new GridData(GridData.FILL_BOTH));
        table.setHeaderVisible(true);

        updateDisplay();
        createColumns();

        tableContentProvider.initialize();
        tableLabelProvider.initialize();
        tableSorter.initialize();
        tableMenuListener.initialize();

        tableViewer.setContentProvider(tableContentProvider);
        tableViewer.setLabelProvider(tableLabelProvider);

        MenuManager popupMenu = new MenuManager("");
        popupMenu.setRemoveAllWhenShown(true);
        popupMenu.addMenuListener(tableMenuListener);
        table.setMenu(popupMenu.createContextMenu(tableViewer.getTable()));

        tableViewer.setSorter(tableSorter);
    }

    /**
     * create the table columns
     */
    protected void createColumns() {
        Table table = tableViewer.getTable();
        TableColumn[] tableColumns = table.getColumns();

        manualDispose = true;

        for (int i = tableColumns.length - 1; i > -1; i--) {
            tableColumns[ i ].dispose();
        }

        manualDispose = false;

        String prefCols = PreferenceLoader.loadString(preferenceString + "TableColumns");
        columnIDs = (validColumnIDs(prefCols) ? prefCols : allColumns);

        final PreferenceStore p = PreferenceLoader.getPreferenceStore();
        tableViewer.setColumnIDs(columnIDs);

        for (int i = 0; i < columnIDs.length(); i++) {
            final int arrayItem = columnIDs.charAt(i) - ColumnSelector.MAGIC_NUMBER;
            final int columnIndex = i;

            TableColumn tableColumn = new TableColumn(table, columnAlignment[ arrayItem ]);
            p.setDefault(columnLabels[ arrayItem ], columnDefaultWidths[ arrayItem ]);
            tableColumn.setText(G2GuiResources.getString(columnLabels[ arrayItem ]));
            tableColumn.setWidth(p.getInt(columnLabels[ arrayItem ]));

            tableColumn.addDisposeListener(new DisposeListener() {
                    public synchronized void widgetDisposed(DisposeEvent e) {
                        if (!manualDispose) {
                            TableColumn thisColumn = (TableColumn) e.widget;
                            p.setValue(columnLabels[ arrayItem ], thisColumn.getWidth());
                        }
                    }
                });
            tableColumn.addListener(SWT.Selection,
                new Listener() {
                    public void handleEvent(Event e) {
                        tableSorter.setColumnIndex(columnIndex);
                        tableViewer.refresh();
                    }
                });
        }
    }

    /**
     * @param string
     * @return true if String contains valid columnIDs, else false
     */
    public boolean validColumnIDs(String string) {
        if (string.equals("") || (string.length() > allColumns.length())) {
            return false;
        }

        for (int i = 0; i < string.length(); i++) {
            if (allColumns.indexOf(string.charAt(i)) == -1) {
                return false;
            }
        }

        return true;
    }

    /**
     * Reset columns
     */
    public void resetColumns() {
        Object oldInput = tableViewer.getInput();
        tableViewer.setInput(null);
        tableSorter.setColumnIndex(0);
        createColumns();
        tableViewer.setInput(oldInput);
        updateDisplay();
    }

    /**
     * UpdateDisplay after preference changes
     */
    public void updateDisplay() {
        Table table = tableViewer.getTable();
        table.setLinesVisible(PreferenceLoader.loadBoolean("displayGridLines"));
    }

    protected void addMenuListener() {
        /*
         * add a menulistener to set the first item to default
         * sadly not possible with the MenuManager Class
         * (Feature Request on eclipse?)
         */
        Menu menu = tableViewer.getTable().getMenu();
        menu.addMenuListener(new MenuAdapter() {
                public void menuShown(MenuEvent e) {
                    Menu menu = tableViewer.getTable().getMenu();

                    if (!tableViewer.getSelection().isEmpty()) {
                        menu.setDefaultItem(menu.getItem(0));
                    }
                }
            });
    }

    /**
     * @return CustomTableViewer
     */
    public CustomTableViewer getTableViewer() {
        return tableViewer;
    }

    /**
     * @return CoreCommunication
     */
    public CoreCommunication getCore() {
        return core;
    }

    /**
     * @return GTableContentProvider
     */
    public GTableContentProvider getContentProvider() {
        return tableContentProvider;
    }

    /**
     * @return String of the current columnIDs
     */
    public String getColumnIDs() {
        return columnIDs;
    }

    /**
     * @return String[] of the columnLabels
     */
    public String[] getColumnLabels() {
        return columnLabels;
    }

    /**
     * @return String of allColumnIDs
     */
    public String getAllColumnIDs() {
        return allColumns;
    }

    /**
     * @return preferenceString
     */
    public String getPreferenceString() {
        return preferenceString;
    }
}


/*
$Log: GTableViewer.java,v $
Revision 1.2  2003/10/22 16:39:27  zet
validate columnIds

Revision 1.1  2003/10/22 01:36:59  zet
add column selector to server/search (might not be finished yet..)



*/
