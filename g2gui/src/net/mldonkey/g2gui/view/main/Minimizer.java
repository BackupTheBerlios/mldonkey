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
package net.mldonkey.g2gui.view.main;

import java.text.DecimalFormat;

import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.ClientStats;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.widgets.Shell;

/**
 * A temp class to help use swt TrayIcon on win32 only
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=30834
 *
 * @version $Id: Minimizer.java,v 1.3 2003/09/18 10:12:53 lemmster Exp $
 */
public class Minimizer implements Observer {
	private static final DecimalFormat decimalFormat = new DecimalFormat( "0.#" );
    private Shell shell;
    private CoreCommunication core;
    private String titleBarText;

	/**
	 * DOCUMENT ME!
	 * 
	 * @param shell DOCUMENT ME!
	 * @param core DOCUMENT ME!
	 * @param titleBarText DOCUMENT ME!
	 */
    public Minimizer( Shell shell, CoreCommunication core, String titleBarText ) {
        this.shell = shell;
        this.core = core;
        this.titleBarText = titleBarText;
    }

    /**
     * DOCUMENT ME!
     */
    public void setTitleBarText() {
        shell.setText( titleBarText + " " + G2GuiResources.getString( "BUILD_INFORMATION" ) );
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean close() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param systemTray DOCUMENT ME!
     */
    public void minimize( boolean systemTray ) {
        core.getClientStats().addObserver( this );
    }

    /**
     * DOCUMENT ME!
     */
    public void restore() {
        core.getClientStats().deleteObserver( this );
        setTitleBarText();
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     * @param receivedInfo DOCUMENT ME!
     */
    public void update( Observable arg0, Object receivedInfo ) {
        final ClientStats clientInfo = ( ClientStats ) receivedInfo;
        if ( !shell.isDisposed() )
            shell.getDisplay().syncExec( new Runnable() {
                    public void run() {
                        if ( !shell.isDisposed() && shell.getMinimized() ) {
                            String prependText =
                                           "(D:" + decimalFormat.format( clientInfo.getTcpDownRate() )
                                           + ")" + "(U:"
                                           + decimalFormat.format( clientInfo.getTcpUpRate() ) + ")";
                            shell.setText( prependText + " : " + titleBarText );
                        }
                    }
                } );

    }
}

/*
$Log: Minimizer.java,v $
Revision 1.3  2003/09/18 10:12:53  lemmster
checkstyle

Revision 1.2  2003/09/08 17:28:59  zet
more

Revision 1.1  2003/09/08 15:39:51  zet
minimizer



*/
