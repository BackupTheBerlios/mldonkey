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

import java.util.HashMap;
import java.util.Map;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.helper.ViewFrame;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.filters.AlwaysFalseGViewerFilter;
import net.mldonkey.g2gui.view.viewers.filters.GViewerFilter;
import net.mldonkey.g2gui.view.viewers.table.GTableContentProvider;
import net.mldonkey.g2gui.view.viewers.table.GTableLabelProvider;
import net.mldonkey.g2gui.view.viewers.table.GTableMenuListener;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;


/**
 * GViewer - partial implementation of IGViewer
 *
 * @version $Id: GView.java,v 1.11 2003/11/27 21:42:33 zet Exp $
 *
 */
public abstract class GView {
    protected boolean manualDispose;
    protected CoreCommunication core;
    protected String[] columnLabels;
    protected int[] columnAlignment;
    protected int[] columnDefaultWidths;
    protected String allColumns = "";
    protected String columnIDs;
    protected String preferenceString;
    protected GSorter gSorter;
    protected GTableLabelProvider tableLabelProvider;
    protected StructuredViewer sViewer;
    protected ViewFrame viewFrame;

    public abstract GTableContentProvider getTableContentProvider();

    public abstract GTableMenuListener getTableMenuListener();

    public abstract Table getTable();

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.IGViewer#getAllColumnIDs()
     */
    public String getAllColumnIDs() {
        return allColumns;
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.IGViewer#getColumnIDs()
     */
    public String getColumnIDs() {
        return columnIDs;
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.IGViewer#getColumnLabels()
     */
    public String[] getColumnLabels() {
        return columnLabels;
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.IGViewer#getPreferenceString()
     */
    public String getPreferenceString() {
        return preferenceString;
    }

    /**
     * @return GTableLabelProvider
     */
    public GTableLabelProvider getTableLabelProvider() {
        return tableLabelProvider;
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.IGViewer#getCore()
     */
    public CoreCommunication getCore() {
        return core;
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.IGViewer#addDisposeListener(net.mldonkey.g2gui.view.viewers.GPaneListener)
     */
    public void addDisposeListener(GPaneListener listener) {
        getTable().addDisposeListener(listener);
    }

    /**
     * @param aClass
     * @return GViewerFilter
     */
    public GViewerFilter getFilter(Class aClass) {
        Map aMap = (Map) this.getViewer().getData(GViewerFilter.class.getName());

        if (aMap.containsKey(aClass.getName())) {
            return (GViewerFilter) aMap.get(aClass.getName());
        }

        return new AlwaysFalseGViewerFilter(this);
    }

    /**
     * @param aViewerFilter
     */
    public void removeFilter(ViewerFilter aViewerFilter) {
        //check if the ViewerFilter is our AlwaysFalseFilter
        if (aViewerFilter instanceof GViewerFilter) {
            GViewerFilter filter = (GViewerFilter) aViewerFilter;

            if (!filter.isNotAlwaysFalse()) {
                return;
            }
        }

        Map aMap = (Map) getViewer().getData(GViewerFilter.class.getName());
        aMap.remove(aViewerFilter);
        sViewer.removeFilter(aViewerFilter);
    }

    /**
     * @param aViewerFilter
     */
    public void addFilter(ViewerFilter aViewerFilter) {
        if (aViewerFilter instanceof GViewerFilter) {
            GViewerFilter filter = (GViewerFilter) aViewerFilter;

            if (!filter.isNotAlwaysFalse()) {
                return;
            }
        }

        Map aMap = (Map) getViewer().getData(GViewerFilter.class.getName());
        aMap.put(aViewerFilter.getClass().getName(), aViewerFilter);
        sViewer.addFilter(aViewerFilter);
    }

    /**
     * updateDisplay (after preference changes)
     */
    public void updateDisplay() {
        getTable().setLinesVisible(PreferenceLoader.loadBoolean("displayGridLines"));
		getTable().setFont(PreferenceLoader.loadFont("viewerFontData"));
    }

    /**
     * add a menulistener to set the first item to default
     * sadly not possible with the MenuManager Class
     * (Feature Request on eclipse?)
     */
    protected void addMenuListener() {
        Menu menu = getTable().getMenu();
        menu.addMenuListener(new MenuAdapter() {
                public void menuShown(MenuEvent e) {
                    Menu aMenu = getTable().getMenu();

                    if (!((StructuredViewer) getViewer()).getSelection().isEmpty()) {
                        aMenu.setDefaultItem(aMenu.getItem(0));
                    }
                }
            });
    }

    /**
     * @param string
     * @return true if String contains valid columnIDs, else false
     */
    public static boolean validColumnIDs(String selected, String allowed) {
        if (selected.equals("") || (selected.length() > allowed.length())) {
            return false;
        }

        for (int i = 0; i < selected.length(); i++) {
            if (allowed.indexOf(selected.charAt(i)) == -1) {
                return false;
            }
        }

        return true;
    }

    /**
     * loadColumnIDs
     */
    protected void loadColumnIDs() {
        String prefCols = PreferenceLoader.loadString(preferenceString + "TableColumns");

        if (validColumnIDs(prefCols, allColumns)) {
            columnIDs = prefCols;
        } else {
            columnIDs = allColumns;

            PreferenceStore p = PreferenceLoader.getPreferenceStore();
            p.setValue(preferenceString + "TableColumns", columnIDs);
        }
    }

    /**
     * createColumns
     */
    protected void createColumns() {
        loadColumnIDs();
        ((ICustomViewer) getViewer()).setColumnIDs(columnIDs);

        final PreferenceStore p = PreferenceLoader.getPreferenceStore();

        Table table = getTable();
        table.setHeaderVisible(true);

        TableColumn[] tableColumns = table.getColumns();

        // The SWT TableColumn DisposeEvent.widget returns a width of 0 when manually disposing
        manualDispose = true;

        for (int i = tableColumns.length - 1; i > -1; i--) {
            tableColumns[ i ].dispose();
        }

        manualDispose = false;

        for (int i = 0; i < columnIDs.length(); i++) {
            final int columnIndex = i;
            final int arrayItem = columnIDs.charAt(i) - 65;

            TableColumn tableColumn = new TableColumn(table, columnAlignment[ arrayItem ]);
            p.setDefault(columnLabels[ arrayItem ], columnDefaultWidths[ arrayItem ]);
            tableColumn.setText(G2GuiResources.getString(columnLabels[ arrayItem ]));
            int oldWidth = p.getInt(columnLabels[ arrayItem ]);
            tableColumn.setWidth(oldWidth > 0 ? oldWidth : columnDefaultWidths[ arrayItem ] );

            tableColumn.addDisposeListener(new DisposeListener() {
                    public synchronized void widgetDisposed(DisposeEvent e) {
                        TableColumn thisColumn = (TableColumn) e.widget;

                        if (!manualDispose) {
                            p.setValue(columnLabels[ arrayItem ], thisColumn.getWidth());
                        }
                    }
                });

            tableColumn.addListener(SWT.Selection,
                new Listener() {
                    public void handleEvent(Event e) {
                        sortByColumn(columnIndex);
                    }
                });
        }
    }

    /**
     * @param column
     */
    public void sortByColumn(int column) {
		gSorter.setColumnIndex(column);
		refresh();
    }
    
    /**
     * createContents
     */
    protected void createContents() {
        sViewer.setData(GViewerFilter.class.getName(), new HashMap());

        for (int i = 0; i < columnLabels.length; i++) {
            allColumns += String.valueOf((char) (ColumnSelector.MAGIC_NUMBER + i));
        }

        Table table = getTable();
        table.setLayoutData(new GridData(GridData.FILL_BOTH));

        createColumns();
        updateDisplay();

        getTableContentProvider().initialize();
        getTableLabelProvider().initialize();
        gSorter.initialize();
        getTableMenuListener().initialize();

        sViewer.setContentProvider(getTableContentProvider());
        sViewer.setLabelProvider(getTableLabelProvider());

        MenuManager popupMenu = new MenuManager("");
        popupMenu.setRemoveAllWhenShown(true);
        popupMenu.addMenuListener(getTableMenuListener());
        table.setMenu(popupMenu.createContextMenu(getTable()));

        sViewer.setSorter(gSorter);
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.viewers.IGViewer#resetColumns()
     */
    public void resetColumns() {
        ICustomViewer cViewer = (ICustomViewer) getViewer();

        Object o = cViewer.getInput();
        cViewer.setInput(null);
        cViewer.setEditors(false);
        gSorter.setColumnIndex(0);
        cViewer.closeAllTTE();
        createColumns();
        cViewer.setInput(o);
        updateDisplay();
    }

    /**
     * Discards this viewer's filters and triggers refiltering and resorting
     * of the elements.
     */
    public void resetFilters() {
        this.sViewer.resetFilters();
		Map aMap = (Map) getViewer().getData(GViewerFilter.class.getName());
		aMap.clear();
    }

    /**
     * @return ViewerFilter[]
     */
    public ViewerFilter[] getFilters() {
        return sViewer.getFilters();
    }

    /**
     * @return StructuredViewer
     */
    public StructuredViewer getViewer() {
        return this.sViewer;
    }

    /**
     * @return Shell
     */
    public Shell getShell() {
        return getTable().getShell();
    }

    /**
     * @return ViewFrame
     */
    public ViewFrame getViewFrame() {
        return viewFrame;
    }
    
    /**
     * StructuredViewer#refresh
     */
    public void refresh() {
        this.sViewer.refresh();
    }
}


/*
$Log: GView.java,v $
Revision 1.11  2003/11/27 21:42:33  zet
integrate ViewFrame a little more.. more to come.

Revision 1.10  2003/11/27 16:30:38  zet
prevent column widths of 0

Revision 1.9  2003/11/24 21:13:39  zet
default viewer font

Revision 1.8  2003/11/23 17:58:03  lemmster
removed dead/unused code

Revision 1.7  2003/11/14 00:46:04  zet
sort by column menu item (for macOS)

Revision 1.6  2003/11/09 22:31:51  lemmster
fixed 'show all' bug

Revision 1.5  2003/11/07 02:24:03  zet
push sViewer into GView

Revision 1.4  2003/11/06 14:59:06  lemmster
clean up

Revision 1.3  2003/11/06 13:52:33  lemmster
filters back working

Revision 1.2  2003/11/04 21:06:35  lemmster
enclouse iteration of getFilters() to getFilter(someClass) into GView. Next step is optimisation of getFilter(someClass) in GView

Revision 1.1  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.1  2003/10/31 10:42:47  lemmster
Renamed GViewer, GTableViewer and GTableTreeViewer to GPage... to avoid mix-ups with StructuredViewer...
Removed IGViewer because our abstract class GPage do the job
Use supertype/interface where possible to keep the design flexible!

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

*/
