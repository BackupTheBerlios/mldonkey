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

import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.view.MessagesTab;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;

/**
 * TableMenuListener
 *
 * @author $Author: zet $
 * @version $Id: TableMenuListener.java,v 1.2 2003/08/18 01:42:24 zet Exp $ 
 *
 */
public class TableMenuListener implements ISelectionChangedListener, IMenuListener {

	private TableContentProvider tableContentProvider;
	private TableViewer tableViewer;
	private CoreCommunication core;
	private ClientInfo selectedClientInfo;
	private MessagesTab messagesTab;

	/**
	 * Creates a new TableMenuListener
	 * @param The parent TableViewer
	 * @param The CoreCommunication supporting this with data
	 */
	public TableMenuListener( TableViewer tableViewer, CoreCommunication core, MessagesTab messagesTab ) {
		super();
		this.tableViewer = tableViewer;
		this.core = core;
		this.messagesTab = messagesTab;
		this.tableContentProvider = ( TableContentProvider ) this.tableViewer.getContentProvider();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public void selectionChanged( SelectionChangedEvent event ) {
		IStructuredSelection sSel = ( IStructuredSelection ) event.getSelection();
		Object o = sSel.getFirstElement();

		if ( o instanceof ClientInfo )
			selectedClientInfo = ( ClientInfo ) o;
		else
			selectedClientInfo = null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */
	public void menuAboutToShow( IMenuManager menuManager ) {
		/* disconnect */
		if ( selectedClientInfo != null )
			menuManager.add( new RemoveFriendAction() );
			
		menuManager.add( new RemoveAllFriendsAction() );

		if (selectedClientInfo != null)
			menuManager.add( new SendMessageAction() );
		
	}
	private class RemoveFriendAction extends Action {
		public RemoveFriendAction() {
			super();
			setText( G2GuiResources.getString( "FR_MENU_REMOVE_FRIEND" ) );
		}
		public void run() {
			ClientInfo.removeFriend(core, selectedClientInfo.getClientid());
		}
	}

	private class RemoveAllFriendsAction extends Action {
		public RemoveAllFriendsAction() {
			super();
			setText( G2GuiResources.getString( "FR_MENU_REMOVE_ALL_FRIENDS" ) );
		}
		public void run() {
			ClientInfo.removeAllFriends(core);
		}
	}

	private class SendMessageAction extends Action {
			public SendMessageAction() {
				super();
				setText( G2GuiResources.getString( "FR_MENU_SEND_MESSAGE" ) );
			}
			public void run() {
				messagesTab.openTab(selectedClientInfo);
			}
	}

}

/*
$Log: TableMenuListener.java,v $
Revision 1.2  2003/08/18 01:42:24  zet
centralize resource bundle

Revision 1.1  2003/08/12 04:10:29  zet
try to remove dup clientInfos, add friends/basic messaging



*/