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
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * ResultTableMenuListener
 *
 * @author $Author: lemmster $
 * @version $Id: ResultTableMenuListener.java,v 1.1 2003/08/20 10:05:46 lemmster Exp $ 
 *
 */
public class ResultTableMenuListener implements ISelectionChangedListener, IMenuListener {

	private CTabItem cTabItem;

	private ResultInfo selectedResult;

	private ResultInfoIntMap resultInfoMap;

	private List selectedResults;

	private ResultTableContentProvider tableContentProvider;

	private CoreCommunication core;

	private TableViewer tableViewer;
	
	private Clipboard clipboard;

	/**
	 * Creates a new TableMenuListener
	 * @param tableViewer The parent TableViewer
	 * @param core The CoreCommunication supporting this with data
	 * @param cTabItem The CTabItem in which the table res
	 */
	public ResultTableMenuListener( TableViewer tableViewer, CoreCommunication core, CTabItem cTabItem ) {
		super();
		this.tableViewer = tableViewer;
		this.core = core;
		this.cTabItem = cTabItem;
		this.resultInfoMap = this.core.getResultInfoIntMap();
		this.tableContentProvider =
				( ResultTableContentProvider ) this.tableViewer.getContentProvider();
		this.selectedResults = new ArrayList();
		this.clipboard = new Clipboard( tableViewer.getTable().getDisplay() );
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
		for ( Iterator it = sSel.iterator(); it.hasNext(); ) {
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
			MenuManager copyManager =
					new MenuManager( G2GuiResources.getString( "ST_COPYLINK" ) );
			copyManager.add( new CopyNameAsPlainAction() );
			copyManager.add( new CopyNameAsHtmlAction() );
			menuManager.add( copyManager );
	
			menuManager.add( new Separator() );
	
			/* webservices */
/*			MenuManager webManager =
No				new MenuManager( G2GuiResources.getString( "ST_WEBSERVICES" ) );
Fake		webManager.add( new FakeSearchAction() );
Search		webManager.add( new FakeSearchAction() );
Yet			menuManager.add( webManager );
	
			menuManager.add( new Separator() );
*/
		}
		
		/* columns toogle */
		MenuManager columnsSubMenu = new MenuManager( G2GuiResources.getString( "TML_COLUMN" ) );
		Table table = tableViewer.getTable();
		for ( int i = 0; i < table.getColumnCount(); i++ ) {
			ToggleColumnsAction tCA = new ToggleColumnsAction( i );
			if ( table.getColumn( i ).getResizable() ) tCA.setChecked( true );
			columnsSubMenu.add( tCA );
		}
		menuManager.add( columnsSubMenu );
		
		/* filter submenu (select network to display) */			
		MenuManager filterSubMenu = new MenuManager( G2GuiResources.getString( "TML_FILTER" ) );
		AllFiltersAction aFA = new AllFiltersAction();
		if ( tableViewer.getFilters().length == 0 ) aFA.setChecked( true );
		filterSubMenu.add( aFA );
		filterSubMenu.add( new Separator() );
			
		NetworkInfo[] networks = core.getNetworkInfoMap().getNetworks();
		
		for ( int i = 0; i < networks.length; i++ ) {
			NetworkInfo network = networks[ i ];
			if ( network.isEnabled() ) {
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
	
	public boolean isFiltered( NetworkInfo.Enum networkType ) {
		ViewerFilter[] viewerFilters = tableViewer.getFilters();
		for ( int i = 0; i < viewerFilters.length; i++ ) {
			if ( viewerFilters [ i ] instanceof NetworkFilter )
				if ( ( ( NetworkFilter ) viewerFilters[ i ] ).getNetworkType().equals( networkType ) )
					return true; 
	
		}
		return false;
	}

	public void toggleFilter( ViewerFilter viewerFilter, boolean toggle ) {
		if ( toggle ) 
			tableViewer.addFilter( viewerFilter );
		else 
			tableViewer.removeFilter( viewerFilter );
	}

	private class DownloadAction extends Action {
		public DownloadAction() {
			super();
			setText( G2GuiResources.getString( "ST_DOWNLOAD" ) );
		}
		public void run() {
			Download download = new Download( core );
			for ( int i = 0; i < selectedResults.size(); i++ ) {
				ResultInfo result = ( ResultInfo ) selectedResults.get( i );
				download.setPossibleNames( result.getNames() );	
				download.setResultID( result.getResultID() );
				download.setForce( false );
				download.send();
			}
			download = null;
		}
	}
	
	private class CopyNameAction extends Action {
		public CopyNameAction() {
			super();
			setText( G2GuiResources.getString( "ST_COPYNAME" ) );
		}
		public void run() {
			for ( int i = 0; i < selectedResults.size(); i++ ) {
				ResultInfo result = ( ResultInfo ) selectedResults.get( i );
				TextTransfer textTransfer = TextTransfer.getInstance();
				clipboard.setContents( new Object[] { result.getNames()[ 0 ] },
								new Transfer[] { textTransfer } );
			}
		}
	}

	private class CopyNameAsPlainAction extends Action {
		public CopyNameAsPlainAction() {
			super();
			setText( G2GuiResources.getString( "ST_ASPLAIN" ) );
		}
		public void run() {
			for ( int i = 0; i < selectedResults.size(); i++ ) {
				ResultInfo result = ( ResultInfo ) selectedResults.get( i );
				TextTransfer textTransfer = TextTransfer.getInstance();
				clipboard.setContents( new Object[] { result.getLink() }, 
								new Transfer[] { textTransfer } );
			}
		}
	}

	private class CopyNameAsHtmlAction extends Action {
		public CopyNameAsHtmlAction() {
			super();
			setText( G2GuiResources.getString( "ST_ASHTML" ) );
		}
		public void run() {
			for ( int i = 0; i < selectedResults.size(); i++ ) {
				ResultInfo result = ( ResultInfo ) selectedResults.get( i );
				String aString = "<a href=\"" + result.getLink() + "\">"
								 + result.getNames()[ 0 ] + "</a>";
				TextTransfer textTransfer = TextTransfer.getInstance();
				clipboard.setContents( new Object[] { aString }, 
								new Transfer[] { textTransfer } );
			}
		}
	}
	
	private class FakeSearchAction extends Action {
		public FakeSearchAction() {
			super();
			setText( G2GuiResources.getString( "ST_WEBSERVICE1" ) );
		}
		public void run() {
		}
	}
	
	private class RemoveAction extends Action {
		public RemoveAction() {
			super();
			setText( G2GuiResources.getString( "ST_REMOVE" ) );
		}
		public void run() {
			tableViewer.getTable().remove( tableViewer.getTable().getSelectionIndices() );
		}
	}

	private class CloseAction extends Action {
		public CloseAction() {
			super();
			setText( G2GuiResources.getString( "ST_CLOSE" ) );
		}
		public void run() {
			tableViewer.getTable().dispose();
			cTabItem.dispose();
		}
	}

	private class ToggleColumnsAction extends Action {
		private int column;
		private Table table = tableViewer.getTable();
		private TableColumn tableColumn;
			
		public ToggleColumnsAction( int column ) {
			super( "", Action.AS_CHECK_BOX );
			this.column = column;
			tableColumn = table.getColumn( column );
			setText( tableColumn.getText() );
		}
			
		public  void run () {
			if ( !isChecked() ) {
				tableColumn.setWidth( 0 );
				tableColumn.setResizable( false );
			} else {
				tableColumn.setResizable( true );
				tableColumn.setWidth( 100 );
			}
		}
	}

	private class AllFiltersAction extends Action {
		public AllFiltersAction() {
			super();
			setText( G2GuiResources.getString( "TML_NO_FILTERS" ) );
		}
		public void run() {
			ViewerFilter[] viewerFilters = tableViewer.getFilters();
			for ( int i = 0; i < viewerFilters.length; i++ ) 
				toggleFilter( viewerFilters[ i ], false );
		}
	}
		
	private class NetworkFilterAction extends Action {
		private NetworkInfo.Enum networkType;
	
		public NetworkFilterAction( String name, NetworkInfo.Enum networkType ) {
			super( name, Action.AS_CHECK_BOX );
			this.networkType = networkType;
		}
		public void run() {
			if ( !isChecked() ) {
				ViewerFilter[] viewerFilters = tableViewer.getFilters();
				for ( int i = 0; i < viewerFilters.length; i++ ) {
					if ( viewerFilters[i] instanceof NetworkFilter )
						if ( ( ( NetworkFilter ) viewerFilters[ i ] ).getNetworkType() == networkType ) {
							toggleFilter( viewerFilters[ i ], false );
						}
				}
			}
			else {
				toggleFilter( new NetworkFilter( networkType ), true );
			}
		}
	}

	public static class NetworkFilter extends ViewerFilter {
		private NetworkInfo.Enum networkType;
			
		public NetworkFilter( NetworkInfo.Enum enum ) {
			this.networkType = enum;	
		}	
			
		public NetworkInfo.Enum getNetworkType () {
			return networkType;
		}
	
		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ViewerFilter#
		 * select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public boolean select( Viewer viewer, Object parentElement, Object element ) {
			if ( element instanceof ResultInfo ) {
				ResultInfo server = ( ResultInfo ) element;
				if ( server.getNetwork().getNetworkType() == networkType )
					return true;
				else 
					return false;
			}
			return true;
		}
	}
}

/*
$Log: ResultTableMenuListener.java,v $
Revision 1.1  2003/08/20 10:05:46  lemmster
MenuListener added

*/