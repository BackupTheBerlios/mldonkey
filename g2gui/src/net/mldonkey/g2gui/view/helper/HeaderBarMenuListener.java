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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Control;


/**
 * HeaderBarMenusListener - helper for HeaderBar Menus
 *
 * @version $Id: HeaderBarMenuListener.java,v 1.1 2003/09/27 00:27:55 zet Exp $
 *
 */
public class HeaderBarMenuListener implements IMenuListener {
    private SashForm sashForm;
    private Control control;

    public HeaderBarMenuListener( SashForm sashForm, Control control ) {
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

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
     */
    public void menuAboutToShow( IMenuManager menuManager ) {
        menuManager.add( new Separator(  ) );
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
            } else {
                setText( G2GuiResources.getString( "MISC_RESTORE" ) );
            }
        }

        public void run(  ) {
            maximize(  );
        }
    }
}


/*
$Log: HeaderBarMenuListener.java,v $
Revision 1.1  2003/09/27 00:27:55  zet
initial

*/
