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
package net.mldonkey.g2gui.view;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.helper.WidgetFactory;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.statusline.CoreConsoleItem;
import net.mldonkey.g2gui.view.statusline.LinkEntry;
import net.mldonkey.g2gui.view.statusline.LinkEntryItem;
import net.mldonkey.g2gui.view.statusline.NetworkItem;
import net.mldonkey.g2gui.view.statusline.SpeedItem;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


/**
 * Statusline, This class handles all the Information that should be visible all the time in a
 * so called Status-line. It is an own implementation of this widget, as the JFace-Statusline
 * is way too complicated for our simple needs. It has to be placed in a GridLayout, since it
 * applies a GridData object for its appearance.
 *
 *
 * @version $Id: StatusLine.java,v 1.23 2003/12/04 08:47:27 lemmy Exp $
 *
 */
public class StatusLine {
    private CLabel cLabel;
    private MainWindow mainTab;
    private CoreCommunication core;
    private Composite statusLineComposite;
    private Composite linkEntryComposite;

    /**
     * Creates a new StatusLine obj
     * @param mainTab The <code>MainTab></code> we display our content in
     */
    public StatusLine(MainWindow mainTab) {
        this.mainTab = mainTab;
        this.core = mainTab.getCore();
        createContents();
    }

    /**
     * createContents
     */
    private void createContents() {
        // Have we spawned a core?
        boolean spawnedCore = ((G2Gui.getCoreConsole() != null) &&
            PreferenceLoader.loadBoolean("advancedMode"));

        Composite mainComposite = new Composite(mainTab.getMainComposite(), SWT.BORDER);
        mainComposite.setLayout(WidgetFactory.createGridLayout(1, 0, 0, 0, 0, false));
        mainComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        /*
         * This hidden composite contains linkEntry which is displayed
         * on demand from linkEntryItem
         */
        createLinkEntry(mainComposite);

        /* the linkEntry */
        new LinkEntry(this, this.core, linkEntryComposite);

        statusLineComposite = new Composite(mainComposite, SWT.NONE);
        statusLineComposite.setLayout(WidgetFactory.createGridLayout(spawnedCore ? 9 : 7, 0, 0, 0, 0, false));
        statusLineComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        /*
         *  the left field
         */
        new NetworkItem(this, this.core);
        addSeparator(statusLineComposite);

        if (spawnedCore) {
            new CoreConsoleItem(this);
            addSeparator(statusLineComposite);
        }

        /* the toggle for linkEntry */
        new LinkEntryItem(this);

        addSeparator(statusLineComposite);

        /* the fill field */
        Composite middle = new Composite(statusLineComposite, SWT.NONE);
        middle.setLayout(new FillLayout());
        middle.setLayoutData(new GridData(GridData.FILL_BOTH));
        cLabel = new CLabel(middle, SWT.BORDER);
        cLabel.setText("");

        addSeparator(statusLineComposite);

        /* the right field */
        new SpeedItem(this, this.core);
    }

    /**
     * @param composite
     */
    private void addSeparator(Composite composite) {
        Label separator = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL);
        GridData gd = new GridData(GridData.FILL_VERTICAL);

        // heightHint of 0 is nescessary so that this Label doesn't blow up the status-line
        gd.heightHint = 0;
        separator.setLayoutData(gd);
    }

    /**
     * Create the hidden linkEntryComposite
     * @param parent Composite
     */
    private void createLinkEntry(Composite parent) {
        linkEntryComposite = new Composite(parent, SWT.NONE);
        linkEntryComposite.setLayout(WidgetFactory.createGridLayout(2, 0, 0, 0, 0, false));

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.heightHint = 0;
        linkEntryComposite.setLayoutData(gd);
    }

    /**
     * Updates a String at a specific position
     * @param aString The new String to display
     */
    public void update(String aString) {
        if (!cLabel.isDisposed()) {
            cLabel.setText(aString);
        }
    }

    /**
     *         Sets the tooltip of the Statusbar Item
     * @param aString The tooltip to show
     */
    public void updateToolTip(String aString) {
        if (!cLabel.isDisposed()) {
            cLabel.setToolTipText(aString);
        }
    }

    /**
     * @return the Composite in which the statusline is created
     */
    public Composite getStatusline() {
        return statusLineComposite;
    }

    /**
     * @return the LinkEntryComposite
     */
    public Composite getLinkEntryComposite() {
        return linkEntryComposite;
    }

    /**
     * @return The maintab we are started from
     */
    public MainWindow getMainTab() {
        return mainTab;
    }
}


/*
$Log: StatusLine.java,v $
Revision 1.23  2003/12/04 08:47:27  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.22  2003/11/29 17:01:00  zet
update for mainWindow

Revision 1.21  2003/11/23 17:58:03  lemmy
removed dead/unused code

Revision 1.20  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

Revision 1.19  2003/11/09 04:13:38  zet
cleanup

Revision 1.18  2003/10/31 22:06:28  zet
fix status line when spawning a core

Revision 1.17  2003/10/22 15:46:06  dek
flattened status bar

Revision 1.16  2003/10/17 03:36:50  zet
use toolbar

Revision 1.15  2003/09/18 09:44:57  lemmy
checkstyle

Revision 1.14  2003/09/03 14:49:07  zet
optionally spawn core from gui

Revision 1.13  2003/08/28 22:44:30  zet
GridLayout helper class

Revision 1.12  2003/08/28 16:07:48  zet
update linkentry

Revision 1.11  2003/08/25 12:24:09  zet
Toggleable link entry.  It should parse links from pasted HTML as well.

Revision 1.10  2003/08/23 15:21:37  zet
remove @author

Revision 1.9  2003/08/22 21:06:48  lemmy
replace $user$ with $Author: lemmy $

Revision 1.8  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.7  2003/08/10 12:59:01  lemmy
"manage servers" in NetworkItem implemented

Revision 1.6  2003/08/02 19:32:43  zet
marginwidths

Revision 1.5  2003/08/02 12:56:47  zet
size statusline

Revision 1.4  2003/07/31 14:10:58  lemmy
reworked

Revision 1.3  2003/07/29 09:41:56  lemmy
still in progress, removed networkitem for compatibility

Revision 1.2  2003/07/18 04:34:22  lemmy
checkstyle applied

Revision 1.1  2003/07/17 14:58:37  lemmy
refactored

Revision 1.7  2003/06/28 09:50:12  lemmy
ResourceBundle added

Revision 1.6  2003/06/28 09:39:30  lemmy
added isDisposed() check

Revision 1.5  2003/06/27 13:37:28  dek
tooltips added

Revision 1.4  2003/06/27 13:21:12  dek
added connected Networks

Revision 1.3  2003/06/26 21:11:10  dek
speed is shown

Revision 1.2  2003/06/26 14:11:58  dek
NPE fixed ;-)

Revision 1.1  2003/06/26 14:04:59  dek
Class statusline for easy information display

*/
