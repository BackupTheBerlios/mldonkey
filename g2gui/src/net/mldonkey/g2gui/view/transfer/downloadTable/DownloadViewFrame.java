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
package net.mldonkey.g2gui.view.transfer.downloadTable;

import net.mldonkey.g2gui.view.GuiTab;
import net.mldonkey.g2gui.view.helper.ViewFrame;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;
import net.mldonkey.g2gui.view.viewers.CustomTableTreeViewer;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;


/**
 * DownloadViewFrame
 *
 * @version $Id: DownloadViewFrame.java,v 1.1 2003/11/24 01:33:27 zet Exp $
 *
 */
public class DownloadViewFrame extends ViewFrame {
    private boolean advancedMode = PreferenceLoader.loadBoolean("advancedMode");

    public DownloadViewFrame(SashForm parentSashForm, String prefString, String prefImageString,
        GuiTab guiTab) {
        super(parentSashForm, prefString, prefImageString, guiTab);

        gView = new DownloadTableTreeView(this);
        createPaneListener(new DownloadPaneMenuListener(this));
        createPaneToolBar();
    }

    public SashForm getParentSashForm(boolean grandParent) {
        if (grandParent)
            return (SashForm) super.getParentSashForm().getParent();
        else

            return super.getParentSashForm();
    }

    public SashForm getParentSashForm() {
        return getParentSashForm(advancedMode);
    }

    public Control getControl() {
        if (advancedMode)
            return super.getParentSashForm();
        else
            return super.getControl();
    }

    public void createPaneToolBar() {
        super.createPaneToolBar();

        addToolItem("TT_D_TT_COMMIT_ALL", "commit",
            new SelectionAdapter() {
                public void widgetSelected(SelectionEvent s) {
                    getCore().getFileInfoIntMap().commitAll();
                }
            });

        if (advancedMode)
            addToolItem("TT_D_TT_SHOW_CLIENTS", "split-table",
                new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent s) {
                        ((DownloadTableTreeView) gView).toggleClientsTable();
                    }
                });

        addToolItem("TT_D_TT_COLLAPSE_ALL", "collapseAll",
            new SelectionAdapter() {
                public void widgetSelected(SelectionEvent s) {
                    ((CustomTableTreeViewer) gView.getViewer()).collapseAll();
                }
            });

        addToolItem("TT_D_TT_EXPAND_ALL", "expandAll",
            new SelectionAdapter() {
                public void widgetSelected(SelectionEvent s) {
                    ((CustomTableTreeViewer) gView.getViewer()).expandAll();
                }
            });
    }
}


/*
$Log: DownloadViewFrame.java,v $
Revision 1.1  2003/11/24 01:33:27  zet
move some classes

*/
