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
package net.mldonkey.g2gui.view.helper;

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.view.GuiTab;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.viewers.GView;

import org.eclipse.jface.action.MenuManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;


/**
 * ViewFrame
 *
 * @version $Id: ViewFrame.java,v 1.4 2003/11/27 21:42:33 zet Exp $
 *
 */
public class ViewFrame {
    protected GuiTab guiTab;
    protected ViewForm viewForm;
    protected CLabel cLabel;
    protected SashForm parentSashForm;
    protected Composite childComposite;
    protected MenuManager menuManager;
    protected GView gView;
    protected ToolBar toolBar;

    public ViewFrame(SashForm parentSashForm, String prefString, String prefImageString,
        GuiTab guiTab) {
        this.parentSashForm = parentSashForm;
        this.guiTab = guiTab;

        viewForm = WidgetFactory.createViewForm(parentSashForm);

        childComposite = new Composite(viewForm, SWT.NONE);
        childComposite.setLayout(new FillLayout());

        cLabel = WidgetFactory.createCLabel(viewForm, prefString, prefImageString);

        viewForm.setContent(childComposite);
        viewForm.setTopLeft(cLabel);
    }

    /**
     * @param gPaneListener
     */
    public void createPaneListener(ViewFrameListener viewFrameListener) {
        menuManager = new MenuManager("");
        menuManager.setRemoveAllWhenShown(true);
        menuManager.addMenuListener(viewFrameListener);

        cLabel.addMouseListener(new MaximizeSashMouseAdapter(cLabel, menuManager,
                getParentSashForm(), getControl()));
        cLabel.addDisposeListener(new DisposeListener() {
                public void widgetDisposed(DisposeEvent e) {
                    menuManager.dispose();
                }
            });
    }

    public void createPaneToolBar() {
        toolBar = new ToolBar(viewForm, SWT.RIGHT | SWT.FLAT);
        viewForm.setTopRight(toolBar);
    }

    /**
     * @param resToolTipString
     * @param resImageString
     * @param selectionListener
     */
    public void addToolItem(String resToolTipString, String resImageString,
        SelectionListener selectionListener) {
        ToolItem toolItem = new ToolItem(toolBar, SWT.NONE);
        toolItem.setToolTipText(G2GuiResources.getString(resToolTipString));
        toolItem.setImage(G2GuiResources.getImage(resImageString));
        toolItem.addSelectionListener(selectionListener);
    }

    public GuiTab getGuiTab() {
        return guiTab;
    }

    public CoreCommunication getCore() {
        return getGuiTab().getMainTab().getCore();
    }

    public Composite getChildComposite() {
        return childComposite;
    }

    public SashForm getParentSashForm() {
        return parentSashForm;
    }

    public CLabel getCLabel() {
        return cLabel;
    }

    public GView getGView() {
        return gView;
    }

    public ViewForm getViewForm() {
        return viewForm;
    }

    public Control getControl() {
        return getViewForm();
    }

    public void updateCLabelText(String string) {
        if ((cLabel != null) && !cLabel.isDisposed())
            if (!cLabel.getText().equals(string))
                cLabel.setText(string);
    }

    public void updateCLabelTextInGuiThread(final String string) {
        if ((cLabel != null) && !cLabel.isDisposed())
            cLabel.getDisplay().asyncExec(new Runnable() {
                    public void run() {
                        updateCLabelText(string);
                    }
                });

    }
}


/*
$Log: ViewFrame.java,v $
Revision 1.4  2003/11/27 21:42:33  zet
integrate ViewFrame a little more.. more to come.

Revision 1.3  2003/11/24 01:33:27  zet
move some classes

Revision 1.2  2003/11/23 21:53:09  zet
comment

Revision 1.1  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

*/
