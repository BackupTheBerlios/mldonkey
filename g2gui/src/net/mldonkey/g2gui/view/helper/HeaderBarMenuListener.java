/*
 * Copyright 2003
 * g2gui Team
 *
 *
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package net.mldonkey.g2gui.view.helper;

import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.ColumnSelectorPaneListener;
import net.mldonkey.g2gui.view.viewers.GTableViewer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Control;


/**
 * HeaderBarMenusListener - helper for HeaderBar Menus
 *
 * @version $Id: HeaderBarMenuListener.java,v 1.4 2003/10/22 01:37:10 zet Exp $
 *
 */
public class HeaderBarMenuListener extends ColumnSelectorPaneListener  {
    private SashForm sashForm;
    private Control control;

    public HeaderBarMenuListener( SashForm sashForm, Control control, GTableViewer gTableViewer ) {
        super(gTableViewer);
        this.sashForm = sashForm;
        this.control = control;
    }

    protected void maximize(  ) {
        if ( sashForm.getMaximizedControl(  ) == null ) {
            sashForm.setMaximizedControl( control );
        } else {
            sashForm.setMaximizedControl( null );
        }
    }

	protected void flipSash(  ) {
		sashForm.setOrientation( sashForm.getOrientation() == SWT.HORIZONTAL ? SWT.VERTICAL : SWT.HORIZONTAL );
	}

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
     */
    public void menuAboutToShow( IMenuManager menuManager ) {
        super.menuAboutToShow(menuManager);
        menuManager.add( new Separator(  ) );
        menuManager.add( new FlipSashAction( ) );
        menuManager.add( new MaximizeAction(  ) );
    }

    /**
     * MaximizeAction
     */
    public class MaximizeAction extends Action {
        public MaximizeAction(  ) {
            super(  );

            if ( sashForm.getMaximizedControl(  ) == null ) {
                setText( G2GuiResources.getString( "MISC_MAXIMIZE" ) );
				setImageDescriptor( G2GuiResources.getImageDescriptor( "maximize" ) );
            } else {
                setText( G2GuiResources.getString( "MISC_RESTORE" ) );
				setImageDescriptor( G2GuiResources.getImageDescriptor( "restore" ) );
            }
        }
        public void run(  ) {
            maximize(  );
        }
    }
    
	/**
     * FlipSashAction
     */
    public class FlipSashAction extends Action {
		public FlipSashAction(  ) {
			super( G2GuiResources.getString( "MISC_FLIP_SASH" ) );
			setImageDescriptor( G2GuiResources.getImageDescriptor( "rotate" ) );
		}
		public void run(  ) {
			flipSash( );
		}
	}
    
}


/*
$Log: HeaderBarMenuListener.java,v $
Revision 1.4  2003/10/22 01:37:10  zet
add column selector to server/search (might not be finished yet..)

Revision 1.3  2003/10/15 18:24:20  zet
icons

Revision 1.2  2003/10/01 21:16:16  zet
add flip sash menuitem

Revision 1.1  2003/09/27 00:27:55  zet
initial

*/
