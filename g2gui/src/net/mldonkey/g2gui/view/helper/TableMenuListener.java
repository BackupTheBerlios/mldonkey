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

import net.mldonkey.g2gui.model.ResultInfo;
import net.mldonkey.g2gui.model.ServerInfo;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.table.GTableMenuListener;
import net.mldonkey.g2gui.view.viewers.table.GTableView;

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
 * @version $Id: TableMenuListener.java,v 1.21 2003/12/04 08:47:31 lemmy Exp $
 *
 */
public abstract class TableMenuListener extends GTableMenuListener implements ISelectionChangedListener, IMenuListener {
    protected ViewerFilter incrementalViewerFilter;

    public TableMenuListener( GTableView gTableViewer ) {
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
}

/*
$Log: TableMenuListener.java,v $
Revision 1.21  2003/12/04 08:47:31  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.20  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.19  2003/10/31 10:42:47  lemmy
Renamed GViewer, GTableViewer and GTableTreeViewer to GPage... to avoid mix-ups with StructuredViewer...
Removed IGViewer because our abstract class GPage do the job
Use supertype/interface where possible to keep the design flexible!

Revision 1.18  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.17  2003/10/29 16:56:21  lemmy
added reasonable class hierarchy for panelisteners, viewers...

Revision 1.16  2003/10/28 12:33:31  lemmy
moved NetworkInfo.Enum -> enum.EnumNetwork;
added Enum.MaskMatcher

Revision 1.15  2003/10/28 11:07:32  lemmy
move NetworkInfo.Enum -> enum.EnumNetwork
add MaskMatcher for "Enum[]"

Revision 1.14  2003/10/22 01:37:10  zet
add column selector to server/search (might not be finished yet..)

Revision 1.13  2003/10/21 17:00:45  lemmy
class hierarchy for tableviewer

Revision 1.12  2003/10/13 19:55:22  zet
remove high ascii

Revision 1.11  2003/09/23 14:47:53  zet
rename MenuListener to avoid conflict with swt MenuListener

Revision 1.10  2003/09/18 10:04:57  lemmy
checkstyle

Revision 1.9  2003/09/17 20:07:44  lemmy
avoid NPEs in search

Revision 1.8  2003/09/14 13:24:30  lemmy
add header button to servertab

Revision 1.7  2003/09/08 17:09:38  dek
;; --> ;

Revision 1.6  2003/09/08 16:37:52  lemmy
refine search toggleable for case sensitive

Revision 1.5  2003/09/08 16:10:36  lemmy
RefineSearch added

Revision 1.4  2003/09/08 15:43:34  lemmy
work in progress

Revision 1.3  2003/08/29 00:54:42  zet
Move wordFilter public

Revision 1.2  2003/08/23 15:21:37  zet
remove @author

Revision 1.1  2003/08/23 09:46:18  lemmy
superclass TableMenuListener added

*/
