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
package net.mldonkey.g2gui.view.transfer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.view.helper.WidgetFactory;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


/**
 * DetailDialog
 *
 * @version $Id: DetailDialog.java,v 1.3 2003/12/01 14:22:45 lemmster Exp $
 *
 */
public abstract class DetailDialog extends Dialog implements Observer {
    protected ArrayList chunkCanvases = new ArrayList();
    private int leftColumn = 100;
    private int rightColumn = leftColumn * 3;

    protected DetailDialog(Shell parentShell) {
        super(parentShell);
    }

    
	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setImage(G2GuiResources.getImage("ProgramIcon"));
	}
    
    /**
     * @param composite
     * @param resString
     * @param longlabel
     * @return
     */
    protected CLabel createLine(Composite composite, String resString, boolean longLabel) {
        Label label = new Label(composite, SWT.NONE);
        label.setText(G2GuiResources.getString(resString));

        GridData gridData = new GridData();
        gridData.widthHint = leftColumn;
        label.setLayoutData(gridData);

        CLabel cLabel = new CLabel(composite, SWT.NONE);
        gridData = new GridData();

        if (longLabel) {
            gridData.widthHint = rightColumn;
            gridData.horizontalSpan = 3;
        } else {
            gridData.widthHint = leftColumn;
        }

        cLabel.setLayoutData(gridData);

        return cLabel;
    }

    /**
     * @param parent
     * @param string
     * @param clientInfo
     * @param fileInfo
     * @param networkInfo
     * @return ChunkCanvas
     */
    protected ChunkCanvas createChunkGroup(Composite parent, String string, ClientInfo clientInfo,
        FileInfo fileInfo, NetworkInfo networkInfo) {
        Group chunkGroup = new Group(parent, SWT.SHADOW_ETCHED_OUT);

        String totalChunks = "";

        if (networkInfo == null) {
            totalChunks = (clientInfo == null) ? (" (" + fileInfo.getAvail().length() + ")") : "";
        } else {
        	if (fileInfo.hasAvails() ) {
                totalChunks = " (" + ((String) fileInfo.getAvails(networkInfo)).length() +
                    ")";
            }
        }

        chunkGroup.setText(string + totalChunks);
        chunkGroup.setLayout(WidgetFactory.createGridLayout(1, 5, 2, 0, 0, false));
        chunkGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        ChunkCanvas chunkCanvas = new ChunkCanvas(chunkGroup, SWT.NO_BACKGROUND, clientInfo,
                fileInfo, networkInfo);

        GridData canvasGD = new GridData(GridData.FILL_HORIZONTAL);
        canvasGD.heightHint = 18;
        chunkCanvas.setLayoutData(canvasGD);

        chunkCanvases.add(chunkCanvas);

        return chunkCanvas;
    }

    /**
     * @param cLabel
     * @param string
     */
    protected void updateLabel(CLabel cLabel, String string) {
        if (!cLabel.isDisposed()) {
            cLabel.setText(string);
            cLabel.setToolTipText((string.length() > 10) ? string : "");
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.window.Window#close()
     */
    public boolean close() {
        Iterator i = chunkCanvases.iterator();

        while (i.hasNext())
            ((ChunkCanvas) i.next()).dispose();

        return super.close();
    }

    /**
     * updateLabels()
     */
    public abstract void updateLabels();

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable o, Object arg) {
        if (!getShell().isDisposed()) {
            getShell().getDisplay().asyncExec(new Runnable() {
                    public void run() {
                        updateLabels();
                    }
                });
        }
    }
}


/*
$Log: DetailDialog.java,v $
Revision 1.3  2003/12/01 14:22:45  lemmster
ProtocolVersion handling completely rewritten

Revision 1.2  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

Revision 1.1  2003/11/11 02:31:33  zet
initial

*/
