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
package net.mldonkey.g2gui.view.statusline;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.StatusLine;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * LinkEntryItem
 *
 * @version $Id: LinkEntryItem.java,v 1.3 2003/09/18 11:37:24 lemmster Exp $
 *
 */
public class LinkEntryItem {
    private Composite linkEntryComposite;
    private StatusLine statusLine;
    private Composite composite;
    private boolean linkEntryToggle = false;

	/**
	 * DOCUMENT ME!
	 *  
	 * @param statusLine DOCUMENT ME!
	 * @param mldonkey DOCUMENT ME!
	 */
    public LinkEntryItem( StatusLine statusLine, CoreCommunication mldonkey ) {
        this.linkEntryComposite = statusLine.getLinkEntryComposite();
        this.statusLine = statusLine;
        this.composite = statusLine.getStatusline();
        createContents();
    }

    /**
     * DOCUMENT ME!
     */
    public void createContents() {
        Composite linkComposite = new Composite( composite, SWT.BORDER );
        linkComposite.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_CENTER ) );
        linkComposite.setLayout( new FillLayout() );
        CLabel link = new CLabel( linkComposite, SWT.BORDER );
        link.setImage( G2GuiResources.getImage( "UpArrowBlue" ) );
        link.setToolTipText( G2GuiResources.getString( "LE_TOGGLE" ) );
        link.addMouseListener( new MouseListener() {
                // hide/show the linkEntry
                public void mouseDown( MouseEvent e ) {
                    GridData g = new GridData( GridData.FILL_HORIZONTAL );
                    if ( linkEntryToggle )
                        g.heightHint = 0;
                    else
                        g.heightHint = 75;
                    linkEntryToggle = !linkEntryToggle;
                    linkEntryComposite.setLayoutData( g );
                    statusLine.getMainTab().getMainComposite().layout();
                }

                public void mouseUp( MouseEvent e ) {
                }

                public void mouseDoubleClick( MouseEvent e ) {
                }
            } );
    }
}

/*
$Log: LinkEntryItem.java,v $
Revision 1.3  2003/09/18 11:37:24  lemmster
checkstyle

Revision 1.2  2003/08/28 16:07:48  zet
update linkentry

Revision 1.1  2003/08/25 12:24:09  zet
Toggleable link entry.  It should parse links from pasted HTML as well.


*/
