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

import java.util.ArrayList;
import java.util.Iterator;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.NetworkInfo.Enum;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.model.enum.EnumPriority;
import net.mldonkey.g2gui.view.MainTab;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * @author zet
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DownloadTableTreeMenuListener implements ISelectionChangedListener, IMenuListener {

	private FileInfo lastSelectedFile, selectedFile;
	private TreeClientInfo selectedClient;
	private ArrayList selectedClients = new ArrayList();
	private ArrayList selectedFiles = new ArrayList();
	private TableTreeViewer tableTreeViewer;
	private TableViewer clientTableViewer;
	private DownloadTableTreeContentProvider tableTreeContentProvider;
	private CoreCommunication mldonkey;
	private boolean createClientTable = false;
	
	// move these external some day
	private static String[] ExtensionNames = {
		G2GuiResources.getString("TT_DOWNLOAD_FILTER_AUDIO"), 
		G2GuiResources.getString("TT_DOWNLOAD_FILTER_VIDEO"), 
		G2GuiResources.getString("TT_DOWNLOAD_FILTER_ARCHIVE"), 
		G2GuiResources.getString("TT_DOWNLOAD_FILTER_CDIMAGE"), 
		G2GuiResources.getString("TT_DOWNLOAD_FILTER_PICTURE")
	};
	
	private static String[] AudioExtensions = {
		"aac", "ape", "au", "flac", "mpc", "mp2", "mp3", "mp4", 
		"wav", "ogg", "wma"	
	};
	
	private static String[] VideoExtensions = {
		"avi", "mpg", "mpeg", "ram", "rm", "asf", "vob",
		"divx", "vivo", "ogm", "mov", "wmv"
	};
	
	private static String[] ArchiveExtensions = {
		"gz", "zip", "ace", "rar"
	};
	
	private static String[] CDImageExtensions = {
		"ccd", "sub", "cue", "bin", "iso", "nrg", "img",
		"bwa", "bwi", "bws", "bwt", "mds", "mdf"	
	};
	
	private static String[] PictureExtensions = {
		"jpg", "jpeg", "bmp", "gif", "tif", "tiff", "png"
	};
	
	private static String[][] Extensions = {
		AudioExtensions, VideoExtensions, ArchiveExtensions,
		CDImageExtensions, PictureExtensions
	};
		
	public DownloadTableTreeMenuListener (TableTreeViewer tableTreeViewer, TableViewer clientTableViewer, CoreCommunication mldonkey) {

		this.tableTreeViewer = tableTreeViewer;
		this.clientTableViewer = clientTableViewer;
		this.mldonkey = mldonkey;
		tableTreeContentProvider = (DownloadTableTreeContentProvider) tableTreeViewer.getContentProvider();
		
	}
	
	// Interface implementers
	
	public void selectionChanged(SelectionChangedEvent e) {
		IStructuredSelection sSel = (IStructuredSelection) e.getSelection();
		Object o = sSel.getFirstElement();

		if (o instanceof FileInfo) {
			FileInfo fileInfo = (FileInfo) o;
			selectedFile = fileInfo;
			if (createClientTable && 
				(lastSelectedFile == null || lastSelectedFile != selectedFile)) {
				clientTableViewer.setInput(fileInfo);
			}
			lastSelectedFile = selectedFile;
			
		} else
			selectedFile = null;
			
		if (o instanceof TreeClientInfo) 
			selectedClient = (TreeClientInfo) o;
		else
			selectedClient = null;
		
		selectedClients.clear();
		selectedFiles.clear();	
		for (Iterator it = sSel.iterator(); it.hasNext(); ) {
			o = it.next();
			if (o instanceof FileInfo) 
				selectedFiles.add((FileInfo) o);
			else if (o instanceof TreeClientInfo)
				selectedClients.add((TreeClientInfo) o);	
		}
	}
	
	public void updateClientsTable(boolean b) {
		if (b) {
			if (createClientTable != b)
				clientTableViewer.setInput(lastSelectedFile);				
		} else {
			clientTableViewer.setInput(null);
		}
		
		createClientTable = b;
	}
	
	
	public void menuAboutToShow(IMenuManager menuManager) {
		fillContextMenu(menuManager);
	}

	// Build the menu
	
	public void fillContextMenu(IMenuManager menuManager) {

		if (selectedFile != null
			&& selectedFile.getState().getState() == EnumFileState.DOWNLOADED)
			menuManager.add(new CommitAction());

		if (selectedFile != null
			&& selectedFile.getState().getState() == EnumFileState.DOWNLOADING)
			menuManager.add(new PauseAction());

		if (selectedFile != null
			&& selectedFile.getState().getState() == EnumFileState.PAUSED)
			menuManager.add(new ResumeAction());

		if (selectedFile != null
			&& selectedFile.getState().getState() != EnumFileState.DOWNLOADED)
			menuManager.add(new CancelAction());

		if (selectedFile != null
			&& selectedFile.getState().getState() != EnumFileState.DOWNLOADED)
		{
			MenuManager prioritySubMenu =
				new MenuManager(G2GuiResources.getString("TT_DOWNLOAD_MENU_PRIORITY"));
			prioritySubMenu.add(new PriorityHighAction());
			prioritySubMenu.add(new PriorityNormalAction());
			prioritySubMenu.add(new PriorityLowAction());
			menuManager.add(prioritySubMenu);
		}
		
		if (selectedClient != null && PreferenceLoader.loadBoolean( "advancedMode" ))
			menuManager.add(new AddFriendAction());
		
		if (selectedClient != null)
			menuManager.add(new ClientDetailAction());
		
		if (selectedFile != null)
			menuManager.add(new FileDetailAction());
		

		if (selectedFile != null) {
			menuManager.add(new LinkToClipboardAction(false));
			menuManager.add(new LinkToClipboardAction(true));
		}	

		menuManager.add(new Separator());
		
		menuManager.add(new ExpandCollapseAction(true));
		menuManager.add(new ExpandCollapseAction(false));
		
		
		// columns submenu
		
		menuManager.add(new Separator());
		MenuManager columnsSubMenu = new MenuManager(G2GuiResources.getString("TT_DOWNLOAD_MENU_COLUMNS"));
		
		Table table = tableTreeViewer.getTableTree().getTable();
		for (int i = 0; i < table.getColumnCount(); i++) {
			ToggleColumnsAction tCA = new ToggleColumnsAction(i);
			if (table.getColumn(i).getResizable()) tCA.setChecked(true);
			columnsSubMenu.add(tCA);
		}
		menuManager.add(columnsSubMenu);
		
			
		// filter submenu			
		MenuManager filterSubMenu = new MenuManager(G2GuiResources.getString("TT_DOWNLOAD_MENU_FILTER"));
		AllFiltersAction aFA = new AllFiltersAction();
		if (tableTreeViewer.getFilters().length == 0) aFA.setChecked(true);
		filterSubMenu.add(aFA);
		
		filterSubMenu.add(new Separator());
		
		FileStateFilterAction fFA = new FileStateFilterAction("queued",  EnumFileState.QUEUED);
		if (!isFiltered(EnumFileState.QUEUED)) fFA.setChecked(true);
		filterSubMenu.add(fFA);		
		
		
		filterSubMenu.add(new Separator());
			
		NetworkInfo[] networks = mldonkey.getNetworkInfoMap().getNetworks();
		
		for ( int i = 0; i < networks.length; i++ ) {
			NetworkInfo network = networks[ i ];
			if (network.isEnabled()) {
				NetworkFilterAction nFA = new NetworkFilterAction(network.getNetworkName(), network.getNetworkType());
				if (isFiltered(network.getNetworkType())) nFA.setChecked(true);
				filterSubMenu.add(nFA);
			}
		}
		
		filterSubMenu.add(new Separator());
		
		for (int i = 0; i < ExtensionNames.length; i++ ) {
			String extensionName = ExtensionNames [ i ];
			ExtensionsFilterAction eFA = new ExtensionsFilterAction(extensionName, Extensions[ i ]);
			if (isFiltered(extensionName)) eFA.setChecked(true);
			filterSubMenu.add(eFA);
		}
		
		menuManager.add(filterSubMenu);

	}
		
	// Helpers
		
	public boolean isFiltered(Enum networkType) {
		ViewerFilter[] viewerFilters = tableTreeViewer.getFilters();
		for (int i = 0; i < viewerFilters.length; i++) {
			if (viewerFilters [ i ] instanceof NetworkFilter)
				if (((NetworkFilter) viewerFilters[ i ]).getNetworkType().equals(networkType))
					return true; 
		}
		return false;
	}
	
	public boolean isFiltered(EnumFileState fileState) {
		ViewerFilter[] viewerFilters = tableTreeViewer.getFilters();
		for (int i = 0; i < viewerFilters.length; i++) {
			if (viewerFilters [ i ] instanceof FileStateFilter)
				if (((FileStateFilter) viewerFilters[ i ]).getFileStateType().equals(fileState))
					return true; 
		}
		return false;
	}
	
	public boolean isFiltered(String name) {
			ViewerFilter[] viewerFilters = tableTreeViewer.getFilters();
			for (int i = 0; i < viewerFilters.length; i++) {
				if (viewerFilters [ i ] instanceof FileExtensionsFilter)
					if (((FileExtensionsFilter) viewerFilters[ i ]).getName().equals(name))
						return true; 
			}
			return false;
	}	
		
		
	public void toggleFilter(ViewerFilter viewerFilter, boolean toggle) {
		tableTreeContentProvider.closeAllEditors();
	
		if (toggle) 
			tableTreeViewer.addFilter(viewerFilter);
		else 
			tableTreeViewer.removeFilter(viewerFilter);
			
		tableTreeContentProvider.updateAllEditors();
	}
		
	// Menu Actions	
	
	class FileDetailAction extends Action {
		public FileDetailAction() {
			super();
			setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_FILE_DETAILS"));
		}
		public void run() {
			new FileDetailDialog(selectedFile);
		}
		
	}
	
	class AddFriendAction extends Action {
		public AddFriendAction() {
			super();
			setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_ADD_FRIEND"));
		}
		public void run() {
			for (int i = 0; i < selectedClients.size(); i++) {
				TreeClientInfo selectedClientInfo = (TreeClientInfo) selectedClients.get(i);
					ClientInfo.addFriend(mldonkey, selectedClientInfo.getClientInfo().getClientid());
			}
		}
	}
	
	
	class ClientDetailAction extends Action {
		public ClientDetailAction() {
			super();
			setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_CLIENT_DETAILS"));
		}
		public void run() {
			new ClientDetailDialog(selectedClient.getFileInfo(), selectedClient.getClientInfo());
		}
		
	}	
	class PauseAction extends Action {
			public PauseAction() {
				super();
				setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_PAUSE"));
			}
			public void run() {
				for (int i = 0; i < selectedFiles.size(); i++)	
					((FileInfo) selectedFiles.get(i)).setState(EnumFileState.PAUSED);
			}
	}
	
		
	class CommitAction extends Action {
		public CommitAction() {
			super();
			setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_COMMIT"));
		}
		public void run() {
			for (int i = 0; i < selectedFiles.size(); i++) {
				FileInfo selectedFileInfo = (FileInfo) selectedFiles.get(i);
				if (selectedFileInfo.getState().getState() == EnumFileState.DOWNLOADED)
					selectedFileInfo.saveFileAs( selectedFileInfo.getName() );
			}
		
		}
	}

	class ResumeAction extends Action {
		public ResumeAction() {
			super();
			setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_RESUME"));
		}
		public void run() {
			for (int i = 0; i < selectedFiles.size(); i++)	
				((FileInfo) selectedFiles.get(i)).setState(EnumFileState.DOWNLOADING);
			
		}
	}
	
	class CancelAction extends Action {
		public CancelAction() {
			super();
			setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_CANCEL"));
		}
		public void run() {
			MessageBox reallyCancel =
					new MessageBox( 
						MainTab.getShell(),
						SWT.YES | SWT.NO | SWT.ICON_QUESTION );
						
			reallyCancel.setMessage( G2GuiResources.getString( "TT_REALLY_CANCEL" ) + " (" + selectedFiles.size() + ")" );
			int answer = reallyCancel.open();
			if ( answer == SWT.YES ) {
				for (int i = 0; i < selectedFiles.size(); i++)	
					((FileInfo) selectedFiles.get(i)).setState(EnumFileState.CANCELLED);
				
			}
				
		}
	}
	
	class PriorityHighAction extends Action {
		public PriorityHighAction() {
			super(G2GuiResources.getString("TT_DOWNLOAD_MENU_PRIORITY_HIGH"), Action.AS_CHECK_BOX);
		}
		public void run() {
			for (int i = 0; i < selectedFiles.size(); i++)	
				((FileInfo) selectedFiles.get(i)).setPriority(EnumPriority.HIGH);
		}
		public boolean isChecked() {
			return (selectedFile.getPriority() == EnumPriority.HIGH);
		}
	}

	class PriorityNormalAction extends Action {

		public PriorityNormalAction() {
			super(G2GuiResources.getString("TT_DOWNLOAD_MENU_PRIORITY_NORMAL"), Action.AS_CHECK_BOX);
					}
		public void run() {
			for (int i = 0; i < selectedFiles.size(); i++)	
				((FileInfo) selectedFiles.get(i)).setPriority(EnumPriority.NORMAL);
		}
		public boolean isChecked() {
			return (selectedFile.getPriority() == EnumPriority.NORMAL);
		}
	}

	class PriorityLowAction extends Action {
		public PriorityLowAction() {
			super(G2GuiResources.getString("TT_DOWNLOAD_MENU_PRIORITY_LOW"), Action.AS_CHECK_BOX);
		}
		public void run() {
			for (int i = 0; i < selectedFiles.size(); i++)	
					((FileInfo) selectedFiles.get(i)).setPriority(EnumPriority.LOW);
		}
		public boolean isChecked() {
					return (selectedFile.getPriority() == EnumPriority.LOW);
		}
	}
	
	class ExpandCollapseAction extends Action {
		private boolean expand;
		public ExpandCollapseAction(boolean expand) {
			super();
			if (expand)
				setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_EXPANDALL"));
			else
				setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_COLLAPSEALL"));
			this.expand = expand;
		}
		public void run() {
			tableTreeContentProvider.closeAllEditors();
			if (expand) tableTreeViewer.expandAll();
			else tableTreeViewer.collapseAll();
			tableTreeContentProvider.updateAllEditors();
			
		}
	
	}
	
	class AllFiltersAction extends Action {
		public AllFiltersAction() {
			super(G2GuiResources.getString("TT_DOWNLOAD_MENU_FILTER_ALL"), Action.AS_CHECK_BOX);
		}
		public void run() {
			ViewerFilter[] viewerFilters = tableTreeViewer.getFilters();
			for (int i = 0; i < viewerFilters.length; i++) 
				toggleFilter(viewerFilters[ i ], false);
		}
	}	
		
	class NetworkFilterAction extends Action {

		public Enum networkType;

		public NetworkFilterAction(String name, Enum networkType) {
			super(name, Action.AS_CHECK_BOX);
			this.networkType = networkType;
		}
		public void run() {
			
			if (!isChecked()) {
				ViewerFilter[] viewerFilters = tableTreeViewer.getFilters();
				for (int i = 0; i < viewerFilters.length; i++) {
					if (viewerFilters[i] instanceof NetworkFilter)
						if (((NetworkFilter) viewerFilters[i]).getNetworkType() == networkType) {
							toggleFilter(viewerFilters[i], false);
						}
				}
			} else {
				toggleFilter(new NetworkFilter(networkType), true);
			}

		}
	}
	
	class FileStateFilterAction extends Action {

		public EnumFileState fileState;

		public FileStateFilterAction(String name, EnumFileState fileState) {
			super(name, Action.AS_CHECK_BOX);
			this.fileState = fileState;
		}
		public void run() {
		
			if (isChecked()) {
				ViewerFilter[] viewerFilters = tableTreeViewer.getFilters();
				for (int i = 0; i < viewerFilters.length; i++) {
					if (viewerFilters[i] instanceof FileStateFilter)
						if (((FileStateFilter) viewerFilters[i]).getFileStateType() == fileState) {
							toggleFilter(viewerFilters[i], false);
						}
				}
			} else {
				toggleFilter(new FileStateFilter(fileState), true);
			}
		}
	}
	
	
	class ExtensionsFilterAction extends Action {

		public String extensionsName;
		public String[] fileExtensions;

		public ExtensionsFilterAction(String extensionsName, String[] fileExtensions) {
			super(extensionsName, Action.AS_CHECK_BOX);
			this.extensionsName = extensionsName;
			this.fileExtensions = fileExtensions;
		}
		public void run() {
		
			if (!isChecked()) {
				ViewerFilter[] viewerFilters = tableTreeViewer.getFilters();
				for (int i = 0; i < viewerFilters.length; i++) {
					if (viewerFilters[i] instanceof FileExtensionsFilter)
						if (((FileExtensionsFilter) viewerFilters[i]).getName().equals( extensionsName )) {
							toggleFilter(viewerFilters[i], false);
						}
				}
			} else {
				toggleFilter(new FileExtensionsFilter(extensionsName, fileExtensions ), true);
			}

		}
	}

	class LinkToClipboardAction extends Action {
		
		private boolean useHTML = false;
		
		public LinkToClipboardAction(boolean useHTML) {
			super();
			this.useHTML = useHTML;
			setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_LINKTO")
				+ (useHTML ? " (html)" : ""));
		}
		public void run() {
			Clipboard clipBoard = new Clipboard( MainTab.getShell().getDisplay() );
			String link = "";
			for (int i = 0; i < selectedFiles.size(); i++) {
			 	FileInfo aFileInfo = (FileInfo) selectedFiles.get(i);
			 	if (link.length() > 0) 
					link += (SWT.getPlatform().equals("win32") ? "\r\n" : "\n");
			 	
				link += (useHTML ? "<a href=\"" : "") 
					+ "ed2k://|file|"
					+ aFileInfo.getName()
					+ "|"
					+ aFileInfo.getSize()
					+ "|"
					+ aFileInfo.getMd4()
					+ "|/" 
					+ (useHTML ? "\">" + aFileInfo.getName() + "</a>" : "");
									
			}		
			clipBoard.setContents( 
				new Object[] { link },
				new Transfer[] { TextTransfer.getInstance()} );
			clipBoard.dispose();
		}
	}	
	
	class ToggleColumnsAction extends Action {
		
		private int column;
		private Table table = tableTreeViewer.getTableTree().getTable();
		private TableColumn tableColumn;
		public ToggleColumnsAction (int column) {
			super("", Action.AS_CHECK_BOX);
			this.column = column;
			tableColumn = table.getColumn(column);
			setText(tableColumn.getText());
		}
		
		public  void run () {
			if (!isChecked()) {
				tableColumn.setWidth(0);
				tableColumn.setResizable(false);
			} else {
				tableColumn.setResizable(true);
				tableColumn.setWidth(100);
			}
		}
	
	}
	
	// Filters	
		
	public class NetworkFilter extends ViewerFilter {
		
		private Enum networkType;
		
		public NetworkFilter(Enum enum) {
			this.networkType = enum;	
		}	
		
		public Enum getNetworkType () {
			return networkType;
		}
		
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (element instanceof FileInfo) {
				FileInfo fileInfo = (FileInfo) element;
				if (fileInfo.getNetwork().getNetworkType() == networkType)
					return true;
				else 
					return false;
			}
			return true;
		}
	}
	
	public class FileStateFilter extends ViewerFilter {
		
		private EnumFileState fileState;
	
		public FileStateFilter(EnumFileState enum) {
			this.fileState = enum;	
		}	
	
		public EnumFileState getFileStateType () {
			return fileState;
		}
	
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (element instanceof FileInfo) {
				FileInfo fileInfo = (FileInfo) element;
				if ((EnumFileState) fileInfo.getState().getState() == fileState)
					return false;
				else 
					return true;
			}
			return true;
		}
	}

	
	public class FileExtensionsFilter extends ViewerFilter {

		private String[] fileExtensions;
		private String filterName;

		public FileExtensionsFilter(String filterName, String[] fileExtensions) {
			this.fileExtensions = fileExtensions;
			this.filterName = filterName;
		}

		public String getName() {
			return filterName;
		}

		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (element instanceof FileInfo) {
				FileInfo fileInfo = (FileInfo) element;
				for (int i = 0; i < fileExtensions.length; i++) {
					if (fileInfo.getName().toLowerCase().endsWith("." + fileExtensions[i]))
						return true;
				}
				return false;
			}
			return true;
		}
	}
	
}


/*
$Log: DownloadTableTreeMenuListener.java,v $
Revision 1.9  2003/08/20 14:58:43  zet
sources clientinfo viewer

Revision 1.8  2003/08/19 12:14:16  lemmster
first try of simple/advanced mode

Revision 1.7  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.6  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging

Revision 1.5  2003/08/11 00:30:10  zet
show queued files

Revision 1.4  2003/08/08 20:51:11  zet
localise strings

Revision 1.3  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.2  2003/08/06 17:14:50  zet
file details

Revision 1.1  2003/08/04 19:22:08  zet
trial tabletreeviewer


*/