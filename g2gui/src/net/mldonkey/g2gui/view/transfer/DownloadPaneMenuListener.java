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
package net.mldonkey.g2gui.view.transfer;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.transfer.downloadTable.DownloadTableTreeContentProvider;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * DownloadPaneMenuListener
 *
 *
 * @version $Id: DownloadPaneMenuListener.java,v 1.2 2003/09/21 23:39:31 zet Exp $
 *
 */
public class DownloadPaneMenuListener implements IMenuListener {
	
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

	private DownloadTableTreeContentProvider tableTreeContentProvider;
	private CustomTableTreeViewer tableTreeViewer;
	private CoreCommunication core;

    public DownloadPaneMenuListener( CustomTableTreeViewer tableTreeViewer, CoreCommunication core ) {
        this.tableTreeViewer = tableTreeViewer;
        this.core = core;
        this.tableTreeContentProvider = (DownloadTableTreeContentProvider) tableTreeViewer.getContentProvider(  );
    }

    public void menuAboutToShow( IMenuManager menuManager ) {
        fillContextMenu( menuManager );
    }

    // Build the menu
    public void fillContextMenu( IMenuManager menuManager ) {
        menuManager.add( new ExpandCollapseAction( true ) );
        menuManager.add( new ExpandCollapseAction( false ) );

        // columns submenu
        menuManager.add( new Separator(  ) );

        MenuManager columnsSubMenu = new MenuManager( G2GuiResources.getString( "TT_DOWNLOAD_MENU_COLUMNS" ) );

        Table table = tableTreeViewer.getTableTree(  ).getTable(  );

        for ( int i = 0; i < table.getColumnCount(  ); i++ ) {
            ToggleColumnsAction tCA = new ToggleColumnsAction( i );

            if ( table.getColumn( i ).getResizable(  ) ) {
                tCA.setChecked( true );
            }

            columnsSubMenu.add( tCA );
        }

        menuManager.add( columnsSubMenu );

        // filter submenu			
        MenuManager filterSubMenu = new MenuManager( G2GuiResources.getString( "TT_DOWNLOAD_MENU_FILTER" ) );
        AllFiltersAction aFA = new AllFiltersAction(  );

        if ( tableTreeViewer.getFilters(  ).length == 0 ) {
            aFA.setChecked( true );
        }

        filterSubMenu.add( aFA );

        filterSubMenu.add( new Separator(  ) );

        FileStateFilterAction fFA = new FileStateFilterAction( "queued", EnumFileState.QUEUED );

        if ( !isFiltered( EnumFileState.QUEUED ) ) {
            fFA.setChecked( true );
        }

        filterSubMenu.add( fFA );

        filterSubMenu.add( new Separator(  ) );

        NetworkInfo[] networks = core.getNetworkInfoMap(  ).getNetworks(  );

        for ( int i = 0; i < networks.length; i++ ) {
            NetworkInfo network = networks[ i ];

            if ( network.isEnabled(  ) ) {
                NetworkFilterAction nFA = new NetworkFilterAction( network.getNetworkName(  ), network.getNetworkType(  ) );

                if ( isFiltered( network.getNetworkType(  ) ) ) {
                    nFA.setChecked( true );
                }

                filterSubMenu.add( nFA );
            }
        }

        filterSubMenu.add( new Separator(  ) );

        for ( int i = 0; i < Extensions.length; i++ ) {
            String[] extensions = Extensions[ i ];
            String extensionName = ExtensionNames[ i ];
            ExtensionsFilterAction eFA = new ExtensionsFilterAction( extensionName, Extensions[ i ] );

            if ( isFiltered( extensions ) ) {
                eFA.setChecked( true );
            }

            filterSubMenu.add( eFA );
        }

        menuManager.add( filterSubMenu );
    }

    public boolean isFiltered( NetworkInfo.Enum networkType ) {
        ViewerFilter[] viewerFilters = tableTreeViewer.getFilters(  );

        for ( int i = 0; i < viewerFilters.length; i++ ) {
            if ( viewerFilters[ i ] instanceof NetworkFilter ) {
                NetworkFilter filter = (NetworkFilter) viewerFilters[ i ];

                for ( int j = 0; j < filter.getNetworkType(  ).size(  ); j++ ) {
                    if ( filter.getNetworkType(  ).get( j ).equals( networkType ) ) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isFiltered( EnumFileState fileState ) {
        ViewerFilter[] viewerFilters = tableTreeViewer.getFilters(  );

        for ( int i = 0; i < viewerFilters.length; i++ ) {
            if ( viewerFilters[ i ] instanceof FileStateFilter ) {
                if ( ( (FileStateFilter) viewerFilters[ i ] ).getFileStateType(  ).equals( fileState ) ) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isFiltered( String[] extensions ) {
        ViewerFilter[] viewerFilters = tableTreeViewer.getFilters(  );

        for ( int i = 0; i < viewerFilters.length; i++ ) {
            if ( viewerFilters[ i ] instanceof FileExtensionsFilter ) {
                FileExtensionsFilter filter = (FileExtensionsFilter) viewerFilters[ i ];

                for ( int j = 0; j < filter.getFileExtensionList(  ).size(  ); j++ ) {
                    String[] fileExtensions = (String[]) filter.getFileExtensionList(  ).get( j );

                    if ( fileExtensions.equals( extensions ) ) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void toggleFilter( ViewerFilter viewerFilter, boolean toggle ) {
        if ( toggle ) {
            tableTreeViewer.addFilter( viewerFilter );
        } else {
            tableTreeViewer.removeFilter( viewerFilter );
        }

        tableTreeContentProvider.updateAllEditors(  );
    }

    class ExpandCollapseAction extends Action {
        private boolean expand;

        public ExpandCollapseAction( boolean expand ) {
            super(  );

            if ( expand ) {
                setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_EXPANDALL" ) );
            } else {
                setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_COLLAPSEALL" ) );
            }

            this.expand = expand;
        }

        public void run(  ) {
            //	tableTreeContentProvider.closeAllEditors();
            if ( expand ) {
                tableTreeViewer.expandAll(  );
            } else {
                tableTreeViewer.collapseAll(  );
            }

            tableTreeContentProvider.updateAllEditors(  );
        }
    }

    class AllFiltersAction extends Action {
        public AllFiltersAction(  ) {
            super( G2GuiResources.getString( "TT_DOWNLOAD_MENU_FILTER_ALL" ), Action.AS_CHECK_BOX );
        }

        public void run(  ) {
            tableTreeViewer.resetFilters(  );
            tableTreeContentProvider.updateAllEditors(  );
        }
    }

    private class NetworkFilterAction extends Action {
        private NetworkInfo.Enum networkType;

        public NetworkFilterAction( String name, NetworkInfo.Enum networkType ) {
            super( name, Action.AS_CHECK_BOX );
            this.networkType = networkType;
        }

        public void run(  ) {
            if ( !isChecked(  ) ) {
                ViewerFilter[] viewerFilters = tableTreeViewer.getFilters(  );

                for ( int i = 0; i < viewerFilters.length; i++ ) {
                    if ( viewerFilters[ i ] instanceof NetworkFilter ) {
                        NetworkFilter filter = (NetworkFilter) viewerFilters[ i ];

                        for ( int j = 0; j < filter.getNetworkType(  ).size(  ); j++ ) {
                            if ( filter.getNetworkType(  ).get( j ) == networkType ) {
                                if ( filter.getNetworkType(  ).size(  ) == 1 ) {
                                    toggleFilter( viewerFilters[ i ], false );
                                } else {
                                    filter.remove( networkType );
                                    tableTreeViewer.refresh(  );
                                    tableTreeContentProvider.updateAllEditors(  );
                                }
                            }
                        }
                    }
                }
            } else {
                ViewerFilter[] viewerFilters = tableTreeViewer.getFilters(  );

                for ( int i = 0; i < viewerFilters.length; i++ ) {
                    if ( viewerFilters[ i ] instanceof NetworkFilter ) {
                        NetworkFilter filter = (NetworkFilter) viewerFilters[ i ];
                        filter.add( networkType );
                        tableTreeViewer.refresh(  );
                        tableTreeContentProvider.updateAllEditors(  );

                        return;
                    }
                }

                NetworkFilter filter = new NetworkFilter(  );
                filter.add( networkType );
                toggleFilter( filter, true );
            }
        }
    }

    class FileStateFilterAction extends Action {
        public EnumFileState fileState;

        public FileStateFilterAction( String name, EnumFileState fileState ) {
            super( name, Action.AS_CHECK_BOX );
            this.fileState = fileState;
        }

        public void run(  ) {
            if ( isChecked(  ) ) {
                ViewerFilter[] viewerFilters = tableTreeViewer.getFilters(  );

                for ( int i = 0; i < viewerFilters.length; i++ ) {
                    if ( viewerFilters[ i ] instanceof FileStateFilter ) {
                        if ( ( (FileStateFilter) viewerFilters[ i ] ).getFileStateType(  ) == fileState ) {
                            toggleFilter( viewerFilters[ i ], false );
                        }
                    }
                }
            } else {
                toggleFilter( new FileStateFilter( fileState ), true );
            }
        }
    }

    private class ExtensionsFilterAction extends Action {
        public String[] extensions;

        public ExtensionsFilterAction( String name, String[] extensions ) {
            super( name, Action.AS_CHECK_BOX );
            this.extensions = extensions;
        }

        public void run(  ) {
            if ( !isChecked(  ) ) {
                ViewerFilter[] viewerFilters = tableTreeViewer.getFilters(  );

                for ( int i = 0; i < viewerFilters.length; i++ ) {
                    if ( viewerFilters[ i ] instanceof FileExtensionsFilter ) {
                        FileExtensionsFilter filter = (FileExtensionsFilter) viewerFilters[ i ];

                        for ( int j = 0; j < filter.getFileExtensionList(  ).size(  ); j++ ) {
                            String[] fileExtensions = (String[]) filter.getFileExtensionList(  ).get( j );

                            if ( fileExtensions.equals( extensions ) ) {
                                if ( filter.getFileExtensionList(  ).size(  ) == 1 ) {
                                    toggleFilter( viewerFilters[ i ], false );
                                } else {
                                    filter.remove( fileExtensions );
                                    tableTreeViewer.refresh(  );
                                    tableTreeContentProvider.updateAllEditors(  );
                                }
                            }
                        }
                    }
                }
            } else {
                ViewerFilter[] viewerFilters = tableTreeViewer.getFilters(  );

                for ( int i = 0; i < viewerFilters.length; i++ ) {
                    if ( viewerFilters[ i ] instanceof FileExtensionsFilter ) {
                        FileExtensionsFilter filter = (FileExtensionsFilter) viewerFilters[ i ];
                        filter.add( extensions );
                        tableTreeViewer.refresh(  );
                        tableTreeContentProvider.updateAllEditors(  );

                        return;
                    }
                }

                FileExtensionsFilter filter = new FileExtensionsFilter(  );
                filter.add( extensions );
                toggleFilter( filter, true );
                tableTreeContentProvider.updateAllEditors(  );
            }
        }
    }

    class ToggleColumnsAction extends Action {
        private int column;
        private Table table = tableTreeViewer.getTableTree(  ).getTable(  );
        private TableColumn tableColumn;

        public ToggleColumnsAction( int column ) {
            super( "", Action.AS_CHECK_BOX );
            this.column = column;
            tableColumn = table.getColumn( column );
            setText( tableColumn.getText(  ) );
        }

        public void run(  ) {
            if ( !isChecked(  ) ) {
                // gtk doesn't support column width 0
                tableColumn.setWidth( SWT.getPlatform(  ).equals( "gtk" ) ? 1 : 0 );
                tableColumn.setResizable( false );
            } else {
                tableColumn.setResizable( true );
                tableColumn.setWidth( 100 );
            }
        }
    }

    // Filters	
    public static class NetworkFilter extends ViewerFilter {
        private List networkType;

        public NetworkFilter(  ) {
            this.networkType = new ArrayList(  );
        }

        public List getNetworkType(  ) {
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
            if ( element instanceof FileInfo ) {
                FileInfo fileInfo = (FileInfo) element;

                for ( int i = 0; i < this.networkType.size(  ); i++ ) {
                    if ( fileInfo.getNetwork(  ).getNetworkType(  ) == networkType.get( i ) ) {
                        return true;
                    }
                }

                return false;
            }

            return true;
        }
    }

    public class FileStateFilter extends ViewerFilter {
        private EnumFileState fileState;

        public FileStateFilter( EnumFileState enum ) {
            this.fileState = enum;
        }

        public EnumFileState getFileStateType(  ) {
            return fileState;
        }

        public boolean select( Viewer viewer, Object parentElement, Object element ) {
            if ( element instanceof FileInfo ) {
                FileInfo fileInfo = (FileInfo) element;

                if ( (EnumFileState) fileInfo.getState(  ).getState(  ) == fileState ) {
                    return false;
                } else {
                    return true;
                }
            }

            return true;
        }
    }

    public static class FileExtensionsFilter extends ViewerFilter {
        private List fileExtensionsList;

        public FileExtensionsFilter(  ) {
            this.fileExtensionsList = new ArrayList(  );
        }

        public List getFileExtensionList(  ) {
            return fileExtensionsList;
        }

        public boolean add( String[] extensions ) {
            return this.fileExtensionsList.add( extensions );
        }

        public boolean remove( String[] extensions ) {
            return this.fileExtensionsList.remove( extensions );
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
         */
        public boolean select( Viewer viewer, Object parentElement, Object element ) {
            if ( element instanceof FileInfo ) {
                FileInfo fileInfo = (FileInfo) element;

                for ( int i = 0; i < this.fileExtensionsList.size(  ); i++ ) {
                    String[] fileExtensions = (String[]) fileExtensionsList.get( i );

                    for ( int j = 0; j < fileExtensions.length; j++ ) {
                        if ( fileInfo.getName(  ).toLowerCase(  ).endsWith( "." + fileExtensions[ j ] ) ) {
                            return true;
                        }
                    }
                }

                return false;
            }

            return true;
        }
    }
}


/*
$Log: DownloadPaneMenuListener.java,v $
Revision 1.2  2003/09/21 23:39:31  zet
displayTableColors preference

Revision 1.1  2003/09/20 14:39:21  zet
move transfer package

Revision 1.4  2003/09/18 14:11:01  zet
revert

Revision 1.2  2003/09/01 01:38:30  zet
gtk workaround

Revision 1.1  2003/08/30 00:44:01  zet
move tabletree menu

*/
