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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


/**
 * Emulated tooltip handler
 * Notice that we could display anything in a tooltip besides text and images.
 * For instance, it might make sense to embed large tables of data or buttons linking
 * data under inspection to material elsewhere, or perform dynamic lookup for creating
 * tooltip text on the fly.
 */
class ToolTipHandler implements Listener {
	private ToolTip solid, liquid;
	private Event event;
	private Event lastEvent = new Event();
	private Point lastPoint = new Point( 0, 0 );
	private boolean locked;
	/**
     * Creates a new tooltip handler
     *
     * @param parent the parent Shell
     */
    public ToolTipHandler(Composite parent) {
    	parent.addListener(SWT.KeyDown, this);
        parent.addListener(SWT.MouseExit, this);
        parent.addListener(SWT.MouseMove, this);
        parent.addListener(SWT.MouseDown, this);
        parent.addListener(SWT.MouseHover, this);
        parent.addListener(SWT.Close, this);

        this.solid = new SolidToolTip( parent, this );
        this.liquid = new LiquidToolTip( parent );
        }

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event e) {
		// dont do anything if one of them is null
		if ( solid == null || liquid == null ) return;

		// this is damn complicated because MouseHover is sent as long as the mouse is hovering
		switch ( e.type ) {
			case SWT.MouseHover:
				if ( lastEvent == event 
					&& lastEvent.x == event.x 
					&& lastEvent.y == event.y 
					|| locked ) return;
				liquid.dispose();
				liquid.create( e );
				event = e;
				break;

			case SWT.KeyDown:
				if ( e.keyCode != SWT.F2 ) return;
				liquid.dispose();
				solid.dispose();
				solid.create( event );
				break;

			case SWT.MouseDown:
				liquid.dispose();
				solid.dispose();
				break;
			
			case SWT.MouseExit:
				if ( solid.isVisisble() && solid.contains( e.x, e.y ) ) return;
				liquid.dispose();
				solid.dispose();
				break;
				
			case SWT.Close:
				if ( locked ) {
					solid.dispose();
				}
				break;
				
			default:
				liquid.dispose();
				break;
		}
		lastEvent = e;
	}
	
	public void setLocked( boolean b ) {
		this.locked = b;
	}
}
/*
$Log: ToolTipHandler.java,v $
Revision 1.1  2003/11/29 13:03:54  lemmster
ToolTip complete reworked (to be continued)

*/