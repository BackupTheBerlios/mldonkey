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

import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.ResultInfo;
import net.mldonkey.g2gui.model.ServerInfo;
import net.mldonkey.g2gui.model.enum.Enum;
import net.mldonkey.g2gui.model.enum.EnumNetwork;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.GTableMenuListener;
import net.mldonkey.g2gui.view.viewers.GTableViewer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * TableMenuListener
 *
 *
 * @version $Id: TableMenuListener.java,v 1.16 2003/10/28 12:33:31 lemmster Exp $
 *
 */
public abstract class TableMenuListener extends GTableMenuListener implements ISelectionChangedListener, IMenuListener {
    protected ViewerFilter incrementalViewerFilter;
	protected Enum.MaskMatcher aMatcher;

    public TableMenuListener( GTableViewer gTableViewer ) {
        super(gTableViewer);
    }
        
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */
    public void menuAboutToShow( IMenuManager menuManager ) {
		menuManager.add( new Separator() );
			
		menuManager.add( new RefineFilterAction() );
		
		super.menuAboutToShow( menuManager );				
    }

    public boolean isFiltered( NetworkInfo aNetworkInfo ) {
        ViewerFilter[] viewerFilters = tableViewer.getFilters();
        for ( int i = 0; i < viewerFilters.length; i++ ) {
            if ( viewerFilters[ i ] instanceof NetworkViewerFilter ) {
                NetworkViewerFilter filter = ( NetworkViewerFilter ) viewerFilters[ i ];
                if ( filter.matches( aNetworkInfo ) )
                        return true;
            }
        }
        return false;
    }

    public boolean isFiltered( EnumState state ) {
        ViewerFilter[] viewerFilters = tableViewer.getFilters();
        for ( int i = 0; i < viewerFilters.length; i++ ) {
            if ( viewerFilters[ i ] instanceof EnumStateViewerFilter ) {
                EnumStateViewerFilter filter = ( EnumStateViewerFilter ) viewerFilters[ i ];
                if ( filter.matches( state ) )
                    return true;
            }
        }
        return false;
    }
    
	protected void toggleFilter( ViewerFilter viewerFilter, boolean toggle ) {
		if ( toggle )
			tableViewer.addFilter( viewerFilter );
		else
			tableViewer.removeFilter( viewerFilter );
	}

	/**
	 * 
	 * RefineFilter
	 *
	 */
    protected class RefineFilter extends ViewerFilter {
        private String refineString;
        private boolean caseInSensitive = true;
        
        public void setRefineString( String aString ) {
        	this.refineString = aString;
        	tableViewer.refresh();	
        }
        
        public void toggleCaseInSensitive() {
        	this.caseInSensitive = !this.caseInSensitive;
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
         */
        public boolean select( Viewer viewer, Object parentElement, Object element ) {
            if ( this.refineString == null ) return true;
            
            String aString = null;
            if ( element instanceof ServerInfo )
				aString = ( ( ServerInfo ) element ).getNameOfServer();
            else if ( element instanceof ResultInfo )
            	aString = ( ( ResultInfo ) element ).getName();

			if ( this.caseInSensitive ) {
				aString = aString.toLowerCase();
				refineString = refineString.toLowerCase();
			}

			if ( aString.startsWith( this.refineString ) )
				return true;

            return false;
        }
    }

    /**
     *
     * NetworkFilter
     *
     */
	public static class NetworkViewerFilter extends MyViewerFilter {
        public boolean matches( NetworkInfo networkInfo ) {
        	return super.matches( networkInfo.getNetworkType() );
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
         */
        public boolean select( Viewer viewer, Object parentElement, Object element ) {
            if ( element instanceof ServerInfo ) {
                ServerInfo server = ( ServerInfo ) element;
                if ( matches( server.getNetwork() ) )
                   return true;
                return false;
            }
            if ( element instanceof ResultInfo ) {
                ResultInfo result = ( ResultInfo ) element;
                if ( matches( result.getNetwork() ) )
                    return true;
                return false;
            }
            if ( element instanceof FileInfo ) {
                FileInfo fileInfo = ( FileInfo ) element;
                if ( matches( fileInfo.getNetwork() ) )
                    return true;
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
	public static class EnumStateViewerFilter extends MyViewerFilter {
        /* (non-Javadoc)
         * @see org.eclipse.jface.viewers.ViewerFilter#
         * select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
         */
        public boolean select( Viewer viewer, Object parentElement, Object element ) {
            if ( element instanceof ServerInfo ) {
                ServerInfo server = ( ServerInfo ) element;
                if ( aMatcher.matches( server.getConnectionState().getState() ) )
                    return true;
                return false;
            }
            return true;
        }
    }
    
    /**
     * MyFilter Superclass for EnumStateFilter and NetworkFilter
     */
    public static abstract class MyViewerFilter extends ViewerFilter {
		protected Enum.MaskMatcher aMatcher;
		/**
		 * Creates a new EnumStateFilter
		 */
		public MyViewerFilter() {
			this.aMatcher = new Enum.MaskMatcher();
		}

		public void add( Enum enum ) {
			this.aMatcher.add( enum );
		}

		public void remove( Enum state ) {
			this.aMatcher.remove( state );
		}
        
		public boolean matches( Enum enum ) {
			return this.aMatcher.matches( enum );        	
		}
        
		public int count() {
			return this.aMatcher.count();
		}	
		public abstract boolean select( Viewer viewer, Object parentElement, Object element );
    }

    /**
     *
     * EnumStateFilterAction
     *
     */
    protected class EnumStateFilterAction extends Action {
        private EnumState state;

        public EnumStateFilterAction( String name, EnumState state ) {
            super( name, Action.AS_CHECK_BOX );
            this.state = state;
        }

        public void run() {
            if ( !isChecked() ) {
                ViewerFilter[] viewerFilters = tableViewer.getFilters();
                for ( int i = 0; i < viewerFilters.length; i++ ) {
                    if ( viewerFilters[ i ] instanceof EnumStateViewerFilter ) {
                        EnumStateViewerFilter filter = ( EnumStateViewerFilter ) viewerFilters[ i ];
                        if ( filter.matches( state ) )
                            if ( filter.count() == 1 )
                                toggleFilter( viewerFilters[ i ], false );
                            else {
                                filter.remove( state );
                                tableViewer.refresh();
                            }
                    }
                }
            }
            else {
                ViewerFilter[] viewerFilters = tableViewer.getFilters();
                for ( int i = 0; i < viewerFilters.length; i++ ) {
                    if ( viewerFilters[ i ] instanceof EnumStateViewerFilter ) {
                        EnumStateViewerFilter filter = ( EnumStateViewerFilter ) viewerFilters[ i ];
                        filter.add( state );
                        tableViewer.refresh();
                        return;
                    }
                }
                EnumStateViewerFilter filter = new EnumStateViewerFilter();
                filter.add( state );
                toggleFilter( filter, true );
            }
        }
    }
    
    protected class RefineFilterAction extends Action {
    	public RefineFilterAction() {
    		super();
    		setText( G2GuiResources.getString( "TML_REFINE" ) );
    	}
    	public void run() {
    		incrementalViewerFilter = new RefineFilter();
			tableViewer.addFilter( incrementalViewerFilter );
			Dialog myDialog = new MyDialog( ( ( TableViewer ) tableViewer ).getTable().getShell(),
								G2GuiResources.getString( "TML_REFINE" ),
								G2GuiResources.getString( "TML_REFINE_LABEL" ) );
			myDialog.open();
			tableViewer.removeFilter( incrementalViewerFilter );
    	}

		private class MyDialog extends Dialog {
			private String dialogMessage, dialogTitle;
			private Text text;
			/**
			 * @param parent
			 */
			public MyDialog( Shell parent, String dialogTitle, String dialogMessage ) {
				super( parent );
				this.dialogMessage = dialogMessage;
				this.dialogTitle = dialogTitle;
			}

			/* (non-Javadoc)
			 * Method declared in Window.
			 */
			protected int getShellStyle() {
				return SWT.TITLE;
			}

			/* (non-Javadoc)
			 * Method declared in Window.
			 */
			protected void configureShell( Shell shell ) {
				super.configureShell( shell );
				if ( this.dialogTitle != null )
					shell.setText( this.dialogTitle );
			}

			/* (non-Javadoc)
			 * Method declared on Dialog.
			 */
			protected void createButtonsForButtonBar( Composite parent ) {
				createButton( parent, IDialogConstants.OK_ID, IDialogConstants.CLOSE_LABEL, true );
			}

			/* (non-Javadoc)
			 * Method declared on Dialog.
			 */
			protected Control createDialogArea( Composite parent ) {
				Composite composite = ( Composite ) super.createDialogArea( parent );

				if ( this.dialogMessage != null ) {
					Label label = new Label( composite, SWT.WRAP );
					label.setText( this.dialogMessage );
					GridData data = new GridData(
						GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL
						| GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_CENTER );
					data.widthHint = 
						convertHorizontalDLUsToPixels( IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH );
					label.setLayoutData( data );
					label.setFont( parent.getFont() );
				}

				text = new Text( composite, SWT.SINGLE | SWT.BORDER );
				text.setLayoutData( new GridData(
					GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL ) );
				text.setFont( parent.getFont() );
				text.addModifyListener(	new ModifyListener() {
						public void modifyText( ModifyEvent e ) {
							if ( text.getText().equals( "" ) )
								( ( RefineFilter ) incrementalViewerFilter ).setRefineString( null );
							else
								( ( RefineFilter ) incrementalViewerFilter ).setRefineString( text.getText() );
						}
					}
				);
				
				GridLayout gridLayout = new GridLayout();
				gridLayout.numColumns = 2;
				gridLayout.marginWidth = 0;
				Composite aSubComposite = new Composite( composite, SWT.NONE );
				aSubComposite.setLayout( gridLayout );
				
				Button caseButton = new Button( aSubComposite, SWT.CHECK );
				caseButton.addSelectionListener( new SelectionListener() {
					public void widgetDefaultSelected( SelectionEvent e ) { }
					public void widgetSelected( SelectionEvent e ) {
						( ( RefineFilter ) incrementalViewerFilter ).toggleCaseInSensitive();
					}
				} );
				
				Label label = new Label( aSubComposite, SWT.NONE );
				label.setText( G2GuiResources.getString( "TML_REFINE_CASE" ) );

				return composite;
			}
		}
    }

    /**
     *
     * NetworkFilterAction
     *
     */
    public class NetworkFilterAction extends Action {
        private EnumNetwork networkType;

		/**
		 * Creates a new NetworkFilterAction
		 * @param name The name we should display on the <code>MenuManager</code>
		 * @param networkType The <code>NetworkInfo.Enum</code> we should filter
		 */
        public NetworkFilterAction( String name, EnumNetwork networkType ) {
            super( name, Action.AS_CHECK_BOX );
            this.networkType = networkType;
        }

        public void run() {
            if ( !isChecked() ) {
                ViewerFilter[] viewerFilters = tableViewer.getFilters();
                for ( int i = 0; i < viewerFilters.length; i++ ) {
                    if ( viewerFilters[ i ] instanceof NetworkViewerFilter ) {
                        NetworkViewerFilter filter = ( NetworkViewerFilter ) viewerFilters[ i ];
                        if ( filter.matches( networkType ) )
                            if ( filter.count() == 1 )
                                toggleFilter( viewerFilters[ i ], false );
                            else {
                                filter.remove( networkType );
                                tableViewer.refresh();
                            }
                    }
                }
            }
            else {
                ViewerFilter[] viewerFilters = tableViewer.getFilters();
                for ( int i = 0; i < viewerFilters.length; i++ ) {
                    if ( viewerFilters[ i ] instanceof NetworkViewerFilter ) {
                        NetworkViewerFilter filter = ( NetworkViewerFilter ) viewerFilters[ i ];
                        filter.add( networkType );
                        tableViewer.refresh();
                        return;
                    }
                }
                NetworkViewerFilter filter = new NetworkViewerFilter();
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
        /**
         * Creates a new AllFiltersAction
         */
        public AllFiltersAction() {
            super();
            setText( G2GuiResources.getString( "TML_NO_FILTERS" ) );
        }

        public void run() {
            ViewerFilter[] viewerFilters = tableViewer.getFilters();
            for ( int i = 0; i < viewerFilters.length; i++ )
                if ( !( viewerFilters[ i ] instanceof WordFilter ) )
                    toggleFilter( viewerFilters[ i ], false );
        }
    }

}

/*
$Log: TableMenuListener.java,v $
Revision 1.16  2003/10/28 12:33:31  lemmster
moved NetworkInfo.Enum -> enum.EnumNetwork;
added Enum.MaskMatcher

Revision 1.15  2003/10/28 11:07:32  lemmster
move NetworkInfo.Enum -> enum.EnumNetwork
add MaskMatcher for "Enum[]"

Revision 1.14  2003/10/22 01:37:10  zet
add column selector to server/search (might not be finished yet..)

Revision 1.13  2003/10/21 17:00:45  lemmster
class hierarchy for tableviewer

Revision 1.12  2003/10/13 19:55:22  zet
remove high ascii

Revision 1.11  2003/09/23 14:47:53  zet
rename MenuListener to avoid conflict with swt MenuListener

Revision 1.10  2003/09/18 10:04:57  lemmster
checkstyle

Revision 1.9  2003/09/17 20:07:44  lemmster
avoid NPEs in search

Revision 1.8  2003/09/14 13:24:30  lemmster
add header button to servertab

Revision 1.7  2003/09/08 17:09:38  dek
;; --> ;

Revision 1.6  2003/09/08 16:37:52  lemmster
refine search toggleable for case sensitive

Revision 1.5  2003/09/08 16:10:36  lemmster
RefineSearch added

Revision 1.4  2003/09/08 15:43:34  lemmster
work in progress

Revision 1.3  2003/08/29 00:54:42  zet
Move wordFilter public

Revision 1.2  2003/08/23 15:21:37  zet
remove @author

Revision 1.1  2003/08/23 09:46:18  lemmster
superclass TableMenuListener added

*/
