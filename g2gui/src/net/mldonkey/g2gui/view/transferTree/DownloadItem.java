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
 * (  at your option  ) any later version.
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.ResourceBundle;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;
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
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
/**
 * DownloadItem
 *
 * @author $user$
 * @version $Id: DownloadItem.java,v 1.20 2003/07/20 12:35:07 dek Exp $ 
 *
 */
public class DownloadItem extends TableTreeItem implements IItemHasMenue, SelectionListener {
		
	private MenuItem pauseItem,
		resumeItem,
		cancelItem,
		renameItem,
		linkItem,
		clearItem,
		fakeItem,
		prio1Item,
		prio2Item,
		prio3Item;
	private ChunkView chunks;
	private TableTree tableTree;
	private TIntObjectHashMap namedclients = new TIntObjectHashMap();
	private FileInfo fileInfo;
	private TableTreeEditor editor;
	/**
	 * @param parent where this Item should appear
	 * @param style the style
	 * @param fileInfo the fileInfo, this Object represents
	 */
	public DownloadItem( TableTree parent, int style, FileInfo fileInfo ) {
		super( parent, style );
		this.tableTree = parent;
		this.fileInfo = fileInfo;
		editor = new TableTreeEditor( this.getParent() );
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		/*
		 * Now fill the columns with initial values, that never change...
		 */
		updateCell( 0, String.valueOf( fileInfo.getId() ) );
		updateCell( 1, fileInfo.getNetwork().getNetworkName() );
		updateCell( 2, "" );
		updateCell( 3, "" );
		/*creating the chunkb-bar*/		
			Control oldEditor = editor.getEditor();
			if ( oldEditor != null )
				oldEditor.dispose();
			this.chunks =
				new ChunkView( this.getParent().getTable(), SWT.NONE, fileInfo, 4 );
			editor.setEditor( chunks, this, 4 );
		updateCell( 4, "" );
		updateCell( 5, "" );
		updateCell( 6, "" );
		updateCell( 7, String.valueOf( fileInfo.getSize() ) );
		updateColumns();
		Iterator it = fileInfo.getClientInfos().iterator();
		while ( it.hasNext() ) {
			ClientInfo clientInfo = ( ClientInfo ) it.next();
			if ( isInteresting( clientInfo ) ) {
				if ( namedclients.containsKey( clientInfo.getClientid() ) ) {
					namedclients.get( clientInfo.getClientid() );
					ClientItem existingItem =
						( ClientItem ) namedclients.get( clientInfo.getClientid() );
					existingItem.update();
				} else {
					ClientItem newItem =
						new ClientItem( this, SWT.NONE, clientInfo );
					namedclients.put( clientInfo.getClientid(), newItem );
				}
			} else if ( namedclients.contains( clientInfo.getClientid() ) ) {
				ClientItem toBeRemovedItem =
					( ClientItem ) namedclients.get( clientInfo.getClientid() );
				toBeRemovedItem.dispose();
				namedclients.remove( clientInfo.getClientid() );
			}
		}
		addDisposeListener( new DisposeListener() {
			public void widgetDisposed( DisposeEvent e ) {
				chunks.dispose();
				//resetting the chunk-bar editor is a must 
				editor.setEditor( null );
				TIntObjectIterator it = namedclients.iterator();
				while ( it.hasNext() ) {
					while ( it.hasNext() ) {
						it.advance();
						ClientItem clientItem = ( ClientItem ) it.value();
						clientItem.dispose();
					}
				}
			}
		} );
	}
	/**
	 * simply decide, wether we want to display this clientInfo as ClientItem by asking some
	 * questions.
	 * @param clientInfo
	 * @return is this client info worth to show?
	 */
	private boolean isInteresting( ClientInfo clientInfo ) {
		
		if /*we are downloading from this client, so he is interesting*/
			( clientInfo.getState().getState() == EnumState.CONNECTED_DOWNLOADING )
			return true;
		else if /*we are connected to this client and have a queue-rank != 0*/
			( clientInfo.getState().getState() == EnumState.CONNECTED_AND_QUEUED
					&& clientInfo.getState().getRank() != 0 )
			return true;
		else if /*we were connected to this client but have a queue-rank != 0*/
			( clientInfo.getState().getState() == EnumState.NOT_CONNECTED_WAS_QUEUED
					&& clientInfo.getState().getRank() != 0 )
			return true;
		else
			return false;
	}
	/**
	 * Sorts the children( ClientItems ) of this DownloadItem, with the given
	 * column-hint
	 * @param columnIndex
	 * @param order the sort order: ascending if > 0. desc, otherwise
	 */
	protected void sort( int columnIndex, int order ) {
		Object[] items = namedclients.getValues();
		ClientInfo[] clients = new ClientInfo[ items.length ];
		int[] expanded = new int[ items.length ];
		for ( int i = 0; i < items.length; i++ ) {
			ClientItem temp = ( ( ClientItem ) items[ i ] );
			clients[ i ] = temp.getClientInfo();
			temp.dispose();
			namedclients.clear();
		}
		Arrays.sort( clients, new ClientInfoComparator( columnIndex, fileInfo ) );
		if ( order > 0 ) {
			for ( int i = 0; i < clients.length; i++ ) {
				ClientItem newItem = new ClientItem( this, SWT.NONE, clients[ i ] );
				namedclients.put( clients[ i ].getClientid(), newItem );
			}
		} else {
			// reverse order if the current column is selected again
			int j = clients.length - 1;
			for ( int i = clients.length - 1; i >= 0; i-- ) {
				ClientItem newItem = new ClientItem( this, SWT.NONE, clients[ i ] );
				namedclients.put( clients[ i ].getClientid(), newItem );
			}
		}
	}
	void update() {
		updateColumns();
		Iterator it = fileInfo.getClientInfos().iterator();
		while ( it.hasNext() ) {
			ClientInfo clientInfo = ( ClientInfo ) it.next();
			// Here comes the question, wether we want to add this clientInfo, or not?? at the moment, 
			// all clientInfos are accepted
			if ( isInteresting( clientInfo ) ) {
				if ( namedclients.containsKey( clientInfo.getClientid() ) ) {
					namedclients.get( clientInfo.getClientid() );
					ClientItem existingItem =
						( ClientItem ) namedclients.get( clientInfo.getClientid() );
					existingItem.update();
				} else {
					ClientItem newItem =
						new ClientItem( this, SWT.NONE, clientInfo );
					namedclients.put( clientInfo.getClientid(), newItem );
				}
			} else if ( namedclients.contains( clientInfo.getClientid() ) ) {
				ClientItem toBeRemovedItem =
					( ClientItem ) namedclients.get( clientInfo.getClientid() );
				toBeRemovedItem.dispose();
				namedclients.remove( clientInfo.getClientid() );
			}
		}
	}
	private void updateColumns() {
		//"ID"|"Network"|"Filename"|"Rate"|"Chunks"|"%"|"Downloaded"|"Size"
		//  0     1           2        3       4      5    6            7		
		if ( getText( 0 ) != null && !getText( 0 ).equals( String.valueOf( fileInfo.getId() ) ) ) {
			updateCell( 0, String.valueOf( fileInfo.getId() ) );			
		}
		if ( getText( 1 ) != null && !getText( 1 ).equals( fileInfo.getNetwork().getNetworkName() ) ) {
			updateCell( 1, fileInfo.getNetwork().getNetworkName() );			
		}
		if ( getText( 2 ) != null && !getText( 2 ).equals( fileInfo.getName() ) ) {
			updateCell( 2, fileInfo.getName()  );			
		}
		/*descision, what to display in rate-column, rate, or paused??*/
			if ( fileInfo.getState().getState() == EnumFileState.PAUSED ) {		
				if ( getText( 3 ) != null && !getText( 3 ).equals( "paused" ) ) {
				updateCell( 3, "paused" );			
				}
			}			
			else if ( getText( 3 ) != null && !getText( 3 ).equals( String.valueOf( fileInfo.getRate() ) ) )
					updateCell( 3, String.valueOf( fileInfo.getRate() ) );

		chunks.refresh();
		
		
		if ( getText( 5 ) != null && !getText( 5 ).equals( String.valueOf( fileInfo.getPerc() ) ) ) {
			updateCell( 5, String.valueOf( fileInfo.getPerc() ) );			
		}
		if ( getText( 6 ) != null && !getText( 6 ).equals( String.valueOf( fileInfo.getDownloaded() ) ) ) {
			updateCell( 6, String.valueOf( fileInfo.getDownloaded() ) );			
		}
		if ( getText( 7 ) != null && !getText( 7 ).equals( String.valueOf(  fileInfo.getSize()  ) ) ) {
			updateCell( 7, String.valueOf(  fileInfo.getSize()  ) );			
		}
		

	}
	/**
	 * Sets the text of the specific Cell in this row and redraw it
	 * @param column the column, in which this cell lives
	 * @param text what do we want do display in this cell
	 */
	private void updateCell( int column, String text ) {
		setText( column, text );
		int x = getBounds( column ).x;
		int y = getBounds( column ).y;
		int width = getBounds( column ).width;
		int height = getBounds( column ).height;
		//System.out.println( "drawing  "+text+" at " );
		//System.out.println( getBounds(  column  ) );		
		getParent().redraw(  x, y, width, height, true  );	
	}
		
	/**
	 * @return the fileInfo, this Object represents
	 */
	public FileInfo getFileInfo() {
		return fileInfo;
	}
	/** (  non-Javadoc  )
	 * @see net.mldonkey.g2gui.view.transferTree.IItemHasMenue#getMenu()
	 */
	public void createMenu( Menu menu ) {
		ResourceBundle bundle = ResourceBundle.getBundle( "g2gui" );
		if ( fileInfo.getState().getState() == EnumFileState.PAUSED ) {
			/*the file is paused, then we don't need to show
			 * the pauseItem, but the resumeItem
			 */
			resumeItem = new MenuItem( menu, SWT.PUSH );
			resumeItem.setText( bundle.getString( "TT_Menu1" ) );
			resumeItem.addSelectionListener( this );
			menu.setDefaultItem( resumeItem );
		} else {
			/*Download is running, we can pause it*/
			pauseItem = new MenuItem( menu, SWT.PUSH );
			pauseItem.setText( bundle.getString( "TT_Menu0" ) );
			pauseItem.addSelectionListener( this );
			menu.setDefaultItem( pauseItem );
		}
		cancelItem = new MenuItem( menu, SWT.PUSH );
		cancelItem.setText( bundle.getString( "TT_Menu2" ) );
		cancelItem.addSelectionListener( this );
		renameItem = new MenuItem( menu, SWT.PUSH );
		renameItem.setText( bundle.getString( "TT_Menu3" ) );
		renameItem.addSelectionListener( this );
		linkItem = new MenuItem( menu, SWT.PUSH );
		linkItem.setText( bundle.getString( "TT_Menu4" ) );
		linkItem.addSelectionListener( this );
		/* Not used by now
			clearItem = new MenuItem(  menu, SWT.PUSH  );
				clearItem.setText(  bundle.getString(  "TT_Menu8"  )  );
				clearItem.addSelectionListener(  this  );
		
				
			fakeItem = new MenuItem(  menu, SWT.PUSH  );
				fakeItem.setText(  bundle.getString(  "TT_Menu5"  )  );
				fakeItem.addSelectionListener(  this  );
		*/
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
		prioMenuItem.setEnabled( true );
	}
	/** ( non-Javadoc )
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected( org.eclipse.swt.events.SelectionEvent )
	 */
	public void widgetSelected( SelectionEvent arg0 ) {
		widgetDefaultSelected( arg0 );
	}
	/** (  non-Javadoc  )
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(  org.eclipse.swt.events.SelectionEvent  )
	 */
	public void widgetDefaultSelected( SelectionEvent arg0 ) {
		ResourceBundle bundle = ResourceBundle.getBundle( "g2gui" );
		if ( arg0.widget instanceof MenuItem ) {
			MenuItem item = ( MenuItem ) arg0.widget;
			if ( item == pauseItem ) {
				fileInfo.setState( EnumFileState.PAUSED );
			}
			if ( item == resumeItem ) {
				fileInfo.setState( EnumFileState.DOWNLOADING );
			}
			if ( item == cancelItem ) {
				MessageBox reallyCancel =
					new MessageBox( 
						getParent().getShell(),
						SWT.YES | SWT.NO | SWT.ICON_QUESTION );
				reallyCancel.setMessage( bundle.getString( "TT_REALLY_CANCEL" ) );
				int answer = reallyCancel.open();
				if ( answer == SWT.YES )
					fileInfo.setState( EnumFileState.CANCELLED );
			}
			if ( item == renameItem ) {
				/*
				 * BIG FAT TODO-ITEM:
				 * create a window, where a filename can be selected from a sortable / sorted
				 * list
				 */
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
Revision 1.20  2003/07/20 12:35:07  dek
saving some CPU time, when only sorting clientItems of expanded DownloadItems

Revision 1.19  2003/07/20 11:47:04  dek
foobar

Revision 1.18  2003/07/20 11:06:36  dek
still don't know, where flickering comes from (tested with CRT)

Revision 1.17  2003/07/20 10:31:21  dek
done some work on flickering & sorting

Revision 1.16  2003/07/18 09:43:15  dek
never use * / ( without space ) in CVS-commit-comments......

Revision 1.15  2003/07/18 09:40:07  dek
finally got rid of the flickering?? dunno ( * searching CRT to test * )

Revision 1.14  2003/07/16 19:39:46  dek
fixed exception when items were expanded after a sort()

Revision 1.13  2003/07/16 18:16:53  dek
another flickering-test

Revision 1.12  2003/07/15 20:52:05  dek
exit-exceptions are gone...

Revision 1.11  2003/07/15 20:13:56  dek
sorting works now, chunk-display is kind of broken, when sorting with expanded tree-items...

Revision 1.10  2003/07/15 18:14:47  dek
Wow, nice piece of work already done, it works, looks nice, but still lots of things to do

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