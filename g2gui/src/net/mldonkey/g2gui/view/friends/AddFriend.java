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
package net.mldonkey.g2gui.view.friends;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.view.helper.CGridLayout;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * AddFriend
 *
 * @version $Id: AddFriend.java,v 1.7 2003/09/20 01:20:26 zet Exp $
 */
public class AddFriend {
    private Shell shell;
    private Display desktop = Display.getDefault();
    private int width = 300;
    private int height = 200;

	/**
	 * @param core 
	 */
    public AddFriend( final CoreCommunication core ) {
        shell = new Shell( SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL );
        shell.setBounds( ( desktop.getBounds().width - width ) / 2,
                         ( desktop.getBounds().height - height ) / 2, width, height );

        shell.setImage( G2GuiResources.getImage( "ProgramIcon" ) );
        shell.setText( G2GuiResources.getString( "FR_MENU_ADD_BY_IP" ) );
        GridLayout gridLayout = CGridLayout.createGL( 2, 5, 5, 10, 5, false );
        shell.setLayout( gridLayout );

        Label hostLabel = new Label( shell, SWT.NONE );
        hostLabel.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
        hostLabel.setText( G2GuiResources.getString( "FR_ADD_HOST" ) + ":" );

        final Text host = new Text( shell, SWT.BORDER );
        host.setText( "192.168.1.1" );
        host.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

        Label portLabel = new Label( shell, SWT.NONE );
        portLabel.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
        portLabel.setText( G2GuiResources.getString( "FR_ADD_PORT" ) + ":" );

        final Text port = new Text( shell, SWT.BORDER );
        port.setText( "4662" );
        port.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

        Button okButton = new Button( shell, SWT.NONE );
        okButton.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        okButton.setText( G2GuiResources.getString( "BTN_OK" ) );
        okButton.addSelectionListener( new SelectionAdapter() {
                public void widgetSelected( SelectionEvent s ) {
                    String string = "afr " + host.getText() + " " + port.getText();
                    Message consoleMessage = new EncodeMessage( Message.S_CONSOLEMSG, string );
                    consoleMessage.sendMessage( core );
                    consoleMessage = null;
                    shell.close();
                }
            } );

        Button cancelButton = new Button( shell, SWT.NONE );
        cancelButton.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        cancelButton.setText( G2GuiResources.getString( "BTN_CANCEL" ) );
        cancelButton.addSelectionListener( new SelectionAdapter() {
                public void widgetSelected( SelectionEvent s ) {
                    shell.close();
                }
            } );
        shell.pack();
        shell.open();
    }
}

/*
$Log: AddFriend.java,v $
Revision 1.7  2003/09/20 01:20:26  zet
*** empty log message ***

Revision 1.6  2003/09/18 15:29:46  zet
centralize writeStream in core
handle IOException rather than throwing it away

Revision 1.5  2003/09/18 09:54:45  lemmster
checkstyle

Revision 1.4  2003/08/31 15:40:12  zet
port

Revision 1.3  2003/08/31 00:08:59  zet
add buttons

Revision 1.2  2003/08/29 21:05:09  zet
remove imports

Revision 1.1  2003/08/29 21:02:45  zet
Add friend by ip




*/
