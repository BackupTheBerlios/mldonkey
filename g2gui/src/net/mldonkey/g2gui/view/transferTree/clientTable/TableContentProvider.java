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
package net.mldonkey.g2gui.view.transferTree.clientTable;

import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.view.MainTab;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

/**
 * TableContentProvider
 *
 */
public class TableContentProvider implements IStructuredContentProvider, Observer {
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	 
	private TableViewer clientTableViewer;
	private long lastUpdateTime;
	 
	public Object[] getElements( Object inputElement ) {
		FileInfo fileInfo = (FileInfo) inputElement;
		return fileInfo.getClientInfos().toArray();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() { }

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#
	 * inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged( Viewer viewer, Object oldInput, Object newInput ) { 
		clientTableViewer = (TableViewer) viewer;
		if (oldInput != null) {
			FileInfo oldFileInfo = (FileInfo) oldInput;
			oldFileInfo.deleteObserver(this);
		}
		if (newInput != null) {
			FileInfo newFileInfo = (FileInfo) newInput;
			newFileInfo.addObserver(this);
		}
	}
	
	public void update(Observable o, final Object obj) {
		if (obj instanceof ClientInfo) {
			Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				refreshTable((ClientInfo) obj);
			}
		});
		}
	}
	
	// delay for 5 seconds to prevent too much flicker
	// it seems like you must do a full refresh() to maintain sort order
	public void refreshTable(ClientInfo clientInfo) {
		if (clientTableViewer != null || !MainTab.getShell().isDisposed()) {
			if (System.currentTimeMillis() > lastUpdateTime + 5000) {
				clientTableViewer.refresh();
				lastUpdateTime = System.currentTimeMillis();
			} else {
				clientTableViewer.update(clientInfo, null); // widget disposed
			}
		}
	}
}

/*
$Log: TableContentProvider.java,v $
Revision 1.1  2003/08/20 14:58:43  zet
sources clientinfo viewer



*/