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
package net.mldonkey.g2gui.view.server;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.helper.OurTableViewer;
import net.mldonkey.g2gui.view.helper.TableMenuListener;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;

import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * ServerTableViewer
 *
 * @version $Id: ServerTableViewer.java,v 1.1 2003/10/21 17:00:45 lemmster Exp $ 
 *
 */
public class ServerTableViewer extends OurTableViewer {
	private boolean oldValue = PreferenceLoader.loadBoolean( "displayAllServers" );
	private boolean oldValue2 = PreferenceLoader.loadBoolean( "displayTableColors" );

	/**
	 * 
	 * @param composite
	 * @param core
	 */
	public ServerTableViewer( Composite aComposite, CoreCommunication aCore ) {
		super( aComposite, aCore );
		
		/* proto <= 16 does not support favorites */					
		if ( core.getProtoToUse() <= 16 ) {
			this.tableColumns = new String[] { "SVT_NETWORK", "SVT_NAME", "SVT_DESC", "SVT_ADDRESS",
												"SVT_PORT", "SVT_SERVERSCORE", "SVT_USERS",	"SVT_FILES",
												"SVT_STATE" };	
			this.tableWidth = new int[] { 70, 160, 0, 120, 50, 55, 55, 60, 80 };
			
			this.tableAlign = new int[] { SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT,
											SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.LEFT };
		}
		else {
			this.tableColumns = new String[] {	"SVT_NETWORK", "SVT_NAME", "SVT_DESC",
												"SVT_ADDRESS", "SVT_PORT", "SVT_SERVERSCORE",
												"SVT_USERS", "SVT_FILES", "SVT_STATE", "SVT_FAVORITES" };	

			this.tableWidth = new int[] { 70, 160, 160, 120, 50, 55, 55, 60, 80, 50 };
			
			this.tableAlign = new int[] { SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT,
											SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.LEFT, SWT.LEFT };
		}
		
		this.swtLayout = SWT.FULL_SELECTION | SWT.MULTI;
		this.contentProvider = new ServerTableContentProvider();
		this.labelProvider = new ServerTableLabelProvider();
		this.tableSorter = new ServerTableSorter();
		this.menuListener = new ServerTableMenuListener( core );
		

		this.create();
	}

	protected void create() {
		super.create();
		
		// create the tablecolumns
		createTableColumns();

		// add a menulistener to make the first menu item bold
		addMenuListener();

		/* set the state filter if preference says so */
		if ( PreferenceLoader.loadBoolean( "displayAllServers" ) ) {
			ServerTableMenuListener.EnumStateFilter filter = new ServerTableMenuListener.EnumStateFilter();
			filter.add( EnumState.CONNECTED );
			tableViewer.addFilter( filter );
		}
	}
	
	/**
	 * Updates this table on preference close
	 */
	public void updateDisplay() {
		/* only update on pref change */
		boolean temp = PreferenceLoader.loadBoolean( "displayAllServers" );
		if ( oldValue != temp ) {
			/* update the state filter */
			if ( temp ) {
				/* first remove all EnumState filters */
				for ( int i = 0; i < getTableViewer().getFilters().length; i++ ) {
					if ( getTableViewer().getFilters()[ i ] instanceof ServerTableMenuListener.EnumStateFilter )
						getTableViewer().removeFilter( getTableViewer().getFilters()[ i ] );
				}
				/* now add the new one */
				ServerTableMenuListener.EnumStateFilter filter =
					 new ServerTableMenuListener.EnumStateFilter();
				filter.add( EnumState.CONNECTED );
				getTableViewer().addFilter( filter );
			}
			else {
				ViewerFilter[] filters = getTableViewer().getFilters();
				for ( int i = 0; i < filters.length; i++ ) {
					if ( getTableViewer().getFilters()[ i ] instanceof ServerTableMenuListener.EnumStateFilter ) {
						TableMenuListener.EnumStateFilter filter =
							( TableMenuListener.EnumStateFilter ) filters[ i ];
						if ( filter.contains( EnumState.CONNECTED ) ) {
							if (  filter.getEnumState().size() == 1 )
								getTableViewer().removeFilter( filter );
							else {
								filter.remove( EnumState.CONNECTED );
								getTableViewer().refresh();
							}
						}
					}
				}
			}
			this.oldValue = temp;
		}
		/* displayTableColors changed? */
		boolean temp2 = PreferenceLoader.loadBoolean( "displayTableColors" );
		if ( oldValue2 != temp2 ) {
			( ( ServerTableLabelProvider ) getTableViewer().getLabelProvider() ).setColors( temp2 );
			getTableViewer().refresh();
			this.oldValue2 = temp2;
		}
		getTableViewer().getTable().setLinesVisible(
				PreferenceLoader.loadBoolean( "displayGridLines" ) );
	}


	/**
	 * Adds a <code>ViewerFilter</code> to the <code>TableViewer</code> by
	 * removing all present <code>TabelMenuListener.NetworkFilter</code> 
	 * from the table.
	 * @param enum The <code>NetworkInfo.Enum</code> we want to be filtered
	 */
	public void setFilter( NetworkInfo.Enum enum ) {
		ViewerFilter[] filters = getTableViewer().getFilters();
		for ( int i = 0; i < filters.length; i++ ) {
			if ( filters[ i ] instanceof ServerTableMenuListener.NetworkFilter )
				getTableViewer().removeFilter( filters[ i ] );
		}
		ServerTableMenuListener.NetworkFilter filter = new ServerTableMenuListener.NetworkFilter();
		filter.add( enum );
		getTableViewer().addFilter( filter );
	}
}

/*
$Log: ServerTableViewer.java,v $
Revision 1.1  2003/10/21 17:00:45  lemmster
class hierarchy for tableviewer

*/