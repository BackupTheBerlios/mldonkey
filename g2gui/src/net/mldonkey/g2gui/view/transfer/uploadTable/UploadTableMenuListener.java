/*
 * Copyright 2003
 * g2gui-devel Team
 * 
 * 
 * This file is part of g2gui-devel.
 *
 * g2gui-devel is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * ( at your option ) any later version.
 *
 * g2gui-devel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui-devel; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.view.transfer.uploadTable;
import net.mldonkey.g2gui.comm.CoreCommunication;
import net.mldonkey.g2gui.comm.EncodeMessage;
import net.mldonkey.g2gui.comm.Message;
import net.mldonkey.g2gui.model.SharedFileInfo;
import net.mldonkey.g2gui.model.SharedFileInfoIntMap;
import net.mldonkey.g2gui.view.helper.TableMenuListener;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
/**
 * UploadTableMenuListener
 *
 * @version $Id: UploadTableMenuListener.java,v 1.4 2003/10/21 17:00:45 lemmster Exp $ 
 *
 */
class UploadTableMenuListener extends TableMenuListener implements ISelectionChangedListener, IMenuListener {
	private SharedFileInfoIntMap sharedFileInfoMap;
	private SharedFileInfo selectedFile;

	/**
	 * Creates a new TableMenuListener
	 * @param tableViewer The TableViewer
	 * @param core The CoreCommunication supporting this with data
	 */
	public UploadTableMenuListener( CoreCommunication core ) {
		super( core );
		setTableViewer( tableViewer );
		this.sharedFileInfoMap = this.core.getSharedFileInfoIntMap();
	}
	/* ( non-Javadoc )
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged( org.eclipse.jface.viewers.SelectionChangedEvent )
	 */
	public void selectionChanged( SelectionChangedEvent event ) {
		IStructuredSelection sSel = ( IStructuredSelection ) event.getSelection();
		Object o = sSel.getFirstElement();
		if ( o instanceof SharedFileInfo )
			selectedFile = ( SharedFileInfo ) o;
		else {
			selectedFile = null;
			tableViewer.setSelection( null );
		}
	}

	public void menuAboutToShow( IMenuManager menuManager ) {
		/*copy ED2K-Link*/
		if ( ( selectedFile != null ) )
			menuManager.add( new CopyEd2kLinkAction() );
		menuManager.add( new RefreshUploadsAction( ) );
	}
	
	private class CopyEd2kLinkAction extends Action {
		public CopyEd2kLinkAction() {
			super();
			setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_COPYTO" ) );
		}
		public void run() {
			Clipboard clipboard =
				new Clipboard( ( ( TableViewer ) tableViewer ).getTable().getDisplay() );
			String aString = selectedFile.getED2K();
			clipboard.setContents( 
				new Object[] { aString },
				new Transfer[] { TextTransfer.getInstance()} );
			clipboard.dispose();
		}
	}

	/**
	 * RefreshUploadsAction
	 */
	private class RefreshUploadsAction extends Action {
	   public RefreshUploadsAction( ) {
		   super( "Refresh" );
	   }

	   public void run(  ) {
			Message refresh = new EncodeMessage( Message.S_REFRESH_UPLOAD_STATS );
			refresh.sendMessage( core );
	   }
   }
}
/*
$Log: UploadTableMenuListener.java,v $
Revision 1.4  2003/10/21 17:00:45  lemmster
class hierarchy for tableviewer

Revision 1.3  2003/10/19 22:31:53  zet
nil

Revision 1.2  2003/09/27 00:02:37  dek
bugfixes, merged right-mouse-click menues (nothing is uglier than one-item-menues)

Revision 1.1  2003/09/26 11:55:48  dek
right-mouse menue for upload-Table

*/