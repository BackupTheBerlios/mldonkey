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
package net.mldonkey.g2gui.view.transfer.downloadTable;

import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.OptionsInfo;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.model.enum.EnumPriority;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.transfer.FileDetailDialog;
import net.mldonkey.g2gui.view.transfer.TreeClientInfo;
import net.mldonkey.g2gui.view.transfer.UniformResourceLocator;
import net.mldonkey.g2gui.view.viewers.GView;
import net.mldonkey.g2gui.view.viewers.actions.AddClientAsFriendAction;
import net.mldonkey.g2gui.view.viewers.actions.ClientDetailAction;
import net.mldonkey.g2gui.view.viewers.actions.CopyED2KLinkToClipboardAction;
import net.mldonkey.g2gui.view.viewers.actions.ToggleClientsAction;
import net.mldonkey.g2gui.view.viewers.actions.WebServicesAction;
import net.mldonkey.g2gui.view.viewers.table.GTableMenuListener;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 *
 * DownloadTableTreeMenuListener
 *
 * @version $Id: DownloadTableTreeMenuListener.java,v 1.36 2004/03/25 14:41:13 psy Exp $
 *
 */
public class DownloadTableTreeMenuListener extends GTableMenuListener
    implements ISelectionChangedListener {
    private FileInfo lastSelectedFile;
    private FileInfo selectedFile;
    private TreeClientInfo selectedClient;
    private List selectedClients = new ArrayList();
    private List selectedFiles = new ArrayList();
    private GView clientView;
    private boolean createClientTable = false;
    private boolean myDrag = false;

    public DownloadTableTreeMenuListener(DownloadTableTreeView downloadTableTreeViewer) {
        super(downloadTableTreeViewer);
    }

    public void setClientView(GView clientView) {
        this.clientView = clientView;
    }

    public void initialize() {
        super.initialize();

        /*this is to delete the selection, if one clicks in an empty row of the table*/
        gView.getTable().addMouseListener(new MouseAdapter() {
                public void mouseDown(MouseEvent e) {
                    Table table = (Table) e.widget;
                    TableItem item = table.getItem(new Point(e.x, e.y));

                    if (item == null) {
                        gView.getTable().deselectAll();
                        selectedFiles.clear();
                        selectedClients.clear();
                        selectedClient = null;
                        selectedFile = null;
                    }
                }
            });

        if (SWT.getPlatform().equals("win32") && PreferenceLoader.loadBoolean("dragAndDrop"))
            activateDragAndDrop();
    }

    /**
     * Activate drag and drop
     */
    public void activateDragAndDrop() {
        DragSource dragSource = new DragSource(gView.getTable(), DND.DROP_COPY | DND.DROP_LINK);
        dragSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });

        dragSource.addDragListener(new DragSourceAdapter() {
                public void dragStart(DragSourceEvent event) {
                    if (selectedFile == null)
                        event.doit = false;
                    else {
                        event.doit = true;
                        myDrag = true;
                    }
                }

                public void dragSetData(DragSourceEvent event) {
                    event.data = selectedFile.getED2K();
                }

                public void dragFinished(DragSourceEvent event) {
                    myDrag = false;
                }
            });

        DropTarget dropTarget = new DropTarget(gView.getTable(),
                DND.DROP_COPY | DND.DROP_DEFAULT | DND.DROP_LINK);
        final UniformResourceLocator uRL = UniformResourceLocator.getInstance();
        final TextTransfer textTransfer = TextTransfer.getInstance();
        dropTarget.setTransfer(new Transfer[] { uRL, textTransfer });
        dropTarget.addDropListener(new DropTargetAdapter() {
                public void dragEnter(DropTargetEvent event) {
                    event.detail = DND.DROP_COPY;

                    for (int i = 0; i < event.dataTypes.length; i++) {
                        if (uRL.isSupportedType(event.dataTypes[ i ])) {
                            event.detail = DND.DROP_LINK;

                            break;
                        }
                    }
                }

                public void drop(DropTargetEvent event) {
                    if ((event.data == null) || myDrag)
                        return;

                    Message dllLink = new EncodeMessage(Message.S_DLLINK, event.data);
                    dllLink.sendMessage(gView.getCore());
                }
            });
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    public void selectionChanged(SelectionChangedEvent e) {
        IStructuredSelection sSel = (IStructuredSelection) e.getSelection();
        Object o = sSel.getFirstElement();

        if (o instanceof FileInfo) {
            FileInfo fileInfo = (FileInfo) o;
            selectedFile = fileInfo;

            if (createClientTable &&
                    ((lastSelectedFile == null) || (lastSelectedFile != selectedFile)))
                clientView.getViewer().setInput(fileInfo);

            lastSelectedFile = selectedFile;
        } else
            selectedFile = null;

        selectedClient = (o instanceof TreeClientInfo) ? (TreeClientInfo) o : null;
        selectedClients.clear();
        selectedFiles.clear();

        for (Iterator it = sSel.iterator(); it.hasNext();) {
            o = it.next();

            if (o instanceof FileInfo)
                selectedFiles.add(o);
            else if (o instanceof TreeClientInfo)
                selectedClients.add(o);
        }
    }

    public void updateClientsTable(boolean b) {
        if (b) {
            if (createClientTable != b)
                clientView.getViewer().setInput(lastSelectedFile);
        } else
            clientView.getViewer().setInput(null);

        createClientTable = b;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
     */
    public void menuAboutToShow(IMenuManager menuManager) {
        boolean advancedMode = PreferenceLoader.loadBoolean("advancedMode");

        if ((selectedFile != null) && selectedFileListContains(EnumFileState.DOWNLOADED))
            menuManager.add(new CommitAction());

        if ((selectedFile != null) &&
                (selectedFile.getState().getState() == EnumFileState.DOWNLOADED)) {
            MenuManager commitAsSubMenu = new MenuManager(G2GuiResources.getString(
                        "TT_DOWNLOAD_MENU_COMMIT_AS"));

            commitAsSubMenu.add(new CommitAction(true));

            for (int i = 0; i < selectedFile.getNames().length; i++)
                commitAsSubMenu.add(new CommitAction(selectedFile.getNames()[ i ]));

            menuManager.add(commitAsSubMenu);
        }

        if (selectedFile != null)
            menuManager.add(new FileDetailAction());

        if ((selectedFile != null) && selectedFileListContains(EnumFileState.DOWNLOADING))
            menuManager.add(new PauseAction());

        if ((selectedFile != null) && selectedFileListContains(EnumFileState.PAUSED))
            menuManager.add(new ResumeAction());

        if ((selectedFile != null) && selectedFileListContainsOtherThan(EnumFileState.DOWNLOADED))
            menuManager.add(new CancelAction());

        if ((selectedFile != null) && selectedFileListContainsOtherThan(EnumFileState.DOWNLOADED)) {
            MenuManager prioritySubMenu = new MenuManager(G2GuiResources.getString(
                        "TT_DOWNLOAD_MENU_PRIORITY"));
            prioritySubMenu.add(new PriorityAction(EnumPriority.VERY_HIGH));
            prioritySubMenu.add(new PriorityAction(EnumPriority.HIGH));
            prioritySubMenu.add(new PriorityAction(EnumPriority.NORMAL));
            prioritySubMenu.add(new PriorityAction(EnumPriority.LOW));
            prioritySubMenu.add(new PriorityAction(EnumPriority.VERY_LOW));

            if (advancedMode) {
                prioritySubMenu.add(new Separator());
                prioritySubMenu.add(new CustomPriorityAction(false));
                prioritySubMenu.add(new CustomPriorityAction(true));
            }

            menuManager.add(prioritySubMenu);
        }

        if ((selectedFile != null) && (selectedFile.getDownloaded() > 0)) {
        	/* old preview method */
        	OptionsInfo option = (OptionsInfo) gView.getCore().getOptionsInfoMap().get("previewer");
            if (option != null) {
                String previewer = (String) option.getValue();
                if (!previewer.equals(""))
                    menuManager.add(new PreviewAction());
            }
            
            /* new http-preview method */
            if (!false) {
            	menuManager.add(new NetPreviewAction());
            }
        
        }

        if ((selectedFile != null) && advancedMode) {
            menuManager.add(new VerifyChunksAction());
            menuManager.add(new ToggleClientsAction(gView));
        }

        if ((selectedClient != null) && advancedMode) {
            ClientInfo[] clientInfoArray = new ClientInfo[ selectedClients.size() ];

            for (int i = 0; i < selectedClients.size(); i++) {
                TreeClientInfo treeClientInfo = (TreeClientInfo) selectedClients.get(i);
                clientInfoArray[ i ] = treeClientInfo.getClientInfo();
            }

            menuManager.add(new AddClientAsFriendAction(gView.getCore(), clientInfoArray));
        }

        if (selectedClient != null)
            menuManager.add(new ClientDetailAction(gView.getShell(), selectedClient.getFileInfo(),
                    selectedClient.getClientInfo(), gView.getCore()));

        if (selectedFile != null) {
            String[] linkList = new String[ selectedFiles.size() ];

            for (int i = 0; i < selectedFiles.size(); i++)
                linkList[ i ] = new String(((FileInfo) selectedFiles.get(i)).getED2K());

            MenuManager clipboardMenu = new MenuManager(G2GuiResources.getString(
                        "TT_DOWNLOAD_MENU_COPYTO"));
            clipboardMenu.add(new CopyED2KLinkToClipboardAction(false, linkList));
            clipboardMenu.add(new CopyED2KLinkToClipboardAction(true, linkList));
            menuManager.add(clipboardMenu);
        }

        if ((selectedFile != null) && advancedMode) {
            MenuManager webServicesMenu = new MenuManager(G2GuiResources.getString(
                        "TT_DOWNLOAD_MENU_WEB_SERVICES"));
            webServicesMenu.add(new WebServicesAction(WebServicesAction.BITZI,
                    selectedFile.getSize(), selectedFile.getMd4()));
            webServicesMenu.add(new WebServicesAction(WebServicesAction.FILEDONKEY,
                    selectedFile.getSize(), selectedFile.getMd4()));
            webServicesMenu.add(new WebServicesAction(WebServicesAction.JIGLE,
                    selectedFile.getSize(), selectedFile.getMd4()));
            webServicesMenu.add(new WebServicesAction(WebServicesAction.SHAREREACTOR,
                    selectedFile.getSize(), selectedFile.getED2K()));
            webServicesMenu.add(new WebServicesAction(WebServicesAction.DONKEY_FAKES,
                    selectedFile.getSize(), selectedFile.getED2K()));
            menuManager.add(webServicesMenu);
        }
    }

    // Helpers
    private boolean selectedFileListContains(EnumFileState e) {
        for (int i = 0; i < selectedFiles.size(); i++)
            if (((FileInfo) selectedFiles.get(i)).getState().getState() == e)
                return true;

        return false;
    }

    private boolean selectedFileListContainsOtherThan(EnumFileState e) {
        for (int i = 0; i < selectedFiles.size(); i++)
            if (((FileInfo) selectedFiles.get(i)).getState().getState() != e)
                return true;

        return false;
    }

    // Menu Actions

    /**
     * VerifyChunksAction
     */
    private class VerifyChunksAction extends Action {
        public VerifyChunksAction() {
            super(G2GuiResources.getString("TT_DOWNLOAD_MENU_VERIFY_CHUNKS"));
            setImageDescriptor(G2GuiResources.getImageDescriptor("verify"));
        }

        public void run() {
            for (int i = 0; i < selectedFiles.size(); i++)
                ((FileInfo) selectedFiles.get(i)).verifyChunks();
        }
    }

    /**
    * PreviewAction
    */
    private class PreviewAction extends Action {
        public PreviewAction() {
            super(G2GuiResources.getString("TT_DOWNLOAD_MENU_PREVIEW"));
            setImageDescriptor(G2GuiResources.getImageDescriptor("preview"));
        }

        public void run() {
            for (int i = 0; i < selectedFiles.size(); i++)
                ((FileInfo) selectedFiles.get(i)).preview();
        }
    }

    /**
     * PreviewAction
     */
     private class NetPreviewAction extends Action {
         public NetPreviewAction() {
             super(G2GuiResources.getString("TT_DOWNLOAD_MENU_NETPREVIEW"));
             setImageDescriptor(G2GuiResources.getImageDescriptor("preview"));
         }

         public void run() {
             for (int i = 0; i < selectedFiles.size(); i++)
                 ((FileInfo) selectedFiles.get(i)).preview();
         }
     }
    
    /**
     * FileDetailAction
     */
    private class FileDetailAction extends Action {
        public FileDetailAction() {
            super(G2GuiResources.getString("TT_DOWNLOAD_MENU_FILE_DETAILS"));
            setImageDescriptor(G2GuiResources.getImageDescriptor("info"));
        }

        public void run() {
            FileDetailDialog f = new FileDetailDialog(gView.getShell(), selectedFile);
            f.open();
        }
    }

    /**
     * PauseAction
     */
    private class PauseAction extends Action {
        public PauseAction() {
            super(G2GuiResources.getString("TT_DOWNLOAD_MENU_PAUSE"));
            setImageDescriptor(G2GuiResources.getImageDescriptor("pause"));
        }

        public void run() {
            for (int i = 0; i < selectedFiles.size(); i++) {
                FileInfo fileInfo = (FileInfo) selectedFiles.get(i);

                if (fileInfo.getState().getState() == EnumFileState.DOWNLOADING)
                    fileInfo.setState(EnumFileState.PAUSED);
            }
        }
    }

    /**
     * CommitAction
     */
    private class CommitAction extends Action {
        private String commitAs;
        private boolean manualInput = false;

        public CommitAction() {
            super(G2GuiResources.getString("TT_DOWNLOAD_MENU_COMMIT_SELECTED"));
            setImageDescriptor(G2GuiResources.getImageDescriptor("commit"));
        }

        public CommitAction(String commitAs) {
            super(commitAs);
            setImageDescriptor(G2GuiResources.getImageDescriptor("commit"));
            this.commitAs = commitAs;
        }

        public CommitAction(boolean b) {
            super(G2GuiResources.getString("TT_DOWNLOAD_MENU_COMMIT_INPUT"));
            setImageDescriptor(G2GuiResources.getImageDescriptor("commit_question"));
            manualInput = b;
        }

        public void run() {
            if ((commitAs == null) && !manualInput) {
                for (int i = 0; i < selectedFiles.size(); i++) {
                    FileInfo selectedFileInfo = (FileInfo) selectedFiles.get(i);

                    if (selectedFileInfo.getState().getState() == EnumFileState.DOWNLOADED)
                        selectedFileInfo.saveFileAs(selectedFileInfo.getName());
                }
            } else {
                if (manualInput) {
                    InputDialog inputDialog = new InputDialog(gView.getShell(),
                            G2GuiResources.getString("TT_DOWNLOAD_MENU_COMMIT_AS"),
                            G2GuiResources.getString("TT_DOWNLOAD_MENU_COMMIT_AS"),
                            selectedFile.getName(), null);

                    if (inputDialog.open() == InputDialog.OK) {
                        String newFileName = inputDialog.getValue();

                        if (!newFileName.equals(""))
                            selectedFile.saveFileAs(newFileName);
                    }
                } else
                    selectedFile.saveFileAs(commitAs);
            }
        }
    }

    /**
     * ResumeAction
     */
    private class ResumeAction extends Action {
        public ResumeAction() {
            super(G2GuiResources.getString("TT_DOWNLOAD_MENU_RESUME"));
            setImageDescriptor(G2GuiResources.getImageDescriptor("resume"));
        }

        public void run() {
            for (int i = 0; i < selectedFiles.size(); i++) {
                FileInfo fileInfo = (FileInfo) selectedFiles.get(i);

                if (fileInfo.getState().getState() == EnumFileState.PAUSED)
                    fileInfo.setState(EnumFileState.DOWNLOADING);
            }
        }
    }

    /**
     * CancelAction
     */
    private class CancelAction extends Action {
        public CancelAction() {
            super(G2GuiResources.getString("TT_DOWNLOAD_MENU_CANCEL"));
            setImageDescriptor(G2GuiResources.getImageDescriptor("cancel"));
        }

        public void run() {
            MessageBox reallyCancel = new MessageBox(gView.getShell(),
                    SWT.YES | SWT.NO | SWT.ICON_QUESTION);
            
            String cancelFiles = "";
            for (int i = 0; i < selectedFiles.size(); i++) {
            	cancelFiles += ( (FileInfo) selectedFiles.get(i) ).getName() + "\n\n";
            }
            
            reallyCancel.setMessage(G2GuiResources.getString("TT_REALLY_CANCEL") +
                ((selectedFiles.size() > 1) ? (" (" + selectedFiles.size() + " selected)") : "") + 
				"\n\n" + cancelFiles);

            int answer = reallyCancel.open();

            if (answer == SWT.YES) {
                for (int i = 0; i < selectedFiles.size(); i++) {
                    FileInfo fileInfo = (FileInfo) selectedFiles.get(i);

                    if (fileInfo.getState().getState() != EnumFileState.DOWNLOADED)
                        fileInfo.setState(EnumFileState.CANCELLED);

                    // this conceptually breaks core/gui synchronicity and should be removed ASAP.
                    gView.getCore().getResultInfoIntMap().setDownloading(fileInfo, false);
                }
            }
        }
    }

    /**
     * PriorityAction
     */
    private class PriorityAction extends Action {
        private EnumPriority enumPriority;

        public PriorityAction(EnumPriority e) {
            super("", Action.AS_CHECK_BOX);
            enumPriority = e;

            if (e == EnumPriority.VERY_HIGH)
                setText(G2GuiResources.getString("TT_PRIO_Very_High").toLowerCase());
            else if (e == EnumPriority.HIGH)
                setText(G2GuiResources.getString("TT_PRIO_High").toLowerCase());
            else if (e == EnumPriority.NORMAL)
                setText(G2GuiResources.getString("TT_PRIO_Normal").toLowerCase());
            else if (e == EnumPriority.LOW)
                setText(G2GuiResources.getString("TT_PRIO_Low").toLowerCase());
            else if (e == EnumPriority.VERY_LOW)
                setText(G2GuiResources.getString("TT_PRIO_Very_Low").toLowerCase());
        }

        public void run() {
            for (int i = 0; i < selectedFiles.size(); i++) {
                FileInfo fileInfo = (FileInfo) selectedFiles.get(i);

                if (fileInfo.getState().getState() != EnumFileState.DOWNLOADED)
                    fileInfo.sendPriority(enumPriority);
            }
        }

        public boolean isChecked() {
            return (selectedFile.getPriorityEnum() == enumPriority);
        }
    }

    /**
     * CustomPriorityAction
     *
     */
    private class CustomPriorityAction extends Action {
        private boolean relative;

        public CustomPriorityAction(boolean relative) {
            super("", Action.AS_CHECK_BOX);
            this.relative = relative;

            if (relative)
                setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_PRIORITY_CUSTOM_RELATIVE"));
            else
                setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_PRIORITY_CUSTOM_ABSOLUTE"));
        }

        public void run() {
            String title = G2GuiResources.getString("TT_DOWNLOAD_MENU_PRIORITY") + " (" +
                (relative ? G2GuiResources.getString("TT_DOWNLOAD_MENU_PRIORITY_CUSTOM_RELATIVE")
                          : G2GuiResources.getString("TT_DOWNLOAD_MENU_PRIORITY_CUSTOM_ABSOLUTE")) +
                ")";

            PriorityInputDialog priorityInputDialog = new PriorityInputDialog(gView.getShell(),
                    title, title, (relative ? 0 : selectedFile.getPriority()), null);

            if (priorityInputDialog.open() == PriorityInputDialog.OK) {
                int newPriority;

                try {
                    newPriority = Integer.parseInt(priorityInputDialog.getValue().trim());
                } catch (NumberFormatException e) {
                    newPriority = 0;
                }

                for (int i = 0; i < selectedFiles.size(); i++) {
                    FileInfo fileInfo = (FileInfo) selectedFiles.get(i);

                    if (fileInfo.getState().getState() != EnumFileState.DOWNLOADED)
                        fileInfo.sendPriority(relative, newPriority);
                }
            }
        }

        public boolean isChecked() {
            return false;
        }
    }

    /**
     * PriorityInputDialog
     */
    private class PriorityInputDialog extends InputDialog {
        int initialValue;

        public PriorityInputDialog(Shell parentShell, String dialogTitle, String dialogMessage,
            int initialValue, IInputValidator validator) {
            super(parentShell, dialogTitle, dialogMessage, "" + initialValue, validator);
            this.initialValue = initialValue;
        }

        protected Control createDialogArea(Composite parent) {
            Composite composite = (Composite) super.createDialogArea(parent);

            final Text text = this.getText();
            text.setTextLimit(4);

            final Scale scale = new Scale(composite, SWT.HORIZONTAL);
            scale.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            scale.setMinimum(0);
            scale.setMaximum(200);
            scale.setIncrement(1);
            scale.setPageIncrement(5);

            if (initialValue < -100)
                scale.setSelection(0);
            else if (initialValue > 100)
                scale.setSelection(200);
            else
                scale.setSelection(initialValue + 100);

            scale.addSelectionListener(new SelectionListener() {
                    public void widgetDefaultSelected(SelectionEvent e) {
                    }

                    public void widgetSelected(SelectionEvent e) {
                        text.setText("" + (scale.getSelection() - 100));
                    }
                });

            return composite;
        }
    }
}


/*
$Log: DownloadTableTreeMenuListener.java,v $
Revision 1.36  2004/03/25 14:41:13  psy
Added a list of files to the cancel-dialog
Started to add the http-preview stuff...

Revision 1.35  2003/12/04 08:47:28  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.34  2003/12/01 13:28:16  zet
add port info

Revision 1.33  2003/11/25 01:13:13  zet
include filesize for webservice>jigle lookup

Revision 1.32  2003/11/23 17:58:03  lemmy
removed dead/unused code

Revision 1.31  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

Revision 1.30  2003/11/10 18:57:33  zet
use jface dialogs

Revision 1.29  2003/11/03 02:44:04  zet
minor

Revision 1.28  2003/10/31 22:41:59  zet
rename to View

Revision 1.27  2003/10/31 16:30:49  zet
minor renames

Revision 1.26  2003/10/31 16:02:57  zet
use the better 'View' (instead of awkward 'Page') appellation to follow eclipse design

Revision 1.25  2003/10/31 10:42:47  lemmy
Renamed GViewer, GTableViewer and GTableTreeViewer to GPage... to avoid mix-ups with StructuredViewer...
Removed IGViewer because our abstract class GPage do the job
Use supertype/interface where possible to keep the design flexible!

Revision 1.24  2003/10/31 07:24:01  zet
fix: filestate filter - put back important isFilterProperty check
fix: filestate filter - exclusionary fileinfo filters
fix: 2 new null pointer exceptions (search tab)
recommit CTabFolderColumnSelectorAction (why was this deleted from cvs???)
- all search tab tables are column updated
regexp helpers in one class
rework viewers heirarchy
filter clients table properly
discovered sync errors and NPEs in upload table... will continue later.

Revision 1.23  2003/10/24 21:26:30  zet
add donkey fakes web service

Revision 1.22  2003/10/22 20:38:35  zet
common actions

Revision 1.21  2003/10/22 17:17:30  zet
common actions

Revision 1.20  2003/10/22 16:28:52  zet
common actions

Revision 1.19  2003/10/22 01:38:31  zet
*** empty log message ***

Revision 1.18  2003/10/16 23:56:44  zet
not much

Revision 1.17  2003/10/15 21:13:21  zet
show/hide clients from rtclk menu

Revision 1.16  2003/10/15 18:33:35  zet
icons

Revision 1.15  2003/10/15 04:16:51  zet
add filedonkey lookup

Revision 1.14  2003/10/15 00:49:56  zet
add web services hash lookup

Revision 1.13  2003/10/13 21:27:00  zet
nil

Revision 1.12  2003/10/12 19:43:12  zet
very high & low priorities

Revision 1.11  2003/10/12 15:58:29  zet
rewrite downloads table & more..

Revision 1.10  2003/10/07 02:56:43  zet
add priority scale

Revision 1.9  2003/10/05 00:55:29  zet
set priority as any #

Revision 1.8  2003/09/28 17:47:45  zet
prevent local drags (since all hashes are 0 atm)

Revision 1.7  2003/09/26 04:19:35  zet
add drag&drop support

Revision 1.6  2003/09/23 15:24:24  zet
not much..

Revision 1.5  2003/09/23 01:22:45  zet
*** empty log message ***

Revision 1.4  2003/09/23 00:11:00  zet
add Preview

Revision 1.3  2003/09/20 22:37:50  zet
*** empty log message ***

Revision 1.2  2003/09/20 17:46:11  zet
*** empty log message ***

Revision 1.1  2003/09/20 14:39:21  zet
move transfer package

Revision 1.34  2003/09/18 14:11:01  zet
revert

Revision 1.30  2003/09/16 16:58:03  zet
commit as

Revision 1.29  2003/09/15 15:32:09  lemmy
reset state of canceled downloads from search [bug #908]

Revision 1.28  2003/09/14 03:37:43  zet
changedProperties

Revision 1.27  2003/08/31 00:08:59  zet
add buttons

Revision 1.26  2003/08/30 00:44:01  zet
move tabletree menu

Revision 1.25  2003/08/29 23:33:57  zet
*** empty log message ***

Revision 1.24  2003/08/24 18:22:37  zet
*** empty log message ***

Revision 1.23  2003/08/24 18:19:02  zet
still fixing...

Revision 1.22  2003/08/24 16:37:04  zet
combine the preference stores

Revision 1.21  2003/08/24 16:16:26  zet
more fixing....

Revision 1.20  2003/08/24 16:11:24  zet
do not superclass this if you are going to break it...

Revision 1.19  2003/08/23 22:35:55  zet
combine priority actions

Revision 1.18  2003/08/23 20:33:01  zet
multi select actions

Revision 1.17  2003/08/23 15:21:37  zet
remove @author

Revision 1.16  2003/08/23 15:13:00  zet
remove reference to static MainTab methods

Revision 1.15  2003/08/23 09:46:18  lemmy
superclass TableMenuListener added

Revision 1.14  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.13  2003/08/22 21:16:36  lemmy
replace $user$ with $Author: psy $

Revision 1.12  2003/08/22 14:30:45  lemmy
verify chunks added

Revision 1.11  2003/08/21 00:59:57  zet
doubleclick expand

Revision 1.10  2003/08/20 22:18:56  zet
Viewer updates

Revision 1.9  2003/08/20 14:58:43  zet
sources clientinfo viewer

Revision 1.8  2003/08/19 12:14:16  lemmy
first try of simple/advanced mode

Revision 1.7  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.6  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging

Revision 1.5  2003/08/11 00:30:10  zet
show queued files

Revision 1.4  2003/08/08 20:51:11  zet
localise strings

Revision 1.3  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.2  2003/08/06 17:14:50  zet
file details

Revision 1.1  2003/08/04 19:22:08  zet
trial tabletreeviewer


*/
