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
package net.mldonkey.g2gui.view.server;

import net.mldonkey.g2gui.model.ServerInfoIntMap;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * TableContentProvider
 *
 * @author $user$
 * @version $Id: TableContentProvider.java,v 1.1 2003/08/05 13:50:10 lemmstercvs01 Exp $ 
 *
 */
public class TableContentProvider implements IStructuredContentProvider {
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements( Object inputElement ) {
		ServerInfoIntMap servers = ( ServerInfoIntMap ) inputElement;
		return servers.getServers();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() { }

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#
	 * inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged( Viewer viewer, Object oldInput, Object newInput ) { }
}

/*
$Log: TableContentProvider.java,v $
Revision 1.1  2003/08/05 13:50:10  lemmstercvs01
initial commit

*/