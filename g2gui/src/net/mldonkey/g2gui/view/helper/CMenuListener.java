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

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * MenuListener
 *
 * @version $Id: CMenuListener.java,v 1.3 2003/10/21 17:00:45 lemmster Exp $
 *
 */
public abstract class CMenuListener implements IMenuListener {
    protected CoreCommunication core;
    protected StructuredViewer tableViewer;

	/**
	 * Creates a new MenuListener
	 * @param viewer The Viewer which is the parent of this objs
	 * @param core The parent core3
	 */
    public CMenuListener( CoreCommunication core ) {
        this.core = core;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
     */
    public void menuAboutToShow( IMenuManager menuManager ) {
        /* columns toogle */
        MenuManager columnsSubMenu = new MenuManager( G2GuiResources.getString( "TML_COLUMN" ) );
        Table table;
		
		if ( tableViewer instanceof TableTreeViewer )
			table = ( ( TableTreeViewer ) tableViewer ).getTableTree().getTable();
		else
            table = ( ( TableViewer ) tableViewer ).getTable(  );
            
        for ( int i = 0; i < table.getColumnCount(); i++ ) {
            ToggleColumnsAction tCA = new ToggleColumnsAction( i );
            if ( table.getColumn( i ).getResizable() )
                tCA.setChecked( true );
            columnsSubMenu.add( tCA );
        }
        menuManager.add( columnsSubMenu );
    }

    /**
     * ToggleColumnsAction
     */
    public class ToggleColumnsAction extends Action {
        private int column;
        private Table table;
        private TableColumn tableColumn;

		/**
		 * @param column The column which should be toggled
		 */
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
    
        public void run() {
            if ( !isChecked() ) {
//				gtk doesn't support column width 0
				tableColumn.setWidth( SWT.getPlatform().equals( "gtk" ) ? 1 : 0 );
                tableColumn.setResizable( false );
            }
            else {
                tableColumn.setResizable( true );
                tableColumn.setWidth( 100 );
            }
        }
    }

    /**
     * @param viewerFilter 
     * @param toggle 
     */
    protected void toggleFilter( ViewerFilter viewerFilter, boolean toggle ) {
        if ( toggle )
            tableViewer.addFilter( viewerFilter );
        else
            tableViewer.removeFilter( viewerFilter );
    }

	/**
	 * @param tableViewer
	 */
	public void setTableViewer( StructuredViewer tableViewer ) {
		this.tableViewer = tableViewer;
	}
}

/*
$Log: CMenuListener.java,v $
Revision 1.3  2003/10/21 17:00:45  lemmster
class hierarchy for tableviewer

Revision 1.2  2003/09/23 14:59:50  zet
update for tabletreeviewer support

Revision 1.1  2003/09/23 14:47:53  zet
rename MenuListener to avoid conflict with swt MenuListener

Revision 1.2  2003/09/18 10:04:57  lemmster
checkstyle

Revision 1.1  2003/09/14 13:24:30  lemmster
add header button to servertab

*/
