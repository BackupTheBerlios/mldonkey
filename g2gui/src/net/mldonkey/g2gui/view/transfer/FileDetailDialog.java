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
package net.mldonkey.g2gui.view.transfer;

import java.util.Arrays;
import java.util.Iterator;

import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.view.helper.WidgetFactory;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


/**
 *
 * FileDetailDialog
 *
 *
 * @version $Id: FileDetailDialog.java,v 1.14 2004/03/22 15:12:50 dek Exp $
 *
 */
public class FileDetailDialog extends DetailDialog {
    private FileInfo fileInfo;
    private Button fileActionButton;
    private Button fileCancelButton;
    private StyledText clFileName;
    private StyledText clHash;
    private StyledText clSize;
    private StyledText clAge;
    private StyledText clSources;
    private StyledText clChunks;
    private StyledText clTransferred;
    private StyledText clRelativeAvail;
    private StyledText clLast;
    private StyledText clPriority;
    private StyledText clRate;
    private StyledText clETA;
    private List renameList;
    private Text renameText;

    public FileDetailDialog(Shell parentShell, FileInfo fileInfo) {
        super(parentShell);
        this.fileInfo = fileInfo;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(G2GuiResources.getString("TT_File") + " " + fileInfo.getId() + " " +
            G2GuiResources.getString("TT_Details").toLowerCase());
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        composite.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 0, 5, false));

        createFileGeneralGroup(composite);

        createFileTransferGroup(composite);

        // MultiNet chunks or just chunks < proto 17
        createChunkGroup(composite, G2GuiResources.getString("TT_DOWNLOAD_FD_CHUNKS_INFO"), null);

        // Other network chunks
        if (fileInfo.hasAvails()) {
            Iterator i = fileInfo.getAllAvailNetworks().iterator();

            while (i.hasNext()) {
                NetworkInfo networkInfo = (NetworkInfo) i.next();

                if (networkInfo.isEnabled()) {
                    createChunkGroup(composite, networkInfo.getNetworkName(), networkInfo);
                }
            }
        }

        createRenameGroup(composite);

        // Separator
        Label separator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
        separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        updateLabels();
        fileInfo.addObserver(this);

        return composite;
    }

    /**
     * Tell the core to rename the file
     */
    private void renameFile() {
        String newName = "";

        if (!renameText.getText().equals("") && !renameText.getText().equals(fileInfo.getName())) {
            newName = renameText.getText();
        } else if ((renameList.getSelection().length > 0) &&
                !renameList.getSelection()[ 0 ].equals(fileInfo.getName())) {
            newName = renameList.getSelection()[ 0 ];
        }

        if (!newName.equals("")) {
            fileInfo.setName(newName);
        }
    }

    /**
     * @param parent
     * Create group relating to general file information
     */
    private void createFileGeneralGroup(Composite parent) {
        Group fileGeneral = new Group(parent, SWT.SHADOW_ETCHED_OUT);

        fileGeneral.setText(G2GuiResources.getString("TT_DOWNLOAD_FD_FILE_INFO"));
        fileGeneral.setLayout(WidgetFactory.createGridLayout(4, 5, 0, 0, 0, false));
        fileGeneral.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        clFileName = createLine(fileGeneral, "TT_DOWNLOAD_FD_FILENAME", true);
        clHash = createLine(fileGeneral, "TT_DOWNLOAD_FD_HASH", true);
        clSize = createLine(fileGeneral, "TT_DOWNLOAD_FD_SIZE", false);
        clAge = createLine(fileGeneral, "TT_DOWNLOAD_FD_AGE", false);
    }

    /**
     * @param parent
     * Create group relating to the file transfer
     */
    private void createFileTransferGroup(Composite parent) {
        Group fileTransfer = new Group(parent, SWT.SHADOW_ETCHED_OUT);

        fileTransfer.setText(G2GuiResources.getString("TT_DOWNLOAD_FD_TRANSFER_INFO"));
        fileTransfer.setLayout(WidgetFactory.createGridLayout(4, 5, 0, 0, 0, false));
        fileTransfer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        clSources = createLine(fileTransfer, "TT_DOWNLOAD_FD_SOURCES", false);
        clChunks = createLine(fileTransfer, "TT_DOWNLOAD_FD_CHUNKS", false);
        clTransferred = createLine(fileTransfer, "TT_DOWNLOAD_FD_TRANSFERRED", false);
        clRelativeAvail = createLine(fileTransfer, "TT_DOWNLOAD_FD_REL_AVAIL", false);
        clLast = createLine(fileTransfer, "TT_DOWNLOAD_FD_LAST", false);
        clPriority = createLine(fileTransfer, "TT_DOWNLOAD_FD_PRIORITY", false);
        clRate = createLine(fileTransfer, "TT_DOWNLOAD_FD_RATE", false);
        clETA = createLine(fileTransfer, "TT_DOWNLOAD_FD_ETA", false);
    }

    /**
     * @param parent
     * @param resString
     * @param networkInfo
     */
    private void createChunkGroup(Composite parent, String string, NetworkInfo networkInfo) {
        ChunkCanvas chunkCanvas = super.createChunkGroup(parent, string, null, fileInfo, networkInfo);
        fileInfo.addObserver(chunkCanvas);
    }

    /**
     * @param parent
     * Create the rename group
     */
    private void createRenameGroup(Composite parent) {
        Group renameGroup = new Group(parent, SWT.SHADOW_ETCHED_OUT);

        renameGroup.setText(G2GuiResources.getString("TT_DOWNLOAD_FD_ALTERNATIVE_FILENAMES"));
        renameGroup.setLayout(WidgetFactory.createGridLayout(1, 1, 1, 0, 0, false));
        renameGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Arrays.sort(fileInfo.getNames(), String.CASE_INSENSITIVE_ORDER);

        renameList = new List(renameGroup, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);

        for (int i = 0; i < fileInfo.getNames().length; i++)
            renameList.add(fileInfo.getNames()[ i ]);

        GridData listGD = new GridData(GridData.FILL_HORIZONTAL);
        listGD.heightHint = 80;
        listGD.widthHint = 1;
        renameList.setLayoutData(listGD);
        renameList.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent s) {
                    String lItem = renameList.getSelection()[ 0 ];
                    renameText.setText(lItem);
                }
            });

        Composite renameComposite = new Composite(parent, SWT.NONE);

        renameComposite.setLayout(WidgetFactory.createGridLayout(2, 0, 0, 4, 0, false));
        renameComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        renameText = new Text(renameComposite, SWT.BORDER);
        renameText.setText(fileInfo.getName());
        renameText.setFont(PreferenceLoader.loadFont("consoleFontData"));

        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 1;
        renameText.setLayoutData(data);
        renameText.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    if (e.character == SWT.CR) {
                        renameFile();
                        renameText.setText("");
                    }
                }
            });

        Button renameButton = new Button(renameComposite, SWT.NONE);
        renameButton.setText(G2GuiResources.getString("TT_DOWNLOAD_FD_RENAME_BUTTON"));
        renameButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        renameButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent s) {
                    renameFile();
                }
            });
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#createButtonBar(org.eclipse.swt.widgets.Composite)
     */
    protected Control createButtonBar(Composite parent) {
        Composite buttonComposite = new Composite(parent, SWT.NONE);
        buttonComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        buttonComposite.setLayout(WidgetFactory.createGridLayout(3, 5, 5, 5, 0, false));

        if ((fileInfo.getState().getState() == EnumFileState.PAUSED) ||
                (fileInfo.getState().getState() == EnumFileState.DOWNLOADING) ||
                (fileInfo.getState().getState() == EnumFileState.QUEUED)) {
            fileCancelButton = new Button(buttonComposite, SWT.NONE);
            fileCancelButton.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL |
                    GridData.HORIZONTAL_ALIGN_END));
            fileCancelButton.setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_CANCEL") + " " +
                G2GuiResources.getString("TT_File"));
            fileCancelButton.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent s) {
                        MessageBox reallyCancel = new MessageBox(fileCancelButton.getShell(),
                                SWT.YES | SWT.NO | SWT.ICON_QUESTION);

                        reallyCancel.setMessage(G2GuiResources.getString("TT_REALLY_CANCEL"));

                        if (reallyCancel.open() == SWT.YES) {
                            fileInfo.setState(EnumFileState.CANCELLED);
                            fileCancelButton.setEnabled(false);
                            fileActionButton.setEnabled(false);
                        }
                    }
                });
        }

        fileActionButton = new Button(buttonComposite, SWT.NONE);

        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);

        if (fileInfo.getState().getState() == EnumFileState.DOWNLOADED) {
            gridData.horizontalSpan = 2;
            gridData.grabExcessHorizontalSpace = true;
        }

        fileActionButton.setLayoutData(gridData);

        if ((fileInfo.getState().getState() == EnumFileState.PAUSED) ||
                (fileInfo.getState().getState() == EnumFileState.QUEUED)) {
            fileActionButton.setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_RESUME") + " " +
                G2GuiResources.getString("TT_File"));
        } else if (fileInfo.getState().getState() == EnumFileState.DOWNLOADING) {
            fileActionButton.setText("  " + G2GuiResources.getString("TT_DOWNLOAD_MENU_PAUSE") +
                " " + G2GuiResources.getString("TT_File") + "  ");
        } else if (fileInfo.getState().getState() == EnumFileState.DOWNLOADED) {
            fileActionButton.setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_COMMIT") + " " +
                G2GuiResources.getString("TT_File"));
        }

        // until we have an unQueue function..
        if (fileInfo.getState().getState() == EnumFileState.QUEUED) {
            fileActionButton.setEnabled(false);
        }

        fileActionButton.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent s) {
                    if (fileInfo.getState().getState() == EnumFileState.PAUSED) {
                        fileInfo.setState(EnumFileState.DOWNLOADING);
                        fileActionButton.setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_PAUSE") +
                            " " + G2GuiResources.getString("TT_File"));
                    } else if (fileInfo.getState().getState() == EnumFileState.DOWNLOADING) {
                        fileInfo.setState(EnumFileState.PAUSED);
                        fileActionButton.setText(G2GuiResources.getString("TT_DOWNLOAD_MENU_RESUME") +
                            " " + G2GuiResources.getString("TT_File"));
                    } else if (fileInfo.getState().getState() == EnumFileState.DOWNLOADED) {
                        if (renameText.getText().equals("")) {
                            fileInfo.saveFileAs(fileInfo.getName());
                        } else {
                            fileInfo.saveFileAs(renameText.getText());
                        }

                        fileActionButton.setText(G2GuiResources.getString("BTN_OK"));
                        fileActionButton.setEnabled(false);
                    }
                }
            });

        Button closeButton = new Button(buttonComposite, SWT.NONE);
		closeButton.setFocus();
		closeButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		closeButton.setText(G2GuiResources.getString("BTN_CLOSE"));
		closeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent s) {
				close();
			}
		});

        return buttonComposite;
    }

    /**
     * Update the labels
     */
    public void updateLabels() {
        updateLabel(clFileName, fileInfo.getName());
        updateLabel(clHash, fileInfo.getMd4().toUpperCase());
        updateLabel(clSize, fileInfo.getStringSize());
        updateLabel(clAge, fileInfo.getStringAge());
        updateLabel(clSources, fileInfo.getStringSources());
        updateLabel(clChunks,
            Integer.toString(fileInfo.getNumChunks()) + " / " +
            Integer.toString(fileInfo.getChunks().length()));
        updateLabel(clTransferred, fileInfo.getStringDownloaded());
        updateLabel(clRelativeAvail, fileInfo.getRelativeAvail() + "%");
        updateLabel(clLast, fileInfo.getStringOffset());
        updateLabel(clPriority, fileInfo.getStringPriority());
        updateLabel(clRate, fileInfo.getStringRate() + " KB/s");
        updateLabel(clETA, fileInfo.getStringETA());
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
     */
    public boolean close() {
        fileInfo.deleteObserver(this);

        return super.close();
    }
}


/*
$Log: FileDetailDialog.java,v $
Revision 1.14  2004/03/22 15:12:50  dek
changed selection behaviour in detail-dialog

Revision 1.13  2004/03/16 19:27:00  dek
Infos in Detail-Dialogs are now "copy-and-paste" enabled [TM]

Revision 1.12  2003/12/07 19:38:09  lemmy
refactoring

Revision 1.11  2003/12/04 08:47:32  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.10  2003/12/01 14:22:45  lemmy
ProtocolVersion handling completely rewritten

Revision 1.9  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

Revision 1.8  2003/11/11 02:31:42  zet
cleanup

Revision 1.7  2003/11/10 18:57:33  zet
use jface dialogs

Revision 1.6  2003/11/01 18:46:02  zet
swt-fox workaround

Revision 1.5  2003/10/19 16:40:37  zet
centre

Revision 1.4  2003/10/13 21:13:01  zet
nil

Revision 1.3  2003/10/12 15:58:30  zet
rewrite downloads table & more..

Revision 1.2  2003/09/21 23:39:31  zet
displayTableColors preference

Revision 1.1  2003/09/20 14:39:21  zet
move transfer package

Revision 1.33  2003/09/18 14:11:01  zet
revert

Revision 1.31  2003/09/15 22:10:32  zet
add availability %, refresh delay option

Revision 1.30  2003/09/14 22:22:46  zet
use console font

Revision 1.29  2003/09/14 21:54:13  zet
fix rate

Revision 1.28  2003/09/14 16:23:56  zet
multi network avails

Revision 1.27  2003/09/12 15:08:06  zet
fix: [ Bug #897 ]

Revision 1.26  2003/09/08 19:48:00  lemmy
just repaired the log

Revision 1.25  2003/09/04 18:14:56  zet
cancel messagebox

Revision 1.24  2003/08/31 02:35:32  zet
setFocus

Revision 1.23  2003/08/31 02:21:33  zet
commit

Revision 1.22  2003/08/31 02:16:50  zet
cancel button

Revision 1.21  2003/08/31 01:54:25  zet
add spaces

Revision 1.20  2003/08/31 01:48:25  zet
*** empty log message ***

Revision 1.19  2003/08/31 01:46:33  zet
localise

Revision 1.17  2003/08/31 00:08:59  zet
add buttons

Revision 1.16  2003/08/28 22:44:30  zet
GridLayout helper class

Revision 1.15  2003/08/23 15:21:37  zet
remove @author

Revision 1.14  2003/08/23 15:13:00  zet
remove reference to static MainTab methods

Revision 1.13  2003/08/23 01:08:17  zet
*** empty log message ***

Revision 1.12  2003/08/23 01:00:02  zet
*** empty log message ***

Revision 1.11  2003/08/22 22:49:22  vaste
new todos (name + close button)

Revision 1.10  2003/08/22 21:22:58  lemmy

*/
