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
import net.mldonkey.g2gui.view.G2Gui;
import net.mldonkey.g2gui.view.StatusLine;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * CoreConsoleItem
 *
 * @version $Id: CoreConsoleItem.java,v 1.4 2003/09/23 21:58:14 zet Exp $
 *
 */
public class CoreConsoleItem {
    private StatusLine statusLine;
    private Composite composite;
    private boolean linkEntryToggle = false;

	/**
	 * @param statusLine 
	 * @param mldonkey 
	 */
    public CoreConsoleItem( StatusLine statusLine, CoreCommunication mldonkey ) {
        this.statusLine = statusLine;
        this.composite = statusLine.getStatusline();
        createContents();
    }

    public void createContents() {
        Composite linkComposite = new Composite( composite, SWT.BORDER );
        linkComposite.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_CENTER ) );
        linkComposite.setLayout( new FillLayout() );
        CLabel link = new CLabel( linkComposite, SWT.BORDER );
        link.setImage( G2GuiResources.getImage( "G2GuiLogoSmall" ) );
        link.setToolTipText( G2GuiResources.getString( "CORE_CONSOLE_TOOLTIP" ) );
        link.addMouseListener( new MouseAdapter() {
            public void mouseDown( MouseEvent e ) {
                G2Gui.getCoreConsole().getShell().open();
            }
        } );
    }
}

/*
$Log: CoreConsoleItem.java,v $
Revision 1.4  2003/09/23 21:58:14  zet
not much..

Revision 1.3  2003/09/20 01:36:19  zet
*** empty log message ***

Revision 1.2  2003/09/18 11:37:24  lemmster
checkstyle

Revision 1.1  2003/09/03 14:49:07  zet
optionally spawn core from gui


*/
