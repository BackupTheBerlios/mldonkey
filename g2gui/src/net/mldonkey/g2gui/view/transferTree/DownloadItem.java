/*
 * Copyright 2003
 * G2GUI Team
 * 
 * 
 * This file is part of G2GUI.
 *
 * G2GUI is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * ( at your option ) any later version.
 *
 * G2GUI is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2GUI; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.view.transferTree;



import java.util.Iterator;
import java.util.ResourceBundle;

import gnu.trove.TIntObjectHashMap;

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.model.enum.EnumPriority;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.custom.TableTreeEditor;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;


/**
 * DownloadItem
 *
 * @author $user$
 * @version $Id: DownloadItem.java,v 1.9 2003/07/15 13:25:41 dek Exp $ 
 *
 */
public class DownloadItem 
			extends TableTreeItem 
			implements IItemHasMenue, SelectionListener 
			{
	private MenuItem 	pauseItem, resumeItem, cancelItem,
						renameItem,
						linkItem,
						clearItem,
						fakeItem,
						prio1Item, prio2Item, prio3Item;
	

	private ChunkView chunks;

	private TableTree tableTree;

	private TIntObjectHashMap namedclients = new TIntObjectHashMap();

	private FileInfo fileInfo;

	/**
	 * @param parent where this Item should appear
	 * @param style the style
	 * @param fileInfo the fileInfo, this Object represents
	 */
	public DownloadItem( TableTree parent, int style, FileInfo fileInfo ) {
		super( parent, style );		
		this.tableTree = parent;
		this.fileInfo = fileInfo;
		
		TableTreeEditor editor = new TableTreeEditor( this.getParent() );		
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		Control oldEditor = editor.getEditor();
			if ( oldEditor != null )
			oldEditor.dispose();
		this.chunks = new ChunkView( this.getParent().getTable(), SWT.NONE, fileInfo, 4 );
		editor.setEditor ( chunks, this, 4 );	
		
		setText( 6, String.valueOf( fileInfo.getSize() ) );
		setText( 0, String.valueOf( fileInfo.getId() ) );
			
		updateColumns();

		Iterator it =  fileInfo.getClientInfos().iterator();		
		while ( it.hasNext() ) {
		ClientInfo clientInfo =  ( ClientInfo )it.next();
			
	//			Here comes the question, wether we want to add this clientInfo, or not?? at the moment, 
	//			all clientInfos are accepted
				
					if (  clientInfo.getState().getState() == EnumState.CONNECTED_DOWNLOADING ) {				
							if ( namedclients.containsKey( clientInfo.getClientid() ) ) {
								namedclients.get( clientInfo.getClientid() );
								ClientItem existingItem =
									( ClientItem ) namedclients.get( clientInfo.getClientid() );
								existingItem.update();
							} else {
								ClientItem newItem = new ClientItem( this, SWT.NONE, clientInfo );
								namedclients.put( clientInfo.getClientid(), newItem );
							}
					}
					else if ( namedclients.contains( clientInfo.getClientid() ) ) {
						ClientItem toBeRemovedItem =
									( ClientItem ) namedclients.get( clientInfo.getClientid() );
						toBeRemovedItem.dispose();
						namedclients.remove( clientInfo.getClientid() );
						
					}		
			}

		
	}
	
	void update() {
		updateColumns();		
		Iterator it =  fileInfo.getClientInfos().iterator();		
		while ( it.hasNext() ) {
		ClientInfo clientInfo =  ( ClientInfo )it.next();
		
		// Here comes the question, wether we want to add this clientInfo, or not?? at the moment, 
		// all clientInfos are accepted
						if (  clientInfo.getState().getState() == EnumState.CONNECTED_DOWNLOADING ) {				
							if ( namedclients.containsKey( clientInfo.getClientid() ) ) {
								namedclients.get( clientInfo.getClientid() );
								ClientItem existingItem =
									( ClientItem ) namedclients.get( clientInfo.getClientid() );
								existingItem.update();
							} else {
								ClientItem newItem = new ClientItem( this, SWT.NONE, clientInfo );
								namedclients.put( clientInfo.getClientid(), newItem );
							}
					}
					else if ( namedclients.contains( clientInfo.getClientid() ) ) {
						ClientItem toBeRemovedItem =
									( ClientItem ) namedclients.get( clientInfo.getClientid() );
						toBeRemovedItem.dispose();
						namedclients.remove( clientInfo.getClientid() );
					
					}
				
			}
		
	}

	private void updateColumns() {
		//TableColumn column = tableTree.getTable().getColumn( 0 );
		//System.out.printcolumn.getText()
		setText( 1, fileInfo.getName() );
		setText( 2, String.valueOf( fileInfo.getRate() ) );
		setText( 3, String.valueOf( fileInfo.getDownloaded() ) );
		setText( 5, String.valueOf( fileInfo.getPerc() ) );		
		chunks.refresh();
	}
	

	
	
	/**
	 * @return the fileInfo, this Object represents
	 */
	public FileInfo getFileInfo() {
		return fileInfo;
	}

	/** ( non-Javadoc )
	 * @see net.mldonkey.g2gui.view.transferTree.IItemHasMenue#getMenu()
	 */
	public void createMenu( Menu menu ) {
		ResourceBundle bundle = ResourceBundle.getBundle( "g2gui" );
		pauseItem = new MenuItem( menu, SWT.PUSH );
		pauseItem.setText( bundle.getString( "TT_Menu0" ) );
		pauseItem.addSelectionListener( this );
		resumeItem = new MenuItem( menu, SWT.PUSH );
		resumeItem.setText( bundle.getString( "TT_Menu1" ) );
		resumeItem.addSelectionListener( this );
		cancelItem = new MenuItem( menu, SWT.PUSH );
		cancelItem.setText( bundle.getString( "TT_Menu2" ) );
		cancelItem.addSelectionListener( this );
		renameItem = new MenuItem( menu, SWT.PUSH );
		renameItem.setText( bundle.getString( "TT_Menu3" ) );
		renameItem.addSelectionListener( this );
		linkItem = new MenuItem( menu, SWT.PUSH );
		linkItem.setText( bundle.getString( "TT_Menu4" ) );
		linkItem.addSelectionListener( this );
		clearItem = new MenuItem( menu, SWT.PUSH );
		clearItem.setText( bundle.getString( "TT_Menu8" ) );
		clearItem.addSelectionListener( this );
		fakeItem = new MenuItem( menu, SWT.PUSH );
		fakeItem.setText( bundle.getString( "TT_Menu5" ) );
		fakeItem.addSelectionListener( this );
		// new as of 1.8
		MenuItem prioMenuItem = new MenuItem( menu, SWT.CASCADE );
		Menu prioMenu = new Menu( menu );
		prioMenuItem.setMenu( prioMenu );
		prioMenuItem.setText( bundle.getString( "TT_Menu6" ) );
		prio1Item = new MenuItem( prioMenu, SWT.PUSH );
		prio1Item.setText( bundle.getString( "TT_Menu_Prio_High" ) );
		prio1Item.addSelectionListener( this );
		prio2Item = new MenuItem( prioMenu, SWT.PUSH );
		prio2Item.setText( bundle.getString( "TT_Menu_Prio_Medium" ) );
		prio2Item.addSelectionListener( this );
		prio3Item = new MenuItem( prioMenu, SWT.PUSH );
		prio3Item.setText( bundle.getString( "TT_Menu_Prio_Low" ) );
		prio3Item.addSelectionListener( this );
	}

	/** (non-Javadoc)
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	 public void widgetSelected( SelectionEvent arg0 ) {
		 widgetDefaultSelected( arg0 );
	 }

	/** ( non-Javadoc )
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected( org.eclipse.swt.events.SelectionEvent )
	 */
	public void widgetDefaultSelected( SelectionEvent arg0 ) {
		if ( arg0.widget instanceof MenuItem ) {
			MenuItem item = ( MenuItem ) arg0.widget;
			if ( item == pauseItem ) {
				fileInfo.setState( EnumFileState.PAUSED );
			}
			if ( item == resumeItem ) {
				fileInfo.setState( EnumFileState.DOWNLOADING );
			}
			if ( item == cancelItem ) {
				fileInfo.setState( EnumFileState.CANCELLED );
			}
			if ( item == linkItem ) {
				Clipboard clipBoard = new Clipboard( item.getDisplay() );
				String link =
					"ed2k://|file|"
						+ fileInfo.getName()
						+ "|"
						+ fileInfo.getSize()
						+ "|"
						+ fileInfo.getMd4()
						+ "|/";
				clipBoard.setContents( 
					new Object[] { link },
					new Transfer[] { TextTransfer.getInstance()} );
				clipBoard.dispose();
			}
			if ( item == fakeItem ) {
				String link =
					"ed2k://|file|"
						+ fileInfo.getName()
						+ "|"
						+ fileInfo.getSize()
						+ "|"
						+ fileInfo.getMd4()
						+ "|/";
				Program.findProgram( ".htm" ).execute( 
					"http://edonkeyfakes.ath.cx/fakecheck/update/fakecheck.php?ed2k="
						+ link );
			}
			if ( item == prio1Item )
				fileInfo.setPriority( EnumPriority.HIGH );
			if ( item == prio2Item )
				fileInfo.setPriority( EnumPriority.NORMAL );
			if ( item == prio3Item )
				fileInfo.setPriority( EnumPriority.LOW );
		}
	}


}

/*
$Log: DownloadItem.java,v $
Revision 1.9  2003/07/15 13:25:41  dek
right-mouse menu and some action to hopefully avoid flickering table

Revision 1.8  2003/07/14 19:26:40  dek
done some clean.up work, since it seems,as if this view becomes reality..

Revision 1.7  2003/07/13 20:12:39  dek
fixed Exception and applied checkstyle

Revision 1.6  2003/07/13 17:13:08  dek
NPE fixed

Revision 1.5  2003/07/13 12:48:28  dek
chunk-bar begins to work

Revision 1.4  2003/07/12 21:49:36  dek
transferring and queued clients shown

Revision 1.3  2003/07/12 16:20:36  dek
*** empty log message ***

Revision 1.2  2003/07/12 13:50:01  dek
nothing to do, so i do senseless idle-working

Revision 1.1  2003/07/11 17:53:51  dek
Tree for Downloads - still unstable

*/