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

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.ClientStats;
import net.mldonkey.g2gui.view.StatusLine;
import net.mldonkey.g2gui.view.helper.WidgetFactory;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * SpeedItem
 *
 *
 * @version $Id: SpeedItem.java,v 1.27 2003/12/04 08:47:27 lemmy Exp $
 *
 */
public class SpeedItem implements Observer {
	private static final DecimalFormat decimalFormat = new DecimalFormat( "0.##" );
    private Composite composite;
    private CLabel cLabelDown;
    private CLabel cLabelUp;
    private int speedLength = 0;
    private String downRateTCP;
    private String upRateTCP;

	/**
	 * @param statusline 
	 * @param mldonkey 
	 */
    public SpeedItem( StatusLine statusline, CoreCommunication mldonkey ) {
        this.composite = statusline.getStatusline();
        this.createContent();
        mldonkey.getClientStats().addObserver( this );
        mldonkey.addObserver( this );
    }

    /**
     * Create the content layout
     */
    private void createContent() {
        composite = new Composite( composite, SWT.NONE );
        composite.setLayoutData(new GridData( GridData.FILL_VERTICAL ) );
        GridLayout gridLayout = WidgetFactory.createGridLayout( 2, 0, 0, 0, 0, false );
        composite.setLayout( gridLayout );

        /* down rate */
        cLabelDown = new CLabel( composite, SWT.RIGHT );
        cLabelDown.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

        /* up rate */
        cLabelUp = new CLabel( composite, SWT.NONE );
        cLabelUp.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update( Observable o, final Object arg ) {
        if ( composite.isDisposed() )
            return;
        composite.getDisplay().asyncExec( new Runnable() {
                public void run() {
                    if ( arg instanceof IOException )
                        updateDisconnected( ( IOException ) arg );
                    else if ( arg instanceof ClientStats )
                        updateClientStats( ( ClientStats ) arg );
                }
            } );
    }

    /**
     * Update the statusline with stats
	 *
     * @param stats The <code>ClientStats</code> to parse
     */
    public void updateClientStats( ClientStats stats ) {
        if ( composite.isDisposed() )
            return;
        if ( speedLength == 0 ) {
            cLabelDown.setImage( G2GuiResources.getImage( "DownArrow" ) );
            cLabelUp.setImage( G2GuiResources.getImage( "UpArrow" ) );
        }
        downRateTCP = decimalFormat.format( stats.getTcpDownRate() ) + " kb/s";
        upRateTCP = decimalFormat.format( stats.getTcpUpRate() ) + " kb/s";

        /* first the text */
        cLabelDown.setText( downRateTCP );
        cLabelUp.setText( upRateTCP );

        /* now the tooltip */
        cLabelDown.setToolTipText( "TCP: " + downRateTCP + " | UDP: "
                                   + decimalFormat.format( stats.getUdpDownRate() ) + " kb/s | Total: "
                                   + decimalFormat.format( stats.getTcpDownRate()
                                                           + stats.getUdpDownRate() ) + " kb/s" );
        cLabelUp.setToolTipText( "TCP: " + upRateTCP + " | UDP: "
                                 + decimalFormat.format( stats.getUdpUpRate() ) + " kb/s | Total: "
                                 + decimalFormat.format( stats.getTcpUpRate() + stats.getUdpUpRate() )
                                 + " kb/s" );
        // only run Layout() if needed.. it seems to be an expensive call
        if ( speedLength < ( downRateTCP.length() + upRateTCP.length() ) ) {
            cLabelUp.getParent().getParent().layout();
            speedLength = downRateTCP.length() + upRateTCP.length();
        }
    }

    /**
     * Update statusline when disconnected
     *
     * @param e The IOException to use
     */
    public void updateDisconnected( IOException e ) {
        if ( composite.isDisposed() )
            return;
        cLabelDown.setImage( G2GuiResources.getImage( "RedCrossSmall" ) );
        cLabelDown.setText( G2GuiResources.getString( "MISC_DISCONNECTED" ) );
        cLabelDown.setToolTipText( "" );
        cLabelUp.setImage( null );
        cLabelUp.setText( "" );
        cLabelUp.setToolTipText( "" );
        speedLength = 0;
        cLabelUp.getParent().getParent().layout();
		speedLength = 0;
    }
}

/*
$Log: SpeedItem.java,v $
Revision 1.27  2003/12/04 08:47:27  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.26  2003/11/23 17:58:03  lemmy
removed dead/unused code

Revision 1.25  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

Revision 1.24  2003/11/20 15:44:56  dek
reconnect started

Revision 1.23  2003/10/22 15:46:06  dek
flattened status bar

Revision 1.22  2003/10/17 03:36:26  zet
not much

Revision 1.21  2003/09/18 11:37:24  lemmy
checkstyle

Revision 1.20  2003/09/17 14:46:30  zet
localise

Revision 1.19  2003/09/17 14:41:50  zet
update on disconnect

Revision 1.18  2003/09/13 22:24:17  zet
Layout() only when necessary

Revision 1.17  2003/08/28 22:44:30  zet
GridLayout helper class

Revision 1.16  2003/08/23 15:21:37  zet
remove @author

Revision 1.15  2003/08/22 21:13:11  lemmy
replace $user$ with $Author: lemmy $

Revision 1.14  2003/08/17 23:13:42  zet
centralize resources, move images

Revision 1.13  2003/08/15 22:05:58  zet
*** empty log message ***

Revision 1.12  2003/08/02 19:32:14  zet
layout each time

Revision 1.11  2003/08/02 12:56:57  zet
size statusline

Revision 1.10  2003/08/01 17:21:19  lemmy
reworked observer/observable design, added multiversion support

Revision 1.9  2003/07/31 14:11:06  lemmy
reworked

Revision 1.8  2003/07/17 14:58:56  lemmy
refactored

Revision 1.7  2003/06/28 09:37:18  lemmy
syncExec() -> asyncExec()

Revision 1.6  2003/06/27 13:37:28  dek
tooltips added

Revision 1.5  2003/06/27 13:21:12  dek
added connected Networks

Revision 1.4  2003/06/27 11:34:00  lemmy
foobar

Revision 1.3  2003/06/27 11:07:52  lemmy
CoreCommunications implements addObserver(Observer)

Revision 1.2  2003/06/27 10:36:17  lemmy
changed notify to observer/observable

Revision 1.1  2003/06/26 21:11:10  dek
speed is shown

*/
