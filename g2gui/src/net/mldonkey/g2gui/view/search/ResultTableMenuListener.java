/*
 * Copyright 2003
 * G2Gui Team
 * 
 * 
 * This file is part of G2Gui.
 *
 * G2Gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2Gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2Gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.view.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.Download;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.ResultInfo;
import net.mldonkey.g2gui.model.ResultInfoIntMap;
import net.mldonkey.g2gui.view.SearchTab;
import net.mldonkey.g2gui.view.helper.TableMenuListener;
import net.mldonkey.g2gui.view.helper.WordFilter;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.actions.CopyED2KLinkToClipboardAction;
import net.mldonkey.g2gui.view.viewers.actions.WebServicesAction;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * ResultTableMenuListener
 *
 *
 * @version $Id: ResultTableMenuListener.java,v 1.23 2003/10/28 00:36:06 zet Exp $ 
 *
 */
public class ResultTableMenuListener extends TableMenuListener implements ISelectionChangedListener, IMenuListener {
	private CTabItem cTabItem;
	private ResultInfo selectedResult;
	private ResultInfoIntMap resultInfoMap;
	private List selectedResults;
	private CoreCommunication core;

	/**
	 * Creates a new TableMenuListener
	 * @param tableViewer The parent TableViewer
	 * @param core The CoreCommunication supporting this with data
	 * @param cTabItem The CTabItem in which the table res
	 */
	public ResultTableMenuListener( ResultTableViewer rTableViewer ) {
		super( rTableViewer );
		this.selectedResults = new ArrayList();
	}
	
	public void initialize() {
	    super.initialize();
	    this.cTabItem = ((ResultTableViewer) gTableViewer).getCTabItem();
	    this.core = gTableViewer.getCore();
		this.resultInfoMap = this.core.getResultInfoIntMap();
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#
	 * selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public void selectionChanged( SelectionChangedEvent event ) {
		IStructuredSelection sSel = ( IStructuredSelection ) event.getSelection();
		Object o = sSel.getFirstElement();

		if ( o instanceof ResultInfo )
			selectedResult = ( ResultInfo ) o;
		else
			selectedResult = null;
			
		selectedResults.clear();	
		for ( Iterator it = sSel.iterator(); it.hasNext();) {
			o = it.next();
			if ( o instanceof ResultInfo ) 
				selectedResults.add( ( ResultInfo ) o );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuListener#
	 * menuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */
	public void menuAboutToShow( IMenuManager menuManager ) {
		/* download */
		if ( selectedResult != null ) {
			menuManager.add( new DownloadAction() );

			menuManager.add( new Separator() );

			/* copy filename to clipboard */
			menuManager.add( new CopyNameAction() );

			/* copy filename as plain/html */
//			MenuManager copyManager =
//					new MenuManager( G2GuiResources.getString( "ST_COPYLINK" ) );
//			copyManager.add( new CopyED2KAction( false ) );
//			copyManager.add( new CopyED2KAction( true ) );
//			menuManager.add( copyManager );
			
			if (selectedResult != null) {
				String[] linkList = new String[ selectedResults.size() ];

				for (int i = 0; i < selectedResults.size(); i++) {
					linkList[ i ] = new String(((ResultInfo) selectedResults.get(i)).getLink());
				}

				MenuManager clipboardMenu = new MenuManager(G2GuiResources.getString("TT_DOWNLOAD_MENU_COPYTO"));
				clipboardMenu.add(new CopyED2KLinkToClipboardAction(false, linkList));
				clipboardMenu.add(new CopyED2KLinkToClipboardAction(true, linkList));
				menuManager.add(clipboardMenu);
			}

			
	
//			menuManager.add( new Separator() );
	
			/* webservices */
/*			MenuManager webManager =
No				new MenuManager( G2GuiResources.getString( "ST_WEBSERVICES" ) );
Fake		webManager.add( new FakeSearchAction() );
Search		webManager.add( new FakeSearchAction() );
Yet			menuManager.add( webManager );
	
			menuManager.add( new Separator() );
*/
			menuManager.add(new Separator() );
			MenuManager webServicesMenu = new MenuManager( G2GuiResources.getString( "ST_WEBSERVICES" ) );
			
				webServicesMenu.add( new WebServicesAction( WebServicesAction.BITZI, selectedResult.getMd4() ) ) ;
				webServicesMenu.add( new WebServicesAction( WebServicesAction.FILEDONKEY, selectedResult.getMd4() ) ) ;
				webServicesMenu.add( new WebServicesAction( WebServicesAction.JIGLE, selectedResult.getMd4() ) ) ;
				webServicesMenu.add( new WebServicesAction( WebServicesAction.SHAREREACTOR, selectedResult.getLink() ) );
				webServicesMenu.add( new WebServicesAction( WebServicesAction.DONKEY_FAKES, selectedResult.getLink() ) );
			
			menuManager.add( webServicesMenu );
			
		}
		
		super.menuAboutToShow( menuManager );
		
		/* filter submenu (select network to display) */			
		MenuManager filterSubMenu = new MenuManager( G2GuiResources.getString( "TML_FILTER" ) );
		AllFiltersAction aFA = new AllFiltersAction();
		boolean setChecked = true;
		for ( int i = 0; i < tableViewer.getFilters().length; i++ ) {
			if ( !( tableViewer.getFilters()[ i ] instanceof WordFilter ) ) 
				setChecked = false;
		}
		aFA.setChecked( setChecked );
		filterSubMenu.add( aFA );
		filterSubMenu.add( new Separator() );
			
		NetworkInfo[] networks = core.getNetworkInfoMap().getNetworks();
		
		for ( int i = 0; i < networks.length; i++ ) {
			NetworkInfo network = networks[ i ];
			if ( network.isEnabled() && network.isSearchable() ) {
				NetworkFilterAction nFA =
					new NetworkFilterAction( network.getNetworkName(), network.getNetworkType() );
				if ( isFiltered( network.getNetworkType() ) ) nFA.setChecked( true );
				filterSubMenu.add( nFA );
			}
		}

		filterSubMenu.add( new Separator() );
		
		menuManager.add( filterSubMenu );

		menuManager.add( new Separator() );

		/* remove search result */
		if ( selectedResult != null ) {
			menuManager.add( new RemoveAction() );

			/* close search */
			menuManager.add( new CloseAction() );
		}
	}
	
	/**
	 * downloads the selected files and creates error msgs if needed
	 */
	void downloadSelected() {				
		Download download = new Download( core );
		String anErrorString = new String();
		int counter = 0;
		
		
		for ( int i = 0; i < selectedResults.size(); i++ ) {
			ResultInfo result = ( ResultInfo ) selectedResults.get( i );
			download.setPossibleNames( result.getNames() );	
			download.setResultID( result.getResultID() );
			download.setForce( false );

			if ( result.isDownloading() && result.getHistory() ) {
				anErrorString += result.getName() + "\n";
			}
			else if ( !result.getHistory() ) {
				Shell shell = ( ( TableViewer ) tableViewer ).getTable().getShell();
				MessageBox box = new MessageBox( shell, SWT.ICON_INFORMATION | SWT.YES | SWT.NO );
				box.setText( G2GuiResources.getString( "ST_HISTORY_TEXT" ) );
				box.setMessage( G2GuiResources.getString( "ST_HISTORY_MSG" ) );
				int rc = box.open();
				if ( rc == SWT.YES ) {
					download.setForce( true );
					download.send();
					result.setDownloading( true );
					counter++;
				}
			}
			else {
				download.send();
				result.setDownloading( true );
				counter++;
			}
		}
		download = null;
		
		/* display the errors now */
		if ( !anErrorString.equals( "" ) ) {
			Shell shell = ( ( TableViewer ) tableViewer ).getTable().getShell();
			MessageBox box = new MessageBox( shell, SWT.ICON_WARNING );
			box.setText( G2GuiResources.getString( "ST_DOWNLOADED_TEXT" ) );
			box.setMessage( G2GuiResources.getString( "ST_DOWNLOADED_MSG" ) 
							+ "\n\n" + anErrorString );
			box.open();
		}
		
		/* update the statusline */
		String statusline = G2GuiResources.getString( "ST_STARTED_DOWNLOADS" ) + counter;
		SearchTab parent = ( SearchTab ) cTabItem.getParent().getData();
		parent.getMainTab().getStatusline().update( statusline );	

		/* update the downloaded tableitems */
		if ( counter > 0 )
			tableViewer.refresh();
	}
		
	private class DownloadAction extends Action {
		public DownloadAction() {
			super( G2GuiResources.getString( "ST_DOWNLOAD" ) );
			setImageDescriptor( G2GuiResources.getImageDescriptor( "DownArrowGreen" ));
		}
		public void run() {
			downloadSelected();
		}
	}
	
	private class CopyNameAction extends Action {
		public CopyNameAction() {
			super( G2GuiResources.getString( "ST_COPYNAME" ) );
			setImageDescriptor( G2GuiResources.getImageDescriptor( "copy" ));
		}
		public void run() {
			Clipboard clipboard =
				new Clipboard( ( ( TableViewer ) tableViewer ).getTable().getDisplay() );
			String aString = "";
			for ( int i = 0; i < selectedResults.size(); i++ ) {
				ResultInfo result = ( ResultInfo ) selectedResults.get( i );
				if ( aString.length() > 0 )
					aString += ( SWT.getPlatform().equals( "win32" ) ? "\r\n" : "\n" );
				aString += result.getName();
			}
			clipboard.setContents( new Object[] { aString },
								new Transfer[] { TextTransfer.getInstance() } );
			clipboard.dispose();						
		}
	}
	
	private class FakeSearchAction extends Action {
		public FakeSearchAction() {
			super();
			setText( G2GuiResources.getString( "ST_WEBSERVICE1" ) );
		}
		public void run() {
			//TODO add fake search
		}
	}
	
	private class RemoveAction extends Action {
		public RemoveAction() {
			super( G2GuiResources.getString( "ST_REMOVE" ) );
			setImageDescriptor( G2GuiResources.getImageDescriptor( "minus" ));
		}
		public void run() {
			( ( TableViewer ) tableViewer ).getTable().remove(
				 ( ( TableViewer ) tableViewer ).getTable().getSelectionIndices() );
		}
	}

	private class CloseAction extends Action {
		public CloseAction() {
			super( G2GuiResources.getString( "ST_CLOSE" ) );
			setImageDescriptor( G2GuiResources.getImageDescriptor( "cancel" ));
		}
		public void run() {
			( ( TableViewer ) tableViewer ).getTable().dispose();
			cTabItem.dispose();
		}
	}
}

/*
$Log: ResultTableMenuListener.java,v $
Revision 1.23  2003/10/28 00:36:06  zet
move columnselector into the pane

Revision 1.22  2003/10/24 21:26:11  zet
add donkey fakes web service

Revision 1.21  2003/10/22 20:38:35  zet
common actions

Revision 1.20  2003/10/22 16:28:52  zet
common actions

Revision 1.19  2003/10/22 14:38:32  dek
removed malformed UTF-8 char gcj complains about (was only in comment)

Revision 1.18  2003/10/22 01:37:45  zet
add column selector to server/search (might not be finished yet..)

Revision 1.17  2003/10/21 17:00:45  lemmster
class hierarchy for tableviewer

Revision 1.16  2003/09/23 19:57:03  lemmster
copy to... works with multiple files now

Revision 1.15  2003/09/19 15:19:14  lemmster
reworked

Revision 1.14  2003/09/18 10:39:21  lemmster
checkstyle

Revision 1.13  2003/09/17 20:07:44  lemmster
avoid NPEs in search

Revision 1.12  2003/09/16 10:29:40  lemmster
open msgbox just once [bug #909]

Revision 1.11  2003/09/15 15:32:09  lemmster
reset state of canceled downloads from search [bug #908]

Revision 1.10  2003/09/08 15:43:34  lemmster
work in progress

Revision 1.9  2003/09/08 11:54:23  lemmster
added download button

Revision 1.8  2003/08/31 12:32:04  lemmster
major changes to search

Revision 1.7  2003/08/29 00:54:42  zet
Move wordFilter public

Revision 1.6  2003/08/25 16:02:50  zet
remove duplicate code, move dblclick to menulistener

Revision 1.5  2003/08/23 15:21:37  zet
remove @author

Revision 1.4  2003/08/23 10:34:33  lemmster
isSearchable() added

Revision 1.3  2003/08/23 09:46:18  lemmster
superclass TableMenuListener added

Revision 1.2  2003/08/22 19:29:16  lemmster
additiv filters

Revision 1.1  2003/08/20 10:05:46  lemmster
MenuListener added

*/