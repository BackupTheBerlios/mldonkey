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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


/**
 * Emulated tooltip handler
 * Notice that we could display anything in a tooltip besides text and images.
 * For instance, it might make sense to embed large tables of data or buttons linking
 * data under inspection to material elsewhere, or perform dynamic lookup for creating
 * tooltip text on the fly.
 * 
 * ToolTipHandler
 * 
 * @version $Id: ToolTipHandler.java,v 1.2 2003/11/30 09:31:26 lemmster Exp $
 */
class ToolTipHandler implements Listener {
	private ToolTip solid, liquid;
	private Event event, lastEvent;

	/**
     * Creates a new tooltip handler
     * @param parent the parent Shell
     */
    public ToolTipHandler(Composite parent) {
    	// initialize the helpers
    	lastEvent = new Event();
    	lastEvent.x = 0;
    	lastEvent.y = 0;
    	event = lastEvent;

    	// create the two different tooltips
    	this.solid = new SolidToolTip( parent );
    	this.liquid = new LiquidToolTip( parent );
    	
    	// register for this events by the parent
    	parent.addListener(SWT.KeyDown, this);
        parent.addListener(SWT.MouseExit, this);
        parent.addListener(SWT.MouseMove, this);
        parent.addListener(SWT.MouseDown, this);
        parent.addListener(SWT.MouseHover, this);
        parent.addListener(SWT.Close, this);
    }

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event e) {
		// this is damn complicated because MouseHover is sent as long as the mouse is hovering
		// this results in flickering tooltip :(
		switch ( e.type ) {
			// listen for mousehover as long as solid isnt active or the last event was mousehover too
			case SWT.MouseHover:
				if ( lastEvent.type != e.type && !solid.isVisisble() ) {
					liquid.dispose();
					liquid.create( e );
					// store this event for solid
					event = e;
				}
				break;

			// close liquid on any key. open solid on F2
			case SWT.KeyDown:
				liquid.dispose();
				if ( e.keyCode == SWT.F2 ) {
					solid.dispose();
					solid.create( event );
				}
				break;

			// close liquid as well as solid
			case SWT.MouseDown:
				liquid.dispose();
				solid.dispose();
				break;
			
			// close liquid always. solid only on leaving the parent area
			case SWT.MouseExit:
				liquid.dispose();
				if ( !solid.isVisisble() && !solid.contains( e.x, e.y ) )
					solid.dispose();
				break;
				
			// close solid on close event (ESC)
			case SWT.Close:
				if ( solid.isVisisble() )
					solid.dispose();
				break;
				
			// mouse move close liquid
			default:
				liquid.dispose();
				break;
		}
		// we need to know the last event
		lastEvent = e;
	}
}
/*
$Log: ToolTipHandler.java,v $
Revision 1.2  2003/11/30 09:31:26  lemmster
ToolTip complete reworked (complete)

Revision 1.1  2003/11/29 13:03:54  lemmster
ToolTip complete reworked (to be continued)

*/