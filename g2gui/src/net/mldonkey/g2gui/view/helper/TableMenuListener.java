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
package net.mldonkey.g2gui.view.helper;

import java.util.ArrayList;
import java.util.List;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.ResultInfo;
import net.mldonkey.g2gui.model.ServerInfo;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * TableMenuListener
 *
 *
 * @version $Id: TableMenuListener.java,v 1.2 2003/08/23 15:21:37 zet Exp $ 
 *
 */
public abstract class TableMenuListener {
	protected CoreCommunication core;
	protected StructuredViewer tableViewer;
	/**
	 * 
	 * @param tableViewer
	 * @param core
	 */
	public TableMenuListener( StructuredViewer tableViewer, CoreCommunication core ) {
		this.tableViewer = tableViewer;
		this.core = core;
	}
	/**
	 * 
	 * @param networkType
	 * @return
	 */
	public boolean isFiltered( NetworkInfo.Enum networkType ) {
		ViewerFilter[] viewerFilters = tableViewer.getFilters();
		for ( int i = 0; i < viewerFilters.length; i++ ) {
			if ( viewerFilters [ i ] instanceof NetworkFilter ) {
				NetworkFilter filter = ( NetworkFilter ) viewerFilters[ i ];
				for ( int j = 0; j < filter.getNetworkType().size(); j++ ) {
					if ( filter.getNetworkType().get( j ).equals( networkType ) )
						return true;					
				}
			}
		}
		return false;
	}
	/**
	 * 
	 * @param state
	 * @return
	 */
	public boolean isFiltered( EnumState state ) {
		ViewerFilter[] viewerFilters = tableViewer.getFilters();
		for ( int i = 0; i < viewerFilters.length; i++ ) {
			if ( viewerFilters [ i ] instanceof EnumStateFilter ) {
				EnumStateFilter filter = ( EnumStateFilter ) viewerFilters[ i ];
				for ( int j = 0; j < filter.getEnumState().size(); j++ ) {
					if ( filter.getEnumState().get( j ).equals( state ) )
						return true;					
				}
			}
		}
		return false;
	}
	/**
	 * 
	 * @param viewerFilter
	 * @param toggle
	 */
	public void toggleFilter( ViewerFilter viewerFilter, boolean toggle ) {
		if ( toggle ) 
			tableViewer.addFilter( viewerFilter );
		else 
			tableViewer.removeFilter( viewerFilter );
	}

	/**
	 * 
	 * NetworkFilter
	 *
	 */
	public static class NetworkFilter extends ViewerFilter {
		private List networkType;
		
		public NetworkFilter() {
			this.networkType = new ArrayList();	
		}	
		
		public List getNetworkType () {
			return networkType;
		}
		
		public boolean add( NetworkInfo.Enum enum ) {
			return this.networkType.add( enum );
		}
		
		public boolean remove( NetworkInfo.Enum enum ) {
			return this.networkType.remove( enum );
		}		 

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public boolean select( Viewer viewer, Object parentElement, Object element ) {
			if ( element instanceof ServerInfo ) {
				ServerInfo server = ( ServerInfo ) element;
				for ( int i = 0; i < this.networkType.size(); i++ ) {
					if ( server.getNetwork().getNetworkType() == networkType.get( i ) )
						return true;
				}	
				return false;
			}
			if ( element instanceof ResultInfo ) {
				ResultInfo result = ( ResultInfo ) element;
				for ( int i = 0; i < this.networkType.size(); i++ ) {
					if ( result.getNetwork().getNetworkType() == networkType.get( i ) )
						return true;
				}	
				return false;
			}
			if ( element instanceof FileInfo ) {
				FileInfo fileInfo = ( FileInfo ) element;
				for ( int i = 0; i < this.networkType.size(); i++ ) {
					if ( fileInfo.getNetwork().getNetworkType() == networkType.get( i ) )
						return true;
				}
				return false;
			}

			return true;
		}
	}

	/**
	 * 
	 * EnumStateFilter
	 *
	 */
	public static  class EnumStateFilter extends ViewerFilter {
		private List state;
		
		public EnumStateFilter() {
			this.state = new ArrayList();
		}
		
		public List getEnumState() {
			return this.state;
		}
		
		public boolean add( EnumState enum ) {
			return this.state.add( enum );
		}
		
		public boolean remove( EnumState state ) {
			return this.state.remove( state );
		}
		
		public boolean contains( EnumState state ) {
			return this.state.contains( state );
		}			 

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ViewerFilter#
		 * select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if ( element instanceof ServerInfo ) {
				ServerInfo server = ( ServerInfo ) element;
				for ( int i = 0; i < this.state.size(); i++ ) {
					if ( server.getConnectionState().getState() == state.get( i ) )
						return true;
				}	
				return false;
			}
			return true;
		}
	}

	/**
	 * 
	 * EnumStateFilterAction
	 *
	 */
	public class EnumStateFilterAction extends Action {
		private EnumState state;
		
		public EnumStateFilterAction( String name, EnumState state ) {
			super( name, Action.AS_CHECK_BOX );
			this.state = state;			
		}
		
		public void run() {
			if ( !isChecked() ) {
				ViewerFilter[] viewerFilters = tableViewer.getFilters();
				for ( int i = 0; i < viewerFilters.length; i++ ) {
					if ( viewerFilters[i] instanceof EnumStateFilter ) {
						EnumStateFilter filter = ( EnumStateFilter ) viewerFilters[ i ];
						for ( int j = 0; j < filter.getEnumState().size(); j++ ) {
							if ( filter.getEnumState().get( j ) == state )
								if ( filter.getEnumState().size() == 1 )
									toggleFilter( viewerFilters[ i ], false );
								else {
									filter.remove( state );
									tableViewer.refresh();
								}									
						}	
					}
				}
			}
			else {
				ViewerFilter[] viewerFilters = tableViewer.getFilters();
				for ( int i = 0; i < viewerFilters.length; i++ ) {
					if ( viewerFilters[i] instanceof EnumStateFilter ) {
						EnumStateFilter filter = ( EnumStateFilter ) viewerFilters[ i ];
						filter.add( state );
						tableViewer.refresh();
						return;
					}
				}
				EnumStateFilter filter = new EnumStateFilter();
				filter.add( state );
				toggleFilter( filter, true );
			}
		}		
	}
	
	/**
	 * 
	 * NetworkFilterAction
	 *
	 */
	public class NetworkFilterAction extends Action {
		private NetworkInfo.Enum networkType;

		public NetworkFilterAction( String name, NetworkInfo.Enum networkType ) {
			super( name, Action.AS_CHECK_BOX );
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
						return;
					}
				}
				NetworkFilter filter = new NetworkFilter();
				filter.add( networkType );
				toggleFilter( filter, true );
			}
		}
	}

	/**
	 * 
	 * AllFiltersAction
	 *
	 */
	public class AllFiltersAction extends Action {
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
	
	/**
	 * 
	 * ToggleColumnsAction
	 *
	 */
	public class ToggleColumnsAction extends Action {
		private int column;
		private Table table;
		private TableColumn tableColumn;
		
		public ToggleColumnsAction( int column ) {
			super( "", Action.AS_CHECK_BOX );
			this.column = column;
			if ( tableViewer instanceof TableTreeViewer )
				table = ( ( TableTreeViewer ) tableViewer ).getTableTree().getTable();
			else
				table = ( ( TableViewer ) tableViewer ).getTable();
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
}

/*
$Log: TableMenuListener.java,v $
Revision 1.2  2003/08/23 15:21:37  zet
remove @author

Revision 1.1  2003/08/23 09:46:18  lemmster
superclass TableMenuListener added

*/