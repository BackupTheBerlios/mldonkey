/*
 * Copyright 2003
 * g2gui Team
 *
 *
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package net.mldonkey.g2gui.view.shares;

import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.view.GuiTab;
import net.mldonkey.g2gui.view.helper.SashViewFrame;
import net.mldonkey.g2gui.view.helper.Spinner;
import net.mldonkey.g2gui.view.helper.WidgetFactory;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


/**
 * UploadsViewFrame
 *
 * @version $Id: SharesViewFrame.java,v 1.1 2004/01/22 21:28:04 psy Exp $
 *
 */
public class SharesViewFrame extends SashViewFrame {
    public SharesViewFrame(SashForm parentSashForm, String prefString, String prefImageString,
        GuiTab guiTab) {
        super(parentSashForm, prefString, prefImageString, guiTab);

        gView = new SharesTableView(this);
        createPaneListener(new SharesPaneListener(this));
        createPaneToolBar();
    }

    // Temporary - until gui protocol has better access to shared directories
    public void createPaneToolBar() {
        super.createPaneToolBar();

        addToolItem("TT_UT_UNSHARE", "minus",
            new SelectionAdapter() {
                public void widgetSelected(SelectionEvent s) {
                    ShareInputDialog shareInputDialog = new ShareInputDialog(gView.getShell(),
                            "Unshare", "Unshare directory", false, null);

                    if (shareInputDialog.open() == InputDialog.OK) {
                        if (!shareInputDialog.getValue().equals("")) {
                            String string = "unshare" + " \"" + shareInputDialog.getValue() + "\"";

                            Message consoleMessage = new EncodeMessage(Message.S_CONSOLEMSG, string);
                            consoleMessage.sendMessage(getCore());
                        }
                    }
                }
            });
        addToolItem("TT_UT_SHARE", "plus",
            new SelectionAdapter() {
                public void widgetSelected(SelectionEvent s) {
                    ShareInputDialog shareInputDialog = new ShareInputDialog(gView.getShell(),
                            "Share", "Share directory", true, null);

                    if (shareInputDialog.open() == InputDialog.OK) {
                        if (!shareInputDialog.getValue().equals("")) {
                            String string = "share " + shareInputDialog.getPriority() + " \"" +
                                shareInputDialog.getValue() + "\"";

                            Message consoleMessage = new EncodeMessage(Message.S_CONSOLEMSG, string);
                            consoleMessage.sendMessage(getCore());
                        }
                    }
                }
            });

        addToolItem("TT_UT_RESHARE", "rotate",
            new SelectionAdapter() {
                public void widgetSelected(SelectionEvent s) {
                    getCore().getSharedFileInfoIntMap().reshare();
                }
            });
    }

    private class ShareInputDialog extends InputDialog {
        private int priority;
        private boolean share;
        private Spinner spinner;

        public ShareInputDialog(Shell parentShell, String dialogTitle, String dialogMessage,
            boolean share, IInputValidator validator) {
            super(parentShell, dialogTitle, dialogMessage, "", validator);
            this.share = share;
        }

        protected Control createDialogArea(Composite parent) {
            Composite composite = (Composite) super.createDialogArea(parent);

            if (share) {
                Composite priorityComposite = new Composite(composite, SWT.NONE);
                priorityComposite.setLayout(WidgetFactory.createGridLayout(2, 0, 0, 10, 0, false));

                spinner = new Spinner(priorityComposite, SWT.NONE);
                spinner.setMaximum(999);
                spinner.setMinimum(0);

                Label label = new Label(priorityComposite, SWT.NONE);
                label.setText("Priority");
            }

            return composite;
        }

        protected void buttonPressed(int buttonId) {
            if (share)
                priority = spinner.getSelection();

            super.buttonPressed(buttonId);
        }

        public int getPriority() {
            return priority;
        }
    }
}


/*
$Log: SharesViewFrame.java,v $
Revision 1.1  2004/01/22 21:28:04  psy
renamed "uploads" to "shares" and moved it to a tab of its own.
"uploaders" are now called "uploads" for improved naming-consistency

Revision 1.4  2003/12/01 16:53:36  zet
add reshare option

Revision 1.3  2003/11/30 03:31:57  zet
temporary share/unshare dialogs

Revision 1.2  2003/11/28 01:06:21  zet
not much- slowly expanding viewframe - will continue later

Revision 1.1  2003/11/24 01:33:27  zet
move some classes

*/
