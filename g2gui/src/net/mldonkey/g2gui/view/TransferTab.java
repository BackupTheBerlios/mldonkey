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

import gnu.trove.TIntObjectIterator;

import java.util.Observable;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.FileInfoIntMap;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.view.helper.CCLabel;
import net.mldonkey.g2gui.view.helper.CGridLayout;
import net.mldonkey.g2gui.view.helper.MaximizeSashMouseAdapter;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.transfer.DownloadPaneMenuListener;
import net.mldonkey.g2gui.view.transfer.clientTable.ClientPaneListener;
import net.mldonkey.g2gui.view.transfer.clientTable.ClientTableView;
import net.mldonkey.g2gui.view.transfer.downloadTable.DownloadTableTreeView;
import net.mldonkey.g2gui.view.transfer.uploadTable.UploadPaneListener;
import net.mldonkey.g2gui.view.transfer.uploadTable.UploadTableView;
import net.mldonkey.g2gui.view.viewers.CustomTableTreeViewer;
import net.mldonkey.g2gui.view.viewers.GPaneListener;
import net.mldonkey.g2gui.view.viewers.GView;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;


/**
 * TransferTab.java
 *
 * @version $Id: TransferTab.java,v 1.89 2003/11/08 12:04:05 vnc Exp $
 *
 */
public class TransferTab extends TableGuiTab {
    private CLabel downloadCLabel;
    private CoreCommunication mldonkey;
    private GView clientTableView = null;
    private Composite downloadComposite;
    private MenuManager popupMenuDL;
    private MenuManager popupMenuUL;
    private MenuManager popupMenuCL = null;
    private GView uploadTableView = null;
    private String oldDLabelText = "";
    private long lastLabelUpdate = 0;
    boolean advancedMode = PreferenceLoader.loadBoolean("advancedMode");

    /**
     * @param gui where this tab belongs to
     */
    public TransferTab(MainTab gui) {
        super(gui);
        this.mldonkey = gui.getCore();
        createButton("TransfersButton", G2GuiResources.getString("TT_TransfersButton"), G2GuiResources.getString("TT_TransfersButtonToolTip"));
        createContents(this.subContent);
    }

    /* ( non-Javadoc )
     * @see net.mldonkey.g2gui.view.G2guiTab#createContents( org.eclipse.swt.widgets.Composite )
     */
    protected void createContents(Composite aComposite) {
        final SashForm mainSashForm = new SashForm(aComposite, (PreferenceLoader.loadBoolean("transferSashVertical") ? SWT.VERTICAL : SWT.HORIZONTAL));

        mainSashForm.addDisposeListener(new DisposeListener() {
                public void widgetDisposed(DisposeEvent e) {
                    PreferenceStore p = PreferenceLoader.getPreferenceStore();
                    p.setValue("transferSashVertical", ((mainSashForm.getOrientation() == SWT.VERTICAL) ? true : false));
                }
            });

        Control downloadParent;
        if (advancedMode) {
            downloadParent = new SashForm(mainSashForm, (PreferenceLoader.loadBoolean("clientSashHorizontal") ? SWT.HORIZONTAL : SWT.VERTICAL));
        }
        else {
        	downloadParent = mainSashForm;
        }

        ViewForm downloadViewForm = new ViewForm((SashForm) downloadParent,
                SWT.BORDER | (PreferenceLoader.loadBoolean("flatInterface") ? SWT.FLAT : SWT.NONE));

        if (advancedMode) {
            createClientViewForm((SashForm) downloadParent);
        } 
        else {
            downloadParent = downloadViewForm;
        }

        createDownloadHeader(downloadViewForm, mainSashForm, downloadParent);

		GPaneListener aListener = new DownloadPaneMenuListener(this, mldonkey, mainSashForm, downloadParent );
        createPaneToolBar(downloadViewForm, downloadParent, aListener);

        downloadComposite = new Composite(downloadViewForm, SWT.NONE);
        downloadComposite.setLayout(new FillLayout());
		downloadViewForm.setContent(downloadComposite);

        createUploads(mainSashForm);

        gView = new DownloadTableTreeView(downloadComposite, clientTableView, mldonkey, this);

        popupMenuDL.addMenuListener( aListener );

        mainSashForm.setWeights(new int[] { 1, 1 });
        mainSashForm.setMaximizedControl(downloadParent);

        mldonkey.getFileInfoIntMap().addObserver(this);
    }

    /**
     * Create the download pane header with popup menu
     *
     * @param parentViewForm
     */
    public void createDownloadHeader(ViewForm aViewForm, final SashForm aSashForm, final Control downloadParent) {
        popupMenuDL = new MenuManager("");
        popupMenuDL.setRemoveAllWhenShown(true);
        downloadCLabel = CCLabel.createCL(aViewForm, "TT_Downloads", "TransfersButtonSmall");
        downloadCLabel.addMouseListener(new MaximizeSashMouseAdapter(downloadCLabel, popupMenuDL, aSashForm, downloadParent));
        ( (ViewForm) aViewForm ).setTopLeft(downloadCLabel);
    }

    private void createPaneToolBar(ViewForm aViewForm, final Control aControl, IMenuListener menuListener) {
        ToolBar downloadsToolBar = new ToolBar(aViewForm, SWT.RIGHT | SWT.FLAT);
        ToolItem toolItem;

        toolItem = new ToolItem(downloadsToolBar, SWT.NONE);
        toolItem.setToolTipText(G2GuiResources.getString("TT_D_TT_COMMIT_ALL"));
        toolItem.setImage(G2GuiResources.getImage("commit"));
        toolItem.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent s) {
                    mldonkey.getFileInfoIntMap().commitAll();
                }
            });

        if (advancedMode) {
            toolItem = new ToolItem(downloadsToolBar, SWT.NONE);
            toolItem.setToolTipText(G2GuiResources.getString("TT_D_TT_SHOW_CLIENTS"));
            toolItem.setImage(G2GuiResources.getImage("split-table"));
            toolItem.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent s) {
                        ( (DownloadTableTreeView) gView ).toggleClientsTable();
                    }
                });
        }

        toolItem = new ToolItem(downloadsToolBar, SWT.NONE);
        toolItem.setToolTipText(G2GuiResources.getString("TT_D_TT_COLLAPSE_ALL"));
        toolItem.setImage(G2GuiResources.getImage("collapseAll"));
        toolItem.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent s) {
                    ( (CustomTableTreeViewer) gView.getViewer() ).collapseAll();
                }
            });

        toolItem = new ToolItem(downloadsToolBar, SWT.NONE);
        toolItem.setToolTipText(G2GuiResources.getString("TT_D_TT_EXPAND_ALL"));
        toolItem.setImage(G2GuiResources.getImage("expandAll"));
        toolItem.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent s) {
                    ( (CustomTableTreeViewer) gView.getViewer() ).expandAll();
                }
            });
            
        super.createPaneToolBar( downloadsToolBar, menuListener );   

        aViewForm.setTopRight(downloadsToolBar);
    }

    /**
     * Create the uploads window
     *
     * @param mainSashForm
     */
    public void createUploads(final SashForm aSashForm) {
        ViewForm uploadsViewForm = new ViewForm(aSashForm, SWT.BORDER | (PreferenceLoader.loadBoolean("flatInterface") ? SWT.FLAT : SWT.NONE));
        uploadsViewForm.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite uploadersComposite = new Composite(uploadsViewForm, SWT.NONE);
        uploadersComposite.setLayout(new FillLayout());
        uploadTableView = new UploadTableView(uploadersComposite, mldonkey, this);
        createUploadHeader(uploadsViewForm, aSashForm, uploadsViewForm);
        uploadsViewForm.setContent(uploadersComposite);
    }

    public void createUploadHeader(ViewForm parentViewForm, final SashForm mainSashForm, final Control uploadParent) {
        popupMenuUL = new MenuManager("");
        popupMenuUL.setRemoveAllWhenShown(true);
        popupMenuUL.addMenuListener(new UploadPaneListener(this, mldonkey, mainSashForm, parentViewForm));

        CLabel uploadsCLabel = CCLabel.createCL(parentViewForm, "TT_Uploads", "TransfersButtonSmall");
        uploadsCLabel.addMouseListener(new MaximizeSashMouseAdapter(uploadsCLabel, popupMenuUL, mainSashForm, uploadParent));
        parentViewForm.setTopLeft(uploadsCLabel);
    }

    /**
     * Create the hidden client view form
     *
     * @param parentSash
     */
    public Composite createClientViewForm(final SashForm aSashForm) {
        aSashForm.addDisposeListener(new DisposeListener() {
                public void widgetDisposed(DisposeEvent e) {
                    PreferenceStore p = PreferenceLoader.getPreferenceStore();
                    p.setValue("clientSashHorizontal", ((aSashForm.getOrientation() == SWT.HORIZONTAL) ? true : false));
                }
            });

        ViewForm clientViewForm = new ViewForm(aSashForm, SWT.BORDER | (PreferenceLoader.loadBoolean("flatInterface") ? SWT.FLAT : SWT.NONE));

        CLabel clientCLabel = CCLabel.createCL(clientViewForm, "TT_Clients", "TransfersButtonSmall");

        Composite downloadClients = new Composite(clientViewForm, SWT.NONE);
        downloadClients.setLayout(CGridLayout.createGL(1, 0, 0, 0, 0, false));
        clientViewForm.setContent(downloadClients);
        clientViewForm.setTopLeft(clientCLabel);
        downloadClients.addControlListener(new ControlAdapter() {
                public void controlResized(ControlEvent e) {
                    Composite c = (Composite) e.widget;
                    int width = c.getBounds().width;
                    int height = c.getBounds().height;

                    if ((width > 0) && (height > 0) && (gView != null)) {
						( (DownloadTableTreeView) gView ).updateClientsTable(true);
                    } else {
						( (DownloadTableTreeView) gView ).updateClientsTable(false);
                    }
                }
            });
        createClientTableViewer(downloadClients, aSashForm);
        aSashForm.setWeights(new int[] { 100, 0 });
        popupMenuCL = new MenuManager("");

        popupMenuCL.setRemoveAllWhenShown(true);
        popupMenuCL.addMenuListener(new ClientPaneListener(this, mldonkey, aSashForm, clientViewForm));
        clientCLabel.addMouseListener(new MaximizeSashMouseAdapter(clientCLabel, popupMenuCL, aSashForm, clientViewForm));

        return downloadClients;
    }

    /**
     * Create the hidden client table viewer
     *
     * @param parent
     * @param parentSash
     */
    public void createClientTableViewer(Composite aComposite, final SashForm aSashForm) {
        clientTableView = new ClientTableView(aComposite, mldonkey);
    }

    /**
     * Update the label, in the gui thread
     *
     * @param text
     */
    public void runLabelUpdate(final String aText) {
        if (!downloadCLabel.isDisposed()) {
            downloadCLabel.getDisplay().asyncExec(new Runnable() {
                    public void run() {
                        if (!downloadCLabel.isDisposed()) {
                            downloadCLabel.setText(aText);
                        }
                    }
                });
        }
    }

    /* ( non-Javadoc )
     * @see java.util.Observer#update( java.util.Observable, java.lang.Object )
     */
    public void update(Observable o, Object arg) {
        if (System.currentTimeMillis() < (lastLabelUpdate + 1111)) {
            return;
        }

        int totalFiles = 0;
        int totalQueued = 0;
        int totalDownloaded = 0;
        int totalActive = 0;
        int totalPaused = 0;
        long activeTotal = 0;
        long activeDownloaded = 0;
        long queuedTotal = 0;
        long queuedDownloaded = 0;
        long downloadedTotal = 0;
        long pausedTotal = 0;
        long pausedDownloaded = 0;

        if (o instanceof FileInfoIntMap) {
            synchronized (o) {
                FileInfoIntMap files = (FileInfoIntMap) o;
                TIntObjectIterator it = files.iterator();

                while (it.hasNext()) {
                    it.advance();

                    FileInfo fileInfo = (FileInfo) it.value();

                    if (fileInfo.isInteresting()) {
                        totalFiles++;

                        if (fileInfo.getState().getState() == EnumFileState.QUEUED) {
                            queuedTotal += fileInfo.getSize();
                            queuedDownloaded += fileInfo.getDownloaded();
                            totalQueued++;
                        } else if (fileInfo.getState().getState() == EnumFileState.DOWNLOADED) {
                            totalDownloaded++;
                            downloadedTotal += fileInfo.getSize();
                        } else if (fileInfo.getState().getState() == EnumFileState.PAUSED) {
                            totalPaused++;
                            pausedTotal += fileInfo.getSize();
                            pausedDownloaded += fileInfo.getDownloaded();
                        } else {
                            totalActive++;
                            activeTotal += fileInfo.getSize();
                            activeDownloaded += fileInfo.getDownloaded();
                        }
                    }
                }
            }
        }

        String newText = "";

        if (totalActive > 0) {
            newText += (G2GuiResources.getString("TT_Downloads") + ": " + totalActive + " " + G2GuiResources.getString("TT_Active").toLowerCase() + " (" +
            FileInfo.calcStringSize(activeDownloaded) + " / " + FileInfo.calcStringSize(activeTotal) + ")");
        } else {
			newText += (G2GuiResources.getString("TT_Downloads") + ": " + totalActive);
        }

        if (totalPaused > 0) {
            newText += (", " + G2GuiResources.getString("TT_Status0") + ": " + totalPaused + " (" + FileInfo.calcStringSize(pausedDownloaded) +
            " / " + FileInfo.calcStringSize(pausedTotal) + ")");
        }

        if (totalQueued > 0) {
            newText += (", " + G2GuiResources.getString("TT_Queued") + ": " + totalQueued + " (" + FileInfo.calcStringSize(queuedDownloaded) + " / " +
            FileInfo.calcStringSize(queuedTotal) + ")");
        }

        if (totalDownloaded > 0) {
            newText += (", " + G2GuiResources.getString("TT_Downloaded") + ": " + totalDownloaded + " (" + FileInfo.calcStringSize(downloadedTotal) +
            ")");
        }

        if (!oldDLabelText.equals(newText)) {
            runLabelUpdate(newText);
            oldDLabelText = newText;
        }

        lastLabelUpdate = System.currentTimeMillis();
    }

    /* ( non-Javadoc )
     * @see net.mldonkey.g2gui.view.GuiTab#updateDisplay()
     */
    public void updateDisplay() {
        gView.updateDisplay();
        uploadTableView.updateDisplay();

        if (clientTableView != null) {
            clientTableView.updateDisplay();
        }

        super.updateDisplay();
    }

    /* (non-Javadoc)
     * @see net.mldonkey.g2gui.view.GuiTab#dispose()
     */
    public void dispose() {
        super.dispose();
        popupMenuDL.dispose();
        popupMenuUL.dispose();

        if (popupMenuCL != null) {
            popupMenuCL.dispose();
        }
    }
    
    public GView getClientGView() {
    	return this.clientTableView;
    }

	public GView getUploadGView() {
		return this.uploadTableView;
	}
}


/*
$Log: TransferTab.java,v $
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
replace $user$ with $Author: vnc $

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
