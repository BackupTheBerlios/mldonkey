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
import java.util.List;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.model.enum.EnumPriority;
import net.mldonkey.g2gui.view.helper.TableMenuListener;
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

/**
 * 
 * DownloadTableTreeMenuListener
 *
 * @version $Id: DownloadTableTreeMenuListener.java,v 1.18 2003/08/23 20:33:01 zet Exp $ 
 *
 */
public class DownloadTableTreeMenuListener extends TableMenuListener implements ISelectionChangedListener, IMenuListener {

	private FileInfo lastSelectedFile, selectedFile;
	private TreeClientInfo selectedClient;
	private List selectedClients = new ArrayList();
	private List selectedFiles = new ArrayList();
	private TableViewer clientTableViewer;
	private DownloadTableTreeContentProvider tableTreeContentProvider;
	private TableTreeViewer tableTreeViewer;
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
		"gz", "zip", "ace", "rar", "tar", "tgz", "bz2"
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
		super( tableTreeViewer, mldonkey );
		this.clientTableViewer = clientTableViewer;
		this.tableTreeViewer = tableTreeViewer;
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
			
		if (o instanceof TreeClientInfo) {
			selectedClient = (TreeClientInfo) o;
		} else
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
	
	public boolean selectedFileListContains(EnumFileState e) {
		for (int i = 0; i < selectedFiles.size(); i++) 
			if ( ((FileInfo)selectedFiles.get(i)).getState().getState() == e)
				return true;
		return false;
	}
	
	public boolean selectedFileListContainsOtherThan(EnumFileState e) {
		for (int i = 0; i < selectedFiles.size(); i++) 
			if ( ((FileInfo)selectedFiles.get(i)).getState().getState() != e)
				return true;
		return false;
	}
	
	
	public void fillContextMenu(IMenuManager menuManager) {

		if (selectedFile != null
			&& selectedFileListContains(EnumFileState.DOWNLOADED))
			menuManager.add(new CommitAction());

		if (selectedFile != null)
			menuManager.add(new FileDetailAction());

		if (selectedFile != null
			&& selectedFileListContains(EnumFileState.DOWNLOADING))
			menuManager.add(new PauseAction());

		if (selectedFile != null
			&& selectedFileListContains(EnumFileState.PAUSED))
			menuManager.add(new ResumeAction());

		if (selectedFile != null
			&& selectedFileListContainsOtherThan(EnumFileState.DOWNLOADED))
			menuManager.add(new CancelAction());

		if (selectedFile != null
			&& selectedFileListContainsOtherThan(EnumFileState.DOWNLOADED))
		{
			MenuManager prioritySubMenu =
				new MenuManager(G2GuiResources.getString("TT_DOWNLOAD_MENU_PRIORITY"));
			prioritySubMenu.add(new PriorityHighAction());
			prioritySubMenu.add(new PriorityNormalAction());
			prioritySubMenu.add(new PriorityLowAction());
			menuManager.add(prioritySubMenu);
		}
		
		if ( selectedFile != null && PreferenceLoader.loadBoolean( "advancedMode") )
			menuManager.add( new VerifyChunksAction() );
		
		if (selectedClient != null && PreferenceLoader.loadBoolean( "advancedMode" ))
			menuManager.add(new AddFriendAction());
		
		if (selectedClient != null)
			menuManager.add(new ClientDetailAction());

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
		
		Table table = ( ( TableTreeViewer ) tableViewer ).getTableTree().getTable();
		for (int i = 0; i < table.getColumnCount(); i++) {
			ToggleColumnsAction tCA = new ToggleColumnsAction(i);
			if (table.getColumn(i).getResizable()) tCA.setChecked(true);
			columnsSubMenu.add(tCA);
		}
		menuManager.add(columnsSubMenu);
		
			
		// filter submenu			
		MenuManager filterSubMenu = new MenuManager(G2GuiResources.getString("TT_DOWNLOAD_MENU_FILTER"));
		AllFiltersAction aFA = new AllFiltersAction();
		if (tableViewer.getFilters().length == 0) aFA.setChecked(true);
		filterSubMenu.add(aFA);
		
		filterSubMenu.add(new Separator());
		
		FileStateFilterAction fFA = new FileStateFilterAction("queued",  EnumFileState.QUEUED);
		if (!isFiltered(EnumFileState.QUEUED)) fFA.setChecked(true);
		filterSubMenu.add(fFA);		
		
		
		filterSubMenu.add(new Separator());
			
		NetworkInfo[] networks = core.getNetworkInfoMap().getNetworks();
		
		for ( int i = 0; i < networks.length; i++ ) {
			NetworkInfo network = networks[ i ];
			if (network.isEnabled()) {
				NetworkFilterAction nFA = new NetworkFilterAction(network.getNetworkName(), network.getNetworkType());
				if (isFiltered(network.getNetworkType())) nFA.setChecked(true);
				filterSubMenu.add(nFA);
			}
		}
		
		filterSubMenu.add(new Separator());
		
		for (int i = 0; i < Extensions.length; i++ ) {
			String[] extensions = Extensions [ i ];
			String extensionName = ExtensionNames [i ];
			ExtensionsFilterAction eFA = new ExtensionsFilterAction(extensionName, Extensions[ i ]);
			if (isFiltered(extensions)) eFA.setChecked(true);
			filterSubMenu.add(eFA);
		}
		
		menuManager.add(filterSubMenu);

	}
		
	// Helpers
			
	public boolean isFiltered(EnumFileState fileState) {
		ViewerFilter[] viewerFilters = tableViewer.getFilters();
		for (int i = 0; i < viewerFilters.length; i++) {
			if (viewerFilters [ i ] instanceof FileStateFilter)
				if (((FileStateFilter) viewerFilters[ i ]).getFileStateType().equals(fileState))
					return true; 
		}
		return false;
	}
	
	
	public boolean isFiltered( String[] extensions ) {
				ViewerFilter[] viewerFilters = tableViewer.getFilters();
				for ( int i = 0; i < viewerFilters.length; i++ ) {
					if ( viewerFilters [ i ] instanceof FileExtensionsFilter ) {
						FileExtensionsFilter filter = ( FileExtensionsFilter ) viewerFilters[ i ];
						for ( int j = 0; j < filter.getFileExtensionList().size(); j++ ) {
								String[] fileExtensions = (String[]) filter.getFileExtensionList().get(j);
								if (fileExtensions.equals( extensions ) )
								return true;					
						}
					}
				}
				return false;
		}
			
	// Menu Actions
	
	class VerifyChunksAction extends Action {
		public VerifyChunksAction() {
			super();
			setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_VERIFY_CHUNKS" ) );	
		}
		public void run() {
			for (int i = 0; i < selectedFiles.size(); i++)	
				( ( FileInfo ) selectedFiles.get( i ) ).verifyChunks();
		}
	}
	
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
					ClientInfo.addFriend(core, selectedClientInfo.getClientInfo().getClientid());
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
				for (int i = 0; i < selectedFiles.size(); i++) {	
					FileInfo fileInfo = (FileInfo) selectedFiles.get(i);
					if (fileInfo.getState().getState() == EnumFileState.DOWNLOADING)
						fileInfo.setState(EnumFileState.PAUSED);
				}
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
			for (int i = 0; i < selectedFiles.size(); i++) {	
				FileInfo fileInfo = (FileInfo) selectedFiles.get(i);
				if (fileInfo.getState().getState() == EnumFileState.PAUSED)
					fileInfo.setState(EnumFileState.DOWNLOADING);
			}
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
						tableTreeViewer.getTableTree().getShell(),
						SWT.YES | SWT.NO | SWT.ICON_QUESTION );
						
			reallyCancel.setMessage( G2GuiResources.getString( "TT_REALLY_CANCEL" ) + " (" + selectedFiles.size() + ")" );
			int answer = reallyCancel.open();
			if ( answer == SWT.YES ) {
				for (int i = 0; i < selectedFiles.size(); i++) {	
				FileInfo fileInfo = (FileInfo) selectedFiles.get(i);
				if (fileInfo.getState().getState() != EnumFileState.DOWNLOADED)
					fileInfo.setState(EnumFileState.CANCELLED);
				}
				
			}
				
		}
	}
	
	class PriorityHighAction extends Action {
		public PriorityHighAction() {
			super(G2GuiResources.getString("TT_DOWNLOAD_MENU_PRIORITY_HIGH"), Action.AS_CHECK_BOX);
		}
		public void run() {
			for (int i = 0; i < selectedFiles.size(); i++) {	
				FileInfo fileInfo = (FileInfo) selectedFiles.get(i);
				if (fileInfo.getState().getState() != EnumFileState.DOWNLOADED)
					fileInfo.setPriority(EnumPriority.HIGH);
			}
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
			for (int i = 0; i < selectedFiles.size(); i++) {	
				FileInfo fileInfo = (FileInfo) selectedFiles.get(i);
				if (fileInfo.getState().getState() != EnumFileState.DOWNLOADED)
					fileInfo.setPriority(EnumPriority.NORMAL);
			}
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
			for (int i = 0; i < selectedFiles.size(); i++) {	
				FileInfo fileInfo = (FileInfo) selectedFiles.get(i);
				if (fileInfo.getState().getState() != EnumFileState.DOWNLOADED)
					fileInfo.setPriority(EnumPriority.LOW);
			}
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
		//	tableTreeContentProvider.closeAllEditors();
			if (expand) ( ( TableTreeViewer ) tableViewer ).expandAll();
			else ( ( TableTreeViewer ) tableViewer ).collapseAll();
			tableTreeContentProvider.updateAllEditors();
			
		}
	
	}
	
	private class NetworkFilterAction extends Action {
		private NetworkInfo.Enum networkType;

		public NetworkFilterAction( String name, NetworkInfo.Enum networkType ) {
			super(name, Action.AS_CHECK_BOX);
			this.networkType = networkType;
		}
		public void run() {
			if ( !isChecked() ) {
				ViewerFilter[] viewerFilters = tableViewer.getFilters();
				for ( int i = 0; i < viewerFilters.length; i++ ) {
					if ( viewerFilters[i] instanceof NetworkFilter ) {
						NetworkFilter filter = ( NetworkFilter ) viewerFilters[ i ];
						for ( int j = 0; j < filter.getNetworkType().size(); j++ ) {
							if ( filter.getNetworkType().get( j ) == networkType )
								if ( filter.getNetworkType().size() == 1 )
									toggleFilter( viewerFilters[ i ], false );
								else {
									filter.remove( networkType );
									tableViewer.refresh();
									tableTreeContentProvider.updateAllEditors();
								}									
						}	
					}
				}
			}
			else {
				ViewerFilter[] viewerFilters = tableViewer.getFilters();
				for ( int i = 0; i < viewerFilters.length; i++ ) {
					if ( viewerFilters[i] instanceof NetworkFilter ) {
						NetworkFilter filter = ( NetworkFilter ) viewerFilters[ i ];
						filter.add( networkType );
						tableViewer.refresh();
						tableTreeContentProvider.updateAllEditors();
						return;
					}
				}
				NetworkFilter filter = new NetworkFilter();
				filter.add( networkType );
				toggleFilter( filter, true );
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
				ViewerFilter[] viewerFilters = tableViewer.getFilters();
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
	private class ExtensionsFilterAction extends Action {
		public String[] extensions;
		
		public ExtensionsFilterAction( String name, String[] extensions ) {
			super(name, Action.AS_CHECK_BOX);
			this.extensions = extensions;
			
		}
		public void run() {
			if ( !isChecked() ) {
				ViewerFilter[] viewerFilters = tableViewer.getFilters();
				for ( int i = 0; i < viewerFilters.length; i++ ) {
					if ( viewerFilters[i] instanceof FileExtensionsFilter ) {
						FileExtensionsFilter filter = ( FileExtensionsFilter ) viewerFilters[ i ];
						for ( int j = 0; j < filter.getFileExtensionList().size(); j++ ) {
							String[] fileExtensions = (String[]) filter.getFileExtensionList().get( j );
							if ( fileExtensions.equals( extensions )  ) {
								if (filter.getFileExtensionList().size() == 1) {
									toggleFilter( viewerFilters[ i ], false );
								} else {
									filter.remove( fileExtensions );
									tableViewer.refresh();
									tableTreeContentProvider.updateAllEditors();
								}									
							}
						}	
					}
				}
			}
			else {
				ViewerFilter[] viewerFilters = tableViewer.getFilters();
				for ( int i = 0; i < viewerFilters.length; i++ ) {
					if ( viewerFilters[i] instanceof FileExtensionsFilter ) {
						FileExtensionsFilter filter = ( FileExtensionsFilter ) viewerFilters[ i ];
						filter.add( extensions );
						tableViewer.refresh();
						tableTreeContentProvider.updateAllEditors();
						return;
					}
				}
				FileExtensionsFilter filter = new FileExtensionsFilter();
				filter.add( extensions );
				toggleFilter( filter, true );
				tableTreeContentProvider.updateAllEditors();
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
			Clipboard clipBoard = new Clipboard( tableTreeViewer.getTableTree().getDisplay() );
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
	
	// Filters	
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
	public static class FileExtensionsFilter extends ViewerFilter {
		private List fileExtensionsList;

		public FileExtensionsFilter() {
			this.fileExtensionsList = new ArrayList();
		}

		public List getFileExtensionList() {
			return fileExtensionsList;
		}

		public boolean add(String[] extensions) {
			return this.fileExtensionsList.add(extensions);
		}

		public boolean remove(String[] extensions) {
			return this.fileExtensionsList.remove(extensions);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public boolean select(
			Viewer viewer,
			Object parentElement,
			Object element) {
			if (element instanceof FileInfo) {
				FileInfo fileInfo = (FileInfo) element;
				for (int i = 0; i < this.fileExtensionsList.size(); i++) {
					String[] fileExtensions = (String[]) fileExtensionsList.get(i);
					for (int j = 0; j < fileExtensions.length; j++) {
						if (fileInfo.getName().toLowerCase().endsWith("." + fileExtensions[j]))
							return true;
					}	
				}
				return false;
			}
			return true;
		}
	}
		
}


/*
$Log: DownloadTableTreeMenuListener.java,v $
Revision 1.18  2003/08/23 20:33:01  zet
multi select actions

Revision 1.17  2003/08/23 15:21:37  zet
remove @author

Revision 1.16  2003/08/23 15:13:00  zet
remove reference to static MainTab methods

Revision 1.15  2003/08/23 09:46:18  lemmster
superclass TableMenuListener added

Revision 1.14  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.13  2003/08/22 21:16:36  lemmster
replace $user$ with $Author: zet $

Revision 1.12  2003/08/22 14:30:45  lemmster
verify chunks added

Revision 1.11  2003/08/21 00:59:57  zet
doubleclick expand

Revision 1.10  2003/08/20 22:18:56  zet
Viewer updates

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