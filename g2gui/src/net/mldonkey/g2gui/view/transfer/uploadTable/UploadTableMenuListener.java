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
import net.mldonkey.g2gui.model.SharedFileInfo;
import net.mldonkey.g2gui.model.SharedFileInfoIntMap;
import net.mldonkey.g2gui.view.helper.TableMenuListener;
import net.mldonkey.g2gui.view.resource.G2GuiResources;
import net.mldonkey.g2gui.view.transfer.uploadTable.UploadTableViewer.MyContentProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
/**
 * UploadTableMenuListener
 *
 * @version $Id: UploadTableMenuListener.java,v 1.1 2003/09/26 11:55:48 dek Exp $ 
 *
 */
class UploadTableMenuListener
	extends TableMenuListener
	implements ISelectionChangedListener, IMenuListener {
	/**
	 * CopyEd2kLinkAction
	 *
	 * @version $Id: UploadTableMenuListener.java,v 1.1 2003/09/26 11:55:48 dek Exp $ 
	 *
	 */
	private SharedFileInfoIntMap sharedFileInfoMap;
	private MyContentProvider tableContentProvider;
	private SharedFileInfo selectedFile;
	/**
	 * Creates a new TableMenuListener
	 * @param tableViewer The TableViewer
	 * @param core The CoreCommunication supporting this with data
	 */
	public UploadTableMenuListener( StructuredViewer tableViewer, CoreCommunication core ) {
		super( tableViewer, core );
		this.sharedFileInfoMap = this.core.getSharedFileInfoIntMap();
		this.tableContentProvider =
			( UploadTableViewer.MyContentProvider ) this.tableViewer.getContentProvider();
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
	}
	
	private class CopyEd2kLinkAction extends Action {
		public CopyEd2kLinkAction() {
			super();
			setText( G2GuiResources.getString( "TT_DOWNLOAD_MENU_LINKTO" ) );
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
}
/*
$Log: UploadTableMenuListener.java,v $
Revision 1.1  2003/09/26 11:55:48  dek
right-mouse menue for upload-Table

*/