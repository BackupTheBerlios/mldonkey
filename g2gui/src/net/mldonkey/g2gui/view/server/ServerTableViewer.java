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
import net.mldonkey.g2gui.view.helper.TableMenuListener;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.viewers.GTableViewer;

import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * ServerTableViewer
 *
 * @version $Id: ServerTableViewer.java,v 1.4 2003/10/26 11:07:33 lemmster Exp $ 
 *
 */
public class ServerTableViewer extends GTableViewer {
	private boolean oldValue = PreferenceLoader.loadBoolean( "displayAllServers" );
	private boolean oldValue2 = PreferenceLoader.loadBoolean( "displayTableColors" );
	
	public static final int NETWORK = 0;
	public static final int NAME = 1;
	public static final int DESCRIPTION = 2;
	public static final int ADDRESS = 3;
	public static final int PORT = 4;
	public static final int SCORE = 5;
	public static final int USERS = 6;
	public static final int FILES = 7;
	public static final int STATE = 8;
	public static final int FAVORITE = 9;
	

	/**
	 * @param composite
	 * @param core
	 */
	public ServerTableViewer( Composite parent, CoreCommunication aCore ) {
		super( parent, aCore );
		preferenceString = "server";
		
		/* proto <= 16 does not support favorites */					
		if ( core.getProtoToUse() <= 16 ) {
			columnLabels = new String[] { "SVT_NETWORK", "SVT_NAME", "SVT_DESC", "SVT_ADDRESS",
												"SVT_PORT", "SVT_SERVERSCORE", "SVT_USERS",	"SVT_FILES",
												"SVT_STATE" };	
			columnDefaultWidths = new int[] { 70, 160, 160, 120, 50, 55, 55, 60, 80 };
			columnAlignment = new int[] { SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT,
											SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.LEFT };
		}
		else {
			columnLabels = new String[] {	"SVT_NETWORK", "SVT_NAME", "SVT_DESC",
												"SVT_ADDRESS", "SVT_PORT", "SVT_SERVERSCORE",
												"SVT_USERS", "SVT_FILES", "SVT_STATE", "SVT_FAVORITES" };	
			columnDefaultWidths = new int[] { 70, 160, 160, 120, 50, 55, 55, 60, 80, 50 };
			columnAlignment = new int[] { SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.RIGHT, SWT.RIGHT,
											SWT.RIGHT, SWT.RIGHT, SWT.RIGHT, SWT.LEFT, SWT.LEFT };
		}
		
		tableContentProvider = new ServerTableContentProvider( this );
		tableLabelProvider = new ServerTableLabelProvider( this );
		tableSorter = new ServerTableSorter( this );
		tableMenuListener = new ServerTableMenuListener( this );
		

		this.createContents( parent );
	}

	protected void createContents( Composite parent ) {
		super.createContents( parent );
		tableViewer.addSelectionChangedListener((ServerTableMenuListener) tableMenuListener);
		
		// add a menulistener to make the first menu item bold
	    addMenuListener();

		/* set the state filter if preference says so */
		if ( PreferenceLoader.loadBoolean( "displayAllServers" ) ) {
			ServerTableMenuListener.EnumStateFilter filter = new ServerTableMenuListener.EnumStateFilter();
			filter.add( EnumState.CONNECTED );
			tableViewer.addFilter( filter );
		}
	}
	
	//wtf
	public void setInput( Object object ) {
	    tableViewer.setInput( object );
	}
	/**
	 * Updates this table on preference close
	 */
	public void updateDisplay() {
	    super.updateDisplay();
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
Revision 1.4  2003/10/26 11:07:33  lemmster
fixed zero width for description

Revision 1.3  2003/10/22 02:25:24  zet
+prefString

Revision 1.2  2003/10/22 01:37:55  zet
add column selector to server/search (might not be finished yet..)

Revision 1.1  2003/10/21 17:00:45  lemmster
class hierarchy for tableviewer

*/