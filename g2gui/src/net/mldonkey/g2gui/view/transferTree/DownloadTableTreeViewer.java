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

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.view.MainTab;
import net.mldonkey.g2gui.view.TransferTab;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * DownloadTable
 *
 *
 * @version $Id: DownloadTableTreeViewer.java,v 1.18 2003/08/24 02:34:16 zet Exp $ 
 *
 */
public class DownloadTableTreeViewer implements ICellModifier {

	private CustomTableTreeViewer tableTreeViewer;
	private TableTree tableTree;
	private Table table;
	private Shell shell;
	private DownloadTableTreeSorter downloadTableTreeSorter;
	private DownloadTableTreeContentProvider tableTreeContentProvider;
	private FileInfo selectedFile = null;
	private MenuManager popupMenu;
	private DownloadTableTreeMenuListener tableTreeMenuListener;
	private static boolean displayChunkGraphs = false;
	private boolean advancedMode = false;
	private CoreCommunication mldonkey;
	private CellEditor[] cellEditors;
	private TableViewer clientTableViewer;
	
	private String[] COLUMN_LABELS;
	private final String[] COLUMN_LABELS_ADVANCED =
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
	private final String[] COLUMN_LABELS_BASIC =
		{	"TT_Download_Id",
			"TT_Download_Network",
			"TT_Download_Name",
			"TT_Download_Size",
			"TT_Download_%",
			"TT_Download_Rate",
			"TT_Download_ETA",
		};

	private int[] COLUMN_DEFAULT_WIDTHS;
	private final int[] COLUMN_DEFAULT_WIDTHS_ADVANCED =
		{ 50, 50, 250, 75, 75, 50, 50, 50, 75, 75, 50, 75, 75};
	private final int[] COLUMN_DEFAULT_WIDTHS_BASIC =
		{ 50, 50, 250, 75, 50, 75, 75 };	

	private int[] COLUMN_ALIGNMENT;
	private final int[] COLUMN_ALIGNMENT_ADVANCED =
		{
			SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, 
			SWT.RIGHT, SWT.LEFT, SWT.RIGHT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT
		};
		
	private final int[] COLUMN_ALIGNMENT_BASIC =
		{
			SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.RIGHT
		};	
		
	private static int CHUNKS_COLUMN = 8;
		
	/**
	 * 
	 * Creates a new Viewer inside the composite parent
	 * @param parent 
	 * @param mldonkey 
	 * @param page 
	 */
	public DownloadTableTreeViewer( Composite parent, TableViewer clientTableViewer, final CoreCommunication mldonkey, TransferTab page ) 
	{
		this.clientTableViewer = clientTableViewer;
		this.shell = parent.getShell();
		this.mldonkey = mldonkey;
		if (PreferenceLoader.loadBoolean("advancedMode")) {
			advancedMode = true;
			COLUMN_LABELS = COLUMN_LABELS_ADVANCED;
			COLUMN_DEFAULT_WIDTHS = COLUMN_DEFAULT_WIDTHS_ADVANCED;
			COLUMN_ALIGNMENT = COLUMN_ALIGNMENT_ADVANCED;
			displayChunkGraphs = PreferenceLoader.loadBoolean("displayChunkGraphs");
		} else {
			COLUMN_LABELS = COLUMN_LABELS_BASIC;
			COLUMN_DEFAULT_WIDTHS = COLUMN_DEFAULT_WIDTHS_BASIC;
			COLUMN_ALIGNMENT = COLUMN_ALIGNMENT_BASIC;
		}
		createTableTreeViewer(parent, mldonkey);
	}
	
	public void createTableTreeViewer(Composite parent, final CoreCommunication mldonkey) {
			
		tableTreeViewer = new CustomTableTreeViewer ( parent,  SWT.MULTI | SWT.FULL_SELECTION );
		tableTree = tableTreeViewer.getTableTree();
		table = tableTree.getTable();
		
		tableTree.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));
		
		tableTreeViewer.setColumnProperties(COLUMN_LABELS);
		table.setLinesVisible( PreferenceLoader.loadBoolean("displayGridLines") );
		table.setHeaderVisible( true );
		
		cellEditors = new CellEditor[COLUMN_LABELS.length];
		cellEditors[2] = new TextCellEditor(table);
				
		for (int i = 0; i < COLUMN_LABELS.length; i++) {
			TableColumn tableColumn = new TableColumn(table, COLUMN_ALIGNMENT[ i ]);
			MainTab.getStore().setDefault(COLUMN_LABELS[ i ], COLUMN_DEFAULT_WIDTHS[ i ]);
			tableColumn.setText ( G2GuiResources.getString( COLUMN_LABELS[ i ] )  );
			tableColumn.setWidth(MainTab.getStore().getInt(COLUMN_LABELS [ i ] ));
			if (MainTab.getStore().getDefaultInt(COLUMN_LABELS[ i ]) <= 1) {
				MainTab.getStore().setDefault(COLUMN_LABELS [ i ] + "_Resizable", false);
			} else {
				MainTab.getStore().setDefault(COLUMN_LABELS [ i ] + "_Resizable", true);
			}
			tableColumn.setData(COLUMN_LABELS[ i ]);
			tableColumn.setResizable(MainTab.getStore().getBoolean(COLUMN_LABELS [ i ] + "_Resizable"));
						
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
					downloadTableTreeSorter.setColumnIndex( columnIndex );
					tableTreeViewer.refresh();
					tableTreeContentProvider.updateAllEditors();
				}	
			} ); 
								 
		}
		
		/*this is to delete the selection, if one clicks in an empty row of the table*/
		tableTreeViewer.getTableTree().getTable().addMouseListener( new MouseListener() {
			public void mouseDoubleClick( MouseEvent e ) { }
			
			public void mouseDown( MouseEvent e ) {
				Table table = ( Table ) e.widget;
				TableItem item = table.getItem( new Point( e.x, e.y ) );	
				if ( item == null )
					table.setSelection( new int[ 0 ] );		
			}
			public void mouseUp( MouseEvent e ) { }
		} );
		/*this is to expand/collapse the selected item on double-click*/
		tableTreeViewer.addDoubleClickListener(new IDoubleClickListener(){
			public void doubleClick(DoubleClickEvent e) {
			
				IStructuredSelection sSel = (IStructuredSelection) e.getSelection();
				Object o = sSel.getFirstElement();
				if (o instanceof FileInfo) {
					FileInfo fileInfo = (FileInfo) o;
					if (tableTreeViewer.getExpandedState(fileInfo)) 
						tableTreeViewer.collapseToLevel(fileInfo, AbstractTreeViewer.ALL_LEVELS);
					else 
						tableTreeViewer.expandToLevel(fileInfo,AbstractTreeViewer.ALL_LEVELS);	
					tableTreeContentProvider.updateAllEditors();		
				
				} else if (o instanceof TreeClientInfo) {
					TreeClientInfo treeClientInfo = (TreeClientInfo) o;
					new ClientDetailDialog(treeClientInfo.getFileInfo(), treeClientInfo.getClientInfo());
				}
			}
		});
		DownloadTableTreeLabelProvider treeLabelProvider;
		
		if (PreferenceLoader.loadBoolean("advancedMode")) {
			treeLabelProvider = new DownloadTableTreeLabelProviderAdvanced();
		} else {
			treeLabelProvider = new DownloadTableTreeLabelProviderBasic();
		}
		treeLabelProvider.setTableTreeViewer(tableTreeViewer);
		tableTreeViewer.setLabelProvider(treeLabelProvider);
		
		tableTreeContentProvider = new DownloadTableTreeContentProvider();
		tableTreeContentProvider.setDownloadTableTreeViewer(this);
		tableTreeViewer.setContentProvider(tableTreeContentProvider);
		tableTreeViewer.setUseHashlookup(true);
		
		tableTree.addTreeListener(tableTreeContentProvider);
			
		tableTreeMenuListener = new DownloadTableTreeMenuListener(tableTreeViewer, clientTableViewer, mldonkey);
		
		tableTreeViewer.addSelectionChangedListener(tableTreeMenuListener);
	
	// tableTreeViewer.addTreeListener(tableTreeContentProvider);
		
		popupMenu = new MenuManager("");
		popupMenu.setRemoveAllWhenShown(true);
		popupMenu.addMenuListener(tableTreeMenuListener);
						
		tableTree.setMenu(popupMenu.createContextMenu(tableTree));
		
		if (advancedMode) {
			downloadTableTreeSorter = new DownloadTableTreeSorterAdvanced();
		} else {
			downloadTableTreeSorter = new DownloadTableTreeSorterBasic();
		}
		
		downloadTableTreeSorter.setMaintainSortOrder( PreferenceLoader.loadBoolean("maintainSortOrder"));
		tableTreeViewer.setSorter(downloadTableTreeSorter);	
		tableTreeViewer.setInput( mldonkey.getFileInfoIntMap() );
		mldonkey.getFileInfoIntMap().addObserver( tableTreeContentProvider );
		tableTreeContentProvider.updateAllEditors();
		
		if (PreferenceLoader.loadBoolean("tableCellEditors")) {
			tableTreeViewer.setCellEditors(cellEditors);
			tableTreeViewer.setCellModifier(this);
		}		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
	 */
	public boolean canModify(Object element, String property) {
		if (element instanceof FileInfo) 
			return true;
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
		if (newName.length() > 0) fileInfo.setName(newName);
			
	}
	public static boolean displayChunkGraphs() {
		return displayChunkGraphs;
	}
		
	public static int getChunksColumn() {
		return CHUNKS_COLUMN;
	}
	public void updateDisplay() {
		table.setLinesVisible( PreferenceLoader.loadBoolean("displayGridLines") );
		
		if (PreferenceLoader.loadBoolean("tableCellEditors")) {
			tableTreeViewer.setCellEditors(cellEditors);
			tableTreeViewer.setCellModifier(this);
		} else {
			tableTreeViewer.setCellEditors(null);
			tableTreeViewer.setCellModifier(null);		
		}
		
		tableTreeContentProvider.closeAllEditors();
		if (advancedMode) {
			displayChunkGraphs = PreferenceLoader.loadBoolean("displayChunkGraphs");
		}
		tableTreeViewer.refresh();
		tableTreeContentProvider.updateAllEditors();
		if (tableTreeViewer.getSorter() != null)
			((DownloadTableTreeSorter) tableTreeViewer.getSorter()).setMaintainSortOrder( PreferenceLoader.loadBoolean("maintainSortOrder"));
	
	}

	public void updateClientsTable(boolean b) {
		tableTreeMenuListener.updateClientsTable(b);
	}
}

/*
$Log: DownloadTableTreeViewer.java,v $
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
