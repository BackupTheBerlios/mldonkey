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
package net.mldonkey.g2gui.view.friends;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import net.mldonkey.g2gui.model.ClientInfo;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * TableContentProvider
 *
 *
 * @version $Id: FriendsTableContentProvider.java,v 1.4 2003/09/04 02:35:06 zet Exp $
 */
public class FriendsTableContentProvider implements IStructuredContentProvider, Observer {
	
	public TableViewer viewer;
	public List observedClients = Collections.synchronizedList(new ArrayList());
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements( Object inputElement ) {
		
		for (int i = 0; i < observedClients.size(); i++) {
			ClientInfo clientInfo = (ClientInfo) observedClients.get(i);
			if (clientInfo != null) {
				clientInfo.deleteObserver( this );
			}
		}
		
		observedClients.clear();
		
		for (int i = 0; i < ((List) inputElement).size(); i++) {
			((ClientInfo) ((List) inputElement).get(i)).addObserver(this);
			observedClients.add((ClientInfo) ((List) inputElement).get(i));
		}
				
		return ((List) inputElement).toArray();
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
		this.viewer = (TableViewer) viewer;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update (final Observable o, Object obj) {
		if (o instanceof ClientInfo && viewer != null) {
			if (viewer.getTable().isDisposed()) return;
			viewer.getTable().getDisplay().asyncExec(new Runnable() {
				public void run() {
					if (viewer != null)
						viewer.update((ClientInfo) o, null);
				}
			});
		}
	}
	
}

/*
$Log: FriendsTableContentProvider.java,v $
Revision 1.4  2003/09/04 02:35:06  zet
check disposed

Revision 1.3  2003/08/31 15:37:30  zet
friend icons

Revision 1.2  2003/08/23 15:21:37  zet
remove @author

Revision 1.1  2003/08/23 09:47:46  lemmster
just rename

Revision 1.2  2003/08/22 21:10:57  lemmster
replace $user$ with $Author: zet $

Revision 1.1  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging



*/