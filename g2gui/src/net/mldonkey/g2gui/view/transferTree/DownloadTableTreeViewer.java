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
package net.mldonkey.g2gui.view.transferTree;

import java.io.IOException;
import java.util.ResourceBundle;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.view.MainTab;
import net.mldonkey.g2gui.view.TransferTab;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * DownloadTable
 *
 * @author $user$
 * @version $Id: DownloadTableTreeViewer.java,v 1.2 2003/08/06 17:16:09 zet Exp $ 
 *
 */
public class DownloadTableTreeViewer implements ICellModifier {

	private TableTreeViewer tableTreeViewer;
	private TableTree tableTree;
	private Table table;
	private Shell shell;
	public static ResourceBundle res = ResourceBundle.getBundle("g2gui");
	private DownloadTableTreeSorter downloadTableTreeSorter = new DownloadTableTreeSorter();
	private DownloadTableTreeContentProvider tableTreeContentProvider;
	private FileInfo selectedFile = null;
	private MenuManager popupMenu;
	private DownloadTableTreeMenuListener tableTreeMenuListener;
	private static boolean displayChunkGraphs;
	private CoreCommunication mldonkey;
	
	private final String[] COLUMN_LABELS =
		{	"TT_Download_Id",
			"TT_Download_Network",
			"TT_Download_Name",
			"TT_Download_Size",
			"TT_Download_Downloaded", 
			"TT_Download_%",
			"TT_Download_Sources",
			"TT_Download_Rate",
			"TT_Download_Chunks",
			"TT_Download_ETA",
			"TT_Download_Priority",	
			"TT_Download_Last",
			"TT_Download_Age"
		};
	
	private final int[] COLUMN_DEFAULT_WIDTHS =
		{ 50, 50, 250, 75, 75, 50, 50, 50, 75, 75, 50, 75, 75};

	private final int[] COLUMN_ALIGNMENT =
		{
			SWT.LEFT,
			SWT.LEFT,
			SWT.LEFT,
			SWT.RIGHT,
			SWT.RIGHT,
			SWT.RIGHT,
			SWT.RIGHT,
			SWT.RIGHT,
			SWT.LEFT,
			SWT.RIGHT,
			SWT.LEFT,
			SWT.RIGHT,
			SWT.RIGHT
		};
		
	// set to 0 to turn off table editors
	private static int CHUNKS_COLUMN = 8;
		
	/**
	 * 
	 * Creates a new Viewer inside the composite parent
	 * @param parent 
	 * @param mldonkey 
	 * @param page 
	 */
	public DownloadTableTreeViewer( Composite parent, final CoreCommunication mldonkey, TransferTab page ) 
	{
		this.shell = parent.getShell();
		this.mldonkey = mldonkey;
		displayChunkGraphs = loadBoolean("displayChunkGraphs");
				
		for (int i = 0; i < COLUMN_LABELS.length ; i++)
			MainTab.getStore().setDefault(COLUMN_LABELS[ i ], COLUMN_DEFAULT_WIDTHS[ i ]);
	
		createTableTreeViewer(parent, mldonkey);
	}
	
	public void createTableTreeViewer(Composite parent, final CoreCommunication mldonkey) {
			
		tableTreeViewer = new TableTreeViewer ( parent,  SWT.MULTI | SWT.FULL_SELECTION );
		tableTree = tableTreeViewer.getTableTree();
		table = tableTree.getTable();
		
		tableTree.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));
		
		tableTreeViewer.setColumnProperties(COLUMN_LABELS);
		table.setLinesVisible( loadBoolean("displayGridLines") );
		table.setHeaderVisible( true );
		
		CellEditor[] cellEditors = new CellEditor[COLUMN_LABELS.length];
		cellEditors[2] = new TextCellEditor(table);
		tableTreeViewer.setCellEditors(cellEditors);
		tableTreeViewer.setCellModifier(this);
				
		for (int i = 0; i < COLUMN_LABELS.length; i++) {
			TableColumn tableColumn = new TableColumn(table, COLUMN_ALIGNMENT[ i ]);
			tableColumn.setText ( res.getString( COLUMN_LABELS[ i ] )  );
			tableColumn.setWidth(MainTab.getStore().getInt(COLUMN_LABELS [ i ] ));
			MainTab.getStore().setDefault(COLUMN_LABELS [ i ] + "_Resizable", true);
			tableColumn.setResizable(MainTab.getStore().getBoolean(COLUMN_LABELS [ i ] + "_Resizable" ));
			tableColumn.setData(COLUMN_LABELS[ i ]);
						
			final int columnIndex = i;
			tableColumn.addDisposeListener(new DisposeListener() {
				public synchronized void widgetDisposed( DisposeEvent e ) {
					TableColumn thisColumn = ( TableColumn ) e.widget;
					MainTab.getStore().setValue(COLUMN_LABELS [ columnIndex ] , thisColumn.getWidth() );
					MainTab.getStore().setValue(COLUMN_LABELS [ columnIndex ] + "_Resizable" , thisColumn.getResizable() );
				}
			} );
		
			tableColumn.addListener( SWT.Selection, new Listener() {
				public void handleEvent( Event e ) {
					// duplicate to reset the sorter 
					DownloadTableTreeSorter dTTS = new DownloadTableTreeSorter();
					dTTS.setLastColumnIndex( downloadTableTreeSorter.getLastColumnIndex() );
					dTTS.setLastSort( downloadTableTreeSorter.getLastSort() );
					downloadTableTreeSorter = dTTS;
					// set the column to sort
					downloadTableTreeSorter.setColumnIndex( columnIndex );
					// close all child editors and sort
					tableTreeContentProvider.closeAllEditors();
					tableTreeViewer.setSorter( downloadTableTreeSorter );
					tableTreeContentProvider.updateAllEditors();
				}	
			} ); 
								 
		}
		
		
		
		DownloadTableTreeLabelProvider treeLabelProvider = new DownloadTableTreeLabelProvider();
		tableTreeViewer.setLabelProvider(treeLabelProvider);
		
		tableTreeContentProvider = new DownloadTableTreeContentProvider();
		tableTreeContentProvider.setUpdateBuffer( loadInteger("displayBuffer") );
		tableTreeViewer.setContentProvider(tableTreeContentProvider);
		tableTreeViewer.setUseHashlookup(true);
		
		tableTree.addTreeListener(tableTreeContentProvider);
			
		tableTreeMenuListener = new DownloadTableTreeMenuListener(tableTreeViewer, mldonkey);
		
		tableTreeViewer.addSelectionChangedListener(tableTreeMenuListener);
	
	// tableTreeViewer.addTreeListener(tableTreeContentProvider);
		
		popupMenu = new MenuManager("");
		popupMenu.setRemoveAllWhenShown(true);
		popupMenu.addMenuListener(tableTreeMenuListener);
						
		tableTree.setMenu(popupMenu.createContextMenu(tableTree));
		
		tableTreeViewer.setSorter(downloadTableTreeSorter);	
		tableTreeViewer.setInput( mldonkey.getFileInfoIntMap() );
		mldonkey.getFileInfoIntMap().addObserver( tableTreeContentProvider );
		tableTreeContentProvider.updateAllEditors();
	
				
	}
	
	public boolean canModify(Object element, String property) {
		if (element instanceof FileInfo) 
			return true;
		return false;
	}
	
	public Object getValue(Object element, String property) {
		FileInfo fileInfo = (FileInfo) element;
		return fileInfo.getName();
	}
	
	public void modify(Object element, String property, Object value) {
		
		TableTreeItem item = (TableTreeItem) element;
		FileInfo fileInfo = (FileInfo) item.getData();
		String newName = ((String) value).trim();
		if (newName.length() > 0) fileInfo.setName(newName);
			
	}
	
	
	
	public static boolean displayChunkGraphs() {
		return displayChunkGraphs;
	}
		
	public static int getChunksColumn() {
		return CHUNKS_COLUMN;
	}
	
	static boolean loadBoolean (String preferenceString ) {
				PreferenceStore preferenceStore = new PreferenceStore( "g2gui.pref" );
					try { preferenceStore.load(); } catch ( IOException e ) { }		
		
			if (preferenceStore.contains( preferenceString ))
				return preferenceStore.getBoolean( preferenceString );
			return true;
	}	
	
	static int  loadInteger(String preferenceString ) {
		PreferenceStore preferenceStore = new PreferenceStore( "g2gui.pref" );
					try { preferenceStore.load(); } catch ( IOException e ) { }		
		
			if (preferenceStore.contains( preferenceString ))
				return preferenceStore.getInt( preferenceString );
			return 1;
	}

	public void updateDisplay() {
		table.setLinesVisible( loadBoolean("displayGridLines") );
		boolean newChunkValue = loadBoolean("displayChunkGraphs");
		tableTreeContentProvider.closeAllEditors();
		tableTreeViewer.refresh();
		displayChunkGraphs = newChunkValue;
		tableTreeContentProvider.updateAllEditors();
		tableTreeContentProvider.setUpdateBuffer( loadInteger("displayBuffer") );
	}


		
}

/*
$Log: DownloadTableTreeViewer.java,v $
Revision 1.2  2003/08/06 17:16:09  zet
cell modifiers

Revision 1.1  2003/08/04 19:22:08  zet
trial tabletreeviewer


*/
