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
 * ( at your option ) any later version.
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

import net.mldonkey.g2gui.view.helper.WidgetFactory;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.transfer.clientTable.ClientTableView;
import net.mldonkey.g2gui.view.transfer.clientTable.ClientViewFrame;
import net.mldonkey.g2gui.view.transfer.downloadTable.DownloadTableTreeView;
import net.mldonkey.g2gui.view.transfer.downloadTable.DownloadViewFrame;
import net.mldonkey.g2gui.view.transfer.uploadTable.UploadViewFrame;
import net.mldonkey.g2gui.view.transfer.uploadersTable.UploadersViewFrame;
import net.mldonkey.g2gui.view.viewers.GView;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

import java.util.Observable;


/**
 * TransferTab.java
 *
 * @version $Id: TransferTab.java,v 1.99 2003/11/29 17:21:22 zet Exp $
 *
 */
public class TransferTab extends GViewGuiTab {
    private GView clientTableView = null;
    private GView uploadTableView = null;
    private GView uploadersTableView = null;

    /**
     * @param gui where this tab belongs to
     */
    public TransferTab(MainWindow gui) {
        super(gui);
        createButton("TransfersButton");
        createContents(getContent());
    }

    /* ( non-Javadoc )
     * @see net.mldonkey.g2gui.view.G2guiTab#createContents( org.eclipse.swt.widgets.Composite )
     */
    protected void createContents(Composite composite) {
        String sashPrefString = "transferSash";

        SashForm sashForm = WidgetFactory.createSashForm(composite, sashPrefString);

        createDownloadsViews(sashForm);
        createUploadsView(sashForm);

        WidgetFactory.loadSashForm(sashForm, sashPrefString);
    }

    /**
     * Create the DownloadView(s) (if advancedMode, add a Client table)
     * @param sashForm
     */
    private void createDownloadsViews(SashForm sashForm) {
        if (PreferenceLoader.loadBoolean("advancedMode")) {
            String sashPrefString = "clientSash";
            SashForm sashForm2 = WidgetFactory.createSashForm(sashForm, sashPrefString);
            createDownloadsView(sashForm2);
            createClientsView(sashForm2);
            WidgetFactory.loadSashForm(sashForm2, sashPrefString);
        } else
            createDownloadsView(sashForm);
    }

    private void createDownloadsView(SashForm sashForm) {
        gView = new DownloadViewFrame(sashForm, "TT_Downloads", "TransfersButtonSmall", this).getGView();
    }

    // gView must be initialized before this is called
    private void createClientsView(SashForm sashForm) {
        clientTableView = new ClientViewFrame(sashForm, "TT_Clients", "TransfersButtonSmall", this,
                gView).getGView();
        ((DownloadTableTreeView) gView).setClientTableView((ClientTableView) clientTableView);
    }

    private void createUploadsView(SashForm sashForm) {
        String sashPrefString = "uploadsSash";
        SashForm sashForm2 = WidgetFactory.createSashForm(sashForm, sashPrefString);

        uploadTableView = new UploadViewFrame(sashForm2, "TT_Uploads", "UpArrowBlue", this).getGView();
        uploadersTableView = new UploadersViewFrame(sashForm2, "TT_Uploaders", "UpArrowBlue", this).getGView();

        WidgetFactory.loadSashForm(sashForm2, sashPrefString);
    }

    /* ( non-Javadoc )
     * @see net.mldonkey.g2gui.view.GuiTab#updateDisplay()
     */
    public void updateDisplay() {
        gView.updateDisplay();
        uploadTableView.updateDisplay();
        uploadersTableView.updateDisplay();

        if (clientTableView != null)
            clientTableView.updateDisplay();

        super.updateDisplay();
    }

    /**
     * @return GView
     */
    public GView getClientGView() {
        return this.clientTableView;
    }

    /**
     * @return GView
     */
    public GView getUploadGView() {
        return this.uploadTableView;
    }
    
    /**
     * @return GView
     */
    public GView getUploadersGView() {
        return this.uploadersTableView;
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable o, Object obj) {
    }
    
    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.GuiTab#setActive()
     */
    public void setActive() {
        gView.setActive(true);
        uploadersTableView.setActive(true);
        uploadTableView.setActive(true);
        clientTableView.setActive(true);
        super.setActive();
    }
	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.GuiTab#setInActive()
	 */
	public void setInActive() {
	   gView.setActive(false);
	   uploadersTableView.setActive(false);
	   uploadTableView.setActive(false);
	   clientTableView.setActive(false);
	   super.setInActive();
   }
}


/*
$Log: TransferTab.java,v $
Revision 1.99  2003/11/29 17:21:22  zet
minor cleanup

Revision 1.98  2003/11/29 17:01:00  zet
update for mainWindow

Revision 1.97  2003/11/29 01:51:53  zet
a few more viewframe changes.. will continue later.

Revision 1.96  2003/11/26 07:43:15  zet
quick attempt at an uploaders table w/proto 19 - still in progress...

Revision 1.95  2003/11/24 01:33:27  zet
move some classes

Revision 1.94  2003/11/23 17:58:03  lemmster
removed dead/unused code

Revision 1.93  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

Revision 1.92  2003/11/09 21:49:26  zet
uploads arrow

Revision 1.91  2003/11/09 02:18:37  zet
put some info in the headers

Revision 1.90  2003/11/08 22:47:15  zet
update client table header

Revision 1.89  2003/11/08 12:04:05  vnc
minor TT-header info adjustments

Revision 1.88  2003/11/06 15:40:50  zet
typo

Revision 1.87  2003/11/04 20:38:27  zet
update for transparent gifs

Revision 1.86  2003/10/31 22:41:59  zet
rename to View

Revision 1.85  2003/10/31 16:02:17  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.84  2003/10/31 13:20:31  lemmster
added PaneGuiTab and TableGuiTab
added "dropdown" button to all PaneGuiTabs (not finished yet, continue on monday)

Revision 1.83  2003/10/31 10:42:47  lemmster
Renamed GViewer, GTableViewer and GTableTreeViewer to GPage... to avoid mix-ups with StructuredViewer...
Removed IGViewer because our abstract class GPage do the job
Use supertype/interface where possible to keep the design flexible!

Revision 1.82  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.81  2003/10/29 16:56:21  lemmster
added reasonable class hierarchy for panelisteners, viewers...

Revision 1.80  2003/10/22 17:17:30  zet
common actions

Revision 1.79  2003/10/22 01:58:32  zet
comma delimited

Revision 1.78  2003/10/22 01:36:26  zet
add column selector to server/search (might not be finished yet..)

Revision 1.76  2003/10/20 00:13:03  zet
update header bar

Revision 1.75  2003/10/19 21:39:03  zet
columnselector support

Revision 1.74  2003/10/19 17:07:08  zet
check height

Revision 1.73  2003/10/16 23:56:53  zet
not much

Revision 1.72  2003/10/16 21:22:32  zet
toggle clients icon

Revision 1.71  2003/10/16 20:43:14  zet
remove null

Revision 1.70  2003/10/16 19:58:12  zet
icons

Revision 1.69  2003/10/16 16:10:05  zet
updateDisplay()

Revision 1.68  2003/10/12 23:14:23  zet
nil

Revision 1.67  2003/10/12 15:58:03  zet
rewrite downloads table & more..

Revision 1.66  2003/09/27 12:30:40  dek
upload-Table has now same show-Gridlines-behaviour as download-Table

Revision 1.65  2003/09/27 00:26:41  zet
put menu back

Revision 1.63  2003/09/26 17:19:55  zet
add refresh menuitem

Revision 1.62  2003/09/26 16:08:02  zet
dblclick header to maximize/restore

Revision 1.61  2003/09/26 15:45:59  dek
we now have upload-stats (well, kind of...)

Revision 1.60  2003/09/20 14:39:48  zet
move transfer package

Revision 1.59  2003/09/20 01:23:18  zet
*** empty log message ***

Revision 1.58  2003/09/18 13:01:23  lemmster
checkstyle

Revision 1.57  2003/09/16 02:12:17  zet
headerbar menu

Revision 1.56  2003/09/15 01:25:06  zet
move menu

Revision 1.55  2003/09/13 22:25:01  zet
rate, !rawrate

Revision 1.54  2003/09/04 20:40:49  vnc
removed ugly "@" sign, added "files at" text instead

Revision 1.53  2003/08/31 20:32:50  zet
active button states

Revision 1.52  2003/08/31 01:46:33  zet
localise

Revision 1.51  2003/08/30 13:55:10  zet
*** empty log message ***

Revision 1.50  2003/08/30 11:44:29  dek
client-Table is now own ViewForm, and some checkstyle work

Revision 1.49  2003/08/30 00:44:01  zet
move tabletree menu

Revision 1.48  2003/08/29 22:11:47  zet
add CCLabel helper class

Revision 1.47  2003/08/29 21:21:46  zet
sash_width

Revision 1.46  2003/08/29 19:30:50  zet
font colour

Revision 1.45  2003/08/29 18:28:42  zet
remove import Shell

Revision 1.44  2003/08/29 18:24:24  zet
localise

Revision 1.43  2003/08/29 17:45:35  zet
localise text

Revision 1.42  2003/08/29 17:33:20  zet
remove headerbar

Revision 1.41  2003/08/29 16:06:54  zet
optional shadow

Revision 1.40  2003/08/29 15:43:43  zet
try gradient headerbar

Revision 1.39  2003/08/28 22:44:30  zet
GridLayout helper class

Revision 1.38  2003/08/25 22:17:12  zet
*** empty log message ***

Revision 1.37  2003/08/24 16:37:04  zet
combine the preference stores

Revision 1.36  2003/08/24 02:34:16  zet
update sorter properly

Revision 1.35  2003/08/23 15:21:37  zet
remove @author

Revision 1.34  2003/08/23 14:58:38  lemmster
cleanup of MainTab, transferTree.* broken

Revision 1.33  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.32  2003/08/22 21:06:48  lemmster
replace $user$ with $Author: zet $

Revision 1.31  2003/08/21 10:12:10  dek
removed empty expression

Revision 1.30  2003/08/20 22:18:56  zet
Viewer updates

Revision 1.29  2003/08/20 18:35:16  zet
uploaders label

Revision 1.28  2003/08/20 14:58:43  zet
sources clientinfo viewer

Revision 1.27  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.26  2003/08/15 22:05:58  zet
*** empty log message ***

Revision 1.25  2003/08/11 00:30:10  zet
show queued files

Revision 1.24  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.23  2003/08/06 17:10:50  zet
check for shell.isdisposed

Revision 1.22  2003/08/04 19:22:08  zet
trial tabletreeviewer

Revision 1.21  2003/07/27 22:39:36  zet
small buttons toggle ( in popup ) for main cool menu

Revision 1.20  2003/07/24 02:22:46  zet
doesn't crash if no core is running

Revision 1.19  2003/07/23 04:08:07  zet
looks better with icons

Revision 1.18  2003/07/18 09:58:00  dek
checkstyle

Revision 1.17  2003/07/17 14:58:37  lemmstercvs01
refactored

Revision 1.3  2003/07/15 18:14:47  dek
Wow, nice piece of work already done, it works, looks nice, but still lots of things to do

Revision 1.2  2003/07/14 19:26:40  dek
done some clean.up work, since it seems, as if this view becomes reality..

Revision 1.1  2003/07/11 17:53:51  dek
Tree for Downloads - still unstable

*/
