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
import net.mldonkey.g2gui.view.G2Gui;
import net.mldonkey.g2gui.view.helper.ViewFrame;
import net.mldonkey.g2gui.view.helper.ViewFrameListener;
import net.mldonkey.g2gui.view.helper.VersionCheck;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.actions.BestFitColumnAction;
import net.mldonkey.g2gui.view.viewers.filters.AlwaysFalseGViewerFilter;
import net.mldonkey.g2gui.view.viewers.filters.GViewerFilter;
import net.mldonkey.g2gui.view.viewers.table.GTableContentProvider;
import net.mldonkey.g2gui.view.viewers.table.GTableLabelProvider;
import net.mldonkey.g2gui.view.viewers.table.GTableMenuListener;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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
 * @version $Id: GView.java,v 1.26 2004/04/14 09:49:58 dek Exp $
 *
 */
public abstract class GView {
    private BestFitColumnAction myCListener;
	private int[] columnWidths;
    
	protected boolean adjustingCols = false;
	
	protected boolean manualDispose;
    protected CoreCommunication core;
    protected String[] columnLabels;
    protected int[] columnAlignment;
    protected int[] columnDefaultWidths;
    protected String allColumns = G2Gui.emptyString;
    protected String columnIDs;
    protected String preferenceString;
    protected GSorter gSorter;
    protected GTableLabelProvider tableLabelProvider;
    protected StructuredViewer sViewer;
    protected ViewFrame viewFrame;
    protected boolean isActive;
    
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

    /**
     * @param listener
     */
    public void addDisposeListener(ViewFrameListener listener) {
    	getTable().addDisposeListener(listener);
    }
    
    public void addDynColListener( BestFitColumnAction cListener ) {
    	/* remove our old listener in any case */
    	if ( myCListener != null )
    		this.getTable().removeControlListener( myCListener );

    	/* if the new listener isn't null, add it */
    	myCListener = cListener;
    	if ( myCListener != null ) {
    		this.getTable().addControlListener( myCListener );
    		this.getTable().getHorizontalBar().setVisible(false);
    		this.getTable().getVerticalBar().setVisible(true);
    	} else {
    		this.getTable().getHorizontalBar().setVisible(true);    		
    	}
    }
    
    public int getDynColListenerIsOn() {
    	if ( myCListener != null )
    		return this.myCListener.getColumnId();
    	return -1;
    }

    
    /**
     * @param aClass
     * @return GViewerFilter
     */
    public GViewerFilter getFilter(Class aClass) {
        Map aMap = (Map) this.getViewer().getData(GViewerFilter.class.getName());

        if (aMap.containsKey(aClass.getName()))
            return (GViewerFilter) aMap.get(aClass.getName());

        return new AlwaysFalseGViewerFilter(this);
    }

    /**
     * @param aViewerFilter
     */
    public void removeFilter(ViewerFilter aViewerFilter) {
        //check if the ViewerFilter is our AlwaysFalseFilter
        if (aViewerFilter instanceof GViewerFilter) {
            GViewerFilter filter = (GViewerFilter) aViewerFilter;

            if (!filter.isNotAlwaysFalse())
                return;
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

            if (!filter.isNotAlwaysFalse())
                return;
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

                    /* set the default menue entry, this makes problems with motif
                     * if nothing in the table has been selected */
                    if (!((StructuredViewer) getViewer()).getSelection().isEmpty() &&
                    		!VersionCheck.isMotif())
                       	aMenu.setDefaultItem(aMenu.getItem(0));
                }
            });
    }

    /**
     * @param string
     * @return true if String contains valid columnIDs, else false
     */
    public static boolean validColumnIDs(String selected, String allowed) {
        if (selected.equals(G2Gui.emptyString) || (selected.length() > allowed.length()))
            return false;

        for (int i = 0; i < selected.length(); i++) {
            if (allowed.indexOf(selected.charAt(i)) == -1)
                return false;
        }

        return true;
    }

    
    /**
     * This method strips a gtk bogus column from the string if it exists. 
     * @param prefCols a columnID-String
     * @return a columnID-String without the bogus column in it
     */
    public String stripBogusColumn(String prefCols) {
    	if (prefCols.length() > 0 && 
    		(prefCols.charAt(prefCols.length()-1) == 
    		(char) (ColumnSelector.MAGIC_NUMBER + columnLabels.length))) {
    		return prefCols.substring(0, prefCols.length() - 1);
    	} else
    		return prefCols;
    }
    
    /**
     * loadColumnIDs
     */
    protected void loadColumnIDs() {
    	/* read out column-config from the prefs */
    	String prefCols = PreferenceLoader.loadString(preferenceString + "TableColumns");
    	
    	/* check if the last column is the gtk bogus column */
    	if (VersionCheck.isGtk()) {
    		char bogusColumn = (char) (ColumnSelector.MAGIC_NUMBER + columnLabels.length-1);
    		/* add the gtk bogus column if it is not present */
    		if ( prefCols.length() > 0 && bogusColumn != prefCols.charAt(prefCols.length()-1)) {
    			prefCols += bogusColumn;
    		}
		/* check for a gtk bogus column and remove it */
    	} else prefCols = stripBogusColumn(prefCols);
    	
    	/* check if all our previously saved columns are within our valid column-range */
    	if (validColumnIDs(prefCols, allColumns)) {
        	/* they are all valid and exist, use them */
            columnIDs = prefCols;
        } else {
        	/* reset: use all columns which are available */
        	columnIDs = allColumns;
            PreferenceLoader.setValue(preferenceString + "TableColumns", columnIDs);
        }
   
    }

    /**
     * This method is used to add an extra-column for GTK to improve the
     * table-behaviour consistency for the user. 
     * Blame GTK for sucking that bad ;).
     * 
     * We're simply adding a new column to the column-arrays.
     */
    protected void addBogusColumn() {
    	/* TODO: improve the way for adding the bogus GTK-column
    	 * This is really really ugly.
    	 */ 
    	String[] labelTemp = new String[columnLabels.length + 1];
    	for (int i = 0; i < columnLabels.length; i++) labelTemp[i] = columnLabels[i];
    	labelTemp[labelTemp.length - 1] = "";
    	columnLabels = (String[]) labelTemp.clone();

    	int[] alignTemp = new int[columnAlignment.length + 1];
    	for (int i = 0; i < columnAlignment.length; i++) alignTemp[i] = columnAlignment[i];
    	alignTemp[alignTemp.length - 1] = SWT.RIGHT;
    	columnAlignment = (int[]) alignTemp.clone();
    	
    	int[] widthsTemp = new int[columnDefaultWidths.length + 1];
    	for (int i = 0; i < columnDefaultWidths.length; i++) widthsTemp[i] = columnDefaultWidths[i];
    	widthsTemp[widthsTemp.length - 1] = 0;
    	columnDefaultWidths = (int[]) widthsTemp.clone();
    }
    
    /**
     * createColumns
     */
    protected void createColumns() {
    	loadColumnIDs();
    	((ICustomViewer) getViewer()).setColumnIDs(columnIDs);

        Table table = getTable();
        table.setHeaderVisible(true);

        TableColumn[] tableColumns = table.getColumns();
        
        // The SWT TableColumn DisposeEvent.widget returns a width of 0 when manually disposing
        manualDispose = true;

        for (int i = tableColumns.length - 1; i > -1; i--)
            tableColumns[ i ].dispose();

        manualDispose = false;

        for (int i = 0; i < columnIDs.length(); i++) {
            final int columnIndex = i;
            final int arrayItem;
            arrayItem = columnIDs.charAt(i) - 65;
   
            TableColumn tableColumn = new TableColumn(table, columnAlignment[ arrayItem ]);
            PreferenceLoader.setDefault(columnLabels[ arrayItem ], columnDefaultWidths[ arrayItem ]);
            tableColumn.setText(G2GuiResources.getString(columnLabels[ arrayItem ]));

            int oldWidth = PreferenceLoader.getInt(columnLabels[ arrayItem ]);
            tableColumn.setWidth((oldWidth > 0) ? oldWidth : columnDefaultWidths[ arrayItem ]);
   
            tableColumn.addDisposeListener(new DisposeListener() {
                    public synchronized void widgetDisposed(DisposeEvent e) {
                        TableColumn thisColumn = (TableColumn) e.widget;

                        if (!manualDispose && 
                        		columnLabels[ arrayItem ] != "" && 
                        		!PreferenceLoader.isRelaunching() )
                        	PreferenceLoader.setValue(columnLabels[ arrayItem ], thisColumn.getWidth());
                    }
                });

            /* add our sorter */
            tableColumn.addListener(SWT.Selection,
                new Listener() {
                    public void handleEvent(Event e) {
                    	sortByColumn(columnIndex);
                    }
                });

            /* event which is triggered on a column-resize,
             * be careful: while g2gui is starting up, its triggered very often
             */
            tableColumn.addListener(SWT.Resize,
            	new Listener() {
            	public void handleEvent(Event e) {
            		if ( !adjustingCols && getDynColListenerIsOn() != -1) 
            			setColumnWidth(getDynColListenerIsOn());
            	}
            }); 
        }
        
    }

    /**
     * Determine the number of "real" columns, which is
     * the number of all columns minus a possibly existent gtk bogus column
     * @return the number of real columns
     */
    public int getRealColumnCount() {
    	return VersionCheck.isGtk() ? 
			getTable().getColumns().length - 1 : getTable().getColumns().length; 
    }

    /**
     * @param column
     */
    public void sortByColumn(int column) {
        gSorter.setColumnIndex(column);
        refresh();
    }
    
    public void setColumnWidth( int dynColumnId ) {
    	adjustingCols = true;

    	Table table = getTable();
    	if ( !table.isDisposed() ) {
    		/* adjust the right offset to prevent the horizontal scrollbar
    		 * from appearing */
    		int totalWidth = table.getSize().x - table.getVerticalBar().getSize().x - 5;
     		if (VersionCheck.isMotif()) totalWidth -= 5;
    		
    		TableColumn[] columns = table.getColumns();
    		
    		/* only cycle through "real" columns and not our gtk bogus column */
    		for ( int i = 0; i < getRealColumnCount(); i++ ) {
    			/* make sure to not substract the dynamic column from totalWidth */ 
    			if ( i != dynColumnId )
    				totalWidth -= columns[ i ].getWidth();
    		}

    		/* make sure that columns cannot vanish */
    		if (totalWidth < 10) {
    			totalWidth = 10;
    		}
    		table.getColumn( dynColumnId ).setWidth( totalWidth );
   		
    		/* when using GTK, we have our additional right column
    		 * which will automatically expand to fit (standard GTK behaviour),
    		 * even if we assign it a size of 1.
    		 */ 
    		if (VersionCheck.isGtk())
    			table.getColumn(columns.length-1).setWidth(5);
    	
    	}
    	adjustingCols = false;
    }

    
    /**
     * createContents
     */
    protected void createContents() {
        sViewer.setData(GViewerFilter.class.getName(), new HashMap());

        /* add a bogus column for GTK to allow homogenous table-handling */
        if (VersionCheck.isGtk()) 
        	addBogusColumn();
        
        for (int i = 0; i < columnLabels.length; i++)
            allColumns += String.valueOf((char) (ColumnSelector.MAGIC_NUMBER + i));
        
        
        final Table table = getTable();
        table.setLayoutData(new GridData(GridData.FILL_BOTH));

        table.addKeyListener( new KeyAdapter() {
	            public void keyPressed( KeyEvent e ) {
	                if ( e.keyCode == SWT.ESC ) {
	                	table.deselectAll();
	                }
	            }
        	} );

        createColumns();
        updateDisplay();

        getTableContentProvider().initialize();
        getTableLabelProvider().initialize();
        gSorter.initialize();
        getTableMenuListener().initialize();

        sViewer.setContentProvider(getTableContentProvider());
        sViewer.setLabelProvider(getTableLabelProvider());

        MenuManager popupMenu = new MenuManager(G2Gui.emptyString);
        popupMenu.setRemoveAllWhenShown(true);
        popupMenu.addMenuListener(getTableMenuListener());
        table.setMenu(popupMenu.createContextMenu(getTable()));

        sViewer.setSorter(gSorter);
        //setColumnWidth(getTable().);
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

    public void setActive(boolean a) {
        isActive = a;
    }

    public boolean isActive() {
        return isActive;
    }
}


/*
$Log: GView.java,v $
Revision 1.26  2004/04/14 09:49:58  dek
best fit was _not_ stored , when unselected (no best sort anymore...) which is done now

removed some deprecated imports as well, some minor stuff

Revision 1.25  2004/03/31 20:28:58  psy
added ESC for deselection

Revision 1.24  2004/03/31 19:13:55  psy
improved dynamic column handling

Revision 1.23  2004/03/29 14:51:44  dek
some mem-improvements

Revision 1.22  2004/03/26 20:21:49  psy
do not save tablecolumns when restarting (because they are 0 on manual dispose)

Revision 1.21  2004/03/25 18:07:25  dek
profiling

Revision 1.20  2004/02/18 13:25:30  psy
hardened code for motif and chunksdisplay

Revision 1.19  2004/02/17 22:48:45  psy
removed old debug info

Revision 1.18  2004/02/05 20:44:43  psy
hopefully fixed dynamic column behaviour under gtk by introducing a
bogus column.

Revision 1.17  2003/12/17 13:06:04  lemmy
save all panelistener states correctly to the prefstore

Revision 1.16  2003/12/07 19:40:45  lemmy
[Bug #1156] Allow a certain column to be 100% by pref

Revision 1.15  2003/12/04 08:47:30  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.14  2003/12/03 22:19:11  lemmy
store g2gui.pref in ~/.g2gui/g2gui.pref instead of the program directory

Revision 1.13  2003/11/29 17:02:27  zet
more viewframes.. will continue later.

Revision 1.12  2003/11/29 01:51:53  zet
a few more viewframe changes.. will continue later.

Revision 1.11  2003/11/27 21:42:33  zet
integrate ViewFrame a little more.. more to come.

Revision 1.10  2003/11/27 16:30:38  zet
prevent column widths of 0

Revision 1.9  2003/11/24 21:13:39  zet
default viewer font

Revision 1.8  2003/11/23 17:58:03  lemmy
removed dead/unused code

Revision 1.7  2003/11/14 00:46:04  zet
sort by column menu item (for macOS)

Revision 1.6  2003/11/09 22:31:51  lemmy
fixed 'show all' bug

Revision 1.5  2003/11/07 02:24:03  zet
push sViewer into GView

Revision 1.4  2003/11/06 14:59:06  lemmy
clean up

Revision 1.3  2003/11/06 13:52:33  lemmy
filters back working

Revision 1.2  2003/11/04 21:06:35  lemmy
enclouse iteration of getFilters() to getFilter(someClass) into GView. Next step is optimisation of getFilter(someClass) in GView

Revision 1.1  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.1  2003/10/31 10:42:47  lemmy
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
